package it.dpe.igeriv.bo.arretrati;

import it.dpe.igeriv.bo.BaseRepository;

import java.util.List;
import java.util.Map;

interface ArretratiRepository<T> extends BaseRepository {
	
	/**
	 * @param codDl
	 * @param codRivendita	
	 * @return
	 */
	public List<T> getArretrati(Integer codFiegDl, Integer codEdicola);
	
	/**
	 * @param codDl
	 * @param codRivendita	
	 * @return
	 */
	public List<T> getReportArretrati(Integer codFiegDl, Integer codEdicola);
	
	/**
	 * @param mapArretrati
	 */
	public void updateArretrati(Map<Integer, Integer> mapArretrati, Map<Integer, String> mapNoteArretrato);
	
}
