package it.dpe.igeriv.web.extremecomponents;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.beanutils.BeanUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;

import com.google.common.base.Strings;

import it.dpe.igeriv.dto.BollaResaFuoriVoceDettaglioDto;
import it.dpe.igeriv.dto.BollaResaRichiamoPersonalizzatoDettaglioDto;
import it.dpe.igeriv.util.DPEWebContants;
import it.dpe.igeriv.util.NumberUtils;
import it.dpe.igeriv.vo.BollaResaFuoriVoceVo;

public class DpeBollaResaTextItemCell implements Cell {

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
        return NumberUtils.formatNumber(value, locale, column.getAttributeAsString("numberFormat"));
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
			String fieldName = "reso";
			String modificatoFieldName = "modificato";
			if (bean instanceof BollaResaRichiamoPersonalizzatoDettaglioDto) {
				fieldName = "resoRichiamo";
				modificatoFieldName = "modificatoRR";
			} else if (bean instanceof BollaResaFuoriVoceDettaglioDto) {
				fieldName = "resoFuoriVoce";
				modificatoFieldName = "modificatoFV";
			} else if (bean instanceof BollaResaFuoriVoceVo) {
				fieldName = "resoFuoriVoce";
				modificatoFieldName = "modificatoFV";
			}
			columnBuilder.getHtmlBuilder().input("text")
				.name(fieldName + "['" + pkVal + "']")
				.id("reso" + pkVal)
				.append(" modificato=false")
				.styleClass(styleClass)
				.style(style)
				.size(size)
				.maxlength(maxlength)
				.value(columnValue);
			columnBuilder.getHtmlBuilder().append("validateIsNumeric=true ");
			columnBuilder.getHtmlBuilder().xclose();
			
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
