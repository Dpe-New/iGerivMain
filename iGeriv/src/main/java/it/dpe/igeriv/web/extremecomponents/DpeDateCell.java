package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.DateCell;
import org.extremecomponents.table.core.TableModel;

public class DpeDateCell extends DateCell {
	
	@Override
	public String getExportDisplay(TableModel model, Column column) {
		if (column.getAttribute("exportStyle") != null) {
			column.setStyle(column.getAttributeAsString("exportStyle"));
		}
		return super.getExportDisplay(model, column);
	}
	
	@Override
	protected String getCellValue(TableModel model, Column column) {
		String cellValue = super.getCellValue(model, column);
		if (cellValue.contains(" ")) {
			cellValue = cellValue.replaceAll(" ", "&nbsp;");
		}
		return cellValue;
	}
}
