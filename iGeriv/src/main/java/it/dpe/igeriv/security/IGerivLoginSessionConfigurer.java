package it.dpe.igeriv.security;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.batik.script.Window.GetURLHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.prodotti.ProdottiService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.dto.AnagraficaEditoreDto;
import it.dpe.igeriv.dto.EmailDlDto;
import it.dpe.igeriv.dto.ParametriEdicolaDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.MetodoPagamentoClienteVo;
import it.dpe.igeriv.vo.UserVo;

/**
 * Classe che setta le variabili di sessione una volta loggato l'utente.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Component("IGerivLoginSessionConfigurer")
public class IGerivLoginSessionConfigurer {
	private final ProdottiService prodottiService;
	private final AgenzieService agenzieService;
	private final EdicoleService edicoleService;
	private final PubblicazioniService pubblicazioniService;
	private final AccountService accountService;
	private final int numGiorniAlertSospensione;
	
	private String keyGoogleMaps;
	
	
	@Autowired
	IGerivLoginSessionConfigurer(
			ProdottiService prodottiService, 
			AgenzieService agenzieService, 
			EdicoleService edicoleService, 
			ClientiService<MetodoPagamentoClienteVo> clientiService,
			PubblicazioniService pubblicazioniService,
			@Value("${igeriv.giorni.alert.sospensione}") int numGiorniAlertSospensione, 
			@Value("${igeriv.key.google.maps}") String keymaps,
			AccountService accountService) {
		this.prodottiService = prodottiService;
		this.agenzieService = agenzieService;
		this.edicoleService = edicoleService;
		this.numGiorniAlertSospensione = numGiorniAlertSospensione;
		this.keyGoogleMaps = keymaps;
		this.pubblicazioniService = pubblicazioniService;
		this.accountService = accountService;
	}
	
	public void configureSession(Integer tipoUtente, HttpServletRequest httpServletRequest, UserAbbonato user) {
		configureSessionEdicola(httpServletRequest, user);
	}
	
	@SuppressWarnings("unchecked")
	public void configureSessionEdicola(HttpServletRequest request, UserAbbonato user) {
		HttpSession session = request.getSession();
		session.setAttribute(IGerivConstants.USER_ID, user.getCodUtente());
		session.setAttribute(IGerivConstants.SESSION_VAR_APP_NAME, user.getCodFiegDl().equals(IGerivConstants.CDL_CODE) ? IGerivMessageBundle.get("igeriv.cdl") : IGerivMessageBundle.get("gp.titolo"));
		session.setAttribute(IGerivConstants.SESSION_VAR_COD_FIEG_DL, user.getCodFiegDl());
		session.setAttribute(IGerivConstants.SESSION_VAR_TIPO_CONTROLLO_PUBBLICAZIONI_RESPINTE, user.getTipoControlloPubblicazioniRespinte());
		session.setAttribute(IGerivConstants.SESSION_VAR_EDICOLA_IN_GRUPPO_SCONTO_BASE, user.isEdicolaInGruppoScontoBase());
		session.setAttribute("listCategorie", prodottiService.getCategorie(user.getCodFiegDl(), user.getId()));
		//session.setAttribute("listCategoriePNE", iGerivPublicService.getCategorieProdottiNonEditoriali(user.getCodFiegDl()));
		session.setAttribute(IGerivConstants.SESSION_VAR_EMAIL_DL, user.getEmailDl());
		List<EmailDlDto> emailsDl = agenzieService.getEmailsDl(user.getCodFiegDl());
		Gson gson = new GsonBuilder().setExclusionStrategies(new TestExclStrat()).create();
		session.setAttribute(IGerivConstants.SESSION_VAR_EMAIL_DL_JSON, gson.toJson(emailsDl));
		session.setAttribute(IGerivConstants.SESSION_VAR_EMAIL_DL_LIST, emailsDl);
		session.setAttribute("numMaxCpuResaDimeticata", user.getNumMaxCpuResaDimenticata());
		
		session.setAttribute(IGerivConstants.SESSION_VAR_URL_PATH, request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getContextPath())));
		session.setAttribute(IGerivConstants.SESSION_VAR_REQUEST_IP_ADDRESS, (request.getHeader("x-forwarded-for") != null && !request.getHeader("x-forwarded-for").equals("")) ? request.getHeader("x-forwarded-for") : request.getRemoteAddr());
		session.setAttribute(IGerivConstants.SESSION_VAR_TIMEOUT_MINUTES, request.getSession().getMaxInactiveInterval() / 60);
		session.setAttribute(IGerivConstants.SESSION_VAR_BROWSER_NAME, StringUtility.getBrowserName(request.getHeader(IGerivConstants.HEADER_USER_AGENT)));
		String roleName = (user.getAuthorities() != null && !user.getAuthorities().isEmpty()) ? (user.getAuthorities().iterator().next().getAuthority()) : "";
		boolean hasProfiloEdicola = roleName.equals(IGerivConstants.ROLE_IGERIV_BASE) || roleName.equals(IGerivConstants.ROLE_IGERIV_BASE_ADMIN) || roleName.equals(IGerivConstants.ROLE_IGERIV_BASE_ADMIN_DEVIETTI_TODIS) || roleName.equals(IGerivConstants.ROLE_IGERIV_LITE) || roleName.equals(IGerivConstants.ROLE_IGERIV_TEST) || roleName.equals(IGerivConstants.ROLE_IGERIV_ADV_2) || roleName.equals(IGerivConstants.ROLE_IGERIV_ADV_3) || roleName.equals(IGerivConstants.ROLE_IGERIV_ADV_4) || roleName.equals(IGerivConstants.ROLE_IGERIV_STARTER) || roleName.equals(IGerivConstants.ROLE_IGERIV_BASE_ARCIRC) || roleName.equals(IGerivConstants.ROLE_CHIMI_LIGHT_IMG);
		boolean hasProfiloEdicolaBaseNotTest = (roleName.equals(IGerivConstants.ROLE_IGERIV_BASE) || roleName.equals(IGerivConstants.ROLE_IGERIV_BASE_ADMIN) || roleName.equals(IGerivConstants.ROLE_IGERIV_BASE_ADMIN_DEVIETTI_TODIS)) && !roleName.equals(IGerivConstants.ROLE_IGERIV_TEST);
		session.setAttribute("hasProfiloEdicola", hasProfiloEdicola);
		session.setAttribute("hasProfiloStarter", roleName.equals(IGerivConstants.ROLE_IGERIV_STARTER));
		session.setAttribute(IGerivConstants.SESSION_VAR_HAS_PROFILO_EDICOLA_BASE_NOT_TEST, hasProfiloEdicolaBaseNotTest);
		session.setAttribute("abilitataCorrezioneBarcode", user.getAbilitataCorrezioneBarcode());
		session.setAttribute("hasButtonCopiaDifferenze", user.getHasButtonCopiaDifferenze());
		session.setAttribute("hasResaAnticipata", user.getHasResaAnticipata());
		setAlertNumeroGiorniAllaSospensione(session, user.getDtSospensioneEdicola());
		setParametriEdicola(session, user.getCodDpeWebEdicola());
		
		session.setAttribute("showLeftBar", Boolean.valueOf(((ParametriEdicolaDto) ((Map<String, ParametriEdicolaDto>) session.getAttribute(IGerivConstants.SESSION_VAR_MAP_PARAMETRI_EDICOLA)).get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_MOSTRA_BARRA_LATERALE_SINISTRA_VENDITE)).getParamValue()));
		session.setAttribute("showRightBar", Boolean.valueOf(((ParametriEdicolaDto) ((Map<String, ParametriEdicolaDto>) session.getAttribute(IGerivConstants.SESSION_VAR_MAP_PARAMETRI_EDICOLA)).get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_MOSTRA_BARRA_LATERALE_DESTRA_VENDITE)).getParamValue()));
		session.setAttribute("ricercaModalitaContenuto", Boolean.valueOf(((ParametriEdicolaDto) ((Map<String, ParametriEdicolaDto>) session.getAttribute(IGerivConstants.SESSION_VAR_MAP_PARAMETRI_EDICOLA)).get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_RICERCA_MODALITA_CONTENUTO)).getParamValue()));
		//Ticket 0000371
		session.setAttribute("abilitaBeepVenditaBarcode", Boolean.valueOf(((ParametriEdicolaDto) ((Map<String, ParametriEdicolaDto>) session.getAttribute(IGerivConstants.SESSION_VAR_MAP_PARAMETRI_EDICOLA)).get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_EDICOLA_ABILITA_BEEP_VENDITE_BARCODE)).getParamValue()));
		//DPNA - 21/03/2018 - Controllo delle pubblicazione in resa non presenti nella bolla 
		//Generare popup con richiesta esplicita di inserimento in bolla della pubblicazione selezionata
		session.setAttribute("abilitaControlloResaFuoriBolla", Boolean.valueOf(((ParametriEdicolaDto) ((Map<String, ParametriEdicolaDto>) session.getAttribute(IGerivConstants.SESSION_VAR_MAP_PARAMETRI_EDICOLA)).get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_EDICOLA_ABILITA_CONTROLLO_RESA_FUORI_BOLLA)).getParamValue()));
		
		
		session.setAttribute("isDlInforiv", user.isDlInforiv());
		session.setAttribute("keyGoogleMaps", keyGoogleMaps); 
		
		Integer[] codDl = (user.isMultiDl() || user.getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_CLIENTE_EDICOLA)) ? user.getArrCodFiegDl() : new Integer[]{user.getCodFiegDl()};
		List<AnagraficaEditoreDto> listAnagEditori = new ArrayList<AnagraficaEditoreDto>();
		listAnagEditori = pubblicazioniService.getListAnagraficaEditori(codDl);
		session.setAttribute("listAnagraficaEditori", listAnagEditori);	
		
		
		
		Boolean isMenta = (user.getCodFiegDl().equals(IGerivConstants.MENTA_CODE)) ? true: false;
        session.setAttribute("isEdicolaMenta", isMenta);	
		
		//Gestione Profili
		UserVo userVo = accountService.getEdicolaByCodiceEdicola(user.getId());
		
		
		Boolean viewImg =  userVo.getDlGruppoModuliVo().getGruppoModuli().getViewImageByProfile();
		session.setAttribute("viewImageByProfile", viewImg);
		// add 14/02/2017 - Gestione dell'esportazione in XLS , PDF e TIFF (Gestione dei dati sensibili)
		Boolean isEnabledExportXLS 		=  userVo.getDlGruppoModuliVo().getGruppoModuli().getIsEnabledExportXLS();
		session.setAttribute("isEnabledExportXLS", isEnabledExportXLS);
		Boolean isEnabledPKInExportXLS 	=  userVo.getDlGruppoModuliVo().getGruppoModuli().getIsEnabledPKInExportXLS();
		session.setAttribute("isEnabledPKInExportXLS", isEnabledPKInExportXLS);
		Boolean isEnabledExportPDF 		=  userVo.getDlGruppoModuliVo().getGruppoModuli().getIsEnabledExportPDF();
		session.setAttribute("isEnabledExportPDF", isEnabledExportPDF);
		Boolean isEnabledPKInExportPDF 	=  userVo.getDlGruppoModuliVo().getGruppoModuli().getIsEnabledPKInExportPDF();
		session.setAttribute("isEnabledPKInExportPDF", isEnabledPKInExportPDF);
		Boolean isEnabledExportPDFToTIFF 	=  userVo.getDlGruppoModuliVo().getGruppoModuli().getIsEnabledExportPDFToTIFF();
		session.setAttribute("isEnabledExportPDFToTIFF", isEnabledExportPDFToTIFF);
		
		Boolean isProfileBollaConsegnaReadOnly 	=  userVo.getDlGruppoModuliVo().getGruppoModuli().getIsBollaConsegnaReadOnly();
		session.setAttribute("isProfileBollaConsegnaReadOnly", isProfileBollaConsegnaReadOnly);
		Boolean isProfileBollaResaReadOnly 		=  userVo.getDlGruppoModuliVo().getGruppoModuli().getIsBollaResaReadOnly();
		session.setAttribute("isProfileBollaResaReadOnly", isProfileBollaResaReadOnly);

		//17/01/2016 - Inserito per gestire in CDL il profilo BASIC
		session.setAttribute("roleIdProfile", userVo.getDlGruppoModuliVo().getGruppoModuli().getId());
		session.setAttribute("roleNameProfile", userVo.getDlGruppoModuliVo().getGruppoModuli().getRoleName());
		
		
		
	}
	
	/**
	 * @param session
	 * @param codDpeWebEdicola
	 */
	private void setParametriEdicola(HttpSession session, Integer codDpeWebEdicola) {
		List<ParametriEdicolaDto> parametriEdicola = edicoleService.getParametriEdicola(codDpeWebEdicola);
		Map<String, ParametriEdicolaDto> mapParams = new HashMap<>(); 
		for (ParametriEdicolaDto dto : parametriEdicola) {
			if (dto.getCodParametro().equals(IGerivConstants.COD_PARAMETRO_SPUNTA_BOLLA_CONSEGNA)) {
				Boolean hasProfiloStarter = session.getAttribute("hasProfiloStarter") == null ? false : Boolean.parseBoolean(session.getAttribute("hasProfiloStarter").toString());
				if (hasProfiloStarter) {
					dto.setDefaultValue("false");
					dto.setValue("false");
				}
				mapParams.put(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA  + dto.getCodParametro(), dto);
			} else {
				mapParams.put(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA  + dto.getCodParametro(), dto);
			}
		}
		session.setAttribute(IGerivConstants.SESSION_VAR_MAP_PARAMETRI_EDICOLA, mapParams);
	}

	/**
	 * Setta un messaggio con i dei giorni che rimangono alla sospensione del
	 * servizio alla rivendita in una variabile di sessione che viene letta
	 * subito dopo il login..
	 * 
	 * @param Timestamp
	 *            dtSospensioneEdicola
	 * @param HttpSession
	 *            session
	 */
	private void setAlertNumeroGiorniAllaSospensione(HttpSession session, Timestamp dtSospensioneEdicola) {
		if (dtSospensioneEdicola != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dtSospensioneEdicola);
			cal.add(Calendar.DATE, -numGiorniAlertSospensione);
			Date now = new Date();
			if (now.after(cal.getTime())) {
				Calendar calDtSosp = Calendar.getInstance();
				Calendar calDtNow = Calendar.getInstance();
				calDtSosp.setTime(dtSospensioneEdicola);
				calDtNow.setTime(now);
				long giorniAllaSospensione = DateUtilities.getDifference(calDtNow, calDtSosp, TimeUnit.DAYS) + 1;
				session.setAttribute("alertSospensione", MessageFormat.format(IGerivMessageBundle.get("msg.giorni.alla.sospensione.edicola"), giorniAllaSospensione));
			}
		}
	}

	private class TestExclStrat implements ExclusionStrategy {
		
		public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }

        public boolean shouldSkipField(FieldAttributes f) {
            return (f.getDeclaringClass() == EmailDlDto.class && f.getName().equals("email"));
        }

	}
	
}
