package it.dpe.jms.visitor;

import it.dpe.jms.dto.VenditaJmsMessage;

public interface JmsMessageVisitor {
	
	public void visit(VenditaJmsMessage message);
	
	public void visit(Object message);
	
}
