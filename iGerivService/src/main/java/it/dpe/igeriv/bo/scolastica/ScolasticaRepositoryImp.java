package it.dpe.igeriv.bo.scolastica;

import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.OrdineLibriDto;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.EstrattoContoEdicolaDettaglioVo;
import it.dpe.igeriv.vo.OrdineLibriDettVo;
import it.dpe.igeriv.vo.OrdineLibriDettVo.ETrack;
import it.dpe.igeriv.vo.OrdineLibriVo;
import scala.Int;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

@Repository
class ScolasticaRepositoryImp extends BaseRepositoryImpl implements ScolasticaRepository{

	public static final String SEQ_9400 = "SEQUENZA_9400";
	public static final String SEQ_9401 = "SEQUENZA_9401"; 
	
	@Autowired
	ScolasticaRepositoryImp(BaseDao<OrdineLibriVo> dao) {
		super(dao);
		
	}

	@Override
	public List<OrdineLibriDto> findListOrdiniLibri(Integer codFiegDl,Integer codEdicola, Long numeroOrdine, Long codCliente) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriVo.class, "ordini");
		criteria.createCriteria("ordini.listDettaglioOrdine", "dettaglio", JoinType.INNER_JOIN);
		criteria.createCriteria("ordini.anagraficaEdicolaVo", "anagrafica_edicola", JoinType.INNER_JOIN);
		criteria.createCriteria("ordini.cliente", "cliente", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ordini.codDl", codFiegDl));
		if (codEdicola!=null) {
			criteria.add(Restrictions.eq("ordini.codDpeWebEdicola", codEdicola));
		}
		if(numeroOrdine!=null) {
			criteria.add(Restrictions.eq("ordini.numeroOrdine", numeroOrdine));
		}
		if(codCliente!=null) {
			criteria.add(Restrictions.eq("ordini.codCliente", codCliente));
		}
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("ordini.numeroOrdine"), "numeroOrdine");
		properties.add(Projections.property("ordini.listDettaglioOrdine"), "listDettaglioOrdine");
		properties.add(Projections.property("ordini.codDpeWebEdicola"), "codDpeWebEdicola");
		properties.add(Projections.property("ordini.anagraficaEdicolaVo"), "anagraficaEdicolaVo");
		properties.add(Projections.property("ordini.codCliente"), "codCliente");
		properties.add(Projections.property("ordini.dataAperturaOrdine"), "dataAperturaOrdine");
		properties.add(Projections.property("ordini.dataChiusuraOrdine"), "dataChiusuraOrdine");
		properties.add(Projections.property("ordini.note"), "note");	
		properties.add(Projections.property("ordini.numeroOrdineTxt"), "numeroOrdineTxt");
		
		criteria.setProjection(properties);
		criteria.addOrder(Order.asc("ordini.numeroOrdine"));
		criteria.setResultTransformer(Transformers.aliasToBean(OrdineLibriDto.class));	
		return getDao().findObjectByDetachedCriteria(criteria);	
		
	}
	
	public List<OrdineLibriVo> findOrdiniLibri(Integer codFiegDl, Integer codEdicola, Long numeroOrdine, Long codCliente, Long numeroCollo) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriVo.class);
		criteria.createCriteria("listDettaglioOrdine", "dettaglio");
		criteria.setFetchMode("listDettaglioOrdine", FetchMode.JOIN);
		criteria.add(Restrictions.eq("codDl", codFiegDl));
		if (codEdicola!=null) {
			criteria.add(Restrictions.eq("codDpeWebEdicola", codEdicola));
		}
		if(numeroOrdine!=null) {
			criteria.add(Restrictions.eq("numeroOrdine", numeroOrdine));
		}
		if(codCliente!=null) {
			criteria.add(Restrictions.eq("codCliente", codCliente));
		}
		if(numeroCollo!=null) {
			criteria.add(Restrictions.eq("dettaglio.numeroCollo", numeroCollo));
		}
		criteria.addOrder(Order.asc("numeroOrdine"));
		return getDao().findByDetachedCriteria(criteria);	
	}

	

	@Override
	public boolean deleteOrdineLibri(Long numeroOrdine) {
		getDao().bulkUpdate("delete from OrdineLibriDettVo vo where vo.numeroOrdine = ? ", new Object[]{numeroOrdine});
		getDao().bulkUpdate("delete from OrdineLibriVo vo where vo.numeroOrdine = ? ", new Object[]{numeroOrdine});
		return true;
	}

	
	@Override
	public boolean existOrdineAttivo(Integer codFiegDl, Integer codEdicola,Long codCliente) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriVo.class, "ordini");
		criteria.add(Restrictions.eq("ordini.codCliente", codCliente));
		criteria.add(Restrictions.eq("ordini.codDl", codFiegDl));
		criteria.add(Restrictions.eq("ordini.codDpeWebEdicola", codEdicola));
		criteria.add(Restrictions.isNull("ordini.dataChiusuraOrdine"));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("ordini.numeroOrdine"));
		criteria.setProjection(props);
		Long numeroOrdine = getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		return (numeroOrdine == null) ? false : true;

	}

	@Override
	public Long getOrdineAttivo(Integer codFiegDl, Integer codEdicola,Long codCliente) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriVo.class, "ordini");
		criteria.add(Restrictions.eq("ordini.codCliente", codCliente));
		criteria.add(Restrictions.eq("ordini.codDl", codFiegDl));
		criteria.add(Restrictions.eq("ordini.codDpeWebEdicola", codEdicola));
		criteria.add(Restrictions.isNull("ordini.dataChiusuraOrdine"));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("ordini.numeroOrdine"));
		criteria.setProjection(props);
		Long numeroOrdine = getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		return (numeroOrdine == null) ? null : numeroOrdine;
	}

	@Override
	public Long insertNewOrdineLibri(Integer codFiegDl, Integer codEdicola,Long codCliente) {
		OrdineLibriVo nuovoOrdine = new OrdineLibriVo();
		nuovoOrdine.setNumeroOrdine(getNewCodOrdine());
		nuovoOrdine.setCodDl(codFiegDl);
		nuovoOrdine.setCodDpeWebEdicola(codEdicola);
		nuovoOrdine.setCodCliente(codCliente);
		nuovoOrdine.setDataAperturaOrdine(new Date());
		saveBaseVo(nuovoOrdine);
		return nuovoOrdine.getNumeroOrdine();
	}
	
	
	private Long getNewCodOrdine(){
		return getDao().getNextSeqVal(SEQ_9400);
	}
	private Long getNewSeqOrdine(){
		return getDao().getNextSeqVal(SEQ_9401);
	}

	
	@Override
	public Long countLibriCarrello(Long numOrdine) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriDettVo.class, "dettordini");
		criteria.add(Restrictions.eq("dettordini.numeroOrdine", numOrdine));
		
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.count("dettordini.numeroOrdine"));
		criteria.setProjection(props);
		
		Long count = (Long) getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		return (count != null) ? count : new Long(0);
	}

	@Override
	public void addLibriCarrello(OrdineLibriDettVo dettaglioLibro) {
		if(dettaglioLibro.getSeqordine()==null)
			dettaglioLibro.setSeqordine(this.getNewSeqOrdine());
		saveBaseVo(dettaglioLibro);
	}

	@Override
	public OrdineLibriVo getDettaglioOrdineLibri(Long numeroOrdine) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriVo.class, "ordini");
		criteria.createCriteria("ordini.listDettaglioOrdine", "dettaglio", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ordini.anagraficaEdicolaVo", "anagrafica_edicola", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ordini.cliente", "cliente", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ordini.numeroOrdine", numeroOrdine));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public OrdineLibriVo getDettaglioOrdineLibri(Long numeroOrdine, ETrack track) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriVo.class, "ordini");
		criteria.createCriteria("ordini.listDettaglioOrdine", "dettaglio", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ordini.anagraficaEdicolaVo", "anagrafica_edicola", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ordini.cliente", "cliente", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ordini.numeroOrdine", numeroOrdine));
		criteria.add(Restrictions.eq("dettaglio.stato", track.getId()));
		criteria.add(Restrictions.isNull("dettaglio.dataArrivoCliente"));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	
	@Override
	public BigDecimal sumPrezzoLibriCarrello(Long numOrdine) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriDettVo.class, "dettordini");
		criteria.add(Restrictions.eq("dettordini.numeroOrdine", numOrdine));
		
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.sum("dettordini.prezzoCopertina"));
		criteria.setProjection(props);
		
		BigDecimal sumTot = (BigDecimal) getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		return (sumTot != null) ? sumTot : new BigDecimal(0);
	}
	
	
	@Override
	public BigDecimal sumPrezzoCopertinatura(Long numOrdine) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriDettVo.class, "dettordini");
		criteria.add(Restrictions.eq("dettordini.numeroOrdine", numOrdine));
		
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.sum("dettordini.prezzoCopertinaCliente"));
		criteria.setProjection(props);
		
		BigDecimal sumTot = (BigDecimal) getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		return (sumTot != null) ? sumTot : new BigDecimal(0);
	}

	@Override
	public BigDecimal sumPrezzoTotale(Long numOrdine) {
		BigDecimal sumPrezzoLibriCarrello = sumPrezzoLibriCarrello(numOrdine);
		BigDecimal sumPrezzoCopertinatura = sumPrezzoCopertinatura(numOrdine);
		BigDecimal sumTot = sumPrezzoLibriCarrello.add(sumPrezzoCopertinatura);
		return (sumTot != null) ? sumTot : new BigDecimal(0);
	}

	

	
	@Override
	public boolean deleteLibroDaOrdine(Long numeroOrdine,Long seqOrdine,String sku) {
		getDao().bulkUpdate("delete from OrdineLibriDettVo vo where vo.numeroOrdine = ? and vo.seqordine = ? and vo.sku = ? ", new Object[]{numeroOrdine,seqOrdine,sku});
		return true;
	
	}

	

	@Override
	public boolean confermaOrdine(OrdineLibriVo ordine) {
		saveBaseVo(ordine);
		return true;
	}
	@Override
	public List<OrdineLibriDettVo> getOrdiniFornitore(String ordineFornitore, ETrack track, Integer codEdicola) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriDettVo.class);
		criteria.createAlias("ordine", "o");
		
		if (ordineFornitore!=null) {
			criteria.add(Restrictions.eq("o.numeroOrdineTxt", ordineFornitore));
		}
		if (codEdicola!=null) {
			criteria.add(Restrictions.eq("o.codDpeWebEdicola", codEdicola));
		}
		
		if (track!=null) {
			criteria.add(Restrictions.eq("stato", track.getId()));
		}
		
		return getDao().findByDetachedCriteria(criteria);
		
	}
	public List<OrdineLibriDettVo> getOrdiniFornitore(Long[] ids) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriDettVo.class);
		
		
		if (ids!=null) {
			criteria.add(Restrictions.in("seqordine", ids));
		}
		return getDao().findByDetachedCriteria(criteria);
		
		
	}
	public List<OrdineLibriDettVo> getOrdiniFornitore(Long cliente) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriDettVo.class);
		criteria.createAlias("ordine", "o");
		
		if (cliente!=null) {
			criteria.add(Restrictions.eq("o.codCliente", cliente));
		}
		
		return getDao().findByDetachedCriteria(criteria);
		
		
	}
	
	@Override
	public OrdineLibriVo getOrdine(String numOrdineTxt) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriVo.class, "ordini");
		criteria.createCriteria("ordini.listDettaglioOrdine", "dettaglio", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ordini.anagraficaEdicolaVo", "anagrafica_edicola", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ordini.cliente", "cliente", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ordini.numeroOrdineTxt", numOrdineTxt));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public OrdineLibriVo getOrdine(String numOrdineTxt,Long codCliente) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriVo.class, "ordini");
		criteria.createCriteria("ordini.listDettaglioOrdine", "dettaglio", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ordini.anagraficaEdicolaVo", "anagrafica_edicola", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ordini.cliente", "cliente", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ordini.numeroOrdineTxt", numOrdineTxt));
		criteria.add(Restrictions.eq("ordini.cliente.codCliente", codCliente));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	

	@Override
	public OrdineLibriDettVo getDettaglioLibro(Long numeroOrdine, String sku) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriDettVo.class, "ordiniDett");
		criteria.add(Restrictions.eq("ordiniDett.numeroOrdine", numeroOrdine));
		criteria.add(Restrictions.eq("ordiniDett.sku", sku));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public OrdineLibriDettVo getDettaglioLibro(Long numeroOrdine, String sku, Long keynum) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriDettVo.class, "ordiniDett");
		criteria.add(Restrictions.eq("ordiniDett.numeroOrdine", numeroOrdine));
		criteria.add(Restrictions.eq("ordiniDett.sku", sku));
		criteria.add(Restrictions.eq("ordiniDett.keynum",keynum));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public OrdineLibriDettVo getDettaglioLibroNotKeyNum(Long numeroOrdine, String sku) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriDettVo.class, "ordiniDett");
		criteria.add(Restrictions.eq("ordiniDett.numeroOrdine", numeroOrdine));
		criteria.add(Restrictions.eq("ordiniDett.sku", sku));
		criteria.add(Restrictions.isNull("ordiniDett.keynum"));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public List<OrdineLibriDettVo> getDettaglioOrdiniByNumCollo(Integer codDl,Long numeroCollo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriDettVo.class, "ordiniDett");
		criteria.createCriteria("ordiniDett.ordine", "ordine", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("ordiniDett.numeroCollo", numeroCollo));
		criteria.add(Restrictions.isNull("ordiniDett.dataArrivoRivendita"));
		criteria.add(Restrictions.eq("ordine.codDl", codDl));
		return getDao().findObjectByDetachedCriteria(criteria);	
	}

	@Override
	public List<OrdineLibriDettVo> getDettaglioOrdiniInEdicolaByNumCollo(Integer codDl, Long numeroCollo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriDettVo.class, "ordiniDett");
		criteria.createCriteria("ordiniDett.ordine", "ordine", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("ordiniDett.numeroCollo", numeroCollo));
		criteria.add(Restrictions.isNotNull("ordiniDett.dataArrivoRivendita"));
		criteria.add(Restrictions.eq("ordine.codDl", codDl));
		
		return getDao().findObjectByDetachedCriteria(criteria);	
	}
	

	@Override
	public boolean existOrdiniInattivi(Integer codFiegDl, Integer codEdicola,Long codCliente) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriVo.class, "ordini");
		criteria.add(Restrictions.eq("ordini.codCliente", codCliente));
		criteria.add(Restrictions.eq("ordini.codDl", codFiegDl));
		criteria.add(Restrictions.eq("ordini.codDpeWebEdicola", codEdicola));
		criteria.add(Restrictions.isNull("ordini.dataChiusuraOrdine"));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("ordini.numeroOrdine"));
		criteria.setProjection(props);
		Long numeroOrdine = getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		
		Long libriNelCarrello = null;
		if(numeroOrdine!=null){
			libriNelCarrello = this.countLibriCarrello(numeroOrdine);
			return (libriNelCarrello!=null && libriNelCarrello == 0)?false:true;
		}
		
		return false;
		//return (numeroOrdine == null) ? false : true;
		
	}

	

	@Override
	public boolean existOrdini(Integer codFiegDl, Integer codEdicola,Long codCliente) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriVo.class, "ordini");
		criteria.add(Restrictions.eq("ordini.codCliente", codCliente));
		criteria.add(Restrictions.eq("ordini.codDl", codFiegDl));
		criteria.add(Restrictions.eq("ordini.codDpeWebEdicola", codEdicola));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.count("ordini.numeroOrdine"));
		criteria.setProjection(props);
		Number count = (Number) getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		return (count != null && count.intValue() > 0) ? true : false;
		
	}

	
	@Override
	public List<Long> getDistinctClientiByNumCollo(Integer codDl,Long numeroCollo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriVo.class, "ordini");
		criteria.setProjection( Projections.distinct( Projections.property( "ordini.numeroOrdine" ) ) );
		criteria.createCriteria("ordini.listDettaglioOrdine", "listDettaglioOrdine", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("ordini.codDl", codDl));
		criteria.add(Restrictions.eq("listDettaglioOrdine.numeroCollo", numeroCollo));
		return getDao().findObjectByDetachedCriteria(criteria);	
	}

	
	

	
	
	
	
	
	
	@Override 
	public int updateStatoOrdini(List<OrdineLibriDettVo> ordini, ETrack stato) {
		if (ordini ==null || ordini.isEmpty() || stato==null || stato == ETrack.ND) {
			return 0;
		}
		Timestamp dataArrivo = getDao().getSysdate();
		for (OrdineLibriDettVo vo : ordini) {
			switch (stato) {
				case DL:	vo.setDataArrivoDL(dataArrivo);			break;
				case RIV:	vo.setDataArrivoRivendita(dataArrivo);	break;
				case CLI:	vo.setDataArrivoCliente(dataArrivo);	break;
				default:											break;
			}
			vo.setStato(stato.getId());
			vo = getDao().update(vo);
		}
	
		return ordini.size();
	}

	
	

	@Override
	public List<OrdineLibriDto> findListOrdiniPerDettaglio(Integer codFiegDl,
			Integer codEdicola, Long numeroOrdine, Long codCliente) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriVo.class, "ordini");
		criteria.createCriteria("ordini.anagraficaEdicolaVo", "anagrafica_edicola", JoinType.INNER_JOIN);
		criteria.createCriteria("ordini.cliente", "cliente", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ordini.codDl", codFiegDl));
		if (codEdicola!=null) {
			criteria.add(Restrictions.eq("ordini.codDpeWebEdicola", codEdicola));
		}
		if(numeroOrdine!=null) {
			criteria.add(Restrictions.eq("ordini.numeroOrdine", numeroOrdine));
		}
		if(codCliente!=null) {
			criteria.add(Restrictions.eq("ordini.codCliente", codCliente));
		}
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("ordini.numeroOrdine"), "numeroOrdine");
		properties.add(Projections.property("ordini.listDettaglioOrdine"), "listDettaglioOrdine");
		properties.add(Projections.property("ordini.codDpeWebEdicola"), "codDpeWebEdicola");
		properties.add(Projections.property("ordini.anagraficaEdicolaVo"), "anagraficaEdicolaVo");
		properties.add(Projections.property("ordini.codCliente"), "codCliente");
		properties.add(Projections.property("ordini.dataAperturaOrdine"), "dataAperturaOrdine");
		properties.add(Projections.property("ordini.dataChiusuraOrdine"), "dataChiusuraOrdine");
		properties.add(Projections.property("ordini.note"), "note");	
		properties.add(Projections.property("ordini.numeroOrdineTxt"), "numeroOrdineTxt");
		
		criteria.setProjection(properties);
		criteria.addOrder(Order.asc("ordini.numeroOrdine"));
		criteria.setResultTransformer(Transformers.aliasToBean(OrdineLibriDto.class));	
		return getDao().findObjectByDetachedCriteria(criteria);	
	}

	
	@Override
	public boolean existLibriDaConsegnare(Integer codDl, String numOrdineTxt){
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriVo.class, "ordini");
		criteria.createCriteria("ordini.listDettaglioOrdine", "listDettaglioOrdine", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("ordini.codDl", codDl));
		criteria.add(Restrictions.eq("ordini.numeroOrdineTxt", numOrdineTxt));
		criteria.add(Restrictions.eq("listDettaglioOrdine.stato", ETrack.RIV.getId()));
		
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.count("ordini.numeroOrdine"));
		criteria.setProjection(props);
		Number count = (Number) getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		return (count != null && count.intValue() > 0) ? true : false;
	}

	@Override
	public boolean existTrackingOrdine(Integer codDl, String numOrdineTxt) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriVo.class, "ordini");
		criteria.createCriteria("ordini.listDettaglioOrdine", "listDettaglioOrdine", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("ordini.codDl", codDl));
		criteria.add(Restrictions.eq("ordini.numeroOrdineTxt", numOrdineTxt));
		criteria.add(Restrictions.not(Restrictions.eq("listDettaglioOrdine.stato", ETrack.CLI.getId())));
		
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.count("ordini.numeroOrdine"));
		criteria.setProjection(props);
		Number count = (Number) getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		return (count != null && count.intValue() > 0) ? true : false;
	}

	@Override
	public boolean existTrackingOrdineEdicola(Integer codDl,Integer codDpeWebEdicola, String numOrdineTxt) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriVo.class, "ordini");
		criteria.createCriteria("ordini.listDettaglioOrdine", "listDettaglioOrdine", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("ordini.codDl", codDl));
		criteria.add(Restrictions.eq("ordini.codDpeWebEdicola", codDpeWebEdicola));
		criteria.add(Restrictions.eq("ordini.numeroOrdineTxt", numOrdineTxt));
		criteria.add(Restrictions.not(Restrictions.eq("listDettaglioOrdine.stato", ETrack.CLI.getId())));
		
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.count("ordini.numeroOrdine"));
		criteria.setProjection(props);
		Number count = (Number) getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		return (count != null && count.intValue() > 0) ? true : false;
	}
	
	
	
	
	@Override
	public boolean existTrackingOrdineParzialmenteEvaso(Integer codDl,
			String numOrdineTxt) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriVo.class, "ordini");
		criteria.createCriteria("ordini.listDettaglioOrdine", "listDettaglioOrdine", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("ordini.codDl", codDl));
		criteria.add(Restrictions.eq("ordini.numeroOrdineTxt", numOrdineTxt));
		criteria.add(Restrictions.eq("listDettaglioOrdine.stato", ETrack.CLI.getId()));
		
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.count("ordini.numeroOrdine"));
		criteria.setProjection(props);
		Number count = (Number) getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		return (count != null && count.intValue() > 0) ? true : false;
	}

	

	@Override
	public List<Date> getDateDiConsegnaCliente(Integer codDl, String numOrdineTxt) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriVo.class, "ordini");
		criteria.createCriteria("ordini.listDettaglioOrdine", "listDettaglioOrdine", JoinType.INNER_JOIN);
		criteria.setProjection( Projections.distinct( Projections.property( "listDettaglioOrdine.dataArrivoCliente" ) ) );
		criteria.add(Restrictions.eq("ordini.codDl", codDl));
		criteria.add(Restrictions.eq("ordini.numeroOrdineTxt", numOrdineTxt));
		criteria.add(Restrictions.eq("listDettaglioOrdine.stato", ETrack.CLI.getId()));
		criteria.addOrder(Order.desc("listDettaglioOrdine.dataArrivoCliente"));
		return getDao().findObjectByDetachedCriteria(criteria);	
	}

	@Override
	public boolean existOrdiniDaConsegnareAlCliente(Integer codFiegDl,Integer codEdicola, Long codCliente) {
		boolean res = false;
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriVo.class, "ordini");
		criteria.add(Restrictions.eq("ordini.codCliente", codCliente));
		criteria.add(Restrictions.eq("ordini.codDl", codFiegDl));
		criteria.add(Restrictions.eq("ordini.codDpeWebEdicola", codEdicola));
		criteria.add(Restrictions.isNotNull("ordini.dataChiusuraOrdine"));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("ordini.numeroOrdineTxt"));
		criteria.setProjection(props);
		List<String> listNumeroOrdineTxt = getDao().findObjectByDetachedCriteria(criteria);	
		if(listNumeroOrdineTxt!=null){
			for(String numTxt : listNumeroOrdineTxt){
				boolean boolRes = existLibriDaConsegnare(codFiegDl,numTxt);
				if(boolRes){
					res = boolRes;
					break;
				}
			}
			
		}
		return res;
	}

	@Override
	public boolean visualizzareOrdineCliente(Long numeroOrdine) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriVo.class, "ordini");
		criteria.createCriteria("ordini.listDettaglioOrdine", "listDettaglioOrdine", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("ordini.numeroOrdine", numeroOrdine));
		
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.count("ordini.numeroOrdine"));
		criteria.setProjection(props);
		Number count = (Number) getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		return (count != null && count.intValue() > 0) ? true : false;
	}

	@Override
	public OrdineLibriDettVo getInformazioneCopertinaLibro(Long numeroOrdine, Long seqOrdine) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriDettVo.class, "ordiniDett");
		criteria.add(Restrictions.eq("ordiniDett.seqordine", seqOrdine));
		criteria.add(Restrictions.eq("ordiniDett.numeroOrdine", numeroOrdine));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public OrdineLibriDettVo getInformazioneCopertinaUltimoLibroInserito(Long numeroOrdine) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriDettVo.class, "ordiniDett");
		criteria.add(Restrictions.eq("ordiniDett.numeroOrdine", numeroOrdine));
		criteria.add(Restrictions.eq("ordiniDett.flagCopertina", new Integer(1)));
		criteria.addOrder(Order.desc("ordiniDett.seqordine"));
//		ProjectionList props = Projections.projectionList();
//		props.add(Projections.max("ordiniDett.seqordine"));
//		criteria.setProjection(props);
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public List<OrdineLibriVo> getListOrdiniCliente(Integer codFiegDl,Integer codEdicola, Long codCliente) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(OrdineLibriVo.class, "ordini");
		criteria.add(Restrictions.eq("ordini.codCliente", codCliente));
		criteria.add(Restrictions.eq("ordini.codDl", codFiegDl));
		criteria.add(Restrictions.eq("ordini.codDpeWebEdicola", codEdicola));
		
		return getDao().findByDetachedCriteria(criteria);
	}


	

	

	
	
	
}
