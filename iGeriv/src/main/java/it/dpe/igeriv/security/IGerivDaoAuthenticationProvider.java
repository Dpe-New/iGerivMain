package it.dpe.igeriv.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import com.google.common.base.Strings;

import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.NumberUtils;

public class IGerivDaoAuthenticationProvider extends DaoAuthenticationProvider{
	
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		Object salt = null;

        if (getSaltSource() != null) {
            salt = getSaltSource().getSalt(userDetails);
        }

        if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }

        String presentedPassword = authentication.getCredentials().toString();
        
        boolean bNotAuthenticated = false;
        // AUTENTICAZIONE EDICOLA
        
        if (userDetails instanceof UserAbbonato && ((UserAbbonato) userDetails).isPwdCriptata()) {
        	bNotAuthenticated = !getPasswordEncoder().isPasswordValid(userDetails.getPassword(), presentedPassword, salt);
        } else {
        	bNotAuthenticated = !userDetails.getPassword().equals(presentedPassword);
        }
        
        
        //VITTORIO 29/06/20 TOGLIERE !!!!!!!!
        //bNotAuthenticated = false;
        
        
        if (((UserAbbonato) userDetails).isPwdCriptata() && bNotAuthenticated) {
        	salt = "" + ((((UserAbbonato) userDetails).getId() + NumberUtils.getOnlyNumerics(((UserAbbonato) userDetails).getCodUtente())) * IGerivConstants.ENCODE_FACTOR);
        	bNotAuthenticated = !getPasswordEncoder().isPasswordValid(userDetails.getPassword(), presentedPassword, salt);
        }
		if (bNotAuthenticated) {
            logger.debug("Authentication failed: password does not match stored value");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
	 * 
	 * Per bypassare il login in ambiente di test
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		boolean igerivModeTest = !Strings.isNullOrEmpty(System.getenv("igeriv.mode.test")) ? Boolean.parseBoolean(System.getenv("igeriv.mode.test").toString()) : false;
		if (igerivModeTest) {
	        String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
	
	        boolean cacheWasUsed = true;
	        UserDetails user = getUserCache().getUserFromCache(username);
	
	        if (user == null) {
	            cacheWasUsed = false;
	
	            try {
	                user = retrieveUser(username, (UsernamePasswordAuthenticationToken) authentication);
	            } catch (UsernameNotFoundException notFound) {
	                logger.debug("User '" + username + "' not found");
	
	                if (hideUserNotFoundExceptions) {
	                    throw new BadCredentialsException(messages.getMessage(
	                            "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
	                } else {
	                    throw notFound;
	                }
	            }
	
	            Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");
	        }
	
	        if (!cacheWasUsed) {
	            getUserCache().putUserInCache(user);
	        }
	
	        Object principalToReturn = user;

	        return createSuccessAuthentication(principalToReturn, authentication, user);
		} else {
			return super.authenticate(authentication);
		}
	}
}
