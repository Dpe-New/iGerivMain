package it.dpe.igeriv.web.extremecomponents;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;

import com.google.common.base.Strings;

import it.dpe.igeriv.dto.RichiestaRifornimentoDto;
import it.dpe.igeriv.util.DPEWebContants;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.NumberUtils;

public class DpeNoteRifornimentoTextCell implements Cell {

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
		if (column.getAttribute("hasCurrencySign") != null && Boolean.parseBoolean(column.getAttribute("hasCurrencySign").toString()) 
				&& !Strings.isNullOrEmpty(value)) {
			value = IGerivConstants.EURO_SIGN_ASCII + " " + value;
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
			RichiestaRifornimentoDto bean = (RichiestaRifornimentoDto) model.getCurrentRowBean();
			if (column.getValue() != null) {
				
			}
			String string = bean.getNoteVendita() != null ? bean.getNoteVendita() : "";
			columnBuilder.getHtmlBuilder().input("text").name("noteVenditaMap['" + bean.getPk().toString() + "']").id(bean.getPk().toString()).value(string).styleClass("extremeTableFieldsLarger");
			columnBuilder.getHtmlBuilder().xclose();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}

}
