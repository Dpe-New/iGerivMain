package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.bo.statistiche.StatisticheService;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.dto.StatisticaDettaglioDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.ValidationCodiceBarre;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.StoricoCopertineVo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.jfree.util.Log;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * Classe action per la gestione delle pubblicazioni.
 */
@Getter
@Setter
@Scope("prototype")
@Component("pubblicazioniAction")
@SuppressWarnings({ "rawtypes" })
public class PubblicazioniAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(getClass());
	
	private final PubblicazioniService pubblicazioniService;
	private final StatisticheService statisticheService;
	private final ValidationCodiceBarre validationCodiceBarre;
	
	private final String crumbName = getText("igeriv.ricerca.pubblicazioni");
	private final String crumbNamePubbInv = getText("igeriv.pubblicazioni.invedute");
	private final String crumbNamePubbCD = getText("igeriv.pubblicazioni.conto.deposito");
	private final String crumbNamePubbSta = getText("igeriv.statistica.pubblicazioni");
	private final String crumbNamePrenoCli = getText("igeriv.prenotazioni.clienti");
	private final String crumbNameInsVaria = getText("igeriv.bolla.inserisci.variazioni");
	private final String crumbNameInsRif = getText("igeriv.bolla.inserisci.rifornimenti");
	
	@Setter
	@Getter
	private Map<String, String> nuovoBarcodeMap;
	private String tableHeight;
	private String tableTitle;
	private String filterTitle;
	private String titolo;
	private String titoloDet;
	private String numero;
	private String sottotitolo;
	private String argomento;
	private String periodicita;
	private String prezzo;
	private String codPubblicazione;
	private String codPubblicazione_opInserisci;
	private String codBarre;
	private String codBarreEAN_opInserisci;
	private String dataTipoBolla;
	private String barcodePubb;
	private List<PubblicazioneDto> listPubblicazioni;
	
	private List<StatisticaDettaglioDto> listStatisticaDettaglio;
	private String nessunRisultato;
	private String actionName;
	private String tableStyle;
	private String idCliente;
	@Getter(AccessLevel.NONE)
	private String nomeCliente;
	private String idtn;
	private String StatisticaDettaglioTab_ev;
	private Timestamp dataStorico;
	private Boolean hasBolleQuotidianiPeriodiciDivise;
	
	private Integer flgOperazione;
	private Integer hidden_flgOperazione;
	
	private String titoloInsertBarcode;
	private String sottotitoloInsertBarcode;
	private String prezzoInsertBarcode;
	private String codPubblicazioneInsertBarcode;
	private String codBarreInsertBarcode;
	
	
	
	
	public PubblicazioniAction() {
		this(null,null,null);
	}
	
	@Autowired
	public PubblicazioniAction(PubblicazioniService pubblicazioniService, StatisticheService statisticheService, ValidationCodiceBarre validationCodiceBarre) {
		this.pubblicazioniService = pubblicazioniService;
		this.statisticheService = statisticheService;
		this.validationCodiceBarre = validationCodiceBarre;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		dataStorico = getAuthUser().getDataStorico();
	}
	
	@Override
	public void validate() {
		
		if(flgOperazione!=null &&  flgOperazione==1){
			if ((titolo == null || titolo.equals("")) && (sottotitolo == null || sottotitolo.equals("")) && (argomento == null || argomento.equals(""))
					&& (periodicita == null || periodicita.equals("")) && (prezzo == null || prezzo.equals("")) 
					&& (codPubblicazione == null || codPubblicazione.equals("")) && (codBarre == null || codBarre.equals(""))) {
				addActionError(getText("error.criterio.ricerca"));
			}
			if (titolo != null && !titolo.equals("") && (sottotitolo == null || sottotitolo.equals(""))) {
				if (titolo.trim().length() < 1) {
					addActionError(getText("error.ricerca.per.titolo.almeno.un.carattere"));
				}
			}
			if (sottotitolo != null && !sottotitolo.equals("") && (titolo == null || titolo.equals(""))) {
				if (sottotitolo.trim().length() < 1) {
					addActionError(getText("error.ricerca.per.titolo.almeno.un.carattere"));
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
			
		}else if(flgOperazione!=null && flgOperazione==0){
			
 			if ((titoloInsertBarcode == null || titoloInsertBarcode.equals("")) && (prezzoInsertBarcode == null || prezzoInsertBarcode.equals("")) 
					&& (codPubblicazioneInsertBarcode == null || codPubblicazioneInsertBarcode.equals("")) && (codBarreInsertBarcode == null || codBarreInsertBarcode.equals(""))) {
				addActionError(getText("error.criterio.ricerca"));
			}
			if (titoloInsertBarcode != null && !titoloInsertBarcode.equals("") && (sottotitoloInsertBarcode == null || sottotitoloInsertBarcode.equals(""))) {
				if (titoloInsertBarcode.trim().length() < 1) {
					addActionError(getText("error.ricerca.per.titolo.almeno.un.carattere"));
				}
			}
			if (sottotitoloInsertBarcode != null && !sottotitoloInsertBarcode.equals("") && (titoloInsertBarcode == null || titoloInsertBarcode.equals(""))) {
				if (sottotitoloInsertBarcode.trim().length() < 1) {
					addActionError(getText("error.ricerca.per.titolo.almeno.un.carattere"));
				}
			}
			if (prezzoInsertBarcode != null && !prezzoInsertBarcode.equals("")) {
				int commaPos = prezzoInsertBarcode.lastIndexOf(",");
				int dotPos = prezzoInsertBarcode.lastIndexOf(".");
				if (dotPos > commaPos) {
					prezzoInsertBarcode = prezzoInsertBarcode.replaceAll(",", "");
				} else if (commaPos > dotPos) {
					prezzoInsertBarcode = prezzoInsertBarcode.replaceAll("\\.", "");
					if (prezzoInsertBarcode.contains(",")) {
						prezzoInsertBarcode = prezzoInsertBarcode.replace(',', '.');
					}
				}
				if (!NumberUtils.isNumber(prezzoInsertBarcode)) {
					addFieldError("prezzo", getText("xwork.default.invalid.fieldvalue"));
				}
				if ((titoloInsertBarcode == null || titoloInsertBarcode.equals("")) && (sottotitoloInsertBarcode == null || sottotitoloInsertBarcode.equals("")) 
						&& (codPubblicazioneInsertBarcode == null || codPubblicazioneInsertBarcode.equals("")) 
						&& (codBarreInsertBarcode == null || codBarreInsertBarcode.equals(""))) {
					addActionError(getText("error.criterio.ricerca.prezzo"));
				}
			}
			
			if (codPubblicazioneInsertBarcode != null && !codPubblicazioneInsertBarcode.equals("")) {
				if (!NumberUtils.isNumber(codPubblicazioneInsertBarcode)) {
					addFieldError("codPubblicazione", getText("xwork.default.invalid.fieldvalue"));
				}
				
			}
			if (codBarreInsertBarcode != null && !codBarreInsertBarcode.equals("")) {
				if (!NumberUtils.isNumber(codBarreInsertBarcode)) {
					addFieldError("codBarre", getText("xwork.default.invalid.fieldvalue"));
				}
			}
		}else{
			//SAVE
			
		}
		
	}
	
	@BreadCrumb(value="%{crumbName}")
	@SkipValidation
	public String showFilter() {
		tableTitle = getText("igeriv.ricerca.pubblicazioni");
		this.flgOperazione = 1;
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameInsRif}")
	@SkipValidation
	public String showFilterRifornimenti() throws Exception {
		tableTitle = getText("igeriv.bolla.inserisci.rifornimenti");
		return SUCCESS;
	}
	
	
	
	@BreadCrumb("%{crumbNameInsVaria}")
	@SkipValidation
	public String showFilterVariazioni() throws Exception {
		tableTitle = getText("igeriv.bolla.inserisci.variazioni");
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNamePubbSta}")
	@SkipValidation
	public String showFilterStatistica() throws Exception {
		tableTitle = getText("igeriv.statistica.pubblicazioni");
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNamePrenoCli}")
	@SkipValidation
	public String showFilterPrenotazioneClienti() throws Exception {
		tableTitle = getText("igeriv.prenotazioni.clienti");
		return SUCCESS;
	}
	
	@BreadCrumb(value="%{crumbName}")
	public String showPubblicazioni() {
		tableTitle = getText("igeriv.ricerca.pubblicazioni");
		Integer codPubblicazioneInt = (codPubblicazione != null && !codPubblicazione.equals("")) ? Integer.parseInt(codPubblicazione) : null;
		BigDecimal prezzoBd = (prezzo != null && !prezzo.equals("")) ? new BigDecimal(prezzo) : null;
		Integer[] codDl = (getAuthUser().isMultiDl() || getAuthUser().getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_CLIENTE_EDICOLA)) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
		
		listPubblicazioni = pubblicazioniService.getCopertineByDL(true,codDl[0], titolo, sottotitolo, argomento, periodicita, prezzoBd, codPubblicazioneInt, codBarre);
		
		requestMap.put("listPubblicazioni", listPubblicazioni);
		if (listPubblicazioni == null || listPubblicazioni.isEmpty()) {
			nessunRisultato = getText("igeriv.nessun.risultato");
		} else {
			tableHeight = getTabHeight(listPubblicazioni);
		}
		//RICERCA PUBBLICAZIONE
		this.flgOperazione = 1;
		
		return SUCCESS;
	}
	
	@BreadCrumb(value="%{crumbName}")
	public String showPubblicazioniInserimentoBarcode() {
		tableTitle = getText("igeriv.ricerca.pubblicazioni");
		Integer codPubblicazioneInt = (codPubblicazioneInsertBarcode != null && !codPubblicazioneInsertBarcode.equals("")) ? Integer.parseInt(codPubblicazioneInsertBarcode) : null;
		BigDecimal prezzoBd = (prezzoInsertBarcode != null && !prezzoInsertBarcode.equals("")) ? new BigDecimal(prezzoInsertBarcode) : null;
		Integer[] codDl = (getAuthUser().isMultiDl() || getAuthUser().getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_CLIENTE_EDICOLA)) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
		
		listPubblicazioni = pubblicazioniService.getCopertineByDL(true,codDl[0], titoloInsertBarcode, sottotitoloInsertBarcode, argomento, periodicita, prezzoBd, codPubblicazioneInt, codBarreInsertBarcode);
		
		requestMap.put("listPubblicazioni", listPubblicazioni);
		if (listPubblicazioni == null || listPubblicazioni.isEmpty()) {
			nessunRisultato = getText("igeriv.nessun.risultato");
		} else {
			tableHeight = getTabHeight(listPubblicazioni);
		}
		//INSERISCI BARCODE
		this.flgOperazione = 0;
		
		return SUCCESS;
	}
	

	
	@SkipValidation
	public String showNumeriPubblicazioni() {
		Integer codPubblicazioneInt = (codPubblicazione != null && !codPubblicazione.equals("")) ? Integer.parseInt(codPubblicazione) : null;
		Integer[] codDl = (getAuthUser().isMultiDl() || getAuthUser().getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_CLIENTE_EDICOLA)) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
		
		listPubblicazioni = pubblicazioniService.getCopertineByDL(false,codDl[0], null, null, null, null, null, codPubblicazioneInt, null);
		
		requestMap.put("listPubblicazioni", listPubblicazioni);
		if (listPubblicazioni.size() > 0) {
			String titolo = ((PubblicazioneDto) listPubblicazioni.get(0)).getTitolo();
			tableTitle = getText("igeriv.pubblicazione") + ": " + titolo;
		}
		return IGerivConstants.ACTION_NUMERI_PRECEDENTI;
	}
	
	
	@SkipValidation
	public String showNumeriPubblicazioniInserimentoBarcode() {
		Integer codPubblicazioneInt = (codPubblicazione != null && !codPubblicazione.equals("")) ? Integer.parseInt(codPubblicazione) : null;
		Integer[] codDl = (getAuthUser().isMultiDl() || getAuthUser().getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_CLIENTE_EDICOLA)) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
		
		listPubblicazioni = pubblicazioniService.getCopertineByDL(false,codDl[0], null, null, null, null, null, codPubblicazioneInt, null);
		
		requestMap.put("listPubblicazioni", listPubblicazioni);
		if (listPubblicazioni.size() > 0) {
			String titolo = ((PubblicazioneDto) listPubblicazioni.get(0)).getTitolo();
			tableTitle = getText("igeriv.pubblicazione") + ": " + titolo;
		}
		return IGerivConstants.ACTION_NUMERI_PRECEDENTI_INSERT_BARCODE;
	}
	
	@SkipValidation
	public String saveListNewBarcode(){
		try{
			Integer codDl = null;
			LinkedHashMap<String,StoricoCopertineVo> listStoricoCopertine = new LinkedHashMap<String,StoricoCopertineVo>();
			
			 
			 Iterator it = getNuovoBarcodeMap().entrySet().iterator();
			 while (it.hasNext()) {
			    Map.Entry entry = (Map.Entry)it.next();
			    Integer codicePubblicazione = null;
			    String numeroCopertina = null;
			    String dataUscita = null;
			    Integer idtn = null;
			    
			    String[] split = ((String) entry.getKey()).split(",");
				for (String pk : split) {
					String[] pkSplit = pk.split("\\|");
					codDl 				= new Integer(pkSplit[0]); 
					codicePubblicazione = new Integer(pkSplit[1]);
					numeroCopertina 	= new String(pkSplit[2]);
					dataUscita 			= new String(pkSplit[3]);
					idtn 				= new Integer(pkSplit[4]);
				}
			    if(entry.getValue()!=null && !entry.getValue().equals("")){
			    	String barcodenew = (String) entry.getValue();
			    	
			    	//VALIDAZIONE CODICE A BARRE NUOVO
			    	if(barcodenew.length()==18){
			    		String msgError = validationCodiceBarre.validateCodiceBarre(barcodenew.substring(0,13), barcodenew.substring(13, 18));
				    	if(msgError!=null){
				    		requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + msgError + IGerivConstants.END_EXCEPTION_TAG);
							throw new IGerivRuntimeException();
				    	}
			    	}else{
			    		String msgErrorLenght = "Il codice a barre [ "+barcodenew+" ] risulta di lunghezza impropria \n Aggiornamento non permesso.";
			    		requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + msgErrorLenght + IGerivConstants.END_EXCEPTION_TAG);
						throw new IGerivRuntimeException();
			    	}
			    	//VERIFICA ESISTENZA STORICOCOPERTINE CODICE A BARRE NUOVO
			    	StoricoCopertineVo cop_daBarcode = pubblicazioniService.getStoricoCopertinaByBarcode(codDl, barcodenew);
			    	if(cop_daBarcode!=null){
			    		String msg = "Il Codice a Barre [ "+barcodenew+" ] risulta associato al Codice Pubblicazione : "+cop_daBarcode.getCodicePubblicazione()
			    						+"\n Numero : "+cop_daBarcode.getNumeroCopertina()
			    						+"\n Aggiornamento non permesso";
						requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + msg + IGerivConstants.END_EXCEPTION_TAG);
						throw new IGerivRuntimeException();
			    	}else{
			    		//SE IL CODICE NON ESISTE CONTINUO CON IL PROCESSO DI ASSOCIAZIONE
			    		StoricoCopertineVo cop_daIdtn = pubblicazioniService.getStoricoCopertinaByPk(codDl, idtn);
			    		listStoricoCopertine.put(barcodenew,cop_daIdtn);
			    	}
			    }
			    
			 }
			 
			 //PROCESSO DI ASSOCIAZIONE CODICE A BARRE NUOVO
			 if(listStoricoCopertine!=null && !listStoricoCopertine.isEmpty()){
				 Iterator iterListStorico = listStoricoCopertine.entrySet().iterator();
				 while (iterListStorico.hasNext()) {
					 Map.Entry me = (Map.Entry)iterListStorico.next();
					 
					 String barcodeNuovo = (String) me.getKey();
					 StoricoCopertineVo objStoricoCop = (StoricoCopertineVo) me.getValue();
					 //Vecchio barcode
					 objStoricoCop.setCodiceBarrePrecedente((objStoricoCop.getCodiceBarre()!=null)?objStoricoCop.getCodiceBarre():null);
					 //Nuovo barcode
					 objStoricoCop.setCodiceBarre(barcodeNuovo);
					 //Data di modifica del barcode
					 Timestamp sysdate = pubblicazioniService.getSysdate();
					 objStoricoCop.setDataCorrezioneBarcode(sysdate);
					 //Codice DL della modifica
					 objStoricoCop.setCodEdicolaCorrezioneBarcode(codDl);
					 
					 pubblicazioniService.saveBaseVo(objStoricoCop);
					
				}
			 }
			 
			
		} catch (Throwable e) {
			log.error("Errore -  saveListNewBarcode ", e);
			throw e;
		}
		
		return SUCCESS;
	}
	
	
	
	public String showPubblicazioniResa() {
		 
		tableTitle = getText("igeriv.ricerca.pubblicazioni");
		Integer codPubblicazioneInt = (codPubblicazione != null && !codPubblicazione.equals("")) ? Integer.parseInt(codPubblicazione) : null;
		BigDecimal prezzoBd = (prezzo != null && !prezzo.equals("")) ? new BigDecimal(prezzo) : null;
		Integer[] codDl = (getAuthUser().isMultiDl() || (getAuthUser().isDlInforiv() && getAuthUser().getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_CLIENTE_EDICOLA))) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
		listPubblicazioni = pubblicazioniService.getCopertine(true, false, false, getAuthUser().getCodEdicolaMaster(), codDl, getAuthUser().getArrId(), titolo, sottotitolo, argomento, periodicita, prezzoBd, codPubblicazioneInt, codBarre, false, dataStorico, getAuthUser().getGruppoSconto(), false, getAuthUser().getCodFiegDl(),null);
		
		final List<Integer> listCodFiegDl = Arrays.asList(getAuthUser().getArrCodFiegDl());
		CollectionUtils.forAllDo(listPubblicazioni, new Closure() {
			public void execute(final Object obj) {
				 PubblicazioneDto dto = (PubblicazioneDto) obj;
				 dto.setCrivw(getAuthUser().getArrId()[listCodFiegDl.indexOf(dto.getCoddl())]);
			}
		});
		requestMap.put("listPubblicazioni", listPubblicazioni);
		if (listPubblicazioni == null || listPubblicazioni.isEmpty()) {
			nessunRisultato = getText("igeriv.nessun.risultato");
		} else {
			tableHeight = getTabHeight(listPubblicazioni);
		}
		 
		return SUCCESS;
	}
	
	public String showPubblicazioniBarraSceltaRapida() {
		tableTitle = getText("igeriv.ricerca.pubblicazioni");
		Integer codPubblicazioneInt = (codPubblicazione != null && !codPubblicazione.equals("")) ? Integer.parseInt(codPubblicazione) : null;
		BigDecimal prezzoBd = (prezzo != null && !prezzo.equals("")) ? new BigDecimal(prezzo) : null;
		Integer[] codDl = (getAuthUser().isMultiDl() || (getAuthUser().isDlInforiv() && getAuthUser().getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_CLIENTE_EDICOLA))) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
		listPubblicazioni = pubblicazioniService.getCopertine(true, false, false, getAuthUser().getCodEdicolaMaster(), codDl, getAuthUser().getArrId(), titolo, sottotitolo, argomento, periodicita, prezzoBd, codPubblicazioneInt, codBarre, false, dataStorico, getAuthUser().getGruppoSconto(), false, getAuthUser().getCodFiegDl(),null);
		requestMap.put("listPubblicazioni", listPubblicazioni);
		if (listPubblicazioni == null || listPubblicazioni.isEmpty()) {
			nessunRisultato = getText("igeriv.nessun.risultato");
		} else {
			tableHeight = getTabHeight(listPubblicazioni);
		}
		return SUCCESS;
	}
	
