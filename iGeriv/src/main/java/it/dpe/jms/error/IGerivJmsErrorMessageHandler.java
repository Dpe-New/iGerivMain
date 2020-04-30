package it.dpe.jms.error;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHandlingException;
import org.springframework.integration.MessageHeaders;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.service.mail.MailingListService;

/**
 * Ricevitore della fila (queue) di messaggi di errore nel scambio di mesaggi JMS.
 * 
 * @author romanom
 *
 */
@Component("IGerivErrorMessageHandler")
public class IGerivJmsErrorMessageHandler {
	private final MailingListService mailingListService;
	
	@Autowired
	IGerivJmsErrorMessageHandler(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}
	
	public void onMessageFailed(Message<?> message) {
		try {
			String headers = extractHeaders(message.getHeaders());
			String body = "";
			String errMsg = "";
			String stackTrace = "";
			if (message.getPayload() instanceof MessageHandlingException) {
				MessageHandlingException payload = (MessageHandlingException) message.getPayload();
				body = extractBody(payload.getFailedMessage());
				errMsg = payload.getMessage();
				stackTrace = StringUtility.getStackTrace(payload.fillInStackTrace());
			} else {
				errMsg = message.getPayload().toString();
			}
			String emailMsg = MessageFormat.format(IGerivMessageBundle.get("error.jms.fatal.body"), headers, body, errMsg, stackTrace);
			mailingListService.sendEmail(null, IGerivMessageBundle.get("error.jms.fatal"), emailMsg, true);
		} catch (Throwable e) {
			String emailMsg = MessageFormat.format(IGerivMessageBundle.get("error.jms.fatal.unexpected.body"), (e.getMessage() == null ? "" : e.getMessage()), StringUtility.getStackTrace(e));
			mailingListService.sendEmail(null, IGerivMessageBundle.get("error.jms.fatal"), emailMsg, true);
		}
	}

	private String extractHeaders(MessageHeaders messageHeaders) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, Object> entry : messageHeaders.entrySet()) {
			sb.append(entry.getKey() + " : " + entry.getValue() + System.getProperty("line.separator"));
		}
		return sb.toString();
	}
	
	private String extractBody(Message<?> failedMessage) {
		StringBuffer sb = new StringBuffer();
		Object payload = failedMessage.getPayload();
		if (payload instanceof Collection) {
			Collection<?> collection = (Collection<?>) payload;
			int i = 0;
			for (Object obj : collection) {
				sb.append("[" + i++ + "] " + obj.toString() + System.getProperty("line.separator"));
			}
		} else {
			return failedMessage.toString();
		}
		return sb.toString();
	}
}
