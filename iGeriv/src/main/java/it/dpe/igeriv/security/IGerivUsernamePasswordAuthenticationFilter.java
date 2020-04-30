package it.dpe.igeriv.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vendite.inputmodulelistener.VenditeInputModuleListener;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe che esegue il redirect alla pagina di cambio password quando la password
 * dell'utente è stata rigenerata dal sistema, altrimenti esegue il redirect sulla home page.
 * 
 * @author romanom
 *
 */
@Getter
@Setter
public class IGerivUsernamePasswordAuthenticationFilter extends	UsernamePasswordAuthenticationFilter {
	private SimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler;
	private EdicoleService edicoleService;
	private AccountService accountService;
	private IGerivLoginSessionConfigurer iGerivLoginSessionConfigurer;
	private VenditeInputModuleListener minicardInputModuleListener;
	

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,	HttpServletResponse response) throws AuthenticationException { 
		Authentication attemptAuthentication = super.attemptAuthentication(request, response);
		if (attemptAuthentication != null && attemptAuthentication.isAuthenticated()) {
			UserAbbonato user = (UserAbbonato) attemptAuthentication.getPrincipal();
			iGerivLoginSessionConfigurer.configureSessionEdicola(request, user);
			if (user.isChangePassword()) {
				authenticationSuccessHandler.setDefaultTargetUrl("/changePwd_input.action?id=" + user.getCodUtente());
				authenticationSuccessHandler.setAlwaysUseDefaultTargetUrl(true);
			} else {
				accountService.updateUserAgent(user.getCodUtente(), request.getHeader(IGerivConstants.HEADER_USER_AGENT));
				authenticationSuccessHandler.setDefaultTargetUrl("/home.action?loginExecuted=true");
				authenticationSuccessHandler.setAlwaysUseDefaultTargetUrl(true);
				
//				try {
//
//					authenticationSuccessHandler.onAuthenticationSuccess(request, response, attemptAuthentication);
//					
//					SecurityContextHolder.getContext().setAuthentication(attemptAuthentication);
//					request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
//		        	((HttpServletResponse)response).sendRedirect(request.getContextPath() + "/home.action");
//		        	iGerivLoginSessionConfigurer.configureSessionEdicola(request, user);		
//					
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (ServletException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				//test
//				authenticationSuccessHandler.setDefaultTargetUrl("http://www.igeriv.it/igeriv/home.action?loginExecuted=true");
//				authenticationSuccessHandler.setAlwaysUseDefaultTargetUrl(true);
//				
//				HttpServletRequest httpServletRequest = (HttpServletRequest)request;
//				httpServletRequest.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
// 	        	try {
//					((HttpServletResponse)response).sendRedirect("http://www.igeriv.it/igeriv/home.action?loginExecuted=true");
//						
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
			}
			super.setAuthenticationSuccessHandler(authenticationSuccessHandler);
		}
		return attemptAuthentication;
	}
	
}
