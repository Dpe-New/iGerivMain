package it.dpe.igeriv.web.actions;

import java.awt.Color;
import java.awt.Font;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.NumberUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per la generazione di grafici.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("viewChartAction")
public class ViewChartAction extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;
	private JFreeChart chart;
	private String date;
	private String vendutoQuotidiani;
	private String vendutoPeriodici;
	private String venduto;
	private Boolean hasSuddivisioneQuotidianiPeriodiciReportVenduto = true;
	
	public String getVendutoEstrattoContoChart() throws ParseException {
		Map<Date, List<Double>> mapValues = buildDataMap();
		TimeSeries dsQuotidiani = vendutoQuotidiani != null ? new TimeSeries(getText("igeriv.venduto.quotidiani"), Day.class) : null;
		TimeSeries dsPeriodici = new TimeSeries(getText("igeriv.venduto.periodici"), Day.class);
		TimeSeries dsVenduto = new TimeSeries(getText("igeriv.venduto"), Day.class);
		Calendar cal = Calendar.getInstance();
		for (Map.Entry<Date, List<Double>> entry : mapValues.entrySet()) {
			Date date = entry.getKey();
			List<Double> value = entry.getValue();
			cal.setTime(date);
			Day day = new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
			if (dsQuotidiani != null) {
				dsQuotidiani.add(day, value.get(0));
			}
			dsPeriodici.add(day, value.get(1));
			dsVenduto.add(day, value.get(2));
		}
		
		XYDataset xyDatasetQuotidiani = null;
		if (dsQuotidiani != null) {
			xyDatasetQuotidiani = new TimeSeriesCollection(dsQuotidiani);
		}
		XYDataset xyDatasetPeriodici = new TimeSeriesCollection(dsPeriodici);
		XYDataset xyDatasetVenduto = new TimeSeriesCollection(dsVenduto);
		
		Object[] keySet = mapValues.keySet().toArray();
		chart = ChartFactory.createTimeSeriesChart(
				MessageFormat.format(getText("igeriv.andamento.vendite.dal.al"), DateUtilities.getTimestampAsString((Date)keySet[0], DateUtilities.FORMATO_DATA_SLASH), DateUtilities.getTimestampAsString((Date)keySet[keySet.length - 1], DateUtilities.FORMATO_DATA_SLASH)), 
				getText("igeriv.periodo"), 
				getText("igeriv.importo") + "(\u20AC)", 
				null, 
				hasSuddivisioneQuotidianiPeriodiciReportVenduto.booleanValue(), 
				true, 
				false);

		chart.setBackgroundPaint(Color.white);

		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
		
		if (xyDatasetQuotidiani != null) {
			plot.setDataset(0, xyDatasetQuotidiani);
			plot.setDataset(1, xyDatasetPeriodici);
			plot.setDataset(2, xyDatasetVenduto);
		} else {
			plot.setDataset(0, xyDatasetPeriodici);
			plot.setDataset(1, xyDatasetVenduto);
		}
		
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat(DateUtilities.FORMATO_DATA_SLASH));
		axis.setTickLabelFont(new Font("Arial",Font.BOLD,12));
		
		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setTickLabelFont(new Font("Arial",Font.BOLD,12));
		
		java.awt.geom.Ellipse2D.Double double1 = new java.awt.geom.Ellipse2D.Double(-4D, -4D, 8D, 8D);
		if (xyDatasetQuotidiani != null) {
			XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
			renderer.setBaseShapesVisible(true);
			renderer.setSeriesShape(1, double1);
			renderer.setSeriesPaint(1, Color.blue);
			renderer.setSeriesFillPaint(1, Color.blue);
			renderer.setSeriesOutlinePaint(1, Color.blue);
			plot.setRenderer(0, renderer);
			
			XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();
			renderer1.setBaseShapesVisible(true);
			renderer1.setSeriesShape(1, double1);
			renderer1.setSeriesPaint(1, Color.magenta);
			renderer1.setSeriesFillPaint(1, Color.magenta);
			renderer1.setSeriesOutlinePaint(1, Color.magenta);
			plot.setRenderer(1, renderer1);
			
			XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
			renderer2.setBaseShapesVisible(true);
			renderer2.setSeriesShape(1, double1);
			renderer2.setSeriesPaint(1, Color.cyan);
			renderer2.setSeriesFillPaint(1, Color.cyan);
			renderer2.setSeriesOutlinePaint(1, Color.cyan);
			plot.setRenderer(2, renderer2);
		} else {
			XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();
			renderer1.setBaseShapesVisible(true);
			renderer1.setSeriesShape(1, double1);
			renderer1.setSeriesPaint(1, Color.magenta);
			renderer1.setSeriesFillPaint(1, Color.magenta);
			renderer1.setSeriesOutlinePaint(1, Color.magenta);
			plot.setRenderer(0, renderer1);
			
			XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
			renderer2.setBaseShapesVisible(true);
			renderer2.setSeriesShape(1, double1);
			renderer2.setSeriesPaint(1, Color.cyan);
			renderer2.setSeriesFillPaint(1, Color.cyan);
			renderer2.setSeriesOutlinePaint(1, Color.cyan);
			plot.setRenderer(1, renderer2);
		}
		
		return SUCCESS;
	}

	/**
	 * @return
	 * @throws ParseException
	 */
	private Map<Date, List<Double>> buildDataMap() throws ParseException {
		Map<Date, List<Double>> mapValues = new TreeMap<Date, List<Double>>();
		String[] listStrDates = date.split("\\|");
		String[] listVendutoQuot = vendutoQuotidiani != null ? vendutoQuotidiani.split("\\|") : null;
		String[] listVendutoPer = vendutoPeriodici.split("\\|");
		String[] listVenduto = venduto.split("\\|");
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtilities.FORMATO_DATA_SLASH);
		int count = 0;
		for (String strDate : listStrDates) {
			List<Double> listValues = new ArrayList<Double>();
			listValues.add(listVendutoQuot != null ? Double.parseDouble(NumberUtils.unformatNumber(listVendutoQuot[count]).toString()) : null);
			listValues.add(Double.parseDouble(NumberUtils.unformatNumber(listVendutoPer[count]).toString()));
			listValues.add(Double.parseDouble(NumberUtils.unformatNumber(listVenduto[count]).toString()));
			mapValues.put(sdf.parse(strDate), listValues);
			count++;
		}
		return mapValues;
	}
	
}
