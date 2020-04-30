package it.dpe.igeriv.security;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.exception.EdicolaSospesaException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.EncryptUtils;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import it.dpe.igeriv.vo.UserVo;

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

/**
 * Classe che serve per validare l'email che è stata inviato all'email del cliente.
 * 
 * @author romanom
 *
 */
@Component("emailValidationFilter")
public class EmailValidationFilter extends GenericFilterBean {
	@Autowired
	private AccountService accountService;
	@Autowired
	private ClientiService<ClienteEdicolaVo> clientiService;
	@Autowired
	private EdicoleService edicoleService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ReflectionSaltSource saltSource;
	@Autowired
	private IGerivLoginSessionConfigurer iGerivLoginSessionConfigurer;
	@Autowired
	private UserDetailBuilderClienteEdicola userDetailBuilderClienteEdicola;
	
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
			String idUtente = params[0];
			Integer codFiegDl = new Integer(params[1]);
			Integer codEdicolaDl = new Integer(params[2]);
			Integer userType = new Integer(params[3]);
			
			String pwd = null;
			String roleName = null;
			boolean isPwdCriptata = false;
			boolean edicolaSospesa = false;
			BaseVo utente = null;
			if (userType.equals(IGerivConstants.TIPO_UTENTE_EDICOLA)) {
				UserVo utenteEdicola = accountService.getEdicolaByCodice(idUtente);
				pwd = utenteEdicola.getPwd();
				roleName = utenteEdicola.getDlGruppoModuliVo().getGruppoModuli().getRoleName();
				isPwdCriptata = utenteEdicola.getPwdCriptata() == null || !utenteEdicola.equals("1");
				edicolaSospesa = utenteEdicola.getAbbinamentoEdicolaDlVo().getDtSospensioneEdicola() == null || utenteEdicola.getAbbinamentoEdicolaDlVo().getDtSospensioneEdicola().after(new Date());
				utente = utenteEdicola;
			} else if (userType.equals(IGerivConstants.TIPO_UTENTE_CLIENTE_EDICOLA)) {
				ClienteEdicolaVo utenteCliente = clientiService.getCienteEdicolaByCodiceLogin(new Long(idUtente));
				pwd = utenteCliente.getPassword(); 
				roleName = utenteCliente.getGruppoModuliVo().getRoleName();
				isPwdCriptata = utenteCliente.getPwdCriptata() == null || !utenteCliente.equals("1");
				utente = utenteCliente;
			}
			Integer tipoUtente = new Integer(userType);
			UserAbbonato user = userDetailBuilderClienteEdicola.buildUserDetails(idUtente, utente);
			List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
			authList.add(new GrantedAuthorityImpl(roleName));
			if (isPwdCriptata) {
				pwd = passwordEncoder.encodePassword(pwd, saltSource.getSalt(user));
			}
			if (pwd.equals(userPwd)) {
				if (tipoUtente.equals(IGerivConstants.TIPO_UTENTE_EDICOLA)) {
					if (edicolaSospesa) {
						AbbinamentoEdicolaDlVo abed = edicoleService.getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(codFiegDl, codEdicolaDl);
						abed.setEmailValido(true);
						edicoleService.saveBaseVo(abed);
					} else {
		        		EdicolaSospesaException ex = new EdicolaSospesaException(IGerivMessageBundle.get("edicola.sospesa.message"));
		        		httpServletRequest.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", ex);
		        	}
				} else if (tipoUtente.equals(IGerivConstants.TIPO_UTENTE_CLIENTE_EDICOLA)) {
					((ClienteEdicolaVo) utente).setEmailValido(true);
					edicoleService.saveBaseVo(utente);
				}
				final Authentication auth = new UsernamePasswordAuthenticationToken(user, pwd, authList);
				if (auth != null && auth.getPrincipal() != null && auth.getPrincipal() instanceof UserAbbonato) {
					UserAbbonato ua = (UserAbbonato) auth.getPrincipal();
					ua.setEmailValido(true);
				}
	        	SecurityContextHolder.getContext().setAuthentication(auth);
				httpServletRequest.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
 	        	((HttpServletResponse)response).sendRedirect(httpServletRequest.getRequestURI().substring(0, httpServletRequest.getRequestURI().lastIndexOf("/")) + "/home.action" + "?checkedEmail=true");
	        	iGerivLoginSessionConfigurer.configureSession(tipoUtente, httpServletRequest, user);
			}
		}
		filterChain.doFilter(request, response);
	}
	
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public ReflectionSaltSource getSaltSource() {
		return saltSource;
	}

	public void setSaltSource(ReflectionSaltSource saltSource) {
		this.saltSource = saltSource;
	}

	public IGerivLoginSessionConfigurer getiGerivLoginSessionConfigurer() {
		return iGerivLoginSessionConfigurer;
	}

	public void setiGerivLoginSessionConfigurer(IGerivLoginSessionConfigurer iGerivLoginSessionConfigurer) {
		this.iGerivLoginSessionConfigurer = iGerivLoginSessionConfigurer;
	}

	public UserDetailBuilderClienteEdicola getUserDetailBuilderClienteEdicola() {
		return userDetailBuilderClienteEdicola;
	}

	public void setUserDetailBuilderClienteEdicola(UserDetailBuilderClienteEdicola userDetailBuilderClienteEdicola) {
		this.userDetailBuilderClienteEdicola = userDetailBuilderClienteEdicola;
	}

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public ClientiService<ClienteEdicolaVo> getClientiService() {
		return clientiService;
	}

	public void setClientiService(ClientiService<ClienteEdicolaVo> clientiService) {
		this.clientiService = clientiService;
	}

	public EdicoleService getEdicoleService() {
		return edicoleService;
	}

	public void setEdicoleService(EdicoleService edicoleService) {
		this.edicoleService = edicoleService;
	}
	
}
