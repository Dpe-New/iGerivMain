package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;

import it.dpe.igeriv.dto.ArretratiDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;

public class DpeConfermaArretratoCell implements Cell {
	
	@Override
	public String getExportDisplay(TableModel model, Column column) {
		ArretratiDto bean = (ArretratiDto) model.getCurrentRowBean();
		if (bean.getConfermaSi() != null && bean.getConfermaSi()) {
			return model.getMessages().getMessage("igeriv.si");
		} else if (bean.getConfermaNo() != null && bean.getConfermaNo()) {
			return model.getMessages().getMessage("igeriv.no");
		} else if (bean.getConfermaSi() == null && bean.getConfermaNo() == null) {
			return model.getMessages().getMessage("plg.reimposta");
		}
		return null;
	}
	
	@Override
	public String getHtmlDisplay(TableModel model, Column column) {
		DpeColumnBuilder columnBuilder = new DpeColumnBuilder(column);
		ArretratiDto bean = (ArretratiDto) model.getCurrentRowBean();
		columnBuilder.tdStart();
		String id = bean.getCodiceArretrato().toString();
		columnBuilder.getHtmlBuilder().span().style("width:200px; margin-left:auto; margin-right:auto; text-align:center; white-space:nowrap;").close();
		columnBuilder.getHtmlBuilder().input("radio").name("arretratiMap[" + id + "]").id(id).styleClass("extremeTableFieldsSmaller").value("" + IGerivConstants.COD_SI);
		if (bean.getConfermaSi() != null && bean.getConfermaSi()) {
			columnBuilder.getHtmlBuilder().checked();
		}
		columnBuilder.getHtmlBuilder().close();
		columnBuilder.getHtmlBuilder().nbsp();
		columnBuilder.getHtmlBuilder().append(IGerivMessageBundle.get("igeriv.si"));
		columnBuilder.getHtmlBuilder().nbsp().nbsp();
		columnBuilder.getHtmlBuilder().input("radio").name("arretratiMap[" + id + "]").id(id).styleClass("extremeTableFieldsSmaller").value("" + IGerivConstants.COD_NO);;
		if (bean.getConfermaNo() != null && bean.getConfermaNo()) {
			columnBuilder.getHtmlBuilder().checked();
		}
		columnBuilder.getHtmlBuilder().close();
		columnBuilder.getHtmlBuilder().nbsp();
		columnBuilder.getHtmlBuilder().append(IGerivMessageBundle.get("igeriv.no"));
		columnBuilder.getHtmlBuilder().nbsp().nbsp();
		columnBuilder.getHtmlBuilder().input("radio").name("arretratiMap[" + id + "]").id(id).styleClass("extremeTableFieldsSmaller").value(null);;
		if (bean.getConfermaSi() == null && bean.getConfermaNo() == null) {
			columnBuilder.getHtmlBuilder().checked();
		}
		columnBuilder.getHtmlBuilder().close();
		columnBuilder.getHtmlBuilder().nbsp();
		columnBuilder.getHtmlBuilder().append(IGerivMessageBundle.get("plg.sospendi"));
		columnBuilder.getHtmlBuilder().nbsp();
		columnBuilder.getHtmlBuilder().spanEnd();
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}

}
