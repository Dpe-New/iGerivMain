package it.dpe.igeriv.web.extremecomponents;

import java.util.StringTokenizer;

import org.apache.commons.beanutils.BeanUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.extremecomponents.util.HtmlBuilder;

import it.dpe.igeriv.util.DPEWebContants;
import it.dpe.igeriv.util.IGerivConstants;

public class DpeComboboxCell implements Cell {

	public String getExportDisplay(TableModel model, Column column) {
		Object bean = model.getCurrentRowBean();
		try {
			if (column.getAlias().equals("stato")) {
				String property = null; 
				try {
					property = BeanUtils.getProperty(bean, column.getProperty());
				} catch (NoSuchMethodException e) {}
				Integer indicatoreValorizzare = (property != null) ? Integer.valueOf(property) : null;
				if (indicatoreValorizzare != null) {
					if (indicatoreValorizzare.equals(IGerivConstants.INDICATORE_BOLLA_NON_TRASMESSA)) {
						return model.getMessages().getMessage(IGerivConstants.BOLLA_NON_TRASMESSA);
					} else if (indicatoreValorizzare.equals(IGerivConstants.INDICATORE_BOLLA_DA_TRASMETTERE)) {
						return model.getMessages().getMessage(IGerivConstants.BOLLA_DA_TRASMETTERE);
					} else if (indicatoreValorizzare.equals(IGerivConstants.INDICATORE_BOLLA_IN_TRASMISSIONE)) {
						return model.getMessages().getMessage(IGerivConstants.BOLLA_IN_TRASMISSIONE);
					} else if (indicatoreValorizzare.equals(IGerivConstants.INDICATORE_BOLLA_TRASMESSA)) {
						return model.getMessages().getMessage(IGerivConstants.BOLLA_TRASMESSA);
					} 
				}
			} else if (column.getAlias().equals("aliquota")) {
				return column.getValueAsString();
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
			String pkName = (column.getAttribute("pkName") != null) ? (String)column.getAttribute("pkName") : DPEWebContants.BLANK;
			String fieldName = (column.getAttribute("fieldName") != null) ? (String)column.getAttribute("fieldName") : DPEWebContants.BLANK;
			String sessionVarName = (column.getAttribute("sessionVarName") != null) ? (String)column.getAttribute("sessionVarName") : DPEWebContants.BLANK;
			String name = sessionVarName + DPEWebContants.UNDERSCORE;
			String optionKeys = (column.getAttribute("optionKeys") != null) ? (String)column.getAttribute("optionKeys") : DPEWebContants.BLANK;			
			String optionValues = (column.getAttribute("optionValues") != null) ? (String)column.getAttribute("optionValues") : DPEWebContants.BLANK;
			String styleClass = (column.getAttribute("styleClass") != null) ? (String)column.getAttribute("styleClass") : DPEWebContants.BLANK;
			String style = (column.getAttribute("style") != null) ? (String)column.getAttribute("style") : DPEWebContants.BLANK;
			Boolean hasHiddenPkField = Boolean.valueOf((column.getAttribute("hasHiddenPkField") != null) ? (String)column.getAttribute("hasHiddenPkField") : "false");
			Boolean hasEmptyOption = Boolean.valueOf((column.getAttribute("hasEmptyOption") != null) ? (String)column.getAttribute("hasEmptyOption") : "false");
			Boolean fieldEnabled = Boolean.valueOf((column.getAttribute("fieldEnabled") != null) ? (String) column.getAttribute("fieldEnabled") : "true");
			HtmlBuilder obj = columnBuilder.getHtmlBuilder().select()
					.name(sessionVarName)
					.id(name + BeanUtils.getProperty(bean, pkName))
					.styleClass(styleClass)
					.style(style);
			if (!fieldEnabled) {
				obj.disabled();
			}
			obj.close();
			if (!optionKeys.equals(DPEWebContants.BLANK) && !optionValues.equals(DPEWebContants.BLANK)) {
				StringTokenizer st = new StringTokenizer(optionKeys, ",");
				StringTokenizer st1 = new StringTokenizer(optionValues, ",");
				String fieldValue = (fieldName != null && !fieldName.equals("")) ? BeanUtils.getProperty(bean, fieldName) : null;
				int  count =  0;
				if (hasEmptyOption) {
					obj.option().value("");
					obj.close();
					obj.optionEnd();
				}
				while (st.hasMoreElements()) {
					String nextKey = st.nextToken();
					String nextVal = st1.nextToken();
					obj.option().value(nextKey);
					if (fieldValue == null && count == 0 && !hasEmptyOption) {
						obj.selected();
					} else if (fieldValue != null && fieldValue.equals(nextKey)) {
						obj.selected();
					}
					obj.close();
					obj.append(nextVal);
					obj.optionEnd();
					count++;
				}
			}
			obj.selectEnd();
			if (hasHiddenPkField) {
				columnBuilder.getHtmlBuilder().input("hidden").name("pk").value(BeanUtils.getProperty(bean, pkName)).xclose();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}

}
