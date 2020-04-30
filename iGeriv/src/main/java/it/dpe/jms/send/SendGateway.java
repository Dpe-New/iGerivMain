package it.dpe.jms.send;

import org.springframework.integration.Message;

/**
 * Interfaccia utilizzata nell'invio di messaggi JMS
 * (vedi integrationContext.xml)
 * 
 * @author romanom
 *
 */
public interface SendGateway<T> {
	
	public void send(Message<T> msg);
	
}
