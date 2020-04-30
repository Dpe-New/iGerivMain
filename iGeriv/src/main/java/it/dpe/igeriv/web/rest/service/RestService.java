package it.dpe.igeriv.web.rest.service;

import org.springframework.http.ResponseEntity;

/**
 * @author mromano
 *
 * @param <T>
 * @param <V>
 */
public interface RestService<T, V> {
	
	public ResponseEntity<T> getEntity(V params);
	
	public ResponseEntity<T> putEntity(V entity);
	
	
	
	
	
}
