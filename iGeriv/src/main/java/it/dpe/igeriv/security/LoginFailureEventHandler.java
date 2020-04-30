package it.dpe.igeriv.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.vo.UserAdminVo;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe che gestisce i tentativi di login falliti.
 * Aggiorna il contatore dei tentativi falliti che poi blocca l'utente.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
public class LoginFailureEventHandler extends SimpleUrlAuthenticationFailureHandler {
	private AccountService accountService;
	public static final short MAX_FAILED_LOGIN_ATTEMPTS = 3;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		super.onAuthenticationFailure(request, response, exception);
		String name = exception.getAuthentication().getName();
		UserAdminVo userVo = accountService.getUserAdminVo(name, true);
		if (userVo != null) {
			Integer failedLoginAttempts = userVo.getFailedLoginAttempts() == null ? 0 : userVo.getFailedLoginAttempts();
			if (failedLoginAttempts > MAX_FAILED_LOGIN_ATTEMPTS) {
				 request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", new LockedException(IGerivMessageBundle.get("msg.security.utente.bloccato")));
			}
			userVo.setIpLoginAddress((request.getHeader("x-forwarded-for") != null && !request.getHeader("x-forwarded-for").equals("")) ? request.getHeader("x-forwarded-for") : request.getRemoteAddr());
			userVo.setFailedLoginAttempts(++failedLoginAttempts);
			accountService.saveBaseVo(userVo);
		}
	}
	
}
