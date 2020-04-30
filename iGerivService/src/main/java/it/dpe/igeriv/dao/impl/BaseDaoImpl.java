package it.dpe.igeriv.dao.impl;

import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.BaseVo;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.TimestampType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
 
/**
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
@Repository("BaseDao")
@Transactional
public class BaseDaoImpl<T extends BaseVo> implements BaseDao<T> {
	private final Logger log = Logger.getLogger(getClass());
	private SessionFactory factory;
	
	/*
	 * Gestire connessioni multiple (multi tenancy):
	 * Create a front controller that provides a unified front-door to tenants logging in for the first time
	 * Hibernate supporting multi-tenant configurations out of the box from version 4. 
	 * http://blog.springsource.com/2007/01/23/dynamic-datasource-routing/
	 * 
	 */
	
	@Autowired
	BaseDaoImpl(SessionFactory factory) {
		this.factory = factory;
	}
	
	
	/* (non-Javadoc)
	 * @see it.dpe.dao.BaseDao#create(it.dpe.igeriv.vo.BaseVo)
	 */
	@Override
	public <T extends BaseVo> T create(T entity) {
        return (T) factory.getCurrentSession().save(entity);
	}
	
	/* (non-Javadoc)
	 * @see it.dpe.dao.BaseDao#delete(it.dpe.igeriv.vo.BaseVo)
	 */
	@Override
	public <T extends BaseVo> void delete(T entity) {
		factory.getCurrentSession().delete(entity);
	}
	
	/* (non-Javadoc)
	 * @see it.dpe.dao.BaseDao#saveOrUpdate(it.dpe.igeriv.vo.BaseVo)
	 */
	@Override
	public <T extends BaseVo> T saveOrUpdate(T entity) {
		factory.getCurrentSession().saveOrUpdate(entity);
        return entity;

	}
		
	/* (non-Javadoc)
	 * @see it.dpe.dao.BaseDao#merge(it.dpe.igeriv.vo.BaseVo)
	 */
	@Override
	public <T extends BaseVo> T merge(T entity) {
		return (T) factory.getCurrentSession().merge(entity);
	}
	
	/* (non-Javadoc)
	 * @see it.dpe.dao.BaseDao#update(it.dpe.igeriv.vo.BaseVo)
	 */
	@Override
	public <T extends BaseVo> T update(T entity) {
		factory.getCurrentSession().update(entity);
		return entity;
	}
	
	/* (non-Javadoc)
	 * @see it.dpe.dao.BaseDao#bulkUpdate(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void bulkUpdate(String query, Object[] args) {
		Query createQuery = factory.getCurrentSession().createQuery(query);
		int index = 0;
		for (Object obj : args) {
			createQuery.setParameter(index++, obj);
		}
		createQuery.executeUpdate();
	}
	
	/* (non-Javadoc)
	 * @see it.dpe.dao.BaseDao#bulkUpdate(java.lang.String)
	 */
	@Override
	public void bulkUpdate(String query) {
		factory.getCurrentSession().createQuery(query).executeUpdate();
	}

	/* (non-Javadoc)
	 * @see it.dpe.dao.BaseDao#findByQuery(java.lang.String)
	 */
	@Override
	public <T extends BaseVo> List<T> findByQuery(String query) {
		return factory.getCurrentSession().createQuery(query).list();
	}
	
	@Override
	public <T extends BaseVo> List<T> findByQueryParams(String query, Object... parameter) {
		Query createQuery = factory.getCurrentSession().createQuery(query);
		int index = 0;
		for (Object obj : parameter) {
			createQuery.setParameter(index++, obj);
		}
		return createQuery.list();
	}
	
	/* (non-Javadoc)
	 * @see it.dpe.dao.BaseDao#loadAll(java.lang.Class)
	 */
	@Override
	public <T extends BaseVo> T loadByPk(Class<T> entityClass, Serializable id) {
		return (T) factory.getCurrentSession().load(entityClass, id);
	}

	/* (non-Javadoc)
	 * @see it.dpe.dao.BaseDao#findByQuery(java.lang.String, java.lang.Object[])
	 */
	@Override
	public <T extends BaseVo> List<T> findByStringQuery(String query, Object... parameters) {
		return findByQuery(query, parameters);
	}
	
	/* (non-Javadoc)
	 * @see it.dpe.dao.BaseDao#findByNamedQuery(java.lang.String)
	 */
	@Override
	public <T extends BaseVo> List<T> findByNamedQuery(String query) {
		return factory.getCurrentSession().getNamedQuery(query).list();
	}
	
	/* (non-Javadoc)
	 * @see it.dpe.dao.BaseDao#findByNamedQuery(java.lang.String, java.lang.Object[])
	 */
	@Override
	public <T extends BaseVo> List<T> findByNamedQuery(String query, Object... parameters) {
		return findByQuery(getNamedQueryString(query), parameters);
	}
	
	/* (non-Javadoc)
	 * @see it.dpe.igeriv.dao.BaseDao#findObjectByNamedQuery(java.lang.String, java.lang.Object[])
	 */
	@Override
	public List<T> findObjectByNamedQuery(String query, Object... parameters) {
		return findByQuery(getNamedQueryString(query), parameters);
	}
	
	/* (non-Javadoc)
	 * @see it.dpe.dao.BaseDao#findByDetachedCriteria(org.hibernate.criterion.DetachedCriteria)
	 */
	@Override
	public <T extends BaseVo> List<T> findByDetachedCriteria(DetachedCriteria criteria) {
		return criteria.getExecutableCriteria(factory.getCurrentSession()).list();
	}
	
	/* (non-Javadoc)
	 * @see it.dpe.igeriv.dao.BaseDao#findObjectByDetachedCriteria(org.hibernate.criterion.DetachedCriteria)
	 */
	@Override
	public <T> List<T> findObjectByDetachedCriteria(DetachedCriteria criteria) {
		return criteria.getExecutableCriteria(factory.getCurrentSession()).list();
	}
	
	/* (non-Javadoc)
	 * @see it.dpe.dao.BaseDao#findByDetachedCriteria(org.hibernate.criterion.DetachedCriteria, int, int)
	 */
	@Override
	public <T extends Object> List<T> findByDetachedCriteria(DetachedCriteria criteria, int firstResult, int maxResults) {
		return criteria.getExecutableCriteria(factory.getCurrentSession()).setFirstResult(firstResult).setMaxResults(maxResults).list();
	}
	
	/* (non-Javadoc)
	 * @see it.dpe.dao.BaseDao#findByHibernateCallback(org.springframework.orm.hibernate3.HibernateCallback)
	 */
	@Override
	public List findByHibernateCallback(HibernateCallback action) {
		try {
			return (List) action.doInHibernate(factory.getCurrentSession());
		} catch (HibernateException e) {
			log.error("HibernateException", e);
			throw new RuntimeException(e);
		} catch (SQLException e) {
			log.error("SQLException", e);
			throw new RuntimeException(e);
		}
	}
	
	public <T extends Object> Set<T> findSetByHibernateCallback(HibernateCallback action) {
		try {
			return (Set) action.doInHibernate(factory.getCurrentSession());
		} catch (HibernateException e) {
			log.error("HibernateException", e);
			throw new RuntimeException(e);
		} catch (SQLException e) {
			log.error("SQLException", e);
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public <T> T findUniqueResultByHibernateCallback(HibernateCallback action) {
		try {
			return (T) action.doInHibernate(factory.getCurrentSession());
		} catch (HibernateException e) {
			log.error("HibernateException", e);
			throw new RuntimeException(e);
		} catch (SQLException e) {
			log.error("SQLException", e);
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void executeByHibernateCallback(HibernateCallback action) {
		try {
			action.doInHibernate(factory.getCurrentSession());
		} catch (HibernateException e) {
			log.error("HibernateException", e);
			throw new RuntimeException(e);
		} catch (SQLException e) {
			log.error("SQLException", e);
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see it.dpe.web.dao.BaseDAO#findByCallingStoredProc(java.lang.String, java.lang.Object[])
	 */
	@Override
	public <T extends BaseVo> List<T> findByCallingStoredProc(final String procName,
			final Object[] params) {
		try {
			return (List<T>) (new HibernateCallback() {
				public Object doInHibernate(final Session session)
						throws HibernateException, SQLException {
					Query namedQuery = session.getNamedQuery(procName);
					for (int i = 0; i < params.length; i++) {
						namedQuery.setParameter(i, params[i]);
					}
					return namedQuery.list();
				}
			}).doInHibernate(factory.getCurrentSession());
		} catch (HibernateException e) {
			log.error("HibernateException", e);
			throw new RuntimeException(e);
		} catch (SQLException e) {
			log.error("SQLException", e);
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see it.dpe.web.dao.BaseDAO#findByCriteria(java.lang.String, java.util.Map)
	 */
	@Override
	public <T extends BaseVo> List<T> findByCriteria(final Map<String, Object> params, final Class<T> persistentClass) {
		try {
			return (List<T>) (new HibernateCallback() {
				public Object doInHibernate(final Session session)
						throws HibernateException, SQLException {
					Criteria criteria = session.createCriteria(persistentClass);
					Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<String, Object> pairs = it.next();
						String key = pairs.getKey();
						Object value = pairs.getValue();
						criteria.add(Restrictions.eq(key, value));
					}
					List<T> list = null;
					try {
						list = criteria.list();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return list;
				}
			}).doInHibernate(factory.getCurrentSession());
		} catch (HibernateException e) {
			log.error("HibernateException", e);
			throw new RuntimeException(e);
		} catch (SQLException e) {
			log.error("SQLException", e);
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see it.dpe.dao.BaseDao#findUniqueResultByDetachedCriteria(org.hibernate.criterion.DetachedCriteria)
	 */
	@Override
	public <T extends BaseVo> T findUniqueResultByDetachedCriteria(
			DetachedCriteria criteria) {
		T result = null;
		List<T> findByNamedQuery = criteria.getExecutableCriteria(factory.getCurrentSession()).list();
		if (findByNamedQuery != null && !findByNamedQuery.isEmpty()) {
			result = findByNamedQuery.get(0);
		}
		return result;
	}
	
	@Override
	public <T> T findUniqueResultObjectByDetachedCriteria(
			DetachedCriteria criteria) {
		T result = null;
		List<T> findByNamedQuery = criteria.getExecutableCriteria(factory.getCurrentSession()).list();
		if (findByNamedQuery != null && !findByNamedQuery.isEmpty()) {
			result = findByNamedQuery.get(0);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see it.dpe.dao.BaseDao#findUniqueResultByNamedQuery(java.lang.String, java.lang.Object[])
	 */
	@Override
	public <T extends BaseVo> T findUniqueResultByNamedQuery(String query, Object... parameter) {
		T result = null;
		String strQuery = getNamedQueryString(query);
		List<T> findByNamedQuery = findByQuery(strQuery, parameter);
		if (findByNamedQuery != null && !findByNamedQuery.isEmpty()) {
			result = findByNamedQuery.get(0);
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see it.dpe.igeriv.dao.BaseDao#findUniqueResultObjectByNamedQuery(java.lang.String, java.lang.Object[])
	 */
	@Override
	public <T extends Object> T findUniqueResultObjectByNamedQuery(String query, Object... parameter) {
		T result = null;
		String strQuery = getNamedQueryString(query);
		List<T> findByNamedQuery = findByQuery(strQuery, parameter);
		if (findByNamedQuery != null && !findByNamedQuery.isEmpty()) {
			result = findByNamedQuery.get(0);
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see it.dpe.dao.BaseDao#findUniqueResultByQuery(java.lang.String, java.lang.Object[])
	 */
	@Override
	public <T extends Object> T findUniqueResultByQuery(String query, Object... parameter) {
		T result = null;
		List<T> findByNamedQuery = findByQuery(query, parameter);
		if (findByNamedQuery != null && !findByNamedQuery.isEmpty()) {
			result = findByNamedQuery.get(0);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see it.dpe.dao.BaseDao#getSysdate()
	 */
	@Override
	public Timestamp getSysdate() {
		HibernateCallback hibernateCallback = new HibernateCallback() {
			public Object doInHibernate(final Session session)
					throws HibernateException, SQLException {
				String sql = "SELECT SYSDATE AS NOW FROM DUAL";
				SQLQuery sqlQuery = session.createSQLQuery(sql);
				sqlQuery.addScalar("NOW", TimestampType.INSTANCE);
				return (Timestamp) sqlQuery.uniqueResult();
			}
		};
		try {
			return (Timestamp) hibernateCallback.doInHibernate(factory.getCurrentSession());
		} catch (HibernateException e) {
			log.error("HibernateException", e);
			throw new RuntimeException(e);
		} catch (SQLException e) {
			log.error("SQLException", e);
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see it.dpe.dao.BaseDao#getNamedQueryString(java.lang.String)
	 */
	@Override
	public String getNamedQueryString(final String queryNameGetAnagraficaMinicard) {
		String namedQueryString = null;
		HibernateCallback hibernateCallback = new HibernateCallback() {
			public Object doInHibernate(final Session session)
					throws HibernateException, SQLException {
				Query query = session.getNamedQuery(queryNameGetAnagraficaMinicard);
				return query.getQueryString();
			}
		};
		try {
			namedQueryString = (String) hibernateCallback.doInHibernate(factory.getCurrentSession());
		} catch (HibernateException e) {
			log.error("HibernateException", e);
			throw new RuntimeException(e);
		} catch (SQLException e) {
			log.error("SQLException", e);
			throw new RuntimeException(e);
		}
		return namedQueryString;
	}
	
	/**
	 * Esegue la query con i parametri contenuti nel var-arg <code>parameter</code>.
	 * Il var-arg <code>parameter</code> può contenere:
	 * - Una java.util.Map di NamedParam(String) = valore(Object), 
	 * dove il NamedParam deve essere lo stesso definito nella query 
	 * usando i due punti, per esempio dove nella query "codUtente = :nome_del_parametro"
	 * il NamedParam nella java.util.Map sarà "nome_del_parametro".
	 * - Uno o più valori oppure un array di valori (Object) che saranno 
	 * passati alla query nel loro ordine seuqenziale. Funziona sia
	 * quando i parametri nella query sono dichiarati in forma sequenziale
	 * (con il "?"), sia con i NamedParams.
	 * 
	 * @param String query
	 * @param Object... parameter
	 * @return List<T>
	 */
	private <T> List<T> findByQuery(String query, Object... parameters) {
		List<T> findByNamedQuery = null;
		if (parameters != null && parameters.length > 0) {
			Query cq = factory.getCurrentSession().createQuery(query);
			if (parameters[0] instanceof Map) {
				Map map = (Map) parameters[0];
				Query createQuery = cq;
				for (Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext();) {
					Map.Entry<String, Object> entry = it.next();
					String regex = IGerivQueryContants.QUERY_NAMED_PARAM_DELIM + "\\b" + entry.getKey() + "\\b";  
					Pattern pattern = Pattern.compile(regex);  
					Matcher matcher = pattern.matcher(query);  
					if (matcher.find()) {
						createQuery.setParameter(entry.getKey(), entry.getValue());
					}
				}
				findByNamedQuery = createQuery.list();
			} else {
				if (query.indexOf(IGerivQueryContants.QUERY_NAMED_PARAM_DELIM) != -1) {
					StringTokenizer st = new StringTokenizer(query, " ");
					Query createQuery = cq;
					int count = 0;
					while (st.hasMoreTokens()) {
						String nextToken = st.nextToken();
						if (nextToken.startsWith(IGerivQueryContants.QUERY_NAMED_PARAM_DELIM)) {
							createQuery.setParameter(nextToken.substring(1), parameters[count++]);
						}
					}
					findByNamedQuery = createQuery.list();
				} else {
					int index = 0;
					for (Object obj : parameters) {
						cq.setParameter(index++, obj);
					}
					findByNamedQuery = (List<T>) cq.list();
				}
			}
		} 
		return findByNamedQuery;
	}
	
	@Override
	public void evict(Object entity) {
		factory.getCurrentSession().evict(entity);
	}
	
	@Override
	public void clearSession() {
		factory.getCurrentSession().getSessionFactory().getCurrentSession().clear();
	}
	
	@Override
	public Long getNextSeqVal(final String seqName) {
		HibernateCallback<Long> action = new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery createSQLQuery = arg0.createSQLQuery("select " + seqName + ".nextval from dual");
				return new Long(createSQLQuery.uniqueResult().toString());
			}
		};
		try {
			return (Long) action.doInHibernate(factory.getCurrentSession());
		} catch (HibernateException e) {
			log.error("HibernateException", e);
			throw new RuntimeException(e);
		} catch (SQLException e) {
			log.error("SQLException", e);
			throw new RuntimeException(e);
		}
	}
}
