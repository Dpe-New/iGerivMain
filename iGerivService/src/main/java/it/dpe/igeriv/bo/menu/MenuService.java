package it.dpe.igeriv.bo.menu;

import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.dto.MenuDto;
import it.dpe.igeriv.dto.ProfiloDto;
import it.dpe.igeriv.vo.AssociazioneGruppoModuliVo;
import it.dpe.igeriv.vo.DlGruppoModuliVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.MenuModuloVo;
import it.dpe.igeriv.vo.ModuloInputVo;

import java.util.List;

/**
 * Interfaccia menu
 * 
 * @author mromano
 *
 */
public interface MenuService extends BaseService {
	
	/**
	 * @param idModulo
	 * @return
	 */
	public List<MenuModuloVo> getModuliByIdGruppo(Integer idModulo);
	
	/**
	 * @param <T>
	 * @param roleName
	 * @return
	 */
	public List<MenuModuloVo> getModuliByRoleName(String roleName);
	
	/**
	 * @param <T>
	 * @param moduliSelected
	 * @return
	 */
	public List<MenuModuloVo> getModuliByIds(final List<Integer> moduliSelected);
	
	/**
	 * @return
	 */
	public List<GruppoModuliVo> getGruppoModuliCustomizzabili();
	
	/**
	 * @param codDl
	 * @return
	 */
	public List<DlGruppoModuliVo> getListDlGruppoModuliVo(Integer codDl, boolean soloModuliConfigurabiliDl);
	
	/**
	 * @param <T>
	 * @param idModulo
	 * @return
	 */
	public GruppoModuliVo getGruppoModuliById(Integer idModulo);
	
	/**
	 * @param userType
	 * @return
	 */
	public List<GruppoModuliVo> getGruppoModuliByUserType(Integer userType);
	
	/**
	 * @param <T>
	 * @return
	 */
	public List<ModuloInputVo> getModuliInput();
	
	/**
	 * @param roleIgerivClienteEdicola
	 * @return
	 */
	public GruppoModuliVo getGruppoModuliByRole(String roleIgerivClienteEdicola);
	
	/**
	 * @return
	 */
	public List<ProfiloDto> getProfili();

	/**
	 * @param idProfilo
	 * @return
	 */
	public List<List<List<MenuDto>>> getMenus(Integer idProfilo);
	
	/**
	 * @return
	 */
	public List<MenuModuloVo> getMenuModuli();

	/**
	 * @param idProfilo
	 * @return
	 */
	public ProfiloDto getProfilo(Integer idProfilo);

	/**
	 * @param idMenu
	 * @return
	 */
	public MenuDto getMenu(Integer idMenu);

	/**
	 * @param moduli
	 * @return
	 */
	public List<List<List<MenuModuloVo>>> buildListModuli(List<MenuModuloVo> moduli);

	/**
	 * @param idMenu
	 * @return
	 */
	public MenuModuloVo getMenuModulo(Integer idMenu);

	/**
	 * @param idProfilo
	 * @return
	 */
	public GruppoModuliVo getGruppoModuli(Integer idProfilo);

	/**
	 * @param idProfilo
	 * @param menuDto
	 * @param idUtente 
	 */
	public void saveConfigurazioneMenu(List<MenuDto> menuDto, String idUtente);

	/**
	 * @return
	 */
	public List<MenuDto> getMenusDto(boolean soloModuliPadre);
	
	/**
	 * @param idMenu
	 */
	public void deleteFromMenuJoinTable(Integer idMenu);

	/**
	 * @param idModuloPadre
	 * @return
	 */
	public List<MenuModuloVo> getMenuModuliChilds(Integer idModuloPadre);
	
	/**
	 * @return
	 */
	public List<GruppoModuliVo> getGruppoModuli();
	
	
	
	public GruppoModuliVo getGruppoModuli(Integer codDl, Integer crivw);
	
	/**
	 * @param idGruppoMOduli
	 * @param codDl
	 * @return
	 */
	public DlGruppoModuliVo getDlGruppoModuliVo(Integer idGruppoModuli, Integer codDl);
	
	/**
	 * @param idMenu
	 */
	public void deleteMenu(Integer idMenu);
	
	/**
	 * @param profilo
	 * @param idUtente
	 */
	public void saveProfilo(ProfiloDto profilo, Integer idUtente);
	
	/**
	 * @param gruppoModuli
	 * @param codDl
	 */
	public void saveProfiloEdicolaDl(GruppoModuliVo gruppoModuli, Integer codDl);
	
	/**
	 * @param menu
	 * @param idProfilo
	 * @param idUtente
	 */
	public void saveMenu(MenuDto menu, Integer idProfilo, Integer idUtente);
	
	/**
	 * @param gruppo
	 * @param lisAssociazioni
	 */
	public void saveModuliProfilo(GruppoModuliVo gruppo, List<AssociazioneGruppoModuliVo> lisAssociazioni);
	
	/**
	 * @param idProfilo
	 */
	public void deleteProfilo(Integer idProfilo, Integer codDl);
	
	
}
