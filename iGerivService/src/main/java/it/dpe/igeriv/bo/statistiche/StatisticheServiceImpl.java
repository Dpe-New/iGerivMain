package it.dpe.igeriv.bo.statistiche;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.dto.StatisticaDettaglioDto;
import it.dpe.igeriv.dto.StatisticaUtilizzoDto;
import it.dpe.igeriv.vo.StatistichePagineVo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("StatisticheService")
class StatisticheServiceImpl extends BaseServiceImpl implements StatisticheService {
	private StatisticheRepository repository;
	
	@Autowired
	StatisticheServiceImpl(StatisticheRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public List<StatisticaDettaglioDto> getStatisticaDettaglioFornito(Integer[] codFiegDl, Integer[] codEdicola, Integer idtn,Timestamp dataStorico) {
		return repository.getStatisticaDettaglioFornito(codFiegDl, codEdicola, idtn,dataStorico);
	}

	@Override
	public List<StatisticaDettaglioDto> getStatisticaDettaglioFornitoRifornimenti(Integer[] codFiegDl, Integer[] codEdicola, Integer idtn) {
		return repository.getStatisticaDettaglioFornitoRifornimenti(codFiegDl, codEdicola, idtn);
	}

	@Override
	public List<StatisticaDettaglioDto> getStatisticaDettaglioReso(Integer[] codFiegDl, Integer[] codEdicola, Integer idtn) {
		return repository.getStatisticaDettaglioReso(codFiegDl, codEdicola, idtn);
	}

	@Override
	public List<StatisticaDettaglioDto> getStatisticaDettaglioResoRiscontrato(Integer[] codFiegDl, Integer[] codEdicola, Integer idtn) {
		return repository.getStatisticaDettaglioResoRiscontrato(codFiegDl, codEdicola, idtn);
	}

	@Override
	public List<StatisticaDettaglioDto> getStatisticaDettaglioVenduto(Integer[] codFiegDl, Integer[] codEdicola, Integer idtn) {
		return repository.getStatisticaDettaglioVenduto(codFiegDl, codEdicola, idtn);
	}
	
	@Override
	public List<StatisticaUtilizzoDto> getStatisticheUtilizzo(Integer codDl, Timestamp dataIniziale, Timestamp dataFinale, Integer codRivenditaWeb) {
		return repository.getStatisticheUtilizzo(codDl, dataIniziale, dataFinale, codRivenditaWeb);
	}

	@Override
	public Map<String, Integer> getPageMonitorMap() {
		return repository.getPageMonitorMap();
	}
	
	@Override
	public StatistichePagineVo getUltimaPagina(Integer codEdicola, String codUtente) {
		return repository.getUltimaPagina(codEdicola, codUtente);
	}
	
	@Override
	public StatistichePagineVo getPaginaCorrente(Integer codEdicola, String codUtente, Integer codPagina) {
		return repository.getPaginaCorrente(codEdicola, codUtente, codPagina);
	}
	
}
