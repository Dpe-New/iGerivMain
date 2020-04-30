package it.dpe.igeriv.bo.statistiche;

import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.dto.StatisticaDettaglioDto;
import it.dpe.igeriv.dto.StatisticaUtilizzoDto;
import it.dpe.igeriv.vo.StatistichePagineVo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


/**
 * Interfaccia account
 * 
 * @author mromano
 *
 */
public interface StatisticheService extends BaseService {
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param idtn
	 * @return
	 */
	public List<StatisticaDettaglioDto> getStatisticaDettaglioFornito(Integer[] codFiegDl, Integer[] codEdicola, Integer idtn,Timestamp dataStorico);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param idtn 
	 * @return
	 */
	public List<StatisticaDettaglioDto> getStatisticaDettaglioFornitoRifornimenti(Integer[] codFiegDl, Integer[] codEdicola, Integer idtn);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param idtn
	 * @return
	 */
	public List<StatisticaDettaglioDto> getStatisticaDettaglioReso(Integer[] codFiegDl, Integer[] codEdicola, Integer idtn);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param idtn
	 * @return
	 */
	public List<StatisticaDettaglioDto> getStatisticaDettaglioResoRiscontrato(Integer[] codFiegDl, Integer[] codEdicola, Integer idtn);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param idtn
	 * @return
	 */
	public List<StatisticaDettaglioDto> getStatisticaDettaglioVenduto(Integer[] codFiegDl, Integer[] codEdicola, Integer idtn);
	
	/**
	 * 
	 * @param codDl
	 * @param dataIniziale
	 * @param dataFinale
	 * @return
	 */
	public List<StatisticaUtilizzoDto> getStatisticheUtilizzo(Integer codDl, Timestamp dataIniziale, Timestamp dataFinale, Integer codRivenditaWeb);
	
	/**
	 * @return
	 */
	public Map<String, Integer> getPageMonitorMap();

	/**
	 * @param codEdicola
	 * @param codUtente
	 * @return
	 */
	public StatistichePagineVo getUltimaPagina(Integer codEdicola, String codUtente);
	
	/**
	 * @param codEdicola
	 * @param codUtente
	 * @param codPagina
	 * @return
	 */
	public StatistichePagineVo getPaginaCorrente(Integer codEdicola, String codUtente, Integer codPagina);
	
}
