package it.dpe.igeriv.web.extremecomponents;

import it.dpe.igeriv.dto.ParametriClienteDto;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.EstrattoContoEdicolaDettaglioVo;

import java.util.Iterator;
import java.util.List;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.bean.Export;
import org.extremecomponents.table.calc.CalcResult;
import org.extremecomponents.table.calc.CalcUtils;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.PdfView;
import org.extremecomponents.util.ExtremeUtils;

import com.google.common.base.Strings;

public class DpePdfView extends PdfView {
	public final static String FONT = "exportPdf.font";
	public final static String TOTALS_FONT_SIZE = "exportPdf.totalsFontSize";
	public final static String FONT_SIZE = "exportPdf.fontSize";
	public final static String HEAD_FONT_SIZE = "exportPdf.headFontSize";
	public final static String HEADER_BACKGROUND_COLOR = "headerBackgroundColor";
	public final static String IS_LANDSCAPE = "isLandscape";
	public final static String MARGIN_TOP = "marginTopInches";
	public final static String IS_REPEAT_COLUMN_HEADERS = "repeatColumnHeaders";
	public final static String REGION_BEFORE_EXTENT = "regionBeforeExtentInches";
	public final static String LOGO_IMAGE = "logoImage";
	public final static String HEADER_TITLE = "headerTitle";
	public final static String TOTAL_ROW = "totalRow";
	public final static String HEADER_COLOR = "headerColor";

	private StringBuffer xlsfo = new StringBuffer();
	private String font;
	private String fontSize;
	private String totalsFontSize;
	private String headFontSize;
	private String marginTop;
	private String regionBeforeExtentInches;

	public DpePdfView() {
	}

	public void beforeBody(TableModel model) {
		Export export = model.getExportHandler().getCurrentExport();
		this.font = model.getPreferences().getPreference(FONT);
		this.fontSize = model.getPreferences().getPreference(FONT_SIZE);
		this.totalsFontSize = model.getPreferences().getPreference(TOTALS_FONT_SIZE);
		this.headFontSize = Strings.isNullOrEmpty(export.getAttributeAsString("headFontSize")) ? model.getPreferences().getPreference(HEAD_FONT_SIZE) : export.getAttributeAsString("headFontSize");

		String isLandscape = export.getAttributeAsString(IS_LANDSCAPE);
		String isRepeatColumnHeaders = export.getAttributeAsString(IS_REPEAT_COLUMN_HEADERS);
		ParametriClienteDto param = model.getContext().getSessionAttribute("paramEdicola5") != null ? (ParametriClienteDto) model.getContext().getSessionAttribute("paramEdicola5") : null;
		Boolean isRepeatHeaders = (param != null && !Strings.isNullOrEmpty(param.getValue())) ? Boolean.parseBoolean(param.getValue()) : (param != null && !Strings.isNullOrEmpty(param.getDefaultValue())) ? Boolean.parseBoolean(param.getDefaultValue()) : true;
		marginTop = export.getAttributeAsString(MARGIN_TOP);
		regionBeforeExtentInches = export.getAttributeAsString(REGION_BEFORE_EXTENT);
		String logoImage = getLogoImage(model);
		if (logoImage != null) {
			regionBeforeExtentInches = "2.5";
			marginTop = "2.0";
		}
		if (isLandscape != null && Boolean.parseBoolean(isLandscape)) {
			xlsfo.append(startRootLandscape(isRepeatHeaders));
		} else {
			xlsfo.append(startRoot(isRepeatHeaders));
		}
		xlsfo.append(regionBefore(model, logoImage));
		if (isRepeatHeaders) {
			xlsfo.append(regionAfter());
		}
		xlsfo.append(columnDefinitions(model, isRepeatColumnHeaders));
		xlsfo.append(header(model));
		xlsfo.append(" <fo:table-body> ");
	}

