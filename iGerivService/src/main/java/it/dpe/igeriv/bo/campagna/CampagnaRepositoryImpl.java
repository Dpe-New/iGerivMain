package it.dpe.igeriv.bo.campagna;

import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.CampagnaDto;
import it.dpe.igeriv.dto.CampagnaEdicoleDto;
import it.dpe.igeriv.dto.EmailDlDto;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.CampagnaEdicoleVo;
import it.dpe.igeriv.vo.CampagnaVo;
import it.dpe.igeriv.vo.EmailDlVo;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
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
public class CampagnaRepositoryImpl extends BaseRepositoryImpl implements CampagnaRepository{

	
	@Autowired
	CampagnaRepositoryImpl(BaseDao<CampagnaVo> dao) {
		super(dao);
	}

	@Override
	public List<CampagnaDto> getCampagnaIsActive() {
		DetachedCriteria criteria = DetachedCriteria.forClass(CampagnaVo.class, "em");
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("em.id9226"), 			"id9226");
		properties.add(Projections.property("em.coddl9226"), 		"coddl9226"); 
		properties.add(Projections.property("em.dlrif9226"), 		"dlrif9226");
		properties.add(Projections.property("em.dtinizio9226"), 	"dtinizio9226"); 
		properties.add(Projections.property("em.dtfine9226"), 		"dtfine9226"); 
		properties.add(Projections.property("em.dtfinesoll9226"), 	"dtfinesoll9226"); 
		properties.add(Projections.property("em.tr1Op1Al9226"), 	"tr1Op1Al9226"); 
		properties.add(Projections.property("em.tr1Op1Dal9226"), 	"tr1Op1Dal9226"); 
		properties.add(Projections.property("em.tr1Op2Al9226"), 	"tr1Op2Al9226"); 
		properties.add(Projections.property("em.tr1Op2Dal9226"), 	"tr1Op2Dal9226"); 
		properties.add(Projections.property("em.tr2Op1Al9226"), 	"tr2Op1Al9226"); 
		properties.add(Projections.property("em.tr2Op1Dal9226"), 	"tr2Op1Dal9226"); 
		properties.add(Projections.property("em.tr2Op2Al9226"), 	"tr2Op2Al9226"); 
		properties.add(Projections.property("em.tr2Op2Dal9226"), 	"tr2Op2Dal9226"); 
	
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(CampagnaDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}

	@Override
	public List<CampagnaDto> getCampagnaByCodiceDl(Integer codDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CampagnaVo.class, "em");
		criteria.add(Restrictions.eq("em.coddl9226", codDl));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("em.id9226"), 			"id9226");
		properties.add(Projections.property("em.coddl9226"), 		"coddl9226"); 
		properties.add(Projections.property("em.dlrif9226"), 		"dlrif9226");
		properties.add(Projections.property("em.dtinizio9226"), 	"dtinizio9226"); 
		properties.add(Projections.property("em.dtfine9226"), 		"dtfine9226"); 
		properties.add(Projections.property("em.dtfinesoll9226"), 	"dtfinesoll9226"); 
		properties.add(Projections.property("em.tr1Op1Al9226"), 	"tr1Op1Al9226"); 
		properties.add(Projections.property("em.tr1Op1Dal9226"), 	"tr1Op1Dal9226"); 
		properties.add(Projections.property("em.tr1Op2Al9226"), 	"tr1Op2Al9226"); 
		properties.add(Projections.property("em.tr1Op2Dal9226"), 	"tr1Op2Dal9226"); 
		properties.add(Projections.property("em.tr2Op1Al9226"), 	"tr2Op1Al9226"); 
		properties.add(Projections.property("em.tr2Op1Dal9226"), 	"tr2Op1Dal9226"); 
		properties.add(Projections.property("em.tr2Op2Al9226"), 	"tr2Op2Al9226"); 
		properties.add(Projections.property("em.tr2Op2Dal9226"), 	"tr2Op2Dal9226"); 
	
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(CampagnaDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}

	@Override
	public List<CampagnaEdicoleDto> getEdicoleByIdCampagna(Integer idCamp) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CampagnaEdicoleVo.class, "em");
		criteria.add(Restrictions.eq("em.campagna.id9226", idCamp));
		
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("em.crivw9227"), 		"crivw9227");
		properties.add(Projections.property("em.dtOp1Al9227"), 		"dtOp1Al9227"); 
		properties.add(Projections.property("em.dtOp1Dal9227"), 	"dtOp1Dal9227");
		properties.add(Projections.property("em.dtOp2Al9227"), 		"dtOp2Al9227"); 
		properties.add(Projections.property("em.dtOp2Dal9227"), 	"dtOp2Dal9227"); 
		properties.add(Projections.property("em.dtconferma9227"), 	"dtconferma9227"); 
		properties.add(Projections.property("em.flgaperto9227"), 	"flgaperto9227"); 
		properties.add(Projections.property("em.flgstato9227"), 	"flgstato9227"); 
		properties.add(Projections.property("em.totup9227"), 		"totup9227"); 
		properties.add(Projections.property("em.turno9227"), 		"turno9227"); 
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(CampagnaEdicoleDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}

	@Override
	public List<CampagnaEdicoleDto> getEdicoleByIdCampagnaStato(Integer idCamp,Integer flagStato) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CampagnaEdicoleVo.class, "em");
		criteria.add(Restrictions.eq("em.campagna.id9226", idCamp));
		criteria.add(Restrictions.eq("em.flgstato9227", flagStato));
		
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("em.crivw9227"), 		"crivw9227");
		properties.add(Projections.property("em.dtOp1Al9227"), 		"dtOp1Al9227"); 
		properties.add(Projections.property("em.dtOp1Dal9227"), 	"dtOp1Dal9227");
		properties.add(Projections.property("em.dtOp2Al9227"), 		"dtOp2Al9227"); 
		properties.add(Projections.property("em.dtOp2Dal9227"), 	"dtOp2Dal9227"); 
		properties.add(Projections.property("em.dtconferma9227"), 	"dtconferma9227"); 
		properties.add(Projections.property("em.flgaperto9227"), 	"flgaperto9227"); 
		properties.add(Projections.property("em.flgstato9227"), 	"flgstato9227"); 
		properties.add(Projections.property("em.totup9227"), 		"totup9227"); 
		properties.add(Projections.property("em.turno9227"), 		"turno9227"); 
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(CampagnaEdicoleDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public CampagnaEdicoleVo getEdicoleByCrivw(Integer crivw) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CampagnaEdicoleVo.class, "em");
		criteria.add(Restrictions.eq("em.crivw9227", crivw));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public void updateCampagnaEdicole(CampagnaEdicoleVo campEdicole) {
		saveBaseVo(campEdicole);
	}

	
	
	@Override
	public CampagnaVo getCampagnaByIdCamp(Integer idCamp) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CampagnaVo.class, "em");
		criteria.add(Restrictions.eq("em.id9226", idCamp));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}

	

	
	
	
}
