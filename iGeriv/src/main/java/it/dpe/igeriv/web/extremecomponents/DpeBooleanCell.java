package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.DisplayCell;
import org.extremecomponents.table.core.TableModel;

public class DpeBooleanCell extends DisplayCell {

	@Override
	public String getExportDisplay(TableModel model, Column column) {
		Boolean value = (Boolean)column.getValue();
		
		return (value != null && value) ? model.getMessages().getMessage("igeriv.si") : model.getMessages().getMessage("igeriv.no");
	}
	
	@Override
	protected String getCellValue(TableModel model, Column column) {
		Boolean value = (Boolean)column.getValue();
		
		return (value != null && value) ? model.getMessages().getMessage("igeriv.si") : model.getMessages().getMessage("igeriv.no");
	}
	
}
