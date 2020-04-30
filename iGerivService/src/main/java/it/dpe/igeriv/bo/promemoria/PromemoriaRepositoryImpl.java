package it.dpe.igeriv.bo.promemoria;

import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.PromemoriaDto;
import it.dpe.igeriv.vo.PromemoriaVo;
import it.dpe.igeriv.vo.UserVo;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
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

@Repository
class PromemoriaRepositoryImpl extends BaseRepositoryImpl implements PromemoriaRepository {
	
	@Autowired
	PromemoriaRepositoryImpl(BaseDao<UserVo> dao) {
		super(dao);
	}
	
	@Override
	public List<PromemoriaDto> getListPromemoria(Integer codEdicola, java.sql.Date dtMessaggioDa, java.sql.Date dtMessaggioA) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromemoriaVo.class, "pr");
		criteria.add(Restrictions.eq("pr.codEdicola", codEdicola));
		criteria.add(Restrictions.between("pr.dataMessaggio", dtMessaggioDa, dtMessaggioA));
		criteria.addOrder(Order.desc("pr.dataMessaggio")).addOrder(Order.asc("pr.letto"));
		ProjectionList props = Projections.projectionList();
		props.add(Projections.property("pr.codPromemoria"), "codPromemoria");
		props.add(Projections.property("pr.messaggio"), "messaggio");
		props.add(Projections.property("pr.dataMessaggio"), "dataMessaggio");
		props.add(Projections.property("pr.letto"), "letto");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(PromemoriaDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public PromemoriaDto getPromemoria(Long codPromemoria) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromemoriaVo.class, "pr");
		criteria.add(Restrictions.eq("pr.codPromemoria", codPromemoria));
		ProjectionList props = Projections.projectionList();
		props.add(Projections.property("pr.codPromemoria"), "codPromemoria");
		props.add(Projections.property("pr.messaggio"), "messaggio");
		props.add(Projections.property("pr.dataMessaggio"), "dataMessaggio");
		props.add(Projections.property("pr.letto"), "letto");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(PromemoriaDto.class));
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public PromemoriaDto getFirstPromemoria(final Integer codEdicola) {
		HibernateCallback<PromemoriaDto> action = new HibernateCallback<PromemoriaDto>() {
			@Override
			public PromemoriaDto doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(PromemoriaVo.class, "pr");
				criteria.add(Restrictions.eq("pr.codEdicola", codEdicola));
				criteria.add(Restrictions.eq("pr.dataMessaggio", new java.sql.Date(getSysdate().getTime())));
				criteria.add(Restrictions.eq("pr.letto", false));
				ProjectionList props = Projections.projectionList();
				props.add(Projections.property("pr.codPromemoria"), "codPromemoria");
				props.add(Projections.property("pr.messaggio"), "messaggio");
				props.add(Projections.property("pr.dataMessaggio"), "dataMessaggio");
				props.add(Projections.property("pr.letto"), "letto");
				criteria.setProjection(props);
				criteria.setResultTransformer(Transformers.aliasToBean(PromemoriaDto.class));
				criteria.setMaxResults(1);
				Object uniqueResult = criteria.uniqueResult();
				return uniqueResult != null ? (PromemoriaDto) uniqueResult : null;
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public void deletePromemoria(PromemoriaDto promemoria) {
		getDao().bulkUpdate("delete from PromemoriaVo vo where vo.codPromemoria = ?", new Object[]{promemoria.getCodPromemoria()});
	}
	
	@Override
	public Boolean hasPromemoria(Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PromemoriaVo.class, "pr");
		criteria.add(Restrictions.eq("pr.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("pr.dataMessaggio", new java.sql.Date(getSysdate().getTime())));
		criteria.add(Restrictions.eq("pr.letto", false));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.count("pr.codPromemoria"));
		criteria.setProjection(props);
		Number count = (Number) getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		return (count != null && count.intValue() > 0) ? true : false;
	}
	
	@Override
	public void updatePromemoriaLetto(Long codPromemoria) {
		getDao().bulkUpdate("update PromemoriaVo vo set vo.letto = true where vo.codPromemoria = ?", new Object[]{codPromemoria});
	}
	
}
