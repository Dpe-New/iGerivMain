package it.dpe.igeriv.web.extremecomponents;

import java.sql.Timestamp;
import java.util.Date;

import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.util.DPEWebContants;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;

import org.apache.commons.beanutils.BeanUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.extremecomponents.util.HtmlBuilder;

import com.google.common.base.Strings;

/**
 * @author romanom
 *
 */
public class DpeEdicolaPromoCheckboxItemBollaCell implements Cell {

	public String getExportDisplay(TableModel arg0, Column arg1) {
		Object bean = arg0.getCurrentRowBean();
		try {
			if (arg1.getAlias().equals("edicolaPromo")) {
				String property = null; 
				try {
					property = BeanUtils.getProperty(bean, arg1.getProperty());
				} catch (NoSuchMethodException e) {}
				if (!Strings.isNullOrEmpty(property) && property.contains("true")) {
					return arg0.getMessages().getMessage(IGerivConstants.SI);
				} else {
					return arg0.getMessages().getMessage(IGerivConstants.NO);
				} 
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		return null;
	}

	public String getHtmlDisplay(TableModel model, Column column) {
		String styleClass = (column.getAttribute("styleClass") != null) ? (String)column.getAttribute("styleClass") : DPEWebContants.BLANK;
 		ColumnBuilder columnBuilder = new ColumnBuilder(column);
		columnBuilder.tdStart();
		try {
			EdicolaDto bean = (EdicolaDto) model.getCurrentRowBean();
			String name = "edicolaPromo";
			String pk = bean.getCodEdicolaWeb().toString();
			HtmlBuilder check = columnBuilder.getHtmlBuilder().input("checkbox").name(name).id(name + pk);
			Timestamp dataInizioiGerivPromo = bean.getDataInizioiGerivPromo();
			Timestamp dataFineiGerivPromo = bean.getDataFineiGerivPromo() == null ? new Timestamp(DateUtilities.END_OF_TIME.getTime()) : bean.getDataFineiGerivPromo();
			Date now = new Date();
			if (dataInizioiGerivPromo != null && dataFineiGerivPromo != null 
					&& (dataInizioiGerivPromo.before(now) && dataFineiGerivPromo.after(now))) {
				check.checked();
			}
			check.styleClass(DPEWebContants.STYLE_CLASS_TEXT);
			
			//add disabled
			//02.08.2017 commento il check.disabled perchè igeriv-adm il processo di Visualizza Edicole
			//risulta essere in readonly
			//check.disabled();
			check.xclose();
			
			columnBuilder.getHtmlBuilder().nbsp();
			columnBuilder.getHtmlBuilder().nbsp();
			String textNameIni = name + "dtIni";
			String dataIniStr = dataInizioiGerivPromo != null ? DateUtilities.getTimestampAsString(dataInizioiGerivPromo, DateUtilities.FORMATO_DATA_SLASH) : "";
			//add disabled
			//02.08.2017 commento il check.disabled perchè igeriv-adm il processo di Visualizza Edicole
			//risulta essere in readonly
			//columnBuilder.getHtmlBuilder().input("text").name(textNameIni).id(textNameIni + "|" + pk).value(dataIniStr).styleClass(styleClass).style("width:70px").maxlength("10").disabled().xclose();
			columnBuilder.getHtmlBuilder().input("text").name(textNameIni).id(textNameIni + "|" + pk).value(dataIniStr).styleClass(styleClass).style("width:70px").maxlength("10").xclose();
			columnBuilder.getHtmlBuilder().nbsp();
			String textNameFin = name + "dtFin";
			String dataFinStr = (dataFineiGerivPromo.getTime() != DateUtilities.END_OF_TIME.getTime()) ? DateUtilities.getTimestampAsString(dataFineiGerivPromo, DateUtilities.FORMATO_DATA_SLASH) : "";
			//add disabled
			//02.08.2017 commento il check.disabled perchè igeriv-adm il processo di Visualizza Edicole
			//risulta essere in readonly
			//columnBuilder.getHtmlBuilder().input("text").name(textNameFin).id(textNameFin + "|" + pk).value(dataFinStr).styleClass(styleClass).style("width:70px").maxlength("10").disabled().xclose();
			columnBuilder.getHtmlBuilder().input("text").name(textNameFin).id(textNameFin + "|" + pk).value(dataFinStr).styleClass(styleClass).style("width:70px").maxlength("10").xclose();

		} catch (Exception e) {
		}
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}
	
}
