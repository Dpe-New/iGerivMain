package it.dpe.igeriv.bo.account;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.flatten;
import static ch.lambdaj.Lambda.forEach;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.selectFirst;
import static org.hamcrest.Matchers.equalTo;
import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.livellamenti.LivellamentiService;
import it.dpe.igeriv.bo.menu.MenuService;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.ArretratiDto;
import it.dpe.igeriv.dto.CodDlEdicoleSecondarieDto;
import it.dpe.igeriv.dto.UserDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.util.IGerivUtils;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.DlGruppoModuliVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.MenuModuloVo;
import it.dpe.igeriv.vo.PianoProfiliEdicolaVo;
import it.dpe.igeriv.vo.UserAdminVo;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.igeriv.vo.UtenteAgenziaVo;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;

@Repository
class AccountRepositoryImpl extends BaseRepositoryImpl implements AccountRepository {
	private final MenuService menuService;
	private final EdicoleService edicoleService;
	private final LivellamentiService<ArretratiDto> livellamentiService;
	private final IGerivUtils iGerivUtils;
	
	@Autowired
	AccountRepositoryImpl(BaseDao<UserVo> dao, MenuService menuService, EdicoleService edicoleService, LivellamentiService<ArretratiDto> livellamentiService, IGerivUtils iGerivUtils) {
		super(dao);
		this.menuService = menuService;
		this.edicoleService = edicoleService;
		this.livellamentiService = livellamentiService;
		this.iGerivUtils = iGerivUtils;
	}
	
	@Override
	public UserVo getEdicolaByCodice(final String codEdicola) {
		HibernateCallback<UserVo> action = new HibernateCallback<UserVo>() {
			@Override
			public UserVo doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(UserVo.class, "vo");
				criteria.createCriteria("vo.abbinamentoEdicolaDlVo", "ab", JoinType.INNER_JOIN);
				criteria.createCriteria("ab.anagraficaAgenziaVo", "aa", JoinType.INNER_JOIN);
				criteria.createCriteria("aa.agenziaSecondaria", "as", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("ab.anagraficaEdicolaVo", "ae", JoinType.INNER_JOIN);
				criteria.createCriteria("ae.anagraficaEdicolaAggiuntiviVo", "aea", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("aea.immagine1", "im", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("vo.dlGruppoModuliVo", "dlgr", JoinType.INNER_JOIN);
				criteria.createCriteria("dlgr.gruppoModuli", "gm", JoinType.INNER_JOIN);
				criteria.createCriteria("gm.moduli", "mo", JoinType.INNER_JOIN);
				criteria.add(Restrictions.eq("vo.codUtente", codEdicola));
				Object uniqueResult = criteria.uniqueResult();
				UserVo userVo = uniqueResult != null ? (UserVo) uniqueResult : null;
				if (userVo != null) {
					Hibernate.initialize(userVo.getDlGruppoModuliVo().getGruppoModuli().getModuli());
				}
				return uniqueResult != null ? (UserVo) uniqueResult : null;
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public UserVo getEdicolaByEmail(final String email) {
		HibernateCallback<UserVo> action = new HibernateCallback<UserVo>() {
			@Override
			public UserVo doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(UserVo.class, "vo");
				criteria.createCriteria("vo.abbinamentoEdicolaDlVo", "ab", JoinType.INNER_JOIN);
				criteria.createCriteria("ab.anagraficaAgenziaVo", "aa", JoinType.INNER_JOIN);
				criteria.createCriteria("aa.agenziaSecondaria", "as", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("ab.anagraficaEdicolaVo", "ae", JoinType.INNER_JOIN);
				criteria.createCriteria("vo.dlGruppoModuliVo", "dlgr", JoinType.INNER_JOIN);
				criteria.createCriteria("dlgr.gruppoModuli", "gm", JoinType.INNER_JOIN);
				criteria.createCriteria("gm.moduli", "mo", JoinType.INNER_JOIN);
				criteria.add(Restrictions.eq("vo.email", email));
				criteria.setMaxResults(1);
				Object uniqueResult = criteria.uniqueResult();
				UserVo userVo = uniqueResult != null ? (UserVo) uniqueResult : null;
				if (uniqueResult != null) { 
					Hibernate.initialize(userVo.getDlGruppoModuliVo().getGruppoModuli().getModuli());
				}
				return userVo;
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public UserVo getEdicolaByCodiceEdicola(Integer codEdicola) {
		return getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_USER_BY_CODICE_EDICOLA_JOINS, codEdicola);
	}
	
	@Override
	public UserVo getSottoUtenteEdicolaByEmail(String email) {
		return getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_SUB_USER_BY_EMAIL, email);
	}
	
	
	@Override
	public List<UserDto> getUtentiEdicola(Integer codEdicola, Boolean isAdmin, String codUtente, String nomeUtente, String codUtenteLoggato) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserVo.class, "usr");
		criteria.createCriteria("usr.dlGruppoModuliVo", "dlg");
		criteria.createCriteria("dlg.gruppoModuli", "gm");
		criteria.createCriteria("usr.abbinamentoEdicolaDlVo", "ae");
		criteria.add(Restrictions.eq("ae.codDpeWebEdicola", codEdicola));
		if (codUtente != null && !codUtente.equals("")) {
			criteria.add(Restrictions.eq("usr.codUtente", codUtente));
		}
		if (codUtenteLoggato != null && !codUtenteLoggato.equals("")) {
			criteria.add(Restrictions.ne("usr.codUtente", codUtenteLoggato));
		}
		if (nomeUtente != null && !nomeUtente.equals("")) {
			criteria.add(Restrictions.eq("usr.nomeUtente", nomeUtente));
		}
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("usr.codUtente"), "codUtente");
		properties.add(Projections.property("usr.nomeUtente"), "nomeUtente");
		properties.add(Projections.property("usr.email"), "email");
		properties.add(Projections.property("gm.titolo"), "titolo"); 
		properties.add(Projections.property("usr.abilitato"), "abilitatoInt"); 
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(UserDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public UserVo getUtenteEdicola(String codUtente) {
		return getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_USER_BY_CODICE, codUtente);
	}
	@Override
	public void updateUserAgent(final String codUtente, final String header) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_UPDATE_USER_AGENT);
				query.setString("userAgent", header);
				query.setString("codUtente", codUtente);
				return query.executeUpdate();
			}
		};
		getDao().executeByHibernateCallback(action);
	}
	
