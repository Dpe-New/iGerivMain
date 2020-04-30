package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.HeaderCell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.util.HtmlBuilder;

public class DpeNullCell extends HeaderCell {

	@Override
	protected void buildHeaderHtml(HtmlBuilder html, TableModel model, Column column, String headerClass, String sortImage, String sortOrder) {
		html.td(2);
        html.close();
        html.tdEnd();
	}

}
