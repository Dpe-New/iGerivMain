package it.dpe.igeriv.bo.arretrati;

import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.ArretratiDto;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.ArretratiVo;

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
class ArretratiRepositoryImpl extends BaseRepositoryImpl implements ArretratiRepository<ArretratiDto> {
	
	@Autowired
	ArretratiRepositoryImpl(BaseDao<ArretratiDto> dao) {
		super(dao);
	}
	
	@Override
	public List<ArretratiDto> getArretrati(Integer codFiegDl, Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArretratiVo.class, "ri");
		criteria.createCriteria("ri.anagraficaPubblicazione", "pub");
		criteria.createCriteria("ri.copertina", "co", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("co.anagraficaPubblicazioniVo", "ap", JoinType.LEFT_OUTER_JOIN);		
		criteria.add(Restrictions.eq("ri.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("ri.codEdicola", codEdicola));
		criteria.add(Restrictions.in("ri.indicatoreTrasmessoDl", new Object[]{0, 1}));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("ri.codiceArretrato"), "codiceArretrato");
		properties.add(Projections.property("ri.dtBolla"), "dtBolla");
		properties.add(Projections.property("ri.indicatoreTrasmessoDl"), "indicatoreTrasmessoDl");
		properties.add(Projections.property("pub.titolo"), "titolo");
		properties.add(Projections.property("co.sottoTitolo"), "sottoTitolo");
		properties.add(Projections.property("ri.numeroCopertina"), "numeroCopertina");
		properties.add(Projections.property("ri.dataUscita"), "dataUscita");
		properties.add(Projections.property("ri.quantita"), "quantita");
		properties.add(Projections.property("ri.note"), "note");		
		properties.add(Projections.property("ri.confermaSi"), "confermaSi");
		properties.add(Projections.property("ri.confermaNo"), "confermaNo");
		criteria.setProjection(properties);
		criteria.addOrder(Order.asc("ri.dtBolla")).addOrder(Order.asc("ri.posizioneRiga"));
		criteria.setResultTransformer(Transformers.aliasToBean(ArretratiDto.class));	
		return getDao().findObjectByDetachedCriteria(criteria);	
	}
	
	@Override
	public List<ArretratiDto> getReportArretrati(Integer codFiegDl, Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArretratiVo.class, "ri");
		criteria.createCriteria("ri.anagraficaPubblicazione", "pub");
		criteria.createCriteria("ri.copertina", "co", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("co.anagraficaPubblicazioniVo", "ap", JoinType.LEFT_OUTER_JOIN);		
		criteria.add(Restrictions.eq("ri.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("ri.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("ri.indicatoreTrasmessoDl", 3));
		criteria.add(Restrictions.eq("ri.confermaSi", true));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("ri.dataEvasione"), "dataEvasione");
		properties.add(Projections.property("ri.dataSpedizioneConferma"), "dataSpedizioneConferma");
		properties.add(Projections.property("pub.titolo"), "titolo");
		properties.add(Projections.property("co.sottoTitolo"), "sottoTitolo");
		properties.add(Projections.property("ri.numeroCopertina"), "numeroCopertina");
		properties.add(Projections.property("ri.dataUscita"), "dataUscita");
		properties.add(Projections.property("ri.quantita"), "quantita");
		properties.add(Projections.property("ri.quantitaEvasa"), "quantitaEvasa");
		properties.add(Projections.property("ri.note"), "note");		
		criteria.setProjection(properties);
		criteria.addOrder(Order.desc("ri.dataEvasione")).addOrder(Order.asc("ap.titolo")).addOrder(Order.asc("co.dataUscita"));
		criteria.setResultTransformer(Transformers.aliasToBean(ArretratiDto.class));	
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public void updateArretrati(Map<Integer, Integer> mapConfermeArretrati, Map<Integer, String> mapNoteArretrato) {
		for (Map.Entry<Integer, Integer> entry : mapConfermeArretrati.entrySet()) {
			Integer key = entry.getKey();
			ArretratiVo vo = getDao().findUniqueResultByQuery("from ArretratiVo vo where vo.codiceArretrato = ?", new Object[]{key});
			if (vo != null) {
				Integer value = entry.getValue();
				String nota = mapNoteArretrato.get(key);
				if (value != null && value.equals(IGerivConstants.COD_SI)) {
					vo.setConfermaSi(true);
					vo.setConfermaNo(null);
					vo.setIndicatoreTrasmessoDl(1);
				} else if (value != null && value.equals(IGerivConstants.COD_NO)) {
					vo.setConfermaSi(null);
					vo.setConfermaNo(true);
					vo.setIndicatoreTrasmessoDl(1);
				} else {
					vo.setConfermaSi(null);
					vo.setConfermaNo(null);
				}
				vo.setNote(nota);
				getDao().saveOrUpdate(vo);
			}
		}
	}

}
