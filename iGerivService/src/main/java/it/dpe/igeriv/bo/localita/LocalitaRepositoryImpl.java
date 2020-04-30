package it.dpe.igeriv.bo.localita;

import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.LocalitaVo;
import it.dpe.igeriv.vo.MessaggioVo;
import it.dpe.igeriv.vo.PaeseVo;
import it.dpe.igeriv.vo.ProvinciaVo;
import it.dpe.igeriv.vo.TipoLocalitaVo;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
class LocalitaRepositoryImpl extends BaseRepositoryImpl implements LocalitaRepository {
	
	@Autowired
	LocalitaRepositoryImpl(BaseDao<MessaggioVo> dao) {
		super(dao);
	}
	
	@Override
	public List<TipoLocalitaVo> getTipiLocalita() {
		return getDao().findByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_TIPO_LOCALITA);
	}
	
	@Override
	public List<LocalitaVo> getLocalita() {
		return getDao().findByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_LOCALITA);
	}
	
	@Override
	public List<LocalitaVo> getLocalita(String localita) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LocalitaVo.class, "lo");
		criteria.createCriteria("lo.provinciaVo", "pr");
		if (localita != null && !localita.equals("")) {
			criteria.add(Restrictions.ilike("lo.descrizione", localita.toUpperCase(), MatchMode.START));
		}
		List<LocalitaVo> findByDetachedCriteria = getDao().findByDetachedCriteria(criteria);
		return findByDetachedCriteria;
	}
	
	@Override
	public List<PaeseVo> getPaesi() {
		return getDao().findByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_PAESI);
	}
	
	@Override
	public List<ProvinciaVo> getProvince() {
		return getDao().findByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_PROVINCIE);
	}
	
}