	@Override
	public void updateClientUserAgent(final Long codUtente, final Integer codEdicolaDl,
			final String header) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_UPDATE_CLIENT_USER_AGENT);
				query.setString("userAgent", header);
				query.setInteger("codEdicola", codEdicolaDl);
				query.setLong("codCliente", codUtente);
				return query.executeUpdate();
			}
		};
		getDao().executeByHibernateCallback(action);
	}
	
	@Override
	public UserAdminVo getUserAdminVo(String codUtente, boolean getModuli) {
		if (getModuli) {
			return getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_USER_ADMIN_BY_CODICE_JOINS, codUtente);
		}
		DetachedCriteria criteria = DetachedCriteria.forClass(UserAdminVo.class, "uavo");
		criteria.add(Restrictions.eq("uavo.codUtente", codUtente));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public UserAdminVo getUserAdminVo(String email) {
		return getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_USER_ADMIN_BY_EMAIL_JOINS, email);
	}
	
	@Override
	public Integer getCodDpeWebEdicolaSecondaria(Integer codFiegDlSecondario, Integer codEdicolaDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AbbinamentoEdicolaDlVo.class, "abevo");
		criteria.createCriteria("abevo.anagraficaAgenziaVo", "aa");
		criteria.add(Restrictions.eq("aa.codFiegDl", codFiegDlSecondario));
		criteria.add(Restrictions.eq("abevo.codEdicolaDl", codEdicolaDl));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("abevo.codDpeWebEdicola"));
		criteria.setProjection(props);
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<CodDlEdicoleSecondarieDto> getListCodDlECodEdicolaSecondarie(Integer codDpeWebEdicolaMaster) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AbbinamentoEdicolaDlVo.class, "abevo");
		criteria.createCriteria("abevo.anagraficaAgenziaVo", "aa");
		criteria.add(Restrictions.eq("abevo.codEdicolaMaster", codDpeWebEdicolaMaster));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("abevo.codDpeWebEdicola"), "codDpeWebEdicola");
		props.add(Projections.property("aa.codFiegDl"), "codFiegDl");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(CodDlEdicoleSecondarieDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public void saveDowngradeAccountToStarter(UserVo utente) {
		GruppoModuliVo gruppoModuliAdmin = menuService.getGruppoModuliByRole(IGerivConstants.ROLE_IGERIV_STARTER);
		DlGruppoModuliVo gruppoModuliByRole = menuService.getDlGruppoModuliVo(gruppoModuliAdmin.getId(), utente.getDlGruppoModuliVo().getPk().getCodDl());
		utente.setDlGruppoModuliVo(gruppoModuliByRole);
		utente.getAbbinamentoEdicolaDlVo().setDtSospensioneEdicola(null);
		saveBaseVo(utente.getAbbinamentoEdicolaDlVo());
		saveBaseVo(utente);
	}
	
	@Override
	public UserAbbonato buildUserDetails(String userId, BaseVo utenteBase) {
		UserVo utente = (UserVo) utenteBase;
		UserAbbonato user = null;
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		AbbinamentoEdicolaDlVo abbinamentoEdicolaDlVo = utente.getAbbinamentoEdicolaDlVo();
		Integer codEdicolaDl = abbinamentoEdicolaDlVo.getCodEdicolaDl();
		Integer codDpeWebEdicola = abbinamentoEdicolaDlVo.getCodDpeWebEdicola();
		AnagraficaAgenziaVo anagraficaAgenziaVo = abbinamentoEdicolaDlVo.getAnagraficaAgenziaVo();
		Integer codFiegDl = anagraficaAgenziaVo.getCodFiegDl();
		Integer codFiegDlSecondario = anagraficaAgenziaVo.getAgenziaSecondaria() != null ? anagraficaAgenziaVo.getAgenziaSecondaria().getCodFiegDl() : null;
		authList.add(new GrantedAuthorityImpl(utente.getDlGruppoModuliVo().getGruppoModuli().getRoleName()));
		if (!Strings.isNullOrEmpty(utente.getPwd())) {
			user = new UserAbbonato(userId, utente.getPwd(), true, true, true, true, authList);
			user.setId(codDpeWebEdicola);
			user.setCodEdicolaDl(codEdicolaDl);
			user.setCodDpeWebEdicola(codDpeWebEdicola);
			user.setCodFiegDl(codFiegDl);
			user.setCodUtente(userId);
			user.setStatoAttivo(true);
			user.setArrId(new Integer[]{codDpeWebEdicola});
			user.setArrCodFiegDl(new Integer[]{codFiegDl});
			user.setCodFiegDlMaster(codFiegDl);
			user.setCodEdicolaMaster(codDpeWebEdicola);
			
			GruppoModuliVo gruppoModuli = utente.getDlGruppoModuliVo().getGruppoModuli();
			user.setCodGruppo(gruppoModuli.getId());
			
			
			
			if (codFiegDlSecondario != null 
					&& (user.getEdicolaDeviettiTodis() || abbinamentoEdicolaDlVo.getCodEdicolaMaster() != null)
					&& !user.isCodiceDLCavaglia()) {
				Integer codFiegDlMaster = anagraficaAgenziaVo.getCodFiegDlMaster();
				if (anagraficaAgenziaVo.getDlInforiv()) {
					List<CodDlEdicoleSecondarieDto> listCodDpeWebEdicola = getListCodDlECodEdicolaSecondarie(abbinamentoEdicolaDlVo.getCodEdicolaMaster());
					user.setArrId(extract(listCodDpeWebEdicola, on(CodDlEdicoleSecondarieDto.class).getCodDpeWebEdicola()).toArray(new Integer[]{}));
					user.setArrCodFiegDl(extract(listCodDpeWebEdicola, on(CodDlEdicoleSecondarieDto.class).getCodFiegDl()).toArray(new Integer[]{}));
					user.setCodFiegDlMaster(codFiegDlMaster);
					user.setCodEdicolaMaster(iGerivUtils.getCorrispondenzaCodEdicolaMultiDl(codFiegDlMaster, user.getArrCodFiegDl(), user.getArrId()));
				} else {
					Integer codDpeWebEdicolaMaster = getCodDpeWebEdicolaSecondaria(codFiegDlMaster, codEdicolaDl);
					Integer codDpeWebEdicolaSecodaria = getCodDpeWebEdicolaSecondaria(codFiegDlSecondario, codEdicolaDl);
					user.setCodFiegDlSecondario(codFiegDlSecondario);
					user.setCodFiegDlMaster(codFiegDlMaster);
					user.setCodEdicolaMaster(codDpeWebEdicolaMaster);
					user.setCodDpeWebEdicolaSecodaria(codDpeWebEdicolaSecodaria);
					user.setArrId(new Integer[]{codDpeWebEdicola, codDpeWebEdicolaSecodaria});
					user.setArrCodFiegDl(new Integer[]{codFiegDl, codFiegDlSecondario});
				}
				user.setMultiDl(true);
				List<AnagraficaAgenziaVo> listDl = new ArrayList<AnagraficaAgenziaVo>();
				listDl.add(anagraficaAgenziaVo);
				listDl.add(anagraficaAgenziaVo.getAgenziaSecondaria());
				user.setListDl(listDl);
			}
			user.setRagioneSocialeDl(anagraficaAgenziaVo.getRagioneSocialeDlPrimaRiga());
			user.setIndirizzoAgenziaPrimaRiga(anagraficaAgenziaVo.getIndirizzoDlPrimaRiga());
			user.setLocalitaAgenziaPrimaRiga(anagraficaAgenziaVo.getLocalitaDlPrimaRiga());
			user.setProvinciaAgenzia(anagraficaAgenziaVo.getSiglaProvincia());
			user.setCapAgenzia(anagraficaAgenziaVo.getCap());
			user.setPivaAgenzia(anagraficaAgenziaVo.getPiva());
			AnagraficaEdicolaVo anagraficaEdicolaVo = abbinamentoEdicolaDlVo.getAnagraficaEdicolaVo();
			user.setRagioneSocialeEdicola(anagraficaEdicolaVo.getRagioneSocialeEdicolaPrimaRiga());
			user.setIndirizzoEdicolaPrimaRiga(anagraficaEdicolaVo.getIndirizzoEdicolaPrimaRiga());
			user.setLocalitaEdicolaPrimaRiga(anagraficaEdicolaVo.getLocalitaEdicolaPrimaRiga());
			user.setProvinciaEdicola(anagraficaEdicolaVo.getSiglaProvincia());
			user.setCapEdicola(anagraficaEdicolaVo.getCap());
			user.setNumMaxCpuResaDimenticata(anagraficaAgenziaVo.getNumMaxCpuResaDimeticata());
			user.setEmail(anagraficaEdicolaVo.getEmail());
			user.setEmailDl(anagraficaAgenziaVo.getEmail());
			user.setChangePassword(utente.getChangePassword() != null && utente.getChangePassword().equals(1) ? true : false);
			user.setRtaeAccessEnabled(anagraficaAgenziaVo.isRtaeAccessEnabled());
			user.setAdmin(utente.getUtenteAmministratore() != null && utente.getUtenteAmministratore().equals(1) ? true : false);
			user.setTipoUtente(IGerivConstants.TIPO_UTENTE_EDICOLA);
			user.setPwdCriptata(utente.getPwdCriptata() != null && utente.getPwdCriptata().equals(1) ? true : false);
			user.setGruppoSconto(abbinamentoEdicolaDlVo.getGruppoSconto());
			user.setVisualizzaSemaforoGiacenza(anagraficaAgenziaVo.getVisualizzaSemaforoGiacenza());
			user.setDtSospensioneEdicola(abbinamentoEdicolaDlVo.getDtSospensioneEdicola());
			user.setCodContabileCliente(abbinamentoEdicolaDlVo.getCodiceContabileCliente());
			user.setCodiceFiscale(anagraficaEdicolaVo.getCodiceFiscale());
			user.setPiva(anagraficaEdicolaVo.getPiva());
			user.setImgLogo(anagraficaAgenziaVo.getImgLogo());
			user.setUrlDL(anagraficaAgenziaVo.getUrl());
			user.setEdicolaTest(abbinamentoEdicolaDlVo.getEdicolaTest());
			user.setEmailReplyToInstantMessages(anagraficaAgenziaVo.getEmailReplyToInstantMessages());
			user.setReturnReceiptTo(anagraficaAgenziaVo.isReturnReceiptTo());
			user.setDlInforiv(anagraficaAgenziaVo.getDlInforiv());
			user.setDataStorico(abbinamentoEdicolaDlVo.getDataCreazioneStatistica());
			user.setGiornoSettimanaPermessaResaDimenticata(anagraficaAgenziaVo.getGiornoSettimanaPermessaResaDimenticata());
			user.setTipoControlloPubblicazioniRespinte(anagraficaAgenziaVo.getTipoControlloPubblicazioniRespinte());
			user.setAbilitataCorrezioneBarcode(abbinamentoEdicolaDlVo.getAbilitataCorrezioneBarcode());
			user.setHasButtonCopiaDifferenze(anagraficaAgenziaVo.getHasButtonCopiaDifferenze());
			user.setHasResaAnticipata(anagraficaAgenziaVo.getHasResaAnticipata());
			user.setEdicolaInGruppoScontoBase(anagraficaAgenziaVo.getGruppoScontoBase() != null && abbinamentoEdicolaDlVo.getGruppoSconto() != null && abbinamentoEdicolaDlVo.getGruppoSconto().equals(anagraficaAgenziaVo.getGruppoScontoBase()));
			user.setEdicolaStarter(abbinamentoEdicolaDlVo.getEdicolaStarter());
			user.setRichiestaProva(abbinamentoEdicolaDlVo.getRichiestaProva());
			user.setDataInizioEstrattoContoPdf(anagraficaAgenziaVo.getDataInizioEstrattoContoPdf());
			user.setDtAttivazioneEdicola(abbinamentoEdicolaDlVo.getDtAttivazioneEdicola());
			user.setEdicolaTestPerModifiche(abbinamentoEdicolaDlVo.getEdicolaTestPerModifiche());
			user.setPermetteInserimentoRichiesteRifornimentoFuture(anagraficaAgenziaVo.getPermetteInserimentoRichiesteRifornimentoFuture());
			user.setSpuntaObbligatoriaBollaConsegna(anagraficaAgenziaVo.getSpuntaObbligatoriaBollaConsegna());
			user.setSuddivisioneQuotidianiPeriodiciReportVenduto(anagraficaAgenziaVo.getSuddivisioneQuotidianiPeriodiciReportVenduto());
			
			//Ticket 0000045
			//user.setHasLivellamenti(anagraficaAgenziaVo.getHasLivellamenti() && livellamentiService.getEdicoleVicineVo(user.getCodDpeWebEdicola()) != null);
			user.setHasLivellamenti(anagraficaAgenziaVo.getHasLivellamenti());
			
			user.setCheckConsegneGazzetta(abbinamentoEdicolaDlVo.getCheckConsegneGazzetta());
			user.setAccettoResaCD(abbinamentoEdicolaDlVo.getAccettoResaCD());
			// Vittorio 26/08/2020
			user.setGesSepDevTod(abbinamentoEdicolaDlVo.getGesSepDevTod());
			user.setAgenziaFatturazione(abbinamentoEdicolaDlVo.getAgenziaFatturazione());
			user.setEdSecCintura(abbinamentoEdicolaDlVo.getEdSecCintura());
			
			// Vittorio 11/01/2021
			user.setDtPartSecondaCintura(anagraficaAgenziaVo.getDtPartSecondaCintura());
			
			List<AbbinamentoEdicolaDlVo> edicoleAutorizzateAggiornaBarcode = edicoleService.getEdicoleAutorizzateAggiornamentoBarcode(codFiegDl);
			user.setHasEdicoleAutorizzateAggiornaBarcode(edicoleAutorizzateAggiornaBarcode != null && !edicoleAutorizzateAggiornaBarcode.isEmpty());
			List<MenuModuloVo> moduli = utente.getDlGruppoModuliVo().getGruppoModuli().getModuli();
			forEach(moduli).setAttivo(true);
			String roleName = (user.getAuthorities() != null && !user.getAuthorities().isEmpty()) ? (user.getAuthorities().iterator().next().getAuthority()) : "";
			boolean addModuliDisabilitati = roleName.equals(IGerivConstants.ROLE_IGERIV_STARTER);
			if (addModuliDisabilitati) {
				List<MenuModuloVo> moduliAdmin = menuService.getModuliByRoleName(IGerivConstants.ROLE_IGERIV_BASE_ADMIN);
				for (MenuModuloVo ma : moduliAdmin) {
					if (!moduli.contains(ma)) {
						ma.setAttivo(false);
						moduli.add(ma);
					}
				}
			}
			List<List<List<MenuModuloVo>>> list = menuService.buildListModuli(moduli);
			if (!user.getHasLivellamenti()) {
				removeMenuLivellamenti(list);
			}
			user.setModuli(list);
			user.setHasBolle(getMenu(list, IGerivMessageBundle.get("igeriv.menu.5")));
			user.setHasVendite(getMenu(list, IGerivMessageBundle.get("igeriv.menu.59")));
			user.setHasClienti(getMenu(list, IGerivMessageBundle.get("igeriv.menu.11")));
			user.setHasProdottiVari(getMenu(list, IGerivMessageBundle.get("igeriv.menu.42")));
			user.setHasPrenotazioni(getMenu(list, IGerivMessageBundle.get("igeriv.menu.40")));
			user.setEmailValido(findEmailValido(user, abbinamentoEdicolaDlVo.getEmailValido()));
			user.setGestioneAnagraficaRivenditaObbligatoria(anagraficaAgenziaVo.getGestioneAnagraficaRivenditaObbligatoria());
			user.setAnagraficaCompilata(abbinamentoEdicolaDlVo.getAnagraficaCompilata());
			user.setCondizioniUsoAccettate(abbinamentoEdicolaDlVo.getCondizioniUsoAccettate());
			user.setVisualizzaResoRiscontratoStatistica(anagraficaAgenziaVo.getVisualizzaResoRiscontratoStatistica());
			user.setPrenotazioniEvasioneQuantitaEvasaEmpty(anagraficaAgenziaVo.getPrenotazioniEvasioneQuantitaEvasaEmpty());
			user.setEdicoleVedonoMessaggiDpe(anagraficaAgenziaVo.getEdicoleVedonoMessaggiDpe());
			user.setFotoEdicolaInserita(anagraficaEdicolaVo.getAnagraficaEdicolaAggiuntiviVo() != null && Hibernate.isInitialized(anagraficaEdicolaVo.getAnagraficaEdicolaAggiuntiviVo()) && anagraficaEdicolaVo.getAnagraficaEdicolaAggiuntiviVo().getImmagine1() != null && Hibernate.isInitialized(anagraficaEdicolaVo.getAnagraficaEdicolaAggiuntiviVo().getImmagine1()));
			user.setVenditeEsauritoControlloGiacenzaDL(anagraficaAgenziaVo.getVenditeEsauritoControlloGiacenzaDL());
			user.setHasPopupConfermaSuMemorizzaInviaBolle(anagraficaAgenziaVo.getHasPopupConfermaSuMemorizzaInviaBolle());
			user.setHasMessaggioDocumentoDisponibile(anagraficaAgenziaVo.getHasMessaggioDocumentoDisponibile());
			user.setEdicolaPromo(abbinamentoEdicolaDlVo.getIsEdicolaPromo());
			
			//17/01/2016 - Inserito per gestire in CDL il profilo BASIC
			user.setRoleIdProfile(utente.getDlGruppoModuliVo().getGruppoModuli().getId());
			user.setRoleNameProfile(utente.getDlGruppoModuliVo().getGruppoModuli().getRoleName());
			
			//Vittorio 09/11/18
			user.setGruppoModuliVo(utente.getDlGruppoModuliVo().getGruppoModuli());
			
		}
		return user;
	}
	
	/**
	 * Toglie il menu della Rete Edicola (Livellamenti)
	 * 
	 * @param list
	 */
	private void removeMenuLivellamenti(List<List<List<MenuModuloVo>>> list) {
		List<Object> flatten = flatten(list);
		
		MenuModuloVo anagraficaEdicolaModuloVo = selectFirst(flatten, having(on(MenuModuloVo.class).getPropKey(), equalTo("igeriv.menu.95")));
		if (anagraficaEdicolaModuloVo != null) {
			Integer idPadre = anagraficaEdicolaModuloVo.getIdModuloPadre();
			
			List<List<List<MenuModuloVo>>> moduli = list;
			for (List<List<MenuModuloVo>> firstLevel : moduli) {
				boolean found = false;
				MenuModuloVo modulo = firstLevel.get(0).get(0);
				if (modulo.getId().equals(idPadre)) {
					// Trovato il padre
					for (int i = 1, n = firstLevel.size(); i < n; i++) {
						modulo = firstLevel.get(i).get(0);
						if (modulo.getPropKey().equals("igeriv.menu.95")) {
							// Trovato il modulo
							firstLevel.remove(i);
							found = true;
							break;
						}
					}
				}
				if (found) {
					break;
				}
			}
		}
		
	}

	@Override
	public UserAbbonato buildDlUserDetails(String userId, BaseVo utenteBase) {
		UtenteAgenziaVo utente = (UtenteAgenziaVo) utenteBase;
		AnagraficaAgenziaVo ag = utente != null ? utente.getAgenzia() : null;
		UserAbbonato user = null;
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		if (utente != null && ag != null) {
			GruppoModuliVo gruppoModuli = utente.getDlGruppoModuliVo().getGruppoModuli();
			authList.add(new GrantedAuthorityImpl(gruppoModuli.getRoleName()));
			user = new UserAbbonato(userId, utente.getPasswordDl(), true, true, true, true, authList);
			user.setId(new Integer(userId));
			user.setCodUtente(userId);
			user.setStatoAttivo(true);
			user.setNome(ag.getRagioneSocialeDlPrimaRiga());
			user.setCognome("");
			user.setRagioneSocialeDl(ag.getRagioneSocialeDlPrimaRiga());
			user.setRagioneSocialeEdicola(ag.getRagioneSocialeDlPrimaRiga());
			user.setIndirizzoEdicolaPrimaRiga(ag.getIndirizzoDlPrimaRiga());
			user.setIndirizzoAgenziaPrimaRiga(ag.getIndirizzoDlPrimaRiga());
			user.setLocalitaEdicolaPrimaRiga(ag.getLocalitaDlPrimaRiga());
			user.setLocalitaAgenziaPrimaRiga(ag.getLocalitaDlPrimaRiga());
			user.setCodDpeWebEdicola(ag.getCodDpeWebDl());
			user.setCodFiegDl(ag.getCodFiegDl());
			user.setCodEdicolaDl(ag.getCodFiegDl());
			user.setEmail(utente.getEmail());
			user.setFtpServerGestionaleAddress(ag.getFtpServerGestionaleAddress());
			user.setFtpServerGestionaleUser(ag.getFtpServerGestionaleUser());
			user.setFtpServerGestionalePwd(ag.getFtpServerGestionalePwd());
			user.setFtpServerGestionaleDir(ag.getFtpServerGestionaleDir());
			user.setHttpProxyServer(ag.getHttpProxyServer());
			user.setHttpProxyPort(ag.getHttpProxyPort());
			user.setChangePassword(utente.getChangePassword() != null && utente.getChangePassword().equals(1) ? true : false);
			user.setRtaeAccessEnabled(ag.isRtaeAccessEnabled());
			user.setTipoUtente(IGerivConstants.TIPO_UTENTE_DL);
			user.setCodGruppo(gruppoModuli.getId());
			user.setPwdCriptata(true);
			user.setImgLogo(ag.getImgLogo());
			user.setUrlDL(ag.getUrl());
			user.setGestioneAnagraficaRivenditaObbligatoria(ag.getGestioneAnagraficaRivenditaObbligatoria());
			List<MenuModuloVo> moduli = gruppoModuli.getModuli();
			forEach(moduli).setAttivo(true);
			List<List<List<MenuModuloVo>>> list = menuService.buildListModuli(moduli);
			user.setModuli(list);
		}
		return user;
	}
	
	@Override
	public List<UserVo> getEdicolaByCodiceGruppo(Integer idGruppo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserVo.class, "vo");
		criteria.createCriteria("vo.abbinamentoEdicolaDlVo", "ab", JoinType.INNER_JOIN);
		criteria.createCriteria("vo.dlGruppoModuliVo", "dlgr", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("dlgr.pk.codGruppo", idGruppo));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	/**
	 * @param list
	 * @param titolo
	 * @return
	 */
	private Boolean getMenu(List<List<List<MenuModuloVo>>> list, String titolo) {
		for (List<List<MenuModuloVo>> l1 : list) {
			for (List<MenuModuloVo> l2 : l1) {
				for (MenuModuloVo menu : l2) {
					if (!Strings.isNullOrEmpty(menu.getTitolo()) && menu.getTitolo().contains(titolo)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Cerca se la mail è stata validata dall'utente (invio di email di conferma e clic sul link di validazione)
	 * Nel caso di edicola multi-dl verifico:
	 * 1. Per edicola devietti/todis:  se uno degli utenti con lo stesso crivdl ha già validato la mail. 
	 * 2. Per edicola multi-dl inforiv: se una delle edicole con lo stesso cod. edicola master ha già validato la mail.
	 * 
	 * @param user
	 * @param isEmailValidoOnCurrentUser
	 * @return
	 */
	private Boolean findEmailValido(UserAbbonato user, Boolean isEmailValidoOnCurrentUser) {
		Boolean isValid = false;
		Integer coddl = user.getCodFiegDl();
		if (!coddl.equals(IGerivConstants.CDL_CODE)) {
			if (user.isMultiDl()) {
				isValid = edicoleService.getEmailValidoMultiDl(user.getArrId(), user.getArrCodFiegDl());
			} else {
				isValid = (!Strings.isNullOrEmpty(user.getEmail()) && isEmailValidoOnCurrentUser);
			}
		} else {
			isValid = true;
		}
		return isValid;
	}

	@Override
	public void saveDtInserimentoDtSospensioneEdicola(Integer codFiegDl, Integer codDpeWebEdicola, Timestamp dtAttivazioneEdicola, Timestamp dtSospensioneEdicola) {
		AbbinamentoEdicolaDlVo aedl = edicoleService.getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(codFiegDl, codDpeWebEdicola);
		aedl.setDtAttivazioneEdicola(dtAttivazioneEdicola);
		aedl.setDtSospensioneEdicola(dtSospensioneEdicola);
		edicoleService.saveBaseVo(aedl);
	}

	@Override
	public void saveProfilazioneEdicola(PianoProfiliEdicolaVo profiloEdicola) {
		/*
		DetachedCriteria criteria = DetachedCriteria.forClass(PianoProfiliEdicolaVo.class, "prof");
		criteria.add(Restrictions.eq("prof.codDpeWebEdicola", profiloEdicola.getCodDpeWebEdicola()));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
		*/
		//edicoleService.saveBaseVo(profiloEdicola);
		if(profiloEdicola!=null)
			saveBaseVo(profiloEdicola);
		
	}

	@Override
	public void deleteProfilazioneEdicola(Integer codFiegDl, Integer codDpeWebEdicola, Timestamp dtAttivazioneProfiloEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PianoProfiliEdicolaVo.class, "prof");
		criteria.createCriteria("prof.anagraficaAgenziaVo", "ab", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("ab.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("prof.codDpeWebEdicola", codDpeWebEdicola));
		criteria.add(Restrictions.eq("prof.dtAttivazioneProfiloEdicola", dtAttivazioneProfiloEdicola));
		PianoProfiliEdicolaVo pianoProf = getDao().findUniqueResultByDetachedCriteria(criteria);
		if(pianoProf!=null)
			edicoleService.deleteVo(pianoProf);
	
	
	}
	
	
}
