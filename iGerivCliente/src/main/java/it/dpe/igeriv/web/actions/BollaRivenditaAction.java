package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sum;
import it.dpe.igeriv.bo.bolle.BolleService;
import it.dpe.igeriv.bo.messaggi.MessaggiService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.bo.rifornimenti.RifornimentiService;
import it.dpe.igeriv.dto.BaseDto;
import it.dpe.igeriv.dto.BollaDettaglioDto;
import it.dpe.igeriv.dto.DateTipiBollaDto;
import it.dpe.igeriv.dto.FondoBollaDettaglioDto;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.dto.PrenotazioneDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.dto.RichiestaClienteDto;
import it.dpe.igeriv.dto.RichiestaRifornimentoDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivUtils;
import it.dpe.igeriv.vo.BollaRiassuntoVo;
import it.dpe.igeriv.vo.MessaggiBollaVo;
import it.dpe.igeriv.vo.PrenotazioneVo;
import it.dpe.igeriv.vo.RichiestaClienteVo;
import it.dpe.igeriv.vo.RichiestaFissaClienteEdicolaVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoVo;
import it.dpe.igeriv.vo.pk.RichiestaClientePk;
import it.dpe.igeriv.vo.pk.RichiestaRifornimentoPk;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.extremecomponents.util.ExtremeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * Classe action per la bolla di consegna.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@SuppressWarnings({"unchecked", "rawtypes"}) 
@Scope("prototype")
@Component("bollaRivenditaAction")
public class BollaRivenditaAction<T extends BaseDto> extends RestrictedAccessBaseAction implements State, 
		RequestAware {
	private static final long serialVersionUID = 1L;
	@Autowired
	private BolleService bolleService;
	@Autowired
	private PubblicazioniService pubblicazioniService;
	@Autowired
	private MessaggiService messaggiService;
	@Autowired
	private RifornimentiService rifornimentiService;
	@Autowired
	private IGerivUtils iGerivUtils;
	private String tipoOperazione;
	private String soloRigheSpuntare;
	private List<BollaRiassuntoVo> bolleRiassunto;
	private String dataTipoBolla;
	private List itensBolla;
	private List<MessaggiBollaVo> messaggiBolla;
	private List<RichiestaRifornimentoDto> richiesteRifornimento;
	private List<PrenotazioneDto> variazioni;
	private String idtn;
	private String coddl;
	private String periodicita;
	private String filterTitle;
	private String tableTitle;
	private String actionName;
	private String actionName1;
	private Integer tableHeight;
	private PrenotazioneVo prenotazioneVo;
	private String quantitaVariazioneServizio;
	private RichiestaFissaClienteEdicolaVo richiestaFissaClienteEdicolaVo;
	private String prenotazioneDisabled = "false";
	private boolean disableForm;
	private Map<String, String> fieldMap;
	private Map<String, String> fieldFBMap;
	private Map<String, String> spuntaMap;
	private Map<String, String> spuntaFBMap;
	private Map<String, String> modificatoMap;
	private Map<String, String> modificatoFBMap;
	private String pk;
	private String pkSel;
	private String dataScadenzaRichiesta;
	private String quantitaRifornimento;
	private String noteVendita;
	private String dataScadenzaRichiestaAggiunte;
	private String quantitaRifornimentoAggiunte;
	private String noteVenditaAggiunte;
	private String pkAggiunte;
	private String titolo;
	private String stato;
	private String idCliente;
	private String nomeCliente;
	private String importoLordo;
	private String downloadToken;
	private String dataDa;
	private String dataA;
	private String BollaControlloTab_ev;
	private String cpu;
	private boolean isDlInforiv;
	private Timestamp dataStorico;
	private String spunteRichiestaFissa;
	private String spunteRichiesta;
	
	@Override
	public void validate() {
		if (getActionName() != null && getActionName().equals("viewRifornimenti_showRichiesteRifornimenti.action")) {
			filterTitle = getText("igeriv.bolla.visualizza.rifornimenti");
			requestMap.put("tipoRifornimento", "edicola");
			if (dataDa == null || dataA == null || dataDa.equals("") || dataA.equals("")) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				dataA = DateUtilities.getTimestampAsString(cal.getTime(), DateUtilities.FORMATO_DATA_SLASH);
				cal.add(Calendar.DAY_OF_MONTH, -15);
				dataDa = DateUtilities.getTimestampAsString(cal.getTime(), DateUtilities.FORMATO_DATA_SLASH);
			} else {
				try {
					DateUtilities.floorDay(DateUtilities.parseDate(dataDa, DateUtilities.FORMATO_DATA_SLASH));
				} catch (ParseException e) {
					addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataDa));
					return;
				}
				try {
					DateUtilities.ceilDay(DateUtilities.parseDate(dataA, DateUtilities.FORMATO_DATA_SLASH));
				} catch (ParseException e) {
					addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataA));
					return;
				}
			}
		} else if (getActionName() != null && getActionName().equals("viewVariazioni_showVariazioniInserite.action")) {
			filterTitle = getText("igeriv.bolla.visualizza.variazioni.inserite");
			if (dataDa == null || dataA == null || dataDa.equals("") || dataA.equals("")) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				dataA = DateUtilities.getTimestampAsString(cal.getTime(), DateUtilities.FORMATO_DATA_SLASH);
				cal.add(Calendar.DAY_OF_MONTH, -15);
				dataDa = DateUtilities.getTimestampAsString(cal.getTime(), DateUtilities.FORMATO_DATA_SLASH);
			} else {
				try {
					DateUtilities.floorDay(DateUtilities.parseDate(dataDa, DateUtilities.FORMATO_DATA_SLASH));
				} catch (ParseException e) {
					addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataDa));
					return;
				}
				try {
					DateUtilities.ceilDay(DateUtilities.parseDate(dataA, DateUtilities.FORMATO_DATA_SLASH));
				} catch (ParseException e) {
					addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataA));
					return;
				}
			}
		}
		if (quantitaVariazioneServizio != null && !quantitaVariazioneServizio.trim().equals("")) {
			try {
				org.springframework.util.NumberUtils.parseNumber(quantitaVariazioneServizio.replaceAll("\\+", ""), Integer.class);
			} catch (Exception e) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.msg.variazione.servizio") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
		}
		if (!Strings.isNullOrEmpty(dataTipoBolla) && dataTipoBolla.contains(",")) {
			dataTipoBolla = dataTipoBolla.substring(0, dataTipoBolla.indexOf(",")).trim();
		}
		if (!Strings.isNullOrEmpty(soloRigheSpuntare) && soloRigheSpuntare.contains(",")) {
			soloRigheSpuntare = soloRigheSpuntare.substring(0, soloRigheSpuntare.indexOf(",")).trim();
		}
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		if (getAuthUser().getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_EDICOLA)) {
			requestMap.put("memorizza", getText("igeriv.memorizza"));
			requestMap.put("memorizzaInvia", getText("igeriv.memorizza.invia"));
			requestMap.put("esportaOrdini", getText("igeriv.esporta.ordini.clienti"));
			requestMap.put("esportaOrdiniDettaglio", getText("igeriv.esporta.ordini.clienti.dettaglio"));
			isDlInforiv = getAuthUser().isDlInforiv();
			dataStorico = getAuthUser().getDataStorico();
			doShowFilter();
		}
	}
	
	@SkipValidation
	public String showFilter() throws Exception {
		filterTitle = getText("igeriv.bolla.consegna");
		return SUCCESS;
	}
	
	@SkipValidation
	public String showFilterSonoInoltreUscite() throws Exception {
		filterTitle = getText("igeriv.sono.inoltre.uscite");
		return SUCCESS;
	}
	
	public String showBolla() throws Exception {
		filterTitle = getText("igeriv.bolla.consegna");
		if (dataTipoBolla == null || dataTipoBolla.equals("")) {
			return "input";
		}
		StringTokenizer st = new StringTokenizer(dataTipoBolla, "|");
		String strData = st.nextToken();
		String tipo = st.nextToken().replaceFirst(IGerivConstants.TIPO, "").trim();
		Boolean enabled = Boolean.valueOf(st.nextToken().trim());
		Timestamp dtBolla = DateUtilities.parseDate(strData, DateUtilities.FORMATO_DATA);
		boolean showSoloRigheSpuntare = soloRigheSpuntare == null ? false : (soloRigheSpuntare.equals("1") ? true : false);
		itensBolla = bolleService.getDettaglioBolla(new Integer[]{getAuthUser().getCodFiegDl()}, new Integer[]{getAuthUser().getId()}, dtBolla, tipo, showSoloRigheSpuntare, false);
		List dettagliFondoBolla = bolleService.getDettagliFondoBolla(new Integer[]{getAuthUser().getCodFiegDl()}, new Integer[]{getAuthUser().getId()}, dtBolla, tipo, showSoloRigheSpuntare, false, false);
		BigDecimal sumImportoLordoBolla = sum(itensBolla, on(BollaDettaglioDto.class).getImportoLordo());
		BigDecimal sumImportoLordoFondoBolla = sum(dettagliFondoBolla, on(FondoBollaDettaglioDto.class).getImportoLordo());
		BigDecimal sumBolla = sum(itensBolla, on(BollaDettaglioDto.class).getImporto());
		BigDecimal sumFondoBolla = sum(dettagliFondoBolla, on(FondoBollaDettaglioDto.class).getImporto());
		String totaleBollaFormat = ExtremeUtils.formatNumber(IGerivConstants.DECIMAL_FORMAT_PATTERN, sumBolla, getCurrentLocale());
		String totaleFondoBollaFormat = ExtremeUtils.formatNumber(IGerivConstants.DECIMAL_FORMAT_PATTERN, sumFondoBolla, getCurrentLocale());
		String totaleBollaLordoFormat = ExtremeUtils.formatNumber(IGerivConstants.DECIMAL_FORMAT_PATTERN, sumImportoLordoBolla, getCurrentLocale());
		String totaleFondoBollaLordoFormat = ExtremeUtils.formatNumber(IGerivConstants.DECIMAL_FORMAT_PATTERN, sumImportoLordoFondoBolla, getCurrentLocale());
		messaggiBolla = messaggiService.getMessaggiBolla(new Integer[]{getAuthUser().getCodFiegDl()}, new Integer[]{getAuthUser().getId()}, dtBolla, tipo);
		itensBolla.addAll(dettagliFondoBolla);
		importoLordo = ExtremeUtils.formatNumber(IGerivConstants.DECIMAL_FORMAT_PATTERN, (sumImportoLordoBolla.add(sumImportoLordoFondoBolla)), getCurrentLocale());
		int count = 0;
		for (Object vo : itensBolla) {
			BeanUtils.setProperty(vo, "rownum", ++count);
		}
		
		requestMap.put("totaleBollaFormat", totaleBollaFormat);
		requestMap.put("totaleFondoBollaFormat", totaleFondoBollaFormat);
		requestMap.put("totaleBollaLordoFormat", totaleBollaLordoFormat);
		requestMap.put("totaleFondoBollaLordoFormat", totaleFondoBollaLordoFormat);
		requestMap.put("itensBolla", itensBolla);
		requestMap.put("messaggiBolla", messaggiBolla);
		requestMap.put("strData", strData);
		requestMap.put("tipo", tipo);
		
		if (BollaControlloTab_ev != null && (BollaControlloTab_ev.toUpperCase().equals("PDF") || BollaControlloTab_ev.toUpperCase().equals("XLS"))) {
			setAgenziaEdicolaExportTitle();
		}
		disableForm = !enabled;
		return SUCCESS;
	}

	public String showBollaSonoInoltreUscite() throws Exception {
		filterTitle = getText("igeriv.sono.inoltre.uscite");
		if (dataTipoBolla == null || dataTipoBolla.equals("")) {
			return "input";
		}
		StringTokenizer st = new StringTokenizer(dataTipoBolla, "|");
		String strData = st.nextToken();
		String tipo = st.nextToken().replaceFirst(IGerivConstants.TIPO, "").trim();
		Boolean enabled = Boolean.valueOf(st.nextToken().trim());
		Timestamp dtBolla = DateUtilities.parseDate(strData, DateUtilities.FORMATO_DATA);
		itensBolla = bolleService.getBollaVoSonoInoltreUscite(getAuthUser().getCodFiegDl(), getAuthUser().getId(), dtBolla, tipo);
		int count = 0;
		for (Object vo : itensBolla) {
			BeanUtils.setProperty(vo, "rownum", ++count);
		}
		requestMap.put("itensBolla", itensBolla);
		requestMap.put("title", getText("igeriv.sono.inoltre.uscite"));
		if (BollaControlloTab_ev != null && (BollaControlloTab_ev.toUpperCase().equals("PDF") || BollaControlloTab_ev.toUpperCase().equals("XLS"))) {
			setAgenziaEdicolaExportTitle();
		}
		disableForm = !enabled;
		return SUCCESS;
	}
	
	@SkipValidation
	public String showRichiesteRifornimentoFilter() {
		filterTitle = getText("igeriv.bolla.visualizza.rifornimenti");
		requestMap.put("tipoRifornimento", "edicola");
		return IGerivConstants.ACTION_RICHIESTE_RIFORNIMENTI;
	}
	
	public String showRichiesteRifornimenti() throws ParseException {
		filterTitle = getText("igeriv.bolla.visualizza.rifornimenti");
		requestMap.put("tipoRifornimento", "edicola");
		if (!Strings.isNullOrEmpty(idtn) && !Strings.isNullOrEmpty(periodicita) && !Strings.isNullOrEmpty(this.coddl)) {
			Integer coddl = Strings.isNullOrEmpty(this.coddl) ? getAuthUser().getCodFiegDl() : new Integer(this.coddl);
			Integer codEdicola = Strings.isNullOrEmpty(this.coddl) ? getAuthUser().getId() : iGerivUtils.getCorrispondenzaCodEdicolaMultiDl(coddl, getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId());
			Integer idtnInt = new Integer(idtn);
			richiesteRifornimento = rifornimentiService.getRichiesteRifornimenti(coddl, getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), idtnInt, isDlInforiv, dataStorico, null);
			PubblicazioneDto copertina = pubblicazioniService.getCopertinaByIdtn(coddl, idtnInt);
			List statistiche = pubblicazioniService.getCopertine(false, true, false, getAuthUser().getCodEdicolaMaster(), getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), null, null, null, periodicita, null, copertina.getCodicePubblicazione(), null, false, dataStorico, null, false, null, idtnInt);
			requestMap.put("statistica", statistiche);
			if (requestMap.get("prenotazioneVo") == null) {
				RichiestaRifornimentoDto rfvo = (RichiestaRifornimentoDto) richiesteRifornimento.get(0);
				prenotazioneVo = rifornimentiService.getPrenotazione(coddl, codEdicola, rfvo.getCodicePubblicazione());
				quantitaVariazioneServizio = prenotazioneVo.getQuantitaRichiesta() != null ? prenotazioneVo.getQuantitaRichiesta().toString() : "";
				if (!prenotazioneVo.isEnabled()) {
					prenotazioneDisabled = "true";
				}
			}
			String titolo = ((RichiestaRifornimentoDto) richiesteRifornimento.get(0)).getTitolo().replaceAll("&nbsp;", " ").replaceAll("\\.", " ");
			requestMap.put("title", getText("igeriv.bolla.rifornimenti") + ": " + titolo);
			requestMap.put("titleStatistica", getText("igeriv.statistica.pubblicazioni") + ": " + titolo);
			requestMap.put("titleSemaforo", " ");
			if (getAuthUser().getVisualizzaSemaforoGiacenza()) {
				requestMap.put("titleSemaforo", getText("igeriv.disponibilita"));
				if (copertina.getGiancezaPressoDl() != null && copertina.getGiancezaPressoDl() > 0) {
					requestMap.put("giacenzaPressoDl", copertina.getGiancezaPressoDl());
				} else {
					requestMap.put("giacenzaPressoDl", -1);
				}
			}
		} else {
			Timestamp dataDa = null;
			Timestamp dataA = null;
			if (this.dataDa != null) {
				dataDa = DateUtilities.floorDay(DateUtilities.parseDate(this.dataDa, DateUtilities.FORMATO_DATA_SLASH));
			}
			if (this.dataA != null) {
				dataA = DateUtilities.ceilDay(DateUtilities.parseDate(this.dataA, DateUtilities.FORMATO_DATA_SLASH));
			}
			richiesteRifornimento = new ArrayList<RichiestaRifornimentoDto>();
			for (int i = 0; i < getAuthUser().getArrCodFiegDl().length; i++) {
				richiesteRifornimento.addAll((List)rifornimentiService.getRichiesteRifornimenti(getAuthUser().getArrCodFiegDl()[i], getAuthUser().getArrId()[i], titolo, stato, dataDa, dataA, isDlInforiv));
			}
		}
		requestMap.put("richiesteRifornimento", richiesteRifornimento);
		requestMap.put("filterTitle", filterTitle);
		return IGerivConstants.ACTION_RICHIESTE_RIFORNIMENTI;
	}
	
	@SkipValidation
	public String showVariazioni() {
		if (idtn != null && !idtn.equals("") && periodicita != null && !periodicita.equals("")) {
			Integer idtnInt = new Integer(idtn);
			Integer coddl = Strings.isNullOrEmpty(this.coddl) ? getAuthUser().getCodFiegDl() : new Integer(this.coddl);
			Integer codEdicola = Strings.isNullOrEmpty(this.coddl) ? getAuthUser().getId() : iGerivUtils.getCorrispondenzaCodEdicolaMultiDl(coddl, getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId());
			PubblicazioneDto copertina = pubblicazioniService.getCopertinaByIdtn(coddl, idtnInt);
			prenotazioneVo = rifornimentiService.getPrenotazione(coddl, codEdicola, copertina.getCodicePubblicazione());
			quantitaVariazioneServizio = prenotazioneVo.getQuantitaRichiesta() != null ? prenotazioneVo.getQuantitaRichiesta().toString() : "";
			boolean showOnlyUltimoDistribuito = false;
			if (getAuthUser().getCodFiegDl().equals(IGerivConstants.CDL_CODE)) {
				GrantedAuthority auth = new GrantedAuthorityImpl(IGerivConstants.ROLE_IGERIV_BASE);
				if (getAuthUser().getAuthorities().contains(auth)) {
					showOnlyUltimoDistribuito = true;
				}
			}
			List statistiche = pubblicazioniService.getCopertine(false, true, false, getAuthUser().getCodEdicolaMaster(), getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), null, null, null, periodicita, null, copertina.getCodicePubblicazione(), null, showOnlyUltimoDistribuito, dataStorico, null, false, null, codEdicola);
			requestMap.put("statistica", statistiche);
			if (!prenotazioneVo.isEnabled()) {
				prenotazioneDisabled = "true";
			}
		}
		return IGerivConstants.ACTION_RICHIESTE_VARIAZIONI;
	}
	
	@SkipValidation
	public String showVariazioniInseriteFilter() {
		filterTitle = getText("igeriv.bolla.visualizza.variazioni.inserite");
		return SUCCESS;
	}
	
	public String showVariazioniInserite() throws ParseException {
		filterTitle = getText("igeriv.bolla.visualizza.variazioni.inserite");
		Timestamp dataDa = null;
		Timestamp dataA = null;
		if (this.dataDa != null) {
			dataDa = DateUtilities.floorDay(DateUtilities.parseDate(this.dataDa, DateUtilities.FORMATO_DATA_SLASH));
		}
		if (this.dataA != null) {
			dataA = DateUtilities.ceilDay(DateUtilities.parseDate(this.dataA, DateUtilities.FORMATO_DATA_SLASH));
		}
		Integer codUtente = null;
		if (getAuthUser().getCodFiegDl().equals(IGerivConstants.CDL_CODE)) {
			GrantedAuthority auth = new GrantedAuthorityImpl(IGerivConstants.ROLE_IGERIV_BASE);
			if (getAuthUser().getAuthorities().contains(auth)) {
				codUtente = new Integer(getAuthUser().getCodUtente());
			}
		} 
		variazioni = rifornimentiService.getRichiesteVariazioni(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), titolo, (stato != null && !stato.equals("")) ? new Integer(stato) : null, dataDa, dataA, codUtente);
		requestMap.put("variazioni", variazioni);
		requestMap.put("filterTitle", filterTitle);
		return SUCCESS;
	}
	
	@SkipValidation
	public String showRichiesteRifornimentiEdit() throws Exception {
		showRichiesteRifornimenti();
		return IGerivConstants.ACTION_RICHIESTE_RIFORNIMENTI_EDIT;
	}
	
	@SkipValidation
	public String editRichiesteRifornimenti() throws Exception {
		filterTitle = getText("filterTitle"); 
		showRichiesteRifornimenti();
		return IGerivConstants.ACTION_RICHIESTE_RIFORNIMENTI_EDIT;
	}
	
	public String save() {
		try {
			StringTokenizer st = new StringTokenizer(dataTipoBolla, "|");
			String strData = st.nextToken();
			String tipo = st.nextToken().replaceFirst(IGerivConstants.TIPO, "").trim();
			Timestamp dtBolla = DateUtilities.parseDate(strData, DateUtilities.FORMATO_DATA);
			BollaRiassuntoVo bollaRiassunto = bolleService.getBollaRiassunto(getAuthUser().getCodFiegDl(), getAuthUser().getId(), dtBolla, tipo);
			//if (bollaRiassunto.getBollaTrasmessaDl().intValue() >= IGerivConstants.INDICATORE_RECORD_TRASMESSO_DL) {
			if (bollaRiassunto.getBollaTrasmessaDl().intValue() >= IGerivConstants.INDICATORE_RECORD_IN_TRASMISSIONE_DL) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("impossibile.salvare.bolla.gia.trasmessa") + IGerivConstants.END_EXCEPTION_TAG);
	    		throw new IGerivRuntimeException();
	    	}
			Map<String, String> fields = new HashMap<String, String>();
			Map<String, String> spunte = new HashMap<String, String>();
			Map<String, String> fieldsFB = new HashMap<String, String>();
			Map<String, String> spunteFB = new HashMap<String, String>();
			buildModifiedMaps(fields, spunte, fieldsFB, spunteFB);
			bolleService.saveBollaRivendita(getAuthUser().getId(), fields, spunte, fieldsFB, spunteFB);
		} catch (IGerivRuntimeException e) {
			throw e;
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "save";
	}

	public String send() {
		try {
			StringTokenizer st = new StringTokenizer(dataTipoBolla, "|");
			String strData = st.nextToken();
			String tipo = st.nextToken().replaceFirst(IGerivConstants.TIPO, "").trim();
			Timestamp dtBolla = DateUtilities.parseDate(strData, DateUtilities.FORMATO_DATA);
			BollaRiassuntoVo bollaRiassunto = bolleService.getBollaRiassunto(getAuthUser().getCodFiegDl(), getAuthUser().getId(), dtBolla, tipo);
			//if (bollaRiassunto.getBollaTrasmessaDl().intValue() >= IGerivConstants.INDICATORE_RECORD_TRASMESSO_DL) {
			if (bollaRiassunto.getBollaTrasmessaDl().intValue() >= IGerivConstants.INDICATORE_RECORD_IN_TRASMISSIONE_DL) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("impossibile.salvare.bolla.gia.trasmessa") + IGerivConstants.END_EXCEPTION_TAG);
	    		throw new IGerivRuntimeException();
	    	}
			Map<String, String> fields = new HashMap<String, String>();
			Map<String, String> spunte = new HashMap<String, String>();
			Map<String, String> fieldsFB = new HashMap<String, String>();
			Map<String, String> spunteFB = new HashMap<String, String>();
			buildModifiedMaps(fields, spunte, fieldsFB, spunteFB);
			bolleService.saveAndSendBollaRivendita(getAuthUser().getId(), fields, spunte, fieldsFB, spunteFB, bollaRiassunto);
		} catch (IGerivRuntimeException e) {
			throw e;
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "save";
	}
	
	/**
	 * Rimepie le mappe con i valori modificati di differenze bolla e spunte
	 * 
	 * @param fields
	 * @param spunte
	 * @param fieldsFB
	 * @param spunteFB
	 */
	private void buildModifiedMaps(Map<String, String> fields, Map<String, String> spunte, Map<String, String> fieldsFB, Map<String, String> spunteFB) {
		if (modificatoMap != null && !modificatoMap.isEmpty()) {
			for (Map.Entry<String, String> entry : modificatoMap.entrySet()) {
				String pk = entry.getKey();
				Boolean modified = new Boolean(entry.getValue());
				if (modified != null && modified) {
					fields.put(pk, fieldMap.get(pk));
					spunte.put(pk, spuntaMap.get(pk));
				}
			}
		}
		if (modificatoFBMap != null && !modificatoFBMap.isEmpty()) {
			for (Map.Entry<String, String> entry : modificatoFBMap.entrySet()) {
				String pk = entry.getKey();
				Boolean modified = new Boolean(entry.getValue());
				if (modified != null && modified) {
					fieldsFB.put(pk, fieldFBMap.get(pk));
					spunteFB.put(pk, spuntaFBMap.get(pk));
				}
			}
		}
	}
	
	/**
	 * Costruisce le richieste di rifornimenti agguntive per un pubblicazione già richiesta nello stesso giorno.
	 * 
	 * @param buildSet
	 * @param dataScadenzaRichiestaAggiunte
	 * @param quantitaRifornimentoAggiunt2
	 * @param noteVenditaAggiunte
	 * @return
	 * @throws ParseException
	 */
	private List<RichiestaRifornimentoVo> buildListRichiestaRifornimentoVoAggiunte(Set<String> buildSet, String dataScadenzaRichiestaAggiunte, String quantitaRifornimentoAggiunte, String noteVenditaAggiunte) throws ParseException {
		List<RichiestaRifornimentoVo> retList = new ArrayList<RichiestaRifornimentoVo>();
		if (buildSet != null && buildSet.size() > 0) {
			List<String> listPks = new ArrayList<String>(buildSet);
			if (listPks.size() > 0 && dataScadenzaRichiestaAggiunte != null && quantitaRifornimentoAggiunte != null && noteVenditaAggiunte != null) {
				String[] arrDataRichiesta = dataScadenzaRichiestaAggiunte.split(",");
				String[] arrQtaRichiesta = quantitaRifornimentoAggiunte.split(",");
				String[] arrNote = noteVenditaAggiunte.split(",");
				for (int i = 0; i < listPks.size(); i++) {
					String[] pkArrVal = listPks.get(i).toString().split("\\|");
					RichiestaRifornimentoVo vo = new RichiestaRifornimentoVo();
					RichiestaRifornimentoPk pk = new RichiestaRifornimentoPk();
					pk.setCodEdicola(new Integer(pkArrVal[1]));
					pk.setCodFiegDl(new Integer(pkArrVal[0]));
					pk.setDataOrdine(DateUtilities.parseDate(pkArrVal[2].trim(), DateUtilities.FORMATO_DATA_YYYY_MM_DD_HHMMSS));
					pk.setIdtn(new Integer(pkArrVal[3]));
					vo.setPk(pk);
					Integer days = (arrDataRichiesta[i] != null && !arrDataRichiesta[i].trim().equals("")) ? new Integer(arrDataRichiesta[i].trim()) : 0;
					Timestamp dtScadenza = buildDataScadenzaFromDays(days);
					vo.setDataScadenzaRichiesta(dtScadenza);
					vo.setGiorniValiditaRichiesteRifornimento(days);
					Integer quantitaRichiesta = new Integer(arrQtaRichiesta[i].trim().replaceAll("\\+", ""));
					if (quantitaRichiesta < 0) {
						requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.msg.qta.richiesta.negativa") + IGerivConstants.END_EXCEPTION_TAG);
						throw new IGerivRuntimeException();
					}
					vo.setQuantitaRichiesta(quantitaRichiesta);
					vo.setNoteVendita(arrNote[i].trim());
					retList.add(vo);
				}
			}
		}
		return retList;
	}

	public String saveVariazioni() throws Exception {
		if (prenotazioneVo != null) {
			prenotazioneVo.setIndicatoreTrasmessoDl(IGerivConstants.INDICATORE_RECORD_NON_TRASMESSO_DL);
			Integer quantitaVariazioneServ = (quantitaVariazioneServizio != null && !quantitaVariazioneServizio.trim().equals("")) ? new Integer(quantitaVariazioneServizio.trim().replaceAll("\\+", "")) : null;
			if (quantitaVariazioneServ == null) {
				rifornimentiService.deleteVo(prenotazioneVo);
			} else {
				prenotazioneVo.setCodUtente(getAuthUser().getId());
				if (getAuthUser().getCodFiegDl().equals(IGerivConstants.CDL_CODE)) {
					GrantedAuthority auth = new GrantedAuthorityImpl(IGerivConstants.ROLE_IGERIV_BASE);
					if (getAuthUser().getAuthorities().contains(auth)) {
						prenotazioneVo.setCodUtente(new Integer(getAuthUser().getCodUtente()));
					}
				}
				if (quantitaVariazioneServ < 0) {
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.msg.qta.var.servizio.negativa") + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				}
				prenotazioneVo.setQuantitaRichiesta(quantitaVariazioneServ);
				rifornimentiService.deleteVo(prenotazioneVo);
				prenotazioneVo.getPk().setDataRichiesta(new java.sql.Date(new Date().getTime()));
				rifornimentiService.saveBaseVo(prenotazioneVo);
			}
		}
		return IGerivConstants.ACTION_RICHIESTE_VARIAZIONI;
	}
	
	public String savePrenotazioniClienteEdicola() {
		try {
			Set<String> pkSet = buildSet(pk);
			List<RichiestaClienteVo> richiestaRifornimento = rifornimentiService.getRichiesteClienteByPk(getAuthUser().getArrCodFiegDl(), 
					getAuthUser().getArrId(), 
					new Long(idCliente),
					IGerivConstants.COD_PROVENIENZA_RICHIESTA_EDICOLA,
					pkSet);
			List<RichiestaClienteVo> listRichiestaRifornimentoVo = buildListRichiestaClienteVo(richiestaRifornimento, pkSet, quantitaRifornimento);
			if (richiestaFissaClienteEdicolaVo.getQuantitaRichiesta() != null && richiestaFissaClienteEdicolaVo.getQuantitaRichiesta() < 0) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.msg.qta.richiesta.fissa.negativa") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
			rifornimentiService.saveRichiestaRifornimentoClienteEdicola(listRichiestaRifornimentoVo, richiestaFissaClienteEdicolaVo);
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return IGerivConstants.ACTION_PRENOTAZIONE_CLIENTI;
	}
	
	@SkipValidation
	public String showOrdini() throws Exception {
		if (idtn != null && !idtn.equals("")) {
			List<RichiestaClienteDto> ordini = rifornimentiService.getRichiesteClienteByIdtn(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), new Integer(idtn), null);
			requestMap.put("ordini", ordini);
		}
		requestMap.put("title", getText("igeriv.prenotazioni.clienti") + (richiestaFissaClienteEdicolaVo != null && richiestaFissaClienteEdicolaVo.getTitolo() != null ?  " : " + richiestaFissaClienteEdicolaVo.getTitolo() : ""));
		return IGerivConstants.ACTION_ORDINI;
	}
	
	@SkipValidation
	public String showPrenotazioniClienteEdicola() throws Exception {
		if (!Strings.isNullOrEmpty(idtn) || !Strings.isNullOrEmpty(idCliente)) {
			Integer coddl = Strings.isNullOrEmpty(this.coddl) ? getAuthUser().getCodFiegDl() : new Integer(this.coddl);
			Integer codEdicola = Strings.isNullOrEmpty(this.coddl) ? getAuthUser().getId() : iGerivUtils.getCorrispondenzaCodEdicolaMultiDl(coddl, getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId());
			if (!Strings.isNullOrEmpty(idtn) && !Strings.isNullOrEmpty(idCliente)) {
				List<RichiestaRifornimentoDto> ordini = rifornimentiService.getRichiesteCliente(coddl, getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), new Integer(idtn), new Long(idCliente), IGerivConstants.PROVENIENZA_EDICOLA_PER_CONTO_CLIENTE);
				requestMap.put("ordini", ordini);
			}
			if (requestMap.get("prenotazioneVo") == null && cpu != null) {
				Integer codicePub = new Integer(cpu);
				if (!Strings.isNullOrEmpty(idCliente)) {
					richiestaFissaClienteEdicolaVo = rifornimentiService.getRichiestaFissaClienteVo(coddl, codEdicola, getAuthUser().getArrId(), new Long(idCliente), getAuthUser().getArrCodFiegDl(), codicePub);
					if (!richiestaFissaClienteEdicolaVo.isEnabled()) {
						prenotazioneDisabled = "true";
					}
				}
			}
		}
		requestMap.put("title", getText("igeriv.prenotazioni.clienti") + " : " + (richiestaFissaClienteEdicolaVo != null && richiestaFissaClienteEdicolaVo.getTitolo() != null ? richiestaFissaClienteEdicolaVo.getTitolo() : ""));
		return IGerivConstants.ACTION_PRENOTAZIONE_CLIENTI;
	}
	
	@SkipValidation
	public String viewPrenotazioniClientiEdicola() throws Exception { 
		if (nomeCliente != null) {
			filterTitle = getText("igeriv.visualizza.prenotazioni.cliente") + " <b>" + nomeCliente.toUpperCase() + "</b>";
			requestMap.put("tipoRifornimento", "clienteEdicola");
			if (idCliente != null && !idCliente.equals("")) {
				List listRichieste = new ArrayList();
				if (stato != null && !stato.trim().equals("") ) {
					Integer statoInt = new Integer(stato); 
					if (statoInt.equals(IGerivConstants.PRENOTAZIONI_EVASE) || statoInt.equals(IGerivConstants.PRENOTAZIONI_PARZIALMENTE_EVASE) || statoInt.equals(IGerivConstants.PRENOTAZIONI_INSERITE)) {
						listRichieste = rifornimentiService.getRichiesteClienteByIdClienteViewOnly(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), Arrays.asList(new Long[]{new Long(idCliente)}), titolo, stato, null, null, null, false);
					} 
					else if (statoInt.equals(IGerivConstants.PRENOTAZIONI_FISSE)) {
						listRichieste = rifornimentiService.getRichiesteClienteFisseByIdCliente(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), new Long(idCliente), titolo, stato, null, null, false);
					} 
				} else {
					listRichieste = rifornimentiService.getRichiesteClienteByIdClienteViewOnly(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), Arrays.asList(new Long[]{new Long(idCliente)}), titolo, stato, null, null, null, false);
					listRichieste.addAll(rifornimentiService.getRichiesteClienteFisseByIdCliente(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), new Long(idCliente), titolo, stato, null, null, false));
				}
				requestMap.put("richiesteRifornimento", listRichieste);
			}
			String titoloRichiesteRifornimento = getText("igeriv.bolla.rifornimenti") + " del " + DateUtilities.getTimestampAsString(new Date(), DateUtilities.FORMATO_DATA_SLASH);
			String cliente	= getText("igeriv.provenienza.evasione.cliente") + " " + nomeCliente.toUpperCase().replaceAll("&nbsp;", " ").replaceAll("\\.", " ");
			requestMap.put("titoloRichiesteRifornimento", titoloRichiesteRifornimento);
			requestMap.put("cliente", cliente);
		}
		return IGerivConstants.ACTION_VIEW_PRENOTAZIONI_INSERITE_CLIENTI_EDICOLA;
	}
	
	/**
	 * Metodo chiamato via ajax dalla pagina di visualizzaizone delle prenotazioni 
	 * 
	 * @return
	 * @throws ParseException
	 */
	@SkipValidation
	public String deletePrenotazioniClientiEdicola() throws ParseException {
		if (!Strings.isNullOrEmpty(spunteRichiestaFissa)) {
			String[] split = spunteRichiestaFissa.split(",");
			for (String pk : split) {
				String[] pkSplit = pk.split("\\|");
				Integer codEdicola = new Integer(pkSplit[0]); 
				Long codCliente = new Long(pkSplit[1]); 
				Integer codDl = new Integer(pkSplit[2]);
				Integer codicePubblicazione = new Integer(pkSplit[3]);
				RichiestaFissaClienteEdicolaVo vo = rifornimentiService.getRichiestaFissaClienteEdicolaVo(codEdicola, codCliente, codDl, codicePubblicazione);
				rifornimentiService.deleteVo(vo);
			}
		}
		if (!Strings.isNullOrEmpty(spunteRichiesta)) {
			String[] split = spunteRichiesta.split(",");
			for (String pk : split) {
				String[] pkSplit = pk.split("\\|");
				Integer codEdicola = new Integer(pkSplit[0]); 
				Long codCliente = new Long(pkSplit[1]); 
				Integer provenienza = new Integer(pkSplit[2]);
				Timestamp dataInserimento = DateUtilities.parseDate(pkSplit[3].replaceAll("\\.0", ""), DateUtilities.FORMATO_DATA_YYYY_MM_DD_HHMMSS);
				Integer codDl = new Integer(pkSplit[4]);
				Integer idtnR = new Integer(pkSplit[5]);
				RichiestaClienteVo vo = rifornimentiService.getRichiestaClienteVo(codEdicola, codCliente, provenienza, dataInserimento, codDl, idtnR);
				rifornimentiService.deleteVo(vo);
			}
		}
		return "save";
	}
	
	/** 
	 * Ritorna la lista di RichiestaRifornimentoVo caricata dal db 
	 * con gli attributi introdotti dall'utente via form.
	 * 
	 * @param List<RichiestaRifornimentoVo> richiestaRifornimento
	 * @param Set<String> pk
	 * @param String dataScadenzaRichiesta
	 * @param String quantitaRifornimento
	 * @param String noteVendita
	 * @return List<RichiestaRifornimentoVo>
	 * @throws ParseException
	 */
	private List<RichiestaRifornimentoVo> buildListRichiestaRifornimentoVo(
			List<RichiestaRifornimentoVo> richiestaRifornimento, Set<String> pkSet, String dataScadenzaRichiesta,
			String quantitaRifornimento, String noteVendita) throws ParseException {
		Set<RichiestaRifornimentoVo> retSet = new HashSet<RichiestaRifornimentoVo>(); 
		if (pkSet != null && pkSet.size() > 0) {
			String[] arrDataRichiesta = dataScadenzaRichiesta.split(",");
			String[] arrQtaRichiesta = quantitaRifornimento.split(",");
			String[] arrNote = noteVendita.split(",");
			List<String> listPks = new ArrayList<String>(pkSet);
			if (richiestaRifornimento != null && !richiestaRifornimento.isEmpty()) {
				for (RichiestaRifornimentoVo vo : richiestaRifornimento) {
					String pkVal = vo.getPk().toString().trim();
					for (int i = 0; i < arrDataRichiesta.length; i++) {
						if (pkVal.equals(listPks.get(i).toString())) {
							Integer quantitaRichiesta = (arrQtaRichiesta[i] != null && !arrQtaRichiesta[i].trim().equals("")) ? new Integer(arrQtaRichiesta[i].trim().replaceAll("\\+", "")) : 0;
							Integer days = (arrDataRichiesta[i] != null && !arrDataRichiesta[i].trim().equals("")) ? new Integer(arrDataRichiesta[i].trim()) : 0;
							Timestamp dtScadenza = buildDataScadenzaFromDays(days);
							vo.setDataScadenzaRichiesta(dtScadenza);
							vo.setGiorniValiditaRichiesteRifornimento(days);
							if (quantitaRichiesta < 0) {
								requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.msg.qta.richiesta.negativa") + IGerivConstants.END_EXCEPTION_TAG);
								throw new IGerivRuntimeException();
							}
							vo.setQuantitaRichiesta(quantitaRichiesta);
							vo.setNoteVendita((arrNote[i] != null) ? arrNote[i].trim() : "");
							retSet.add(vo);
							break;
						}
					} 
				}
			} 
			for (int i = 0; i < arrQtaRichiesta.length; i++) {
				if (arrQtaRichiesta[i] != null && !arrQtaRichiesta[i].trim().equals("")) {
					String[] pkArrVal = listPks.get(i).toString().split("\\|");
					RichiestaRifornimentoVo vo = new RichiestaRifornimentoVo();
					RichiestaRifornimentoPk pk = new RichiestaRifornimentoPk();
					pk.setCodEdicola(new Integer(pkArrVal[1]));
					pk.setCodFiegDl(new Integer(pkArrVal[0]));
					pk.setDataOrdine(DateUtilities.parseDate(pkArrVal[2], DateUtilities.FORMATO_DATA_YYYY_MM_DD_HHMMSS));
					pk.setIdtn(new Integer(pkArrVal[3]));
					vo.setPk(pk);
					Integer days = (arrDataRichiesta[i] != null && !arrDataRichiesta[i].trim().equals("")) ? new Integer(arrDataRichiesta[i].trim()) : 0;
					Timestamp dtScadenza = buildDataScadenzaFromDays(days);
					vo.setDataScadenzaRichiesta(dtScadenza);
					vo.setGiorniValiditaRichiesteRifornimento(days);
					Integer quantitaRichiesta = new Integer(arrQtaRichiesta[i].trim().replaceAll("\\+", ""));
					if (quantitaRichiesta < 0) {
						requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.msg.qta.richiesta.negativa") + IGerivConstants.END_EXCEPTION_TAG);
						throw new IGerivRuntimeException();
					}
					vo.setQuantitaRichiesta(quantitaRichiesta);
					vo.setNoteVendita(arrNote[i].trim());
					retSet.add(vo);
				}
			}
		}
		return new ArrayList<RichiestaRifornimentoVo>(retSet);
	}
	
	private Timestamp buildDataScadenzaFromDays(Integer days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, days);
		return new Timestamp(cal.getTime().getTime());
	}

	/** 
	 * Ritorna la lista di RichiestaClienteVo caricata dal db 
	 * con gli attributi introdotti dall'utente via form.
	 * 
	 * @param List<RichiestaRifornimentoVo> richiestaRifornimento
	 * @param Set pk
	 * @param String quantitaRifornimento
	 * @return List<RichiestaRifornimentoVo>
	 * @throws ParseException
	 */
	private List<RichiestaClienteVo> buildListRichiestaClienteVo(
			List<RichiestaClienteVo> richiestaRifornimento, Set<String> pkSet, String quantitaRifornimento) throws ParseException {
		Set<RichiestaClienteVo> set = new HashSet<RichiestaClienteVo>(); 
		String[] arrQtaRichiesta = quantitaRifornimento.split(",");
		List<String> listPks = new ArrayList<String>();
		listPks.addAll(pkSet);
		for (RichiestaClienteVo vo : richiestaRifornimento) {
			String pkVal = vo.getPk().toString().trim();
			for (int i = 0; i < arrQtaRichiesta.length; i++) {
				if (pkVal.equals(listPks.get(i).toString())) {
					Integer quantitaRichiesta = (arrQtaRichiesta[i] != null && !arrQtaRichiesta[i].trim().equals("")) ? new Integer(arrQtaRichiesta[i].trim().replaceAll("\\+", "")) : 0;
					if (quantitaRichiesta < 0) {
						requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.msg.qta.richiesta.negativa") + IGerivConstants.END_EXCEPTION_TAG);
						throw new IGerivRuntimeException();
					}
					vo.setQuantitaRichiesta(quantitaRichiesta);
					set.add(vo);
					break;
				}
			}
		}
		for (int i = 0; i < arrQtaRichiesta.length; i++) {
			if (arrQtaRichiesta[i] != null && !arrQtaRichiesta[i].trim().equals("")) {
				String[] pkArrVal = listPks.get(i).toString().split("\\|");
				RichiestaClienteVo vo = new RichiestaClienteVo();
				RichiestaClientePk pk = new RichiestaClientePk();
				pk.setCodDl(new Integer(pkArrVal[4]));
				pk.setCodEdicola(new Integer(pkArrVal[0]));
				pk.setCodCliente(new Long(idCliente));
				pk.setDataInserimento(DateUtilities.parseDate(pkArrVal[3], DateUtilities.FORMATO_DATA_YYYY_MM_DD_HHMMSS));
				pk.setIdtn(new Integer(pkArrVal[5]));
				pk.setProvenienza(new Integer(pkArrVal[2]));
				vo.setPk(pk);
				Integer quantitaRichiesta = new Integer(arrQtaRichiesta[i].trim().replaceAll("\\+", ""));
				if (quantitaRichiesta < 0) {
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.msg.qta.richiesta.negativa") + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				}
				vo.setQuantitaRichiesta(quantitaRichiesta);
				vo.setQuantitaEvasa(0);
				vo.setStatoEvasione(IGerivConstants.COD_STATO_EVASIONE_INSERITO);
				vo.setRichiedereDifferenzaDl(IGerivConstants.COD_RICHIEDERE_DIFFERENZA_DL_NO);
				set.add(vo);
			}
		}
		return new ArrayList<RichiestaClienteVo>(set);
	}

	private void doShowFilter() {
		bolleRiassunto = bolleService.getBolleRiassunto(new Integer[]{getAuthUser().getCodFiegDl()}, new Integer[]{getAuthUser().getId()}, null);
		List<KeyValueDto> listSoloRighaDaSpuntare = buildMapSoloRighaDaSpuntare();
		List<DateTipiBollaDto> listDateTipiBolla = new ArrayList<DateTipiBollaDto>();
		int count = 0;
		listDateTipiBolla = new ArrayList<DateTipiBollaDto>();
		for (BollaRiassuntoVo vo : bolleRiassunto) {
			DateTipiBollaDto dto = new DateTipiBollaDto();
			BollaRiassuntoVo brvo = (BollaRiassuntoVo) vo;
			dto.setIdDataTipoBolla(count);
			dto.setDataBollaFormat(DateUtilities.getTimestampAsString(brvo.getPk().getDtBolla(), DateUtilities.FORMATO_DATA));
			dto.setTipoBolla(IGerivConstants.TIPO + " " + brvo.getPk().getTipoBolla());
			//dto.setReadonly((brvo.getBollaTrasmessaDl() != null && brvo.getBollaTrasmessaDl().intValue() >= IGerivConstants.INDICATORE_RECORD_TRASMESSO_DL) ? false : true);
			dto.setReadonly((brvo.getBollaTrasmessaDl() != null && brvo.getBollaTrasmessaDl().intValue() >= IGerivConstants.INDICATORE_RECORD_IN_TRASMISSIONE_DL) ? false : true);
			dto.setBollaTrasmessaDl(brvo.getBollaTrasmessaDl());
			listDateTipiBolla.add(dto);
			count++;
		}
		requestMap.put("listDateTipiBolla", listDateTipiBolla);
		requestMap.put("listSoloRighaDaSpuntare", listSoloRighaDaSpuntare);
		if (tipoOperazione == null || tipoOperazione.equals("")) {
			tipoOperazione = "1";
		}
	}
	
	public List<BollaRiassuntoVo> getBolleRiassunto() {
		return bolleRiassunto;
	}

	public void setBolleRiassunto(List<BollaRiassuntoVo> bolleRiassunto) {
		this.bolleRiassunto = bolleRiassunto;
	}

	public String getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public String getSoloRigheSpuntare() {
		return soloRigheSpuntare;
	}

	public void setSoloRigheSpuntare(String soloRigheSpuntare) {
		this.soloRigheSpuntare = soloRigheSpuntare;
	}
	
	public List<T> getItensBolla() {
		return itensBolla;
	}

	public void setItensBolla(List<T> itensBolla) {
		this.itensBolla = itensBolla;
	}
	
	public List<RichiestaRifornimentoDto> getRichiesteRifornimento() {
		return richiesteRifornimento;
	}

	public void setRichiesteRifornimento(
			List<RichiestaRifornimentoDto> richiesteRifornimento) {
		this.richiesteRifornimento = richiesteRifornimento;
	}

	public String getDataTipoBolla() {
		return dataTipoBolla;
	}

	public void setDataTipoBolla(String dataTipoBolla) {
		this.dataTipoBolla = dataTipoBolla;
	}
	
	/* (non-Javadoc)
	 * @see org.extremecomponents.table.state.State#getParameters(org.extremecomponents.table.context.Context, java.lang.String, java.lang.String)
	 */
	public Map getParameters(Context arg0, String arg1, String arg2) {
		return arg0.getParameterMap();
	}

	/* (non-Javadoc)
	 * @see org.extremecomponents.table.state.State#saveParameters(org.extremecomponents.table.context.Context, java.lang.String, java.util.Map)
	 */
	public void saveParameters(Context context, String arg1, Map arg2) {
	}
	
	@Override
	public String getTitle() {
		String title = getText("igeriv.bolla.consegna");
		if (!Strings.isNullOrEmpty(getActionName())) {
			if (getActionName().contains("sonoInoltreUscite_")) {
				title = getText("igeriv.sono.inoltre.uscite");
			} else if (getActionName().contains("viewRifornimenti_")) {
				title = getText("igeriv.bolla.visualizza.rifornimenti");
			} else if (getActionName().contains("viewVariazioni_")) {
				title = getText("igeriv.menu.61");
			}
		}
		return super.getTitle() + title; 
	}
	
	public String getIdtn() {
		return idtn;
	}

	public void setIdtn(String idtn) {
		this.idtn = idtn;
	}
	
	public String getCoddl() {
		return coddl;
	}

	public void setCoddl(String coddl) {
		this.coddl = coddl;
	}

	public String getPeriodicita() {
		return periodicita;
	}

	public void setPeriodicita(String periodicita) {
		this.periodicita = periodicita;
	}

	public String getFilterTitle() {
		return filterTitle;
	}

	public void setFilterTitle(String filterTitle) {
		this.filterTitle = filterTitle;
	}
	
	public String getTableTitle() {
		return tableTitle;
	}

	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	public String getActionName1() {
		return actionName1;
	}

	public void setActionName1(String actionName1) {
		this.actionName1 = actionName1;
	}

	public Integer getTableHeight() {
		return tableHeight;
	}

	public void setTableHeight(Integer tableHeight) {
		this.tableHeight = tableHeight;
	}
	
	public PrenotazioneVo getPrenotazioneVo() {
		return prenotazioneVo;
	}

	public void setPrenotazioneVo(PrenotazioneVo prenotazioneVo) {
		this.prenotazioneVo = prenotazioneVo;
	}
	
	public RichiestaFissaClienteEdicolaVo getRichiestaFissaClienteEdicolaVo() {
		return richiestaFissaClienteEdicolaVo;
	}

	public void setRichiestaFissaClienteEdicolaVo(RichiestaFissaClienteEdicolaVo richiestaFissaClienteEdicolaVo) {
		this.richiestaFissaClienteEdicolaVo = richiestaFissaClienteEdicolaVo;
	}

	public String getPrenotazioneDisabled() {
		return prenotazioneDisabled;
	}

	public void setPrenotazioneDisabled(String prenotazioneDisabled) {
		this.prenotazioneDisabled = prenotazioneDisabled;
	}
	
	public boolean isDisableForm() {
		return disableForm;
	}

	public void setDisableForm(boolean disableForm) {
		this.disableForm = disableForm;
	}
	
	public String getDataScadenzaRichiesta() {
		return dataScadenzaRichiesta;
	}

	public void setDataScadenzaRichiesta(String dataScadenzaRichiesta) {
		this.dataScadenzaRichiesta = dataScadenzaRichiesta;
	}

	public String getQuantitaRifornimento() {
		return quantitaRifornimento;
	}

	public void setQuantitaRifornimento(String quantitaRifornimento) {
		this.quantitaRifornimento = quantitaRifornimento;
	}

	public String getNoteVendita() {
		return noteVendita;
	}

	public void setNoteVendita(String noteVendita) {
		this.noteVendita = noteVendita;
	}
	
	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	
	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}
	
	public String getImportoLordo() {
		return importoLordo;
	}

	public void setImportoLordo(String importoLordo) {
		this.importoLordo = importoLordo;
	}
	
	public String getNomeCliente() {
		return (nomeCliente != null) ? nomeCliente.toUpperCase() : nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}
	
	public String getDownloadToken() {
		return downloadToken;
	}

	public void setDownloadToken(String downloadToken) {
		this.downloadToken = downloadToken;
	}
	
	public String getDataDa() {
		return dataDa;
	}

	public void setDataDa(String dataDa) {
		this.dataDa = dataDa;
	}

	public String getDataA() {
		return dataA;
	}

	public void setDataA(String dataA) {
		this.dataA = dataA;
	}

	protected List<KeyValueDto> buildMapSoloRighaDaSpuntare() {
		List<KeyValueDto> listSoloRigheDaSpuntare = new ArrayList<KeyValueDto>();
		KeyValueDto dto1 = new KeyValueDto();
		dto1.setKey("1");
		dto1.setValue(getText("igeriv.si"));
		KeyValueDto dto2 = new KeyValueDto();
		dto2.setKey("2");
		dto2.setValue(getText("igeriv.no"));
		listSoloRigheDaSpuntare.add(dto2);
		listSoloRigheDaSpuntare.add(dto1);
		return listSoloRigheDaSpuntare;
	}

	public String getBollaControlloTab_ev() {
		return BollaControlloTab_ev;
	}

	public void setBollaControlloTab_ev(String bollaControlloTab_ev) {
		BollaControlloTab_ev = bollaControlloTab_ev;
	}

	public String getQuantitaVariazioneServizio() {
		return quantitaVariazioneServizio;
	}

	public void setQuantitaVariazioneServizio(String quantitaVariazioneServizio) {
		this.quantitaVariazioneServizio = quantitaVariazioneServizio;
	}

	public String getDataScadenzaRichiestaAggiunte() {
		return dataScadenzaRichiestaAggiunte;
	}

	public void setDataScadenzaRichiestaAggiunte(String dataScadenzaRichiestaAggiunte) {
		this.dataScadenzaRichiestaAggiunte = dataScadenzaRichiestaAggiunte;
	}

	public String getQuantitaRifornimentoAggiunte() {
		return quantitaRifornimentoAggiunte;
	}

	public void setQuantitaRifornimentoAggiunte(String quantitaRifornimentoAggiunte) {
		this.quantitaRifornimentoAggiunte = quantitaRifornimentoAggiunte;
	}

	public String getNoteVenditaAggiunte() {
		return noteVenditaAggiunte;
	}

	public void setNoteVenditaAggiunte(String noteVenditaAggiunte) {
		this.noteVenditaAggiunte = noteVenditaAggiunte;
	}

	public String getPkAggiunte() {
		return pkAggiunte;
	}

	public void setPkAggiunte(String pkAggiunte) {
		this.pkAggiunte = pkAggiunte;
	}
	
	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getPkSel() {
		return pkSel;
	}

	public void setPkSel(String pkSel) {
		this.pkSel = pkSel;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public IGerivUtils getiGerivUtils() {
		return iGerivUtils;
	}

	public void setiGerivUtils(IGerivUtils iGerivUtils) {
		this.iGerivUtils = iGerivUtils;
	}

	public String getSpunteRichiestaFissa() {
		return spunteRichiestaFissa;
	}

	public void setSpunteRichiestaFissa(String spunteRichiestaFissa) {
		this.spunteRichiestaFissa = spunteRichiestaFissa;
	}

	public String getSpunteRichiesta() {
		return spunteRichiesta;
	}

	public void setSpunteRichiesta(String spunteRichiesta) {
		this.spunteRichiesta = spunteRichiesta;
	}

	public Map<String, String> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map<String, String> fieldMap) {
		this.fieldMap = fieldMap;
	}

	public Map<String, String> getSpuntaMap() {
		return spuntaMap;
	}

	public void setSpuntaMap(Map<String, String> spuntaMap) {
		this.spuntaMap = spuntaMap;
	}

	public Map<String, String> getModificatoMap() {
		return modificatoMap;
	}

	public void setModificatoMap(Map<String, String> modificatoMap) {
		this.modificatoMap = modificatoMap;
	}

	public Map<String, String> getFieldFBMap() {
		return fieldFBMap;
	}

	public void setFieldFBMap(Map<String, String> fieldFBMap) {
		this.fieldFBMap = fieldFBMap;
	}

	public Map<String, String> getSpuntaFBMap() {
		return spuntaFBMap;
	}

	public void setSpuntaFBMap(Map<String, String> spuntaFBMap) {
		this.spuntaFBMap = spuntaFBMap;
	}

	public Map<String, String> getModificatoFBMap() {
		return modificatoFBMap;
	}

	public void setModificatoFBMap(Map<String, String> modificatoFBMap) {
		this.modificatoFBMap = modificatoFBMap;
	}

	public BolleService getBolleService() {
		return bolleService;
	}

	public void setBolleService(BolleService bolleService) {
		this.bolleService = bolleService;
	}

	public PubblicazioniService getPubblicazioniService() {
		return pubblicazioniService;
	}

	public void setPubblicazioniService(PubblicazioniService pubblicazioniService) {
		this.pubblicazioniService = pubblicazioniService;
	}

	public MessaggiService getMessaggiService() {
		return messaggiService;
	}

	public void setMessaggiService(MessaggiService messaggiService) {
		this.messaggiService = messaggiService;
	}

	public RifornimentiService getRifornimentiService() {
		return rifornimentiService;
	}

	public void setRifornimentiService(RifornimentiService rifornimentiService) {
		this.rifornimentiService = rifornimentiService;
	}
	
}
