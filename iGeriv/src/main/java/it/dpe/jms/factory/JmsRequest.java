package it.dpe.jms.factory;

import java.util.Map;


public interface JmsRequest<T> {
	
	void prepareRequest(Map<String, Object> headers, T vo);
	
}
