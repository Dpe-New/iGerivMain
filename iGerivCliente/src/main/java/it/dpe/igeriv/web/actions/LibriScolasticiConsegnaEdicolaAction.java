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
import org.apache.poi.hssf.dev.BiffViewer;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.apache.struts2.util.ServletContextAware;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.hibernate.criterion.Restrictions;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
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
@Component("libriScolasticiConsegnaEdicolaAction")
@SuppressWarnings({"unchecked", "rawtypes", "unused", "deprecation"}) 
public class LibriScolasticiConsegnaEdicolaAction extends RestrictedAccessBaseAction implements State, RequestAware {
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
	private final String crumbNameConsegnaEdicola = getText("igeriv.scolastica.consegna.edicola");
	
	
	
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
	//Consegna in edicola
	private String numeroCollo;
	private String numeroColloToSave;
	
	
	private Map<String, Object> evasioneJsonResult;
	
	public LibriScolasticiConsegnaEdicolaAction() {
		this(null, null, null, null, null, null, null, null, null,null,null,null);
	}
	
	@Autowired
	public LibriScolasticiConsegnaEdicolaAction(
		ClientiService<ClienteEdicolaVo> clientiService,
		RifornimentiService rifornimentiService,
		PubblicazioniService pubblicazioniService,
		MenuService menuService,
		PasswordEncoder passwordEncoder,
		ReflectionSaltSource saltSource,
		MailingListService mailingListService,
		@Value("${igeriv.url}") String igerivUrl,
		ScolasticaService scolasticaService,
		@Qualifier("LibriScolasticiRestService") RestService<RisultatoRicercaLibriDto, RicercaLibriTestoDto> libriScolasticiRestService,
		@Qualifier("LibriScolasticiDettaglioRestService") RestService<RisultatoRicercaLibriDto, RicercaLibriTestoDto> libriScolasticiDettaglioRestService,
		@Qualifier("LibriScolasticiTrackingOrdiniRestService") RestService<TrackingDto, RicercaLibriTestoDto> libriScolasticiTrackingOrdiniRestService) {
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
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
	
	@Override
	public void validate() {
		
	}
	
	/* -- */
	@BreadCrumb("%{crumbNameConsegnaEdicola}")
	@SkipValidation
	public String showFilterConsegnaEdicola() throws Exception {
		requestMap.put("tableTitle", getText("igeriv.scolastica.consegna.edicola"));
		actionName = "libriScolasticiConsegnaEdicola_showConsegnaEdicola.action";
		return SUCCESS;
	}
	
	/* -- */
	@BreadCrumb("%{crumbNameConsegnaEdicola}")
	@SkipValidation
	public String showConsegnaEdicola() {
		requestMap.put("tableTitle", getText("igeriv.scolastica.consegna.edicola"));
		List<OrdineLibriDettVo> listResLibri  = null;
		List<OrdineLibriDettVo> listResLibriRiepilogo  = null;
	
		
		if(numeroCollo!=null && !numeroCollo.equals("") && NumberUtils.isNumber(numeroCollo)){
			listResLibri = scolasticaService.getDettaglioOrdiniByNumCollo(getAuthUser().getCodFiegDl(), new Long(numeroCollo));
			if(listResLibri!=null && !listResLibri.isEmpty()){
				requestMap.put("listResLibri", listResLibri);
			}else{
				listResLibriRiepilogo = scolasticaService.getDettaglioOrdiniInEdicolaByNumCollo(getAuthUser().getCodFiegDl(), new Long(numeroCollo));
				requestMap.put("listResLibriRiepilogo", listResLibriRiepilogo);
			}
		}
		
		actionName = "libriScolasticiConsegnaEdicola_showConsegnaEdicola.action";
		return SUCCESS;
	}
	
	
	
	/* -- */
	@SkipValidation
	public String saveArrivoInEdicolaCollo() throws Exception {
		requestMap.put("tableTitle", getText("igeriv.tracking.scolastica.clienti"));
		List<OrdineLibriDettVo> listResLibri  = null;
		List<OrdineLibriDettVo> listResLibriRiepilogo  = null;
		if(numeroCollo!=null && !numeroCollo.equals("")){
			listResLibri = scolasticaService.getDettaglioOrdiniByNumCollo(getAuthUser().getCodFiegDl(), new Long(numeroCollo));
			//Cambia stato da 1 a 2
			if(listResLibri!=null){
				for(OrdineLibriDettVo iterObj : listResLibri){
					iterObj.setDataArrivoRivendita(pubblicazioniService.getSysdate());
					iterObj.setStato(ETrack.RIV.getId());
				}
				scolasticaService.saveVoList(listResLibri);
				
				List<Long> listClientiEmail = scolasticaService.getDistinctClientiByNumCollo(getAuthUser().getCodFiegDl(), new Long(numeroCollo));
				if(listClientiEmail!=null && !listClientiEmail.isEmpty()){
					for(Long iterClienti : listClientiEmail){
						//Modifica 28/08/2015 Invio mail con dettaglio con stato a RIV = Consegnato alla rivendita 
						//Modifica 01/09/2015 Aggiunta Restrictions.isNull("dettaglio.dataArrivoCliente")
						OrdineLibriVo ordineLibro = scolasticaService.getDettaglioOrdineLibri(iterClienti,ETrack.RIV);
						if(ordineLibro.getCliente().getEmail()!=null  && !ordineLibro.getCliente().getEmail().equals("")){
							String message = createMessageEmail(ordineLibro);
							String[] emailAgenzia = new String[1];
							emailAgenzia[0] = ordineLibro.getCliente().getEmail();
							mailingListService.sendEmailWithAttachment(emailAgenzia, getText("msg.subject.scolastica.arrivo.in.edicola.ordine"), message, null, true, null, false, null);
							//mailingListService.sendEmail(ordineLibro.getCliente().getEmail(), getText("msg.subject.scolastica.arrivo.in.edicola.ordine"), message);
						}
					
					}
				}
			
			}
			//Invia mail a tutti i clienti
			listResLibri = null;
			//listResLibriRiepilogo = scolasticaService.getDettaglioOrdiniByNumCollo(getAuthUser().getCodFiegDl(), new Long(numeroCollo));
		}
		requestMap.put("listResLibri", listResLibri);
		//requestMap.put("listResLibriRiepilogo", listResLibriRiepilogo);
		actionName = "libriScolasticiConsegnaEdicola_showConsegnaEdicola.action";
		return SUCCESS;
	}
	
	
	
	/* -- */
	@SkipValidation
	public String showClienteDettaglioOrdini() throws Exception {
		requestMap.put("tableTitle", getText("igeriv.inserisci.ordine.filter.clienti"));
		List<OrdineLibriDto> listOrdini =null;
		if (idCliente != null && !idCliente.equals("")) {
			listOrdini = scolasticaService.findListOrdiniLibri(getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodDpeWebEdicola(), null, new Long(idCliente));
			
			for(OrdineLibriDto iter : listOrdini){
				BigDecimal sumTotPrezzoLibriInOrdine = scolasticaService.sumPrezzoLibriCarrello(iter.getNumeroOrdine());
				iter.setPrezzoTotale(sumTotPrezzoLibriInOrdine);
			}
		}
		actionName = "libriScolasticiClienti_showClienteDettaglioOrdini.action";
		requestMap.put("listOrdini", listOrdini);
		return IGerivConstants.ACTION_DETT_ORDINE_CLIENTE_POPUP;
	}
	
	
	/* -- */
	@SkipValidation
	public String showDettaglioLibro() throws Exception {
		requestMap.put("tableTitle", getText("DETTAGLIO TESTO"));
		if(sku != null && !sku.equals("") && guid!=null && !guid.equals("")) {
			RicercaLibriTestoDto ricParams = new RicercaLibriTestoDto();
			ricParams.limite = 100;
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
		}		
		requestMap.put("numeroOrdine", idNumeroOrdine);
		return IGerivConstants.ACTION_VIEW_DETTAGLIO_LIBRO;
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
		requestMap.put("numeroOrdine", idNumeroOrdine);
		
		if(ordine!=null && ordine.getDataChiusuraOrdine()==null){
			actionName = "libriScolasticiClienti_showDettaglioOrdine.action";
			return IGerivConstants.ACTION_VIEW_DETTAGLIO_ORDINE;
		}else{
			actionName = "libriScolasticiClienti_showDettaglioOrdine.action";
			return IGerivConstants.ACTION_VIEW_DETTAGLIO_ORDINE_RIEPILOGO;
		}
		
		
	}
	
	public String createMessageEmail(OrdineLibriVo ord){
		String message = "";
		
		 StringBuilder result = new StringBuilder("<html><head><style>\n" +
                 "table, th, td {border: 0px solid black; padding-left: .5em;}" +
                 "td {padding-left: .5em;    border-bottom: 1pt solid black; }" +
                 ".right {text-align: right;}" +
                 ".center {text-align: center;}" +
                 ".left {text-align: left;}\n" +
                 "</style></head><body>");
					result.append(String.format("<p class=\"left\"><b>Gentile %s,</b><p>\n", ord.getCliente().getNomeCognome() !=null ? ord.getCliente().getNomeCognome() : " Cliente"));
					result.append(String.format("<p class=\"left\">i seguenti libri inerenti l\'ordine N° %s da lei prenotati sono pronti per il ritiro presso il nostro punto vendita:<p>\n",ord.getNumeroOrdineTxt()));
					result.append("<p><table border=\"0\"><tr>");
					result.append("<th class=\"center\">ISBN</th>");
					result.append("<th class=\"left\">TITOLO</th>");
					result.append("<th class=\"left\">AUTORE</th>");
					result.append("<th class=\"right\">EDITORE</th>");
					result.append("<th class=\"right\">PREZZO</th>");
					result.append("</tr>\n");
					
					
					BigDecimal totale = new BigDecimal(0);
					for(OrdineLibriDettVo iter : ord.getListDettaglioOrdine()){
						result.append("<tr>\n");
						result.append(String.format("<td class=\"center\">%s</td>",	(iter.getBarcode()!=null)?iter.getBarcode():""));
						result.append(String.format("<td class=\"center\">%s</td>", (iter.getTitolo()!=null)?iter.getTitolo():""));
						result.append(String.format("<td class=\"center\">%s</td>", (iter.getAutore()!=null)?iter.getAutore():""));
						result.append(String.format("<td class=\"center\">%s</td>", (iter.getEditore()!=null)?iter.getEditore():""));
						totale = totale.add((iter.getPrezzoCopertina()!=null)?iter.getPrezzoCopertina():new BigDecimal(0));
						
						DecimalFormat df = new DecimalFormat("#0.00");
						String prezzoFormat = df.format((iter.getPrezzoCopertina()!=null)?iter.getPrezzoCopertina():new BigDecimal(0))+" &euro; " ;
						
						result.append(String.format("<td class=\"center\">%s</td>", prezzoFormat));
						result.append("</tr>\n");
					}
					result.append("<tr>\n");
					result.append(String.format("<td class=\"right\" colspan=\"4\" >%s</td>",	"TOTALE: "));
					DecimalFormat df = new DecimalFormat("#0.00");
					String totaleFormat = df.format(totale)+" &euro; " ;
					result.append(String.format("<td class=\"center\">%s</td>",totaleFormat));
					result.append("</tr>\n");
					
		result.append("</table>\n");
        result.append("<p>Distinti saluti</p>");
        result.append("<p><b>");
        result.append(ord.getAnagraficaEdicolaVo().getRagioneSocialeEdicolaPrimaRiga());
        result.append("</b></p>");
        result.append("<p>"+ord.getAnagraficaEdicolaVo().getIndirizzoViaNumeroCitta()+" ( "+ord.getAnagraficaEdicolaVo().getSiglaProvincia()+" ) "+"</p>");
        result.append("</body></html>\n");
        message = result.toString();

        return message;
		
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
