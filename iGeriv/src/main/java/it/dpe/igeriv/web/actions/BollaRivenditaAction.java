package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.selectUnique;
import static ch.lambdaj.Lambda.sum;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import it.dpe.igeriv.bo.bolle.BolleService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.livellamenti.LivellamentiService;
import it.dpe.igeriv.bo.messaggi.MessaggiService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.bo.rifornimenti.RifornimentiService;
import it.dpe.igeriv.dto.BaseDto;
import it.dpe.igeriv.dto.BollaDettaglioDto;
import it.dpe.igeriv.dto.BollaVoDto;
import it.dpe.igeriv.dto.DateTipiBollaDto;
import it.dpe.igeriv.dto.FondoBollaDettaglioDto;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.dto.ParametriEdicolaDto;
import it.dpe.igeriv.dto.PrenotazioneDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.dto.PubblicazioneFornito;
import it.dpe.igeriv.dto.RichiestaClienteDto;
import it.dpe.igeriv.dto.RichiestaRifornimentoDto;
import it.dpe.igeriv.enums.StatoRichiestaLivellamento;
import it.dpe.igeriv.enums.StatoRichiestaRifornimentoLivellamento;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivUtils;
import it.dpe.igeriv.util.NumberUtils;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.AnagraficaPubblicazioniVo;
import it.dpe.igeriv.vo.BollaDettaglioVo;
import it.dpe.igeriv.vo.BollaRiassuntoVo;
import it.dpe.igeriv.vo.EdicoleVicineVo;
import it.dpe.igeriv.vo.LivellamentiVo;
import it.dpe.igeriv.vo.MessaggiBollaVo;
import it.dpe.igeriv.vo.PrenotazioneVo;
import it.dpe.igeriv.vo.RichiestaClienteVo;
import it.dpe.igeriv.vo.RichiestaFissaClienteEdicolaVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoLivellamentiVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoVo;
import it.dpe.igeriv.vo.StoricoCopertineVo;
import it.dpe.igeriv.vo.pk.BollaDettaglioPk;
import it.dpe.igeriv.vo.pk.RichiestaClientePk;
import it.dpe.igeriv.vo.pk.RichiestaRifornimentoPk;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per la bolla di consegna.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("bollaRivenditaAction")
@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
public class BollaRivenditaAction<T extends BaseDto> extends RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	private final BolleService bolleService;
	private final MessaggiService messaggiService;
	private final RifornimentiService rifornimentiService;
	private final PubblicazioniService pubblicazioniService;
	private final LivellamentiService livellamentiService;
	private final EdicoleService edicoleService;
	private final IGerivUtils iGerivUtils;
	private final String crumbName = getText("igeriv.bolla.consegna");
	private final String crumbNameSIU = getText("igeriv.sono.inoltre.uscite");
	private final String crumbNameViewRifo = getText("igeriv.bolla.visualizza.rifornimenti");
	private final String crumbNameViewVaria = getText("igeriv.bolla.inserisci.variazioni");
	private final String crumbNameViewVariaIns = getText("igeriv.bolla.visualizza.variazioni.inserite");
	private final String crumbNamePreNonEv = getText("igeriv.prenotazioni.non.evase");
	private final String crumbNameViewPreCli = getText("igeriv.visualizza.prenotazioni.cliente");
	private String tipoOperazione;
	private String soloRigheSpuntare;
	private List<BollaRiassuntoVo> bolleRiassunto;
	private String dataTipoBolla;
	private List itensBolla;
	private List<MessaggiBollaVo> messaggiBolla;
	private List<RichiestaRifornimentoDto> richiesteRifornimento;
	private List<RichiestaRifornimentoDto> dettaglioEsitorichiesteRifornimento;
	private List<Map<String,String>> resultList;
	private List<PrenotazioneDto> variazioni;
	private Integer quantita;
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
	private Map<String, String> testMap;
	private String pk;
	private String pkSel;
	private String quantitaRifornimento;
	private Map<String, String> giorniValiditaRichiesteRifornimentoMap;
	private Map<String, String> quantitaRifornimentoMap;
	private Map<String, String> noteVenditaMap;
	private String giorniValiditaRichiesteRifornimentoAggiunte;
	private String quantitaRifornimentoAggiunte;
	private String noteVenditaAggiunte;
	private String pkAggiunte;
	private String titolo;
	private String stato;
	private String idCliente;
	@Getter(AccessLevel.NONE)
	private String nomeCliente;
	private String importoLordo;
	private String downloadToken;
	private String dataDa;
	private String dataA;
	private String BollaControlloTab_ev;
	private String SonoInoltreUsciteTab_ev;
	private String cpu;
	private boolean isDlInforiv;
	private Timestamp dataStorico;
	private String spunteRichiestaFissa;
	private String spunteRichiesta;
	private Boolean hasPrenotazioniFisseNonEvase;
	private Boolean isQuotidiano;
	private Map<String, String> quantitaRichiestaMap;
	private Map<String, String> motivoRichiestaMap;
	private Boolean msgBollaLetti = false;
	private Boolean findUltimoIdtnPrenotazioniFisse = false;
	private Map<String, Object> mapResult;
	private Boolean modlitaInforiv;
	private List<String> chkLivellamenti;
	
	public BollaRivenditaAction() {
		this(null,null,null,null,null,null,null);
	}

	@Autowired
	public BollaRivenditaAction(BolleService bolleService, MessaggiService messaggiService, RifornimentiService rifornimentiService, PubblicazioniService pubblicazioniService, IGerivUtils iGerivUtils, LivellamentiService livellamentiService, EdicoleService edicoleService) {
		this.bolleService = bolleService;
		this.messaggiService = messaggiService;
		this.rifornimentiService = rifornimentiService;
		this.pubblicazioniService = pubblicazioniService;
		this.iGerivUtils = iGerivUtils;
		this.livellamentiService = livellamentiService;
		this.edicoleService = edicoleService;
	}

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
		//try {
			super.prepare();
			if (getAuthUser().getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_EDICOLA)) {
				requestMap.put("memorizza", getText("igeriv.memorizza"));
				requestMap.put("memorizzaCtrlM", getText("igeriv.memorizza.ctrl.m"));
				requestMap.put("memorizzaInvia", getText("igeriv.memorizza.invia"));
				requestMap.put("inserisciCopieFuoriCompetenza", getText("igeriv.inserisci.fuori.competenza"));
				requestMap.put("inserisciCopieFuoriCompetenzaTooltip", getText("igeriv.inserisci.fuori.competenza.tooltip"));
				requestMap.put("memorizzaInviaCtrlI", getText("igeriv.memorizza.invia.ctrl.i"));
				requestMap.put("esportaOrdini", getText("igeriv.esporta.ordini.clienti"));
				requestMap.put("esportaOrdiniDettaglio", getText("igeriv.esporta.ordini.clienti.dettaglio"));
				requestMap.put("time", DateUtilities.getTimestampAsString(new Date(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS));
				Map<String, ParametriEdicolaDto> mapParam = (Map<String, ParametriEdicolaDto>) sessionMap.get(IGerivConstants.SESSION_VAR_MAP_PARAMETRI_EDICOLA);
				boolean modalitaInforiv = mapParam.containsKey(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_ABILITA_MODALITA_INFORIV_BOLLE) ? Boolean.valueOf(mapParam.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_ABILITA_MODALITA_INFORIV_BOLLE).getParamValue()) : false;
				requestMap.put("modalitaInforiv", modalitaInforiv);
				isDlInforiv = getAuthUser().isDlInforiv();
				dataStorico = getAuthUser().getDataStorico();
				doShowFilter();
			}
		//} catch (Throwable e) {
		//	addActionError(getText("gp.errore.imprevisto"));
		//}
	}

	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showFilter() {
		filterTitle = getText("igeriv.bolla.consegna");
		return SUCCESS;
	}

	@BreadCrumb("%{crumbNameSIU}")
	@SkipValidation
	public String showFilterSonoInoltreUscite() {
		filterTitle = getText("igeriv.sono.inoltre.uscite");
		return SUCCESS;
	}

	@BreadCrumb("%{crumbName}")
	public String showBolla() {
		filterTitle = getText("igeriv.bolla.consegna");
		if (dataTipoBolla == null || dataTipoBolla.equals("")) {
			return INPUT;
		}
		try {
			StringTokenizer st = new StringTokenizer(dataTipoBolla, "|");
			String strData = st.nextToken();
			String tipo = st.nextToken().replaceFirst(IGerivConstants.TIPO, "").trim();
			Boolean enabled = Boolean.valueOf(st.nextToken().trim());
			Timestamp dtBolla = DateUtilities.parseDate(strData, DateUtilities.FORMATO_DATA);
			boolean showSoloRigheSpuntare = soloRigheSpuntare == null ? false : (soloRigheSpuntare.equals("1") ? true : false);
			itensBolla = bolleService.getDettaglioBolla(new Integer[] { getAuthUser().getCodFiegDl() }, new Integer[] { getAuthUser().getId() }, dtBolla, tipo, showSoloRigheSpuntare, false);
			String time = itensBolla != null && !itensBolla.isEmpty() && ((BollaDettaglioDto) itensBolla.get(0)) != null && ((BollaDettaglioDto) itensBolla.get(0)).getCreated() != null ? DateUtilities.getTimestampAsString(((BollaDettaglioDto) itensBolla.get(0)).getCreated(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS) : DateUtilities.getTimestampAsString(new Date(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS);
			List dettagliFondoBolla = bolleService.getDettagliFondoBolla(new Integer[] { getAuthUser().getCodFiegDl() }, new Integer[] { getAuthUser().getId() }, dtBolla, tipo, showSoloRigheSpuntare, false, false);
			BigDecimal sumImportoLordoBolla = sum(itensBolla, on(BollaDettaglioDto.class).getImportoLordo());
			BigDecimal sumImportoLordoFondoBolla = sum(dettagliFondoBolla, on(FondoBollaDettaglioDto.class).getImportoLordo());
			BigDecimal sumBolla = sum(itensBolla, on(BollaDettaglioDto.class).getImporto());
			BigDecimal sumFondoBolla = sum(dettagliFondoBolla, on(FondoBollaDettaglioDto.class).getImporto());
			hasPrenotazioniFisseNonEvase = rifornimentiService.hasPrenotazioniFisseNonEvase(getAuthUser().getId(), getAuthUser().getCodFiegDl(), dtBolla, tipo);
			String totaleBollaFormat = NumberUtils.formatNumber(sumBolla);
			String totaleFondoBollaFormat = NumberUtils.formatNumber(sumFondoBolla);
			String totaleBollaLordoFormat = NumberUtils.formatNumber(sumImportoLordoBolla);
			String totaleFondoBollaLordoFormat = NumberUtils.formatNumber(sumImportoLordoFondoBolla);
			messaggiBolla = messaggiService.getMessaggiBolla(new Integer[] { getAuthUser().getCodFiegDl() }, new Integer[] { getAuthUser().getId() }, dtBolla, tipo);
			itensBolla.addAll(dettagliFondoBolla);
			importoLordo = NumberUtils.formatNumber(sumImportoLordoBolla.add(sumImportoLordoFondoBolla));
			int count = 0;
			for (Object vo : itensBolla) {
				BeanUtils.setProperty(vo, "rownum", ++count);
			}
			BollaRiassuntoVo vo = bolleService.getBollaRiassunto(getAuthUser().getCodFiegDl(), getAuthUser().getId(), dtBolla, tipo);
			msgBollaLetti = vo.getMessaggiInBollaLetti();
			requestMap.put("totaleBollaFormat", totaleBollaFormat);
			requestMap.put("totaleFondoBollaFormat", totaleFondoBollaFormat);
			requestMap.put("totaleBollaLordoFormat", totaleBollaLordoFormat);
			requestMap.put("totaleFondoBollaLordoFormat", totaleFondoBollaLordoFormat);
			requestMap.put("itensBolla", itensBolla);
			requestMap.put("messaggiBolla", messaggiBolla);
			requestMap.put("strData", strData);
			String msg = "";
			if (getAuthUser().getHasMessaggioDocumentoDisponibile()) {
				BollaRiassuntoVo brvo = bolleService.getBollaRiassunto(getAuthUser().getCodFiegDl(), getAuthUser().getId(), dtBolla, tipo);
				msg = (!Strings.isNullOrEmpty(brvo.getDataRegistrazioneDocumentoFormat()) && !Strings.isNullOrEmpty(brvo.getOraRegistrazioneDocumentoFormat())) ? StringUtility.checkSpecialChars(StringUtility.unescapeHTML(MessageFormat.format(getText("igeriv.msg.bolla.consegna.documento.disponibile"), brvo.getDataRegistrazioneDocumentoFormat(), brvo.getOraRegistrazioneDocumentoFormat()))) : "";
			}
			requestMap.put("pdfTitleDocumentoDisponibile", msg);
			requestMap.put("tipo", tipo);
			requestMap.put("time", time); 
			if (BollaControlloTab_ev != null && (BollaControlloTab_ev.toUpperCase().equals("PDF") || BollaControlloTab_ev.toUpperCase().equals("XLS"))) {
				setAgenziaEdicolaExportTitle();
			}
			disableForm = !enabled;
		} catch (Throwable e) {
			addActionError(getText("gp.errore.imprevisto"));
			return INPUT;
		}
		return SUCCESS;
	}

	@BreadCrumb("%{crumbNameSIU}")
	public String showBollaSonoInoltreUscite() {
		filterTitle = getText("igeriv.sono.inoltre.uscite");
		if (dataTipoBolla == null || dataTipoBolla.equals("")) {
			return INPUT;
		}
		try {
			StringTokenizer st = new StringTokenizer(dataTipoBolla, "|");
			String strData = st.nextToken();
			String tipo = st.nextToken().replaceFirst(IGerivConstants.TIPO, "").trim();
			Boolean enabled = Boolean.valueOf(st.nextToken().trim());
			Timestamp dtBolla = DateUtilities.parseDate(strData, DateUtilities.FORMATO_DATA);
			itensBolla = bolleService.getBollaVoSonoInoltreUscite(getAuthUser().getCodFiegDl(), getAuthUser().getId(), dtBolla, tipo);

			boolean isEdicolaDeviettiTodis = getAuthUser().getCodFiegDl().equals(IGerivConstants.COD_FIEG_DL_DEVIETTI) || getAuthUser().getCodFiegDl().equals(IGerivConstants.COD_FIEG_DL_TODIS);
			Integer agenziaFatturazione = getAuthUser().getAgenziaFatturazione();
			if (getAuthUser().isMultiDl() && isEdicolaDeviettiTodis && getAuthUser().getGesSepDevTod() && agenziaFatturazione != null && agenziaFatturazione > 0) {
				Boolean isEdicolaSecondaCintura = getAuthUser().getEdSecCintura();
				Timestamp dtPartenzaSecondaCintura = getAuthUser().getDtPartSecondaCintura();
				if (isEdicolaSecondaCintura && dtPartenzaSecondaCintura != null) {
					if (dtBolla.compareTo(dtPartenzaSecondaCintura)>=0) {
						agenziaFatturazione = 2;
					} else {
						agenziaFatturazione = 1;
					}
				}

				Iterator<BollaVoDto> it = (Iterator<BollaVoDto>) itensBolla.iterator();
				while (it.hasNext()) {
					BollaVoDto dto = it.next();
					Boolean editoreComune = iGerivUtils.isFornitoreDevTodisComune(dto.getCodFornitore());
					
					//TODO SECONDA CINTURA
					if (editoreComune) {
						switch (agenziaFatturazione) {
						case 1:
							if ( dto.getPk().getCodFiegDl().equals(IGerivConstants.COD_FIEG_DL_TODIS)) {
							    it.remove();
							}
							break;
						case 2:
							if ( dto.getPk().getCodFiegDl().equals(IGerivConstants.COD_FIEG_DL_DEVIETTI)) {
							    it.remove();
							}
							break;
						}
					}
						
				}
			}
			
			int count = 0;
			for (Object vo : itensBolla) {
				BeanUtils.setProperty(vo, "rownum", ++count);
			}
			requestMap.put("itensBolla", itensBolla);
			requestMap.put("title", getText("igeriv.sono.inoltre.uscite"));
			if (SonoInoltreUsciteTab_ev != null && (SonoInoltreUsciteTab_ev.toUpperCase().equals("PDF") || SonoInoltreUsciteTab_ev.toUpperCase().equals("XLS"))) {
				setAgenziaEdicolaExportTitle();
			}
			disableForm = !enabled;
		} catch (Throwable e) {
			addActionError(getText("gp.errore.imprevisto"));
			return INPUT;
		}
		return SUCCESS;
	}

	@BreadCrumb("%{crumbNameViewRifo}")
	@SkipValidation
	public String showRichiesteRifornimentoFilter() {
		filterTitle = getText("igeriv.bolla.visualizza.rifornimenti");
		requestMap.put("tipoRifornimento", "edicola");
		return IGerivConstants.ACTION_RICHIESTE_RIFORNIMENTI;
	}

	public String showRichiesteRifornimenti() {
		filterTitle = getText("igeriv.bolla.visualizza.rifornimenti");
		requestMap.put("tipoRifornimento", "edicola");
		
		try {
			if (!Strings.isNullOrEmpty(idtn) && !Strings.isNullOrEmpty(periodicita) && !Strings.isNullOrEmpty(this.coddl)) {
				Integer coddl = Strings.isNullOrEmpty(this.coddl) ? getAuthUser().getCodFiegDl() : new Integer(this.coddl);
				Integer codEdicola = Strings.isNullOrEmpty(this.coddl) ? getAuthUser().getId() : iGerivUtils.getCorrispondenzaCodEdicolaMultiDl(coddl, getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId());
				Integer idtnInt = new Integer(idtn);
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("agenziaFatturazione", getAuthUser().getAgenziaFatturazione());
				params.put("isSecondaCintura", getAuthUser().getEdSecCintura());
				params.put("dataPartSecCintura", getAuthUser().getDtPartSecondaCintura());
				//Mod 23/09/2014 -> Rifornimenti per data periodo : idtnInt, getAuthUser().isMultiDl(), dataStorico
				//Mod 24/09/2014 -> Rifornimenti per data periodo : idtnInt, false, dataStorico
				richiesteRifornimento = rifornimentiService.getRichiesteRifornimenti(coddl, getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), idtnInt, getAuthUser().isMultiDl(), dataStorico, getAuthUser().getCodFiegDl(), params);
				//richiesteRifornimento = rifornimentiService.getPubblicazioniPossibiliPerRichiesteRifornimenti(coddl, getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), idtnInt, getAuthUser().isMultiDl(), dataStorico, getAuthUser().getCodFiegDl());
				if (richiesteRifornimento == null || richiesteRifornimento.isEmpty()) {
					throw new IGerivBusinessException(getText("igeriv.pubblicazione.non.disponibile.per.rifornimento"));
				}
				
				if(getAuthUser().getCodFiegDl().equals(IGerivConstants.COD_FIEG_MENTA)){
				
					RichiestaRifornimentoDto tmp_richiestarif = richiesteRifornimento.get(0);
					RichiestaRifornimentoPk tmp_pk = (RichiestaRifornimentoPk)tmp_richiestarif.getPk();
					dettaglioEsitorichiesteRifornimento = rifornimentiService.getPubblicazioniEsitoRichiesteRifornimenti(coddl, getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(),tmp_pk.getIdtn() , getAuthUser().isMultiDl(), dataStorico, getAuthUser().getCodFiegDl());
					//Ticket 0000373
					StoricoCopertineVo stCopertinaVo = pubblicazioniService.getStoricoCopertinaByPk(coddl, tmp_pk.getIdtn());
					
					Integer msgControllaDettaglioPerRifornimentiAncoraAttiviFirst = 0; // Nessun messaggio
					Collections.reverse(dettaglioEsitorichiesteRifornimento);
					
						//Ticket 0000373
						if(dettaglioEsitorichiesteRifornimento!=null && dettaglioEsitorichiesteRifornimento.size()>0){
							for (RichiestaRifornimentoDto vo : dettaglioEsitorichiesteRifornimento) {
								
								//Escludere dalla validazione dei messaggi tutte le richieste 
								//scadute OR tutte quelle che isosp = 0
								Date oggi = new Date();
								Integer confrontoDate = oggi.compareTo(new Date(vo.getDataScadenza().getTime()));
								Boolean isRichiestaScaduta = (confrontoDate > 0)?true:false;
								Boolean richiestaEvasa = (vo.getRichiestaSospesa()!=null && vo.getRichiestaSospesa()==0)?true:false;
								
								if(isRichiestaScaduta || richiestaEvasa){
									continue;
								}
								
								Boolean richiestaSospesa = (vo.getRichiestaSospesa()!=null && vo.getRichiestaSospesa()==1)?true:false;
								Boolean isRichiestaAttiva = (confrontoDate<=0)?true:false;
								
								if(richiestaSospesa && isRichiestaAttiva){
									msgControllaDettaglioPerRifornimentiAncoraAttiviFirst = 1; // Messaggio True
								}else{
									msgControllaDettaglioPerRifornimentiAncoraAttiviFirst = 2; // Messaggio False
								}
							}
					
						//Ticket 0000373
						}else if(stCopertinaVo!=null && stCopertinaVo.getDataRichiamoResa()!=null && stCopertinaVo.getTipoRichiamoResa()!=null){
								if(stCopertinaVo.getTipoRichiamoResa()!=null 
										&& stCopertinaVo.getTipoRichiamoResa().intValue() != 1
										&& stCopertinaVo.getTipoRichiamoResa().intValue() != 2){
									msgControllaDettaglioPerRifornimentiAncoraAttiviFirst = 3; // Messaggio "Pubblicazione chiamata in resa"
									String  dataRichiamoResa = (stCopertinaVo.getDataRichiamoResa()!=null)?DateUtilities.getTimestampAsString(new Date(stCopertinaVo.getDataRichiamoResa().getTime()), DateUtilities.FORMATO_DATA):""; 
									requestMap.put("dataRichiamoResa","" + dataRichiamoResa);
								}
						}
					
					requestMap.put("msgControllaDettaglioPerRifornimentiAncoraAttiviFirst", msgControllaDettaglioPerRifornimentiAncoraAttiviFirst);
					
				}
				
				
				PubblicazioneDto copertina = pubblicazioniService.getCopertinaByIdtn(coddl, idtnInt);
				isQuotidiano = copertina.getCodInizioQuotidiano() != null && copertina.getCodFineQuotidiano() != null && !copertina.getCodInizioQuotidiano().equals(copertina.getCodFineQuotidiano());

				// Integer agenziaFatturazione, Boolean isSecondaCintura, Timestamp dataPartSecCintura
				Integer agenziaFatturazione = getAuthUser().getAgenziaFatturazione();
				Boolean isEdicolaSecondaCintura = getAuthUser().getEdSecCintura();
				Timestamp dtPartenzaSecondaCintura = getAuthUser().getDtPartSecondaCintura();
				/*if (isEdicolaSecondaCintura != null && isEdicolaSecondaCintura) {
					requestMap.put("statistica", pubblicazioniService.getCopertine(false, true, false, getAuthUser().getCodEdicolaMaster(), getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), null, null, null, periodicita, null, copertina.getCodicePubblicazione(), null, false, dataStorico, null, false, getAuthUser().getCodFiegDl(),null, null, null, null));
				} else {
					requestMap.put("statistica", pubblicazioniService.getCopertine(false, true, false, getAuthUser().getCodEdicolaMaster(), getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), null, null, null, periodicita, null, copertina.getCodicePubblicazione(), null, false, dataStorico, null, false, getAuthUser().getCodFiegDl(),null, agenziaFatturazione, isEdicolaSecondaCintura, dtPartenzaSecondaCintura));
				}*/
				requestMap.put("statistica", pubblicazioniService.getCopertine(false, true, false, getAuthUser().getCodEdicolaMaster(), getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), null, null, null, periodicita, null, copertina.getCodicePubblicazione(), null, false, dataStorico, null, false, getAuthUser().getCodFiegDl(),null, null, null, null));
				
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
				//Ticket 0000373
				if(getAuthUser().getCodFiegDl().equals(IGerivConstants.COD_FIEG_MENTA))
					requestMap.put("title_DettaglioEsitoRifornimenti", getText("igeriv.bolla.esito.rifornimenti") + ": " + titolo);
				
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
					richiesteRifornimento.addAll((List) rifornimentiService.getRichiesteRifornimenti(getAuthUser().getArrCodFiegDl()[i], getAuthUser().getArrId()[i], titolo, stato, dataDa, dataA, isDlInforiv));
				}
			}
			
			requestMap.put("richiesteRifornimento", richiesteRifornimento);
			//Ticket 0000373
			if(getAuthUser().getCodFiegDl().equals(IGerivConstants.COD_FIEG_MENTA))
				requestMap.put("dettaglioEsitorichiesteRifornimento", dettaglioEsitorichiesteRifornimento);
			
			//requestMap.put("resultList", resultList);
			requestMap.put("filterTitle", filterTitle);
			requestMap.put("sysdate", rifornimentiService.getSysdate());
			
		} catch (IGerivBusinessException e) {
			addActionError(e.getMessage());
		} catch (Throwable e) {
			addActionError(getText("gp.errore.imprevisto"));
		}
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
			List statistiche = pubblicazioniService.getCopertine(false, true, false, getAuthUser().getCodEdicolaMaster(), getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), null, null, null, periodicita, null, copertina.getCodicePubblicazione(), null, showOnlyUltimoDistribuito, dataStorico, null, false, getAuthUser().getCodFiegDl(),null, null, null, null);
			requestMap.put("statistica", statistiche);
			if (!prenotazioneVo.isEnabled()) {
				prenotazioneDisabled = "true";
			}

			String[] periodicitaSplitted = periodicita.split("\\|");
			isQuotidiano = periodicitaSplitted[1].equals("1");
		}
		return IGerivConstants.ACTION_RICHIESTE_VARIAZIONI;
	}

	@SkipValidation
	public String showPrenotazioniNonEvase() {
		filterTitle = getText("igeriv.prenotazioni.non.evase");
		try {
			if (!Strings.isNullOrEmpty(dataTipoBolla)) {
				StringTokenizer st = new StringTokenizer(dataTipoBolla, "|");
				String strData = st.nextToken();
				String tipo = st.nextToken().replaceFirst(IGerivConstants.TIPO, "").trim();
				Timestamp dtBolla = DateUtilities.parseDate(strData, DateUtilities.FORMATO_DATA);
				List<PrenotazioneDto> listRichieste = rifornimentiService.getPrenotazioniFisseNonEvase(getAuthUser().getId(), getAuthUser().getCodFiegDl(), dtBolla, tipo);
				requestMap.put("richiesteRifornimento", listRichieste);
			}
		} catch (Throwable e) {
			addActionError(getText("gp.errore.imprevisto"));
		}
		return IGerivConstants.ACTION_RICHIESTE_NON_EVASE;
	}

	@BreadCrumb("%{crumbNameViewVariaIns}")
	@SkipValidation
	public String showVariazioniInseriteFilter() {
		filterTitle = getText("igeriv.bolla.visualizza.variazioni.inserite");
		return SUCCESS;
	}

	@BreadCrumb("%{crumbNameViewVariaIns}")
	public String showVariazioniInserite() {
		filterTitle = getText("igeriv.bolla.visualizza.variazioni.inserite");
		try {
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
		} catch (Throwable e) {
			addActionError(getText("gp.errore.imprevisto"));
			return INPUT;
		}
		return SUCCESS;
	}

	@SkipValidation
	public String showRichiesteRifornimentiEdit() {
		showRichiesteRifornimenti();
		return IGerivConstants.ACTION_RICHIESTE_RIFORNIMENTI_EDIT;
	}

	@SkipValidation
	public String editRichiesteRifornimenti() {
		filterTitle = getText("igeriv.modifica.richieste.inserite");
		
		try {
			Timestamp dataDa = DateUtilities.floorDay(new Date());
			Timestamp dataA = DateUtilities.ceilDay(new Date());
			stato = null;
			
			richiesteRifornimento = new ArrayList<RichiestaRifornimentoDto>();
			for (int i = 0; i < getAuthUser().getArrCodFiegDl().length; i++) {
				richiesteRifornimento.addAll((List) rifornimentiService.getRichiesteRifornimenti(getAuthUser().getArrCodFiegDl()[i], getAuthUser().getArrId()[i], titolo, stato, dataDa, dataA, isDlInforiv));
			}
			
			requestMap.put("richiesteRifornimento", richiesteRifornimento);
			requestMap.put("filterTitle", filterTitle);
			requestMap.put("sysdate", rifornimentiService.getSysdate());
		} catch (Throwable e) {
			addActionError(getText("gp.errore.imprevisto"));
		}
		
		return IGerivConstants.ACTION_RICHIESTE_RIFORNIMENTI_EDIT;
	}

	@SkipValidation
	public String saveMessaggiBollaLetti() {
		if (!Strings.isNullOrEmpty(dataTipoBolla)) {
			try {
				StringTokenizer st = new StringTokenizer(dataTipoBolla, "|");
				String strData = st.nextToken();
				String tipo = st.nextToken().replaceFirst(IGerivConstants.TIPO, "").trim();
				Timestamp dtBolla = DateUtilities.parseDate(strData, DateUtilities.FORMATO_DATA);
				BollaRiassuntoVo vo = bolleService.getBollaRiassunto(getAuthUser().getCodFiegDl(), getAuthUser().getId(), dtBolla, tipo);
				vo.setMessaggiInBollaLetti(true);
				bolleService.saveBaseVo(vo);
			} catch (Throwable e) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
		}
		return "blank";
	}

	public String save() {
		try {
			StringTokenizer st = new StringTokenizer(dataTipoBolla, "|");
			String strData = st.nextToken();
			String tipo = st.nextToken().replaceFirst(IGerivConstants.TIPO, "").trim();
			Timestamp dtBolla = DateUtilities.parseDate(strData, DateUtilities.FORMATO_DATA);
			BollaRiassuntoVo bollaRiassunto = bolleService.getBollaRiassunto(getAuthUser().getCodFiegDl(), getAuthUser().getId(), dtBolla, tipo);
			if (bollaRiassunto.getBollaTrasmessaDl().intValue() >= IGerivConstants.INDICATORE_RECORD_IN_TRASMISSIONE_DL) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("impossibile.salvare.bolla.gia.trasmessa") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
			Map<String, String> fields = new HashMap<String, String>();
			Map<String, String> spunte = new HashMap<String, String>();
			Map<String, String> fieldsFB = new HashMap<String, String>();
			Map<String, String> spunteFB = new HashMap<String, String>();
			buildModifiedMaps(fields, spunte, fieldsFB, spunteFB);
			bolleService.saveBollaRivendita(getAuthUser().getId(), fields, spunte, fieldsFB, spunteFB, bollaRiassunto);
			//bolleService.saveBollaRivendita(getAuthUser().getId(), fields, spunte, fieldsFB, spunteFB);
			
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

	
	
	public String saveRichiestaRifornimento() {
		try {
			List<RichiestaRifornimentoVo> list = null;
			Set<String> pkSet = buildSet(pk);
			list = rifornimentiService.getRichiesteRifornimentiVo(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), pkSet);
			List<RichiestaRifornimentoVo> listRichiestaRifornimentoVo = buildListRichiestaRifornimentoVo(list, pkSet);
			List<RichiestaRifornimentoVo> listRichiestaRifornimentoVoAggiunte = buildListRichiestaRifornimentoVoAggiunte(buildSet(pkAggiunte), giorniValiditaRichiesteRifornimentoAggiunte, quantitaRifornimentoAggiunte, noteVenditaAggiunte);
			Integer quantitaVariazioneServ = (quantitaVariazioneServizio != null && !quantitaVariazioneServizio.trim().equals("")) ? new Integer(quantitaVariazioneServizio.trim().replaceAll("\\+", "")) : null;
			if (prenotazioneVo != null) {
				if (quantitaVariazioneServ == null) {
					rifornimentiService.deleteVo(prenotazioneVo);
					prenotazioneVo = null;
				} else {
					prenotazioneVo.setCodUtente(getAuthUser().getId());
					if (getAuthUser().getCodFiegDl().equals(IGerivConstants.CDL_CODE)) {
						GrantedAuthority auth = new GrantedAuthorityImpl(IGerivConstants.ROLE_IGERIV_BASE);
						if (getAuthUser().getAuthorities().contains(auth)) {
							prenotazioneVo.setCodUtente(new Integer(getAuthUser().getCodUtente()));
						}
					}
					if (quantitaVariazioneServ < 0) {
						throw new IGerivBusinessException(getText("dpe.validation.msg.qta.var.servizio.negativa"));
					}
					rifornimentiService.deleteVo(prenotazioneVo);
					prenotazioneVo.setQuantitaRichiesta(quantitaVariazioneServ);
					prenotazioneVo.getPk().setDataRichiesta(new java.sql.Date(new java.util.Date().getTime()));
					prenotazioneVo.setIndicatoreTrasmessoDl(IGerivConstants.INDICATORE_RECORD_NON_TRASMESSO_DL);
				}
			}
			List<RichiestaRifornimentoVo> listDeleteRichiestaRifornimentoVo = new ArrayList<>();
			if (!listRichiestaRifornimentoVo.isEmpty()) {
				listDeleteRichiestaRifornimentoVo.addAll(select(listRichiestaRifornimentoVo, having(on(RichiestaRifornimentoVo.class).getQuantitaRichiesta(), equalTo(0))
						.or(having(on(RichiestaRifornimentoVo.class).getQuantitaRichiesta(), equalTo(nullValue())))));
				listRichiestaRifornimentoVo.removeAll(listDeleteRichiestaRifornimentoVo);
			}
			if (!listRichiestaRifornimentoVoAggiunte.isEmpty()) {
				List<RichiestaRifornimentoVo> select = select(listRichiestaRifornimentoVoAggiunte,
						having(on(RichiestaRifornimentoVo.class).getQuantitaRichiesta(), equalTo(0)).or(having(on(RichiestaRifornimentoVo.class).getQuantitaRichiesta(), equalTo(nullValue()))));
				listDeleteRichiestaRifornimentoVo.addAll(select);
				listRichiestaRifornimentoVoAggiunte.removeAll(select);
			}
			
			List<RichiestaRifornimentoLivellamentiVo> listDeleteRichiestaRifornimentoLivellamentiVo = new ArrayList<>();
			List<RichiestaRifornimentoLivellamentiVo> richiesteLivellamentiSenzaEdicole = new ArrayList<>();
			List<RichiestaRifornimentoLivellamentiVo> richiesteLivellamenti = new ArrayList<>();
			if (getAuthUser().getHasLivellamenti() && chkLivellamenti != null && chkLivellamenti.size() > 0) {
				richiesteLivellamenti = buildListRichiestaRifornimentoLivellamenti(pkSet, listRichiestaRifornimentoVo, listDeleteRichiestaRifornimentoVo, richiesteLivellamentiSenzaEdicole);
			}
			if (!richiesteLivellamenti.isEmpty()) {
				listDeleteRichiestaRifornimentoLivellamentiVo.addAll(select(richiesteLivellamenti, having(on(RichiestaRifornimentoLivellamentiVo.class).getQuantitaRichiesta(), equalTo(0))
						.or(having(on(RichiestaRifornimentoLivellamentiVo.class).getQuantitaRichiesta(), equalTo(nullValue())))));
				richiesteLivellamenti.removeAll(listDeleteRichiestaRifornimentoLivellamentiVo);
			}
			rifornimentiService.saveRichiestaRifornimento(listRichiestaRifornimentoVo, listRichiestaRifornimentoVoAggiunte, listDeleteRichiestaRifornimentoVo, prenotazioneVo, richiesteLivellamenti, listDeleteRichiestaRifornimentoLivellamentiVo);
			if (getAuthUser().getHasLivellamenti() && !richiesteLivellamentiSenzaEdicole.isEmpty()) {
				Set<String> l = new HashSet<>();
				for (RichiestaRifornimentoLivellamentiVo vo : richiesteLivellamentiSenzaEdicole) {
					if (vo != null && vo.getIdtn() != null) {
						PubblicazioneDto cop = pubblicazioniService.getCopertinaByIdtn(vo.getCoddl(), vo.getIdtn());
						l.add(cop.getTitolo() + " " + getText("igeriv.numero") + " " + cop.getNumeroCopertina().trim());
					}
				}
				throw new IGerivBusinessException(MessageFormat.format(getText("igeriv.error.message.invio.richiesta.rifornimento.livellamenti"), Joiner.on(", ").join(l)), null);
			}
		} catch (IGerivBusinessException e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + e.getMessage() + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
 		return IGerivConstants.ACTION_RICHIESTE_RIFORNIMENTI;
	}
	
	//Metodo utilizzato nel popup di modifica rifornimento
	//18-04-2016 - Modifica effettuata per verificare che all'interno del popup ci siano sempre dati consistenti
	public String saveRichiestaRifornimentoModifica() {
		try {
			List<RichiestaRifornimentoVo> list = null;
			Set<String> pkSet = buildSet(pk);
			list = rifornimentiService.getRichiesteRifornimentiVo(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), pkSet);
			List<RichiestaRifornimentoVo> listRichiestaRifornimentoVo = buildListRichiestaRifornimentoVo(list, pkSet);
			List<RichiestaRifornimentoVo> listRichiestaRifornimentoVoAggiunte = buildListRichiestaRifornimentoVoAggiunte(buildSet(pkAggiunte), giorniValiditaRichiesteRifornimentoAggiunte, quantitaRifornimentoAggiunte, noteVenditaAggiunte);
			Integer quantitaVariazioneServ = (quantitaVariazioneServizio != null && !quantitaVariazioneServizio.trim().equals("")) ? new Integer(quantitaVariazioneServizio.trim().replaceAll("\\+", "")) : null;
			if (prenotazioneVo != null) {
				if (quantitaVariazioneServ == null) {
					rifornimentiService.deleteVo(prenotazioneVo);
					prenotazioneVo = null;
				} else {
					prenotazioneVo.setCodUtente(getAuthUser().getId());
					if (getAuthUser().getCodFiegDl().equals(IGerivConstants.CDL_CODE)) {
						GrantedAuthority auth = new GrantedAuthorityImpl(IGerivConstants.ROLE_IGERIV_BASE);
						if (getAuthUser().getAuthorities().contains(auth)) {
							prenotazioneVo.setCodUtente(new Integer(getAuthUser().getCodUtente()));
						}
					}
					if (quantitaVariazioneServ < 0) {
						throw new IGerivBusinessException(getText("dpe.validation.msg.qta.var.servizio.negativa"));
					}
					rifornimentiService.deleteVo(prenotazioneVo);
					prenotazioneVo.setQuantitaRichiesta(quantitaVariazioneServ);
					prenotazioneVo.getPk().setDataRichiesta(new java.sql.Date(new java.util.Date().getTime()));
					prenotazioneVo.setIndicatoreTrasmessoDl(IGerivConstants.INDICATORE_RECORD_NON_TRASMESSO_DL);
				}
			}
			List<RichiestaRifornimentoVo> listDeleteRichiestaRifornimentoVo = new ArrayList<>();
			if (!listRichiestaRifornimentoVo.isEmpty()) {
				listDeleteRichiestaRifornimentoVo.addAll(select(listRichiestaRifornimentoVo, having(on(RichiestaRifornimentoVo.class).getQuantitaRichiesta(), equalTo(0))
						.or(having(on(RichiestaRifornimentoVo.class).getQuantitaRichiesta(), equalTo(nullValue())))));
				listRichiestaRifornimentoVo.removeAll(listDeleteRichiestaRifornimentoVo);
			
				List<RichiestaRifornimentoVo> tmp_listRichiestaRifornimentoVo = new ArrayList<RichiestaRifornimentoVo>();
				for (RichiestaRifornimentoVo richRif:listRichiestaRifornimentoVo) {
					RichiestaRifornimentoVo readRichiestaRifoVo = rifornimentiService.getRichiestaRifornimento(richRif.getPk().getCodFiegDl(), richRif.getPk().getCodEdicola(),richRif.getPk().getDataOrdine(), richRif.getPk().getIdtn());
					if(readRichiestaRifoVo!=null && readRichiestaRifoVo.getStato()!= null && !readRichiestaRifoVo.getStato().equals("")){
						//DA ESCLUDERE
						throw new IGerivBusinessException("Le tue richieste sono state gia\' prese in carico e non sono piu\' modificabili",  null);
					}
				}
			
			}
			if (!listRichiestaRifornimentoVoAggiunte.isEmpty()) {
				List<RichiestaRifornimentoVo> select = select(listRichiestaRifornimentoVoAggiunte,
						having(on(RichiestaRifornimentoVo.class).getQuantitaRichiesta(), equalTo(0)).or(having(on(RichiestaRifornimentoVo.class).getQuantitaRichiesta(), equalTo(nullValue()))));
				listDeleteRichiestaRifornimentoVo.addAll(select);
				listRichiestaRifornimentoVoAggiunte.removeAll(select);
			}
			
			List<RichiestaRifornimentoLivellamentiVo> listDeleteRichiestaRifornimentoLivellamentiVo = new ArrayList<>();
			List<RichiestaRifornimentoLivellamentiVo> richiesteLivellamentiSenzaEdicole = new ArrayList<>();
			List<RichiestaRifornimentoLivellamentiVo> richiesteLivellamenti = new ArrayList<>();
			if (getAuthUser().getHasLivellamenti() && chkLivellamenti != null && chkLivellamenti.size() > 0) {
				richiesteLivellamenti = buildListRichiestaRifornimentoLivellamenti(pkSet, listRichiestaRifornimentoVo, listDeleteRichiestaRifornimentoVo, richiesteLivellamentiSenzaEdicole);
			}
			if (!richiesteLivellamenti.isEmpty()) {
				listDeleteRichiestaRifornimentoLivellamentiVo.addAll(select(richiesteLivellamenti, having(on(RichiestaRifornimentoLivellamentiVo.class).getQuantitaRichiesta(), equalTo(0))
						.or(having(on(RichiestaRifornimentoLivellamentiVo.class).getQuantitaRichiesta(), equalTo(nullValue())))));
				richiesteLivellamenti.removeAll(listDeleteRichiestaRifornimentoLivellamentiVo);
			}
			rifornimentiService.saveRichiestaRifornimento(listRichiestaRifornimentoVo, listRichiestaRifornimentoVoAggiunte, listDeleteRichiestaRifornimentoVo, prenotazioneVo, richiesteLivellamenti, listDeleteRichiestaRifornimentoLivellamentiVo);
			if (getAuthUser().getHasLivellamenti() && !richiesteLivellamentiSenzaEdicole.isEmpty()) {
				Set<String> l = new HashSet<>();
				for (RichiestaRifornimentoLivellamentiVo vo : richiesteLivellamentiSenzaEdicole) {
					if (vo != null && vo.getIdtn() != null) {
						PubblicazioneDto cop = pubblicazioniService.getCopertinaByIdtn(vo.getCoddl(), vo.getIdtn());
						l.add(cop.getTitolo() + " " + getText("igeriv.numero") + " " + cop.getNumeroCopertina().trim());
					}
				}
				throw new IGerivBusinessException(MessageFormat.format(getText("igeriv.error.message.invio.richiesta.rifornimento.livellamenti"), Joiner.on(", ").join(l)), null);
			}
		} catch (IGerivBusinessException e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + e.getMessage() + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
 		return IGerivConstants.ACTION_RICHIESTE_RIFORNIMENTI;
	}
	
	
	/**
	 * Seleziona le richieste di rifornimento livellamenti 
	 * 
	 * @param pkSet
	 * @param listRichiestaRifornimentoVo 
	 * @param listDeleteRichiestaRifornimentoVo 
	 * @param richiesteLivellamentiSenzaEdicole 
	 * @param richiesteLivellamenti 
	 * @return
	 * @throws ParseException 
	 * @throws IGerivBusinessException 
	 */
	private List<RichiestaRifornimentoLivellamentiVo> buildListRichiestaRifornimentoLivellamenti(Set<String> pkSet, List<RichiestaRifornimentoVo> listRichiestaRifornimentoVo, List<RichiestaRifornimentoVo> listDeleteRichiestaRifornimentoVo, List<RichiestaRifornimentoLivellamentiVo> richiesteLivellamentiSenzaEdicole) throws ParseException, IGerivBusinessException {
		List<RichiestaRifornimentoLivellamentiVo> richiesteLivellamenti = new ArrayList<>();
		pkSet.retainAll(chkLivellamenti);
		List idList = new ArrayList<>();
		Iterator<String> it1 = pkSet.iterator();
		while (it1.hasNext()) {
			idList.add(new Long(it1.next().split("\\|")[3]));
		}
		List<EdicoleVicineVo> listEdicoleVicine = livellamentiService.getListEdicoleDelGruppo(getAuthUser().getId());
		Iterator<String> it = pkSet.iterator();
		while (it.hasNext()) {
			String pk = it.next();
			String[] split = pk.split("\\|");
			Integer idtn = new Integer(split[3]);
			Integer quantitaRichiesta = !Strings.isNullOrEmpty(quantitaRifornimentoMap.get(pk)) ? new Integer(quantitaRifornimentoMap.get(pk)) : 0;
			Integer days = !Strings.isNullOrEmpty(giorniValiditaRichiesteRifornimentoMap.get(pk)) ? new Integer(giorniValiditaRichiesteRifornimentoMap.get(pk)) : 0;
			Timestamp dtScadenza = buildDataScadenzaFromDays(days);
			RichiestaRifornimentoVo richiesta = selectUnique(listRichiestaRifornimentoVo, having(on(RichiestaRifornimentoVo.class).getPk().toString(), equalTo(pk)));
			if (richiesta != null) {
				listRichiestaRifornimentoVo.remove(richiesta);
				listDeleteRichiestaRifornimentoVo.add(richiesta);
			}
			//19/02/2016 - Controllo dei giorni di scadenza inseriti 
			if (days < 1 || days > 70) {
				throw new IGerivBusinessException(getText("dpe.validation.msg.day.richiesta.rifornimento"));
			}
			if (quantitaRichiesta < 0) {
				throw new IGerivBusinessException(getText("dpe.validation.msg.qta.richiesta.negativa"));
			}
			RichiestaRifornimentoLivellamentiVo vo = livellamentiService.getRichiestaRifornimentoLivellamenti(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), idtn, Arrays.asList(new StatoRichiestaRifornimentoLivellamento[]{StatoRichiestaRifornimentoLivellamento.INSERITO}));
			if (vo == null && quantitaRichiesta.equals(0)) {
				continue;
			} else if (vo == null) {
				vo = new RichiestaRifornimentoLivellamentiVo();
			}
			vo.setEdicolaRichiedente(edicoleService.getAnagraficaEdicola(new Integer(split[1])));
			Integer codDl = new Integer(split[0]);
			vo.setCoddl(codDl);
			vo.setDataRichiesta(DateUtilities.parseDate(split[2], DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS));
			vo.setIdtn(idtn);
			vo.setQuantitaRichiesta(quantitaRichiesta);
			vo.setRecordTrasferitoDl(false);
			vo.setStato(StatoRichiestaRifornimentoLivellamento.INSERITO);
			if (vo.getLivellamenti() != null && !vo.getLivellamenti().isEmpty()) {
				vo.getLivellamenti().clear();
			} else {
				vo.setLivellamenti(new ArrayList<LivellamentiVo>());
			}
			buildLivellamenti(quantitaRichiesta, idtn, dtScadenza, vo, listEdicoleVicine);
			if (quantitaRichiesta > 0 && (vo.getLivellamenti() == null || vo.getLivellamenti().isEmpty())) {
				richiesteLivellamentiSenzaEdicole.add(vo);
			}
			richiesteLivellamenti.add(vo);
		}
		return richiesteLivellamenti;
	}
	
	/**
	 * Crea una richiesta livellamento per ogni edicola vicina e per il numero di copie richieste
	 * 
	 * @param quantitaRichiesta
	 * @param idtn
	 * @param dataScadenza 
	 * @param richiestaLivellamento
	 * @param listEdicole
	 */
	private void buildLivellamenti(Integer quantitaRichiesta, Integer idtn, Timestamp dataScadenza, RichiestaRifornimentoLivellamentiVo richiestaLivellamento, List<EdicoleVicineVo> listEdicole) {
		for (EdicoleVicineVo ed : listEdicole) {
			for (int i = 0; i < quantitaRichiesta; i++) {
				Long giacenza = pubblicazioniService.getGiacenza(ed.getEdicola().getAnagraficaAgenziaVo().getCodFiegDl(), ed.getEdicola().getCodDpeWebEdicola(), idtn, ed.getEdicola().getDataCreazioneStatistica());
				if (giacenza != null && giacenza > 0l) {
					LivellamentiVo liv = new LivellamentiVo();
					liv.setEdicola(ed.getEdicola().getAnagraficaEdicolaVo());
					liv.setQuantita(1);
					liv.setDataScadenza(dataScadenza);
					liv.setRichiesta(richiestaLivellamento);
					liv.setStatoRichiesta(StatoRichiestaLivellamento.INSERITO);
					liv.setStatoVendita(StatoRichiestaLivellamento.INSERITO);
					richiestaLivellamento.getLivellamenti().add(liv);
				}
			}
		}
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
					if (fieldMap != null) {
						fields.put(pk, fieldMap.get(pk));
					}
					if (spuntaMap != null) {
						spunte.put(pk, spuntaMap.get(pk));
					}
				}
			}
		}
		if (modificatoFBMap != null && !modificatoFBMap.isEmpty()) {
			for (Map.Entry<String, String> entry : modificatoFBMap.entrySet()) {
				String pk = entry.getKey();
				Boolean modified = new Boolean(entry.getValue());
				if (modified != null && modified) {
					if (fieldFBMap != null) {
						fieldsFB.put(pk, fieldFBMap.get(pk));
					}
					if (spuntaFBMap != null) {
						spunteFB.put(pk, spuntaFBMap.get(pk));
					}
				}
			}
		}
	}

	/**
	 * Costruisce le richieste di rifornimenti agguntive per un pubblicazione
	 * già richiesta nello stesso giorno.
	 * 
	 * @param buildSet
	 * @param dataScadenzaRichiestaAggiunte
	 * @param quantitaRifornimentoAggiunt2
	 * @param noteVenditaAggiunte
	 * @return
	 * @throws ParseException
	 * @throws IGerivBusinessException 
	 */
	private List<RichiestaRifornimentoVo> buildListRichiestaRifornimentoVoAggiunte(Set<String> buildSet, String giorniValiditaRichiesteRifornimentoAggiunte, String quantitaRifornimentoAggiunte, String noteVenditaAggiunte) throws ParseException, IGerivBusinessException {
		List<RichiestaRifornimentoVo> retList = new ArrayList<RichiestaRifornimentoVo>();
		if (buildSet != null && buildSet.size() > 0) {
			List<String> listPks = new ArrayList<String>(buildSet);
			if (listPks.size() > 0 && giorniValiditaRichiesteRifornimentoAggiunte != null && quantitaRifornimentoAggiunte != null && noteVenditaAggiunte != null) {
				String[] arrGiorniValiditaRichiesteRifornimento = giorniValiditaRichiesteRifornimentoAggiunte.split(",");
				String[] arrQtaRichiesta = quantitaRifornimentoAggiunte.split(",");
				String[] arrNote = noteVenditaAggiunte.split(",");
				for (int i = 0; i < listPks.size(); i++) {
					String[] pkArrVal = listPks.get(i).toString().split("\\|");
					RichiestaRifornimentoVo vo = new RichiestaRifornimentoVo();
					RichiestaRifornimentoPk pk = new RichiestaRifornimentoPk();
					pk.setCodEdicola(new Integer(pkArrVal[1]));
					Integer codFiegDl = new Integer(pkArrVal[0]);
					pk.setCodFiegDl(codFiegDl);
					pk.setDataOrdine(DateUtilities.parseDate(pkArrVal[2].trim(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS));
					Integer idtn = new Integer(pkArrVal[3]);
					pk.setIdtn(idtn);
					vo.setPk(pk);
					Integer days = (arrGiorniValiditaRichiesteRifornimento[i] != null && !arrGiorniValiditaRichiesteRifornimento[i].trim().equals("")) ? new Integer(arrGiorniValiditaRichiesteRifornimento[i].trim()) : 0;
					Timestamp dtScadenza = buildDataScadenzaFromDays(days);
					vo.setDataScadenzaRichiesta(dtScadenza);
					vo.setGiorniValiditaRichiesteRifornimento(days);
					Integer quantitaRichiesta = new Integer(arrQtaRichiesta[i].trim().replaceAll("\\+", ""));
					vo.setStoricoCopertineVo(pubblicazioniService.getStoricoCopertinaByPk(codFiegDl, idtn));
					if (quantitaRichiesta < 0) {
						throw new IGerivBusinessException(IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.msg.qta.richiesta.negativa"));
					} else if ((vo.getStoricoCopertineVo() != null && vo.getStoricoCopertineVo().getCopiePerConfezione() != null
							&& vo.getStoricoCopertineVo().getIndComponentePaccotto() != null 
							&& vo.getStoricoCopertineVo().getIndComponentePaccotto() > 1 
							&& vo.getStoricoCopertineVo().getCopiePerConfezione() > 0) 
							&& (quantitaRichiesta % vo.getStoricoCopertineVo().getCopiePerConfezione() != 0)) {
						throw new IGerivBusinessException(IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("dpe.validation.msg.qta.multiplo"), vo.getTitolo().toUpperCase(), vo.getStoricoCopertineVo().getCopiePerConfezione()));
					} 
					vo.setQuantitaRichiesta(quantitaRichiesta);
					vo.setNoteVendita(arrNote[i].trim());
					retList.add(vo);
				}
			}
		}
		return retList;
	}

	public String saveVariazioni() {
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
			List<RichiestaClienteVo> richiestaRifornimento = rifornimentiService.getRichiesteClienteByPk(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), new Long(idCliente), IGerivConstants.COD_PROVENIENZA_RICHIESTA_EDICOLA, pkSet);
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
	public String showOrdini() {
		if (idtn != null && !idtn.equals("")) {
			List<RichiestaClienteDto> ordini = rifornimentiService.getRichiesteClienteByIdtn(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), new Integer(idtn), null);
			List<RichiestaClienteDto> clienti = select(ordini, having(on(RichiestaClienteDto.class).getStatoEvasione(), equalTo(IGerivConstants.PRENOTAZIONI_INSERITE_O_PARZIALMENTE_EVASE)));
			List<String> nomiClientiConEC = extract(select(clienti, having(on(RichiestaClienteDto.class).getTipoEstrattoConto(), notNullValue())), on(RichiestaClienteDto.class).getNomeCognomeCliente());
			List<Long> listCodClienti = extract(select(clienti, having(on(RichiestaClienteDto.class).getTipoEstrattoConto(), nullValue())), on(RichiestaClienteDto.class).getCodCliente());
			requestMap.put("ordini", ordini);
			requestMap.put("nomiClientiConEC", nomiClientiConEC);
			requestMap.put("titolo", (ordini != null && !ordini.isEmpty() ? ordini.get(0).getTitolo() : ""));
			requestMap.put("listCodClienti", listCodClienti != null ? StringUtils.join(listCodClienti.toArray(), ",") : "");
		}
		requestMap.put("title", getText("igeriv.prenotazioni.clienti") + (richiestaFissaClienteEdicolaVo != null && richiestaFissaClienteEdicolaVo.getTitolo() != null ? " : " + richiestaFissaClienteEdicolaVo.getTitolo() : ""));
		return IGerivConstants.ACTION_ORDINI;
	}

	@SkipValidation
	public String showPrenotazioniClienteEdicola() {
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
	public String showVariazioniSettimana() {
		if (!Strings.isNullOrEmpty(idtn)) {
			Integer idtnInt = new Integer(idtn);
			Integer coddl = Strings.isNullOrEmpty(this.coddl) ? getAuthUser().getCodFiegDl() : new Integer(this.coddl);
			Integer codEdicola = Strings.isNullOrEmpty(this.coddl) ? getAuthUser().getId() : iGerivUtils.getCorrispondenzaCodEdicolaMultiDl(coddl, getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId());
			PubblicazioneDto copertina = pubblicazioniService.getCopertinaByIdtn(coddl, idtnInt);
			Integer cpuInizio = copertina.getCodInizioQuotidiano();
			Integer cpuFine = copertina.getCodFineQuotidiano();

			List<AnagraficaPubblicazioniVo> pubblicazioni = pubblicazioniService.getQuotidiani(coddl, cpuInizio, cpuFine);

			List<PrenotazioneVo> prenotazioni = new ArrayList<>();
			for (AnagraficaPubblicazioniVo pubblicazione : pubblicazioni) {
				PrenotazioneVo prenotazione = rifornimentiService.getPrenotazione(coddl, codEdicola, pubblicazione.getPk().getCodicePubblicazione());
				prenotazione.setPubblicazione(pubblicazione);
				prenotazioni.add(prenotazione);
			}

			requestMap.put("prenotazioni", prenotazioni);
		}

		return IGerivConstants.ACTION_RICHIESTE_VARIAZIONI_SETTIMANA;
	}

	@SkipValidation
	public String saveVariazioniSettimana() {
		Integer coddl = Strings.isNullOrEmpty(this.coddl) ? getAuthUser().getCodFiegDl() : new Integer(this.coddl);
		Integer codEdicola = Strings.isNullOrEmpty(this.coddl) ? getAuthUser().getId() : iGerivUtils.getCorrispondenzaCodEdicolaMultiDl(coddl, getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId());

		for (String cpu : quantitaRichiestaMap.keySet()) {
			String quantitaRichiesta = quantitaRichiestaMap.get(cpu);
			String motivoRichiesta = motivoRichiestaMap.get(cpu);

			prenotazioneVo = rifornimentiService.getPrenotazione(coddl, codEdicola, Integer.valueOf(cpu));
			prenotazioneVo.setMotivoRichiesta(Strings.isNullOrEmpty(motivoRichiesta) ? null : motivoRichiesta);
			quantitaVariazioneServizio = quantitaRichiesta;

			saveVariazioni();
		}

		return IGerivConstants.ACTION_RICHIESTE_VARIAZIONI_SETTIMANA;
	}
	
	@SkipValidation
	public String getQuantitaFuoriCompetenzaInserita() {
		mapResult = new HashMap<String, Object>();
		if (!Strings.isNullOrEmpty(pk) && !Strings.isNullOrEmpty(idtn)) {
			try {
				String[] split = pk.split("\\|");
				Integer coddl = new Integer(split[0]);
				String dtBolla = split[1];
				String tipo = split[2];
				Integer codEd = getAuthUser().getCodEdicolaMaster();
				String pk = coddl + "|" + codEd + "|" + dtBolla + "|" + tipo;
				BollaDettaglioVo det = bolleService.getBollaDettaglioVo(pk, new Integer(idtn));
				Integer diff = det != null && det.getDifferenze() != null ? det.getDifferenze() : 0;
				mapResult.put("qta", diff);
			} catch (IGerivRuntimeException e) {
				if (requestMap.get("igerivException") != null) {
					mapResult.put("error", requestMap.get("igerivException").toString().replace(IGerivConstants.START_EXCEPTION_TAG, "").replace(IGerivConstants.END_EXCEPTION_TAG, ""));
				}
			} catch (Throwable e) {
				mapResult.put("error", getText("gp.errore"));
			}
		}
		return SUCCESS;
	}
	
	@SkipValidation
	public String saveCopieFuoriCompetenza() {
		if (!Strings.isNullOrEmpty(pk) && !Strings.isNullOrEmpty(idtn) && quantita != null) {
			try {
				String[] split = pk.split("\\|");
				Integer coddl = new Integer(split[0]);
				Timestamp dtBolla = DateUtilities.parseDate(split[1], DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS);
				String tipo = split[2];
				//Integer codEd = getAuthUser().getCodEdicolaMaster();
				Integer codEd = getAuthUser().getCodDpeWebEdicola();
				
				PubblicazioneDto pub = pubblicazioniService.getPubblicazioneConPrezzoEdicola(getAuthUser().getArrId(), coddl, new Integer(idtn), getAuthUser().getGruppoSconto());
				String bdpk = coddl + "|" + codEd + "|" + DateUtilities.getTimestampAsString(dtBolla, DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS) + "|" + tipo;
				BollaDettaglioVo det = bolleService.getBollaDettaglioVo(bdpk, new Integer(idtn));
				if (det == null) {
					det = new BollaDettaglioVo();
					BollaDettaglioPk pk = new BollaDettaglioPk();
					pk.setCodEdicola(codEd);
					pk.setCodFiegDl(coddl);
					pk.setDtBolla(dtBolla);
					pk.setTipoBolla(tipo);
					pk.setPosizioneRiga(bolleService.getBollaVo(coddl, dtBolla, tipo, null, pub.getIdtn()).getPk().getPosizioneRiga());
					det.setPk(pk);
				}
				det.setIdtn(new Integer(idtn));
				det.setQuantitaConsegnata(0);
				det.setDifferenze(quantita);
				det.setPrezzoNetto(pub.getPrezzoEdicola());
				det.setPrezzoLordo(pub.getPrezzoCopertina());
				det.setIndicatoreValorizzare(IGerivConstants.INDICATORE_VALORIZZARE);
				det.setAggiuntaFuoriCompetenza(true);
				det.setSconto(BigDecimal.ZERO);
				det.setIndicatorePrezzoVariato(" ");
				det.setSpunta(IGerivConstants.COD_NO);
				det.setQuantitaSpuntata(0);
				bolleService.saveBaseVo(det);
			} catch (Throwable e) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
		}
		return "blank";
	}

	@BreadCrumb("%{crumbNameViewPreCli}")
	@SkipValidation
	public String viewPrenotazioniClientiEdicola() {
		if (nomeCliente != null) {
			filterTitle = getText("igeriv.visualizza.prenotazioni.cliente") + " <b>" + nomeCliente.toUpperCase() + "</b>";
			requestMap.put("tipoRifornimento", "clienteEdicola");
			if (idCliente != null && !idCliente.equals("")) {
				List listRichieste = new ArrayList();
				if (stato != null && !stato.trim().equals("")) {
					Integer statoInt = new Integer(stato);
					if (statoInt.equals(IGerivConstants.PRENOTAZIONI_EVASE) || statoInt.equals(IGerivConstants.PRENOTAZIONI_PARZIALMENTE_EVASE) || statoInt.equals(IGerivConstants.PRENOTAZIONI_INSERITE)) {
						listRichieste = rifornimentiService.getRichiesteClienteByIdClienteViewOnly(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), Arrays.asList(new Long[] { new Long(idCliente) }), titolo, stato, null, null, null, false);
					} else if (statoInt.equals(IGerivConstants.PRENOTAZIONI_FISSE)) {
						listRichieste = rifornimentiService.getRichiesteClienteFisseByIdCliente(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), new Long(idCliente), titolo, stato, null, null, findUltimoIdtnPrenotazioniFisse);
					}
				} else {
					listRichieste = rifornimentiService.getRichiesteClienteByIdClienteViewOnly(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), Arrays.asList(new Long[] { new Long(idCliente) }), titolo, stato, null, null, null, false);
					listRichieste.addAll(rifornimentiService.getRichiesteClienteFisseByIdCliente(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), new Long(idCliente), titolo, stato, null, null, findUltimoIdtnPrenotazioniFisse));
				}
				requestMap.put("richiesteRifornimento", listRichieste);
			}
			String titoloRichiesteRifornimento = getText("igeriv.bolla.rifornimenti") + " del " + DateUtilities.getTimestampAsString(new Date(), DateUtilities.FORMATO_DATA_SLASH);
			String cliente = getText("igeriv.provenienza.evasione.cliente") + " " + nomeCliente.toUpperCase().replaceAll("&nbsp;", " ").replaceAll("\\.", " ");
			requestMap.put("titoloRichiesteRifornimento", titoloRichiesteRifornimento);
			requestMap.put("cliente", cliente);
		}
		return IGerivConstants.ACTION_VIEW_PRENOTAZIONI_INSERITE_CLIENTI_EDICOLA;
	}

	/**
	 * Metodo chiamato via ajax dalla pagina di visualizzaizone delle
	 * prenotazioni
	 * 
	 * @return
	 */
	@SkipValidation
	public String deletePrenotazioniClientiEdicola() {
		try {
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
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "save";
	}

	/**
	 * Ritorna la lista di RichiestaRifornimentoVo caricata dal db con gli
	 * attributi introdotti dall'utente via form.
	 * 
	 * @param richiestaRifornimento
	 * @param pkSet
	 * @return
	 * @throws ParseException
	 * @throws IGerivBusinessException 
	 */
	private List<RichiestaRifornimentoVo> buildListRichiestaRifornimentoVo(List<RichiestaRifornimentoVo> richiestaRifornimento, Set<String> pkSet) throws ParseException, IGerivBusinessException {
		Set<RichiestaRifornimentoVo> retSet = new HashSet<RichiestaRifornimentoVo>();
		Iterator<String> it = pkSet.iterator();
		while (it.hasNext()) {
			String pk = it.next();
			Integer quantitaRichiesta = !Strings.isNullOrEmpty(quantitaRifornimentoMap.get(pk)) ? new Integer(quantitaRifornimentoMap.get(pk)) : 0;
			Integer days = !Strings.isNullOrEmpty(giorniValiditaRichiesteRifornimentoMap.get(pk)) ? new Integer(giorniValiditaRichiesteRifornimentoMap.get(pk)) : 0;
			//19/02/2016 - Controllo dei giorni di scadenza inseriti 
			if (days < 1 || days > 70) {
				throw new IGerivBusinessException(IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.msg.day.richiesta.rifornimento"));
			}
			
			Timestamp dtScadenza = buildDataScadenzaFromDays(days);
			if (quantitaRichiesta < 0) {
				throw new IGerivBusinessException(IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.msg.qta.richiesta.negativa"));
			}
			RichiestaRifornimentoVo vo = selectUnique(richiestaRifornimento, having(on(RichiestaRifornimentoVo.class).getPk().toString(), equalTo(pk)));
			if (vo == null && quantitaRichiesta.equals(0)) {
				continue;
			} else if (vo == null) {
				vo = new RichiestaRifornimentoVo();
				RichiestaRifornimentoPk rpk = new RichiestaRifornimentoPk();
				String[] split = pk.split("\\|");
				rpk.setCodEdicola(new Integer(split[1]));
				Integer codDl = new Integer(split[0]);
				rpk.setCodFiegDl(codDl);
				rpk.setDataOrdine(DateUtilities.parseDate(split[2], DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS));
				Integer idtn = new Integer(split[3]);
				rpk.setIdtn(idtn);
				vo.setPk(rpk);
				vo.setStoricoCopertineVo(pubblicazioniService.getStoricoCopertinaByPk(codDl, idtn));
			}
			if ((vo.getStoricoCopertineVo() != null && vo.getStoricoCopertineVo().getCopiePerConfezione() != null 
					//&& vo.getStoricoCopertineVo().getIndComponentePaccotto() != null  //Modifica effettuata in merito alla segnalazione del : martedì 22 settembre 2015 18:43 Azzolini
					//&& vo.getStoricoCopertineVo().getIndComponentePaccotto() > 1 
					&& vo.getStoricoCopertineVo().getCopiePerConfezione() > 1)
					&& (quantitaRichiesta % vo.getStoricoCopertineVo().getCopiePerConfezione() != 0)) {
				throw new IGerivBusinessException(IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("dpe.validation.msg.qta.multiplo"), vo.getTitolo().toUpperCase(), vo.getStoricoCopertineVo().getCopiePerConfezione()));
			} 
			vo.setDataScadenzaRichiesta(dtScadenza);
			vo.setGiorniValiditaRichiesteRifornimento(days);
			vo.setQuantitaRichiesta(quantitaRichiesta);
			vo.setNoteVendita((noteVenditaMap.get(pk) != null) ? noteVenditaMap.get(pk).trim() : "");
			retSet.add(vo);
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
	 * Ritorna la lista di RichiestaClienteVo caricata dal db con gli attributi
	 * introdotti dall'utente via form.
	 * 
	 * @param List
	 *            <RichiestaRifornimentoVo> richiestaRifornimento
	 * @param Set
	 *            pk
	 * @param String
	 *            quantitaRifornimento
	 * @return List<RichiestaRifornimentoVo>
	 * @throws ParseException
	 */
	private List<RichiestaClienteVo> buildListRichiestaClienteVo(List<RichiestaClienteVo> richiestaRifornimento, Set<String> pkSet, String quantitaRifornimento) throws ParseException {
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
		int i = 0;
		bolleRiassunto = bolleService.getBolleRiassunto(new Integer[] { getAuthUser().getCodFiegDl() }, new Integer[] { getAuthUser().getId() }, null);
		List<KeyValueDto> listSoloRighaDaSpuntare = buildMapSoloRighaDaSpuntare();
		List<DateTipiBollaDto> listDateTipiBolla = new ArrayList<DateTipiBollaDto>();
		int count = 0;
		listDateTipiBolla = new ArrayList<DateTipiBollaDto>();
		for (BollaRiassuntoVo vo : bolleRiassunto) {
			DateTipiBollaDto dto = new DateTipiBollaDto();
			BollaRiassuntoVo brvo = (BollaRiassuntoVo) vo;
			
			
			Integer codDLMaster = getAuthUser().getCodFiegDlMaster();
			Integer roleIdProfile = getAuthUser().getRoleIdProfile();
			String roleNameProfile = getAuthUser().getRoleNameProfile();
			
			if(getAuthUser().getCodFiegDlMaster().equals(IGerivConstants.CDL_CODE) 
						&& roleIdProfile.equals(IGerivConstants.CDL_ROLE_IGERIV_BASIC_CDL)|| roleIdProfile.equals(IGerivConstants.CDL_ROLE_IGERIV_BASIC_CDL_SOTTOUTENTE)){
				//17/01/2017 - Solo se il CODDL è CDL e il profilo dell'utente è 101 - ROLE_IGERIV_BASIC_CDL
				Date dateBolla = new Date(brvo.getPk().getDtBolla().getTime());
				//17/01/2017 - Solo se la data della bolla è minore o uguale alla data odierna viene inserita nella lista delle bolle da visualizzare
				if(dateBolla.compareTo(new Date()) <= 0){
					dto.setIdDataTipoBolla(count);
					dto.setDataBollaFormat(DateUtilities.getTimestampAsString(brvo.getPk().getDtBolla(), DateUtilities.FORMATO_DATA));
					dto.setTipoBolla(IGerivConstants.TIPO + " " + brvo.getPk().getTipoBolla());
					// dto.setReadonly((brvo.getBollaTrasmessaDl() != null &&
					// brvo.getBollaTrasmessaDl().intValue() >=
					// IGerivConstants.INDICATORE_RECORD_TRASMESSO_DL) ? false : true);
					if (isProfileBollaReadOnly()) {
						dto.setReadonly(false);
					} else {
						dto.setReadonly((brvo.getBollaTrasmessaDl() != null && brvo.getBollaTrasmessaDl().intValue() >= IGerivConstants.INDICATORE_RECORD_IN_TRASMISSIONE_DL) ? false : true);
					}
					dto.setBollaTrasmessaDl(brvo.getBollaTrasmessaDl());
					dto.setDataRegistrazioneDocumento(brvo.getDataRegistrazioneDocumentoFormat());
					dto.setOraRegistrazioneDocumento(brvo.getOraRegistrazioneDocumentoFormat());
					listDateTipiBolla.add(dto);
					count++;
				}
			}else{
				dto.setIdDataTipoBolla(count);
				dto.setDataBollaFormat(DateUtilities.getTimestampAsString(brvo.getPk().getDtBolla(), DateUtilities.FORMATO_DATA));
				dto.setTipoBolla(IGerivConstants.TIPO + " " + brvo.getPk().getTipoBolla());
				// dto.setReadonly((brvo.getBollaTrasmessaDl() != null &&
				// brvo.getBollaTrasmessaDl().intValue() >=
				// IGerivConstants.INDICATORE_RECORD_TRASMESSO_DL) ? false : true);
				if (isProfileBollaReadOnly()) {
					dto.setReadonly(false);
				} else {
					dto.setReadonly((brvo.getBollaTrasmessaDl() != null && brvo.getBollaTrasmessaDl().intValue() >= IGerivConstants.INDICATORE_RECORD_IN_TRASMISSIONE_DL) ? false : true);
				}
				dto.setBollaTrasmessaDl(brvo.getBollaTrasmessaDl());
				dto.setDataRegistrazioneDocumento(brvo.getDataRegistrazioneDocumentoFormat());
				dto.setOraRegistrazioneDocumento(brvo.getOraRegistrazioneDocumentoFormat());
				listDateTipiBolla.add(dto);
				count++;
			}
			
			
			
		}
		requestMap.put("listDateTipiBolla", listDateTipiBolla);
		requestMap.put("listSoloRighaDaSpuntare", listSoloRighaDaSpuntare);
		if (tipoOperazione == null || tipoOperazione.equals("")) {
			tipoOperazione = "1";
		}
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

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
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
	
	private Boolean isProfileBollaReadOnly() {
		boolean result = false;
		if (getAuthUser() != null && getAuthUser().getGruppoModuliVo() != null) {
			result = getAuthUser().getGruppoModuliVo().getIsBollaConsegnaReadOnly();
		}
		return result;
	}

}
