package it.dpe.igeriv.web.extremecomponents;

import it.dpe.igeriv.dto.ClienteEdicolaDto;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DPEWebContants;

import java.util.List;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.extremecomponents.util.HtmlBuilder;

public class DpeECScontoComboboxCellPne implements Cell {

	public String getExportDisplay(TableModel model, Column column) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public String getHtmlDisplay(TableModel model, Column column) {
		ColumnBuilder columnBuilder = new ColumnBuilder(column);
		columnBuilder.tdStart();
		try {
			ClienteEdicolaDto bean = (ClienteEdicolaDto) model.getCurrentRowBean();
			String styleClass = (column.getAttribute("styleClass") != null) ? (String)column.getAttribute("styleClass") : DPEWebContants.BLANK;
			String style = (column.getAttribute("style") != null) ? (String)column.getAttribute("style") : DPEWebContants.BLANK;
			Long codCliente = bean.getCodCliente();
			HtmlBuilder obj = columnBuilder.getHtmlBuilder().select()
					.name("scontoPne[" + codCliente + "]")
					.id("scontoPne_" + codCliente)
					.styleClass(styleClass)
					.style(style);
			if (!bean.hasProdottiVariInEstratto()) {
				columnBuilder.getHtmlBuilder().disabled();
			}
			columnBuilder.getHtmlBuilder().close();
			obj.option().value("-1");
			obj.close();
			obj.append(IGerivMessageBundle.get("igeriv.default"));
			obj.optionEnd();
			List<KeyValueDto> list = (List<KeyValueDto>) model.getContext().getSessionAttribute("listNumberVo0to100");
			for (KeyValueDto dto : list) {
				obj.option().value(dto.getValue());
				obj.close();
				obj.append(dto.getKeyInt().toString());
				obj.optionEnd();
			}
			obj.selectEnd();
		} catch (Exception e) {
			e.printStackTrace();
		}
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}

}
