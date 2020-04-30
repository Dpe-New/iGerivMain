package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.flatten;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.lang.WordUtils;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.opensymphony.xwork2.Preparable;

import ch.lambdaj.group.Group;
import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.bo.livellamenti.LivellamentiService;
import it.dpe.igeriv.bo.messaggi.MessaggiService;
import it.dpe.igeriv.bo.rifornimenti.RifornimentiService;
import it.dpe.igeriv.dto.AvvisoMessaggioDto;
import it.dpe.igeriv.dto.PubblicazionePiuVendutaDto;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivUtils;
import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.HelpVo;
import it.dpe.igeriv.vo.MenuModuloVo;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.igeriv.vo.VideoHelpVo;
import it.dpe.igeriv.web.resources.ExposablePropertyPaceholderConfigurer;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action padre di tutte le actions che necessitano di autenticazione.
 * Implementa funzionalità comuni a molte actions.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@SuppressWarnings({"unchecked", "rawtypes"}) 
@Scope("prototype")
@Component("restrictedAccessBaseAction")
public class RestrictedAccessBaseAction extends BaseAction implements Preparable, RequestAware, ServletContextAware {
	private static final long serialVersionUID = 1L;
	@Autowired
	private RifornimentiService rifornimentiService;
	@Autowired
	private IGerivUtils iGerivUtils;
	@Autowired
	private AccountService accountService;
	@Autowired
	private LivellamentiService livellamentiService;
	@Autowired
	private ExposablePropertyPaceholderConfigurer exposablePropertyPaceholderConfigurer;
	@Value("${igeriv.url}")
	private String igerivUrl;
	protected AvvisoMessaggioDto messaggioNonLettoDto;
	private List<Long> idLivellamenti;
	private List<Long> idLivellamentiAccettati;
	private Boolean hasMessaggiDpe;
	private Boolean hasNotificheOrdiniClienti;
	protected Map<String, Object> requestMap = new HashMap<String, Object>();
	protected ServletContext context;
	private String coddlSelect;
	private String help;
	private List<VideoHelpVo> listHelpVideo;
	private Boolean checkedEmail;
	
