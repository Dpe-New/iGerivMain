package it.dpe.igeriv.bo.statistiche;

import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.StatisticaDettaglioDto;
import it.dpe.igeriv.dto.StatisticaUtilizzoDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.PaginaMonitorStatisticaVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoVo;
import it.dpe.igeriv.vo.StatistichePagineVo;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.igeriv.vo.VenditaDettaglioVo;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

@Repository
class StatisticheRepositoryImpl extends BaseRepositoryImpl implements StatisticheRepository {
	
	@Autowired
	StatisticheRepositoryImpl(BaseDao<UserVo> dao) {
		super(dao);
	}
	
	@Override
	public List<StatisticaDettaglioDto> getStatisticaDettaglioFornito(final Integer[] codFiegDl, final Integer[] codEdicola, final Integer idtn,final Timestamp dataStorico) {
		HibernateCallback<List<StatisticaDettaglioDto>> action = new HibernateCallback<List<StatisticaDettaglioDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<StatisticaDettaglioDto> doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery(IGerivQueryContants.SQL_QUERY_GET_STATISTICA_DETTAGLI_FORNITO);
				sqlQuery.setParameterList("coddl", codFiegDl);
				sqlQuery.setParameterList("codEdicola", codEdicola);
				sqlQuery.setInteger("idtn", idtn);
				sqlQuery.setTimestamp("dataStorico", dataStorico);
				sqlQuery.setResultTransformer(Transformers.aliasToBean(StatisticaDettaglioDto.class));
				sqlQuery.addScalar("data", TimestampType.INSTANCE);
				sqlQuery.addScalar("tipoBolla", StringType.INSTANCE);
				sqlQuery.addScalar("tipoFondoBolla", StringType.INSTANCE);
				sqlQuery.addScalar("copie", IntegerType.INSTANCE);
				return sqlQuery.list();
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public List<StatisticaDettaglioDto> getStatisticaDettaglioFornitoRifornimenti(final Integer[] codFiegDl, final Integer[] codEdicola, final Integer idtn) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaRifornimentoVo.class, "rrvo");
		criteria.add(Restrictions.in("rrvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.in("rrvo.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("rrvo.pk.idtn", idtn));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("rrvo.dataInvioRichiesta"), "data");
		properties.add(Projections.property("rrvo.quantitaEvasa"), "copie");
		criteria.setProjection(properties);
		criteria.setResultTransformer(Transformers.aliasToBean(StatisticaDettaglioDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<StatisticaDettaglioDto> getStatisticaDettaglioReso(final Integer[] codFiegDl, final Integer[] codEdicola, final Integer idtn) {
		HibernateCallback<List<StatisticaDettaglioDto>> action = new HibernateCallback<List<StatisticaDettaglioDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<StatisticaDettaglioDto> doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery(IGerivQueryContants.SQL_QUERY_GET_STATISTICA_DETTAGLIO_RESO);
				sqlQuery.setParameterList("coddl", codFiegDl);
				sqlQuery.setParameterList("codEdicola", codEdicola);
				sqlQuery.setInteger("idtn", idtn);
				sqlQuery.setResultTransformer(Transformers.aliasToBean(StatisticaDettaglioDto.class));
				sqlQuery.addScalar("data", TimestampType.INSTANCE);
				sqlQuery.addScalar("tipoBolla", StringType.INSTANCE);
				sqlQuery.addScalar("copie", IntegerType.INSTANCE);
				return sqlQuery.list();
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public List<StatisticaDettaglioDto> getStatisticaDettaglioResoRiscontrato(final Integer[] codFiegDl, final Integer[] codEdicola, final Integer idtn) {
		HibernateCallback<List<StatisticaDettaglioDto>> action = new HibernateCallback<List<StatisticaDettaglioDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<StatisticaDettaglioDto> doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = IGerivQueryContants.SQL_QUERY_GET_STATISTICA_DETTAGLIO_RESO_RISCONTRATO;
				SQLQuery sqlQuery = session.createSQLQuery(sql.toString());
				sqlQuery.setParameterList("coddl", codFiegDl);
				sqlQuery.setParameterList("codEdicola", codEdicola);
				sqlQuery.setInteger("idtn", idtn);
				sqlQuery.setResultTransformer(Transformers.aliasToBean(StatisticaDettaglioDto.class));
				sqlQuery.addScalar("data", TimestampType.INSTANCE);
				sqlQuery.addScalar("tipoBolla", StringType.INSTANCE);
				sqlQuery.addScalar("copie", IntegerType.INSTANCE);
				return sqlQuery.list();
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public List<StatisticaDettaglioDto> getStatisticaDettaglioVenduto(Integer[] codFiegDl, Integer[] codEdicola, Integer idtn) {
		DetachedCriteria criteria = DetachedCriteria.forClass(VenditaDettaglioVo.class, "vdvo");
		criteria.createCriteria("vdvo.venditaVo", "vvo");
		criteria.add(Restrictions.eq("vdvo.deleted", false));
		criteria.add(Restrictions.eq("vvo.deleted", false));
		criteria.add(Restrictions.in("vdvo.codFiegDl", codFiegDl));
		criteria.add(Restrictions.in("vdvo.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("vdvo.idtn", idtn));
		criteria.addOrder(Order.asc("vvo.dataVendita"));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("vvo.dataVendita"), "data");
		properties.add(Projections.property("vdvo.quantita"), "copie");
		criteria.setProjection(properties);
		criteria.setResultTransformer(Transformers.aliasToBean(StatisticaDettaglioDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<StatisticaUtilizzoDto> getStatisticheUtilizzo(final Integer codDl, final Timestamp dataIniziale, final Timestamp dataFinale, final Integer codRivendita) {
		HibernateCallback<List<StatisticaUtilizzoDto>> action = new HibernateCallback<List<StatisticaUtilizzoDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<StatisticaUtilizzoDto> doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer query = new StringBuffer();
				query.append("select codRivenditaDl, codRivenditaWeb, nomeRivendita, ");
				query.append("(select count(1) from tbl_9610 where coddl9610 = coddl and crivw9610 = codRivenditaWeb and ibotr9610 > 0 and datbc9610 between to_date(:dataIniziale, '" + DateUtilities.FORMATO_DATA + "') and to_date(:dataFinale, '" + DateUtilities.FORMATO_DATA_SLASH + "')) numBolle, ");
				query.append("(select count(1) from tbl_9620 where coddl9620 = coddl and crivw9620 = codRivenditaWeb and iretr9620 > 0 and datbr9620 between to_date(:dataIniziale, '" + DateUtilities.FORMATO_DATA + "') and to_date(:dataFinale, '" + DateUtilities.FORMATO_DATA_SLASH + "')) numRese, ");
				query.append("(select count(1) from tbl_9625, tbl_9626 where idven9626 = idven9625 and coddl9626 = coddl and crivw9626 = codRivenditaWeb and datve9625 between to_date(:dataIniziale, '" + DateUtilities.FORMATO_DATA + "') and to_date(:dataFinale, '" + DateUtilities.FORMATO_DATA_SLASH + "')) numVendite, ");
				query.append("(select count(1) from tbl_9131 where coddl9131 = coddl and crivw9131 = codRivenditaWeb and dataord9131 between to_date(:dataIniziale, '" + DateUtilities.FORMATO_DATA + "') and to_date(:dataFinale, '" + DateUtilities.FORMATO_DATA_SLASH + "')) numRifornimenti, ");
				query.append("(select count(1) from tbl_9129 where coddl9129 = coddl and crivw9129 = codRivenditaWeb and dari9129 between to_date(:dataIniziale, '" + DateUtilities.FORMATO_DATA + "') and to_date(:dataFinale, '" + DateUtilities.FORMATO_DATA_SLASH + "')) numVariazioni, ");
				query.append("(select sum(diffe9611) from tbl_9611 where coddl9611 = coddl and crivw9611 = codRivenditaWeb and datbc9611 between to_date(:dataIniziale, '" + DateUtilities.FORMATO_DATA + "') and to_date(:dataFinale, '" + DateUtilities.FORMATO_DATA_SLASH + "') and diffe9611 > 0) numMancanze, ");
				query.append("(select -sum(diffe9611) from tbl_9611 where coddl9611 = coddl and crivw9611 = codRivenditaWeb and datbc9611 between to_date(:dataIniziale, '" + DateUtilities.FORMATO_DATA + "') and to_date(:dataFinale, '" + DateUtilities.FORMATO_DATA_SLASH + "') and diffe9611 < 0) numEccedenze ");
				query.append("from (");
				query.append("select coddl9206 coddl, crivw9206 codRivenditaWeb, crivdl9206 codRivenditaDl, nomeriva9106 nomeRivendita ");
				query.append("from tbl_9206 inner join tbl_9106 on (crivw9206 = crivw9106) ");
				query.append("where coddl9206 = :codDl");
				if (codRivendita != null) {
					query.append(" and crivdl9206 = :codRivendita");
				}
				query.append(") ");
				query.append("order by codRivenditaDl");
				
				SQLQuery sqlQuery = session.createSQLQuery(query.toString());
				sqlQuery.setInteger("codDl", codDl);
				sqlQuery.setString("dataIniziale", DateUtilities.getTimestampAsString(DateUtilities.floorDay(dataIniziale), DateUtilities.FORMATO_DATA));
				sqlQuery.setString("dataFinale", DateUtilities.getTimestampAsString(DateUtilities.floorDay(dataFinale), DateUtilities.FORMATO_DATA));
				if (codRivendita != null) {
					sqlQuery.setInteger("codRivendita", codRivendita);
				}
				
				sqlQuery.setResultTransformer(Transformers.aliasToBean(StatisticaUtilizzoDto.class));
				sqlQuery.addScalar("codRivenditaDl", IntegerType.INSTANCE);
				sqlQuery.addScalar("codRivenditaWeb", IntegerType.INSTANCE);
				sqlQuery.addScalar("nomeRivendita", StringType.INSTANCE);
				sqlQuery.addScalar("numBolle", LongType.INSTANCE);
				sqlQuery.addScalar("numRese", LongType.INSTANCE);
				sqlQuery.addScalar("numVendite", LongType.INSTANCE);
				sqlQuery.addScalar("numRifornimenti", LongType.INSTANCE);
				sqlQuery.addScalar("numVariazioni", LongType.INSTANCE);
				sqlQuery.addScalar("numMancanze", LongType.INSTANCE);
				sqlQuery.addScalar("numEccedenze", LongType.INSTANCE);
				
				return sqlQuery.list();
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public Map<String, Integer> getPageMonitorMap() {
		DetachedCriteria criteria = DetachedCriteria.forClass(PaginaMonitorStatisticaVo.class, "pag");
		List<PaginaMonitorStatisticaVo> pagine = getDao().findByDetachedCriteria(criteria);
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (PaginaMonitorStatisticaVo vo : pagine) {
			map.put(vo.getNomePagina(), vo.getCodPagina());
		}
		return Collections.unmodifiableMap(map);
	}
	
	@Override
	public StatistichePagineVo getUltimaPagina(Integer codEdicola, String codUtente) {
		DetachedCriteria criteria = DetachedCriteria.forClass(StatistichePagineVo.class, "sp");
		criteria.createCriteria("sp.pagina");
		criteria.add(Restrictions.eq("sp.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("sp.codUtente", codUtente));
		criteria.add(Restrictions.isNull("sp.dataUscita"));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public StatistichePagineVo getPaginaCorrente(Integer codEdicola, String codUtente, Integer codPagina) {
		DetachedCriteria criteria = DetachedCriteria.forClass(StatistichePagineVo.class, "sp");
		criteria.createCriteria("sp.pagina");
		criteria.add(Restrictions.eq("sp.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("sp.codUtente", codUtente));
		criteria.add(Restrictions.eq("sp.codPagina", codPagina));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
}
