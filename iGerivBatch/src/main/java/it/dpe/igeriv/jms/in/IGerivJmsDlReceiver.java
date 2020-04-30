package it.dpe.igeriv.jms.in;

import it.dpe.igeriv.bo.batch.IGerivImportBo;
import it.dpe.igeriv.jms.importer.Importer;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivBatchConstants;
import it.dpe.igeriv.util.JmsConstants;
import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.jms.dto.BaseJmsMessage;
import it.dpe.mail.MailingListService;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.jms.JmsHeaders;
import org.springframework.integration.message.GenericMessage;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * @author romanom
 *
 */
@Component("IGerivJmsDlReceiver")
public class IGerivJmsDlReceiver {
	private final Logger log = Logger.getLogger(getClass());
	
	@Autowired
	private MailingListService mailingListService;
	
	@Autowired
	private IGerivImportBo iGerivImportBo;
	
	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

	public IGerivImportBo getiGerivImportBo() {
		return iGerivImportBo;
	}

	public void setiGerivImportBo(IGerivImportBo iGerivImportBo) {
		this.iGerivImportBo = iGerivImportBo;
	}
	
	@SuppressWarnings("unchecked")
	public Message<? extends BaseJmsMessage> receive(Message<? extends BaseJmsMessage> message) {
		log.info("Receiving message from DL:" + message.toString());
		Message<BaseJmsMessage> msg = null;
		try {
			String tipoMessaggio = message.getHeaders().get(JmsConstants.HEADER_KEY_TIPO_MESSAGGIO).toString();
			Integer codFornitore = Integer.parseInt(message.getHeaders().get(JmsConstants.HEADER_KEY_COD_FORNITORE).toString());
			long timeout = message.getHeaders().get(JmsConstants.HEADER_KEY_TIMEOUT) != null ? Long.parseLong(message.getHeaders().get(JmsConstants.HEADER_KEY_TIMEOUT).toString()) : JmsConstants.TIMEOUT_SECONDS; 
			List<BaseJmsMessage> payload = (List<BaseJmsMessage>) message.getPayload();
			Map<String, Object> headers = new HashMap<String, Object>();
			headers.put(JmsConstants.HEADER_KEY_CHANNEL_NAME, MessageFormat.format(IGerivBatchConstants.CHANNEL_NAME_TEMPLATE_DL_IGERIV_REPLY, codFornitore));
			String msgId = message.getHeaders().get(JmsHeaders.MESSAGE_ID).toString();
			headers.put(JmsConstants.HEADER_KEY_TIPO_MESSAGGIO, tipoMessaggio);
			headers.put(JmsHeaders.CORRELATION_ID, msgId);
			if (message.getHeaders().containsKey(JmsConstants.HEADER_KEY_EXECUTION_TIME)) {
				headers.put(JmsConstants.HEADER_KEY_EXECUTION_TIME, message.getHeaders().get(JmsConstants.HEADER_KEY_EXECUTION_TIME));
			}
			headers.put(JmsConstants.HEADER_KEY_STATO, JmsConstants.COD_STATO_IMPORTAZIONE_MESSAGGIO_SUCCESS);
			try {
				Object bean = null;
				try {
					bean = SpringContextManager.getService(tipoMessaggio + IGerivBatchConstants.IMPORTER_BEAN_ID_SUFFIX);
				} catch (NoSuchBeanDefinitionException e) {}
				if (!Strings.isNullOrEmpty(tipoMessaggio) && bean != null) {
					Importer<BaseJmsMessage> importer = (Importer<BaseJmsMessage>) bean;
					importer.importa(payload, codFornitore);
				}
	    	} catch (Exception e) {
	    		headers.put(JmsConstants.HEADER_KEY_STATO, JmsConstants.COD_STATO_IMPORTAZIONE_MESSAGGIO_ERROR);
	    		headers.put(JmsConstants.HEADER_KEY_DESCRIZIONE_ERRORE, (e.getMessage() == null ? "" : e.getMessage()));
	    		headers.put(JmsConstants.HEADER_KEY_STACK_TRACE, StringUtility.getStackTrace(e));
	    	}
			BaseJmsMessage bjm = new BaseJmsMessage(MessageFormat.format(IGerivMessageBundle.get("igeriv.jms.conferma.ricezione.msg"), codFornitore));
			Date timestamp = new Date(Long.parseLong(message.getHeaders().get(JmsConstants.HEADER_KEY_TIMESTAMP).toString()));
			if (isMessageExpired(timestamp, timeout)) {
				headers.put(JmsConstants.HEADER_KEY_CHANNEL_NAME, MessageFormat.format(IGerivBatchConstants.CHANNEL_NAME_TEMPLATE_DL_IGERIV_REPLY_TIMEOUT, codFornitore));
			}
			msg = new GenericMessage<BaseJmsMessage>(bjm, headers);
			log.info("Sending reply message to DL:" + msg.toString());
		} catch (Throwable e) {
			String subject = IGerivMessageBundle.get("error.jms.fatal.unexpected.subject");
			String emailMsg = MessageFormat.format(IGerivMessageBundle.get("error.jms.fatal.unexpected.body"), (e.getMessage() == null ? "" : e.getMessage()), StringUtility.getStackTrace(e));
			mailingListService.sendEmail(null, subject, emailMsg, true);
		}
		return msg;
	}
	
	/**
	 * @param msgDate
	 * @param timeout 
	 * @return
	 */
	private boolean isMessageExpired(Date msgDate, long timeout) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(msgDate);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(new Date());
		long difference = DateUtilities.getDifference(cal1, cal2, TimeUnit.SECONDS) - 1l;
		return difference > timeout;
	}
	
}
