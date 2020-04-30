package it.dpe.igeriv.bo.card;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.dto.VenditaDettaglioDto;
import it.dpe.igeriv.vo.IGerivCardVo;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("CardService")
class CardServiceImpl extends BaseServiceImpl implements CardService {
	private final CardRepository repository;
	
	@Autowired
	CardServiceImpl(CardRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public IGerivCardVo getIGerivCardVo(String inputText, Integer idEdicola, Long codCliente) {
		return repository.getIGerivCardVo(inputText, idEdicola, codCliente);
	}

	@Override
	public List<VenditaDettaglioDto> getUltimiAcquistiIGerivCard(String inputText, Integer idEdicola, Integer codFiegDl, Integer giorni, Long codCliente, Timestamp dataStorico) {
		return repository.getUltimiAcquistiIGerivCard(inputText, idEdicola, codFiegDl, giorni, codCliente, dataStorico);
	}

	@Override
	public List<VenditaDettaglioDto> getSuggerimentiVendita(String inputText, Integer idEdicola) {
		return repository.getSuggerimentiVendita(inputText, idEdicola);
	}

	@Override
	public void associaIGerivCard(String barcode, Long codCliente, Integer codEdicola, boolean byPassClienteCheck) throws Exception {
		repository.associaIGerivCard(barcode, codCliente, codEdicola, byPassClienteCheck);
	}

	

}
