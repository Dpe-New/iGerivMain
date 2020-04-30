package it.dpe.igeriv.web.extremecomponents;

import org.apache.commons.beanutils.BeanUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;

import it.dpe.igeriv.util.DPEWebContants;

public class DpeHiddenCell implements Cell {

	public String getExportDisplay(TableModel arg0, Column arg1) {
		return null;
	}

	public String getHtmlDisplay(TableModel model, Column column) {
 		ColumnBuilder columnBuilder = new ColumnBuilder(column);
		columnBuilder.tdStart();
		try {
			Object bean = model.getCurrentRowBean();
			String pkName = (column.getAttribute("pkName") != null) ? (String)column.getAttribute("pkName") : DPEWebContants.BLANK;
			String columnValue = "";
			if (column.getValue() != null) {
				columnValue = column.getValue().toString();
			}
			columnBuilder.getHtmlBuilder().input("hidden").name("" + BeanUtils.getProperty(bean,  pkName)).id("hidden_" +
							BeanUtils.getProperty(bean,  pkName)).value(columnValue);
			columnBuilder.getHtmlBuilder().xclose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}

}
