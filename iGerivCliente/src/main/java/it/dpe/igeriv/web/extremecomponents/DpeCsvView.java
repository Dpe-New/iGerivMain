package it.dpe.igeriv.web.extremecomponents;

import org.apache.commons.lang.StringUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.CsvView;

public class DpeCsvView extends CsvView {
	
	@Override
	public void body(TableModel model, Column column) {
		
		String cellDisplay = column.getCellDisplay();
		if (StringUtils.contains(cellDisplay, "&nbsp;")) {
			cellDisplay = cellDisplay.replaceAll("&nbsp;", " ");
        }
		column.setCellDisplay(cellDisplay);
		
        super.body(model, column);
	}
}
