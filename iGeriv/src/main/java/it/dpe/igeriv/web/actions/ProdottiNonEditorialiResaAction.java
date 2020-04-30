package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.forEach;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.selectFirst;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.WordUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.contabilita.ContabilitaService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.fornitori.FornitoriService;
import it.dpe.igeriv.bo.prodotti.ProdottiService;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.JmsConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiBollaDettaglioVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiBollaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCausaliMagazzinoVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiFornitoreVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiPrezziAcquistoVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiVo;
import it.dpe.igeriv.vo.ProvinciaVo;
import it.dpe.igeriv.vo.TipoLocalitaVo;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per la gestione delle rese dei prodotti non editoriali.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("prodottiNonEditorialiResaAction")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ProdottiNonEditorialiResaAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final FornitoriService fornitoriService;
	private final ProdottiService prodottiService;
	private final EdicoleService edicoleService;
	private final ContabilitaService contabilitaService;
	private final String crumbName = getText("igeriv.resa.bolla.prodotti.vari");
	private String tableTitle;
	private List<ProdottiNonEditorialiFornitoreVo> fornitori;
	private List<ProdottiNonEditorialiBollaVo> rese;
	private ProdottiNonEditorialiBollaVo resa;
	private List<ProdottiNonEditorialiBollaDettaglioVo> dettagli;
	private Integer codFornitore;
	private String dataDocumento;
	private String numeroDocumento;
	private Integer codiceCausale;
	private String idDocumento;
	private Integer reso;
	private Integer idProdotto;
	private String BollaResaEditTab_ev;
	private Map<String, Object> mapResult;
	private Boolean isNew = false;
	private Boolean bollaInviata = false;
	private Boolean hasMemorizzaInvia = false;
	
	public ProdottiNonEditorialiResaAction() {
		this.fornitoriService = null;
		this.prodottiService = null;
		this.edicoleService = null;
		this.contabilitaService = null;
	}
	
	@Autowired
	public ProdottiNonEditorialiResaAction(FornitoriService fornitoriService, ProdottiService prodottiService, EdicoleService edicoleService, ContabilitaService contabilitaService) {
		this.fornitoriService = fornitoriService;
		this.prodottiService = prodottiService;
		this.edicoleService = edicoleService;
		this.contabilitaService = contabilitaService;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		fornitori = fornitoriService.getFornitoriEdicola(getAuthUser().getCodEdicolaMaster(), null, null, false);
		requestMap.put("aggiungiRiga", getText("igeriv.aggiungi.riga") + " (" + getText("igeriv.ctrl.a") + ")");
		requestMap.put("rimuoviRiga", getText("igeriv.rimuovi.riga") + " (" + getText("igeriv.ctrl.d") + ")");
	}
	
	@Override
	public void validate() {
		if (getActionName() != null && getActionName().contains("showBolle")) {
			tableTitle = getText("igeriv.resa.bolla.prodotti.vari");
			if (dataDocumento != null && !dataDocumento.equals("")) { 
				try {
					DateUtilities.floorDay(DateUtilities.parseDate(dataDocumento, DateUtilities.FORMATO_DATA_SLASH));
				} catch (ParseException e) {
					addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataDocumento));
					return;
				}
			}
		} else if (getActionName().contains("deleteBolla")) {
			if (dettagli != null && selectFirst(dettagli, having(on(ProdottiNonEditorialiBollaDettaglioVo.class).getStatoEsportazione(), not(nullValue()))) != null) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("error.bolla.resa.prodotti.vari.non.cancellabile") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
		}
	}

	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showFilter() {
		tableTitle = getText("igeriv.resa.bolla.prodotti.vari");
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbName}")
	public String showBolle() {
		tableTitle = getText("igeriv.resa.bolla.prodotti.vari");
		try {
			Timestamp dataDocumento = (this.dataDocumento != null && !this.dataDocumento.equals("")) ? DateUtilities.parseDate(this.dataDocumento, DateUtilities.FORMATO_DATA_SLASH) : null;
			rese = prodottiService.getReseProdottiVariEdicola(getAuthUser().getCodEdicolaMaster(), codFornitore, dataDocumento, (!Strings.isNullOrEmpty(numeroDocumento) ? numeroDocumento : null));
			requestMap.put("rese", rese);
		} catch (Throwable e) {
			addActionError(getText("gp.errore.imprevisto"));
			return INPUT;
		}
		return SUCCESS;
	}
	
	@SkipValidation
	public String editBolla() {
		tableTitle = getText("igeriv.inserisc.aggiorna.resa.bolla.prodotti.vari");
		if (idDocumento != null) {
			Long idDocumento = !Strings.isNullOrEmpty(this.idDocumento) ? new Long(this.idDocumento) : null;
			resa = prodottiService.getResaProdottiVariEdicola(idDocumento);
			dettagli = resa != null && resa.getDettagli() != null ? select(resa.getDettagli(), notNullValue()) : null;
			Object det = dettagli != null ? selectFirst(dettagli, having(on(ProdottiNonEditorialiBollaDettaglioVo.class).getStatoEsportazione(), not(nullValue()))) : null;
			bollaInviata = det != null;
		} else {
			resa = new ProdottiNonEditorialiBollaVo(); 
			resa.setNumeroDocumento(contabilitaService.getNextProgressivoFatturazione(getAuthUser().getCodEdicolaMaster(), IGerivConstants.TIPO_DOCUMENTO_BOLLA_RESA_PRODOTTI_VARI, DateUtilities.getDateFirstDayYear()).toString());
			requestMap.put("dataDocumentoEdit", DateUtilities.getTimestampAsString(new Date(), DateUtilities.FORMATO_DATA_SLASH));
			isNew = true;
		}
		codFornitore = resa != null && resa.getCodiceFornitore() != null ? resa.getCodiceFornitore() : codFornitore;
		hasMemorizzaInvia = codFornitore != null && codFornitore < IGerivConstants.COD_INIZIO_FORNITORI_NON_DL;
		if (BollaResaEditTab_ev != null && (BollaResaEditTab_ev.toUpperCase().equals("PDF") || BollaResaEditTab_ev.toUpperCase().equals("XLS"))) {
			setFornitoreEdicolaExportTitle(resa);
			requestMap.put("titolo", MessageFormat.format(getText("igeriv.bolla.resa.prodotti.vari.titolo"), resa.getNumeroDocumento().toString(), DateUtilities.getTimestampAsString(resa.getDataDocumento(), DateUtilities.FORMATO_DATA)));
		}
		return "editBollaResa";
	}
	
	@SkipValidation
	public String saveBolla() {
		mapResult = new HashMap<String, Object>();
		try {
			doSaveValidation();
			if (resa != null && dettagli != null) {
				save();
			}
		} catch (IGerivRuntimeException e) {
			if (requestMap.get("igerivException") != null) {
				mapResult.put("error", requestMap.get("igerivException").toString().replace(IGerivConstants.START_EXCEPTION_TAG, "").replace(IGerivConstants.END_EXCEPTION_TAG, ""));
			}
		} catch (Throwable e) {
			mapResult.put("error", getText("gp.errore"));
		}
		return SUCCESS;
	}

	@SkipValidation
	public String sendBolla() {
		mapResult = new HashMap<String, Object>();
		try {
			doSaveValidation();
			if (resa != null && dettagli != null) {
				forEach(dettagli).setStatoEsportazione(JmsConstants.COD_STATO_MESSAGGIO_PRONTO_PER_INVIO);
				save();
			}
		} catch (IGerivRuntimeException e) {
			if (requestMap.get("igerivException") != null) {
				mapResult.put("error", requestMap.get("igerivException").toString().replace(IGerivConstants.START_EXCEPTION_TAG, "").replace(IGerivConstants.END_EXCEPTION_TAG, ""));
			}
		} catch (Throwable e) {
			mapResult.put("error", getText("gp.errore"));
		}
		return SUCCESS;
	}
	
	public String deleteBolla() {
		tableTitle = getText("igeriv.resa.bolla.prodotti.vari");
		try {
			if (resa != null) {
				prodottiService.deleteBollaResaProdottiVari(getAuthUser().getCodEdicolaMaster(), resa.getIdDocumento(), new Integer(resa.getNumeroDocumento()));
			}
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "blank";
	}
	
	@SkipValidation
	public String getReportBollaResaFileName() {
		mapResult = new HashMap<String, Object>();
		try {
			Long idDocumento = !Strings.isNullOrEmpty(this.idDocumento) ? new Long(this.idDocumento) : null;
			ProdottiNonEditorialiBollaVo resa = prodottiService.getResaProdottiVariEdicola(idDocumento);
			mapResult.put("result", (resa != null && resa.getDocumentoResa() != null ? resa.getDocumentoResa().getNomeFile() : ""));
		} catch (Throwable e) {
			mapResult.put("error", getText("gp.errore"));
		}
		return SUCCESS;
	}
	
	@SkipValidation
	public String isDataDocumentoBollaResaValida() {
		mapResult = new HashMap<String, Object>();
		try {
			mapResult.put("result", isDataDocValida());
		} catch (Throwable e) {
			mapResult.put("error", getText("gp.errore"));
		}
		return SUCCESS;
	}

	@SkipValidation
	public String isQuantitaResoValida() {
		mapResult = new HashMap<String, Object>();
		mapResult.put("result", true);
		try {
			if (reso != null && idProdotto != null) {
				ProdottiNonEditorialiVo prodotto = prodottiService.getProdottiNonEditorialiVo(new Long(idProdotto), getAuthUser().getCodEdicolaMaster());
				if (prodotto != null && prodotto.getPercentualeResaSuDistribuito() != null) {
					// In data 05/01/2018 viene disabilitato il controllo CDL 
//					Long qtaDistr = prodottiService.getQuantitaDistribuita(getAuthUser().getCodEdicolaMaster(), new Long(idProdotto));
//					Long maxReso = Math.round((qtaDistr.doubleValue() / 100d ) * prodotto.getPercentualeResaSuDistribuito().doubleValue());
//					Long qr = prodottiService.getQuantitaResa(getAuthUser().getCodEdicolaMaster(), new Long(idProdotto));
//					Long qtaReso = (qr == null ? 0l : qr) + new Long(reso);
//					mapResult.put("result", maxReso >= qtaReso);
					mapResult.put("result", true);
				}
			}
		} catch (Throwable e) {
			mapResult.put("error", getText("gp.errore"));
		}
		return SUCCESS;
	}
	
	/**
	 * Validazioni per saveBolla e sendBolla
	 */
	private void doSaveValidation() {
		tableTitle = getText("igeriv.inserisc.aggiorna.resa.bolla.prodotti.vari");
		if (resa == null) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		if (dettagli == null) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.bolla.carico.vuota") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		if (resa.getDataDocumento() == null) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("igeriv.data.documento")) + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		if (resa.getNumeroDocumento() == null) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("igeriv.numero.documento")) + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		if (resa.getCodiceFornitore() == null) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("igeriv.fornitore")) + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		} 
		checkDuplicateProducts(dettagli);
		dataDocumento = DateUtilities.getTimestampAsString(resa.getDataDocumento(), DateUtilities.FORMATO_DATA_SLASH);
		codFornitore = resa.getCodiceFornitore();
		if (isNew && !isDataDocValida()) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("error.data.documento.bolla.resa.prodotti.vari.non.valida") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		if (selectFirst(dettagli, having(on(ProdottiNonEditorialiBollaDettaglioVo.class).getStatoEsportazione(), not(nullValue()))) != null) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("error.bolla.resa.prodotti.vari.non.modificabile") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
	}
	
	/**
	 * Salva la bolla
	 */
	private void save() {
		if (isNew) {
			Long codResa = prodottiService.getNextSeqVal(IGerivConstants.SEQ_BOLLE_CARICO_PRODOTTI_VARI);
			resa.setIdDocumento(codResa);
			forEach(dettagli).getPk().setIdDocumento(codResa);
		} else {
			forEach(dettagli).getPk().setIdDocumento(resa.getIdDocumento());
		}
		setPrezzo();
		ProdottiNonEditorialiCausaliMagazzinoVo causale = prodottiService.getCausaleMagazzino(IGerivConstants.CODICE_CAUSALE_RESA);
		forEach(dettagli).setCausale(causale);
		forEach(dettagli).setMagazzinoDa(IGerivConstants.COD_MAGAZZINO_INTERNO);
		forEach(dettagli).setMagazzinoA(IGerivConstants.COD_MAGAZZINO_ESTERNO);
		resa.setDettagli(dettagli);
		resa.setCausale(causale);
		resa.setDataRegistrazione(prodottiService.getSysdate());
		resa.setEdicola(edicoleService.getAnagraficaEdicola(getAuthUser().getCodEdicolaMaster()));
		resa.setIndicatoreEmessoRicevuto(IGerivConstants.INDICATORE_DOCUMENTO_EMESSO);
		prodottiService.saveBollaResaProdottiVari(getAuthUser().getCodEdicolaMaster(), resa, isNew);
		mapResult.put("result", (resa != null ? resa.getIdDocumento() : ""));
	}

	/**
	 * Setta il prezzo di acquisto su ogni dettaglio, sovrascrivendo ogni eventuale modifica apportata dall'utente
	 */
	private void setPrezzo() {
		for (ProdottiNonEditorialiBollaDettaglioVo det : dettagli) {
			if (det.getProdotto() != null && det.getProdotto().getCodProdottoInterno() != null && det.getCodiceProdottoFornitore() != null) {
				ProdottiNonEditorialiPrezziAcquistoVo prezzo = prodottiService.getProdottiNonEditorialiPrezzoAcquisto(getAuthUser().getCodEdicolaMaster(), det.getProdotto().getCodProdottoInterno(), det.getCodiceProdottoFornitore());
				if (prezzo != null && prezzo.getUltimoPrezzoAcquisto() != null) {
					det.setPrezzo(prezzo.getUltimoPrezzoAcquisto());
				}
			}
		}
	}
	
	/**
	 * Verifica che la data del documento è successiva alla data dell'ultimo documento emesso 
	 * 
	 * @return boolean
	 */
	private boolean isDataDocValida() {
		boolean isDateValid = false;
		try {
			Timestamp lastDataBollaResa = prodottiService.getLastDataBollaResa(codFornitore, getAuthUser().getCodEdicolaMaster(), resa.getIdDocumento());
			Timestamp dataDocumento = (this.dataDocumento != null && !this.dataDocumento.equals("")) ? DateUtilities.parseDate(this.dataDocumento, DateUtilities.FORMATO_DATA_SLASH) : null;
			isDateValid = (dataDocumento != null && lastDataBollaResa != null) ? dataDocumento.getTime() > lastDataBollaResa.getTime() : true;
		} catch (ParseException e) {
			throw new IGerivRuntimeException(e);
		}
		return isDateValid;
	}
	
	/**
	 * Verifica se esistono prodotti duplicati nella bolla.
	 * 
	 * @param dettagli
	 */
	private void checkDuplicateProducts(List<ProdottiNonEditorialiBollaDettaglioVo> dettagli) {
		List<Long> productIds = extract(dettagli, on(ProdottiNonEditorialiBollaDettaglioVo.class).getProdotto().getCodProdottoInterno());
		Set<Long> set = new HashSet<Long>(productIds);
		if (set.size() < productIds.size()) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.prodotti.duplicati.bolla.carico.prodotti.vari") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
	}
	
	/**
	 * Costruisce il titolo da visualizzare nella testa del report bolla di resa prodotti 
	 * @param bolla 
	 */
	private void setFornitoreEdicolaExportTitle(ProdottiNonEditorialiBollaVo bolla) {
		ProdottiNonEditorialiFornitoreVo fornitore = bolla.getFornitore() != null ? bolla.getFornitore() : null;
		String ragSocFornitore = fornitore != null ? fornitore.getNome() != null ? fornitore.getNome()  : "" : "";
		String indirizzoFornitore = fornitore != null ? fornitore.getIndirizzo() != null ? fornitore.getIndirizzo() : "" : "";
		String pivaFornitore = fornitore != null ? fornitore.getPiva() != null ? fornitore.getPiva() : "" : "";
		String capFornitore = fornitore != null ? fornitore.getCap() != null ? fornitore.getCap() : "" : "";
		String cittaFornitore = fornitore != null ? fornitore.getLocalitaDesc() != null ? fornitore.getLocalitaDesc() : "" : "";
		String provFornitore = fornitore != null ? fornitore.getProvinciaDesc() != null ? fornitore.getProvinciaDesc() : "" : "";
		String ragSocEdicola = getAuthUser().getRagioneSocialeEdicola(); 
		String indirizzoEdicola = getAuthUser().getIndirizzoEdicolaPrimaRiga();
		String capEdicola = getAuthUser().getCapEdicola() != null ? getAuthUser().getCapEdicola() : "";
		String cittaEdicola = getAuthUser().getLocalitaEdicolaPrimaRiga();
		String provEdicola = getAuthUser().getProvinciaEdicola();
		String pivaEdicola = getAuthUser().getPiva();
		requestMap.put("codFornitore",  StringUtility.checkSpecialChars(getText("igeriv.pne.report.magazzino.conto").replaceAll("&nbsp;", " ")));
		requestMap.put("codEdicola", StringUtility.checkSpecialChars(getText("igeriv.provenienza.evasione.edicola").replaceAll("&nbsp;", " ")));
		requestMap.put("ragSocFornitore", StringUtility.checkSpecialChars(((ragSocFornitore != null) ? ragSocFornitore.replaceAll("&nbsp;", " ") : "")));
		requestMap.put("indirizzoFornitore", StringUtility.checkSpecialChars((((indirizzoFornitore != null) ? indirizzoFornitore.replaceAll("&nbsp;", " ") : "")) + " - " + StringUtility.checkSpecialChars(((capFornitore != null) ? capFornitore.replaceAll("&nbsp;", " ") : "")) + " - " + StringUtility.checkSpecialChars((cittaFornitore != null ? WordUtils.capitalizeFully(cittaFornitore.replaceAll("&nbsp;", " ").replaceAll("\\*", " ")) : ""))) + StringUtility.checkSpecialChars((provFornitore != null ? " (" + provFornitore.replaceAll("&nbsp;", " ") + ")" : "")));
		requestMap.put("pivaFornitore", StringUtility.checkSpecialChars(getText("dpe.partita.iva")) + ": " + StringUtility.checkSpecialChars((((pivaFornitore != null) ? pivaFornitore.replaceAll("&nbsp;", " ") : ""))));
		requestMap.put("ragSocEdicola",  StringUtility.checkSpecialChars(((ragSocEdicola != null) ? ragSocEdicola.replaceAll("&nbsp;", " ") : "")));
		requestMap.put("indirizzoEdicola",  StringUtility.checkSpecialChars(((indirizzoEdicola != null) ? WordUtils.capitalizeFully(indirizzoEdicola.replaceAll("&nbsp;", " ").replaceAll("\\*", " ")) : "")) + " - " + StringUtility.checkSpecialChars(((capEdicola != null) ? capEdicola.replaceAll("&nbsp;", " ") : "")) + " - " + StringUtility.checkSpecialChars(((cittaEdicola != null) ? WordUtils.capitalizeFully(cittaEdicola.replaceAll("&nbsp;", " ").replaceAll("\\*", " ")) : "")) + StringUtility.checkSpecialChars((provEdicola != null ? " (" + provEdicola.replaceAll("&nbsp;", " ") + ")" : "")));
		requestMap.put("pivaEdicola",  StringUtility.checkSpecialChars(getText("dpe.partita.iva")) + ": " + StringUtility.checkSpecialChars(((pivaEdicola != null) ? pivaEdicola.replaceAll("&nbsp;", " ") : "")));
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
		return super.getTitle() + getText("igeriv.resa.bolla.prodotti.vari");
	}

	public List<TipoLocalitaVo> getTipiLocalita	() {
		return (List<TipoLocalitaVo>) context.getAttribute("tipiLocalita");
	}

	public List<ProvinciaVo> getProvince() {
		return (List<ProvinciaVo>) context.getAttribute("province");
	}

	public String getComboIvaKeys() {
		return context.getAttribute("listAliquoteIva").toString().replace("[", "").replace("]", "").replaceAll(" ", "");
	}

	public String getComboIvaValues() {
		return context.getAttribute("listAliquoteIva").toString().replace("[", "").replace("]", "").replaceAll(" ", "");
	}

	public String getBollaResaEditTab_ev() {
		return BollaResaEditTab_ev;
	}

	public void setBollaResaEditTab_ev(String bollaResaEditTab_ev) {
		BollaResaEditTab_ev = bollaResaEditTab_ev;
	}
	
}
