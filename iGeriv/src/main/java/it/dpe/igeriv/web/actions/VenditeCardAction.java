package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.selectUnique;
import static ch.lambdaj.Lambda.sort;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.awt.Font;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.prodotti.ProdottiService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.bo.vendite.VenditeService;
import it.dpe.igeriv.dto.ClienteEdicolaDto;
import it.dpe.igeriv.dto.IntervalloVenditaDto;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.dto.ParametriEdicolaDto;
import it.dpe.igeriv.dto.PositionSizeDto;
import it.dpe.igeriv.dto.ProdottoDto;
import it.dpe.igeriv.dto.PubblicazionePiuVendutaDto;
import it.dpe.igeriv.dto.UserDto;
import it.dpe.igeriv.dto.VenditaDettaglioDto;
import it.dpe.igeriv.dto.VenditaRiepilogoDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.FileUtils;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vendite.inputmodulelistener.impl.MinicardInputModuleListener;
import it.dpe.igeriv.vo.BarraSceltaRapidaProdottiVariVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import it.dpe.igeriv.vo.MessaggioRegistratoreCassaVo;
import it.dpe.igeriv.vo.ModuloInputVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiAliquotaIvaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCategoriaEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiSottoCategoriaEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiVo;
import it.dpe.igeriv.vo.RegistratoreCassaVo;
import it.dpe.igeriv.vo.pk.BarraSceltaRapidaProdottiVariPk;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per la gestione delle vendite.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("venditeCardAction")
@SuppressWarnings({ "rawtypes" })
public class VenditeCardAction<T extends BaseVo> extends RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(getClass());
	private final ClientiService<ClienteEdicolaVo> clientiService;
	private final EdicoleService edicoleService;
	private final VenditeService venditeService;
	private final ProdottiService prodottiService;
	private final PubblicazioniService pubblicazioniService;
	private final AccountService accountService;
	private final IGerivBatchService iGerivBatchService;
	private final Integer venditeIconWidth;
	private final Integer venditeIconHeight;
	private final String appletDir;
	private final String imgMiniaturePathDir;
	private final Integer prodottiVariIconWidth;
	private final Integer prodottiVariconHeight;
	private final String crumbName = getText("gp.report.vendite.card");
	private final String crumbNameRepVend = getText("gp.report.report.vendite");
	private String regExprMinicard; 
	private String regExprMinicardPlg; 
	private String filterTitle;
	private String strDataMessaggioDa;
	private String strDataMessaggioA;
	private String raggruppamento;
	private String strDataVendita; 
	private List<VenditaDettaglioDto> venditeDettaglio;
	private List<VenditaRiepilogoDto> venditeRiepilogo;
	private List<KeyValueDto> listRaggruppamento;
	private List<PubblicazionePiuVendutaDto> listPubblicazioniBarraSceltaRapidaSinistra;
	private List<PubblicazionePiuVendutaDto> listPubblicazioniBarraSceltaRapidaDestra;
	private List<ProdottoDto> listProdottiEdicola;
	private List<ProdottiNonEditorialiCategoriaEdicolaVo> listCategorie;
	private List<ProdottiNonEditorialiSottoCategoriaEdicolaVo> listSottocatgorie;
	private List<IntervalloVenditaDto> listIntervalliVendite;
	private List<ProdottiNonEditorialiAliquotaIvaVo> listAliquoteReparti;
	private PositionSizeDto positionSizeDto;
	private Integer abilitaResto;
	private Integer abilitaBeepVenditaBarcode;
	private Integer ricercaProdottiVari;
	private Integer aggiornaBarcode;
	private Integer tipoProdotto;
	private List<KeyValueDto> utenti;
	private String codUtente;
	private Long idConto;
	private Long idCliente;
	private String contoCliente;
	private List<ClienteEdicolaDto> listClienti;
	private Boolean showGraficoProdottiVari = false;
	private RegistratoreCassaVo registratoreCassa;
	private Boolean msgRepartiRegistratoreCassaVisto;
	private Boolean scontrinoDet;
	private Integer left;
	private Integer top;
	private Integer width;
	private Integer height;
	private Boolean barraProdottiVariVisible;
	private Boolean showLeftBar;
	private Boolean showRightBar;
	private Map<String,String> mapResult;
	
	public VenditeCardAction() {
		this(null,null,null,null,null,null,null,null,null,null,null,null,null,null);
	}
	
	@Autowired
	public VenditeCardAction(
			ClientiService<ClienteEdicolaVo> clientiService,
			EdicoleService edicoleService,
			VenditeService venditeService,
			ProdottiService prodottiService,
			PubblicazioniService pubblicazioniService,
			AccountService accountService,
			IGerivBatchService iGerivBatchService,
			MinicardInputModuleListener minicardInputModuleListener,
			@Value("${vendite.icon.width}") Integer venditeIconWidth,
			@Value("${vendite.icon.height}") Integer venditeIconHeight,
			@Value("${applet.tomcat.dir}") String appletDir,
			@Value("${img.miniature.edicola.prodotti.vari.path.dir}") String imgMiniaturePathDir,
			@Value("${prodotti.vari.icon.width}") Integer prodottiVariIconWidth,
			@Value("${prodotti.vari.icon.height}") Integer prodottiVariconHeight) {
		this.clientiService = clientiService;
		this.edicoleService = edicoleService;
		this.venditeService = venditeService;
		this.prodottiService = prodottiService;
		this.pubblicazioniService = pubblicazioniService;
		this.accountService = accountService;
		this.iGerivBatchService = iGerivBatchService;
		this.venditeIconWidth = venditeIconWidth;
		this.venditeIconHeight = venditeIconHeight;
		this.appletDir = appletDir;
		this.imgMiniaturePathDir = imgMiniaturePathDir;
		this.prodottiVariIconWidth = prodottiVariIconWidth;
		this.prodottiVariconHeight = prodottiVariconHeight;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void prepare() throws Exception {
		super.prepare();
		String requestURL = request.getRequestURL().toString();
		String contextPath = request.getContextPath();
		String baseUrl = requestURL.substring(0, requestURL.indexOf(contextPath));
		requestMap.put("url", baseUrl + contextPath + "/applet");
		requestMap.put("appletDir", appletDir);
		requestMap.put("mostraGrafico", getText("igeriv.grafico.venduto.prodotti.vari"));
		Gson gson = new GsonBuilder().setExclusionStrategies().create();
		List<ClienteEdicolaDto> clientiEdicola = clientiService.getClientiEdicola(getAuthUser().getArrId(), null, null, null, null);
		ClienteEdicolaDto clienteAnonimo = new ClienteEdicolaDto();
		clienteAnonimo.setCodCliente(-1l);
		clienteAnonimo.setNome(getText("username.cliente"));
		clienteAnonimo.setCognome(getText("username.anomimo"));
		clientiEdicola.add(0, clienteAnonimo);
		requestMap.put("clientiEdicola", gson.toJson(clientiEdicola));
		Map<String, ParametriEdicolaDto> mapParam = (Map<String, ParametriEdicolaDto>) sessionMap.get(IGerivConstants.SESSION_VAR_MAP_PARAMETRI_EDICOLA);
		Object paramAbilitaResto = mapParam.containsKey(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_EDICOLA_ABILITA_RESTO) ? mapParam.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_EDICOLA_ABILITA_RESTO) : null;
		String strAbilitaResto = paramAbilitaResto != null ? (Strings.isNullOrEmpty(((ParametriEdicolaDto) paramAbilitaResto).getValue()) ? ((ParametriEdicolaDto) paramAbilitaResto).getDefaultValue() : ((ParametriEdicolaDto) paramAbilitaResto).getValue()) : "false";
		abilitaResto = strAbilitaResto.equals("true") ? 1 : 0;
		
		//TICKET 0000371
		Object paramBeepVenditeBarcode = mapParam.containsKey(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_EDICOLA_ABILITA_BEEP_VENDITE_BARCODE) ? mapParam.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_EDICOLA_ABILITA_BEEP_VENDITE_BARCODE) : null;
		String strBeepVenditeBarcode = paramBeepVenditeBarcode != null ? (Strings.isNullOrEmpty(((ParametriEdicolaDto) paramBeepVenditeBarcode).getValue()) ? ((ParametriEdicolaDto) paramBeepVenditeBarcode).getDefaultValue() : ((ParametriEdicolaDto) paramBeepVenditeBarcode).getValue()) : "false";
		abilitaBeepVenditaBarcode = strBeepVenditeBarcode.equals("true") ? 1 : 0;
		
		
		
		ricercaProdottiVari = getAuthUser().getRicercaProdottiVari() == null || !getAuthUser().getRicercaProdottiVari() ? 0 : 1;
		registratoreCassa = venditeService.getRegistratoreCassa(mapParam.containsKey(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_REGISTRATORE_CASSA) ? new Integer(mapParam.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_REGISTRATORE_CASSA).getParamValue()) : null);
		ParametriEdicolaDto paramScontrinoDettagliato = mapParam.containsKey(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_SCONTRINO_DETTAGLIATO) ? mapParam.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_SCONTRINO_DETTAGLIATO) : null;
		scontrinoDet = Boolean.valueOf(paramScontrinoDettagliato != null ? paramScontrinoDettagliato.getParamValue() : "false");
		msgRepartiRegistratoreCassaVisto = getMsgRegistratoreCassaVisto(registratoreCassa);
		if (getActionName().contains("reportVendite_")) {
			if (getAuthUser().isAdmin())  {
				buildUtenti();
			}
		}
		ModuloInputVo moduloInputMinicard = selectUnique((List<ModuloInputVo>) context.getAttribute("moduliInput"), having(on(ModuloInputVo.class).getClasse(), equalTo("MinicardInputModuleListener")));
		this.regExprMinicard = moduloInputMinicard != null ? moduloInputMinicard.getEspressioneRegolare() : "";
		ModuloInputVo moduloInputMinicardPlg = selectUnique((List<ModuloInputVo>) context.getAttribute("moduliInput"), having(on(ModuloInputVo.class).getClasse(), equalTo("MinicardPLGInputModuleListener")));
		this.regExprMinicardPlg = moduloInputMinicardPlg != null ? moduloInputMinicardPlg.getEspressioneRegolare() : "";
		this.positionSizeDto = venditeService.getBarraSceltaRapidaProdottiVariPositionSizeDto(getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodEdicolaMaster());
	}

	/**
	 * Ritorna true se il messaggio customizzato del reg. di cassa è stato visto
	 * 
	 * @param RegistratoreCassaVo registratoreCassa
	 * @return boolean
	 */
	private boolean getMsgRegistratoreCassaVisto(RegistratoreCassaVo registratoreCassa) {
		if (registratoreCassa != null) {
			MessaggioRegistratoreCassaVo vo = venditeService.getMessaggioRegistratoreCassa(getAuthUser().getCodEdicolaMaster(), registratoreCassa.getCodRegCassa());
			if (vo != null) {
				return vo.getMessaggioVisto();
			} else {
				return false;
			}
		}
		return true;
	}

	@Override
	public String input() throws Exception {
		filterTitle = getText("gp.report.vendite.card");
		return INPUT;
	}
	
	@Override
	public void validate() {
		filterTitle = getText("gp.report.report.vendite");
		if ((strDataMessaggioDa == null || strDataMessaggioDa.equals("")) 
				&& (strDataMessaggioA == null || strDataMessaggioA.equals(""))
				&& (strDataVendita == null || strDataVendita.equals(""))) {
			addActionError(getText("error.specificare.data.o.intervallo.date"));
		}
	}
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showConto() throws IOException {
		return showFilter();
	}
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showFilter() throws IOException {
		filterTitle = getText("gp.report.vendite.card");
		Integer[] arrCoddl = (getAuthUser().isMultiDl() || getAuthUser().isDlInforiv()) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDlMaster()};
		Integer[] arrCodEdicola = (getAuthUser().isMultiDl() || getAuthUser().isDlInforiv()) ? getAuthUser().getArrId() : new Integer[]{getAuthUser().getCodEdicolaMaster()};
		listCategorie = iGerivBatchService.getProdottiNonEditorialiCategorieEdicolaVo(getAuthUser().getCodEdicolaMaster());
		for (ProdottiNonEditorialiCategoriaEdicolaVo vo : listCategorie) {
			if (vo.getImmagine() == null || vo.getImmagine().equals("")) {
				vo.setImmagine(FileUtils.createFakeImage(vo.getDescrizione(), prodottiVariIconWidth, prodottiVariconHeight, imgMiniaturePathDir, 6, Font.BOLD, 12));
				prodottiService.saveBaseVo(vo);
			}
			vo.setDescrizione(!Strings.isNullOrEmpty(vo.getDescrizione()) ? vo.getDescrizione().replaceAll("\\'", "\\\\'") : vo.getDescrizione());
		}
		List<ProdottiNonEditorialiSottoCategoriaEdicolaVo> listAllSottocatgorie = iGerivBatchService.getProdottiNonEditorialiSottoCategorieEdicolaVo(null, getAuthUser().getCodEdicolaMaster());
		listSottocatgorie = new ArrayList<ProdottiNonEditorialiSottoCategoriaEdicolaVo>();
		for (ProdottiNonEditorialiCategoriaEdicolaVo catVo : listCategorie) {
			List<ProdottiNonEditorialiSottoCategoriaEdicolaVo> sort = sort(select(listAllSottocatgorie, having(on(ProdottiNonEditorialiSottoCategoriaEdicolaVo.class).getPk().getCodCategoria(), equalTo(catVo.getPk().getCodCategoria()))), on(ProdottiNonEditorialiSottoCategoriaEdicolaVo.class).getPosizione());
			listSottocatgorie.addAll(sort);
			for (ProdottiNonEditorialiSottoCategoriaEdicolaVo vo : sort) {
				if (vo.getImmagine() == null || vo.getImmagine().equals("")) {
					vo.setImmagine(FileUtils.createFakeImage(vo.getDescrizione(), prodottiVariIconWidth, prodottiVariconHeight, imgMiniaturePathDir, 6, Font.BOLD, 12));
					prodottiService.saveBaseVo(vo);
				}
				vo.setDescrizione(!Strings.isNullOrEmpty(vo.getDescrizione()) ? vo.getDescrizione().replaceAll("\\'", "\\\\'") : vo.getDescrizione());
			}
		}
		List<ProdottoDto> listAllProdottiEdicola = prodottiService.getProdottiNonEditorialiEdicolaDto(getAuthUser().getCodEdicolaMaster());
		listProdottiEdicola = new ArrayList<ProdottoDto>();
		for (ProdottiNonEditorialiSottoCategoriaEdicolaVo scevo : listSottocatgorie) {
			List<ProdottoDto> sort = sort(select(listAllProdottiEdicola, having(on(ProdottoDto.class).getCodCategoria(), equalTo(scevo.getPk().getCodCategoria())).and(having(on(ProdottoDto.class).getCodSubCategoria(), equalTo(scevo.getPk().getCodSottoCategoria()))).and(having(on(ProdottoDto.class).getEscludiDalleVendite(), equalTo(false)))), on(ProdottoDto.class).getPosizione());
			listProdottiEdicola.addAll(sort);
			for (ProdottoDto dto : sort) {
				String imgProd = dto.getNomeImmagine();
				if (imgProd == null || imgProd.equals("")) {
					ProdottiNonEditorialiVo vo = prodottiService.getProdottiNonEditorialiVo(dto.getCodProdottoInterno(), getAuthUser().getCodEdicolaMaster());
					String createFakeImage = FileUtils.createFakeImage(vo.getDescrizioneProdottoA(), prodottiVariIconWidth, prodottiVariconHeight, imgMiniaturePathDir, 6, Font.BOLD, 12);
					dto.setNomeImmagine(createFakeImage);
					vo.setNomeImmagine(createFakeImage);
					prodottiService.saveBaseVo(vo);
				}
				dto.setDescrizione(!Strings.isNullOrEmpty(dto.getDescrizione()) ? dto.getDescrizione().replaceAll("\\'", "\\\\'") : dto.getDescrizione());
				dto.setDescrizioneB(!Strings.isNullOrEmpty(dto.getDescrizioneB()) ? dto.getDescrizioneB().replaceAll("\\'", "\\\\'") : dto.getDescrizioneB());
			}
		}
		listAliquoteReparti = prodottiService.getListAliquoteIva();
		listPubblicazioniBarraSceltaRapidaSinistra = pubblicazioniService.getListPubblicazioniBarraSceltaRapidaSinistra(arrCoddl, arrCodEdicola, true);
		listPubblicazioniBarraSceltaRapidaDestra = pubblicazioniService.getListPubblicazioniBarraSceltaRapidaDestra(arrCoddl, arrCodEdicola, true);
		listClienti = clientiService.getClientiEdicola(getAuthUser().getArrId(), null, null, null, null);
		
		List<ParametriEdicolaDto> listParametriEdicola = edicoleService.getParametriEdicola(getAuthUser().getCodEdicolaMaster());
		
		
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameRepVend}")
	@SkipValidation
	public String showReportFilter() {
		filterTitle = getText("gp.report.report.vendite");
		listRaggruppamento = new ArrayList<KeyValueDto>();
		KeyValueDto periodicita = new KeyValueDto();
		periodicita.setKey("" + IGerivConstants.COD_RAGGRUPPAMENTO_PERIODICITA);
		periodicita.setValue(getText("igeriv.periodicita"));
		KeyValueDto argomento = new KeyValueDto();
		argomento.setKey("" + IGerivConstants.COD_RAGGRUPPAMENTO_ARGOMENTO);
		argomento.setValue(getText("igeriv.argomento"));
		listRaggruppamento.add(periodicita);
		listRaggruppamento.add(argomento);
		sessionMap.put("listRaggruppamento", listRaggruppamento);
		raggruppamento = (raggruppamento == null || raggruppamento.equals("")) ? "" + IGerivConstants.COD_RAGGRUPPAMENTO_PERIODICITA : raggruppamento;
		return "reportsVendite";
	}
	
	@BreadCrumb("%{crumbNameRepVend}")
	public String showReport() throws ParseException {
		filterTitle = getText("gp.report.report.vendite");
		String codUtente = (getAuthUser().isAdmin() || getAuthUser().isMultiDl()) ? ((this.codUtente != null && this.codUtente.equals("0") ? null : this.codUtente)) : getAuthUser().getCodUtente();
		if (!Strings.isNullOrEmpty(strDataVendita)) {
			Timestamp dataDa = DateUtilities.floorDay(DateUtilities.parseDate(strDataVendita, DateUtilities.FORMATO_DATA_SLASH));
			Timestamp dataA = DateUtilities.ceilDay(DateUtilities.parseDate(strDataVendita, DateUtilities.FORMATO_DATA_SLASH));
			venditeDettaglio = venditeService.getContiVenditeDettaglio(getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodEdicolaMaster(), codUtente, dataDa, dataA, tipoProdotto);
			requestMap.put("strData", strDataVendita);
			requestMap.put("venditeDettaglio", venditeDettaglio);
		} else {
			Timestamp dataDa = DateUtilities.parseDate(strDataMessaggioDa, DateUtilities.FORMATO_DATA_SLASH);
			Timestamp dataA = DateUtilities.parseDate(strDataMessaggioA, DateUtilities.FORMATO_DATA_SLASH);
			Integer codRaggruppamento = new Integer(raggruppamento);
			venditeRiepilogo = venditeService.getVenditeRiepilogo(getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodEdicolaMaster(), codUtente, dataDa, dataA, codRaggruppamento);
			if (tipoProdotto != null && tipoProdotto.equals(1)) {
				venditeRiepilogo = select(venditeRiepilogo, having(on(VenditaRiepilogoDto.class).getRaggruppamento(), equalTo(getText("igeriv.prodotti.vari"))));
			}
			requestMap.put("strDataDa", strDataMessaggioDa);
			requestMap.put("strDataA", strDataMessaggioA);
			requestMap.put("raggruppamento", (codRaggruppamento.equals(1)) ? getText("igeriv.periodicita") : getText("igeriv.argomento"));
			requestMap.put("venditeRiepilogo", venditeRiepilogo);
		}
		if (tipoProdotto != null && tipoProdotto.equals(1)) {
			showGraficoProdottiVari = true;
		}
		return "reportsVendite";
	}
	
	@SkipValidation
	public String showVendutoProdottiVariChart() throws ParseException {
		Timestamp dataDa = null;
		Timestamp dataA = null;
		if (!Strings.isNullOrEmpty(strDataVendita)) {
			dataDa = DateUtilities.parseDate(strDataVendita, DateUtilities.FORMATO_DATA_SLASH);
			dataA = DateUtilities.parseDate(strDataVendita, DateUtilities.FORMATO_DATA_SLASH);
		} else {
			dataDa = DateUtilities.parseDate(strDataMessaggioDa, DateUtilities.FORMATO_DATA_SLASH);
			dataA = DateUtilities.parseDate(strDataMessaggioA, DateUtilities.FORMATO_DATA_SLASH);
		}
		listIntervalliVendite = venditeService.getVenditeIntervalli(getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodFiegDlMaster(), dataDa, dataA);
		return "vendutoChart";
	}
	
	@SkipValidation
	public String setBarraSceltaRapidaProdottiVariPosAndSize() {
		mapResult = new HashMap<String, String>();
		try {
			BarraSceltaRapidaProdottiVariVo vo = venditeService.getBarraSceltaRapidaProdottiVariVo(getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodEdicolaMaster());
			if (vo == null) {
				vo = new BarraSceltaRapidaProdottiVariVo();
				BarraSceltaRapidaProdottiVariPk pk = new BarraSceltaRapidaProdottiVariPk();
				pk.setCodFiegDl(getAuthUser().getCodFiegDlMaster());
				pk.setCodEdicola(getAuthUser().getCodEdicolaMaster());
				vo.setPk(pk);
			}
			vo.setLeft(left);
			vo.setTop(top);
			vo.setWidth(width);
			vo.setHeight(height);
			vo.setBarraProdottiVariVisible(barraProdottiVariVisible);
			venditeService.saveBaseVo(vo);
			mapResult.put("result", "success");
		} catch (Throwable e) {
			mapResult.put("result", "error");
			log.error("Errore in VenditeCardRpcAction.setBarPosAndSize(" + left + "," + top + "," + width + "," + height + ")", e);
		}
		return SUCCESS;
	}
	
	@SkipValidation
	public String setMostraNascodiBarreLaterali() {
		mapResult = new HashMap<String, String>();
		try {
			sessionMap.put("showLeftBar", showLeftBar);
			sessionMap.put("showRightBar", showRightBar);
			mapResult.put("result", "success");
		} catch (Throwable e) {
			mapResult.put("result", "error");
			log.error("Errore in VenditeCardRpcAction.setMostraNascodiBarreLaterali(" + showLeftBar + "," + showRightBar + ")", e);
		}
		return SUCCESS;
	}
	
	/**
	 * Costruisce la lista di utenti per riempire la combo
	 */
	private void buildUtenti() {
		utenti = new ArrayList<KeyValueDto>();
		List<UserDto> users = accountService.getUtentiEdicola(getAuthUser().getCodEdicolaMaster(), getAuthUser().isAdmin(), null, null, null);
		KeyValueDto tutti = new KeyValueDto();
		tutti.setKey("0");
		tutti.setValue(getText("igeriv.tutti"));
		utenti.add(tutti);
		for (UserDto dto : users) {
			KeyValueDto kvdto = new KeyValueDto();
			kvdto.setKey(dto.getCodUtente());
			kvdto.setValue(dto.getCodUtente());
			utenti.add(kvdto);
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
		String title = getText("gp.report.vendite.minicard");
		if (!Strings.isNullOrEmpty(getActionName())) {
			if (getActionName().contains("reportVendite_")) {
				title = getText("gp.report.report.vendite");
			}
		}
		return super.getTitle() + title;
	}
	
	@SuppressWarnings("unchecked")
	public List<KeyValueDto> getTipiProdotto() {
		return (List<KeyValueDto>) context.getAttribute("tipiProdotto");
	}
	
	@Override
	public void setServletContext(ServletContext context) {
		this.context = context;
	}

	public List<ClienteEdicolaDto> getListClientiConEC() {
		List<ClienteEdicolaDto> list = new ArrayList<ClienteEdicolaDto>();
		if (listClienti != null && !listClienti.isEmpty()) {
			list = sort(select(listClienti, having(on(ClienteEdicolaDto.class).getTipoEstrattoConto(), notNullValue())), on(ClienteEdicolaDto.class).getNomeSort());
		}
		return list;
	}
	
	public List<ClienteEdicolaDto> getListClientiSenzaEC() {
		List<ClienteEdicolaDto> list = new ArrayList<ClienteEdicolaDto>();
		if (listClienti != null && !listClienti.isEmpty()) {
			list = sort(select(listClienti, having(on(ClienteEdicolaDto.class).getTipoEstrattoConto(), nullValue())), on(ClienteEdicolaDto.class).getNomeSort());
		}
		return list;
	}

	
}
