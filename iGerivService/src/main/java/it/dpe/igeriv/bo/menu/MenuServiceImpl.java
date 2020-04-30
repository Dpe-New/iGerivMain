package it.dpe.igeriv.bo.menu;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.dto.MenuDto;
import it.dpe.igeriv.dto.ProfiloDto;
import it.dpe.igeriv.vo.AssociazioneGruppoModuliVo;
import it.dpe.igeriv.vo.CorrispondenzaMenuPorfiloVo;
import it.dpe.igeriv.vo.DlGruppoModuliVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.MenuModuloVo;
import it.dpe.igeriv.vo.ModuloInputVo;
import it.dpe.igeriv.vo.pk.CorrispondenzaMenuPorfiloPk;
import it.dpe.igeriv.vo.pk.DlGruppoModuliPk;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("MenuService")
class MenuServiceImpl extends BaseServiceImpl implements MenuService {
	private final MenuRepository repository;
	
	@Autowired
	MenuServiceImpl(MenuRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public List<MenuModuloVo> getModuliByIdGruppo(Integer idModulo) {
		return repository.getModuliByIdGruppo(idModulo);
	}

	@Override
	public List<MenuModuloVo> getModuliByRoleName(String roleName) {
		return repository.getModuliByRoleName(roleName);
	}
	
	@Override
	public List<MenuModuloVo> getModuliByIds(List<Integer> moduliSelected) {
		return repository.getModuliByIds(moduliSelected);
	}

	@Override
	public List<GruppoModuliVo> getGruppoModuliCustomizzabili() {
		return repository.getGruppoModuliCustomizzabili();
	}

	@Override
	public GruppoModuliVo getGruppoModuliById(Integer idModulo) {
		return repository.getGruppoModuliById(idModulo);
	}

	@Override
	public List<GruppoModuliVo> getGruppoModuliByUserType(Integer userType) {
		return repository.getGruppoModuliByUserType(userType);
	}
	
	@Override
	public List<ModuloInputVo> getModuliInput() {
		return repository.getModuliInput();
	}
	
	@Override
	public GruppoModuliVo getGruppoModuliByRole(String roleIgerivClienteEdicola) {
		return repository.getGruppoModuliByRole(roleIgerivClienteEdicola);
	}

	@Override
	public List<ProfiloDto> getProfili() {
		return repository.getProfili();
	}

	@Override
	public List<List<List<MenuDto>>> getMenus(Integer idProfilo) {
		return repository.getMenus(idProfilo);
	}

	@Override
	public List<MenuModuloVo> getMenuModuli() {
		return repository.getMenuModuli();
	}

	@Override
	public ProfiloDto getProfilo(Integer idProfilo) {
		return repository.getProfilo(idProfilo);
	}

	@Override
	public MenuDto getMenu(Integer idMenu) {
		return repository.getMenu(idMenu);
	}

	@Override
	public List<List<List<MenuModuloVo>>> buildListModuli(List<MenuModuloVo> moduli) {
		return repository.buildListModuli(moduli);
	}

	@Override
	public MenuModuloVo getMenuModulo(Integer idMenu) {
		return repository.getMenuModulo(idMenu);
	}

	@Override
	public GruppoModuliVo getGruppoModuli(Integer idProfilo) {
		return repository.getGruppoModuli(idProfilo);
	}

	@Override
	public void saveConfigurazioneMenu(List<MenuDto> menuDto, String idUtente) {
		repository.saveConfigurazioneMenu(menuDto, idUtente);
	}

	@Override
	public List<MenuDto> getMenusDto(boolean soloModuliPadre) {
		return repository.getMenusDto(soloModuliPadre);
	}

	@Override
	public void deleteFromMenuJoinTable(Integer idMenu) {
		repository.deleteFromMenuJoinTable(idMenu);
	}

	@Override
	public List<MenuModuloVo> getMenuModuliChilds(Integer idModuloPadre) {
		return repository.getMenuModuliChilds(idModuloPadre);
	}
	
	@Override
	public List<GruppoModuliVo> getGruppoModuli() {
		return repository.getGruppoModuli();
	}
	
	public GruppoModuliVo getGruppoModuli(Integer codDl, Integer crivw){
		return repository.getGruppoModuli(codDl,crivw);
	}
	
	@Override
	public DlGruppoModuliVo getDlGruppoModuliVo(Integer idGruppoModuli, Integer codDl) {
		return repository.getDlGruppoModuliVo(idGruppoModuli, codDl);
	}
	
	public List<DlGruppoModuliVo> getListDlGruppoModuliVo(Integer codDl, boolean soloModuliConfigurabiliDl) {
		return repository.getListDlGruppoModuliVo(codDl, soloModuliConfigurabiliDl);
	}
	
	@Override
	public void deleteMenu(Integer idMenu) {
		MenuModuloVo vo = getMenuModulo(idMenu);
		deleteFromMenuJoinTable(idMenu);
		deleteVo(vo);
	}
	
	@Override
	public void saveProfilo(ProfiloDto profilo, Integer idUtente) {
		GruppoModuliVo gruppoModuli = null;
		if (profilo.getId() != null) {
			gruppoModuli = getGruppoModuli(profilo.getId());
		} else {
			gruppoModuli = new GruppoModuliVo();
			gruppoModuli.setId(getLastId(GruppoModuliVo.class, "id", null));
		}
		gruppoModuli.setDescrizione(profilo.getDescrizione());
		gruppoModuli.setRoleName(profilo.getRoleName());
		gruppoModuli.setTipoProfilo(profilo.getTipoProfilo());
		gruppoModuli.setTitolo(profilo.getTitolo());
		gruppoModuli.setCodUtente(idUtente.toString());
		gruppoModuli.setDtUltimaModifica(getSysdate());
		saveBaseVo(gruppoModuli);
	}
	
	@Override
	public void saveProfiloEdicolaDl(GruppoModuliVo gruppoModuli, Integer codDl) {
		saveBaseVo(gruppoModuli);
		DlGruppoModuliVo dlGruppo = getDlGruppoModuliVo(gruppoModuli.getId(), codDl);
		if (dlGruppo == null) {
			dlGruppo = new DlGruppoModuliVo();
			DlGruppoModuliPk pk = new DlGruppoModuliPk();
			pk.setCodDl(codDl);
			pk.setCodGruppo(gruppoModuli.getId());
			dlGruppo.setPk(pk);
			saveBaseVo(dlGruppo);
		}
	}
	
	@Override
	public void saveMenu(MenuDto menu, Integer idProfilo, Integer idUtente) {
		MenuModuloVo vo = null;
		if (menu.getId() != null) {
			vo = getMenuModulo(menu.getId());
		} else {
			vo = new MenuModuloVo();
			vo.setId(getLastId(MenuModuloVo.class, "id", null));
		}
		vo.setActionName(menu.getActionName() != null ? menu.getActionName().trim() : null);
		vo.setDescrizione(menu.getDescrizione() != null ? menu.getDescrizione().trim() : null);
		vo.setPosizioneItem(menu.getPosizioneItem());
		vo.setPosizioneMenu(menu.getPosizioneMenu());
		vo.setLivello(menu.getLivello());
		vo.setCodUtente(idUtente.toString());
		vo.setDtUltimaModifica(getSysdate());
		if (menu.getIdModuloPadre() != null) {
			vo.setIdModuloPadre(menu.getIdModuloPadre());
			MenuModuloVo voPadre = getMenuModulo(menu.getIdModuloPadre());
			vo.setPosizioneMenu(voPadre.getPosizioneMenu());
			List<MenuModuloVo> childs = getMenuModuliChilds(menu.getIdModuloPadre());
			for (MenuModuloVo child : childs) {
				if (vo.getPosizioneItem().equals(child.getPosizioneItem()) && !vo.equals(child)) {
					int posizioneItem = childs.get(childs.size() - 1).getPosizioneItem() + 1;
					vo.setPosizioneItem(posizioneItem);
					break;
				}
			}
			if (vo.getLivello() != (voPadre.getLivello() + 1)) {
				vo.setLivello(voPadre.getLivello() + 1);
			}
		} else {
			vo.setIdModuloPadre(vo.getId());
		}
		vo.setModuloPadre(menu.getModuloPadre());
		vo.setTitolo(menu.getTitolo() != null ? menu.getTitolo().trim() : null);
		vo.setUrl(menu.getUrl() != null ? menu.getUrl().trim() : null);
		if (vo.isModuloPadre()) {
			vo.setUrl(null);
		}
		saveBaseVo(vo);
		if (menu.getId() == null) {
			CorrispondenzaMenuPorfiloVo vo1 = new CorrispondenzaMenuPorfiloVo();
			CorrispondenzaMenuPorfiloPk pk = new CorrispondenzaMenuPorfiloPk();
			pk.setIdGruppo(idProfilo);
			pk.setIdModulo(vo.getId());
			vo1.setPk(pk);
			saveBaseVo(vo1);
		}
	}
	
	@Override
	public void saveModuliProfilo(GruppoModuliVo gruppo, List<AssociazioneGruppoModuliVo> lisAssociazioni) {
		saveBaseVo(gruppo);
		repository.deleteAssociazioni(gruppo.getId());
		if (lisAssociazioni != null && !lisAssociazioni.isEmpty()) {
			saveVoList(lisAssociazioni);
		}
	}
	
	@Override
	public void deleteProfilo(Integer idProfilo, Integer codDl) {
		repository.deleteAssociazioni(idProfilo);
		DlGruppoModuliVo dlGruppoModuliVo = getDlGruppoModuliVo(idProfilo, codDl);
		if (dlGruppoModuliVo != null) {
			deleteVo(dlGruppoModuliVo);
		}
		GruppoModuliVo gruppoModuli = getGruppoModuli(idProfilo);
		if (gruppoModuli != null) {
			deleteVo(gruppoModuli);
		}
	}
	
}
