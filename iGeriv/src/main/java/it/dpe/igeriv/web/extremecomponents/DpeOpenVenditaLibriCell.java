package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;

import it.dpe.igeriv.dto.OrdineLibriDto;
import it.dpe.igeriv.util.DateUtilities;

public class DpeOpenVenditaLibriCell extends DpeTextItemBollaCell {

	@Override
	public String getHtmlDisplay(TableModel model, Column column) {
		DpeColumnBuilder columnBuilder = new DpeColumnBuilder(column);
		OrdineLibriDto bean = (OrdineLibriDto) model.getCurrentRowBean();
		columnBuilder.tdStart();
		
		if(bean.getDataChiusuraOrdine()!=null && bean.getNumeroOrdineTxt()!=null && !bean.getNumeroOrdineTxt().equals("") && bean.dettaglioConsegnaLibri){
			String numOrdineTXT = bean.getNumeroOrdineTxt();
			
			columnBuilder.getHtmlBuilder().input("button").id("viewPageConsegnaAlCliente"+numOrdineTXT).align("middle").value("CONSEGNA LIBRI").onclick("javascript:viewPageConsegnaAlCliente('"+numOrdineTXT+"')").close();
		}else{
			if(bean.getDataConsegnaAlCliente()!=null){
				String strDtConsegnaCliente = DateUtilities.getTimestampAsString(bean.getDataConsegnaAlCliente(), DateUtilities.FORMATO_DATA_SLASH);
				columnBuilder.getHtmlBuilder().append("&nbsp;"+strDtConsegnaCliente);
			}else{
				columnBuilder.getHtmlBuilder().append("&nbsp;");
			}
		}
		
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}

}
