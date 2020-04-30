package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;

import com.google.common.base.Strings;

import it.dpe.igeriv.dto.ArretratiDto;

public class DpeNoteArretratoCell extends DpeTextItemBollaCell {

	@Override
	public String getHtmlDisplay(TableModel model, Column column) {
		DpeColumnBuilder columnBuilder = new DpeColumnBuilder(column);
		ArretratiDto bean = (ArretratiDto) model.getCurrentRowBean();
		columnBuilder.tdStart();
		String id = bean.getCodiceArretrato().toString();
		columnBuilder.getHtmlBuilder().input("text").name("noteArretratoMap[" + id + "]").id(id).size("35").maxlength("30").value(Strings.isNullOrEmpty(bean.getNote()) ? "" : bean.getNote());
		columnBuilder.getHtmlBuilder().xclose();
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}

}
