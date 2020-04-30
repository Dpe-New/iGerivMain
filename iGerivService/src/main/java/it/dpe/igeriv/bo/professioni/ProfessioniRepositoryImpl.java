package it.dpe.igeriv.bo.professioni;

import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.vo.ProfessioneVo;
import it.dpe.igeriv.vo.UserVo;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
class ProfessioniRepositoryImpl extends BaseRepositoryImpl implements ProfessioniRepository {
	
	@Autowired
	ProfessioniRepositoryImpl(BaseDao<UserVo> dao) {
		super(dao);
	}
	
	@Override
	public List<KeyValueDto> getProfessioni() {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProfessioneVo.class, "pvo");
		criteria.addOrder(Order.asc("pvo.descrizione"));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("pvo.codice"), "keyInt");
		props.add(Projections.property("pvo.descrizione"), "value");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(KeyValueDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public ProfessioneVo getProfessione(Integer codProfessione) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProfessioneVo.class, "pvo");
		criteria.add(Restrictions.eq("pvo.codice", codProfessione));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
}
