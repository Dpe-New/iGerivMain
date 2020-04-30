package it.dpe.jms.service;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.message.GenericMessage;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.JmsConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.jms.factory.JmsRequest;
import it.dpe.jms.send.SendGateway;
import it.dpe.service.mail.MailingListService;

@Component
public class BaseJmsService<T> {
	private final Logger log = Logger.getLogger(getClass());
	private final SendGateway<T> sendGateway;
	private final MailingListService mailingListService;
	
	@Autowired
	BaseJmsService(SendGateway<T> sendGateway, MailingListService mailingListService) {
		this.sendGateway = sendGateway;
		this.mailingListService = mailingListService;
	}
	
	public void send(T message, JmsRequest<T> jmsRequest) {
		Map<String, Object> headers = new HashMap<String, Object>();
		try { 
			jmsRequest.prepareRequest(headers, message);
			Message<T> msg = new GenericMessage<T>(message, headers);
			sendGateway.send(msg);
		} catch (NoSuchBeanDefinitionException e) {
			log.error(e);
		} catch (Throwable e) {
			String codDl = headers.get(JmsConstants.HEADER_KEY_COD_FORNITORE) != null ? headers.get(JmsConstants.HEADER_KEY_COD_FORNITORE).toString() : "" ;
			String tipoMessaggio = headers.get(JmsConstants.HEADER_KEY_TIPO_MESSAGGIO) != null ? headers.get(JmsConstants.HEADER_KEY_TIPO_MESSAGGIO).toString() : "" ;
			log.error(MessageFormat.format(IGerivMessageBundle.get("error.invio.jms.dati"), codDl, tipoMessaggio), e);
    		String subject = MessageFormat.format(IGerivMessageBundle.get("error.invio.jms.dati.subject"), codDl, tipoMessaggio);
			String emailMsg = MessageFormat.format(IGerivMessageBundle.get("error.invio.jms.dati.body"), (Strings.isNullOrEmpty(e.getMessage()) ? "" : e.getMessage()), StringUtility.getStackTrace(e));
			mailingListService.sendEmail(null, subject, emailMsg, true);
		}
	}
	
}
