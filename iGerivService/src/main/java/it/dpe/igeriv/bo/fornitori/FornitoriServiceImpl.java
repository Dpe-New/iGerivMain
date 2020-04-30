package it.dpe.igeriv.bo.fornitori;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.vo.ProdottiNonEditorialiFornitoreVo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("FornitoriService")
class FornitoriServiceImpl extends BaseServiceImpl implements FornitoriService {
	private final FornitoriRepository repository;
	
	@Autowired
	FornitoriServiceImpl(FornitoriRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public void deleteFornitore(Integer codFornitore, Integer codEdicola) {
		repository.deleteFornitore(codFornitore, codEdicola);
	}

	@Override
	public List<ProdottiNonEditorialiFornitoreVo> getFornitoriEdicola(Integer codEdicola, String ragioneSociale, String piva, boolean excludeDl) {
		return repository.getFornitoriEdicola(codEdicola, ragioneSociale, piva, excludeDl);
	}

	@Override
	public ProdottiNonEditorialiFornitoreVo getFornitore(Integer codEdicola, Integer codFornitore) {
		return repository.getFornitore(codEdicola, codFornitore);
	}

	

}
