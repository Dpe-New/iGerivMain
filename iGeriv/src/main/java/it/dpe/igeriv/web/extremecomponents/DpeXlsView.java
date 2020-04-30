package it.dpe.igeriv.web.extremecomponents;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.calc.CalcResult;
import org.extremecomponents.table.calc.CalcUtils;
import org.extremecomponents.table.core.PreferencesConstants;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.ExportViewUtils;
import org.extremecomponents.table.view.XlsView;

import com.google.common.base.Strings;

import it.dpe.igeriv.util.NumberUtils;

@SuppressWarnings({"rawtypes","unchecked"})
public class DpeXlsView extends XlsView {
	
	private static Log logger = LogFactory.getLog(DpeXlsView.class);
    public static final int WIDTH_MULT = 240; // width per char
    public static final int MIN_CHARS = 8; // minimum char width
    public static final short DEFAULT_FONT_HEIGHT = 8;
    public static final double NON_NUMERIC = -.99999;
    public static final String DEFAULT_MONEY_FORMAT = "$###,###,##0.00";
    public static final String DEFAULT_PERCENT_FORMAT = "##0.0%";
    public static final String NBSP = "&nbsp;";

    private HSSFWorkbook wb;
    private HSSFSheet sheet;
    private HSSFPrintSetup ps;
    private Map styles;
    private short rownum;
    private short cellnum;
    private HSSFRow hssfRow;
    private String moneyFormat;
    private String dateFormat;
    private String percentFormat;
    private String encoding;

    public DpeXlsView() {
    }

    public void beforeBody(TableModel model) {
        logger.debug("XlsView.init()");

        moneyFormat = model.getPreferences().getPreference(PreferencesConstants.TABLE_EXPORTABLE + "format.money");
        dateFormat = model.getPreferences().getPreference(PreferencesConstants.TABLE_EXPORTABLE + "format.date");
        if (StringUtils.isEmpty(moneyFormat)) {
            moneyFormat = DEFAULT_MONEY_FORMAT;
        }
        moneyFormat = moneyFormat.replaceAll("\u20AC", "");
        moneyFormat = moneyFormat.trim();
        
        percentFormat = model.getPreferences().getPreference(PreferencesConstants.TABLE_EXPORTABLE + "format.percent");
        if (StringUtils.isEmpty(percentFormat)) {
            percentFormat = DEFAULT_PERCENT_FORMAT;
        }

        encoding = model.getExportHandler().getCurrentExport().getEncoding();

        wb = new HSSFWorkbook();
        sheet = wb.createSheet();

        if (encoding.equalsIgnoreCase("UTF")) {
            wb.setSheetName(0, "Export Workbook", HSSFWorkbook.ENCODING_UTF_16);
        } else if (encoding.equalsIgnoreCase("UNICODE")) {
            wb.setSheetName(0, "Export Workbook", HSSFWorkbook.ENCODING_COMPRESSED_UNICODE);
        }

        styles = initStyles(wb);
        ps = sheet.getPrintSetup();

        sheet.setAutobreaks(true);
        ps.setFitHeight((short) 1);
        ps.setFitWidth((short) 1);

        createHeader(model);
    }

    public void body(TableModel model, Column column) {
    	String cellDisplay = column.getCellDisplay();
    	String cellDisplay2 = cellDisplay;
		if (StringUtils.contains(cellDisplay, "&nbsp;")) {
			cellDisplay = cellDisplay.replaceAll("&nbsp;", " ");
        } 
		if (column.getCell().equals(DpeNumberCell.class.getCanonicalName())) {
			cellDisplay = NumberUtils.defaultFormatNumber(column.getFormat(), cellDisplay, model.getLocale());
		}
		column.setCellDisplay(cellDisplay);
		
        if (column.isFirstColumn()) {
            rownum++;
            cellnum = 0;
            hssfRow = sheet.createRow(rownum);
        }

        String value = ExportViewUtils.parseXLS(column.getCellDisplay());

        HSSFCell hssfCell = hssfRow.createCell(cellnum);

        setCellEncoding(hssfCell);
        String styleModifier = getStyleModifier(column.getFormat());
        
        if (column.isEscapeAutoFormat()) {
            writeToCellAsText(hssfCell, value, styleModifier);
        } else if (column.isDate()) {
        	hssfCell.setCellStyle(getCellStyle("dateStyle", ""));
        	hssfCell.setCellValue(value);
        } else if (cellDisplay2 != null){
            writeToCellFormatted(hssfCell, cellDisplay2, styleModifier, model.getLocale());
        }
        cellnum++;
    }

