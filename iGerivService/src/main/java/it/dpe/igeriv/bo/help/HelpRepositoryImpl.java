package it.dpe.igeriv.bo.help;

import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.HelpVideoDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.HelpVo;
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
class HelpRepositoryImpl extends BaseRepositoryImpl implements HelpRepository {
	
	@Autowired
	HelpRepositoryImpl(BaseDao<UserVo> dao) {
		super(dao);
	}
	
	@Override
	public List<HelpVo> getAllHelp() {
		return getDao().findByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_ALL_HELP);
	}

	@Override
	public List<HelpVo> getHelpByCod(Integer[] codHelp) {
		DetachedCriteria criteria = DetachedCriteria.forClass(HelpVo.class, "help");
		criteria.createCriteria("help.videoHelp", "vHelp");
		criteria.add(Restrictions.in("help.codice", codHelp));
		//criteria.addOrder(Order.asc("ae.ragioneSocialeEdicolaPrimaRiga"));
		
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("help.codice"), "codice");
		properties.add(Projections.property("vHelp.titolo"), "titolo");
		properties.add(Projections.property("vHelp.nomeFile"), "nomeFile");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(HelpVideoDto.class));
		
		return getDao().findByDetachedCriteria(criteria);
	}
	
	
}
