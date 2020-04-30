package it.dpe.jms.factory;

import java.text.MessageFormat;
import java.util.Map;

import com.google.common.base.Strings;

import it.dpe.igeriv.util.IGerivEdicoleConstants;
import it.dpe.igeriv.util.JmsConstants;
import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.jms.dto.VenditaJmsMessage;

public class JmsRequestFactory<T, V> {
	
	public static JmsRequest<VenditaJmsMessage> prepareVenditeJmsRequest(final String methodName) {
		JmsRequest<VenditaJmsMessage> request = new JmsRequest<VenditaJmsMessage>() {
			@Override
			public void prepareRequest(Map<String, Object> headers, VenditaJmsMessage msg) {
				String channelName = MessageFormat.format(IGerivEdicoleConstants.CHANNEL_NAME_JMS_VENDITE_SEND_CHANNEL, msg.getCodFiegDl());
				SpringContextManager.getService(channelName);
				headers.put(JmsConstants.HEADER_KEY_CHANNEL_NAME, channelName);
				headers.put(JmsConstants.HEADER_KEY_TIPO_MESSAGGIO, JmsConstants.TIPO_MESSAGGIO_VENDITE);
				headers.put(JmsConstants.HEADER_KEY_COD_FORNITORE, msg.getCodFiegDl());
				if (methodName != null && (methodName.contains("delete") || methodName.contains("update")) && !Strings.isNullOrEmpty(msg.getCorrelationId())) {
					headers.put(JmsConstants.HEADER_KEY_OLD_CORRELATION_ID, msg.getCorrelationId());
				}
			}
		};
		return request;
	}
	
}
