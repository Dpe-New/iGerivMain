package it.dpe.igeriv.bo.livellamenti;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.dto.ArretratiDto;
import it.dpe.igeriv.dto.LivellamentiDto;
import it.dpe.igeriv.enums.StatoRichiestaPropostaFilterLivellamento;
import it.dpe.igeriv.enums.StatoRichiestaRifornimentoLivellamento;
import it.dpe.igeriv.vo.EdicoleVicineVo;
import it.dpe.igeriv.vo.LivellamentiVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoLivellamentiVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.lambdaj.group.Group;

@Service("LivellamentiService")
class LivellamentiServiceImpl extends BaseServiceImpl implements LivellamentiService<ArretratiDto> {
	private final LivellamentiRepository<LivellamentiVo> repository;
	
	@Autowired
	LivellamentiServiceImpl(LivellamentiRepository<LivellamentiVo> repository) {
		super(repository);
		this.repository = repository;
	}
	
	@Override
	public List<Long> getIdRichiesteLivellamentiInserite(Integer codEdicola) {
		return repository.getIdRichiesteLivellamentiInserite(codEdicola);
	}
	
	@Override
	public List<Long> getIdRichiesteLivellamentiAccettate(Integer codEdicola) {
		List<Long> idRichiesteLivellamentiAccettate = new ArrayList<>();
		List<LivellamentiVo> richiesteLivellamentiAccettate = repository.getRichiesteLivellamentiAccettate(codEdicola);
		if (richiesteLivellamentiAccettate != null && !richiesteLivellamentiAccettate.isEmpty()) {
			Group<LivellamentiVo> group = group(richiesteLivellamentiAccettate, by(on(LivellamentiVo.class).getRichiesta().getIdRichiestaLivellamento()));
			for (Group<LivellamentiVo> subgroup : group.subgroups()) {
				Group<LivellamentiVo> groupEdicole = group(subgroup.findAll(), by(on(LivellamentiVo.class).getEdicola().getCodEdicola()));
				List<Group<LivellamentiVo>> list = groupEdicole.subgroups();
				Collections.sort(list, new Comparator<Group<LivellamentiVo>>() {
					@Override
					public int compare(Group<LivellamentiVo> o1, Group<LivellamentiVo> o2) {
						return new CompareToBuilder().append(o2.findAll().size(), o1.findAll().size()).append(o1.findAll().get(0).getDataAccettazione(), o2.findAll().get(0).getDataAccettazione()).toComparison();
					}
				});
				Group<LivellamentiVo> selectedGroup = list.get(0);
				idRichiesteLivellamentiAccettate.addAll(extract(selectedGroup.findAll(), on(LivellamentiVo.class).getIdLivellamento()));
			}
		}
		return idRichiesteLivellamentiAccettate;
	}
	
	@Override
	public List<LivellamentiVo> getLivellamentiByIdLivellamenti(List<Long> idLivellamento) {
		return repository.getLivellamentiByIdLivellamenti(idLivellamento);
	}
	
	@Override
	public List<LivellamentiVo> getLivellamentiByIdRichiestaLivellamento(List<Long> idLivellamento) {
		return repository.getLivellamentiByIdRichiestaLivellamento(idLivellamento);
	}
	
	@Override
	public List<LivellamentiVo> getAllLivellamentiAccettatiByIdRichiestaLivellamento(Long idRichiestaLivellamenti){
		return repository.getAllLivellamentiAccettatiByIdRichiestaLivellamento(idRichiestaLivellamenti);
	}
	
	@Override
	public List<LivellamentiDto> getLivellamentiDtoAccettatiDaRitirareByIdRichiesta(final Long idRichiestaLivellamenti){
		return repository.getLivellamentiDtoAccettatiDaRitirareByIdRichiesta(idRichiestaLivellamenti);
	}
	@Override
	public List<LivellamentiVo> getLivellamentiAccettatiRitiratiByIdRichiesta(List<Long> idRichiestaLivellamenti){
		return repository.getLivellamentiAccettatiRitiratiByIdRichiesta(idRichiestaLivellamenti);
	}
	
	
	@Override
	public List<LivellamentiVo> getLivellamentiAccettatiByIdRichiestaLivellamentoIdEdicola(Long idRichiestaLivellamenti, Integer idEdicola){
		return repository.getLivellamentiAccettatiByIdRichiestaLivellamentoIdEdicola(idRichiestaLivellamenti,idEdicola);
	}
	
	@Override
	public List<LivellamentiVo> getLivellamentiInStatoAccettatoByIdRichiestaLivellamentoIdEdicola(Long idRichiestaLivellamenti, Integer idEdicola){
		return repository.getLivellamentiInStatoAccettatoByIdRichiestaLivellamentoIdEdicola(idRichiestaLivellamenti, idEdicola);
	}

	
	@Override
	public List<LivellamentiVo> getAllLivellamentiAccettatiByIdRichiestaLivellamento(List<Long> idRichiestaLivellamenti){
		return repository.getAllLivellamentiAccettatiByIdRichiestaLivellamento(idRichiestaLivellamenti);
	}
	
	@Override
	public List<LivellamentiVo> getLivellamentiAccettatiDaRitirareByIdRichiesta(List<Long> idRichiestaLivellamenti){
		return repository.getLivellamentiAccettatiDaRitirareByIdRichiesta(idRichiestaLivellamenti);
	}
	
