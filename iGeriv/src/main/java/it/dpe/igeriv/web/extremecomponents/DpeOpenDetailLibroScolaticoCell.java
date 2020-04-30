package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;

import it.dpe.igeriv.dto.RisultatiRicercaLibriDto;

public class DpeOpenDetailLibroScolaticoCell extends DpeTextItemBollaCell {

	
	@Override
	public String getHtmlDisplay(TableModel model, Column column) {
		DpeColumnBuilder columnBuilder = new DpeColumnBuilder(column);
		RisultatiRicercaLibriDto bean = (RisultatiRicercaLibriDto) model.getCurrentRowBean();
		columnBuilder.tdStart();
		String sku = bean.getSKU();
		
		columnBuilder.getHtmlBuilder().input("button").id("openDetail_LibroScolastico"+sku).align("middle").value("DETTAGLIO TESTO").onclick("javascript:openDetailLibroScolastico("+sku+")").close();
		
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}
}
