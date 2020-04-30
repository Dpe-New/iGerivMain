package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.bo.menu.MenuService;
import it.dpe.igeriv.dto.MenuDto;
import it.dpe.igeriv.dto.ProfiloDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.BaseVo;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @deprecated 
 * Non utilizzare questa action
 * 
 * Classe action per la gestione dei messaggi del sistema da parte del DL.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@SuppressWarnings({ "unused" })
@Scope("prototype")
@Component("menuAction")
public class MenuAction<T extends BaseVo> extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;
	@Autowired
	private MenuService menuService;
	private String tableHeight;
	private String tableTitle;
	private String filterTitle;
	private List<MenuDto> menus;
	private List<List<List<MenuDto>>> menusProfilo;
	private List<ProfiloDto> profili;
	private List<Integer> menusSelected;
	private String actionName;
	private String tableStyle;
	private String nomeUtente;
	private String codUtente;
	private String inviaEmail;
	private String newUser;
	private String idProfilo;
	private String idMenu;
	private String idMenuPadre;
	private ProfiloDto profilo;
	private MenuDto menu;
	private List<MenuDto> listMenus;
	private String ids;
	private String parentIds;
	private String livello;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		profili = menuService.getProfili();
	}
	
	@Override
	public void validate() {
		if (getActionName() != null && getActionName().contains("saveMenu")) {
			if (menu.getTitolo() == null || menu.getTitolo().equals("")) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("label.print.Table.Title")) + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
			if (menu.getDescrizione() == null || menu.getDescrizione().equals("")) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("igeriv.descrizione")) + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
			if (menu.getPosizioneMenu() == null || menu.getPosizioneMenu().equals("")) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("dpe.menu.posizione")) + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
			if (menu.getPosizioneItem() == null || menu.getPosizioneItem().equals("")) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("dpe.menu.posizione.interna")) + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
		}
	}
	
	@SkipValidation
	public String showMenusFilter() {
		tableTitle = getText("igeriv.inserisci.aggiorna.menu");
		return SUCCESS;
	}
	
	@SkipValidation
	public String showMenus() {
		tableTitle = getText("igeriv.inserisci.aggiorna.menu");
		if (idProfilo != null && !idProfilo.equals("")) {
			menusProfilo = menuService.getMenus(new Integer(idProfilo));
		}
		return SUCCESS;
	}
	
	@SkipValidation
	public String saveConfigurazioneMenu() {
		tableTitle = getText("igeriv.inserisci.aggiorna.menu");
		if (idProfilo != null && ids != null && parentIds != null && livello != null && !ids.equals("") && !parentIds.equals("") && !livello.equals("")) {
			List<MenuDto> listMenuDto = buildListMenuDto();
			menuService.saveConfigurazioneMenu(listMenuDto, getAuthUser().getId().toString());
		}
		return IGerivConstants.REDIRECT;
	}

	@SkipValidation
	public String editProfilo() {
		tableTitle = getText("igeriv.inserisci.aggiorna.profilo");
		if (idProfilo != null && !idProfilo.equals("")) {
			profilo = menuService.getProfilo(new Integer(idProfilo));
		} else {
			profilo = new ProfiloDto();
		}
		return "editProfilo";
	}
	
	@SkipValidation
	public String editMenu() {
		tableTitle = getText("igeriv.inserisci.aggiorna.menu");
		if (idMenu != null && !idMenu.equals("")) {
			menu = menuService.getMenu(new Integer(idMenu));
		} else {
			menu = new MenuDto();
		}
		listMenus = menuService.getMenusDto(true);
		return "editMenu";
	}
	
	@SkipValidation
	public String insertMenu() {
		tableTitle = getText("igeriv.inserisci.aggiorna.menu");
		if (idMenu != null && !idMenu.equals("")) {
			menu = menuService.getMenu(new Integer(idMenu));
		} else {
			menu = new MenuDto();
		}
		menu.setLivello(livello != null ? new Integer(livello) : null);
		menu.setIdModuloPadre((idMenuPadre != null && livello != null && new Integer(livello) > 0) ? new Integer(idMenuPadre) : null);
		menu.setModuloPadre((idMenuPadre == null || livello == null || new Integer(livello) == 0) ? true : false);	
		listMenus = menuService.getMenusDto(true);
		return "editMenu";
	}
	
	public String saveProfilo() {
		if (profilo != null) {
			menuService.saveProfilo(profilo, getAuthUser().getId());
		}
		return SUCCESS;
	}
	
	public String saveMenu() {
		if (menu != null && idProfilo != null && !idProfilo.equals("")) {
			menuService.saveMenu(menu, new Integer(idProfilo), getAuthUser().getId());
		}
		return SUCCESS;
	}
	
	@SkipValidation
	public String deleteProfilo() {
		/*tableTitle = getText("igeriv.inserisci.aggiorna.profilo");
		if (idProfilo != null && !idProfilo.equals("")) {
			menuService.deleteProfilo(new Integer(idProfilo));
		}*/
		return SUCCESS;
	}
	
	@SkipValidation
	public String deleteMenu() {
		tableTitle = getText("igeriv.inserisci.aggiorna.profilo");
		if (idMenu != null && !idMenu.equals("")) {
			menuService.deleteMenu(new Integer(idMenu));
		}
		return SUCCESS;
	}
	
	private List<MenuDto> buildListMenuDto() {
		List<MenuDto> listMenuDto = new ArrayList<MenuDto>();
		String[] arrIds = ids.split(",");
		String[] arrParentIds = parentIds.split(",");
		String[] arrLivello = livello.split(",");
		
		Map<Integer, Integer> mapPosizioni = new HashMap<Integer, Integer>();
		for (int i = 1; i < arrIds.length; i++) {
			Integer id = new Integer(arrIds[i].trim());
			int level = new Integer(arrLivello[i].trim()) - 1;
			int idModuloPadre = 0;
			if (NumberUtils.isNumber(arrParentIds[i].trim())) {
				idModuloPadre = new Integer(arrParentIds[i].trim());
			} else {
				idModuloPadre = id;
			} 
			mapPosizioni.put(idModuloPadre, 1);
		}
		
		int count = 0;
		for (int i = 1; i < arrIds.length; i++) {
			MenuDto dto = new MenuDto();
			Integer id = new Integer(arrIds[i].trim());
			int level = new Integer(arrLivello[i].trim()) - 1;
			dto.setId(id);
			int idModuloPadre = 0;
			if (NumberUtils.isNumber(arrParentIds[i].trim())) {
				idModuloPadre = new Integer(arrParentIds[i].trim());
			} else {
				count++;
				idModuloPadre = id;
			}
			dto.setIdModuloPadre(idModuloPadre);
			dto.setLivello(level);
			dto.setPosizioneItem(mapPosizioni.get(idModuloPadre));
			dto.setPosizioneMenu(count);
			Integer posizioneItem = mapPosizioni.get(idModuloPadre) != null ? Integer.valueOf(mapPosizioni.get(idModuloPadre).toString()) + 1 : new Integer(1);
			mapPosizioni.put(idModuloPadre, posizioneItem);
			listMenuDto.add(dto);
		}
		return listMenuDto;
	}

	@Override
	public String getTitle() {
		return super.getTitle() + getText("igeriv.aggiorna.profili.edicola");
	}

	public String getTableHeight() {
		return tableHeight;
	}

	public void setTableHeight(String tableHeight) {
		this.tableHeight = tableHeight;
	}
	
	public String getTableTitle() {
		return tableTitle;
	}

	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
	}
	
	public String getFilterTitle() {
		return filterTitle;
	}

	public void setFilterTitle(String filterTitle) {
		this.filterTitle = filterTitle;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
		
	public String getNomeUtente() {
		return nomeUtente;
	}

	public void setNomeUtente(String nomeUtente) {
		this.nomeUtente = nomeUtente;
	}
	
	public String getCodUtente() {
		return codUtente;
	}

	public void setCodUtente(String codUtente) {
		this.codUtente = codUtente;
	}

	public String getTableStyle() {
		return tableStyle;
	}

	public void setTableStyle(String tableStyle) {
		this.tableStyle = tableStyle;
	}

	public String getInviaEmail() {
		return inviaEmail;
	}

	public void setInviaEmail(String inviaEmail) {
		this.inviaEmail = inviaEmail;
	}

	public String getNewUser() {
		return newUser;
	}

	public void setNewUser(String newUser) {
		this.newUser = newUser;
	}

	public MenuService getMenuService() {
		return menuService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	public List<MenuDto> getMenus() {
		return menus;
	}

	public void setMenus(List<MenuDto> menus) {
		this.menus = menus;
	}

	public List<ProfiloDto> getProfili() {
		return profili;
	}

	public void setProfili(List<ProfiloDto> profili) {
		this.profili = profili;
	}

	public List<List<List<MenuDto>>> getMenusProfilo() {
		return menusProfilo;
	}

	public void setMenusProfilo(List<List<List<MenuDto>>> menusProfilo) {
		this.menusProfilo = menusProfilo;
	}

	public List<Integer> getMenusSelected() {
		return menusSelected;
	}

	public void setMenusSelected(List<Integer> menusSelected) {
		this.menusSelected = menusSelected;
	}

	public String getIdProfilo() {
		return idProfilo;
	}

	public void setIdProfilo(String idProfilo) {
		this.idProfilo = idProfilo;
	}

	public String getIdMenu() {
		return idMenu;
	}

	public void setIdMenu(String idMenu) {
		this.idMenu = idMenu;
	}
	
	public String getIdMenuPadre() {
		return idMenuPadre;
	}

	public void setIdMenuPadre(String idMenuPadre) {
		this.idMenuPadre = idMenuPadre;
	}

	public ProfiloDto getProfilo() {
		return profilo;
	}

	public void setProfilo(ProfiloDto profilo) {
		this.profilo = profilo;
	}

	public MenuDto getMenu() {
		return menu;
	}

	public void setMenu(MenuDto menu) {
		this.menu = menu;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getLivello() {
		return livello;
	}

	public void setLivello(String livello) {
		this.livello = livello;
	}

	public List<MenuDto> getListMenus() {
		return listMenus;
	}

	public void setListMenus(List<MenuDto> listMenus) {
		this.listMenus = listMenus;
	}
	
}
