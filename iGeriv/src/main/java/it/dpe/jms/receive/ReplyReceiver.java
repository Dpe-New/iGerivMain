package it.dpe.jms.receive;

import org.springframework.integration.Message;

import it.dpe.jms.dto.BaseJmsMessage;

public interface ReplyReceiver<T extends BaseJmsMessage> {

	void receive(Message<T> message);

}