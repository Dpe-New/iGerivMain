package it.dpe.igeriv.jms.fake;

import it.dpe.igeriv.jms.out.SendGateway;
import it.dpe.jms.dto.BaseJmsMessage;
import it.dpe.jms.dto.NullJmsMessage;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.integration.Message;

public class SendGatewayFakeImpl implements SendGateway<NullJmsMessage> {

	@Override
	public Future<Message<BaseJmsMessage>> send(Message<List<NullJmsMessage>> msg) {
		return null;
	}
	
}
