package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.UserAdminVo;

import java.util.Map;

import org.apache.struts2.interceptor.RequestAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Classe action per il cambio della password.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Scope("prototype")
@Component("changePwdManagerAction")
public class ChangePwdManagerAction extends RestrictedAccessBaseAction implements RequestAware {
	private static final long serialVersionUID = 1L;
	@Autowired
	private AccountService accountService;
	private GruppoModuliVo anagraficaMinicardVo;
	@Autowired
	private ReflectionSaltSource saltSource;
	@Autowired
	private PasswordEncoder passwordEncoder;
	private String id;
	private String codDpeWebEdicola;
	private String tipoUtente;
	private String oldPassword;
	private String password;
	private String confermaPassword;
	@SuppressWarnings("unused")
	private Map<String, Object> request;
	private String loginType;
	@Value("${igeriv.env.deploy.name}")
	private String env;
	
	@Override
	public String input() throws Exception {
		return INPUT;
	}
	
	public String changeAdm() throws Exception {
		checkUserId();
		UserAdminVo userVo = accountService.getUserAdminVo(id, false);
		UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String pwd = passwordEncoder.encodePassword(password, saltSource.getSalt(ud));
		userVo.setPwd(pwd);
		userVo.setChangePassword(0);
		accountService.saveBaseVo(userVo);
		SecurityContextHolder.clearContext();
		return IGerivConstants.REDIRECT;
	}
	
	@Override
	public void validate() {
		super.validate();
		checkUserId();
		if (loginType != null) {
			UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			UserAdminVo userVo = accountService.getUserAdminVo(id, false);
			String oldPwd = passwordEncoder.encodePassword(oldPassword, saltSource.getSalt(ud));
			if (!oldPwd.equals(userVo.getPwd())) {
				addFieldError("oldPassword", getText("msg.pwd.invalida"));
			}
		}
		if (password != null && confermaPassword != null) {
			if (!password.equals(confermaPassword)) {
				addFieldError("confermaPassword", getText("dpe.validation.msg.password.non.coincide"));
			}
		}
	}
	
	private void checkUserId() {
		if ((id == null || id.equals("")) && sessionMap.get(IGerivConstants.USER_ID) != null) {
			id = sessionMap.get(IGerivConstants.USER_ID).toString();
		} else if (id == null || id.equals("")) {
			throw new AccessDeniedException(null);
		}
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCodDpeWebEdicola() {
		return codDpeWebEdicola;
	}

	public void setCodDpeWebEdicola(String codDpeWebEdicola) {
		this.codDpeWebEdicola = codDpeWebEdicola;
	}
	
	public String getTipoUtente() {
		return tipoUtente;
	}

	public void setTipoUtente(String tipoUtente) {
		this.tipoUtente = tipoUtente;
	}

	public String getTitle() {
		return super.getTitle() + getText("gp.cambio.password");
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfermaPassword() {
		return confermaPassword;
	}

	public void setConfermaPassword(String confermaPassword) {
		this.confermaPassword = confermaPassword;
	}
	
	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public GruppoModuliVo getAnagraficaMinicardVo() {
		return anagraficaMinicardVo;
	}

	public void setAnagraficaMinicardVo(GruppoModuliVo anagraficaMinicardVo) {
		this.anagraficaMinicardVo = anagraficaMinicardVo;
	}

	public ReflectionSaltSource getSaltSource() {
		return saltSource;
	}

	public void setSaltSource(ReflectionSaltSource saltSource) {
		this.saltSource = saltSource;
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void setRequest(Map<String, Object> arg0) {
		this.request = arg0;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}
	
}
