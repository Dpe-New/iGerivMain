package it.dpe.igeriv.vendite.inputmodulelistener.impl;

import java.net.ConnectException;

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
import it.dpe.igeriv.vendite.inputmodulelistener.VenditeInputModuleListener;
import it.dpe.igeriv.web.rest.service.RestService;
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
@Component("MinicardInputModuleListener")
public class MinicardInputModuleListener implements VenditeInputModuleListener {
	private final Logger log = Logger.getLogger(getClass());
	private final RestService<String, VenditeParamDto> tokenRestService;
	private final RestService<HttpJsonResponse, VenditeParamDto> abbonamentiRicaricabiliRestService;
	
	@Autowired
	MinicardInputModuleListener(@Qualifier("TokenRestService") RestService<String, VenditeParamDto> tokenRestService, @Qualifier("AbbonamentiRicaricabiliRestService") RestService<HttpJsonResponse, VenditeParamDto> abbonamentiRicaricabiliRestService) {
		this.tokenRestService = tokenRestService;
		this.abbonamentiRicaricabiliRestService = abbonamentiRicaricabiliRestService;
	}
	
    @Override
	public VenditeCardResultDto execute(VenditeParamDto params) throws IGerivBusinessException {
    	try {
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
    		if (e instanceof ConnectException) {
    			throw new IGerivBusinessException(IGerivMessageBundle.get("igeriv.rtae.non.disponibile"));
    		} else {
    			throw new IGerivBusinessException(IGerivMessageBundle.get("igeriv.rtae.non.disponibile"));
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
			dto.setRitiri(content.getRitiri());
			dto.setEditori(content.getEditori());
			dto.setProdotti(content.getProdotti());
			dto.setTipiTessera(content.getTipiTessera());
		}
		return dto;
	}

	
}
