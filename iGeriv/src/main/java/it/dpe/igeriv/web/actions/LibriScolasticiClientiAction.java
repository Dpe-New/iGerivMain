package it.dpe.igeriv.web.actions;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.apache.struts2.util.ServletContextAware;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.stereotype.Component;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.bo.menu.MenuService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.bo.rifornimenti.RifornimentiService;
import it.dpe.igeriv.bo.scolastica.ScolasticaService;
import it.dpe.igeriv.dto.ClienteEdicolaDto;
import it.dpe.igeriv.dto.DettaglioRicercaLibriDto;
import it.dpe.igeriv.dto.OrdineLibriDto;
import it.dpe.igeriv.dto.ParametriRicercaLibriDto;
import it.dpe.igeriv.dto.RisultatiRicercaLibriDto;
import it.dpe.igeriv.dto.RisultatoRicercaLibriDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.OrdineLibriDettVo;
import it.dpe.igeriv.vo.OrdineLibriDettVo.ETrack;
import it.dpe.igeriv.vo.OrdineLibriVo;
import it.dpe.igeriv.vo.ProvinciaVo;
import it.dpe.igeriv.vo.TipoLocalitaVo;
import it.dpe.igeriv.web.rest.dto.AnagraficaDto;
import it.dpe.igeriv.web.rest.dto.ClespDettaglioLibroDto;
import it.dpe.igeriv.web.rest.dto.ClespDettaglioLibroOrdineDto;
import it.dpe.igeriv.web.rest.dto.ClespDettaglioTrakingOrdineDto;
import it.dpe.igeriv.web.rest.dto.ClespNuovoConsumatoreDto;
import it.dpe.igeriv.web.rest.dto.ClespNuovoOrdineDto;
import it.dpe.igeriv.web.rest.dto.ClespRicercaCatalogoLibriDto;
import it.dpe.igeriv.web.rest.dto.ClespRicercaTrackingOrdineDto;
import it.dpe.igeriv.web.rest.dto.ClespRisultatoNuovoConsumatoreDto;
import it.dpe.igeriv.web.rest.dto.ClespRisultatoNuovoOrdineDto;
import it.dpe.igeriv.web.rest.dto.ClespRisultatoRicercaCatalogoLibriDto;
import it.dpe.igeriv.web.rest.dto.ClespRisultatoTrackingOrdineDto;
import it.dpe.igeriv.web.rest.dto.ConfermaOrdine;
import it.dpe.igeriv.web.rest.dto.ContattiDto;
import it.dpe.igeriv.web.rest.dto.ElencoLibriDto;
import it.dpe.igeriv.web.rest.dto.InformazioniDto;
import it.dpe.igeriv.web.rest.dto.OrdineDto;
import it.dpe.igeriv.web.rest.dto.ParametriDto;
import it.dpe.igeriv.web.rest.dto.RicercaLibriTestoDto;
import it.dpe.igeriv.web.rest.dto.TrackingDto;
import it.dpe.igeriv.web.rest.dto.TrackingElencoLibriDto;
import it.dpe.igeriv.web.rest.service.ClespConsumatoreService;
import it.dpe.igeriv.web.rest.service.ClespOrdineService;
import it.dpe.igeriv.web.rest.service.ClespRicercaCatalogoLibriService;
import it.dpe.igeriv.web.rest.service.ClespTrackingOrdineService;
import it.dpe.igeriv.web.rest.service.RestService;
import it.dpe.service.mail.MailingListService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per la gestione dei clienti della rivendita.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("libriScolasticiClientiAction")
@SuppressWarnings({"unchecked", "rawtypes", "unused", "deprecation"}) 
public class LibriScolasticiClientiAction extends RestrictedAccessBaseAction implements State, RequestAware,ServletContextAware {
	private static final long serialVersionUID = 1L;
	private final ClientiService<ClienteEdicolaVo> clientiScolasticaService;
	private final ScolasticaService scolasticaService;
	private final RifornimentiService rifornimentiService;
	private final PubblicazioniService pubblicazioniService;
	private final ClespConsumatoreService clespConsumatoreService;
	private final ClespRicercaCatalogoLibriService clespRicercaCatalogoLibriService;
	private final ClespOrdineService clespOrdineService;
	private final ClespTrackingOrdineService clespTrackingOrdineService;
	
	
	private final MenuService menuService;
	private final PasswordEncoder passwordEncoder;
	private final ReflectionSaltSource saltSource;
	private final MailingListService mailingListService;
	private final AgenzieService agenzieService;
	
	private final String igerivUrl;
	private final String crumbName = getText("igeriv.inserisci.aggiorna.scolastica.clienti");
	private final String crumbNameAggCli = getText("igeriv.inserisci.aggiorna.scolastica.clienti");
	private final String crumbNameInsPreCli = getText("igeriv.inserisci.prenotazioni.clienti");
	private final String crumbNameViewPreCli = getText("igeriv.visualizza.clienti.con.prenotazioni");
	private final String crumbNameViewCliConPre = getText("igeriv.visualizza.clienti.con.prenotazioni");
	private final String crumbNameEvPreCli = getText("igeriv.evasione.prenotazioni.clienti");
	private final String crumbNameConfermaEvaCli = getText("igeriv.conferma.evasione.prenotazioni.clienti");
	private final String crumbNamePreCliEvase = getText("igeriv.prenotazioni.clienti.evase");
	private final String crumbNameRepEC = getText("igeriv.report.estratto.conto.clienti");
	
	private final String crumbNameTracking = getText("igeriv.tracking.scolastica.clienti");
	
	
	
	private String nome;
	private String cognome;
	private String numeroOrdine;
	private String codiceFiscale;
	private String piva;
	private List<ClienteEdicolaDto> clienti;
	private String tableTitle;
	private String actionName;
	private Integer tableHeight;
	@Getter(AccessLevel.NONE)
	private List<TipoLocalitaVo> tipiLocalita;
	private List<ProvinciaVo> province;
	private ClienteEdicolaVo clienteGestioneScolastica;
	private ClienteEdicolaVo clienteScolastica;
	
	private String idCliente;
	private String term;
	private String clienteGestioneScolastica_inviaEmail;
	private String titolo;
	private String nomeCliente;
	private String risposteClientiSelectKeys = "";
	private String risposteClientiSelectValues = "";
	private String evaso;
	private String ultimaRisposta;
	private String messLibero;
	private String pk;
	private String qtaDaEvadere;
	private String filterTitle;
	private Integer tipoRicerca;
	private String numero;
	private String sottotitolo;
	private String argomento;
	private String periodicita;
	private String prezzo;
	private String codPubblicazione;
	private String codBarre;
	private Long idConto;
	private String spunte;
	private String conPrenotazioniFisse;
	private String dataDa;
	private String dataA;
	private String dataCompEC;
	
	//Testo ricerca scolastica	
	private String testoRicerca;
	private String tipoRicercaRadio;
	private RestService<RisultatoRicercaLibriDto, RicercaLibriTestoDto> libriScolasticiRestService;
	private RestService<RisultatoRicercaLibriDto, RicercaLibriTestoDto> libriScolasticiDettaglioRestService;
	private RestService<TrackingDto, RicercaLibriTestoDto> libriScolasticiTrackingOrdiniRestService;
	private String sku;
	private String seq;
	private String seqOrdine;
	private String guid;
	private Long idNumeroOrdine;
	private DettaglioRicercaLibriDto dettaglioLibroPopup;
	private Long codCliente;
	private Long numOrdine;
	private Map<String,String> resultMap;
	private String operation;
	private boolean addCart = false;
	private String numOrdineTxt;
	
	private String sceltaCopertina;
	private String copertina_primariga;
	private String copertina_secondariga;
	private String copertina_terzariga;
	private String sceltaLogo;
	private String costoCopertinatura;
	
	@Value("${path.files.pdf}")
	private String pathFilesPdf;
	
	private Map<String, Object> evasioneJsonResult;
	private List<Map<String,String>> resultListInfoCopertina;
	
	public LibriScolasticiClientiAction() {
		this(null, null, null, null, null, null, null, null, null,null,null,null,null,null,null,null,null);
	}
	
