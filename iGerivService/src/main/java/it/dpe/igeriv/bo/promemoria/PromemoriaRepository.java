package it.dpe.igeriv.bo.promemoria;

import it.dpe.igeriv.bo.BaseRepository;
import it.dpe.igeriv.dto.PromemoriaDto;

import java.util.List;



interface PromemoriaRepository extends BaseRepository {
	
	/**
	 * @param codEdicola
	 * @param dtMessaggioDa
	 * @param dtMessaggioA
	 * @return
	 */
	public List<PromemoriaDto> getListPromemoria(Integer codEdicola, java.sql.Date dtMessaggioDa, java.sql.Date dtMessaggioA);

	/**
	 * @param codPromemoria
	 * @return
	 */
	public PromemoriaDto getPromemoria(Long codPromemoria);

	/**
	 * @param promemoria
	 */
	public void deletePromemoria(PromemoriaDto promemoria);

	/**
	 * @param codEdicola
	 * @return
	 */
	public Boolean hasPromemoria(Integer codEdicola);

	/**
	 * @param codEdicola
	 * @return
	 */
	public PromemoriaDto getFirstPromemoria(Integer codEdicola);

	/**
	 * @param codPromemoria
	 */
	public void updatePromemoriaLetto(Long codPromemoria);
	
}
