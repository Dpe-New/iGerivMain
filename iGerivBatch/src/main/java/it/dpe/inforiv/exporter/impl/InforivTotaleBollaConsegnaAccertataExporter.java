package it.dpe.inforiv.exporter.impl;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import it.dpe.igeriv.bo.inforiv.InforivExportService;
import it.dpe.inforiv.dto.output.InforivTotaleBollaConsegnaAccertataDto;
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
 * Crea le righe da esportare per la bolla di consegna accertata e aggiunge le righe
 * al file corrispondente a ciascuna edicola.
 * 
 * @author romanom
 *
 */
@SuppressWarnings({"rawtypes"})
@Component("InforivTotaleBollaConsegnaAccertataExporter")
public class InforivTotaleBollaConsegnaAccertataExporter extends InforivBaseExporter implements InforivExporter {
	@Autowired
	private InforivExportService exportBo;
	
	@Override
	public void exportData(String tipoRecord, Timestamp now, Integer codFiegDl, Map<Integer, File> mapExportFiles, Map<Integer, Map<String, String>> mapFtpParams, FixedFormatManager manager,File dirOutputLocalDL) {
		List<InforivTotaleBollaConsegnaAccertataDto> bolleTrasmesseDl = exportBo.getBolleRiassuntoDaTrasmettereDl(codFiegDl);
		Group<InforivTotaleBollaConsegnaAccertataDto> groupBolleTrasmesse = group(bolleTrasmesseDl, by(on(InforivTotaleBollaConsegnaAccertataDto.class).getCodEdicola()));
		for (Group<InforivTotaleBollaConsegnaAccertataDto> subgroup : groupBolleTrasmesse.subgroups()) {
			List list = select(subgroup.findAll(), having(on(InforivTotaleBollaConsegnaAccertataDto.class).getTotaleDifferenze(), notNullValue()).and(having(on(InforivTotaleBollaConsegnaAccertataDto.class).getTotaleDifferenze(), not(equalTo(0)))));
			exportLines(tipoRecord, now, codFiegDl, mapExportFiles, mapFtpParams, manager, list,dirOutputLocalDL);
		}
		exportBo.updateBolleRiassuntoTrasmesseDl(codFiegDl);
	}

	public InforivExportService getExportBo() {
		return exportBo;
	}

	public void setExportBo(InforivExportService exportBo) {
		this.exportBo = exportBo;
	}
	
}
