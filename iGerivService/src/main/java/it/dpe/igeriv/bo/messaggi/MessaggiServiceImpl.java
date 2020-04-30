package it.dpe.igeriv.bo.messaggi;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.dto.AvvisoMessaggioDto;
import it.dpe.igeriv.dto.ConfermaLetturaMessaggioDto;
import it.dpe.igeriv.dto.EmailRivenditaDto;
import it.dpe.igeriv.dto.GiroDto;
import it.dpe.igeriv.dto.GiroTipoDto;
import it.dpe.igeriv.dto.MessaggioDpeDto;
import it.dpe.igeriv.dto.MessaggioDto;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.MessaggiBollaVo;
import it.dpe.igeriv.vo.MessaggioClienteVo;
import it.dpe.igeriv.vo.MessaggioDpeVo;
import it.dpe.igeriv.vo.MessaggioIdtnVo;
import it.dpe.igeriv.vo.MessaggioVo;
import it.dpe.igeriv.vo.pk.MessaggioPk;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("MessaggiService")
class MessaggiServiceImpl extends BaseServiceImpl implements MessaggiService {
	private final MessaggiRepository repository;
	private final AgenzieService agenzieService;
	
	@Autowired
	MessaggiServiceImpl(MessaggiRepository repository, AgenzieService agenzieService) {
		super(repository);
		this.repository = repository;
		this.agenzieService = agenzieService;
	}

	@Override
	public List<MessaggiBollaVo> getMessaggiBolla(Integer[] codFiegDl, Integer[] codEdicolaDl, Timestamp dtBolla, String tipo) {
		return repository.getMessaggiBolla(codFiegDl, codEdicolaDl, dtBolla, tipo);
	}

	@Override
	public List<MessaggiBollaVo> getMessaggiBollaEdicola(Integer codFiegDl, Integer codEdicolaDl, Timestamp dtBolla, String tipo) {
		return repository.getMessaggiBollaEdicola(codFiegDl, codEdicolaDl, dtBolla, tipo);
	}

	@Override
	public List<MessaggioDto> getMessaggiRivendite(Integer[] codDl, Timestamp dtMessaggioDa, Timestamp dtMessaggioA, Integer[] codEdicola, boolean showMessaggioLetto) {
		return repository.getMessaggiRivendite(codDl, dtMessaggioDa, dtMessaggioA, codEdicola, showMessaggioLetto);
	}

	@Override
	public List<ConfermaLetturaMessaggioDto> getConfermeLettura(Integer codEdicola, Integer codFiegDl, Timestamp dtMessaggio, Integer tipoDestinatario, Integer destinatarioA, Integer destinatarioB) {
		return repository.getConfermeLettura(codEdicola, codFiegDl, dtMessaggio, tipoDestinatario, destinatarioA, destinatarioB);
	}

	@Override
	public MessaggioDto getMessaggioRivendita(Integer codEdicola, Integer codFiegDl, Timestamp dtMessaggio, Integer tipoDestinatario, Integer destinatarioA, Integer destinatarioB) {
		return repository.getMessaggioRivendita(codEdicola, codFiegDl, dtMessaggio, tipoDestinatario, destinatarioA, destinatarioB);
	}

	@Override
	public MessaggioVo getMessaggioRivenditaVo(Integer codFiegDl, Timestamp dtMessaggio, Integer tipoDestinatario, Integer destinatarioA, Integer destinatarioB) {
		return repository.getMessaggioRivenditaVo(codFiegDl, dtMessaggio, tipoDestinatario, destinatarioA, destinatarioB);
	}
	
	@Override
	public AvvisoMessaggioDto getPkMessaggioNonLetto(Integer codEdicola, Integer codDl, List<Integer> giroZonaTipo, List<Integer> giriZone, Integer tipoMessaggio, Timestamp dtAttivazioneEdicola) {
		return repository.getPkMessaggioNonLetto(codEdicola, codDl, giroZonaTipo, giriZone, tipoMessaggio, dtAttivazioneEdicola);
	}
	
	@Override
	public List<EmailRivenditaDto> getEmailInviatiDaRivendita(Integer codRivendita, Timestamp dataDa, Timestamp dataA) {
		return repository.getEmailInviatiDaRivendita(codRivendita, dataDa, dataA);
	}

	@Override
	public EmailRivenditaDto getMessaggioInviatoDaRivendita(Integer idEmailRivendita) {
		return repository.getMessaggioInviatoDaRivendita(idEmailRivendita);
	}

	@Override
	public List<GiroDto> getGiri(Integer codEdicola, Integer giroTipo) {
		return repository.getGiri(codEdicola, giroTipo);
	}

	@Override
	public List<GiroDto> getZone(Integer codEdicola, Integer zonaTipo) {
		return repository.getZone(codEdicola, zonaTipo);
	}

	@Override
	public List<GiroTipoDto> getGiriTipo(Integer codFiegDl, Integer codEdicola, Integer codice, String desc) {
		return repository.getGiriTipo(codFiegDl, codEdicola, codice, desc);
	}

	@Override
	public List<GiroTipoDto> getZoneTipo(Integer codFiegDl, Integer codEdicola, Integer codice, String desc) {
		return repository.getZoneTipo(codFiegDl, codEdicola, codice, desc);
	}

	@Override
	public List<MessaggioDpeDto> getMessaggiDpe(Timestamp dataDa, Timestamp dataA, Boolean abilitati) {
		return repository.getMessaggiDpe(dataDa, dataA, abilitati);
	}