//	@BreadCrumb("%{crumbNameInsRif}")
//	public String showPubblicazioniRifornimenti() {
//		showPubblicazioni();
//		tableTitle = getText("igeriv.bolla.inserisci.rifornimenti");
//		return SUCCESS;
//	}
//	
//	@BreadCrumb("%{crumbNameInsVaria}")
//	public String showPubblicazioniVariazioni() {
//		showPubblicazioni();
//		tableTitle = getText("igeriv.bolla.inserisci.variazioni");
//		return SUCCESS;
//	}
//	
//	@BreadCrumb("%{crumbNamePubbSta}")
//	public String showPubblicazioniStatistica() {
//		showPubblicazioni();
//		tableTitle = getText("igeriv.statistica.pubblicazioni");
//		return SUCCESS;
//	}
//	
//	@BreadCrumb("%{crumbNamePrenoCli}")
//	public String showPubblicazioniPrenotazioneClienti() {
//		showPubblicazioni();
//		tableTitle = getText("igeriv.prenotazioni.clienti");
//		return SUCCESS;
//	}
//	
	
//	
//	@SkipValidation
//	public String showNumeriPubblicazioniStatistica() {
//		Integer codPubblicazioneInt = (codPubblicazione != null && !codPubblicazione.equals("") && NumberUtils.isNumber(codPubblicazione)) ? Integer.parseInt(codPubblicazione) : null;
//		if (codPubblicazioneInt != null) {
//			listPubblicazioni = pubblicazioniService.getCopertine(false, true, false, getAuthUser().getCodEdicolaMaster(), getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), null, null, null, periodicita, null, codPubblicazioneInt, null, false, dataStorico, getAuthUser().getGruppoSconto(), false, getAuthUser().getCodFiegDl());
//			Integer idtn = Strings.isNullOrEmpty(this.idtn) ? null : new Integer(this.idtn);
//			if (idtn != null) {
//				PubblicazioneDto copertina = selectUnique(listPubblicazioni, having(on(PubblicazioneDto.class).getIdtn(), equalTo(idtn)));
//				listPubblicazioni.clear();
//				listPubblicazioni.add(copertina);
//			}
//			requestMap.put("listPubblicazioni", listPubblicazioni); 
//			titoloDet = listPubblicazioni.size() > 0 ? ((PubblicazioneDto) listPubblicazioni.get(0)).getTitolo() : "";
//		}
//		tableTitle = getText("igeriv.pubblicazione") + ": " + titoloDet;
//		return IGerivConstants.ACTION_NUMERI_PRECEDENTI;
//	}
//	
//	@SkipValidation
//	public String showStatisticaDettaglioFornito() {
//		Integer idtn = (this.idtn != null && !this.idtn.equals("")) ? Integer.parseInt(this.idtn) : null;
//		listStatisticaDettaglio = statisticheService.getStatisticaDettaglioFornito(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), idtn,dataStorico);
//		if (listStatisticaDettaglio == null || listStatisticaDettaglio.isEmpty() || listStatisticaDettaglio.get(0).getCopie() == null || listStatisticaDettaglio.get(0).getCopie().equals(0)) {
//			listStatisticaDettaglio = statisticheService.getStatisticaDettaglioFornitoRifornimenti(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), idtn);
//			if (listStatisticaDettaglio != null && !listStatisticaDettaglio.isEmpty()) {
//				forEach(listStatisticaDettaglio).setTipo(IGerivConstants.TIPO_STATISTICA_DETTAGLIO_RIFORNIMENTI);
//			}
//		} else if (listStatisticaDettaglio != null && !listStatisticaDettaglio.isEmpty()) {
//			forEach(listStatisticaDettaglio).setTipo(IGerivConstants.TIPO_STATISTICA_DETTAGLIO_FORNITO);
//		}
//		requestMap.put("listStatisticaDettaglio", listStatisticaDettaglio); 
//		tableTitle = getText("igeriv.dettaglio.fornito") + "<br>" + getText("igeriv.pubblicazione") + ": " + titolo + " " + getText("igeriv.report.numero") + ": " + numero;
//		if (StatisticaDettaglioTab_ev != null && (StatisticaDettaglioTab_ev.toUpperCase().equals("PDF") || StatisticaDettaglioTab_ev.toUpperCase().equals("XLS"))) {
//			tableTitle = StringUtility.checkSpecialChars(tableTitle);
//		}
//		return "statisticaDettaglioFornito";
//	}
//	
//	@SkipValidation
//	public String showStatisticaDettaglioReso() {
//		Integer idtn = (this.idtn != null && !this.idtn.equals("")) ? Integer.parseInt(this.idtn) : null;
//		listStatisticaDettaglio = statisticheService.getStatisticaDettaglioReso(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), idtn);
//		requestMap.put("listStatisticaDettaglio", listStatisticaDettaglio); 
//		tableTitle = getText("igeriv.dettaglio.reso") + "<br>" + getText("igeriv.pubblicazione") + ": " + titolo + " " + getText("igeriv.report.numero") + ": " + numero;
//		if (StatisticaDettaglioTab_ev != null && (StatisticaDettaglioTab_ev.toUpperCase().equals("PDF") || StatisticaDettaglioTab_ev.toUpperCase().equals("XLS"))) {
//			tableTitle = StringUtility.checkSpecialChars(tableTitle);
//		}
//		return "statisticaDettaglioReso";
//	}
//	
//	@SkipValidation
//	public String showStatisticaDettaglioResoRiscontrato() {
//		Integer idtn = (this.idtn != null && !this.idtn.equals("")) ? Integer.parseInt(this.idtn) : null;
//		listStatisticaDettaglio = statisticheService.getStatisticaDettaglioResoRiscontrato(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), idtn);
//		requestMap.put("listStatisticaDettaglio", listStatisticaDettaglio); 
//		tableTitle = getText("igeriv.dettaglio.reso.riscontrato") + "<br>" + getText("igeriv.pubblicazione") + ": " + titolo + " " + getText("igeriv.report.numero") + ": " + numero;
//		if (StatisticaDettaglioTab_ev != null && (StatisticaDettaglioTab_ev.toUpperCase().equals("PDF") || StatisticaDettaglioTab_ev.toUpperCase().equals("XLS"))) {
//			tableTitle = StringUtility.checkSpecialChars(tableTitle);
//		}
//		return "statisticaDettaglioReso";
//	}
//	
//	@SkipValidation
//	public String showStatisticaDettaglioVenduto() {
//		Integer idtn = (this.idtn != null && !this.idtn.equals("")) ? Integer.parseInt(this.idtn) : null;
//		listStatisticaDettaglio = statisticheService.getStatisticaDettaglioVenduto(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), idtn);
//		requestMap.put("listStatisticaDettaglio", listStatisticaDettaglio); 
//		tableTitle = getText("igeriv.dettaglio.venduto") + "<br>" + getText("igeriv.pubblicazione") + ": " + titolo + " " + getText("igeriv.report.numero") + ": " + numero;
//		if (StatisticaDettaglioTab_ev != null && (StatisticaDettaglioTab_ev.toUpperCase().equals("PDF") || StatisticaDettaglioTab_ev.toUpperCase().equals("XLS"))) {
//			tableTitle = StringUtility.checkSpecialChars(tableTitle);
//		}
//		return "statisticaDettaglioVenduto";
//	}
//	
//	@BreadCrumb("%{crumbNamePubbCD}")
//	@SkipValidation
//	public String reportContoDepositoFilter() {
//		tableTitle = getText("igeriv.pubblicazioni.conto.deposito");
//		return IGerivConstants.ACTION_REPORT_CONTO_DEPOSITO;
//	}
//	
//	@BreadCrumb("%{crumbNamePubbCD}")
//	@SkipValidation
//	public String reportContoDeposito() {
//		actionName = "infoPubblicazioni_reportContoDeposito.action";
//		List<ContoDepositoDto> listContoDeposito = pubblicazioniService.getPubblicazioniContoDeposito(getAuthUser().getArrCodFiegDl(), 
//				getAuthUser().getArrId(), getAuthUser().getGruppoSconto(), titolo, codBarre);
//		requestMap.put("listContoDeposito", listContoDeposito);
//		tableTitle = getText("igeriv.pubblicazioni.conto.deposito");
//		return IGerivConstants.ACTION_REPORT_CONTO_DEPOSITO;
//	}
	
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
			String act = getActionName();
			if (getActionName().contains("variazioni_")) {
				title = getText("igeriv.bolla.variazioni");
			} else if (getActionName().contains("viewVariazioni_")) {
				title = getText("igeriv.menu.61");
			} else if (getActionName().contains("statisticaPubblicazioni_")) {
				title = getText("igeriv.statistica.pubblicazioni");
			} else if (getActionName().contains("ContoDeposito")) {
				title = getText("igeriv.pubblicazioni.conto.deposito");
			}else if(getActionName().contains("rifornimenti_")){
				title = getText("igeriv.bolla.rifornimenti");   //Modifica 03/07/2014
			}
		}
		return super.getTitle() + title;
	}

	public String getNomeCliente() {
		return (nomeCliente != null) ? nomeCliente.toUpperCase() : nomeCliente;
	}

}
