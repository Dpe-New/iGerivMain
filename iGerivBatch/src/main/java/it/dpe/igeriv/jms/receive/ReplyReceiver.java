package it.dpe.igeriv.jms.receive;

import it.dpe.jms.dto.BaseJmsMessage;

import org.springframework.integration.Message;

public interface ReplyReceiver<T extends BaseJmsMessage> {

	void receive(Message<T> message);

}