package it.dpe.igeriv.bo.campagna;

import java.util.List;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.dto.CampagnaDto;
import it.dpe.igeriv.dto.CampagnaEdicoleDto;
import it.dpe.igeriv.vo.CampagnaEdicoleVo;
import it.dpe.igeriv.vo.CampagnaVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("CampagnaService")
public class CampagnaServiceImpl extends BaseServiceImpl implements CampagnaService  {

	private final CampagnaRepository repository;
	
	@Autowired
	CampagnaServiceImpl(CampagnaRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public List<CampagnaDto> getCampagnaIsActive() {
		return repository.getCampagnaIsActive();
				
	}

	@Override
	public List<CampagnaDto> getCampagnaByCodiceDl(Integer codDl) {
		return repository.getCampagnaByCodiceDl(codDl);
	}

	@Override
	public CampagnaVo getCampagnaByIdCamp(Integer idCamp) {
		return repository.getCampagnaByIdCamp(idCamp);
	}
	
	@Override
	public List<CampagnaEdicoleDto> getEdicoleByIdCampagna(Integer idCamp) {
		return repository.getEdicoleByIdCampagna(idCamp);
	}

	@Override
	public List<CampagnaEdicoleDto> getEdicoleByIdCampagnaStato(Integer idCamp,Integer flagStato) {
		return repository.getEdicoleByIdCampagnaStato(idCamp, flagStato);
	}
	
	@Override
	public CampagnaEdicoleVo getEdicoleByCrivw(Integer crivw) {
		return repository.getEdicoleByCrivw(crivw);
	}

	@Override
	public void updateCampagnaEdicole(CampagnaEdicoleVo campEdicole) {
		repository.updateCampagnaEdicole(campEdicole);
		
	}

	

	
}
