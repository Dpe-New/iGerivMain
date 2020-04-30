package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;

import it.dpe.igeriv.dto.OrdineLibriDto;

public class DpeOpenTrackingOrdineCell extends DpeTextItemBollaCell {

	@Override
	public String getHtmlDisplay(TableModel model, Column column) {
		DpeColumnBuilder columnBuilder = new DpeColumnBuilder(column);
		OrdineLibriDto bean = (OrdineLibriDto) model.getCurrentRowBean();
		columnBuilder.tdStart();
		
		if(bean.getDataChiusuraOrdine()!=null && bean.getNumeroOrdineTxt()!=null && !bean.getNumeroOrdineTxt().equals("")){
			String numOrdineTXT = bean.getNumeroOrdineTxt();
			if(bean.getTracking_attivo() && bean.getTracking_attivo_parziale()){
				columnBuilder.getHtmlBuilder().input("button").id("viewTracking"+numOrdineTXT).align("middle").value("TRACKING ORDINE").onclick("javascript:viewTracking('"+numOrdineTXT+"')").style("border: 2px solid red;").close();
			}else if(bean.getTracking_attivo() && !bean.getTracking_attivo_parziale()){
				columnBuilder.getHtmlBuilder().input("button").id("viewTracking"+numOrdineTXT).align("middle").value("TRACKING ORDINE").onclick("javascript:viewTracking('"+numOrdineTXT+"')").close();
			}else if(!bean.getTracking_attivo() && !bean.getTracking_attivo_parziale()){
				columnBuilder.getHtmlBuilder().append("&nbsp;");
			}
		}else{
			columnBuilder.getHtmlBuilder().append("&nbsp;");
		}
		
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}

}

