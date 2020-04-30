package it.dpe.igeriv.bo.ws.epipoli;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.dto.ConsumaCodiceB2CResponse;
import it.dpe.igeriv.vo.RegistrazioneRisposteWSEpipoliVo;

@Service("EpipoliWebServices")
public class EpipoliWebServicesImpl extends BaseServiceImpl implements EpipoliWebServices {

	private final EpipoliWebServicesBo EpiWSBo;
	
	@Autowired
	EpipoliWebServicesImpl(EpipoliWebServicesBo EpiWSBo) {
		super(EpiWSBo);
		this.EpiWSBo = EpiWSBo;
	}

	@Override
	public ConsumaCodiceB2CResponse consumaCodiceB2C(String codiceEan, String valore, String idRichiesta,Integer codFiegDl,Integer codEdicola) {
		return EpiWSBo.consumaCodiceB2C(codiceEan,valore,idRichiesta,codFiegDl,codEdicola);
	}

	@Override
	public List<RegistrazioneRisposteWSEpipoliVo> findRispostaByIdRichiesta(Integer[] idRichiesta, Integer codFiegDl,
			Integer codEdicola) {
		return EpiWSBo.findRispostaByIdRichiesta(idRichiesta, codFiegDl, codEdicola);
	}

}

