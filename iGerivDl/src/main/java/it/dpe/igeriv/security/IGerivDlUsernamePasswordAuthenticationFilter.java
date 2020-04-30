package it.dpe.igeriv.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Classe che esegue il redirect alla pagina di cambio password quando la password
 * dell'utente è stata rigenerata dal sistema, altrimenti esegue il redirect sulla home page.
 * 
 * @author romanom
 *
 */
public class IGerivDlUsernamePasswordAuthenticationFilter extends
		UsernamePasswordAuthenticationFilter {
	private SimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler;
	private IGerivLoginSessionConfigurer iGerivLoginSessionConfigurer;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		Authentication attemptAuthentication = super.attemptAuthentication(request, response);
		if (attemptAuthentication.isAuthenticated()) {
			UserAbbonato user = (UserAbbonato) attemptAuthentication.getPrincipal();
			iGerivLoginSessionConfigurer.configureSessionEdicola(request, user);
			if (user.isChangePassword()) {
				authenticationSuccessHandler.setDefaultTargetUrl("/changePwd_input.action?id=" + user.getCodUtente());
				authenticationSuccessHandler.setAlwaysUseDefaultTargetUrl(true);
			} else {
				authenticationSuccessHandler.setDefaultTargetUrl("/home.action");
				authenticationSuccessHandler.setAlwaysUseDefaultTargetUrl(true);
			}
			super.setAuthenticationSuccessHandler(authenticationSuccessHandler);
		}
		return attemptAuthentication;
	}
	
	public SimpleUrlAuthenticationSuccessHandler getAuthenticationSuccessHandler() {
		return authenticationSuccessHandler;
	}

	public void setAuthenticationSuccessHandler(
			SimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler) {
		this.authenticationSuccessHandler = authenticationSuccessHandler;
	}

	public IGerivLoginSessionConfigurer getiGerivLoginSessionConfigurer() {
		return iGerivLoginSessionConfigurer;
	}

	public void setiGerivLoginSessionConfigurer(IGerivLoginSessionConfigurer iGerivLoginSessionConfigurer) {
		this.iGerivLoginSessionConfigurer = iGerivLoginSessionConfigurer;
	}
	
}
