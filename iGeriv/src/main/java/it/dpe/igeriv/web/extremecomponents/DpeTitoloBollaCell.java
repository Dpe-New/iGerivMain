package it.dpe.igeriv.web.extremecomponents;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.DisplayCell;
import org.extremecomponents.table.core.TableModel;

import com.google.common.base.Strings;

import it.dpe.igeriv.dto.FondoBollaDettaglioDto;
import it.dpe.igeriv.util.DateUtilities;

public class DpeTitoloBollaCell extends DisplayCell {
	private final Logger log = Logger.getLogger(getClass());
	
	@Override
	public String getExportDisplay(TableModel model, Column column) {
		StringBuilder val = new StringBuilder("");
		try {
			val.append(BeanUtils.getProperty(model.getCurrentRowBean(), "titolo"));
			String sottotitolo = BeanUtils.getProperty(model.getCurrentRowBean(), "sottoTitolo");
			sottotitolo = (sottotitolo != null) ? sottotitolo.trim() : sottotitolo;
			if (!Strings.isNullOrEmpty(sottotitolo)) {
				val.append(" (");
				val.append(sottotitolo);
				val.append(")");
			}
		} catch (Exception e) {
			log.error("Errore in DpeTitoloBollaCell", e);
		}
		return val.toString();
	}
	
	@Override
	protected String getCellValue(TableModel model, Column column) {
		StringBuilder val = new StringBuilder("");
		try {
			val.append(BeanUtils.getProperty(model.getCurrentRowBean(), "titolo"));
			String sottotitolo = BeanUtils.getProperty(model.getCurrentRowBean(), "sottoTitolo");
			sottotitolo = (sottotitolo != null) ? sottotitolo.trim() : sottotitolo;
			
			Object beanFB = model.getCurrentRowBean();
			Boolean isFondoBolla = (beanFB instanceof FondoBollaDettaglioDto);
			if(!isFondoBolla){
				if (!Strings.isNullOrEmpty(sottotitolo)) {
					val.append("<br/><span class='sottotitoloBolla'>");
					val.append(sottotitolo);
					val.append("</span>");
				}
				
			}else{
				String dataUscita = BeanUtils.getProperty(model.getCurrentRowBean(), "dataUscita");
				Timestamp time =  DateUtilities.parseDate(dataUscita,DateUtilities.FORMATO_DATA_YYYY_MM_DD_HHMMSS);
				
				dataUscita = (dataUscita != null) ? new SimpleDateFormat("dd/MM/yyyy").format(time) : dataUscita;
									
				if (!Strings.isNullOrEmpty(sottotitolo)) {
					val.append("<br/><span class='sottotitoloBolla'>");
					val.append(sottotitolo);
					if(!Strings.isNullOrEmpty(dataUscita))
						val.append(" - "+dataUscita+"");
					val.append("</span>");
				}else{
					val.append("<br/><span class='sottotitoloBolla'>");
					if(!Strings.isNullOrEmpty(dataUscita))
						val.append(""+dataUscita+"");
					val.append("</span>");
				}
			}
			
		} catch (Exception e) {
			log.error("Errore in DpeTitoloBollaCell", e);
		}
		return val.toString();
	}
}
