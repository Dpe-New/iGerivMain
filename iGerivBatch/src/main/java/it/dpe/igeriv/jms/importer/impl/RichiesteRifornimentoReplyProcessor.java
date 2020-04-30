package it.dpe.igeriv.jms.importer.impl;

import it.dpe.igeriv.jms.importer.ReplyProcessor;
import it.dpe.igeriv.jms.service.JmsService;
import it.dpe.jms.dto.BaseJmsMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("ERRCReplyProcessor")
public class RichiesteRifornimentoReplyProcessor implements ReplyProcessor<BaseJmsMessage> {
	private final Logger log = Logger.getLogger(getClass());
	private final JmsService jmsService;
	
	@Autowired
	RichiesteRifornimentoReplyProcessor(JmsService jmsService) {
		this.jmsService = jmsService;
	}
	
	@Override
	public void process(String jmsCorrelationId) {
		log.info("Entered method RichiesteRifornimentoReplyProcessor.importa() with parameters jmsCorrelationId = " + jmsCorrelationId);
		jmsService.updateEsportazioneRichiestaRifornimentoSuccess(jmsCorrelationId);
	}

}
