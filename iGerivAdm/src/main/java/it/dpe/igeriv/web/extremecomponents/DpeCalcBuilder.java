package it.dpe.igeriv.web.extremecomponents;

import java.util.Iterator;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.calc.CalcResult;
import org.extremecomponents.table.calc.CalcUtils;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.BuilderConstants;
import org.extremecomponents.table.view.html.CalcBuilder;
import org.extremecomponents.util.ExtremeUtils;
import org.extremecomponents.util.HtmlBuilder;

public class DpeCalcBuilder extends CalcBuilder {

	public DpeCalcBuilder(HtmlBuilder html, TableModel model) {
		super(html, model);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void singleRowCalcResults() {
		getHtmlBuilder().tr(1).styleClass(BuilderConstants.CALC_ROW_CSS).close();
        for (Iterator iter = getTableModel().getColumnHandler().getColumns().iterator(); iter.hasNext();) {
            Column column = (Column) iter.next();
            if (column.isFirstColumn()) {
                String calcTitle[] = CalcUtils.getFirstCalcColumnTitles(getTableModel());
                if (calcTitle != null && calcTitle.length > 0) {
                    getHtmlBuilder().td(2).styleClass(BuilderConstants.CALC_TITLE_CSS).close();
                    for (int i = 0; i < calcTitle.length; i++) {
                        String title = calcTitle[i];
                        getHtmlBuilder().append(title);
                        if (calcTitle.length > 0 && i + 1 != calcTitle.length) {
                            getHtmlBuilder().append(" / ");
                        }
                    }
                    getHtmlBuilder().tdEnd();
                }

                continue;
            }

            if (column.isCalculated()) {            	
                HtmlBuilder td = getHtmlBuilder().td(2).styleClass(BuilderConstants.CALC_RESULT_CSS);
                if (column.getAttribute("totalCellStyle") != null) {
                	td.style(column.getAttributeAsString("totalCellStyle"));
                }
                td.close();
                CalcResult calcResults[] = CalcUtils.getCalcResults(getTableModel(), column);
                for (int i = 0; i < calcResults.length; i++) {
                    CalcResult calcResult = calcResults[i];
                    Number value = calcResult.getValue();
                    if (value == null) {
                        getHtmlBuilder().append(calcResult.getName());
                    } else {
                    	String format = column.getAttribute("totalFormat") != null ? column.getAttributeAsString("totalFormat") : column.getFormat();
                        getHtmlBuilder().append(ExtremeUtils.formatNumber(format, value, getTableModel().getLocale()));
                    }

                    if (calcResults.length > 0 && i + 1 != calcResults.length) {
                        getHtmlBuilder().append(" / ");
                    }
                }
            } else {
                getHtmlBuilder().td(2).close();
                getHtmlBuilder().nbsp();
            }

            getHtmlBuilder().tdEnd();
        }
        getHtmlBuilder().trEnd(1);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void multiRowCalcResults() {
		Column firstCalcColumn = getTableModel().getColumnHandler().getFirstCalcColumn();
        int rows = firstCalcColumn.getCalc().length;

        for (int i = 0; i < rows; i++) {
            getHtmlBuilder().tr(1).styleClass(BuilderConstants.CALC_ROW_CSS).close();

            for (Iterator iter = getTableModel().getColumnHandler().getColumns().iterator(); iter.hasNext();) {
                Column column = (Column) iter.next();
                if (column.isFirstColumn()) {
                    String calcTitle = CalcUtils.getFirstCalcColumnTitleByPosition(getTableModel(), i);
                    getHtmlBuilder().td(2).style(column.getStyle()).styleClass(BuilderConstants.CALC_TITLE_CSS).close();
                    getHtmlBuilder().append(calcTitle);
                    getHtmlBuilder().tdEnd();
                    continue;
                }

                if (column.isCalculated()) {
                	HtmlBuilder td = getHtmlBuilder().td(2).styleClass(BuilderConstants.CALC_RESULT_CSS);
                    if (column.getAttribute("totalCellStyle") != null) {
                    	td.style(column.getAttributeAsString("totalCellStyle"));
                    }
                    td.close();
                    CalcResult calcResult = CalcUtils.getCalcResultsByPosition(getTableModel(), column, i);
                    Number value = calcResult.getValue();
                    if (value == null) {
                        getHtmlBuilder().append(calcResult.getName());
                    } else {
                    	String format = column.getAttribute("totalFormat") != null ? column.getAttributeAsString("totalFormat") : column.getFormat();
                        getHtmlBuilder().append(ExtremeUtils.formatNumber(format, value, getTableModel().getLocale()));
                    }
                } else {
                    getHtmlBuilder().td(2).close();
                    getHtmlBuilder().nbsp();
                }

                getHtmlBuilder().tdEnd();
            }

            getHtmlBuilder().trEnd(1);
        }
	}

}
