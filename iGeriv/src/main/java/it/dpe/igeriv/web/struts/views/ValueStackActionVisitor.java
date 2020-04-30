package it.dpe.igeriv.web.struts.views;

import java.util.List;

import it.dpe.igeriv.visitor.ActionVisitor;
import it.dpe.igeriv.web.actions.ReportAction;
import it.dpe.igeriv.web.actions.ReportProdottiVariAction;

public class ValueStackActionVisitor implements ActionVisitor {
	private List<?> dataSourceList;
	
	@Override
	public void visit(ReportAction action) {
		dataSourceList = action.getVendite();
	}

	@Override
	public void visit(ReportProdottiVariAction action) {
		dataSourceList = action.getDettagli();
	}

	@Override
	public void visit(Object action) {
		dataSourceList = null;
	}
	
	public List<?> getDataSourceList() {
		return dataSourceList;
	}

}
