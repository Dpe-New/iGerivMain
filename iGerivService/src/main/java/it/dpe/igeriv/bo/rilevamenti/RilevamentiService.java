package it.dpe.igeriv.bo.rilevamenti;

import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.dto.RilevamentiDto;

import java.util.List;
import java.util.Map;

/**
 * Interfaccia rilevamenti
 * 
 * @author mromano
 *
 */
public interface RilevamentiService extends BaseService {
	
	/**
	 * Ritorna i rilevamenti da aggiornare per una edicola
	 * 
	 * @param codDl
	 * @param codRivendita	
	 * @return
	 */
	public List<RilevamentiDto> getRilevamenti(Integer codFiegDl, Integer codEdicola);
	
	/**
	 * Esegue l'update dei rilevamenti
	 * 
	 * @param listRilevamenti
	 * @param codFiegDl
	 * @param codEdicola
	 */
	public void updateRilevamenti(Map<Integer, Integer> mapRilevamenti, Integer codFiegDl, Integer codEdicola);
	
}
