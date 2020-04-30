package it.dpe.igeriv.bo;

import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.vo.BaseVo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("BaseRepository")
public class BaseRepositoryImpl implements BaseRepository {
	private final BaseDao<?> dao;
	
	@Autowired
	public BaseRepositoryImpl(BaseDao<?> dao) {
		this.dao = dao;
	}
	
	public BaseDao<?> getDao() {
		return dao;
	}

	@Override
	public Timestamp getSysdate() {
		return dao.getSysdate();
	}
	
	@Override
	public Long getNextSeqVal(String seqClientiEdicola) {
		return dao.getNextSeqVal(seqClientiEdicola);
	}
	
	@Override
	public <T extends BaseVo> T saveBaseVo(T vo) {
		return dao.saveOrUpdate(vo);
	}
	
	@Override
	public <T extends BaseVo> T mergeBaseVo(T vo) {
		return dao.merge(vo);
	}
	
	@Override
	public void deleteVo(BaseVo vo) {
		dao.delete(vo);
	};
	
	@Override
	public <T extends BaseVo> void deleteVoList(Collection<T> list) {
		for (T vo : list) {
			dao.delete(vo);
		}
	}
	
	@Override
	public <T extends BaseVo> Collection<T> saveVoList(Collection<T> list) {
		List<T> list1 = new ArrayList<T>();
		for (T vo : list) {
			T saveOrUpdate = dao.saveOrUpdate(vo);
			list1.add(saveOrUpdate);
		}
		return list1; 
	}
	
	@Override
	public <T extends BaseVo> Collection<T> mergeVoList(Collection<T> list) {
		List<T> list1 = new ArrayList<T>();
		for (T vo : list) {
			T saveOrUpdate = dao.merge(vo);
			list1.add(saveOrUpdate);
		}
		return list1; 
	}
	
	@Override
	public synchronized Integer getLastId(Class<?> clazz, String pkName, Criterion restriction) {
		DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
		if (restriction != null) {
			criteria.add(restriction);
		}
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.max(pkName));
		criteria.setProjection(props);
		Integer lastId = dao.findUniqueResultObjectByDetachedCriteria(criteria);
		return ((lastId == null) ? 0 : lastId) + 1;
	}
	
}
