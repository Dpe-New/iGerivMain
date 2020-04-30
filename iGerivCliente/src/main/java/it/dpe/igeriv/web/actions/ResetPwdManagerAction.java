package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.service.mail.MailingListService;

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

/**
 * Classe action che esegue il reset della password.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Scope("prototype")
@Component("resetPwdManagerAction")
public class ResetPwdManagerAction extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;
	@Autowired
	private ClientiService<ClienteEdicolaVo> clientiService;
	@Autowired
	private MailingListService mailingListService;
	private GruppoModuliVo anagraficaMinicardVo;
	@Autowired
	private ReflectionSaltSource saltSource;
	@Autowired
	private PasswordEncoder passwordEncoder;
	private String codEdicola;
	private String emailEdicola;
	private String oldPassword;
	private String password;
	private String confermaPassword;
	@Value("${igeriv.env.deploy.name}")
	private String env;
	
	@Override
	public String input() throws Exception {
		return INPUT;
	}
	
	@Override
	public void validate() {
		
	}
	
	public String resetPwdCliente() throws Exception {
		ClienteEdicolaVo user = null;
		String email = null;
		try {
			user = clientiService.getCienteEdicolaByCodice(new Long(codEdicola));
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
		authList.add(new GrantedAuthorityImpl(IGerivConstants.ROLE_IGERIV_CLIENTE_EDICOLA));
		UserAbbonato ua = new UserAbbonato(codEdicola, newPassword, true, true, true, true, authList);
		ua.setTipoUtente(IGerivConstants.TIPO_UTENTE_CLIENTE_EDICOLA);
		ua.setId(new Integer(codEdicola));
		ua.setCodUtente(codEdicola);
		String pwd = passwordEncoder.encodePassword(newPassword, saltSource.getSalt(ua));
		user.setPassword(pwd);
		user.setChangePassword(1);
		user.setPwdCriptata(1);
		clientiService.saveBaseVo(user);
		String message = MessageFormat.format(getText("msg.email.reset.pwd"), newPassword);
		mailingListService.sendEmail(email, getText("msg.subject.pwd.reset"), message);
		return IGerivConstants.REDIRECT;
	}
	
	public String getCodEdicola() {
		return codEdicola;
	}

	public void setCodEdicola(String codEdicola) {
		this.codEdicola = codEdicola;
	}
	
	public String getEmailEdicola() {
		return emailEdicola;
	}

	public void setEmailEdicola(String emailEdicola) {
		this.emailEdicola = emailEdicola;
	}

	@Override
	public String getTitle() {
		return super.getTitle() + getText("password.dimenticata");
	}
	
	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
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

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public ClientiService<ClienteEdicolaVo> getClientiService() {
		return clientiService;
	}

	public void setClientiService(ClientiService<ClienteEdicolaVo> clientiService) {
		this.clientiService = clientiService;
	}
	
}
