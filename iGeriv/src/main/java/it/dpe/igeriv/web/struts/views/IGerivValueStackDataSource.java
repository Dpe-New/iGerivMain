package it.dpe.igeriv.web.struts.views;

import java.util.List;

import org.apache.struts2.views.jasperreports.ValueStackDataSource;

import com.opensymphony.xwork2.util.ValueStack;

import it.dpe.igeriv.web.actions.BaseAction;
import lombok.Getter;

@Getter
public class IGerivValueStackDataSource extends ValueStackDataSource {
    private List<?> dataSourceList; 
    
    /**
     * Create a value stack data source on the given iterable property
     *
     * @param valueStack The value stack to base the data source on
     * @param dataSource The property to iterate over for the report
     */
    public IGerivValueStackDataSource(ValueStack valueStack, String dataSource) {
    	super(valueStack, dataSource);
    	ValueStackActionVisitor visitor = new ValueStackActionVisitor();
    	((BaseAction) valueStack.getRoot().get(0)).accept(visitor);
    	this.dataSourceList = visitor.getDataSourceList();
    }
    
}