	public void body(TableModel model, Column column) {
		if (column.isFirstColumn()) {
			xlsfo.append(" <fo:table-row> ");
		}

		String value = DpeExportViewUtils.parsePDF(column.getCellDisplay());

		String displayAlign = null;
		if (column.getStyle() != null) {
			String style = column.getStyle().toString();
			if (style.contains("text-align")) {
				int beginIndex = style.indexOf("text-align:") + 11;
				displayAlign = style.substring(beginIndex);
				if (displayAlign.indexOf(";") != -1) {
					try {
						displayAlign = displayAlign.substring(0, displayAlign.indexOf(";"));
					} catch (StringIndexOutOfBoundsException e) {
						
					}
				}
			}
		}

		xlsfo.append(" <fo:table-cell border=\"solid silver .5px\" display-align=\"center\" padding=\"3pt\"> ");
		xlsfo.append(" <fo:block white-space-treatment=\"preserve\" font-family=\"" + getFont() + "'Times'\" font-size=\"" + getFontSize() + "\" " + ((displayAlign != null ? " text-align=\"" + displayAlign + "\" " : "")));
		if (model.getCurrentRowBean() != null && model.getCurrentRowBean() instanceof EstrattoContoEdicolaDettaglioVo) {
			EstrattoContoEdicolaDettaglioVo vo = (EstrattoContoEdicolaDettaglioVo) model.getCurrentRowBean();
			if (vo.getIsBold() != null && vo.getIsBold()) {
				xlsfo.append(" font-weight=\"bold\" ");
			}
		}
		xlsfo.append(">" + value + "</fo:block> ");
		xlsfo.append(" </fo:table-cell> ");

		if (column.isLastColumn()) {
			xlsfo.append(" </fo:table-row> ");
		}
	}

	public Object afterBody(TableModel model) {
		Export export = model.getExportHandler().getCurrentExport();
		String totaRow = export.getAttributeAsString(TOTAL_ROW);
		if (!Strings.isNullOrEmpty(totaRow)) {
			xlsfo.append(totaRow);
		} else {
			if (model.getLimit().getTotalRows() != 0) {
				xlsfo.append(totals(model));
			}
		}
		xlsfo.append(" </fo:table-body> ");
		xlsfo.append(endRoot());
		return xlsfo.toString();
	}

