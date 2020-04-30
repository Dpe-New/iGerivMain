package it.dpe.igeriv.security;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.prodotti.ProdottiService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.dto.ParametriClienteDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivConstants.SQLType;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import it.dpe.igeriv.vo.MetodoPagamentoClienteVo;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.igeriv.util.StringUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Classe che setta le variabili di sessione una volta loggato l'utente.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Component("IGerivLoginSessionConfigurer")
public class IGerivLoginSessionConfigurer {
	@Value("${igeriv.giorni.alert.sospensione}")
	private int numGiorniAlertSospensione;
	@Autowired
	private PubblicazioniService pubblicazioniService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private ClientiService clientiService;
	
	
	@Value("${igeriv.periodo.giorni}")
	private String periodoGiorni; 
	@Value("${igeriv.network.detection.interval.mills}")
	private String networkDetectionIntervalMills;
	@Value("${igeriv.autosave.bolle.interval.mills}")
	private String autoSaveBolleIntervalMills;
	@Value("${igeriv.high.priority.messages.check.interval.mills}")
	private String highPriorityMessagesCheckInterval;
	@Value("${igeriv.vendite.reg.cassa.mills.polling.task.interval}")
	private String millsTaskInterval;
	
	public void configureSession(Integer tipoUtente, HttpServletRequest httpServletRequest, UserAbbonato user) {
		configureSessionClienteEdicola(httpServletRequest, user);
	}
	
