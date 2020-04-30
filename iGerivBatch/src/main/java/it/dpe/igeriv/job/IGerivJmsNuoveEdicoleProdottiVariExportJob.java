package it.dpe.igeriv.job;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.jms.out.SendGateway;
import it.dpe.igeriv.jms.service.JmsService;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.JmsConstants;
import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.jms.dto.BaseJmsMessage;
import it.dpe.jms.dto.NullJmsMessage;
import it.dpe.mail.MailingListService;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.integration.message.GenericMessage;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.google.common.base.Joiner;

/**
 * Classe job schedulata che estrae le edicola create oggi e 
 * invia un messaggio JMS al gestionale per inviare le anagrafiche dei prodotti vari.
 * La definizione del job è nel schedulerContext.xml.
 * 
 * @author romanom
 */
@Getter
@Setter
public class IGerivJmsNuoveEdicoleProdottiVariExportJob extends QuartzJobBean {
	private static final int TIMEOUT_SECONDS = 1200;
	private final Logger log = Logger.getLogger(getClass());
	private Integer timeout;
	private SendGateway<NullJmsMessage> sendGateway;
	private JmsService jmsService;
	private AgenzieService agenzieService;
	private EdicoleService edicoleService;
	private MailingListService mailingListService;
	private Joiner joiner = Joiner.on( "," ).skipNulls();
	
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		for (AnagraficaAgenziaVo agenzia : agenzieService.getAgenzieConFatturazione()) {
			try {
				String channelName = MessageFormat.format("jms{0}CmdSendChannel", agenzia.getCodFiegDl());
				Object bean = null;
				try {
					bean = SpringContextManager.getService(channelName);
				} catch (NoSuchBeanDefinitionException e) {
					continue;
				}
				if (bean != null) {
					Map<String, Object> headers = new HashMap<String, Object>();
					headers.put(JmsConstants.HEADER_KEY_TIPO_MESSAGGIO, JmsConstants.TIPO_MESSAGGIO_COMANDO_ESEGUI_ESPORTAZIONE_PRODOTTI);
					List<EdicolaDto> nuoveEdicole = edicoleService.getNuoveEdicole(agenzia.getCodFiegDl(), DateUtilities.floorDay(new Date()));
					if (nuoveEdicole != null && !nuoveEdicole.isEmpty()) {
						List<Integer> listCodiciWebEdicole = extract(nuoveEdicole, on(EdicolaDto.class).getCodEdicolaWeb());
						headers.put(JmsConstants.HEADER_KEY_CHANNEL_NAME, channelName);
						headers.put(JmsConstants.HEADER_KEY_TIMEOUT, TIMEOUT_SECONDS);		
						List<NullJmsMessage> listMessage = new ArrayList<NullJmsMessage>();
						listMessage.add(new NullJmsMessage());
						Message<List<NullJmsMessage>> msg = new GenericMessage<List<NullJmsMessage>>(listMessage, headers);
						Future<Message<BaseJmsMessage>> send = sendGateway.send(msg);
						Message<BaseJmsMessage> message = send.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
						int stato = new Integer(message.getHeaders().get("stato").toString());
			        	if (stato == JmsConstants.COD_STATO_IMPORTAZIONE_MESSAGGIO_ERROR || stato == JmsConstants.COD_STATO_IMPORTAZIONE_MESSAGGIO_WARNING) {
			        		String descrizioneErrore = message.getHeaders().get("descrizioneErrore").toString();
			        		log.error(MessageFormat.format(IGerivMessageBundle.get("error.jms.cmd.export.prodotti.vari.subject"), agenzia.getCodFiegDl()), new Exception(descrizioneErrore));
			        		String subject = MessageFormat.format(IGerivMessageBundle.get("error.jms.cmd.export.prodotti.vari.subject"), agenzia.getCodFiegDl());
			        		String stackTrace = message.getHeaders().get("stackTrace").toString();
							String emailMsg = MessageFormat.format(IGerivMessageBundle.get("error.jms.cmd.export.prodotti.vari.body"), listCodiciWebEdicole.toString(), agenzia.getCodFiegDl(), descrizioneErrore, stackTrace);
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

}
