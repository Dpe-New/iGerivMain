package it.dpe.igeriv.web.extremecomponents;

import it.dpe.igeriv.util.StringUtility;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.HeaderCell;
import org.extremecomponents.table.core.TableModel;
 
public class DpeHeaderCell extends HeaderCell {
	@Override
	public String getExportDisplay(TableModel model, Column column) {
		return StringUtility.unescapeHTML(column.getTitle());
	}
}
