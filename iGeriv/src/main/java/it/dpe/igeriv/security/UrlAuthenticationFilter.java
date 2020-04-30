package it.dpe.igeriv.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.exception.EdicolaSospesaException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.UserVo;

/**
 * Classe che serve per autenticare gli utenti attrverso la url, bypassando la login form.
 * Utente e Pwd sono criptate e passate attraverso la url.
 * 
 * @author romanom
 */
@Component("urlAuthenticationFilter")
public class UrlAuthenticationFilter extends GenericFilterBean {
	private final PasswordEncoder passwordEncoder;
	private final ReflectionSaltSource saltSource;
	private final AccountService accountService;
	private final IGerivLoginSessionConfigurer iGerivLoginSessionConfigurer;
	
	@Autowired
	UrlAuthenticationFilter(PasswordEncoder passwordEncoder, ReflectionSaltSource saltSource, AccountService accountService, IGerivLoginSessionConfigurer iGerivLoginSessionConfigurer) {
		this.passwordEncoder = passwordEncoder;
		this.saltSource = saltSource;
		this.accountService = accountService;
		this.iGerivLoginSessionConfigurer = iGerivLoginSessionConfigurer;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if (!Strings.isNullOrEmpty(request.getParameter("p1")) && !Strings.isNullOrEmpty(request.getParameter("p2"))) {
			SecurityContextHolder.getContext().setAuthentication(null);
			HttpServletRequest httpServletRequest = (HttpServletRequest)request;
			String paramUser = new String(Base64.decodeBase64(request.getParameter("p1")));
	        String password = request.getParameter("p2");
	        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
			authList.add(new GrantedAuthorityImpl(IGerivConstants.ROLE_IGERIV_BASE_ADMIN));

			UserVo utente = accountService.getEdicolaByCodice(paramUser);
	        if (utente != null) {
		        String pwd = utente.getPwd();
		        if (!utente.getPwdCriptata().equals(1)) {
			        UserAbbonato user = new UserAbbonato(paramUser, pwd, true, true, true, true, authList);
			        user.setId(new Integer(paramUser));
			        user.setCodUtente(paramUser);
			        pwd = passwordEncoder.encodePassword(pwd, saltSource.getSalt(user));
		        }
		        if (password.equals(pwd)) {
		        	UserAbbonato user = accountService.buildUserDetails(paramUser, utente);
		        	if (utente.getAbbinamentoEdicolaDlVo().getDtSospensioneEdicola() == null || utente.getAbbinamentoEdicolaDlVo().getDtSospensioneEdicola().after(new Date())) {
			        	final Authentication auth = new UsernamePasswordAuthenticationToken(user, password, authList);
			        	SecurityContextHolder.getContext().setAuthentication(auth);
						httpServletRequest.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
			        	((HttpServletResponse)response).sendRedirect(httpServletRequest.getContextPath() + "/home.action");
			        	iGerivLoginSessionConfigurer.configureSessionEdicola(httpServletRequest, user);
		        	} else {
		        		EdicolaSospesaException ex = new EdicolaSospesaException(IGerivMessageBundle.get("edicola.sospesa.message"));
		        		httpServletRequest.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", ex);
		        	}
				} 
	        }
		}
		filterChain.doFilter(request, response);
	}
	
}
