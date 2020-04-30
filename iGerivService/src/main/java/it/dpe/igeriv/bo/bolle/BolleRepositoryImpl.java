package it.dpe.igeriv.bo.bolle;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.selectUnique;
import static ch.lambdaj.Lambda.sort;
import static org.hamcrest.Matchers.equalTo;
import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.BollaDettaglioDto;
import it.dpe.igeriv.dto.BollaResaDettaglioDto;
import it.dpe.igeriv.dto.BollaResaFuoriVoceDettaglioDto;
import it.dpe.igeriv.dto.BollaResaRichiamoPersonalizzatoDettaglioDto;
import it.dpe.igeriv.dto.BollaRiassuntoDto;
import it.dpe.igeriv.dto.BollaVoDto;
import it.dpe.igeriv.dto.FondoBollaDettaglioDto;
import it.dpe.igeriv.dto.LavorazioneResaImmagineDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.dto.QuadraturaResaDto;
import it.dpe.igeriv.dto.ResaRiscontrataDto;
import it.dpe.igeriv.dto.StoricoCopertineDto;
import it.dpe.igeriv.dto.VenditeParamDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.AnagraficaPubblicazioniVo;
import it.dpe.igeriv.vo.BollaDettaglioVo;
import it.dpe.igeriv.vo.BollaResaDettaglioVo;
import it.dpe.igeriv.vo.BollaResaFuoriVoceVo;
import it.dpe.igeriv.vo.BollaResaRiassuntoVo;
import it.dpe.igeriv.vo.BollaResaRichiamoPersonalizzatoVo;
import it.dpe.igeriv.vo.BollaResaVo;
import it.dpe.igeriv.vo.BollaRiassuntoVo;
import it.dpe.igeriv.vo.BollaStatisticaStoricoVo;
import it.dpe.igeriv.vo.BollaVo;
import it.dpe.igeriv.vo.DecodificaRichiamiResaVo;
import it.dpe.igeriv.vo.FondoBollaDettaglioVo;
import it.dpe.igeriv.vo.LavorazioneResaImmagineVo;
import it.dpe.igeriv.vo.LavorazioneResaVo;
import it.dpe.igeriv.vo.ResaRiscontrataVo;
import it.dpe.igeriv.vo.RilevamentiVo;
import it.dpe.igeriv.vo.StoricoCopertineVo;
import it.dpe.igeriv.vo.pk.BollaResaFuoriVocePk;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;

@Repository
class BolleRepositoryImpl extends BaseRepositoryImpl implements BolleRepository {
	private final PubblicazioniService pubblicazioniService;
	private final int numMaxBolleManutezioneDl;
	
	@Autowired
	BolleRepositoryImpl(BaseDao<RilevamentiVo> dao, PubblicazioniService pubblicazioniService, @Value("${igeriv.num.max.bolle.manutenzione.dl}") int numMaxBolleManutezioneDl) {
		super(dao);
		this.pubblicazioniService = pubblicazioniService;
		this.numMaxBolleManutezioneDl = numMaxBolleManutezioneDl;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BollaRiassuntoVo> getBolleRiassunto(final Integer[] codFiegDl,
			final Integer[] codEdicolaDl, final Timestamp dataBolla) {
		List<BollaRiassuntoVo> bolleRiassunto = null;
		if (dataBolla != null && codEdicolaDl != null) {
			HibernateCallback<List<BollaRiassuntoVo>> action = new HibernateCallback<List<BollaRiassuntoVo>>() {
				@Override
				public List<BollaRiassuntoVo> doInHibernate(Session session) throws HibernateException, SQLException {
					Query namedQuery = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_GET_BOLLE_RIASSUNTO_EDICOLA_BY_DATE);
					namedQuery.setParameterList("codFiegDl", codFiegDl);
					namedQuery.setParameterList("codEdicola", codEdicolaDl);
					namedQuery.setTimestamp("dtBolla", dataBolla);
					return namedQuery.list();
				}
			};
			bolleRiassunto = getDao().findByHibernateCallback(action);
		} else if (dataBolla != null && codEdicolaDl == null) {
			HibernateCallback<List<BollaRiassuntoVo>> action = new HibernateCallback<List<BollaRiassuntoVo>>() {
				@Override
				public List<BollaRiassuntoVo> doInHibernate(Session session) throws HibernateException, SQLException {
					Query namedQuery = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_GET_BOLLE_RIASSUNTO_EDICOLE_BY_DATE);
					namedQuery.setParameterList("codFiegDl", codFiegDl);
					namedQuery.setTimestamp("dtBolla", dataBolla);
					return namedQuery.list();
				}
			};
			bolleRiassunto = getDao().findByHibernateCallback(action);
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DAY_OF_YEAR, -numMaxBolleManutezioneDl);
			final Timestamp dtBolla = DateUtilities.floorDay(cal.getTime());
			HibernateCallback<List<BollaRiassuntoVo>> action = new HibernateCallback<List<BollaRiassuntoVo>>() {
				@Override
				public List<BollaRiassuntoVo> doInHibernate(Session session) throws HibernateException, SQLException {
					Query namedQuery = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_GET_BOLLE_RIASSUNTO);
					namedQuery.setParameterList("codFiegDl", codFiegDl);
					namedQuery.setParameterList("codEdicola", codEdicolaDl);
					namedQuery.setTimestamp("dtBolla", dtBolla);
					return namedQuery.list();
				}
			};
			bolleRiassunto = getDao().findByHibernateCallback(action);
		}
		return bolleRiassunto;
	}
	
	@Override
	public List<BollaRiassuntoVo> getBolleRiassunto(Integer[] codFiegDl, Integer[] codEdicolaDl, Timestamp dataBolla, String tipoBolla, Integer statoBolla, Boolean excludeSospese) {
		List<BollaRiassuntoVo> bolleRiassunto = null;

		DetachedCriteria criteria = null;
		criteria = DetachedCriteria.forClass(BollaRiassuntoVo.class, "br");
		criteria.createCriteria("br.abbinamentoEdicolaDlVo", "aedl");
		criteria.createCriteria("aedl.anagraficaEdicolaVo", "ae");
		criteria.add(Restrictions.in("br.pk.codFiegDl", codFiegDl));
		if (codEdicolaDl != null) {
			criteria.add(Restrictions.in("br.pk.codEdicola", codEdicolaDl));
			criteria.addOrder(Order.desc("br.pk.dtBolla"));
		}
		else {
			criteria.addOrder(Order.desc("aedl.codEdicolaDl"));
		}
		if (dataBolla != null) {
			criteria.add(Restrictions.eq("br.pk.dtBolla", dataBolla));
		}
		else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DAY_OF_YEAR, -numMaxBolleManutezioneDl);
			criteria.add(Restrictions.ge("br.pk.dtBolla", DateUtilities.floorDay(cal.getTime())));
		}
		if (tipoBolla != null) {
			criteria.add(Restrictions.eq("br.pk.tipoBolla", tipoBolla));
		}
		if (statoBolla != null) {
			criteria.add(Restrictions.eq("br.bollaTrasmessaDl", statoBolla));
		}
		if (excludeSospese) {
			criteria.add(Restrictions.or(Restrictions.isNull("aedl.dtSospensioneEdicola"), Restrictions.ltProperty("br.pk.dtBolla", "aedl.dtSospensioneEdicola")));
		}
		
		bolleRiassunto = getDao().findByDetachedCriteria(criteria);
		
		return bolleRiassunto;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<BollaResaRiassuntoVo> getBolleResaRiassunto(final Integer[] codFiegDl, final Integer[] codEdicolaDl, final Timestamp dataBolla) {
		Set<BollaResaRiassuntoVo> bolleRiassunto = null;
		if (dataBolla != null && codEdicolaDl != null) {
			HibernateCallback<Set<BollaResaRiassuntoVo>> action = new HibernateCallback<Set<BollaResaRiassuntoVo>>() {
				@Override
				public Set<BollaResaRiassuntoVo> doInHibernate(Session session) throws HibernateException, SQLException {
					Query namedQuery = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_GET_BOLLE_RESA_RIASSUNTO_EDICOLA_BY_DATE);
					namedQuery.setParameterList("codFiegDl", codFiegDl);
					namedQuery.setParameterList("codEdicola", codEdicolaDl);
					namedQuery.setTimestamp("dtBolla", dataBolla);
					return new LinkedHashSet<BollaResaRiassuntoVo>(namedQuery.list());
				}
			};
			bolleRiassunto = getDao().findSetByHibernateCallback(action);
		} else if (dataBolla != null && codEdicolaDl == null) {
			HibernateCallback<Set<BollaResaRiassuntoVo>> action = new HibernateCallback<Set<BollaResaRiassuntoVo>>() {
				@Override
				public Set<BollaResaRiassuntoVo> doInHibernate(Session session) throws HibernateException, SQLException {
					Query namedQuery = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_GET_BOLLE_RESA_RIASSUNTO_EDICOLE_BY_DATE);
					namedQuery.setParameterList("codFiegDl", codFiegDl);					
					namedQuery.setTimestamp("dtBolla", dataBolla);
					return new LinkedHashSet<BollaResaRiassuntoVo>(namedQuery.list());
				}
			};
			bolleRiassunto = getDao().findSetByHibernateCallback(action);
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DAY_OF_YEAR, -numMaxBolleManutezioneDl);
			final Timestamp dtBolla = DateUtilities.floorDay(cal.getTime());
			HibernateCallback<Set<BollaResaRiassuntoVo>> action = new HibernateCallback<Set<BollaResaRiassuntoVo>>() {
				@Override
				public Set<BollaResaRiassuntoVo> doInHibernate(Session session) throws HibernateException, SQLException {
					Query namedQuery = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_GET_BOLLE_RESA_RIASSUNTO);
					namedQuery.setParameterList("codFiegDl", codFiegDl);
					namedQuery.setParameterList("codEdicola", codEdicolaDl);
					namedQuery.setTimestamp("dtBolla", dtBolla);
					return new LinkedHashSet<BollaResaRiassuntoVo>(namedQuery.list());
				}
			};
			bolleRiassunto = getDao().findSetByHibernateCallback(action);
		}
		return bolleRiassunto;
	}
	
	@Override
	public List<BollaResaRiassuntoVo> getBolleResaRiassunto(Integer codFiegDl, Integer codEdicolaDl, Timestamp dataBolla, String tipoBolla, Integer statoBolla, Boolean excludeSospese) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaResaRiassuntoVo.class, "brr");
		criteria.createCriteria("brr.abbinamentoEdicolaDlVo", "aedl");
		criteria.createCriteria("aedl.anagraficaEdicolaVo", "ae");
		criteria.add(Restrictions.eq("brr.pk.codFiegDl", codFiegDl));
		
		
