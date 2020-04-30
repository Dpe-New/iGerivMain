package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.visitor.ActionVisitor;


public interface VisitableAction {
	
	public void accept(ActionVisitor visitor);
	
}
