package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;

import it.dpe.igeriv.dto.ClienteEdicolaDto;

public class DpeOpenDetailAnagraficaCell extends DpeTextItemBollaCell {

	@Override
	public String getHtmlDisplay(TableModel model, Column column) {
		DpeColumnBuilder columnBuilder = new DpeColumnBuilder(column);
		ClienteEdicolaDto bean = (ClienteEdicolaDto) model.getCurrentRowBean();
		columnBuilder.tdStart();
		String id = bean.getCodCliente().toString();
		
		columnBuilder.getHtmlBuilder().input("button").id("openDetailAnagrafica"+id).align("middle").value("DETTAGLIO ANAGRAFICA").onclick("javascript:openDetailAnagrafica("+id+")").close();
		
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}

}
