package it.dpe.igeriv.web.extremecomponents;

import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.util.DPEWebContants;
import it.dpe.igeriv.util.IGerivConstants;

import org.apache.commons.beanutils.BeanUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.extremecomponents.util.HtmlBuilder;

import com.google.common.base.Strings;

/**
 * @author romanom
 *
 */
public class DpeEdicolaTestInputTextItemBollaCell implements Cell {

	public String getExportDisplay(TableModel arg0, Column arg1) {
		Object bean = arg0.getCurrentRowBean();
		try {
			if (arg1.getAlias().equals("edicolaTest")) {
				String property = null; 
				try {
					property = BeanUtils.getProperty(bean, arg1.getProperty());
				} catch (NoSuchMethodException e) {}
				if (!Strings.isNullOrEmpty(property) && property.contains("true")) {
					return arg0.getMessages().getMessage(IGerivConstants.SI);
				} else {
					return arg0.getMessages().getMessage(IGerivConstants.NO);
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
			EdicolaDto bean = (EdicolaDto) model.getCurrentRowBean();
			String chkName = column.getProperty();
			String pkName = (column.getAttribute("pkName") != null) ? (String)column.getAttribute("pkName") : DPEWebContants.BLANK;
			boolean readonly = column.getAttribute("readonly") != null && column.getAttribute("readonly").toString().equals(String.valueOf(DPEWebContants.TRUE));
			String spuntaId = BeanUtils.getProperty(bean, pkName);
			
			HtmlBuilder text = columnBuilder.getHtmlBuilder().input("text").name(chkName);
			boolean checked = column.getValue() != null && column.getValue().toString().equals(String.valueOf(DPEWebContants.TRUE));
			String pk = bean.getCodEdicolaWeb().toString() + "|" + checked;
			text.id(pk);
			if (checked) {
				text.value("SI");
			}else{
				text.value(" ");
			}
			
			text.styleClass(DPEWebContants.STYLE_CLASS_TEXT);
			text.xclose();
		} catch (Exception e) {
		}
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}
	
}
