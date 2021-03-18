package it.dpe.igeriv.web.actions;

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

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import it.dpe.igeriv.vo.UserVo;
import lombok.Getter;
import lombok.Setter;

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
	private final ClientiService<ClienteEdicolaVo> clientiService;
	private final AccountService accountService;
	private final ReflectionSaltSource saltSource;
	private final PasswordEncoder passwordEncoder;
	private final String env;
	private String id;
	private String codDpeWebEdicola;
	private String tipoUtente;
	private String oldPassword;
	private String password;
	private String confermaPassword;
	private Map<String, Object> request;
	private String loginType;
	
	public ChangePwdManagerAction() {
		this.agenzieService = null;
		this.clientiService = null;
		this.accountService = null;
		this.saltSource = null;
		this.passwordEncoder = null;
		this.env = null;
	}
	
	@Autowired
	public ChangePwdManagerAction(
		AgenzieService agenzieService,
		ClientiService<ClienteEdicolaVo> clientiService,
		AccountService accountService,
		ReflectionSaltSource saltSource,
		PasswordEncoder passwordEncoder,
		@Value("${igeriv.env.deploy.name}") String env) {
		this.agenzieService = agenzieService;
		this.clientiService = clientiService;
		this.accountService = accountService;
		this.saltSource = saltSource;
		this.passwordEncoder = passwordEncoder;
		this.env = env;
	}
	
	@Override
	public String input() {
		return INPUT;
	}
	
	public String change() {
		checkUserId();
		UserVo userVo = accountService.getEdicolaByCodice(id);
		UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String pwd = passwordEncoder.encodePassword(password, saltSource.getSalt(ud));
		userVo.setPwd(pwd);
		userVo.setChangePassword(0);
		userVo.setPwdCriptata(1);
		accountService.saveBaseVo(userVo);

		/*if (getAuthUser() != null && getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv()) {
			Integer[] arrId = getAuthUser().getArrId();
			if (arrId.length > 1) {
				for (Integer idCorr: arrId) {
					if (!idCorr.toString().equals(id)) {
						UserVo userCorrVo = accountService.getEdicolaByCodice(idCorr.toString());
						if (userCorrVo != null) {
							userCorrVo.setPwd(pwd);
							userCorrVo.setChangePassword(0);
							userCorrVo.setPwdCriptata(1);
							accountService.saveBaseVo(userCorrVo);
						}
					}
				}
			}
		}*/

		SecurityContextHolder.clearContext();
		return IGerivConstants.REDIRECT;
	}

	@Override
	public void validate() {
		checkUserId();
		UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (ud == null) {
			addActionError(getText("msg.utente.non.riconosciuto"));
		} else if (Strings.isNullOrEmpty(oldPassword)) {
			addFieldError("oldPassword", getText("plg.inserisci.pwd.corr"));
		} else if (Strings.isNullOrEmpty(password)) {
			addFieldError("password", getText("plg.inserisci.new.pwd"));
		} else if (Strings.isNullOrEmpty(confermaPassword)) {
			addFieldError("confermaPassword", getText("plg.inserisci.rep.new.pwd"));
		} else {
			UserVo userVo = accountService.getEdicolaByCodice(id);
			String oldPwd = passwordEncoder.encodePassword(oldPassword, saltSource.getSalt(ud));
			if (!oldPwd.equals(userVo.getPwd())) {
				addFieldError("oldPassword", getText("msg.pwd.invalida"));
			} else if (!password.equals(confermaPassword)) {
				addFieldError("confermaPassword", getText("dpe.validation.msg.password.non.coincide"));
			}
		}
	}
	
	private void checkUserId() {
		if (Strings.isNullOrEmpty(id) && sessionMap.get(IGerivConstants.USER_ID) != null) {
			id = sessionMap.get(IGerivConstants.USER_ID).toString();
		} else if (Strings.isNullOrEmpty(id)) {
			throw new AccessDeniedException(null);
		}
	}
	
	public String getTitle() {
		return super.getTitle() + getText("gp.cambio.password");
	}

	@Override
	public void setRequest(Map<String, Object> arg0) {
		this.request = arg0;
	}

}
