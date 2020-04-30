package it.dpe.igeriv.visitor;

import it.dpe.igeriv.web.actions.ReportAction;
import it.dpe.igeriv.web.actions.ReportProdottiVariAction;

public interface ActionVisitor {
	
	public void visit(ReportAction action);
	
	public void visit(ReportProdottiVariAction action);
	
	public void visit(Object action);
	
}
