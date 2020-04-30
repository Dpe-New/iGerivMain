package it.dpe.igeriv.bo.campagna;

import java.util.List;

import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.dto.CampagnaDto;
import it.dpe.igeriv.dto.CampagnaEdicoleDto;
import it.dpe.igeriv.vo.CampagnaEdicoleVo;
import it.dpe.igeriv.vo.CampagnaVo;

public interface CampagnaService extends BaseService{

	/**
	 * Restituisce tutte le campagna attive confrontando le date di inizio e fine campagna con la SYSDATE
	 * 
	 * @return List<CampagnaVo>
	 */
	public List<CampagnaDto> getCampagnaIsActive();
	
	/**
	 * Restituisce tutte le campagna attive per DL
	 * @param codDl
	 * @return CampagnaVo
	 */
	public List<CampagnaDto> getCampagnaByCodiceDl(Integer codDl);
	
	/**
	 * 
	 * @param idCamp
	 * @return CampagnaDto
	 */
	public CampagnaVo getCampagnaByIdCamp(Integer idCamp);
	
	/**
	 * 
	 * @param idCamp
	 * @return List<CampagnaEdicoleVo>
	 */
	public List<CampagnaEdicoleDto> getEdicoleByIdCampagna(Integer idCamp);
	
	/**
	 * 
	 * @param idCamp
	 * @param flagStato
	 * @return List<CampagnaEdicoleDto>
	 */
	public List<CampagnaEdicoleDto> getEdicoleByIdCampagnaStato(Integer idCamp,Integer flagStato);
	
	/**
	 * 
	 * @param crivw
	 * @return CampagnaEdicoleVo
	 */
	public CampagnaEdicoleVo getEdicoleByCrivw(Integer crivw);
	
	/**
	 * 
	 * @param campEdicole
	 */
	public void updateCampagnaEdicole(CampagnaEdicoleVo campEdicole);
	
}