    private String getStyleModifier(String format) {
    	String modifier = "";
		if (!Strings.isNullOrEmpty(format)) {
			if (format.endsWith(",0000") || format.endsWith(".0000")) {
				return "4decimals";
			}
		}
		return modifier;
	}

	public Object afterBody(TableModel model) {
        if (model.getLimit().getTotalRows() != 0) {
            totals(model);
        }
        return wb;
    }

    private void createHeader(TableModel model) {
        rownum = 0;
        cellnum = 0;
        HSSFRow row = sheet.createRow(rownum);

        List columns = model.getColumnHandler().getHeaderColumns();
        for (Iterator iter = columns.iterator(); iter.hasNext();) {
            Column column = (Column) iter.next();
            String title = column.getCellDisplay();
            HSSFCell hssfCell = row.createCell(cellnum);

            setCellEncoding(hssfCell);

            hssfCell.setCellStyle(getCellStyle("titleStyle", ""));
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue(title);
            int valWidth = (title + "").length() * WIDTH_MULT;
            sheet.setColumnWidth(hssfCell.getCellNum(), (short) valWidth);

            cellnum++;
        }
    }

    private void writeToCellAsText(HSSFCell cell, String value, String styleModifier) {
        // format text
        if (value.trim().equals(NBSP)) {
            value = "";
        }
        cell.setCellStyle(getCellStyle("textStyle", styleModifier));
        fixWidthAndPopulate(cell, NON_NUMERIC, value);
    }

    private void writeToCellFormatted(HSSFCell cell, String value, String styleModifier, Locale locale) {
        double numeric = NON_NUMERIC;

        try {
        	if (locale != null) {
        		numeric = org.springframework.util.NumberUtils.parseNumber(value.replaceAll("\u20AC", "").trim(), Double.class, NumberFormat.getNumberInstance(locale));
        	} else {
        		numeric = Double.parseDouble(value);
        	}
        } catch (Exception e) {
            numeric = NON_NUMERIC;
        }
        
        if (value.startsWith("\u20AC") || value.startsWith("$") || value.endsWith("%") || value.startsWith("($")) {
            boolean moneyFlag = (value.startsWith("\u20AC") || value.startsWith("$") || value.startsWith("($"));
            boolean percentFlag = value.endsWith("%");
            
            value = StringUtils.replace(value, "\u20AC", "");
            value = StringUtils.replace(value, "$", "");
            value = StringUtils.replace(value, "%", "");
            value = StringUtils.replace(value, ",", "");
            value = StringUtils.replace(value, "(", "-");
            value = StringUtils.replace(value, ")", "");
            value = value.trim();
            
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            if (moneyFlag) {
                // format money
                HSSFCellStyle hssfCellStyle = getCellStyle("moneyStyle", styleModifier);
				cell.setCellStyle(hssfCellStyle);
            } else if (percentFlag) {
                // format percent
                numeric = numeric / 100;
                cell.setCellStyle(getCellStyle("percentStyle", styleModifier));
            }
        } else if (numeric != NON_NUMERIC) {
            // format numeric
            cell.setCellStyle(getCellStyle("numericStyle", styleModifier));
        } else {
            // format text
            if (value.trim().equals(NBSP)) {
                value = "";
            }
            cell.setCellStyle(getCellStyle("textStyle", styleModifier));
        }

        fixWidthAndPopulate(cell, numeric, value);
    }

    private HSSFCellStyle getCellStyle(String style, String styleModifier) {
    	if (!Strings.isNullOrEmpty(styleModifier) && styles.get(style + styleModifier) != null) {
    		return (HSSFCellStyle) styles.get(style + styleModifier);
    	} else {
    		return (HSSFCellStyle) styles.get(style);
    	}
	}

	private void fixWidthAndPopulate(HSSFCell cell, double numeric, String value) {
        int valWidth = 0;

        if (numeric != NON_NUMERIC) {
            cell.setCellValue(numeric);
            valWidth = (cell.getNumericCellValue() + "$,.").length() * WIDTH_MULT;
        } else {
            cell.setCellValue(value);
            valWidth = (cell.getStringCellValue() + "").length() * WIDTH_MULT;

            if (valWidth < (WIDTH_MULT * MIN_CHARS)) {
                valWidth = WIDTH_MULT * MIN_CHARS;
            }
        }

        if (valWidth > sheet.getColumnWidth(cell.getCellNum())) {
            sheet.setColumnWidth(cell.getCellNum(), (short) valWidth);
        }
    }

    private Map initStyles(HSSFWorkbook wb) {
        return initStyles(wb, DEFAULT_FONT_HEIGHT);
    }

