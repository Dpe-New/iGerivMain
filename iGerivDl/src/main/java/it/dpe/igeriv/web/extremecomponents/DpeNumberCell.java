package it.dpe.igeriv.web.extremecomponents;

import it.dpe.igeriv.util.IGerivConstants;

import org.apache.commons.lang.math.NumberUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.NumberCell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;

public class DpeNumberCell extends NumberCell {
	
	@Override
	public String getExportDisplay(TableModel model, Column column) {
		if (column.getAttribute("exportStyle") != null) {
			column.setStyle(column.getAttributeAsString("exportStyle"));
		} 
		String exportDisplay = super.getExportDisplay(model, column);
		if (column.getAttribute("hasCurrencySign") != null && Boolean.parseBoolean(column.getAttribute("hasCurrencySign").toString()) 
				&& exportDisplay != null && !exportDisplay.equals("")) {
			exportDisplay = IGerivConstants.EURO_SIGN_ASCII + " " + exportDisplay;
		}
		return exportDisplay;
	}
	
	@Override
	public String getHtmlDisplay(TableModel model, Column column) {
		ColumnBuilder columnBuilder = new ColumnBuilder(column);
        columnBuilder.tdStart();
        columnBuilder.tdBody(getCellValue(model, column));
        columnBuilder.tdEnd();
        return columnBuilder.toString();
	}
	
	@Override
	protected String getCellValue(TableModel model, Column column) {
		String cellValue = super.getCellValue(model, column);
		String valueAsString = column.getValueAsString();
		Number n = null;
		if (NumberUtils.isNumber(valueAsString)) {
			n = NumberUtils.createNumber(valueAsString);
		}
		cellValue = (n != null && n.doubleValue() == 0) ? "" : cellValue;
		return cellValue;
	}
	
}
