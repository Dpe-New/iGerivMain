package it.dpe.igeriv.bo.istruzione;

import it.dpe.igeriv.bo.BaseRepository;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.vo.IstruzioneVo;

import java.util.List;


interface IstruzioneRepository extends BaseRepository {
	
	/**
	 * @return
	 */
	public List<KeyValueDto> getIstruzione();
	
	/**
	 * @param codIstruzione
	 * @return
	 */
	public IstruzioneVo getIstruzione(Integer codIstruzione);
	
	
}
