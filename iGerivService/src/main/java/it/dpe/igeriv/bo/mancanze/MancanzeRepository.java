package it.dpe.igeriv.bo.mancanze;

import it.dpe.igeriv.bo.BaseRepository;
import it.dpe.igeriv.dto.MancanzaBollaDto;
import it.dpe.igeriv.dto.MancanzaDto;

import java.sql.Timestamp;
import java.util.List;


interface MancanzeRepository extends BaseRepository {
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dataA 
	 * @param dataDa 
	 * @param titolo
	 * @param stato
	 * @return
	 */
	public List<MancanzaDto> getMancanze(Integer codFiegDl, Integer codEdicola, Timestamp dataDa, Timestamp dataA, String titolo, Integer stato);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dataDa
	 * @param dataA
	 * @param titolo
	 * @return
	 */
	public List<MancanzaBollaDto> getMancanzeBolla(Integer codFiegDl, Integer codEdicola, Timestamp dataDa, Timestamp dataA, String titolo, Boolean soloDifferenze);

	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param idtn
	 * @return
	 */
	public List<MancanzaBollaDto> getMancanzeDettaglioBolla(Integer codFiegDl, Integer codEdicola, Integer idtn, Timestamp dtBolla);
	
	
}
