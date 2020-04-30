package it.dpe.igeriv.jms.in;

import it.dpe.igeriv.jms.importer.ReplyProcessor;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivBatchConstants;
import it.dpe.igeriv.util.JmsConstants;
import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.jms.dto.BaseJmsMessage;
import it.dpe.mail.MailingListService;

import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.jms.JmsHeaders;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * Riceve i messaggi dalla queue dei messaggi in timeout
 * 
 * @author romanom
 *
 */
@Component("IGerivJmsReplyFromDlTimeoutReceiver")
public class IGerivJmsReplyFromDlTimeoutReceiver {
	private final Logger log = Logger.getLogger(getClass());
	
	@Autowired
	private MailingListService mailingListService;
	
	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

	@SuppressWarnings("unchecked")
	public void receive(Message<? extends BaseJmsMessage> message) {
		try {
			if (message.getHeaders().get("tipoMessaggio") != null) {
				String tipoMessaggio = message.getHeaders().get("tipoMessaggio").toString();
				Integer codFornitore = Integer.parseInt(message.getHeaders().get("codFornitore").toString());
				log.info("Receiving reply timeout message tipo: " + tipoMessaggio + " from DL: " + codFornitore);
				String correlationId = message.getHeaders().get(JmsHeaders.CORRELATION_ID).toString();
				int stato = new Integer(message.getHeaders().get("stato").toString());
				if (stato != JmsConstants.COD_STATO_IMPORTAZIONE_MESSAGGIO_ERROR) {
					Object bean = null;
					try {
						bean = SpringContextManager.getService(tipoMessaggio + IGerivBatchConstants.REPLY_PROCESSOR_BEAN_ID_SUFFIX);
					} catch (NoSuchBeanDefinitionException e) {}
					try {
						if (!Strings.isNullOrEmpty(tipoMessaggio) && bean != null) {
							ReplyProcessor<BaseJmsMessage> processor = (ReplyProcessor<BaseJmsMessage>) bean;
							processor.process(correlationId);
							String subject = MessageFormat.format(IGerivMessageBundle.get("igeriv.jms.conferma.ricezione.timeout.msg.subject"), codFornitore);
							String emailMsg = MessageFormat.format(IGerivMessageBundle.get("igeriv.jms.conferma.ricezione.timeout.msg.body"), correlationId);
							mailingListService.sendEmail(null, subject, emailMsg, true);
						}
			    	} catch (Exception e) {
			    		log.error(MessageFormat.format(IGerivMessageBundle.get("error.export.jms.richieste.rifo.reply"), codFornitore), e);
			    	}
				}
			}
		} catch (Throwable e) {
			String subject = IGerivMessageBundle.get("error.jms.fatal.unexpected.subject");
			String emailMsg = MessageFormat.format(IGerivMessageBundle.get("error.jms.fatal.unexpected.body"), (e.getMessage() == null ? "" : e.getMessage()), StringUtility.getStackTrace(e));
			mailingListService.sendEmail(null, subject, emailMsg, true);
		}
	}

}