	@Override
	public void prepare() throws Exception {
		if (getAuthUser() != null && getAuthUser().getTipoUtente() == IGerivConstants.TIPO_UTENTE_EDICOLA) {
			final MessaggiService messaggiService = SpringContextManager.getSpringContext().getBean(MessaggiService.class);
			messaggioNonLettoDto = messaggiService.getPkMessaggioNonLetto(getAuthUser().getId(), getAuthUser().getCodFiegDl(), getAuthUser().getGiroTipo(), getAuthUser().getGiri(), null, getAuthUser().getDtAttivazioneEdicola());
			if (getAuthUser().getHasLivellamenti()) {
				idLivellamenti = livellamentiService.getIdRichiesteLivellamentiInserite(getAuthUser().getId());
				idLivellamentiAccettati = livellamentiService.getIdRichiesteLivellamentiAccettate(getAuthUser().getId());
			}
			hasMessaggiDpe = messaggiService.hasMessaggiDpe();
			if (sessionMap.get("hasProfiloStarter") == null || !Boolean.parseBoolean(sessionMap.get("hasProfiloStarter").toString())) {
				hasNotificheOrdiniClienti = rifornimentiService.hasNotificheOrdiniClienti(getAuthUser().getCodFiegDl(), getAuthUser().getId());
			}
			if (context != null && context.getAttribute("help") != null) {
				List<HelpVo> helpList = (List<HelpVo>) context.getAttribute("help");
				if (helpList != null) {
					HelpVo helpVo = getCurrentHelpVo(helpList);
					if (helpVo != null) {
						help = helpVo.getHelp();
						listHelpVideo = helpVo.getVideoHelp();
					}
				}
			} 
		}
		if(parameterMap!=null){
			if (getAuthUser() != null && getAuthUser().isMultiDl() && parameterMap.get("coddlSelect") != null && parameterMap.get(IGerivConstants.PARAM_COD_DL_SELECT).length > 0 && !Strings.isNullOrEmpty(parameterMap.get("coddlSelect")[0])) {
				Integer userId = iGerivUtils.getCorrispondenzaCodEdicolaMultiDl(new Integer(parameterMap.get("coddlSelect")[0]), getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId());
				UserVo utente = accountService.getEdicolaByCodiceEdicola(userId);
				UserAbbonato user = accountService.buildUserDetails(userId.toString(), utente);
				Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		if (getActionName() != null && !getActionName().toUpperCase().contains("HOME.ACTION") && request.getSession().getAttribute("alertSospensione") != null) {
			request.getSession().removeAttribute("alertSospensione");
		}
		
		/*if (getAuthUser() != null) {
			Integer coddl = getAuthUser().getCodFiegDl();
			String actionName = getActionName();
			if (actionName != null && coddl == 22) {
				checkModule(actionName);
			}
		}*/
	}
	
	private void checkModule(String actionName) {
		boolean bollaReadOnly = false;
		if (getAuthUser() != null && getAuthUser().getGruppoModuliVo() != null) {
			bollaReadOnly = getAuthUser().getGruppoModuliVo().getIsBollaConsegnaReadOnly();
		}
		if (bollaReadOnly) {
			List<MenuModuloVo> moduli = flatten(getListModuli());
			/*
				actionName:
				bollaRivendita_showBolla.action
				bollaResa_showBollaResa.action
				estrattoConto_showEstratto.action
				emailInviati_showEmailInviati.action

				url:
				bollaRivendita_showBolla.action
				bollaResa_showFilter.action
				estrattoConto_showFilter.action
				messages_showMessagesEdicoleFilter.action
				email_showEmailRivendita.action
				emailInviati_showEmailInviatiFilter.action
				changePwd_input.action
				params_showParams.action	
				
				aggiungere
				messages_saveMessageEdicola.action
				login
				logout
				home
				accessDenied
			 */
			boolean authorized = false;
			authorized = 
			   actionName.contains("login")
			    || actionName.contains("logout")
			    || actionName.contains("home")
			    || actionName.contains("accessDenied")
			    || actionName.contains("bollaRivendita")
				|| actionName.contains("bollaResa")
				|| actionName.contains("estrattoConto")
				|| actionName.contains("messages")
				|| actionName.contains("email")
				|| actionName.contains("params")
				|| actionName.contains("changePwd");
			
			if (!authorized) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + "Modulo non Previsto" + IGerivConstants.END_EXCEPTION_TAG);
				// throw new IGerivRuntimeException(); PROVOCA UN LOOP
				throw new AccessDeniedException(null);
			}
		}
	}
	
