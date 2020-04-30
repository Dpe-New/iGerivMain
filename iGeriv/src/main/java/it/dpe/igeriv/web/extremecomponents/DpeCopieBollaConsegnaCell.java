package it.dpe.igeriv.web.extremecomponents;

import java.text.MessageFormat;
import java.util.Map;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.DisplayCell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.extremecomponents.util.HtmlBuilder;

import com.google.common.base.Joiner;

import it.dpe.igeriv.dto.BollaDettaglio;
import it.dpe.igeriv.util.DPEWebContants;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivUtils;
import it.dpe.igeriv.util.SpringContextManager;

public class DpeCopieBollaConsegnaCell extends DisplayCell {
	private final IGerivUtils iGerivUtils;
	private final Joiner.MapJoiner mapJoiner = Joiner.on('&').withKeyValueSeparator("=");
	
	public DpeCopieBollaConsegnaCell() {
		this.iGerivUtils = SpringContextManager.getSpringContext().getBean(IGerivUtils.class);
	}
	
	@Override
	public String getExportDisplay(TableModel model, Column column) {
		BollaDettaglio bean = (BollaDettaglio) model.getCurrentRowBean();
		try {
			if (column.getAlias().equals("tipoFondoBolla")) {
				Integer indicatoreValorizzare = bean.getIndicatoreValorizzare();
				if (indicatoreValorizzare != null) {
					if (indicatoreValorizzare.equals(IGerivConstants.INDICATORE_CONTO_DEPOSITO)) {
						return model.getMessages().getMessage(IGerivConstants.CONTO_DEPOSITO);
					} else if (indicatoreValorizzare.equals(IGerivConstants.INDICATORE_NON_VALORIZZARE)) {
						return model.getMessages().getMessage(IGerivConstants.NON_VALORIZZARE);
					} 
				}
			} else if (column.getAlias().equals("quantitaConsegnata") && model.getContext().getRequestAttribute("BollaControlloTab_ev") != null && model.getContext().getRequestAttribute("BollaControlloTab_ev").equals("pdf")) {
				if (bean.getVariazione() != 0) {
					String variazione = bean.getVariazione() > 0 ? "+" + bean.getVariazione() : bean.getVariazione().toString();
					return column.getPropertyValueAsString() + " (" + variazione + ")";
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
		ColumnBuilder columnBuilder = new ColumnBuilder(column);
		HtmlBuilder htmlBuilder = columnBuilder.getHtmlBuilder();
		try {
			BollaDettaglio bean = (BollaDettaglio) model.getCurrentRowBean();
			String rel = (column.getAttribute("rel") != null) ? (String)column.getAttribute("rel") : DPEWebContants.BLANK;
			String styleClass = (column.getAttribute("styleClass") != null) ? (String)column.getAttribute("styleClass") : DPEWebContants.BLANK;
			Boolean preserveBlankSpaces = (column.getAttribute("preserveBlankSpaces") != null) ? Boolean.valueOf(column.getAttribute("preserveBlankSpaces").toString()) : false;
			Boolean allowZeros = (column.getAttribute("allowZeros") != null) ? Boolean.valueOf(column.getAttribute("allowZeros").toString()) : false;
			String href = (column.getAttribute("href") != null) ? (String)column.getAttribute("href") : DPEWebContants.BLANK;
			Boolean isLink = column.getAttribute("isLink") != null;
			
			Map<String, String> mapParams = iGerivUtils.buildParamsMap(bean, href);
			
			if (isLink && !mapParams.isEmpty() && !mapParams.values().contains(null)) {
				String firstPartHref = href.substring(0, href.indexOf("?") + 1);
				String keys = Joiner.on("").join(mapParams.values());
				htmlBuilder.a(firstPartHref + mapJoiner.join(mapParams)).id(keys);
				if (column.getStyle() != null && !column.getStyle().equals("")) {
					htmlBuilder.style(column.getStyle());
				}
				column.addAttribute("href1", firstPartHref + keys);
				if (!styleClass.equals(DPEWebContants.BLANK)) {
					htmlBuilder.styleClass(styleClass);
				}
				if (!rel.equals(DPEWebContants.BLANK)) {
					htmlBuilder.append(" rel='" + rel + "'");
				}
				htmlBuilder.close();
			} else {
				htmlBuilder.span().style("text-align:center; font-weight:bold; font-size:120%;").close();
			}
			String valueAsString = column.getValueAsString();
			if (preserveBlankSpaces) {
				valueAsString = valueAsString.replaceAll(" ", "&nbsp;");
			}
			htmlBuilder.append((valueAsString.equals("0") && !allowZeros) ? "" : valueAsString);
			if (isLink) {
				htmlBuilder.aEnd();
			}
			if (bean.getVariazione() != 0) {
				String variazione = bean.getVariazione() > 0 ? "+" + bean.getVariazione() : bean.getVariazione().toString();
				htmlBuilder.nbsp().span().title(MessageFormat.format(model.getMessages().getMessage("igeriv.variazione.ditribuito"), variazione)).styleClass("variazioneClass").style("cursor:pointer").close().append("(" + variazione + ")").spanEnd();
			}
			if (!isLink || mapParams.isEmpty() || mapParams.values().contains(null)) {
				htmlBuilder.spanEnd();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return htmlBuilder.toString();
	}
}