	@Override
	public MessaggioDpeDto getMessaggioDpe(Long codMessaggio) {
		return repository.getMessaggioDpe(codMessaggio);
	}

	@Override
	public MessaggioDpeVo getMessaggioDpeVo(Long codice) {
		return repository.getMessaggioDpeVo(codice);
	}
	
	@Override
	public Boolean hasMessaggiDpe() {
		return repository.hasMessaggiDpe();
	}
	
	@Override
	public void saveMessaggioDpe(MessaggioDpeDto messaggio) {
		MessaggioDpeVo vo = (messaggio.getCodice() != null) ? getMessaggioDpeVo(messaggio.getCodice()) : null;
		if (vo == null) {
			vo = new MessaggioDpeVo();
		}
		vo.setAbilitato(messaggio.getAbilitato());
		vo.setData(messaggio.getData());
		vo.setPriorita(messaggio.getPriorita());
		vo.setTitolo(messaggio.getTitolo());
		vo.setTesto(messaggio.getTesto());
		vo.setUrl(messaggio.getUrl());
		saveBaseVo(vo);
	}
	
	@Override
	public void deleteMessaggioDpe(MessaggioDpeDto messaggio) {
		MessaggioDpeVo vo = getMessaggioDpeVo(messaggio.getCodice());
		deleteVo(vo);
	}
	
	@Override
	public void deleteMessaggioRivendita(MessaggioDto messaggio) {
		List<AnagraficaAgenziaVo> agenzie = null;
		Integer codEdicola = 0;
		if (messaggio.getTipoDestinatario() != null && messaggio.getTipoDestinatario().equals(1)) {
			agenzie = new ArrayList<AnagraficaAgenziaVo>();
			agenzie.add((AnagraficaAgenziaVo) agenzieService.getAgenziaByCodice(messaggio.getCodFiegDl()));
			codEdicola = messaggio.getDestinatarioA();
		} else {
			agenzie = agenzieService.getAgenzie();
		}
		for (BaseVo bvo : agenzie) {
			AnagraficaAgenziaVo agvo = (AnagraficaAgenziaVo) bvo;
			MessaggioVo vo = repository.getMessaggioRivenditaVo(agvo.getCodFiegDl(), messaggio.getDtMessaggio(), messaggio.getTipoDestinatario(), codEdicola, 0);
			deleteVo(vo);
		}
	}
	
	@Override
	public void saveMessaggioRivenditaDpe(MessaggioDto messaggio) {
		List<MessaggioVo> list = new ArrayList<MessaggioVo>();
		Timestamp dtMessaggio = messaggio.getDtMessaggio() == null ? getSysdate() : messaggio.getDtMessaggio();
		Integer codEdicola = 0;
		List<AnagraficaAgenziaVo> agenzie = null;
		if (messaggio.getTipoDestinatario().equals(1)) {
			agenzie = new ArrayList<AnagraficaAgenziaVo>();
			agenzie.add((AnagraficaAgenziaVo) agenzieService.getAgenziaByCodice(messaggio.getCodFiegDl()));
			codEdicola = messaggio.getDestinatarioA();
		} else {
			agenzie = agenzieService.getAgenzie();
		}
		for (BaseVo bvo : agenzie) {
			AnagraficaAgenziaVo agvo = (AnagraficaAgenziaVo) bvo;
			if (agvo.getEdicoleVedonoMessaggiDpe()) {
				MessaggioVo vo = new MessaggioVo();
				MessaggioPk pk = new MessaggioPk();
				pk.setCodFiegDl(agvo.getCodFiegDl());
				pk.setDestinatarioA(codEdicola);
				pk.setDestinatarioB(0);
				pk.setDtMessaggio(dtMessaggio);
				pk.setTipoDestinatario(messaggio.getTipoDestinatario());
				vo.setPk(pk);
				vo.setEdicolaLabel(messaggio.getEdicolaLabel());
				vo.setMessaggio(messaggio.getMessaggio());
				vo.setStatoMessaggio(messaggio.getStatoMessaggio());
				vo.setTipoMessaggio(messaggio.getTipoMessaggio());
				vo.setAttachmentName1(messaggio.getAttachmentName1());
				list.add(vo);
			}
		}
		saveVoList(list);
	}
	
	@Override
	public MessaggioIdtnVo getMessaggioIdtnVo(Integer codDl, Integer idtn) {
		return repository.getMessaggioIdtnVo(codDl, idtn);
	}

	@Override
	public MessaggioIdtnVo getMessaggioIdtnVo(Integer idMessaggioIdtn) {
		return repository.getMessaggioIdtnVo(idMessaggioIdtn);
	}

	@Override
	public Timestamp getMaxSecondsDateMessaggio(Integer codFiegDl, Timestamp dtMessaggio) {
		return repository.getMaxSecondsDateMessaggio(codFiegDl, dtMessaggio);
	}
	
	@Override
	public MessaggioClienteVo getMessaggioCliente(Long codMessaggio) {
		return repository.getMessaggioCliente(codMessaggio);
	}
	
	@Override
	public List<MessaggioClienteVo> getMessaggiCliente(Integer codEdicola, Date dataDa, Date dataA, String nome, String cognome, String codFiscale, String piva) {
		return repository.getMessaggioCliente(codEdicola, dataDa, dataA, nome, cognome, codFiscale, piva);
	}
	
	@Override
	public MessaggioVo getMessaggioRilevamenti(Integer codFiegDl, Integer codEdicola) {
		return repository.getMessaggioRilevamenti(codFiegDl, codEdicola);
	}
}
