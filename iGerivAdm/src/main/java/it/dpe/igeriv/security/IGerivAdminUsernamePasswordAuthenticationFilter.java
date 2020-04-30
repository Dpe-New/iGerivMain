package it.dpe.igeriv.security;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.menu.MenuService;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.UserAdminVo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
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
public class IGerivAdminUsernamePasswordAuthenticationFilter extends
		UsernamePasswordAuthenticationFilter {
	private SimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler;
	private AccountService accountService;
	private AgenzieService agenzieService;
	private MenuService menuService;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		Authentication attemptAuthentication = super.attemptAuthentication(request, response);
		UserAdmin user = (UserAdmin) attemptAuthentication.getPrincipal();
		if (attemptAuthentication != null && attemptAuthentication.isAuthenticated()) {
			UserAdminVo utente = accountService.getUserAdminVo(user.getId().toString(), false);
			utente.setFailedLoginAttempts(0);
			utente.setIpLoginAddress((request.getHeader("x-forwarded-for") != null && !request.getHeader("x-forwarded-for").equals("")) ? request.getHeader("x-forwarded-for") : request.getRemoteAddr());
			accountService.saveBaseVo(utente);
			request.getSession().setAttribute(IGerivConstants.USER_ID, user.getCodUtente());
			request.getSession().setAttribute(IGerivConstants.SESSION_VAR_COD_FIEG_DL, 0);
			request.getSession().setAttribute(IGerivConstants.SESSION_VAR_DL, buildAgenzieKeyValueList());
			request.getSession().setAttribute(IGerivConstants.SESSION_VAR_TUTTI_DL, buildTutteAgenzieKeyValueList());
			request.getSession().setAttribute(IGerivConstants.SESSION_VAR_PROFILI_EDICOLA, menuService.getGruppoModuli());
			request.getSession().setAttribute(IGerivConstants.SESSION_VAR_TIMEOUT_MINUTES, request.getSession().getMaxInactiveInterval() / 60);
			request.getSession().setAttribute(IGerivConstants.SESSION_VAR_NETWORK_DETECTION_INTERVAL_MILLS, "");
			request.getSession().setAttribute(IGerivConstants.SESSION_VAR_HIGH_PRIORITY_MESSAGES_CHECK_INTERVAL_MILLS, "");
			request.getSession().setAttribute(IGerivConstants.SESSION_VAR_SAVE_BOLLE_INTERVAL_MILLS, "");
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
	
	public List<KeyValueDto> buildAgenzieKeyValueList() {
		List<KeyValueDto> listKeyValues = new ArrayList<KeyValueDto>();
		List<AnagraficaAgenziaVo> agenzie = agenzieService.getAgenzieConFatturazione();
		for (AnagraficaAgenziaVo vo : agenzie) {
			KeyValueDto dto1 = new KeyValueDto();
			dto1.setKey(vo.getCodFiegDl().toString());
			dto1.setValue(vo.getRagioneSocialeDlPrimaRiga());
			listKeyValues.add(dto1);
		}
		return listKeyValues;
	}
	
	public List<KeyValueDto> buildTutteAgenzieKeyValueList() {
		List<KeyValueDto> listKeyValues = new ArrayList<KeyValueDto>();
		List<AnagraficaAgenziaVo> agenzie = agenzieService.getAgenzie();
		for (AnagraficaAgenziaVo vo : agenzie) {
			KeyValueDto dto1 = new KeyValueDto();
			dto1.setKey(vo.getCodFiegDl().toString());
			dto1.setValue(vo.getRagioneSocialeDlPrimaRiga());
			listKeyValues.add(dto1);
		}
		return listKeyValues;
	}
	
	public SimpleUrlAuthenticationSuccessHandler getAuthenticationSuccessHandler() {
		return authenticationSuccessHandler;
	}

	public void setAuthenticationSuccessHandler(
			SimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler) {
		this.authenticationSuccessHandler = authenticationSuccessHandler;
	}

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public AgenzieService getAgenzieService() {
		return agenzieService;
	}

	public void setAgenzieService(AgenzieService agenzieService) {
		this.agenzieService = agenzieService;
	}

	public MenuService getMenuService() {
		return menuService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		super.unsuccessfulAuthentication(request, response, failed);
	}
	
}
