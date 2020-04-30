package it.dpe.inforiv.importer.impl;

import it.dpe.igeriv.exception.ImportException;
import it.dpe.inforiv.dto.input.InforivBaseDto;
import it.dpe.inforiv.importer.InforivImporter;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * @author romanom
 */
@Component("InforivNullImporter")
public class InforivNullImporterImpl implements InforivImporter<InforivBaseDto> {

	@Override
	public void importData(List<InforivBaseDto> list, List<ImportException> listErrori, Integer codEdicolaWeb, Map<String, String> parameters) {
		
	}

}
