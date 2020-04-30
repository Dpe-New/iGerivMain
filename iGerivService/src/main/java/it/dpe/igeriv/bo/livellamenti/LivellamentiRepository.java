package it.dpe.igeriv.bo.livellamenti;

import it.dpe.igeriv.bo.BaseRepository;
import it.dpe.igeriv.dto.LivellamentiDto;
import it.dpe.igeriv.enums.StatoRichiestaPropostaFilterLivellamento;
import it.dpe.igeriv.enums.StatoRichiestaRifornimentoLivellamento;
import it.dpe.igeriv.vo.EdicoleVicineVo;
import it.dpe.igeriv.vo.LivellamentiVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoLivellamentiVo;

import java.util.Date;
import java.util.List;

interface LivellamentiRepository<T> extends BaseRepository {

	List<Long> getIdRichiesteLivellamentiInserite(Integer codEdicola);
	
	List<LivellamentiVo> getRichiesteLivellamentiAccettate(Integer codEdicola);

	List<LivellamentiVo> getLivellamentiByIdRichiestaLivellamento(List<Long> idLivellamenti);
	
	List<LivellamentiVo> getAllLivellamentiAccettatiByIdRichiestaLivellamento(Long idRichiestaLivellamenti);
	
	List<LivellamentiVo> getAllLivellamentiAccettatiByIdRichiestaLivellamento(List<Long> idRichiestaLivellamenti);
	
	List<LivellamentiDto> getLivellamentiDtoAccettatiDaRitirareByIdRichiesta(final Long idRichiestaLivellamenti);
			
	List<LivellamentiVo> getLivellamentiAccettatiRitiratiByIdRichiesta(List<Long> idRichiestaLivellamenti);
	
	List<LivellamentiVo> getLivellamentiByIdLivellamenti(List<Long> idLivellamenti);
	
	List<LivellamentiVo> getLivellamentiAccettatiByIdRichiestaLivellamentoIdEdicola(Long idRichiestaLivellamenti, Integer idEdicola);
	
	List<LivellamentiVo> getLivellamentiInStatoAccettatoByIdRichiestaLivellamentoIdEdicola(Long idRichiestaLivellamenti, Integer idEdicola);
	
	List<LivellamentiVo> getAllLivellamentiNonAccettatiByIdRichiestaLivellamento(List<Long> idRichiestaLivellamenti,List<Long> listIdLivellamentiAccettati);
	
	List<LivellamentiVo> getLivellamentiAccettatiDaRitirareByIdRichiesta(List<Long> idRichiestaLivellamenti);
	
	boolean isValidCodEdicola(Long idRichiestaLivellamenti,Integer codiceVenditaReteEdicole);
	
	boolean isCheckQuantita(Long idRichiestaLivellamenti,Integer codiceVenditaReteEdicole, Integer codiceEdicola, Integer quantita);
			
	List<EdicoleVicineVo> getListEdicoleDelGruppo(Integer codEdicola);

	RichiestaRifornimentoLivellamentiVo getRichiestaRifornimentoLivellamenti(Integer[] codDl, Integer[] codEdicola, Integer idtn, List<StatoRichiestaRifornimentoLivellamento> stato);

	EdicoleVicineVo getEdicoleVicineVo(Integer codEdicola);
	
	void saveEdicolaVicine(Integer codEdicolaWeb, Integer codEdicolaWebVicina);

	void deleteEdicoleVicine(Integer codEdicolaWeb);
	
	RichiestaRifornimentoLivellamentiVo getRichiestaRifornimentoLivellamentiByEdicolaVenditrice(Integer codDl, Integer codEdicola, Integer idtn);

	RichiestaRifornimentoLivellamentiVo getRichiestaRifornimentoLivellamentiByCodEdicolaVenditrice(Integer codDl, Integer idtn);
	
	RichiestaRifornimentoLivellamentiVo getRichiestaRifornimentoLivellamentiByEdicolaRichiedente(Integer codDl, Integer codEdicolaRichiedente, Integer idtn);
	
	List<RichiestaRifornimentoLivellamentiVo> getRichiesteRifornimentoLivellamentiByIds(List<Long> listIds);

	List<RichiestaRifornimentoLivellamentiVo> getRichiesteRifornimentiLivellamentiByDateStato(Integer codEdicola, Date dataDa, Date dataA,StatoRichiestaRifornimentoLivellamento stato);
	
	List<LivellamentiVo> getRichiesteRifornimentoLivellamentiByDateStato(Integer codEdicola, Date dataDa, Date dataA, StatoRichiestaPropostaFilterLivellamento stato);

	List<LivellamentiVo> getProposteRifornimentoLivellamentiByDateStato(Integer codEdicola, Date dataDa, Date dataA, StatoRichiestaPropostaFilterLivellamento stato);

	
}
