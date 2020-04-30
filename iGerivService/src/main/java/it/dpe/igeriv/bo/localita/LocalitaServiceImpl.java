package it.dpe.igeriv.bo.localita;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.vo.LocalitaVo;
import it.dpe.igeriv.vo.PaeseVo;
import it.dpe.igeriv.vo.ProvinciaVo;
import it.dpe.igeriv.vo.TipoLocalitaVo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("LocalitaService")
class LocalitaServiceImpl extends BaseServiceImpl implements LocalitaService {
	private final LocalitaRepository repository;
	
	@Autowired
	LocalitaServiceImpl(LocalitaRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public List<TipoLocalitaVo> getTipiLocalita() {
		return repository.getTipiLocalita();
	}

	@Override
	public List<LocalitaVo> getLocalita() {
		return repository.getLocalita();
	}

	@Override
	public List<LocalitaVo> getLocalita(String localita) {
		return repository.getLocalita(localita);
	}

	@Override
	public List<PaeseVo> getPaesi() {
		return repository.getPaesi();
	}

	@Override
	public List<ProvinciaVo> getProvince() {
		return repository.getProvince();
	}

}
