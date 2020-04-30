package it.dpe.igeriv.web.extremecomponents;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.DisplayCell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.extremecomponents.util.HtmlBuilder;

import it.dpe.igeriv.util.IGerivConstants;

public class DpeGestioniCestaCell extends DisplayCell {
	private final Logger log = Logger.getLogger(getClass());
	
	@Override
	public String getExportDisplay(TableModel model, Column column) {
		String value = (String)column.getValue();
		
		return value;
	}
	
	@Override
	protected String getCellValue(TableModel model, Column column) {
		
		ColumnBuilder columnBuilder = new ColumnBuilder(column);
		HtmlBuilder htmlBuilder = columnBuilder.getHtmlBuilder();
		
		String valueCesta;
		try {
			
			valueCesta = BeanUtils.getProperty(model.getCurrentRowBean(), "cesta");
			
			String cssCesta ="";
			
			if(valueCesta != null && !valueCesta.equals("") && valueCesta.equals(IGerivConstants.CDL_CESTA_1)){
				cssCesta = "text-align:center; background-color: #a4f48c;";	//#a4f48c; #32cd32;
			}else if(valueCesta != null && !valueCesta.equals("") && valueCesta.equals(IGerivConstants.CDL_CESTA_2)){
				cssCesta = "text-align:center; background-color: #c5e0f6;"; //#c5e0f6;  #ff00ff;
			}else{
				cssCesta = "text-align:center;";
			}
			
			htmlBuilder.div();
			htmlBuilder.style(cssCesta).close();
			htmlBuilder.append(valueCesta).divEnd();
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return htmlBuilder.toString();
		
	}
}
