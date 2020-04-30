package it.dpe.igeriv.web.extremecomponents;

import it.dpe.igeriv.dto.ClienteEdicolaDto;
import it.dpe.igeriv.util.DPEWebContants;
import it.dpe.igeriv.util.IGerivConstants;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;

/**
 * @author romanom
 *
 */
public class DpePagamentoClientiCheckboxCell implements Cell {

	public String getExportDisplay(TableModel arg0, Column arg1) {
		try {
			if (arg1.getAlias().equals("spunta")) {
				return arg0.getMessages().getMessage(IGerivConstants.NO);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		return null;
	}

	public String getHtmlDisplay(TableModel model, Column column) {
 		ColumnBuilder columnBuilder = new ColumnBuilder(column);
		columnBuilder.tdStart();
		try {
			ClienteEdicolaDto bean = (ClienteEdicolaDto) model.getCurrentRowBean();
			String chkName = DPEWebContants.SESSION_VAR_SPUNTE;
			String spuntaId = bean.getCodCliente() + "|" + bean.getDataCompetenzaEstrattoConto();
			columnBuilder.getHtmlBuilder().input("checkbox").name(chkName).id(DPEWebContants.DPE_CHECKBOX_NAME + spuntaId).value(spuntaId).styleClass(DPEWebContants.STYLE_CLASS_TEXT);
			columnBuilder.getHtmlBuilder().xclose();	
		} catch (Exception e) {
		}
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}
	
}
