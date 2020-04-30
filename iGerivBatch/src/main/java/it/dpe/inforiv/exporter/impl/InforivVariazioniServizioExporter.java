package it.dpe.inforiv.exporter.impl;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.bo.inforiv.InforivExportService;
import it.dpe.inforiv.dto.output.InforivVariazioniServizioDto;
import it.dpe.inforiv.exporter.InforivExporter;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.lambdaj.group.Group;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

/**
 * Crea le righe da esportare per le variazioni e aggiunge le righe
 * al file corrispondente a ciascuna edicola.
 * 
 * @author romanom
 *
 */
@Component("InforivVariazioniServizioExporter")
public class InforivVariazioniServizioExporter extends InforivBaseExporter implements InforivExporter {
	@Autowired
	private InforivExportService exportBo;
	
	@Override
	public void exportData(String tipoRecord, Timestamp now, Integer codFiegDl, Map<Integer, File> mapExportFiles, Map<Integer, Map<String, String>> mapFtpParams, FixedFormatManager manager,File dirOutputLocalDL) {
		List<InforivVariazioniServizioDto> bolleTrasmesseDl = exportBo.getVariazioni(codFiegDl);
		Group<InforivVariazioniServizioDto> groupBolleTrasmesse = group(bolleTrasmesseDl, by(on(InforivVariazioniServizioDto.class).getCodEdicola()));
		for (Group<InforivVariazioniServizioDto> subgroup : groupBolleTrasmesse.subgroups()) {
			List<InforivVariazioniServizioDto> list = subgroup.findAll();
			exportLines(tipoRecord, now, codFiegDl, mapExportFiles, mapFtpParams, manager, list,dirOutputLocalDL);
		}
		exportBo.updateVariazioni(codFiegDl);
	}

	public InforivExportService getExportBo() {
		return exportBo;
	}

	public void setExportBo(InforivExportService exportBo) {
		this.exportBo = exportBo;
	}
	
}
