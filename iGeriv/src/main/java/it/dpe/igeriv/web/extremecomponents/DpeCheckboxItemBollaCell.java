package it.dpe.igeriv.web.extremecomponents;

import java.util.Collection;

import org.apache.commons.beanutils.BeanUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;

import it.dpe.igeriv.util.DPEWebContants;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.FondoBollaDettaglioVo;

/**
 * @author romanom
 *
 */
@SuppressWarnings("rawtypes")
public class DpeCheckboxItemBollaCell implements Cell {

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
			String pkName = (column.getAttribute("pkName") != null) ? (String)column.getAttribute("pkName") : DPEWebContants.BLANK;
			String spuntaId = BeanUtils.getProperty(bean, pkName);
			boolean isFondoBolla = bean instanceof FondoBollaDettaglioVo;
			if (isFondoBolla) {
				chkName = "spunteFondoBolla";
			}
			String columnValue = null;
			if (column.getValue() != null) {
				columnValue = column.getValue().toString();
			}
			Collection selectedSpuntaIds = (Collection) model.getContext()
					.getSessionAttribute(DPEWebContants.SESSION_VAR_SPUNTE);
			if (selectedSpuntaIds != null
					&& selectedSpuntaIds.contains(spuntaId)) {
				columnBuilder.getHtmlBuilder().input("checkbox").name(chkName).id(DPEWebContants.DPE_CHECKBOX_NAME + spuntaId).value(spuntaId).styleClass(DPEWebContants.STYLE_CLASS_TEXT).append(" fb=" + isFondoBolla + " ");
				/*columnBuilder.getHtmlBuilder().onclick(
						"setDpeCheckboxState(this)");*/
				columnBuilder.getHtmlBuilder().checked();
				columnBuilder.getHtmlBuilder().xclose();
			} else {
				columnBuilder.getHtmlBuilder().input("checkbox").name(chkName).id(DPEWebContants.DPE_CHECKBOX_NAME + spuntaId).value(spuntaId).styleClass(DPEWebContants.STYLE_CLASS_TEXT).append(" fb=" + isFondoBolla + " ");
				/*columnBuilder.getHtmlBuilder().onclick(
						"setDpeCheckboxState(this)");*/
				if (columnValue != null && columnValue.equals(String.valueOf(DPEWebContants.ONE))) {
					columnBuilder.getHtmlBuilder().checked();
				}
				columnBuilder.getHtmlBuilder().xclose();				
			}
		} catch (Exception e) {
		}
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}
	
}
