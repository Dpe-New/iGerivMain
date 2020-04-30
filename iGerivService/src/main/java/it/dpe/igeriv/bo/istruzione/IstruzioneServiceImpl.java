package it.dpe.igeriv.bo.istruzione;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.vo.IstruzioneVo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("IstruzioneService")
class IstruzioneServiceImpl extends BaseServiceImpl implements IstruzioneService {
	private final IstruzioneRepository repository;
	
	@Autowired
	IstruzioneServiceImpl(IstruzioneRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public List<KeyValueDto> getIstruzione() {
		return repository.getIstruzione();
	}

	@Override
	public IstruzioneVo getIstruzione(Integer codIstruzione) {
		return repository.getIstruzione(codIstruzione);
	}

	

}
