package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.selectUnique;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.not;
import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.bo.menu.MenuService;
import it.dpe.igeriv.dto.UserDto;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AssociazioneGruppoModuliVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.MenuModuloVo;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.igeriv.vo.pk.AssociazioneGruppoModuliPk;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;

/**
 * Classe action per la gestione dei profili utente del sistema.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("profiliAction")
public class ProfiliAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements RequestAware {
	private static final long serialVersionUID = 1L;
	private final MenuService menuService;
	private final AccountService accountService;
	private final String crumbName = getText("igeriv.inserisci.aggiorna.profilo");
	private String tableHeight;
	private String tableTitle;
	private String filterTitle;
	private List<UserDto> utenti;
	private List<GruppoModuliVo> profili;
	private List<MenuModuloVo> moduli;
	private List<Integer> moduliSelected;
	private GruppoModuliVo profilo;
	private String actionName;
	private String tableStyle;
	private String nomeUtente;
	private String codUtente;
	private String inviaEmail;
	private String newUser;
	private Integer idProfilo;
	private List<Map<String,String>> resultList;
	private Map<String, String> deleteResult;
	
	public ProfiliAction() {
		this(null,null);
	}
	
	@Autowired
	public ProfiliAction(MenuService menuService, AccountService accountService) {
		this.menuService = menuService;
		this.accountService = accountService;
	}
	
	@Override
	public void prepare() throws Exception {
		filterTitle = getText("igeriv.filtro.profili.edicola");
		tableTitle = getText("igeriv.inserisci.aggiorna.profilo");
		super.prepare();
	}
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showFilter() {
		profili = menuService.getGruppoModuliCustomizzabili();
		sessionMap.put("profili", profili);
		return SUCCESS;
	}
	
	@SkipValidation
	public String getModuliProfilo() {
		resultList = new ArrayList<Map<String,String>>();
		Map<String,String> mapModuli = new HashMap<String, String>();
		Map<String,String> mapModuliPresenti = new HashMap<String, String>();
		moduli = select(menuService.getModuliByRoleName(IGerivConstants.ROLE_IGERIV_BASE_ADMIN), having(on(MenuModuloVo.class).isModuloPadre(), equalTo(false)));
		profili = menuService.getGruppoModuliCustomizzabili();
		sessionMap.put("profili", profili);
		List<MenuModuloVo> moduliPresenti = null;
		if (idProfilo != null) {
			profilo = menuService.getGruppoModuliById(idProfilo);
			moduliPresenti = profilo.getModuli() != null && !profilo.getModuli().isEmpty() ? select(profilo.getModuli(), having(on(MenuModuloVo.class).isModuloPadre(), equalTo(false))) : null;
			if (moduliPresenti != null && !moduliPresenti.isEmpty()) {
				List<Integer> idModuliPresenti = extract(moduliPresenti, on(MenuModuloVo.class).getId());
				moduli = select(moduli, having(on(MenuModuloVo.class).getId(), not(isIn(idModuliPresenti))));
				for (MenuModuloVo md : (List<MenuModuloVo>) moduliPresenti) {
					mapModuliPresenti.put(md.getId().toString(), md.getTitolo());
				}
			}
		}
		for (MenuModuloVo md : moduli) {
			mapModuli.put(md.getId().toString(), md.getTitolo());
		}
		resultList.add(mapModuli);
		resultList.add(mapModuliPresenti);
		return "successMenuModuli";
	}
	
	@SkipValidation
	public String saveModuliProfilo() {
		if (idProfilo != null) {
			profilo = menuService.getGruppoModuliById(idProfilo);
			if (profilo != null) {
				List<MenuModuloVo> moduliBeans = menuService.getModuliByRoleName(IGerivConstants.ROLE_IGERIV_BASE_ADMIN);
				List<MenuModuloVo> moduliBeansSelected = select(moduliBeans, having(on(MenuModuloVo.class).getId(), isIn(moduliSelected)));
				Set<MenuModuloVo> moduliPadre = new HashSet<MenuModuloVo>();
				for (MenuModuloVo menu : moduliBeansSelected) {
					if (menu.getIdModuloPadre() != null) {
						MenuModuloVo selectUnique = selectUnique(moduliBeans, having(on(MenuModuloVo.class).getId(), equalTo(menu.getIdModuloPadre())));
						if (selectUnique != null) {
							moduliPadre.add(selectUnique);
							if (selectUnique.getIdModuloPadre() != null && !selectUnique.getIdModuloPadre().equals(selectUnique.getId()) ) {
								MenuModuloVo selectUnique1 = selectUnique(moduliBeans, having(on(MenuModuloVo.class).getId(), equalTo(selectUnique.getIdModuloPadre())));
								moduliPadre.add(selectUnique1);
							}
						}
					}
				}
				List<Integer> idModuliPadre = extract(moduliPadre, on(MenuModuloVo.class).getId());
				List<Integer> idModuli = new ArrayList<Integer>();
				idModuli.addAll(moduliSelected);
				idModuli.addAll(idModuliPadre);
				List<AssociazioneGruppoModuliVo> listModuli = new ArrayList<AssociazioneGruppoModuliVo>();
				for (Integer idModulo : idModuli) {
					AssociazioneGruppoModuliVo agm = new AssociazioneGruppoModuliVo();
					AssociazioneGruppoModuliPk pk = new AssociazioneGruppoModuliPk();
					pk.setIdGruppo(idProfilo);
					pk.setIdModulo(idModulo);
					agm.setPk(pk);
					listModuli.add(agm);
				}
				menuService.saveModuliProfilo(profilo, listModuli);
			}
		}
		return "blank";
	}
	
	@SkipValidation
	public String saveProfilo() {
		if (profilo != null) {
			profilo.setTitolo(profilo.getTitolo().toUpperCase());
			profilo.setDescrizione(profilo.getDescrizione().toUpperCase());
			profilo.setRoleName(IGerivConstants.ROLE_IGERIV_BASE_ADMIN);
			profilo.setTipoProfilo(IGerivConstants.TIPO_UTENTE_EDICOLA);
			profilo.setCodUtente(getAuthUser().getCodUtente());
			profilo.setDtUltimaModifica(menuService.getSysdate());
			profilo.setConfigurabileDl(true);
			menuService.saveProfiloEdicolaDl(profilo, getAuthUser().getCodFiegDl());
		}
		return "blank";
	}
	
	@SkipValidation
	public String deleteProfilo() {
		deleteResult = new HashMap<String, String>();
		if (idProfilo != null) {
			try {
				menuService.deleteProfilo(idProfilo, getAuthUser().getCodFiegDl());
			} catch (DataIntegrityViolationException e) {
				GruppoModuliVo profilo = menuService.getGruppoModuli(idProfilo);
				List<UserVo> listEdicola = accountService.getEdicolaByCodiceGruppo(idProfilo);
				String codiciWeb = Joiner.on(", ").join(extract(listEdicola, on(UserVo.class).getAbbinamentoEdicolaDlVo().getCodDpeWebEdicola()));
				deleteResult.put("error", MessageFormat.format(getText("gp.errore.cancellazione.profilo.edicole"), profilo.getTitolo(), codiciWeb));
			}
		}
		return "deleteResult";
	}
	
	@SkipValidation
	public String editProfilo() {
		if (idProfilo != null) {
			profilo = menuService.getGruppoModuli(idProfilo);
		}
		return "editProfilo";
	}
	
	@SkipValidation
	public String newProfilo() {
		profilo = new GruppoModuliVo();
		return "editProfilo";
	}

	@Override
	public String getTitle() {
		return super.getTitle() + getText("igeriv.aggiorna.profili.edicola");
	}
	
}
