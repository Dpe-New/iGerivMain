package it.dpe.igeriv.bo.rilevamenti;

import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.RilevamentiDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.vo.RilevamentiVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
class RilevamentiRepositoryImpl extends BaseRepositoryImpl implements RilevamentiRepository {
	
	@Autowired
	RilevamentiRepositoryImpl(BaseDao<RilevamentiVo> dao) {
		super(dao);
	}
	
	@Override
	public List<RilevamentiDto> getRilevamenti(Integer codFiegDl, Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RilevamentiVo.class, "ri");
		criteria.createCriteria("ri.copertina", "co");
		criteria.createCriteria("co.immagine", "im", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("co.anagraficaPubblicazioniVo", "ap");		
		criteria.add(Restrictions.eq("ri.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("ri.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("ri.indicatoreTrasmessoDl", 0));
		criteria.add(Restrictions.eq("ri.dtBolla", DateUtilities.floorDay(new Date())));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("ri.codiceRilevamento"), "codiceRilevamento");
		properties.add(Projections.property("ri.codFiegDl"), "codFiegDl");
		properties.add(Projections.property("ri.codEdicola"), "codEdicola");
		properties.add(Projections.property("ri.dtBolla"), "dtBolla");
		properties.add(Projections.property("ri.tipoBolla"), "tipoBolla");
		properties.add(Projections.property("ri.posizioneRiga"), "posizioneRiga");
		properties.add(Projections.property("ri.idtn"), "idtn");
		properties.add(Projections.property("ri.distribuito"), "distribuito");
		properties.add(Projections.property("ri.rilevamento"), "rilevamento");
		properties.add(Projections.property("ri.indicatoreTrasmessoDl"), "indicatoreTrasmessoDl");
		properties.add(Projections.property("ap.titolo"), "titolo");
		properties.add(Projections.property("co.sottoTitolo"), "sottoTitolo");
		properties.add(Projections.property("co.numeroCopertina"), "numeroCopertina");
		properties.add(Projections.property("co.dataUscita"), "dataUscita");
		properties.add(Projections.property("co.prezzoCopertina"), "prezzoCopertina");
		properties.add(Projections.property("im.nome"), "nomeImmagine");		
		criteria.setProjection(properties);
		criteria.addOrder(Order.asc("ap.titolo"));
		criteria.setResultTransformer(Transformers.aliasToBean(RilevamentiDto.class));	
		return getDao().findObjectByDetachedCriteria(criteria);	
	}
	
	@Override
	public void updateRilevamenti(Map<Integer, Integer> mapRilevamenti) {
		for (Map.Entry<Integer, Integer> entry : mapRilevamenti.entrySet()) {
			RilevamentiVo vo = getDao().findUniqueResultByQuery("from RilevamentiVo vo where vo.codiceRilevamento = ?", new Object[]{entry.getKey()});
			if (vo != null) {
				vo.setRilevamento(entry.getValue());
				getDao().saveOrUpdate(vo);
			}
		}
	}

}
