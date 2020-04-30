package it.dpe.igeriv.bo.ws;

import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.dto.BollaDettaglioDto;
import it.dpe.igeriv.dto.FondoBollaDettaglioDto;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.gdo.dto.GdoVenditaDto;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.ClientCompatibilitaVersioniVo;
import it.dpe.igeriv.vo.DlGruppoModuliVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.RichiestaDatiWSVo;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.igeriv.vo.UtenteAgenziaVo;
import it.dpe.igeriv.vo.VenditaVo;

import java.sql.Timestamp;
import java.util.List;

/**
 * Interfaccia business per iGeriv.
 *  
 * @author romanom
 *
 */
public interface IGerivWSService extends BaseService {
	
	/**
	 * @param arg0
	 * @return
	 */
	public UserVo getEdicolaByCodice(String codEdicola);
	
	/**
	 * @param arg0
	 * @return
	 */
	public UserVo getEdicolaByEmail(String email);
	
	/**
	 * @param codDpeWebDl
	 * @return
	 */
	public UtenteAgenziaVo getAgenziaByCodice(Integer codFiegDl);
	
	/**
	 * @param codiceDl
	 * @param codiceRivendita
	 */
	public AbbinamentoEdicolaDlVo getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(Integer codiceDl, Integer codiceRivendita);
	
	/**
	 * @param idGruppoMOduli
	 * @param codDl
	 * @return
	 */
	public DlGruppoModuliVo getDlGruppoModuliVo(Integer idGruppoModuli, Integer codDl);
	
	/**
	 * @param roleIgerivClienteEdicola
	 * @return
	 */
	public GruppoModuliVo getGruppoModuliByRole(String roleIgerivClienteEdicola);
	
	/**
	 * @param dataBolla
	 */
	public List<BollaDettaglioDto> getDettaglioBolla(Integer[] codFiegDl, Integer[] codEdicola, Timestamp dataBolla, boolean isMultiDl);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dataBolla
	 * @param tipoBolla
	 * @param showSoloRigheDaSpuntare
	 * @param showSoloRifornimenti
	 * @param isMultiDl
	 * @return
	 */
	public List<FondoBollaDettaglioDto> getDettagliFondoBolla(Integer[] codFiegDl, Integer[] codEdicola, Timestamp dataBolla, String tipoBolla, boolean showSoloRigheDaSpuntare, boolean showSoloRifornimenti, boolean isMultiDl);
	
	/**
	 * Costruisce lo UserDetails da autenticare
	 *  
	 * @param userId
	 * @param utenteBase
	 * @return UserAbbonato
	 */
	public UserAbbonato  buildUserDetails(String userId, BaseVo utenteBase);

	/**
	 * Importa i dati di vendita che provengono da TGS
	 * 
	 * @param UserAbbonato user
	 * @param List<GdoVenditaDto> list
	 * @return VenditaVo
	 */
	public VenditaVo importDatiVenditeGdo(UserAbbonato user, List<GdoVenditaDto> list, List<String> errori) throws IGerivBusinessException;
	
	/**
	 * @param codEdicola
	 * @param dataBolla
	 * @return
	 */
	public RichiestaDatiWSVo getRichiestaDatiWS(Integer codEdicola, Timestamp dataBolla);
	
	/**
	 * @param app
	 * @param clientVersion
	 * @return
	 */
	public ClientCompatibilitaVersioniVo getClientCompatibilitaVersione(String app, Float clientVersion);

}
