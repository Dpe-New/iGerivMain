package it.dpe.igeriv.jms.interceptor;

import it.dpe.igeriv.util.EncryptUtils;

import java.util.Arrays;

import org.apache.activemq.broker.BrokerPluginSupport;
import org.apache.activemq.broker.ProducerBrokerExchange;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.Message;
import org.apache.activemq.util.ByteSequence;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings("unchecked")
public class MessageEncriptionInterceptor extends BrokerPluginSupport {
	private static final Log LOG = LogFactory.getLog(MessageEncriptionInterceptor.class);
	
	@Override
	public void send(ProducerBrokerExchange producerExchange, Message messageSend) throws Exception {
		LOG.info("MessageEncriptionInterceptor.send");
		System.out.println("MessageEncriptionInterceptor.send");
		if (messageSend instanceof ActiveMQObjectMessage) {
			ActiveMQObjectMessage msg = (ActiveMQObjectMessage) messageSend;
			String data = Arrays.toString(msg.getContent().data);
			LOG.info("ActiveMQObjectMessage data=" + data);
			String enc = EncryptUtils.encrypt(EncryptUtils.getEncrypter(), data);
			LOG.info("ActiveMQObjectMessage enc data=" + enc);
			msg.setContent(new ByteSequence(enc.getBytes("UTF-8")));
		}
		super.send(producerExchange, messageSend);
	}

}
