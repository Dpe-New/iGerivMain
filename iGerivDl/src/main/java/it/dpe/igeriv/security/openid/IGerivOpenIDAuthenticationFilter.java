package it.dpe.igeriv.security.openid;

import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.openid.OpenIDAuthenticationFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class IGerivOpenIDAuthenticationFilter extends OpenIDAuthenticationFilter {
	private SimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
		Authentication attemptAuthentication = super.attemptAuthentication(request, response);
		if (attemptAuthentication != null && attemptAuthentication.isAuthenticated()) {
			UserAbbonato user = (UserAbbonato) attemptAuthentication.getPrincipal();
			request.getSession().setAttribute(IGerivConstants.USER_ID, user.getCodUtente());
			request.getSession().setAttribute(IGerivConstants.SESSION_VAR_COD_FIEG_DL, user.getCodFiegDl());
			request.getSession().setAttribute("statiTipoMessaggi", buildStatiTipoMessaggi());
			request.getSession().setAttribute(IGerivConstants.SESSION_VAR_REQUEST_IP_ADDRESS, (request.getHeader("x-forwarded-for") != null && !request.getHeader("x-forwarded-for").equals("")) ? request.getHeader("x-forwarded-for") : request.getRemoteAddr());
			request.getSession().setAttribute(IGerivConstants.SESSION_VAR_TIMEOUT_MINUTES, request.getSession().getMaxInactiveInterval() / 60);
			request.getSession().setAttribute(IGerivConstants.SESSION_VAR_NETWORK_DETECTION_INTERVAL_MILLS, "");
			request.getSession().setAttribute(IGerivConstants.SESSION_VAR_SAVE_BOLLE_INTERVAL_MILLS, "");
			request.getSession().setAttribute(IGerivConstants.SESSION_VAR_HIGH_PRIORITY_MESSAGES_CHECK_INTERVAL_MILLS, "");
			request.getSession().setAttribute(IGerivConstants.SESSION_VAR_BROWSER_NAME, StringUtility.getBrowserName(request.getHeader(IGerivConstants.HEADER_USER_AGENT)));
			request.getSession().setAttribute("abilitataCorrezioneBarcode", false);
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
	
	/**
	 * @return
	 */
	public List<KeyValueDto> buildStatiTipoMessaggi() {
		List<KeyValueDto> stati = new ArrayList<KeyValueDto>();
		KeyValueDto dto1 = new KeyValueDto();
		dto1.setKey("0");
		dto1.setValue(IGerivMessageBundle.get("igeriv.normale"));
		KeyValueDto dto2 = new KeyValueDto();
		dto2.setKey("1");
		dto2.setValue(IGerivMessageBundle.get("igeriv.allerta"));
		KeyValueDto dto3 = new KeyValueDto();
		dto3.setKey("2");
		dto3.setValue(IGerivMessageBundle.get("igeriv.emergenza"));
		stati.add(dto1);
		stati.add(dto2);
		stati.add(dto3);
		return stati;
	}

	public SimpleUrlAuthenticationSuccessHandler getAuthenticationSuccessHandler() {
		return authenticationSuccessHandler;
	}

	public void setAuthenticationSuccessHandler(SimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler) {
		this.authenticationSuccessHandler = authenticationSuccessHandler;
	}
	
}
