package it.dpe.igeriv.job;

import static ch.lambdaj.Lambda.by;
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
import it.dpe.jms.dto.RichiestaRifornimentoJmsMessage;
import it.dpe.mail.MailingListService;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
public class IGerivJmsRichiesteRifornimentoExportJob extends QuartzJobBean {
	private final Logger log = Logger.getLogger(getClass());
	private Integer timeout;
	private SendGateway<RichiestaRifornimentoJmsMessage> sendGateway;
	private JmsService jmsService;
	private MailingListService mailingListService;
	
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		try {
			jmsService.updateEsportazioneRichiestaRifornimentoTemp();
			List<RichiestaRifornimentoJmsMessage> listEsportazioni = jmsService.getListRichiestaRifornimentoJmsMessage();
			Group<RichiestaRifornimentoJmsMessage> group = group(listEsportazioni, by(on(RichiestaRifornimentoJmsMessage.class).getCodFornitore()));
			for (Group<RichiestaRifornimentoJmsMessage> subgroup : group.subgroups()) {
				Map<String, Object> headers = new HashMap<String, Object>();
				List<RichiestaRifornimentoJmsMessage> findAll = subgroup.findAll();
				RichiestaRifornimentoJmsMessage first = findAll.get(0);
				Integer codFornitore = first.getCodFornitore();
				String channelName = MessageFormat.format(IGerivBatchConstants.CHANNEL_NAME_TEMPLATE_IGERIV_DL_RIFORNIMENTI, codFornitore);
				Object bean = null;
				try {
					bean = SpringContextManager.getService(channelName);
				} catch (NoSuchBeanDefinitionException e) {}
				if (bean != null) { 
					headers.put(JmsConstants.HEADER_KEY_CHANNEL_NAME, channelName);
					headers.put(JmsConstants.HEADER_KEY_TIPO_MESSAGGIO, JmsConstants.TIPO_MESSAGGIO_RICHIESTA_RIFORNIMENTO);
					headers.put(JmsConstants.HEADER_KEY_TIMEOUT, JmsConstants.TIMEOUT_SECONDS);
					Message<List<RichiestaRifornimentoJmsMessage>> msg = new GenericMessage<List<RichiestaRifornimentoJmsMessage>>(findAll, headers);
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
			        		log.error(MessageFormat.format(IGerivMessageBundle.get("error.export.jms.richieste.rifo.reply"), codFornitore), new Exception(descrizioneErrore));
			        		jmsService.updateEsportazioneRichiestaRifornimentoError(correlationId);
			        		String subject = IGerivMessageBundle.get("error.export.jms.email.error.receive.igeriv.dl.subject");
			        		String stackTrace = message.getHeaders().get("stackTrace").toString();
							String emailMsg = MessageFormat.format(IGerivMessageBundle.get("error.export.jms.email.error.desc"), descrizioneErrore, stackTrace);
							mailingListService.sendEmail(null, subject, emailMsg, true);
			        	} else {
			        		jmsService.updateEsportazioneRichiestaRifornimentoSuccess(correlationId);
			        	}
					} catch (Throwable e) {
						log.error(MessageFormat.format(IGerivMessageBundle.get("error.export.jms.richieste.rifo"), codFornitore), e);
						jmsService.updateEsportazioneRichiestaRifornimentoError(correlationId);
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

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public SendGateway<RichiestaRifornimentoJmsMessage> getSendGateway() {
		return sendGateway;
	}

	public void setSendGateway(SendGateway<RichiestaRifornimentoJmsMessage> sendGateway) {
		this.sendGateway = sendGateway;
	}

	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

	public JmsService getJmsService() {
		return jmsService;
	}

	public void setJmsService(JmsService jmsService) {
		this.jmsService = jmsService;
	}

}
