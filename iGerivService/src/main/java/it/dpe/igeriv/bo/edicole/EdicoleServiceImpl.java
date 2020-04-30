package it.dpe.igeriv.bo.edicole;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.bo.messaggi.MessaggiService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.dto.EstrattoContoDto;
import it.dpe.igeriv.dto.GiroDto;
import it.dpe.igeriv.dto.GiroTipoDto;
import it.dpe.igeriv.dto.ParametriEdicolaDto;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.AnagraficaEdicolaAggiuntiviVo;
import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.igeriv.vo.BonusInvitaUnCollegaVo;
import it.dpe.igeriv.vo.EdicolaDlVo;
import it.dpe.igeriv.vo.EmailRivenditaVo;
import it.dpe.igeriv.vo.ImmagineAnagraficaEdicolaVo;
import it.dpe.igeriv.vo.ParametriEdicolaVo;
import it.dpe.igeriv.vo.PianoProfiliEdicolaVo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("EdicoleService")
class EdicoleServiceImpl extends BaseServiceImpl implements EdicoleService {
	private final EdicoleRepository repository;
	private final MessaggiService messaggiService;
	private final PubblicazioniService pubblicazioniService;
	
	@Autowired
	EdicoleServiceImpl(EdicoleRepository repository, MessaggiService messaggiService, PubblicazioniService pubblicazioniService) {
		super(repository);
		this.repository = repository;
		this.messaggiService = messaggiService;
		this.pubblicazioniService = pubblicazioniService;
	}

	@Override
	public List<EdicolaDto> getEdicole(Integer codDl, Integer codEdicolaWeb, Integer codEdicolaDl, String ragioneSociale) {
		return repository.getEdicole(codDl, codEdicolaWeb, codEdicolaDl, ragioneSociale);
	}

	@Override
	public List<EdicolaDto> getEdicole(Integer codDl, Integer idProfiloDL, Integer codEdicolaWeb, Integer codEdicolaDl, String ragioneSociale) {
		return repository.getEdicole(codDl, idProfiloDL, codEdicolaWeb, codEdicolaDl, ragioneSociale);
	}
	
	@Override
	public List<EdicolaDto> getEdicole(Integer codDl, Integer codEdicolaWeb, Integer codEdicolaDl, String ragioneSociale, boolean soloUtentiAmministratori) {
		return repository.getEdicole(codDl, codEdicolaWeb, codEdicolaDl, ragioneSociale, soloUtentiAmministratori);
	}
	
	@Override
	public List<EdicolaDto> getEdicoleInforivDl(Integer codDl, Integer codEdicolaWeb, Integer codEdicolaDl, String ragioneSociale, boolean utenteAmministratore) {
		return repository.getEdicoleInforivDl(codDl, codEdicolaWeb, codEdicolaDl, ragioneSociale, utenteAmministratore);
	}

	@Override
	public AbbinamentoEdicolaDlVo getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(Integer codiceDl, Integer codiceRivendita) {
		return repository.getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(codiceDl, codiceRivendita);
	}
	
	@Override
	public AnagraficaEdicolaVo getEdicolaByCodFiegDlCodRivDl(Integer codiceDl, Integer codiceRivendita) {
		return repository.getEdicolaByCodFiegDlCodRivDl(codiceDl, codiceRivendita);
	}
	
	@Override
	public AnagraficaEdicolaAggiuntiviVo getAnagraficaEdicolaAggiuntiviVoByCodEdicolaWeb(Integer codiceRivenditaWeb) {
		return repository.getAnagraficaEdicolaAggiuntiviVoByCodEdicolaWeb(codiceRivenditaWeb);
	}

	@Override
	public Integer getCodDpeWebEdicolaAbbinamentoEdicolaDlVo(Integer codiceRivenditaDl, Integer codFiegDl) {
		return repository.getCodDpeWebEdicolaAbbinamentoEdicolaDlVo(codiceRivenditaDl, codFiegDl);
	}

	@Override
	public void updateDataSospensioneEdicole(Integer codDl, String codEdicole, String dateSospensione, Boolean checkConsegneGazzetta) {
		repository.updateDataSospensioneEdicole(codDl, codEdicole, dateSospensione, checkConsegneGazzetta);
	}

	@Override
	public void updatePwdEdicola(Integer codEdicolaDl, String password, boolean changePassword, boolean isPwdCripata) {
		repository.updatePwdUserEdicola(codEdicolaDl, password, changePassword, isPwdCripata);
	}
	
	@Override
	public AnagraficaEdicolaVo getAnagraficaEdicola(Integer codEdicola) {
		return repository.getAnagraficaEdicola(codEdicola);
	}
	
	@Override
	public void updateDatiEdicole(Integer codUtente, String pk, String dataInserimento, String dataSospensione, String profilo, String edicolaTest, String edicolaPromoDtIni, String edicolaPromoDtFin, String edicolaPlusDtIni, String edicolaPlusDtFin) {
		repository.updateDatiEdicole(codUtente, pk, dataInserimento, dataSospensione, profilo, edicolaTest, edicolaPromoDtIni, edicolaPromoDtFin, edicolaPlusDtIni, edicolaPlusDtFin);
	}
	
	@Override
	public AbbinamentoEdicolaDlVo getAbbinamentoEdicolaDlVoByCodEdicolaWeb(Integer codEdicola) {
		return repository.getAbbinamentoEdicolaDlVoByCodEdicolaWeb(codEdicola);
	}
	
	@Override
	public List<ParametriEdicolaDto> getParametriEdicola(Integer codEdicola) {
		return repository.getParametriEdicola(codEdicola);
	}
	
	@Override
	public ParametriEdicolaVo getParametroEdicola(Integer codEdicola, Integer codParametro) {
		return repository.getParametroEdicola(codEdicola, codParametro);
	}
	
