package it.dpe.igeriv.bo.card;

import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.dto.VenditaDettaglioDto;
import it.dpe.igeriv.vo.IGerivCardVo;

import java.sql.Timestamp;
import java.util.List;


/**
 * Interfaccia account
 * 
 * @author mromano
 *
 */
public interface CardService extends BaseService {
	
	/**
	 * @param inputText
	 * @param idEdicola
	 * @param codCliente, Integer codCliente 
	 * @return
	 */
	public IGerivCardVo getIGerivCardVo(String inputText, Integer idEdicola, Long codCliente);

	/**
	 * @param inputText
	 * @param idEdicola
	 * @return
	 */
	public List<VenditaDettaglioDto> getUltimiAcquistiIGerivCard(String inputText, Integer idEdicola, Integer codFiegDl, Integer giorni, Long codCliente, Timestamp dataStorico);

	/**
	 * @param inputText
	 * @param idEdicola
	 * @return
	 */
	public List<VenditaDettaglioDto> getSuggerimentiVendita(String inputText, Integer idEdicola);

	/**
	 * @param barcode
	 * @param codCliente
	 */
	public void associaIGerivCard(String barcode, Long codCliente, Integer codEdicola, boolean byPassClienteCheck)  throws Exception;
	
}
