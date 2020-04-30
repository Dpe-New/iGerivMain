package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.view.html.ColumnBuilder;

public class DpeColumnBuilder extends ColumnBuilder {

	public DpeColumnBuilder(Column column) {
		super(column);
	}
	
	@Override
	public void tdStart() {
		getHtmlBuilder().td(2);
        styleClass();
        style();
        width();
        if (getColumn().getAttribute("href1") != null && !getColumn().getAttributeAsString("href1").equals("")) {
        	getHtmlBuilder().append(" divParam='" + getColumn().getAttributeAsString("href1") + "'");
        }
        getHtmlBuilder().close();
	}
	
	@Override
	public void tdEnd() {
		super.tdEnd();
	}
	
}
