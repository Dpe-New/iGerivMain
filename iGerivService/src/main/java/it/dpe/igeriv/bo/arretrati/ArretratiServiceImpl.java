package it.dpe.igeriv.bo.arretrati;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.dto.ArretratiDto;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ArretratiService")
class ArretratiServiceImpl extends BaseServiceImpl implements ArretratiService<ArretratiDto> {
	private final ArretratiRepository<ArretratiDto> repository;
	
	@Autowired
	ArretratiServiceImpl(ArretratiRepository<ArretratiDto> repository) {
		super(repository);
		this.repository = repository;
	}
	
	@Override
	public List<ArretratiDto> getArretrati(Integer codFiegDl, Integer codEdicola) {
		return repository.getArretrati(codFiegDl, codEdicola);
	}
	
	@Override
	public List<ArretratiDto> getReportArretrati(Integer codFiegDl, Integer codEdicola) {
		return repository.getReportArretrati(codFiegDl, codEdicola);
	}

	@Override
	public void updateArretrati(Map<Integer, Integer> mapArretrati, Map<Integer, String> mapNoteArretrato) {
		repository.updateArretrati(mapArretrati, mapNoteArretrato);
	}

}
