package it.dpe.igeriv.web.rest.service;


import it.dpe.igeriv.web.rest.factory.RestRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Classe base che definisce la chiamata REST
 * 
 * @author mromano
 *
 * @param <T>
 * @param <V>
 */
class BaseRestService<T, V> {
	
	/**
	 * Esegue la chiamata REST
	 * 
	 * @param restRequest Il command che incapsula la logica per settare gli headers del messaggio 
	 * @param dto Oggetto dto con i valori
	 * @param url Url del servizio rest
	 * @param method Metodo Http del servizio rest
	 * @param responseType Oggeeto di ritorno del servizio rest
	 * @param env Id ambiente di deploy di iGeriv
	 * @return ResponseEntity<T>
	 */
	protected ResponseEntity<T> executeRestRequest(RestRequest<ResponseEntity<T>, V> restRequest, V dto, String url, HttpMethod method, Class<T> responseType, String env) {
		HttpHeaders requestHeaders = new HttpHeaders();
		restRequest.prepareRequest(requestHeaders, dto, env);
		HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
		return restTemplate.exchange(url, method, requestEntity, responseType);
	}
	
}
