package it.dpe.igeriv.web.extremecomponents;

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

import it.dpe.igeriv.dto.RichiestaRifornimentoDto;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.DPEWebContants;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.NumberUtils;

public class DpeRichiestaRifornimentoTextCell implements Cell {

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
			String pkName = (column.getAttribute("pkName") != null) ? (String)column.getAttribute("pkName") : DPEWebContants.BLANK;
			String sessionVarName = (column.getAttribute("sessionVarName") != null) ? (String)column.getAttribute("sessionVarName") : DPEWebContants.BLANK;
			String pkVal = BeanUtils.getProperty(bean, pkName);
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
			Boolean permetteInserimentoRichiesteRifornimentoFuture = ((UserAbbonato) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPermetteInserimentoRichiesteRifornimentoFuture();
			String hiddenIdName = (column.getAttribute("hiddenPkName") != null) ? (String)column.getAttribute("hiddenPkName") : IGerivConstants.METHOD_BASE_PK_NAME;
			String hiddenPkId = (column.getAttribute("hiddenPkId") != null) ? (String)column.getAttribute("hiddenPkId") : IGerivConstants.METHOD_BASE_PK_NAME;
			String hiddenPkFieldName = (column.getAttribute("hiddenPkFieldName") != null) ? (String)column.getAttribute("hiddenPkFieldName") : pkName;
			String hiddenPkVal = (hiddenPkFieldName != null && !hiddenPkFieldName.equals("") && hasHiddenPkField) ? BeanUtils.getProperty(bean, hiddenPkFieldName) : pkVal;
			String columnValue = column.getValue() != null ? column.getValue().toString() : "";
			Boolean noRifornimento = false;
			Timestamp dataUscita = bean.getDataUscita();
			
			Timestamp dataOrdineRifornimento = (bean.getDataOrdine()!=null)?bean.getDataOrdine():null;
			Timestamp dataRispostaDl = (bean.getDataRispostaDl()!=null)?bean.getDataRispostaDl():null;
			Integer quantitaEvasa = (bean.getQuantitaEvasa()!=null)?bean.getQuantitaEvasa():null;
			String descrCausaleNonEvadibilita = (bean.getDescCausaleNonEvadibilita()!=null)?bean.getDescCausaleNonEvadibilita():"";
			
			
			
			
			
			Timestamp sysdate = (Timestamp) model.getContext().getRequestAttribute("sysdate");
			
			if (sysdate != null && dataUscita != null && sysdate.before(dataUscita)) {
				noRifornimento = true;
			}
			columnBuilder.getHtmlBuilder().input("text").name(sessionVarName + "['" + pkVal + "']").id(sessionVarName + pkVal).value(columnValue).styleClass(styleClass).style(style).size(size).maxlength(maxlength);
			columnBuilder.getHtmlBuilder().append("validateIsNumeric=" + validateIsNumeric + " ");
			if (noRifornimento && (permetteInserimentoRichiesteRifornimentoFuture == null || !permetteInserimentoRichiesteRifornimentoFuture)) {
				columnBuilder.getHtmlBuilder().append("noRifornimento='noRifornimento'");
			}
			
			if(dataRispostaDl==null && dataOrdineRifornimento!=null){
					columnBuilder.getHtmlBuilder().append("dataOrdineRifornimento=" + DateUtilities.getTimestampAsString(dataOrdineRifornimento,DateUtilities.FORMATO_DATA) + " ");
				    columnBuilder.getHtmlBuilder().append("dataRispostaDl=\"\"");
			}else if(quantitaEvasa == null || quantitaEvasa == 0){
					columnBuilder.getHtmlBuilder().append("dataOrdineRifornimento=" + DateUtilities.getTimestampAsString(dataOrdineRifornimento,DateUtilities.FORMATO_DATA) + " ");
					columnBuilder.getHtmlBuilder().append("dataRispostaDl=" + DateUtilities.getTimestampAsString(dataRispostaDl,DateUtilities.FORMATO_DATA) + " ");
			}
			
			Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			boolean hasLivellamenti = user != null && ((UserAbbonato) user).getHasLivellamenti();
			boolean hasLivellmentiAndFieldNotEditable = hasLivellamenti && !bean.getLivellamentoEditable();
			boolean fieldNotEditable = !isTextFieldEnabled(bean, enabledConditionFieldsArr, enabledConditionValuesArr);
			if (hasLivellmentiAndFieldNotEditable || fieldNotEditable) {
				columnBuilder.getHtmlBuilder().disabled();
			}
			columnBuilder.getHtmlBuilder().xclose();
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
			}
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

	/**
	 * @param bean
	 * @param enabledConditionFieldsArr
	 * @param enabledConditionValuesArr
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
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
