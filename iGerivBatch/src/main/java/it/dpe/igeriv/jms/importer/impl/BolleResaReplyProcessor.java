package it.dpe.igeriv.jms.importer.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.jms.importer.ReplyProcessor;
import it.dpe.igeriv.jms.service.JmsService;
import it.dpe.jms.dto.BaseJmsMessage;

@Component("RSReplyProcessor")
public class BolleResaReplyProcessor implements ReplyProcessor<BaseJmsMessage> {
	private final Logger log = Logger.getLogger(getClass());
	private final JmsService jmsService;
	
	@Autowired
	BolleResaReplyProcessor(JmsService jmsService) {
		this.jmsService = jmsService;
	}

	@Override
	public void process(String jmsCorrelationId) {
		log.info("Entered method BolleResaReplyProcessor.importa() with parameters jmsCorrelationId = " + jmsCorrelationId);
		jmsService.updateEsportazioneResaSuccess(jmsCorrelationId, null);
	}

}
