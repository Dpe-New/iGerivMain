package it.dpe.igeriv.vendite.inputmodulelistener.impl;

import java.net.ConnectException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import com.google.common.base.Strings;

import it.dpe.igeriv.dto.VenditeCardResultDto;
import it.dpe.igeriv.dto.VenditeParamDto;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.igeriv.vendite.inputmodulelistener.VenditeInputModuleListener;
import it.dpe.igeriv.web.rest.service.RestService;
import it.dpe.service.mail.MailingListService;
import it.dpe.ws.dto.HttpJsonResponse;
import it.dpe.ws.dto.HttpJsonResponse.Content;
import it.dpe.ws.dto.PubblicazioneVo;


/**
 * Classe per la gestione dell'evento di tipo tessera abbonamento/ricaricabile.
 * Chiama il rest service per la consegna abbonamento/ricaricabile su RTAE.
 * 
 * @author romanom
 *
 */
@Component("MinicardPLGInputModuleListener")
public class MinicardPLGInputModuleListener implements VenditeInputModuleListener {
	private static Calendar lastExceptionMail = null;
	private final Logger log = Logger.getLogger(getClass());
	private final RestService<String, VenditeParamDto> tokenRestService;
	private final RestService<HttpJsonResponse, VenditeParamDto> abbonamentiRicaricabiliRestService;
	
	@Autowired
	MinicardPLGInputModuleListener(@Qualifier("PLGTokenRestService") RestService<String, VenditeParamDto> tokenRestService, @Qualifier("AbbonamentiRicaricabiliPLGRestService") RestService<HttpJsonResponse, VenditeParamDto> abbonamentiRicaricabiliRestService) {
		this.tokenRestService = tokenRestService;
		this.abbonamentiRicaricabiliRestService = abbonamentiRicaricabiliRestService;
	}
	
    @Override
	public VenditeCardResultDto execute(VenditeParamDto params) throws IGerivBusinessException {
    	try {
    		if (params.getOperation().equals("")) {
    			
    		}
	    	ResponseEntity<String> tokenResponse = tokenRestService.getEntity(params);
			String token = Strings.isNullOrEmpty(params.getToken()) ? (tokenResponse != null && tokenResponse.getHeaders() != null && tokenResponse.getHeaders().getFirst(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY) != null ? tokenResponse.getHeaders().getFirst(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY).toString() : null) : params.getToken();
	    	boolean firstCall = true;
	    	ResponseEntity<HttpJsonResponse> responseEntity = null;
	    	try {
	    		params.setToken(token);
	    		responseEntity = abbonamentiRicaricabiliRestService.getEntity(params);
				if (responseEntity != null && responseEntity.getHeaders() != null && responseEntity.getHeaders().getFirst(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY) != null) {
					token = responseEntity.getHeaders().getFirst(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY).toString().replaceFirst("SPRING_SECURITY_REMEMBER_ME_COOKIE:", "").trim();
					params.setToken(token);
				}
	    	} catch (HttpStatusCodeException e) {
				if (e.getStatusCode() == HttpStatus.NETWORK_AUTHENTICATION_REQUIRED || e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
					tokenResponse = tokenRestService.getEntity(params);
					token = Strings.isNullOrEmpty(params.getToken()) ? (tokenResponse != null && tokenResponse.getHeaders() != null && tokenResponse.getHeaders().getFirst(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY) != null ? tokenResponse.getHeaders().getFirst(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY).toString() : null) : params.getToken();
					if (!Strings.isNullOrEmpty(token) && firstCall) {
						firstCall = false;
						params.setToken(token);
						responseEntity = abbonamentiRicaricabiliRestService.getEntity(params);
						params.setToken(token);
					}
				}
				else {
					throw e;
				}
			}
	    	return buildVenditeCardResultDto(responseEntity, params);
    	} catch (Throwable e) {
    		log.error("Errore in MinicardInputModuleListener", e);
    		
    		checkAndSendExceptionMail();
    		
    		if (e instanceof ConnectException) {
    			throw new IGerivBusinessException(IGerivMessageBundle.get("igeriv.plg.non.disponibile"));
    		} else {
    			throw new IGerivBusinessException(IGerivMessageBundle.get("igeriv.plg.non.disponibile"));
    		}
    	}
    }
    
