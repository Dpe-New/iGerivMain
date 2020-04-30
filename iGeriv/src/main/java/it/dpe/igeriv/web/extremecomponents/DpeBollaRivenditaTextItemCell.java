package it.dpe.igeriv.web.extremecomponents;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.beanutils.BeanUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.util.HtmlBuilder;

import com.google.common.base.Strings;

import it.dpe.igeriv.dto.BollaDettaglioDto;
import it.dpe.igeriv.dto.FondoBollaDettaglioDto;
import it.dpe.igeriv.util.DPEWebContants;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.NumberUtils;

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
			boolean modalitaInforiv = Boolean.parseBoolean(column.getAttribute("modalitaInforiv") != null ? column.getAttribute("modalitaInforiv").toString() : "false");
			Integer spunta = (bean != null && bean instanceof BollaDettaglioDto) ? ((BollaDettaglioDto) bean).getSpunta() : (bean != null && bean instanceof FondoBollaDettaglioDto) ? ((FondoBollaDettaglioDto) bean).getSpunta() : 0;
			String fieldName = (bean instanceof BollaDettaglioDto) ? "fieldMap" : "fieldFBMap";
			
			
			// ticket 0000003
			//Boolean isMenta = (model.getContext().getSessionAttribute(IGerivConstants.SESSION_VAR_COD_FIEG_DL) != null) ? model.getContext().getSessionAttribute(IGerivConstants.SESSION_VAR_COD_FIEG_DL).equals(IGerivConstants.MENTA_CODE) : false;
	        Integer tipoRecordFondoBolla = (bean != null && bean instanceof FondoBollaDettaglioDto) ? ((FondoBollaDettaglioDto) bean).getTipoRecordFondoBolla() : 0;
			boolean readOnlyFondoBolla = false;
			if(fieldName.equals("fieldFBMap")){
				switch (tipoRecordFondoBolla.intValue()) {
				case IGerivConstants.TIPO_RECORD_FONDO_BOLLA_COR:
					readOnlyFondoBolla = false;
					break;
				case IGerivConstants.TIPO_RECORD_FONDO_BOLLA_RIFORNIMENTI:
					readOnlyFondoBolla = false;
					break;
				case IGerivConstants.TIPO_RECORD_FONDO_BOLLA_LIV:
					readOnlyFondoBolla = false;
					break;
				default:
					readOnlyFondoBolla = true;
					break;
				}
			}
			// end ticket 0000003
			
			String spuntaFieldName = (bean instanceof BollaDettaglioDto) ? "spuntaMap" : "spuntaFBMap";
			String modificatoFieldName = (bean instanceof BollaDettaglioDto) ? "modificatoMap" : "modificatoFBMap";
			if (modalitaInforiv) {
				size = "1";
				String cpLette = Strings.isNullOrEmpty(column.getValueAsString()) ? "" : "" + (new Integer(column.getValueAsString()) + new Integer(BeanUtils.getProperty(bean, "quantitaConsegnata").toString()));
				columnBuilder.getHtmlBuilder().input("text")
					.name("copieLette['" + pkVal + "']")
					.id("copieLette" + pkVal)
					.styleClass(styleClass)
					.style(style)
					.size(size)
					.value(cpLette.toString())
					.maxlength(maxlength)
					.xclose()
					.nbsp();
			}
			HtmlBuilder input = columnBuilder.getHtmlBuilder().input("text");
			input.name(fieldName + "['" + pkVal + "']")
				.id("differenze" + pkVal)
				.append(" modificato=false")
				.styleClass(styleClass)
				.style(style)
				.size(size)
				.maxlength(maxlength)
				.value(columnValue);
			
			boolean pubblicazioneNonUscita = (bean != null && bean instanceof BollaDettaglioDto) ? ((BollaDettaglioDto) bean).getPubblicazioneNonUscita() : false;
			if (modalitaInforiv || pubblicazioneNonUscita || readOnlyFondoBolla) {
				//if (modalitaInforiv || pubblicazioneNonUscita ) { //ticket 0000003
				input.readonly();
			}
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
