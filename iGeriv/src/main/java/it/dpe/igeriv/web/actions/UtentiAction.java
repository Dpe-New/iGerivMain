package it.dpe.igeriv.web.actions;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.bo.menu.MenuService;
import it.dpe.igeriv.dto.UserDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.MenuModuloVo;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.service.mail.MailingListService;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per gestione degli utenti.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("utentiAction")
@SuppressWarnings({ "rawtypes" })
public class UtentiAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final AccountService accountService;
	private final MenuService menuService;
	private final PasswordEncoder passwordEncoder;
	private final ReflectionSaltSource saltSource;
	private final MailingListService mailingListService;
	private final String nomeUtenteDefault;
	private final String emailUtenteDefault;
	private final String crumbName = getText("igeriv.gestione.utenti");
	private String tableHeight;
	private String tableTitle;
	private List<UserDto> utenti;
	private List<GruppoModuliVo> gruppiModuli;
	private List<MenuModuloVo> moduli;
	private UserVo utente;
	private String actionName;
	private String tableStyle;
	private String nomeUtente;
	private String codUtente;
	private Boolean newUser = false;
	private Boolean inviaEmail;
	
	public UtentiAction() {
		this(null,null,null,null,null,null,null);
	}
	
	@Autowired
	public UtentiAction(
			AccountService accountService,
			MenuService menuService,
			PasswordEncoder passwordEncoder,
			ReflectionSaltSource saltSource,
			MailingListService mailingListService,
			@Value("${igeriv.default.user.name}")
			String nomeUtenteDefault,
			@Value("${igeriv.default.user.email}")
			String emailUtenteDefault) {
		this.accountService = accountService;
		this.menuService = menuService;
		this.passwordEncoder = passwordEncoder;
		this.saltSource = saltSource;
		this.mailingListService = mailingListService;
		this.nomeUtenteDefault = nomeUtenteDefault;
		this.emailUtenteDefault = emailUtenteDefault;
	}
	
	@Override
	public void validate() {
		if (utente != null) {
			if (utente.getNomeUtente() == null || utente.getNomeUtente().equals("")) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("dpe.contact.form.name")) + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
			if (Strings.isNullOrEmpty(utente.getEmail())) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("dpe.contact.form.email")) + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			} else if (!Strings.isNullOrEmpty(utente.getEmail()) && !StringUtility.isValidEmailAddress(utente.getEmail())) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.msg.invalid.email") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
			if (utente.getEmail() != null) {
				UserVo utenteEdicolaByEmail = accountService.getSottoUtenteEdicolaByEmail(utente.getEmail());
				if (utenteEdicolaByEmail != null && !utente.getCodUtente().equals(utenteEdicolaByEmail.getCodUtente())) {
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("error.sotto.utente.stessa.mail") + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				}
			}
		}
	}
	
	@SkipValidation
	@BreadCrumb("%{crumbName}")
	public String showUtenti() {
		tableTitle = getText("igeriv.gestione.utenti");
		utenti = accountService.getUtentiEdicola(getAuthUser().getId(), getAuthUser().isAdmin(), codUtente, nomeUtente, getAuthUser().getCodUtente());
		
		requestMap.put("utenti", utenti);
		return SUCCESS;
	}
	
	@SkipValidation
	public String showUtente() {
		tableTitle = getText("igeriv.inserisci.aggiorna.utenti");
		gruppiModuli = new ArrayList<GruppoModuliVo>();
		// 07/11/2016 - I sotto utenti devono essere creati con lo stesso menu del utente master
		//gruppiModuli.add(menuService.getGruppoModuliByRole(IGerivConstants.ROLE_IGERIV_BASE));
		UserVo utenteEdicola = accountService.getUtenteEdicola(getAuthUser().getId().toString());
		gruppiModuli.add(utenteEdicola.getDlGruppoModuliVo().getGruppoModuli());
		
		
		if (codUtente != null && !codUtente.equals("")) {
			utente = accountService.getUtenteEdicola(codUtente);
		} else {
			utente = new UserVo();
			utente.setCodUtente("" + accountService.getNextSeqVal(IGerivConstants.SEQ_UTENTI_EDICOLA));
			AbbinamentoEdicolaDlVo abbinamentoEdicolaDlVo = new AbbinamentoEdicolaDlVo();
			abbinamentoEdicolaDlVo.setCodDpeWebEdicola(getAuthUser().getId());
			utente.setAbbinamentoEdicolaDlVo(abbinamentoEdicolaDlVo);
			utente.setNomeUtente((nomeUtenteDefault != null && !nomeUtenteDefault.equals("")) ? nomeUtenteDefault : "");
			utente.setEmail((emailUtenteDefault != null && !emailUtenteDefault.equals("")) ? emailUtenteDefault : "");
			newUser = true;
		}
		requestMap.put("utente", utente);
		return IGerivConstants.ACTION_NUOVO_UTENTE_EDICOLA;
	}
	
	public String saveUtente() {
		if (utente != null) {
			String randomPwd = null;
			utente.setUtenteAmministratore(0);
			String pwd = null;
			if (newUser || inviaEmail) {
				randomPwd = StringUtility.getRandomString(14);
				GruppoModuliVo gmvo = menuService.getGruppoModuliById(utente.getDlGruppoModuliVo().getPk().getCodGruppo());
				String roleName = gmvo.getRoleName();
				List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
				authList.add(new GrantedAuthorityImpl(roleName));
				UserAbbonato ua = new UserAbbonato("" + utente.getCodUtente(), randomPwd, true, true, true, true, authList);
				ua.setCodFiegDl(getAuthUser().getCodFiegDl());
				ua.setCodEdicolaDl(getAuthUser().getCodEdicolaDl());
				pwd = passwordEncoder.encodePassword(randomPwd, saltSource.getSalt(ua));
				utente.setPwd(pwd);
				utente.setPwdCriptata(1);
				utente.setChangePassword(1);
			} else {
				UserVo utenteEdicola = accountService.getUtenteEdicola(utente.getCodUtente());
				utente.setPwd(utenteEdicola.getPwd());
			}
			utente.setPwdCriptata(1);
			utente.getDlGruppoModuliVo().getPk().setCodDl(getAuthUser().getCodFiegDl());
			accountService.saveBaseVo(utente);
			if (newUser || inviaEmail) {
				String subject = MessageFormat.format(getText("msg.subject.nuovo.utente.edicola"), getNomeApplicazione());
				String body = MessageFormat.format(getText("msg.email.nuovo.utente.edicola"), getNomeApplicazione(), getAuthUser().getRagioneSocialeEdicola(), utente.getCodUtente(), randomPwd);
				mailingListService.sendEmail(utente.getEmail(), subject, body);
			}
		}
		return SUCCESS;
	}
	
	@SkipValidation
	public String deleteUtente() {
		if (utente != null && utente.getCodUtente() != null) {
			UserVo vo = accountService.getUtenteEdicola(utente.getCodUtente());
			if (vo != null) {
				accountService.deleteVo(vo);
			}
		}
		return SUCCESS;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.extremecomponents.table.state.State#getParameters(org.extremecomponents
	 * .table.context.Context, java.lang.String, java.lang.String)
	 */
	public Map getParameters(Context arg0, String arg1, String arg2) {
		return arg0.getParameterMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.extremecomponents.table.state.State#saveParameters(org.extremecomponents
	 * .table.context.Context, java.lang.String, java.util.Map)
	 */
	public void saveParameters(Context context, String arg1, Map arg2) {
	}

	@Override
	public String getTitle() {
		return super.getTitle() + getText("igeriv.inserisci.aggiorna.utenti");
	}

	
}
