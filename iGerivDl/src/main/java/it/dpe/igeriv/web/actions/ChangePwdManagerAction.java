package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.UtenteAgenziaVo;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
@Scope("prototype")
@Component("changePwdManagerAction")
public class ChangePwdManagerAction extends RestrictedAccessBaseAction implements RequestAware {
	private static final long serialVersionUID = 1L;
	private final AgenzieService agenzieService;
	private final ReflectionSaltSource saltSource;
	private final PasswordEncoder passwordEncoder;
	private final String env;
	private GruppoModuliVo anagraficaMinicardVo;
	private String id;
	private String codDpeWebEdicola;
	private String tipoUtente;
	private String oldPassword;
	private String password;
	private String confermaPassword;
	private String loginType;
	
	public ChangePwdManagerAction() {
		this(null,null,null,null);
	}
	
	@Autowired
	public ChangePwdManagerAction(AgenzieService agenzieService, ReflectionSaltSource saltSource, PasswordEncoder passwordEncoder, @Value("${igeriv.env.deploy.name}") String env) {
		this.agenzieService = agenzieService;
		this.saltSource = saltSource;
		this.passwordEncoder = passwordEncoder;
		this.env = env;
	}
	
	@Override
	public String input() throws Exception {
		return INPUT;
	}
	
	public String changeDl() throws Exception {
		checkUserId();
		UtenteAgenziaVo userVo = agenzieService.getAgenziaByCodiceLogin(new Integer(id));
		UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String pwd = passwordEncoder.encodePassword(password, saltSource.getSalt(ud));
		userVo.setPasswordDl(pwd);
		userVo.setChangePassword(false);
		agenzieService.saveBaseVo(userVo);
		SecurityContextHolder.clearContext();
		return IGerivConstants.REDIRECT;
	}
	
	@Override
	public void validate() {
		super.validate();
		checkUserId();
		if (loginType != null) {
			UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			UtenteAgenziaVo userVo = agenzieService.getAgenziaByCodiceLogin(new Integer(id));
			String oldPwd = passwordEncoder.encodePassword(oldPassword, saltSource.getSalt(ud));
			if (!oldPwd.equals(userVo.getPasswordDl())) {
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
	
	public String getTitle() {
		return super.getTitle() + getText("gp.cambio.password");
	}

}