	public String startRoot(Boolean isRepeatHeaders) {
		StringBuffer sb = new StringBuffer();

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

		sb.append("<fo:root xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">");
		
		if (isRepeatHeaders) {
			sb.append(" <fo:layout-master-set> ");
			sb.append(" <fo:simple-page-master master-name=\"simple\" ");
			sb.append(" page-height=\"11in\" ");
			sb.append(" page-width=\"8.5in\" ");
			sb.append(" margin-top=\".5in\" ");
			sb.append(" margin-bottom=\".25in\" ");
			sb.append(" margin-left=\".5in\" ");
			sb.append(" margin-right=\".5in\"> ");
			sb.append(" <fo:region-body margin-top=\"" + (marginTop == null || marginTop.equals("") ? ".5" : marginTop) + "in\" margin-bottom=\".25in\"/> ");
			sb.append(" <fo:region-before extent=\"" + (regionBeforeExtentInches == null || regionBeforeExtentInches.equals("") ? ".5" : regionBeforeExtentInches) + "in\"/> ");
			sb.append(" <fo:region-after extent=\".25in\"/> ");
			sb.append(" </fo:simple-page-master> ");
			sb.append(" </fo:layout-master-set> ");
			sb.append(" <fo:page-sequence master-reference=\"simple\" initial-page-number=\"1\"> ");
		} else {
			sb.append(" <fo:layout-master-set> ");
			sb.append(" <fo:simple-page-master master-name=\"first-page\" ");
			sb.append(" page-height=\"11in\" ");
			sb.append(" page-width=\"8.5in\" ");
			sb.append(" margin-top=\".5in\" ");
			sb.append(" margin-bottom=\".25in\" ");
			sb.append(" margin-left=\".5in\" ");
			sb.append(" margin-right=\".5in\"> ");
			sb.append(" <fo:region-body margin-top=\"" + (marginTop == null || marginTop.equals("") ? ".5" : marginTop) + "in\" margin-bottom=\".25in\"/> ");
			sb.append(" <fo:region-before extent=\"" + (regionBeforeExtentInches == null || regionBeforeExtentInches.equals("") ? ".5" : regionBeforeExtentInches) + "in\"/> ");
			sb.append(" <fo:region-after extent=\".25in\"/> ");
			sb.append(" </fo:simple-page-master> ");
			sb.append(" <fo:simple-page-master master-name=\"all-pages\" ");
			sb.append(" page-height=\"11in\" ");
			sb.append(" page-width=\"8.5in\" ");
			sb.append(" margin-left=\".5in\" ");
			sb.append(" margin-right=\".5in\"> ");
			sb.append(" <fo:region-body margin-top=\".5in\" margin-bottom=\".25in\"/> ");
			sb.append(" </fo:simple-page-master> ");
			sb.append(" <fo:page-sequence-master master-name=\"my-sequence\"> ");
			sb.append(" <fo:single-page-master-reference master-reference=\"first-page\"/> ");
			sb.append(" <fo:repeatable-page-master-reference master-reference=\"all-pages\"/> ");
			sb.append(" </fo:page-sequence-master> ");
			sb.append(" </fo:layout-master-set> ");
			sb.append(" <fo:page-sequence master-reference=\"my-sequence\" initial-page-number=\"1\"> ");
		}

		return sb.toString();
	}

	/**
	 * @return
	 */
	private String startRootLandscape(Boolean isRepeatHeaders) {
		StringBuffer sb = new StringBuffer();

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

		sb.append("<fo:root xmlns:fo=\"http://www.w3.org/1999/XSL/Format\" xmlns:rx=\"http://www.renderx.com/XSL/Extensions\">");

		if (isRepeatHeaders) {
			sb.append(" <fo:layout-master-set> ");
			sb.append(" <fo:simple-page-master master-name=\"simple\" ");
			sb.append(" page-height=\"8.5in\" ");
			sb.append(" page-width=\"11in\" ");
			sb.append(" margin-top=\".5in\" ");
			sb.append(" margin-bottom=\".25in\" ");
			sb.append(" margin-left=\".5in\" ");
			sb.append(" margin-right=\".5in\"> ");
			sb.append(" <fo:region-body margin-top=\"" + (marginTop == null || marginTop.equals("") ? ".5" : marginTop) + "in\" margin-bottom=\".25in\"/> ");
			sb.append(" <fo:region-before extent=\"" + (regionBeforeExtentInches == null || regionBeforeExtentInches.equals("") ? ".5" : regionBeforeExtentInches) + "in\"/> ");
			sb.append(" <fo:region-after extent=\".25in\"/> ");
			sb.append(" </fo:simple-page-master> ");
			sb.append(" </fo:layout-master-set> ");
			sb.append(" <fo:page-sequence master-reference=\"simple\" initial-page-number=\"1\"> ");
		} else {
			sb.append(" <fo:layout-master-set> ");
			sb.append(" <fo:simple-page-master master-name=\"first-page\" ");
			sb.append(" page-height=\"8.5in\" ");
			sb.append(" page-width=\"11in\" ");
			sb.append(" margin-top=\".5in\" ");
			sb.append(" margin-bottom=\".25in\" ");
			sb.append(" margin-left=\".5in\" ");
			sb.append(" margin-right=\".5in\"> ");
			sb.append(" <fo:region-body margin-top=\"" + (marginTop == null || marginTop.equals("") ? ".5" : marginTop) + "in\" margin-bottom=\".25in\"/> ");
			sb.append(" <fo:region-before extent=\"" + (regionBeforeExtentInches == null || regionBeforeExtentInches.equals("") ? ".5" : regionBeforeExtentInches) + "in\"/> ");
			sb.append(" <fo:region-after extent=\".25in\"/> ");
			sb.append(" </fo:simple-page-master> ");
			sb.append(" <fo:simple-page-master master-name=\"all-pages\" ");
			sb.append(" page-height=\"8.5in\" ");
			sb.append(" page-width=\"11in\" ");
			sb.append(" margin-left=\".5in\" ");
			sb.append(" margin-right=\".5in\"> ");
			sb.append(" <fo:region-body margin-top=\".5in\" margin-bottom=\".25in\"/> ");
			sb.append(" </fo:simple-page-master> ");
			sb.append(" <fo:page-sequence-master master-name=\"my-sequence\"> ");
			sb.append(" <fo:single-page-master-reference master-reference=\"first-page\"/> ");
			sb.append(" <fo:repeatable-page-master-reference master-reference=\"all-pages\"/> ");
			sb.append(" </fo:page-sequence-master> ");
			sb.append(" </fo:layout-master-set> ");
			sb.append(" <fo:page-sequence master-reference=\"my-sequence\" initial-page-number=\"1\"> ");
		}
		
		return sb.toString();
	}

	public String regionBefore(TableModel model, String logoImage) {
        StringBuffer sb = new StringBuffer();
		Export export = model.getExportHandler().getCurrentExport();
		String headerColor = export.getAttributeAsString(HEADER_COLOR);
		sb.append("<fo:static-content flow-name=\"xsl-region-before\">");
		String title = export.getAttributeAsString(HEADER_TITLE);
		title = (title != null && !title.equals("")) ? title.replaceAll("<br>", "<fo:block/>") : title;
		if (logoImage != null && model.getContext().getSessionAttribute(IGerivConstants.SESSION_VAR_URL_PATH) != null) {
			sb.append("<fo:block text-align='left'>");
			sb.append("<fo:external-graphic src=\"url('" + model.getContext().getSessionAttribute(IGerivConstants.SESSION_VAR_URL_PATH) + "/" + logoImage + "')\"/>");
			sb.append("</fo:block>");
		} 
		sb.append(" <fo:block linefeed-treatment=\"preserve\" space-after.optimum=\"15pt\" color=\"" + headerColor + "\" font-size=\"" + getHeadFontSize() + "\" font-family=\"" + getHeadFont() + "'Times'\">" + title + "</fo:block> ");
		sb.append("</fo:static-content>");
		return sb.toString();
	}

	private String getLogoImage(TableModel model) {
		Boolean isCdl = (model.getContext().getSessionAttribute(IGerivConstants.SESSION_VAR_COD_FIEG_DL) != null) ? model.getContext().getSessionAttribute(IGerivConstants.SESSION_VAR_COD_FIEG_DL).equals(IGerivConstants.CDL_CODE) : false;
        String logoImage = isCdl ? model.getExportHandler().getCurrentExport().getAttributeAsString(LOGO_IMAGE) : null;
		return logoImage;
	}

	public String regionAfter() {
		StringBuffer sb = new StringBuffer();
		sb.append(" <fo:static-content flow-name=\"xsl-region-after\" display-align=\"after\"> ");
		sb.append(" <fo:block text-align=\"end\">Pagina <fo:page-number/></fo:block> ");
		sb.append(" </fo:static-content> ");
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public String columnDefinitions(TableModel model, String isRepeatColumnHeaders) {
		StringBuffer sb = new StringBuffer();

		sb.append(" <fo:flow flow-name=\"xsl-region-body\"> ");

		sb.append(" <fo:block font-family=\"" + getFont() + "'Times'\">");

		if (isRepeatColumnHeaders != null && Boolean.parseBoolean(isRepeatColumnHeaders)) {
			sb.append(" <fo:table table-layout=\"fixed\" font-size=\"" + getFontSize() + "\" hyphenate=\"true\" language=\"it\" table-omit-header-at-break=\"false\"> ");
		} else {
			sb.append(" <fo:table table-layout=\"fixed\" font-size=\"" + getFontSize() + "\" hyphenate=\"true\" language=\"it\" table-omit-header-at-break=\"true\"> ");
		}

		double columnCount = model.getColumnHandler().columnCount();
		double colwidth = 10 / columnCount;

		Iterator<Column> it = model.getColumnHandler().getColumns().iterator();
		int i = 1;
		while (it.hasNext()) {
			Column column = it.next();
			String width = column.getWidth();
			if (width != null && !width.equals("")) {
				colwidth = (10.0d / 100.0d) * Double.parseDouble(width.replace("%", ""));
			}
			sb.append(" <fo:table-column column-number=\"" + i + "\" column-width=\"" + String.valueOf(colwidth) + "in\"/> ");
			i++;
		}

		return sb.toString();
	}

	private String getFontSize() {
		return fontSize == null ? "10px" : fontSize;
	}

	private String getHeadFontSize() {
		return headFontSize == null ? "17px" : headFontSize;
	}

	public String getTotalsFontSize() {
		return totalsFontSize == null ? "12px" : totalsFontSize;
	}

	@SuppressWarnings("unchecked")
	public String header(TableModel model) {
		StringBuffer sb = new StringBuffer();

		Export export = model.getExportHandler().getCurrentExport();
		String headerColor = export.getAttributeAsString(HEADER_COLOR);
		String headerBackgroundColor = export.getAttributeAsString(HEADER_BACKGROUND_COLOR);

		sb.append(" <fo:table-header background-color=\"" + headerBackgroundColor + "\" color=\"" + headerColor + "\"> ");

		sb.append(" <fo:table-row> ");

		List<Column> columns = model.getColumnHandler().getHeaderColumns();
		for (Iterator<Column> iter = columns.iterator(); iter.hasNext();) {
			Column column = (Column) iter.next();
			String title = StringUtility.unescapeHTML(column.getCellDisplay());
			sb.append(" <fo:table-cell border=\"solid silver .5px\" text-align=\"center\" display-align=\"center\" padding=\"3pt\"> ");
			sb.append(" <fo:block font-family=\"" + getFont() + "'Times'\" font-size=\"" + getFontSize() + "\" font-weight=\"bold\">" + title + "</fo:block> ");
			sb.append(" </fo:table-cell> ");
		}

		sb.append(" </fo:table-row> ");

		sb.append(" </fo:table-header> ");

		return sb.toString();
	}

	public String endRoot() {
		StringBuffer sb = new StringBuffer();

		sb.append(" </fo:table> ");

		sb.append(" </fo:block> ");

		sb.append(" </fo:flow> ");

		sb.append(" </fo:page-sequence> ");

		sb.append(" </fo:root> ");

		return sb.toString();
	}

	protected String getFont() {
		return font == null ? "'" : font + "',";
	}

	protected String getHeadFont() {
		return font == null ? "'" : font + "',";
	}

	/**
	 * TWEST - New Method that answers a StringBuffer containing the totals
	 * information. If no totals exist on the model answer an empty buffer.
	 * 
	 * The totals row will be given the same style as the header row.
	 * 
	 * @param model
	 * @return StringBuffer containing the complete fo statement for totals
	 */
	@SuppressWarnings("unchecked")
	public StringBuffer totals(TableModel model) {
		StringBuffer sb = new StringBuffer();
		Export export = model.getExportHandler().getCurrentExport();
		String headerColor = export.getAttributeAsString(HEADER_COLOR);
		String headerBackgroundColor = export.getAttributeAsString(HEADER_BACKGROUND_COLOR);

		Column firstCalcColumn = model.getColumnHandler().getFirstCalcColumn();

		if (firstCalcColumn != null) {
			int rows = firstCalcColumn.getCalc().length;
			for (int i = 0; i < rows; i++) {
				sb.append("<fo:table-row>");
				int colCount = 0;
				boolean isSecondColCalculated = model.getColumnHandler().getColumns().size() > 1 ? ((Column) model.getColumnHandler().getColumns().get(1)).isCalculated() : false;
				for (Iterator<Column> iter = model.getColumnHandler().getColumns().iterator(); iter.hasNext();) {
					Column column = (Column) iter.next();
					colCount++;
					if (colCount == 2 && !isSecondColCalculated) {
						continue;
					}
					if (column.isFirstColumn()) {
						String calcTitle = CalcUtils.getFirstCalcColumnTitleByPosition(model, i);
						if (!isSecondColCalculated) {
							sb.append(" <fo:table-cell number-columns-spanned=\"2\" border=\"solid silver .5px\" text-align=\"center\" display-align=\"center\" padding=\"3pt\" background-color=\"");
						} else {
							sb.append(" <fo:table-cell border=\"solid silver .5px\" text-align=\"center\" display-align=\"center\" padding=\"3pt\" background-color=\"");
						}
						sb.append(headerBackgroundColor + "\" color=\"" + headerColor + "\">");
						sb.append(" <fo:block hyphenate=\"false\" font-family=\"" + getFont() + "'Times'\" font-size=\"" + getTotalsFontSize() + "\" font-weight=\"bold\">" + calcTitle);
						sb.append(" </fo:block></fo:table-cell> ");
						continue;
					}
					if (column.isCalculated()) {
						if (column.getAttribute("totalFormat") != null) {
							column.setFormat(column.getAttributeAsString("totalFormat"));
						}
						sb.append(" <fo:table-cell border=\"solid silver .5px\" text-align=\"center\" display-align=\"center\" padding=\"3pt\" background-color=\"");
						sb.append(headerBackgroundColor + "\" color=\"" + headerColor + "\"> ");
						sb.append(" <fo:block font-family=\"" + getFont() + "'Times'\" font-size=\"" + getTotalsFontSize() + "\" font-weight=\"bold\">");
						CalcResult calcResult = CalcUtils.getCalcResultsByPosition(model, column, i);
						Number value = calcResult.getValue();
						if (value != null) {
							String formatNumber = ExtremeUtils.formatNumber(column.getFormat(), value, model.getLocale());
							if (column.getAttribute("hasCurrencySign") != null && Boolean.parseBoolean(column.getAttribute("hasCurrencySign").toString())) {
								formatNumber = IGerivConstants.EURO_SIGN_ASCII + " " + formatNumber;
							}
							sb.append(formatNumber);
						} else {
							sb.append("n/a");
						}
						sb.append("</fo:block> ");
					} else {
						sb.append(" <fo:table-cell border=\"solid silver .5px\" text-align=\"center\" display-align=\"center\" padding=\"3pt\" background-color=\"");
						sb.append(headerBackgroundColor + "\" color=\"" + headerColor + "\"> ");
						sb.append(" <fo:block font-family=\"" + getFont() + "'Times'\" font-size=\"" + getFontSize() + "\" font-weight=\"bold\">");
						sb.append(" ");
						sb.append("</fo:block> ");
					}
					sb.append(" </fo:table-cell> ");
				}
				sb.append("</fo:table-row>");

			}
		}
		return sb;
	}
}
