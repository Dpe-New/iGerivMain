package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.core.TableConstants;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.TableActions;
import org.extremecomponents.table.view.html.ToolbarBuilder;
import org.extremecomponents.util.HtmlBuilder;

public class DpeToolbarBuilder extends ToolbarBuilder {

	public DpeToolbarBuilder(HtmlBuilder html, TableModel model) {
		super(html, model);
	}
	
	@Override
	public void rowsDisplayedDroplist() {
		int rowsDisplayed = new Integer(getTableModel().getPreferences().getPreference(DpePreferencesConstants.TABLE_MIN_ROWS_DISPLAYED)).intValue();
        int medianRowsDisplayed = getTableModel().getTableHandler().getTable().getMedianRowsDisplayed();
        int maxRowsDisplayed = getTableModel().getTableHandler().getTable().getMaxRowsDisplayed();
        int currentRowsDisplayed = getTableModel().getLimit().getCurrentRowsDisplayed();

        getHtmlBuilder().select().name(getTableModel().getTableHandler().prefixWithTableId() + TableConstants.ROWS_DISPLAYED);

        StringBuffer onchange = new StringBuffer();
        onchange.append(new TableActions(getTableModel()).getRowsDisplayedAction());
        getHtmlBuilder().onchange(onchange.toString());

        getHtmlBuilder().close();

        getHtmlBuilder().newline();
        getHtmlBuilder().tabs(4);

        // default rows
        getHtmlBuilder().option().value(String.valueOf(rowsDisplayed));
        if (currentRowsDisplayed == rowsDisplayed) {
        	getHtmlBuilder().selected();
        }
        getHtmlBuilder().close();
        getHtmlBuilder().append(String.valueOf(rowsDisplayed));
        getHtmlBuilder().optionEnd();

        // median rows
        getHtmlBuilder().option().value(String.valueOf(medianRowsDisplayed));
        if (currentRowsDisplayed == medianRowsDisplayed) {
        	getHtmlBuilder().selected();
        }
        getHtmlBuilder().close();
        getHtmlBuilder().append(String.valueOf(medianRowsDisplayed));
        getHtmlBuilder().optionEnd();

        // max rows
        getHtmlBuilder().option().value(String.valueOf(maxRowsDisplayed));
        if (currentRowsDisplayed == maxRowsDisplayed) {
        	getHtmlBuilder().selected();
        }
        getHtmlBuilder().close();
        getHtmlBuilder().append(String.valueOf(maxRowsDisplayed));
        getHtmlBuilder().optionEnd();

        getHtmlBuilder().newline();
        getHtmlBuilder().tabs(4);
        getHtmlBuilder().selectEnd();
	}
}
