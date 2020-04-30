package it.dpe.igeriv.bo.professioni;

import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.vo.ProfessioneVo;

import java.util.List;



/**
 * Interfaccia account
 * 
 * @author mromano
 *
 */
public interface ProfessioniService extends BaseService {
	
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
