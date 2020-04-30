package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.forEach;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.notNullValue;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.bo.fornitori.FornitoriService;
import it.dpe.igeriv.bo.prodotti.ProdottiService;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
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
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiPrezziAcquistoPk;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per la gestione delle anagrafiche dei prodotti non editoriali.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("prodottiNonEditorialiCaricoBollaAction")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ProdottiNonEditorialiCaricoBollaAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final FornitoriService fornitoriService;
	private final ProdottiService prodottiService;
	private final String crumbName = getText("igeriv.carico.bolla.prodotti.vari");
	private String tableTitle;
	private List<ProdottiNonEditorialiFornitoreVo> fornitori;
	private List<ProdottiNonEditorialiCausaliMagazzinoVo> causali;
	private List<ProdottiNonEditorialiBollaVo> bolle;
	private ProdottiNonEditorialiBollaVo bolla;
	private List<ProdottiNonEditorialiBollaDettaglioVo> dettagli;
	private Integer codFornitore;
	private String dataDocumento;
	private String numeroDocumento;
	private Integer codiceCausale;
	private Long idDocumento;
	private Boolean isNew;
	private String BollaCaricoEditTab_ev;
	private boolean disabled = false;
	
	public ProdottiNonEditorialiCaricoBollaAction() {
		this.fornitoriService = null;
		this.prodottiService = null;
	}
	
	@Autowired
	public ProdottiNonEditorialiCaricoBollaAction(FornitoriService fornitoriService, ProdottiService prodottiService) {
		this.fornitoriService = fornitoriService;
		this.prodottiService = prodottiService;
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
			tableTitle = getText("igeriv.carico.bolla.prodotti.vari");
			if (dataDocumento != null && !dataDocumento.equals("")) { 
				try {
					DateUtilities.floorDay(DateUtilities.parseDate(dataDocumento, DateUtilities.FORMATO_DATA_SLASH));
				} catch (ParseException e) {
					addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataDocumento));
					return;
				}
			}
		} else if (getActionName().contains("saveBolla")) {
			tableTitle = getText("igeriv.inserisc.aggiorna.carico.bolla.prodotti.vari");
			if (bolla != null) {
				if (bolla.getDataDocumento() == null) {
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("igeriv.data.documento")) + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				}
				if (bolla.getNumeroDocumento() == null) {
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("igeriv.numero.documento")) + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				} 
			}
		}
	}
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showFilter() throws Exception {
		tableTitle = getText("igeriv.carico.bolla.prodotti.vari");
		return SUCCESS;
	}
	
	@SkipValidation
	public String editBolla() throws Exception {
		tableTitle = getText("igeriv.inserisc.aggiorna.carico.bolla.prodotti.vari");
		if (idDocumento != null) {
			bolla = prodottiService.getBolleProdottiVariEdicola(getAuthUser().getCodEdicolaMaster(), idDocumento);
			dettagli = select(bolla.getDettagli(), notNullValue());
			disabled =  (bolla != null && bolla.getCodiceFornitore() < IGerivConstants.COD_INIZIO_FORNITORI_NON_DL);
		} else {
			bolla = new ProdottiNonEditorialiBollaVo(); 
			isNew = true;
		}
		if (BollaCaricoEditTab_ev != null && (BollaCaricoEditTab_ev.toUpperCase().equals("PDF") || BollaCaricoEditTab_ev.toUpperCase().equals("XLS"))) {
			setFornitoreEdicolaExportTitle(bolla);
			requestMap.put("titolo", MessageFormat.format(getText("igeriv.carico.bolla.prodotti.vari.title"), bolla.getNumeroDocumento().toString(), DateUtilities.getTimestampAsString(bolla.getDataDocumento(), DateUtilities.FORMATO_DATA)));
		}
		return "editBollaCarico";
	}
	
	/**
	 * Costruisce il titolo da visualizzare nella testa del report bolla di carico prodotti 
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
		requestMap.put("indirizzoEdicola",  StringUtility.checkSpecialChars(((indirizzoEdicola != null) ?  WordUtils.capitalizeFully(indirizzoEdicola.replaceAll("&nbsp;", " ").replaceAll("\\*", " ")) : "")) + " - " + StringUtility.checkSpecialChars(((capEdicola != null) ? capEdicola.replaceAll("&nbsp;", " ") : "")) + " - " + StringUtility.checkSpecialChars(((cittaEdicola != null) ? WordUtils.capitalizeFully(cittaEdicola.replaceAll("&nbsp;", " ").replaceAll("\\*", " ")) : "")) + StringUtility.checkSpecialChars((provEdicola != null ? " (" + provEdicola.replaceAll("&nbsp;", " ") + ")" : "")));
		requestMap.put("pivaEdicola",  StringUtility.checkSpecialChars(getText("dpe.partita.iva")) + ": " + StringUtility.checkSpecialChars(((pivaEdicola != null) ? pivaEdicola.replaceAll("&nbsp;", " ") : "")));
	}
	
	@BreadCrumb("%{crumbName}")
	public String showBolle() throws ParseException {
		tableTitle = getText("igeriv.carico.bolla.prodotti.vari");
		Timestamp dataDocumento = (this.dataDocumento != null && !this.dataDocumento.equals("")) ? DateUtilities.parseDate(this.dataDocumento, DateUtilities.FORMATO_DATA_SLASH) : null;
		bolle = prodottiService.getBolleProdottiVariEdicola(getAuthUser().getCodEdicolaMaster(), codFornitore, codiceCausale, dataDocumento, numeroDocumento, false);
		requestMap.put("fornitori", fornitori);
		return SUCCESS;
	}
	
	public String saveBolla() throws Exception {
		tableTitle = getText("igeriv.carico.bolla.prodotti.vari");
		try {
			if (bolla != null && dettagli != null) {
				if (isNew) {
					Long idDocumento = prodottiService.getNextSeqVal(IGerivConstants.SEQ_BOLLE_CARICO_PRODOTTI_VARI);
					bolla.setIdDocumento(idDocumento);
					forEach(dettagli).getPk().setIdDocumento(idDocumento);
				}
				if (bolla.getCodiceFornitore().equals(getAuthUser().getCodFiegDl())) {
					throw new Exception();
				}
				bolla.setDeleted(false);
				checkDuplicateProducts(dettagli);
				bolla.setDettagli(dettagli);
				ProdottiNonEditorialiCausaliMagazzinoVo causale = prodottiService.getCausaleMagazzino(codiceCausale);
				bolla.setCausale(causale);
				forEach(dettagli).setCausale(causale);
				bolla.setIndicatoreEmessoRicevuto(IGerivConstants.INDICATORE_DOCUMENTO_RICEVUTO);
				bolla.setDataRegistrazione(prodottiService.getSysdate());
				List<ProdottiNonEditorialiVo> listProdotti = new ArrayList<ProdottiNonEditorialiVo>();
				List<ProdottiNonEditorialiPrezziAcquistoVo> prezziAcquistoEdit = new ArrayList<ProdottiNonEditorialiPrezziAcquistoVo>();
				buildPrezziAndNuoviProdotti(dettagli, prezziAcquistoEdit, listProdotti);
				prodottiService.saveBollaProdottiVari(bolla, prezziAcquistoEdit, listProdotti, bolla.getCodiceFornitore(), getAuthUser().getCodEdicolaMaster(), isNew);
			}
		} catch (IGerivBusinessException e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + e.getLocalizedMessage() + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		} catch (it.dpe.igeriv.exception.ConstraintViolationException e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.codice.fornitore.prodotti.duplicati") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "editBollaCarico";
	}

	/**
	 * Riempie le liste di prezzi (abbinamento prodotti/fornitori) e anagrafiche prodotti da inserire e/o aggiornare.
	 * 
	 * @param dettagli
	 * @param prezziAcquisto
	 * @param listProdotti
	 * @throws IGerivBusinessException 
	 */
	private void buildPrezziAndNuoviProdotti(List<ProdottiNonEditorialiBollaDettaglioVo> dettagli, List<ProdottiNonEditorialiPrezziAcquistoVo> prezziAcquisto, List<ProdottiNonEditorialiVo> listProdotti) throws IGerivBusinessException {
		for (ProdottiNonEditorialiBollaDettaglioVo bdvo : dettagli) {
			bdvo.setDeleted(false);
			
			if (bdvo.getCodiceProdottoFornitore() != null && !bdvo.getCodiceProdottoFornitore().equals("")) {
				ProdottiNonEditorialiPrezziAcquistoVo pneavo = prodottiService.getProdottiNonEditorialiPrezzoAcquisto(getAuthUser().getCodEdicolaMaster(), bdvo.getProdotto().getCodProdottoInterno(), bdvo.getCodiceProdottoFornitore());
				if (pneavo == null) {
					pneavo = new ProdottiNonEditorialiPrezziAcquistoVo();
					ProdottiNonEditorialiPrezziAcquistoPk pk = new ProdottiNonEditorialiPrezziAcquistoPk();
					pk.setCodiceProdottoFornitore(bdvo.getCodiceProdottoFornitore());
					pk.setCodEdicola(getAuthUser().getCodEdicolaMaster());
					if (bdvo.getProdotto().getCodProdottoInterno() != null) {
						pk.setCodProdotto(bdvo.getProdotto().getCodProdottoInterno());
					} 
					pneavo.setPk(pk);
					pneavo.setCodiceFornitore(bolla.getCodiceFornitore());
				} else {
					ProdottiNonEditorialiVo pneb = prodottiService.getProdottoNonEditorialeVo(bdvo.getProdotto().getCodProdottoInterno());
					String barcode = NumberUtils.isNumber(bdvo.getProdotto().getBarcode()) ? bdvo.getProdotto().getBarcode() : null;
					boolean barcodeChanged = (barcode != null && pneb.getBarcode() == null) || (barcode != null && pneb.getBarcode() != null && !barcode.equals(pneb.getBarcode()));
					Integer aliquotaPrecedente = (pneb.getAliquota() == null) ? 0 : pneb.getAliquota();
					Integer aliquota = bdvo.getProdotto().getAliquota() == null ? 0 : bdvo.getProdotto().getAliquota();
					boolean aliquotaChanged = !aliquotaPrecedente.equals(aliquota);
					if (barcodeChanged || aliquotaChanged) {
						if (barcodeChanged) {
							pneb.setBarcode(barcode);
						}
						if (aliquotaChanged) {
							pneb.setAliquota(aliquota);
						}
						listProdotti.add(pneb);
					}
				}
				pneavo.setUltimoPrezzoAcquisto(bdvo.getPrezzo());
				prezziAcquisto.add(pneavo);
			} else {
				ProdottiNonEditorialiVo pnevo = null;
				if (bdvo.getProdotto().getCodProdottoInterno() != null) {
					pnevo = prodottiService.getProdottiNonEditorialiVo(bdvo.getProdotto().getCodProdottoInterno(), getAuthUser().getCodEdicolaMaster());
				} else {
					throw new IGerivBusinessException(MessageFormat.format(getText("gp.errore.prodotto.non.editoriale.non.aggiunto"), bdvo.getProdotto().getBarcode()));
				}
				String barcode = NumberUtils.isNumber(bdvo.getProdotto().getBarcode()) ? bdvo.getProdotto().getBarcode() : null;
				boolean barcodeChanged = (barcode != null && pnevo.getBarcode() == null) || (barcode != null && pnevo.getBarcode() != null && !barcode.equals(pnevo.getBarcode()));
				Integer aliquotaPrecedente = (pnevo.getAliquota() == null) ? 0 : pnevo.getAliquota();
				Integer aliquota = bdvo.getProdotto().getAliquota() == null ? 0 : bdvo.getProdotto().getAliquota();
				boolean aliquotaChanged = !aliquotaPrecedente.equals(aliquota);
				if (barcodeChanged || aliquotaChanged) {
					pnevo.setBarcode(bdvo.getProdotto().getBarcode());
					pnevo.setAliquota(bdvo.getProdotto().getAliquota());
					listProdotti.add(pnevo);
				}
				bdvo.setProdotto(pnevo);
			}
		}
	}
	
	/**
	 * Verifica se esistono prodotti duplicati nella bolla.
	 * 
	 * @param dettagli
	 * @throws IGerivBusinessException
	 */
	private void checkDuplicateProducts(List<ProdottiNonEditorialiBollaDettaglioVo> dettagli) throws IGerivBusinessException {
		List<Long> productIds = extract(dettagli, on(ProdottiNonEditorialiBollaDettaglioVo.class).getProdotto().getCodProdottoInterno());
		Set<Long> set = new HashSet<Long>(productIds);
		if (set.size() < productIds.size()) {
			throw new IGerivBusinessException(getText("gp.errore.prodotti.duplicati.bolla.carico.prodotti.vari"));
		}
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String deleteBolla() throws Exception {
		tableTitle = getText("igeriv.carico.bolla.prodotti.vari");
		if (bolla != null) {
			prodottiService.deleteBollaProdottiVari(bolla.getIdDocumento(), bolla.getCodiceFornitore(), getAuthUser().getCodEdicolaMaster());
		}
		return "editBollaCarico";
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
		return super.getTitle() + getText("igeriv.carico.bolla.prodotti.vari");
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

	public String getBollaCaricoEditTab_ev() {
		return BollaCaricoEditTab_ev;
	}

	public void setBollaCaricoEditTab_ev(String bollaCaricoEditTab_ev) {
		BollaCaricoEditTab_ev = bollaCaricoEditTab_ev;
	}
	
}
