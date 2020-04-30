package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.AbstractHtmlView;
import org.extremecomponents.util.HtmlBuilder;

/**
 * This class makes the ExtremeComponents filter toolbar appear at the bottom 
 * of the table.
 *
 * @author Marcello Romano
 */ 
public class DpeCompactViewToolbarOnTopAndBottom extends AbstractHtmlView {
    
	@Override
	protected void init(HtmlBuilder html, TableModel model) {
		super.init(html, model);
		setTableBuilder(new DpeTableBuilder(html, model)); 
		setRowBuilder(new DpeRowBuilder(html, model));
		setCalcBuilder(new DpeCalcBuilder(html, model));
	}
	
    /**
     *  
     * {@inheritDoc}
     */
    protected void beforeBodyInternal(TableModel model) {
    	toolbar(getHtmlBuilder(), getTableModel());
    	
        getTableBuilder().tableStart();

        getTableBuilder().theadStart();

        getTableBuilder().titleRowSpanColumns();
        
        getTableBuilder().filterRow();

        getTableBuilder().headerRow();

        getTableBuilder().theadEnd();

        getTableBuilder().tbodyStart();
    }

    /**
     *  
     * {@inheritDoc}
     */
    protected void afterBodyInternal(TableModel model) {
        getCalcBuilder().defaultCalcLayout();
        
        getTableBuilder().tbodyEnd();
        
        getTableBuilder().tableEnd();
        
        getTableBuilder().getHtmlBuilder().divEnd();
        
        toolbar(getHtmlBuilder(), getTableModel());
    }

    /**
     * This method provides the ExtremeComponent table with the filter toolbar. 
     * 
     * @param html ExtremeComponents html builder.
     * @param model user defined tabel model.
     */
    protected void toolbar(HtmlBuilder html, TableModel model) {
        new DpeCompactToolbar(html, model).layout();
    }
    
}
