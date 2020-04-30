package it.dpe.igeriv.bo.mancanze;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.dto.MancanzaBollaDto;
import it.dpe.igeriv.dto.MancanzaDto;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("MancanzeService")
class MancanzeServiceImpl extends BaseServiceImpl implements MancanzeService {
	private final MancanzeRepository repository;
	
	@Autowired
	MancanzeServiceImpl(MancanzeRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public List<MancanzaDto> getMancanze(Integer codFiegDl, Integer codEdicola, Timestamp dataDa, Timestamp dataA, String titolo, Integer stato) {
		return repository.getMancanze(codFiegDl, codEdicola, dataDa, dataA, titolo, stato);
	}
	
	@Override
	public List<MancanzaBollaDto> getMancanzeBolla(Integer codFiegDl, Integer codEdicola, Timestamp dataDa, Timestamp dataA, String titolo, Boolean soloDifferenze) {
		return repository.getMancanzeBolla(codFiegDl, codEdicola, dataDa, dataA, titolo, soloDifferenze);
	}
	
	@Override
	public List<MancanzaBollaDto> getMancanzeDettaglioBolla(Integer codFiegDl, Integer codEdicola, Integer idtn, Timestamp dtBolla) {
		return repository.getMancanzeDettaglioBolla(codFiegDl, codEdicola, idtn, dtBolla);
	}
}
