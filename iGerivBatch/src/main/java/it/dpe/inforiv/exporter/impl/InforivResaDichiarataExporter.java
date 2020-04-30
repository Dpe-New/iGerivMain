package it.dpe.inforiv.exporter.impl;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;
import it.dpe.igeriv.bo.inforiv.InforivExportService;
import it.dpe.inforiv.dto.output.InforivResaDichiarataDto;
import it.dpe.inforiv.dto.output.InforivTotaleBollaResaEdicolaDto;
import it.dpe.inforiv.exporter.InforivExporter;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.lambdaj.group.Group;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

/**
 * Crea le righe da esportare la resa dichiarata e aggiunge le righe
 * al file corrispondente a ciascuna edicola.
 * 
 * @spring.bean id="InforivResaDichiarataExporter"
 * 
 * @spring.property name="exportBo" ref="InforivExportBo"
 * 
 * @author romanom
 *
 */
@SuppressWarnings({"rawtypes"})
@Component("InforivResaDichiarataExporter")
public class InforivResaDichiarataExporter extends InforivBaseExporter implements InforivExporter {
	@Autowired
	private InforivExportService exportBo;
	
	@Override
	public void exportData(String tipoRecord, Timestamp now, Integer codFiegDl, Map<Integer, File> mapExportFiles, Map<Integer, Map<String, String>> mapFtpParams, FixedFormatManager manager,File dirOutputLocalDL) {
		List<InforivTotaleBollaResaEdicolaDto> bolleTrasmesseDl = exportBo.getBolleResaRiassuntoDaTrasmettereDl(codFiegDl);
		//Group<InforivTotaleBollaResaEdicolaDto> groupBolleTrasmesse = group(bolleTrasmesseDl, by(on(InforivTotaleBollaResaEdicolaDto.class).getCodEdicola()));
		//for (Group<InforivTotaleBollaResaEdicolaDto> subgroup : groupBolleTrasmesse.subgroups()) {
		for (InforivTotaleBollaResaEdicolaDto dto : bolleTrasmesseDl) {
			//List<InforivTotaleBollaResaEdicolaDto> findAll = subgroup.findAll();
			//InforivTotaleBollaResaEdicolaDto dto = findAll.get(0);
			Integer codEdicola = dto.getCodEdicola();
			Date dataBolla = dto.getDataBolla();
			String tipoBolla = dto.getTipoBolla();
			List<InforivResaDichiarataDto> rese = exportBo.getBollaResaDettaglio(codFiegDl, codEdicola, dataBolla, tipoBolla);
			List<InforivResaDichiarataDto> reseFuoriVoce = exportBo.getBollaResaDettaglioFuoriVoce(codFiegDl, codEdicola, dataBolla, tipoBolla);
			List<InforivResaDichiarataDto> reseRichiamo = exportBo.getBollaResaDettaglioRichiamoPersonalizzato(codFiegDl, codEdicola, dataBolla, tipoBolla);
			List<InforivResaDichiarataDto> listRese = new ArrayList<InforivResaDichiarataDto>();
			listRese.addAll(rese);
			listRese.addAll(reseFuoriVoce);
			listRese.addAll(reseRichiamo);
			List list = sort(listRese, on(InforivResaDichiarataDto.class).getNumeroRiga());
			exportLines(tipoRecord, now, codFiegDl, mapExportFiles, mapFtpParams, manager, list,dirOutputLocalDL);
		}
	}

	public InforivExportService getExportBo() {
		return exportBo;
	}

	public void setExportBo(InforivExportService exportBo) {
		this.exportBo = exportBo;
	}
	
}
