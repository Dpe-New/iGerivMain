package it.dpe.igeriv.bo.professioni;

import it.dpe.igeriv.bo.BaseRepository;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.vo.ProfessioneVo;

import java.util.List;


interface ProfessioniRepository extends BaseRepository {
	
	/**
	 * @return
	 */
	public List<KeyValueDto> getProfessioni();

	/**
	 * @param codProfessione
	 * @return
	 */
	public ProfessioneVo getProfessione(Integer codProfessione);
	
}
