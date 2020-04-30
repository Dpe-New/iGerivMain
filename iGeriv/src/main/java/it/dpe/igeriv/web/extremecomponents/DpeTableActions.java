package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableConstants;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.limit.Sort;
import org.extremecomponents.table.view.html.TableActions;

import com.google.common.base.Strings;

public class DpeTableActions extends TableActions {

	public DpeTableActions(TableModel model) {
		super(model);
	}
	
	@Override
	public String getSortAction(Column column, String sortOrder) {
		StringBuffer action = new StringBuffer("javascript:");
		if (!Strings.isNullOrEmpty(getTableModel().getTableHandler().getTable().getAttributeAsString("beforeUnloadValidationScript"))) {
			action.append(getTableModel().getTableHandler().getTable().getAttributeAsString("beforeUnloadValidationScript"));
		}
		
        Sort sort = getTableModel().getLimit().getSort();
        if (sort.isSorted()) {
            // set the old sort back
            action.append(getFormParameter(TableConstants.SORT + sort.getAlias(), ""));
        }

        action.append(getClearedExportTableIdParameters());

        // set sort on current column
        action.append(getFormParameter(TableConstants.SORT + column.getAlias(), sortOrder));
        action.append(getFormParameter(TableConstants.PAGE, "1"));
        action.append(getOnInvokeOrSubmitAction());

        return action.toString();
	}
	
}
