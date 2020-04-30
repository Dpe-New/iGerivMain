package it.dpe.igeriv.bo.ws;

import it.dpe.igeriv.bo.BaseRepository;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.ClientCompatibilitaVersioniVo;
import it.dpe.igeriv.vo.DlGruppoModuliVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.RichiestaDatiWSVo;
import it.dpe.igeriv.vo.UtenteAgenziaVo;

import java.sql.Timestamp;
import java.util.List;

/**
 * Interfaccia business per iGeriv.
 *  
 * @author romanom
 *
 */
public interface IGerivWSBo extends BaseRepository {
	
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
	 * @param seqClientiEdicola
	 * @return
	 */
	public Long getNextSeqVal(String seqClientiEdicola);
	
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
	 * @param codEdicola
	 * @param dataBolla
	 * @return
	 */
	public RichiestaDatiWSVo getRichiestaDatiWS(Integer codEdicola, Timestamp dataBolla);
	
	/**
	 * @param capDa
	 * @param capA
	 * @return
	 */
	public List<EdicolaDto> getEdicoleGeomappateByCap(Integer capDa, Integer capA);

	/**
	 * @param app
	 * @param clientVersion
	 * @return
	 */
	public ClientCompatibilitaVersioniVo getClientCompatibilitaVersione(String app, Float clientVersion);

}
