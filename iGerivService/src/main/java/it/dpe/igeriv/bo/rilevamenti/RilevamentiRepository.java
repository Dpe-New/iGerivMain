package it.dpe.igeriv.bo.rilevamenti;

import it.dpe.igeriv.bo.BaseRepository;
import it.dpe.igeriv.dto.RilevamentiDto;

import java.util.List;
import java.util.Map;

interface RilevamentiRepository extends BaseRepository {
	
	/**
	 * @param codDl
	 * @param codRivendita	
	 * @return
	 */
	public List<RilevamentiDto> getRilevamenti(Integer codFiegDl, Integer codEdicola);
	
	/**
	 * @param mapRilevamenti
	 * @param codFiegDl
	 * @param codEdicola
	 */
	public void updateRilevamenti(Map<Integer, Integer> mapRilevamenti);
	
}
