package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.tag.ColumnTag;
 
/**
 * @author Marcello
 *
 */
public class DpeColumnTag extends ColumnTag {
	private static final long serialVersionUID = 1L;
	private String sessionVarName;
	private String size;
	private String maxlength;
	private Boolean validateIsNumeric;
	private Boolean isLink;
	private String javascriptFunction;
	private String optionKeys;
	private String optionValues;
	private String styleClass;
	private String pkName;
	private String fieldName;
	private String style;
	private String rel;
	private String href;
	private String linkClass;
	private String enabledConditionFields;
	private String enabledConditionValues;
	private String dateFormat;
	private Boolean preserveBlankSpaces;
	private String totalFormat;
	private String totalCellStyle;
	private String hasHiddenPkField;
	private String exportStyle;
	private String hasEmptyOption;
	private String hasCurrencySign;
	private String allowZeros;
	private String hiddenPkId;
	private String hiddenPkName;
	private String idField;
	private String numberFormat;
	private String hiddenPkFieldName;
	private String valueFieldList;
	
	public String getSessionVarName() {
		return sessionVarName;
	}

	public void setSessionVarName(String sessionVarName) {
		this.sessionVarName = sessionVarName;
	}
	
	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
	public String getMaxlength() {
		return maxlength;
	}

	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
	}
	
	public Boolean getValidateIsNumeric() {
		return validateIsNumeric;
	}

	public void setValidateIsNumeric(Boolean validateIsNumeric) {
		this.validateIsNumeric = validateIsNumeric;
	}

	public Boolean getIsLink() {
		return isLink;
	}

	public void setIsLink(Boolean isLink) {
		this.isLink = isLink;
	}

	public String getJavascriptFunction() {
		return javascriptFunction;
	}

	public void setJavascriptFunction(String javascriptFunction) {
		this.javascriptFunction = javascriptFunction;
	}

	public String getOptionKeys() {
		return optionKeys;
	}

	public void setOptionKeys(String optionKeys) {
		this.optionKeys = optionKeys;
	}
	
	public String getOptionValues() {
		return optionValues;
	}

	public void setOptionValues(String optionValues) {
		this.optionValues = optionValues;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getPkName() {
		return pkName;
	}

	public void setPkName(String pkName) {
		this.pkName = pkName;
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	
	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}	
	
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
	public String getLinkClass() {
		return linkClass;
	}

	public void setLinkClass(String linkClass) {
		this.linkClass = linkClass;
	}
	
	public String getEnabledConditionFields() {
		return enabledConditionFields;
	}

	public void setEnabledConditionFields(String enabledConditionFields) {
		this.enabledConditionFields = enabledConditionFields;
	}

	public String getEnabledConditionValues() {
		return enabledConditionValues;
	}

	public void setEnabledConditionValues(String enabledConditionValues) {
		this.enabledConditionValues = enabledConditionValues;
	}
	
	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public Boolean getPreserveBlankSpaces() {
		return preserveBlankSpaces;
	}

	public void setPreserveBlankSpaces(Boolean preserveBlankSpaces) {
		this.preserveBlankSpaces = preserveBlankSpaces;
	}
	
	public String getTotalFormat() {
		return totalFormat;
	}

	public void setTotalFormat(String totalFormat) {
		this.totalFormat = totalFormat;
	}
	
	public String getTotalCellStyle() {
		return totalCellStyle;
	}

	public void setTotalCellStyle(String totalCellStyle) {
		this.totalCellStyle = totalCellStyle;
	}
	
	public String getHasHiddenPkField() {
		return hasHiddenPkField;
	}

	public void setHasHiddenPkField(String hasHiddenPkField) {
		this.hasHiddenPkField = hasHiddenPkField;
	}
	
	public String getExportStyle() {
		return exportStyle;
	}

	public void setExportStyle(String exportStyle) {
		this.exportStyle = exportStyle;
	}
	
	public String getHasEmptyOption() {
		return hasEmptyOption;
	}

	public void setHasEmptyOption(String hasEmptyOption) {
		this.hasEmptyOption = hasEmptyOption;
	}
	
	public String getHasCurrencySign() {
		return hasCurrencySign;
	}

	public void setHasCurrencySign(String hasCurrencySign) {
		this.hasCurrencySign = hasCurrencySign;
	}
	
	public String getAllowZeros() {
		return allowZeros;
	}

	public void setAllowZeros(String allowZeros) {
		this.allowZeros = allowZeros;
	}

	public String getHiddenPkId() {
		return hiddenPkId;
	}

	public void setHiddenPkId(String hiddenPkId) {
		this.hiddenPkId = hiddenPkId;
	}

	public String getHiddenPkName() {
		return hiddenPkName;
	}

	public void setHiddenPkName(String hiddenPkName) {
		this.hiddenPkName = hiddenPkName;
	}
	
	public String getIdField() {
		return idField;
	}

	public void setIdField(String idField) {
		this.idField = idField;
	}
	
	public String getNumberFormat() {
		return numberFormat;
	}

	public void setNumberFormat(String numberFormat) {
		this.numberFormat = numberFormat;
	}
	
	public String getHiddenPkFieldName() {
		return hiddenPkFieldName;
	}

	public void setHiddenPkFieldName(String hiddenPkFieldName) {
		this.hiddenPkFieldName = hiddenPkFieldName;
	}
	
	public String getValueFieldList() {
		return valueFieldList;
	}

	public void setValueFieldList(String valueFieldList) {
		this.valueFieldList = valueFieldList;
	}

	@Override
	public void addColumnAttributes(TableModel model, Column column) {
		column.addAttribute("sessionVarName", sessionVarName); 
		column.addAttribute("size", size);
		column.addAttribute("maxlength", maxlength);
		column.addAttribute("validateIsNumeric", validateIsNumeric);
		column.addAttribute("isLink", isLink);
		column.addAttribute("javascriptFunction", javascriptFunction);
		column.addAttribute("optionKeys", optionKeys);
		column.addAttribute("optionValues", optionValues);
		column.addAttribute("fieldName", fieldName);
		column.addAttribute("pkName", pkName);
		column.addAttribute("styleClass", styleClass);
		column.addAttribute("style", style);
		column.addAttribute("rel", rel);
		column.addAttribute("href", href);
		column.addAttribute("linkClass", linkClass);
		column.addAttribute("enabledConditionFields", enabledConditionFields);
		column.addAttribute("enabledConditionValues", enabledConditionValues);
		column.addAttribute("dateFormat", dateFormat);
		column.addAttribute("preserveBlankSpaces", preserveBlankSpaces);
		column.addAttribute("totalFormat", totalFormat);
		column.addAttribute("totalCellStyle", totalCellStyle);
		column.addAttribute("hasHiddenPkField", hasHiddenPkField);
		column.addAttribute("exportStyle", exportStyle);
		column.addAttribute("hasEmptyOption", hasEmptyOption);
		column.addAttribute("hasCurrencySign", hasCurrencySign);
		column.addAttribute("allowZeros", allowZeros);
		column.addAttribute("hiddenPkId", hiddenPkId);
		column.addAttribute("hiddenPkName", hiddenPkName);
		column.addAttribute("idField", idField);
		column.addAttribute("numberFormat", numberFormat);
		column.addAttribute("hiddenPkFieldName", hiddenPkFieldName);
		column.addAttribute("valueFieldList", valueFieldList);
		column.setStyle(style);
		column.setStyleClass(styleClass);
		super.addColumnAttributes(model, column);
	}
	
}

