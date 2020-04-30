package it.dpe.jms.dto;

import it.dpe.jms.visitor.JmsMessageVisitor;


/**
 * @author mromano
 *
 */
public interface VisitableJmsMessage {
	
	public void accept(JmsMessageVisitor visitor);
	
}
