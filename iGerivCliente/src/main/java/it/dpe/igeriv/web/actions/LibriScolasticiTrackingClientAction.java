package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.bo.menu.MenuService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.bo.rifornimenti.RifornimentiService;
import it.dpe.igeriv.bo.scolastica.ScolasticaService;
import it.dpe.igeriv.dto.ClienteEdicolaDto;
import it.dpe.igeriv.dto.DettaglioRicercaLibriDto;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.dto.OrdineLibriDto;
import it.dpe.igeriv.dto.ParametriEdicolaDto;
import it.dpe.igeriv.dto.ParametriRicercaLibriDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.dto.RichiestaClienteDto;
import it.dpe.igeriv.dto.RisultatiRicercaLibriDto;
import it.dpe.igeriv.dto.RisultatoRicercaLibriDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.BollaResaFuoriVoceVo;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.OrdineLibriDettVo;
import it.dpe.igeriv.vo.OrdineLibriDettVo.ETrack;
import it.dpe.igeriv.vo.OrdineLibriVo;
import it.dpe.igeriv.vo.ProvinciaVo;
import it.dpe.igeriv.vo.RisposteClientiCodificateVo;
import it.dpe.igeriv.vo.TipoLocalitaVo;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.igeriv.web.rest.dto.AnagraficaDto;
import it.dpe.igeriv.web.rest.dto.ConfermaOrdine;
import it.dpe.igeriv.web.rest.dto.ContattiDto;
import it.dpe.igeriv.web.rest.dto.ElencoLibriDto;
import it.dpe.igeriv.web.rest.dto.InformazioniDto;
import it.dpe.igeriv.web.rest.dto.OrdineDto;
import it.dpe.igeriv.web.rest.dto.RicercaLibriTestoDto;
import it.dpe.igeriv.web.rest.dto.TrackingDto;
import it.dpe.igeriv.web.rest.dto.TrackingElencoLibriDto;
import it.dpe.igeriv.web.rest.service.RestService;
import it.dpe.service.mail.MailingListService;
import it.dpe.ws.dto.HttpJsonResponse;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.math.NumberUtils;
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

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Classe action per la gestione dei clienti della rivendita.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("libriScolasticiTrackingClientAction")
@SuppressWarnings({"unchecked", "rawtypes", "unused", "deprecation"}) 
public class LibriScolasticiTrackingClientAction extends RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	private final ClientiService<ClienteEdicolaVo> clientiScolasticaService;
	private final ScolasticaService scolasticaService;
	private final RifornimentiService rifornimentiService;
	private final PubblicazioniService pubblicazioniService;
	private final MenuService menuService;
	private final PasswordEncoder passwordEncoder;
	private final ReflectionSaltSource saltSource;
	private final MailingListService mailingListService;
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
//	private RestService<RisultatoRicercaLibriDto, RicercaLibriTestoDto> libriScolasticiRestService;
//	private RestService<RisultatoRicercaLibriDto, RicercaLibriTestoDto> libriScolasticiDettaglioRestService;
	private RestService<TrackingDto, RicercaLibriTestoDto> libriScolasticiTrackingOrdiniClientRestService;
	
	private String sku;
	private String guid;
	private Long idNumeroOrdine;
	private DettaglioRicercaLibriDto dettaglioLibroPopup;
	private Long codCliente;
	private Long numOrdine;
	private Map<String,String> resultMap;
	private String operation;
	private boolean addCart = false;
	private String numOrdineTxt;
	
	//Tracking
	private String numOrdineTxtTracking;
	
	
	private Map<String, Object> evasioneJsonResult;
	
	public LibriScolasticiTrackingClientAction() {
		this(null, null, null, null, null, null, null, null, null,null);
	}
	
	@Autowired
	public LibriScolasticiTrackingClientAction(
		ClientiService<ClienteEdicolaVo> clientiService,
		RifornimentiService rifornimentiService,
		PubblicazioniService pubblicazioniService,
		MenuService menuService,
		PasswordEncoder passwordEncoder,
		ReflectionSaltSource saltSource,
		MailingListService mailingListService,
		@Value("${igeriv.url}") String igerivUrl,
		ScolasticaService scolasticaService,
//		@Qualifier("LibriScolasticiRestService") RestService<RisultatoRicercaLibriDto, RicercaLibriTestoDto> libriScolasticiRestService,
//		@Qualifier("LibriScolasticiDettaglioRestService") RestService<RisultatoRicercaLibriDto, RicercaLibriTestoDto> libriScolasticiDettaglioRestService,
		@Qualifier("LibriScolasticiTrackingOrdiniClientRestService") RestService<TrackingDto, RicercaLibriTestoDto> libriScolasticiTrackingOrdiniClientRestService) {
		this.clientiScolasticaService = clientiService;
		this.rifornimentiService = rifornimentiService;
		this.pubblicazioniService = pubblicazioniService;
		this.menuService = menuService;
		this.passwordEncoder = passwordEncoder;
		this.saltSource = saltSource;
		this.mailingListService = mailingListService;
		this.igerivUrl = igerivUrl;
		this.scolasticaService = scolasticaService;
//		this.libriScolasticiRestService = libriScolasticiRestService;
//		this.libriScolasticiDettaglioRestService = libriScolasticiDettaglioRestService;
		this.libriScolasticiTrackingOrdiniClientRestService = libriScolasticiTrackingOrdiniClientRestService;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
	
	@Override
	public void validate() {
		
	}
	
	/* -- */
	@BreadCrumb("%{crumbNameTracking}")
	@SkipValidation
	public String showFilterTracking() throws Exception {
		requestMap.put("tableTitle", getText("igeriv.tracking.scolastica.clienti"));
		actionName = "libriScolasticiTrackingClient_showTracking.action";
		return SUCCESS;
	}
	
	/* -- */
	@BreadCrumb("%{crumbNameTracking}")
	@SkipValidation
	public String showTracking() {
		requestMap.put("tableTitle", getText("igeriv.inserisci.aggiorna.scolastica.clienti"));
		List<TrackingElencoLibriDto> listElencoLibri = null;
		List<OrdineLibriDettVo> listLibriTrackingResult = new ArrayList<OrdineLibriDettVo>();
		TrackingDto trackingDto;
		if(numOrdineTxtTracking!=null && !numOrdineTxtTracking.equals("")){
			OrdineLibriVo ordine = scolasticaService.getOrdine(numOrdineTxtTracking,new Long(getAuthUser().getCodUtente()));
			if(ordine!=null){
				
				RicercaLibriTestoDto ricParams = new RicercaLibriTestoDto();
				ricParams.setNumOrdine(ordine.getNumeroOrdine().toString());
				ricParams.setNumOrdineTxt(ordine.getNumeroOrdineTxt());
				
				ResponseEntity<TrackingDto> res = libriScolasticiTrackingOrdiniClientRestService.getEntity(ricParams);
				if(res!=null && res.getBody()!=null && res.getStatusCode() == HttpStatus.OK){
					trackingDto = res.getBody();
					listElencoLibri = new ArrayList(Arrays.asList(res.getBody().getElencolibri()));
					
					if(trackingDto!=null && listElencoLibri!=null){
					
						
						for(TrackingElencoLibriDto iter :listElencoLibri){
							
							OrdineLibriDettVo dettLibro = scolasticaService.getDettaglioLibro(ordine.getNumeroOrdine(),iter.getSku());
							if(dettLibro.getStato().intValue()>0){
								dettLibro.setDescrizioneStato(dettLibro.getDescStato());
								listLibriTrackingResult.add(dettLibro);
							}else{
								dettLibro.setDescrizioneStato(iter.getStato());
								listLibriTrackingResult.add(dettLibro);
							}
							
						}
						ClienteEdicolaVo clienteEdicola = clientiScolasticaService.getClienteEdicola(getAuthUser().getArrId(), trackingDto.getCodiceCliente());
							
						requestMap.put("clienteEdicola", clienteEdicola);
						requestMap.put("trackingInfo", trackingDto);
						requestMap.put("elencoLibriTraking", listLibriTrackingResult);
					}
				
				}
				
			}
		}
		actionName = "libriScolasticiTrackingClient_showTracking.action";
		return SUCCESS;
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
	
	public void setNumOrdineTxt(String num){
		this.numOrdineTxt = num.trim();
	}
	public void setNumOrdineTxtTracking(String numT){
		this.numOrdineTxtTracking = numT.trim();
	}

}
