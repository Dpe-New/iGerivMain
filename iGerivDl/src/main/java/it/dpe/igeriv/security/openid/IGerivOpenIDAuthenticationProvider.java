package it.dpe.igeriv.security.openid;

import java.util.List;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.openid.AuthenticationCancelledException;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationProvider;
import org.springframework.security.openid.OpenIDAuthenticationStatus;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.util.Assert;

import com.google.common.base.Strings;

public class IGerivOpenIDAuthenticationProvider extends OpenIDAuthenticationProvider {
	
	private UserDetailsService userDetailsService;
	
	public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.userDetailsService, "The userDetailsService must be set");
    }
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (!supports(authentication.getClass())) {
            return null;
        }
        if (authentication instanceof OpenIDAuthenticationToken) {
            OpenIDAuthenticationToken response = (OpenIDAuthenticationToken) authentication;
            OpenIDAuthenticationStatus status = response.getStatus();
            String email = null;
	        List<OpenIDAttribute> attributes = response.getAttributes();
	        for (OpenIDAttribute attribute : attributes) {
	            if (attribute.getName().equals("email")) {
	                email = attribute.getValues().get(0);
	                break;
	            }
	        }
	        String identityUrl = response.getIdentityUrl();
	        if (email == null && !Strings.isNullOrEmpty(identityUrl)) {
	        	if (identityUrl.contains("openid.aol.com")) {
	        		email = identityUrl.substring(identityUrl.lastIndexOf("/") + 1) + "@aol.com";
	        	} else if (identityUrl.contains("http://")) {
	        		email = identityUrl.substring(identityUrl.indexOf("http://") + 7);
	        	} else {
	        		email = identityUrl;
	        	}
	        	if (email.endsWith("/")) {
	        		email = email.substring(0, email.length() - 1);
	        	}
	        }
            if (status == OpenIDAuthenticationStatus.SUCCESS && email != null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                return createSuccessfulAuthentication(userDetails, response);
            } else if (status == OpenIDAuthenticationStatus.CANCELLED) {
                throw new AuthenticationCancelledException("Log in cancelled");
            } else if (status == OpenIDAuthenticationStatus.ERROR) {
                throw new AuthenticationServiceException("Error message from server: " + response.getMessage());
            } else if (status == OpenIDAuthenticationStatus.FAILURE) {
                throw new BadCredentialsException("Log in failed - identity could not be verified");
            } else if (status == OpenIDAuthenticationStatus.SETUP_NEEDED) {
                throw new AuthenticationServiceException(
                        "The server responded setup was needed, which shouldn't happen");
            } else {
                throw new AuthenticationServiceException("Unrecognized return value " + status.toString());
            }
        }
        return null;
	}
	
	/**
     * Handles the creation of the final <tt>Authentication</tt> object which will be returned by the provider.
     * <p>
     * The default implementation just creates a new OpenIDAuthenticationToken from the original, but with the
     * UserDetails as the principal and including the authorities loaded by the UserDetailsService.
     *
     * @param userDetails the loaded UserDetails object
     * @param auth the token passed to the authenticate method, containing
     * @return the token which will represent the authenticated user.
     */
    protected Authentication createSuccessfulAuthentication(UserDetails userDetails, OpenIDAuthenticationToken auth) {
        return userDetails != null ? new OpenIDAuthenticationToken(userDetails, userDetails.getAuthorities(),
                auth.getIdentityUrl(), auth.getAttributes()) : null;
    }

    /**
     * Used to load the authorities for the authenticated OpenID user.
     */
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.authentication.AuthenticationProvider#supports(java.lang.Class)
     */
    public boolean supports(Class<? extends Object> authentication) {
        return OpenIDAuthenticationToken.class.isAssignableFrom(authentication);
    }
	
}
