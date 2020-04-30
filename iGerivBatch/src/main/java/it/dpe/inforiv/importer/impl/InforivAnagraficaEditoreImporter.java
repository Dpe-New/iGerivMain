package it.dpe.inforiv.importer.impl;

import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.exception.ImportException;
import it.dpe.inforiv.dto.input.InforivEditoreDto;
import it.dpe.inforiv.importer.InforivImporter;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author romanom
 */
@Component("InforivAnagraficaEditoreImporter")
public class InforivAnagraficaEditoreImporter extends InforivBaseImporter implements InforivImporter<InforivEditoreDto> {
	private final IGerivBatchService bo;
	
	@Autowired
	InforivAnagraficaEditoreImporter(IGerivBatchService bo) {
		this.bo = bo;
	}
	
	@Override
	public void importData(List<InforivEditoreDto> list, List<ImportException> listErrori, Integer codEdicolaWeb, Map<String, String> parameters) {
		/*for (InforivEditoreDto dto : list) {
			AnagraficaEditoreVo editore = bo.getAnagraficaEditore(dto.getCodFiegDl());
		}*/
	}

}
