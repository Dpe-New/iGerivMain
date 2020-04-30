package it.dpe.igeriv.bo.livellamenti;

import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.dto.LivellamentiDto;
import it.dpe.igeriv.enums.StatoRichiestaPropostaFilterLivellamento;
import it.dpe.igeriv.enums.StatoRichiestaRifornimentoLivellamento;
import it.dpe.igeriv.vo.EdicoleVicineVo;
import it.dpe.igeriv.vo.LivellamentiVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoLivellamentiVo;

import java.util.Date;
import java.util.List;

/**
 * Interfaccia livellamenti
 * 
 * @author mromano
 *
 */
public interface LivellamentiService<T> extends BaseService {
	
	/**
	 * Ritorna gli id delle richieste livellamenti per una edicola
	 * 
	 * @param codDl
	 * @param codRivendita	
	 * @return
	 */
	public List<Long> getIdRichiesteLivellamentiInserite(Integer codEdicola);
	
	/**
	 * Ritorna gli id delle richieste livellamento accetate dalle edicola che vendono
	 * La priorità è definita per:
	 * 1. Quantità di ordini evasi
	 * 2. Data di accettazione dell'ordine
	 * 
	 * @param codEdicola
	 * @return
	 */
	public List<Long> getIdRichiesteLivellamentiAccettate(Integer codEdicola);
	
	/**
	 * Ritorna livellamenti per idLivellamenti
	 * 
	 * @param codDl
	 * @param codRivendita	
	 * @return
	 */
	public List<LivellamentiVo> getLivellamentiByIdLivellamenti(List<Long> idLivellamenti);
	
	
	/**
	 * Ritorna la richiesta livellamenti per idRichiesta
	 * 
	 * @param codDl
	 * @param codRivendita	
	 * @return
	 */
	public List<LivellamentiVo> getLivellamentiByIdRichiestaLivellamento(List<Long> idLivellamenti);
	
	
	
	
	
	/**
	 * @param idRichiestaLivellamenti
	 * @return List<LivellamentiVo>
	 */
	public List<LivellamentiVo> getAllLivellamentiAccettatiByIdRichiestaLivellamento(Long idRichiestaLivellamenti);
	
	
	/**
	 * @param idRichiestaLivellamenti
	 * @return List<LivellamentiVo>
	 */
	public List<LivellamentiVo> getLivellamentiAccettatiByIdRichiestaLivellamentoIdEdicola(Long idRichiestaLivellamenti, Integer idEdicola);
	
	
	/**
	 * 
	 * @param List<idLivellamenti>
	 * @return List<LivellamentiVo>
	 */
	public List<LivellamentiVo> getAllLivellamentiAccettatiByIdRichiestaLivellamento(List<Long> idRichiestaLivellamenti);
	
	/**
	 * 
	 * @param idRichiestaLivellamenti
	 * @return
	 */
	public List<LivellamentiVo> getLivellamentiAccettatiDaRitirareByIdRichiesta(List<Long> idRichiestaLivellamenti);
	
	/**
	 * 
	 * @param idRichiestaLivellamenti
	 * @return
	 */
	public List<LivellamentiVo> getLivellamentiAccettatiRitiratiByIdRichiesta(List<Long> idRichiestaLivellamenti);
	
	/**
	 * 
	 * @param idRichiestaLivellamenti
	 * @return
	 */
	//public List<LivellamentiVo> getLivellamentiAccettatiRitiratiByIdRichiesta(List<Long> idRichiestaLivellamenti);
	
	/**
	 * 
	 * @param idRichiestaLivellamenti
	 * @param idEdicola
	 * @return
	 */
	public List<LivellamentiVo> getLivellamentiInStatoAccettatoByIdRichiestaLivellamentoIdEdicola(Long idRichiestaLivellamenti, Integer idEdicola);
	
	/**
	 * 
	 * @param idRichiestaLivellamenti
	 * @param codiceVenditaReteEdicole
	 * @return
	 */
	public boolean isValidCodEdicola(Long idRichiestaLivellamenti,Integer codiceVenditaReteEdicole);
	
	/**
	 * 
	 * @param idRichiestaLivellamenti
	 * @param codiceVenditaReteEdicole
	 * @return
	 */
	public boolean isCheckQuantita(Long idRichiestaLivellamenti,Integer codiceVenditaReteEdicole, Integer codiceEdicola, Integer quantita);
	
