package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.forEach;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.selectUnique;
import static org.hamcrest.Matchers.equalTo;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.bo.statistiche.StatisticheService;
import it.dpe.igeriv.dto.ContoDepositoDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.dto.StatisticaDettaglioDto;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.BaseVo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * Classe action per la gestione delle pubblicazioni.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@SuppressWarnings({ "rawtypes" })
@Scope("prototype")
@Component("pubblicazioniAction")
public class PubblicazioniAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	@Autowired
	private PubblicazioniService pubblicazioniService;
	@Autowired
	private StatisticheService statisticheService;
	private String tableHeight;
	private String tableTitle;
	private String titolo;
	private String titoloDet;
	private String numero;
	private String sottotitolo;
	private String argomento;
	private String periodicita;
	private String prezzo;
	private String codPubblicazione;
	private String codBarre;
	private String dataTipoBolla;
	private String barcodePubb;
	private List<PubblicazioneDto> listPubblicazioni;
	private List<StatisticaDettaglioDto> listStatisticaDettaglio;
	private String nessunRisultato;
	private String actionName;
	private String tableStyle;
	private String idCliente;
	private String nomeCliente;
	private String idtn;
	private String StatisticaDettaglioTab_ev;
	private Timestamp dataStorico;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		dataStorico = getAuthUser().getDataStorico();
	}
	
	@Override
	public void validate() {
		if ((titolo == null || titolo.equals("")) && (sottotitolo == null || sottotitolo.equals("")) && (argomento == null || argomento.equals(""))
				&& (periodicita == null || periodicita.equals("")) && (prezzo == null || prezzo.equals("")) 
				&& (codPubblicazione == null || codPubblicazione.equals("")) && (codBarre == null || codBarre.equals(""))) {
			addActionError(getText("error.criterio.ricerca"));
		}
		
		if (titolo != null && !titolo.equals("") && (sottotitolo == null || sottotitolo.equals(""))) {
			if (titolo.trim().length() < 3) {
				addActionError(getText("error.ricerca.per.titolo.almeno.tre.caratteri"));
			}else{
				String tmpTitolo = titolo;
				tmpTitolo = tmpTitolo.replaceAll("%", "");
				if (tmpTitolo.trim().length() < 2) {
					addActionError(getText("error.ricerca.per.titolo.almeno.tre.caratteri"));
				}
			}
		}
		if (sottotitolo != null && !sottotitolo.equals("") && (titolo == null || titolo.equals(""))) {
			if (sottotitolo.trim().length() < 3) {
				addActionError(getText("error.ricerca.per.titolo.almeno.un.carattere"));
			}else{
				String tmpSottotitolo = sottotitolo;
				tmpSottotitolo = tmpSottotitolo.replaceAll("%", "");
				if (tmpSottotitolo.trim().length() < 2) {
					addActionError(getText("error.ricerca.per.titolo.almeno.tre.caratteri"));
				}
			}
		}
		
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
			if ((titolo == null || titolo.equals("")) && (sottotitolo == null || sottotitolo.equals("")) && (argomento == null || argomento.equals(""))
					&& (periodicita == null || periodicita.equals("")) && (codPubblicazione == null || codPubblicazione.equals("")) 
					&& (codBarre == null || codBarre.equals(""))) {
				addActionError(getText("error.criterio.ricerca.prezzo"));
			}
		}
		if (periodicita != null && !periodicita.equals("")) {
			if ((titolo == null || titolo.equals("")) && (sottotitolo == null || sottotitolo.equals("")) && (argomento == null || argomento.equals(""))
					&& (codPubblicazione == null || codPubblicazione.equals("")) && (prezzo == null || prezzo.equals("")) 
					&& (codBarre == null || codBarre.equals(""))) {
				addActionError(getText("error.criterio.ricerca.periodicita"));
			}
		}
		if (codPubblicazione != null && !codPubblicazione.equals("")) {
			if (!NumberUtils.isNumber(codPubblicazione)) {
				addFieldError("codPubblicazione", getText("xwork.default.invalid.fieldvalue"));
			}
			
		}
		if (codBarre != null && !codBarre.equals("")) {
			if (!NumberUtils.isNumber(codBarre)) {
				addFieldError("codBarre", getText("xwork.default.invalid.fieldvalue"));
			}
		}
	}
	
	@SkipValidation
	public String showFilter() throws Exception {
		tableTitle = getText("igeriv.ricerca.pubblicazioni");
		return SUCCESS;
	}
	
	@SkipValidation
	public String showFilterRifornimenti() throws Exception {
		tableTitle = getText("igeriv.bolla.inserisci.rifornimenti");
		return SUCCESS;
	}
	
	@SkipValidation
	public String showFilterVariazioni() throws Exception {
		tableTitle = getText("igeriv.bolla.inserisci.variazioni");
		return SUCCESS;
	}
	
	@SkipValidation
	public String showFilterStatistica() throws Exception {
		tableTitle = getText("igeriv.statistica.pubblicazioni");
		return SUCCESS;
	}
	
	@SkipValidation
	public String showFilterPrenotazioneClienti() throws Exception {
		tableTitle = getText("igeriv.prenotazioni.clienti");
		return SUCCESS;
	}
	
	public String showPubblicazioni() {
		tableTitle = getText("igeriv.ricerca.pubblicazioni");
		Integer codPubblicazioneInt = (codPubblicazione != null && !codPubblicazione.equals("")) ? Integer.parseInt(codPubblicazione) : null;
		BigDecimal prezzoBd = (prezzo != null && !prezzo.equals("")) ? new BigDecimal(prezzo) : null;
		Integer[] codDl = ((getAuthUser().isMultiDl() && getAuthUser().isDlInforiv()) || (getAuthUser().isDlInforiv() && getAuthUser().getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_CLIENTE_EDICOLA))) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
		listPubblicazioni = pubblicazioniService.getCopertine(true, false, false, getAuthUser().getCodEdicolaMaster(), codDl, getAuthUser().getArrId(), titolo, sottotitolo, argomento, periodicita, prezzoBd, codPubblicazioneInt, codBarre, false, dataStorico, getAuthUser().getGruppoSconto(), false, null, codPubblicazioneInt);
		requestMap.put("listPubblicazioni", listPubblicazioni);
		if (listPubblicazioni == null || listPubblicazioni.isEmpty()) {
			nessunRisultato = getText("igeriv.nessun.risultato");
		} else {
			tableHeight = getTabHeight(listPubblicazioni);
		}
		return SUCCESS;
	}
	
	public String showPubblicazioniRifornimenti() {
		showPubblicazioni();
		tableTitle = getText("igeriv.bolla.inserisci.rifornimenti");
		return SUCCESS;
	}
	
	public String showPubblicazioniVariazioni() {
		showPubblicazioni();
		tableTitle = getText("igeriv.bolla.inserisci.variazioni");
		return SUCCESS;
	}
	
	public String showPubblicazioniStatistica() {
		showPubblicazioni();
		tableTitle = getText("igeriv.statistica.pubblicazioni");
		return SUCCESS;
	}
	
	public String showPubblicazioniPrenotazioneClienti() {
		showPubblicazioni();
		tableTitle = getText("igeriv.prenotazioni.clienti");
		return SUCCESS;
	}
	
	@SkipValidation
	public String showNumeriPubblicazioni() {
		Integer codPubblicazioneInt = (codPubblicazione != null && !codPubblicazione.equals("")) ? Integer.parseInt(codPubblicazione) : null;
		Integer[] codDl = (getAuthUser().isMultiDl() && getAuthUser().isDlInforiv()) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
		listPubblicazioni = pubblicazioniService.getCopertine(false, false, true, getAuthUser().getCodEdicolaMaster(), codDl, getAuthUser().getArrId(), null, null, null, null, null, codPubblicazioneInt, null, false, dataStorico, getAuthUser().getGruppoSconto(), false, null, codPubblicazioneInt);
		requestMap.put("listPubblicazioni", listPubblicazioni);
		if (listPubblicazioni.size() > 0) {
			String titolo = ((PubblicazioneDto) listPubblicazioni.get(0)).getTitolo();
			tableTitle = getText("igeriv.pubblicazione") + ": " + titolo;
		}
		return IGerivConstants.ACTION_NUMERI_PRECEDENTI;
	}
	
	@SkipValidation
	public String showNumeriPubblicazioniStatistica() {
		Integer codPubblicazioneInt = (codPubblicazione != null && !codPubblicazione.equals("") && NumberUtils.isNumber(codPubblicazione)) ? Integer.parseInt(codPubblicazione) : null;
		if (codPubblicazioneInt != null) {
			Integer[] codDl = (getAuthUser().isMultiDl() && getAuthUser().isDlInforiv()) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
			listPubblicazioni = pubblicazioniService.getCopertine(false, true, false, getAuthUser().getCodEdicolaMaster(), codDl, getAuthUser().getArrId(), null, null, null, periodicita, null, codPubblicazioneInt, null, false, dataStorico, getAuthUser().getGruppoSconto(), false, null, codPubblicazioneInt);
			Integer idtn = Strings.isNullOrEmpty(this.idtn) ? null : new Integer(this.idtn);
			if (idtn != null) {
				PubblicazioneDto copertina = selectUnique(listPubblicazioni, having(on(PubblicazioneDto.class).getIdtn(), equalTo(idtn)));
				listPubblicazioni.clear();
				listPubblicazioni.add(copertina);
			}
			requestMap.put("listPubblicazioni", listPubblicazioni); 
			titoloDet = listPubblicazioni.size() > 0 ? ((PubblicazioneDto) listPubblicazioni.get(0)).getTitolo() : "";
		}
		tableTitle = getText("igeriv.pubblicazione") + ": " + titoloDet;
		return IGerivConstants.ACTION_NUMERI_PRECEDENTI;
	}
	
	@SkipValidation
	public String showStatisticaDettaglioFornito() {
		Integer idtn = (this.idtn != null && !this.idtn.equals("")) ? Integer.parseInt(this.idtn) : null;
		Integer[] codDl = (getAuthUser().isMultiDl() && getAuthUser().isDlInforiv()) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
		listStatisticaDettaglio = statisticheService.getStatisticaDettaglioFornito(codDl, getAuthUser().getArrId(), idtn,dataStorico);
		if (listStatisticaDettaglio == null || listStatisticaDettaglio.isEmpty() || listStatisticaDettaglio.get(0).getCopie() == null || listStatisticaDettaglio.get(0).getCopie().equals(0)) {
			listStatisticaDettaglio = statisticheService.getStatisticaDettaglioFornitoRifornimenti(codDl, getAuthUser().getArrId(), idtn);
			forEach(listStatisticaDettaglio).setTipo(IGerivConstants.TIPO_STATISTICA_DETTAGLIO_RIFORNIMENTI);
		} else {
			forEach(listStatisticaDettaglio).setTipo(IGerivConstants.TIPO_STATISTICA_DETTAGLIO_FORNITO);
		}
		requestMap.put("listStatisticaDettaglio", listStatisticaDettaglio); 
		tableTitle = getText("igeriv.dettaglio.fornito") + "<br>" + getText("igeriv.pubblicazione") + ": " + titolo + " " + getText("igeriv.report.numero") + ": " + numero;
		if (StatisticaDettaglioTab_ev != null && (StatisticaDettaglioTab_ev.toUpperCase().equals("PDF") || StatisticaDettaglioTab_ev.toUpperCase().equals("XLS"))) {
			tableTitle = StringUtility.checkSpecialChars(tableTitle);
		}
		return "statisticaDettaglioFornito";
	}
	
	@SkipValidation
	public String showStatisticaDettaglioReso() {
		Integer idtn = (this.idtn != null && !this.idtn.equals("")) ? Integer.parseInt(this.idtn) : null;
		Integer[] codDl = (getAuthUser().isMultiDl() && getAuthUser().isDlInforiv()) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
		listStatisticaDettaglio = statisticheService.getStatisticaDettaglioReso(codDl, getAuthUser().getArrId(), idtn);
		requestMap.put("listStatisticaDettaglio", listStatisticaDettaglio); 
		tableTitle = getText("igeriv.dettaglio.reso") + "<br>" + getText("igeriv.pubblicazione") + ": " + titolo + " " + getText("igeriv.report.numero") + ": " + numero;
		if (StatisticaDettaglioTab_ev != null && (StatisticaDettaglioTab_ev.toUpperCase().equals("PDF") || StatisticaDettaglioTab_ev.toUpperCase().equals("XLS"))) {
			tableTitle = StringUtility.checkSpecialChars(tableTitle);
		}
		return "statisticaDettaglioReso";
	}
	
	@SkipValidation
	public String showStatisticaDettaglioResoRiscontrato() {
		Integer idtn = (this.idtn != null && !this.idtn.equals("")) ? Integer.parseInt(this.idtn) : null;
		Integer[] codDl = (getAuthUser().isMultiDl() && getAuthUser().isDlInforiv()) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
		listStatisticaDettaglio = statisticheService.getStatisticaDettaglioResoRiscontrato(codDl, getAuthUser().getArrId(), idtn);
		requestMap.put("listStatisticaDettaglio", listStatisticaDettaglio); 
		tableTitle = getText("igeriv.dettaglio.reso.riscontrato") + "<br>" + getText("igeriv.pubblicazione") + ": " + titolo + " " + getText("igeriv.report.numero") + ": " + numero;
		if (StatisticaDettaglioTab_ev != null && (StatisticaDettaglioTab_ev.toUpperCase().equals("PDF") || StatisticaDettaglioTab_ev.toUpperCase().equals("XLS"))) {
			tableTitle = StringUtility.checkSpecialChars(tableTitle);
		}
		return "statisticaDettaglioReso";
	}
	
	@SkipValidation
	public String showStatisticaDettaglioVenduto() {
		Integer idtn = (this.idtn != null && !this.idtn.equals("")) ? Integer.parseInt(this.idtn) : null;
		Integer[] codDl = (getAuthUser().isMultiDl() && getAuthUser().isDlInforiv()) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
		listStatisticaDettaglio = statisticheService.getStatisticaDettaglioVenduto(codDl, getAuthUser().getArrId(), idtn);
		requestMap.put("listStatisticaDettaglio", listStatisticaDettaglio); 
		tableTitle = getText("igeriv.dettaglio.venduto") + "<br>" + getText("igeriv.pubblicazione") + ": " + titolo + " " + getText("igeriv.report.numero") + ": " + numero;
		if (StatisticaDettaglioTab_ev != null && (StatisticaDettaglioTab_ev.toUpperCase().equals("PDF") || StatisticaDettaglioTab_ev.toUpperCase().equals("XLS"))) {
			tableTitle = StringUtility.checkSpecialChars(tableTitle);
		}
		return "statisticaDettaglioVenduto";
	}
	
	@SkipValidation
	public String reportContoDepositoFilter() {
		tableTitle = getText("igeriv.pubblicazioni.conto.deposito");
		return IGerivConstants.ACTION_REPORT_CONTO_DEPOSITO;
	}
	
	@SkipValidation
	public String reportContoDeposito() {
		actionName = "infoPubblicazioni_reportContoDeposito.action";
		Integer[] codDl = (getAuthUser().isMultiDl() && getAuthUser().isDlInforiv()) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
		List<ContoDepositoDto> listContoDeposito = pubblicazioniService.getPubblicazioniContoDeposito(codDl, 
				getAuthUser().getArrId(), getAuthUser().getGruppoSconto(), titolo, codBarre);
		requestMap.put("listContoDeposito", listContoDeposito);
		tableTitle = getText("igeriv.pubblicazioni.conto.deposito");
		return IGerivConstants.ACTION_REPORT_CONTO_DEPOSITO;
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
		String title = getText("igeriv.pubblicazioni");
		if (!Strings.isNullOrEmpty(getActionName())) {
			if (getActionName().contains("variazioni_")) {
				title = getText("igeriv.bolla.variazioni");
			} else if (getActionName().contains("viewVariazioni_")) {
				title = getText("igeriv.menu.61");
			} else if (getActionName().contains("statisticaPubblicazioni_")) {
				title = getText("igeriv.statistica.pubblicazioni");
			} else if (getActionName().contains("ContoDeposito")) {
				title = getText("igeriv.pubblicazioni.conto.deposito");
			}
		}
		return super.getTitle() + title;
	}

	public String getTableHeight() {
		return tableHeight;
	}

	public void setTableHeight(String tableHeight) {
		this.tableHeight = tableHeight;
	}
	
	public String getTableTitle() {
		return tableTitle;
	}

	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getSottotitolo() {
		return sottotitolo;
	}

	public void setSottotitolo(String sottotitolo) {
		this.sottotitolo = sottotitolo;
	}

	public String getArgomento() {
		return argomento;
	}

	public void setArgomento(String argomento) {
		this.argomento = argomento;
	}

	public String getPeriodicita() {
		return periodicita;
	}

	public void setPeriodicita(String periodicita) {
		this.periodicita = periodicita;
	}

	public String getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(String prezzo) {
		this.prezzo = prezzo;
	}

	public String getCodPubblicazione() {
		return codPubblicazione;
	}

	public void setCodPubblicazione(String codPubblicazione) {
		this.codPubblicazione = codPubblicazione;
	}

	public String getCodBarre() {
		return codBarre;
	}

	public void setCodBarre(String codBarre) {
		this.codBarre = codBarre;
	}
	
	public String getDataTipoBolla() {
		return dataTipoBolla;
	}

	public void setDataTipoBolla(String dataTipoBolla) {
		this.dataTipoBolla = dataTipoBolla;
	}
	
	public String getBarcodePubb() {
		return barcodePubb;
	}

	public void setBarcodePubb(String barcodePubb) {
		this.barcodePubb = barcodePubb;
	}

	public List<PubblicazioneDto> getListPubblicazioni() {
		return listPubblicazioni;
	}

	public void setListPubblicazioni(
			List<PubblicazioneDto> listPubblicazioni) {
		this.listPubblicazioni = listPubblicazioni;
	}

	public String getNessunRisultato() {
		return nessunRisultato;
	}

	public void setNessunRisultato(String nessunRisultato) {
		this.nessunRisultato = nessunRisultato;
	}
	
	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	public String getTableStyle() {
		return tableStyle;
	}

	public void setTableStyle(String tableStyle) {
		this.tableStyle = tableStyle;
	}
	
	public String getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}

	public String getNomeCliente() {
		return (nomeCliente != null) ? nomeCliente.toUpperCase() : nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getIdtn() {
		return idtn;
	}

	public void setIdtn(String idtn) {
		this.idtn = idtn;
	}

	public String getTitoloDet() {
		return titoloDet;
	}

	public void setTitoloDet(String titoloDet) {
		this.titoloDet = titoloDet;
	}

	public String getStatisticaDettaglioTab_ev() {
		return StatisticaDettaglioTab_ev;
	}

	public void setStatisticaDettaglioTab_ev(String statisticaDettaglioTab_ev) {
		StatisticaDettaglioTab_ev = statisticaDettaglioTab_ev;
	}

	public PubblicazioniService getPubblicazioniService() {
		return pubblicazioniService;
	}

	public void setPubblicazioniService(PubblicazioniService pubblicazioniService) {
		this.pubblicazioniService = pubblicazioniService;
	}

	public StatisticheService getStatisticheService() {
		return statisticheService;
	}

	public void setStatisticheService(StatisticheService statisticheService) {
		this.statisticheService = statisticheService;
	}

}
