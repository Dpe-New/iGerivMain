package it.dpe.igeriv.bo.ws.epipoli;

import java.util.List;

import it.dpe.igeriv.bo.BaseRepository;
import it.dpe.igeriv.dto.ConsumaCodiceB2CResponse;
import it.dpe.igeriv.vo.RegistrazioneRisposteWSEpipoliVo;


public interface EpipoliWebServicesBo extends BaseRepository {

	public ConsumaCodiceB2CResponse consumaCodiceB2C(String codiceEan, String valore, String idRichiesta,Integer codFiegDl,Integer codEdicola);

	public List<RegistrazioneRisposteWSEpipoliVo> findRispostaByIdRichiesta(Integer[] idRichiesta, Integer codFiegDl,Integer codEdicola);
}
