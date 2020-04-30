package it.dpe.igeriv.bo.promemoria;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.dto.PromemoriaDto;
import it.dpe.igeriv.vo.PromemoriaVo;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("PromemoriaService")
class PromemoriaServiceImpl extends BaseServiceImpl implements PromemoriaService {
	private final PromemoriaRepository repository;
	
	@Autowired
	PromemoriaServiceImpl(PromemoriaRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public List<PromemoriaDto> getListPromemoria(Integer codEdicola, Date dtMessaggioDa, Date dtMessaggioA) {
		return repository.getListPromemoria(codEdicola, dtMessaggioDa, dtMessaggioA);
	}

	@Override
	public PromemoriaDto getPromemoria(Long codPromemoria) {
		return repository.getPromemoria(codPromemoria);
	}

	@Override
	public void deletePromemoria(PromemoriaDto promemoria) {
		repository.deletePromemoria(promemoria);
	}

	@Override
	public Boolean hasPromemoria(Integer codEdicola) {
		return repository.hasPromemoria(codEdicola);
	}

	@Override
	public PromemoriaDto getFirstPromemoria(Integer codEdicola) {
		return repository.getFirstPromemoria(codEdicola);
	}

	@Override
	public void updatePromemoriaLetto(Long codPromemoria) {
		repository.updatePromemoriaLetto(codPromemoria);
	}

	@Override
	public void savePromemoria(PromemoriaDto promemoria) {
		PromemoriaVo vo = new PromemoriaVo();
		vo.setCodPromemoria(promemoria.getCodPromemoria());
		vo.setCodEdicola(promemoria.getCodEdicola());
		vo.setDataMessaggio(new java.sql.Date(promemoria.getDataMessaggio().getTime()));
		vo.setLetto(false);
		vo.setMessaggio(promemoria.getMessaggio());
		saveBaseVo(vo);
	}

}
