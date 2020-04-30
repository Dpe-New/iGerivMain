package it.dpe.igeriv.web.extremecomponents;

import it.dpe.igeriv.dto.BollaResaFuoriVoceDettaglioDto;
import it.dpe.igeriv.dto.BollaResaRichiamoPersonalizzatoDettaglioDto;
import it.dpe.igeriv.dto.FondoBollaDettaglioDto;
import it.dpe.igeriv.dto.RichiestaRifornimentoDto;
import it.dpe.igeriv.util.DPEWebContants;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.BollaResaFuoriVoceVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiBollaDettaglioVo;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.util.ExtremeUtils;
import org.springframework.util.StringUtils;

import com.google.common.base.Strings;

public class DpeTextItemBollaCell implements Cell {

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
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public String getHtmlDisplay(TableModel model, Column column) {
		DpeColumnBuilder columnBuilder = new DpeColumnBuilder(column);
		columnBuilder.tdStart();
		try {
			Object bean = model.getCurrentRowBean();
			String pkName = (column.getAttribute("pkName") != null) ? (String)column.getAttribute("pkName") : DPEWebContants.BLANK;
			String sessionVarName = (column.getAttribute("sessionVarName") != null) ? (String)column.getAttribute("sessionVarName") : DPEWebContants.BLANK;
			String pkVal = sessionVarName + BeanUtils.getProperty(bean, pkName);
			String pkFieldName = pkVal;
			String size = (column.getAttribute("size") != null) ? (String)column.getAttribute("size") : DPEWebContants.BLANK;
			String maxlength = (column.getAttribute("maxlength") != null) ? (String)column.getAttribute("maxlength") : DPEWebContants.BLANK;
			Boolean validateIsNumeric = (column.getAttribute("validateIsNumeric") != null) ? Boolean.valueOf(column.getAttribute("validateIsNumeric").toString()) : false;
			String styleClass = (column.getAttribute("styleClass") != null) ? (String)column.getAttribute("styleClass") : DPEWebContants.BLANK;
			String style = (column.getAttribute("style") != null) ? (String)column.getAttribute("style") : DPEWebContants.BLANK;
			String enabledConditionFields = (column.getAttribute("enabledConditionFields") != null) ? (String)column.getAttribute("enabledConditionFields") : DPEWebContants.BLANK;
			String enabledConditionValues = (column.getAttribute("enabledConditionValues") != null) ? (String)column.getAttribute("enabledConditionValues") : DPEWebContants.BLANK;
			String dateFormat = (column.getAttribute("dateFormat") != null) ? (String)column.getAttribute("dateFormat") : DPEWebContants.BLANK;
			String numberFormat = (column.getAttribute("numberFormat") != null) ? (String)column.getAttribute("numberFormat") : DPEWebContants.BLANK;
			String[] enabledConditionFieldsArr = StringUtils.tokenizeToStringArray(enabledConditionFields, ",");
			String[] enabledConditionValuesArr = StringUtils.tokenizeToStringArray(enabledConditionValues, ",");
			String hiddenIdName = (column.getAttribute("hiddenPkName") != null) ? (String)column.getAttribute("hiddenPkName") : IGerivConstants.METHOD_BASE_PK_NAME;
			String hiddenPkId = (column.getAttribute("hiddenPkId") != null) ? (String)column.getAttribute("hiddenPkId") : IGerivConstants.METHOD_BASE_PK_NAME;
			String hiddenPkFieldName = (column.getAttribute("hiddenPkFieldName") != null) ? (String)column.getAttribute("hiddenPkFieldName") : pkName;
			Boolean hasHiddenPkField = Boolean.valueOf((column.getAttribute("hasHiddenPkField") != null) ? (String)column.getAttribute("hasHiddenPkField") : "false");
			String hiddenPkVal = (hiddenPkFieldName != null && !hiddenPkFieldName.equals("") && hasHiddenPkField) ? BeanUtils.getProperty(bean, hiddenPkFieldName) : pkVal;
			String idField = (column.getAttribute("idField") != null && !column.getAttribute("idField").equals("")) ? (String)column.getAttribute("idField") : "";
			String columnValue = "";
			if (column.getValue() != null) {
				if (!dateFormat.equals("")) {
					SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
					columnValue = sdf.format((Date)column.getValue());
				} else if (!numberFormat.equals("") && org.apache.commons.lang.NumberUtils.isNumber(column.getValue().toString())) {
					columnValue = ExtremeUtils.formatNumber(numberFormat, column.getValue(), model.getLocale());
				} else {
					columnValue = column.getValue().toString();
				}
			}
			Map<String, String> textDifferenze = (Map<String, String>) model.getContext()
					.getSessionAttribute(sessionVarName);
			String name = sessionVarName;
			if (bean instanceof BollaResaRichiamoPersonalizzatoDettaglioDto) {
				name = "resoRichiamo";
				hiddenIdName = "pkRichiamo";
			} else if (bean instanceof BollaResaFuoriVoceDettaglioDto) {
				name = "resoFuoriVoce";
				hiddenIdName = "pkFuoriVoce";
			} else if (bean instanceof FondoBollaDettaglioDto) {
				name = "differenzeFondoBolla";
				hiddenIdName = "pkFondoBolla";
			} else if (bean instanceof BollaResaFuoriVoceVo) {
				name = "resoFuoriVoce";
				hiddenIdName = "pkFuoriVoce";
			} else if (bean instanceof ProdottiNonEditorialiBollaDettaglioVo && sessionVarName.contains("index")) {
				name = sessionVarName.replaceAll("index", "" + ((ProdottiNonEditorialiBollaDettaglioVo) bean).getPk().getProgressivo());
			}
			if (bean instanceof ProdottiNonEditorialiBollaDettaglioVo && hiddenIdName != null && hiddenIdName.contains("index")) {
				hiddenIdName = hiddenIdName.replaceAll("index", "" + ((ProdottiNonEditorialiBollaDettaglioVo) bean).getPk().getProgressivo());
			}
			
			if (textDifferenze != null
					&& textDifferenze.containsKey(sessionVarName + pkFieldName)) {
				columnBuilder.getHtmlBuilder().input("text").name(name).id(pkVal).styleClass(styleClass).style(style).size(size).maxlength(maxlength).value(textDifferenze.get(sessionVarName + pkFieldName));
				columnBuilder.getHtmlBuilder().append("validateIsNumeric=" + validateIsNumeric + " ");
				if (!isTextFieldEnabled(bean, enabledConditionFieldsArr, enabledConditionValuesArr)) {
					columnBuilder.getHtmlBuilder().disabled();
				} 
				columnBuilder.getHtmlBuilder().xclose();
				if (hasHiddenPkField) {
					columnBuilder.getHtmlBuilder().input("hidden").name(hiddenIdName).id(hiddenPkId + pkVal).value(hiddenPkVal).xclose();
				}
				column.addAttribute("textFieldId", pkVal);
			} else {
				String idVal = pkVal;
				if (idVal.contains(".") && !(bean instanceof RichiestaRifornimentoDto)) {
					idVal = idVal.substring(idVal.lastIndexOf(".") + 1);
				}
				columnBuilder.getHtmlBuilder().input("text").name(name).id(idField + idVal).value(columnValue).styleClass(styleClass).style(style).size(size).maxlength(maxlength);
				columnBuilder.getHtmlBuilder().append("validateIsNumeric=" + validateIsNumeric + " ");
				if (!isTextFieldEnabled(bean, enabledConditionFieldsArr, enabledConditionValuesArr)) {
					columnBuilder.getHtmlBuilder().disabled();
				} 
				columnBuilder.getHtmlBuilder().xclose();
				if (hasHiddenPkField) {
					columnBuilder.getHtmlBuilder().input("hidden").name(hiddenIdName).id(hiddenPkId + pkVal).value(hiddenPkVal).xclose();
				}
				column.addAttribute("textFieldId", sessionVarName + pkVal);
			}
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