//		ProjectionList properties = Projections.projectionList(); 
//		properties.add(Projections.property("brr.ferieEdicolaVo"),"ferieEdicolaVo");
//		criteria.setProjection(properties);
		
		
		if (codEdicolaDl != null) {
			criteria.add(Restrictions.eq("brr.pk.codEdicola", codEdicolaDl));
			criteria.addOrder(Order.desc("brr.pk.dtBolla"));
		}
		else {
			criteria.addOrder(Order.desc("aedl.codEdicolaDl"));
		}
		if (dataBolla != null) {
			criteria.add(Restrictions.eq("brr.pk.dtBolla", dataBolla));
		}
		else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DAY_OF_YEAR, -numMaxBolleManutezioneDl);
			criteria.add(Restrictions.ge("brr.pk.dtBolla", DateUtilities.floorDay(cal.getTime())));
		}
		if (tipoBolla != null) {
			criteria.add(Restrictions.eq("brr.pk.tipoBolla", tipoBolla));
		}
		if (statoBolla != null) {
			criteria.add(Restrictions.eq("brr.bollaTrasmessaDl", statoBolla));
		}
		if (excludeSospese) {
			criteria.add(Restrictions.or(Restrictions.isNull("aedl.dtSospensioneEdicola"), Restrictions.ltProperty("brr.pk.dtBolla", "aedl.dtSospensioneEdicola")));
		}
		
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<ResaRiscontrataDto> getBolleResaRiassuntoConResaRiscontrata(Integer codFiegDl, Integer codEdicolaDl) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, -numMaxBolleManutezioneDl);
		Timestamp dtBolla = DateUtilities.floorDay(cal.getTime());
		DetachedCriteria criteria = DetachedCriteria.forClass(ResaRiscontrataVo.class, "rr");
		criteria.add(Restrictions.eq("rr.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("rr.pk.codEdicola", codEdicolaDl));
		criteria.add(Restrictions.ge("rr.pk.dtBolla", dtBolla));
		criteria.addOrder(Order.desc("rr.pk.dtBolla"));
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.distinct(Projections.projectionList()
				  .add(Projections.property("rr.pk.dtBolla"), "dtBolla")
				  .add(Projections.property("rr.pk.tipoBolla"), "tipoBolla")));
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(ResaRiscontrataDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	public List<LavorazioneResaVo> getBolleResaRiassuntoLavorazioneResa(Integer codFiegDl, Integer codEdicolaDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LavorazioneResaVo.class, "lr");
		criteria.createCriteria("lr.anagraficaEdicolaVo", "ae");
		criteria.createCriteria("lr.anagraficaAgenziaVo", "aa");
		criteria.add(Restrictions.eq("aa.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("ae.codEdicola", codEdicolaDl));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BollaDettaglioDto> getDettaglioBolla(final Integer[] codFiegDl,
			final Integer[] codEdicolaDl, final Timestamp dataBolla, final String tipoBolla, final boolean showSoloRigheSpuntare, final boolean isMultiDl) {
		HibernateCallback<List<BollaDettaglioDto>> action = new HibernateCallback<List<BollaDettaglioDto>>() {
			@Override
			public List<BollaDettaglioDto> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(BollaDettaglioVo.class, "bd");
				criteria.createCriteria("bolla", "bo");
				criteria.createCriteria("variazione", "va", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("bo.storicoCopertineVo", "sc", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("sc.motivoResaRespinta", "mrr", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("ap.periodicitaVo", "per", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("sc.immagine", "im", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("sc.messaggioIdtnVo", "mi", JoinType.LEFT_OUTER_JOIN);

				//criteria.add(Restrictions.in("bd.pk.codFiegDl", codFiegDl));
				//criteria.add(Restrictions.in("bd.pk.codEdicola", codEdicolaDl));
				
				criteria.add(Restrictions.eq("bd.pk.codFiegDl", codFiegDl[0]));
				criteria.add(Restrictions.eq("bd.pk.codEdicola", codEdicolaDl[0]));
				
				criteria.add(Restrictions.eq("bd.pk.dtBolla", dataBolla));
				if (tipoBolla != null) {
					criteria.add(Restrictions.eq("bd.pk.tipoBolla", tipoBolla));
				}
				if (showSoloRigheSpuntare) {
					criteria.add(Restrictions.eq("bd.spunta", IGerivConstants.SPUNTA_NO));
				}
				if (isMultiDl) {
					criteria.addOrder(Order.asc("ap.titolo"));
				} else {
					criteria.addOrder(Order.asc("bo.riga"));
				}
				
				ProjectionList properties = Projections.projectionList(); 
				properties.add(Projections.property("bd.pk"), "pk");
				properties.add(Projections.property("bd.prezzoLordo"), "prezzoLordo");
				properties.add(Projections.property("bd.sconto"), "sconto");
				properties.add(Projections.property("bd.prezzoNetto"), "prezzoNetto");
				properties.add(Projections.property("bd.quantitaConsegnata"), "quantitaConsegnata");
				properties.add(Projections.property("bd.spunta"), "spunta");
				properties.add(Projections.property("bd.differenze"), "differenze");
				properties.add(Projections.property("bd.ordini"), "ordini");
				properties.add(Projections.property("bd.note"), "note");
				properties.add(Projections.property("bd.noteByCpu"), "noteByCpu");
				properties.add(Projections.property("bd.idtn"), "idtn");
				properties.add(Projections.property("bd.indicatoreValorizzare"), "indicatoreValorizzare");
				properties.add(Projections.property("bd.indicatorePrezzoVariato"), "indicatorePrezzoVariato");
				properties.add(Projections.property("bd.quantitaSpuntata"), "quantitaSpuntata");
				properties.add(Projections.property("bd.differenze"), "confermaDifferenze");
				properties.add(Projections.property("bd.contoDeposito"), "contoDeposito");
				properties.add(Projections.property("bd.aggiuntaFuoriCompetenza"), "aggiuntaFuoriCompetenza");
				properties.add(Projections.property("bo.percentualeIva"), "percentualeIva");
				properties.add(Projections.property("bo.titolo"), "titolo"); 
				properties.add(Projections.property("bo.sottoTitolo"), "sottoTitolo"); 
				properties.add(Projections.property("bo.numeroPubblicazione"), "numeroPubblicazione");
				properties.add(Projections.property("im.nome"), "immagine");
				properties.add(Projections.property("per.pk"), "periodicitaPk");
				properties.add(Projections.property("mrr.descrizione"), "motivoResaRespinta");
				properties.add(Projections.property("sc.codiceBarre"), "barcode");
				properties.add(Projections.property("sc.codicePubblicazione"), "codicePubblicazione");
				properties.add(Projections.property("sc.dataFatturazionePrevista"), "dataFatturazionePrevista");
				properties.add(Projections.property("sc.contoDepositoInforete"), "contoDepositoInforete");
				properties.add(Projections.property("sc.scontoInforete"), "scontoInforete");
				properties.add(Projections.property("sc.dataUscita"), "dataUscita");
				properties.add(Projections.property("ap.codInizioQuotidiano"), "codInizioQuotidiano");
				properties.add(Projections.property("mi.idMessaggioIdtn"), "idMessaggioIdtn");
				properties.add(Projections.property("va.quantita"), "variazione");
				properties.add(Projections.property("bo.pubblicazioneNonUscita"), "pubblicazioneNonUscita");
				criteria.setProjection(properties); 
				criteria.setResultTransformer(Transformers.aliasToBean(BollaDettaglioDto.class));
				List<BollaDettaglioDto> list = criteria.list();
				
				// SECONDO DL
				if (codFiegDl.length > 1) {
					Criteria criteria2 = session.createCriteria(BollaDettaglioVo.class, "bd");
					criteria2.createCriteria("bolla", "bo");
					criteria2.createCriteria("variazione", "va", JoinType.LEFT_OUTER_JOIN);
					criteria2.createCriteria("bo.storicoCopertineVo", "sc", JoinType.LEFT_OUTER_JOIN);
					criteria2.createCriteria("sc.motivoResaRespinta", "mrr", JoinType.LEFT_OUTER_JOIN);
					criteria2.createCriteria("sc.anagraficaPubblicazioniVo", "ap", JoinType.LEFT_OUTER_JOIN);
					criteria2.createCriteria("ap.periodicitaVo", "per", JoinType.LEFT_OUTER_JOIN);
					criteria2.createCriteria("sc.immagine", "im", JoinType.LEFT_OUTER_JOIN);
					criteria2.createCriteria("sc.messaggioIdtnVo", "mi", JoinType.LEFT_OUTER_JOIN);
					criteria2.add(Restrictions.eq("bd.pk.codFiegDl", codFiegDl[1]));
					criteria2.add(Restrictions.eq("bd.pk.codEdicola", codEdicolaDl[1]));
					criteria2.add(Restrictions.eq("bd.pk.dtBolla", dataBolla));
					if (tipoBolla != null) {
						criteria2.add(Restrictions.eq("bd.pk.tipoBolla", tipoBolla));
					}
					if (showSoloRigheSpuntare) {
						criteria2.add(Restrictions.eq("bd.spunta", IGerivConstants.SPUNTA_NO));
					}
					/*if (isMultiDl) {
						criteria2.addOrder(Order.asc("ap.titolo"));
					} else {
						criteria2.addOrder(Order.asc("bo.riga"));
					}*/
					
					ProjectionList properties2 = Projections.projectionList(); 
					properties2.add(Projections.property("bd.pk"), "pk");
					properties2.add(Projections.property("bd.prezzoLordo"), "prezzoLordo");
					properties2.add(Projections.property("bd.sconto"), "sconto");
					properties2.add(Projections.property("bd.prezzoNetto"), "prezzoNetto");
					properties2.add(Projections.property("bd.quantitaConsegnata"), "quantitaConsegnata");
					properties2.add(Projections.property("bd.spunta"), "spunta");
					properties2.add(Projections.property("bd.differenze"), "differenze");
					properties2.add(Projections.property("bd.ordini"), "ordini");
					properties2.add(Projections.property("bd.note"), "note");
					properties2.add(Projections.property("bd.noteByCpu"), "noteByCpu");
					properties2.add(Projections.property("bd.idtn"), "idtn");
					properties2.add(Projections.property("bd.indicatoreValorizzare"), "indicatoreValorizzare");
					properties2.add(Projections.property("bd.indicatorePrezzoVariato"), "indicatorePrezzoVariato");
					properties2.add(Projections.property("bd.quantitaSpuntata"), "quantitaSpuntata");
					properties2.add(Projections.property("bd.differenze"), "confermaDifferenze");
					properties2.add(Projections.property("bd.contoDeposito"), "contoDeposito");
					properties2.add(Projections.property("bd.aggiuntaFuoriCompetenza"), "aggiuntaFuoriCompetenza");
					properties2.add(Projections.property("bo.percentualeIva"), "percentualeIva");
					properties2.add(Projections.property("bo.titolo"), "titolo"); 
					properties2.add(Projections.property("bo.sottoTitolo"), "sottoTitolo"); 
					properties2.add(Projections.property("bo.numeroPubblicazione"), "numeroPubblicazione");
					properties2.add(Projections.property("im.nome"), "immagine");
					properties2.add(Projections.property("per.pk"), "periodicitaPk");
					properties2.add(Projections.property("mrr.descrizione"), "motivoResaRespinta");
					properties2.add(Projections.property("sc.codiceBarre"), "barcode");
					properties2.add(Projections.property("sc.codicePubblicazione"), "codicePubblicazione");
					properties2.add(Projections.property("sc.dataFatturazionePrevista"), "dataFatturazionePrevista");
					properties2.add(Projections.property("sc.contoDepositoInforete"), "contoDepositoInforete");
					properties2.add(Projections.property("sc.scontoInforete"), "scontoInforete");
					properties2.add(Projections.property("sc.dataUscita"), "dataUscita");
					properties2.add(Projections.property("ap.codInizioQuotidiano"), "codInizioQuotidiano");
					properties2.add(Projections.property("mi.idMessaggioIdtn"), "idMessaggioIdtn");
					properties2.add(Projections.property("va.quantita"), "variazione");
					properties2.add(Projections.property("bo.pubblicazioneNonUscita"), "pubblicazioneNonUscita");
					criteria2.setProjection(properties2); 
					criteria2.setResultTransformer(Transformers.aliasToBean(BollaDettaglioDto.class));
					list.addAll(criteria2.list());
					list = sort(list, on(BollaDettaglioDto.class).getTitolo());
				}
				return list;
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public List<FondoBollaDettaglioDto> getDettagliFondoBolla(final Integer[] codFiegDl,
			final Integer[] codEdicolaDl, final Timestamp dataBolla, final String tipoBolla, final boolean showSoloRigheSpuntare, final boolean showSoloRifornimenti, final boolean isMultiDl) {
		return _getDettagliFondoBolla(codFiegDl, codEdicolaDl, dataBolla, tipoBolla, showSoloRigheSpuntare, showSoloRifornimenti, isMultiDl, false);
	}
	
	@Override
	public List<FondoBollaDettaglioDto> getDettagliFondoBollaPubblicazioni(final Integer[] codFiegDl,
			final Integer[] codEdicolaDl, final Timestamp dataBolla, final String tipoBolla, final boolean showSoloRigheSpuntare, final boolean showSoloRifornimenti, final boolean isMultiDl) {
		return _getDettagliFondoBolla(codFiegDl, codEdicolaDl, dataBolla, tipoBolla, showSoloRigheSpuntare, showSoloRifornimenti, isMultiDl, true);
	}

	private List<FondoBollaDettaglioDto> _getDettagliFondoBolla(final Integer[] codFiegDl, final Integer[] codEdicolaDl, final Timestamp dataBolla, final String tipoBolla, final boolean showSoloRigheSpuntare, final boolean showSoloRifornimenti,
			final boolean isMultiDl, final boolean innerJoinCopertine) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FondoBollaDettaglioVo.class, "bd");
		criteria.createCriteria("bd.tipoFondoBollaVo", "fb");
		if (innerJoinCopertine) {
			criteria.createCriteria("bd.storicoCopertineVo", "sc");
		} else {
			criteria.createCriteria("bd.storicoCopertineVo", "sc", JoinType.LEFT_OUTER_JOIN);
		}
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ap.periodicitaVo", "per", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("sc.immagine", "im", JoinType.LEFT_OUTER_JOIN);
		//criteria.add(Restrictions.in("bd.pk.codFiegDl", codFiegDl));
		//criteria.add(Restrictions.in("bd.pk.codEdicola", codEdicolaDl));
		criteria.add(Restrictions.eq("bd.pk.codFiegDl", codFiegDl[0]));
		criteria.add(Restrictions.eq("bd.pk.codEdicola", codEdicolaDl[0]));
		criteria.add(Restrictions.eq("bd.pk.dtBolla", dataBolla));
		if (tipoBolla != null) {
			criteria.add(Restrictions.eq("bd.pk.tipoBolla", tipoBolla));
		}
		if (showSoloRigheSpuntare) {
			criteria.add(Restrictions.eq("bd.spunta", IGerivConstants.SPUNTA_NO));
		}
		if (showSoloRifornimenti) {
			criteria.add(Restrictions.eq("fb.tipoRecordFondoBolla", IGerivConstants.COD_TIPO_FONDO_BOLLA_RIFORNIMENTI));
		}
		if (isMultiDl) {
			criteria.addOrder(Order.asc("ap.titolo"));
		} else {
			criteria.addOrder(Order.asc("bd.pk.posizioneRiga"));
		}
		
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("bd.pk"), "pk");
		properties.add(Projections.property("bd.titolo"), "titolo"); 
		properties.add(Projections.property("bd.sottoTitolo"), "sottoTitolo"); 
		properties.add(Projections.property("sc.numeroCopertina"), "numeroPubblicazione"); 
		properties.add(Projections.property("sc.dataUscita"), "dataUscita");
		properties.add(Projections.property("ap.codInizioQuotidiano"), "codInizioQuotidiano");
		properties.add(Projections.property("bd.prezzoLordo"), "prezzoLordo");
		properties.add(Projections.property("sc.percIva"), "percentualeIva");
		properties.add(Projections.property("bd.prezzoNetto"), "prezzoNetto");
		properties.add(Projections.property("bd.quantitaConsegnata"), "quantitaConsegnata");
		properties.add(Projections.property("bd.spunta"), "spunta");
		properties.add(Projections.property("bd.differenze"), "differenze");
		properties.add(Projections.property("fb.tipoRecordFondoBolla"), "tipoRecordFondoBolla");
		properties.add(Projections.property("fb.descrizione"), "tipoFondoBolla");
		properties.add(Projections.property("bd.note"), "note");
		properties.add(Projections.property("im.nome"), "immagine");
		properties.add(Projections.property("bd.idtn"), "idtn");
		properties.add(Projections.property("per.pk"), "periodicitaPk");
		properties.add(Projections.property("sc.codiceBarre"), "barcode");
		properties.add(Projections.property("sc.codicePubblicazione"), "codicePubblicazione");
		properties.add(Projections.property("bd.quantitaSpuntata"), "quantitaSpuntata");
		properties.add(Projections.property("bd.differenze"), "confermaDifferenze");
		//properties.add(Projections.property("bd.fornitoBollePrecedenti"), "fornitoBollePrecedenti");
		//properties.add(Projections.property("bd.mancanzeBollePrecedenti"), "mancanzeBollePrecedenti");
		//properties.add(Projections.property("bd.resoDichiaratoBollePrecedenti"), "resoDichiaratoBollePrecedenti");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(FondoBollaDettaglioDto.class));
		List<FondoBollaDettaglioDto> result = getDao().findObjectByDetachedCriteria(criteria);

		if (codFiegDl.length > 1) {
			DetachedCriteria criteria2 = DetachedCriteria.forClass(FondoBollaDettaglioVo.class, "bd");
			criteria2.createCriteria("bd.tipoFondoBollaVo", "fb");
			if (innerJoinCopertine) {
				criteria2.createCriteria("bd.storicoCopertineVo", "sc");
			} else {
				criteria2.createCriteria("bd.storicoCopertineVo", "sc", JoinType.LEFT_OUTER_JOIN);
			}
			criteria2.createCriteria("sc.anagraficaPubblicazioniVo", "ap", JoinType.LEFT_OUTER_JOIN);
			criteria2.createCriteria("ap.periodicitaVo", "per", JoinType.LEFT_OUTER_JOIN);
			criteria2.createCriteria("sc.immagine", "im", JoinType.LEFT_OUTER_JOIN);
			criteria2.add(Restrictions.eq("bd.pk.codFiegDl", codFiegDl[1]));
			criteria2.add(Restrictions.eq("bd.pk.codEdicola", codEdicolaDl[1]));
			criteria2.add(Restrictions.eq("bd.pk.dtBolla", dataBolla));
			if (tipoBolla != null) {
				criteria2.add(Restrictions.eq("bd.pk.tipoBolla", tipoBolla));
			}
			if (showSoloRigheSpuntare) {
				criteria2.add(Restrictions.eq("bd.spunta", IGerivConstants.SPUNTA_NO));
			}
			if (showSoloRifornimenti) {
				criteria2.add(Restrictions.eq("fb.tipoRecordFondoBolla", IGerivConstants.COD_TIPO_FONDO_BOLLA_RIFORNIMENTI));
			}
			
			ProjectionList properties2 = Projections.projectionList(); 
			properties2.add(Projections.property("bd.pk"), "pk");
			properties2.add(Projections.property("bd.titolo"), "titolo"); 
			properties2.add(Projections.property("bd.sottoTitolo"), "sottoTitolo"); 
			properties2.add(Projections.property("sc.numeroCopertina"), "numeroPubblicazione"); 
			properties2.add(Projections.property("sc.dataUscita"), "dataUscita");
			properties2.add(Projections.property("ap.codInizioQuotidiano"), "codInizioQuotidiano");
			properties2.add(Projections.property("bd.prezzoLordo"), "prezzoLordo");
			properties2.add(Projections.property("sc.percIva"), "percentualeIva");
			properties2.add(Projections.property("bd.prezzoNetto"), "prezzoNetto");
			properties2.add(Projections.property("bd.quantitaConsegnata"), "quantitaConsegnata");
			properties2.add(Projections.property("bd.spunta"), "spunta");
			properties2.add(Projections.property("bd.differenze"), "differenze");
			properties2.add(Projections.property("fb.tipoRecordFondoBolla"), "tipoRecordFondoBolla");
			properties2.add(Projections.property("fb.descrizione"), "tipoFondoBolla");
			properties2.add(Projections.property("bd.note"), "note");
			properties2.add(Projections.property("im.nome"), "immagine");
			properties2.add(Projections.property("bd.idtn"), "idtn");
			properties2.add(Projections.property("per.pk"), "periodicitaPk");
			properties2.add(Projections.property("sc.codiceBarre"), "barcode");
			properties2.add(Projections.property("sc.codicePubblicazione"), "codicePubblicazione");
			properties2.add(Projections.property("bd.quantitaSpuntata"), "quantitaSpuntata");
			properties2.add(Projections.property("bd.differenze"), "confermaDifferenze");
			//properties2.add(Projections.property("bd.fornitoBollePrecedenti"), "fornitoBollePrecedenti");
			//properties2.add(Projections.property("bd.mancanzeBollePrecedenti"), "mancanzeBollePrecedenti");
			//properties2.add(Projections.property("bd.resoDichiaratoBollePrecedenti"), "resoDichiaratoBollePrecedenti");
			criteria2.setProjection(properties2); 
			criteria2.setResultTransformer(Transformers.aliasToBean(FondoBollaDettaglioDto.class));
			List<FondoBollaDettaglioDto> result2 = getDao().findObjectByDetachedCriteria(criteria2);
			result.addAll(result2);
			result = sort(result, on(FondoBollaDettaglioDto.class).getTitolo());
		}
		
		return result;
	}
	
	
	private Boolean _getPubblicazionePresenteNelleSuccessiveBolleResa(final Integer codFiegDl, final Integer codEdicolaWeb, final Timestamp dataBolla, final Integer idtn) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FondoBollaDettaglioVo.class, "bd");
		
		criteria.createCriteria("bd.tipoFondoBollaVo", "fb");
		criteria.add(Restrictions.eq("bd.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("bd.pk.codEdicola", codEdicolaWeb));
		criteria.add(Restrictions.eq("bd.idtn", idtn));
		criteria.add(Restrictions.eq("fb.tipoRecordFondoBolla",new Integer(9)));
		criteria.add(Restrictions.gt("bd.pk.dtBolla", dataBolla));
		//criteria.add(Restrictions.sqlRestriction("TRUNC(bd.pk.dtBolla) > TO_DATE('" + dataBolla + "','dd/mm/yyyy')"));
    	ProjectionList props = Projections.projectionList(); 
		props.add(Projections.count("bd.idtn"));
		criteria.setProjection(props);
		Number count = (Number) getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		return (count != null && count.intValue() > 0) ? true : false;
		
	}
	
	
	
	@Override
	public List<BollaVoDto> getBollaVoSonoInoltreUscite(final Integer codFiegDl,
			final Integer codEdicolaDl, final Timestamp dataBolla, final String tipoBolla) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaVo.class, "bd");
		criteria.createCriteria("bd.storicoCopertineVo", "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
		criteria.createCriteria("sc.immagine", "im", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ap.argomentoVo", "arg", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ap.periodicitaVo", "per", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("bd.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("bd.pk.dtBolla", dataBolla));
		criteria.add(Restrictions.eq("bd.pk.tipoBolla", tipoBolla));
		DetachedCriteria subCriteria = DetachedCriteria.forClass(BollaDettaglioVo.class, "bdvo");
		subCriteria.add(Property.forName("bdvo.pk.codFiegDl").eqProperty("bd.pk.codFiegDl"));
		subCriteria.add(Restrictions.eq("bdvo.pk.codEdicola", codEdicolaDl));
		subCriteria.add(Property.forName("bdvo.pk.dtBolla").eqProperty("bd.pk.dtBolla"));
		subCriteria.add(Property.forName("bdvo.pk.tipoBolla").eqProperty("bd.pk.tipoBolla"));
		subCriteria.add(Property.forName("bdvo.pk.posizioneRiga").eqProperty("bd.pk.posizioneRiga"));
		criteria.add(Subqueries.notExists(subCriteria.setProjection(Projections.property("bdvo.idtn"))));
		criteria.addOrder(Order.asc("ap.titolo"));
		
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("bd.pk"), "pk");
		properties.add(Projections.property("bd.idTestataNumero"), "idtn");
		properties.add(Projections.property("bd.titolo"), "titolo"); 
		properties.add(Projections.property("bd.sottoTitolo"), "sottoTitolo"); 
		properties.add(Projections.property("bd.numeroPubblicazione"), "numeroCopertina"); 
		properties.add(Projections.property("bd.prezzoLordo"), "prezzoCopertina");
		properties.add(Projections.property("arg.descrizione"), "argomento");
		properties.add(Projections.property("per.descrizione"), "periodicita");
		properties.add(Projections.property("per.pk"), "periodicitaPk");
		properties.add(Projections.property("im.nome"), "immagine");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(BollaVoDto.class));
		
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BollaResaDettaglioDto> getDettaglioBollaResa(
			final Integer[] codFiegDl, final Integer[] codEdicola, final Timestamp dtBolla,
			final String tipo, final Boolean soloResoDaInserire,final Boolean soloResoConGiacenza) {
		HibernateCallback<List<BollaResaDettaglioDto>> action = new HibernateCallback<List<BollaResaDettaglioDto>>() {
			@Override
			public List<BollaResaDettaglioDto> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(BollaResaDettaglioVo.class, "bd");
				criteria.createCriteria("bd.bollaResa", "br");
				criteria.createCriteria("bd.contoDeposito", "cd", JoinType.LEFT_OUTER_JOIN); 
				criteria.createCriteria("br.numeroRichiamo", "ri", JoinType.LEFT_OUTER_JOIN); 
				criteria.createCriteria("bd.storicoCopertineVo", "sc", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("ap.periodicitaVo", "per", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("sc.immagine", "im", JoinType.LEFT_OUTER_JOIN);

				//criteria.add(Restrictions.in("bd.pk.codFiegDl", codFiegDl));
				//criteria.add(Restrictions.in("bd.pk.codEdicola", codEdicola));
				
				criteria.add(Restrictions.eq("bd.pk.codFiegDl", codFiegDl[0]));
				criteria.add(Restrictions.eq("bd.pk.codEdicola", codEdicola[0]));
				
				criteria.add(Restrictions.eq("bd.pk.dtBolla", dtBolla));
				criteria.add(Restrictions.eq("bd.pk.tipoBolla", tipo));
				if (soloResoDaInserire != null && soloResoDaInserire) {
					criteria.add(Restrictions.isNull("bd.reso"));
				}
				// 0000046
				if(soloResoConGiacenza!= null && soloResoConGiacenza){
					criteria.add(Restrictions.gt("bd.giacenza", new Long(0)));
				}
				
				// CDL Ordinamento bolla di resa per cesta 
				if (codFiegDl[0].equals(IGerivConstants.CDL_CODE)) {
					criteria.addOrder(Order.asc("br.pk.codFiegDl")).addOrder(Order.asc("br.cesta"))
					.addOrder(Order.asc("br.titolo")).addOrder(Order.asc("br.riga"));
				} else {
					criteria.addOrder(Order.asc("br.pk.codFiegDl")).addOrder(Order.asc("br.riga"));
				}
				
				ProjectionList properties = Projections.projectionList(); 
				properties.add(Projections.property("bd.pk"), "pk");
				properties.add(Projections.property("bd.distribuito"), "distribuito");
				properties.add(Projections.property("bd.reso"), "reso");
				properties.add(Projections.property("bd.giacenza"), "giacenza");
				properties.add(Projections.property("bd.prezzoNetto"), "prezzoNetto");
				properties.add(Projections.property("bd.note"), "note");
				properties.add(Projections.property("bd.noteByCpu"), "noteByCpu");
				properties.add(Projections.property("bd.idtn"), "idtn");
				properties.add(Projections.property("bd.ordini"), "ordini");
				properties.add(Projections.property("br.cpuDl"), "cpuDl");
				properties.add(Projections.property("br.titolo"), "titolo"); 
				properties.add(Projections.property("br.sottoTitolo"), "sottoTitolo"); 
				properties.add(Projections.property("br.numeroPubblicazione"), "numeroPubblicazione");
				properties.add(Projections.property("br.prezzoLordo"), "prezzoLordo");
				properties.add(Projections.property("sc.dataUscita"), "dataUscita");
				properties.add(Projections.property("sc.codiceBarre"), "barcode");
				properties.add(Projections.property("im.nome"), "immagine");
				properties.add(Projections.property("ri.descrizione"), "tipoRichiamoExt");
				properties.add(Projections.property("ri.descrizioneBreve"), "tipoRichiamo");
				properties.add(Projections.property("per.pk"), "periodicitaPk");
				properties.add(Projections.property("cd.pk.idtn"), "idtnContoDeposito");
				properties.add(Projections.property("cd.quantita"), "qtaContoDeposito");
				properties.add(Projections.property("br.cesta"), "cesta"); 
				criteria.setProjection(properties); 
				criteria.setResultTransformer(Transformers.aliasToBean(BollaResaDettaglioDto.class));
				List<BollaResaDettaglioDto> list = criteria.list();

				// DL2
				if (codFiegDl.length > 1) {
					Criteria criteria2 = session.createCriteria(BollaResaDettaglioVo.class, "bd");
					criteria2.createCriteria("bd.bollaResa", "br");
					criteria2.createCriteria("bd.contoDeposito", "cd", JoinType.LEFT_OUTER_JOIN); 
					criteria2.createCriteria("br.numeroRichiamo", "ri", JoinType.LEFT_OUTER_JOIN); 
					criteria2.createCriteria("bd.storicoCopertineVo", "sc", JoinType.LEFT_OUTER_JOIN);
					criteria2.createCriteria("sc.anagraficaPubblicazioniVo", "ap", JoinType.LEFT_OUTER_JOIN);
					criteria2.createCriteria("ap.periodicitaVo", "per", JoinType.LEFT_OUTER_JOIN);
					criteria2.createCriteria("sc.immagine", "im", JoinType.LEFT_OUTER_JOIN);

					//criteria.add(Restrictions.in("bd.pk.codFiegDl", codFiegDl));
					//criteria.add(Restrictions.in("bd.pk.codEdicola", codEdicola));
					
					criteria2.add(Restrictions.eq("bd.pk.codFiegDl", codFiegDl[1]));
					criteria2.add(Restrictions.eq("bd.pk.codEdicola", codEdicola[1]));
					
					criteria2.add(Restrictions.eq("bd.pk.dtBolla", dtBolla));
					criteria2.add(Restrictions.eq("bd.pk.tipoBolla", tipo));
					if (soloResoDaInserire != null && soloResoDaInserire) {
						criteria2.add(Restrictions.isNull("bd.reso"));
					}
					// 0000046
					if(soloResoConGiacenza!= null && soloResoConGiacenza){
						criteria2.add(Restrictions.gt("bd.giacenza", new Long(0)));
					}
					
					criteria2.addOrder(Order.asc("br.pk.codFiegDl")).addOrder(Order.asc("br.riga"));
					
					ProjectionList properties2 = Projections.projectionList(); 
					properties2.add(Projections.property("bd.pk"), "pk");
					properties2.add(Projections.property("bd.distribuito"), "distribuito");
					properties2.add(Projections.property("bd.reso"), "reso");
					properties2.add(Projections.property("bd.giacenza"), "giacenza");
					properties2.add(Projections.property("bd.prezzoNetto"), "prezzoNetto");
					properties2.add(Projections.property("bd.note"), "note");
					properties2.add(Projections.property("bd.noteByCpu"), "noteByCpu");
					properties2.add(Projections.property("bd.idtn"), "idtn");
					properties2.add(Projections.property("bd.ordini"), "ordini");
					properties2.add(Projections.property("br.cpuDl"), "cpuDl");
					properties2.add(Projections.property("br.titolo"), "titolo"); 
					properties2.add(Projections.property("br.sottoTitolo"), "sottoTitolo"); 
					properties2.add(Projections.property("br.numeroPubblicazione"), "numeroPubblicazione");
					properties2.add(Projections.property("br.prezzoLordo"), "prezzoLordo");
					properties2.add(Projections.property("sc.dataUscita"), "dataUscita");
					properties2.add(Projections.property("sc.codiceBarre"), "barcode");
					properties2.add(Projections.property("im.nome"), "immagine");
					properties2.add(Projections.property("ri.descrizione"), "tipoRichiamoExt");
					properties2.add(Projections.property("ri.descrizioneBreve"), "tipoRichiamo");
					properties2.add(Projections.property("per.pk"), "periodicitaPk");
					properties2.add(Projections.property("cd.pk.idtn"), "idtnContoDeposito");
					properties2.add(Projections.property("cd.quantita"), "qtaContoDeposito");
					properties2.add(Projections.property("br.cesta"), "cesta"); 
					criteria2.setProjection(properties2); 
					criteria2.setResultTransformer(Transformers.aliasToBean(BollaResaDettaglioDto.class));
					list.addAll(criteria2.list());
					list = sort(list, on(BollaResaDettaglioDto.class).getSortCriteria());
				}
				return list; 
			}
		};
		return getDao().findByHibernateCallback(action );
	}
	
	@Override
	public List<BollaResaDettaglioDto> getDettaglioBollaResaCDLCeste(
			final Integer[] codFiegDl, final Integer[] codEdicola, final Timestamp dtBolla,
			final String tipo, final Boolean soloResoDaInserire,final Boolean soloResoConGiacenza, final String cesta) {
		
		HibernateCallback<List<BollaResaDettaglioDto>> action = new HibernateCallback<List<BollaResaDettaglioDto>>() {
			@Override
			public List<BollaResaDettaglioDto> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(BollaResaDettaglioVo.class, "bd");
				criteria.createCriteria("bd.bollaResa", "br");
				criteria.createCriteria("bd.contoDeposito", "cd", JoinType.LEFT_OUTER_JOIN); 
				criteria.createCriteria("br.numeroRichiamo", "ri", JoinType.LEFT_OUTER_JOIN); 
				criteria.createCriteria("bd.storicoCopertineVo", "sc", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("ap.periodicitaVo", "per", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("sc.immagine", "im", JoinType.LEFT_OUTER_JOIN);
				criteria.add(Restrictions.in("bd.pk.codFiegDl", codFiegDl));
				criteria.add(Restrictions.in("bd.pk.codEdicola", codEdicola));
				criteria.add(Restrictions.eq("bd.pk.dtBolla", dtBolla));
				criteria.add(Restrictions.eq("bd.pk.tipoBolla", tipo));
				criteria.add(Restrictions.eq("bd.pk.tipoBolla", tipo));
				
				if (soloResoDaInserire != null && soloResoDaInserire) {
					criteria.add(Restrictions.isNull("bd.reso"));
				}
				// 0000046
				if(soloResoConGiacenza!= null && soloResoConGiacenza){
					criteria.add(Restrictions.gt("bd.giacenza", new Long(0)));
				}
				
				if(cesta!=null && !cesta.equals("")){
					criteria.add(Restrictions.eq("br.cesta", cesta));
				}
				
				criteria.addOrder(Order.asc("br.pk.codFiegDl")).addOrder(Order.asc("br.riga"));
				
				ProjectionList properties = Projections.projectionList(); 
				properties.add(Projections.property("bd.pk"), "pk");
				properties.add(Projections.property("bd.distribuito"), "distribuito");
				properties.add(Projections.property("bd.reso"), "reso");
				properties.add(Projections.property("bd.giacenza"), "giacenza");
				properties.add(Projections.property("bd.prezzoNetto"), "prezzoNetto");
				properties.add(Projections.property("bd.note"), "note");
				properties.add(Projections.property("bd.noteByCpu"), "noteByCpu");
				properties.add(Projections.property("bd.idtn"), "idtn");
				properties.add(Projections.property("bd.ordini"), "ordini");
				properties.add(Projections.property("br.cpuDl"), "cpuDl");
				properties.add(Projections.property("br.titolo"), "titolo"); 
				properties.add(Projections.property("br.sottoTitolo"), "sottoTitolo"); 
				properties.add(Projections.property("br.numeroPubblicazione"), "numeroPubblicazione");
				properties.add(Projections.property("br.prezzoLordo"), "prezzoLordo");
				properties.add(Projections.property("sc.dataUscita"), "dataUscita");
				properties.add(Projections.property("sc.codiceBarre"), "barcode");
				properties.add(Projections.property("im.nome"), "immagine");
				properties.add(Projections.property("ri.descrizione"), "tipoRichiamoExt");
				properties.add(Projections.property("ri.descrizioneBreve"), "tipoRichiamo");
				properties.add(Projections.property("per.pk"), "periodicitaPk");
				properties.add(Projections.property("cd.pk.idtn"), "idtnContoDeposito");
				properties.add(Projections.property("cd.quantita"), "qtaContoDeposito");
				properties.add(Projections.property("br.cesta"), "cesta"); 
				criteria.setProjection(properties); 
				criteria.setResultTransformer(Transformers.aliasToBean(BollaResaDettaglioDto.class));
				List<BollaResaDettaglioDto> list = criteria.list();
				return list; 
			}
		};
		return getDao().findByHibernateCallback(action );
	}
	
	
	
	
	
	
	@Override
	public List<Integer> getCpusResaDimeticataNotInBollaResa(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipo) {
		return getDao().findObjectByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_CPUS_RESA_DIMETICATA_NOT_IN_BOLLA_RESA, codFiegDl, codEdicola, dtBolla, tipo);
	}
	
	@Override
	public List<BollaResaFuoriVoceDettaglioDto> getDettaglioFuoriVoce(
			final Integer[] codFiegDl, final Integer[] codEdicola, final Timestamp dtBolla,
			final String tipo, final Boolean soloResoDaInserire) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaResaFuoriVoceVo.class, "bd");
		criteria.createCriteria("bd.storicoCopertineVo", "sc", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ap.periodicitaVo", "per", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("sc.immagine", "im", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.in("bd.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.in("bd.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("bd.pk.dtBolla", dtBolla));
		criteria.add(Restrictions.eq("bd.pk.tipoBolla", tipo));
		criteria.add(Restrictions.gt("bd.reso", 0));
		if (soloResoDaInserire != null && soloResoDaInserire) {
			criteria.add(Restrictions.isNull("bd.reso"));
		}
		criteria.addOrder(Order.asc("bd.pk.posizioneRiga"));
		
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("bd.pk"), "pk");
		properties.add(Projections.property("bd.cpuDl"), "cpuDl");
		properties.add(Projections.property("bd.titolo"), "titolo"); 
		properties.add(Projections.property("bd.sottoTitolo"), "sottoTitolo"); 
		properties.add(Projections.property("sc.dataUscita"), "dataUscita");
		properties.add(Projections.property("bd.numeroPubblicazione"), "numeroPubblicazione"); 
		properties.add(Projections.property("bd.distribuito"), "distribuito");
		properties.add(Projections.property("bd.reso"), "reso");
		properties.add(Projections.property("bd.giacenza"), "giacenza");
		properties.add(Projections.property("sc.dataUscita"), "dataUscita");
		properties.add(Projections.property("bd.prezzoLordo"), "prezzoLordo");
		properties.add(Projections.property("bd.prezzoNetto"), "prezzoNetto");
		properties.add(Projections.property("im.nome"), "immagine");
		properties.add(Projections.property("sc.codiceBarre"), "barcode");
		properties.add(Projections.property("per.pk"), "periodicitaPk");
		properties.add(Projections.property("bd.idtn"), "idtn");
		properties.add(Projections.property("bd.resaAnticipata"), "resaAnticipata");
		properties.add(Projections.property("bd.cesta"), "cesta");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(BollaResaFuoriVoceDettaglioDto.class));
		
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<BollaResaRichiamoPersonalizzatoDettaglioDto> getDettaglioRichiamoPersonalizzato(
			final Integer[] codFiegDl, final Integer[] codEdicola, final Timestamp dtBolla,
			final String tipo, final Boolean soloResoDaInserire) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaResaRichiamoPersonalizzatoVo.class, "bd");
		criteria.createCriteria("bd.storicoCopertineVo", "sc", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ap.periodicitaVo", "per", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("bd.numeroRichiamo", "ri", JoinType.LEFT_OUTER_JOIN); 
		criteria.createCriteria("sc.immagine", "im", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.in("bd.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.in("bd.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("bd.pk.dtBolla", dtBolla));
		criteria.add(Restrictions.eq("bd.pk.tipoBolla", tipo));
		if (soloResoDaInserire != null && soloResoDaInserire) {
			criteria.add(Restrictions.isNull("bd.reso"));
		}
		criteria.addOrder(Order.asc("bd.pk.posizioneRiga"));
		
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("bd.pk"), "pk");
		properties.add(Projections.property("bd.cpuDl"), "cpuDl");
		properties.add(Projections.property("bd.titolo"), "titolo"); 
		properties.add(Projections.property("bd.sottoTitolo"), "sottoTitolo"); 
		properties.add(Projections.property("bd.numeroPubblicazione"), "numeroPubblicazione"); 
		properties.add(Projections.property("bd.distribuito"), "distribuito");
		properties.add(Projections.property("bd.reso"), "reso");
		properties.add(Projections.property("bd.giacenza"), "giacenza");
		properties.add(Projections.property("sc.dataUscita"), "dataUscita");
		properties.add(Projections.property("bd.prezzoLordo"), "prezzoLordo");
		properties.add(Projections.property("bd.prezzoNetto"), "prezzoNetto");
		properties.add(Projections.property("im.nome"), "immagine");
		properties.add(Projections.property("ri.descrizione"), "tipoRichiamoExt");
		properties.add(Projections.property("ri.descrizioneBreve"), "tipoRichiamo");
		properties.add(Projections.property("sc.codiceBarre"), "barcode");
		properties.add(Projections.property("per.pk"), "periodicitaPk");
		properties.add(Projections.property("bd.idtn"), "idtn");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(BollaResaRichiamoPersonalizzatoDettaglioDto.class));
		
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<BollaResaFuoriVoceVo> buildNuoviDettagliFuoriVoce(Integer codFiegDl, Integer codEdicola,
			Timestamp dtBolla, String tipo, Integer cpu, Integer gruppoSconto, boolean showNumeriOmogenei, Timestamp dataStorico, String tipoResaNoContoDeposito, Boolean accettoCD) {
		List<BollaResaFuoriVoceVo> listNuovaResaFuoriVoce = new ArrayList<BollaResaFuoriVoceVo>();
		List<BollaResaFuoriVoceVo> dettagliFuoriVoce = getDao().findByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_RICHIAMO_FUORI_VOCE_BOLLA_RESA_BY_CPU, new Object[]{codFiegDl, codEdicola, dtBolla, tipo, cpu});
		AnagraficaPubblicazioniVo pubblicazione = getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_ANAGRAFICA_PUBBLICAZIONI_BY_ID, codFiegDl, cpu);
		Integer numMaxCopertineVecchieResa = pubblicazione != null && pubblicazione.getLimitiPeriodicitaVo() != null && pubblicazione.getLimitiPeriodicitaVo().getNumMaxCopertineVecchieResa() != null ? pubblicazione.getLimitiPeriodicitaVo().getNumMaxCopertineVecchieResa() : 0;
		if (pubblicazione != null && pubblicazione.getIndicatorePaccotto() != null && pubblicazione.getIndicatorePaccotto() == IGerivConstants.COD_ELEMENTO_PACCOTTO_DIVISIBILE) {
			numMaxCopertineVecchieResa = getNumCopertineVecchiePerPaccotto(codFiegDl, cpu, numMaxCopertineVecchieResa);
		}
		
		boolean permetteContoDeposito=false;
		
		if(!tipo.equals(tipoResaNoContoDeposito)){
			permetteContoDeposito = (pubblicazione != null && pubblicazione.getLimitiPeriodicitaVo() != null && pubblicazione.getLimitiPeriodicitaVo().getPermettePubblicazioniInContoDeposito() != null && pubblicazione.getLimitiPeriodicitaVo().getPermettePubblicazioniInContoDeposito().equals("N")) ? false : true;
			if (!permetteContoDeposito && accettoCD != null && accettoCD) {
				permetteContoDeposito = true;
			}
		}else{
			permetteContoDeposito = true;
		}
		//boolean permetteContoDeposito = (pubblicazione != null && pubblicazione.getLimitiPeriodicitaVo() != null && pubblicazione.getLimitiPeriodicitaVo().getPermettePubblicazioniInContoDeposito() != null && pubblicazione.getLimitiPeriodicitaVo().getPermettePubblicazioniInContoDeposito().equals("N")) ? false : true;

		
		List<StoricoCopertineDto> listStoricoCopertineVo = null;
		boolean isQuotidiano = (pubblicazione != null && pubblicazione.getPeriodicitaVo() != null && pubblicazione.getPeriodicitaVo().getPk().getPeriodicita().equals(IGerivConstants.COD_PERIODICITA_QUOTIDIANO)
				&& (pubblicazione.getCodInizioQuotidiano() != null && pubblicazione.getCodFineQuotidiano() != null))
				&& !showNumeriOmogenei;
		if (isQuotidiano) {
			if (DateUtilities.buildCalendarOnDate(dtBolla).get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
				++numMaxCopertineVecchieResa;
			}
			listStoricoCopertineVo = buildListStoricoCopertineQuotidianoDto(codFiegDl, codEdicola, dtBolla, tipo, pubblicazione.getCodInizioQuotidiano(), pubblicazione.getCodFineQuotidiano(), gruppoSconto, numMaxCopertineVecchieResa);
		} else {
			listStoricoCopertineVo = buildListStoricoCopertineDto(codFiegDl, codEdicola, dtBolla, tipo, cpu, gruppoSconto, numMaxCopertineVecchieResa);
		}
		List<BollaResaRichiamoPersonalizzatoVo> dettagliRichiamoPersonalizzato = getDao().findByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_RICHIAMO_PERSONALIZZATO_BOLLA_RESA_BY_CPU, new Object[]{codFiegDl, codEdicola, dtBolla, tipo, cpu});
		Integer lastIndex = findUltimaPosizioneRigaBolla(codFiegDl, codEdicola, dtBolla, tipo);
		for (StoricoCopertineDto vo : listStoricoCopertineVo) {
			final Integer idtn = vo.getPk().getIdtn();
			BollaResaFuoriVoceVo brfv = new BollaResaFuoriVoceVo();
			BollaResaFuoriVocePk pk = new BollaResaFuoriVocePk();
			pk.setCodFiegDl(codFiegDl);
			pk.setCodEdicola(codEdicola);
			pk.setDtBolla(dtBolla);
			pk.setTipoBolla(tipo);
			brfv.setPermetteContoDeposito(permetteContoDeposito);
			boolean numeroInContoDeposito = pubblicazioniService.getPubblicazioneInContoDeposito(codFiegDl, codEdicola, idtn) == null ? false : true;
			brfv.setNumeroInContoDeposito(numeroInContoDeposito);
			// SOLO PER MENTA
			// 11/11/2016 VIENE CONTROLLATA LA PRESENZA ALL'INTERNO DELLE BOLLE SUCCESSIVE, DELLA PUBBLICAZIONE INSERITA IN RESA COME FUORI BOLLA (TBL_9612)
			if(codFiegDl.intValue() == IGerivConstants.MENTA_CODE.intValue()){
				boolean isPubblicazionePresenteNelleSuccessiveBolleResa = this._getPubblicazionePresenteNelleSuccessiveBolleResa(codFiegDl, codEdicola, dtBolla, idtn);
				brfv.setPubblicazionePresenteNelleSuccessiveBolleResa(isPubblicazionePresenteNelleSuccessiveBolleResa);
			}
			brfv.setQuantitaCopieContoDeposito(vo.getQuantitaCopieContoDeposito());
			brfv.setPk(pk);
			brfv.setPrezzoNetto(numeroInContoDeposito ? new BigDecimal(0) : (vo.getPrezzoEdicola() != null) ? vo.getPrezzoEdicola() : new BigDecimal(0));
			brfv.setPrezzoLordo(vo.getPrezzoCopertina());
			//Vittorio 25/07/19
			//brfv.setCpuDl(cpu);
			brfv.setCpuDl(vo.getCodicePubblicazione());
			brfv.setIdtn(idtn);
			brfv.setNumeroPubblicazione(vo.getNumeroCopertina());
			brfv.setTitolo(isQuotidiano ? vo.getTitolo() : (pubblicazione != null ? pubblicazione.getTitolo() : ""));
			brfv.setSottoTitolo(vo.getSottoTitolo());
			brfv.setDataUscita(vo.getDataUscita());
			brfv.setNomeImmagine(vo.getNomeImmagine());
			brfv.setBarcodeStr(vo.getCodiceBarre());
			brfv.setMotivoResaRespinta(vo.getMotivoResaRespinta());
			brfv.setCodMotivoRespinto(vo.getCodMotivoRespinto());
			brfv.setDataRichiamoResa(vo.getDataRichiamoResa());
			brfv.setResaAnticipata(brfv.getDataRichiamoResa() == null || (vo.getTipoRichiamoResa() != null && (vo.getTipoRichiamoResa().equals(1) || vo.getTipoRichiamoResa().equals(2))));
			brfv.setPeriodicita(vo.getPeriodicita());
			brfv.setCesta(vo.getCesta());
			Long distribuitoBolla = new Long((vo.getFornitoBolla() != null) ? vo.getFornitoBolla() : 0);
			Long distribuitoFondoBolla = new Long((vo.getFornitoFondoBolla() != null ) ? vo.getFornitoFondoBolla() : 0);
			Long distribuitoEstrattoConto = new Long((vo.getFornitoEstrattoConto() != null ) ? vo.getFornitoEstrattoConto() : 0);
			Long distribuito = ((distribuitoBolla == null) ? 0 : distribuitoBolla) + ((distribuitoFondoBolla == null) ? 0 : distribuitoFondoBolla)  + ((distribuitoEstrattoConto == null) ? 0 : distribuitoEstrattoConto);
			brfv.setDistribuito(distribuito);  
			if ((pubblicazione != null 
					&& pubblicazione.getLimitiPeriodicitaVo().getPermetteResaNumeroNuovo() != null 
					&& pubblicazione.getLimitiPeriodicitaVo().getPermetteResaNumeroNuovo().toUpperCase().equals("S"))
					|| vo.getDataRichiamoResa() != null) {
				if (dettagliFuoriVoce.contains(brfv)) {
					BollaResaFuoriVoceVo dfv = dettagliFuoriVoce.get(dettagliFuoriVoce.indexOf(brfv));
					dfv.setPermetteContoDeposito(permetteContoDeposito);
					dfv.setQuantitaCopieContoDeposito(brfv.getQuantitaCopieContoDeposito());
					dfv.setNumeroInContoDeposito(brfv.getNumeroInContoDeposito());
					dfv.setDistribuito(brfv.getDistribuito());
					dfv.setBarcodeStr(brfv.getBarcodeStr());
					dfv.setNomeImmagine(brfv.getNomeImmagine());
					dfv.setMotivoResaRespinta(brfv.getMotivoResaRespinta());
					dfv.setCodMotivoRespinto(brfv.getCodMotivoRespinto());
					dfv.setDataRichiamoResa(brfv.getDataRichiamoResa());
					dfv.setResaAnticipata(brfv.getDataRichiamoResa() == null || (vo.getTipoRichiamoResa() != null && (vo.getTipoRichiamoResa().equals(1) || vo.getTipoRichiamoResa().equals(2))));
					listNuovaResaFuoriVoce.add(dfv);
				} else {
					BollaResaRichiamoPersonalizzatoVo ricPer = selectUnique(dettagliRichiamoPersonalizzato, having(on(BollaResaRichiamoPersonalizzatoVo.class).getIdtn(), equalTo(brfv.getIdtn())));
					if (ricPer != null) {
						brfv.getPk().setPosizioneRiga(ricPer.getPk().getPosizioneRiga());
						brfv.setReso(ricPer.getReso());
						brfv.setRichiamoPersonalizzato(true);
					} else {
						brfv.getPk().setPosizioneRiga(++lastIndex);
					}
					brfv.setGiacenza(pubblicazioniService.getGiacenza(codFiegDl, codEdicola, idtn, dataStorico));
					listNuovaResaFuoriVoce.add(brfv);
				}
			}
		}
		//MODIFICA PER GESTIRE LE DATE
//		if (listNuovaResaFuoriVoce.size() >= numMaxCopertineVecchieResa) {
//			listNuovaResaFuoriVoce = new ArrayList<BollaResaFuoriVoceVo>(listNuovaResaFuoriVoce.subList(0, numMaxCopertineVecchieResa));
//		}	
		return listNuovaResaFuoriVoce;
	}
	
	/**
	 * Ritorna il num. massimo di copertine per una pubblicazione paccotto
	 * 
	 * @param codFiegDl
	 * @param cpu
	 * @param numMaxCopertineVecchieResa
	 * @return
	 */
	private Integer getNumCopertineVecchiePerPaccotto(final Integer codFiegDl, final Integer cpu, final Integer numMaxCopertineVecchieResa) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery(IGerivQueryContants.SQL_GET_NUM_MAX_COPERTINE_VECCHIE_PER_PACCOTTO);
				sqlQuery.setInteger("coddl", codFiegDl);
				sqlQuery.setInteger("cpu", cpu);
				sqlQuery.setInteger("numMaxCopertineVecchieResa", numMaxCopertineVecchieResa);
				sqlQuery.addScalar("nrocop", IntegerType.INSTANCE);
				return (Integer) sqlQuery.uniqueResult();
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}

	@Override
	public Integer findUltimaPosizioneRigaBolla(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipo) {
		Integer lastIndexFb = getDao().findUniqueResultObjectByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_LAST_POSIZIONE_RIGA_BOLLA_FUORI_RESA, codFiegDl, codEdicola, dtBolla, tipo);
		Integer lastIndexRp = getDao().findUniqueResultObjectByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_LAST_POSIZIONE_RIGA_BOLLA_RICHIAMO_PERSONALIZZATO, codFiegDl, codEdicola, dtBolla, tipo);
		Integer lastIndex = getDao().findUniqueResultObjectByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_LAST_POSIZIONE_RIGA_BOLLA_RESA, codFiegDl, codEdicola, dtBolla, tipo);
		int maxFb = lastIndexFb == null ? 0 : lastIndexFb;
		int maxRp = lastIndexRp == null ? 0 : lastIndexRp;
		int max = lastIndex == null ? 0 : lastIndex;
		return NumberUtils.max(new int[]{maxFb, maxRp, max});
	}
	
	/**
	 * Ritorna lo storico delle uscite di un quotidiano limitato da numMaxCopertineVecchieResa.
	 * 
	 * @param Integer codFiegDl
	 * @param Integer codEdicola
	 * @param Timestamp dtBolla
	 * @param String tipo
	 * @param Integer codInizioQuotidiano
	 * @param Integer codFineQuotidiano
	 * @param Integer gruppoSconto
	 * @param Integer numMaxCopertineVecchieResa
	 * @return List<StoricoCopertineDto>
	 */
	private List<StoricoCopertineDto> buildListStoricoCopertineQuotidianoDto(
			final Integer codFiegDl, final Integer codEdicola,
			final Timestamp dtBolla, final String tipo, final Integer codInizioQuotidiano,
			final Integer codFineQuotidiano, final Integer gruppoSconto, final Integer numMaxCopertineVecchieResa) {
		HibernateCallback<List<StoricoCopertineDto>> actionSc = new HibernateCallback<List<StoricoCopertineDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<StoricoCopertineDto> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(StoricoCopertineVo.class, "sc");
				criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
				criteria.createCriteria("ap.periodicitaVo", "per");
				criteria.createCriteria("sc.immagine", "img", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("sc.motivoResaRespinta", "mresp", JoinType.LEFT_OUTER_JOIN);
				criteria.add(Restrictions.eq("sc.pk.codDl", codFiegDl));
				criteria.add(Restrictions.ge("sc.codicePubblicazione", codInizioQuotidiano));
				criteria.add(Restrictions.le("sc.codicePubblicazione", codFineQuotidiano));
				criteria.add(Restrictions.le("sc.dataUscita", dtBolla));
				
				DetachedCriteria subCriteria = DetachedCriteria.forClass(BollaResaDettaglioVo.class, "bd");
				subCriteria.createCriteria("bollaResa", "bo");
				subCriteria.add(Restrictions.eq("bd.pk.codFiegDl", codFiegDl));
				subCriteria.add(Restrictions.eq("bd.pk.codEdicola", codEdicola));
				subCriteria.add(Restrictions.eq("bd.pk.dtBolla", dtBolla));
				subCriteria.add(Restrictions.eq("bd.pk.tipoBolla", tipo));
				subCriteria.add(Property.forName("bd.idtn").eqProperty("sc.pk.idtn"));
				criteria.add(Subqueries.notExists(subCriteria.setProjection(Projections.property("bd.idtn"))));
				criteria.addOrder(Order.desc("sc.dataUscita"));
				
				ProjectionList properties = Projections.projectionList(); 
				properties.add(Projections.property("sc.pk"), "pk");
				properties.add(Projections.property("sc.codicePubblicazione"), "codicePubblicazione");
				properties.add(Projections.property("sc.complementoDistribuzione"), "complementoDistribuzione");
				properties.add(Projections.property("sc.numeroCopertina"), "numeroCopertina");
				properties.add(Projections.property("sc.dataUscita"), "dataUscita");
				properties.add(Projections.property("sc.codiceBarre"), "codiceBarre");
				properties.add(Projections.property("ap.titolo"), "titolo");
				properties.add(Projections.property("sc.sottoTitolo"), "sottoTitolo");
				properties.add(Projections.property("sc.codiceBarre"), "codiceBarre");
				properties.add(Projections.property("sc.dataRichiamoResa"), "dataRichiamoResa");
				properties.add(Projections.property("sc.tipoRichiamoResa"), "tipoRichiamoResa");
				properties.add(Projections.property("sc.codiceInforete"), "codiceInforete");
				properties.add(Projections.property("sc.numeroCopertinaInforete"), "numeroCopertinaInforete");
				properties.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertina");
				properties.add(Projections.property("sc.percIva"), "percIva");
				properties.add(Projections.property("sc.compensoCompiegamento"), "compensoCompiegamento");
				properties.add(Projections.property("sc.indComponentePaccotto"), "indComponentePaccotto");
				properties.add(Projections.property("sc.multiplo"), "multiplo");
				properties.add(Projections.property("sc.idtnr"), "idtnr");
				properties.add(Projections.property("img.nome"), "nomeImmagine");
				properties.add(Projections.property("sc.fornitoBolla"), "fornitoBolla");
				properties.add(Projections.property("sc.fornitoFondoBolla"), "fornitoFondoBolla");
				properties.add(Projections.property("sc.fornitoEstrattoConto"), "fornitoEstrattoConto");
				properties.add(Projections.property("sc.prezzoEdicola"), "prezzoEdicola");
				properties.add(Projections.property("sc.quantitaCopieContoDeposito"), "quantitaCopieContoDeposito");
				properties.add(Projections.property("mresp.descrizione"), "motivoResaRespinta");
				properties.add(Projections.property("mresp.pk.codMotivoRespinto"), "codMotivoRespinto");
				properties.add(Projections.property("per.pk"), "periodicita");
				properties.add(Projections.property("sc.cesta"), "cesta");
				criteria.setProjection(properties); 
				criteria.setResultTransformer(Transformers.aliasToBean(StoricoCopertineDto.class));
				
				List<StoricoCopertineDto> list = null;
				session.enableFilter("GruppoScontoFilter").setParameter("gruppoSconto", gruppoSconto);
				session.enableFilter("FornitoFilter").setParameter("codEdicola", codEdicola).setParameter("codEdicola2", codEdicola); 
				session.enableFilter("StoricoFilter").setParameter("dataStorico", new Timestamp(new Date(0L).getTime()));
				criteria.setMaxResults(numMaxCopertineVecchieResa);
				list = criteria.list();
				session.disableFilter("FornitoFilter");
				session.disableFilter("GruppoScontoFilter");
				session.disableFilter("StoricoFilter");
				return list;
			}
		};
		return getDao().findByHibernateCallback(actionSc);
	}
	
	/**
	 * Ritorna lo storico delle uscite di un periodico limitato da numMaxCopertineVecchieResa.
	 * 
	 * @param Integer codFiegDl
	 * @param Integer codEdicola
	 * @param Timestamp dtBolla
	 * @param String tipo
	 * @param Integer cpu
	 * @param Integer gruppoSconto
	 * @param Integer numMaxCopertineVecchieResa
	 * @return List<StoricoCopertineDto>
	 */
	private List<StoricoCopertineDto> buildListStoricoCopertineDto(
			final Integer codFiegDl, final Integer codEdicola,
			final Timestamp dtBolla, final String tipo, final Integer cpu,
			final Integer gruppoSconto, final Integer numMaxCopertineVecchieResa) {
		HibernateCallback<List<StoricoCopertineDto>> actionSc = new HibernateCallback<List<StoricoCopertineDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<StoricoCopertineDto> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(StoricoCopertineVo.class, "sc");
				criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
				criteria.createCriteria("ap.periodicitaVo", "per");
				criteria.createCriteria("sc.immagine", "img", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("sc.motivoResaRespinta", "mresp", JoinType.LEFT_OUTER_JOIN);
				criteria.add(Restrictions.eq("sc.pk.codDl", codFiegDl));
				criteria.add(Restrictions.eq("sc.codicePubblicazione", cpu));
				
				DetachedCriteria subCriteria = DetachedCriteria.forClass(BollaResaDettaglioVo.class, "bd");
				subCriteria.createCriteria("bollaResa", "bo");
				subCriteria.add(Restrictions.eq("bd.pk.codFiegDl", codFiegDl));
				subCriteria.add(Restrictions.eq("bd.pk.codEdicola", codEdicola));
				subCriteria.add(Restrictions.eq("bd.pk.dtBolla", dtBolla));
				subCriteria.add(Restrictions.eq("bd.pk.tipoBolla", tipo));
				subCriteria.add(Property.forName("bd.idtn").eqProperty("sc.pk.idtn"));
				criteria.add(Subqueries.notExists(subCriteria.setProjection(Projections.property("bd.idtn"))));
				
				
				ProjectionList properties = Projections.projectionList(); 
				properties.add(Projections.property("sc.pk"), "pk");
				properties.add(Projections.property("sc.codicePubblicazione"), "codicePubblicazione");
				properties.add(Projections.property("sc.complementoDistribuzione"), "complementoDistribuzione");
				properties.add(Projections.property("sc.numeroCopertina"), "numeroCopertina");
				properties.add(Projections.property("sc.dataUscita"), "dataUscita");
				properties.add(Projections.property("sc.codiceBarre"), "codiceBarre");
				properties.add(Projections.property("sc.sottoTitolo"), "sottoTitolo");
				properties.add(Projections.property("sc.codiceBarre"), "codiceBarre");
				properties.add(Projections.property("sc.dataRichiamoResa"), "dataRichiamoResa");
				properties.add(Projections.property("sc.tipoRichiamoResa"), "tipoRichiamoResa");
				properties.add(Projections.property("sc.codiceInforete"), "codiceInforete");
				properties.add(Projections.property("sc.numeroCopertinaInforete"), "numeroCopertinaInforete");
				properties.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertina");
				properties.add(Projections.property("sc.percIva"), "percIva");
				properties.add(Projections.property("sc.compensoCompiegamento"), "compensoCompiegamento");
				properties.add(Projections.property("sc.indComponentePaccotto"), "indComponentePaccotto");
				properties.add(Projections.property("sc.multiplo"), "multiplo");
				properties.add(Projections.property("sc.idtnr"), "idtnr");
				properties.add(Projections.property("img.nome"), "nomeImmagine");
				properties.add(Projections.property("sc.fornitoBolla"), "fornitoBolla");
				properties.add(Projections.property("sc.fornitoFondoBolla"), "fornitoFondoBolla");
				properties.add(Projections.property("sc.fornitoEstrattoConto"), "fornitoEstrattoConto");
				properties.add(Projections.property("sc.prezzoEdicola"), "prezzoEdicola");
				properties.add(Projections.property("sc.quantitaCopieContoDeposito"), "quantitaCopieContoDeposito");
				properties.add(Projections.property("mresp.descrizione"), "motivoResaRespinta");
				properties.add(Projections.property("mresp.pk.codMotivoRespinto"), "codMotivoRespinto");
				properties.add(Projections.property("per.pk"), "periodicita");
				properties.add(Projections.property("sc.cesta"), "cesta");
				criteria.setProjection(properties); 
				criteria.setResultTransformer(Transformers.aliasToBean(StoricoCopertineDto.class));
				
				List<StoricoCopertineDto> list = null;
				session.enableFilter("GruppoScontoFilter").setParameter("gruppoSconto", gruppoSconto);
				session.enableFilter("FornitoFilter").setParameter("codEdicola", codEdicola).setParameter("codEdicola2", codEdicola);
				session.enableFilter("StoricoFilter").setParameter("dataStorico", new Timestamp(new Date(0L).getTime()));
				//criteria.setMaxResults(numMaxCopertineVecchieResa);
				
				// SUBQUERY PER GESTIRE LE DATE
				Criteria criteriaSubqueryDate = session.createCriteria(StoricoCopertineVo.class, "sq");
				criteriaSubqueryDate.add(Restrictions.eq("sq.codicePubblicazione", cpu));
				criteriaSubqueryDate.add(Restrictions.eq("sq.pk.codDl", codFiegDl));
										
				ProjectionList propertiesSub = Projections.projectionList(); 
				propertiesSub.add(Projections.property("sq.dataUscita"), "dataUscita");
				propertiesSub.add(Projections.groupProperty("sq.dataUscita"));
				criteriaSubqueryDate.setProjection(propertiesSub).addOrder(Order.desc("sq.dataUscita")); 
				
				criteriaSubqueryDate.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				criteriaSubqueryDate.setMaxResults(numMaxCopertineVecchieResa);
				List<Map<String, Object>> listResSubQuery = criteriaSubqueryDate.list();
				
				List<Date> inRestrictions = new ArrayList<Date>();
				for (Map<String, Object> record : listResSubQuery) {
			        Entity entity = (Entity) record.get(Criteria.ROOT_ALIAS);
			        Date child = (Date) record.get("dataUscita");
			        inRestrictions.add(child);
			    }
				criteria.add(Restrictions.in("sc.dataUscita", inRestrictions));
				// FINE SUBQUERY PER GESTIRE LE DATE
				criteria.addOrder(Order.desc("sc.dataUscita")).addOrder(Order.desc("sc.numeroCopertina"));
				
				list = criteria.list();
				session.disableFilter("FornitoFilter");
				session.disableFilter("GruppoScontoFilter");
				session.disableFilter("StoricoFilter");
				return list;
			}
		};
		return getDao().findByHibernateCallback(actionSc);
	}
	
	@Override
	public BollaRiassuntoVo getBollaRiassunto(Integer codFiegDl,
			Integer codEdicola, Timestamp dtBolla, String tipo) {
		return getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_BOLLA_RIASSUNTO_BY_ID, codFiegDl, codEdicola, dtBolla, tipo);
	}
	
	@Override
	public List<BollaResaRiassuntoVo> getBollaResaRiassunto(final Integer[] codFiegDl,
			final Integer[] codEdicola, final Timestamp dtBolla, final String tipo) {
		HibernateCallback<List<BollaResaRiassuntoVo>> action = new HibernateCallback<List<BollaResaRiassuntoVo>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<BollaResaRiassuntoVo> doInHibernate(Session session) throws HibernateException, SQLException {
				Query namedQuery = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_GET_BOLLA_RESA_RIASSUNTO_BY_ID);
				namedQuery.setParameterList("codFiegDl", codFiegDl);
				namedQuery.setParameterList("codEdicola", codEdicola);
				namedQuery.setTimestamp("dtBolla", dtBolla);
				namedQuery.setString("tipoBolla", tipo);
				return namedQuery.list();
			}
		};
		return getDao().findByHibernateCallback(action);			
	}
	
	@Override
	public void saveSpuntaBollaRivendita(final String differenze, final String spunte, final String pks, final String differenzeFondoBolla, final String spunteFondoBolla, final String pkFondoBolla, final Integer codEdicola) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				int executeUpdate = 0;
				try {
					if ((differenze != null || spunte != null) && pks != null && session != null) {
						executeUpdate += updateBollaRivendita(differenze, spunte, pks, session);
					}
					if ((differenzeFondoBolla != null || spunteFondoBolla != null) && pkFondoBolla != null && session != null) {
						executeUpdate += updateFondoBollaRivendita(differenzeFondoBolla, spunteFondoBolla, pkFondoBolla, session);
					}
				} catch (Throwable e) {
					throw new HibernateException(e);
				}
				return executeUpdate;
			}
			
			private int updateBollaRivendita(final String differenze, final String spunte, final String pks, Session session) throws ParseException {
				int executeUpdate = 0;
				Query query = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_UPDATE_DIFFERENZE_COPIE_SPUNTATE);
				String[] arrDiff = differenze.split(",");
				String[] arrPks = pks.split(",");
				List<Integer> stSpunte = null;
				stSpunte = new ArrayList<Integer>();
				for (String sp : spunte.split(",")) {
					if (NumberUtils.isNumber(sp.trim())) {
						stSpunte.add(new Integer(sp.trim()));
					} else {
						stSpunte.add(0);
					}
				}
				for (int i = 0; i < arrPks.length; i++) {
					String pkValue = arrPks[i].trim();
					String diff = arrDiff[i].trim().replaceAll("\\+", "");
					Integer spunta = stSpunte.get(i);
					String[] arrPkValue = pkValue.split("\\|");
					String coddl = arrPkValue[0].trim();
					String codriv = arrPkValue[1].trim();
					String dtBolla = arrPkValue[2].trim();
					String tipBolla = arrPkValue[3].trim();
					String posiz = arrPkValue[4].trim();
					query.setInteger("differenze", new Integer(diff.equals("") ? "0" : diff));
					query.setInteger("qtaSpunta", spunta);
					query.setInteger("spunta", Strings.isNullOrEmpty(diff) ? 0 : 1);
					query.setInteger("codFiegDl", new Integer(coddl.equals("") ? "0" : coddl));
					query.setInteger("codEdicola", new Integer(codriv.equals("") ? "0" : codriv));
					query.setTimestamp("dtBolla", DateUtilities.parseDate(dtBolla, DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS));
					query.setString("tipoBolla", tipBolla);
					query.setInteger("posizioneRiga", new Integer(posiz.equals("") ? "0" : posiz));
					executeUpdate += query.executeUpdate();
				}
				return executeUpdate;
			}
			
			private int updateFondoBollaRivendita(final String differenzeFondoBolla, final String spunteFondoBolla, final String pkFondoBolla, Session session) throws ParseException {
				int executeUpdate = 0;
				Query query = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_UPDATE_DIFFERENZE_COPIE_SPUNTATE_FONDO_BOLLA);
				String[] arrDiff = differenzeFondoBolla.split(",");
				String[] arrPks = pkFondoBolla.split(",");
				List<Integer> stSpunte = null;
				stSpunte = new ArrayList<Integer>();
				for (String sp : spunteFondoBolla.split(",")) {
					if (NumberUtils.isNumber(sp.trim())) {
						stSpunte.add(new Integer(sp.trim()));
					} else {
						stSpunte.add(0);
					}
				}
				for (int i = 0; i < arrPks.length; i++) {
					String pkValue = arrPks[i].trim();
					String diff = arrDiff[i].trim().replaceAll("\\+", "");
					Integer spunta = stSpunte.get(i);
					String[] arrPkValue = pkValue.split("\\|");
					String coddl = arrPkValue[0].trim();
					String codriv = arrPkValue[1].trim();
					String dtBolla = arrPkValue[2].trim();
					String tipBolla = arrPkValue[3].trim();
					String posiz = arrPkValue[4].trim();
					query.setInteger("differenze", new Integer(diff.equals("") ? "0" : diff));
					query.setInteger("spunta", spunta);
					query.setInteger("codFiegDl", new Integer(coddl.equals("") ? "0" : coddl));
					query.setInteger("codEdicola", new Integer(codriv.equals("") ? "0" : codriv));
					query.setTimestamp("dtBolla", DateUtilities.parseDate(dtBolla, DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS));
					query.setString("tipoBolla", tipBolla);
					query.setInteger("posizioneRiga", new Integer(posiz.equals("") ? "0" : posiz));
					executeUpdate += query.executeUpdate();
				}
				return executeUpdate;
			}
		};
		getDao().executeByHibernateCallback(action);
	}
	
	@Override
	public void saveBollaRivendita(final Integer codEdicola, final Map<String, String> fields, final Map<String, String> spunte, final Map<String, String> fieldsFB, final Map<String, String> spunteFB) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				int executeUpdate = 0;
				try {
					executeUpdate += updateBollaRivendita(fields, spunte, session);
					executeUpdate += updateFondoBollaRivendita(fieldsFB, spunteFB, session);
				} catch (Throwable e) {
					throw new HibernateException(e);
				}
				return executeUpdate;
			}

			private int updateBollaRivendita(final Map<String, String> fields, final Map<String, String> spunte, Session session) throws ParseException {
				Query query = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_UPDATE_DIFFERENZE_SPUNTE);
				int executeUpdate = 0;
				for (Map.Entry<String, String> entry : fields.entrySet()) {
					String pk = entry.getKey().trim();
					String diff = entry.getValue().trim().replaceAll("\\+", "");
					Integer spunta = spunte.get(pk) != null ? new Integer(spunte.get(pk)) : 0;
					String[] arrPkValue = pk.split("\\|");
					String coddl = arrPkValue[0].trim();
					String codriv = arrPkValue[1].trim();
					String dtBolla = arrPkValue[2].trim();
					String tipBolla = arrPkValue[3].trim();
					String posiz = arrPkValue[4].trim();
					query.setInteger("differenze", new Integer(diff.equals("") ? "0" : diff));
					query.setInteger("spunta", spunta);
					query.setInteger("codFiegDl", new Integer(coddl.equals("") ? "0" : coddl));
					query.setInteger("codEdicola", new Integer(codriv.equals("") ? "0" : codriv));
					query.setTimestamp("dtBolla", DateUtilities.parseDate(dtBolla, DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS));
					query.setString("tipoBolla", tipBolla);
					query.setInteger("posizioneRiga", new Integer(posiz.equals("") ? "0" : posiz));
					try {
						executeUpdate += query.executeUpdate();
					} catch (Throwable e) {}
				}
				return executeUpdate;
			}
			
			private int updateFondoBollaRivendita(final Map<String, String> fields, final Map<String, String> spunte, Session session) throws ParseException {
				Query query = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_UPDATE_DIFFERENZE_SPUNTE_FONDO_BOLLA);
				int executeUpdate = 0;
				for (Map.Entry<String, String> entry : fields.entrySet()) {
					String pk = entry.getKey().trim();
					String diff = entry.getValue().trim().replaceAll("\\+", "");
					Integer spunta = spunte.get(pk) != null ? new Integer(spunte.get(pk)) : 0;
					String[] arrPkValue = pk.split("\\|");
					String coddl = arrPkValue[0].trim();
					String codriv = arrPkValue[1].trim();
					String dtBolla = arrPkValue[2].trim();
					String tipBolla = arrPkValue[3].trim();
					String posiz = arrPkValue[4].trim();
					query.setInteger("differenze", new Integer(diff.equals("") ? "0" : diff));
					query.setInteger("spunta", spunta);
					query.setInteger("codFiegDl", new Integer(coddl.equals("") ? "0" : coddl));
					query.setInteger("codEdicola", new Integer(codriv.equals("") ? "0" : codriv));
					query.setTimestamp("dtBolla", DateUtilities.parseDate(dtBolla, DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS));
					query.setString("tipoBolla", tipBolla);
					query.setInteger("posizioneRiga", new Integer(posiz.equals("") ? "0" : posiz));
					try {
						executeUpdate += query.executeUpdate();
					} catch (Throwable e) {}
				}
				return executeUpdate;
			}
		};
		getDao().executeByHibernateCallback(action);
		
	}
	
	@Override
	public void saveBollaResa(final Map<String, String> reso, final Map<String, String> resoFuoriVoce, final Map<String, String> resoRichiamo) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				int executeUpdate = 0;
				try {
					executeUpdate += updateBollaResaDettaglio(reso, session);
					executeUpdate += updateBollaResaDettaglioFuoriVoce(resoFuoriVoce, session);
					executeUpdate += updateBollaResaDettaglioRichiamo(resoRichiamo, session);
				} catch (Throwable e) {
					throw new HibernateException(e);
				}
				return executeUpdate;
			}
			
			private int updateBollaResaDettaglio(final Map<String, String> resoMap, Session session)
					throws ParseException {
				Query queryReso = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_UPDATE_BOLLA_RESA_DETTAGLIO);
				int executeUpdate = 0;
				for (Map.Entry<String, String> entry : resoMap.entrySet()) {
					String pkValue = entry.getKey().trim();
					String reso = entry.getValue().trim().replaceAll("\\+", "");
					String[] arrPkValue = pkValue.split("\\|");
					String coddl = arrPkValue[0].trim();
					String codriv = arrPkValue[1].trim();
					String dtBolla = arrPkValue[2].trim();
					String tipBolla = arrPkValue[3].trim();
					String posiz = arrPkValue[4].trim();
					if (!reso.equals("")) {
						queryReso.setInteger("reso", new Integer(reso));
					} else {
						queryReso.setParameter("reso", null);
					}
					queryReso.setInteger("codFiegDl", new Integer(coddl.equals("") ? "0" : coddl));
					queryReso.setInteger("codEdicola", new Integer(codriv.equals("") ? "0" : codriv));
					queryReso.setTimestamp("dtBolla", DateUtilities.parseDate(dtBolla, DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS));
					queryReso.setString("tipoBolla", tipBolla);
					queryReso.setInteger("posizioneRiga", new Integer(posiz.equals("") ? "0" : posiz));
					try {
						executeUpdate += queryReso.executeUpdate();
					} catch (Throwable e) {}
				}
				return executeUpdate;
			}
			
			private int updateBollaResaDettaglioFuoriVoce(final Map<String, String> resoFuoriVoce, final Session session)
					throws ParseException {
				Query queryFuoriVoce = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_UPDATE_BOLLA_RESA_DETTAGLIO_FUORI_VOCE);
				int executeUpdate = 0;
				for (Map.Entry<String, String> entry : resoFuoriVoce.entrySet()) {
					String pkValue = entry.getKey().trim();
					String reso = entry.getValue().trim().replaceAll("\\+", "");
					String[] arrPkValue = pkValue.split("\\|");
					String coddl = arrPkValue[0].trim();
					String codriv = arrPkValue[1].trim();
					String dtBolla = arrPkValue[2].trim();
					String tipBolla = arrPkValue[3].trim();
					String posiz = arrPkValue[4].trim();
					if (!reso.equals("")) {
						queryFuoriVoce.setInteger("reso", new Integer(reso));
					} else {
						queryFuoriVoce.setParameter("reso", null);
					}
					queryFuoriVoce.setInteger("codFiegDl", new Integer(coddl.equals("") ? "0" : coddl));
					queryFuoriVoce.setInteger("codEdicola", new Integer(codriv.equals("") ? "0" : codriv));
					queryFuoriVoce.setTimestamp("dtBolla", DateUtilities.parseDate(dtBolla, DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS));
					queryFuoriVoce.setString("tipoBolla", tipBolla);
					queryFuoriVoce.setInteger("posizioneRiga", new Integer(posiz.equals("") ? "0" : posiz));
					try {
						executeUpdate += queryFuoriVoce.executeUpdate();
					} catch (Throwable e) {}
				}
				return executeUpdate;
			}
			
			private int updateBollaResaDettaglioRichiamo(final Map<String, String> resoRichiamo, final Session session)
					throws ParseException {
				Query queryFuoriVoce = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_UPDATE_BOLLA_RESA_DETTAGLIO_RICHIAMO);
				int executeUpdate = 0;
				for (Map.Entry<String, String> entry : resoRichiamo.entrySet()) {
					String pkValue = entry.getKey().trim();
					String reso = entry.getValue().trim().replaceAll("\\+", "");
					String[] arrPkValue = pkValue.split("\\|");
					String coddl = arrPkValue[0].trim();
					String codriv = arrPkValue[1].trim();
					String dtBolla = arrPkValue[2].trim();
					String tipBolla = arrPkValue[3].trim();
					String posiz = arrPkValue[4].trim();
					if (!reso.equals("")) {
						queryFuoriVoce.setInteger("reso", new Integer(reso));
					} else {
						queryFuoriVoce.setParameter("reso", null);
					}
					queryFuoriVoce.setInteger("codFiegDl", new Integer(coddl.equals("") ? "0" : coddl));
					queryFuoriVoce.setInteger("codEdicola", new Integer(codriv.equals("") ? "0" : codriv));
					queryFuoriVoce.setTimestamp("dtBolla", DateUtilities.parseDate(dtBolla, DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS));
					queryFuoriVoce.setString("tipoBolla", tipBolla);
					queryFuoriVoce.setInteger("posizioneRiga", new Integer(posiz.equals("") ? "0" : posiz));
					try {
						executeUpdate += queryFuoriVoce.executeUpdate();
					} catch (Throwable e) {}
				}
				return executeUpdate;
			}
		};
		getDao().executeByHibernateCallback(action);
	}
	
	@Override
	public void saveBolleRiassunto(final String stato, final String pk) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				int executeUpdate = 0;
				try {
					if (stato != null && !stato.equals("") && pk != null && !pk.equals("")) {
						executeUpdate += updateBollaRiassunto(stato, pk, session, executeUpdate);
					}
				} catch (Exception e) {
					throw new HibernateException(e);
				}
				return executeUpdate;
			}
		};
		getDao().executeByHibernateCallback(action);
	} 
	
	@Override
	public void saveBolleResaRiassunto(final String stato, final String pk) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				int executeUpdate = 0;
				try {
					if (stato != null && !stato.equals("") && pk != null && !pk.equals("")) {
						executeUpdate += updateBollaResaRiassunto(stato, pk, session, executeUpdate);
					}
				} catch (Exception e) {
					throw new HibernateException(e);
				}
				return executeUpdate;
			}
		};
		getDao().executeByHibernateCallback(action);
	}
	
	@Override
	public LavorazioneResaVo getLavorazioneResaVo(String zipFile) {
		return getDao().findUniqueResultObjectByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_LAVORAZIONE_RESA_BY_ID, zipFile);
	}
	
	@Override
	public LavorazioneResaVo getLavorazioneResaVo(Integer codFiegDl, Integer codEdicolaDl, Timestamp dtBolla, String tipoBolla) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LavorazioneResaVo.class, "lr");
		criteria.createCriteria("lr.anagraficaEdicolaVo", "ae");
		criteria.createCriteria("lr.anagraficaAgenziaVo", "aa");
		criteria.add(Restrictions.eq("aa.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("ae.codEdicola", codEdicolaDl));
		criteria.add(Restrictions.eq("lr.dataResa", dtBolla));
		criteria.add(Restrictions.eq("lr.tipoResa", tipoBolla));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public List<LavorazioneResaImmagineDto> getListLavorazioneResaImmagineVo(String nomeFile) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LavorazioneResaImmagineVo.class, "lri");
		criteria.add(Restrictions.eq("lri.pk.lavorazioneResaVo.nomeFile", nomeFile));
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("lri.pk.idtn"), "idtn"); 
		properties.add(Projections.property("lri.pk.dataOraLavorazione"), "dataOraLavorazione");
		properties.add(Projections.property("lri.copie"), "copie");
		properties.add(Projections.property("lri.nomeImmagine"), "nomeImmagine");
		properties.add(Projections.property("lri.titolo"), "titolo");
		properties.add(Projections.property("lri.numeroCopertina"), "numeroCopertina");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(LavorazioneResaImmagineDto.class));
		criteria.addOrder(Order.asc("lri.pk.dataOraLavorazione"));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	/**
	 * @param stato
	 * @param pk
	 * @param executeUpdate 
	 * @param session 
	 * @return
	 * @throws ParseException 
	 */
	private int updateBollaRiassunto(String stato, String pk, Session session, int executeUpdate) throws ParseException {
		Query queryFuoriVoce = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_UPDATE_BOLLA_RIASSUNTO);
		String[] arrPk = pk.split(",");
		String[] arrStato = stato.split(",");
		for (int  i = 0; i < arrPk.length; i++) {
			String pkValue = arrPk[i].trim();
			String statoBolla = arrStato[i].trim();
			String[] arrPkValue = pkValue.split("\\|");
			String coddl = arrPkValue[0].trim();
			String codriv = arrPkValue[1].trim();
			String dtBolla = arrPkValue[2].trim();
			String tipBolla = arrPkValue[3].trim();
			queryFuoriVoce.setInteger("bollaTrasmessaDl", new Integer(statoBolla.equals("") ? "0" : statoBolla));
			queryFuoriVoce.setInteger("codFiegDl", new Integer(coddl.equals("") ? "0" : coddl));
			queryFuoriVoce.setInteger("codEdicola", new Integer(codriv.equals("") ? "0" : codriv));
			queryFuoriVoce.setTimestamp("dtBolla", DateUtilities.parseDate(dtBolla, DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS));
			queryFuoriVoce.setString("tipoBolla", tipBolla);
			executeUpdate += queryFuoriVoce.executeUpdate();
		}
		return executeUpdate;
	}
	
	/**
	 * @param stato
	 * @param pk
	 * @param executeUpdate 
	 * @param session 
	 * @return
	 * @throws ParseException 
	 */
	private int updateBollaResaRiassunto(String stato, String pk, Session session, int executeUpdate) throws ParseException {
		Query queryFuoriVoce = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_UPDATE_BOLLA_RESA_RIASSUNTO);
		String[] arrPk = pk.split(",");
		String[] arrStato = stato.split(",");
		for (int  i = 0; i < arrPk.length; i++) {
			String pkValue = arrPk[i].trim();
			String statoBolla = arrStato[i].trim();
			String[] arrPkValue = pkValue.split("\\|");
			String coddl = arrPkValue[0].trim();
			String codriv = arrPkValue[1].trim();
			String dtBolla = arrPkValue[2].trim();
			String tipBolla = arrPkValue[3].trim();
			queryFuoriVoce.setInteger("bollaTrasmessaDl", new Integer(statoBolla.equals("") ? "0" : statoBolla));
			queryFuoriVoce.setInteger("codFiegDl", new Integer(coddl.equals("") ? "0" : coddl));
			queryFuoriVoce.setInteger("codEdicola", new Integer(codriv.equals("") ? "0" : codriv));
			queryFuoriVoce.setTimestamp("dtBolla", DateUtilities.parseDate(dtBolla, DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS));
			queryFuoriVoce.setString("tipoBolla", tipBolla);
			executeUpdate += queryFuoriVoce.executeUpdate();
		}
		return executeUpdate;
	}
	
	@Override
	public List<QuadraturaResaDto> getQuadraturaResa(final Integer codFiegDl, final Integer codEdicola, final Timestamp dtBolla, final String tipoBolla) {
		HibernateCallback<List<QuadraturaResaDto>> action = new HibernateCallback<List<QuadraturaResaDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<QuadraturaResaDto> doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = IGerivQueryContants.SQL_QUERY_GET_QUANDRATURA_RESA;
				SQLQuery sqlQuery = session.createSQLQuery(sql);
				sqlQuery.setInteger("coddl", codFiegDl);
				sqlQuery.setInteger("codEdicola", codEdicola);
				sqlQuery.setTimestamp("dataBolla", dtBolla);
				sqlQuery.setString("tipoBolla", tipoBolla);
				sqlQuery.setResultTransformer(Transformers.aliasToBean(QuadraturaResaDto.class));
				sqlQuery.addScalar("cpu", IntegerType.INSTANCE);
				sqlQuery.addScalar("titolo", StringType.INSTANCE);
				sqlQuery.addScalar("numero", StringType.INSTANCE);
				sqlQuery.addScalar("copieDichiarate", IntegerType.INSTANCE);
				sqlQuery.addScalar("copieRiscontrate", IntegerType.INSTANCE);
				sqlQuery.addScalar("copieRespinte", IntegerType.INSTANCE);
				sqlQuery.addScalar("motivo", StringType.INSTANCE);
				sqlQuery.addScalar("prezzoDichiarato", BigDecimalType.INSTANCE);
				sqlQuery.addScalar("prezzoRiscontrato", BigDecimalType.INSTANCE);
				return sqlQuery.list();
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public Boolean cpuExistsInBolla(Integer codFiegDl, Timestamp dtBolla, String tipoBolla, Integer cpu) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaResaVo.class, "brvo");
		criteria.add(Restrictions.eq("brvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("brvo.pk.dtBolla", dtBolla));
		criteria.add(Restrictions.eq("brvo.pk.tipoBolla", tipoBolla));
		criteria.add(Restrictions.eq("brvo.cpuDl", cpu));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("brvo.cpuDl"));
		criteria.setProjection(properties);
		Integer cpuDl = getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		
		DetachedCriteria criteria1 = DetachedCriteria.forClass(BollaVo.class, "brvo");
		criteria1.add(Restrictions.eq("brvo.pk.codFiegDl", codFiegDl));
		criteria1.add(Restrictions.eq("brvo.pk.dtBolla", dtBolla));
		criteria1.add(Restrictions.eq("brvo.pk.tipoBolla", tipoBolla));
		criteria1.add(Restrictions.eq("brvo.cpuDl", cpu));
		ProjectionList properties1 = Projections.projectionList();
		properties1.add(Projections.property("brvo.cpuDl"));
		criteria1.setProjection(properties1);
		Integer cpuDl1 = getDao().findUniqueResultObjectByDetachedCriteria(criteria1);
		
		return (cpuDl != null || cpuDl1 != null) ? true : false;
	}
	
	@Override
	public BollaResaVo getBollaResaVo(Integer codFiegDl, Date dataBolla, String tipoBolla, Integer numeroRiga) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaResaVo.class, "brvo");
		criteria.add(Restrictions.eq("brvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("brvo.pk.dtBolla", dataBolla));
		criteria.add(Restrictions.eq("brvo.pk.tipoBolla", tipoBolla));
		criteria.add(Restrictions.eq("brvo.pk.posizioneRiga", numeroRiga));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public DecodificaRichiamiResaVo getRichiamoResa(Integer codFiegDl, Integer tipoRichiamoResa) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DecodificaRichiamiResaVo.class, "brvo");
		criteria.add(Restrictions.eq("brvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("brvo.pk.tipoRichiamoResa", tipoRichiamoResa));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public BollaResaDettaglioVo getBollaResaDettaglioVo(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla, Integer idtn) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaResaDettaglioVo.class, "brvo");
		criteria.add(Restrictions.eq("brvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("brvo.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("brvo.pk.dtBolla", dataBolla));
		criteria.add(Restrictions.eq("brvo.pk.tipoBolla", tipoBolla));
		criteria.add(Restrictions.eq("brvo.pk.posizioneRiga", idtn));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public ResaRiscontrataVo getResaRiscontrataVo(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla, Integer idtn) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ResaRiscontrataVo.class, "brvo");
		criteria.add(Restrictions.eq("brvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("brvo.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("brvo.pk.dtBolla", dataBolla));
		criteria.add(Restrictions.eq("brvo.pk.tipoBolla", tipoBolla));
		criteria.add(Restrictions.eq("brvo.pk.idtn", idtn));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public BollaStatisticaStoricoVo getBollaStatisticaStoricoVo(Integer codFiegDl, Integer codEdicola, Integer idtn) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaStatisticaStoricoVo.class, "brvo");
		criteria.add(Restrictions.eq("brvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("brvo.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("brvo.pk.idtn", idtn));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public Integer getLastRigaBolla(Integer codFiegDl, Timestamp dataBolla, String tipoBolla) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaVo.class, "bvo");
		criteria.add(Restrictions.eq("bvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("bvo.pk.dtBolla", dataBolla));
		criteria.add(Restrictions.eq("bvo.pk.tipoBolla", tipoBolla));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.max("bvo.riga"));
		criteria.setProjection(props);
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public BollaDettaglioVo getBollaDettaglioVo(String pk, Integer idtn) throws ParseException {
		String[] arrPkValue = pk.split("\\|");
		String coddl = arrPkValue[0].trim();
		String codEdicola = arrPkValue[1].trim();
		String dataBolla = arrPkValue[2].trim();
		String tipoBolla = arrPkValue[3].trim();
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaDettaglioVo.class, "bde");
		criteria.add(Restrictions.eq("bde.pk.codFiegDl", new Integer(coddl)));
		criteria.add(Restrictions.eq("bde.pk.codEdicola", new Integer(codEdicola)));
		criteria.add(Restrictions.eq("bde.pk.dtBolla", DateUtilities.parseDate(dataBolla, DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS)));
		criteria.add(Restrictions.eq("bde.pk.tipoBolla", tipoBolla));
		criteria.add(Restrictions.eq("bde.idtn", idtn));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public BollaVo getBollaVo(Integer codFiegDl, Date dataBolla, String tipoBolla, Integer posizioneRiga, Integer idtn) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaVo.class, "bvo");
		criteria.add(Restrictions.eq("bvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("bvo.pk.dtBolla", dataBolla));
		criteria.add(Restrictions.eq("bvo.pk.tipoBolla", tipoBolla));
		if (posizioneRiga != null) {
			criteria.add(Restrictions.eq("bvo.pk.posizioneRiga", posizioneRiga));
		}
		if (idtn != null) {
			criteria.add(Restrictions.eq("bvo.idTestataNumero", idtn));
		}
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public BollaDettaglioVo getDettaglioBolla(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla, Integer posizioneRiga) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaDettaglioVo.class, "bvo");
		criteria.add(Restrictions.eq("bvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("bvo.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("bvo.pk.dtBolla", dataBolla));
		criteria.add(Restrictions.eq("bvo.pk.tipoBolla", tipoBolla));
		if (posizioneRiga != null) {
			criteria.add(Restrictions.eq("bvo.pk.posizioneRiga", posizioneRiga));
		}
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public void deleteFondoBollaEdicolaInforiv(Integer codFiegDl, Integer codEdicola, Timestamp dataBolla, String tipoBolla) {
		getDao().bulkUpdate("delete from FondoBollaDettaglioVo vo where vo.pk.codFiegDl = ? and vo.pk.codEdicola = ? and vo.pk.dtBolla = ? and vo.pk.tipoBolla = ?", new Object[]{codFiegDl, codEdicola, dataBolla, tipoBolla});
	}
	
	@Override
	public Integer getLastPosizioneRigaBolla(Integer codFiegDl, Integer codEdicola, Timestamp dataBolla, String tipoBolla) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaDettaglioVo.class, "bvo");
		criteria.add(Restrictions.eq("bvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("bvo.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("bvo.pk.dtBolla", dataBolla));
		criteria.add(Restrictions.eq("bvo.pk.tipoBolla", tipoBolla));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.max("bvo.pk.posizioneRiga"));
		criteria.setProjection(props);
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<String> getTipiBollaConsegna(Integer codFiegDl, Timestamp dataBolla) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaVo.class, "bvo");
		criteria.add(Restrictions.eq("bvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("bvo.pk.dtBolla", dataBolla));
		criteria.addOrder(Order.asc("bvo.pk.tipoBolla"));

		ProjectionList projection = Projections.projectionList(); 
		projection.add(Projections.distinct(Projections.property("bvo.pk.tipoBolla")));
		criteria.setProjection(projection);
		
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<String> getTipiBollaResa(Integer codFiegDl, Timestamp dataBolla) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaResaVo.class, "brvo");
		criteria.add(Restrictions.eq("brvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("brvo.pk.dtBolla", dataBolla));
		criteria.addOrder(Order.asc("brvo.pk.tipoBolla"));

		ProjectionList projection = Projections.projectionList(); 
		projection.add(Projections.distinct(Projections.property("brvo.pk.tipoBolla")));
		criteria.setProjection(projection);
		
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<PubblicazioneDto> getPubblicazioniInBolla(Integer[] codFiegDl, Integer[] codEdicola, Timestamp dtBolla, String tipoBolla, String titolo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaDettaglioVo.class, "bvo");
		criteria.add(Restrictions.in("bvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.in("bvo.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("bvo.pk.dtBolla", dtBolla));
		criteria.add(Restrictions.eq("bvo.pk.tipoBolla", tipoBolla));
		criteria.createCriteria("bvo.storicoCopertineVo", "sc", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap", JoinType.LEFT_OUTER_JOIN);
		if (!Strings.isNullOrEmpty(titolo)) {
			criteria.add(Restrictions.ilike("ap.titolo", titolo, MatchMode.START));
		}
		criteria.addOrder(Order.asc("ap.titolo"));
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("ap.titolo"), "titolo");
		properties.add(Projections.property("sc.codicePubblicazione"), "codicePubblicazione");
		properties.add(Projections.property("sc.sottoTitolo"), "sottoTitolo"); 
		properties.add(Projections.property("sc.numeroCopertina"), "numeroCopertina"); 
		properties.add(Projections.property("sc.dataUscita"), "dataUscita");
		properties.add(Projections.property("sc.pk.idtn"), "idtn");
		properties.add(Projections.property("sc.pk.codDl"), "coddl");
		properties.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertina");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(PubblicazioneDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public Integer getProgressivoIdtnBollaConsegna(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipoBolla, Integer idtn) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaDettaglioVo.class, "bvo");
		criteria.add(Restrictions.eq("bvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("bvo.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("bvo.pk.dtBolla", dtBolla));
		criteria.add(Restrictions.eq("bvo.pk.tipoBolla", tipoBolla));
		criteria.add(Restrictions.eq("bvo.idtn", idtn));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("bvo.pk.posizioneRiga"));
		criteria.setProjection(properties); 
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public Integer getProgressivoIdtnBollaResa(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipoBolla, Integer idtn) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaResaDettaglioVo.class, "bvo");
		criteria.add(Restrictions.eq("bvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("bvo.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("bvo.pk.dtBolla", dtBolla));
		criteria.add(Restrictions.eq("bvo.pk.tipoBolla", tipoBolla));
		criteria.add(Restrictions.eq("bvo.idtn", idtn));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("bvo.pk.posizioneRiga"));
		criteria.setProjection(properties); 
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public Boolean isPubblicazioneInBolleConsegnaTipo(Integer codFiegDl, Integer cpu, String tipoBolla) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaVo.class, "bvo");
		criteria.add(Restrictions.eq("bvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("bvo.pk.tipoBolla", tipoBolla));
		criteria.add(Restrictions.eq("bvo.cpuDl", cpu));
		criteria.add(Restrictions.ge("bvo.pk.dtBolla", DateUtilities.togliGiorni(new Date(), 60)));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.count("bvo.cpuDl"));
		criteria.setProjection(props);
		Number count = (Number) getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		return (count != null && count.intValue() > 0) ? true : false;
	}
	
	@Override
	public List<BollaRiassuntoDto> getBolleRiassuntoNonInviate(Timestamp[] dateBolla, String tipo, Integer codFiegDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaRiassuntoVo.class, "bvo");
		criteria.createCriteria("bvo.abbinamentoEdicolaDlVo", "ab");
		criteria.add(Restrictions.isNull("ab.dtSospensioneEdicola"));
		criteria.add(Restrictions.eq("bvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.in("bvo.pk.dtBolla", dateBolla));
		criteria.add(Restrictions.eq("bvo.pk.tipoBolla", tipo));
		criteria.add(Restrictions.eq("bvo.bollaTrasmessaDl", IGerivConstants.INDICATORE_BOLLA_NON_TRASMESSA));
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("bvo.pk.codFiegDl"), "codFiegDl");
		properties.add(Projections.property("bvo.pk.codEdicola"), "codEdicola");
		properties.add(Projections.property("bvo.pk.dtBolla"), "dtBolla");
		properties.add(Projections.property("bvo.pk.tipoBolla"), "tipoBolla");
		properties.add(Projections.property("bvo.numVoci"), "numVoci");
		properties.add(Projections.property("bvo.valoreBolla"), "valoreBolla"); 
		properties.add(Projections.property("bvo.gruppoSconto"), "gruppoSconto"); 
		properties.add(Projections.property("bvo.bollaTrasmessaDl"), "bollaTrasmessaDl");
		properties.add(Projections.property("bvo.dtTrasmissione"), "dtTrasmissione");
		properties.add(Projections.property("bvo.totaleDifferenze"), "totaleDifferenze");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(BollaRiassuntoDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<BollaRiassuntoDto> getBolleResaRiassuntoNonInviate(Timestamp[] dateBolla, String tipo, Integer codFiegDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaResaRiassuntoVo.class, "bvo");
		criteria.createCriteria("bvo.abbinamentoEdicolaDlVo", "ab");
		criteria.add(Restrictions.isNull("ab.dtSospensioneEdicola"));
		criteria.add(Restrictions.eq("bvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.in("bvo.pk.dtBolla", dateBolla));
		criteria.add(Restrictions.eq("bvo.pk.tipoBolla", tipo));
		criteria.add(Restrictions.eq("bvo.bollaTrasmessaDl", IGerivConstants.INDICATORE_BOLLA_NON_TRASMESSA));
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("bvo.pk.codFiegDl"), "codFiegDl");
		properties.add(Projections.property("bvo.pk.codEdicola"), "codEdicola");
		properties.add(Projections.property("bvo.pk.dtBolla"), "dtBolla");
		properties.add(Projections.property("bvo.pk.tipoBolla"), "tipoBolla");
		properties.add(Projections.property("bvo.numVoci"), "numVoci");
		properties.add(Projections.property("bvo.valoreBolla"), "valoreBolla"); 
		properties.add(Projections.property("bvo.gruppoSconto"), "gruppoSconto"); 
		properties.add(Projections.property("bvo.bollaTrasmessaDl"), "bollaTrasmessaDl");
		properties.add(Projections.property("bvo.dtTrasmissione"), "dtTrasmissione");
		properties.add(Projections.property("bvo.totaleCopieBollaResaDettaglio"), "totaleCopieBollaResaDettaglio");
		properties.add(Projections.property("bvo.edicolaInFerie"), "edicolaInFerie");
		
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(BollaRiassuntoDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public BollaResaRichiamoPersonalizzatoVo getBollaResaRichiamoPersonalizzato(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipoBolla, Integer progressivo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaResaRichiamoPersonalizzatoVo.class, "bvo");
		criteria.add(Restrictions.eq("bvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("bvo.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.ge("bvo.pk.dtBolla", dtBolla));
		criteria.add(Restrictions.ge("bvo.pk.tipoBolla", tipoBolla));
		criteria.add(Restrictions.ge("bvo.pk.posizioneRiga", progressivo));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public List<BollaRiassuntoDto> getListaBolle(final Integer codFiegDl, final Integer maxResult)
	{
		HibernateCallback<List<BollaRiassuntoDto>> actionSc = new HibernateCallback<List<BollaRiassuntoDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<BollaRiassuntoDto> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(BollaVo.class, "bvo");
				criteria.add(Restrictions.eq("bvo.pk.codFiegDl", codFiegDl));
				
				ProjectionList properties = Projections.projectionList(); 
				properties.add(Projections.distinct(Projections.projectionList()
						  .add(Projections.property("bvo.pk.dtBolla"), "dtBolla")
						  .add(Projections.property("bvo.pk.tipoBolla"), "tipoBolla")));
				criteria.setProjection(properties); 
				criteria.setResultTransformer(Transformers.aliasToBean(BollaRiassuntoDto.class));
				
				criteria.addOrder(Order.desc("bvo.pk.dtBolla"));
				
				if(maxResult != null)
				{
					criteria.setMaxResults(maxResult);
				}
				List<BollaRiassuntoDto> list = criteria.list();
				return list;
			}
		};
		return getDao().findByHibernateCallback(actionSc);
	}
	
	@Override
	public List<BollaVo> getDettaglioBolla(Integer codFiegDl, String tipoBolla, Timestamp dataBolla)
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaVo.class, "bvo");
		criteria.createCriteria("bvo.storicoCopertineVo", "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
		criteria.add(Restrictions.eq("bvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("bvo.pk.dtBolla", dataBolla));
		criteria.add(Restrictions.eq("bvo.pk.tipoBolla", tipoBolla));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	public void updateNonUscite(final String pk, final String selectedDataTipoBolla, final Integer codFiegDl)
	{
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				int executeUpdate = 0;
				Query query = session
						.createQuery(IGerivQueryContants.HQL_UPDATE_INDICATORE_PUBB_NON_USCITA);
				try
				{
					HashMap<String, String> mapKey = new HashMap<String,String>();
					if (pk != null) {
						String[] arrBolla = pk.split(",");
						for (int i = 0; i < arrBolla.length; i++) {
							String pkValue = arrBolla[i].trim();
							executeUpdate += updateIndicatoreNonUscite(query, pkValue,true);
							mapKey.put(pkValue, pkValue);
						}
					}
					
					//recupero tutte le righe della bolla
					String[] split = selectedDataTipoBolla.split("\\|");
					Timestamp dtBolla = DateUtilities.parseDate(split[0], DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS);
					String tipoBolla = split[1];
					List<BollaVo> listDettBolla = getDettaglioBolla(codFiegDl, tipoBolla, dtBolla);
					
					for(BollaVo bollaVo:listDettBolla)
					{
						if(!mapKey.containsKey(bollaVo.getPk().toString()))
						{
							executeUpdate += updateIndicatoreNonUscite(query, bollaVo.getPk().toString(),false);
						}
					}
					
				}
				catch(Throwable e)
				{
					throw new HibernateException(e);
				}
				return executeUpdate;
			}
		};
		getDao().executeByHibernateCallback(action);
	}
	
	private int updateIndicatoreNonUscite(Query query,String pkValue,boolean indNonUscita) throws ParseException
	{
		String[] split = pkValue.split("\\|");
		
		Integer codFiegDl = new Integer(split[0]);
		Timestamp dtBolla = DateUtilities.parseDate(split[1], DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS);
		String tipoBolla = split[2];
		Integer posizioneRiga = new Integer(split[3]);
		
		query.setBoolean("pubblicazioneNonUscita", indNonUscita);
		query.setInteger("codFiegDl", codFiegDl);
		query.setTimestamp("dtBolla", dtBolla);
		query.setString("tipoBolla", tipoBolla);
		query.setInteger("posizioneRiga", posizioneRiga);
		
		return query.executeUpdate();
	}

	
	
}
