package it.dpe.igeriv.bo.rilevamenti;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.bo.messaggi.MessaggiService;
import it.dpe.igeriv.dto.RilevamentiDto;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.ControlloLetturaMessaggioVo;
import it.dpe.igeriv.vo.MessaggioVo;
import it.dpe.igeriv.vo.pk.ControlloLetturaMessaggioPk;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("RilevamentiService")
class RilevamentiServiceImpl extends BaseServiceImpl implements RilevamentiService {
	private final RilevamentiRepository rilevamentiRepository;
	private final MessaggiService messaggiService;
	
	@Autowired
	RilevamentiServiceImpl(RilevamentiRepository rilevamentiRepository, MessaggiService messaggiService) {
		super(rilevamentiRepository);
		this.rilevamentiRepository = rilevamentiRepository;
		this.messaggiService = messaggiService;
	}
	
	@Override
	public List<RilevamentiDto> getRilevamenti(Integer codFiegDl, Integer codEdicola) {
		return rilevamentiRepository.getRilevamenti(codFiegDl, codEdicola);
	}

	@Override
	public void updateRilevamenti(Map<Integer, Integer> mapRilevamenti, Integer codFiegDl, Integer codEdicola) {
		rilevamentiRepository.updateRilevamenti(mapRilevamenti);
		MessaggioVo msgRilevamenti = messaggiService.getMessaggioRilevamenti(codFiegDl, codEdicola);
		if (msgRilevamenti != null) {
			ControlloLetturaMessaggioVo vo = new ControlloLetturaMessaggioVo();
			ControlloLetturaMessaggioPk pk = new ControlloLetturaMessaggioPk();
			pk.setCodFiegDl(codFiegDl);
			pk.setCodiceEdicola(codEdicola);
			pk.setDtMessaggio(msgRilevamenti.getDtMessaggio());
			vo.setPk(pk);
			vo.setMessaggioLetto(IGerivConstants.COD_MESSAGGIO_LETTO);
			saveBaseVo(vo);
		}
	}

}
