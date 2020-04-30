package it.dpe.igeriv.bo.account;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.bo.menu.MenuService;
import it.dpe.igeriv.dto.CodDlEdicoleSecondarieDto;
import it.dpe.igeriv.dto.UserDto;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.DlGruppoModuliVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.PianoProfiliEdicolaVo;
import it.dpe.igeriv.vo.UserAdminVo;
import it.dpe.igeriv.vo.UserVo;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("AccountService")
class AccountServiceImpl extends BaseServiceImpl implements AccountService {
	private final AccountRepository repository;
	private final MenuService menuService;
	
	@Autowired
	AccountServiceImpl(AccountRepository repository, MenuService menuService) {
		super(repository);
		this.repository = repository;
		this.menuService = menuService;
	}

	@Override
	public void updateUserAgent(String codUtente, String header) {
		repository.updateUserAgent(codUtente, header);
	}

	@Override
	public void updateClientUserAgent(Long codUtente, Integer codEdicolaDl, String header) {
		repository.updateClientUserAgent(codUtente, codEdicolaDl, header);
	}

	@Override
	public UserAdminVo getUserAdminVo(String codUtente, boolean getModuli) {
		return repository.getUserAdminVo(codUtente, getModuli);
	}

	@Override
	public UserAdminVo getUserAdminVo(String email) {
		return repository.getUserAdminVo(email);
	}
	
	@Override
	public Integer getCodDpeWebEdicolaSecondaria(Integer codFiegDlSecondario, Integer codEdicolaDl) {
		return repository.getCodDpeWebEdicolaSecondaria(codFiegDlSecondario, codEdicolaDl);
	}
	
	@Override
	public List<CodDlEdicoleSecondarieDto> getListCodDlECodEdicolaSecondarie(Integer codDpeWebEdicolaMaster) {
		return repository.getListCodDlECodEdicolaSecondarie(codDpeWebEdicolaMaster);
	}

	@Override
	public void saveDowngradeAccountToStarter(UserVo utente) {
		repository.saveDowngradeAccountToStarter(utente);
	}

	@Override
	public UserAbbonato buildUserDetails(String userId, BaseVo utenteBase) {
		return repository.buildUserDetails(userId, utenteBase);
	}
	
	@Override
	public UserAbbonato buildDlUserDetails(String userId, BaseVo utenteBase) {
		return repository.buildDlUserDetails(userId, utenteBase);
	}

	@Override
	public UserVo getEdicolaByCodice(String codEdicola) {
		return repository.getEdicolaByCodice(codEdicola);
	}

	@Override
	public UserVo getEdicolaByEmail(String email) {
		return repository.getEdicolaByEmail(email);
	}

	@Override
	public UserVo getEdicolaByCodiceEdicola(Integer codEdicola) {
		return repository.getEdicolaByCodiceEdicola(codEdicola);
	}

	@Override
	public UserVo getSottoUtenteEdicolaByEmail(String email) {
		return repository.getSottoUtenteEdicolaByEmail(email);
	}

	@Override
	public UserVo getUtenteEdicola(String codUtente) {
		return repository.getUtenteEdicola(codUtente);
	}
	
	@Override
	public List<UserVo> getEdicolaByCodiceGruppo(Integer idGruppo) {
		return repository.getEdicolaByCodiceGruppo(idGruppo);
	}

	@Override
	public List<UserDto> getUtentiEdicola(Integer codEdicola, Boolean isAdmin, String codUtente, String nomeUtente, String codUtenteLoggato) {
		return repository.getUtentiEdicola(codEdicola, isAdmin, codUtente, nomeUtente, codUtenteLoggato);
	}
	
	@Override
	public void saveUpgradeAccountToIGerivBaseAdmin(AbbinamentoEdicolaDlVo ab, Timestamp dtSospensioneEdicolaStarter, String codUtente, Integer codDl) {
		UserVo utenteEdicola = getUtenteEdicola(codUtente);
		utenteEdicola.setUtenteAmministratore(1);
		ab.setDtSospensioneEdicola(dtSospensioneEdicolaStarter);
		ab.setRichiestaProva(true);
		GruppoModuliVo gruppoModuliAdmin = menuService.getGruppoModuliByRole(IGerivConstants.ROLE_IGERIV_BASE_ADMIN);
		DlGruppoModuliVo gruppoModuliByRole = menuService.getDlGruppoModuliVo(gruppoModuliAdmin.getId(), codDl);
		utenteEdicola.setDlGruppoModuliVo(gruppoModuliByRole);
		saveBaseVo(ab);
		saveBaseVo(utenteEdicola);
	}

	@Override
	public void saveDtInserimentoDtSospensioneEdicola(Integer codFiegDl, Integer codDpeWebEdicola,
			Timestamp dtAttivazioneEdicola, Timestamp dtSospensioneEdicola) {
		repository.saveDtInserimentoDtSospensioneEdicola(codFiegDl, codDpeWebEdicola, dtAttivazioneEdicola, dtSospensioneEdicola);
	}

	@Override
	public void saveProfilazioneEdicola(PianoProfiliEdicolaVo profiloEdicola) {
		repository.saveProfilazioneEdicola(profiloEdicola);
	}

	@Override
	public void deleteProfilazioneEdicola(Integer codFiegDl, Integer codDpeWebEdicola,
			Timestamp dtAttivazioneProfiloEdicola) {
		repository.deleteProfilazioneEdicola(codFiegDl, codDpeWebEdicola, dtAttivazioneProfiloEdicola);
		
	}

	
	
	
	
	
}
