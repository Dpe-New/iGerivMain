package it.dpe.igeriv.bo.contestazioni;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.dto.ContestazioneResaDto;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("")
class ContestazioniServiceImpl extends BaseServiceImpl implements ContestazioniService {
	private final ContestazioniRepository repository;
	
	@Autowired
	ContestazioniServiceImpl(ContestazioniRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public List<ContestazioneResaDto> getContestazioniResa(Integer codFiegDl, Integer codEdicola, Timestamp dataDa, Timestamp dataA, String titolo, Integer stato) {
		return repository.getContestazioniResa(codFiegDl, codEdicola, dataDa, dataA, titolo, stato);
	}
}
