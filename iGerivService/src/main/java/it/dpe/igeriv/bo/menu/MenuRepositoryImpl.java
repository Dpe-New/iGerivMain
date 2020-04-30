package it.dpe.igeriv.bo.menu;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.comparator.ModuliComparator;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.MenuDto;
import it.dpe.igeriv.dto.ProfiloDto;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.DlGruppoModuliVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.MenuModuloVo;
import it.dpe.igeriv.vo.ModuloInputVo;
import it.dpe.igeriv.vo.UserVo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import ch.lambdaj.group.Group;

@Repository
class MenuRepositoryImpl extends BaseRepositoryImpl implements MenuRepository {
	
	@Autowired
	MenuRepositoryImpl(BaseDao<UserVo> dao) {
		super(dao);
	}

	@Override
	public List<MenuModuloVo> getModuliByIdGruppo(Integer idModulo) {
		GruppoModuliVo gruppoModuli = getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_GRUPPO_BY_ID, idModulo);
		return gruppoModuli.getModuli();
	}
	
	@Override
	public List<MenuModuloVo> getModuliByRoleName(String roleName) {
		GruppoModuliVo gruppoModuli = getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_GRUPPO_BY_ROLE_JOINS, roleName);
		return gruppoModuli.getModuli();
	}
	
	@Override
	public List<MenuModuloVo> getModuliByIds(final List<Integer> moduliSelected) {
		HibernateCallback<List<MenuModuloVo>> action = new HibernateCallback<List<MenuModuloVo>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<MenuModuloVo> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query createQuery = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_GET_MENU_BY_IDS);
				createQuery.setParameterList("ids", moduliSelected);
				return createQuery.list();
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}

	@Override
	public List<GruppoModuliVo> getGruppoModuliCustomizzabili() {
		return getDao().findByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_GRUPPO_CONFIGURABILI_DAL_DL);
	}
	
	@Override
	public GruppoModuliVo getGruppoModuliById(Integer idModulo) {
		return getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_GRUPPO_BY_ID, idModulo);
	}
	
	@Override
	public List<GruppoModuliVo> getGruppoModuliByUserType(Integer userType) {
		return getDao().findByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_GRUPPO_BY_USER_TYPE, userType);
	}
	
	@Override
	public List<ModuloInputVo> getModuliInput() {
		return getDao().findByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_MODULI_INPUT);
	}
	
	@Override
	public GruppoModuliVo getGruppoModuliByRole(String roleIgerivClienteEdicola) {
		return getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_GRUPPO_BY_ROLE_JOINS, roleIgerivClienteEdicola);
	}
	
	@Override
	public List<ProfiloDto> getProfili() {
		DetachedCriteria criteria = DetachedCriteria.forClass(GruppoModuliVo.class, "gmvo");
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("gmvo.id"), "id");
		props.add(Projections.property("gmvo.titolo"), "titolo");
		props.add(Projections.property("gmvo.descrizione"), "descrizione");
		props.add(Projections.property("gmvo.roleName"), "roleName");
		props.add(Projections.property("gmvo.tipoProfilo"), "tipoProfilo");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(ProfiloDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<List<List<MenuDto>>> getMenus(Integer idProfilo) {
		GruppoModuliVo gruppoModulo = getDao().findUniqueResultByQuery("from GruppoModuliVo gmvo join fetch gmvo.moduli where gmvo.id = :idProfilo", idProfilo);
		List<List<List<MenuDto>>> buildListModuli = null;
		if (gruppoModulo != null) {
			buildListModuli = buildMenuDtoList(buildListModuli(gruppoModulo.getModuli()));
		}
		return buildListModuli;
	}
	
	@Override
	public List<MenuModuloVo> getMenuModuli() {
		DetachedCriteria criteria = DetachedCriteria.forClass(MenuModuloVo.class, "mmvo");
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public ProfiloDto getProfilo(Integer idProfilo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GruppoModuliVo.class, "gmvo");
		criteria.add(Restrictions.eq("gmvo.id", idProfilo));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("gmvo.id"), "id");
		props.add(Projections.property("gmvo.titolo"), "titolo");
		props.add(Projections.property("gmvo.descrizione"), "descrizione");
		props.add(Projections.property("gmvo.roleName"), "roleName");
		props.add(Projections.property("gmvo.tipoProfilo"), "tipoProfilo");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(ProfiloDto.class));
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public MenuDto getMenu(Integer idMenu) {
		DetachedCriteria criteria = DetachedCriteria.forClass(MenuModuloVo.class, "mmvo");
		criteria.add(Restrictions.eq("mmvo.id", idMenu));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("mmvo.id"), "id");
		props.add(Projections.property("mmvo.titolo"), "titolo");
		props.add(Projections.property("mmvo.descrizione"), "descrizione");
		props.add(Projections.property("mmvo.url"), "url");
		props.add(Projections.property("mmvo.actionName"), "actionName");
		props.add(Projections.property("mmvo.idModuloPadre"), "idModuloPadre");
		props.add(Projections.property("mmvo.posizioneItem"), "posizioneItem");
		props.add(Projections.property("mmvo.posizioneMenu"), "posizioneMenu");
		props.add(Projections.property("mmvo.moduloPadre"), "moduloPadre");
		props.add(Projections.property("mmvo.livello"), "livello");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(MenuDto.class));
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	
	/**
	 * Ritorna una Lista di Liste con tutti i Moduli/Menu dell'applicazione
	 * con il flag "attivo" settato a "true" se l'utente possiede i permessi 
	 * per accederci.
	 * 
	 * @param List<MenuModuloVo> moduli
	 * @return List<List<MenuModuloVo>>
	 */
	@Override
	public List<List<List<MenuModuloVo>>> buildListModuli(List<MenuModuloVo> moduliAut) {
		List<List<List<MenuModuloVo>>> list = new ArrayList<List<List<MenuModuloVo>>>();
		Collections.sort(moduliAut, new ModuliComparator());
		List<MenuModuloVo> listParentLevel0 = new ArrayList<MenuModuloVo>();
		List<MenuModuloVo> listParentLevel1 = new ArrayList<MenuModuloVo>();
		for (MenuModuloVo vo : moduliAut) {
			if (vo.isModuloPadre() && vo.getLivello() == 0) {
				listParentLevel0.add(vo);
			} else if (vo.isModuloPadre() && vo.getLivello() == 1) {
				listParentLevel1.add(vo);
			}
		}
		for (MenuModuloVo vo : listParentLevel0) {
			List<List<MenuModuloVo>> innerListLevel0 = new ArrayList<List<MenuModuloVo>>();
			Integer idParentLevel0 = vo.getIdModuloPadre();
			for (MenuModuloVo vo1 : moduliAut) {
				if (vo1.getIdModuloPadre().equals(idParentLevel0)) {
					List<MenuModuloVo> innerListLevel1 = new ArrayList<MenuModuloVo>();
					for (MenuModuloVo vo2 : listParentLevel1) {
						if (vo1.getId().equals(vo2.getId())) {
							innerListLevel1.add(vo2);
							Integer idParentLevel1 = vo2.getId();
							for (MenuModuloVo vo3 : moduliAut) {
								if (vo3.getLivello() == 2 && vo3.getIdModuloPadre().equals(idParentLevel1)) {
									innerListLevel1.add(vo3);
								}
							}
						}
					}
					if (innerListLevel1.isEmpty()) {
						innerListLevel1.add(vo1);
					}
					innerListLevel0.add(innerListLevel1);
				}
			}
			list.add(innerListLevel0);
		}
		return list;
	}
	
	@Override
	public MenuModuloVo getMenuModulo(Integer idMenu) {
		DetachedCriteria criteria = DetachedCriteria.forClass(MenuModuloVo.class, "mmvo");
		criteria.add(Restrictions.eq("mmvo.id", idMenu));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public GruppoModuliVo getGruppoModuli(Integer idProfilo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GruppoModuliVo.class, "gmvo");
		criteria.add(Restrictions.eq("gmvo.id", idProfilo));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public void saveConfigurazioneMenu(List<MenuDto> menuDto, String idUtente) {
		for (MenuDto dto : menuDto) {
			getDao().bulkUpdate("update MenuModuloVo vo set vo.idModuloPadre = ?, vo.posizioneItem = ?, vo.posizioneMenu = ?, vo.livello = ?, vo.codUtente = ?, vo.dtUltimaModifica = ? where vo.id = ?", new Object[]{dto.getIdModuloPadre(), dto.getPosizioneItem(), dto.getPosizioneMenu(), dto.getLivello(), idUtente, getSysdate(), dto.getId()});
		}
		setModuliParentAndChilds();
	}
	
	@Override
	public List<MenuDto> getMenusDto(boolean soloModuliPadre) {
		DetachedCriteria criteria = DetachedCriteria.forClass(MenuModuloVo.class, "mmvo");
		if (soloModuliPadre) {
			criteria.add(Restrictions.eq("mmvo.moduloPadre", new Boolean(true)));
		}
		criteria.addOrder(Order.asc("mmvo.titolo"));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("mmvo.id"), "id");
		props.add(Projections.property("mmvo.titolo"), "titolo");
		props.add(Projections.property("mmvo.descrizione"), "descrizione");
		props.add(Projections.property("mmvo.url"), "url");
		props.add(Projections.property("mmvo.actionName"), "actionName");
		props.add(Projections.property("mmvo.idModuloPadre"), "idModuloPadre");
		props.add(Projections.property("mmvo.posizioneItem"), "posizioneItem");
		props.add(Projections.property("mmvo.posizioneMenu"), "posizioneMenu");
		props.add(Projections.property("mmvo.moduloPadre"), "moduloPadre");
		props.add(Projections.property("mmvo.livello"), "livello");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(MenuDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public void deleteFromMenuJoinTable(Integer idMenu) {
		getDao().bulkUpdate("delete from CorrispondenzaMenuPorfiloVo vo where vo.pk.idModulo = ?", new Object[]{idMenu});
	}
	
	@Override
	public List<MenuModuloVo> getMenuModuliChilds(Integer idModuloPadre) {
		DetachedCriteria criteria = DetachedCriteria.forClass(MenuModuloVo.class, "mmvo");
		criteria.add(Restrictions.eq("mmvo.idModuloPadre", idModuloPadre));
		criteria.addOrder(Order.asc("mmvo.posizioneItem"));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<GruppoModuliVo> getGruppoModuli() {
		DetachedCriteria criteria = DetachedCriteria.forClass(GruppoModuliVo.class, "mdvo");
		criteria.add(Restrictions.eq("mdvo.tipoProfilo", IGerivConstants.TIPO_UTENTE_EDICOLA));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public GruppoModuliVo getGruppoModuli(Integer codDl, Integer crivw){
		
		DetachedCriteria criteria = DetachedCriteria.forClass(UserVo.class, "us");
		criteria.createCriteria("us.dlGruppoModuliVo", "dlgruppo");
		criteria.createCriteria("dlgruppo.gruppoModuli", "gruppoMod");
		criteria.add(Restrictions.eq("us.abbinamentoEdicolaDlVo.codDpeWebEdicola", crivw));
		criteria.add(Restrictions.eq("us.dlGruppoModuliVo.dl", codDl));
		
		UserVo user = getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		if(user.getDlGruppoModuliVo().getGruppoModuli()!=null)
			return user.getDlGruppoModuliVo().getGruppoModuli();
		else
			return null;
	}
	
	/**
	 * Ritorna una lista di MenuDto estraendo i dati dalla lista GruppoModuliVo..getModuli()
	 * 
	 * @param GruppoModuliVo gruppoModuliVo
	 * @return List<MenuDto>
	 */
	private List<List<List<MenuDto>>> buildMenuDtoList(List<List<List<MenuModuloVo>>> listMenuModuloVo) {
		List<List<List<MenuDto>>> retList = new ArrayList<List<List<MenuDto>>>();
		for (List<List<MenuModuloVo>> list : listMenuModuloVo) {
			List<List<MenuDto>> list1 = new ArrayList<List<MenuDto>>();
			for (List<MenuModuloVo> innerList : list) {
				List<MenuDto> listMenuDto = new ArrayList<MenuDto>();
				for (MenuModuloVo vo : innerList) {
					MenuDto dto = new MenuDto();
					dto.setActionName(vo.getActionName());
					dto.setAttivo(vo.isAttivo());
					dto.setDescrizione(vo.getDescrizione());
					dto.setId(vo.getId());
					dto.setIdModuloPadre(vo.getIdModuloPadre());
					dto.setPosizioneItem(vo.getPosizioneItem());
					dto.setPosizioneMenu(vo.getPosizioneMenu());
					dto.setTitolo(vo.getTitolo());
					dto.setUrl(vo.getUrl());
					dto.setModuloPadre(vo.isModuloPadre());
					dto.setLivello(vo.getLivello());
					listMenuDto.add(dto);
				}
				list1.add(listMenuDto);
			}
			retList.add(list1);
		}
		return retList;
	}
	
	/**
	 * Risetta i flags della tabella moduli menu.
	 * 
	 * Esegue un loop di tutti i menu con la seguente logica: 
	 * 	=> se il menu non possiede figli, setta a 0 il campo modpa9208 e a " " il campo urlmo9208 (se questo è nullo).
	 * 	=> se il menu possiede figli, setta a 1 il campo modpa9208 e a null il campo urlmo9208.
	 */
	private void setModuliParentAndChilds() {
		List<MenuModuloVo> listMenuModuli = getMenuModuli();
		Group<MenuModuloVo> group = group(listMenuModuli, by(on(MenuModuloVo.class).getLivello()));
		List<Group<MenuModuloVo>> subgroups = group.subgroups();
		Group<MenuModuloVo> listNoduliLivello0 = subgroups.get(0);
		Group<MenuModuloVo> listNoduliLivello1 = subgroups.get(1);
		Group<MenuModuloVo> listNoduliLivello2 = (subgroups.size() > 2) ? subgroups.get(2) : null;
		for (MenuModuloVo menuLivello0 : listNoduliLivello0.findAll()) {
			boolean hasChilds = false;
			for (MenuModuloVo menuLivello1 : listNoduliLivello1.findAll()) {
				if (menuLivello1.getIdModuloPadre() != null && menuLivello1.getIdModuloPadre().equals(menuLivello0.getId())) {
					hasChilds = true;
					break;
				}
			}
			if (hasChilds && (!menuLivello0.isModuloPadre() || menuLivello0.getUrl() != null)) {
				menuLivello0.setModuloPadre(true);
				menuLivello0.setUrl(null);
				saveBaseVo(menuLivello0);
			} else if (!hasChilds && (menuLivello0.isModuloPadre() || menuLivello0.getUrl() == null)) {
				menuLivello0.setModuloPadre(false);
				if (menuLivello0.getUrl() == null) {
					menuLivello0.setUrl(" ");
				}
				saveBaseVo(menuLivello0);
			}
		}
		
		if (listNoduliLivello2 != null) {
			for (MenuModuloVo menuLivello1 : listNoduliLivello1.findAll()) {
				boolean hasChilds = false;
				for (MenuModuloVo menuLivello2 : listNoduliLivello2.findAll()) {
					if (menuLivello2.getIdModuloPadre() != null && menuLivello2.getIdModuloPadre().equals(menuLivello1.getId())) {
						hasChilds = true;
						break;
					}
				}
				if (hasChilds && (!menuLivello1.isModuloPadre() || menuLivello1.getUrl() != null)) {
					menuLivello1.setModuloPadre(true);
					menuLivello1.setUrl(null);
					saveBaseVo(menuLivello1);
				} else if (!hasChilds && (menuLivello1.isModuloPadre() || menuLivello1.getUrl() == null)) {
					menuLivello1.setModuloPadre(false);
					if (menuLivello1.getUrl() == null) {
						menuLivello1.setUrl(" ");
					}
					saveBaseVo(menuLivello1);
				}
			}
		}
	}
	
	@Override
	public DlGruppoModuliVo getDlGruppoModuliVo(Integer idGruppoModuli, Integer codDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DlGruppoModuliVo.class, "dlgmvo");
		criteria.add(Restrictions.eq("dlgmvo.pk.codGruppo", idGruppoModuli));
		criteria.add(Restrictions.eq("dlgmvo.pk.codDl", codDl));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public List<DlGruppoModuliVo> getListDlGruppoModuliVo(Integer codDl, boolean soloModuliConfigurabiliDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DlGruppoModuliVo.class, "dlgmvo");
		criteria.createCriteria("dlgmvo.gruppoModuli", "gm");
		criteria.add(Restrictions.eq("dlgmvo.pk.codDl", codDl));
		if (soloModuliConfigurabiliDl) {
			criteria.add(Restrictions.in("gm.roleName", new String[]{IGerivConstants.ROLE_IGERIV_BASE_ADMIN, IGerivConstants.ROLE_IGERIV_LITE}));
		}
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public void deleteAssociazioni(Integer id) {
		getDao().bulkUpdate("delete from AssociazioneGruppoModuliVo vo where vo.pk.idGruppo = ?", new Object[]{id});
	}
	
}
