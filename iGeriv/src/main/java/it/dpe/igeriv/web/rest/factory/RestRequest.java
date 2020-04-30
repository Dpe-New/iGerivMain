package it.dpe.igeriv.web.rest.factory;

import org.springframework.http.HttpHeaders;

/**
 * @author mromano
 *
 * @param <T>
 * @param <V>
 */
public interface RestRequest<T, V> {
	
	/**
	 * @param requestHeaders
	 * @param obj
	 * @param env
	 */
	void prepareRequest(HttpHeaders requestHeaders, V obj, String env);
	
}
