package it.dpe.inforiv.importer;

import it.dpe.igeriv.exception.ImportException;

import java.util.List;
import java.util.Map;


public interface InforivImporter<T> {
	
	/** 
	 * Esegue l'importazione della lista di dto.
	 * L'implementazione del bean InforivImporter per ogni tipo record è presente nella mappa mapImporters definita nel customBeansContext.xml.
	 *  
	 * @param list
	 * @param listErrori
	 * @param codEdicolaWeb
	 */
	public void importData(List<T> list, List<ImportException> listErrori, Integer codEdicolaWeb, Map<String, String> parameters);
}
