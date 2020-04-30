package it.dpe.igeriv.security;

import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.NumberUtils;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class IGerivDaoAuthenticationProvider  extends DaoAuthenticationProvider {
	
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
        if (userDetails instanceof UserAbbonato && ((UserAbbonato) userDetails).isPwdCriptata()) {
        	bNotAuthenticated = !getPasswordEncoder().isPasswordValid(userDetails.getPassword(), presentedPassword, salt);
        } else {
        	bNotAuthenticated = !userDetails.getPassword().equals(presentedPassword);
        }
        
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
}
