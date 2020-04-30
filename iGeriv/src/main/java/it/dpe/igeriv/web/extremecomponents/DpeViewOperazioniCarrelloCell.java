package it.dpe.igeriv.web.extremecomponents;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.DisplayCell;
import org.extremecomponents.table.core.TableModel;

public class DpeViewOperazioniCarrelloCell extends DisplayCell {
	private final Logger log = Logger.getLogger(getClass());
	
	
	@Override
	protected String getCellValue(TableModel model, Column column) {
		StringBuilder val = new StringBuilder("");
		try {
			
			String fgCopertina 	= BeanUtils.getProperty(model.getCurrentRowBean(), "flagCopertina");
			String numOrdine 	= BeanUtils.getProperty(model.getCurrentRowBean(), "numeroOrdine");
			String seqOrdine 	= BeanUtils.getProperty(model.getCurrentRowBean(), "seqordine");
			String skuLibro 	= BeanUtils.getProperty(model.getCurrentRowBean(), "sku");
			
			if(fgCopertina !=null && fgCopertina.equals("1")){
				val.append("<img src=\"/app_img/infoCopertina.png\" width=\"30px\" height=\"30px\" border=\"0\" style=\"border-style: none\" "
							+ " onclick=\"infoCopertinaLibro("+seqOrdine+");\" />");
				val.append("<img src=\"/app_img/modificaCopertina.png\" width=\"30px\" height=\"30px\" border=\"0\" style=\"border-style: none\" "
						+ " onclick=\"modificaCopertinaLibro("+seqOrdine+");\" />");
			}
			
			val.append("<img src=\"/app_img/del_book2.png\" width=\"30px\" height=\"30px\" border=\"0\" style=\"border-style: none\" "
					+ " onclick=\"deleteLibroDalCarrello("+seqOrdine+","+skuLibro+");\" />");
			
			
		} catch (Exception e) {
			log.error("Errore in DpeViewOperazioniCarrelloCell", e);
		}
		return val.toString();
	}
}
