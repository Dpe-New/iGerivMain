package it.dpe.igeriv.bo.account;

import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.dto.CodDlEdicoleSecondarieDto;
import it.dpe.igeriv.dto.UserDto;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.PianoProfiliEdicolaVo;
import it.dpe.igeriv.vo.UserAdminVo;
import it.dpe.igeriv.vo.UserVo;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


/**
 * Interfaccia account
 * 
 * @author mromano
 *
 */
public interface AccountService extends BaseService {
	
	/**
	 * @param codEdicola
	 * @return
	 */
	public UserVo getEdicolaByCodice(String codEdicola);
	
	/**
	 * @param codEdicola
	 * @return
	 */
	public UserVo getEdicolaByEmail(String email);
	
	/**
	 * @param codEdicola
	 * @return
	 */
	public UserVo getEdicolaByCodiceEdicola(Integer codEdicola);
	
	/**
	 * @param email
	 * @return
	 */
	public UserVo getSottoUtenteEdicolaByEmail(String email);
	
	/**
	 * @param codUtente
	 * @return
	 */
	public UserVo getUtenteEdicola(String codUtente);
	
	/**
	 * @param idGruppo
	 * @return
	 */
	public List<UserVo> getEdicolaByCodiceGruppo(Integer idGruppo);
	
	/**
	 * @param codEdicola
	 * @param isAdmin 
	 * @param codUtente 
	 * @param nomeUtente 
	 * @param codUtenteLoggato 
	 * @return
	 */
	public List<UserDto> getUtentiEdicola(Integer codEdicola, Boolean isAdmin, String codUtente, String nomeUtente, String codUtenteLoggato);
	
	/**
	 * @param header
	 */
	public void updateUserAgent(String codUtente, String header);

	/**
	 * @param codUtente
	 * @param codEdicolaDl
	 * @param header
	 */
	public void updateClientUserAgent(Long codUtente, Integer codEdicolaDl, String header);
	
	/**
	 * @param codCliente
	 * @return
	 */
	public UserAdminVo getUserAdminVo(String codUtente, boolean getModuli);
	
	/**
	 * @param email
	 * @return
	 */
	public UserAdminVo getUserAdminVo(String email);
	
	/**
	 * @param codFiegDlSecondario
	 * @param codEdicolaDl
	 * @param codDpeWebEdicolaMaster
	 * @return
	 */
	public Integer getCodDpeWebEdicolaSecondaria(Integer codFiegDlSecondario, Integer codEdicolaDl);
	
	/**
	 * @param codDpeWebEdicolaMaster
	 * @return
	 */
	public List<CodDlEdicoleSecondarieDto> getListCodDlECodEdicolaSecondarie(Integer codDpeWebEdicolaMaster);
	
	/**
	 * @param utente
	 */
	public void saveDowngradeAccountToStarter(UserVo utente);
	
	
	/**
	 * Costruisce lo UserDetails da autenticare
	 *  
	 * @param userId
	 * @param utenteBase
	 * @return UserAbbonato
	 */
	public UserAbbonato  buildUserDetails(String userId, BaseVo utenteBase);
	
	/**
	 * Costruisce lo UserDetails del Dl da autenticare
	 * 
	 * @param userId
	 * @param utenteBase
	 * @return
	 */
	public UserAbbonato  buildDlUserDetails(String userId, BaseVo utenteBase);
	
	/**
	 * @param ab
	 * @param dtSospensioneEdicolaStarter
	 * @param codUtente
	 * @param codDl
	 */
	public void saveUpgradeAccountToIGerivBaseAdmin(AbbinamentoEdicolaDlVo ab, Timestamp dtSospensioneEdicolaStarter, String codUtente, Integer codDl);
	
	
	/**
	 * @param dtAttivazioneEdicola
	 * @param dtSospensioneEdicola
	 * 
	 */
	public void saveDtInserimentoDtSospensioneEdicola(Integer codFiegDl,Integer codDpeWebEdicola,Timestamp dtAttivazioneEdicola,Timestamp dtSospensioneEdicola);
	
	/**
	 * 
	 * @param profiloEdicola
	 */
	public void saveProfilazioneEdicola(PianoProfiliEdicolaVo profiloEdicola);
	
	/**
	 * 
	 * @param codFiegDl
	 * @param codDpeWebEdicola
	 * @param dtAttivazioneProfiloEdicola
	 */
	public void deleteProfilazioneEdicola(Integer codFiegDl,Integer codDpeWebEdicola,Timestamp dtAttivazioneProfiloEdicola);
	
}
