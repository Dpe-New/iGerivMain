package it.dpe.igeriv.bo.livellamenti;

import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.ArretratiDto;
import it.dpe.igeriv.dto.GiacenzaPneDto;
import it.dpe.igeriv.dto.LivellamentiDto;
import it.dpe.igeriv.enums.StatoRichiestaLivellamento;
import it.dpe.igeriv.enums.StatoRichiestaPropostaFilterLivellamento;
import it.dpe.igeriv.enums.StatoRichiestaRifornimentoLivellamento;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.EdicoleVicineVo;
import it.dpe.igeriv.vo.LivellamentiVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoLivellamentiVo;
import it.dpe.igeriv.vo.pk.EdicoleVicinePk;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

@Repository
class LivellamentiRepositoryImpl extends BaseRepositoryImpl implements LivellamentiRepository<LivellamentiVo> {
	
	@Autowired
	LivellamentiRepositoryImpl(BaseDao<ArretratiDto> dao) {
		super(dao);
	}
	
	@Override
	public List<Long> getIdRichiesteLivellamentiInserite(Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LivellamentiVo.class, "li");
		criteria.createCriteria("li.edicola", "ed");
		criteria.add(Restrictions.eq("li.statoRichiesta", StatoRichiestaLivellamento.INSERITO));
		criteria.add(Restrictions.eq("li.statoVendita", StatoRichiestaLivellamento.INSERITO));
		//criteria.add(Restrictions.ge("li.dataScadenza", getSysdate()));
		criteria.add(Restrictions.eq("ed.codEdicola", codEdicola));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("li.idLivellamento"));
		criteria.setProjection(props);
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<LivellamentiVo> getRichiesteLivellamentiAccettate(Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LivellamentiVo.class, "li");
		criteria.createCriteria("li.edicola", "ed");
		criteria.createCriteria("li.richiesta", "ri");
		criteria.createCriteria("ri.edicolaRichiedente", "er");
		criteria.add(Restrictions.eq("li.statoRichiesta", StatoRichiestaLivellamento.INSERITO));
		criteria.add(Restrictions.eq("li.statoVendita", StatoRichiestaLivellamento.ACCETTATO));
		//criteria.add(Restrictions.ge("li.dataScadenza", getSysdate()));
		criteria.add(Restrictions.eq("er.codEdicola", codEdicola));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<LivellamentiVo> getLivellamentiByIdRichiestaLivellamento(List<Long> idLivellamenti) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LivellamentiVo.class, "li");
		criteria.createCriteria("li.edicola", "ed");
		criteria.createCriteria("li.richiesta", "ri");
		criteria.createCriteria("ri.edicolaRichiedente", "er");
		criteria.createCriteria("ri.storicoCopertineVo", "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "pub");
		//criteria.add(Restrictions.in("li.idLivellamento", idLivellamenti));
		criteria.add(Restrictions.in("li.richiesta.idRichiestaLivellamento", idLivellamenti));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	
	public List<LivellamentiVo> getAllLivellamentiAccettatiByIdRichiestaLivellamento(Long idRichiestaLivellamenti){
		DetachedCriteria criteria = DetachedCriteria.forClass(LivellamentiVo.class, "li");
		criteria.createCriteria("li.edicola", "ed");
		criteria.createCriteria("li.richiesta", "ri");
		criteria.createCriteria("ri.edicolaRichiedente", "er");
		criteria.createCriteria("ri.storicoCopertineVo", "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "pub");
		//criteria.add(Restrictions.in("li.idLivellamento", idLivellamenti));
		criteria.add(Restrictions.eq("li.statoVendita", StatoRichiestaLivellamento.ACCETTATO));
		criteria.add(Restrictions.eq("li.richiesta.idRichiestaLivellamento", idRichiestaLivellamenti));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	public List<LivellamentiVo> getAllLivellamentiAccettatiByIdRichiestaLivellamento(List<Long> idRichiestaLivellamenti){
		DetachedCriteria criteria = DetachedCriteria.forClass(LivellamentiVo.class, "li");
		criteria.createCriteria("li.edicola", "ed");
		criteria.createCriteria("li.richiesta", "ri");
		criteria.createCriteria("ri.edicolaRichiedente", "er");
		criteria.createCriteria("ri.storicoCopertineVo", "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "pub");
		//criteria.add(Restrictions.in("li.idLivellamento", idLivellamenti));
		criteria.add(Restrictions.eq("li.statoVendita", StatoRichiestaLivellamento.ACCETTATO));
		criteria.add(Restrictions.in("li.richiesta.idRichiestaLivellamento", idRichiestaLivellamenti));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	public List<LivellamentiVo> getAllLivellamentiNonAccettatiByIdRichiestaLivellamento(List<Long> idRichiestaLivellamenti,List<Long> listIdLivellamentiAccettati){
		DetachedCriteria criteria = DetachedCriteria.forClass(LivellamentiVo.class, "li");
		criteria.createCriteria("li.edicola", "ed");
		criteria.createCriteria("li.richiesta", "ri");
		criteria.createCriteria("ri.edicolaRichiedente", "er");
		criteria.createCriteria("ri.storicoCopertineVo", "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "pub");
		//criteria.add(Restrictions.in("li.idLivellamento", idLivellamenti));
		criteria.add(Restrictions.ne("li.statoRichiesta", StatoRichiestaLivellamento.ACCETTATO));
		criteria.add(Restrictions.in("li.richiesta.idRichiestaLivellamento", idRichiestaLivellamenti));
		criteria.add(Restrictions.not(Restrictions.in("li.idLivellamento", listIdLivellamentiAccettati)));
				
				
		return getDao().findByDetachedCriteria(criteria);
	}
	
	public List<LivellamentiVo> getLivellamentiAccettatiDaRitirareByIdRichiesta(List<Long> idRichiestaLivellamenti){
		DetachedCriteria criteria = DetachedCriteria.forClass(LivellamentiVo.class, "li");
		criteria.createCriteria("li.edicola", "ed");
		criteria.createCriteria("li.richiesta", "ri");
		criteria.createCriteria("ri.edicolaRichiedente", "er");
		criteria.createCriteria("ri.storicoCopertineVo", "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "pub");
		criteria.add(Restrictions.in("li.richiesta.idRichiestaLivellamento", idRichiestaLivellamenti));
		criteria.add(Restrictions.eq("ri.stato", StatoRichiestaRifornimentoLivellamento.DA_RITIRARE));
		criteria.add(Restrictions.eq("li.statoRichiesta", StatoRichiestaLivellamento.ACCETTATO));
		
		return getDao().findByDetachedCriteria(criteria);
	}

	
	public List<LivellamentiVo> getLivellamentiAccettatiRitiratiByIdRichiesta(List<Long> idRichiestaLivellamenti){
		DetachedCriteria criteria = DetachedCriteria.forClass(LivellamentiVo.class, "li");
		criteria.createCriteria("li.edicola", "ed");
		criteria.createCriteria("li.richiesta", "ri");
		criteria.createCriteria("ri.edicolaRichiedente", "er");
		criteria.createCriteria("ri.storicoCopertineVo", "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "pub");
		criteria.add(Restrictions.in("li.richiesta.idRichiestaLivellamento", idRichiestaLivellamenti));
		criteria.add(Restrictions.eq("ri.stato", StatoRichiestaRifornimentoLivellamento.RITIRATI));
		criteria.add(Restrictions.eq("li.statoVendita", StatoRichiestaLivellamento.VENDUTO));
		
		return getDao().findByDetachedCriteria(criteria);
	}
	
	
	public List<LivellamentiDto> getLivellamentiDtoAccettatiDaRitirareByIdRichiesta(final Long idRichiestaLivellamenti){
	
		HibernateCallback<List<GiacenzaPneDto>> action = new HibernateCallback<List<GiacenzaPneDto>>() {
		@SuppressWarnings("unchecked")
		@Override
		public List<GiacenzaPneDto> doInHibernate(Session session) throws HibernateException, SQLException {
			String query = IGerivQueryContants.SQL_GET_LIVELLAMENTI_MAIL_ACCETTAZIONE_PUBBLICAZIONI;
			
			SQLQuery createSQLQuery = session.createSQLQuery(query);
			createSQLQuery.setInteger("idRichiesta", idRichiestaLivellamenti.intValue());
			
			createSQLQuery.setResultTransformer( Transformers.aliasToBean(LivellamentiDto.class));
			createSQLQuery.addScalar("coddl", IntegerType.INSTANCE);
			createSQLQuery.addScalar("codEdicola", IntegerType.INSTANCE);
			createSQLQuery.addScalar("ragioneSocialeEdicolaPrimaRiga", StringType.INSTANCE);
			createSQLQuery.addScalar("titolo", StringType.INSTANCE);
			createSQLQuery.addScalar("dataUscita", TimestampType.INSTANCE);
			createSQLQuery.addScalar("numeroCopertina", StringType.INSTANCE);
			createSQLQuery.addScalar("quantitaRichiesta", IntegerType.INSTANCE);
			return createSQLQuery.list();
			}
		};
	return getDao().findByHibernateCallback(action);
	
	}
	
	
	
	public List<LivellamentiVo> getLivellamentiAccettatiByIdRichiestaLivellamentoIdEdicola(Long idRichiestaLivellamenti, Integer idEdicola){
		DetachedCriteria criteria = DetachedCriteria.forClass(LivellamentiVo.class, "li");
		criteria.createCriteria("li.edicola", "ed");
		criteria.createCriteria("li.richiesta", "ri");
		criteria.createCriteria("ri.edicolaRichiedente", "er");
		criteria.createCriteria("ri.storicoCopertineVo", "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "pub");
		criteria.add(Restrictions.eq("li.statoVendita", StatoRichiestaLivellamento.ACCETTATO));
		criteria.add(Restrictions.eq("li.richiesta.idRichiestaLivellamento", idRichiestaLivellamenti));
		criteria.add(Restrictions.eq("ed.codEdicola", idEdicola));

		return getDao().findByDetachedCriteria(criteria);
	
	}
	
	
	public List<LivellamentiVo> getLivellamentiInStatoAccettatoByIdRichiestaLivellamentoIdEdicola(Long idRichiestaLivellamenti, Integer idEdicola){
		DetachedCriteria criteria = DetachedCriteria.forClass(LivellamentiVo.class, "li");
		criteria.createCriteria("li.edicola", "ed");
		criteria.createCriteria("li.richiesta", "ri");
		criteria.createCriteria("ri.edicolaRichiedente", "er");
		criteria.createCriteria("ri.storicoCopertineVo", "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "pub");
		criteria.add(Restrictions.eq("li.statoRichiesta", StatoRichiestaLivellamento.ACCETTATO));
		criteria.add(Restrictions.eq("li.richiesta.idRichiestaLivellamento", idRichiestaLivellamenti));
		criteria.add(Restrictions.eq("ed.codEdicola", idEdicola));
		criteria.add(Restrictions.isNull("li.dataVendita"));
		
		return getDao().findByDetachedCriteria(criteria);
	
	}
	
	
	@Override
	public List<LivellamentiVo> getLivellamentiByIdLivellamenti(List<Long> idLivellamenti) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LivellamentiVo.class, "li");
		criteria.createCriteria("li.edicola", "ed");
		criteria.createCriteria("li.richiesta", "ri");
		criteria.createCriteria("ri.edicolaRichiedente", "er");
		criteria.createCriteria("ri.storicoCopertineVo", "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "pub");
		criteria.add(Restrictions.in("li.idLivellamento", idLivellamenti));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	
	@Override
	public RichiestaRifornimentoLivellamentiVo getRichiestaRifornimentoLivellamenti(final Integer[] codDl, final Integer[] codEdicola, final Integer idtn, final List<StatoRichiestaRifornimentoLivellamento> stato) {
		HibernateCallback<RichiestaRifornimentoLivellamentiVo> action = new HibernateCallback<RichiestaRifornimentoLivellamentiVo>() {
			@Override
			public RichiestaRifornimentoLivellamentiVo doInHibernate(Session session) throws HibernateException, SQLException {
				Query namedQuery = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_GET_RICHIESTA_RIFORNIMENTO_LIVELLAMENTI_CON_DETTAGLI);
				namedQuery.setParameterList("codEdicola", codEdicola);
				namedQuery.setParameterList("codDl", codDl);
				namedQuery.setInteger("idtn", idtn);
				namedQuery.setParameterList("stato", stato);
				return (RichiestaRifornimentoLivellamentiVo) namedQuery.uniqueResult();
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public RichiestaRifornimentoLivellamentiVo getRichiestaRifornimentoLivellamentiByEdicolaVenditrice(final Integer codDl, final Integer codEdicola, final Integer idtn) {
		HibernateCallback<RichiestaRifornimentoLivellamentiVo> action = new HibernateCallback<RichiestaRifornimentoLivellamentiVo>() {
			@Override
			public RichiestaRifornimentoLivellamentiVo doInHibernate(Session session) throws HibernateException, SQLException {
				Query namedQuery = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_GET_RICHIESTA_RIFORNIMENTO_LIVELLAMENTI_CON_DETTAGLI_BY_EDICOLA_VENDITRICE);
				namedQuery.setInteger("codEdicola", codEdicola);
				namedQuery.setInteger("codDl", codDl);
				namedQuery.setInteger("idtn", idtn);
				return (RichiestaRifornimentoLivellamentiVo) namedQuery.uniqueResult();
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public RichiestaRifornimentoLivellamentiVo getRichiestaRifornimentoLivellamentiByCodEdicolaVenditrice(Integer codDl, Integer idtn){
		
			DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaRifornimentoLivellamentiVo.class, "li");
			criteria.createCriteria("li.edicolaRichiedente", "er");
			criteria.createCriteria("li.storicoCopertineVo", "sc");
			criteria.createCriteria("li.livellamenti", "liv");
			criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
			criteria.createCriteria("li.edicolaAccettataVenduto", "edav");
			
			criteria.add(Restrictions.eq("li.coddl", codDl));
			criteria.add(Restrictions.eq("li.idtn", idtn));
			criteria.add(Restrictions.eq("li.stato", StatoRichiestaRifornimentoLivellamento.DA_RITIRARE));
			criteria.addOrder(Order.desc("li.dataRichiesta"));
			//criteria.add(Restrictions.eq("edav.codEdicola", codEdicola));
			//criteria.add(Restrictions.eq("er.codEdicola", codEdicola));

			return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public RichiestaRifornimentoLivellamentiVo getRichiestaRifornimentoLivellamentiByEdicolaRichiedente(Integer codDl, Integer codEdicolaRichiedente, Integer idtn){
			DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaRifornimentoLivellamentiVo.class, "li");
			criteria.createCriteria("li.edicolaRichiedente", "er");
			criteria.createCriteria("li.storicoCopertineVo", "sc");
			criteria.createCriteria("li.livellamenti", "liv");
			criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
			criteria.createCriteria("li.edicolaAccettataVenduto", "edav");
			
			criteria.add(Restrictions.eq("li.coddl", codDl));
			criteria.add(Restrictions.eq("er.codEdicola", codEdicolaRichiedente));
			criteria.add(Restrictions.eq("li.idtn", idtn));
			criteria.add(Restrictions.eq("li.stato", StatoRichiestaRifornimentoLivellamento.DA_RITIRARE));
			
			criteria.addOrder(Order.desc("li.dataRichiesta"));
			//criteria.add(Restrictions.eq("edav.codEdicola", codEdicola));
			//criteria.add(Restrictions.eq("er.codEdicola", codEdicola));
	
			return getDao().findUniqueResultByDetachedCriteria(criteria);
		
	}

	
	
	@Override
	public boolean isValidCodEdicola(Long idRichiestaLivellamenti,Integer codiceVenditaReteEdicole){
		DetachedCriteria criteria = DetachedCriteria.forClass(LivellamentiVo.class, "li");
		criteria.createCriteria("li.edicola", "ed");
		criteria.createCriteria("li.richiesta", "ri");
		criteria.createCriteria("ri.edicolaRichiedente", "er");
		criteria.createCriteria("ri.storicoCopertineVo", "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "pub");
		criteria.add(Restrictions.eq("li.statoRichiesta", StatoRichiestaLivellamento.ACCETTATO));
		criteria.add(Restrictions.eq("li.richiesta.idRichiestaLivellamento", idRichiestaLivellamenti));
		criteria.add(Restrictions.eq("er.codEdicola", codiceVenditaReteEdicole));
		criteria.add(Restrictions.isNull("li.dataVendita"));
		
		List<LivellamentiVo> listRes = getDao().findByDetachedCriteria(criteria);
		if(listRes!=null && listRes.size()>0)return true;
		else return false;
	}
	
	@Override
	public boolean isCheckQuantita(Long idRichiestaLivellamenti,Integer codiceVenditaReteEdicole, Integer codiceEdicola, Integer quantita){
		DetachedCriteria criteria = DetachedCriteria.forClass(LivellamentiVo.class, "li");
		criteria.createCriteria("li.edicola", "ed");
		criteria.createCriteria("li.richiesta", "ri");
		criteria.createCriteria("ri.edicolaRichiedente", "er");
		criteria.createCriteria("ri.storicoCopertineVo", "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "pub");
		criteria.add(Restrictions.eq("li.statoRichiesta", StatoRichiestaLivellamento.ACCETTATO));
		criteria.add(Restrictions.eq("li.richiesta.idRichiestaLivellamento", idRichiestaLivellamenti));
		criteria.add(Restrictions.eq("er.codEdicola", codiceVenditaReteEdicole));
		criteria.add(Restrictions.eq("ed.codEdicola", codiceEdicola));
		criteria.add(Restrictions.isNull("li.dataVendita"));
		
		List<LivellamentiVo> listRes = getDao().findByDetachedCriteria(criteria);
		if(listRes!=null && listRes.size()== quantita.intValue())return true;
		else return false;
		
	}
		
	
	
	@Override
	public List<RichiestaRifornimentoLivellamentiVo> getRichiesteRifornimentoLivellamentiByIds(List<Long> listIds) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaRifornimentoLivellamentiVo.class, "rr");
		criteria.add(Restrictions.in("rr.idRichiestaLivellamento", listIds));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<EdicoleVicineVo> getListEdicoleDelGruppo(Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EdicoleVicineVo.class, "ev");
		criteria.createCriteria("ev.edicola", "ed");
		criteria.createCriteria("ed.anagraficaEdicolaVo", "ae");
		criteria.createCriteria("ed.anagraficaAgenziaVo", "ag");
		
//		DetachedCriteria subCriteria = DetachedCriteria.forClass(EdicoleVicineVo.class, "ev1");
//		subCriteria.add(Restrictions.eq("ev1.pk.codiceGruppo", codEdicola));
//		subCriteria.setProjection(Projections.property("ev1.pk.codiceEdicola"));
//		Integer codGruppo = getDao().findUniqueResultObjectByDetachedCriteria(subCriteria);
		
		criteria.add(Restrictions.eq("ev.pk.codiceGruppo", codEdicola));
		//criteria.add(Restrictions.ne("ev.pk.codiceEdicola", codGruppo));
		
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public EdicoleVicineVo getEdicoleVicineVo(Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EdicoleVicineVo.class, "ev");
		criteria.createCriteria("ev.edicola", "ed");
		criteria.createCriteria("ed.anagraficaEdicolaVo", "ae");
		criteria.createCriteria("ed.anagraficaAgenziaVo", "ag");
		criteria.add(Restrictions.eq("ev.pk.codiceEdicola", codEdicola));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public void saveEdicolaVicine(Integer codEdicolaWeb,Integer codEdicolaWebVicina) {
		
		EdicoleVicineVo ed = new EdicoleVicineVo();
		EdicoleVicinePk edPk = new EdicoleVicinePk();
		edPk.setCodiceGruppo(codEdicolaWeb);
		edPk.setCodiceEdicola(codEdicolaWebVicina);
		ed.setPk(edPk);
		saveBaseVo(ed);
		
		
	}

	@Override
	public void deleteEdicoleVicine(Integer codEdicolaWeb) {
		List<EdicoleVicineVo> findListEdicole = getListEdicoleDelGruppo(codEdicolaWeb);
		if(findListEdicole.size()>0){
			//Cencello le edicole vicine
			deleteVoList(findListEdicole);
		}
	}
	
	/**
	 * 09/01/2015
	 * Visualizzo le richieste presenti in tbl_9131a
	 */
	@Override
	public List<RichiestaRifornimentoLivellamentiVo> getRichiesteRifornimentiLivellamentiByDateStato(
			Integer codEdicola, Date dataDa, Date dataA,
			StatoRichiestaRifornimentoLivellamento stato) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaRifornimentoLivellamentiVo.class, "li");
		criteria.createCriteria("li.edicolaRichiedente", "er");
		criteria.createCriteria("li.storicoCopertineVo", "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");

		
		if (stato != null) {
			if (stato.equals(StatoRichiestaRifornimentoLivellamento.INSERITO)) {
				criteria.add(Restrictions.eq("li.stato", StatoRichiestaRifornimentoLivellamento.INSERITO));
			} else if (stato.equals(StatoRichiestaRifornimentoLivellamento.DA_RITIRARE)) {
				criteria.add(Restrictions.eq("li.stato", StatoRichiestaRifornimentoLivellamento.DA_RITIRARE));
			} else if (stato.equals(StatoRichiestaRifornimentoLivellamento.RITIRATI)) {
				criteria.add(Restrictions.eq("li.stato", StatoRichiestaRifornimentoLivellamento.RITIRATI));
			} else{
				//STATO NON CONTEMPLATO
			}
		}
		if (dataDa != null && dataA != null) {
			criteria.add(Restrictions.between("li.dataRichiesta", dataDa, dataA));
		}
		criteria.add(Restrictions.eq("er.codEdicola", codEdicola));
		List<RichiestaRifornimentoLivellamentiVo> findObjectByDetachedCriteria = getDao().findObjectByDetachedCriteria(criteria);
		
		return findObjectByDetachedCriteria;
	}
	
	@Override
	public List<LivellamentiVo> getRichiesteRifornimentoLivellamentiByDateStato(
			Integer codEdicola, Date dataDa, Date dataA,
			StatoRichiestaPropostaFilterLivellamento stato) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LivellamentiVo.class, "li");
		criteria.createCriteria("li.edicola", "ed");
		criteria.createCriteria("li.richiesta", "ri");
		criteria.createCriteria("ri.storicoCopertineVo", "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
		criteria.createCriteria("ri.edicolaRichiedente", "er");
		if (stato != null) {
			if (stato.equals(StatoRichiestaPropostaFilterLivellamento.INSERITI)) {
				criteria.add(Restrictions.eq("li.statoRichiesta", StatoRichiestaLivellamento.INSERITO));
				DetachedCriteria subCriteria = DetachedCriteria.forClass(LivellamentiVo.class, "li1");
				subCriteria.createCriteria("li1.richiesta", "ri1");
				subCriteria.add(Property.forName("ri1.idRichiestaLivellamento").eqProperty("ri.idRichiestaLivellamento"));
				subCriteria.add(Restrictions.in("li1.statoRichiesta", new Object[]{StatoRichiestaLivellamento.ACCETTATO, StatoRichiestaLivellamento.NON_ACCETTATO, StatoRichiestaLivellamento.VENDUTO}));
				criteria.add(Subqueries.notExists(subCriteria.setProjection(Projections.property("ri1.idRichiestaLivellamento"))));
			} else if (stato.equals(StatoRichiestaPropostaFilterLivellamento.DA_RITIRARE)) {
				criteria.add(Restrictions.eq("li.statoRichiesta", StatoRichiestaLivellamento.ACCETTATO));
				criteria.add(Restrictions.eq("li.statoVendita", StatoRichiestaLivellamento.ACCETTATO));
			} else if (stato.equals(StatoRichiestaPropostaFilterLivellamento.RITIRATI)) {
				criteria.add(Restrictions.eq("li.statoRichiesta", StatoRichiestaLivellamento.ACCETTATO));
				criteria.add(Restrictions.eq("li.statoVendita", StatoRichiestaLivellamento.VENDUTO));
			}
		}
		//criteria.add(Restrictions.ge("li.dataScadenza", getSysdate()));
		criteria.add(Restrictions.eq("er.codEdicola", codEdicola));
		List<LivellamentiVo> findObjectByDetachedCriteria = getDao().findObjectByDetachedCriteria(criteria);
		if (stato.equals(StatoRichiestaPropostaFilterLivellamento.INSERITI) && !findObjectByDetachedCriteria.isEmpty() 
				&& findObjectByDetachedCriteria.size() > findObjectByDetachedCriteria.get(0).getRichiesta().getQuantitaRichiesta()) {
			findObjectByDetachedCriteria = findObjectByDetachedCriteria.subList(0, findObjectByDetachedCriteria.get(0).getRichiesta().getQuantitaRichiesta());
		}
		return findObjectByDetachedCriteria;
	}
	
	@Override
	public List<LivellamentiVo> getProposteRifornimentoLivellamentiByDateStato(
			Integer codEdicola, Date dataDa, Date dataA,
			StatoRichiestaPropostaFilterLivellamento stato) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LivellamentiVo.class, "li");
		criteria.createCriteria("li.edicola", "ed");
		criteria.createCriteria("li.richiesta", "ri");
		criteria.createCriteria("ri.storicoCopertineVo", "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
		criteria.createCriteria("ri.edicolaRichiedente", "er");
		if (stato != null) {
			if (stato.equals(StatoRichiestaPropostaFilterLivellamento.DA_CONSEGNARE)) {
				criteria.add(Restrictions.eq("li.statoRichiesta", StatoRichiestaLivellamento.ACCETTATO));
				criteria.add(Restrictions.eq("li.statoVendita", StatoRichiestaLivellamento.ACCETTATO));
			} else if (stato.equals(StatoRichiestaPropostaFilterLivellamento.CONSEGNATI)) {
				criteria.add(Restrictions.eq("li.statoRichiesta", StatoRichiestaLivellamento.ACCETTATO));
				criteria.add(Restrictions.eq("li.statoVendita", StatoRichiestaLivellamento.VENDUTO));
			}
		}
		//criteria.add(Restrictions.ge("li.dataScadenza", getSysdate()));
		criteria.add(Restrictions.eq("ed.codEdicola", codEdicola));
		return getDao().findObjectByDetachedCriteria(criteria);
	}

	


	

	
	
	
}
