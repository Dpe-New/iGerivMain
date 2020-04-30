package it.dpe.igeriv.bo.arretrati;

import it.dpe.igeriv.bo.BaseService;

import java.util.List;
import java.util.Map;

/**
 * Interfaccia arretrati
 * 
 * @author mromano
 *
 */
public interface ArretratiService<T> extends BaseService {
	
	/**
	 * Ritorna gli arretrati da confermare per una edicola
	 * 
	 * @param codDl
	 * @param codRivendita	
	 * @return
	 */
	public List<T> getArretrati(Integer codFiegDl, Integer codEdicola);
	
	/**
	 * Ritorna gli arretrati da visualizzare nel report per una edicola
	 * 
	 * @param codFiegDl
	 * @param codEdicola
	 * @return
	 */
	public List<T> getReportArretrati(Integer codFiegDl, Integer codEdicola);
	
	/**
	 * Esegue l'update degli arretrati
	 * 
	 * @param mapArretrati
	 * @param mapNoteArretrato
	 */
	public void updateArretrati(Map<Integer, Integer> mapArretrati, Map<Integer, String> mapNoteArretrato);
	
}