	/**
	 * @param request
	 * @param user
	 */
	public void configureSessionClienteEdicola(HttpServletRequest request, UserAbbonato user) {
		HttpSession session = request.getSession();
		session.setAttribute(IGerivConstants.USER_ID, user.getCodUtente());
		session.setAttribute(IGerivConstants.SESSION_VAR_NETWORK_DETECTION_INTERVAL_MILLS, "");
		session.setAttribute(IGerivConstants.SESSION_VAR_SAVE_BOLLE_INTERVAL_MILLS, "");
		request.getSession().setAttribute(IGerivConstants.SESSION_VAR_HIGH_PRIORITY_MESSAGES_CHECK_INTERVAL_MILLS, "");
		session.setAttribute(IGerivConstants.SESSION_VAR_COD_FIEG_DL, user.getCodFiegDl());
		session.setAttribute("listArgomento", pubblicazioniService.getArgomenti(user.getCodFiegDl()));
		session.setAttribute("listPeriodicita", pubblicazioniService.getPeriodicita());
		session.setAttribute("statiPrenotazioniPubblicazioni", buildStatiPrenotazioniPubblicazioniClienteEdicola());
		session.setAttribute("statiPrenotazioniPubblicazioniClient", buildStatiPrenotazioniPubblicazioniClienteEdicola());
		session.setAttribute(IGerivConstants.SESSION_VAR_REQUEST_IP_ADDRESS, (request.getHeader("x-forwarded-for") != null && !request.getHeader("x-forwarded-for").equals("")) ? request.getHeader("x-forwarded-for") : request.getRemoteAddr());
		session.setAttribute(IGerivConstants.SESSION_VAR_TIMEOUT_MINUTES, session.getMaxInactiveInterval() / 60);
		session.setAttribute(IGerivConstants.SESSION_VAR_BROWSER_NAME, StringUtility.getBrowserName(request.getHeader(IGerivConstants.HEADER_USER_AGENT)));
		session.setAttribute("abilitataCorrezioneBarcode", false);
		session.setAttribute("listNumberVo1to100", buildNumeroVoltePrenotazioni1to100());
	
		session.setAttribute("codDl_Pdf_Privacy", StringUtils.leftPad(Integer.toString(user.getCodFiegDl()), 3, "0"));
		String urlSessionPdfPrivacy = request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getContextPath()))+"/pdf/PRIVACY_POLICY_"+StringUtils.leftPad(Integer.toString(user.getCodFiegDl()), 3, "0")+".pdf";
		
		 File f = new File(urlSessionPdfPrivacy);
	     if(f.exists()){
	    	 //File exist
	    	 session.setAttribute("url_Pdf_Privacy", urlSessionPdfPrivacy);
	     }else{
	    	 //File not found
	 		urlSessionPdfPrivacy = request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getContextPath()))+"/pdf/PRIVACY_POLICY.pdf";
	 		session.setAttribute("url_Pdf_Privacy", urlSessionPdfPrivacy); 
	     }
		/* 21/04/2017 - AGGIUNGI I PARAMETRI PER CONTROLLARE L'ESTRAZIONE IN FORMATO PDF E XLS */
	    ClienteEdicolaVo cliente = clientiService.getCienteEdicolaByCodiceLogin(new Long(user.getCodUtente()));
	    
		Boolean viewImg =  cliente.getGruppoModuliVo().getViewImageByProfile();
		session.setAttribute("viewImageByProfile", viewImg);
		Boolean isEnabledExportXLS 		=  cliente.getGruppoModuliVo().getIsEnabledExportXLS();
		session.setAttribute("isEnabledExportXLS", isEnabledExportXLS);
		Boolean isEnabledPKInExportXLS 	=  cliente.getGruppoModuliVo().getIsEnabledPKInExportXLS();
		session.setAttribute("isEnabledPKInExportXLS", isEnabledPKInExportXLS);
		Boolean isEnabledExportPDF 		=  cliente.getGruppoModuliVo().getIsEnabledExportPDF();
		session.setAttribute("isEnabledExportPDF", isEnabledExportPDF);
		Boolean isEnabledPKInExportPDF 	=  cliente.getGruppoModuliVo().getIsEnabledPKInExportPDF();
		session.setAttribute("isEnabledPKInExportPDF", isEnabledPKInExportPDF);
		Boolean isEnabledExportPDFToTIFF 	=  cliente.getGruppoModuliVo().getIsEnabledExportPDFToTIFF();
		session.setAttribute("isEnabledExportPDFToTIFF", isEnabledExportPDFToTIFF);
	     
	    setParametriEdicola(session, user.getId(), user);
	}
	
	/**
	 * @param session
	 * @param user 
	 * @param codDpeWebEdicola
	 */
	private void setParametriEdicola(HttpSession session, Integer idUtente, UserAbbonato user) {
		ParametriClienteDto dto = new ParametriClienteDto();
		dto.setCodCliente(idUtente);
		dto.setCodParametro(IGerivConstants.COD_PARAMETRO_EMAIL_CLIENTE);
		dto.setDefaultValue(user.getEmail());
		dto.setSqlType(SQLType.VARCHAR);
		dto.setValue(user.getEmail());
		session.setAttribute(IGerivConstants.SESSION_VAR_PARAMETRO_CLIENTE + dto.getCodParametro(), dto);
	}
	
	/**
	 * Ritorna i numeri da 0 a 100 separati da virgola.
	 * 
	 * @return String
	 */
	/*private String getNumbers1to100() {
		List<Integer> listNumbers = new ArrayList<Integer>();
		for (int i = 0; i <= 100; i++) {
			listNumbers.add(i);
		}
		return Joiner.on(",").join(listNumbers);
	}*/
	
	/**
	 * Ritorna una lista di dto con i numeri da 1 a 100.
	 * 
	 * @return List<KeyValueDto>
	 */
	private List<KeyValueDto> buildNumeroVoltePrenotazioni1to100() {
		List<KeyValueDto> listNumbers = new ArrayList<KeyValueDto>();
		KeyValueDto dto = new KeyValueDto();
		dto.setKeyInt(-1);
		dto.setValue(IGerivMessageBundle.get("igeriv.sempre"));
		listNumbers.add(dto);
		for (int i = 1; i <= 100; i++) {
			KeyValueDto dto1 = new KeyValueDto();
			dto1.setKeyInt(i);
			dto1.setValue("" + i);
			listNumbers.add(dto1);
		}
		return listNumbers;
	}
	
	/**
	 * @return
	 */
	private List<KeyValueDto> buildStatiPrenotazioniPubblicazioniClienteEdicola() {
		List<KeyValueDto> stati = new ArrayList<KeyValueDto>();
		KeyValueDto dto1 = new KeyValueDto();
		dto1.setKey("" + IGerivConstants.PRENOTAZIONI_INSERITE);
		dto1.setValue(IGerivMessageBundle.get("igeriv.prenotazioni.inserite"));
		KeyValueDto dto2 = new KeyValueDto();
		dto2.setKey("" + IGerivConstants.PRENOTAZIONI_EVASE);
		dto2.setValue(IGerivMessageBundle.get("igeriv.prenotazioni.evase"));
		KeyValueDto dto3 = new KeyValueDto();
		dto3.setKey("" + IGerivConstants.PRENOTAZIONI_FISSE);
		dto3.setValue(IGerivMessageBundle.get("igeriv.prenotazioni.fisse"));
		KeyValueDto dto4 = new KeyValueDto();
		dto4.setKey("" + IGerivConstants.PRENOTAZIONI_PARZIALMENTE_EVASE);
		dto4.setValue(IGerivMessageBundle.get("igeriv.prenotazioni.evase.parzialmente"));
		stati.add(dto1);
		stati.add(dto2);
		stati.add(dto4);
		stati.add(dto3);
		return stati;
	}
	
	public int getNumGiorniAlertSospensione() {
		return numGiorniAlertSospensione;
	}

	public void setNumGiorniAlertSospensione(int numGiorniAlertSospensione) {
		this.numGiorniAlertSospensione = numGiorniAlertSospensione;
	}

	public String getPeriodoGiorni() {
		return periodoGiorni;
	}

	public void setPeriodoGiorni(String periodoGiorni) {
		this.periodoGiorni = periodoGiorni;
	}

	public String getNetworkDetectionIntervalMills() {
		return networkDetectionIntervalMills;
	}

	public void setNetworkDetectionIntervalMills(String networkDetectionIntervalMills) {
		this.networkDetectionIntervalMills = networkDetectionIntervalMills;
	}

	public String getAutoSaveBolleIntervalMills() {
		return autoSaveBolleIntervalMills;
	}

	public void setAutoSaveBolleIntervalMills(String autoSaveBolleIntervalMills) {
		this.autoSaveBolleIntervalMills = autoSaveBolleIntervalMills;
	}

	public String getHighPriorityMessagesCheckInterval() {
		return highPriorityMessagesCheckInterval;
	}

	public void setHighPriorityMessagesCheckInterval(String highPriorityMessagesCheckInterval) {
		this.highPriorityMessagesCheckInterval = highPriorityMessagesCheckInterval;
	}

	public String getMillsTaskInterval() {
		return millsTaskInterval;
	}

	public void setMillsTaskInterval(String millsTaskInterval) {
		this.millsTaskInterval = millsTaskInterval;
	}

//	public PubblicazioniService getPubblicazioniService() {
//		return pubblicazioniService;
//	}

//	public void setPubblicazioniService(PubblicazioniService pubblicazioniService) {
//		this.pubblicazioniService = pubblicazioniService;
//	}
	
}
