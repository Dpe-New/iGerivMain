package it.dpe.igeriv.bo.istruzione;

import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.vo.IstruzioneVo;
import it.dpe.igeriv.vo.UserVo;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
class IstruzioneRepositoryImpl extends BaseRepositoryImpl implements IstruzioneRepository {
	
	@Autowired
	IstruzioneRepositoryImpl(BaseDao<UserVo> dao) {
		super(dao);
	}
	
	@Override
	public List<KeyValueDto> getIstruzione() {
		DetachedCriteria criteria = DetachedCriteria.forClass(IstruzioneVo.class, "ivo");
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("ivo.codice"), "keyInt");
		props.add(Projections.property("ivo.descrizione"), "value");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(KeyValueDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public IstruzioneVo getIstruzione(Integer codIstruzione) {
		DetachedCriteria criteria = DetachedCriteria.forClass(IstruzioneVo.class, "ivo");
		criteria.add(Restrictions.eq("ivo.codice", codIstruzione));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	
}
