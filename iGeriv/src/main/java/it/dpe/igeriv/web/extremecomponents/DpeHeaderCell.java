package it.dpe.igeriv.web.extremecomponents;

import org.apache.commons.lang.StringUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.HeaderCell;
import org.extremecomponents.table.core.MessagesConstants;
import org.extremecomponents.table.core.TableConstants;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.BuilderConstants;
import org.extremecomponents.util.HtmlBuilder;

import it.dpe.igeriv.util.StringUtility;
 
public class DpeHeaderCell extends HeaderCell {
	
	@Override
	public String getExportDisplay(TableModel model, Column column) {
		return StringUtility.unescapeHTML(column.getTitle());
	}
	
	@Override
	protected void buildHeaderHtml(HtmlBuilder html, TableModel model, Column column, String headerClass, String sortImage, String sortOrder) {
		html.th(2);

        if (StringUtils.isNotEmpty(headerClass)) {
            html.styleClass(headerClass);
        }

        if (StringUtils.isNotEmpty(column.getHeaderStyle())) {
            html.style(column.getHeaderStyle());
        }

        if (StringUtils.isNotEmpty(column.getWidth())) {
            html.width(column.getWidth());
        } 

        if (column.isSortable()) { 
            if (sortOrder.equals(TableConstants.SORT_ASC)) {
                html.onmouseover("this.className='" + BuilderConstants.TABLE_HEADER_SORT_CSS + "';this.style.cursor='pointer'");
                if (StringUtils.isNotEmpty(headerClass)) {
                    html.onmouseout("this.className='" + headerClass + "';this.style.cursor='default'");
                } else {
                    html.onmouseout("this.className='" + BuilderConstants.TABLE_HEADER_CSS + "';this.style.cursor='default'");
                }
            }

            if (sortOrder.equals(TableConstants.SORT_DEFAULT) || sortOrder.equals(TableConstants.SORT_DESC)) {
                html.onmouseover("this.style.cursor='pointer'");
                html.onmouseout("this.style.cursor='default'");
            }

            html.onclick(new DpeTableActions(model).getSortAction(column, sortOrder));

            boolean showTooltips = model.getTableHandler().getTable().isShowTooltips();
            if (showTooltips) {
                String headercellTooltip = model.getMessages().getMessage(MessagesConstants.COLUMN_HEADERCELL_TOOLTIP_SORT);
                html.title(headercellTooltip + " " + column.getTitle());
            }
        }

        html.close();

        html.append(column.getTitle());

        if (column.isSortable()) {
            if (StringUtils.isNotEmpty(sortImage)) {
                html.nbsp();
                html.img();
                html.src(sortImage);
                html.style("border:0");
                html.alt("Arrow");
                html.xclose();
            }
        }

        html.thEnd();
	}
}