	private Map initStyles(HSSFWorkbook wb, short fontHeight) {
        Map result = new HashMap();
        HSSFCellStyle titleStyle = wb.createCellStyle();
        HSSFCellStyle textStyle = wb.createCellStyle();
        HSSFCellStyle boldStyle = wb.createCellStyle();
        HSSFCellStyle numericStyle = wb.createCellStyle();
        HSSFCellStyle numericStyleBold = wb.createCellStyle();
        HSSFCellStyle moneyStyle = wb.createCellStyle();
        HSSFCellStyle moneyStyle4decimals = wb.createCellStyle();
        HSSFCellStyle moneyStyleBold = wb.createCellStyle();
        HSSFCellStyle percentStyle = wb.createCellStyle();
        HSSFCellStyle percentStyleBold = wb.createCellStyle();

        // Add to export totals
        HSSFCellStyle moneyStyle_Totals = wb.createCellStyle();
        HSSFCellStyle naStyle_Totals = wb.createCellStyle();
        HSSFCellStyle numericStyle_Totals = wb.createCellStyle();
        HSSFCellStyle percentStyle_Totals = wb.createCellStyle();
        HSSFCellStyle textStyle_Totals = wb.createCellStyle();
        
        // Date Style
        HSSFCellStyle dateStyle = wb.createCellStyle();
        
        result.put("titleStyle", titleStyle);
        result.put("textStyle", textStyle);
        result.put("boldStyle", boldStyle);
        result.put("numericStyle", numericStyle);
        result.put("numericStyleBold", numericStyleBold);
        result.put("moneyStyle", moneyStyle);
        result.put("moneyStyle4decimals", moneyStyle4decimals);
        result.put("moneyStyleBold", moneyStyleBold);
        result.put("percentStyle", percentStyle);
        result.put("percentStyleBold", percentStyleBold);

        // Add to export totals
        result.put("moneyStyle_Totals", moneyStyle_Totals);
        result.put("naStyle_Totals", naStyle_Totals);
        result.put("numericStyle_Totals", numericStyle_Totals);
        result.put("percentStyle_Totals", percentStyle_Totals);
        result.put("textStyle_Totals", textStyle_Totals);
        
        result.put("dateStyle", dateStyle);
        
        HSSFDataFormat format = wb.createDataFormat();

        // Global fonts
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        font.setColor(HSSFColor.BLACK.index);
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints(fontHeight);

        HSSFFont fontBold = wb.createFont();
        fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        fontBold.setColor(HSSFColor.BLACK.index);
        fontBold.setFontName(HSSFFont.FONT_ARIAL);
        fontBold.setFontHeightInPoints(fontHeight);
        
        // Date Style
        dateStyle.setFont(font);
        dateStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        
        // Money Style
        moneyStyle.setFont(font);
        moneyStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        moneyStyle.setDataFormat(format.getFormat(moneyFormat));

        // Money Style 4 decimals
        moneyStyle4decimals.setFont(font);
        moneyStyle4decimals.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        moneyStyle4decimals.setDataFormat(format.getFormat("###,###,##0.0000"));

        
        // Money Style Bold
        moneyStyleBold.setFont(fontBold);
        moneyStyleBold.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        moneyStyleBold.setDataFormat(format.getFormat(moneyFormat));

        // Percent Style
        percentStyle.setFont(font);
        percentStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        percentStyle.setDataFormat(format.getFormat(percentFormat));

        // Percent Style Bold
        percentStyleBold.setFont(fontBold);
        percentStyleBold.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        percentStyleBold.setDataFormat(format.getFormat(percentFormat));

        // Standard Numeric Style
        numericStyle.setFont(font);
        numericStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

        // Standard Numeric Style Bold
        numericStyleBold.setFont(fontBold);
        numericStyleBold.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

        // Title Style
        titleStyle.setFont(font);
        titleStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        titleStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        titleStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        titleStyle.setRightBorderColor(HSSFColor.BLACK.index);
        titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        titleStyle.setTopBorderColor(HSSFColor.BLACK.index);
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // Standard Text Style
        textStyle.setFont(font);
        textStyle.setWrapText(true);

        // Standard Text Style
        boldStyle.setFont(fontBold);
        boldStyle.setWrapText(true);

        // Money Style Total
        moneyStyle_Totals.setFont(fontBold);
        moneyStyle_Totals.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        moneyStyle_Totals.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        moneyStyle_Totals.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        moneyStyle_Totals.setBottomBorderColor(HSSFColor.BLACK.index);
        moneyStyle_Totals.setBorderTop(HSSFCellStyle.BORDER_THIN);
        moneyStyle_Totals.setTopBorderColor(HSSFColor.BLACK.index);
        moneyStyle_Totals.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        moneyStyle_Totals.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        moneyStyle_Totals.setDataFormat(format.getFormat(moneyFormat));

        // n/a Style Total
        naStyle_Totals.setFont(fontBold);
        naStyle_Totals.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        naStyle_Totals.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        naStyle_Totals.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        naStyle_Totals.setBottomBorderColor(HSSFColor.BLACK.index);
        naStyle_Totals.setBorderTop(HSSFCellStyle.BORDER_THIN);
        naStyle_Totals.setTopBorderColor(HSSFColor.BLACK.index);
        naStyle_Totals.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        naStyle_Totals.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // Numeric Style Total
        numericStyle_Totals.setFont(fontBold);
        numericStyle_Totals.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        numericStyle_Totals.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        numericStyle_Totals.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        numericStyle_Totals.setBottomBorderColor(HSSFColor.BLACK.index);
        numericStyle_Totals.setBorderTop(HSSFCellStyle.BORDER_THIN);
        numericStyle_Totals.setTopBorderColor(HSSFColor.BLACK.index);
        numericStyle_Totals.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        numericStyle_Totals.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // Percent Style Total
        percentStyle_Totals.setFont(fontBold);
        percentStyle_Totals.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        percentStyle_Totals.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        percentStyle_Totals.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        percentStyle_Totals.setBottomBorderColor(HSSFColor.BLACK.index);
        percentStyle_Totals.setBorderTop(HSSFCellStyle.BORDER_THIN);
        percentStyle_Totals.setTopBorderColor(HSSFColor.BLACK.index);
        percentStyle_Totals.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        percentStyle_Totals.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        percentStyle_Totals.setDataFormat(format.getFormat(percentFormat));

        // Text Style Total
        textStyle_Totals.setFont(fontBold);
        textStyle_Totals.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        textStyle_Totals.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        textStyle_Totals.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        textStyle_Totals.setBottomBorderColor(HSSFColor.BLACK.index);
        textStyle_Totals.setBorderTop(HSSFCellStyle.BORDER_THIN);
        textStyle_Totals.setTopBorderColor(HSSFColor.BLACK.index);
        textStyle_Totals.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        textStyle_Totals.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return result;
    }

