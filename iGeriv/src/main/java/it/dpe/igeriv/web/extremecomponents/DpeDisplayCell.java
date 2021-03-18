package it.dpe.igeriv.web.extremecomponents;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.BeanUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.DisplayCell;
import org.extremecomponents.table.core.TableModel;

import it.dpe.igeriv.dto.BollaDettaglioDto;
import it.dpe.igeriv.util.DPEWebContants;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;

public class DpeDisplayCell extends DisplayCell {
	
	@Override
	public String getExportDisplay(TableModel model, Column column) {
		Object bean = model.getCurrentRowBean();
		try {
			if (column.getAlias().equals("tipoFondoBolla")) {
				String property = null;
				Timestamp dataFatturazione = null;
				try {
					property = BeanUtils.getProperty(bean, "indicatoreValorizzare");
				} catch (NoSuchMethodException e) {}
				if (bean instanceof BollaDettaglioDto) {
					BollaDettaglioDto dto = (BollaDettaglioDto)bean;
					dataFatturazione = dto.getDataFatturazionePrevista();
				}
				Integer indicatoreValorizzare = (property != null) ? Integer.valueOf(property) : null;
				if (indicatoreValorizzare != null) {
					if (indicatoreValorizzare.equals(IGerivConstants.INDICATORE_CONTO_DEPOSITO)) {
						String result = model.getMessages().getMessage(IGerivConstants.CONTO_DEPOSITO);
						if (dataFatturazione != null) {
							result += " " + DateUtilities.getTimestampAsString(dataFatturazione, "dd-MM-yyyy");
						}
						return result;
					} else if (indicatoreValorizzare.equals(IGerivConstants.INDICATORE_NON_VALORIZZARE)) {
						return model.getMessages().getMessage(IGerivConstants.NON_VALORIZZARE);
					} 
				}
			}
			if (column.getAttribute("exportStyle") != null) {
				column.setStyle(column.getAttributeAsString("exportStyle"));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		return super.getExportDisplay(model, column);
	}
	
	@Override
	protected String getCellValue(TableModel model, Column column) {
		StringBuffer cellValue = new StringBuffer("");
		try {
			Object bean = model.getCurrentRowBean();
			String javascriptFunction = (column.getAttribute("javascriptFunction") != null) ? (String)column.getAttribute("javascriptFunction") : DPEWebContants.BLANK;
			String href = (column.getAttribute("href") != null) ? (String)column.getAttribute("href") : DPEWebContants.BLANK;
			String rel = (column.getAttribute("rel") != null) ? (String)column.getAttribute("rel") : DPEWebContants.BLANK;
			String styleClass = (column.getAttribute("styleClass") != null) ? (String)column.getAttribute("styleClass") : DPEWebContants.BLANK;
			Boolean preserveBlankSpaces = (column.getAttribute("preserveBlankSpaces") != null) ? Boolean.valueOf(column.getAttribute("preserveBlankSpaces").toString()) : false;
			Boolean allowZeros = (column.getAttribute("allowZeros") != null) ? Boolean.valueOf(column.getAttribute("allowZeros").toString()) : false;
			List<String> paramList = null;
			StringBuffer sbHrefParams = new StringBuffer();
			StringBuffer sbHrefValues = new StringBuffer();
			if (!javascriptFunction.equals(DPEWebContants.BLANK)) {
				String jsFunction = javascriptFunction.substring(javascriptFunction.indexOf("(") + 1, javascriptFunction.indexOf(")"));
				paramList = new ArrayList<String>(); 
				StringTokenizer st = new StringTokenizer(jsFunction,",");
				while (st.hasMoreTokens()) {
					String token = st.nextToken().trim();
					String property = BeanUtils.getProperty(bean, token).replaceAll("'", "\\\\'").replaceAll(" ", "&nbsp;");
					paramList.add("'" + property + "'");
				}
			}
			if (!href.equals(DPEWebContants.BLANK)) {
				String hrefParams = href.substring(href.indexOf("?") + 1);
				paramList = new ArrayList<String>(); 
				StringTokenizer st = new StringTokenizer(hrefParams,"&");
				int count = 0;
				while (st.hasMoreTokens()) {
					String field = st.nextToken().trim();
					StringTokenizer st1 = new StringTokenizer(field,"=");
					while (st1.hasMoreTokens()) {
						String token = st1.nextToken().trim();
						if ((count % 2) != 0) {
							sbHrefParams.append("=");
							String property = token;
							try {
								property = BeanUtils.getProperty(bean, token);
							} catch (NoSuchMethodException e) {
								//
							}
							sbHrefParams.append(property);
							sbHrefValues.append(property);
						} else {
							if (count > 0) {
								sbHrefParams.append("&");
							}
							sbHrefParams.append(token);
						}
						count++;
					}
				}
			}
			Boolean isLink = (column.getAttribute("isLink") != null) ? Boolean.valueOf(column.getAttribute("isLink").toString()) : false;
			if (isLink) {
				if (!javascriptFunction.equals(DPEWebContants.BLANK)) {
					javascriptFunction = javascriptFunction.substring(0, javascriptFunction.indexOf("("));
					String params = paramList.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");
					cellValue.append("<a id='" + params.replaceAll("'", "").replaceAll(",", "") + "' href=javascript:" + javascriptFunction + "(" + params + ")");
				} else if (!href.equals(DPEWebContants.BLANK)) {
					String firstPartHref = href.substring(0, href.indexOf("?") + 1);
					cellValue.append("<a id='" + sbHrefValues.toString() + "' href='" + firstPartHref + sbHrefParams.toString() + "'");
					if (column.getStyle() != null && !column.getStyle().equals("")) {
						cellValue.append(" style='" + column.getStyle() + "' ");
					}
					column.addAttribute("href1", firstPartHref + sbHrefParams.toString());
				}
				if (!styleClass.equals(DPEWebContants.BLANK)) {
					cellValue.append(" class='" + styleClass + "'");
				}
				if (!rel.equals(DPEWebContants.BLANK)) {
					cellValue.append(" rel='" + rel + "'");
				}
				cellValue.append(">");
			} else if (column.getProperty().equals("email")) {
				cellValue.append("<a href='mailto:" + column.getValue() + "' target='_blank'>");
			}
			String valueAsString = column.getValueAsString();
			if (preserveBlankSpaces) {
				valueAsString = valueAsString.replaceAll(" ", "&nbsp;");
			}
			if (column.getAlias().equals("immagine")) {
				if (BeanUtils.getProperty(bean, "immagine") == null || BeanUtils.getProperty(bean, "immagine").equals("")) {
					valueAsString = "";
				}
			} 
			if (column.getAlias().equals("nomeImmagine")) {
				if (BeanUtils.getProperty(bean, "nomeImmagine") == null || BeanUtils.getProperty(bean, "nomeImmagine").equals("")) {
					valueAsString = "";
				}
			}
			cellValue.append((valueAsString.equals("0") && !allowZeros) ? "" : valueAsString);
			if (isLink || column.getProperty().equals("email")) {
				cellValue.append("</a>");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return cellValue.toString();
	}
}

