package it.dpe.igeriv.web.actions;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.service.mail.MailingListService;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action che esegue il reset della password.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("resetPwdManagerAction")
public class ResetPwdManagerAction extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;
	private final AccountService accountService;
	private final MailingListService mailingListService;
	private final ReflectionSaltSource saltSource;
	private final PasswordEncoder passwordEncoder;
	@Value("${igeriv.env.deploy.name}")
	private final String env;
	private GruppoModuliVo anagraficaMinicardVo;
	private String codEdicola;
	private String emailEdicola;
	private String oldPassword;
	private String password;
	private String confermaPassword;
	
	public ResetPwdManagerAction() {
		this.accountService = null;
		this.mailingListService = null;
		this.saltSource = null;
		this.passwordEncoder = null;
		this.env = null;
	}
	
	@Autowired
	public ResetPwdManagerAction(
			AccountService accountService,
			MailingListService mailingListService,
			ReflectionSaltSource saltSource,
			PasswordEncoder passwordEncoder,
			@Value("${igeriv.env.deploy.name}") String env) {
		this.accountService = accountService;
		this.mailingListService = mailingListService;
		this.saltSource = saltSource;
		this.passwordEncoder = passwordEncoder;
		this.env = env;
	}
	
	@Override
	public String input() throws Exception {
		return INPUT;
	}
	
	@Override
	public void validate() {
		
	}
	
	public String resetPwdEdicola() throws Exception {
		UserVo user = null;
		String email = null;
		try {
			user = accountService.getEdicolaByCodice(codEdicola);			
			email = user.getEmail();
			if (email == null || emailEdicola== null || email.equals("") || emailEdicola.equals("")) {
				throw new IGerivBusinessException(getText("msg.email.email.non.esiste"));
			}
			if (!emailEdicola.equalsIgnoreCase(email)) {
				throw new IGerivBusinessException(getText("msg.dati.non.corrispondono"));
			}
		} catch (IGerivBusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new IGerivBusinessException(getText("msg.email.utente.non.esiste"));
		}
		String newPassword = StringUtility.getRandomString(14);
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		authList.add(new GrantedAuthorityImpl(IGerivConstants.ROLE_IGERIV_BASE));
		UserAbbonato ua = new UserAbbonato(codEdicola, newPassword, true, true, true, true, authList);
		UserVo utente = accountService.getEdicolaByCodice(codEdicola);
		ua.setCodFiegDl(utente.getAbbinamentoEdicolaDlVo().getAnagraficaAgenziaVo().getCodFiegDl());
		ua.setCodEdicolaDl(utente.getAbbinamentoEdicolaDlVo().getCodEdicolaDl());
		String pwd = passwordEncoder.encodePassword(newPassword, saltSource.getSalt(ua));
		user.setPwd(pwd);
		user.setChangePassword(1);
		user.setPwdCriptata(1);
		accountService.saveBaseVo(user);
		String message = MessageFormat.format(getText("msg.email.reset.pwd"), newPassword);
		mailingListService.sendEmail(email, getText("msg.subject.pwd.reset"), message);
		return IGerivConstants.REDIRECT;
	}
	
	@Override
	public String getTitle() {
		return super.getTitle() + getText("password.dimenticata");
	}
	
}
