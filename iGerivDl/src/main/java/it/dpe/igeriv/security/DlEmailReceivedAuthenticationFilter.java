package it.dpe.igeriv.security;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.util.EncryptUtils;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.EmailRivenditaVo;
import it.dpe.igeriv.vo.UtenteAgenziaVo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.google.common.base.Strings;

/**
 * Classe che serve per validare l'email che è stata inviato all'email del cliente.
 * 
 * @author romanom
 *
 */
@Component("dlEmailReceivedAuthenticationFilter")
public class DlEmailReceivedAuthenticationFilter extends GenericFilterBean {
	private final AgenzieService agenzieService;
	private final AccountService accountService;
	private final EdicoleService edicoleService;
	private final IGerivLoginSessionConfigurer iGerivLoginSessionConfigurer;
	
	@Autowired
	public DlEmailReceivedAuthenticationFilter(AgenzieService agenzieService, AccountService accountService, EdicoleService edicoleService, IGerivLoginSessionConfigurer iGerivLoginSessionConfigurer) {
		this.agenzieService = agenzieService;
		this.accountService = accountService;
		this.edicoleService = edicoleService;
		this.iGerivLoginSessionConfigurer = iGerivLoginSessionConfigurer;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String parameter = request.getParameter("hash");
		String userPwd = request.getParameter("hash1");
		if (!Strings.isNullOrEmpty(parameter) && !Strings.isNullOrEmpty(userPwd)) {
			SecurityContextHolder.getContext().setAuthentication(null);
			HttpServletRequest httpServletRequest = (HttpServletRequest)request;
			String[] params = null;
			try {
				params = EncryptUtils.decrypt(EncryptUtils.getDecrypter(IGerivConstants.ENCRYPTION_TOKEN), parameter).split("\\|");
			} catch (Exception e) {
				throw new ServletException(e);
			} 
			Integer codFiegDl = new Integer(params[0]);
			Integer codEdicolaDl = new Integer(params[1]);
			UtenteAgenziaVo agenzia = agenzieService.getAgenziaByCodiceLogin(codFiegDl);
			String roleName = agenzia.getDlGruppoModuliVo().getGruppoModuli().getRoleName();
			List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
			authList.add(new GrantedAuthorityImpl(roleName));
			if (agenzia.getPasswordDl().equals(userPwd)) {
				UserAbbonato user = accountService.buildDlUserDetails(codFiegDl.toString(), agenzia);
				iGerivLoginSessionConfigurer.configureSessionEdicola(httpServletRequest, user);
				final Authentication auth = new UsernamePasswordAuthenticationToken(user, agenzia.getPasswordDl(), authList);
				if (auth != null && auth.getPrincipal() != null && auth.getPrincipal() instanceof UserAbbonato) {
					UserAbbonato ua = (UserAbbonato) auth.getPrincipal();
					ua.setEmailValido(true);
				}
				if (!Strings.isNullOrEmpty(request.getParameter("id")) && NumberUtils.isNumber(request.getParameter("id").trim())) {
					Integer idEmailRivendita = new Integer(request.getParameter("id").trim());
					EmailRivenditaVo vo = edicoleService.getEmailRivenditaVo(idEmailRivendita);
					if (vo != null) {
						vo.setLetto(true);
						edicoleService.saveBaseVo(vo);
					}
				}
	        	SecurityContextHolder.getContext().setAuthentication(auth);
				httpServletRequest.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
 	        	String url = httpServletRequest.getRequestURI().substring(0, httpServletRequest.getRequestURI().lastIndexOf("/")) + "/confermeLetturaMsgEdicole_execute.action?codEdicolaDl=" + codEdicolaDl;
				((HttpServletResponse)response).sendRedirect(url);
			}
		}
		filterChain.doFilter(request, response);
	}
	
}
