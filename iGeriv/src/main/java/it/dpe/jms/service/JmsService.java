package it.dpe.jms.service;


public interface JmsService<T> {
	
	public void send(T object);
	
}
