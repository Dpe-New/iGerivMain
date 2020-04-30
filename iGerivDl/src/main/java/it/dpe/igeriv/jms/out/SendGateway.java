package it.dpe.igeriv.jms.out;

import it.dpe.jms.dto.BaseJmsMessage;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.integration.Message;

/**
 * Interfaccia utilizzata nell'invio di messaggi JMS
 * (vedi integrationContext.xml)
 * 
 * @author romanom
 *
 */
public interface SendGateway<T extends BaseJmsMessage> {
	
	public Future<Message<BaseJmsMessage>> send(Message<List<T>> msg);
	
}
