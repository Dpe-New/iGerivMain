package it.dpe.igeriv.jms.importer;

import it.dpe.jms.dto.BaseJmsMessage;

public interface ReplyProcessor<T extends BaseJmsMessage> {
	
	/**
	 * @param message
	 * @param jmsCorrelationId 
	 */
	public void process(String jmsCorrelationId);
	
}
