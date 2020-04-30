package it.dpe.igeriv.security.openid;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.openid.OpenIDAuthenticationFilter;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.security.IGerivLoginSessionConfigurer;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.IGerivConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IGerivOpenIDAuthenticationFilter extends OpenIDAuthenticationFilter {
	private AccountService accountService;
	private IGerivLoginSessionConfigurer iGerivLoginSessionConfigurer;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
		Authentication attemptAuthentication = super.attemptAuthentication(request, response);
		if (attemptAuthentication != null && attemptAuthentication.isAuthenticated()) {
			UserAbbonato user = (UserAbbonato) attemptAuthentication.getPrincipal();
			iGerivLoginSessionConfigurer.configureSessionEdicola(request, user);
			accountService.updateUserAgent(user.getCodUtente(), request.getHeader(IGerivConstants.HEADER_USER_AGENT));
		}
		return attemptAuthentication;
	}
	
}
