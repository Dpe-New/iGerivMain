package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.dto.AvvisoMessaggioDto;
import it.dpe.igeriv.dto.PubblicazionePiuVendutaDto;
import it.dpe.igeriv.resources.ExposablePropertyPaceholderConfigurer;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.MenuModuloVo;
import it.dpe.igeriv.vo.VideoHelpVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import lombok.Getter;
import lombok.Setter;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.security.core.context.SecurityContextHolder;

import ch.lambdaj.group.Group;

import com.opensymphony.xwork2.Preparable;

/**
 * Classe action padre di tutte le actions che necessitano di autenticazione.
 * Implementa funzionalità comuni a molte actions.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@SuppressWarnings({"rawtypes"}) 
public class RestrictedAccessBaseAction extends BaseAction implements Preparable, RequestAware, ServletContextAware {
	private static final long serialVersionUID = 1L;
	private AvvisoMessaggioDto messaggioNonLettoDto;
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
		String codAgenzia = "" + getAuthUser().getCodFiegDl();
		String ragSocAgenzia = getAuthUser().getRagioneSocialeDl();
		String indirizzoAgenzia = getAuthUser().getIndirizzoAgenziaPrimaRiga();
		String cittaAgenzia = getAuthUser().getLocalitaAgenziaPrimaRiga();
		String codEdicola = "" + getAuthUser().getCodEdicolaDl();
		String ragSocEdicola = getAuthUser().getRagioneSocialeEdicola(); 
		String indirizzoEdicola = getAuthUser().getIndirizzoEdicolaPrimaRiga();
		String cittaEdicola = getAuthUser().getLocalitaEdicolaPrimaRiga();
		requestMap.put("intestazioneAg", " ");
		requestMap.put("codAgenzia",  StringUtility.checkSpecialChars(getText("username.dl").replaceAll("&nbsp;", " ")) + ": " + StringUtility.checkSpecialChars(((codAgenzia != null) ? codAgenzia.replaceAll("&nbsp;", " ") : "")));
		requestMap.put("ragSocAgenzia", StringUtility.checkSpecialChars(getText("dpe.rag.sociale")) + ": " + " " + StringUtility.checkSpecialChars(((ragSocAgenzia != null) ? ragSocAgenzia.replaceAll("&nbsp;", " ") : "")));
		requestMap.put("indirizzoAgenzia", StringUtility.checkSpecialChars(getText("dpe.indirizzo")) + ": " + StringUtility.checkSpecialChars((((indirizzoAgenzia != null) ? indirizzoAgenzia.replaceAll("&nbsp;", " ") : "")) + " - " + StringUtility.checkSpecialChars((cittaAgenzia != null ? cittaAgenzia.replaceAll("&nbsp;", " ") : ""))));
		requestMap.put("intestazioneRiv", StringUtility.checkSpecialChars(getText("igeriv.spett")));
		requestMap.put("codEdicola", StringUtility.checkSpecialChars(getText("dpe.login.ed.user").replaceAll("&nbsp;", " ")) + ": " + StringUtility.checkSpecialChars(((codEdicola != null) ? codEdicola.replaceAll("&nbsp;", " ") : "")));
		requestMap.put("ragSocEdicola",  StringUtility.checkSpecialChars(getText("igeriv.provenienza.evasione.edicola")) + ": " + " " + StringUtility.checkSpecialChars(((ragSocEdicola != null) ? ragSocEdicola.replaceAll("&nbsp;", " ") : "")));
		requestMap.put("indirizzoEdicola",  StringUtility.checkSpecialChars(getText("dpe.indirizzo")) + ": " + StringUtility.checkSpecialChars(((indirizzoEdicola != null) ? indirizzoEdicola.replaceAll("&nbsp;", " ") : "")) + " - " + StringUtility.checkSpecialChars(((cittaEdicola != null) ? cittaEdicola.replaceAll("&nbsp;", " ") : "")));
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
		boolean isCdl = getAuthUser().getCodFiegDl() != null && getAuthUser().getCodFiegDl().equals(IGerivConstants.CDL_CODE);
		return getTitolo(isCdl) + (isCdl ? "" : " | ");
	}
	
	private String getTitolo(boolean isCdl) {
		String titolo = getText("gp.titolo");
		if (isCdl) {
			titolo = "";
		}
		return titolo;
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
		ExposablePropertyPaceholderConfigurer props = (ExposablePropertyPaceholderConfigurer) SpringContextManager.getService("igerivProperties");
		return props.getResolvedProps().get("igeriv.numero.giorni.prova.per.igeriv.starter");
	}
	
	public Integer getCampagnaInvitaColleghiAbilitata() {
		ExposablePropertyPaceholderConfigurer props = (ExposablePropertyPaceholderConfigurer) SpringContextManager.getService("igerivProperties");
		return props.getResolvedProps().get("igeriv.campagna.invita.colleghi.abilitata") == null ? 0 : new Integer(props.getResolvedProps().get("igeriv.campagna.invita.colleghi.abilitata"));
	}
	
}
