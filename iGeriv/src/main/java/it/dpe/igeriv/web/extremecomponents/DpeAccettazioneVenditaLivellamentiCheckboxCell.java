package it.dpe.igeriv.web.extremecomponents;

import java.text.MessageFormat;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;

import it.dpe.igeriv.util.DPEWebContants;
import it.dpe.igeriv.vo.LivellamentiVo;

/**
 * @author romanom
 *
 */
public class DpeAccettazioneVenditaLivellamentiCheckboxCell implements Cell {

	@Override
	public String getExportDisplay(TableModel arg0, Column arg1) {
		return null;
	}

	@Override
	public String getHtmlDisplay(TableModel model, Column column) {
 		ColumnBuilder columnBuilder = new ColumnBuilder(column);
		columnBuilder.tdStart();
		try {
			LivellamentiVo bean = (LivellamentiVo) model.getCurrentRowBean();
			String chkName = DPEWebContants.SESSION_VAR_SPUNTE;
			String spuntaId = bean.getIdLivellamento().toString();
			columnBuilder.getHtmlBuilder().input("checkbox").name(chkName)
				.id(DPEWebContants.DPE_CHECKBOX_NAME + spuntaId)
				.value(spuntaId)
				.styleClass(DPEWebContants.STYLE_CLASS_TEXT)
				.title(MessageFormat.format(model.getMessages().getMessage("igeriv.checkbox.livellamenti.accetta.proposta.vendita"), bean.getEdicola().getRagioneSocialeEdicolaPrimaRiga()));
			columnBuilder.getHtmlBuilder().xclose();	
		} catch (Exception e) {
		}
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}
	
}
