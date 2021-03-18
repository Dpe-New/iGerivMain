package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.selectFirst;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.nullValue;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.bolle.BolleService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.dto.BollaResaBaseDto;
import it.dpe.igeriv.dto.BollaResaDettaglioDto;
import it.dpe.igeriv.dto.DateTipiBollaDto;
import it.dpe.igeriv.dto.LavorazioneResaImmagineDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.dto.PubblicazioneFornito;
import it.dpe.igeriv.dto.QuadraturaResaDto;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivUtils;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.BollaResa;
import it.dpe.igeriv.vo.BollaResaFuoriVoceVo;
import it.dpe.igeriv.vo.BollaResaRiassunto;
import it.dpe.igeriv.vo.BollaResaRiassuntoVo;
import it.dpe.igeriv.vo.BollaResaRichiamoPersonalizzatoVo;
import it.dpe.igeriv.vo.LavorazioneResaVo;
import it.dpe.igeriv.vo.PrenotazioneVo;
import it.dpe.igeriv.vo.pk.BollaResaRichiamoPersonalizzatoPk;
import lombok.Getter;
import lombok.Setter;


/**
 * Classe action per la bolla di resa.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("bollaResaAction")
@SuppressWarnings({"unchecked", "rawtypes"}) 
public class BollaResaAction<T extends BaseVo> extends RestrictedAccessBaseAction implements State, 
		RequestAware {
	private static final long serialVersionUID = 1L;
	private final BolleService bolleService;
	private final PubblicazioniService pubblicazioniService;
	private final String crumbName = getText("igeriv.bolla.resa");
	private final String crumbNameResaDim = getText("igeriv.resa.dimenticata");
	private final String crumbNameViewLavResa = getText("igeriv.visualizza.lavorazione.resa");
	private final String crumbNameResaRiscResp = getText("igeriv.resa.riscontrata.respinta");
	private final String crumbNameResaFuoriVoce = getText("plg.fuori.voce");
	private String tipoOperazione;
	private String dataTipoBolla;
	private Timestamp dtBolla;
	private String tipoBolla;
	private Integer gruppoSconto;
	private Boolean bollaEnabled;
	private List itensBolla;
	
	private List itensBolla_cestaB1;
	private List itensBolla_cestaB2;

	
	private List<QuadraturaResaDto> listQuadraturaResa;
	private String idtn;
	private String filterTitle;
	private String actionName;
	private Integer tableHeight;
	private PrenotazioneVo prenotazioneVo;
	private boolean disableForm;
	private List<BollaResaFuoriVoceVo> itensBollaResaFuoriVoce;
	private String cpu;
	private String coddl;
	private String crivw;
	private Map<String, String> reso;
	private Map<String, String> resoRichiamo;
	private Map<String, String> resoFuoriVoce;
	private Map<String, String> modificato;
	private Map<String, String> modificatoRR;
	private Map<String, String> modificatoFV;
	private String autoincrement;
	private String exportOnlyResoValorizzato;
	private String BollaResaTab_ev;
	private boolean showNumeriOmogenei = false;
	private Integer forzaNonRespingere;
	private Boolean soloResoDaInserire;
	private Boolean soloResoConGiacenza;
	private Boolean hasBolleQuotidianiPeriodiciDivise;
	private AgenzieService agenzieService;
	private String nroCesteResa;
	
	public BollaResaAction() {
		this.bolleService = null;
		this.pubblicazioniService = null;
		this.agenzieService = null;
	}
	
	@Autowired
	public BollaResaAction(BolleService bolleService, PubblicazioniService pubblicazioniService,AgenzieService agenzieService) {
		this.bolleService = bolleService;
		this.pubblicazioniService = pubblicazioniService;
		this.agenzieService = agenzieService;
	}
	
	@Override
	public void prepare() throws Exception {
		//try {
			super.prepare();
			requestMap.put("inserisciNuovo", getText("plg.inserisci.nuovo.fuori.voce"));
			requestMap.put("memorizza", getText("igeriv.memorizza"));
			requestMap.put("memorizzaCtrlM", getText("igeriv.memorizza.ctrl.m"));
			requestMap.put("memorizzaInvia", getText("igeriv.memorizza.invia"));
			requestMap.put("memorizzaInviaCtrlI", getText("igeriv.memorizza.invia.ctrl.i"));
			requestMap.put("time", DateUtilities.getTimestampAsString(new Date(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS));
		//} catch (Throwable e) {
		//	addActionError(getText("gp.errore.imprevisto"));
		//}
	}
	
	@Override
	public void validate() {
		try {
			if (!Strings.isNullOrEmpty(getActionName()) && getActionName().equals("bollaResa_showBollaResaDimenticata.action")) {
				try {
					if (getAuthUser().getEdicolaDeviettiTodis() && !getAuthUser().getEdicolaTestPerModifiche()) {
						List<PubblicazioneDto> listCopertine = pubblicazioniService.getCopertineByTitoloBarcodeCpu(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), null, null, true, false, new Integer(cpu), null, null, null, null, getAuthUser().isDlInforiv());
						if (listCopertine.isEmpty() || (!listCopertine.isEmpty() && !listCopertine.get(0).getCoddl().equals(getAuthUser().getCodFiegDl()))) {
							requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + (listCopertine.isEmpty() ? getText("error.pubblicazione.non.fornita.dal.dl.corrente.1") : MessageFormat.format(getText("error.pubblicazione.non.fornita.dal.dl.corrente"), new Object[]{listCopertine.get(0).getTitolo()})) + IGerivConstants.END_EXCEPTION_TAG);
							throw new IGerivRuntimeException();
						}
					}
					setDataTipoGruppoScontoBolla();
					AnagraficaAgenziaVo agenziaVo = agenzieService.getAgenziaByCodice(getAuthUser().getCodFiegDl());
					Boolean allowResaDimenticata = bolleService.allowResaDimenticata(getAuthUser().getCodFiegDl(), getAuthUser().getId(), dtBolla, tipoBolla, new Integer(cpu), getAuthUser().getNumMaxCpuResaDimenticata(),agenziaVo.getTipoResaNoRifornimentoDimenticato(), getAuthUser().getGiornoSettimanaPermessaResaDimenticata());
					
					//Boolean allowResaContoDeposito = (tipoBolla.equals(agenziaVo.getTipoResaNoContoDeposito()));
					
					if (!allowResaDimenticata) {
						String msgErrorCustomized = "";
						if(getAuthUser().getCodFiegDl().intValue() == IGerivConstants.COD_FIEG_DL_CDL){
							msgErrorCustomized = IGerivConstants.START_EXCEPTION_TAG + getText("error.numero.massimo.resa.dimeticata.cdl") + IGerivConstants.END_EXCEPTION_TAG;
						}else{
							msgErrorCustomized = IGerivConstants.START_EXCEPTION_TAG + getText("error.numero.massimo.resa.dimeticata") + IGerivConstants.END_EXCEPTION_TAG;	
						}	
						requestMap.put("igerivException", msgErrorCustomized);
						throw new IGerivRuntimeException();
					} 
				} catch (ParseException e) {
					requestMap.put("igerivException", getText("igeriv.errore.esecuzione.procedura") + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				}
			}
			
			if (!Strings.isNullOrEmpty(dataTipoBolla) && dataTipoBolla.contains(",")) {
				dataTipoBolla = dataTipoBolla.substring(0, dataTipoBolla.indexOf(",")).trim();
			}
			if (!Strings.isNullOrEmpty(autoincrement) && autoincrement.contains(",")) {
				autoincrement = autoincrement.substring(0, autoincrement.indexOf(",")).trim();
			}

		} catch (IGerivRuntimeException e) {
			throw e;
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
	}
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showFilter() {
		filterTitle = getText("igeriv.bolla.resa");
		actionName = "bollaResa_showBollaResa.action";
		// Vittorio 26/08/2020
//		Set<BollaResaRiassuntoVo> bolleResaRiassunto = bolleService.getBolleResaRiassunto(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), null);
		Integer[] arrCodFiegDl = (getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv() && !getAuthUser().getGesSepDevTod()) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
		Integer[] arrId = (getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv() && !getAuthUser().getGesSepDevTod()) ? getAuthUser().getArrId() : new Integer[]{getAuthUser().getCodDpeWebEdicola()};
		Set<BollaResaRiassuntoVo> bolleResaRiassunto = bolleService.getBolleResaRiassunto(arrCodFiegDl, arrId, null);
		doShowFilter(bolleResaRiassunto);
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameResaDim}")
	@SkipValidation
	public String showFilterResaDimenticata() {
		// TODO VITTORIO
		filterTitle = getText("igeriv.resa.dimenticata");
		actionName = "resaDimenticata_showBollaResaDimenticata.action";
		Set<BollaResaRiassuntoVo> bolleResaRiassunto = bolleService.getBolleResaRiassunto(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), null);
		doShowFilter(bolleResaRiassunto);
		if (sessionMap.get("dataTipoBolla") != null) {
			dataTipoBolla = (String) sessionMap.get("dataTipoBolla");
		}
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameViewLavResa}")
	@SkipValidation
	public String showFilterLavorazioneResa() {
		filterTitle = getText("igeriv.visualizza.lavorazione.resa");
		actionName = "lavorazioneResa_showBollaLavorazioneResa.action";
		List<LavorazioneResaVo> bolleResaRiassunto = bolleService.getBolleResaRiassuntoLavorazioneResa(getAuthUser().getCodFiegDl(), getAuthUser().getId());
		doShowFilter(bolleResaRiassunto);
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbName}")
	public String showBollaResa() {
		try {
			filterTitle = getText("igeriv.bolla.resa");
			if (dataTipoBolla == null || dataTipoBolla.equals("")) {
				return "input";
			}
			// Vittorio 26/08/2020
			//Set<BollaResaRiassuntoVo> bolleResaRiassunto = bolleService.getBolleResaRiassunto(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), null);
			Integer[] arrCodFiegDl = (getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv() && !getAuthUser().getGesSepDevTod()) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
			Integer[] arrId = (getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv() && !getAuthUser().getGesSepDevTod()) ? getAuthUser().getArrId() : new Integer[]{getAuthUser().getCodDpeWebEdicola()};
			Set<BollaResaRiassuntoVo> bolleResaRiassunto = bolleService.getBolleResaRiassunto(arrCodFiegDl, arrId, null);
			doShowFilter(bolleResaRiassunto); 
			setDataTipoBolla();
			List<BollaResaRiassuntoVo> bolleResaRiassuntoData = bolleService.getBollaResaRiassunto(new Integer[]{getAuthUser().getCodFiegDl()}, new Integer[]{getAuthUser().getCodDpeWebEdicola()}, dtBolla, tipoBolla);
			if (bolleResaRiassuntoData!= null && !bolleResaRiassuntoData.isEmpty()) {
				BollaResaRiassuntoVo bollaResaRiassuntoData = bolleResaRiassuntoData.get(0);
				nroCesteResa = bollaResaRiassuntoData.getNumeroColli()!=null
						? bollaResaRiassuntoData.getNumeroColli().toString()
						: "";
			}
			
			// 25/01/2017 Gestione Ceste CDL - Ordinamento standard raggruppato per tipo cesta
			if(getAuthUser().isEdicolaCDLBologna()){
				
				//DA MODIFICARE 25/01/2017 Gestione Ceste CDL - Ordinamento parametrizzato 
				//Map<String, ParametriEdicolaDto> mapParam = (Map<String, ParametriEdicolaDto>) sessionMap.get(IGerivConstants.SESSION_VAR_MAP_PARAMETRI_EDICOLA);
				//boolean cdlOrdinamentoRaggruppatoPerCesta = mapParam.containsKey(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_ABILITA_MODALITA_INFORIV_BOLLE) ? Boolean.valueOf(mapParam.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_ABILITA_MODALITA_INFORIV_BOLLE).getParamValue()) : false;
				boolean cdlOrdinamentoRaggruppatoPerCesta = true;
				if(cdlOrdinamentoRaggruppatoPerCesta && tipoBolla.equals("B")){
					
					itensBolla_cestaB1 = bolleService.getDettaglioBollaResaCDLCeste(arrCodFiegDl, arrId, dtBolla, tipoBolla, soloResoDaInserire,soloResoConGiacenza,IGerivConstants.CDL_CESTA_1);
					itensBolla_cestaB2 = bolleService.getDettaglioBollaResaCDLCeste(arrCodFiegDl, arrId, dtBolla, tipoBolla, soloResoDaInserire,soloResoConGiacenza,IGerivConstants.CDL_CESTA_2);
					itensBolla = new ArrayList<BollaResaDettaglioDto>();
					
					Iterator<BollaResaDettaglioDto> it_B1 = itensBolla_cestaB1.iterator();
					while (it_B1.hasNext()) {
						BollaResaDettaglioDto bollaResaDettaglioDto_B1 = (BollaResaDettaglioDto) it_B1.next();
						itensBolla.add(bollaResaDettaglioDto_B1);
					}
					Iterator<BollaResaDettaglioDto> it_B2 = itensBolla_cestaB2.iterator();
					while (it_B2.hasNext()) {
						BollaResaDettaglioDto bollaResaDettaglioDto_B2 = (BollaResaDettaglioDto) it_B2.next();
						itensBolla.add(bollaResaDettaglioDto_B2);
					}
				}else{
					itensBolla = bolleService.getDettaglioBollaResa(arrCodFiegDl, arrId, dtBolla, tipoBolla, soloResoDaInserire,soloResoConGiacenza);	
				}

			}else{
				/* Recupero dati bolla  */
				itensBolla = bolleService.getDettaglioBollaResa(arrCodFiegDl, arrId, dtBolla, tipoBolla, soloResoDaInserire,soloResoConGiacenza);
			}
		
			

			
			List dettaglioRichiamoPersonalizzato = bolleService.getDettaglioRichiamoPersonalizzato(arrCodFiegDl, arrId, dtBolla, tipoBolla, soloResoDaInserire);
			List dettaglioFuoriVoce = bolleService.getDettaglioFuoriVoce(arrCodFiegDl, arrId, dtBolla, tipoBolla, soloResoDaInserire);
			itensBolla.addAll(dettaglioRichiamoPersonalizzato);
			itensBolla = buildOrderedBollaResaDettagliList(itensBolla, dettaglioFuoriVoce);
			String time = itensBolla != null && !itensBolla.isEmpty() && ((BollaResaBaseDto) itensBolla.get(0)) != null && ((BollaResaBaseDto) itensBolla.get(0)).getCreated() != null ? DateUtilities.getTimestampAsString(((BollaResaBaseDto) itensBolla.get(0)).getCreated(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS) : DateUtilities.getTimestampAsString(new Date(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS);
			int count = 0;
			for (Object vo : itensBolla) {
				BeanUtils.setProperty(vo, "rownum", ++count);
			}
			Object bollaTipoA = selectFirst(bolleResaRiassunto, having(on(BollaResaRiassuntoVo.class).getPk().getTipoBolla(), equalTo(IGerivConstants.TIPO_BOLLA_QUOTIDIANI)));
			Object bollaTipoB = selectFirst(bolleResaRiassunto, having(on(BollaResaRiassuntoVo.class).getPk().getTipoBolla(), equalTo(IGerivConstants.TIPO_BOLLA_PERIODICI)));
			hasBolleQuotidianiPeriodiciDivise = bollaTipoA != null && bollaTipoB != null;
			requestMap.put("itensBolla", itensBolla);
			requestMap.put("time", time);
			
			
			requestMap.put("gestceste_dtBolla", dtBolla);
			requestMap.put("gestceste_tipoBolla", tipoBolla);
			
			
			AnagraficaAgenziaVo agenziaVo = agenzieService.getAgenziaByCodice(getAuthUser().getCodFiegDl());
			
			if(agenziaVo.getTiboresval() != null && agenziaVo.getTiboresval().equals(tipoBolla))
			{
				requestMap.put("strOnSubmitFunction", "{setTimeout(function() {$('#exportOnlyResoValorizzato').val('false'); strToReplace; document.forms.BollaResaForm.ec_eti.value='';unBlockUI();}, 1000); return false;}");
			}
			else
			{
				requestMap.put("strOnSubmitFunction", "jConfirmEsportaResoPdf('" + getText("gp.esportare.solo.reso.valorizzato") + "', attenzioneMsg, function(val) {if (val == 1) { $('#exportOnlyResoValorizzato').val('true'); strToReplace; document.forms.BollaResaForm.ec_eti.value=''} else if (val == 2) { $('#exportOnlyResoValorizzato').val('false'); strToReplace; document.forms.BollaResaForm.ec_eti.value=''} else {unBlockUI(); document.forms.BollaResaForm.ec_eti.value=''} })");
			}

			disableForm = !bollaEnabled;
		} catch (Throwable e) {
			addActionError(getText("gp.errore.imprevisto"));
			return INPUT;
		}
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameResaRiscResp}")
	@SkipValidation
	public String showQuadraturaResaFilter() {
		doShowFilter(bolleService.getBolleResaRiassuntoConResaRiscontrata(getAuthUser().getCodFiegDl(), getAuthUser().getId()));
		filterTitle = getText("igeriv.resa.riscontrata.respinta");
		actionName = "bollaResa_showQuadraturaResa.action";
		return "showReportQuadraturaResa";
	}
	
	@BreadCrumb("%{crumbNameResaRiscResp}")
	@SkipValidation
	public String showQuadraturaResa() {
		doShowFilter(bolleService.getBolleResaRiassuntoConResaRiscontrata(getAuthUser().getCodFiegDl(), getAuthUser().getId()));
		filterTitle = getText("igeriv.resa.riscontrata.respinta");
		try {
			setDataTipoBolla();
			listQuadraturaResa = bolleService.getQuadraturaResa(getAuthUser().getCodFiegDl(), getAuthUser().getId(), dtBolla, tipoBolla);
			requestMap.put("listQuadraturaResa", listQuadraturaResa);
			requestMap.put("title", getText("igeriv.resa.riscontrata.respinta"));
		} catch (Throwable e) {
			addActionError(getText("gp.errore.imprevisto"));
			return INPUT;
		}
		return "showReportQuadraturaResa";
	}
	
	public String showBollaResaFuoriVoce() {
		try {
			AnagraficaAgenziaVo agenziaVo = agenzieService.getAgenziaByCodice(getAuthUser().getCodFiegDl());
			setDataTipoGruppoScontoBolla();
			if (!Strings.isNullOrEmpty(cpu) && !Strings.isNullOrEmpty(coddl) && !Strings.isNullOrEmpty(crivw)) {
				itensBollaResaFuoriVoce = bolleService.buildNuoviDettagliFuoriVoce(new Integer(coddl), new Integer(crivw), dtBolla, tipoBolla, new Integer(cpu), gruppoSconto, showNumeriOmogenei, getAuthUser().getDataStorico(),agenziaVo.getTipoResaNoContoDeposito(), getAuthUser().getAccettoResaCD());
				// TODO SECONDA CINTURA
				boolean isEdicolaDeviettiTodis = getAuthUser().getCodFiegDl().equals(IGerivConstants.COD_FIEG_DL_DEVIETTI) || getAuthUser().getCodFiegDl().equals(IGerivConstants.COD_FIEG_DL_TODIS);
				Integer agenziaFatturazione = getAuthUser().getAgenziaFatturazione();
				if (getAuthUser().isMultiDl() && isEdicolaDeviettiTodis && getAuthUser().getGesSepDevTod() && agenziaFatturazione != null && agenziaFatturazione > 0) {
					Boolean isEdicolaSecondaCintura = getAuthUser().getEdSecCintura();
					Timestamp dtPartenzaSecondaCintura = getAuthUser().getDtPartSecondaCintura();
					if (isEdicolaSecondaCintura != null && isEdicolaSecondaCintura && dtPartenzaSecondaCintura != null) {
						Iterator<BollaResaFuoriVoceVo> it = (Iterator<BollaResaFuoriVoceVo>) itensBollaResaFuoriVoce.iterator();
						while (it.hasNext()) {
							BollaResaFuoriVoceVo vo = it.next();
							if (vo.getDataUscita() != null && vo.getEditore()!=null && IGerivUtils.isFornitoreDevTodisComune(vo.getEditore())) {
								if (getAuthUser().getCodFiegDl().equals(IGerivConstants.COD_FIEG_DL_DEVIETTI) && vo.getDataUscita().compareTo(dtPartenzaSecondaCintura)>=0) {
									it.remove();
								}
								if (getAuthUser().getCodFiegDl().equals(IGerivConstants.COD_FIEG_DL_TODIS) && vo.getDataUscita().compareTo(dtPartenzaSecondaCintura)<0) {
									it.remove();
								}
							}
						}

					}
				}
			} 
			
			// CDL 17/03/17 - 08/05/2017 ADD LANZA
			boolean controlloNuovo = false;
			if (agenziaVo.getTipoResaNoRifornimentoDimenticato() == null || !agenziaVo.getTipoResaNoRifornimentoDimenticato().equals(tipoBolla)) {
				controlloNuovo = getAuthUser().getCodFiegDl().equals(IGerivConstants.COD_FIEG_DL_CDL) || 
						getAuthUser().getCodFiegDl().equals(IGerivConstants.COD_FIEG_DL_REBECCHI) || 
								getAuthUser().getCodFiegDl().equals(IGerivConstants.COD_FIEG_DL_LANZA);
			}

			itensBollaResaFuoriVoce = controlloNuovo
					? select(itensBollaResaFuoriVoce, having(on(BollaResaFuoriVoceVo.class).getCodMotivoRespinto(), nullValue()).or(having(on(BollaResaFuoriVoceVo.class).getCodMotivoRespinto(), equalTo(0))))
					: select(itensBollaResaFuoriVoce, having(on(BollaResaFuoriVoceVo.class).getCodMotivoRespinto(), nullValue()).or(having(on(BollaResaFuoriVoceVo.class).getCodMotivoRespinto(), equalTo(0))).or(having(on(BollaResaFuoriVoceVo.class).getCodMotivoRespinto(), equalTo(IGerivConstants.COD_MOTIVO_RESPINTO_NUOVO))));
			requestMap.put("itensBollaResaFuoriVoce", itensBollaResaFuoriVoce);
			requestMap.put("title", getText("plg.fuori.voce") + ": " + ((itensBollaResaFuoriVoce != null && !itensBollaResaFuoriVoce.isEmpty()) ? itensBollaResaFuoriVoce.get(0).getTitolo() : "").replaceAll("&nbsp;", " ").replaceAll("\\.", " "));
			disableForm = !bollaEnabled;
		} catch (Throwable e) {
			addActionError(getText("gp.errore.imprevisto"));
			return INPUT;
		}
		return IGerivConstants.ACTION_BOLLA_RESA_FUORI_VOCE;
	}
	
	public String showBollaResaDimenticata() {
		//showNumeriOmogenei = true;
		showNumeriOmogenei = false;
		return showBollaResaFuoriVoce();
	}
	
	@BreadCrumb("%{crumbNameViewLavResa}")
	@SkipValidation
	public String showBollaLavorazioneResa() {
		filterTitle = getText("igeriv.visualizza.lavorazione.resa");
		try {
			setDataTipoBolla();
			LavorazioneResaVo lavorazioneResaVo = bolleService.getLavorazioneResaVo(getAuthUser().getCodFiegDl(), getAuthUser().getId(), dtBolla, tipoBolla);
			if (lavorazioneResaVo != null) {
				List<LavorazioneResaImmagineDto> listLavorazioneResaImmagineVo = bolleService.getListLavorazioneResaImmagineVo(lavorazioneResaVo.getNomeFile());
				requestMap.put("lavorazioneResaVo", lavorazioneResaVo);
				requestMap.put("listLavorazioneResaImmagineVo", listLavorazioneResaImmagineVo);
			}
		} catch (Throwable e) {
			addActionError(getText("gp.errore.imprevisto"));
			return INPUT;
		}
		return SUCCESS;
	}
	
	public String save() {
		try {
			Integer nroCeste = null;
			if (nroCesteResa != null && !nroCesteResa.trim().equals("")) {
				try {
					nroCeste = new Integer(nroCesteResa.trim().replaceAll("\\+", ""));
					if (nroCeste > 99) {
						requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("dpe.validation.msg.numero.colli"), 99) + IGerivConstants.END_EXCEPTION_TAG);
						throw new IGerivRuntimeException();
					} else if (nroCeste < 0) {
						requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.msg.numero.colli") + IGerivConstants.END_EXCEPTION_TAG);
						throw new IGerivRuntimeException();
					}
				} catch (Exception e) {
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.msg.numero.colli") + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				}
			}
			setDataTipoBolla();
			// Vittorio 26/08/2020
			// List<BollaResaRiassuntoVo> bollaResaRiassunto = bolleService.getBollaResaRiassunto(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), dtBolla, tipoBolla);
			Integer[] arrCodFiegDl = (getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv() && !getAuthUser().getGesSepDevTod()) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
			Integer[] arrId = (getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv() && !getAuthUser().getGesSepDevTod()) ? getAuthUser().getArrId() : new Integer[]{getAuthUser().getCodDpeWebEdicola()};
			List<BollaResaRiassuntoVo> bollaResaRiassunto = bolleService.getBollaResaRiassunto(arrCodFiegDl, arrId, dtBolla, tipoBolla);
			if (select(bollaResaRiassunto, having(on(BollaResaRiassuntoVo.class).getBollaTrasmessaDl().intValue(), greaterThanOrEqualTo(IGerivConstants.INDICATORE_RECORD_IN_TRASMISSIONE_DL))).size() > 0) {
	    		requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("impossibile.salvare.bolla.gia.trasmessa") + IGerivConstants.END_EXCEPTION_TAG);
	    		throw new IGerivRuntimeException();
	    	}
	    	Map<String, String> resoMod = new HashMap<String, String>();
			Map<String, String> resoFuoriVoceMod = new HashMap<String, String>();
			Map<String, String> resoRichiamoMod = new HashMap<String, String>();
			buildModifiedMaps(resoMod, resoFuoriVoceMod, resoRichiamoMod);
			for (BollaResaRiassuntoVo vo: bollaResaRiassunto) {
				vo.setNumeroColli(nroCeste);
			}
			bolleService.saveBollaResa(resoMod, resoFuoriVoceMod, resoRichiamoMod, bollaResaRiassunto);
			//bolleService.saveBollaResa(resoMod, resoFuoriVoceMod, resoRichiamoMod);
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
			Integer nroCeste = null;
			if (nroCesteResa != null && !nroCesteResa.trim().equals("")) {
				try {
					nroCeste = new Integer(nroCesteResa.trim().replaceAll("\\+", ""));
					if (nroCeste > 99) {
						requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("dpe.validation.msg.numero.colli"), 99) + IGerivConstants.END_EXCEPTION_TAG);
						throw new IGerivRuntimeException();
					} else if (nroCeste < 0) {
						requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.msg.numero.colli") + IGerivConstants.END_EXCEPTION_TAG);
						throw new IGerivRuntimeException();
					}
				} catch (Exception e) {
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.msg.numero.colli") + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				}
			}
			setDataTipoBolla();
			// Vittorio 26/08/2020
			//List<BollaResaRiassuntoVo> bollaResaRiassunto = bolleService.getBollaResaRiassunto(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), dtBolla, tipoBolla);
			Integer[] arrCodFiegDl = (getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv() && !getAuthUser().getGesSepDevTod()) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
			Integer[] arrId = (getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv() && !getAuthUser().getGesSepDevTod()) ? getAuthUser().getArrId() : new Integer[]{getAuthUser().getCodDpeWebEdicola()};
			List<BollaResaRiassuntoVo> bollaResaRiassunto = bolleService.getBollaResaRiassunto(arrCodFiegDl, arrId, dtBolla, tipoBolla);
			if (select(bollaResaRiassunto, having(on(BollaResaRiassuntoVo.class).getBollaTrasmessaDl().intValue(), greaterThanOrEqualTo(IGerivConstants.INDICATORE_RECORD_IN_TRASMISSIONE_DL))).size() > 0) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("impossibile.salvare.bolla.gia.trasmessa") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
	    	}
			Map<String, String> resoMod = new HashMap<String, String>();
			Map<String, String> resoFuoriVoceMod = new HashMap<String, String>();
			Map<String, String> resoRichiamoMod = new HashMap<String, String>();
			buildModifiedMaps(resoMod, resoFuoriVoceMod, resoRichiamoMod);
			for (BollaResaRiassuntoVo vo: bollaResaRiassunto) {
				vo.setNumeroColli(nroCeste);
			}
			bolleService.saveAndSendBollaResa(resoMod, resoFuoriVoceMod, resoRichiamoMod, bollaResaRiassunto);
		} catch (IGerivRuntimeException e) {
			throw e;
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "save";
	}
	
	public String saveBollaResaFuoriVoce() {
		try {
			setDataTipoBolla(); 
			showBollaResaFuoriVoce();
			List<BollaResaFuoriVoceVo> itensBolla = (List<BollaResaFuoriVoceVo>) requestMap.get("itensBollaResaFuoriVoce");
			List<BollaResaFuoriVoceVo> listBollaDettaglioVo = buildListBollaDettaglioVo(itensBolla, resoFuoriVoce);
			List<BollaResaRichiamoPersonalizzatoVo> listResaRichiamoPersonalizzato = extractListRichiamoPersonalizzato(listBollaDettaglioVo);

			// Vittorio 26/08/2020
//			List<BollaResaRiassuntoVo> bollaResaRiassunto = bolleService.getBollaResaRiassunto(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), dtBolla, tipoBolla);
			Integer[] arrCodFiegDl = (getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv() && !getAuthUser().getGesSepDevTod()) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
			Integer[] arrId = (getAuthUser().isMultiDl() && !getAuthUser().isDlInforiv() && !getAuthUser().getGesSepDevTod()) ? getAuthUser().getArrId() : new Integer[]{getAuthUser().getCodDpeWebEdicola()};
			List<BollaResaRiassuntoVo> bollaResaRiassunto = bolleService.getBollaResaRiassunto(arrCodFiegDl, arrId, dtBolla, tipoBolla);
			
			if (select(bollaResaRiassunto, having(on(BollaResaRiassuntoVo.class).getBollaTrasmessaDl().intValue(), greaterThanOrEqualTo(IGerivConstants.INDICATORE_RECORD_IN_TRASMISSIONE_DL))).size() > 0) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("impossibile.salvare.bolla.gia.trasmessa") + IGerivConstants.END_EXCEPTION_TAG);
	    		throw new IGerivRuntimeException();
	    	}
	    	if (!listBollaDettaglioVo.isEmpty()) {
	    		bolleService.saveVoList(listBollaDettaglioVo);
	    	}
	    	if (!listResaRichiamoPersonalizzato.isEmpty()) {
	    		bolleService.saveVoList(listResaRichiamoPersonalizzato);
	    	}
		} catch (IGerivRuntimeException e) {
			throw e;
		} catch (Throwable e) {
			addActionError(getText("gp.errore.imprevisto"));
			return INPUT;
		}
		return IGerivConstants.ACTION_BOLLA_RESA_FUORI_VOCE;
	}
	
	/**
	 * Rimuove dalla lista i vo che devono essere convertiti in BollaResaRichiamoPersonalizzatoVo
	 * e costruisce una nuova lista di BollaResaRichiamoPersonalizzatoVo
	 * 
	 * @param listBollaDettaglioVo
	 * @return
	 */
	private List<BollaResaRichiamoPersonalizzatoVo> extractListRichiamoPersonalizzato(List<BollaResaFuoriVoceVo> listBollaDettaglioVo) {
		List<BollaResaRichiamoPersonalizzatoVo> list = new ArrayList<BollaResaRichiamoPersonalizzatoVo>();
		Iterator<BollaResaFuoriVoceVo> iter = listBollaDettaglioVo.iterator();
		while (iter.hasNext()) {
			BollaResaFuoriVoceVo vo = iter.next();
			if (vo.getRichiamoPersonalizzato()) {
				iter.remove();
				BollaResaRichiamoPersonalizzatoVo rp = bolleService.getBollaResaRichiamoPersonalizzato(vo.getPk().getCodFiegDl(), vo.getPk().getCodEdicola(), vo.getPk().getDtBolla(), vo.getPk().getTipoBolla(), vo.getPk().getPosizioneRiga());
				if (rp == null) {
					rp = new BollaResaRichiamoPersonalizzatoVo();
					BollaResaRichiamoPersonalizzatoPk pk = new BollaResaRichiamoPersonalizzatoPk();
					pk.setCodFiegDl(vo.getPk().getCodFiegDl());
					pk.setCodEdicola(vo.getPk().getCodEdicola());
					pk.setDtBolla(vo.getPk().getDtBolla());
					pk.setTipoBolla(vo.getPk().getTipoBolla());
					pk.setPosizioneRiga(vo.getPk().getPosizioneRiga());
					rp.setPk(pk);
				}
				rp.setCpuDl(vo.getCpuDl());
				rp.setIdtn(vo.getIdtn());
				rp.setDistribuito(vo.getDistribuito());
				rp.setGiacenza(vo.getGiacenza());
				rp.setNumeroPubblicazione(vo.getNumeroPubblicazione());
				rp.setTitolo(vo.getTitolo());
				rp.setSottoTitolo(vo.getSottoTitolo());
				rp.setPrezzoLordo(vo.getPrezzoLordo());
				rp.setPrezzoNetto(vo.getPrezzoNetto());
				rp.setDistribuito(vo.getDistribuito());
				rp.setReso(vo.getReso());
				rp.setResoRiscontrato(vo.getResoRiscontrato());
				list.add(rp);
			}
		}
		return list;
	}

	/**
	 * Verifica se il giorno della bolla di resa coincide con il giorno permesso per la resa dimenticata (se presente)
	 * definito nella tabella anagrafica dell'agenzia.
	 * 
	 * @param Timestamp dtBolla
	 * @return Boolean
	 */
	public Boolean isGiornoBollaResaValidoPerResaDimenticata(Timestamp dtBolla) {
		Boolean isValid = true;
		if (getAuthUser().getGiornoSettimanaPermessaResaDimenticata() != null && (getAuthUser().getGiornoSettimanaPermessaResaDimenticata() >= 1 && getAuthUser().getGiornoSettimanaPermessaResaDimenticata() <= 7)) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dtBolla);
			int day = cal.get(Calendar.DAY_OF_WEEK);
			return day == getAuthUser().getGiornoSettimanaPermessaResaDimenticata().intValue();
		}
		return isValid;
	}
	
	/**
	 * Rimepie le mappe con i valori modificati di resa
	 * 
	 * @param resoMod
	 * @param resoFuoriVoceMod
	 * @param resoRichiamoMod
	 */
	private void buildModifiedMaps(Map<String, String> resoMod, Map<String, String> resoFuoriVoceMod, Map<String, String> resoRichiamoMod) {
		if (modificato != null && !modificato.isEmpty()) {
			for (Map.Entry<String, String> entry : modificato.entrySet()) {
				String pk = entry.getKey();
				Boolean modified = new Boolean(entry.getValue());
				if (modified != null && modified) {
					resoMod.put(pk, reso.get(pk));
				}
			}
		}
		if (modificatoFV != null && !modificatoFV.isEmpty()) {
			for (Map.Entry<String, String> entry : modificatoFV.entrySet()) {
				String pk = entry.getKey();
				Boolean modified = new Boolean(entry.getValue());
				if (modified != null && modified) {
					resoFuoriVoceMod.put(pk, resoFuoriVoce.get(pk));
				}
			}
		}
		if (modificatoRR != null && !modificatoRR.isEmpty()) {
			for (Map.Entry<String, String> entry : modificatoRR.entrySet()) {
				String pk = entry.getKey();
				Boolean modified = new Boolean(entry.getValue());
				if (modified != null && modified) {
					resoRichiamoMod.put(pk, resoRichiamo.get(pk));
				}
			}
		}
	}
	
	/**
	 * @throws ParseException
	 */
	private void setDataTipoBolla() throws ParseException {
		if (!Strings.isNullOrEmpty(dataTipoBolla)) {
			StringTokenizer st = new StringTokenizer(dataTipoBolla, "|");
			String strData = st.nextToken();
			tipoBolla = st.nextToken().replaceFirst(IGerivConstants.TIPO, "").trim();
			bollaEnabled = Boolean.valueOf(st.nextToken().trim());
			dtBolla = DateUtilities.parseDate(strData, DateUtilities.FORMATO_DATA);
			requestMap.put("strData", strData);
			requestMap.put("tipo", tipoBolla);
			if (BollaResaTab_ev != null && (BollaResaTab_ev.toUpperCase().equals("PDF") || BollaResaTab_ev.toUpperCase().equals("XLS"))) {
				setAgenziaEdicolaExportTitle();
			}
		}
	}
	
	/**
	 * @return
	 * @throws ParseException
	 */
	private void setDataTipoGruppoScontoBolla() throws ParseException {
		StringTokenizer st = new StringTokenizer(dataTipoBolla, "|");
		String strData = st.nextToken();
		tipoBolla = st.nextToken().replaceFirst(IGerivConstants.TIPO, "").trim();
		bollaEnabled = Boolean.valueOf(st.nextToken().trim());
		gruppoSconto = Integer.valueOf(st.nextToken().trim());
		dtBolla = DateUtilities.parseDate(strData, DateUtilities.FORMATO_DATA);
	}
	
	/**
	 * @param bolleRiassunto 
	 * 
	 */
	@SuppressWarnings("hiding")
	private <T extends BollaResaRiassunto> void doShowFilter(Collection<T> bolleRiassunto) {
		List<DateTipiBollaDto> listDateTipiBolla = new ArrayList<DateTipiBollaDto>();
		int count = 0;
		listDateTipiBolla = new ArrayList<DateTipiBollaDto>();
		for (BollaResaRiassunto brvo : bolleRiassunto) {
			
			Integer codDLMaster = getAuthUser().getCodFiegDlMaster();
			Integer roleIdProfile = getAuthUser().getRoleIdProfile();
			String roleNameProfile = getAuthUser().getRoleNameProfile();
			
			if(getAuthUser().getCodFiegDlMaster().equals(IGerivConstants.CDL_CODE) 
						&& roleIdProfile.equals(IGerivConstants.CDL_ROLE_IGERIV_BASIC_CDL) || roleIdProfile.equals(IGerivConstants.CDL_ROLE_IGERIV_BASIC_CDL_SOTTOUTENTE)){
				//17/01/2017 - Solo se il CODDL è CDL e il profilo dell'utente è 101 - ROLE_IGERIV_BASIC_CDL
				Date dateBolla = new Date(brvo.getDtBolla().getTime());
				//17/01/2017 - Solo se la data della bolla è minore o uguale alla data odierna viene inserita nella lista delle bolle da visualizzare
				if(dateBolla.compareTo(new Date()) <= 0){
					DateTipiBollaDto dto = new DateTipiBollaDto();
					dto.setIdDataTipoBolla(count);
					dto.setDataBollaFormat(DateUtilities.getTimestampAsString(brvo.getDtBolla(), DateUtilities.FORMATO_DATA));
					dto.setTipoBolla(IGerivConstants.TIPO + " " + brvo.getTipoBolla());
					if (isProfileBollaReadOnly()) {
						dto.setReadonly(false);
					} else {
						dto.setReadonly((brvo.getBollaTrasmessaDl() != null && brvo.getBollaTrasmessaDl().intValue() >= IGerivConstants.INDICATORE_RECORD_IN_TRASMISSIONE_DL) ? false : true);
					}
					dto.setBollaTrasmessaDl(brvo.getBollaTrasmessaDl());
					dto.setGruppoSconto(brvo.getGruppoSconto());
					listDateTipiBolla.add(dto);
					count++;
				}
			}else{
				DateTipiBollaDto dto = new DateTipiBollaDto();
				dto.setIdDataTipoBolla(count);
				dto.setDataBollaFormat(DateUtilities.getTimestampAsString(brvo.getDtBolla(), DateUtilities.FORMATO_DATA));
				dto.setTipoBolla(IGerivConstants.TIPO + " " + brvo.getTipoBolla());
				if (isProfileBollaReadOnly()) {
					dto.setReadonly(false);
				} else {
					dto.setReadonly((brvo.getBollaTrasmessaDl() != null && brvo.getBollaTrasmessaDl().intValue() >= IGerivConstants.INDICATORE_RECORD_IN_TRASMISSIONE_DL) ? false : true);
				}
				dto.setBollaTrasmessaDl(brvo.getBollaTrasmessaDl());
				dto.setGruppoSconto(brvo.getGruppoSconto());
				listDateTipiBolla.add(dto);
				count++;
			}
		}
		if (tipoOperazione == null || tipoOperazione.equals("")) {
			tipoOperazione = "1";
		}
		requestMap.put("listDateTipiBolla", listDateTipiBolla);
	}
	
	/**
	 * Ritorna una lista di VOs BollaResa dove i vo dei dettagli fuori voce
	 * figurano dopo il richiamo bolla corrispondente. 
	 * 
	 * @param List<BollaResa> itensBolla
	 * @param List<BollaResa> dettaglioFuoriVoce
	 * @return List<BollaResa>
	 */
	private List<BollaResa> buildOrderedBollaResaDettagliList(List<BollaResa> itensBolla,
			List<BollaResa> dettaglioFuoriVoce) {
		List<BollaResa> list = new ArrayList<BollaResa>();
		for (BollaResa br : itensBolla) {
			list.add(br);
			Iterator<BollaResa> it = dettaglioFuoriVoce.iterator();
			while (it.hasNext()) {
				BollaResa br1 = (BollaResa) it.next();
				if (br1.getCpuDl().equals(br.getCpuDl())) {
					list.add(br1);		
					it.remove();
				}
			}
		}
		if (!dettaglioFuoriVoce.isEmpty()) {
			list.addAll(dettaglioFuoriVoce);
		}
		return list;
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
	
	/**
	 * Ritorna la lista di BollaResaFuoriVoceVo caricata dal db 
	 * aggornata con la resa intrdotta nel form.
	 * 
	 * @param List<BollaResaFuoriVoceVo> itensBolla
	 * @param Map<String, String> resoFuoriVoceMap
	 * @return List<BollaResaFuoriVoceVo>
	 */
	private List<BollaResaFuoriVoceVo> buildListBollaDettaglioVo(List<BollaResaFuoriVoceVo> itensBolla, Map<String, String> resoFuoriVoceMap) {
		List<BollaResaFuoriVoceVo> retList = new ArrayList<BollaResaFuoriVoceVo>();
		for (BollaResaFuoriVoceVo vo : itensBolla) {
			boolean isDomenica = false;
			if (vo != null) {
				Timestamp dtBolla = vo.getPk().getDtBolla();
				Calendar cal = Calendar.getInstance();
				cal.setTime(dtBolla);
				isDomenica = cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
			}
			for (Map.Entry<String, String> entry : resoFuoriVoceMap.entrySet()) {
				String pkVal = (!entry.getKey().equals("")) ? entry.getKey().trim() : "";
				Integer resoVal = (entry.getValue() != null && !entry.getValue().trim().equals("")) ? new Integer(entry.getValue().trim().replaceAll("\\+", "")) : null;
				if (resoVal != null && pkVal.equals(vo.getPk().toString())) {
					boolean forzaNonRespingere = !Strings.isNullOrEmpty(vo.getMotivoResaRespinta()) && (getAuthUser().getTipoControlloPubblicazioniRespinte() != null && getAuthUser().getTipoControlloPubblicazioniRespinte().equals(2) && this.forzaNonRespingere != null && this.forzaNonRespingere.equals(1));
					boolean respingerePubblicazioni = !Strings.isNullOrEmpty(vo.getMotivoResaRespinta()) && (vo.getCodMotivoRespinto() != null && !vo.getCodMotivoRespinto().equals(2)) && (getAuthUser().getTipoControlloPubblicazioniRespinte() != null && getAuthUser().getTipoControlloPubblicazioniRespinte().equals(1) && !forzaNonRespingere);
					if (!vo.isPermetteContoDeposito() && (vo.getQuantitaCopieContoDeposito() != null && vo.getQuantitaCopieContoDeposito() > 0)) {
						requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("pubblicazione.in.conto.deposito"), vo.getTitolo(), vo.getNumeroPubblicazione()) + IGerivConstants.END_EXCEPTION_TAG);
			    		throw new IGerivRuntimeException();
					}
					// SOLO PER MENTA
					// 11/11/2016 VIENE CONTROLLATA LA PRESENZA ALL'INTERNO DELLE BOLLE SUCCESSIVE, DELLA PUBBLICAZIONE INSERITA IN RESA COME FUORI BOLLA (TBL_9612)
					else if (vo != null && vo.isPubblicazionePresenteNelleSuccessiveBolleResa()) {
						if(this.getAuthUser().getCodFiegDl().intValue() == IGerivConstants.MENTA_CODE.intValue()){
							requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("pubblicazione.in.conto.deposito"), vo.getTitolo(), vo.getNumeroPubblicazione()) + IGerivConstants.END_EXCEPTION_TAG);
							//requestMap.put("igerivException", getText("pubblicazione.presente.nelle.successive.bolle.resa") + IGerivConstants.END_EXCEPTION_TAG);
							throw new IGerivRuntimeException();
						}
					}else if (respingerePubblicazioni) {
						requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.numero.resa.respinto"), vo.getTitolo(), vo.getNumeroPubblicazione(), vo.getMotivoResaRespinta())  + IGerivConstants.END_EXCEPTION_TAG);
						throw new IGerivRuntimeException();
					} else if (!bolleService.isPeriodicitaPubblicazioneRifornimentoCorretta(vo.getPeriodicita().getPeriodicita(), requestMap.get("tipo").toString(), hasBolleQuotidianiPeriodiciDivise, getAuthUser().getCodFiegDl(), new Integer(cpu))) {
						Integer per = vo.getPeriodicita().getPeriodicita();
						String periodicita = per.equals(IGerivConstants.COD_PERIODICITA_QUOTIDIANO) ? getText("igeriv.quotidiano").toLowerCase() : getText("igeriv.periodico").toLowerCase();
						String tipoBolla = per.equals(IGerivConstants.COD_PERIODICITA_QUOTIDIANO) ? IGerivConstants.TIPO_BOLLA_QUOTIDIANI : IGerivConstants.TIPO_BOLLA_PERIODICI;
						requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.inserimento.resa.fuori.voce.periodicita.pubblicazione.errata"), vo.getTitolo(), periodicita, tipoBolla)  + IGerivConstants.END_EXCEPTION_TAG);
						throw new IGerivRuntimeException();
					} // CONTROLLO DOMENICA NO PERIODICI MENTA
					  else if(getAuthUser().getCodFiegDl().equals(IGerivConstants.MENTA_CODE) && 
						isDomenica && vo != null && !vo.getPeriodicita().getPeriodicita().equals(IGerivConstants.COD_PERIODICITA_QUOTIDIANO)){
						requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + "Nella bolla di Domenica si possono rendere solo Quotidiani");
						throw new IGerivRuntimeException();
					}
					if (vo.getNumeroInContoDeposito()) {
						vo.setPrezzoNetto(BigDecimal.ZERO);
						vo.setPrezzoLordo(BigDecimal.ZERO);
						vo.setResoInContoDeposito(true);
					}
					vo.setReso(resoVal);
					retList.add(vo);
					break;
				}
			}
		}
		return retList;
	}

	@Override
	public String getTitle() {
		return super.getTitle() + ((!Strings.isNullOrEmpty(getActionName()) && getActionName().contains("QuadraturaResa")) ? getText("igeriv.menu.47") : getText("igeriv.bolla.resa"));
	}
	
	public String getDataBollaFolder() {
		return (dtBolla != null) ? DateUtilities.getTimestampAsStringExport(dtBolla, DateUtilities.FORMATO_DATA_YYYYMMDD) : null;
	}

	private Boolean isProfileBollaReadOnly() {
		boolean result = false;
		if (getAuthUser() != null && getAuthUser().getGruppoModuliVo() != null) {
			result = getAuthUser().getGruppoModuliVo().getIsBollaResaReadOnly();
		}
		return result;
	}
}
