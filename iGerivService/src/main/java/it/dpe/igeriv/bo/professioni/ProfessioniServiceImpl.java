package it.dpe.igeriv.bo.professioni;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.vo.ProfessioneVo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ProfessioniService")
class ProfessioniServiceImpl extends BaseServiceImpl implements ProfessioniService {
	private final ProfessioniRepository repository;
	
	@Autowired
	ProfessioniServiceImpl(ProfessioniRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public List<KeyValueDto> getProfessioni() {
		return repository.getProfessioni();
	}

	@Override
	public ProfessioneVo getProfessione(Integer codProfessione) {
		return repository.getProfessione(codProfessione);
	}

	

}
