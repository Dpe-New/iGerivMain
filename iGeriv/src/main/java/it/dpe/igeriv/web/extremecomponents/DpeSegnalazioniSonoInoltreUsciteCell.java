package it.dpe.igeriv.web.extremecomponents;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;

import it.dpe.igeriv.dto.BollaVoDto;

public class DpeSegnalazioniSonoInoltreUsciteCell implements Cell {

	@Override
	public String getExportDisplay(TableModel model, Column column) {
		return null;
	}

	@Override
	public String getHtmlDisplay(TableModel model, Column column) {
		DpeColumnBuilder columnBuilder = new DpeColumnBuilder(column);
		columnBuilder.tdStart();
		try {
			BollaVoDto dto = (BollaVoDto) model.getCurrentRowBean();
			
			//09-06-2014 Errore nell'apertura del popup SaveCopieFuoriCompetenza
			String titolo = dto.getTitolo().replace("\'","\\'");
			
			columnBuilder.getHtmlBuilder()
			.img()
			.src("/app_img/fuori_competenza.png")
			.name("fuori_competenza")
			.title(model.getMessages().getMessage("msg.copie.fuori.competenza.title.desc"))
			.style("cursor:pointer")
			.onclick("promptSaveCopieFuoriCompetenza('" + dto.getPk() + "','" + dto.getIdtn() + "','" + titolo + "','" + dto.getNumeroCopertina().trim() + "')")
			.xclose();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		columnBuilder.tdEnd();
		return columnBuilder.toString();
	}

}
