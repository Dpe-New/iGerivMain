package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;

import it.dpe.igeriv.dto.ClienteEdicolaDto;

public class DpeOpenNewOrdineCell extends DpeTextItemBollaCell {

	@Override
	public String getHtmlDisplay(TableModel model, Column column) {
		DpeColumnBuilder columnBuilder = new DpeColumnBuilder(column);
		ClienteEdicolaDto bean = (ClienteEdicolaDto) model.getCurrentRowBean();
		columnBuilder.tdStart();
		String id = bean.getCodCliente().toString();
				
		columnBuilder.getHtmlBuilder().input("button").id("openDetailOrdini"+id).align("middle").value("NUOVO ORDINE").onclick("javascript:openNuovoOrdine("+id+")").close();
			
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}

}
