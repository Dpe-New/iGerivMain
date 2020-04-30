package it.dpe.inforiv.exporter.impl;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.bo.inforiv.InforivExportService;
import it.dpe.inforiv.dto.output.InforivMancanzeEccedenzeDto;
import it.dpe.inforiv.dto.output.InforivTotaleBollaConsegnaAccertataDto;
import it.dpe.inforiv.exporter.InforivExporter;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.lambdaj.group.Group;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

/**
 * Crea le righe da esportare per le mancanze/eccedenze e aggiunge le righe
 * al file corrispondente a ciascuna edicola.
 * 
 * @author romanom
 *
 */
@Component("InforivMancanzeEccedenzeExporter")
public class InforivMancanzeEccedenzeExporter extends InforivBaseExporter implements InforivExporter {
	@Autowired
	private InforivExportService exportBo;
	
	@Override
	public void exportData(String tipoRecord, Timestamp now, Integer codFiegDl, Map<Integer, File> mapExportFiles, Map<Integer, Map<String, String>> mapFtpParams, FixedFormatManager manager,File dirOutputLocalDL) {
		List<InforivTotaleBollaConsegnaAccertataDto> bolleTrasmesseDl = exportBo.getBolleRiassuntoDaTrasmettereDl(codFiegDl);
		//Group<InforivTotaleBollaConsegnaAccertataDto> groupBolleTrasmesse = group(bolleTrasmesseDl, by(on(InforivTotaleBollaConsegnaAccertataDto.class).getCodEdicola()));
		//for (Group<InforivTotaleBollaConsegnaAccertataDto> subgroup : groupBolleTrasmesse.subgroups()) {
		for (InforivTotaleBollaConsegnaAccertataDto dto : bolleTrasmesseDl) {
			//List<InforivTotaleBollaConsegnaAccertataDto> findAll = subgroup.findAll();
			//InforivTotaleBollaConsegnaAccertataDto dto = findAll.get(0);
			Integer codEdicola = dto.getCodEdicola();
			Date dataBolla = dto.getDataBolla();
			String tipoBolla = dto.getTipoBolla();
			List<InforivMancanzeEccedenzeDto> mancanzeEccedenza = exportBo.getBollaDettaglioConDifferenze(codFiegDl, codEdicola, dataBolla, tipoBolla);
			exportLines(tipoRecord, now, codFiegDl, mapExportFiles, mapFtpParams, manager, mancanzeEccedenza,dirOutputLocalDL);
		}
	}

	public InforivExportService getExportBo() {
		return exportBo;
	}

	public void setExportBo(InforivExportService exportBo) {
		this.exportBo = exportBo;
	}
	
	

}
