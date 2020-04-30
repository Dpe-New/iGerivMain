package it.dpe.igeriv.web.extremecomponents;

import it.dpe.igeriv.dto.BollaDettaglioDto;
import it.dpe.igeriv.dto.FondoBollaDettaglioDto;
import it.dpe.igeriv.util.DPEWebContants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.beanutils.BeanUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.util.ExtremeUtils;

import com.google.common.base.Strings;

public class DpeBollaRivenditaTextItemCell implements Cell {

	public String getExportDisplay(TableModel model, Column column) {
		String dateFormat = (column.getAttribute("dateFormat") != null) ? (String) column.getAttribute("dateFormat") : DPEWebContants.BLANK;
		if (dateFormat != null && !dateFormat.equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			return (column.getValue() != null) ? sdf.format((Date) column.getValue()) : null;
		}
		if (column.getAttribute("exportStyle") != null) {
			column.setStyle(column.getAttributeAsString("exportStyle"));
		}
		String value = (column != null && column.getPropertyValue() != null) ? column.getPropertyValue().toString() : null;
		if (!Strings.isNullOrEmpty(column.getAttributeAsString("numberFormat"))) {
			value = formatColumnNumberValue(model, column);
		}
		return value;
	}
	
	private String formatColumnNumberValue(TableModel model, Column column) {
        Locale locale = model.getLocale();
        String value = column.getPropertyValueAsString();
        return ExtremeUtils.formatNumber(column.getAttributeAsString("numberFormat"), value, locale);
	}
	
	public String getHtmlDisplay(TableModel model, Column column) {
		DpeColumnBuilder columnBuilder = new DpeColumnBuilder(column);
		columnBuilder.tdStart();
		try {
			Object bean = model.getCurrentRowBean();
			String styleClass = (column.getAttribute("styleClass") != null) ? (String)column.getAttribute("styleClass") : DPEWebContants.BLANK;
			String style = (column.getAttribute("style") != null) ? (String)column.getAttribute("style") : DPEWebContants.BLANK;
			String pkName = (column.getAttribute("pkName") != null) ? (String)column.getAttribute("pkName") : DPEWebContants.BLANK;
			String sessionVarName = (column.getAttribute("sessionVarName") != null) ? (String)column.getAttribute("sessionVarName") : DPEWebContants.BLANK;
			String pkVal = sessionVarName + BeanUtils.getProperty(bean, pkName);
			String columnValue = column.getValue() != null ? column.getValue().toString() : "";
			String size = (column.getAttribute("size") != null) ? (String)column.getAttribute("size") : DPEWebContants.BLANK;
			String maxlength = (column.getAttribute("maxlength") != null) ? (String)column.getAttribute("maxlength") : DPEWebContants.BLANK;
			Integer spunta = (bean instanceof BollaDettaglioDto) ? ((BollaDettaglioDto) bean).getSpunta() : (bean instanceof FondoBollaDettaglioDto) ? ((FondoBollaDettaglioDto) bean).getSpunta() : 0;
			String fieldName = (bean instanceof BollaDettaglioDto) ? "fieldMap" : "fieldFBMap";
			String spuntaFieldName = (bean instanceof BollaDettaglioDto) ? "spuntaMap" : "spuntaFBMap";
			String modificatoFieldName = (bean instanceof BollaDettaglioDto) ? "modificatoMap" : "modificatoFBMap";
			columnBuilder.getHtmlBuilder().input("text")
				.name(fieldName + "['" + pkVal + "']")
				.id("differenze" + pkVal)
				.append(" modificato=false")
				.styleClass(styleClass)
				.style(style)
				.size(size)
				.maxlength(maxlength)
				.value(columnValue);
			columnBuilder.getHtmlBuilder().append("validateIsNumeric=true ");
			columnBuilder.getHtmlBuilder().xclose();
			
			columnBuilder.getHtmlBuilder().input("hidden")
				.name(spuntaFieldName + "['" + pkVal + "']")
				.id(spuntaFieldName + pkVal)
				.value(spunta.toString())
				.xclose();
			
			columnBuilder.getHtmlBuilder().input("hidden")
				.name(modificatoFieldName + "['" + pkVal + "']")
				.id(modificatoFieldName + pkVal)
				.value("false")
				.xclose();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}

}
