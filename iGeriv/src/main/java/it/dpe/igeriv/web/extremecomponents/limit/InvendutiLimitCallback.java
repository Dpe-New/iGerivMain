package it.dpe.igeriv.web.extremecomponents.limit;

import java.util.Collection;

import org.extremecomponents.table.bean.Table;
import org.extremecomponents.table.callback.FilterRowsCallback;
import org.extremecomponents.table.callback.RetrieveRowsCallback;
import org.extremecomponents.table.callback.SortRowsCallback;
import org.extremecomponents.table.core.RetrievalUtils;
import org.extremecomponents.table.core.TableConstants;
import org.extremecomponents.table.core.TableModel;

@SuppressWarnings("rawtypes")
public class InvendutiLimitCallback implements RetrieveRowsCallback, FilterRowsCallback, SortRowsCallback {
	public final static String TOTAL_ROWS = "totalRows";
	public static final int MAX_PAGE_ROWS = 500;
	public static final int MEDIAN_PAGE_ROWS = 300;
	
    /**
     * Retrieve the collection from the given scope. Requires that a variable be
     * passed in from any of the web scopes (page, request, session,
     * application) that is called "totalRows". The totalRows is a Integer
     * value.
     */
	public Collection retrieveRows(TableModel model) throws Exception {
        Table table = model.getTableHandler().getTable();
        Collection rows = RetrievalUtils.retrieveCollection(model.getContext(), table.getItems(), table.getScope());

        Object totalRows = RetrievalUtils.retrieve(model.getContext(), TableConstants.TOTAL_ROWS);
        if (totalRows == null) {
            totalRows = (Integer) RetrievalUtils.retrieve(model.getContext(), model.getTableHandler().prefixWithTableId() + TableConstants.TOTAL_ROWS);
        }

        if (totalRows instanceof Integer) {
            model.getTableHandler().setTotalRows((Integer)totalRows);
        } else {
            String message = "You need to specify the " + TableConstants.TOTAL_ROWS + " (as an Integer) to use the " + this.getClass().getName() + ".";
            throw new Exception(message);
        }
        	
        table.setMaxRowsDisplayed(MAX_PAGE_ROWS);
        table.setMedianRowsDisplayed(MEDIAN_PAGE_ROWS);
        
        return rows;
    }

    public Collection filterRows(TableModel model, Collection rows) throws Exception {
        return rows;
    }

    public Collection sortRows(TableModel model, Collection rows) throws Exception {
        return rows;
    }
}
