package it.dpe.inforiv.importer.factory;

import it.dpe.inforiv.dto.output.InforivVenditeDto;
import it.dpe.inforiv.importer.InforivImporter;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@SuppressWarnings("rawtypes")
@Component("ImportDtoFactory")
public class ImportDtoFactory {
	
	@Resource
	private Map<String, Object> mapTipoRecord;
	
	@Resource
	private Map<String, InforivImporter> mapImporters;
	
	/**
	 * @param tipoRecord
	 * @param riga
	 * @return
	 */
	public Object getImportDto(String tipoRecord, String riga) {
		Object tr = null;
		if (tipoRecord.equals("VE") && riga.length() == InforivVenditeDto.lineLength) {
			tr = mapTipoRecord.get("VN");
		} else {
			tr = mapTipoRecord.get(tipoRecord);
		}
		return tr;
	}
	
	/**
	 * @param tipoRecord
	 * @param riga
	 * @return
	 */
	public InforivImporter getInforivImporter(String tipoRecord, String riga) {
		InforivImporter tr = null;
		if (tipoRecord.equals("VE") && riga.length() == InforivVenditeDto.lineLength) {
			tr = mapImporters.get("VN");
		} else {
			tr = mapImporters.get(tipoRecord);
		}
		return tr;
	}
	
}
