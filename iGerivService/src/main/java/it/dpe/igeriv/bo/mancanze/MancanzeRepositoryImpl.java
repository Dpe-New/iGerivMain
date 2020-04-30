package it.dpe.igeriv.bo.mancanze;

import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.MancanzaBollaDto;
import it.dpe.igeriv.dto.MancanzaDto;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.FondoBollaDettaglioVo;
import it.dpe.igeriv.vo.MancanzaVo;
import it.dpe.igeriv.vo.UserVo;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

@Repository
class MancanzeRepositoryImpl extends BaseRepositoryImpl implements MancanzeRepository {
	
	@Autowired
	MancanzeRepositoryImpl(BaseDao<UserVo> dao) {
		super(dao);
	}
	
	@Override
	public List<MancanzaDto> getMancanze(Integer codFiegDl, Integer codEdicola, Timestamp dataDa, Timestamp dataA, String titolo, Integer stato) {
		DetachedCriteria criteria = DetachedCriteria.forClass(MancanzaVo.class, "ma");
		criteria.createCriteria("ma.copertina", "ac");
		criteria.createCriteria("ac.anagraficaPubblicazioniVo", "ap");
		criteria.add(Restrictions.eq("ma.pk.codDl", codFiegDl));
		criteria.add(Restrictions.eq("ma.pk.codEdicola", codEdicola));
		if (dataDa != null && dataA != null) {
			criteria.add(Restrictions.between("ac.dataUscita", dataDa, dataA));
		}
		if (titolo != null && !titolo.equals("")) {
			criteria.add(Restrictions.ilike("ap.titolo", titolo, MatchMode.ANYWHERE));
		}
		if (stato != null) {
			criteria.add(Restrictions.eq("ma.stato", stato));
		}
		criteria.addOrder(Order.asc("ac.dataUscita")).addOrder(Order.asc("ap.titolo"));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("ap.titolo"), "titolo");
		properties.add(Projections.property("ac.sottoTitolo"), "sottotitolo"); 
		properties.add(Projections.property("ac.numeroCopertina"), "numero");
		properties.add(Projections.property("ac.dataUscita"), "dtUscita"); 
		properties.add(Projections.property("ma.quantita"), "copie");
		properties.add(Projections.property("ma.stato"), "stato");
		properties.add(Projections.property("ma.dtBolla"), "dtBolla");
		properties.add(Projections.property("ma.tipoBolla"), "tipoBolla");
		properties.add(Projections.property("ma.note"), "note");
		criteria.setProjection(properties);
		criteria.setResultTransformer(Transformers.aliasToBean(MancanzaDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<MancanzaBollaDto> getMancanzeBolla(final Integer codFiegDl, final Integer codEdicola, final Timestamp dataDa, final Timestamp dataA, final String titolo, final Boolean soloDifferenze) {
		HibernateCallback<List<MancanzaBollaDto>> action = new HibernateCallback<List<MancanzaBollaDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<MancanzaBollaDto> doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = IGerivQueryContants.SQL_QUERY_GET_MANCANZE_BOLLA;
				sql += ") t1 ";
				if (soloDifferenze) {
					sql += " where nvl(t1.mancanze, 0) <> nvl(t1.mancanzeAccreditate, 0)";
				}
				if (titolo != null && !titolo.equals("")) {
					sql += (soloDifferenze ? " and" : " where") + " upper(t1.titolo) like '%" + titolo.toUpperCase() + "%'";
				}
				sql += " order by t1.dtBolla desc, t1.titolo";
				SQLQuery createSQLQuery = session.createSQLQuery(sql);
				createSQLQuery.setInteger("coddl", codFiegDl);
				createSQLQuery.setInteger("codEdicola", codEdicola);
				createSQLQuery.setTimestamp("dataInizio", dataDa);
				createSQLQuery.setTimestamp("dataFine", dataA);
				createSQLQuery.setResultTransformer( Transformers.aliasToBean(MancanzaBollaDto.class));
				createSQLQuery.addScalar("dtBolla", TimestampType.INSTANCE);
				createSQLQuery.addScalar("tipoBolla", StringType.INSTANCE);
				createSQLQuery.addScalar("codicePubblicazione",IntegerType.INSTANCE);
				createSQLQuery.addScalar("titolo", StringType.INSTANCE);
				createSQLQuery.addScalar("idtn",IntegerType.INSTANCE);
				createSQLQuery.addScalar("dtUscita", TimestampType.INSTANCE);
				createSQLQuery.addScalar("numero", StringType.INSTANCE);
				createSQLQuery.addScalar("mancanze",IntegerType.INSTANCE);
				createSQLQuery.addScalar("mancanzeAccreditate", IntegerType.INSTANCE);
				createSQLQuery.addScalar("tipo", StringType.INSTANCE);
				return createSQLQuery.list();
			}
		}; 
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public List<MancanzaBollaDto> getMancanzeDettaglioBolla(Integer codFiegDl, Integer codEdicola, Integer idtn, Timestamp dtBolla) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FondoBollaDettaglioVo.class, "brvo");
		criteria.createCriteria("brvo.tipoFondoBollaVo", "tp");
		criteria.add(Restrictions.eq("brvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("brvo.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.gt("brvo.pk.dtBolla", dtBolla));
		criteria.add(Restrictions.eq("brvo.idtn", idtn));
		criteria.add(Restrictions.in("tp.tipoRecordFondoBolla", new Object[]{1,10}));
		criteria.add(Restrictions.lt("brvo.quantitaConsegnata", 0));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("brvo.pk.dtBolla"), "dtBolla");
		props.add(Projections.property("brvo.pk.tipoBolla"), "tipoBolla");
		props.add(Projections.property("brvo.quantitaConsegnata"), "mancanze");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(MancanzaBollaDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
}
