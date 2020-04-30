package it.dpe.igeriv.web.extremecomponents;


import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.dto.RichiestaRifornimentoDto;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.DPEWebContants;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.NumberUtils;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lombok.val;

import org.apache.commons.beanutils.BeanUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.util.HtmlBuilder;
import org.jfree.data.DataUtilities;
import org.jsoup.helper.DataUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import com.google.common.base.Strings;

public class DpeTextBarcodeCell implements Cell{

	@Override
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

	@Override
	public String getHtmlDisplay(TableModel model, Column column) {
		DpeColumnBuilder columnBuilder = new DpeColumnBuilder(column);
		columnBuilder.tdStart();
		try {
			PubblicazioneDto beanDto = (PubblicazioneDto) model.getCurrentRowBean();
			String altBarcode="";			
			//0000200
			if(beanDto.getDataCorrezioneBarcode()!=null){
				String val_msg = (beanDto.getCodiceBarrePrecedente()!=null && !beanDto.getBarcode().equals(""))?beanDto.getCodiceBarrePrecedente():" ";
				val_msg+=" - in data : "+DateUtilities.getTimestampAsString(new Date(beanDto.getDataCorrezioneBarcode().getTime()), DateUtilities.FORMATO_DATA_SLASH);
				columnBuilder.getHtmlBuilder().a("#").onclick("openJAlertBarcode('"+val_msg+"')").close();
				columnBuilder.getHtmlBuilder().div().style("font-weight: bold").close();
				columnBuilder.getHtmlBuilder().append(beanDto.getBarcode());
				columnBuilder.getHtmlBuilder().divEnd();
				columnBuilder.getHtmlBuilder().aEnd();
			}else{
				columnBuilder.getHtmlBuilder().append((beanDto.getBarcode()!=null && !beanDto.getBarcode().equals(""))?beanDto.getBarcode():" ");
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}

	private String formatColumnNumberValue(TableModel model, Column column) {
        Locale locale = model.getLocale();
        String value = column.getPropertyValueAsString();
        return NumberUtils.formatNumber(value, locale, column.getAttributeAsString("numberFormat"));
	}
	
	private boolean isTextFieldEnabled(Object bean,
			String[] enabledConditionFieldsArr,
			String[] enabledConditionValuesArr) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (enabledConditionFieldsArr.length > 0 && enabledConditionValuesArr.length > 0 && enabledConditionFieldsArr.length == enabledConditionValuesArr.length) {
			for (int i = 0; i < enabledConditionFieldsArr.length; i++) {
				String property = BeanUtils.getProperty(bean, enabledConditionFieldsArr[i]);
				if (!property.equals(enabledConditionValuesArr[i])) {
					return false;
				}
			}
		}
		return true;
	}

}
