package it.dpe.igeriv.web.extremecomponents;

import org.apache.commons.beanutils.BeanUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.extremecomponents.util.HtmlBuilder;

import it.dpe.igeriv.util.DPEWebContants;
import it.dpe.igeriv.util.IGerivConstants;

public class DpeCheckboxOrdiniClienti  implements Cell {

	public String getExportDisplay(TableModel arg0, Column arg1) {
		Object bean = arg0.getCurrentRowBean();
		try {
			if (arg1.getAlias().equals("spunta")) {
				String property = null; 
				try {
					property = BeanUtils.getProperty(bean, "spunta");
				} catch (NoSuchMethodException e) {}
				Integer indicatoreValorizzare = (property != null) ? Integer.valueOf(property) : null;
				if (indicatoreValorizzare != null) {
					if (indicatoreValorizzare.equals(IGerivConstants.COD_SI)) {
						return arg0.getMessages().getMessage(IGerivConstants.SI);
					} else if (indicatoreValorizzare.equals(IGerivConstants.COD_NO)) {
						return arg0.getMessages().getMessage(IGerivConstants.NO);
					} 
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		return null;
	}

	public String getHtmlDisplay(TableModel model, Column column) {
 		ColumnBuilder columnBuilder = new ColumnBuilder(column);
		columnBuilder.tdStart();
				try {
			Object bean = model.getCurrentRowBean();
			String chkName = DPEWebContants.SESSION_VAR_SPUNTE;
			String id = column.getAttribute("pkName")!=null ? column.getAttribute("pkName").toString() : "seqordine";
			
			String spuntaId = BeanUtils.getProperty(bean, id);
			
			String columnValue = null;
			if (column.getValue() != null) {
				columnValue = column.getValue().toString();
			}
			HtmlBuilder html = columnBuilder.getHtmlBuilder();
			html.input("checkbox").name(chkName).id(DPEWebContants.DPE_CHECKBOX_NAME + spuntaId).value(spuntaId).styleClass(DPEWebContants.STYLE_CLASS_TEXT).append(" ");
			html.onclick ("updateTotale(this)"); 
			if (columnValue != null && columnValue.equals(String.valueOf(DPEWebContants.ONE))) {
				html.checked();
			}
			html.xclose();				
		} 
		catch (Exception e) {}
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}
	
}