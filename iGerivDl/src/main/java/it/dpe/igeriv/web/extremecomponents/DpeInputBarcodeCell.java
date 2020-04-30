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

import org.apache.commons.beanutils.BeanUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.util.HtmlBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import com.google.common.base.Strings;

public class DpeInputBarcodeCell implements Cell{

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
			//RichiestaRifornimentoDto bean = (RichiestaRifornimentoDto) model.getCurrentRowBean();
			String pkName = (column.getAttribute("pkName") != null) ? (String)column.getAttribute("pkName") : DPEWebContants.BLANK;
			String sessionVarName = (column.getAttribute("sessionVarName") != null) ? (String)column.getAttribute("sessionVarName") : DPEWebContants.BLANK;
			String pkVal = beanDto.getCoddl()+"|"+beanDto.getCodicePubblicazione()+"|"+beanDto.getNumeroCopertina().trim()+"|"+DateUtilities.getTimestampAsString(beanDto.getDataUscita(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS)+"|"+beanDto.getIdtn(); 
			//String pkVal = BeanUtils.getProperty(beanDto, pkName);
			
			if(beanDto.getCodiceBarrePrecedente()!=null){
				altBarcode = "Codice a Barre Precedente : "+beanDto.getCodiceBarrePrecedente()+"  Modificato il :"+beanDto.getDataCorrezioneBarcode();
			}
			
			String size = (column.getAttribute("size") != null) ? (String)column.getAttribute("size") : DPEWebContants.BLANK;
			String maxlength = (column.getAttribute("maxlength") != null) ? (String)column.getAttribute("maxlength") : DPEWebContants.BLANK;
			Boolean validateIsNumeric = (column.getAttribute("validateIsNumeric") != null) ? Boolean.valueOf(column.getAttribute("validateIsNumeric").toString()) : false;
			String styleClass = (column.getAttribute("styleClass") != null) ? (String)column.getAttribute("styleClass") : DPEWebContants.BLANK;
			String style = (column.getAttribute("style") != null) ? (String)column.getAttribute("style") : DPEWebContants.BLANK;
			Boolean hasHiddenPkField = Boolean.valueOf((column.getAttribute("hasHiddenPkField") != null) ? (String)column.getAttribute("hasHiddenPkField") : "false");
			String enabledConditionFields = (column.getAttribute("enabledConditionFields") != null) ? (String)column.getAttribute("enabledConditionFields") : DPEWebContants.BLANK;
			String enabledConditionValues = (column.getAttribute("enabledConditionValues") != null) ? (String)column.getAttribute("enabledConditionValues") : DPEWebContants.BLANK;
			String[] enabledConditionFieldsArr = StringUtils.tokenizeToStringArray(enabledConditionFields, ",");
			String[] enabledConditionValuesArr = StringUtils.tokenizeToStringArray(enabledConditionValues, ",");
			
			
			
			//Boolean permetteInserimentoRichiesteRifornimentoFuture = ((UserAbbonato) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPermetteInserimentoRichiesteRifornimentoFuture();
			String hiddenIdName = (column.getAttribute("hiddenPkName") != null) ? (String)column.getAttribute("hiddenPkName") : IGerivConstants.METHOD_BASE_PK_NAME;
			String hiddenPkId = (column.getAttribute("hiddenPkId") != null) ? (String)column.getAttribute("hiddenPkId") : IGerivConstants.METHOD_BASE_PK_NAME;
			String hiddenPkFieldName = (column.getAttribute("hiddenPkFieldName") != null) ? (String)column.getAttribute("hiddenPkFieldName") : pkName;
			String hiddenPkVal = (hiddenPkFieldName != null && !hiddenPkFieldName.equals("") && hasHiddenPkField) ? BeanUtils.getProperty(beanDto, hiddenPkFieldName) : pkVal;
			String columnValue = column.getValue() != null ? column.getValue().toString() : "";
			Boolean noRifornimento = false;
			Timestamp dataUscita = beanDto.getDataUscita();
			Timestamp sysdate = (Timestamp) model.getContext().getRequestAttribute("sysdate");
			
			if (sysdate != null && dataUscita != null && sysdate.before(dataUscita)) {
				noRifornimento = true;
			}
			columnBuilder.getHtmlBuilder().input("text").name(sessionVarName + "['" + pkVal + "']").id(pkVal).value(columnValue).styleClass(styleClass).style(style).size(size).maxlength(maxlength).alt(altBarcode);
			columnBuilder.getHtmlBuilder().append("validateIsNumeric=" + validateIsNumeric + " ");
			
			//			if (noRifornimento && (permetteInserimentoRichiesteRifornimentoFuture == null || !permetteInserimentoRichiesteRifornimentoFuture)) {
//				columnBuilder.getHtmlBuilder().append("noRifornimento='noRifornimento'");
//			}
			/*
			Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			boolean hasLivellamenti = user != null && ((UserAbbonato) user).getHasLivellamenti();
			boolean hasLivellmentiAndFieldNotEditable = hasLivellamenti && !bean.getLivellamentoEditable();
			boolean fieldNotEditable = !isTextFieldEnabled(bean, enabledConditionFieldsArr, enabledConditionValuesArr);
			if (hasLivellmentiAndFieldNotEditable || fieldNotEditable) {
				columnBuilder.getHtmlBuilder().disabled();
			}*/
			columnBuilder.getHtmlBuilder().xclose();
			/*
			if (hasLivellamenti) {
				HtmlBuilder check = columnBuilder.getHtmlBuilder().nbsp().input("checkbox").name("chkLivellamenti").id("chkLivellamenti_" + bean.getPk().toString()).value(bean.getPk().toString()).title(model.getMessages().getMessage("igeriv.message.richiesta.rifornimento.rete.edicole"));
				if (bean.getIdRichiestaLivellamento() != null) {
					check.checked();
				}
				if (!bean.getLivellamentoEditable()) {
					check.disabled();
				}
				check.styleClass(DPEWebContants.STYLE_CLASS_TEXT);
				check.xclose();
			}*/
			if (hasHiddenPkField) {
				columnBuilder.getHtmlBuilder().input("hidden").name(hiddenIdName).id(hiddenPkId + pkVal).value(hiddenPkVal).xclose();
			}
			column.addAttribute("textFieldId", sessionVarName + pkVal);
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