	@Override
	public boolean isValidCodEdicola(Long idRichiestaLivellamenti,Integer codiceVenditaReteEdicole){
		return repository.isValidCodEdicola(idRichiestaLivellamenti,codiceVenditaReteEdicole);
	}
	
	@Override
	public boolean isCheckQuantita(Long idRichiestaLivellamenti,Integer codiceVenditaReteEdicole, Integer codiceEdicola, Integer quantita){
		return repository.isCheckQuantita(idRichiestaLivellamenti,codiceVenditaReteEdicole, codiceEdicola, quantita);
	}
	
	@Override
	public List<LivellamentiVo> getAllLivellamentiNonAccettatiByIdRichiestaLivellamento(List<Long> idRichiestaLivellamenti,List<Long> listIdLivellamentiAccettati){
		return repository.getAllLivellamentiNonAccettatiByIdRichiestaLivellamento(idRichiestaLivellamenti,listIdLivellamentiAccettati);
	}

	
	@Override
	public RichiestaRifornimentoLivellamentiVo getRichiestaRifornimentoLivellamenti(Integer[] codDl, Integer[] codEdicola, Integer idtn, List<StatoRichiestaRifornimentoLivellamento> stato) {
		return repository.getRichiestaRifornimentoLivellamenti(codDl, codEdicola, idtn, stato);
	}
	
	@Override
	public List<RichiestaRifornimentoLivellamentiVo> getRichiesteRifornimentoLivellamentiByIds(List<Long> listIds) {
		return repository.getRichiesteRifornimentoLivellamentiByIds(listIds);
	}
	
	@Override
	public RichiestaRifornimentoLivellamentiVo getRichiestaRifornimentoLivellamentiByEdicolaVenditrice(
			Integer codDl, Integer codEdicola, Integer idtn) {
		return repository.getRichiestaRifornimentoLivellamentiByEdicolaVenditrice(codDl, codEdicola, idtn);
	}
	
	@Override
	public RichiestaRifornimentoLivellamentiVo getRichiestaRifornimentoLivellamentiByCodEdicolaVenditrice(Integer codDl, Integer idtn){
		return repository.getRichiestaRifornimentoLivellamentiByCodEdicolaVenditrice(codDl, idtn);
	}
	@Override
	public RichiestaRifornimentoLivellamentiVo getRichiestaRifornimentoLivellamentiByEdicolaRichiedente(Integer codDl, Integer codEdicolaRichiedente, Integer idtn){
		return repository.getRichiestaRifornimentoLivellamentiByEdicolaRichiedente(codDl,codEdicolaRichiedente,idtn);
	}
	
	
	@Override
	public List<EdicoleVicineVo> getListEdicoleDelGruppo(Integer codEdicola) {
		return repository.getListEdicoleDelGruppo(codEdicola);
	}
	@Override
	public void saveEdicolaVicine(Integer codEdicolaWeb, Integer codEdicolaWebVicina){
		repository.saveEdicolaVicine(codEdicolaWeb, codEdicolaWebVicina);
	}
	
	
	
	
	@Override
	public void saveAccettazioneLivellamenti(List<LivellamentiVo> listLivellamentiAccetati, List<LivellamentiVo> listLivellamentiNonAccettati, List<RichiestaRifornimentoLivellamentiVo> richieste) {
		if (listLivellamentiAccetati != null && !listLivellamentiAccetati.isEmpty()) {
			saveVoList(listLivellamentiAccetati);
		}
		if (richieste != null && !richieste.isEmpty()) {
			saveVoList(richieste);
		}
		if (listLivellamentiNonAccettati != null && !listLivellamentiNonAccettati.isEmpty()) {
			saveVoList(listLivellamentiNonAccettati);
		}
	}
	
	@Override
	public void saveVenditaLivellamenti(RichiestaRifornimentoLivellamentiVo richiesta) {
		if (richiesta != null) {
			if (richiesta.getLivellamenti() != null && !richiesta.getLivellamenti().isEmpty()) {
				saveVoList(richiesta.getLivellamenti());
			}
			saveBaseVo(richiesta);
		}
	}
	
	@Override
	public EdicoleVicineVo getEdicoleVicineVo(Integer codEdicola) {
		return repository.getEdicoleVicineVo(codEdicola);
	}
	
	@Override
	public List<RichiestaRifornimentoLivellamentiVo> getRichiesteRifornimentiLivellamentiByDateStato(Integer codEdicola, Date dataDa, Date dataA,StatoRichiestaRifornimentoLivellamento stato) {
		return repository.getRichiesteRifornimentiLivellamentiByDateStato(codEdicola, dataDa, dataA, stato);
	}	
	
	@Override
	public List<LivellamentiVo> getRichiesteRifornimentoLivellamentiByDateStato(Integer codEdicola, Date dataDa, Date dataA, StatoRichiestaPropostaFilterLivellamento stato) {
		return repository.getRichiesteRifornimentoLivellamentiByDateStato(codEdicola, dataDa, dataA, stato);
	}
	
	@Override
	public List<LivellamentiVo> getProposteRifornimentoLivellamentiByDateStato(Integer codEdicola, Date dataDa, Date dataA, StatoRichiestaPropostaFilterLivellamento stato) {
		return repository.getProposteRifornimentoLivellamentiByDateStato(codEdicola, dataDa, dataA, stato);
	}

	@Override
	public void deleteEdicoleVicine(Integer codEdicolaWeb) {
		repository.deleteEdicoleVicine(codEdicolaWeb);
	}

	
	

}