    // Add to export totals
    public void totals(TableModel model) {
        Column firstCalcColumn = model.getColumnHandler().getFirstCalcColumn();
        if (firstCalcColumn != null) {
            int rows = firstCalcColumn.getCalc().length;
            for (int i = 0; i < rows; i++) {
                rownum++;
                HSSFRow row = sheet.createRow(rownum);
                cellnum = 0;
                for (Iterator iter = model.getColumnHandler().getColumns().iterator(); iter.hasNext();) {
                    Column column = (Column) iter.next();
                    if (column.isFirstColumn()) {
                        String calcTitle = CalcUtils.getFirstCalcColumnTitleByPosition(model, i);
                        HSSFCell cell = row.createCell(cellnum);
                        setCellEncoding(cell);
                        if (column.isEscapeAutoFormat()) {
                            writeToCellAsText(cell, calcTitle, "_Totals");
                        } else {
                            writeToCellFormatted(cell, calcTitle, "_Totals", model.getLocale());
                        }
                        cellnum++;
                        continue;
                    }

                    if (column.isCalculated()) {
                        CalcResult calcResult = CalcUtils.getCalcResultsByPosition(model, column, i);
                        Number value = calcResult.getValue();
                        HSSFCell cell = row.createCell(cellnum);
                        setCellEncoding(cell);
                        if (value != null)
                            if (column.isEscapeAutoFormat()) {
                                writeToCellAsText(cell, value.toString(), "_Totals");
                            } else {
                                writeToCellFormatted(cell, NumberUtils.formatNumber(value, model.getLocale(), column.getFormat()), "_Totals", model.getLocale());
                            }
                        else {
                            cell.setCellStyle(getCellStyle("naStyle_Totals", ""));
                            cell.setCellValue("n/a");
                        }
                        cellnum++;
                    } else {
                        HSSFCell cell = row.createCell(cellnum);
                        setCellEncoding(cell);
                        writeToCellFormatted(cell, "", "_Totals", model.getLocale());
                        cellnum++;
                    }
                }
            }
        }

    }


    //add to set Cell encoding
    private void setCellEncoding(HSSFCell cell) {
        if (encoding.equalsIgnoreCase("UTF")) {
            cell.setEncoding(HSSFCell.ENCODING_UTF_16);
        } else if (encoding.equalsIgnoreCase("UNICODE")) {
            cell.setEncoding(HSSFCell.ENCODING_COMPRESSED_UNICODE);
        }
    }

	
}
