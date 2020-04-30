package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.bo.menu.MenuService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.bo.rifornimenti.RifornimentiService;
import it.dpe.igeriv.bo.scolastica.ScolasticaService;
import it.dpe.igeriv.dto.ClienteEdicolaDto;
import it.dpe.igeriv.dto.ParametriEdicolaDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.dto.RichiestaClienteDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.OrdineLibriVo;
import it.dpe.igeriv.vo.ProvinciaVo;
import it.dpe.igeriv.vo.RisposteClientiCodificateVo;
import it.dpe.igeriv.vo.TipoLocalitaVo;
import it.dpe.igeriv.web.rest.dto.ClespNuovoConsumatoreDto;
import it.dpe.igeriv.web.rest.dto.ClespRisultatoNuovoConsumatoreDto;
import it.dpe.igeriv.web.rest.service.ClespConsumatoreService;
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
@Component("gestioneClientiAction")
@SuppressWarnings({"unchecked", "rawtypes", "unused", "deprecation"}) 
public class GestioneClientiAction extends RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	private final ClientiService<ClienteEdicolaVo> clientiService;
	private final RifornimentiService rifornimentiService;
	private final PubblicazioniService pubblicazioniService;
	private final MenuService menuService;
	private final ScolasticaService scolasticaService;
	
	
	private final PasswordEncoder passwordEncoder;
	private final ReflectionSaltSource saltSource;
	private final MailingListService mailingListService;
	private final String igerivUrl;
	private final String crumbName = getText("igeriv.inserisci.aggiorna.clienti");
	private final String crumbNameAggCli = getText("igeriv.inserisci.aggiorna.clienti");
	private final String crumbNameInsPreCli = getText("igeriv.inserisci.prenotazioni.clienti");
	private final String crumbNameViewPreCli = getText("igeriv.visualizza.clienti.con.prenotazioni");
	private final String crumbNameViewCliConPre = getText("igeriv.visualizza.clienti.con.prenotazioni");
	private final String crumbNameEvPreCli = getText("igeriv.evasione.prenotazioni.clienti");
	private final String crumbNameConfermaEvaCli = getText("igeriv.conferma.evasione.prenotazioni.clienti");
	private final String crumbNamePreCliEvase = getText("igeriv.prenotazioni.clienti.evase");
	private final String crumbNameRepEC = getText("igeriv.report.estratto.conto.clienti");
	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String piva;
	private List<ClienteEdicolaDto> clienti;
	private String tableTitle;
	private String actionName;
	private Integer tableHeight;
	@Getter(AccessLevel.NONE)
	private List<TipoLocalitaVo> tipiLocalita;
	private List<ProvinciaVo> province;
	private ClienteEdicolaVo cliente;
	private String idCliente;
	private String term;
	private String inviaEmail;
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
	private Map<String, Object> evasioneJsonResult;
	
	public GestioneClientiAction() {
		this(null, null, null, null, null, null, null, null,null);
	}
	
	@Autowired
	public GestioneClientiAction(
		ClientiService<ClienteEdicolaVo> clientiService,
		RifornimentiService rifornimentiService,
		PubblicazioniService pubblicazioniService,
		MenuService menuService,
		PasswordEncoder passwordEncoder,
		ReflectionSaltSource saltSource,
		MailingListService mailingListService,
		ScolasticaService scolasticaService,
		@Value("${igeriv.url}") String igerivUrl) {
		this.clientiService = clientiService;
		this.rifornimentiService = rifornimentiService;
		this.pubblicazioniService = pubblicazioniService;
		this.menuService = menuService;
		this.passwordEncoder = passwordEncoder;
		this.saltSource = saltSource;
		this.mailingListService = mailingListService;
		this.scolasticaService = scolasticaService;
		this.igerivUrl = igerivUrl;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
	
	@Override
	public void validate() {
		if (getActionName() != null && getActionName().contains("saveCliente"))  {
			if (cliente != null) {
				if (cliente.getNome() == null || cliente.getNome().equals("")) {
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("dpe.contact.form.name")) + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				}
//				//ADD per la corretta gestione dei libri
//				if (cliente.getCognome() == null || cliente.getCognome().equals("")) {
//					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("dpe.contact.form.name")) + IGerivConstants.END_EXCEPTION_TAG);
//					throw new IGerivRuntimeException();
//				}
				if (cliente.getDtSospensionePrenotazioniDa() == null && cliente.getDtSospensionePrenotazioniA() != null) {
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("error.date.sospensione.da.prenotazione") + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				} else if (cliente.getDtSospensionePrenotazioniDa() != null && cliente.getDtSospensionePrenotazioniA() == null) {
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("error.date.sospensione.a.prenotazione") + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				} else if (cliente.getDtSospensionePrenotazioniDa() != null && cliente.getDtSospensionePrenotazioniA() != null) {
					if (cliente.getDtSospensionePrenotazioniDa().after(cliente.getDtSospensionePrenotazioniA())) {
						requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("error.date.sospensione.prenotazione.date.incongruenti") + IGerivConstants.END_EXCEPTION_TAG);
						throw new IGerivRuntimeException();
					}
				}
				if ((inviaEmail != null && inviaEmail.equals("true")) && (cliente.getEmail() == null || cliente.getEmail().trim().equals(""))) {
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("cliente.email")) + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				}
				if (cliente.getEmail() != null) {
					ClienteEdicolaVo cienteEdicolaByEmail = clientiService.getCienteEdicolaByEmail(cliente.getEmail());
					if (cienteEdicolaByEmail != null && !cienteEdicolaByEmail.getCodCliente().equals(cliente.getCodCliente())) {
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
	
	@BreadCrumb("%{crumbNameAggCli}")
	@SkipValidation
	public String showFilter() throws Exception {
		requestMap.put("tableTitle", getText("igeriv.inserisci.aggiorna.clienti"));
		actionName = "gestioneClienti_showClienti.action";
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameInsPreCli}")
	@SkipValidation
	public String showFilterPrenotazioni() throws Exception {
		requestMap.put("tableTitle", getText("igeriv.inserisci.prenotazioni.clienti"));
		actionName = "gestioneClienti_showClientiPrenotazioni.action";
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameViewPreCli}")
	@SkipValidation
	public String showFilterViewPrenotazioni() throws Exception {
		requestMap.put("tableTitle", getText("igeriv.visualizza.clienti.con.prenotazioni"));
		actionName = "gestioneClienti_showClientiViewPrenotazioni.action";
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameEvPreCli}")
	@SkipValidation
	public String showFilterEvasionePrenotazioni() {
		requestMap.put("tableTitle", getText("igeriv.evasione.prenotazioni.clienti"));
		actionName = "gestioneClienti_showClientiEvasionePrenotazioni.action";
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameRepEC}")
	@SkipValidation
	public String showFilterEstrattoContoClienti() throws Exception {
		requestMap.put("tableTitle", getText("igeriv.report.estratto.conto.clienti"));
		actionName = "gestioneClienti_showClientiEstrattoConto.action";
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameAggCli}")
	@SkipValidation
	public String showClienti() {
		requestMap.put("tableTitle", getText("igeriv.inserisci.aggiorna.clienti"));
		if (tipoRicerca == null || tipoRicerca.equals(0)) { 
			if (!Strings.isNullOrEmpty(getActionName()) && getActionName().equals("gestioneClienti_showClientiViewPrenotazioni.action")) {
				clienti = clientiService.getClientiConEvasione(getAuthUser().getArrId(), nome, cognome, codiceFiscale, piva, true);
				clienti = select(clienti, having(on(ClienteEdicolaDto.class).getQtaOrdiniDaEvadere(), greaterThan(0l)));
			} else {
				clienti = clientiService.getClientiEdicola(getAuthUser().getArrId(), nome, cognome, codiceFiscale, piva);
			}
		} else {
			Integer codPubblicazioneInt = (!Strings.isNullOrEmpty(codPubblicazione) && NumberUtils.isNumber(codPubblicazione)) ? Integer.parseInt(codPubblicazione) : null;
			BigDecimal prezzoBd = (!Strings.isNullOrEmpty(prezzo) && NumberUtils.isNumber(prezzo)) ? new BigDecimal(prezzo) : null;
			clienti = new ArrayList(clientiService.getClientiEdicolaByPubblicazione(getAuthUser().getArrId(), titolo, sottotitolo, numero, argomento, periodicita, prezzoBd, codPubblicazioneInt, codBarre, (conPrenotazioniFisse == null ? true : Boolean.parseBoolean(conPrenotazioniFisse))));
		}
		requestMap.put("clienti", clienti);
		actionName = "gestioneClienti_showClienti.action";
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameInsPreCli}")
	@SkipValidation
	public String showClientiPrenotazioni() {
		showClienti();
		tableTitle = getText("igeriv.inserisci.prenotazioni.clienti");
		actionName = "gestioneClienti_showClientiPrenotazioni.action";
		requestMap.put("tableTitle", getText("igeriv.inserisci.prenotazioni.clienti"));
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameViewCliConPre}")
	@SkipValidation
	public String showClientiViewPrenotazioni() {
		if (!Strings.isNullOrEmpty(prezzo)) {
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
		}
		showClienti();
		requestMap.put("tableTitle", getText("igeriv.visualizza.clienti.con.prenotazioni"));
		actionName = "gestioneClienti_showClientiViewPrenotazioni.action";
		return "successViewPrenotazioni";
	}
	
	@BreadCrumb("%{crumbNameEvPreCli}")
	@SkipValidation
	public String showClientiEvasionePrenotazioni() {
		clienti = clientiService.getClientiConEvasione(getAuthUser().getArrId(), nome, cognome, codiceFiscale, piva, false);
		clienti = select(clienti, having(on(ClienteEdicolaDto.class).getQtaOrdiniDaEvadere(), greaterThan(0l)));
		requestMap.put("clienti", clienti);
		requestMap.put("tableTitle", getText("igeriv.evasione.prenotazioni.clienti"));
		actionName = "gestioneClienti_showClientiEvasionePrenotazioni.action";
		return "successEvasionePrenotazioni";
	}
	
	@BreadCrumb("%{crumbNameRepEC}")
	@SkipValidation
	public String showClientiEstrattoConto() {
		showClienti();
		tableTitle = getText("igeriv.report.estratto.conto.clienti");
		requestMap.put("tableTitle", getText("igeriv.report.estratto.conto.clienti"));
		return "successReportEstrattoContoClienti";
	}
	
	@SkipValidation
	public String showCliente() throws Exception {
		requestMap.put("tableTitle", getText("igeriv.inserisci.aggiorna.clienti"));
		if (idCliente != null && !idCliente.equals("")) {
			cliente = clientiService.getClienteEdicola(getAuthUser().getArrId(), new Long(idCliente));
		} else {
			cliente = new ClienteEdicolaVo();
			cliente.setCodEdicola(getAuthUser().getId());
			cliente.setCodCliente(clientiService.getNextSeqVal(IGerivConstants.SEQ_CLIENTI_EDICOLA));
			GruppoModuliVo gruppoModuliVo = menuService.getGruppoModuliByRole(IGerivConstants.ROLE_IGERIV_CLIENTE_EDICOLA);
			cliente.setGruppoModuliVo(gruppoModuliVo);
			if (getAuthUser().getEdicolaDeviettiTodis()) {
				inviaEmail = "false";
			}
		}
		requestMap.put("cliente", cliente);
		return IGerivConstants.ACTION_NUOVO_CLIENTE;
	}
	
	@BreadCrumb("%{crumbNameConfermaEvaCli}")
	@SkipValidation
	public String viewEvasionePrenotazioniClientiEdicola() throws ParseException { 
		requestMap.put("tipoRifornimento", "evasione");
		filterTitle = getText("igeriv.visualizza.evsaione.cliente") + " <b>" + ((nomeCliente != null) ? nomeCliente.toUpperCase() : "") + "</b>";
		actionName = "gestioneClienti_viewEvasionePrenotazioniClientiEdicola.action";
		List<Long> listCodClienti = new ArrayList<Long>();
		if (!Strings.isNullOrEmpty(idCliente)) {
			listCodClienti.add(new Long(idCliente));
		} else if (!Strings.isNullOrEmpty(spunte)) {
			filterTitle = getText("igeriv.visualizza.evsaione.clienti");
			String[] split = spunte.split(",");
			for (String codCliente : split) {
				if (!Strings.isNullOrEmpty(codCliente)) {
					listCodClienti.add(new Long(codCliente.trim()));
				}
			}
		}
		if (!listCodClienti.isEmpty()) {
			Timestamp dataDa = Strings.isNullOrEmpty(this.dataDa) ? null : DateUtilities.floorDay(DateUtilities.parseDate(this.dataDa, DateUtilities.FORMATO_DATA_SLASH));
			Timestamp dataA = Strings.isNullOrEmpty(this.dataA) ? null : DateUtilities.ceilDay(DateUtilities.parseDate(this.dataA, DateUtilities.FORMATO_DATA_SLASH));
			List<RichiestaClienteDto> listRichieste = rifornimentiService.getRichiesteClienteByIdClienteViewOnly(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), listCodClienti, titolo, "" + IGerivConstants.PRENOTAZIONI_INSERITE_O_PARZIALMENTE_EVASE, null, dataDa, dataA, true);
			List<RisposteClientiCodificateVo> rpc = (List<RisposteClientiCodificateVo>) context.getAttribute("risposteClientiCodificate");
			int count = 0;
			for (RisposteClientiCodificateVo vo : rpc) {
				if (count > 0 && count < rpc.size()) {
					risposteClientiSelectKeys += ",";
					risposteClientiSelectValues += ",";
				}
				risposteClientiSelectKeys += vo.getCodRisposta();
				risposteClientiSelectValues += vo.getDescrizioneSintetica();
				count++;
			}
			requestMap.put("richiesteRifornimento", listRichieste);
			requestMap.put("emailValido", getAuthUser().isEmailValido());
			requestMap.put("emailClienteValido", ((!Strings.isNullOrEmpty(idCliente) && listRichieste != null && !listRichieste.isEmpty() && !Strings.isNullOrEmpty(listRichieste.get(0).getEmail())) || !Strings.isNullOrEmpty(spunte)) ? true : false);
			requestMap.put("prenotazioneEvasioneQuantitaEvasaEmpty", getAuthUser().getPrenotazioniEvasioneQuantitaEvasaEmpty());
		} 
		return IGerivConstants.ACTION_EVASIONE_PRENOTAZIONI_CLIENTI_EDICOLA;
	}
	
	@BreadCrumb("%{crumbNamePreCliEvase}")
	@SkipValidation
	public String saveEvasionePrenotazioniClientiEdicola() {
		try {
			if (pk != null) {
				saveEvasione();
				showResultEvasionePrenotazioniClientiEdicola();
			} else {
				return viewEvasionePrenotazioniClientiEdicola();
			}
			requestMap.put("stampaRicevuta", getText("tooltip.main_frame.Print"));
		} catch (IGerivRuntimeException e) {
			throw e;
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return IGerivConstants.ACTION_RESULT_EVASIONE_PRENOTAZIONI_CLIENTI_EDICOLA;
	}

	/**
	 * @throws ParseException
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public String saveEvasione() throws ParseException, MessagingException, UnsupportedEncodingException {
		evasioneJsonResult = new HashMap<String, Object>();
		try {
			List<Long> listCodClienti = new ArrayList<Long>();
			if (!Strings.isNullOrEmpty(idCliente)) {
				listCodClienti.add(new Long(idCliente));
			} else if (!Strings.isNullOrEmpty(spunte)) {
				String[] split = spunte.split(",");
				for (String codCliente : split) {
					if (!Strings.isNullOrEmpty(codCliente)) {
						listCodClienti.add(new Long(codCliente.trim()));
					}
				}
			}
			Timestamp dataCompEC = Strings.isNullOrEmpty(this.dataCompEC) ? null : DateUtilities.floorDay(DateUtilities.parseDate(this.dataCompEC, DateUtilities.FORMATO_DATA_SLASH)); 
			List<ClienteEdicolaVo> clienteEdicola = clientiService.getClientiEdicola(getAuthUser().getArrId(), listCodClienti);
			Map<Long, String> mapMailClienti = (!Strings.isNullOrEmpty(inviaEmail) && Boolean.parseBoolean(inviaEmail)) ? buildMapEmailClienti(clienteEdicola) : null;
			Map<String, ParametriEdicolaDto> mapParam = (Map<String, ParametriEdicolaDto>) sessionMap.get(IGerivConstants.SESSION_VAR_MAP_PARAMETRI_EDICOLA);
			ParametriEdicolaDto paramSaveEvasioneVendite = mapParam.containsKey(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_INCLUDI_EVASIONE_PRENOTAZIONE_VENIDTE) ? mapParam.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_INCLUDI_EVASIONE_PRENOTAZIONE_VENIDTE) : null;
			Boolean saveEvasionePrenotazioneVendite = !Strings.isNullOrEmpty(paramSaveEvasioneVendite.getParamValue()) && Boolean.parseBoolean(paramSaveEvasioneVendite.getParamValue()) ? true : false;
			Map<Long, List<RichiestaClienteDto>> mapEvasione = rifornimentiService.buildMapEvasione(pk, evaso, qtaDaEvadere, ultimaRisposta, messLibero);
			Long idConto = rifornimentiService.saveEvasionePrenotazioniClientiEdicola(getAuthUser().getCodUtente(), dataCompEC, saveEvasionePrenotazioneVendite, mapEvasione);
			for (Map.Entry<Long, List<RichiestaClienteDto>> entry : mapEvasione.entrySet()) {
				Long codCliente = entry.getKey();
				List<RichiestaClienteDto> values = entry.getValue();
				List<Map<String, String>> emailParams = new ArrayList<Map<String, String>>();
				for (RichiestaClienteDto dto : values) {
					Map<String, String> map = new HashMap<String, String>();
					PubblicazioneDto copertina = pubblicazioniService.getCopertinaByIdtn(dto.getCodDl(), dto.getIdtn());
					map.put("codDl", dto.getCodDl().toString());
					map.put("idtn", dto.getIdtn().toString());
					map.put("titolo", copertina.getTitolo());
					map.put("quantitaEvasa", dto.getQuantitaEvasa().toString());
					map.put("ultRisposta", dto.getUltRisposta().toString());
					map.put("mLibero", dto.getMessagioLibero());
					emailParams.add(map);
				}
				String email = (mapMailClienti != null && !mapMailClienti.isEmpty()) ? mapMailClienti.get(codCliente) : null;
				if (mapMailClienti != null && !mapMailClienti.isEmpty() && !Strings.isNullOrEmpty(email)) {
					sendEmailToLettore(email, getAuthUser().getRagioneSocialeEdicola(), emailParams, getAuthUser().getCodFiegDl().equals(IGerivConstants.CDL_CODE), getAuthUser().getEmail());
				}
			}
			this.idConto = idConto;
			evasioneJsonResult.put("success", "true");
		} catch (Throwable e) {
			evasioneJsonResult.put("error", getText("gp.errore.imprevisto"));
			throw e;
		}
		return SUCCESS;
	}
		
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
	private void sendEmailToLettore(String email, String ragSoc, List<Map<String, String>> emailParams, Boolean isCDL, String emailEdicola) throws MessagingException, UnsupportedEncodingException {
		String subject = IGerivMessageBundle.get("msg.email.evasione.ordine.subject");
		if (isCDL) {
			subject = subject.replaceAll("iGeriv", IGerivMessageBundle.get("igeriv.cdl"));
		}
		StringBuffer msg = new StringBuffer(IGerivMessageBundle.get("msg.email.gent.cliente"));
		for (Map<String, String> map : emailParams) {
			msg.append("<br><br>");
			Integer ultRisposta = map.get("ultRisposta") != null ? new Integer(map.get("ultRisposta").toString()) : 0;
			Integer quantitaEvasa = map.get("quantitaEvasa") != null ? new Integer(map.get("quantitaEvasa").toString()) : 0;
			String mLibero = map.get("mLibero") != null ? map.get("mLibero").toString() : "";
			String titolo = map.get("titolo") != null ? map.get("titolo").toString() : "";
			if (ultRisposta.equals(IGerivConstants.COD_MESSAGGIO_CODIFICATO_PUBBLICAZIONE_ESAURITA)) {
				String key = IGerivMessageBundle.get("msg.email.evasione.ordine.non.disponibile");
				if (mLibero != null && !mLibero.equals("")) {
					key = IGerivMessageBundle.get("msg.email.evasione.ordine.non.disponibile.descrizione");
					msg.append(MessageFormat.format(key, titolo, ragSoc, mLibero));
				} else { 
					msg.append(MessageFormat.format(key, titolo, ragSoc));
				}
			} else if (ultRisposta.equals(IGerivConstants.COD_MESSAGGIO_CODIFICATO_ATTENDO_RIFORNIMENTO_AGENZIA)) {
				String key = (quantitaEvasa > 0) ? IGerivMessageBundle.get("msg.email.evasione.parziale.ordine.attesa.rifo") : IGerivMessageBundle.get("msg.email.evasione.ordine.attesa.rifo");
				if (mLibero != null && !mLibero.equals("")) {
					key = (quantitaEvasa > 0) ? IGerivMessageBundle.get("msg.email.evasione.parziale.ordine.attesa.rifo.descrizione") : IGerivMessageBundle.get("msg.email.evasione.ordine.attesa.rifo.descrizione");
					msg.append((quantitaEvasa > 0) ? MessageFormat.format(key, titolo, ragSoc, quantitaEvasa, mLibero) : MessageFormat.format(key, titolo, ragSoc, mLibero));
				} else {
					msg.append((quantitaEvasa > 0) ? MessageFormat.format(key, titolo, ragSoc, quantitaEvasa) : MessageFormat.format(key, titolo, ragSoc));
				}
			} else if (ultRisposta.equals(IGerivConstants.COD_MESSAGGIO_CODIFICATO_ARRETRATO)) {
				String key = IGerivMessageBundle.get("msg.email.evasione.ordine.arretrato");
				if (mLibero != null && !mLibero.equals("")) {
					key = IGerivMessageBundle.get("msg.email.evasione.ordine.arretrato.descrizione");
					msg.append(MessageFormat.format(key, titolo, ragSoc, mLibero));
				} else {
					msg.append(MessageFormat.format(key, titolo, ragSoc));
				}
				msg = new StringBuffer(MessageFormat.format(key, titolo, ragSoc, mLibero));
			} else if (ultRisposta.equals(IGerivConstants.COD_MESSAGGIO_CODIFICATO_RIFIUTATO_CLIENTE)) {
				String key = IGerivMessageBundle.get("msg.email.evasione.ordine.rifiutato");
				if (mLibero != null && !mLibero.equals("")) {
					key = IGerivMessageBundle.get("msg.email.evasione.ordine.rifiutato.descrizione");
					msg.append(MessageFormat.format(key, titolo, ragSoc, mLibero));
				} else {
					msg.append(MessageFormat.format(key, titolo, ragSoc));
				}
				msg = new StringBuffer(MessageFormat.format(key, titolo, ragSoc, mLibero));
			} else {
				String key = IGerivMessageBundle.get("msg.email.evasione.ordine");
				if (mLibero != null && !mLibero.equals("")) {
					key += "<br>Descrizione: {3}";
					msg.append(MessageFormat.format(key, titolo, ragSoc, quantitaEvasa, mLibero));
				} else {
					msg.append(MessageFormat.format(key, titolo, ragSoc, quantitaEvasa));
				}
			}
		} 
		mailingListService.sendEmailWithAttachment(new String[]{email}, subject, msg.toString(), null, true, emailEdicola, false, getAuthUser().getRagioneSocialeEdicola(), null, getAuthUser().getRagioneSocialeEdicola());
	}
	
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

	@BreadCrumb("%{crumbNamePreCliEvase}")
	@SkipValidation
	public String showResultEvasionePrenotazioniClientiEdicola() {
		List<RichiestaClienteDto> listRichieste = rifornimentiService.getRichiesteClienteByPk(pk);
		String[] arrPk = pk.split(",");
		String[] arrQtaEvasa = evaso.split(",");
		for (int  i = 0; i < arrPk.length; i++) {
			String pkValue = arrPk[i].trim();
			String qtaEv = arrQtaEvasa[i].trim();
			for (RichiestaClienteDto dto : listRichieste) {
				if (dto.getPk().toString().equals(pkValue)) {
					dto.setQuantitaConsegnare((qtaEv != null && !qtaEv.equals("")) ? new Integer(qtaEv) : 0);
					break;
				}
			}
		}
		listRichieste = select(listRichieste, having(on(RichiestaClienteDto.class).getQuantitaConsegnare(), greaterThanOrEqualTo(0)));
		requestMap.put("richiesteRifornimento", listRichieste);
		if (!Strings.isNullOrEmpty(idCliente)) {
			ClienteEdicolaVo clienteEdicola = clientiService.getClienteEdicola(getAuthUser().getArrId(), new Long(idCliente));
			String consegnaPrenotazioni = MessageFormat.format(getText("igeriv.consegna.prenotazioni"), DateUtilities.getTimestampAsString(new Date(), DateUtilities.FORMATO_DATA_SLASH)); 
			String cliente	= nomeCliente.toUpperCase().replaceAll("&nbsp;", " ").replaceAll("\\.", " ");
			String indirizzoCliente = (clienteEdicola.getIndirizzo() != null) ? clienteEdicola.getIndirizzo().replaceAll("&nbsp;", " ").replaceAll("\\.", " ") : "-" ; 
			String cittaCliente = (clienteEdicola.getLocalitaDesc() != null) ? clienteEdicola.getLocalitaDesc().replaceAll("&nbsp;", " ").replaceAll("\\.", " ") : "-" ; 
			String codEdicola = "" + getAuthUser().getCodEdicolaDl();
			String ragSocEdicola = getAuthUser().getRagioneSocialeEdicola().replaceAll("&nbsp;", " ").replaceAll("\\.", " "); 
			String indirizzoEdicola = getAuthUser().getIndirizzoEdicolaPrimaRiga().replaceAll("&nbsp;", " ").replaceAll("\\.", " ");
			String cittaEdicola = getAuthUser().getLocalitaEdicolaPrimaRiga().replaceAll("&nbsp;", " ").replaceAll("\\.", " ");
			requestMap.put("consegnaPrenotazioni", consegnaPrenotazioni);
			requestMap.put("codEdicola", codEdicola);
			requestMap.put("ragSocEdicola", ragSocEdicola);
			requestMap.put("indirizzoEdicola", indirizzoEdicola);
			requestMap.put("cittaEdicola", cittaEdicola);
			requestMap.put("cliente", getText("igeriv.provenienza.evasione.cliente"));
			requestMap.put("ragSocCliente", cliente);
			requestMap.put("indirizzoCliente", indirizzoCliente);
			requestMap.put("cittaCliente", cittaCliente);
			requestMap.put("nomeCliente", clienteEdicola.getNome() + " " + clienteEdicola.getCognome());
		} 
		return IGerivConstants.ACTION_RESULT_EVASIONE_PRENOTAZIONI_CLIENTI_EDICOLA;
	}
	
	public String saveCliente() throws Exception {
		try {
			if (cliente != null) {
				
				//Controllo se ci sono ordini attivi per il cliente
				List<OrdineLibriVo> listOrdineLibri = scolasticaService.getListOrdiniCliente(getAuthUser().getCodFiegDl(),cliente.getCodEdicola(),cliente.getCodCliente());
				if(!listOrdineLibri.isEmpty()){
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("msg.errore.modifica.cliente.presenza.ordini") + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				}
				
				
				prepareClienteVo();
				String randomPwd = null;
				if (inviaEmail != null && inviaEmail.equals("true")) {
					List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
					authList.add(new GrantedAuthorityImpl(IGerivConstants.ROLE_IGERIV_CLIENTE_EDICOLA));
					randomPwd = StringUtility.getRandomString(14);
					String pass = "" + ((cliente.getCodCliente() + cliente.getCodCliente()) * IGerivConstants.ENCODE_FACTOR);
					String pwd = passwordEncoder.encodePassword(randomPwd, pass);
					cliente.setPassword(pwd);
					cliente.setPwdCriptata(1);
					cliente.setChangePassword(1);
				} 		
				if (cliente.getDtSospensionePrenotazioniDa() != null) {
					cliente.setDtSospensionePrenotazioniDa(DateUtilities.floorDay(cliente.getDtSospensionePrenotazioniDa()));
				}
				if (cliente.getDtSospensionePrenotazioniA() != null) {
					cliente.setDtSospensionePrenotazioniA(DateUtilities.ceilDay(cliente.getDtSospensionePrenotazioniA()));
				}
				cliente.setNome(!Strings.isNullOrEmpty(cliente.getNome()) ? cliente.getNome().toUpperCase() : cliente.getNome());
				cliente.setCognome(!Strings.isNullOrEmpty(cliente.getCognome()) ? cliente.getCognome().toUpperCase() : cliente.getCognome());
				clientiService.saveBaseVo(cliente);
				if (cliente.getEmail() != null && !cliente.getEmail().equals("") && randomPwd != null && !randomPwd.equals("")) {
					String message = MessageFormat.format(getText("msg.email.nuovo.account.cliente.edicola"), getAuthUser().getRagioneSocialeEdicola(), cliente.getCodCliente().toString().replaceAll("\\.", ""), randomPwd, igerivUrl);
					if (getAuthUser().getCodFiegDl().equals(IGerivConstants.CDL_CODE)) {
						//CDL BOLOGNA
						mailingListService.sendEmail(cliente.getEmail(), getText("msg.subject.nuovo.account.cdl.online"), message.replaceAll("iGeriv", getText("igeriv.cdl")));
					} else if (getAuthUser().getCodFiegDl().equals(IGerivConstants.CHIMINELLI_CODE_1) || getAuthUser().getCodFiegDl().equals(IGerivConstants.CHIMINELLI_CODE_2)) {
						//CHIMINELLI AMAZON
						message = MessageFormat.format(getText("msg.email.nuovo.account.chiminelli.cliente.edicola"), getAuthUser().getRagioneSocialeEdicola(), cliente.getCodCliente().toString().replaceAll("\\.", ""), randomPwd, "http://www.ilchiosco.it/igeriv-client/");
						mailingListService.sendEmail(cliente.getEmail(), getText("msg.subject.nuovo.account.chiminelli.online"), message);
					} else {
						//ALL
						mailingListService.sendEmail(cliente.getEmail(), getText("msg.subject.nuovo.account.cliente.edicola"), message);
					}
				}
			}
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return SUCCESS;
	}
	
	@SkipValidation
	public String deleteCliente() throws Exception {
		if (cliente != null
				&& cliente.getCodCliente() != null && cliente.getCodEdicola() != null) {
				//CONTROLLO ESISTENZA ORDINE LIBRI
				List<OrdineLibriVo> listOrdineLibri = scolasticaService.getListOrdiniCliente(getAuthUser().getCodFiegDl(),cliente.getCodEdicola(),cliente.getCodCliente());
				if(listOrdineLibri.isEmpty()){
					prepareClienteVo();
					try {
						clientiService.deleteCliente(cliente.getCodCliente());
					} catch (Exception e) {
						if ((e.getCause() != null && e.getCause() instanceof SQLIntegrityConstraintViolationException) || 
								(e.getCause() != null && e.getCause().getCause() != null && e.getCause().getCause() instanceof org.hibernate.exception.ConstraintViolationException)) {
							try {
								clientiService.deleteClienteWithDependencies(cliente.getCodCliente(), getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId());
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
		String title = getText("igeriv.inserisci.aggiorna.clienti");
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
		if (cliente.getProvincia() != null && cliente.getProvincia().getCodProvincia() == null) {
			cliente.setProvincia(null);
		}
		if (cliente.getTipoLocalita() != null && cliente.getTipoLocalita().getTipoLocalita() == null) {
			cliente.setTipoLocalita(null);
		}
		if (cliente.getLocalita() != null && cliente.getLocalita().getCodLocalita() == null) {
			cliente.setLocalita(null);
		}
		if (cliente.getPaese() != null && cliente.getPaese().getCodPaese() == null) {
			cliente.setPaese(null);
		}
		if (cliente.getTipoPagamento() != null && cliente.getTipoPagamento().getCodMetodoPagamento() == null) {
			cliente.setTipoPagamento(null);
		}
		if (cliente.getBanca() != null && cliente.getBanca().getCodBanca() == null) {
			cliente.setBanca(null);
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
