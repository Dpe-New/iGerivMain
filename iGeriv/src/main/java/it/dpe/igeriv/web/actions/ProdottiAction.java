package it.dpe.igeriv.web.actions;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.fornitori.FornitoriService;
import it.dpe.igeriv.bo.prodotti.ProdottiService;
import it.dpe.igeriv.dto.ProdottoDto;
import it.dpe.igeriv.dto.RichiestaProdottoDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiFornitoreVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiRichiesteRifornimentoDettaglioVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiRichiesteRifornimentoVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiVo;
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiRichiesteRifornimentoDettaglioPk;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per la gestione dei prodotti non editoriali del dl.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("prodottiAction")
@SuppressWarnings({ "rawtypes" })
public class ProdottiAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	private final ProdottiService prodottiService;
	private final EdicoleService edicoleService;
	private final FornitoriService fornitoriService;
	private final String crumbNamePneDl = getText("igeriv.ricerca.prodotti.non.editoriali.dl");
	private final String crumbNameRiPneDl = getText("igeriv.inserisci.richieste.prodotti.non.editoriali.dl");
	private final String crumbNameViewRiPneDl = getText("igeriv.view.richieste.prodotti.non.editoriali.dl");
	private String tableHeight;
	private String tableTitle;
	private String codice;
	private String codRichiestaRifornimento;
	private String descrizione;
	private String categoria;
	private String sottocategoria;
	private String barcode;
	private String prezzo;
	private List<ProdottoDto> listProdotti;
	private List<RichiestaProdottoDto> listRichiesteProdotti;
	private RichiestaProdottoDto richiestaRifornimentoProdotto;
	private String nessunRisultato;
	private String actionName;
	private String tableStyle;
	private String strDataDa;
	private String strDataA;
	private String stato;
	private Timestamp dataDa; 
	private Timestamp dataA;
	private String prenotazioneDisabled = "false";
	
	public ProdottiAction() {
		this.prodottiService = null;
		this.edicoleService = null;
		this.fornitoriService = null;
	}
	
	@Autowired
	public ProdottiAction(ProdottiService prodottiService, EdicoleService edicoleService, FornitoriService fornitoriService) {
		this.prodottiService = prodottiService;
		this.edicoleService = edicoleService;
		this.fornitoriService = fornitoriService;
	}
	
	@Override
	public void validate() {
		if (getActionName() != null && !getActionName().toUpperCase().contains("VIEW")) {
			tableTitle = getActionName().contains("richiesteProdotti_") ? getText("igeriv.inserisci.richieste.prodotti.non.editoriali.dl") : getText("igeriv.ricerca.prodotti.non.editoriali.dl");
			if (prezzo != null && !prezzo.equals("")) {
				int commaPos = prezzo.lastIndexOf(",");
				int dotPos = prezzo.lastIndexOf(".");
				if (dotPos > commaPos) {
					prezzo = prezzo.replaceAll(",", "");
				} else if (commaPos > dotPos) {
					prezzo = prezzo.replaceAll("\\.", "");
					if (prezzo.contains(",")) {
						prezzo = prezzo.replace(',', '.');
					}
				}
				if (!NumberUtils.isNumber(prezzo)) {
					addFieldError("prezzo", getText("xwork.default.invalid.fieldvalue"));
				}
			}
			if (codice != null && !codice.equals("")) {
				if (!NumberUtils.isNumber(codice)) {
					addFieldError("codPubblicazione", getText("xwork.default.invalid.fieldvalue"));
				}
				
			}
			if (barcode != null && !barcode.equals("")) {
				if (!NumberUtils.isNumber(barcode)) {
					addFieldError("codBarre", getText("xwork.default.invalid.fieldvalue"));
				}
			}
		} else if (getActionName() != null && getActionName().toUpperCase().contains("SAVE")) {
			if (richiestaRifornimentoProdotto != null && richiestaRifornimentoProdotto.getQuatitaRichiesta() == null) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.required.fields") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
		} else {
			tableTitle = getText("igeriv.view.richieste.prodotti.non.editoriali.dl");
			if (strDataDa == null || strDataA == null || strDataDa.equals("") || strDataA.equals("")) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				strDataA = DateUtilities.getTimestampAsString(cal.getTime(), DateUtilities.FORMATO_DATA_SLASH);
				cal.add(Calendar.DAY_OF_MONTH, 15);
				strDataDa = DateUtilities.getTimestampAsString(cal.getTime(), DateUtilities.FORMATO_DATA_SLASH);
			} else {
				try {
					dataDa = DateUtilities.floorDay(DateUtilities.parseDate(strDataDa, DateUtilities.FORMATO_DATA_SLASH));
				} catch (ParseException e) {
					addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), strDataDa));
					return;
				}
				try {
					dataA = DateUtilities.ceilDay(DateUtilities.parseDate(strDataA, DateUtilities.FORMATO_DATA_SLASH));
				} catch (ParseException e) {
					addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), strDataA));
					return;
				}
			}
		}
	}
	
	@BreadCrumb("%{crumbNamePneDl}")
	@SkipValidation
	public String showProdottiFilter() throws Exception {
		tableTitle = getText("igeriv.ricerca.prodotti.non.editoriali.dl");
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameRiPneDl}")
	@SkipValidation
	public String showRichiesteProdottiFilter() throws Exception {
		tableTitle = getText("igeriv.inserisci.richieste.prodotti.non.editoriali.dl");
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameViewRiPneDl}")
	@SkipValidation
	public String showViewRichiesteProdottiFilter() throws Exception {
		tableTitle = getText("igeriv.view.richieste.prodotti.non.editoriali.dl");
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNamePneDl}")
	public String showProdotti() {
		tableTitle = getText("igeriv.ricerca.prodotti.non.editoriali.dl");
		Long categoria = it.dpe.igeriv.util.NumberUtils.getAsLong(this.categoria);
		Long sottocategoria = it.dpe.igeriv.util.NumberUtils.getAsLong(this.sottocategoria);
		Float prezzo = (this.prezzo != null && !this.prezzo.equals("")) ? new Float(this.prezzo) : null;
		listProdotti = prodottiService.getProdotti(getAuthUser().getCodFiegDl(), getAuthUser().getId(), getAuthUser().getCodContabileCliente(), this.codice, descrizione, categoria, sottocategoria, barcode, prezzo);
		setImmagineGrandeProdotto(listProdotti);
		requestMap.put("listProdotti", listProdotti);
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameRiPneDl}")
	public String showRichiesteProdotti() {
		tableTitle = getText("igeriv.inserisci.richieste.prodotti.non.editoriali.dl");
		Long categoria = it.dpe.igeriv.util.NumberUtils.getAsLong(this.categoria);
		Long sottocategoria = it.dpe.igeriv.util.NumberUtils.getAsLong(this.sottocategoria);
		Float prezzo = (this.prezzo != null && !this.prezzo.equals("")) ? new Float(this.prezzo) : null;
		listProdotti = prodottiService.getProdotti(getAuthUser().getCodFiegDl(), getAuthUser().getId(), getAuthUser().getCodContabileCliente(), this.codice, descrizione, categoria, sottocategoria, barcode, prezzo);
		setImmagineGrandeProdotto(listProdotti);
		requestMap.put("listProdotti", listProdotti);
		return SUCCESS; 
	}
	
	public String showRichiestaRifornimentoProdotto() {
		tableTitle = MessageFormat.format(getText("igeriv.richiesta.rifornimento.prodotto"), descrizione);
		Long codice = it.dpe.igeriv.util.NumberUtils.getAsLong(this.codice);
		richiestaRifornimentoProdotto = prodottiService.getRichiestaRifornimentoProdotto(codice);
		if (richiestaRifornimentoProdotto != null) {
			codRichiestaRifornimento = richiestaRifornimentoProdotto.getCodRichiestaRifornimento().toString();
			this.codice = richiestaRifornimentoProdotto.getCodProdottoInterno().toString();
		} else {
			richiestaRifornimentoProdotto = new RichiestaProdottoDto();
			richiestaRifornimentoProdotto.setStato("" + IGerivConstants.COD_STATO_EVASIONE_INSERITO);
		}
		return "richiesteRifornimentiProdotti";
	}
	
	public String saveRichiestaRifornimentoProdotto() {
		tableTitle = getText("igeriv.inserisci.richieste.prodotti.non.editoriali.dl");
		Long codice = it.dpe.igeriv.util.NumberUtils.getAsLong(this.codice);
		ProdottiNonEditorialiRichiesteRifornimentoDettaglioVo vo = null;
		Timestamp now = new Timestamp(new Date().getTime());
		if (!Strings.isNullOrEmpty(codRichiestaRifornimento)) {
			vo = prodottiService.getProdottiRichiesteRifornimentoDettaglioVo(codice, new Long(codRichiestaRifornimento));
		}
		if (vo == null || vo.getStato() != IGerivConstants.STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_INSERITO) {
			vo = new ProdottiNonEditorialiRichiesteRifornimentoDettaglioVo();
			ProdottiNonEditorialiRichiesteRifornimentoVo pvo = new ProdottiNonEditorialiRichiesteRifornimentoVo();
			pvo.setDataRichiesta(now);
			pvo.setEdicola(edicoleService.getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(getAuthUser().getCodFiegDl(), getAuthUser().getCodEdicolaDl()));
			AbbinamentoEdicolaDlVo edicola = edicoleService.getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(getAuthUser().getCodFiegDl(), getAuthUser().getCodEdicolaDl());
			pvo.setEdicola(edicola);
			ProdottiNonEditorialiFornitoreVo fornitore = fornitoriService.getFornitore(getAuthUser().getId(), getAuthUser().getCodFiegDl());
			pvo.setCodFornitore(fornitore.getPk().getCodFornitore());
			pvo.setStato(IGerivConstants.STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_NON_ANCORA_ELABORATA);
			prodottiService.saveBaseVo(pvo);
			ProdottiNonEditorialiRichiesteRifornimentoDettaglioPk pk1 = new ProdottiNonEditorialiRichiesteRifornimentoDettaglioPk();
			pk1.setCodProdottoInterno(codice);
			pk1.setCodRichiestaRifornimento(pvo.getCodRichiestaRifornimento());
			vo.setPk(pk1);
			vo.setEdicola(edicola.getAnagraficaEdicolaVo());
			ProdottiNonEditorialiVo prodottoNonEditorialeVo = prodottiService.getProdottoNonEditorialeVo(codice);
			vo.setProdotto(prodottoNonEditorialeVo);
		}
		vo.setDataUltAggiornamento(now);
		vo.setQuatitaRichiesta(richiestaRifornimentoProdotto.getQuatitaRichiesta());
		vo.setStato(IGerivConstants.STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_INSERITO);
		if (vo.getProdotto().getFormazionePacco() != null && vo.getProdotto().getFormazionePacco() > 0) {
			if (richiestaRifornimentoProdotto.getQuatitaRichiesta() != null 
					&& (richiestaRifornimentoProdotto.getQuatitaRichiesta() % vo.getProdotto().getFormazionePacco() != 0)) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.quantita.richiesta.non.multiplo.di.formazione.pacco"), vo.getProdotto().getFormazionePacco(), vo.getProdotto().getFormazionePacco()) + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException(); 
			}
		}
		if (richiestaRifornimentoProdotto.getQuatitaRichiesta() == null || richiestaRifornimentoProdotto.getQuatitaRichiesta().equals(0)) {
			prodottiService.deleteVo(vo);
		} else {
			if (!Strings.isNullOrEmpty(codRichiestaRifornimento) && vo.getRichiesteRifornimento() != null) {
				ProdottiNonEditorialiRichiesteRifornimentoVo richiesteRifornimento = vo.getRichiesteRifornimento();
				richiesteRifornimento.setDataRichiesta(now);
				prodottiService.saveBaseVo(richiesteRifornimento);
			}	
			prodottiService.saveBaseVo(vo);
		}
		requestMap.put("richiestaRifornimentoProdotto", richiestaRifornimentoProdotto);
		return "richiesteRifornimentiProdotti";
	}
	
	@BreadCrumb("%{crumbNameViewRiPneDl}")
	public String showViewRichiesteProdotti() {
		tableTitle = getText("igeriv.view.richieste.prodotti.non.editoriali.dl");
		listRichiesteProdotti = prodottiService.getRichiesteProdotti(getAuthUser().getCodFiegDl(), getAuthUser().getCodEdicolaDl(), descrizione, this.stato, dataDa, dataA);
		requestMap.put("listRichiesteProdotti", listRichiesteProdotti);
		return SUCCESS;
	}
	
	/**
	 * Setta il nome dell'immagine a null se l'immagine è una "fake" (non esiste immagine per il prodotto)
	 * 
	 * @param listProdotti
	 */
	private void setImmagineGrandeProdotto(List<ProdottoDto> listProdotti) {
		for (ProdottoDto dto : listProdotti) {
			if (dto.getImmagine() != null && dto.getImmagine().contains("_fake")) {
				dto.setImmagine(null);
			}
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
		String title = getText("igeriv.menu.43");
		return super.getTitle() + title;
	}

}
