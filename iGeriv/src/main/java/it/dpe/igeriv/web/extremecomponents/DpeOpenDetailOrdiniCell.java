package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;

import it.dpe.igeriv.dto.ClienteEdicolaDto;

public class DpeOpenDetailOrdiniCell extends DpeTextItemBollaCell {

	@Override
	public String getHtmlDisplay(TableModel model, Column column) {
		DpeColumnBuilder columnBuilder = new DpeColumnBuilder(column);
		ClienteEdicolaDto bean = (ClienteEdicolaDto) model.getCurrentRowBean();
		columnBuilder.tdStart();
		String id = bean.getCodCliente().toString();
		if(bean.getOrdiniPresenti()){
			if(!bean.getOrdIncompleti()){
				if(bean.getOrdDaConsegnare()){
					columnBuilder.getHtmlBuilder().input("button").id("openDetailOrdini"+id).align("middle").value("DETTAGLIO ORDINI").onclick("javascript:openDetailOrdini("+id+")").style("border: 2px solid green;").close();
				}else{
					columnBuilder.getHtmlBuilder().input("button").id("openDetailOrdini"+id).align("middle").value("DETTAGLIO ORDINI").onclick("javascript:openDetailOrdini("+id+")").close();
				}
			}else{
				if(bean.getOrdDaConsegnare()){
					columnBuilder.getHtmlBuilder().input("button").id("openDetailOrdini"+id).align("middle").value("DETTAGLIO ORDINI").onclick("javascript:openDetailOrdini("+id+")").style("border: 2px solid green;").close();
				}else{
					columnBuilder.getHtmlBuilder().input("button").id("openDetailOrdini"+id).align("middle").value("DETTAGLIO ORDINI").onclick("javascript:openDetailOrdini("+id+")").style("border: 2px solid red;").close();
				}
			}
		}else{
			columnBuilder.getHtmlBuilder().append("&nbsp;");
		}
		//$('#openDetailOrdini27105').attr('style', ' border: 2px solid red;');
		
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}

}
