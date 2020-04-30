package it.dpe.igeriv.web.extremecomponents;

import it.dpe.igeriv.dto.FileFatturaDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivUtils;
import it.dpe.igeriv.util.SpringContextManager;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;

public class DpeImgStornoFatturaCell implements Cell {
	private static Log logger = LogFactory.getLog(DpeImgStornoFatturaCell.class);
	private Calendar cal = Calendar.getInstance();
	
	public DpeImgStornoFatturaCell() {
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, IGerivConstants.MAX_NUM_DAYS_TO_DELETE_CONTO_VENDITE);
	}
	
	public String getExportDisplay(TableModel arg0, Column arg1) {
		return null;
	}

	public String getHtmlDisplay(TableModel model, Column column) {
 		ColumnBuilder columnBuilder = new ColumnBuilder(column);
		columnBuilder.tdStart();
		try {
			FileFatturaDto bean = (FileFatturaDto) model.getCurrentRowBean();
			if (bean.getTipoDocumento() != null) {
				if (bean.getTipoDocumento().equals(IGerivConstants.FATTURA) 
						&& DateUtilities.ceilDay(bean.getData()).after(((IGerivUtils)SpringContextManager.getService("iGerivUtils")).getVenditeDeleteModuleDeployDate())
						&& bean.getData().after(cal.getTime())) {
					columnBuilder.getHtmlBuilder().img()
						.src("/app_img/storno.png")
						.name("imgStorno")
						.id("imgStorno" + bean.getNumero())
						.alt(model.getMessages().getMessage("igeriv.storno.fattura"))
						.title(model.getMessages().getMessage("igeriv.storno.fattura"))
						.style("border-style: none; cursor:pointer")
						.border("0")
						.onclick("javascript:stornaFattura('" + bean.getCodCliente() + "','" + bean.getNomeCognome() + "','" + bean.getNumero() + "')")
						.xclose();
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}
}
