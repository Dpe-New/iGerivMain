package it.dpe.igeriv.bo.localita;


import it.dpe.igeriv.bo.BaseRepository;
import it.dpe.igeriv.vo.LocalitaVo;
import it.dpe.igeriv.vo.PaeseVo;
import it.dpe.igeriv.vo.ProvinciaVo;
import it.dpe.igeriv.vo.TipoLocalitaVo;

import java.util.List;

interface LocalitaRepository extends BaseRepository {
	
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