	@Override
	public List<AbbinamentoEdicolaDlVo> getEdicoleNonSospese() {
		return repository.getEdicoleNonSospese();
	}
	
	@Override
	public List<AbbinamentoEdicolaDlVo> getEdicoleNonSospese(Integer coddl) {
		return repository.getEdicoleNonSospese(coddl);
	}
	
	@Override
	public List<AbbinamentoEdicolaDlVo> getEdicoleAutorizzateAggiornamentoBarcode(Integer coddl) {
		return repository.getEdicoleAutorizzateAggiornamentoBarcode(coddl);
	}
	
	@Override
	public EdicolaDlVo getEdicolaDl(Integer codEdicolaDl, Integer codDl) {
		return repository.getEdicolaDl(codEdicolaDl, codDl);
	}
	
	@Override
	public List<EdicolaDlVo> getEdicoleDl(Integer coddl, Integer codEdicola, String ragSoc) {
		return repository.getEdicoleDl(coddl, codEdicola, ragSoc);
	}
	
	@Override
	public BonusInvitaUnCollegaVo getBonusInvitaUnCollega(Integer codEdicola, Integer codDl) {
		return repository.getBonusInvitaUnCollega(codEdicola, codDl);
	}
	
	@Override
	public Boolean getEmailValidoMultiDl(Integer[] arrId, Integer[] arrCodFiegDl) {
		return repository.getEmailValidoMultiDl(arrId, arrCodFiegDl);
	}
	
	@Override
	public EstrattoContoDto getEstrattoContoEdicolaPdf(Integer coddl, Integer codEdicola, Date dataEC) {
		return repository.getEstrattoContoEdicolaPdf(coddl, codEdicola, dataEC);
	}
	
	@Override
	public EmailRivenditaVo getEmailRivenditaVo(Integer idEmailRivendita) {
		return repository.getEmailRivenditaVo(idEmailRivendita);
	}

	@Override
	public List<GiroDto> getGiri(Integer codEdicola, Integer giroTipo) {
		return messaggiService.getGiri(codEdicola, giroTipo);
	}

	@Override
	public List<GiroDto> getZone(Integer codEdicola, Integer zonaTipo) {
		return messaggiService.getZone(codEdicola, zonaTipo);
	}

	@Override
	public List<GiroTipoDto> getGiriTipo(Integer codFiegDl, Integer codEdicola, Integer codice, String desc) {
		return messaggiService.getGiriTipo(codFiegDl, codEdicola, codice, desc);
	}

	@Override
	public List<GiroTipoDto> getZoneTipo(Integer codFiegDl, Integer codEdicola, Integer codice, String desc) {
		return messaggiService.getZoneTipo(codFiegDl, codEdicola, codice, desc);
	}
	
	@Override
	public ImmagineAnagraficaEdicolaVo getImmagineAnagraficaEdicola(Integer codEdicola, Integer progressivo) {
		return repository.getImmagineAnagraficaEdicola(codEdicola, progressivo);
	}
	
	@Override
	public void saveAnagraficaEdicola(AnagraficaEdicolaAggiuntiviVo anagraficaEdicolaAggiuntiviVo, List<ImmagineAnagraficaEdicolaVo> listImg) {
		repository.saveBaseVo(anagraficaEdicolaAggiuntiviVo);
		if (listImg != null && !listImg.isEmpty()) {
			repository.saveVoList(listImg);
		}
	}
	
	@Override
	public List<EdicolaDto> getNuoveEdicole(Integer codFiegDl, java.util.Date dataCreazione) {
		return repository.getNuoveEdicole(codFiegDl, dataCreazione);
	}
	
	@Override
	public List<EdicolaDto> getEdicoleByIdtn(Integer codFiegDl, Integer idtn) {
		return repository.getEdicoleByIdtn(codFiegDl, idtn);
	}
	
	@Override
	public List<AbbinamentoEdicolaDlVo> getEdicoleConPubblicazioneInGiacenza(Integer coddl, Integer idtn) {
		List<AbbinamentoEdicolaDlVo> listEdicole = new ArrayList<>();
		for (AbbinamentoEdicolaDlVo edicola : getEdicoleNonSospese(coddl)) {
			if (pubblicazioniService.getGiacenza(edicola.getAnagraficaAgenziaVo().getCodFiegDl(), edicola.getCodDpeWebEdicola(), idtn, edicola.getDataCreazioneStatistica()) > 0l) {
				listEdicole.add(edicola);
			}
		}
		return listEdicole;
	}
	
	@Override
	public List<AbbinamentoEdicolaDlVo> getEdicoleInforivDlByCodEdicolaWebMaster(Integer codEdicolaMaster) {
		return repository.getEdicoleInforivDlByCodEdicolaWebMaster(codEdicolaMaster);
	}
	
	@Override
	public List<EdicolaDto> getEdicoleInArea(AnagraficaEdicolaVo edicola, Integer kmArea){
		return repository.getEdicoleInArea(edicola, kmArea);
	}
		
	@Override
	public List<EdicolaDto> getEdicoleDlAttive(Integer codFiegDl){
		return repository.getEdicoleDlAttive(codFiegDl);
	}

	@Override
	public List<PianoProfiliEdicolaVo> getPianoProfiliEdicola(Integer codEdicolaWeb, Integer codDl) {
		return repository.getPianoProfiliEdicola(codEdicolaWeb,codDl);
	}

	@Override
	public PianoProfiliEdicolaVo getPianoProfiloEdicolaAttivo(Integer codEdicolaWeb, Integer codDl) {
		return repository.getPianoProfiloEdicolaAttivo(codEdicolaWeb,codDl);
	}
	
	
	
	
}