	/**
	 * 	
	 * @param idRichiestaLivellamenti
	 * @return List<LivellamentiVo>
	 */
	public List<LivellamentiVo> getAllLivellamentiNonAccettatiByIdRichiestaLivellamento(List<Long> idRichiestaLivellamenti,List<Long> listIdLivellamentiAccettati);
	
	
	/**
	 * 
	 * @param idRichiestaLivellamenti
	 * @return
	 */
	public List<LivellamentiDto> getLivellamentiDtoAccettatiDaRitirareByIdRichiesta(final Long idRichiestaLivellamenti);
	
	
	/**
	 * @param codDl
	 * @param codEdicola
	 * @return
	 */
	public RichiestaRifornimentoLivellamentiVo getRichiestaRifornimentoLivellamenti(Integer[] codDl, Integer[] codEdicola, Integer idtn, List<StatoRichiestaRifornimentoLivellamento> stato);
	
	/**
	 * @return
	 */
	public List<RichiestaRifornimentoLivellamentiVo> getRichiesteRifornimentoLivellamentiByIds(List<Long> listIds);
	
	/**
	 * @param codDl
	 * @param codEdicola
	 * @param idtn
	 * @return
	 */
	public RichiestaRifornimentoLivellamentiVo getRichiestaRifornimentoLivellamentiByEdicolaVenditrice(Integer codDl, Integer codEdicola, Integer idtn);
	
	
	/**
	 * @param codDl
	 * @param codEdicolaRichiedente
	 * @param idtn
	 * @return
	 */
	public RichiestaRifornimentoLivellamentiVo getRichiestaRifornimentoLivellamentiByEdicolaRichiedente(Integer codDl, Integer codEdicolaRichiedente, Integer idtn);
	
	
	
	/**
	 * 
	 * @param codDl
	 * @param codEdicola
	 * @param idtn
	 * @return
	 */
	public RichiestaRifornimentoLivellamentiVo getRichiestaRifornimentoLivellamentiByCodEdicolaVenditrice(Integer codDl, Integer idtn);
	
	/**
	 * @param codEdicola
	 * @return
	 */
	public List<EdicoleVicineVo> getListEdicoleDelGruppo(Integer codEdicola);
	
	/**
	 * 
	 * @param codEdicolaWeb
	 * @param codEdicolaWebVicina
	 */
	public void saveEdicolaVicine(Integer codEdicolaWeb, Integer codEdicolaWebVicina);
	
	
	/**
	 * 
	 * @param codEdicolaWeb
	 */
	public void deleteEdicoleVicine(Integer codEdicolaWeb);
	
	/**
	 * @param listLivellamentiAccetati
	 * @param listLivellamentiNonAccettati
	 * @param richieste
	 */
	public void saveAccettazioneLivellamenti(List<LivellamentiVo> listLivellamentiAccetati, List<LivellamentiVo> listLivellamentiNonAccettati, List<RichiestaRifornimentoLivellamentiVo> richieste);
	
	/**
	 * Memorizza una vendita livellamenti
	 * Setta lo stato della vendita a StatoRichiestaLivellamento.VENDUTO
	 * 
	 * @param richiesta
	 */
	public void saveVenditaLivellamenti(RichiestaRifornimentoLivellamentiVo richiesta);
	
	/**
	 * @param codEdicola
	 * @return
	 */
	public EdicoleVicineVo getEdicoleVicineVo(Integer codEdicola);
	
	/**
	 * 
	 * @param codEdicola
	 * @param dataDa
	 * @param dataA
	 * @param stato
	 * @return
	 */
	public List<RichiestaRifornimentoLivellamentiVo> getRichiesteRifornimentiLivellamentiByDateStato(Integer codEdicola, Date dataDa, Date dataA,StatoRichiestaRifornimentoLivellamento stato);
	
	
	/**
	 * @param codEdicola
	 * @param dataDa
	 * @param dataA
	 * @param stato
	 * @return
	 */
	public List<LivellamentiVo> getRichiesteRifornimentoLivellamentiByDateStato(Integer codEdicola, Date dataDa, Date dataA, StatoRichiestaPropostaFilterLivellamento stato);
	
	/**
	 * @param codEdicola
	 * @param dataDa
	 * @param dataA
	 * @param stato
	 * @return
	 */
	public List<LivellamentiVo> getProposteRifornimentoLivellamentiByDateStato(Integer codEdicola, Date dataDa, Date dataA, StatoRichiestaPropostaFilterLivellamento stato);
	
}
