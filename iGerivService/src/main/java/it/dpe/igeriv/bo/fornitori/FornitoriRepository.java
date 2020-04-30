package it.dpe.igeriv.bo.fornitori;

import it.dpe.igeriv.bo.BaseRepository;
import it.dpe.igeriv.vo.ProdottiNonEditorialiFornitoreVo;

import java.util.List;




interface FornitoriRepository extends BaseRepository {
	
	/**
	 * @param codFornitore
	 */
	public void deleteFornitore(Integer codFornitore, Integer codEdicola);
	
	/**
	 * @param codEdicola
	 * @param ragioneSociale
	 * @param piva
	 * @param excludeDl 
	 * @return
	 */
	public List<ProdottiNonEditorialiFornitoreVo> getFornitoriEdicola(Integer codEdicola, String ragioneSociale, String piva, boolean excludeDl);

	/**
	 * @param codEdicola
	 * @param codFornitore
	 * @return
	 */
	public ProdottiNonEditorialiFornitoreVo getFornitore(Integer codEdicola, Integer codFornitore);	
	
	
}
