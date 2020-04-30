package it.dpe.igeriv.dao;

import it.dpe.igeriv.vo.BaseVo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * Classe abstrata.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@SuppressWarnings({ "rawtypes", "hiding" })
public interface BaseDao<T extends Serializable> {
	
	/**
	 * @param <T>
	 * @param entity
	 * @return
	 */
	public <T extends BaseVo> T create(T entity);
	
	/**
	 * @param <T>
	 * @param entity
	 * @return
	 */
	public <T extends BaseVo> T saveOrUpdate(T entity);
	
	/**
	 * @param entity
	 */
	public <T extends BaseVo> void delete(T entity);
	
	/**
	 * @param <T>
	 * @param entity
	 * @return
	 */
	public <T extends BaseVo> T update(T entity);
	
	/**
	 * @param query
	 * @param args
	 */
	public void bulkUpdate(String query, Object[] args);
	
	/**
	 * @param query
	 */
	public void bulkUpdate(String query);
	
	/**
	 * @param <T>
	 * @param object
	 * @return
	 */
	public <T extends BaseVo> T merge(T object);

	/**
	 * @param <T>
	 * @param query
	 * @return
	 */
	public <T extends BaseVo> List<T> findByQuery(String query);
	
	/**
	 * @param <T>
	 * @param query
	 * @return
	 */
	public <T extends BaseVo> List<T> findByQueryParams(String query, Object... parmeters);

	/**
	 * @param <T>
	 * @param entityClass
	 * @param id
	 * @return
	 */
	public <T extends BaseVo> T loadByPk(Class<T> entityClass, Serializable id);
	
	/**
	 * @param <T>
	 * @param query
	 * @param parameters
	 * @return
	 */
	public <T extends BaseVo> List<T> findByStringQuery(String query, Object... parameters);
	
	/**
	 * @param <T>
	 * @param query
	 * @param parameter
	 * @return
	 */
	public <T extends Object> T findUniqueResultByQuery(String query, Object... parameter);
	
	/**
	 * @param <T>
	 * @param query
	 * @return
	 */
	public <T extends BaseVo> List<T> findByNamedQuery(String query);
	
	/**
	 * @param <T>
	 * @param query
	 * @param parameter
	 * @return
	 */
	public <T extends BaseVo> List<T> findByNamedQuery(String query, Object... parameter);
	
	/**
	 * @param <T>
	 * @param query
	 * @param parameter
	 * @return
	 */
	public <T extends Object> List<T> findObjectByNamedQuery(String query, Object... parameter);
	
	/**
	 * @param <T>
	 * @param query
	 * @param parameter
	 * @return
	 */
	public <T extends BaseVo> T findUniqueResultByNamedQuery(String query, Object... parameter);
	
	
	/**
	 * @param <T>
	 * @param query
	 * @param parameter
	 * @return
	 */
	public <T extends Object> T findUniqueResultObjectByNamedQuery(String query, Object... parameter);

	/**
	 * @param <T>
	 * @param criteria
	 * @return
	 */
	public <T extends BaseVo> List<T> findByDetachedCriteria(DetachedCriteria criteria);
	
	
	/**
	 * @param <T>
	 * @param criteria
	 * @return
	 */
	public <T extends Object> List<T> findObjectByDetachedCriteria(DetachedCriteria criteria);

	/**
	 * @param <T>
	 * @param criteria
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public <T extends Object> List<T> findByDetachedCriteria(DetachedCriteria criteria,
			int firstResult, int maxResults);
	
	/**
	 * @param <T>
	 * @param criteria
	 * @return
	 */
	public <T extends BaseVo> T findUniqueResultByDetachedCriteria(DetachedCriteria criteria);
	
	/**
	 * @param <T>
	 * @param criteria
	 * @return
	 */
	public <T extends Object> T findUniqueResultObjectByDetachedCriteria(DetachedCriteria criteria);
	
	/**
	 * @param <T>
	 * @param action
	 * @return
	 */
	public <T extends Object> List<T> findByHibernateCallback(HibernateCallback action);
	
	/**
	 * @param <T>
	 * @param action
	 * @return
	 */
	public <T extends Object> Set<T> findSetByHibernateCallback(HibernateCallback action);
	
	/**
	 * @param action
	 * @return
	 */
	public void executeByHibernateCallback(HibernateCallback action);
	
	/**
	 * @param <t>
	 * @param action
	 */
	public <T extends Object> T findUniqueResultByHibernateCallback(HibernateCallback action);
	
	/**
	 * @param <T>
	 * @param procName
	 * @param params
	 * @return
	 */
	public <T extends BaseVo> List<T> findByCallingStoredProc(final String procName, final Object[] params);
	
	/**
	 * @param <T>
	 * @param params
	 * @param persistentClass
	 * @return
	 */
	public <T extends BaseVo> List<T> findByCriteria(final Map<String, Object> params, final Class<T> persistentClass);
	
	/**
	 * @return
	 */
	public Timestamp getSysdate();

	/**
	 * @param queryNameGetAnagraficaMinicard
	 * @return
	 */
	public String getNamedQueryString(String queryNameGetAnagraficaMinicard);
	
	
	/**
	 * @param entity
	 */
	public void evict(Object entity);

	/**
	 * 
	 */
	public void clearSession();

	/**
	 * @param seqClientiEdicola
	 * @return
	 */
	public Long getNextSeqVal(String seqClientiEdicola);

}