	/**
	 * @param responseEntity
	 * @param params 
	 * @return
	 */
	private VenditeCardResultDto buildVenditeCardResultDto(ResponseEntity<HttpJsonResponse> responseEntity, VenditeParamDto params) {
		VenditeCardResultDto dto = null;
		if (responseEntity != null) {
			dto = new VenditeCardResultDto();
			Content content = responseEntity.getBody().getContent();
			dto.setType(VenditeCardResultDto.ResultType.valueOf(content.getType()));
			dto.setBarcode(dto.getBarcode());
			dto.setCodCliente(dto.getCodCliente());
			dto.setResult(content);
			dto.setListRicariche(content.getListRicariche());
			dto.setIdEditore(content.getIdEditore());
			dto.setIdProdotto(content.getIdProdotto());
			dto.setListStatoTessera(content.getListStatoTessera());
			dto.setMsgParams(content.getMsgParams());
			dto.setPuoiRicaricare(Boolean.parseBoolean(Strings.isNullOrEmpty(content.getPuoiRicaricare()) ? "false" : content.getPuoiRicaricare()));
			PubblicazioneVo pubblicazione = content.getPubblicazione();
			if (pubblicazione != null) {
				pubblicazione.setQuantita(content.getMsgParams() != null && content.getMsgParams().get("copieDaConsegnare") != null ? new Integer(content.getMsgParams().get("copieDaConsegnare")) : 1);
			}
			dto.setPubblicazione(pubblicazione);
			dto.setToken(params.getToken());
			dto.setListVenditeAbbonatiRivPeriodo(content.getListVenditeAbbonatiRivPeriodo());
			dto.setListVenditeMinicardRivPeriodo(content.getListVenditeMinicardRivPeriodo());
			dto.setListRicaricheMinicardRivPeriodo(content.getListRicaricheMinicardRivPeriodo());
			dto.setPlgBarcodeCollaterale(content.getBarcodeCollaterale());
			dto.setPlgTitoloCollaterale(content.getTitoloCollaterale());
			dto.setPlgNumeroCollaterale(content.getNumeroCollaterale());
			dto.setPlgIdCollaterale(content.getIdCollaterale());
		}
		return dto;
	}

	
	private void checkAndSendExceptionMail() {
		Boolean sendExceptionMail = true;
		if (lastExceptionMail != null) {
			Calendar current = new GregorianCalendar();
			current.setTimeInMillis(System.currentTimeMillis());
			if (lastExceptionMail.get(Calendar.YEAR) != current.get(Calendar.YEAR) || lastExceptionMail.get(Calendar.DAY_OF_YEAR) != current.get(Calendar.DAY_OF_YEAR)) {
				lastExceptionMail = current;
			}
			else {
				int deltaH = current.get(Calendar.HOUR_OF_DAY) - lastExceptionMail.get(Calendar.HOUR_OF_DAY);
				if (deltaH > 0) {
					int deltaMinLast = 60 - lastExceptionMail.get(Calendar.MINUTE);
					int totalMins = ((deltaH - 1) * 60) + deltaMinLast + current.get(Calendar.MINUTE);
					if (totalMins > 180) {
						lastExceptionMail = current;
					}
					else {
						sendExceptionMail = false;
					}
				}
				else {
					sendExceptionMail = false;
				}
			}
		}
		else {
			lastExceptionMail = new GregorianCalendar();
			lastExceptionMail.setTimeInMillis(System.currentTimeMillis());
		}
		
		if (sendExceptionMail) {
			MailingListService mailingListService = (MailingListService)SpringContextManager.getSpringContext().getBean("MailingListService");
			mailingListService.sendEmail(new String[] {""}, IGerivMessageBundle.get("igeriv.plg.non.disponibile"), IGerivMessageBundle.get("igeriv.plg.non.disponibile"), false);
		}
	}
	
}