	@Autowired
	public LibriScolasticiClientiAction(
		ClientiService<ClienteEdicolaVo> clientiService,
		RifornimentiService rifornimentiService,
		PubblicazioniService pubblicazioniService,
		MenuService menuService,
		PasswordEncoder passwordEncoder,
		ReflectionSaltSource saltSource,
		MailingListService mailingListService,
		@Value("${igeriv.url}") String igerivUrl,
		ScolasticaService scolasticaService,
		ClespConsumatoreService clespConsumatoreService,
		ClespRicercaCatalogoLibriService clespRicercaCatalogoLibriService,
		ClespOrdineService clespOrdineService,
		ClespTrackingOrdineService clespTrackingOrdineService,
		AgenzieService agenzieService,
		@Qualifier("LibriScolasticiRestService") RestService<RisultatoRicercaLibriDto, RicercaLibriTestoDto> libriScolasticiRestService,
		@Qualifier("LibriScolasticiDettaglioRestService") RestService<RisultatoRicercaLibriDto, RicercaLibriTestoDto> libriScolasticiDettaglioRestService,
		@Qualifier("LibriScolasticiTrackingOrdiniRestService") RestService<TrackingDto, RicercaLibriTestoDto> libriScolasticiTrackingOrdiniRestService
		) {
		this.clientiScolasticaService = clientiService;
		this.rifornimentiService = rifornimentiService;
		this.pubblicazioniService = pubblicazioniService;
		this.menuService = menuService;
		this.passwordEncoder = passwordEncoder;
		this.saltSource = saltSource;
		this.mailingListService = mailingListService;
		this.igerivUrl = igerivUrl;
		this.scolasticaService = scolasticaService;
		this.libriScolasticiRestService = libriScolasticiRestService;
		this.libriScolasticiDettaglioRestService = libriScolasticiDettaglioRestService;
		this.libriScolasticiTrackingOrdiniRestService = libriScolasticiTrackingOrdiniRestService;
		this.clespConsumatoreService = clespConsumatoreService;
		this.clespRicercaCatalogoLibriService = clespRicercaCatalogoLibriService;
		this.clespOrdineService = clespOrdineService;
		this.clespTrackingOrdineService = clespTrackingOrdineService;
		this.agenzieService = agenzieService;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
	
	@Override
	public void validate() {
		
		if (getActionName() != null && getActionName().contains("saveClienteScolastica"))  {
			if (clienteGestioneScolastica != null) {
				if (clienteGestioneScolastica.getNome() == null || clienteGestioneScolastica.getNome().equals("")) {
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("dpe.contact.form.name")) + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				}
				if (clienteGestioneScolastica.getCognome() == null || clienteGestioneScolastica.getCognome().equals("")) {
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("dpe.cognome")) + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				}
				if (clienteGestioneScolastica.getDtSospensionePrenotazioniDa() == null && clienteGestioneScolastica.getDtSospensionePrenotazioniA() != null) {
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("error.date.sospensione.da.prenotazione") + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				} else if (clienteGestioneScolastica.getDtSospensionePrenotazioniDa() != null && clienteGestioneScolastica.getDtSospensionePrenotazioniA() == null) {
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("error.date.sospensione.a.prenotazione") + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				} else if (clienteGestioneScolastica.getDtSospensionePrenotazioniDa() != null && clienteGestioneScolastica.getDtSospensionePrenotazioniA() != null) {
					if (clienteGestioneScolastica.getDtSospensionePrenotazioniDa().after(clienteGestioneScolastica.getDtSospensionePrenotazioniA())) {
						requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("error.date.sospensione.prenotazione.date.incongruenti") + IGerivConstants.END_EXCEPTION_TAG);
						throw new IGerivRuntimeException();
					}
				}
				if ((clienteGestioneScolastica_inviaEmail != null && clienteGestioneScolastica_inviaEmail.equals("true")) && (clienteGestioneScolastica.getEmail() == null || clienteGestioneScolastica.getEmail().trim().equals(""))) {
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("cliente.email")) + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				}
				if (clienteGestioneScolastica.getEmail() != null) {
					ClienteEdicolaVo cienteEdicolaByEmail = clientiScolasticaService.getCienteEdicolaByEmail(clienteGestioneScolastica.getEmail());
					if (cienteEdicolaByEmail != null && !cienteEdicolaByEmail.getCodCliente().equals(clienteGestioneScolastica.getCodCliente())) {
						requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("error.cliente.stessa.mail") + IGerivConstants.END_EXCEPTION_TAG);
						throw new IGerivRuntimeException();
					}
				}
			}
		} else if (getActionName() != null && getActionName().contains("viewEvasionePrenotazioniClientiEdicola") && (!Strings.isNullOrEmpty(dataDa) || !Strings.isNullOrEmpty(dataA)))  {
			try {
				if (!Strings.isNullOrEmpty(dataDa)) {
					DateUtilities.parseDate(dataDa, DateUtilities.FORMATO_DATA_SLASH);
				}
			} catch (ParseException e) {
				addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataDa));
				return;
			}
			try {
				if (!Strings.isNullOrEmpty(dataA)) {
					DateUtilities.parseDate(dataA, DateUtilities.FORMATO_DATA_SLASH);
				}
			} catch (ParseException e) {
				addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataA));
				return;
			}
		} else if (getActionName() != null && getActionName().contains("saveEvasionePrenotazioniClientiEdicola") && !Strings.isNullOrEmpty(dataCompEC))  {
			try {
				DateUtilities.parseDate(dataCompEC, DateUtilities.FORMATO_DATA_SLASH);
			} catch (ParseException e) {
				addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataDa));
				return;
			}
		}
		
		
	}
	
	/* -- */
	@BreadCrumb("%{crumbNameAggCli}")
	@SkipValidation
	public String showFilter() throws Exception {
		requestMap.put("tableTitle", getText("igeriv.inserisci.aggiorna.scolastica.clienti"));
		actionName = "libriScolasticiClienti_showClienti.action";
		return SUCCESS;
	}
	
	/* -- */
	@BreadCrumb("%{crumbNameAggCli}")
	@SkipValidation
	public String showClienti() {
		requestMap.put("tableTitle", getText("igeriv.inserisci.aggiorna.scolastica.clienti"));
		clienti = clientiScolasticaService.getClientiEdicolaScolastica(getAuthUser().getArrId(), nome, cognome, codCliente,numOrdineTxt);
		if(clienti!=null && !clienti.isEmpty()){
			for(ClienteEdicolaDto iter : clienti){
				boolean res = false;
				boolean resDaConsegnare = false;
				boolean resOrdiniPresenti = scolasticaService.existOrdini(getAuthUser().getCodFiegDl(), getAuthUser().getCodEdicolaMaster(), iter.getCodCliente());
				iter.setOrdiniPresenti(resOrdiniPresenti);
				if(resOrdiniPresenti)
					res =  scolasticaService.existOrdiniInattivi(getAuthUser().getCodFiegDl(), getAuthUser().getCodEdicolaMaster(), iter.getCodCliente());
				iter.setOrdIncompleti(res);
				
				if(resOrdiniPresenti)
					resDaConsegnare  =  scolasticaService.existOrdiniDaConsegnareAlCliente(getAuthUser().getCodFiegDl(), getAuthUser().getCodEdicolaMaster(), iter.getCodCliente());
				iter.setOrdDaConsegnare(resDaConsegnare);
			}
		}
		requestMap.put("cliente", clienti);
		actionName = "libriScolasticiClienti_showClienti.action";
		return SUCCESS;
	}
	
	/* -- */
	@BreadCrumb("%{crumbNameTracking}")
	@SkipValidation
	public String showFilterTracking() throws Exception {
		requestMap.put("tableTitle", getText("igeriv.tracking.scolastica.clienti"));
		actionName = "libriScolasticiClienti_showTrackingClesp.action";
		return IGerivConstants.ACTION_VIEW_PAGE_TRACKING;
	}
	
	/* -- */
	@BreadCrumb("%{crumbNameTracking}")
	@SkipValidation
	public String showTracking() {
		requestMap.put("tableTitle", getText("igeriv.inserisci.aggiorna.scolastica.clienti"));
		clienti = clientiScolasticaService.getClientiEdicolaScolastica(getAuthUser().getArrId(), nome, cognome, codCliente,numOrdineTxt);
		requestMap.put("cliente", clienti);
		actionName = "libriScolasticiClienti_showClienti.action";
		return IGerivConstants.ACTION_VIEW_PAGE_TRACKING;
	}
	
	
	
	
	
	/* -- */
	@BreadCrumb("%{crumbNameAggCli}")
	@SkipValidation
	public String showOrdiniFilter() throws Exception {
		requestMap.put("tableTitle", getText("igeriv.inserisci.ordine.filter.clienti"));
		actionName = "libriScolasticiClienti_showOrdiniClienti.action";
		return SUCCESS;
	}
	
	/* -- */
	@BreadCrumb("%{crumbNameAggCli}")
	@SkipValidation
	public String showOrdiniClienti() {
		requestMap.put("tableTitle", getText("igeriv.inserisci.ordine.filter.clienti"));
		
		clienti = clientiScolasticaService.getClientiEdicola(getAuthUser().getArrId(), nome, cognome, codCliente);
		
		requestMap.put("cliente", clienti);
		actionName = "libriScolasticiClienti_showOrdiniClienti.action";
		return "success_ordini";
	}
	
	/* -- */
	@SkipValidation
	public String showCliente() throws Exception {
		requestMap.put("tableTitle", getText("igeriv.inserisci.aggiorna.scolastica.clienti"));
		if (idCliente != null && !idCliente.equals("")) {
			clienteGestioneScolastica = clientiScolasticaService.getClienteEdicola(getAuthUser().getArrId(), new Long(idCliente));
		} else {
			clienteGestioneScolastica = new ClienteEdicolaVo();
			clienteGestioneScolastica.setCodEdicola(getAuthUser().getId());
			clienteGestioneScolastica.setCodCliente(clientiScolasticaService.getNextSeqVal(IGerivConstants.SEQ_CLIENTI_EDICOLA));
			GruppoModuliVo gruppoModuliVo = menuService.getGruppoModuliByRole(IGerivConstants.ROLE_IGERIV_CLIENTE_EDICOLA);
			clienteGestioneScolastica.setGruppoModuliVo(gruppoModuliVo);
			if (getAuthUser().getEdicolaDeviettiTodis()) {
				clienteGestioneScolastica_inviaEmail = "false";
			}
		}
		actionName = "libriScolasticiClienti_showCliente.action";
		requestMap.put("clienteGestioneScolastica", clienteGestioneScolastica);
		return IGerivConstants.ACTION_DETT_GESTIONE_CLIENTE_SCOLASTICA_POPUP;
	}
	
	/* -- */
	@SkipValidation
	public String showClienteReadOnly() throws Exception {
		requestMap.put("tableTitle", getText("igeriv.inserisci.ordine.filter.clienti"));
		if (idCliente != null && !idCliente.equals("")) {
			clienteScolastica = clientiScolasticaService.getClienteEdicola(getAuthUser().getArrId(), new Long(idCliente));
		} else {
			clienteScolastica = new ClienteEdicolaVo();
			clienteScolastica.setCodEdicola(getAuthUser().getId());
			clienteScolastica.setCodCliente(clientiScolasticaService.getNextSeqVal(IGerivConstants.SEQ_CLIENTI_EDICOLA));
			GruppoModuliVo gruppoModuliVo = menuService.getGruppoModuliByRole(IGerivConstants.ROLE_IGERIV_CLIENTE_EDICOLA);
			clienteScolastica.setGruppoModuliVo(gruppoModuliVo);
			if (getAuthUser().getEdicolaDeviettiTodis()) {
				clienteGestioneScolastica_inviaEmail = "false";
			}
		}
		actionName = "libriScolasticiClienti_showClienteReadOnly.action";
		requestMap.put("clienteScolastica", clienteScolastica);
		return IGerivConstants.ACTION_DETT_CLIENTE_SCOLASTICA_POPUP;
	}
	
	
	/* -- */
	@SkipValidation
	public String showClienteDettaglioOrdini() throws Exception {
		requestMap.put("tableTitle", getText("igeriv.inserisci.ordine.filter.clienti"));
		List<OrdineLibriDto> listOrdini = null;
		List<OrdineLibriDto> listOrdiniResult  = new ArrayList<OrdineLibriDto>();
		if (idCliente != null && !idCliente.equals("")) {
			listOrdini = scolasticaService.findListOrdiniPerDettaglio(getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodDpeWebEdicola(), null, new Long(idCliente));
			
			for(OrdineLibriDto iter : listOrdini){
				Boolean ordineDaVisualizzare = scolasticaService.visualizzareOrdineCliente(iter.getNumeroOrdine());
				if(ordineDaVisualizzare){
					BigDecimal sumTotPrezzoLibriInOrdine = scolasticaService.sumPrezzoLibriCarrello(iter.getNumeroOrdine());
					iter.setPrezzoTotale(sumTotPrezzoLibriInOrdine);
					//Pulsante Consegna libri dettaglio cliente
					Boolean boolConsegnaLibri = scolasticaService.existLibriDaConsegnare(getAuthUser().getCodFiegDlMaster(),iter.getNumeroOrdineTxt());
					iter.setDettaglioConsegnaLibri(boolConsegnaLibri);
					//Pulsante Tracking dettaglio cliente
					Boolean boolExistTrackingOrdine = scolasticaService.existTrackingOrdine(getAuthUser().getCodFiegDlMaster(),iter.getNumeroOrdineTxt());
					iter.setTracking_attivo(boolExistTrackingOrdine);
					Boolean boolExistTrackingOrdineParzialmenteEvaso = scolasticaService.existTrackingOrdineParzialmenteEvaso(getAuthUser().getCodFiegDlMaster(),iter.getNumeroOrdineTxt());
					iter.setTracking_attivo_parziale(boolExistTrackingOrdineParzialmenteEvaso);
					List<Date> listDate =  scolasticaService.getDateDiConsegnaCliente(getAuthUser().getCodFiegDlMaster(),iter.getNumeroOrdineTxt());
					iter.setDataConsegnaAlCliente((listDate!=null && !listDate.isEmpty())?listDate.get(0):null);
					
					listOrdiniResult.add(iter);
				}
			}
		}
		actionName = "libriScolasticiClienti_showClienteDettaglioOrdini.action";
		requestMap.put("listOrdini", listOrdiniResult);
		return IGerivConstants.ACTION_DETT_ORDINE_CLIENTE_POPUP;
	}
	
	
	
	/* -- */
	@SkipValidation
	public String apriNuovoOrdineCliente() throws Exception {
		Long numeroOrdine = null;
		Long countLibriCarrello = null;
		String msgTipoOrdine = "";
		requestMap.put("tableTitle", getText("igeriv.inserisci.ordine.filter.clienti"));
		if (idCliente != null && !idCliente.equals("")) {
			clienteScolastica = clientiScolasticaService.getClienteEdicola(getAuthUser().getArrId(), new Long(idCliente));
			//SERVIZIO NUOVO CLIENTE CLESP
			ClespNuovoConsumatoreDto dtoNuovoCliente = new ClespNuovoConsumatoreDto();
			dtoNuovoCliente.nome = (clienteScolastica.getNome()!=null && !clienteScolastica.getNome().equals(""))?clienteScolastica.getNome().toUpperCase():"";
			dtoNuovoCliente.cognome=(clienteScolastica.getCognome()!=null && !clienteScolastica.getCognome().equals(""))?clienteScolastica.getCognome().toUpperCase():"";
			dtoNuovoCliente.classe="";
			dtoNuovoCliente.email="";
			dtoNuovoCliente.cellulare="";
			dtoNuovoCliente.codice_pdc_menta = getAuthUser().getCodEdicolaDl().toString();
			dtoNuovoCliente.codice_consumatore_menta= clienteScolastica.getCodCliente().intValue();
			dtoNuovoCliente.cod_fiscale="";
			dtoNuovoCliente.socio="";						
			ResponseEntity<ClespRisultatoNuovoConsumatoreDto> response = clespConsumatoreService.putEntity(dtoNuovoCliente);
			
			
			if(scolasticaService.existOrdineAttivo(getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodDpeWebEdicola(), new Long(idCliente))){
				numeroOrdine = scolasticaService.getOrdineAttivo(getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodDpeWebEdicola(), new Long(idCliente));
				countLibriCarrello = scolasticaService.countLibriCarrello(numeroOrdine);
				msgTipoOrdine = "Continua ordine Numero : "+numeroOrdine;
			}else{
				numeroOrdine = scolasticaService.insertNewOrdineLibri(getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodDpeWebEdicola(), new Long(idCliente));
				countLibriCarrello = scolasticaService.countLibriCarrello(numeroOrdine);
				msgTipoOrdine = "Nuovo ordine Numero : "+numeroOrdine;
			}
		} 
		requestMap.put("cliente", clienteScolastica);
		requestMap.put("numeroOrdine", numeroOrdine);
		requestMap.put("msgOrdine", msgTipoOrdine);
		requestMap.put("countLibriCarrello",countLibriCarrello);
		
		//actionName = "libriScolasticiClienti_restCercaTesto.action";
		actionName = "libriScolasticiClienti_restCercaTestoClesp.action";
		return IGerivConstants.ACTION_PAGINA_NUOVO_ORDINE_LIBRI;
	}
	
	
	/* -- */
	@SkipValidation
	public String continuaOrdineCliente() throws Exception {
		requestMap.put("tableTitle", getText("igeriv.inserisci.aggiorna.scolastica.clienti"));
		if (idCliente != null && !idCliente.equals("")) {
			clienteScolastica = clientiScolasticaService.getClienteEdicola(getAuthUser().getArrId(), new Long(idCliente));
			//select in 9400 / 9401  dt_chiusura is null
		} 
		requestMap.put("cliente", clienteScolastica);
		return IGerivConstants.ACTION_PAGINA_CONTINUA_ORDINE_LIBRI;
	}
	
	
	/**
	 * Ricerca Testo Clesp
	 * @return
	 * @throws Exception
	 */
	@SkipValidation
	public String restCercaTestoClesp() throws Exception {
		requestMap.put("tableTitle", getText("igeriv.inserisci.ordine.filter.clienti"));
		if(this.testoRicerca!=null && !this.testoRicerca.equals("") && this.testoRicerca.length() >= 3){
		
			String tipoRicerca = this.tipoRicercaRadio;
			RicercaLibriTestoDto ricParams = new RicercaLibriTestoDto();
			ricParams.limite = 100;
			ricParams.guid="DPE";
			ricParams.disponibilita = 10;
			ricParams.testoRicerca = this.testoRicerca.trim();
			
			List<ParametriRicercaLibriDto> listParamRicercaLibri=new ArrayList<ParametriRicercaLibriDto>();
			List<RisultatiRicercaLibriDto> listRisultatiRicercaLibri = new ArrayList<RisultatiRicercaLibriDto>();
			
			ClespRicercaCatalogoLibriDto dtoClespRicercaCatalogo = new ClespRicercaCatalogoLibriDto();
			dtoClespRicercaCatalogo.ean_titolo = this.testoRicerca.trim();
			ParametriRicercaLibriDto dtoParametroRicerca = new ParametriRicercaLibriDto();
			
			ResponseEntity<ClespRisultatoRicercaCatalogoLibriDto> result = clespRicercaCatalogoLibriService.putEntity(dtoClespRicercaCatalogo);
			
			dtoParametroRicerca.setGUID(this.testoRicerca.trim());
			dtoParametroRicerca.setTOT_RECORD(""+result.getBody().getArticoli().length);
			listParamRicercaLibri.add(dtoParametroRicerca);
			
			if(result!=null && result.getBody().getArticoli()!=null){
				
				for(ClespDettaglioLibroDto iter : result.getBody().getArticoli() ){
					RisultatiRicercaLibriDto dtoRes = new RisultatiRicercaLibriDto();
					dtoRes.setBARCODE(iter.ean);
					dtoRes.setTITOLO(iter.titolo);
					dtoRes.setEDITORE(iter.editore);
					dtoRes.setAUTORI(iter.autore);
					dtoRes.setPREZZO(iter.prezzo.toString());
					dtoRes.setURL(iter.url_immagine);
					dtoRes.setDisponibile(iter.prenotabile);
					dtoRes.setSKU(iter.ean);
					dtoRes.setClasse_sconto(iter.classe_sconto);
					dtoRes.setVOLUME((iter.volume!=null)?iter.volume:"");
					listRisultatiRicercaLibri.add(dtoRes);
				}
				
				
				//VITTORIO 24/08/15
				//RIMUOVO I LIBRI NON DISPONIBILI 
				Iterables.removeIf(listRisultatiRicercaLibri, new Predicate<RisultatiRicercaLibriDto>() {
					public boolean apply(RisultatiRicercaLibriDto t) {
						return !t.isDisponibileClesp();
					}
				});
				
				requestMap.put("risricercalibri", listRisultatiRicercaLibri);
				requestMap.put("paramricercalibri", listParamRicercaLibri.get(0));
			}
		}else{
			if(this.testoRicerca.length() != 0)
				addActionError(MessageFormat.format(getText("error.campo.x.lunghezza.minima"),  getText("dpe.contact.form.ricerca")));
		}
		Long countLibriCarrello = null;
		String msgTipoOrdine = "";
		if(idNumeroOrdine!=null){
			countLibriCarrello = scolasticaService.countLibriCarrello(new Long(idNumeroOrdine));
			msgTipoOrdine = "Continua ordine Numero : "+idNumeroOrdine;
			
			requestMap.put("numeroOrdine", idNumeroOrdine);
			requestMap.put("msgOrdine", msgTipoOrdine);
			requestMap.put("countLibriCarrello",countLibriCarrello);
		}
		
		
		return IGerivConstants.ACTION_PAGINA_NUOVO_ORDINE_LIBRI;
	}
	
	
	
	/* -- */
	@SkipValidation
	public String showDettaglioLibroClesp() throws Exception {
		requestMap.put("tableTitle", getText("DETTAGLIO TESTO"));
		
		if(sku != null && !sku.equals("")){
			RicercaLibriTestoDto ricParams = new RicercaLibriTestoDto();
			ricParams.limite = 30;
			ricParams.guid="DPE";
			ricParams.disponibilita = 10;
			ricParams.typeOp = RicercaLibriTestoDto.TYPE.VIEW_DETAIL_BOOK;
			ricParams.sku= sku;
			
			List<DettaglioRicercaLibriDto> dettaglioRicercaLibriDto = new ArrayList<DettaglioRicercaLibriDto>();
			ClespRicercaCatalogoLibriDto dtoClespRicercaCatalogo = new ClespRicercaCatalogoLibriDto();
			dtoClespRicercaCatalogo.ean_titolo = this.sku;
			ResponseEntity<ClespRisultatoRicercaCatalogoLibriDto> result = clespRicercaCatalogoLibriService.putEntity(dtoClespRicercaCatalogo);
			if(result!=null && result.getBody().getArticoli()!=null){		
				for(ClespDettaglioLibroDto iter : result.getBody().getArticoli() ){
					DettaglioRicercaLibriDto dtoRes = new DettaglioRicercaLibriDto();
					dtoRes.setSKU(iter.ean);
					dtoRes.setBARCODE(iter.ean);
					dtoRes.setTITOLO(iter.titolo);
					dtoRes.setAUTORI(iter.autore);
					dtoRes.setEDITORE(iter.editore);
					dtoRes.setPREZZO(iter.prezzo.toString());
					dtoRes.setTOMI(iter.numero_tomi.toString());
					dtoRes.setURL((iter.url_immagine!=null && !iter.url_immagine.equals(""))?iter.url_immagine:"/app_img/not_img_book.jpg");
					dtoRes.setDISPONIBILE((iter.disponibilita!=null)?iter.disponibilita:"NO");
					dtoRes.setClasse_sconto((iter.classe_sconto!=null)?iter.classe_sconto:new Integer(0));
					dtoRes.setVOLUME((iter.volume!=null)?iter.volume:"");
					dettaglioRicercaLibriDto.add(dtoRes);
				}
			}
			
			OrdineLibriDettVo dettaglioLibro = scolasticaService.getDettaglioLibro(idNumeroOrdine, sku);
			requestMap.put("dettaglioLibroScolastico", dettaglioRicercaLibriDto.get(0));
			requestMap.put("ricParams", ricParams);
			requestMap.put("seqOrdine_modifica", seqOrdine);
			requestMap.put("dettaglioLibro_copertina", dettaglioLibro);
			
			
		}		
		requestMap.put("numeroOrdine", idNumeroOrdine);
		return IGerivConstants.ACTION_VIEW_DETTAGLIO_LIBRO;
	}
	
	
	/* -- */
	@SkipValidation
	public String addCartClesp() throws Exception {
		Long countLibriCarrello = null;
		String msgTipoOrdine = "";
		if(sku != null && !sku.equals("") && guid!=null && !guid.equals("")) {
				
			if(idNumeroOrdine != null){
				OrdineLibriVo ordine = scolasticaService.getDettaglioOrdineLibri(new Long(idNumeroOrdine));
				if(ordine!=null && ordine.getDataChiusuraOrdine()!=null){
						return IGerivConstants.REDIRECT;
					}
				}
			
				List<DettaglioRicercaLibriDto> dettaglioRicercaLibriDto = new ArrayList<DettaglioRicercaLibriDto>();
				List<ParametriRicercaLibriDto> listParamRicercaLibri=new ArrayList<ParametriRicercaLibriDto>();
			
				ClespRicercaCatalogoLibriDto dtoClespRicercaCatalogo = new ClespRicercaCatalogoLibriDto();
				dtoClespRicercaCatalogo.ean_titolo = this.sku;
				ResponseEntity<ClespRisultatoRicercaCatalogoLibriDto> result = clespRicercaCatalogoLibriService.putEntity(dtoClespRicercaCatalogo);
				
				if(result!=null && result.getBody().getArticoli()!=null){		
					for(ClespDettaglioLibroDto iter : result.getBody().getArticoli() ){
						DettaglioRicercaLibriDto dtoRes = new DettaglioRicercaLibriDto();
						dtoRes.setSKU(iter.ean);
						dtoRes.setBARCODE(iter.ean);
						dtoRes.setTITOLO(iter.titolo);
						dtoRes.setAUTORI(iter.autore);
						dtoRes.setEDITORE(iter.editore);
						dtoRes.setPREZZO(iter.prezzo.toString());
						dtoRes.setTOMI(iter.numero_tomi.toString());
						dtoRes.setURL((iter.url_immagine!=null && !iter.url_immagine.equals(""))?iter.url_immagine:"/app_img/not_img_book.jpg");
						dtoRes.setDISPONIBILE((iter.disponibilita!=null)?iter.disponibilita:"NO");
						dtoRes.setClasse_sconto((iter.classe_sconto!=null)?iter.classe_sconto:new Integer(0));
						dtoRes.setVOLUME((iter.volume!=null)?iter.volume:"0");
						dettaglioRicercaLibriDto.add(dtoRes);
					}
				}
				
				ParametriRicercaLibriDto dtoParametroRicerca = new ParametriRicercaLibriDto();
				dtoParametroRicerca.setGUID(dtoClespRicercaCatalogo.getEan_titolo());
				dtoParametroRicerca.setTOT_RECORD(""+result.getBody().getArticoli().length);
				listParamRicercaLibri.add(dtoParametroRicerca);
				
				RicercaLibriTestoDto ricParams = new RicercaLibriTestoDto();
				ricParams.limite = 30;
				ricParams.guid="DPE";
				ricParams.disponibilita = 10;
				ricParams.typeOp = RicercaLibriTestoDto.TYPE.VIEW_DETAIL_BOOK;
				ricParams.sku= sku;
				ricParams.setGuid(guid);	
			
				DettaglioRicercaLibriDto dettLibro = dettaglioRicercaLibriDto.get(0);
				
				OrdineLibriDettVo dettLibroOrdine = new OrdineLibriDettVo();
				dettLibroOrdine.setNumeroOrdine(new Long(idNumeroOrdine));
				dettLibroOrdine.setSku(dettLibro.getSKU());
				
				dettLibroOrdine.setBarcode(dettLibro.getBARCODE());
				dettLibroOrdine.setTitolo(dettLibro.getTITOLO());
				dettLibroOrdine.setAutore(dettLibro.getAUTORI());
				dettLibroOrdine.setEditore(dettLibro.getEDITORE());
				
				String numberString = dettLibro.getPREZZO();
				numberString = numberString.replaceAll(",", ".");
				BigDecimal prezzo = new BigDecimal(numberString);
				dettLibroOrdine.setPrezzoCopertina(prezzo);
				
				dettLibroOrdine.setFlagCopertina((sceltaCopertina.equals("true"))? new Integer("1") : new Integer("0"));
				dettLibroOrdine.setTomi(new Integer((dettLibro.getTOMI()!=null)?dettLibro.getTOMI():"0"));
				dettLibroOrdine.setIdLogoCopertina((sceltaLogo!=null)? new Integer(sceltaLogo):new Integer("0"));
				
				// 18-05-2018 TIPO TESTO PER GESTIONE AGGI
				dettLibroOrdine.setTipotesto(dettLibro.getClasse_sconto());
				
				//27-06-2018 volume
				dettLibroOrdine.setVolume(dettLibro.getVOLUME());
				
				if(sceltaCopertina.equals("true")){
					//[mailto:vittorio.bassignani@dpe.it] Inviato: mercoledì 22 giugno 2016 15:48
					//Quindi iva compresa:
					//agenzia 1,10
					//edicola 1,34
					//cliente finale 1,50
					dettLibroOrdine.setPrezzoUnitarioCopertinaCliente(new BigDecimal("1.50"));
					dettLibroOrdine.setPrezzoUnitarioCopertinaRivendita(new BigDecimal("1.34"));
					dettLibroOrdine.setPrezzoUnitarioCopertinaDl(new BigDecimal("0.98"));
					
					BigDecimal prezzoCalcolatoCliente = dettLibroOrdine.getPrezzoUnitarioCopertinaCliente().multiply((dettLibroOrdine.getTomi()<=0)?new BigDecimal("1"):new BigDecimal(dettLibroOrdine.getTomi()));
					dettLibroOrdine.setPrezzoCopertinaCliente(prezzoCalcolatoCliente);
					BigDecimal prezzoCalcolatoRivendita = dettLibroOrdine.getPrezzoUnitarioCopertinaRivendita().multiply((dettLibroOrdine.getTomi()<=0)?new BigDecimal("1"):new BigDecimal(dettLibroOrdine.getTomi()));
					dettLibroOrdine.setPrezzoCopertinaRivendita(prezzoCalcolatoRivendita);
					BigDecimal prezzoCalcolatoDl = dettLibroOrdine.getPrezzoUnitarioCopertinaDl().multiply((dettLibroOrdine.getTomi()<=0)?new BigDecimal("1"):new BigDecimal(dettLibroOrdine.getTomi()));
					dettLibroOrdine.setPrezzoCopertinaDl(prezzoCalcolatoDl);
					
					// 21-06-2016 Gestione del servizio di copertinatura
					dettLibroOrdine.setPrimaRigaCopertina((copertina_primariga!=null)?copertina_primariga:"");
					dettLibroOrdine.setSecondaRigaCopertina((copertina_secondariga!=null)?copertina_secondariga:"");
					dettLibroOrdine.setTerzaRigaCopertina((copertina_terzariga!=null)?copertina_terzariga:"");
					
				}
				
				dettLibroOrdine.setQuantitaLibri(new Integer(1));
				dettLibroOrdine.setStato(ETrack.INS.getId());
				dettLibroOrdine.setUrlImmagineCopertina(dettLibro.getURL());
				
				//UPDATE
				if(seqOrdine!=null){
					dettLibroOrdine.setSeqordine(new Long(seqOrdine));
				}
				
				scolasticaService.addLibriCarrello(dettLibroOrdine);
				
				countLibriCarrello = scolasticaService.countLibriCarrello(new Long(idNumeroOrdine));
				msgTipoOrdine = "Continua ordine Numero : "+idNumeroOrdine;
				
				requestMap.put("numeroOrdine", idNumeroOrdine);
				requestMap.put("msgOrdine", msgTipoOrdine);
				requestMap.put("countLibriCarrello",countLibriCarrello);
				//clearActionErrors();
				clearErrorsAndMessages();
				clearMessages();
				addCart = true;
		}
		
		//return IGerivConstants.ACTION_PAGINA_NUOVO_ORDINE_LIBRI;
		return SUCCESS;
	}
	
	
	/* -- */
	@SkipValidation
	public String confermaOrdineClesp() throws Exception {
		requestMap.put("tableTitle", getText("DETTAGLIO ORDINE"));
		resultMap = new LinkedHashMap<String, String>();
		
		if(idNumeroOrdine!=null){
			
			OrdineLibriVo ordine = scolasticaService.getDettaglioOrdineLibri(new Long(idNumeroOrdine));
			if(ordine.getListDettaglioOrdine().size()<=0){
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("dpe.ordine.msg.carrello.vuoto") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
			Long codicePdcClesp = new Long(ordine.getAnagraficaEdicolaVo().getCodEdicolaDl());	
			Long codiceConsumatoreClesp = ordine.getCliente().getCodCliente();
			
			ClespNuovoOrdineDto dtoOrdine = new ClespNuovoOrdineDto();
			
			ClespDettaglioLibroOrdineDto[] listDettaglioOrdine = new ClespDettaglioLibroOrdineDto[ordine.getListDettaglioOrdine().size()];
			List<OrdineLibriDettVo> listDettLibri = ordine.getListDettaglioOrdine();
			int countArr = 0;
			for (OrdineLibriDettVo iter : listDettLibri) {
				ClespDettaglioLibroOrdineDto dto = new ClespDettaglioLibroOrdineDto();
				dto.ean= iter.getBarcode();
				dto.codice_pdc = codicePdcClesp;
				dto.codice_consumatore = codiceConsumatoreClesp;
				dto.usato = "No";
				dto.copertinato = (iter.getFlagCopertina()>0)?"Si":"No";
				dto.riga1= iter.getPrimaRigaCopertina();
				dto.riga2 = iter.getSecondaRigaCopertina();
				dto.riga3 = iter.getTerzaRigaCopertina();
				listDettaglioOrdine[countArr] = dto;
				countArr++;
			}
			dtoOrdine.setListLibriOrdine(listDettaglioOrdine);
			
	        ResponseEntity<ClespRisultatoNuovoOrdineDto> response = clespOrdineService.putEntity(dtoOrdine);
	        //POST ORDINE - RETURN NUMERO_ORDINE_TXT
	    	if(response!=null){
				ordine.setDataChiusuraOrdine(new Date());
				ordine.setNumeroOrdineTxt(response.getBody().id_ordine.toString());
				scolasticaService.confermaOrdine(ordine);
			}	
			
			//TRACKING CLESP PER SET ID_RIGA_ORDINE
	    	ClespRicercaTrackingOrdineDto dtoTrackingClesp = new ClespRicercaTrackingOrdineDto();
			dtoTrackingClesp.numero_ordine = new Long(ordine.getNumeroOrdineTxt());
			ResponseEntity<ClespRisultatoTrackingOrdineDto> res = clespTrackingOrdineService.putEntity(dtoTrackingClesp);
			if(res!=null && res.getBody()!=null && res.getStatusCode() == HttpStatus.OK){
				if(res.getBody().tracking!=null && res.getBody().tracking.length>0){
					for(ClespDettaglioTrakingOrdineDto iter : res.getBody().tracking ){
						// Modifica add keynum 16/09/2015
						OrdineLibriDettVo dettLibro = scolasticaService.getDettaglioLibroNotKeyNum(ordine.getNumeroOrdine(),iter.ean);
						dettLibro.setKeynum(new Long(iter.id_riga_ordine));
						scolasticaService.saveBaseVo(dettLibro);
					}
				}
			}
	    	
	    	
	    	/*
			//Modifica del 16/09/2015
			//Eseguire il tracking dopo la conferma dell'ordine per poter registrare per ogni singolo testo il proprio keynum
			List<TrackingElencoLibriDto> listElencoLibri = null;
			List<OrdineLibriDettVo> listLibriTrackingResult = new ArrayList<OrdineLibriDettVo>();
			TrackingDto trackingDto;
			RicercaLibriTestoDto ricParams = new RicercaLibriTestoDto();
			ricParams.setNumOrdine(ordine.getNumeroOrdine().toString());
			ricParams.setNumOrdineTxt(ordine.getNumeroOrdineTxt());
			
			ResponseEntity<TrackingDto> res = libriScolasticiTrackingOrdiniRestService.getEntity(ricParams);
			if(res!=null && res.getBody()!=null && res.getStatusCode() == HttpStatus.OK){
				trackingDto = res.getBody();
				listElencoLibri = new ArrayList(Arrays.asList(res.getBody().getElencolibri()));
				if(trackingDto!=null && listElencoLibri!=null){
					for(TrackingElencoLibriDto iter :listElencoLibri){
						
						OrdineLibriDettVo dettLibro = scolasticaService.getDettaglioLibroNotKeyNum(ordine.getNumeroOrdine(),iter.getSku());
						dettLibro.setKeynum(new Long(iter.getKeynum()));
						scolasticaService.saveBaseVo(dettLibro);
												
					}
				}
			}
			*/
			
			//INVIO MAIL CON ALLEGATO
			if(ordine.getCliente().getEmail()!=null && !ordine.getCliente().getEmail().toString().equals("")){
				SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
				String dateInString = "04-11-2018 23:59:00";
				Date dateScadenza = sdf.parse(dateInString);
				Date oggi = new Date();
				Integer confrontoDate = oggi.compareTo(dateScadenza);
				Boolean isRichiestaScaduta = (confrontoDate > 0)?true:false;
				if(!isRichiestaScaduta)				
					this.sendEmailVoucher(ordine.getCliente().getEmail());
			}
			
			
			resultMap.put("result", MessageFormat.format(getText("igeriv.ritiro.cliente.cancellato"), nome));
		}
			
		return SUCCESS;
	}
	
	
	private void sendEmailVoucher(String email) throws MessagingException, UnsupportedEncodingException {
		//AnagraficaAgenziaVo agenzia = agenzieService.getAgenziaByCodice(getAuthUser().getCodFiegDl());
		String[] emailAgenzia = new String[1];
		emailAgenzia[0] = email;
		
		String subject = "Invio Regolamento e Coupon Mirabilandia 2018 ";
		String message = "Gentile Cliente, <br/>la ringraziamo per aver effettuato l’ordine dei testi scolastici "
				+ " in edicola e come da promozione in atto in allegato trova il "
				+ " rispettivo regolamento ed il cupon che dovrà stampare e presentare "
				+ "direttamente alle casse di Mirabilandia per usufruire dell’offerta a Lei riservata.<br/>"
				+ "Ringraziandola nuovamente l’attendiamo in edicola per futuri "
				+ "acquisti e cordialmente salutiamo."
				+ " <br/>"
				+ " <br/>"				
				+ " <br/>"
				+ " <br/>"
				+ " A.d.g. Menta Srl";
		
		File[] allegati = new File[2];
		allegati[0] = new File(pathFilesPdf + System.getProperty("file.separator") + "Coupon-Mirabilandia-2018.pdf");
		allegati[1] = new File(pathFilesPdf + System.getProperty("file.separator") + "Regolamento-MIRABILANDIA-2018.pdf");

		mailingListService.sendEmailWithAttachments(emailAgenzia, subject, message, allegati, true, null, false, null, null);
		//mailingListService.sendEmailWithAttachments(emailAgenzia, subject, message, null, true, null, false, null,null);
	}
	
	
	
	/* -- */
	@SkipValidation
	public String confermaOrdineRiepilogoClesp() throws Exception {
		requestMap.put("tableTitle", getText("RIEPILOGO ORDINE"));
		resultMap = new LinkedHashMap<String, String>();
		
		if(idNumeroOrdine!=null){
			
			OrdineLibriVo ordine = scolasticaService.getDettaglioOrdineLibri(new Long(idNumeroOrdine));
			if(ordine!=null){
				BigDecimal sumTotPrezzoLibriInOrdine = scolasticaService.sumPrezzoLibriCarrello(new Long(idNumeroOrdine));
				
				DecimalFormat df = new DecimalFormat("#0.00");
				String prezzoFormat = df.format(sumTotPrezzoLibriInOrdine)+" &euro; " ;
				
				Long countLibriOrdine = scolasticaService.countLibriCarrello(new Long(idNumeroOrdine));
				requestMap.put("infoordine", ordine);
				requestMap.put("dettaglioordine", ordine.getListDettaglioOrdine());
				requestMap.put("totaleDovuto", prezzoFormat);
				requestMap.put("countLibriOrdine", countLibriOrdine);
			}
			ClienteEdicolaVo cliente = clientiScolasticaService.getClienteEdicola(getAuthUser().getArrId(), ordine.getCodCliente());
			if(cliente!=null)
				requestMap.put("anagraficacliente", cliente);
			
			
			
			resultMap.put("result", MessageFormat.format(getText("igeriv.ritiro.cliente.cancellato"), nome));
		}
			
		return IGerivConstants.ACTION_VIEW_DETTAGLIO_ORDINE_RIEPILOGO;
	}
	
	
	
	
	
	
	/* -- */
	@SkipValidation
	public String restCercaTesto() throws Exception {
		requestMap.put("tableTitle", getText("igeriv.inserisci.ordine.filter.clienti"));
		
		if(this.testoRicerca!=null && !this.testoRicerca.equals("") && this.testoRicerca.length() >= 3){
		
			String tipoRicerca = this.tipoRicercaRadio;
			RicercaLibriTestoDto ricParams = new RicercaLibriTestoDto();
			ricParams.limite = 100;
			ricParams.guid="DPE";
			ricParams.disponibilita = 10;
			ricParams.testoRicerca = this.testoRicerca.trim();
			
			List<ParametriRicercaLibriDto> listParamRicercaLibri=null;
			List<RisultatiRicercaLibriDto> listRisultatiRicercaLibri =null;
			
			ResponseEntity<RisultatoRicercaLibriDto> res = libriScolasticiRestService.getEntity(ricParams);
			if(res!=null && res.getBody().getParametri()!=null && res.getBody().getRicerca()!=null){
				listParamRicercaLibri = new ArrayList(Arrays.asList(res.getBody().getParametri()));
				listRisultatiRicercaLibri = new ArrayList(Arrays.asList(res.getBody().getRicerca()));
				//VITTORIO 24/08/15
				//RIMUOVO I LIBRI NON DISPONIBILI 
				Iterables.removeIf(listRisultatiRicercaLibri, new Predicate<RisultatiRicercaLibriDto>() {
					public boolean apply(RisultatiRicercaLibriDto t) {
						return !t.isDisponibile();
					}
				});
				
				requestMap.put("risricercalibri", listRisultatiRicercaLibri);
				requestMap.put("paramricercalibri", listParamRicercaLibri.get(0));
			}
		}else{
			if(this.testoRicerca.length() != 0)
				addActionError(MessageFormat.format(getText("error.campo.x.lunghezza.minima"),  getText("dpe.contact.form.ricerca")));
		}
		Long countLibriCarrello = null;
		String msgTipoOrdine = "";
		if(idNumeroOrdine!=null){
			countLibriCarrello = scolasticaService.countLibriCarrello(new Long(idNumeroOrdine));
			msgTipoOrdine = "Continua ordine Numero : "+idNumeroOrdine;
			
			requestMap.put("numeroOrdine", idNumeroOrdine);
			requestMap.put("msgOrdine", msgTipoOrdine);
			requestMap.put("countLibriCarrello",countLibriCarrello);
		}
		
		
		return IGerivConstants.ACTION_PAGINA_NUOVO_ORDINE_LIBRI;
	}
	
	/* -- */
	@SkipValidation
	public String showDettaglioLibro() throws Exception {
		requestMap.put("tableTitle", getText("DETTAGLIO TESTO"));
		if(sku != null && !sku.equals("") && guid!=null && !guid.equals("")) {
			RicercaLibriTestoDto ricParams = new RicercaLibriTestoDto();
			ricParams.limite = 30;
			ricParams.guid="DPE";
			ricParams.disponibilita = 10;
			ricParams.typeOp = RicercaLibriTestoDto.TYPE.VIEW_DETAIL_BOOK;
			ricParams.sku= sku;
			ricParams.setGuid(guid);
			ResponseEntity<RisultatoRicercaLibriDto> res = libriScolasticiDettaglioRestService.getEntity(ricParams);
			List<DettaglioRicercaLibriDto> dettaglioRicercaLibriDto = new ArrayList(Arrays.asList(res.getBody().getItem()));
			if(dettaglioRicercaLibriDto.get(0).getURL()==null || dettaglioRicercaLibriDto.get(0).getURL().equals(""))
				dettaglioRicercaLibriDto.get(0).setURL("/app_img/not_img_book.jpg");
			requestMap.put("dettaglioLibroScolastico", dettaglioRicercaLibriDto.get(0));
			requestMap.put("ricParams", ricParams);
			
		}else if(sku != null && !sku.equals("")){
			RicercaLibriTestoDto ricParams = new RicercaLibriTestoDto();
			ricParams.limite = 30;
			ricParams.guid="DPE";
			ricParams.disponibilita = 10;
			ricParams.typeOp = RicercaLibriTestoDto.TYPE.VIEW_DETAIL_BOOK;
			ricParams.sku= sku;
			ResponseEntity<RisultatoRicercaLibriDto> res = libriScolasticiDettaglioRestService.getEntity(ricParams);
			List<DettaglioRicercaLibriDto> dettaglioRicercaLibriDto = new ArrayList(Arrays.asList(res.getBody().getItem()));
			if(dettaglioRicercaLibriDto.get(0).getURL()==null || dettaglioRicercaLibriDto.get(0).getURL().equals(""))
				dettaglioRicercaLibriDto.get(0).setURL("/app_img/not_img_book.jpg");
			
			OrdineLibriDettVo dettaglioLibro = scolasticaService.getDettaglioLibro(idNumeroOrdine, sku);
			
			requestMap.put("dettaglioLibroScolastico", dettaglioRicercaLibriDto.get(0));
			requestMap.put("ricParams", ricParams);
			requestMap.put("seqOrdine_modifica", seqOrdine);
			requestMap.put("dettaglioLibro_copertina", dettaglioLibro);
			
			
		}		
		requestMap.put("numeroOrdine", idNumeroOrdine);
		return IGerivConstants.ACTION_VIEW_DETTAGLIO_LIBRO;
	}
	
	
	
	
	
	
	/* -- */
	@SkipValidation
	public String addCart() throws Exception {
		Long countLibriCarrello = null;
		String msgTipoOrdine = "";
		if(sku != null && !sku.equals("") && guid!=null && !guid.equals("")) {
				
			if(idNumeroOrdine != null){
				OrdineLibriVo ordine = scolasticaService.getDettaglioOrdineLibri(new Long(idNumeroOrdine));
				if(ordine!=null && ordine.getDataChiusuraOrdine()!=null){
						return IGerivConstants.REDIRECT;
					}
				}
			
				RicercaLibriTestoDto ricParams = new RicercaLibriTestoDto();
				ricParams.typeOp = RicercaLibriTestoDto.TYPE.VIEW_DETAIL_BOOK;
				ricParams.sku= sku;
				ricParams.setGuid(guid);
				ResponseEntity<RisultatoRicercaLibriDto> res = libriScolasticiDettaglioRestService.getEntity(ricParams);
				List<DettaglioRicercaLibriDto> dettaglioRicercaLibriDto = new ArrayList(Arrays.asList(res.getBody().getItem()));
				DettaglioRicercaLibriDto dettLibro = dettaglioRicercaLibriDto.get(0);
				
				OrdineLibriDettVo dettLibroOrdine = new OrdineLibriDettVo();
				dettLibroOrdine.setNumeroOrdine(new Long(idNumeroOrdine));
				dettLibroOrdine.setSku(dettLibro.getSKU());
				
				dettLibroOrdine.setBarcode(dettLibro.getBARCODE());
				dettLibroOrdine.setTitolo(dettLibro.getTITOLO());
				dettLibroOrdine.setAutore(dettLibro.getAUTORI());
				dettLibroOrdine.setEditore(dettLibro.getEDITORE());
				
				String numberString = dettLibro.getPREZZO();
				numberString = numberString.replaceAll(",", ".");
				BigDecimal prezzo = new BigDecimal(numberString);
				dettLibroOrdine.setPrezzoCopertina(prezzo);
				
				dettLibroOrdine.setFlagCopertina((sceltaCopertina.equals("true"))? new Integer("1") : new Integer("0"));
				dettLibroOrdine.setTomi(new Integer((dettLibro.getTOMI()!=null)?dettLibro.getTOMI():"0"));
				dettLibroOrdine.setIdLogoCopertina((sceltaLogo!=null)? new Integer(sceltaLogo):new Integer("0"));
				
				if(sceltaCopertina.equals("true")){
					//[mailto:vittorio.bassignani@dpe.it] Inviato: mercoledì 22 giugno 2016 15:48
					//Quindi iva compresa:
					//agenzia 1,10
					//edicola 1,34
					//cliente finale 1,50
					dettLibroOrdine.setPrezzoUnitarioCopertinaCliente(new BigDecimal("1.50"));
					dettLibroOrdine.setPrezzoUnitarioCopertinaRivendita(new BigDecimal("1.34"));
					dettLibroOrdine.setPrezzoUnitarioCopertinaDl(new BigDecimal("1.10"));
					
					BigDecimal prezzoCalcolatoCliente = dettLibroOrdine.getPrezzoUnitarioCopertinaCliente().multiply(new BigDecimal(dettLibroOrdine.getTomi()));
					dettLibroOrdine.setPrezzoCopertinaCliente(prezzoCalcolatoCliente);
					BigDecimal prezzoCalcolatoRivendita = dettLibroOrdine.getPrezzoUnitarioCopertinaRivendita().multiply(new BigDecimal(dettLibroOrdine.getTomi()));
					dettLibroOrdine.setPrezzoCopertinaRivendita(prezzoCalcolatoRivendita);
					BigDecimal prezzoCalcolatoDl = dettLibroOrdine.getPrezzoUnitarioCopertinaDl().multiply(new BigDecimal(dettLibroOrdine.getTomi()));
					dettLibroOrdine.setPrezzoCopertinaDl(prezzoCalcolatoDl);
					
					// 21-06-2016 Gestione del servizio di copertinatura
					dettLibroOrdine.setPrimaRigaCopertina((copertina_primariga!=null)?copertina_primariga:"");
					dettLibroOrdine.setSecondaRigaCopertina((copertina_secondariga!=null)?copertina_secondariga:"");
					dettLibroOrdine.setTerzaRigaCopertina((copertina_terzariga!=null)?copertina_terzariga:"");
					
				}
				
				dettLibroOrdine.setQuantitaLibri(new Integer(1));
				dettLibroOrdine.setStato(ETrack.INS.getId());
				dettLibroOrdine.setUrlImmagineCopertina(dettLibro.getURL());
				
				//UPDATE
				if(seqOrdine!=null){
					dettLibroOrdine.setSeqordine(new Long(seqOrdine));
				}
				
				scolasticaService.addLibriCarrello(dettLibroOrdine);
				
				countLibriCarrello = scolasticaService.countLibriCarrello(new Long(idNumeroOrdine));
				msgTipoOrdine = "Continua ordine Numero : "+idNumeroOrdine;
				
				requestMap.put("numeroOrdine", idNumeroOrdine);
				requestMap.put("msgOrdine", msgTipoOrdine);
				requestMap.put("countLibriCarrello",countLibriCarrello);
				//clearActionErrors();
				clearErrorsAndMessages();
				clearMessages();
				addCart = true;
		}
		
		//return IGerivConstants.ACTION_PAGINA_NUOVO_ORDINE_LIBRI;
		return SUCCESS;
	}
	
	/* -- */
	@SkipValidation
	public String showOrdiniCliente() throws Exception {
		requestMap.put("tableTitle", getText("igeriv.inserisci.aggiorna.scolastica.clienti"));
		List<OrdineLibriDto> listOrdini =null;
		if (idCliente != null && !idCliente.equals("")) {
			listOrdini = scolasticaService.findListOrdiniLibri(getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodDpeWebEdicola(), null, new Long(idCliente));
		}
		requestMap.put("listOrdini", listOrdini);
		return IGerivConstants.ACTION_NUOVO_CLIENTE;
	}
	
	
	/* -- */
	@SkipValidation
	public String showDettaglioOrdine() throws Exception {
		OrdineLibriVo ordine = null;
		requestMap.put("tableTitle", getText("DETTAGLIO ORDINE"));
		if(idNumeroOrdine != null){
			ordine = scolasticaService.getDettaglioOrdineLibri(new Long(idNumeroOrdine));
			if(ordine!=null){
				BigDecimal sumTotPrezzoLibriInOrdine = scolasticaService.sumPrezzoLibriCarrello(new Long(idNumeroOrdine));
				BigDecimal sumTotPrezzoCopertinatura = scolasticaService.sumPrezzoCopertinatura(new Long(idNumeroOrdine));
				BigDecimal sumTotPrezzoTotale = scolasticaService.sumPrezzoTotale(new Long(idNumeroOrdine));
				
				DecimalFormat df = new DecimalFormat("#0.00");
				String prezzoFormatPrezzoLibriInOrdine = df.format(sumTotPrezzoLibriInOrdine)+" &euro; " ;
				String prezzoFormatPrezzoCopertinatura = df.format(sumTotPrezzoCopertinatura)+" &euro; " ;
				String prezzoFormatPrezzoTotale = df.format(sumTotPrezzoTotale)+" &euro; " ;
				
				requestMap.put("infoordine", ordine);
				requestMap.put("dettaglioordine", ordine.getListDettaglioOrdine());
				requestMap.put("totalePrezzoLibri", prezzoFormatPrezzoLibriInOrdine);
				requestMap.put("totalePrezzoCopertine", prezzoFormatPrezzoCopertinatura);
				requestMap.put("totaleDovuto", prezzoFormatPrezzoTotale);
			}
			ClienteEdicolaVo cliente = clientiScolasticaService.getClienteEdicola(getAuthUser().getArrId(), ordine.getCodCliente());
			if(cliente!=null)
				requestMap.put("anagraficacliente", cliente);
			
		}		
		requestMap.put("numeroOrdine", idNumeroOrdine);
		
		if(ordine!=null && ordine.getDataChiusuraOrdine()==null){
			actionName = "libriScolasticiClienti_showDettaglioOrdine.action";
			return IGerivConstants.ACTION_VIEW_DETTAGLIO_ORDINE;
		}else{
			actionName = "libriScolasticiClienti_showDettaglioOrdine.action";
			return IGerivConstants.ACTION_VIEW_DETTAGLIO_ORDINE_RIEPILOGO;
		}
		
		
	}
	
	
	
	/* -- */
	@SkipValidation
	public String deleteOrdineIntero() throws Exception {
		requestMap.put("tableTitle", getText("DETTAGLIO ORDINE"));
		resultMap = new LinkedHashMap<String, String>();
		if(idNumeroOrdine != null){
			boolean resdelete = scolasticaService.deleteOrdineLibri(new Long(idNumeroOrdine));		
			if(resdelete)
				resultMap.put("result", getText("dpe.ordine.msg.cancella.ordine.intero.ok"));
		}		
		requestMap.put("numeroOrdine", idNumeroOrdine);
		return SUCCESS;
	}
	
	
	/* -- */
	@SkipValidation
	public String deleteLibroDalCarrello() throws Exception {
		requestMap.put("tableTitle", getText("DETTAGLIO ORDINE"));
		resultMap = new LinkedHashMap<String, String>();
		
		if(idNumeroOrdine != null && seq != null && sku != null ){
			boolean resdelete = scolasticaService.deleteLibroDaOrdine(idNumeroOrdine,new Long(seq), sku);
			resultMap.put("result", getText("dpe.ordine.msg.cancella.ordine.intero.ok"));
			
			OrdineLibriVo ordine = scolasticaService.getDettaglioOrdineLibri(new Long(idNumeroOrdine));
			if(ordine!=null){
				BigDecimal sumTotPrezzoLibriInOrdine = scolasticaService.sumPrezzoLibriCarrello(new Long(idNumeroOrdine));
				DecimalFormat df = new DecimalFormat("#0.00");
				String prezzoFormat = df.format(sumTotPrezzoLibriInOrdine)+" &euro; " ;
				
				requestMap.put("infoordine", ordine);
				requestMap.put("dettaglioordine", ordine.getListDettaglioOrdine());
				requestMap.put("sumTotPrezzoLibri", prezzoFormat);
			}
			ClienteEdicolaVo cliente = clientiScolasticaService.getClienteEdicola(getAuthUser().getArrId(), ordine.getCodCliente());
			if(cliente!=null)
				requestMap.put("anagraficacliente", cliente);
			
		}
			
		actionName = "libriScolasticiClienti_showDettaglioOrdine.action";
		requestMap.put("numeroOrdine", idNumeroOrdine);
		return SUCCESS;
	}
	
	
	
	
	/* -- */
	@SkipValidation
	public String confermaOrdine() throws Exception {
		requestMap.put("tableTitle", getText("DETTAGLIO ORDINE"));
		resultMap = new LinkedHashMap<String, String>();
		
		if(idNumeroOrdine!=null){
			
			OrdineLibriVo ordine = scolasticaService.getDettaglioOrdineLibri(new Long(idNumeroOrdine));
			if(ordine.getListDettaglioOrdine().size()<=0){
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("dpe.ordine.msg.carrello.vuoto") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
			
			
			/*WS REST PER CONFERMA ORDINE*/
			
			ConfermaOrdine confermaOrdine = new ConfermaOrdine();
			confermaOrdine.setDocument("RichiestaOrdine");
			confermaOrdine.setCodiceCliente(ordine.getCodCliente());
			confermaOrdine.setVersionJson("1");
			
			AnagraficaDto anagrafica = new AnagraficaDto();
			anagrafica.setNome((ordine.getCliente().getNome()!=null)?ordine.getCliente().getNome():"");
			anagrafica.setCognome((ordine.getCliente().getCognome()!=null)?ordine.getCliente().getCognome():"");
			//Vittorio 21/08/15
			//anagrafica.setContatti(new ContattiDto((ordine.getCliente().getEmail()!=null)?ordine.getCliente().getEmail():"",(ordine.getCliente().getTelefono()!=null)?ordine.getCliente().getTelefono():""));
			anagrafica.setContatti(new ContattiDto("",""));
			confermaOrdine.setAnagrafica(anagrafica);
			
			
			OrdineDto ordinedto = new OrdineDto();
			ordinedto.setOrdineExt(ordine.getNumeroOrdine().toString());           //Codice ordine interno per riferimento esterno
			//Vittorio 17/08/15
			//ordinedto.setPdvDestinazione(ordine.getCodDpeWebEdicola().toString()); //Punto vendita di destinazione
			ordinedto.setPdvDestinazione(getAuthUser().getCodEdicolaDl().toString());
			
			List<InformazioniDto> informazioni = new ArrayList<InformazioniDto>();
			InformazioniDto info = new InformazioniDto("indirizzoDiConsegna",(getAuthUser().getIndirizzoEdicolaPrimaRiga()!=null)?getAuthUser().getIndirizzoEdicolaPrimaRiga():"");
			informazioni.add(info);
			InformazioniDto info2 = new InformazioniDto("indirizzoDiConsegna","01/01/2001");
			informazioni.add(info2);
			
			List<ElencoLibriDto> elencoLibri = new ArrayList<ElencoLibriDto>();
			
			for(OrdineLibriDettVo ind: ordine.getListDettaglioOrdine()){
				ElencoLibriDto newLibro = new ElencoLibriDto();
				newLibro.setSku(ind.getSku().toString());
				if(ind.getFlagCopertina()==1){
					List<ParametriDto> parametri = new ArrayList<ParametriDto>();
					
					ParametriDto copertina = new ParametriDto();
					copertina.setChiave("copertina");
					copertina.setValore("true");
					parametri.add(copertina);
					
					ParametriDto idLogoCopertina = new ParametriDto();
					idLogoCopertina.setChiave("idLogoCopertina");
					idLogoCopertina.setValore(ind.getIdLogoCopertina().toString());
					parametri.add(idLogoCopertina);
					
					ParametriDto testoCopertina1 = new ParametriDto();
					testoCopertina1.setChiave("testoCopertina1");
					testoCopertina1.setValore((ind.getPrimaRigaCopertina()!=null && !ind.getPrimaRigaCopertina().equals(""))?ind.getPrimaRigaCopertina():"");
					parametri.add(testoCopertina1);
					
					ParametriDto testoCopertina2 = new ParametriDto();
					testoCopertina2.setChiave("testoCopertina2");
					testoCopertina2.setValore((ind.getSecondaRigaCopertina()!=null && !ind.getSecondaRigaCopertina().equals(""))?ind.getSecondaRigaCopertina():"");
					parametri.add(testoCopertina2);
					
					ParametriDto testoCopertina3 = new ParametriDto();
					testoCopertina3.setChiave("testoCopertina3");
					testoCopertina3.setValore((ind.getTerzaRigaCopertina()!=null && !ind.getTerzaRigaCopertina().equals(""))?ind.getTerzaRigaCopertina():"");
					parametri.add(testoCopertina3);
					
					newLibro.setParametri(parametri);
				}
				
				elencoLibri.add(newLibro);
			}
			ordinedto.setInformazioni(informazioni);
			confermaOrdine.setElencoLibri(elencoLibri);
			
			confermaOrdine.setOrdine(ordinedto);
			
			GsonBuilder builder = new GsonBuilder();
	        Gson gson = builder.create();
	        String jsonConfermaOrdine = gson.toJson(confermaOrdine);
	        RicercaLibriTestoDto ric = new RicercaLibriTestoDto();
	        ric.setJsonPutOrdine(jsonConfermaOrdine);
	        
	        
	        //POST ORDINE - RETURN NUMERO_ORDINE_TXT
	        ResponseEntity<RisultatoRicercaLibriDto> response = libriScolasticiRestService.putEntity(ric);
			if(response!=null){
				ordine.setDataChiusuraOrdine(new Date());
				ordine.setNumeroOrdineTxt(response.getBody().getOrdineTXT());
				scolasticaService.confermaOrdine(ordine);
			}	
			
			
			//Modifica del 16/09/2015
			//Eseguire il tracking dopo la conferma dell'ordine per poter registrare per ogni singolo testo il proprio keynum
			List<TrackingElencoLibriDto> listElencoLibri = null;
			List<OrdineLibriDettVo> listLibriTrackingResult = new ArrayList<OrdineLibriDettVo>();
			TrackingDto trackingDto;
			RicercaLibriTestoDto ricParams = new RicercaLibriTestoDto();
			ricParams.setNumOrdine(ordine.getNumeroOrdine().toString());
			ricParams.setNumOrdineTxt(ordine.getNumeroOrdineTxt());
			
			ResponseEntity<TrackingDto> res = libriScolasticiTrackingOrdiniRestService.getEntity(ricParams);
			if(res!=null && res.getBody()!=null && res.getStatusCode() == HttpStatus.OK){
				trackingDto = res.getBody();
				listElencoLibri = new ArrayList(Arrays.asList(res.getBody().getElencolibri()));
				if(trackingDto!=null && listElencoLibri!=null){
					for(TrackingElencoLibriDto iter :listElencoLibri){
						
						OrdineLibriDettVo dettLibro = scolasticaService.getDettaglioLibroNotKeyNum(ordine.getNumeroOrdine(),iter.getSku());
						dettLibro.setKeynum(new Long(iter.getKeynum()));
						scolasticaService.saveBaseVo(dettLibro);
												
					}
				}
			}
			
			
			
			
			resultMap.put("result", MessageFormat.format(getText("igeriv.ritiro.cliente.cancellato"), nome));
		}
			
		return SUCCESS;
	}
	
	/* -- */
	@SkipValidation
	public String confermaOrdineRiepilogo() throws Exception {
		requestMap.put("tableTitle", getText("RIEPILOGO ORDINE"));
		resultMap = new LinkedHashMap<String, String>();
		
		if(idNumeroOrdine!=null){
			
			OrdineLibriVo ordine = scolasticaService.getDettaglioOrdineLibri(new Long(idNumeroOrdine));
			if(ordine!=null){
				BigDecimal sumTotPrezzoLibriInOrdine = scolasticaService.sumPrezzoLibriCarrello(new Long(idNumeroOrdine));
				
				DecimalFormat df = new DecimalFormat("#0.00");
				String prezzoFormat = df.format(sumTotPrezzoLibriInOrdine)+" &euro; " ;
				
				Long countLibriOrdine = scolasticaService.countLibriCarrello(new Long(idNumeroOrdine));
				requestMap.put("infoordine", ordine);
				requestMap.put("dettaglioordine", ordine.getListDettaglioOrdine());
				requestMap.put("totaleDovuto", prezzoFormat);
				requestMap.put("countLibriOrdine", countLibriOrdine);
			}
			ClienteEdicolaVo cliente = clientiScolasticaService.getClienteEdicola(getAuthUser().getArrId(), ordine.getCodCliente());
			if(cliente!=null)
				requestMap.put("anagraficacliente", cliente);
			
			
			
			resultMap.put("result", MessageFormat.format(getText("igeriv.ritiro.cliente.cancellato"), nome));
		}
			
		return IGerivConstants.ACTION_VIEW_DETTAGLIO_ORDINE_RIEPILOGO;
	}
	
	/* -- */
	@SkipValidation
	public String chiudiConfermaOrdineRiepilogo() throws Exception {
		requestMap.put("tableTitle", getText("DETTAGLIO ORDINE"));
		resultMap = new LinkedHashMap<String, String>();
		
		if(idNumeroOrdine!=null){
			resultMap.put("result", MessageFormat.format(getText("igeriv.ritiro.cliente.cancellato"), nome));
		}
			
		return SUCCESS;
	}
	
	
	
	
	
	
	
//	@BreadCrumb("%{crumbNameInsPreCli}")
//	@SkipValidation
//	public String showFilterPrenotazioni() throws Exception {
//		requestMap.put("tableTitle", getText("igeriv.inserisci.prenotazioni.clienti"));
//		actionName = "libriScolasticiClienti_showClientiPrenotazioni.action";
//		return SUCCESS;
//	}
//	
//	@BreadCrumb("%{crumbNameViewPreCli}")
//	@SkipValidation
//	public String showFilterViewPrenotazioni() throws Exception {
//		requestMap.put("tableTitle", getText("igeriv.visualizza.clienti.con.prenotazioni"));
//		actionName = "libriScolasticiClienti_showClientiViewPrenotazioni.action";
//		return SUCCESS;
//	}
//	
//	@BreadCrumb("%{crumbNameEvPreCli}")
//	@SkipValidation
//	public String showFilterEvasionePrenotazioni() {
//		requestMap.put("tableTitle", getText("igeriv.evasione.prenotazioni.clienti"));
//		actionName = "libriScolasticiClienti_showClientiEvasionePrenotazioni.action";
//		return SUCCESS;
//	}
//	
//	@BreadCrumb("%{crumbNameRepEC}")
//	@SkipValidation
//	public String showFilterEstrattoContoClienti() throws Exception {
//		requestMap.put("tableTitle", getText("igeriv.report.estratto.conto.clienti"));
//		actionName = "libriScolasticiClienti_showClientiEstrattoConto.action";
//		return SUCCESS;
//	}
//	
//	@BreadCrumb("%{crumbNameInsPreCli}")
//	@SkipValidation
//	public String showClientiPrenotazioni() {
//		showClienti();
//		tableTitle = getText("igeriv.inserisci.prenotazioni.clienti");
//		actionName = "libriScolasticiClienti_showClientiPrenotazioni.action";
//		requestMap.put("tableTitle", getText("igeriv.inserisci.prenotazioni.clienti"));
//		return SUCCESS;
//	}
//	
//	@BreadCrumb("%{crumbNameViewCliConPre}")
//	@SkipValidation
//	public String showClientiViewPrenotazioni() {
//		if (!Strings.isNullOrEmpty(prezzo)) {
//			int commaPos = prezzo.lastIndexOf(",");
//			int dotPos = prezzo.lastIndexOf(".");
//			if (dotPos > commaPos) {
//				prezzo = prezzo.replaceAll(",", "");
//			} else if (commaPos > dotPos) {
//				prezzo = prezzo.replaceAll("\\.", "");
//				if (prezzo.contains(",")) {
//					prezzo = prezzo.replace(',', '.');
//				}
//			}
//		}
//		showClienti();
//		requestMap.put("tableTitle", getText("igeriv.visualizza.clienti.con.prenotazioni"));
//		actionName = "libriScolasticiClienti_showClientiViewPrenotazioni.action";
//		return "successViewPrenotazioni";
//	}
	
//	@BreadCrumb("%{crumbNameEvPreCli}")
//	@SkipValidation
//	public String showClientiEvasionePrenotazioni() {
//		clienti = clientiScolasticaService.getClientiConEvasione(getAuthUser().getArrId(), nome, cognome, codiceFiscale, piva, false);
//		clienti = select(clienti, having(on(ClienteEdicolaDto.class).getQtaOrdiniDaEvadere(), greaterThan(0l)));
//		requestMap.put("clienti", clienti);
//		requestMap.put("tableTitle", getText("igeriv.evasione.prenotazioni.clienti"));
//		actionName = "libriScolasticiClienti_showClientiEvasionePrenotazioni.action";
//		return "successEvasionePrenotazioni";
//	}
//	
//	@BreadCrumb("%{crumbNameRepEC}")
//	@SkipValidation
//	public String showClientiEstrattoConto() {
//		showClienti();
//		tableTitle = getText("igeriv.report.estratto.conto.clienti");
//		requestMap.put("tableTitle", getText("igeriv.report.estratto.conto.clienti"));
//		return "successReportEstrattoContoClienti";
//	}
	
	
	
//	@BreadCrumb("%{crumbNameConfermaEvaCli}")
//	@SkipValidation
//	public String viewEvasionePrenotazioniClientiEdicola() throws ParseException { 
//		requestMap.put("tipoRifornimento", "evasione");
//		filterTitle = getText("igeriv.visualizza.evsaione.cliente") + " <b>" + ((nomeCliente != null) ? nomeCliente.toUpperCase() : "") + "</b>";
//		actionName = "libriScolasticiClienti_viewEvasionePrenotazioniClientiEdicola.action";
//		List<Long> listCodClienti = new ArrayList<Long>();
//		if (!Strings.isNullOrEmpty(idCliente)) {
//			listCodClienti.add(new Long(idCliente));
//		} else if (!Strings.isNullOrEmpty(spunte)) {
//			filterTitle = getText("igeriv.visualizza.evsaione.clienti");
//			String[] split = spunte.split(",");
//			for (String codCliente : split) {
//				if (!Strings.isNullOrEmpty(codCliente)) {
//					listCodClienti.add(new Long(codCliente.trim()));
//				}
//			}
//		}
//		if (!listCodClienti.isEmpty()) {
//			Timestamp dataDa = Strings.isNullOrEmpty(this.dataDa) ? null : DateUtilities.floorDay(DateUtilities.parseDate(this.dataDa, DateUtilities.FORMATO_DATA_SLASH));
//			Timestamp dataA = Strings.isNullOrEmpty(this.dataA) ? null : DateUtilities.ceilDay(DateUtilities.parseDate(this.dataA, DateUtilities.FORMATO_DATA_SLASH));
//			List<RichiestaClienteDto> listRichieste = rifornimentiService.getRichiesteClienteByIdClienteViewOnly(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), listCodClienti, titolo, "" + IGerivConstants.PRENOTAZIONI_INSERITE_O_PARZIALMENTE_EVASE, null, dataDa, dataA, true);
//			List<RisposteClientiCodificateVo> rpc = (List<RisposteClientiCodificateVo>) context.getAttribute("risposteClientiCodificate");
//			int count = 0;
//			for (RisposteClientiCodificateVo vo : rpc) {
//				if (count > 0 && count < rpc.size()) {
//					risposteClientiSelectKeys += ",";
//					risposteClientiSelectValues += ",";
//				}
//				risposteClientiSelectKeys += vo.getCodRisposta();
//				risposteClientiSelectValues += vo.getDescrizioneSintetica();
//				count++;
//			}
//			requestMap.put("richiesteRifornimento", listRichieste);
//			requestMap.put("emailValido", getAuthUser().isEmailValido());
//			requestMap.put("emailClienteValido", ((!Strings.isNullOrEmpty(idCliente) && listRichieste != null && !listRichieste.isEmpty() && !Strings.isNullOrEmpty(listRichieste.get(0).getEmail())) || !Strings.isNullOrEmpty(spunte)) ? true : false);
//			requestMap.put("prenotazioneEvasioneQuantitaEvasaEmpty", getAuthUser().getPrenotazioniEvasioneQuantitaEvasaEmpty());
//		} 
//		return IGerivConstants.ACTION_EVASIONE_PRENOTAZIONI_CLIENTI_EDICOLA;
//	}
	
//	@BreadCrumb("%{crumbNamePreCliEvase}")
//	@SkipValidation
//	public String saveEvasionePrenotazioniClientiEdicola() {
//		try {
//			if (pk != null) {
//				saveEvasione();
//				showResultEvasionePrenotazioniClientiEdicola();
//			} else {
//				return viewEvasionePrenotazioniClientiEdicola();
//			}
//			requestMap.put("stampaRicevuta", getText("tooltip.main_frame.Print"));
//		} catch (IGerivRuntimeException e) {
//			throw e;
//		} catch (Throwable e) {
//			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
//			throw new IGerivRuntimeException();
//		}
//		return IGerivConstants.ACTION_RESULT_EVASIONE_PRENOTAZIONI_CLIENTI_EDICOLA;
//	}

	/**
	 * @throws ParseException
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
//	public String saveEvasione() throws ParseException, MessagingException, UnsupportedEncodingException {
//		evasioneJsonResult = new HashMap<String, Object>();
//		try {
//			List<Long> listCodClienti = new ArrayList<Long>();
//			if (!Strings.isNullOrEmpty(idCliente)) {
//				listCodClienti.add(new Long(idCliente));
//			} else if (!Strings.isNullOrEmpty(spunte)) {
//				String[] split = spunte.split(",");
//				for (String codCliente : split) {
//					if (!Strings.isNullOrEmpty(codCliente)) {
//						listCodClienti.add(new Long(codCliente.trim()));
//					}
//				}
//			}
//			Timestamp dataCompEC = Strings.isNullOrEmpty(this.dataCompEC) ? null : DateUtilities.floorDay(DateUtilities.parseDate(this.dataCompEC, DateUtilities.FORMATO_DATA_SLASH)); 
//			List<ClienteEdicolaVo> clienteEdicola = clientiScolasticaService.getClientiEdicola(getAuthUser().getArrId(), listCodClienti);
//			Map<Long, String> mapMailClienti = (!Strings.isNullOrEmpty(inviaEmail) && Boolean.parseBoolean(inviaEmail)) ? buildMapEmailClienti(clienteEdicola) : null;
//			Map<String, ParametriEdicolaDto> mapParam = (Map<String, ParametriEdicolaDto>) sessionMap.get(IGerivConstants.SESSION_VAR_MAP_PARAMETRI_EDICOLA);
//			ParametriEdicolaDto paramSaveEvasioneVendite = mapParam.containsKey(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_INCLUDI_EVASIONE_PRENOTAZIONE_VENIDTE) ? mapParam.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_INCLUDI_EVASIONE_PRENOTAZIONE_VENIDTE) : null;
//			Boolean saveEvasionePrenotazioneVendite = !Strings.isNullOrEmpty(paramSaveEvasioneVendite.getParamValue()) && Boolean.parseBoolean(paramSaveEvasioneVendite.getParamValue()) ? true : false;
//			Map<Long, List<RichiestaClienteDto>> mapEvasione = rifornimentiService.buildMapEvasione(pk, evaso, qtaDaEvadere, ultimaRisposta, messLibero);
//			Long idConto = rifornimentiService.saveEvasionePrenotazioniClientiEdicola(getAuthUser().getCodUtente(), dataCompEC, saveEvasionePrenotazioneVendite, mapEvasione);
//			for (Map.Entry<Long, List<RichiestaClienteDto>> entry : mapEvasione.entrySet()) {
//				Long codCliente = entry.getKey();
//				List<RichiestaClienteDto> values = entry.getValue();
//				List<Map<String, String>> emailParams = new ArrayList<Map<String, String>>();
//				for (RichiestaClienteDto dto : values) {
//					Map<String, String> map = new HashMap<String, String>();
//					PubblicazioneDto copertina = pubblicazioniService.getCopertinaByIdtn(dto.getCodDl(), dto.getIdtn());
//					map.put("codDl", dto.getCodDl().toString());
//					map.put("idtn", dto.getIdtn().toString());
//					map.put("titolo", copertina.getTitolo());
//					map.put("quantitaEvasa", dto.getQuantitaEvasa().toString());
//					map.put("ultRisposta", dto.getUltRisposta().toString());
//					map.put("mLibero", dto.getMessagioLibero());
//					emailParams.add(map);
//				}
//				String email = (mapMailClienti != null && !mapMailClienti.isEmpty()) ? mapMailClienti.get(codCliente) : null;
//				if (mapMailClienti != null && !mapMailClienti.isEmpty() && !Strings.isNullOrEmpty(email)) {
//					sendEmailToLettore(email, getAuthUser().getRagioneSocialeEdicola(), emailParams, getAuthUser().getCodFiegDl().equals(IGerivConstants.CDL_CODE), getAuthUser().getEmail());
//				}
//			}
//			this.idConto = idConto;
//			evasioneJsonResult.put("success", "true");
//		} catch (Throwable e) {
//			evasioneJsonResult.put("error", getText("gp.errore.imprevisto"));
//			throw e;
//		}
//		return SUCCESS;
//	}
		
	/**
	 * Invia un email al lettore informando lo stato del proprio ordine.
	 * @param emailEdicola 
	 * 
	 * @param String email
	 * @param String ragSoc
	 * @param List<Map<String, String>> emailParams
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException 
	 */
//	private void sendEmailToLettore(String email, String ragSoc, List<Map<String, String>> emailParams, Boolean isCDL, String emailEdicola) throws MessagingException, UnsupportedEncodingException {
//		String subject = IGerivMessageBundle.get("msg.email.evasione.ordine.subject");
//		if (isCDL) {
//			subject = subject.replaceAll("iGeriv", IGerivMessageBundle.get("igeriv.cdl"));
//		}
//		StringBuffer msg = new StringBuffer(IGerivMessageBundle.get("msg.email.gent.cliente"));
//		for (Map<String, String> map : emailParams) {
//			msg.append("<br><br>");
//			Integer ultRisposta = map.get("ultRisposta") != null ? new Integer(map.get("ultRisposta").toString()) : 0;
//			Integer quantitaEvasa = map.get("quantitaEvasa") != null ? new Integer(map.get("quantitaEvasa").toString()) : 0;
//			String mLibero = map.get("mLibero") != null ? map.get("mLibero").toString() : "";
//			String titolo = map.get("titolo") != null ? map.get("titolo").toString() : "";
//			if (ultRisposta.equals(IGerivConstants.COD_MESSAGGIO_CODIFICATO_PUBBLICAZIONE_ESAURITA)) {
//				String key = IGerivMessageBundle.get("msg.email.evasione.ordine.non.disponibile");
//				if (mLibero != null && !mLibero.equals("")) {
//					key = IGerivMessageBundle.get("msg.email.evasione.ordine.non.disponibile.descrizione");
//					msg.append(MessageFormat.format(key, titolo, ragSoc, mLibero));
//				} else { 
//					msg.append(MessageFormat.format(key, titolo, ragSoc));
//				}
//			} else if (ultRisposta.equals(IGerivConstants.COD_MESSAGGIO_CODIFICATO_ATTENDO_RIFORNIMENTO_AGENZIA)) {
//				String key = (quantitaEvasa > 0) ? IGerivMessageBundle.get("msg.email.evasione.parziale.ordine.attesa.rifo") : IGerivMessageBundle.get("msg.email.evasione.ordine.attesa.rifo");
//				if (mLibero != null && !mLibero.equals("")) {
//					key = (quantitaEvasa > 0) ? IGerivMessageBundle.get("msg.email.evasione.parziale.ordine.attesa.rifo.descrizione") : IGerivMessageBundle.get("msg.email.evasione.ordine.attesa.rifo.descrizione");
//					msg.append((quantitaEvasa > 0) ? MessageFormat.format(key, titolo, ragSoc, quantitaEvasa, mLibero) : MessageFormat.format(key, titolo, ragSoc, mLibero));
//				} else {
//					msg.append((quantitaEvasa > 0) ? MessageFormat.format(key, titolo, ragSoc, quantitaEvasa) : MessageFormat.format(key, titolo, ragSoc));
//				}
//			} else if (ultRisposta.equals(IGerivConstants.COD_MESSAGGIO_CODIFICATO_ARRETRATO)) {
//				String key = IGerivMessageBundle.get("msg.email.evasione.ordine.arretrato");
//				if (mLibero != null && !mLibero.equals("")) {
//					key = IGerivMessageBundle.get("msg.email.evasione.ordine.arretrato.descrizione");
//					msg.append(MessageFormat.format(key, titolo, ragSoc, mLibero));
//				} else {
//					msg.append(MessageFormat.format(key, titolo, ragSoc));
//				}
//				msg = new StringBuffer(MessageFormat.format(key, titolo, ragSoc, mLibero));
//			} else if (ultRisposta.equals(IGerivConstants.COD_MESSAGGIO_CODIFICATO_RIFIUTATO_CLIENTE)) {
//				String key = IGerivMessageBundle.get("msg.email.evasione.ordine.rifiutato");
//				if (mLibero != null && !mLibero.equals("")) {
//					key = IGerivMessageBundle.get("msg.email.evasione.ordine.rifiutato.descrizione");
//					msg.append(MessageFormat.format(key, titolo, ragSoc, mLibero));
//				} else {
//					msg.append(MessageFormat.format(key, titolo, ragSoc));
//				}
//				msg = new StringBuffer(MessageFormat.format(key, titolo, ragSoc, mLibero));
//			} else {
//				String key = IGerivMessageBundle.get("msg.email.evasione.ordine");
//				if (mLibero != null && !mLibero.equals("")) {
//					key += "<br>Descrizione: {3}";
//					msg.append(MessageFormat.format(key, titolo, ragSoc, quantitaEvasa, mLibero));
//				} else {
//					msg.append(MessageFormat.format(key, titolo, ragSoc, quantitaEvasa));
//				}
//			}
//		} 
//		mailingListService.sendEmailWithAttachment(new String[]{email}, subject, msg.toString(), null, true, emailEdicola, false, getAuthUser().getRagioneSocialeEdicola(), null, getAuthUser().getRagioneSocialeEdicola());
//	}
	
	/**
	 * @param clienteEdicola
	 * @return
	 */
	private Map<Long, String> buildMapEmailClienti(List<ClienteEdicolaVo> clienteEdicola) {
		Map<Long, String> map = new HashMap<Long, String>();
		for (ClienteEdicolaVo cevo : clienteEdicola) {
			if (!Strings.isNullOrEmpty(cevo.getEmail())) {
				map.put(cevo.getCodCliente(), cevo.getEmail());
			}
		}
		return map;
	}

//	@BreadCrumb("%{crumbNamePreCliEvase}")
//	@SkipValidation
//	public String showResultEvasionePrenotazioniClientiEdicola() {
//		List<RichiestaClienteDto> listRichieste = rifornimentiService.getRichiesteClienteByPk(pk);
//		String[] arrPk = pk.split(",");
//		String[] arrQtaEvasa = evaso.split(",");
//		for (int  i = 0; i < arrPk.length; i++) {
//			String pkValue = arrPk[i].trim();
//			String qtaEv = arrQtaEvasa[i].trim();
//			for (RichiestaClienteDto dto : listRichieste) {
//				if (dto.getPk().toString().equals(pkValue)) {
//					dto.setQuantitaConsegnare((qtaEv != null && !qtaEv.equals("")) ? new Integer(qtaEv) : 0);
//					break;
//				}
//			}
//		}
//		listRichieste = select(listRichieste, having(on(RichiestaClienteDto.class).getQuantitaConsegnare(), greaterThanOrEqualTo(0)));
//		requestMap.put("richiesteRifornimento", listRichieste);
//		if (!Strings.isNullOrEmpty(idCliente)) {
//			ClienteEdicolaVo clienteEdicola = clientiScolasticaService.getClienteEdicola(getAuthUser().getArrId(), new Long(idCliente));
//			String consegnaPrenotazioni = MessageFormat.format(getText("igeriv.consegna.prenotazioni"), DateUtilities.getTimestampAsString(new Date(), DateUtilities.FORMATO_DATA_SLASH)); 
//			String cliente	= nomeCliente.toUpperCase().replaceAll("&nbsp;", " ").replaceAll("\\.", " ");
//			String indirizzoCliente = (clienteEdicola.getIndirizzo() != null) ? clienteEdicola.getIndirizzo().replaceAll("&nbsp;", " ").replaceAll("\\.", " ") : "-" ; 
//			String cittaCliente = (clienteEdicola.getLocalitaDesc() != null) ? clienteEdicola.getLocalitaDesc().replaceAll("&nbsp;", " ").replaceAll("\\.", " ") : "-" ; 
//			String codEdicola = "" + getAuthUser().getCodEdicolaDl();
//			String ragSocEdicola = getAuthUser().getRagioneSocialeEdicola().replaceAll("&nbsp;", " ").replaceAll("\\.", " "); 
//			String indirizzoEdicola = getAuthUser().getIndirizzoEdicolaPrimaRiga().replaceAll("&nbsp;", " ").replaceAll("\\.", " ");
//			String cittaEdicola = getAuthUser().getLocalitaEdicolaPrimaRiga().replaceAll("&nbsp;", " ").replaceAll("\\.", " ");
//			requestMap.put("consegnaPrenotazioni", consegnaPrenotazioni);
//			requestMap.put("codEdicola", codEdicola);
//			requestMap.put("ragSocEdicola", ragSocEdicola);
//			requestMap.put("indirizzoEdicola", indirizzoEdicola);
//			requestMap.put("cittaEdicola", cittaEdicola);
//			requestMap.put("cliente", getText("igeriv.provenienza.evasione.cliente"));
//			requestMap.put("ragSocCliente", cliente);
//			requestMap.put("indirizzoCliente", indirizzoCliente);
//			requestMap.put("cittaCliente", cittaCliente);
//			requestMap.put("nomeCliente", clienteEdicola.getNome() + " " + clienteEdicola.getCognome());
//		} 
//		return IGerivConstants.ACTION_RESULT_EVASIONE_PRENOTAZIONI_CLIENTI_EDICOLA;
//	}
//	
	public String saveClienteScolastica() throws Exception {
		
		if (clienteGestioneScolastica != null) {
				//Controllo se ci sono ordini attivi per il cliente
				List<OrdineLibriVo> listOrdineLibri = scolasticaService.getListOrdiniCliente(getAuthUser().getCodFiegDl(),clienteGestioneScolastica.getCodEdicola(),clienteGestioneScolastica.getCodCliente());
				if(!listOrdineLibri.isEmpty()){
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("msg.errore.modifica.cliente.presenza.ordini") + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				}else{
						try {
							prepareClienteVo();
							String randomPwd = null;
							if (clienteGestioneScolastica_inviaEmail != null && clienteGestioneScolastica_inviaEmail.equals("true")) {
								List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
								authList.add(new GrantedAuthorityImpl(IGerivConstants.ROLE_IGERIV_CLIENTE_EDICOLA));
								randomPwd = StringUtility.getRandomString(14);
								String pass = "" + ((clienteGestioneScolastica.getCodCliente() + clienteGestioneScolastica.getCodCliente()) * IGerivConstants.ENCODE_FACTOR);
								String pwd = passwordEncoder.encodePassword(randomPwd, pass);
								clienteGestioneScolastica.setPassword(pwd);
								clienteGestioneScolastica.setPwdCriptata(1);
								clienteGestioneScolastica.setChangePassword(1);
							} 		
							if (clienteGestioneScolastica.getDtSospensionePrenotazioniDa() != null) {
								clienteGestioneScolastica.setDtSospensionePrenotazioniDa(DateUtilities.floorDay(clienteGestioneScolastica.getDtSospensionePrenotazioniDa()));
							}
							if (clienteGestioneScolastica.getDtSospensionePrenotazioniA() != null) {
								clienteGestioneScolastica.setDtSospensionePrenotazioniA(DateUtilities.ceilDay(clienteGestioneScolastica.getDtSospensionePrenotazioniA()));
							}
							clienteGestioneScolastica.setNome(!Strings.isNullOrEmpty(clienteGestioneScolastica.getNome()) ? clienteGestioneScolastica.getNome().toUpperCase() : clienteGestioneScolastica.getNome());
							clienteGestioneScolastica.setCognome(!Strings.isNullOrEmpty(clienteGestioneScolastica.getCognome()) ? clienteGestioneScolastica.getCognome().toUpperCase() : clienteGestioneScolastica.getCognome());
							clientiScolasticaService.saveBaseVo(clienteGestioneScolastica);
							
							
							if (clienteGestioneScolastica.getEmail() != null && !clienteGestioneScolastica.getEmail().equals("") && randomPwd != null && !randomPwd.equals("")) {
								String message = MessageFormat.format(getText("msg.email.nuovo.account.cliente.edicola"), getAuthUser().getRagioneSocialeEdicola(), clienteGestioneScolastica.getCodCliente().toString().replaceAll("\\.", ""), randomPwd, igerivUrl);
								if (getAuthUser().getCodFiegDl().equals(IGerivConstants.CDL_CODE)) {
									//CDL BOLOGNA
									mailingListService.sendEmail(clienteGestioneScolastica.getEmail(), getText("msg.subject.nuovo.account.cdl.online"), message.replaceAll("iGeriv", getText("igeriv.cdl")));
								} else if (getAuthUser().getCodFiegDl().equals(IGerivConstants.CHIMINELLI_CODE_1) || getAuthUser().getCodFiegDl().equals(IGerivConstants.CHIMINELLI_CODE_2)) {
									//CHIMINELLI AMAZON
									message = MessageFormat.format(getText("msg.email.nuovo.account.chiminelli.cliente.edicola"), getAuthUser().getRagioneSocialeEdicola(), clienteGestioneScolastica.getCodCliente().toString().replaceAll("\\.", ""), randomPwd, "http://www.ilchiosco.it/igeriv-client/");
									mailingListService.sendEmail(clienteGestioneScolastica.getEmail(), getText("msg.subject.nuovo.account.chiminelli.online"), message);
								
								} else if (getAuthUser().getCodFiegDl().equals(IGerivConstants.MENTA_CODE)) {
									//CLESP - Nuovo utente presso CLESP 20/04/2018
									ClespNuovoConsumatoreDto dtoNuovoCliente = new ClespNuovoConsumatoreDto();
									dtoNuovoCliente.nome = (clienteGestioneScolastica.getNome()!=null && !clienteGestioneScolastica.getNome().equals(""))?clienteGestioneScolastica.getNome().toUpperCase():"";
									dtoNuovoCliente.cognome=(clienteGestioneScolastica.getCognome()!=null && !clienteGestioneScolastica.getCognome().equals(""))?clienteGestioneScolastica.getCognome().toUpperCase():"";
									dtoNuovoCliente.classe="";
									dtoNuovoCliente.email="";
									dtoNuovoCliente.cellulare="";
									dtoNuovoCliente.codice_pdc_menta = getAuthUser().getCodEdicolaDl().toString();
									dtoNuovoCliente.codice_consumatore_menta= clienteGestioneScolastica.getCodCliente().intValue();
									dtoNuovoCliente.cod_fiscale="";
									dtoNuovoCliente.socio="";						
									ResponseEntity<ClespRisultatoNuovoConsumatoreDto> response = clespConsumatoreService.putEntity(dtoNuovoCliente);
									//ALL
									mailingListService.sendEmail(clienteGestioneScolastica.getEmail(), getText("msg.subject.nuovo.account.cliente.edicola"), message);
			
								} else {
									//ALL
									mailingListService.sendEmail(clienteGestioneScolastica.getEmail(), getText("msg.subject.nuovo.account.cliente.edicola"), message);
									}
								}
							
						} catch (Throwable e) {
							requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
							throw new IGerivRuntimeException();
						}
		
			}
		
		}
		return SUCCESS;
	}
	
	@SkipValidation
	public String deleteClienteScolastica() throws Exception {
		if (clienteGestioneScolastica != null
				&& clienteGestioneScolastica.getCodCliente() != null 
				&& clienteGestioneScolastica.getCodEdicola() != null) {
			
			//CONTROLLO ESISTENZA ORDINE LIBRI
			List<OrdineLibriVo> listOrdineLibri = scolasticaService.getListOrdiniCliente(getAuthUser().getCodFiegDl(),clienteGestioneScolastica.getCodEdicola(),clienteGestioneScolastica.getCodCliente());
			if(listOrdineLibri.isEmpty()){
				prepareClienteVo();
				try {
					clientiScolasticaService.deleteCliente(clienteGestioneScolastica.getCodCliente());
				} catch (Exception e) {
					if ((e.getCause() != null && e.getCause() instanceof SQLIntegrityConstraintViolationException) || 
							(e.getCause() != null && e.getCause().getCause() != null && e.getCause().getCause() instanceof org.hibernate.exception.ConstraintViolationException)) {
						try {
							clientiScolasticaService.deleteClienteWithDependencies(clienteGestioneScolastica.getCodCliente(), getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId());
						} catch (Exception e1) {
							requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("igeriv.errore.esecuzione.procedura") + IGerivConstants.END_EXCEPTION_TAG);
							throw new IGerivRuntimeException();
						}
					} else {
						requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("igeriv.errore.esecuzione.procedura") + IGerivConstants.END_EXCEPTION_TAG);
						throw new IGerivRuntimeException();
					}
				}
				
			}else{
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("msg.errore.delete.cliente.presenza.ordini") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
			
		}
		return SUCCESS;
	}
	
	@Override
	public String getTitle() {
		String title = getText("igeriv.inserisci.aggiorna.scolastica.clienti");
		if (!Strings.isNullOrEmpty(getActionName())) {
			if (getActionName().contains("showFilterPrenotazioni") || getActionName().contains("showClientiPrenotazioni")) {
				title = getText("igeriv.inserisci.prenotazioni.clienti");
			} else if (getActionName().contains("EvasionePrenotazioni")) {
				title = getText("igeriv.evasione.prenotazioni.clienti");
			} else if (getActionName().contains("ViewPrenotazioni")) {
				title = getText("igeriv.visualizza.prenotazioni.clienti");
			}
		}
		return super.getTitle() + title;
	}
	
	public List<TipoLocalitaVo> getTipiLocalita	() {
		return tipiLocalita = (List<TipoLocalitaVo>) context.getAttribute("tipiLocalita");
	}

	public List<ProvinciaVo> getProvince() {
		return province = (List<ProvinciaVo>) context.getAttribute("province");
	}

	private void prepareClienteVo() {
		if (clienteGestioneScolastica.getProvincia() != null && clienteGestioneScolastica.getProvincia().getCodProvincia() == null) {
			clienteGestioneScolastica.setProvincia(null);
		}
		if (clienteGestioneScolastica.getTipoLocalita() != null && clienteGestioneScolastica.getTipoLocalita().getTipoLocalita() == null) {
			clienteGestioneScolastica.setTipoLocalita(null);
		}
		if (clienteGestioneScolastica.getLocalita() != null && clienteGestioneScolastica.getLocalita().getCodLocalita() == null) {
			clienteGestioneScolastica.setLocalita(null);
		}
		if (clienteGestioneScolastica.getPaese() != null && clienteGestioneScolastica.getPaese().getCodPaese() == null) {
			clienteGestioneScolastica.setPaese(null);
		}
		if (clienteGestioneScolastica.getTipoPagamento() != null && clienteGestioneScolastica.getTipoPagamento().getCodMetodoPagamento() == null) {
			clienteGestioneScolastica.setTipoPagamento(null);
		}
		if (clienteGestioneScolastica.getBanca() != null && clienteGestioneScolastica.getBanca().getCodBanca() == null) {
			clienteGestioneScolastica.setBanca(null);
		}
	}

	@Override
	public void saveParameters(Context context, String tableId, Map parameterMap) {
		
	}

	@Override
	public Map getParameters(Context context, String tableId, String stateAttr) {
		return null;
	}

	public String getNomeCliente() {
		return (nomeCliente != null) ? nomeCliente.toUpperCase() : nomeCliente;
	}

}
