package it.dpe.igeriv.bo.localita;


import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.vo.LocalitaVo;
import it.dpe.igeriv.vo.PaeseVo;
import it.dpe.igeriv.vo.ProvinciaVo;
import it.dpe.igeriv.vo.TipoLocalitaVo;

import java.util.List;

/**
 * Interfaccia messaggi
 * 
 * @author mromano
 *
 */
public interface LocalitaService extends BaseService {
	
	/**
	 * @return
	 */
	public List<TipoLocalitaVo> getTipiLocalita();
	
	/**
	 * @param <T>
	 * @return
	 */
	public List<LocalitaVo> getLocalita();
	
	/**
	 * @param localita
	 * @return
	 */
	public List<LocalitaVo> getLocalita(String localita);
	
	/**
	 * @return
	 */
	public List<PaeseVo> getPaesi();
	
	/**
	 * @param <T>
	 * @return
	 */
	public List<ProvinciaVo> getProvince();
}
