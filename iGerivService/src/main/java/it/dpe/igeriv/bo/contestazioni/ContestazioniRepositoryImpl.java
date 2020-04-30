package it.dpe.igeriv.bo.contestazioni;

import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.ContestazioneResaDto;
import it.dpe.igeriv.vo.ContestazioneResaVo;
import it.dpe.igeriv.vo.UserVo;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
class ContestazioniRepositoryImpl extends BaseRepositoryImpl implements ContestazioniRepository {
	
	@Autowired
	ContestazioniRepositoryImpl(BaseDao<UserVo> dao) {
		super(dao);
	}
	
	@Override
	public List<ContestazioneResaDto> getContestazioniResa(Integer codFiegDl, Integer codEdicola, Timestamp dataDa, Timestamp dataA, String titolo, Integer stato) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ContestazioneResaVo.class, "crvo");
		criteria.add(Restrictions.eq("crvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("crvo.codEdicola", codEdicola));
		if (titolo != null && !titolo.equals("")) {
			criteria.createCriteria("crvo.copertina", "ac");
			criteria.createCriteria("ac.anagraficaPubblicazioniVo", "ap");
			criteria.add(Restrictions.ilike("ap.titolo", titolo, MatchMode.ANYWHERE));
		} else {
			criteria.createCriteria("crvo.copertina", "ac", JoinType.LEFT_OUTER_JOIN);
			criteria.createCriteria("ac.anagraficaPubblicazioniVo", "ap", JoinType.LEFT_OUTER_JOIN);
		}
		if (dataDa != null && dataA != null) {
			criteria.add(Restrictions.between("crvo.dtBolla", dataDa, dataA));
		}
		if (stato != null) {
			criteria.add(Restrictions.eq("crvo.stato", stato));
		}
		criteria.addOrder(Order.asc("crvo.dataRegistrazione")).addOrder(Order.asc("crvo.tipoBolla")).addOrder(Order.asc("ap.titolo"));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("crvo.dtBolla"), "dataResa");
		properties.add(Projections.property("crvo.tipoBolla"), "tipoResa"); 
		properties.add(Projections.property("ap.titolo"), "titolo");
		properties.add(Projections.property("ac.numeroCopertina"), "numero"); 
		properties.add(Projections.property("crvo.stato"), "stato");
		properties.add(Projections.property("crvo.quantita"), "copieDichiarate");
		properties.add(Projections.property("crvo.quantitaApprovata"), "copieApprovate");
		properties.add(Projections.property("crvo.importoValore"), "importoDichiarato");
		properties.add(Projections.property("crvo.importoValoreApprovato"), "importoApprovato");
		properties.add(Projections.property("crvo.motivazioneRifiuto"), "motivazioneRifiuto");
		properties.add(Projections.property("crvo.noteChiusura"), "noteChiusura");
		criteria.setProjection(properties);
		criteria.setResultTransformer(Transformers.aliasToBean(ContestazioneResaDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
}
