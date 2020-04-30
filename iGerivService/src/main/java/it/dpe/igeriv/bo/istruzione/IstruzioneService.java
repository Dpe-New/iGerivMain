package it.dpe.igeriv.bo.istruzione;

import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.vo.IstruzioneVo;

import java.util.List;



/**
 * Interfaccia account
 * 
 * @author mromano
 *
 */
public interface IstruzioneService extends BaseService {
	
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
