package it.dpe.igeriv.web.extremecomponents;

import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.DPEWebContants;
import it.dpe.igeriv.util.IGerivConstants;

import org.apache.commons.beanutils.BeanUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author romanom
 *
 */
public class DpeRecuperaRitiriClienteCheckboxCell implements Cell {

	public String getExportDisplay(TableModel arg0, Column arg1) {
		return null;
	}

	public String getHtmlDisplay(TableModel model, Column column) {
 		ColumnBuilder columnBuilder = new ColumnBuilder(column);
		columnBuilder.tdStart();
		Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (object != null) {
			UserAbbonato user = (UserAbbonato) object;
			if (user.getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_EDICOLA)) {
				try {
					PubblicazioneDto bean = (PubblicazioneDto) model.getCurrentRowBean();
					String pkName = (column.getAttribute("pkName") != null) ? (String)column.getAttribute("pkName") : DPEWebContants.BLANK;
					String spuntaId = BeanUtils.getProperty(bean, pkName) + "|" + bean.getProdottoNonEditoriale();
					columnBuilder.getHtmlBuilder().input("checkbox").name("pk").id(DPEWebContants.DPE_CHECKBOX_NAME + spuntaId).value(spuntaId).styleClass(DPEWebContants.STYLE_CLASS_TEXT);
					columnBuilder.getHtmlBuilder().xclose();				
				} catch (Exception e) {
				}
			}
		}
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}
	
}
