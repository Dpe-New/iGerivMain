package it.dpe.igeriv.bo.contestazioni;

import it.dpe.igeriv.bo.BaseRepository;
import it.dpe.igeriv.dto.ContestazioneResaDto;

import java.sql.Timestamp;
import java.util.List;


interface ContestazioniRepository extends BaseRepository {
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dataA 
	 * @param dataDa 
	 * @param titolo
	 * @param stato
	 * @return
	 */
	public List<ContestazioneResaDto> getContestazioniResa(Integer codFiegDl, Integer codEdicola, Timestamp dataDa, Timestamp dataA, String titolo, Integer stato);
	
}
