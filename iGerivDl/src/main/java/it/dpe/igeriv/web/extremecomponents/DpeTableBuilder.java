package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.TableBuilder;
import org.extremecomponents.util.HtmlBuilder;

/**
 * @author Marcello
 *
 */
public class DpeTableBuilder extends TableBuilder {

	public DpeTableBuilder(HtmlBuilder html, TableModel model) {
		super(html, model);
	}
	
	@Override
	public void themeStart() {
		getHtmlBuilder().newline();
        String theme = getTableModel().getTableHandler().getTable().getTheme();
        getHtmlBuilder().div().styleClass(theme).id(getTableModel().getTableHandler().getTable().getAttributeAsString("id"));
        getHtmlBuilder().close();
	}
	
	@Override
	public void themeEnd() {
		
	}
	
	
}
