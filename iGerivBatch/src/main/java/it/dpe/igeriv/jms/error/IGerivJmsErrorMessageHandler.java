package it.dpe.igeriv.jms.error;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.mail.MailingListService;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHandlingException;
import org.springframework.integration.MessageHeaders;
import org.springframework.stereotype.Component;

/**
 * Ricevitore della fila (queue) di messaggi di errore nel scambio di mesaggi JMS tra iGeriv e RTAE.
 * (vedi integrationContext.xml) 
 * 
 * @author romanom
 *
 */
@Component("IGerivErrorMessageHandler")
public class IGerivJmsErrorMessageHandler {
	@Autowired
	private MailingListService mailingListService;
	
	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}
	
	public void onMessageFailed(Message<?> message) {
		try {
			String subject = IGerivMessageBundle.get("error.export.jms.error.connection.send.subject");
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
			String emailMsg = MessageFormat.format(IGerivMessageBundle.get("error.export.jms.error.connection.send.body"), headers, body, errMsg, stackTrace);
			mailingListService.sendEmail(null, subject, emailMsg, true);
		} catch (Throwable e) {
			String subject = IGerivMessageBundle.get("error.jms.fatal.unexpected.subject");
			String emailMsg = MessageFormat.format(IGerivMessageBundle.get("error.jms.fatal.unexpected.body"), (e.getMessage() == null ? "" : e.getMessage()), StringUtility.getStackTrace(e));
			mailingListService.sendEmail(null, subject, emailMsg, true);
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
