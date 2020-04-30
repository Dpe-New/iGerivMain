package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;

import it.dpe.igeriv.dto.RilevamentiDto;

public class DpeRilevamentoCell extends DpeTextItemBollaCell {

	@Override
	public String getHtmlDisplay(TableModel model, Column column) {
		DpeColumnBuilder columnBuilder = new DpeColumnBuilder(column);
		RilevamentiDto bean = (RilevamentiDto) model.getCurrentRowBean();
		columnBuilder.tdStart();
		String id = bean.getCodiceRilevamento().toString();
		columnBuilder.getHtmlBuilder().input("text").name("rilevamentoMap[" + id + "]").id(id).styleClass("extremeTableFieldsSmaller").style("font-size:120%; font-weight:bold; text-align:left").size("2").maxlength("3").value(bean.getRilevamento() != null ? bean.getRilevamento().toString() : "");
		columnBuilder.getHtmlBuilder().xclose();
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}

}
