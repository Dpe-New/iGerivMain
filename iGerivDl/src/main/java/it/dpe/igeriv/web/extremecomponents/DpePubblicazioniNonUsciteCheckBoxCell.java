package it.dpe.igeriv.web.extremecomponents;

import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.DPEWebContants;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.BollaVo;

import org.apache.commons.beanutils.BeanUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.springframework.security.core.context.SecurityContextHolder;

public class DpePubblicazioniNonUsciteCheckBoxCell implements Cell {

	public String getExportDisplay(TableModel arg0, Column arg1) {
		return null;
	}

	public String getHtmlDisplay(TableModel model, Column column) {
		ColumnBuilder columnBuilder = new ColumnBuilder(column);
		columnBuilder.tdStart();
		Object object = SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (object != null) {
			UserAbbonato user = (UserAbbonato) object;
			if (user.getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_DL)) {
				try {
					BollaVo bean = (BollaVo) model.getCurrentRowBean();
					String pkName = (column.getAttribute("pkName") != null) ? (String) column
							.getAttribute("pkName") : DPEWebContants.BLANK;
					if (!pkName.isEmpty()) {
						String value = bean.getPk().toString();
						columnBuilder
								.getHtmlBuilder()
								.input("checkbox")
								.name("pk")
								.value(value)
								.styleClass(DPEWebContants.STYLE_CLASS_TEXT);
						
						if (bean.getPubblicazioneNonUscita()) {
							columnBuilder.getHtmlBuilder().checked();
						}
						
						columnBuilder.getHtmlBuilder().xclose();
					}
				} catch (Exception e) {
				}
			}
		}
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}

}
