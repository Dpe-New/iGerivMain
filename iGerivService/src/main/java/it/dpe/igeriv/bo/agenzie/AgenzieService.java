package it.dpe.igeriv.bo.agenzie;

import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.dto.AnagraficaAgenziaDto;
import it.dpe.igeriv.dto.EmailDlDto;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.DlVo;
import it.dpe.igeriv.vo.UtenteAgenziaVo;

import java.util.List;


/**
 * Interfaccia agenzie
 * 
 * @author mromano
 *
 */
public interface AgenzieService extends BaseService {
	
	/**
	 * @param codDpeWebDl
	 * @return
	 */
	public AnagraficaAgenziaVo getAgenziaByCodice(Integer codFiegDl);
	
	/**
	 * @param email
	 * @return
	 */
	public AnagraficaAgenziaVo getAgenziaByEmail(String email);
	
	/**
	 * @param codDpeWebDl
	 * @return
	 */
	public UtenteAgenziaVo getAgenziaByCodiceLogin(Integer codUtente);
	
	/**
	 * @return
	 */
	public List<AnagraficaAgenziaVo> getAgenzie();
	
	/**
	 * @return
	 */
	public List<AnagraficaAgenziaVo> getAgenzieConFatturazione();
	
	/**
	 * @return
	 */
	public List<AnagraficaAgenziaVo> getAgenzieInforiv();
	
	/**
	 * @return
	 */
	public List<AnagraficaAgenziaVo> getAgenzieModalitaLocaleFtpInforiv();
	
	/**
	 * @return
	 */
	public List<DlVo> getListAgenzie();

	/**
	 * @return
	 */
	public List<AnagraficaAgenziaDto> getListAgenzieDpe();
	
	/**
	 * @param codFiegDl
	 * @return
	 */
	public String getPasswordDl(Integer codFiegDl);
	
	/**
	 * @param codDl
	 * @param fileName
	 * @param tipo
	 */
	public void updateDataDownload(Integer codDl, String fileName, Integer tipo);
	
	/**
	 * @param codFiegDl
	 */
	public List<EmailDlDto> getEmailsDl(Integer codFiegDl);
	
	/**
	 * @param codDpeWebDl
	 * @return
	 */
	public AnagraficaAgenziaVo getAgenziaByCodiceDpeWeb(Integer codDpeWebDl);
	
	/**
	 * @param agenzia
	 */
	public void insertNewAgenzia(AnagraficaAgenziaVo agenzia);
	
	/**
	 * 
	 * @return
	 */
	public Long getNewCodDlWeb();
	
	/**
	 * 
	 * @return List<AnagraficaAgenziaVo>
	 */
	public List<AnagraficaAgenziaVo> getAgenzieModalitaInforiv();
	
	/**
	 * 
	 * @param codDl
	 * @return
	 */
	public Boolean isMod2Inforiv(Integer codDl);
	
}
