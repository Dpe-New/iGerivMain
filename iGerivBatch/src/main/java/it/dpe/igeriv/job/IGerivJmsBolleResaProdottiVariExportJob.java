package it.dpe.igeriv.job;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.jms.out.SendGateway;
import it.dpe.igeriv.jms.service.JmsService;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivBatchConstants;
import it.dpe.igeriv.util.JmsConstants;
import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.jms.dto.BaseJmsMessage;
import it.dpe.jms.dto.BollaResaProdottiVariJmsMessage;
import it.dpe.mail.MailingListService;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.integration.Message;
import org.springframework.integration.jms.JmsHeaders;
import org.springframework.integration.message.GenericMessage;
import org.springframework.scheduling.quartz.QuartzJobBean;

import ch.lambdaj.group.Group;

/**
 * Classe job schedulata che esegue l'esportazione dei ritiri RTAE verso iGeriv.
 * La definizione del job è nel schedulerContext.xml.
 * 
 * @author romanom
 */
@Getter
@Setter
public class IGerivJmsBolleResaProdottiVariExportJob extends QuartzJobBean {
	private final Logger log = Logger.getLogger(getClass());
	private Integer timeout;
	private SendGateway<BollaResaProdottiVariJmsMessage> sendGateway;
	private JmsService jmsService;
	private MailingListService mailingListService;
	
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		try {
			jmsService.updateEsportazioneResaTemp();
			List<BollaResaProdottiVariJmsMessage> listEsportazioni = jmsService.getListReseProdottiVariJmsMessage();
			Group<BollaResaProdottiVariJmsMessage> group = group(listEsportazioni, by(on(BollaResaProdottiVariJmsMessage.class).getCodFornitore()));
			for (Group<BollaResaProdottiVariJmsMessage> subgroup : group.subgroups()) {
				Map<String, Object> headers = new HashMap<String, Object>();
				List<BollaResaProdottiVariJmsMessage> findAll = subgroup.findAll();
				BollaResaProdottiVariJmsMessage first = findAll.get(0);
				List<Long> listIdDocumento = extract(findAll, on(BollaResaProdottiVariJmsMessage.class).getIdDocumento());
				Integer codFornitore = first.getCodFornitore();
				String channelName = MessageFormat.format(IGerivBatchConstants.CHANNEL_NAME_TEMPLATE_IGERIV_DL_RIFORNIMENTI, codFornitore);
				Object bean = null;
				try {
					bean = SpringContextManager.getService(channelName);
				} catch (NoSuchBeanDefinitionException e) {}
				if (bean != null) { 
					headers.put(JmsConstants.HEADER_KEY_CHANNEL_NAME, channelName);
					headers.put(JmsConstants.HEADER_KEY_TIPO_MESSAGGIO, JmsConstants.TIPO_MESSAGGIO_RESA_PRODOTTI_VARI);
					headers.put(JmsConstants.HEADER_KEY_TIMEOUT, JmsConstants.TIMEOUT_SECONDS);
					Message<List<BollaResaProdottiVariJmsMessage>> msg = new GenericMessage<List<BollaResaProdottiVariJmsMessage>>(findAll, headers);
					String correlationId = null;
					try { 
						log.info("Sending Message to DL:" + msg.toString());
						Future<Message<BaseJmsMessage>> send = sendGateway.send(msg);
						Message<BaseJmsMessage> message = send.get(JmsConstants.TIMEOUT_SECONDS, TimeUnit.SECONDS); 
						log.info("Reply message from DL:" + message.toString());
			        	int stato = new Integer(message.getHeaders().get("stato").toString());
			        	correlationId = message.getHeaders().get(JmsHeaders.CORRELATION_ID) != null ? message.getHeaders().get(JmsHeaders.CORRELATION_ID).toString() : correlationId;
			        	if (stato == JmsConstants.COD_STATO_IMPORTAZIONE_MESSAGGIO_ERROR) {
			        		String descrizioneErrore = message.getHeaders().get("descrizioneErrore").toString();
			        		log.error(MessageFormat.format(IGerivMessageBundle.get("error.export.jms.bolle.resa.prodotti.vari.reply"), codFornitore), new Exception(descrizioneErrore));
			        		jmsService.updateEsportazioneResaError(correlationId, listIdDocumento);
			        		String subject = IGerivMessageBundle.get("error.export.jms.email.error.receive.igeriv.dl.subject");
			        		String stackTrace = message.getHeaders().get("stackTrace").toString();
							String emailMsg = MessageFormat.format(IGerivMessageBundle.get("error.export.jms.email.error.desc"), descrizioneErrore, stackTrace);
							mailingListService.sendEmail(null, subject, emailMsg, true);
			        	} else {
			        		jmsService.updateEsportazioneResaSuccess(correlationId, listIdDocumento);
			        	}
					} catch (Throwable e) {
						log.error(MessageFormat.format(IGerivMessageBundle.get("error.export.jms.bolle.resa.prodotti.vari"), codFornitore), e);
						jmsService.updateEsportazioneResaError(correlationId, listIdDocumento);
						String subject = MessageFormat.format(IGerivMessageBundle.get("error.export.jms.email.error.timeout.igeriv.dl.subject"), codFornitore);
						String emailMsg = MessageFormat.format(IGerivMessageBundle.get("error.export.jms.email.error.desc"), e.getMessage(), StringUtility.getStackTrace(e));
						mailingListService.sendEmail(null, subject, emailMsg, true);
					}
				}
			}
		} catch (Throwable e) {
			log.error(IGerivMessageBundle.get("error.jms.fatal"), e);
			try {
				String subject = IGerivMessageBundle.get("error.jms.fatal.unexpected.subject");
				String emailMsg = MessageFormat.format(IGerivMessageBundle.get("error.jms.fatal.unexpected.body"), (e.getMessage() == null ? "" : e.getMessage()), StringUtility.getStackTrace(e));
				mailingListService.sendEmail(null, subject, emailMsg, true);
			} catch (Throwable e1) {
    			log.error(IGerivMessageBundle.get("imp.error.send.email"), e1);
    		}
		}
	}

}
