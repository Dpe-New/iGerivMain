package it.dpe.jms.receive;

import static ch.lambdaj.Lambda.forEach;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.notNullValue;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.jms.JmsHeaders;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.vendite.VenditeService;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.JmsConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.VenditaDettaglioVo;
import it.dpe.igeriv.vo.VenditaVo;
import it.dpe.jms.dto.VenditeJmsReplyMessage;
import it.dpe.service.mail.MailingListService;

/**
 * Riceve i messaggi dalla queue dei messaggi in timeout
 * 
 * @author romanom
 *
 */
@Component("VenditeReplyReceiver")
public class VenditeReplyReceiver implements ReplyReceiver<VenditeJmsReplyMessage> {
	private final Logger log = Logger.getLogger(getClass());
	private final VenditeService venditeService;
	private final MailingListService mailingListService;
	
	@Autowired
	VenditeReplyReceiver(VenditeService venditeService, MailingListService mailingListService) {
		this.venditeService = venditeService;
		this.mailingListService = mailingListService;
	}

	@Override
	public void receive(Message<VenditeJmsReplyMessage> message) {
		try {
			if (message.getHeaders().get("tipoMessaggio") != null) {
				String tipoMessaggio = message.getHeaders().get("tipoMessaggio").toString();
				log.info("Receiving reply timeout message tipo: " + tipoMessaggio);
				String correlationId = message.getHeaders().get(JmsHeaders.CORRELATION_ID).toString();
				int stato = new Integer(message.getHeaders().get("stato").toString());
				if (stato != JmsConstants.COD_STATO_IMPORTAZIONE_MESSAGGIO_ERROR) {
					try {
						if (message.getPayload() != null && message.getPayload().getCodVendita() != null) {
							if (message.getPayload().getOperation().equals(JmsConstants.OPERATION_INSERT)) {
								VenditaVo vendita = venditeService.getContoVendite(message.getPayload().getCodVendita());
								if (vendita != null) {
									vendita.setTrasferitaGestionale(IGerivConstants.INDICATORE_RECORD_TRASFERITO);
									vendita.setCorrelationId(correlationId);
									Timestamp sysdate = venditeService.getSysdate();
									vendita.setDataTrasfGestionale(sysdate);
									List<VenditaDettaglioVo> dettagli = select(vendita.getListVenditaDettaglio(), notNullValue());
									forEach(dettagli, VenditaDettaglioVo.class).setTrasferitaGestionale(true);
									forEach(dettagli, VenditaDettaglioVo.class).setDataTrasfGestionale(sysdate);
									venditeService.saveBaseVo(vendita);
								}
							}
						}
			    	} catch (Exception e) {
						log.error(MessageFormat.format(IGerivMessageBundle.get("error.ricezione.jms.vendite.reply"), message.getPayload().getCodFornitore()), e);
			    		String subject = MessageFormat.format(IGerivMessageBundle.get("error.jms.conferma.ricezione.vendite.msg.subject"), message.getPayload().getCodFornitore());
						String emailMsg = MessageFormat.format(IGerivMessageBundle.get("error.jms.conferma.ricezione.vendite.msg.body"), (Strings.isNullOrEmpty(e.getMessage()) ? "" : e.getMessage()), StringUtility.getStackTrace(e));
						mailingListService.sendEmail(null, subject, emailMsg, true);
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
