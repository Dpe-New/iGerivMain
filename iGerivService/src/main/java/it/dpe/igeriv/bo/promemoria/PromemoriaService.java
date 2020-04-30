package it.dpe.igeriv.bo.promemoria;

import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.dto.PromemoriaDto;

import java.util.List;


/**
 * Interfaccia account
 * 
 * @author mromano
 *
 */
public interface PromemoriaService extends BaseService {
	
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
	
	/**
	 * @param promemoria
	 */
	public void savePromemoria(PromemoriaDto promemoria);
	
}
