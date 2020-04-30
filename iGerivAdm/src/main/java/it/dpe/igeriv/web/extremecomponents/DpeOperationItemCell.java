package it.dpe.igeriv.web.extremecomponents;

import org.apache.commons.beanutils.BeanUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;

import com.google.common.base.Strings;

import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.util.DPEWebContants;
import it.dpe.igeriv.util.IGerivConstants;

/**
 * @author romanom
 *
 */
public class DpeOperationItemCell implements Cell {

	public String getExportDisplay(TableModel arg0, Column arg1) {
		Object bean = arg0.getCurrentRowBean();
		try {
			if (arg1.getAlias().equals("edicolaTest")) {
				String property = null; 
				try {
					property = BeanUtils.getProperty(bean, arg1.getProperty());
				} catch (NoSuchMethodException e) {}
				if (!Strings.isNullOrEmpty(property) && property.contains("true")) {
					return arg0.getMessages().getMessage(IGerivConstants.SI);
				} else {
					return arg0.getMessages().getMessage(IGerivConstants.NO);
				} 
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
			EdicolaDto bean = (EdicolaDto) model.getCurrentRowBean();
			
			String codDl	= bean.getCodDl().toString();
			String crivw	= bean.getCodEdicolaWeb().toString();
			String crivdl	= bean.getCodEdicolaDl().toString();
			
			columnBuilder.getHtmlBuilder().input("button").name(crivw).id(crivw).value("DETTAGLI").styleClass("btn-xs btn-info").maxlength("12").onclick("openPopUpDettaglioProfilo("+codDl+","+crivw+")").xclose();
			columnBuilder.getHtmlBuilder().nbsp();
			
		} catch (Exception e) {
		}
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}
	
}
