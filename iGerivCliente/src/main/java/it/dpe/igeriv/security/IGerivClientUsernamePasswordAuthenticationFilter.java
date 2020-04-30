package it.dpe.igeriv.security;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.util.IGerivConstants;

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
public class IGerivClientUsernamePasswordAuthenticationFilter extends
		UsernamePasswordAuthenticationFilter {
	private SimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler;
	private AccountService accountService;
	private IGerivLoginSessionConfigurer iGerivLoginSessionConfigurer;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		Authentication attemptAuthentication = super.attemptAuthentication(request, response);
		if (attemptAuthentication != null && attemptAuthentication.isAuthenticated()) {
			UserAbbonato user = (UserAbbonato) attemptAuthentication.getPrincipal();
			iGerivLoginSessionConfigurer.configureSessionClienteEdicola(request, user);
			if (user.isChangePassword()) {
				authenticationSuccessHandler.setDefaultTargetUrl("/changePwd_input.action?id=" + user.getCodUtente());
				authenticationSuccessHandler.setAlwaysUseDefaultTargetUrl(true);
			} else {
				accountService.updateClientUserAgent(new Long(user.getCodUtente()), user.getCodDpeWebEdicola(), request.getHeader(IGerivConstants.HEADER_USER_AGENT));
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

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
}
