package it.dpe.igeriv.jms.importer;

import it.dpe.jms.dto.BaseJmsMessage;

import java.util.List;

public interface Importer<T extends BaseJmsMessage> {
	
	/**
	 * @param message
	 * @param codFornitore 
	 * @param codEdicolaDlFilter 
	 * @param jmsCorrelationId 
	 */
	public void importa(List<T> message, Integer codFornitore);
	
}