	/**
	 * @param helpList
	 * @return
	 */
	private HelpVo getCurrentHelpVo(List<HelpVo> helpList) {
		for (HelpVo vo : helpList) {
			if (vo.getAction().equalsIgnoreCase(getActionName())) {
				return vo;
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	public UserAbbonato getAuthUser() {
		return (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserAbbonato) ? (UserAbbonato) SecurityContextHolder.getContext().getAuthentication().getPrincipal() : null;
	}
	
	/**
	 * @param param
	 * @return
	 */
	protected Set<String> buildSet(String param) {
		Set<String> pkSet = new LinkedHashSet<String>();
		if (param != null && !param.equals("")) {
			String[] split = param.split(",");
			for (String s : split) {
				s = s.trim();
				if (!s.equals("")) {
					pkSet.add(s);
				}
			}
		}
		return pkSet;
	}
	
	/**
	 * Costruisce il titolo da visualizzare nella testa dei i report 
	 * delle bolle (consegna, resa, sono inoltre uscite e C/D).
	 */
	protected void setAgenziaEdicolaExportTitle() {
		String ragSocAgenzia = getAuthUser().getRagioneSocialeDl();
		String indirizzoAgenzia = getAuthUser().getIndirizzoAgenziaPrimaRiga();
		String pivaAgenzia = getAuthUser().getPivaAgenzia();
		String cittaAgenzia = getAuthUser().getLocalitaAgenziaPrimaRiga();
		String capAgenzia = getAuthUser().getCapAgenzia();
		String provAgenzia = getAuthUser().getProvinciaAgenzia();
		String codEdicola = getAuthUser().getCodEdicolaDl().toString();
		String ragSocEdicola = getAuthUser().getRagioneSocialeEdicola(); 
		String indirizzoEdicola = getAuthUser().getIndirizzoEdicolaPrimaRiga();
		String pivaEdicola = getAuthUser().getPiva();
		String cittaEdicola = getAuthUser().getLocalitaEdicolaPrimaRiga();
		String capEdicola = getAuthUser().getCapEdicola();
		String provEdicola = getAuthUser().getProvinciaEdicola();
		requestMap.put("intestazioneAg", " ");
		requestMap.put("codAgenzia",  "");
		requestMap.put("ragSocAgenzia", " " + StringUtility.checkSpecialChars(((ragSocAgenzia != null) ? ragSocAgenzia.replaceAll("&nbsp;", " ") : "")));
		requestMap.put("indirizzoAgenzia",  StringUtility.checkSpecialChars(((indirizzoAgenzia != null) ? WordUtils.capitalizeFully(indirizzoAgenzia.replaceAll("&nbsp;", " ").replaceAll("\\*", " ")) : "")) + " - " + StringUtility.checkSpecialChars(((capAgenzia != null) ? capAgenzia.replaceAll("&nbsp;", " ") : "")) + " - " + StringUtility.checkSpecialChars(((cittaAgenzia != null) ? WordUtils.capitalizeFully(cittaAgenzia.replaceAll("&nbsp;", " ").replaceAll("\\*", " ")) : "")) + StringUtility.checkSpecialChars((provAgenzia != null ? " (" + provAgenzia.replaceAll("&nbsp;", " ") + ")" : "")));
		requestMap.put("pivaAgenzia", StringUtility.checkSpecialChars(getText("dpe.partita.iva")) + ": " + StringUtility.checkSpecialChars((((pivaAgenzia != null) ? pivaAgenzia.replaceAll("&nbsp;", " ") : ""))));
		requestMap.put("intestazioneRiv", StringUtility.checkSpecialChars(getText("igeriv.spett")));
		requestMap.put("codEdicola", StringUtility.checkSpecialChars(getText("igeriv.provenienza.evasione.edicola")) + ": " + StringUtility.checkSpecialChars(((codEdicola != null) ? codEdicola.replaceAll("&nbsp;", " ") : "")));
		requestMap.put("ragSocEdicola",  " " + StringUtility.checkSpecialChars(((ragSocEdicola != null) ? ragSocEdicola.replaceAll("&nbsp;", " ") : "")));
		requestMap.put("indirizzoEdicola",  StringUtility.checkSpecialChars(((indirizzoEdicola != null) ? WordUtils.capitalizeFully(indirizzoEdicola.replaceAll("&nbsp;", " ").replaceAll("\\*", " ")) : "")) + " - " + StringUtility.checkSpecialChars(((capEdicola != null) ? capEdicola.replaceAll("&nbsp;", " ") : "")) + " - " + StringUtility.checkSpecialChars(((cittaEdicola != null) ? WordUtils.capitalizeFully(cittaEdicola.replaceAll("&nbsp;", " ").replaceAll("\\*", " ")) : "")) + StringUtility.checkSpecialChars((provEdicola != null ? " (" + provEdicola.replaceAll("&nbsp;", " ") + ")" : "")));
		requestMap.put("pivaEdicola",  StringUtility.checkSpecialChars(getText("dpe.partita.iva")) + ": " + StringUtility.checkSpecialChars(((pivaEdicola != null) ? pivaEdicola.replaceAll("&nbsp;", " ") : "")));
	}
	
	/**
	 * Riordina la lista di PubblicazionePiuVendutaDto per i casi dei quotidiani che hanno titoli diversi 
	 * e diversi CPU per ogni giorno della settimana.
	 * Se esistono entrate con lo stesso codice inizio quotidiano e codice fine quotidiano, somma le copie della stessa pubblicazione 
	 * e mantiene solo la prima della lista.  
	 * @param listPubblicazioniPiuVendute 
	 * 
	 * @return List<PubblicazionePiuVendutaDto>
	 */
	protected List<PubblicazionePiuVendutaDto> buildListQuotidianiPiuVenduti(List<PubblicazionePiuVendutaDto> listPubblicazioniPiuVendute) {
		List<PubblicazionePiuVendutaDto> retList = new ArrayList<PubblicazionePiuVendutaDto>();
		Group<PubblicazionePiuVendutaDto> group = group(listPubblicazioniPiuVendute, by(on(PubblicazionePiuVendutaDto.class).getCodInizioQuotidiano()), by(on(PubblicazionePiuVendutaDto.class).getCodFineQuotidiano()));
		if (group.subgroups().size() > 0) {
			for (Group<PubblicazionePiuVendutaDto> subgroup : group.subgroups()) {
				List<PubblicazionePiuVendutaDto> findAll = subgroup.findAll();
				if (findAll.size() > 1) {
					PubblicazionePiuVendutaDto first = subgroup.first();
					long quantita = 0;
					for (PubblicazionePiuVendutaDto ppv : findAll) {
						quantita += ppv.getQuantita() != null ? ppv.getQuantita() : 0;
					}
					findAll.removeAll(findAll);
					first.setQuantita(quantita);
					retList.add(first);
				} else {
					retList.add(findAll.get(0));
				}
			}
		} 
		return retList;
	}
	
	/**
	 * @param list
	 * @return
	 */
	protected String getTabHeight(List<?> list) {
		int tabHeight = list.size() * IGerivConstants.TABLE_ROW_HEIGHT;
		if (tabHeight > IGerivConstants.MAX_TABLE_HEIGHT) {
			tabHeight = IGerivConstants.MAX_TABLE_HEIGHT;
		}
		String tableHeight = tabHeight + "px";
		return tableHeight;
	}
	
	public List<List<List<MenuModuloVo>>> getListModuli() {
		return getAuthUser().getModuli();
	}

	public String getTitle() {
		boolean isCdl = getAuthUser() != null && getAuthUser().getCodFiegDl() != null && getAuthUser().getCodFiegDl().equals(IGerivConstants.CDL_CODE);
		boolean isGazzetta = getAuthUser() != null && getAuthUser().getCodFiegDl() != null && getAuthUser().getCodFiegDl().equals(IGerivConstants.MENTA_CODE);
		boolean isDevietti = getAuthUser() != null && getAuthUser().getEdicolaDeviettiTodis();
		return getTitolo(isCdl, isDevietti, isGazzetta) + (isCdl ? "" : " | ");
	}
	
	private String getTitolo(boolean isCdl, boolean isDevietti, boolean isGazzetta) {
		String titolo = getText("gp.titolo");
		if (isCdl) {
			titolo = "";
		} else if (isDevietti) {
			titolo = getText("gp.titolo.edismart");
		} else if (isGazzetta) {
			titolo = getText("gp.titolo.gazzetta");
		}
		return titolo;
	}
	
	protected String getNomeApplicazione() {
		if (getAuthUser().getCodFiegDl() != null) {
			if (getAuthUser().getCodFiegDl().equals(IGerivConstants.CDL_CODE)) {
				return getText("igeriv.cdl");
			} else if (getAuthUser().getCodFiegDl().equals(IGerivConstants.COD_FIEG_DL_DEVIETTI) || getAuthUser().getCodFiegDl().equals(IGerivConstants.COD_FIEG_DL_TODIS)) {
				return getText("gp.titolo.edismart");
			}
		}
		return getText("gp.titolo");
	}
	
	
	
	
	
	public Map getConstants() {
		return (Map) context.getAttribute(IGerivConstants.ID);
	}
	
	@Override
	public void setRequest(Map<String, Object> request) {
		this.requestMap = request;
	}

	@Override
	public void setServletContext(ServletContext context) {
		this.context = context;
	}
	
	public String getGiorniProvaPerIGerivStarter() {
		return exposablePropertyPaceholderConfigurer.getResolvedProps().get("igeriv.numero.giorni.prova.per.igeriv.starter");
	}
	
	public Integer getCampagnaInvitaColleghiAbilitata() {
		return exposablePropertyPaceholderConfigurer.getResolvedProps().get("igeriv.campagna.invita.colleghi.abilitata") == null ? 0 : new Integer(exposablePropertyPaceholderConfigurer.getResolvedProps().get("igeriv.campagna.invita.colleghi.abilitata"));
	}
	
	public String getIgerivUrl() {
		return this.igerivUrl;
	}
	
	public String SetIgerivUrl(String igerivUrl) {
		return this.igerivUrl = igerivUrl;
	}
	
	public String getListIdLivellamenti() {
		return getIdLivellamenti() != null && getIdLivellamenti().size() > 0 ? Joiner.on(",").join(getIdLivellamenti()) : "";
	}
	
	public String getListIdLivellamentiAccettati() {
		return getIdLivellamentiAccettati() != null && getIdLivellamentiAccettati().size() > 0 ? Joiner.on(",").join(getIdLivellamentiAccettati()) : "";
	}
	
}
