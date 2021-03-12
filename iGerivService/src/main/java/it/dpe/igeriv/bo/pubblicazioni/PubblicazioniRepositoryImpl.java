package it.dpe.igeriv.bo.pubblicazioni;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.forEach;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.sort;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.AnagraficaEditoreDto;
import it.dpe.igeriv.dto.ContoDepositoDto;
import it.dpe.igeriv.dto.EmailDlDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.dto.PubblicazioneFornito;
import it.dpe.igeriv.dto.PubblicazionePiuVendutaDto;
import it.dpe.igeriv.dto.RichiestaRifornimentoDto;
import it.dpe.igeriv.filter.ListFilter;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.util.ProjectionOrder;
import it.dpe.igeriv.vo.AbbinamentoIdtnInforivVo;
import it.dpe.igeriv.vo.AnagraficaEditoreDlVo;
import it.dpe.igeriv.vo.AnagraficaPubblicazioniVo;
import it.dpe.igeriv.vo.ArgomentoVo;
import it.dpe.igeriv.vo.BarraSceltaRapidaDestraVo;
import it.dpe.igeriv.vo.BarraSceltaRapidaSinistraVo;
import it.dpe.igeriv.vo.BollaDettaglioVo;
import it.dpe.igeriv.vo.BollaStatisticaStoricoVo;
import it.dpe.igeriv.vo.ContoDepositoVo;
import it.dpe.igeriv.vo.FondoBollaDettaglioVo;
import it.dpe.igeriv.vo.ImmaginePubblicazioneVo;
import it.dpe.igeriv.vo.ParametriEdicolaVo;
import it.dpe.igeriv.vo.PeriodicitaTrascodificaInforeteVo;
import it.dpe.igeriv.vo.PeriodicitaVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoVo;
import it.dpe.igeriv.vo.StoricoCopertineVo;
import it.dpe.igeriv.vo.VenditaDettaglioVo;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;

import oracle.jdbc.OracleTypes;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.mortbay.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import ch.lambdaj.group.Group;

import com.google.common.base.Strings;
import com.ibm.icu.text.SimpleDateFormat;

@Repository
class PubblicazioniRepositoryImpl extends BaseRepositoryImpl implements PubblicazioniRepository {
	private final int pubblicazioniPiuVenduteMaxResults;
	private ListFilter<RichiestaRifornimentoDto> richiestaRifornimentoDtoListFilter;
	private ListFilter<PubblicazioneDto> pubblicazioneListFilter;
	
	@Autowired
	PubblicazioniRepositoryImpl(BaseDao<AnagraficaPubblicazioniVo> dao, @Value("${igeriv.pubblicazioni.piu.vendute.max.results}") int pubblicazioniPiuVenduteMaxResults) {
		super(dao);
		this.pubblicazioniPiuVenduteMaxResults = pubblicazioniPiuVenduteMaxResults;
		richiestaRifornimentoDtoListFilter = new PubblicazioneFornitoListFilter<>();
		pubblicazioneListFilter = new PubblicazioneFornitoListFilter<>();
		
	}
	
	@Override
	public PubblicazioneDto getCopertinaByIdtn(Integer codDl, Integer idtn) {
		return getCopertinaByIdtn(new Integer[]{codDl}, null, idtn, true, false);
	}
	
	@Override
	public PubblicazioneDto getCopertinaByIdtn(final Integer[] codDl, final Integer[] codEdicola, final Integer idtn, final boolean showPubblicazioniTuttiDl, final Boolean dlInforiv) {
		HibernateCallback<PubblicazioneDto> action = new HibernateCallback<PubblicazioneDto>() {
			@Override
			public PubblicazioneDto doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(StoricoCopertineVo.class, "sc");
				criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
				criteria.createCriteria("ap.limitiPeriodicitaVo", "lp");
				criteria.add(Restrictions.in("sc.pk.codDl", codDl));
				criteria.add(Restrictions.eq("sc.pk.idtn", idtn));
				if (dlInforiv == null || !dlInforiv && (showPubblicazioniTuttiDl && codDl != null && codEdicola != null && codDl.length > 1 && codEdicola.length > 1)) {
					addClausesForMultipleDl(codEdicola, criteria);
				}
				ProjectionList properties = Projections.projectionList(); 
				properties.add(Projections.property("ap.titolo"), "titolo");
				properties.add(Projections.property("ap.codInizioQuotidiano"), "codInizioQuotidiano");
				properties.add(Projections.property("ap.codFineQuotidiano"), "codFineQuotidiano");
				properties.add(Projections.property("ap.numCopertinePrecedentiPerRifornimenti"), "numCopertinePrecedentiPerRifornimenti");
				properties.add(Projections.property("sc.codicePubblicazione"), "codicePubblicazione");
				properties.add(Projections.property("sc.sottoTitolo"), "sottoTitolo"); 
				properties.add(Projections.property("sc.numeroCopertina"), "numeroCopertina"); 
				properties.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertina");
				properties.add(Projections.property("sc.dataUscita"), "dataUscita");
				properties.add(Projections.property("sc.pk.idtn"), "idtn");
				properties.add(Projections.property("lp.giorniValiditaRichiesteRifornimento"), "giorniValiditaRichiesteRifornimento");
				properties.add(Projections.property("lp.maxStatisticaVisualizzare"), "maxStatisticaVisualizzare");
				properties.add(Projections.property("lp.numGiorniDaDataUscitaPerRichiestaRifornimento"), "numGiorniDaDataUscitaPerRichiestaRifornimento");
				properties.add(Projections.property("sc.giancezaPressoDl"), "giancezaPressoDl");
				properties.add(Projections.property("sc.codEdicolaCorrezioneBarcode"), "codEdicolaCorrezioneBarcode");
				properties.add(Projections.property("sc.pk.codDl"), "coddl");
				criteria.setProjection(properties); 
				criteria.setResultTransformer(Transformers.aliasToBean(PubblicazioneDto.class));
				Object findByDetachedCriteria = criteria.uniqueResult();
				return findByDetachedCriteria != null ? (PubblicazioneDto) findByDetachedCriteria : null;
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public PubblicazioneDto getCopertinaByCpuNum(final Integer[] codDl, final Integer[] codEdicola, final Integer cpu, final Integer cddt, final String num, final Boolean multiDl) {
		HibernateCallback<PubblicazioneDto> action = new HibernateCallback<PubblicazioneDto>() {
			@Override
			public PubblicazioneDto doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(StoricoCopertineVo.class, "sc");
				criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
				criteria.createCriteria("ap.limitiPeriodicitaVo", "lp");
				criteria.add(Restrictions.in("sc.pk.codDl", codDl));
				criteria.add(Restrictions.eq("sc.codicePubblicazione", cpu));
				criteria.add(Restrictions.eq("sc.complementoDistribuzione", cddt));
				criteria.add(Restrictions.eq("sc.numeroCopertina", num));
				if (multiDl == null || multiDl) {
					addClausesForMultipleDl(codEdicola, criteria);
				}
				ProjectionList properties = Projections.projectionList(); 
				properties.add(Projections.property("ap.titolo"), "titolo");
				properties.add(Projections.property("ap.codInizioQuotidiano"), "codInizioQuotidiano");
				properties.add(Projections.property("ap.codFineQuotidiano"), "codFineQuotidiano");
				properties.add(Projections.property("ap.numCopertinePrecedentiPerRifornimenti"), "numCopertinePrecedentiPerRifornimenti");
				properties.add(Projections.property("sc.codicePubblicazione"), "codicePubblicazione");
				properties.add(Projections.property("sc.sottoTitolo"), "sottoTitolo"); 
				properties.add(Projections.property("sc.numeroCopertina"), "numeroCopertina"); 
				properties.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertina");
				properties.add(Projections.property("sc.dataUscita"), "dataUscita");
				properties.add(Projections.property("sc.pk.idtn"), "idtn");
				properties.add(Projections.property("lp.giorniValiditaRichiesteRifornimento"), "giorniValiditaRichiesteRifornimento");
				properties.add(Projections.property("lp.maxStatisticaVisualizzare"), "maxStatisticaVisualizzare");
				properties.add(Projections.property("sc.giancezaPressoDl"), "giancezaPressoDl");
				properties.add(Projections.property("sc.codEdicolaCorrezioneBarcode"), "codEdicolaCorrezioneBarcode");
				properties.add(Projections.property("sc.pk.codDl"), "coddl");
				criteria.setProjection(properties); 
				criteria.setResultTransformer(Transformers.aliasToBean(PubblicazioneDto.class));
				Object findByDetachedCriteria = criteria.uniqueResult();
				return findByDetachedCriteria != null ? (PubblicazioneDto) findByDetachedCriteria : null;
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public PubblicazioneDto getCopertinaByIdtn(Integer[] codDl, Integer[] codEdicola, Integer idtn) {
		return getCopertinaByIdtn(codDl, codEdicola, idtn, true, false);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RichiestaRifornimentoDto> getCopertineByCodPubblicazione(final Integer[] codDl, final Integer[] codEdicola, 
			final  Integer codicePubblicazione, final Integer codiceInizioQuotidiano, final Integer codiceFineQuotidiano, 
			final Integer numCopertinePrecedentiPerRifornimenti, final Timestamp dataStorico, final Timestamp dataUscitaLimite, 
			final boolean isMultiDl, final Integer currCodDl, final Integer agenziaFatturazione, Map<String,Object> params) {
		HibernateCallback<List<RichiestaRifornimentoDto>> action = new HibernateCallback<List<RichiestaRifornimentoDto>>() {
			@Override
			public List<RichiestaRifornimentoDto> doInHibernate(Session session)
					throws HibernateException, SQLException {
				
				
				
				Criteria criteria = session.createCriteria(StoricoCopertineVo.class, "sc");
				criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
				criteria.createCriteria("sc.immagine", "im", JoinType.LEFT_OUTER_JOIN);
				if (codicePubblicazione != null && !codicePubblicazione.equals("")) {
					criteria.add(Restrictions.eq("sc.codicePubblicazione", codicePubblicazione));
				} else if (codiceInizioQuotidiano != null && !codiceInizioQuotidiano.equals("") && 
						codiceFineQuotidiano != null && !codiceFineQuotidiano.equals("")) {
					criteria.add(Restrictions.between("sc.codicePubblicazione", codiceInizioQuotidiano, codiceFineQuotidiano));
					if (dataUscitaLimite != null) {
						criteria.add(Restrictions.le("sc.dataUscita", dataUscitaLimite));
					}
				}
				
				if (!isMultiDl || currCodDl == null) {
					if (codicePubblicazione != null && !codicePubblicazione.equals("")) {
						Criteria criteriaSubquery = session.createCriteria(StoricoCopertineVo.class, "sq");
						criteriaSubquery.add(Restrictions.eq("sq.codicePubblicazione", codicePubblicazione));
						criteriaSubquery.add(Restrictions.in("sq.pk.codDl", codDl));
												
						ProjectionList propertiesSub = Projections.projectionList(); 
						propertiesSub.add(Projections.property("sq.dataUscita"), "dataUscita");
						propertiesSub.add(Projections.groupProperty("sq.dataUscita"));
						criteriaSubquery.setProjection(propertiesSub).addOrder(Order.desc("sq.dataUscita")); 
						
						criteriaSubquery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						criteriaSubquery.setMaxResults(numCopertinePrecedentiPerRifornimenti);
						List<Map<String, Object>> listResSubQuery = criteriaSubquery.list();
						
						//System.out.println("LIST : "+listResSubQuery.size());
						List<Date> inRestrictions = new ArrayList<Date>();
						for (Map<String, Object> record : listResSubQuery) {
					        Entity entity = (Entity) record.get(Criteria.ROOT_ALIAS);
					        Date child = (Date) record.get("dataUscita");
					        //String s = DateUtilities.getTimestampAsString(child, DateUtilities.FORMATO_DATA);
					        //System.out.println("DATE : "+s);
					        inRestrictions.add(child);
					    }
						criteria.add(Restrictions.in("sc.dataUscita", inRestrictions));
					}
				}
				
				criteria.add(Restrictions.in("sc.pk.codDl", codDl));
				criteria.addOrder(Order.desc("sc.dataUscita"));
				ProjectionList properties = Projections.projectionList(); 
				properties.add(Projections.property("ap.titolo"), "titolo");
				properties.add(Projections.property("ap.codFornitore"), "editore");
				properties.add(Projections.property("ap.codInizioQuotidiano"), "codInizioQuotidiano");
				properties.add(Projections.property("ap.codFineQuotidiano"), "codFineQuotidiano");
				properties.add(Projections.property("ap.numCopertinePrecedentiPerRifornimenti"), "numCopertinePrecedentiPerRifornimenti");
				properties.add(Projections.property("ap.indicatorePaccotto"), "indicatorePaccotto");
				
				properties.add(Projections.property("sc.codicePubblicazione"), "codicePubblicazione");
				properties.add(Projections.property("sc.sottoTitolo"), "sottoTitolo"); 
				properties.add(Projections.property("sc.numeroCopertina"), "numeroCopertina"); 
				properties.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertina");
				properties.add(Projections.property("sc.dataUscita"), "dataUscita");
				
				if (isMultiDl && currCodDl != null) {
					properties.add(Projections.property("sc.fornitoBolla"), "fornitoBolla");
					properties.add(Projections.property("sc.fornitoFondoBolla"), "fornitoFondoBolla");
					properties.add(Projections.property("sc.fornitoStorico"), "fornitoStorico");
					properties.add(Projections.property("sc.fornitoEstrattoConto"), "fornitoEstrattoConto");
				}

				properties.add(Projections.property("sc.pk.idtn"), "idtn");
				properties.add(Projections.property("sc.pk.codDl"), "coddl");
				properties.add(Projections.property("sc.codiceInforete"), "codiceInforete");
				properties.add(Projections.property("sc.numeroCopertinaInforete"), "numeroCopertinaInforete");
				properties.add(Projections.property("im.nome"), "immagine");
				criteria.setProjection(properties); 
				criteria.setResultTransformer(Transformers.aliasToBean(RichiestaRifornimentoDto.class));
				if (isMultiDl && currCodDl != null) {
					Integer codEdicola1 = codEdicola.length > 1 ? codEdicola[1] : codEdicola[0];
					session.enableFilter("FornitoFilter").setParameter("codEdicola", codEdicola[0]).setParameter("codEdicola2", codEdicola1); 
					session.enableFilter("StoricoFilter").setParameter("dataStorico", (dataStorico == null) ? new Timestamp(new Date(0L).getTime()) : dataStorico);
				}
				if (!isMultiDl || currCodDl == null) {
					//criteria.setMaxResults(numCopertinePrecedentiPerRifornimenti);
				}
				
				List<RichiestaRifornimentoDto> list = criteria.list();
				session.disableFilter("FornitoFilter");
				session.disableFilter("StoricoFilter");
				return list;
			}
		};
		List<RichiestaRifornimentoDto> copertine = getDao().findByHibernateCallback(action);
		if (isMultiDl && currCodDl != null) {
			if (params == null) {
				params = new HashMap<String, Object>();
			}
			params.put("codFiegDl", currCodDl);
			if (copertine != null && !copertine.isEmpty()) {
				richiestaRifornimentoDtoListFilter.filter(copertine, params);
				
				if(copertine.size()>0){
					RichiestaRifornimentoDto dto = copertine.get(0);
					if(dto.getIndicatorePaccotto().intValue() != IGerivConstants.COD_ELEMENTO_PACCOTTO_DIVISIBILE){
						if (copertine.size() > numCopertinePrecedentiPerRifornimenti) {
							copertine = copertine.subList(0, numCopertinePrecedentiPerRifornimenti);
						}
					}
				}
			}
			//Ticket 0000276
		}
		//Ticket 0000363 Commento inserito in merito alla segnalazione->
//		else{
//			if (copertine.size() > numCopertinePrecedentiPerRifornimenti) {
//				copertine = copertine.subList(0, numCopertinePrecedentiPerRifornimenti);
//			}
//		}
		return copertine;
	}
	
	/**
	 * Ritorna la giacenza di un idtn chiamando la stored proc P_BOLLA.GET_GIACENZA
	 * 
	 * @param Integer codFiegDl
	 * @param Integer codEdicola
	 * @param Integer idtn
	 * @return Long
	 */
	@Override
	public Long getGiacenza(final Integer codFiegDl, final Integer codEdicola, final Integer idtn, final Timestamp dataStorico) {
		HibernateCallback<Long> action = new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				return session.doReturningWork(new FindGiacenzaWork(codFiegDl, codEdicola, idtn, dataStorico));
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	/**
	 * Classe interna per l'esecuzione della function. 
	 * 
	 * @author romanom
	 *
	 */
	private class FindGiacenzaWork implements ReturningWork<Long> {
		private Integer codFiegDl;
		private Integer codEdicola;
		private Integer idtn;
		private Timestamp dataStorico;

		public FindGiacenzaWork(Integer codFiegDl, Integer codEdicola, Integer idtn, Timestamp dataStorico) {
			this.codFiegDl = codFiegDl;
			this.codEdicola = codEdicola;
			this.idtn = idtn;
			this.dataStorico = dataStorico;
		}

		@Override
		public Long execute(Connection connection) throws SQLException {
			Long value = 0l;
			if (codFiegDl != null && codEdicola != null && idtn != null) {
				CallableStatement cs = connection.prepareCall(IGerivQueryContants.SQL_QUERY_GET_GIACENZA); 
				cs.clearParameters(); 
		        cs.registerOutParameter(1, OracleTypes.INTEGER); 
		        cs.setInt(2, codFiegDl); 
		        cs.setInt(3, codEdicola);
		        cs.setInt(4, idtn); 
		        cs.setTimestamp(5, dataStorico);
		        cs.execute(); 
		        value = cs.getLong(1); 
			}
			return value;
		}
	}
	
	
	
	/**
	 * Ritorna la giacenza di un idtn chiamando la stored proc P_BOLLA.GET_FORNITO
	 * 
	 * @param Integer codFiegDl
	 * @param Integer codEdicola
	 * @param Integer idtn
	 * @return Long
	 */
	@Override
	public Long getFornito(final Integer codFiegDl, final Integer codEdicola, final Integer idtn) {
		HibernateCallback<Long> action = new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				return session.doReturningWork(new FindFornitoWork(codFiegDl, codEdicola, idtn));
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	/**
	 * Classe interna per l'esecuzione della function Fornito
	 */
	private class FindFornitoWork implements ReturningWork<Long> {
		private Integer codFiegDl;
		private Integer codEdicola;
		private Integer idtn;

		public FindFornitoWork(Integer codFiegDl, Integer codEdicola, Integer idtn) {
			this.codFiegDl = codFiegDl;
			this.codEdicola = codEdicola;
			this.idtn = idtn;
		}

		@Override
		public Long execute(Connection connection) throws SQLException {
			Long value = 0l;
			if (codFiegDl != null && codEdicola != null && idtn != null) {
				CallableStatement cs = connection.prepareCall(IGerivQueryContants.SQL_QUERY_GET_FORNITO); 
				cs.clearParameters(); 
		        cs.registerOutParameter(1, OracleTypes.INTEGER); 
		        cs.setInt(2, codFiegDl); 
		        cs.setInt(3, codEdicola);
		        cs.setInt(4, idtn); 
		        cs.execute(); 
		        value = cs.getLong(1); 
			}
			return value;
		}
	}
	
	
	
	
	
	
	@Override
	public ContoDepositoVo getPubblicazioneInContoDeposito(Integer codFiegDl, Integer codEdicola, Integer idtn) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ContoDepositoVo.class, "cdvo");
		criteria.add(Restrictions.eq("cdvo.pk.codDl", codFiegDl));
		criteria.add(Restrictions.eq("cdvo.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("cdvo.pk.idtn", idtn));
		criteria.add(Restrictions.gt("cdvo.quantita", 0));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public List<ContoDepositoDto> getPubblicazioniContoDeposito(final Integer[] codFiegDl, 
			final Integer[] codEdicola, final Integer gruppoSconto, final String titolo, final String codBarre) {
		HibernateCallback<List<ContoDepositoDto>> action = new HibernateCallback<List<ContoDepositoDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<ContoDepositoDto> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(ContoDepositoVo.class, "cd");
				criteria.createCriteria("storicoCopertineVo", "sc");
				criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
				criteria.createCriteria("sc.immagine", "im", JoinType.LEFT_OUTER_JOIN);
				criteria.add(Restrictions.in("cd.pk.codDl", codFiegDl));
				criteria.add(Restrictions.in("cd.pk.codEdicola", codEdicola));
				if (titolo != null && !titolo.equals("")) {
					criteria.add(Restrictions.ilike("ap.titolo", titolo, MatchMode.ANYWHERE));
				}
				if (codBarre != null && !codBarre.equals("")) {
					criteria.add(Restrictions.eq("sc.codiceBarre", codBarre));
				}
				ProjectionList properties = Projections.projectionList(); 
				properties.add(Projections.property("sc.dataUscita"), "dataUscita");
				properties.add(Projections.property("sc.codicePubblicazione"), "codicePubblicazione");
				properties.add(Projections.property("sc.numeroCopertina"), "numeroCopertina");
				properties.add(Projections.property("im.nome"), "immagine");
				properties.add(Projections.property("ap.titolo"), "titolo");
				properties.add(Projections.property("cd.quantita"), "quantitaContoDeposito");
				properties.add(Projections.property("cd.resoBolla"), "resoBolla");
				properties.add(Projections.property("cd.resoFuoriVoce"), "resoFuoriVoce");
				properties.add(Projections.property("cd.resoRichiamoPersonalizzato"), "resoRichiamoPersonalizzato");
				properties.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertina");
				properties.add(Projections.property("sc.prezzoEdicola"), "prezzoEdicola");
				properties.add(Projections.property("sc.dataFatturazionePrevista"), "dataFatturazionePrevista");
				criteria.setProjection(properties); 
				criteria.setResultTransformer(Transformers.aliasToBean(ContoDepositoDto.class));
				criteria.addOrder(Order.asc("sc.dataFatturazionePrevista")).addOrder(Order.asc("sc.dataUscita"));
				session.enableFilter("GruppoScontoFilter").setParameter("gruppoSconto", gruppoSconto);
				List<ContoDepositoDto> list = criteria.list();
				session.disableFilter("GruppoScontoFilter");
				return list;
			}
		};
		List<ContoDepositoDto> findByDetachedCriteria = getDao().findByHibernateCallback(action);
		return findByDetachedCriteria;
	}
	
	/**
	 * Aggiunge la where clause per filtrare la pubblicazone fornita dal DL di competenza basandosi sulle bolle di consegna. 
	 * 
	 * @param Integer[] codEdicola
	 * @param Integer[] criteria
	 */
	private void addClausesForMultipleDl(final Integer[] codEdicola, Criteria criteria) {
		DetachedCriteria dc = DetachedCriteria.forClass(BollaDettaglioVo.class, "bd");
		dc.add(Restrictions.eqProperty("bd.pk.codFiegDl", "ap.pk.codFiegDl"));
		dc.add(Restrictions.eqProperty("bd.idtn", "sc.pk.idtn"));
		dc.add(Restrictions.in("bd.pk.codEdicola", codEdicola));
		ProjectionList props = Projections.projectionList();
		props.add(Projections.property("bd.pk.codFiegDl"));
		dc.setProjection(props);
		
		DetachedCriteria dc1 = DetachedCriteria.forClass(FondoBollaDettaglioVo.class, "fbd");
		dc1.add(Restrictions.eqProperty("fbd.pk.codFiegDl", "ap.pk.codFiegDl"));
		dc1.add(Restrictions.eqProperty("fbd.idtn", "sc.pk.idtn"));
		dc1.add(Restrictions.in("fbd.pk.codEdicola", codEdicola));
		dc1.add(Restrictions.eq("fbd.tipoFondoBollaVo.tipoRecordFondoBolla", IGerivConstants.COD_TIPO_FONDO_BOLLA_RIFORNIMENTI));
		ProjectionList props1 = Projections.projectionList();
		props1.add(Projections.property("fbd.pk.codFiegDl"));
		dc1.setProjection(props1);
		
		DetachedCriteria dc2 = DetachedCriteria.forClass(RichiestaRifornimentoVo.class, "rr");
		dc2.add(Restrictions.eqProperty("rr.pk.codFiegDl", "ap.pk.codFiegDl"));
		dc2.add(Restrictions.eqProperty("rr.pk.idtn", "sc.pk.idtn"));
		dc2.add(Restrictions.in("rr.pk.codEdicola", codEdicola));
		dc2.add(Restrictions.eq("rr.flagStato", IGerivConstants.COD_SI));
		ProjectionList props2 = Projections.projectionList();
		props2.add(Projections.property("rr.pk.codFiegDl"));
		dc2.setProjection(props2);
		
		DetachedCriteria dc3 = DetachedCriteria.forClass(BollaStatisticaStoricoVo.class, "ss");
		dc3.add(Restrictions.eqProperty("ss.pk.codFiegDl", "ap.pk.codFiegDl"));
		dc3.add(Restrictions.eqProperty("ss.pk.idtn", "sc.pk.idtn"));
		dc3.add(Restrictions.in("ss.pk.codEdicola", codEdicola));
		ProjectionList props3 = Projections.projectionList();
		props3.add(Projections.property("ss.pk.codFiegDl"));
		dc3.setProjection(props3);
		
		Disjunction disjunction = Restrictions.disjunction();
		disjunction.add(Subqueries.exists(dc));
		disjunction.add(Subqueries.exists(dc1));
		disjunction.add(Subqueries.exists(dc2));
		disjunction.add(Subqueries.exists(dc3));
		criteria.add(disjunction);
	}
	
	@Override
	public List<PubblicazioneDto> getCopertine(final boolean ultimaPubblicazione, final boolean statistiche, final boolean contoDeposito, final Integer codEdicolaMaster, final Integer[] codDl, 
			final Integer[] codEdicola, final String titolo, final String sottotitolo, final String argomento, final String periodicita, 
			final BigDecimal prezzo, final Integer codPubblicazione, final String codBarre, final boolean showOnlyUltimoDistribuito, final Timestamp dataStorico, final Integer gruppoSconto, 
			final boolean showPubblicazioniTuttiDl, final Integer currDlMultiDl,final Integer anagEditori, final Integer agenziaFatturazione, final Boolean isSecondaCintura, final Timestamp dataPartSecCintura) {
		HibernateCallback<List<PubblicazioneDto>> action = new HibernateCallback<List<PubblicazioneDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<PubblicazioneDto> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hint = "";
				String tables = null;
				String lastCondition = "";
				if (codBarre != null && !codBarre.equals("")) {
					tables = "tbl_9607 cop,tbl_9606 pub";
				} else if ((codPubblicazione != null && !codPubblicazione.equals("")) 
						|| (titolo != null && !titolo.equals(""))
						|| (argomento != null && !argomento.equals(""))
						|| (periodicita != null && !periodicita.equals(""))) {
					hint = "/*+ ordered use_nl (tbl_9607) */";
					tables = "tbl_9606 pub,tbl_9607 cop";
					if (ultimaPubblicazione) {
						lastCondition = "and cop.cddt9607 = (select min(sc1_.cddt9607) as y0_ from tbl_9607 sc1_ where sc1_.coddl9607=cop.coddl9607 and sc1_.cpu9607=cop.cpu9607) ";
					}
				} else {
					hint = "/*+ ordered use_nl (tbl_9606) */";
					tables = "tbl_9607 cop,tbl_9606 pub";
				}
				StringBuffer sql = new StringBuffer();
				boolean hasStatistiche = (statistiche || (codDl != null && codDl.length > 1 && currDlMultiDl != null));
				if (hasStatistiche) {
					sql.append("select * from (");
				}
				String sqlPrezzoEdicola = gruppoSconto != null ? "(select t5.prnet9617 from tbl_9617 t5 where t5.coddl9617 = coddl9607 and t5.idtn9617 = idtn9607 and t5.gs9617 = :gruppoSconto) prezzoEdicola," : "";
				sql.append("select " + hint + " pub.titolo9606 as titolo, pub.edi9606 as editore, pub.ciq9606 as codInizioQuotidiano, pub.cfq9606 as codFineQuotidiano, cop.sottot9607 as sottoTitolo, cop.cpu9607 as codicePubblicazione, cop.num9607 as numeroCopertina, cop.barcode9607 as barcode, arg.descr9217 as argomento, per.descr9216 as periodicita, (per.tipop9216 || '|' || perio9216) as periodicitaPk, cop.prezzo9607 as prezzoCopertina, " + sqlPrezzoEdicola + " cop.datausc9607 as dataUscita, img.nomei9700 as immagine, pub.imgnm9606 as imgMiniaturaName, cop.idtn9607 as idtn, cop.dare9607 as dataRichiamoResa, cop.codinf9607 as codiceInforete, cop.numinf9607 as numeroCopertinaInforete, drr.descr9624 as tipoRichiamoResa, nr.noter9640 as note, nr1.noter9641 as noteByCpu, pub.coddl9606 as coddl, editDl.NOMEA9114 as descrEditoreDlNomeA ");
				if (hasStatistiche) { 
					sql.append(",");
					String dataStatisticheBollaCondition = dataStorico != null ? " and (datbc9611 > :dataStorico) " : "";
					String dataStatisticheFondoBollaCondition = dataStorico != null ? " and (datbc9612 > :dataStorico) " : "";
					String dataStatisticheResoRicontratoCondition = dataStorico != null ? " and (datbr9635 > :dataStorico) " : "";
					sql.append("(select nvl(sum(nvl(quant9611,0)),0) from tbl_9611 where coddl9611 = cop.coddl9607 and crivw9611 in (:codEdicola) and idtn9611 = cop.idtn9607 and ivalo9611 <> " + IGerivConstants.INDICATORE_NON_VALORIZZARE + dataStatisticheBollaCondition + ") as fornitoBolla, ");
					sql.append("(select nvl(sum(nvl(quant9612,0)),0) from tbl_9612 where coddl9612 = cop.coddl9607 and crivw9612 in (:codEdicola) and idtn9612 = cop.idtn9607 and trfon9612 in (" + IGerivConstants.LIST_COD_TIPO_FONDO_BOLLA + ") " + dataStatisticheFondoBollaCondition + ") as fornitoFondoBolla, ");
					sql.append("(select nvl(sum(nvl(quaev9131,0)),0) from tbl_9131 where coddl9131 = cop.coddl9607 and crivw9131 in (:codEdicola) and idtn9131 = cop.idtn9607 and flstat9131 = " + IGerivConstants.COD_SI + ") as fornitoRifornimenti, ");
					sql.append("(select nvl(sum(nvl(qreso9621,0)),0) from tbl_9621 where coddl9621 = cop.coddl9607 and crivw9621 in (:codEdicola) and idtn9621 = cop.idtn9607) as resoBolla, ");
					sql.append("(select nvl(sum(nvl(qreso9622,0)),0) from tbl_9622 where coddl9622 = cop.coddl9607 and crivw9622 in (:codEdicola) and idtn9622 = cop.idtn9607) as resoFuoriVoce, ");
					sql.append("(select nvl(sum(nvl(qreso9622b,0)),0) from tbl_9622b where coddl9622b = cop.coddl9607 and crivw9622b in (:codEdicola) and idtn9622b = cop.idtn9607) as resoRichiamoPersonalizzato, ");
					sql.append("(select nvl(sum(nvl(qresor9635,0)),0) from tbl_9635 where coddl9635 = cop.coddl9607 and crivw9635 in (:codEdicola) and idtn9635 = cop.idtn9607 " + dataStatisticheResoRicontratoCondition + ") as resoRiscontrato, ");
					sql.append("(select nvl(sum(nvl(quanti9626,0)),0) from tbl_9626 where coddl9626 = cop.coddl9607 and crivw9626 in (:codEdicola) and idtn9626 = cop.idtn9607) as vendite, ");
					sql.append("(select nvl(sum(nvl(qresor9636,0)),0) from tbl_9636 where coddl9636 = cop.coddl9607 and crivw9636 in (:codEdicola) and idtn9636 = cop.idtn9607) as respinto ");
					if (dataStorico != null) {
						sql.append(", (select nvl(qforn9638,0) from tbl_9638 where coddl9638 = cop.coddl9607 and crivw9638 in (:codEdicola) and idtn9638 = cop.idtn9607) as fornitoStorico, ");
						sql.append("(select nvl(qreso9638,0) from tbl_9638 where coddl9638 = cop.coddl9607 and crivw9638 in (:codEdicola) and idtn9638 = cop.idtn9607) as resoStorico ");
					}
				} 
				if (contoDeposito) {
					sql.append(", cd.quant9618 as contoDeposito ");
				}
				sql.append("from " + tables + " ,tbl_9217 arg,tbl_9216 per,tbl_9700 img,tbl_9624 drr, tbl_9640 nr, tbl_9641 nr1 , tbl_9114 editDl ");
				if (contoDeposito) {
					sql.append(",tbl_9618 cd ");
				}
				sql.append("where ");
				sql.append("cop.coddl9607=pub.coddl9606 ");
				sql.append("and cop.cpu9607=pub.cpu9606 ");
				sql.append("and pub.coddl9606=arg.coddl9217(+) ");
				sql.append("and pub.argodl9606=arg.segm9217(+) ");
				sql.append("and pub.peridl9606=per.perio9216(+) ");
				sql.append("and " + IGerivConstants.TIPO_OPERAZIONE_GESDIS + "=per.tipop9216(+) ");
				sql.append("and cop.barcode9607=img.barcode9700(+) ");	
				sql.append("and cop.coddl9607=drr.coddl9624(+) ");
				sql.append("and cop.tire9607=drr.trr9624(+) ");
				sql.append("and cop.idtn9607=nr.idtn9640(+) ");
				sql.append("and :codEdicolaMaster=nr.crivw9640(+) ");
				sql.append("and cop.cpu9607=nr1.cpu9641(+) ");
				sql.append("and :codEdicolaMaster=nr1.crivw9641(+) ");
				sql.append("and cop.coddl9607 in (:coddl) ");
				sql.append("and pub.coddl9606 in (:coddl) ");
				sql.append("and pub.coddl9606	=	editDl.CODDL9114(+) ");
				sql.append("and pub.edi9606		=	editDl.EDIT9114(+) ");

				
				if (contoDeposito) {
					sql.append("and cop.coddl9607 = cd.coddl9618(+) and cop.idtn9607 = cd.idtn9618(+) and cd.crivw9618(+) in (:codEdicola) ");
				}
				sql.append(lastCondition);
				if (showPubblicazioniTuttiDl && codDl.length > 1 && codEdicola.length > 1) {
					sql.append("and (exists (select null from tbl_9611 where coddl9611 = pub.coddl9606 and crivw9611 in (:codEdicolaList) and idtn9611 = cop.idtn9607) ");
					sql.append("or exists (select null from tbl_9612 where coddl9612 = pub.coddl9606 and crivw9612 in (:codEdicolaList) and idtn9612 = cop.idtn9607 and trfon9612=" + IGerivConstants.COD_TIPO_FONDO_BOLLA_RIFORNIMENTI + ") ");
					sql.append("or exists (select null from tbl_9131 where coddl9131 = pub.coddl9606 and crivw9131 in (:codEdicolaList) and idtn9131 = cop.idtn9607 and flstat9131=" + IGerivConstants.COD_SI + ") ");
					sql.append("or exists (select null from tbl_9638 where coddl9638 = pub.coddl9606 and crivw9638 in (:codEdicolaList) and idtn9638 = cop.idtn9607)) ");
				}
				
				if (!Strings.isNullOrEmpty(titolo)) {
					sql.append("and upper(pub.titolo9606) like :titolo ");
				}
				
				
				
				//String tit = !Strings.isNullOrEmpty(titolo) ? "and upper(ap2_.titolo9606) like '%" + titolo.toUpperCase() + "%'" : "";
				
				
				if (!Strings.isNullOrEmpty(sottotitolo)) {
					sql.append("and upper(cop.sottot9607) like :sottotitolo ");
				}
				if (!Strings.isNullOrEmpty(argomento)) {
					sql.append("and arg.segm9217 = :argomento ");
				}
				if (!Strings.isNullOrEmpty(periodicita)) {
					sql.append("and per.perio9216 = :periodicita ");
				}
				if (prezzo != null) {
					sql.append("and cop.prezzo9607 = :prezzo ");
				}
				if (codPubblicazione != null) {
					sql.append("and cop.cpu9607 = :codPubblicazione ");
				}
				if (!Strings.isNullOrEmpty(codBarre)) {
					sql.append("and cop.barcode9607 = :codBarre ");
				}
				if (anagEditori != null) {
					sql.append("and pub.edi9606 = :anagEditori ");
				}
				sql.append("order by titolo9606, cpu9607, cddt9607, num9607");
				
				if (hasStatistiche) {
					if (showOnlyUltimoDistribuito) {
						sql.append(") where rownum <= 1");
					} else if (!Strings.isNullOrEmpty(periodicita)) {
						String[] arrPk = periodicita.split("\\|");
						// 07/10/2016
						// java.lang.ArrayIndexOutOfBoundsException: 1 
						// at it.dpe.igeriv.bo.pubblicazioni.PubblicazioniRepositoryImpl$7.doInHibernate(PubblicazioniRepositoryImpl.java:677)
						Integer peridicitaInt = null;
						if(arrPk!=null && arrPk.length>0)
							peridicitaInt = new Integer(arrPk[1]);
						
						// 17/11/2015 
						if(codDl.length > 1){
							sql.append(")");
						}else{
							sql.append(") where rownum <= (select decode(masvi9108, 0, " + Integer.MAX_VALUE + ", masvi9108) from tbl_9108 t2 where coddl9108 in (:coddl) and perio9108 = " + peridicitaInt + " and rownum < 2)");
						}
					} else {
						sql.append(")");
					}
				}
				
				SQLQuery sqlQuery = session.createSQLQuery(sql.toString());
				
				sqlQuery.setResultTransformer( Transformers.aliasToBean(PubblicazioneDto.class));
				if (!Strings.isNullOrEmpty(titolo)) {
					sqlQuery.setString("titolo", titolo.toUpperCase() + "%");
				}
				if (!Strings.isNullOrEmpty(sottotitolo)) {
					sqlQuery.setString("sottotitolo", sottotitolo.toUpperCase() + "%");
				}
				if (!Strings.isNullOrEmpty(argomento)) {
					String[] arrPk = argomento.split("\\|");
					sqlQuery.setInteger("argomento", new Integer(arrPk[1]));
				}
				if (!Strings.isNullOrEmpty(periodicita)) {
					String[] arrPk = periodicita.split("\\|");
					sqlQuery.setInteger("periodicita", new Integer(arrPk[1]));
				}
				if (prezzo != null) {
					sqlQuery.setBigDecimal("prezzo", prezzo);
				}
				if (codPubblicazione != null) {
					sqlQuery.setInteger("codPubblicazione", codPubblicazione);
				}
				if (!Strings.isNullOrEmpty(codBarre)) {
					sqlQuery.setString("codBarre", codBarre);
				}
				if (anagEditori != null) {
					sqlQuery.setInteger("anagEditori", anagEditori);
				}
				
				sqlQuery.setParameterList("coddl", codDl);
				if (hasStatistiche || contoDeposito) {
					sqlQuery.setParameterList("codEdicola", codEdicola);
				}
				if (showPubblicazioniTuttiDl && codDl.length > 1 && codEdicola.length > 1) {
					sqlQuery.setParameterList("codEdicolaList", codEdicola);
				}
				sqlQuery.setInteger("codEdicolaMaster", codEdicolaMaster);
				if (hasStatistiche && dataStorico != null) {
					sqlQuery.setTimestamp("dataStorico", dataStorico);
				}
				if (gruppoSconto != null) {
					sqlQuery.setInteger("gruppoSconto", gruppoSconto);
				}
				sqlQuery.addScalar("titolo", StringType.INSTANCE);
				sqlQuery.addScalar("editore", IntegerType.INSTANCE);
				sqlQuery.addScalar("codInizioQuotidiano", IntegerType.INSTANCE);
				sqlQuery.addScalar("codFineQuotidiano", IntegerType.INSTANCE);
				sqlQuery.addScalar("sottoTitolo", StringType.INSTANCE);
				sqlQuery.addScalar("codicePubblicazione", IntegerType.INSTANCE);
				sqlQuery.addScalar("numeroCopertina", StringType.INSTANCE);
				sqlQuery.addScalar("barcode", StringType.INSTANCE);
				sqlQuery.addScalar("argomento", StringType.INSTANCE);
				sqlQuery.addScalar("periodicita", StringType.INSTANCE);
				sqlQuery.addScalar("periodicitaPk", StringType.INSTANCE);
				sqlQuery.addScalar("prezzoCopertina", BigDecimalType.INSTANCE);
				if (gruppoSconto != null) {
					sqlQuery.addScalar("prezzoEdicola", BigDecimalType.INSTANCE);
				}
				sqlQuery.addScalar("dataUscita", TimestampType.INSTANCE);
				sqlQuery.addScalar("immagine", StringType.INSTANCE);
				sqlQuery.addScalar("imgMiniaturaName", StringType.INSTANCE);
				sqlQuery.addScalar("idtn", IntegerType.INSTANCE);
				sqlQuery.addScalar("dataRichiamoResa", TimestampType.INSTANCE);
				sqlQuery.addScalar("tipoRichiamoResa", StringType.INSTANCE);
				sqlQuery.addScalar("coddl", IntegerType.INSTANCE);			
				if (hasStatistiche) {
					sqlQuery.addScalar("fornitoBolla", IntegerType.INSTANCE);
					sqlQuery.addScalar("fornitoFondoBolla", IntegerType.INSTANCE);
					sqlQuery.addScalar("fornitoRifornimenti", IntegerType.INSTANCE);
					sqlQuery.addScalar("resoBolla", IntegerType.INSTANCE);
					sqlQuery.addScalar("resoFuoriVoce", IntegerType.INSTANCE);
					sqlQuery.addScalar("resoRichiamoPersonalizzato", IntegerType.INSTANCE);
					sqlQuery.addScalar("resoRiscontrato", IntegerType.INSTANCE);
					sqlQuery.addScalar("vendite", LongType.INSTANCE);
					sqlQuery.addScalar("respinto", IntegerType.INSTANCE);
					if (dataStorico != null) {
						sqlQuery.addScalar("fornitoStorico", IntegerType.INSTANCE);
						sqlQuery.addScalar("resoStorico", IntegerType.INSTANCE);
					}
				}
				if (contoDeposito) {
					sqlQuery.addScalar("contoDeposito", IntegerType.INSTANCE);
				}
				sqlQuery.addScalar("note", StringType.INSTANCE);
				sqlQuery.addScalar("noteByCpu", StringType.INSTANCE);
				sqlQuery.addScalar("descrEditoreDlNomeA", StringType.INSTANCE);
				
				List<PubblicazioneDto> list = sqlQuery.list();
				return list;
			}
		};
		List<PubblicazioneDto> copertine = getDao().findByHibernateCallback(action);
		boolean isEdicolaDeviettiTodis = currDlMultiDl != null && (currDlMultiDl.equals(IGerivConstants.COD_FIEG_DL_DEVIETTI) || currDlMultiDl.equals(IGerivConstants.COD_FIEG_DL_TODIS));
		if ((codDl.length > 1 && currDlMultiDl != null) || isEdicolaDeviettiTodis) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("codFiegDl", currDlMultiDl);
			m.put("agenziaFatturazione", agenziaFatturazione);
			m.put("isSecondaCintura", isSecondaCintura);
			m.put("dataPartSecCintura", dataPartSecCintura);
			if (!Strings.isNullOrEmpty(periodicita)) {
				m.put("maxRows", getMaxRows(codDl, periodicita));
			}
			pubblicazioneListFilter.filter(copertine, m);
		}
		return copertine;
	}
	
	
	public List<PubblicazioneDto> getCopertineByDL(final boolean ultimaPubblicazione,final Integer codDl, final String titolo, final String sottotitolo, final String argomento, 
			final String periodicita, final BigDecimal prezzo, final Integer codPubblicazione, final String codBarre){
		
		HibernateCallback<List<PubblicazioneDto>> action = new HibernateCallback<List<PubblicazioneDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<PubblicazioneDto> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hint = "";
				String tables = null;
				String lastCondition = "";
				if (codBarre != null && !codBarre.equals("")) {
					tables = "tbl_9607 cop,tbl_9606 pub";
				} else if ((codPubblicazione != null && !codPubblicazione.equals("")) 
						|| (titolo != null && !titolo.equals(""))
						|| (argomento != null && !argomento.equals(""))
						|| (periodicita != null && !periodicita.equals(""))) {
					hint = "/*+ ordered use_nl (tbl_9607) */";
					tables = "tbl_9606 pub,tbl_9607 cop";
					if (ultimaPubblicazione) {
						lastCondition = "and cop.cddt9607 = (select min(sc1_.cddt9607) as y0_ from tbl_9607 sc1_ where sc1_.coddl9607=cop.coddl9607 and sc1_.cpu9607=cop.cpu9607) ";
					}
				} else {
					hint = "/*+ ordered use_nl (tbl_9606) */";
					tables = "tbl_9607 cop,tbl_9606 pub";
				}
				StringBuffer sql = new StringBuffer();
				
				String sqlPrezzoEdicola = "";
				sql.append("select " + hint + " pub.titolo9606 as titolo, pub.ciq9606 as codInizioQuotidiano, pub.cfq9606 as codFineQuotidiano, cop.sottot9607 as sottoTitolo, cop.cpu9607 as codicePubblicazione, cop.num9607 as numeroCopertina, "
						+ "	cop.barcode9607 as barcode, cop.dtabar9607 as dataCorrezioneBarcode, cop.cribar9607 as codEdicolaCorrezioneBarcode, cop.barcodep9607 as codiceBarrePrecedente, "
						+ " arg.descr9217 as argomento, per.descr9216 as periodicita, (per.tipop9216 || '|' || perio9216) as periodicitaPk, cop.prezzo9607 as prezzoCopertina, " + sqlPrezzoEdicola + " cop.datausc9607 as dataUscita, img.nomei9700 as immagine, pub.imgnm9606 as imgMiniaturaName, cop.idtn9607 as idtn, cop.dare9607 as dataRichiamoResa, cop.codinf9607 as codiceInforete, cop.numinf9607 as numeroCopertinaInforete, drr.descr9624 as tipoRichiamoResa, pub.coddl9606 as coddl ");
				
				sql.append("from " + tables + " ,tbl_9217 arg,tbl_9216 per,tbl_9700 img,tbl_9624 drr ");
				sql.append("where ");
				sql.append("cop.coddl9607=pub.coddl9606 ");
				sql.append("and cop.cpu9607=pub.cpu9606 ");
				sql.append("and pub.coddl9606=arg.coddl9217(+) ");
				sql.append("and pub.argodl9606=arg.segm9217(+) ");
				sql.append("and pub.peridl9606=per.perio9216(+) ");
				sql.append("and " + IGerivConstants.TIPO_OPERAZIONE_GESDIS + "=per.tipop9216(+) ");
				sql.append("and cop.barcode9607=img.barcode9700(+) ");	
				sql.append("and cop.coddl9607=drr.coddl9624(+) ");
				sql.append("and cop.tire9607=drr.trr9624(+) ");
				//sql.append("and cop.idtn9607=nr.idtn9640(+) ");
				//sql.append("and :codEdicolaMaster=nr.crivw9640(+) ");
				//sql.append("and cop.cpu9607=nr1.cpu9641(+) ");
				//sql.append("and :codEdicolaMaster=nr1.crivw9641(+) ");
				sql.append("and cop.coddl9607 in (:coddl) ");
				sql.append("and pub.coddl9606 in (:coddl) ");
				
				sql.append(lastCondition);
								
				if (!Strings.isNullOrEmpty(titolo)) {
					sql.append("and upper(pub.titolo9606) like :titolo ");
				}
				if (!Strings.isNullOrEmpty(sottotitolo)) {
					sql.append("and upper(cop.sottot9607) like :sottotitolo ");
				}
				if (!Strings.isNullOrEmpty(argomento)) {
					sql.append("and arg.segm9217 = :argomento ");
				}
				if (!Strings.isNullOrEmpty(periodicita)) {
					sql.append("and per.perio9216 = :periodicita ");
				}
				if (prezzo != null) {
					sql.append("and cop.prezzo9607 = :prezzo ");
				}
				if (codPubblicazione != null) {
					sql.append("and cop.cpu9607 = :codPubblicazione ");
				}
				if (!Strings.isNullOrEmpty(codBarre)) {
					sql.append("and cop.barcode9607 = :codBarre ");
				}
				sql.append("order by titolo9606, cpu9607, cddt9607, num9607");
				
							
				SQLQuery sqlQuery = session.createSQLQuery(sql.toString());
				
				sqlQuery.setResultTransformer( Transformers.aliasToBean(PubblicazioneDto.class));
				if (!Strings.isNullOrEmpty(titolo)) {
					sqlQuery.setString("titolo", titolo.toUpperCase() + "%");
				}
				if (!Strings.isNullOrEmpty(sottotitolo)) {
					sqlQuery.setString("sottotitolo", sottotitolo.toUpperCase() + "%");
				}
				if (!Strings.isNullOrEmpty(argomento)) {
					String[] arrPk = argomento.split("\\|");
					sqlQuery.setInteger("argomento", new Integer(arrPk[1]));
				}
				if (!Strings.isNullOrEmpty(periodicita)) {
					String[] arrPk = periodicita.split("\\|");
					sqlQuery.setInteger("periodicita", new Integer(arrPk[1]));
				}
				if (prezzo != null) {
					sqlQuery.setBigDecimal("prezzo", prezzo);
				}
				if (codPubblicazione != null) {
					sqlQuery.setInteger("codPubblicazione", codPubblicazione);
				}
				if (!Strings.isNullOrEmpty(codBarre)) {
					sqlQuery.setString("codBarre", codBarre);
				}
				sqlQuery.setParameter("coddl", codDl);
				
				
				
				sqlQuery.addScalar("titolo", StringType.INSTANCE);
				sqlQuery.addScalar("codInizioQuotidiano", IntegerType.INSTANCE);
				sqlQuery.addScalar("codFineQuotidiano", IntegerType.INSTANCE);
				sqlQuery.addScalar("sottoTitolo", StringType.INSTANCE);
				sqlQuery.addScalar("codicePubblicazione", IntegerType.INSTANCE);
				sqlQuery.addScalar("numeroCopertina", StringType.INSTANCE);
				
				sqlQuery.addScalar("barcode", StringType.INSTANCE);
				sqlQuery.addScalar("dataCorrezioneBarcode", TimestampType.INSTANCE);
				sqlQuery.addScalar("codEdicolaCorrezioneBarcode", IntegerType.INSTANCE);
				sqlQuery.addScalar("codiceBarrePrecedente", StringType.INSTANCE);
				
				sqlQuery.addScalar("argomento", StringType.INSTANCE);
				sqlQuery.addScalar("periodicita", StringType.INSTANCE);
				sqlQuery.addScalar("periodicitaPk", StringType.INSTANCE);
				sqlQuery.addScalar("prezzoCopertina", BigDecimalType.INSTANCE);
				sqlQuery.addScalar("dataUscita", TimestampType.INSTANCE);
				sqlQuery.addScalar("immagine", StringType.INSTANCE);
				sqlQuery.addScalar("imgMiniaturaName", StringType.INSTANCE);
				sqlQuery.addScalar("idtn", IntegerType.INSTANCE);
				sqlQuery.addScalar("dataRichiamoResa", TimestampType.INSTANCE);
				sqlQuery.addScalar("tipoRichiamoResa", StringType.INSTANCE);
				sqlQuery.addScalar("coddl", IntegerType.INSTANCE);			
				//sqlQuery.addScalar("note", StringType.INSTANCE);
				//sqlQuery.addScalar("noteByCpu", StringType.INSTANCE);
				List<PubblicazioneDto> list = sqlQuery.list();
				return list;
			
				}
			};
			
			List<PubblicazioneDto> copertine = getDao().findByHibernateCallback(action);
			return copertine;
	}
	
	
	
	/**
	 * Ritorna il max rownum della query precedente da passare al filtro 
	 * per limitare i risultati nel caso di doppio dl e pubblicazione fornito da un unico dl 
	 * 
	 * @param codDl
	 * @param periodicita
	 * @return
	 */
	private Integer getMaxRows(final Integer[] codDl, final String periodicita) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				Integer peridicitaInt = new Integer(periodicita.split("\\|")[1]);
				SQLQuery sqlQuery = session.createSQLQuery("select decode(masvi9108, 0, " + Integer.MAX_VALUE + ", masvi9108) from tbl_9108 t2 where coddl9108 in (:coddl) and perio9108 = " + peridicitaInt + " and rownum < 2");
				sqlQuery.setParameterList("coddl", codDl);
				return ((BigDecimal) sqlQuery.uniqueResult()).intValue();
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}

	@Override
	public List<PubblicazioneDto> getCopertineByTitoloBarcodeCpu(final Integer[] codDl, final Integer[] codEdicola, final String titolo, final String codBarre, 
			final boolean soloUltimeCopertine, final boolean soloPubblicazioniBarcodeNullo, final Integer cpu, final Integer gruppoSconto, final Boolean findCopieInContoDeposito,
			final Boolean findGiacenza, final Timestamp dataStorico, final Boolean dlInforiv) {
		HibernateCallback<List<PubblicazioneDto>> action = new HibernateCallback<List<PubblicazioneDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<PubblicazioneDto> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = null;
				if (!Strings.isNullOrEmpty(titolo) || cpu != null) {
					criteria = session.createCriteria(AnagraficaPubblicazioniVo.class, "ap");
					criteria.createCriteria("ap.storicoCopertineVo", "sc");
					criteria.createCriteria("sc.motivoResaRespinta", "mr", JoinType.LEFT_OUTER_JOIN);
					criteria.createCriteria("ap.limitiPeriodicitaVo", "lp", JoinType.LEFT_OUTER_JOIN);
					criteria.createCriteria("ap.periodicitaVo", "per", JoinType.LEFT_OUTER_JOIN);
					if (!Strings.isNullOrEmpty(titolo)) {
						criteria.add(Restrictions.ilike("ap.titolo", titolo.toUpperCase(), MatchMode.START));
					}
					if (cpu != null) {
						criteria.add(Restrictions.eq("ap.pk.codicePubblicazione", cpu));
					}
					criteria.add(Restrictions.in("ap.pk.codFiegDl", codDl));
				} else if (!Strings.isNullOrEmpty(codBarre)) {
					criteria = session.createCriteria(StoricoCopertineVo.class, "sc");
					criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
					criteria.createCriteria("sc.motivoResaRespinta", "mr", JoinType.LEFT_OUTER_JOIN);
					criteria.createCriteria("sc.immagine", "im", JoinType.LEFT_OUTER_JOIN);
					criteria.createCriteria("ap.limitiPeriodicitaVo", "lp", JoinType.LEFT_OUTER_JOIN);
					criteria.createCriteria("ap.periodicitaVo", "per", JoinType.LEFT_OUTER_JOIN);
					if (!Strings.isNullOrEmpty(codBarre)) {
						criteria.add(Restrictions.eq("sc.codiceBarre", codBarre));
					}
					criteria.add(Restrictions.in("sc.pk.codDl", codDl));
				}
				if (soloUltimeCopertine) {
					DetachedCriteria subCriteria = DetachedCriteria.forClass(StoricoCopertineVo.class, "sc1");
					subCriteria.add(Restrictions.eqProperty("sc1.pk.codDl", "ap.pk.codFiegDl"));
					subCriteria.add(Restrictions.eqProperty("sc1.codicePubblicazione", "ap.pk.codicePubblicazione"));
					//Add 02/09/2014 Form vendite - Ricerca testuale della pubblicazione - bug:Visualizza anche le pubblicazioni di domani
					subCriteria.add(Restrictions.le("sc1.dataUscita", DateUtilities.floorDay(new Date())));
					
					ProjectionList props = Projections.projectionList();
					props.add(Projections.max("sc1.dataUscita"));
					subCriteria.setProjection(props);
					criteria.add(Subqueries.propertyEq("sc.dataUscita", subCriteria));
					
				}
				if (soloPubblicazioniBarcodeNullo) {
					criteria.add(Restrictions.sqlRestriction(" trim(barcode9607) is null "));
				}
				if (dlInforiv == null || !dlInforiv && (codDl.length > 1 && codEdicola.length > 1)) {
					addClausesForMultipleDl(codEdicola, criteria);
				}
				criteria.addOrder(Order.desc("sc.dataUscita"));
				
				ProjectionList properties = Projections.projectionList();
				properties.add(Projections.property("ap.pk.codFiegDl"), "coddl"); 
				properties.add(Projections.property("ap.titolo"), "titolo"); 
				properties.add(Projections.property("sc.sottoTitolo"), "sottoTitolo"); 
				properties.add(Projections.property("sc.numeroCopertina"), "numeroCopertina"); 
				properties.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertina");
				if (gruppoSconto != null) {
					properties.add(Projections.property("sc.prezzoEdicola"), "prezzoEdicola");
				}
				if (findCopieInContoDeposito != null && findCopieInContoDeposito) {
					properties.add(Projections.property("sc.quantitaCopieContoDeposito"), "quantitaCopieContoDeposito");
				}
				if (findGiacenza != null && findGiacenza) {
					properties.add(Projections.property("sc.giacenza"), "giacenzaSP");
				}
				properties.add(Projections.property("mr.pk.codMotivoRespinto"), "codMotivoRespinto");
				properties.add(Projections.property("lp.numGiorniDaDataUscitaPerRichiestaRifornimento"), "numGiorniDaDataUscitaPerRichiestaRifornimento");
				properties.add(Projections.property("per.pk.periodicita"), "periodicitaInt");
				properties.add(Projections.property("per.pk.tipo"), "tipo");
				properties.add(Projections.property("sc.dataUscita"), "dataUscita");
				properties.add(Projections.property("sc.pk.idtn"), "idtn");
				criteria.setProjection(properties); 
				criteria.setResultTransformer(Transformers.aliasToBean(PubblicazioneDto.class));
				if (gruppoSconto != null) {
					session.enableFilter("GruppoScontoFilter").setParameter("gruppoSconto", gruppoSconto);
				}
				if ((findCopieInContoDeposito != null && findCopieInContoDeposito) || (findGiacenza != null && findGiacenza)) {
					Integer codEdicola1 = codEdicola[0];
					Integer codEdicola2 = codEdicola.length > 1 ? codEdicola[1] : codEdicola1;
					session.enableFilter("FornitoFilter").setParameter("codEdicola", codEdicola1).setParameter("codEdicola2", codEdicola2);
					session.enableFilter("StoricoFilter").setParameter("dataStorico", dataStorico);
				}
				List<PubblicazioneDto> list = criteria.list();
				if (gruppoSconto != null) {
					session.disableFilter("GruppoScontoFilter");
				}
				if ((findCopieInContoDeposito != null && findCopieInContoDeposito) || (findGiacenza != null && findGiacenza)) {
					session.disableFilter("FornitoFilter");
					session.disableFilter("StoricoFilter");
				}
				return list;
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public List<PubblicazioneDto> getPubblicazioniByTitoloCpu(final Integer codDl, final String titolo, final Integer cpu) {
		HibernateCallback<List<PubblicazioneDto>> action = new HibernateCallback<List<PubblicazioneDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<PubblicazioneDto> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(AnagraficaPubblicazioniVo.class, "ap");
				if (!Strings.isNullOrEmpty(titolo)) {
					criteria.add(Restrictions.ilike("ap.titolo", titolo.toUpperCase(), MatchMode.START));
				}
				if (cpu != null) {
					criteria.add(Restrictions.eq("ap.pk.codicePubblicazione", cpu));
				}
				criteria.add(Restrictions.eq("ap.pk.codFiegDl", codDl));
				criteria.addOrder(Order.asc("ap.titolo"));
				ProjectionList properties = Projections.projectionList();
				properties.add(Projections.property("ap.pk.codFiegDl"), "coddl"); 
				properties.add(Projections.property("ap.titolo"), "titolo"); 
				properties.add(Projections.property("ap.pk.codicePubblicazione"), "codicePubblicazione");
				criteria.setProjection(properties);
				criteria.setResultTransformer(Transformers.aliasToBean(PubblicazioneDto.class));
				
				return criteria.list();
			}
		};
		
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public AnagraficaPubblicazioniVo getAnagraficaPubblicazioneByPk(Integer codFiegDl, Integer codPubblicazioneInt) {
		DetachedCriteria criteria = DetachedCriteria.forClass(StoricoCopertineVo.class, "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
		criteria.createCriteria("ap.limitiPeriodicitaVo", "lp");
		criteria.add(Restrictions.eq("ap.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("ap.pk.codicePubblicazione", codPubblicazioneInt));
		StoricoCopertineVo copertina = getDao().findUniqueResultByDetachedCriteria(criteria);
		return (copertina != null) ? copertina.getAnagraficaPubblicazioniVo() : null; 
	}
	
	@Override
	public List<AnagraficaPubblicazioniVo> getQuotidianoByTitolo(String titolo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AnagraficaPubblicazioniVo.class, "ap");
		criteria.createCriteria("ap.periodicitaVo", "per");
		criteria.add(Restrictions.eq("per.pk.periodicita", IGerivConstants.PERIODICITA_QUOTIDIANO));
		criteria.add(Restrictions.ilike("ap.titolo", titolo, MatchMode.ANYWHERE));
		criteria.add(Restrictions.or(Restrictions.isNull("ap.imgMiniaturaName"), Restrictions.ilike("ap.imgMiniaturaName", "_fake", MatchMode.ANYWHERE)));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<AnagraficaPubblicazioniVo> getPeriodicoByTitolo(String titolo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AnagraficaPubblicazioniVo.class, "ap");
		criteria.createCriteria("ap.periodicitaVo", "per");
		criteria.add(Restrictions.ne("per.pk.periodicita", IGerivConstants.PERIODICITA_QUOTIDIANO));
		criteria.add(Restrictions.ilike("ap.titolo", titolo, MatchMode.ANYWHERE));
		criteria.add(Restrictions.or(Restrictions.isNull("ap.imgMiniaturaName"), Restrictions.ilike("ap.imgMiniaturaName", "_fake", MatchMode.ANYWHERE)));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<AnagraficaPubblicazioniVo> getListAnagraficaPubblicazioneByCodQuotidiano(Integer codFiegDl, Integer codInizioQuotidiano, Integer codFineQuotidiano) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AnagraficaPubblicazioniVo.class, "ap");
		criteria.add(Restrictions.eq("ap.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("ap.codInizioQuotidiano", codInizioQuotidiano));
		criteria.add(Restrictions.eq("ap.codFineQuotidiano", codFineQuotidiano));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<PubblicazionePiuVendutaDto> getListPubblicazioniPiuVendute(final Integer codFiegDl, final Integer codEdicola, 
			final Integer tipoPubblicazione, final Integer periodo) {
		List<PubblicazionePiuVendutaDto> listPubblicazionePiuVendutaDto = getPubblicazioniPiuVendute(codFiegDl, codEdicola, tipoPubblicazione, periodo);
		if (!listPubblicazionePiuVendutaDto.isEmpty()) {
			forEach(listPubblicazionePiuVendutaDto).setTipoImmagine(IGerivConstants.COD_TIPO_IMMAGINE_MINIATURA);
		}
		for (PubblicazionePiuVendutaDto pdto : listPubblicazionePiuVendutaDto) {
			DetachedCriteria criteria = DetachedCriteria.forClass(StoricoCopertineVo.class, "sc");
			criteria.createCriteria("sc.immagine", "im", JoinType.LEFT_OUTER_JOIN);
			criteria.add(Restrictions.eq("sc.pk.codDl", codFiegDl));
			criteria.add(Restrictions.eq("sc.codicePubblicazione", pdto.getCodicePubblicazione()));
			
			DetachedCriteria subCriteria = DetachedCriteria.forClass(StoricoCopertineVo.class, "sc1");
			subCriteria.add(Restrictions.eq("sc1.pk.codDl", codFiegDl));
			subCriteria.add(Restrictions.eq("sc1.codicePubblicazione", pdto.getCodicePubblicazione()));
			ProjectionList properties = Projections.projectionList();
			properties.add(Projections.max("sc1.dataUscita"));
			subCriteria.setProjection(properties);
			
			criteria.add(Subqueries.propertyEq("sc.dataUscita", subCriteria));
			
			ProjectionList props = Projections.projectionList(); 
			props.add(Projections.property("im.nome"), "nomeImmagine");
			criteria.setProjection(props);
			
			String nomeImgUltimaCopertina = getDao().findUniqueResultObjectByDetachedCriteria(criteria);
			pdto.setImmagineUltimaCopertina(nomeImgUltimaCopertina);
		}
		return listPubblicazionePiuVendutaDto;
	}

	@Override
	public PubblicazionePiuVendutaDto getPubblicazionePiuVendutaByCpu(Integer[] codFiegDl, Integer[] codEdicola, Integer cpu) {
		DetachedCriteria criteria = DetachedCriteria.forClass(StoricoCopertineVo.class, "sc");
		criteria.createCriteria("sc.immagine", "im", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
		criteria.createCriteria("ap.periodicitaVo", "per", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.in("sc.pk.codDl", codFiegDl));
		criteria.add(Restrictions.eq("sc.codicePubblicazione", cpu));
		
		DetachedCriteria subCriteria = DetachedCriteria.forClass(StoricoCopertineVo.class, "sc1");
		subCriteria.add(Restrictions.eqProperty("sc1.pk.codDl", "sc.pk.codDl"));
		subCriteria.add(Restrictions.eqProperty("sc1.codicePubblicazione", "sc.codicePubblicazione"));
		ProjectionList props = Projections.projectionList();
		props.add(Projections.max("sc1.dataUscita"));
		subCriteria.setProjection(props);
		criteria.add(Subqueries.propertyEq("sc.dataUscita", subCriteria));
		
		DetachedCriteria subCriteria1 = DetachedCriteria.forClass(BarraSceltaRapidaSinistraVo.class, "bsr");
		subCriteria1.add(Property.forName("bsr.pk.codFiegDl").eqProperty("sc.pk.codDl"));
		subCriteria1.add(Property.forName("bsr.pk.codEdicola").in(codEdicola));
		subCriteria1.add(Property.forName("bsr.pk.codicePubblicazione").eqProperty("sc.codicePubblicazione"));
		criteria.add(Subqueries.notExists(subCriteria1.setProjection(Projections.property("bsr.pk.codFiegDl"))));
		
		DetachedCriteria subCriteria2 = DetachedCriteria.forClass(BarraSceltaRapidaDestraVo.class, "bsr");
		subCriteria2.add(Property.forName("bsr.pk.codFiegDl").eqProperty("sc.pk.codDl"));
		subCriteria2.add(Property.forName("bsr.pk.codEdicola").in(codEdicola));
		subCriteria2.add(Property.forName("bsr.pk.codicePubblicazione").eqProperty("sc.codicePubblicazione"));
		criteria.add(Subqueries.notExists(subCriteria2.setProjection(Projections.property("bsr.pk.codFiegDl"))));
		
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("ap.pk.codicePubblicazione"), "codicePubblicazione");
		properties.add(Projections.property("ap.codInizioQuotidiano"), "codInizioQuotidiano");
		properties.add(Projections.property("ap.codFineQuotidiano"), "codFineQuotidiano");
		properties.add(Projections.property("ap.titolo"), "titolo");
		properties.add(Projections.property("ap.imgMiniaturaName"), "nomeImmagine");
		properties.add(Projections.property("im.nome"), "immagineUltimaCopertina");
		properties.add(Projections.property("sc.codiceBarre"), "barcode");
		properties.add(Projections.property("per.pk.periodicita"), "codPeriodicita");
		properties.add(Projections.property("sc.pk.codDl"), "codFiegDl");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(PubblicazionePiuVendutaDto.class));
				
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	/**
	 * Ritorna le pubblicazioni più vendute di una rivendita in un periodo di giorni.
	 * 
	 * @param Integer codFiegDl
	 * @param Integer codEdicola
	 * @param Integer tipoPubblicazione
	 * @param Integer periodo
	 * @return List<PubblicazionePiuVendutaDto>
	 */
	private List<PubblicazionePiuVendutaDto> getPubblicazioniPiuVendute(final Integer codFiegDl, final Integer codEdicola, final Integer tipoPubblicazione, final Integer periodo) {
		HibernateCallback<List<PubblicazionePiuVendutaDto>> action = new HibernateCallback<List<PubblicazionePiuVendutaDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<PubblicazionePiuVendutaDto> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(VenditaDettaglioVo.class, "vd");
				criteria.createCriteria("vd.storicoCopertineVo", "sc");
				criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
				criteria.createCriteria("ap.periodicitaVo", "per");
				criteria.createCriteria("vd.venditaVo", "ven");
				criteria.add(Restrictions.or(Restrictions.eq("vd.deleted", false), Restrictions.isNull("vd.deleted")));
				criteria.add(Restrictions.or(Restrictions.eq("ven.deleted", false), Restrictions.isNull("ven.deleted")));
				criteria.add(Restrictions.eq("ven.codFiegDl", codFiegDl));
				criteria.add(Restrictions.eq("vd.codEdicola", codEdicola));
				if (tipoPubblicazione != null && tipoPubblicazione.equals(IGerivConstants.TIPO_PUBBLICAZIONE_QUOTIDIANO)) {
					criteria.add(Restrictions.eq("per.pk.tipo", IGerivConstants.COD_TIPO_PERIODICITA_QUOTIDIANO));
					criteria.add(Restrictions.eq("per.pk.periodicita", IGerivConstants.COD_PERIODICITA_QUOTIDIANO));
				} else if (tipoPubblicazione != null && tipoPubblicazione.equals(IGerivConstants.TIPO_PUBBLICAZIONE_PERIODICO)){
					criteria.add(Restrictions.ne("per.pk.periodicita", IGerivConstants.COD_PERIODICITA_QUOTIDIANO));
				}
				if (periodo != null) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					cal.add(Calendar.DAY_OF_MONTH, -periodo);
					criteria.add(Restrictions.between("ven.dataVendita", DateUtilities.floorDay(cal.getTime()), DateUtilities.ceilDay(getDao().getSysdate())));
				}
				DetachedCriteria subCriteria = DetachedCriteria.forClass(BarraSceltaRapidaSinistraVo.class, "bsr");
				subCriteria.add(Property.forName("bsr.pk.codFiegDl").eqProperty("vd.codFiegDl"));
				subCriteria.add(Property.forName("bsr.pk.codEdicola").eqProperty("vd.codEdicola"));
				subCriteria.add(Property.forName("bsr.pk.codicePubblicazione").eqProperty("ap.pk.codicePubblicazione"));
				criteria.add(Subqueries.notExists(subCriteria.setProjection(Projections.property("bsr.pk.codFiegDl"))));
				
				DetachedCriteria subCriteria1 = DetachedCriteria.forClass(BarraSceltaRapidaDestraVo.class, "bsr");
				subCriteria1.add(Property.forName("bsr.pk.codFiegDl").eqProperty("vd.codFiegDl"));
				subCriteria1.add(Property.forName("bsr.pk.codEdicola").eqProperty("vd.codEdicola"));
				subCriteria1.add(Property.forName("bsr.pk.codicePubblicazione").eqProperty("ap.pk.codicePubblicazione"));
				criteria.add(Subqueries.notExists(subCriteria1.setProjection(Projections.property("bsr.pk.codFiegDl"))));
				
				ProjectionList properties = Projections.projectionList();
				properties.add(Projections.groupProperty("ap.pk.codicePubblicazione"), "codicePubblicazione");
				properties.add(Projections.groupProperty("ap.pk.codFiegDl"), "codFiegDl");
				properties.add(Projections.groupProperty("ap.titolo"), "titolo");
				properties.add(Projections.groupProperty("ap.codInizioQuotidiano"), "codInizioQuotidiano");
				properties.add(Projections.groupProperty("ap.codFineQuotidiano"), "codFineQuotidiano");
				properties.add(Projections.groupProperty("per.pk.periodicita"), "codPeriodicita");
				properties.add(Projections.groupProperty("ap.imgMiniaturaName"), "nomeImmagine");
				properties.add(Projections.sum("vd.quantita"), "quantita");
				criteria.setProjection(properties); 
				ProjectionOrder orderSumQuantita = new ProjectionOrder("sum", "vd.quantita", false);
				criteria.addOrder(orderSumQuantita);
				criteria.setResultTransformer(Transformers.aliasToBean(PubblicazionePiuVendutaDto.class));
				criteria.setMaxResults(pubblicazioniPiuVenduteMaxResults);
				return criteria.list();
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public Set<Integer> getCpuBarreSceltaRapidaPubblicazioni(Integer[] codFiegDl, Integer[] codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BarraSceltaRapidaSinistraVo.class, "bsr");
		criteria.createCriteria("bsr.anagraficaPubblicazioniVo", "ap");
		criteria.add(Restrictions.in("bsr.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.in("bsr.pk.codEdicola", codEdicola));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("ap.pk.codicePubblicazione"));
		criteria.setProjection(properties);
		
		DetachedCriteria criteria1 = DetachedCriteria.forClass(BarraSceltaRapidaDestraVo.class, "bsr");
		criteria1.createCriteria("bsr.anagraficaPubblicazioniVo", "ap");
		criteria1.add(Restrictions.in("bsr.pk.codFiegDl", codFiegDl));
		criteria1.add(Restrictions.in("bsr.pk.codEdicola", codEdicola));
		ProjectionList properties1 = Projections.projectionList();
		properties1.add(Projections.property("ap.pk.codicePubblicazione"));
		criteria1.setProjection(properties1);
		
		List<Integer> listSx = getDao().findObjectByDetachedCriteria(criteria);
		List<Integer> listDx = getDao().findObjectByDetachedCriteria(criteria1);
		Set<Integer> cpuSx = new HashSet<Integer>(listSx);
		Set<Integer> cpuDx = new HashSet<Integer>(listDx);
		cpuSx.addAll(cpuDx);
		
		return cpuSx;
	}
	
	@Override
	public List<PubblicazionePiuVendutaDto> getListPubblicazioniBarraSceltaRapidaSinistra(Integer[] codFiegDl, Integer[] codEdicola) {
		return getListPubblicazioniBarraSceltaRapidaSinistra(codFiegDl, codEdicola, false);
	}
	
	@Override
	public List<PubblicazionePiuVendutaDto> getListPubblicazioniBarraSceltaRapidaSinistra(Integer[] codFiegDl, Integer[] codEdicola, boolean barcodeUltimaCopertina) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BarraSceltaRapidaSinistraVo.class, "bsr");
		criteria.createCriteria("bsr.anagraficaPubblicazioniVo", "ap");
		criteria.createCriteria("ap.periodicitaVo", "per");
		criteria.add(Restrictions.in("bsr.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.in("bsr.pk.codEdicola", codEdicola));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("ap.pk.codicePubblicazione"), "codicePubblicazione");
		properties.add(Projections.property("ap.titolo"), "titolo");
		properties.add(Projections.property("ap.imgMiniaturaName"), "barcode");
		properties.add(Projections.property("per.pk.periodicita"), "codPeriodicita");
		properties.add(Projections.property("ap.codInizioQuotidiano"), "codInizioQuotidiano");
		properties.add(Projections.property("ap.codFineQuotidiano"), "codFineQuotidiano");
		properties.add(Projections.property("bsr.top"), "top");
		properties.add(Projections.property("bsr.left"), "left");
		properties.add(Projections.property("bsr.width"), "width");
		properties.add(Projections.property("bsr.height"), "height");
		properties.add(Projections.property("bsr.nomeImmagine"), "nomeImmagine");
		properties.add(Projections.property("bsr.tipoImmagine"), "tipoImmagine");
		properties.add(Projections.property("ap.imgMiniaturaName"), "nomeImmagineDefault");
		properties.add(Projections.property("bsr.pk.codFiegDl"), "codFiegDl");
		
		criteria.setProjection(properties);
		criteria.addOrder(Order.asc("bsr.posizione"));
		criteria.setResultTransformer(Transformers.aliasToBean(PubblicazionePiuVendutaDto.class));
		List<PubblicazionePiuVendutaDto> listPubblicazioniBarraSceltaRapida = getDao().findObjectByDetachedCriteria(criteria);
		if (barcodeUltimaCopertina) {
			for (PubblicazionePiuVendutaDto pdto : listPubblicazioniBarraSceltaRapida) {
				Object[] arrBarcodeImmagineUltCopertina = null;
				Integer codPub = pdto.getCodicePubblicazione();
				DetachedCriteria scCriteria = DetachedCriteria.forClass(StoricoCopertineVo.class, "sc");
				scCriteria.createCriteria("sc.immagine", "im", JoinType.LEFT_OUTER_JOIN);
				scCriteria.add(Restrictions.in("sc.pk.codDl", codFiegDl));
				if (pdto.getCodPeriodicita().equals(IGerivConstants.COD_PERIODICITA_QUOTIDIANO)) {
					scCriteria.add(Restrictions.between("sc.codicePubblicazione", pdto.getCodInizioQuotidiano(), pdto.getCodFineQuotidiano()));
				} else {
					scCriteria.add(Restrictions.eq("sc.codicePubblicazione", codPub));
				}
				DetachedCriteria subCriteria = DetachedCriteria.forClass(StoricoCopertineVo.class, "sc1");
				subCriteria.add(Restrictions.in("sc1.pk.codDl", codFiegDl));
				subCriteria.add(Restrictions.eq("sc1.codicePubblicazione", codPub));
				subCriteria.add(Restrictions.le("sc1.dataUscita", DateUtilities.floorDay(new Date())));
				ProjectionList props = Projections.projectionList();
				props.add(Projections.max("sc1.dataUscita"));
				subCriteria.setProjection(props);
				
				scCriteria.add(Subqueries.propertyEq("sc.dataUscita", subCriteria));
				
				ProjectionList props1 = Projections.projectionList(); 
				props1.add(Projections.property("sc.codiceBarre"), "barcode");
				props1.add(Projections.property("im.nome"), "nomeImmagine");
				props1.add(Projections.property("sc.pk.idtn"), "idtn");
				props1.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertina");
				props1.add(Projections.property("sc.numeroCopertina"), "numeroCopertina");
				scCriteria.setProjection(props1);
				arrBarcodeImmagineUltCopertina = getDao().findUniqueResultObjectByDetachedCriteria(scCriteria);
				
				if (arrBarcodeImmagineUltCopertina != null && arrBarcodeImmagineUltCopertina.length > 0) {
					pdto.setBarcode((arrBarcodeImmagineUltCopertina[0] != null) ? arrBarcodeImmagineUltCopertina[0].toString().trim() : "");
					pdto.setImmagineUltimaCopertina((arrBarcodeImmagineUltCopertina[1] != null) ? arrBarcodeImmagineUltCopertina[1].toString().trim() : "");
					pdto.setIdtn((arrBarcodeImmagineUltCopertina[2] != null) ? arrBarcodeImmagineUltCopertina[2].toString().trim() : "");
					pdto.setPrezzoCopertina(arrBarcodeImmagineUltCopertina[3] != null ? (BigDecimal) arrBarcodeImmagineUltCopertina[3] : null);
					pdto.setNumeroCopertina(arrBarcodeImmagineUltCopertina[4] != null ? arrBarcodeImmagineUltCopertina[4].toString().trim() : "");
				} else {
					pdto.setBarcode("");
					pdto.setImmagineUltimaCopertina("");
					pdto.setIdtn("");
					pdto.setNumeroCopertina("");
				}
				
			}
		}
		return listPubblicazioniBarraSceltaRapida;
	}
	
	@Override
	public List<PubblicazionePiuVendutaDto> getListPubblicazioniBarraSceltaRapidaDestra(Integer[] codFiegDl, Integer[] codEdicola, boolean barcodeUltimaCopertina) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BarraSceltaRapidaDestraVo.class, "bsr");
		criteria.createCriteria("bsr.anagraficaPubblicazioniVo", "ap");
		criteria.createCriteria("ap.periodicitaVo", "per");
		criteria.add(Restrictions.in("bsr.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.in("bsr.pk.codEdicola", codEdicola));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("ap.pk.codicePubblicazione"), "codicePubblicazione");
		properties.add(Projections.property("ap.titolo"), "titolo");
		properties.add(Projections.property("ap.imgMiniaturaName"), "barcode");
		properties.add(Projections.property("per.pk.periodicita"), "codPeriodicita");
		properties.add(Projections.property("ap.codInizioQuotidiano"), "codInizioQuotidiano");
		properties.add(Projections.property("ap.codFineQuotidiano"), "codFineQuotidiano");
		properties.add(Projections.property("bsr.top"), "top");
		properties.add(Projections.property("bsr.left"), "left");
		properties.add(Projections.property("bsr.width"), "width");
		properties.add(Projections.property("bsr.height"), "height");
		properties.add(Projections.property("bsr.nomeImmagine"), "nomeImmagine");
		properties.add(Projections.property("bsr.tipoImmagine"), "tipoImmagine");
		properties.add(Projections.property("ap.imgMiniaturaName"), "nomeImmagineDefault");
		properties.add(Projections.property("bsr.pk.codFiegDl"), "codFiegDl");
		
		criteria.setProjection(properties);
		criteria.addOrder(Order.asc("bsr.posizione"));
		criteria.setResultTransformer(Transformers.aliasToBean(PubblicazionePiuVendutaDto.class));
		List<PubblicazionePiuVendutaDto> listPubblicazioniBarraSceltaRapida = getDao().findObjectByDetachedCriteria(criteria);
		if (barcodeUltimaCopertina) {
			for (PubblicazionePiuVendutaDto pdto : listPubblicazioniBarraSceltaRapida) {
				Object[] arrBarcodeImmagineUltCopertina = null;
				Integer codPub = pdto.getCodicePubblicazione();
				DetachedCriteria scCriteria = DetachedCriteria.forClass(StoricoCopertineVo.class, "sc");
				scCriteria.createCriteria("sc.immagine", "im", JoinType.LEFT_OUTER_JOIN);
				scCriteria.add(Restrictions.in("sc.pk.codDl", codFiegDl));
				if (pdto.getCodPeriodicita().equals(IGerivConstants.COD_PERIODICITA_QUOTIDIANO)) {
					scCriteria.add(Restrictions.between("sc.codicePubblicazione", pdto.getCodInizioQuotidiano(), pdto.getCodFineQuotidiano()));
				} else {
					scCriteria.add(Restrictions.eq("sc.codicePubblicazione", codPub));
				}
				DetachedCriteria subCriteria = DetachedCriteria.forClass(StoricoCopertineVo.class, "sc1");
				subCriteria.add(Restrictions.in("sc1.pk.codDl", codFiegDl));
				subCriteria.add(Restrictions.eq("sc1.codicePubblicazione", codPub));
				subCriteria.add(Restrictions.le("sc1.dataUscita", DateUtilities.floorDay(new Date())));
				ProjectionList props = Projections.projectionList();
				props.add(Projections.max("sc1.dataUscita"));
				subCriteria.setProjection(props);
				
				scCriteria.add(Subqueries.propertyEq("sc.dataUscita", subCriteria));
				
				ProjectionList props1 = Projections.projectionList(); 
				props1.add(Projections.property("sc.codiceBarre"), "barcode");
				props1.add(Projections.property("im.nome"), "nomeImmagine");
				props1.add(Projections.property("sc.pk.idtn"), "idtn");
				props1.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertina");
				props1.add(Projections.property("sc.numeroCopertina"), "numeroCopertina");
				scCriteria.setProjection(props1);
				arrBarcodeImmagineUltCopertina = getDao().findUniqueResultObjectByDetachedCriteria(scCriteria);
				
				if (arrBarcodeImmagineUltCopertina != null && arrBarcodeImmagineUltCopertina.length > 0) {
					pdto.setBarcode((arrBarcodeImmagineUltCopertina[0] != null) ? arrBarcodeImmagineUltCopertina[0].toString().trim() : "");
					pdto.setImmagineUltimaCopertina((arrBarcodeImmagineUltCopertina[1] != null) ? arrBarcodeImmagineUltCopertina[1].toString().trim() : "");
					pdto.setIdtn((arrBarcodeImmagineUltCopertina[2] != null) ? arrBarcodeImmagineUltCopertina[2].toString().trim() : "");
					pdto.setPrezzoCopertina(arrBarcodeImmagineUltCopertina[3] != null ? (BigDecimal) arrBarcodeImmagineUltCopertina[3] : null);
					pdto.setNumeroCopertina(arrBarcodeImmagineUltCopertina[4] != null ? arrBarcodeImmagineUltCopertina[4].toString().trim() : "");
				} else {
					pdto.setBarcode("");
					pdto.setImmagineUltimaCopertina("");
					pdto.setIdtn("");
					pdto.setNumeroCopertina("");
				}
			}
		}
		return listPubblicazioniBarraSceltaRapida;
	}
	
	@Override
	public List<ArgomentoVo> getArgomenti(Integer codDl) {
		return getDao().findByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_ARGOMENTI, codDl);
	}
	
	@Override
	public Map<Integer, List<ArgomentoVo>> getMapArgomentiDl() {
		Map<Integer, List<ArgomentoVo>> map = new HashMap<>();
		List<ArgomentoVo> findByNamedQuery = getDao().findByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_ALL_ARGOMENTI);
		Group<ArgomentoVo> group = group(findByNamedQuery, by(on(ArgomentoVo.class).getPk().getCodDl()));
		for (Group<ArgomentoVo> subgroup : group.subgroups()) {
			List<ArgomentoVo> list = sort(subgroup.findAll(), on(ArgomentoVo.class).getDescrizione());
			map.put(new Integer(subgroup.key().toString()), list);
		}
		return map;
	}
	
	@Override
	public List<PeriodicitaVo> getPeriodicita() {
		return getDao().findByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_PERIODICITA);
	}
	
	/**
	 * Ritorna un array di codice a barre, nome immagine e idtn della pubblicazione (quotidiano)
	 * per codFiegDl, ciq, cfq e data uscita. 
	 * 
	 * @param Integer codFiegDl
	 * @param Integer codiceInizioQuotidiano
	 * @param Integer codiceFineQuotidiano
	 * @param Date dataUscita
	 * @return Object[]
	 */
	/*private Object[] getCodiceBarreNomeImmagineByCiqCfqAndDataUscita(Integer[] codFiegDl, Integer codiceInizioQuotidiano, Integer codiceFineQuotidiano, Date dataUscita) {
		DetachedCriteria criteria = DetachedCriteria.forClass(StoricoCopertineVo.class, "sc");
		criteria.createCriteria("sc.immagine", "im", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.in("sc.pk.codDl", codFiegDl));
		criteria.add(Restrictions.between("sc.codicePubblicazione", codiceInizioQuotidiano, codiceFineQuotidiano));
		criteria.add(Restrictions.eq("sc.dataUscita", new java.sql.Date(DateUtilities.floorDay(dataUscita).getTime())));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("sc.codiceBarre"), "barcode");
		props.add(Projections.property("im.nome"), "nomeImmagine");
		props.add(Projections.property("sc.pk.idtn"), "idtn");
		props.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertina");
		props.add(Projections.property("sc.numeroCopertina"), "numeroCopertina");
		criteria.setProjection(props);
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}*/
	
	@Override
	public Integer getCpuByBarcode(Integer codFiegDl, String barcode) {
		DetachedCriteria criteria = DetachedCriteria.forClass(StoricoCopertineVo.class, "scvo");
		criteria.add(Restrictions.eq("scvo.pk.codDl", codFiegDl));
		criteria.add(Restrictions.eq("scvo.codiceBarre", barcode));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("scvo.codicePubblicazione"));
		criteria.setProjection(properties);
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public ImmaginePubblicazioneVo getImmaginePubblicazione(String barcode) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ImmaginePubblicazioneVo.class, "brvo");
		criteria.add(Restrictions.eq("brvo.barcode", barcode));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public PeriodicitaTrascodificaInforeteVo getPeriodicitaTrascodificaInforete(Integer codPeriodicitaInforete) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PeriodicitaTrascodificaInforeteVo.class, "brvo");
		criteria.createCriteria("brvo.periodicita", "per");
		criteria.add(Restrictions.eq("brvo.periodicitaInforete", codPeriodicitaInforete));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public PeriodicitaVo getPeriodicita(Integer tipoOperazione, Integer codPeriodicita) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PeriodicitaVo.class, "per");
		criteria.add(Restrictions.eq("per.pk.tipo", tipoOperazione));
		criteria.add(Restrictions.eq("per.pk.periodicita", codPeriodicita));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public List<PubblicazioneDto> getPubblicazioniInvendute(final Integer[] arrCodFiegDl, final Integer[] arrCodEdicola, final String titolo, final Timestamp dataDa, final Timestamp dataA, final Integer gruppoSconto, final Boolean escludiQuotidiani, final Boolean escludiCD, final Integer baseCalcolo, final Integer page, final Integer maxRows) {
		HibernateCallback<List<PubblicazioneDto>> action = new HibernateCallback<List<PubblicazioneDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<PubblicazioneDto> doInHibernate(Session session) throws HibernateException, SQLException {
				String query = baseCalcolo != null && baseCalcolo.equals(IGerivConstants.BASE_CALCOLO_VENDITE) ? IGerivQueryContants.SQL_GET_PUBBLICAZIONI_INVENDUTE_SU_VENDITE : IGerivQueryContants.SQL_GET_PUBBLICAZIONI_INVENDUTE_SU_RESE;
				String per = (escludiQuotidiani != null && escludiQuotidiani) ? "and per5_.perio9216<>:periodicita" : "";
				String cd = (escludiCD != null && escludiCD) ? "and not exists (select 1 from tbl_9618 cd where cd.coddl9618 in (:coddl) and cd.crivw9618 in (:codEdicola) and cd.idtn9618 = sc1_.idtn9607 and cd.quant9618 > 0)" : "";
				String tit = !Strings.isNullOrEmpty(titolo) ? "and upper(ap2_.titolo9606) like '%" + titolo.toUpperCase() + "%'" : "";
				query = query.replace("$1", per);
				query = query.replace("$2", tit);
				query = query.replace("$3", cd);
				SQLQuery sqlQuery = session.createSQLQuery(query);
				sqlQuery.setResultTransformer(Transformers.aliasToBean(PubblicazioneDto.class));
				sqlQuery.setInteger("gruppoSconto", gruppoSconto);
				sqlQuery.setParameterList("coddl", arrCodFiegDl);
				sqlQuery.setParameterList("codEdicola", arrCodEdicola);
				sqlQuery.setTimestamp("dataDa", dataDa);
				sqlQuery.setTimestamp("dataA", dataA);
				if (escludiQuotidiani != null && escludiQuotidiani) {
					sqlQuery.setInteger("periodicita", IGerivConstants.PERIODICITA_QUOTIDIANO);
				}
				sqlQuery.addScalar("titolo",  StringType.INSTANCE);
				sqlQuery.addScalar("codicePubblicazione",  IntegerType.INSTANCE);
				sqlQuery.addScalar("sottoTitolo",  StringType.INSTANCE); 
				sqlQuery.addScalar("numeroCopertina",  StringType.INSTANCE); 
				sqlQuery.addScalar("prezzoCopertina",  BigDecimalType.INSTANCE);
				sqlQuery.addScalar("dataUscita", TimestampType.INSTANCE);
				sqlQuery.addScalar("idtn", IntegerType.INSTANCE);
				sqlQuery.addScalar("coddl", IntegerType.INSTANCE);
				sqlQuery.addScalar("prezzoEdicola", BigDecimalType.INSTANCE);
				sqlQuery.addScalar("argomento", StringType.INSTANCE);
				sqlQuery.addScalar("periodicita", StringType.INSTANCE);
				sqlQuery.addScalar("periodicitaPk", StringType.INSTANCE);
				sqlQuery.addScalar("immagine", StringType.INSTANCE);
				sqlQuery.addScalar("note", StringType.INSTANCE);
				sqlQuery.addScalar("noteByCpu", StringType.INSTANCE);
				sqlQuery.addScalar("fornitoSP", LongType.INSTANCE);
				sqlQuery.setFirstResult((page - 1) * maxRows);
				sqlQuery.setMaxResults(maxRows);
				return sqlQuery.list();
			}
		}; 
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public Integer getCountPubblicazioniInvendute(final Integer[] arrCodFiegDl, final Integer[] arrCodEdicola, final String titolo, final Timestamp dataDa, final Timestamp dataA, final Integer gruppoSconto, final Boolean escludiQuotidiani, final Boolean escludiCD, final Integer baseCalcolo) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				String query = baseCalcolo != null && baseCalcolo.equals(IGerivConstants.BASE_CALCOLO_VENDITE) ? IGerivQueryContants.SQL_COUNT_GET_PUBBLICAZIONI_INVENDUTE_SU_VENDITE : IGerivQueryContants.SQL_COUNT_GET_PUBBLICAZIONI_INVENDUTE_SU_RESE;
				String per = (escludiQuotidiani != null && escludiQuotidiani) ? "and per5_.perio9216<>:periodicita" : "";
				String cd = (escludiCD != null && escludiCD) ? "and not exists (select 1 from tbl_9618 cd where cd.coddl9618 in (:coddl) and cd.crivw9618 in (:codEdicola) and cd.idtn9618 = sc1_.idtn9607 and cd.quant9618 > 0)" : "";
				String tit = !Strings.isNullOrEmpty(titolo) ? "and upper(ap2_.titolo9606) like '%" + titolo.toUpperCase() + "%'" : "";
				query = query.replace("$1", per);
				query = query.replace("$2", tit);
				query = query.replace("$3", cd);
				SQLQuery sqlQuery = session.createSQLQuery(query);
				sqlQuery.setInteger("gruppoSconto", gruppoSconto);
				sqlQuery.setParameterList("coddl", arrCodFiegDl);
				sqlQuery.setParameterList("codEdicola", arrCodEdicola);
				sqlQuery.setTimestamp("dataDa", dataDa);
				sqlQuery.setTimestamp("dataA", dataA);
				if (escludiQuotidiani != null && escludiQuotidiani) {
					sqlQuery.setInteger("periodicita", IGerivConstants.PERIODICITA_QUOTIDIANO);
				}
				sqlQuery.addScalar("count",  IntegerType.INSTANCE);
				return (Integer) sqlQuery.uniqueResult();
			}
		}; 
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public AbbinamentoIdtnInforivVo getAbbinamentoIdtnInforiv(Integer codFiegDl, String idtni) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AbbinamentoIdtnInforivVo.class, "abii");
		criteria.add(Restrictions.eq("abii.pk.codDl", codFiegDl));
		criteria.add(Restrictions.eq("abii.pk.idtnInforete", idtni));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public StoricoCopertineVo getStoricoCopertinaByPk(Integer codFiegDl, Integer idtn) {
		DetachedCriteria criteria = DetachedCriteria.forClass(StoricoCopertineVo.class, "scvo");
		criteria.createCriteria("scvo.anagraficaPubblicazioniVo", "ap");
		criteria.add(Restrictions.eq("scvo.pk.codDl", codFiegDl));
		criteria.add(Restrictions.eq("scvo.pk.idtn", idtn));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public PubblicazioneDto getPubblicazioneConPrezzoEdicola(final Integer[] codEdicola, final Integer codFiegDl, final Integer idtn, final Integer gruppoSconto) {
		HibernateCallback<PubblicazioneDto> action = new HibernateCallback<PubblicazioneDto>() {
			@Override
			public PubblicazioneDto doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(StoricoCopertineVo.class, "sc");
				criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
				criteria.add(Restrictions.eq("sc.pk.codDl", codFiegDl));
				criteria.add(Restrictions.eq("sc.pk.idtn", idtn));
				ProjectionList properties = Projections.projectionList(); 
				properties.add(Projections.property("ap.titolo"), "titolo");
				properties.add(Projections.property("sc.codicePubblicazione"), "codicePubblicazione");
				properties.add(Projections.property("sc.sottoTitolo"), "sottoTitolo"); 
				properties.add(Projections.property("sc.numeroCopertina"), "numeroCopertina"); 
				properties.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertina");
				properties.add(Projections.property("sc.prezzoEdicola"), "prezzoEdicola");
				properties.add(Projections.property("sc.dataUscita"), "dataUscita");
				properties.add(Projections.property("sc.pk.idtn"), "idtn");
				properties.add(Projections.property("sc.pk.codDl"), "coddl");
				criteria.setProjection(properties); 
				criteria.setResultTransformer(Transformers.aliasToBean(PubblicazioneDto.class));
				Integer codEdicola1 = codEdicola.length > 1 ? codEdicola[1] : codEdicola[0];
				session.enableFilter("FornitoFilter").setParameter("codEdicola", codEdicola[0]).setParameter("codEdicola2", codEdicola1);
				session.enableFilter("GruppoScontoFilter").setParameter("gruppoSconto", gruppoSconto);
				PubblicazioneDto sc = (PubblicazioneDto) criteria.uniqueResult();
				session.disableFilter("FornitoFilter");
				session.disableFilter("GruppoScontoFilter");
				return sc;
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public StoricoCopertineVo getStoricoCopertinaByBarcode(Integer coddl, String barcode) {
		Integer[] dls = new Integer[]{coddl};
		return getStoricoCopertinaByBarcode(dls, barcode);
	}
	
	@Override
	public StoricoCopertineVo getStoricoCopertinaByBarcode(final Integer[] coddl, final String barcode){
		HibernateCallback<StoricoCopertineVo> action = new HibernateCallback<StoricoCopertineVo>() {
			@Override
			public StoricoCopertineVo doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(StoricoCopertineVo.class, "scvo");
				criteria.createCriteria("scvo.anagraficaPubblicazioniVo");
				criteria.add(Restrictions.in("scvo.pk.codDl", coddl));
				criteria.add(Restrictions.eq("scvo.codiceBarre", barcode));
				session.disableFilter("StoricoFilter");
				session.disableFilter("FornitoFilter");
				session.disableFilter("GruppoScontoFilter");
				// Può restituire più righe, prendiamo la prima
				criteria.setMaxResults(1);
				return (StoricoCopertineVo) criteria.uniqueResult();
//				StoricoCopertineVo vo = (StoricoCopertineVo) criteria.uniqueResult();
//				return vo;
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
		/*
		DetachedCriteria criteria = DetachedCriteria.forClass(StoricoCopertineVo.class, "scvo");
		//criteria.createCriteria("scvo.anagraficaPubblicazioniVo", "ap");
		criteria.add(Restrictions.in("scvo.pk.codDl", coddl));
		criteria.add(Restrictions.eq("scvo.codiceBarre", barcode));
		
		return getDao().findUniqueResultByDetachedCriteria(criteria);
		*/
	}
	
	
	@Override
	public List<StoricoCopertineVo> getStoricoCopertineByCpu(Integer codDl, Integer cpu) {
		DetachedCriteria criteria = DetachedCriteria.forClass(StoricoCopertineVo.class, "sc");
		criteria.add(Restrictions.eq("sc.pk.codDl", codDl));
		criteria.add(Restrictions.eq("sc.codicePubblicazione", cpu));
		criteria.addOrder(Order.desc("sc.dataUscita"));
		
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public StoricoCopertineVo getLastStoricoCopertina(final Integer codDl, final Integer cpu) {
		HibernateCallback<StoricoCopertineVo> action = new HibernateCallback<StoricoCopertineVo>() {
			@Override
			public StoricoCopertineVo doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(StoricoCopertineVo.class, "sc");
				criteria.add(Restrictions.eq("sc.pk.codDl", codDl));
				criteria.add(Restrictions.eq("sc.codicePubblicazione", cpu));
				criteria.add(Restrictions.le("sc.dataUscita", DateUtilities.floorDay(getSysdate())));
				criteria.addOrder(Order.desc("sc.dataUscita"));
				criteria.setMaxResults(1);
				return (StoricoCopertineVo) criteria.uniqueResult();
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public PubblicazioneDto getLastPubblicazioneDto(final Integer codDl, final Integer cpu) {
		HibernateCallback<PubblicazioneDto> action = new HibernateCallback<PubblicazioneDto>() {
			@Override
			public PubblicazioneDto doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(StoricoCopertineVo.class, "sc");
				criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
				criteria.createCriteria("ap.periodicitaVo", "per");
				criteria.createCriteria("ap.limitiPeriodicitaVo", "lp");
				criteria.add(Restrictions.eq("sc.pk.codDl", codDl));
				criteria.add(Restrictions.eq("sc.codicePubblicazione", cpu));
				criteria.add(Restrictions.le("sc.dataUscita", DateUtilities.floorDay(getSysdate())));
				criteria.addOrder(Order.desc("sc.dataUscita"));
				criteria.setMaxResults(1);
				ProjectionList properties = Projections.projectionList(); 
				properties.add(Projections.property("ap.titolo"), "titolo");
				properties.add(Projections.property("ap.codInizioQuotidiano"), "codInizioQuotidiano");
				properties.add(Projections.property("ap.codFineQuotidiano"), "codFineQuotidiano");
				properties.add(Projections.property("ap.numCopertinePrecedentiPerRifornimenti"), "numCopertinePrecedentiPerRifornimenti");
				properties.add(Projections.property("sc.codicePubblicazione"), "codicePubblicazione");
				properties.add(Projections.property("sc.sottoTitolo"), "sottoTitolo"); 
				properties.add(Projections.property("sc.numeroCopertina"), "numeroCopertina"); 
				properties.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertina");
				properties.add(Projections.property("sc.dataUscita"), "dataUscita");
				properties.add(Projections.property("sc.pk.idtn"), "idtn");
				properties.add(Projections.property("lp.giorniValiditaRichiesteRifornimento"), "giorniValiditaRichiesteRifornimento");
				properties.add(Projections.property("lp.maxStatisticaVisualizzare"), "maxStatisticaVisualizzare");
				properties.add(Projections.property("lp.numGiorniDaDataUscitaPerRichiestaRifornimento"), "numGiorniDaDataUscitaPerRichiestaRifornimento");
				properties.add(Projections.property("sc.giancezaPressoDl"), "giancezaPressoDl");
				properties.add(Projections.property("sc.codEdicolaCorrezioneBarcode"), "codEdicolaCorrezioneBarcode");
				properties.add(Projections.property("sc.pk.codDl"), "coddl");
				properties.add(Projections.property("per.pk.tipo"), "tipo");
				properties.add(Projections.property("per.pk.periodicita"), "periodicitaInt");
				criteria.setProjection(properties); 
				criteria.setResultTransformer(Transformers.aliasToBean(PubblicazioneDto.class));
				Object findByDetachedCriteria = criteria.uniqueResult();
				return findByDetachedCriteria != null ? (PubblicazioneDto) findByDetachedCriteria : null;
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public List<AnagraficaPubblicazioniVo> getQuotidiani(Integer codDl, Integer cpuInizio, Integer cpuFine) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AnagraficaPubblicazioniVo.class, "ap");
		criteria.add(Restrictions.eq("ap.pk.codFiegDl", codDl));
		criteria.add(Restrictions.between("ap.pk.codicePubblicazione", cpuInizio, cpuFine));
		criteria.addOrder(Order.asc("ap.pk.codicePubblicazione"));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<PubblicazioneDto> getCopertineUsciteData(final Timestamp dataUscita, final Integer codFiegDl) {
		HibernateCallback<List<PubblicazioneDto>> action = new HibernateCallback<List<PubblicazioneDto>>() {

			@Override
			public List<PubblicazioneDto> doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuilder select = new StringBuilder();
				select.append("SELECT DISTINCT descr9217 as argomento, titolo9609 as titolo, sottot9607 as sottoTitolo, num9609 as numeroCopertina, prezzo9607 as prezzoCopertina, descr9216 as periodicita, nomei9700 as immagine ");
				select.append("FROM tbl_9609 INNER JOIN tbl_9607 ON (idtn9609 = idtn9607 AND coddl9609 = coddl9607) ");
				select.append("INNER JOIN tbl_9606 ON (cpu9607 = cpu9606 AND coddl9607 = coddl9606) ");
				select.append("LEFT OUTER JOIN tbl_9217 ON (argodl9606 = segm9217 AND coddl9606 = coddl9217) ");
				select.append("LEFT OUTER JOIN tbl_9216 ON (peridl9606 = perio9216) ");
				select.append("LEFT OUTER JOIN tbl_9700 ON (barcode9607 = barcode9700) ");
				select.append("WHERE coddl9609 = :coddl AND datbc9609 = :dataUscita ");
				select.append(" 	AND tipop9216 = 1  ");
				select.append("ORDER BY descr9217, titolo9609");
				
				SQLQuery sqlQuery = session.createSQLQuery(select.toString());
				sqlQuery.setResultTransformer(Transformers.aliasToBean(PubblicazioneDto.class));
				sqlQuery.setInteger("coddl", codFiegDl);
				sqlQuery.setTimestamp("dataUscita", dataUscita);
				
				sqlQuery.addScalar("argomento", StringType.INSTANCE);
				sqlQuery.addScalar("titolo", StringType.INSTANCE);
				sqlQuery.addScalar("sottoTitolo", StringType.INSTANCE);
				sqlQuery.addScalar("numeroCopertina", StringType.INSTANCE);
				sqlQuery.addScalar("prezzoCopertina", BigDecimalType.INSTANCE);
				sqlQuery.addScalar("periodicita", StringType.INSTANCE);
				sqlQuery.addScalar("immagine", StringType.INSTANCE);
				
				return sqlQuery.list();
			}
			
		};
		
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public void updateBarcodeTodisConDevietti(Integer days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getSysdate());
		cal.add(Calendar.DAY_OF_MONTH, -days);
		Timestamp date = DateUtilities.floorDay(cal.getTime());
		getDao().bulkUpdate("update StoricoCopertineVo vo set vo.codiceBarre = (select vo1.codiceBarre from StoricoCopertineVo vo1 where vo1.pk.codDl = ? and vo.codicePubblicazione = vo1.codicePubblicazione and vo.complementoDistribuzione = vo1.complementoDistribuzione and vo.numeroCopertina = vo1.numeroCopertina) where vo.pk.codDl = ? and vo.dataUscita >= ?", new Object[]{IGerivConstants.COD_FIEG_DL_DEVIETTI, IGerivConstants.COD_FIEG_DL_TODIS, date});
	}
	
	@Override
	public PubblicazioneDto getQuotidianoByDataUscita(final Integer coddl, final Integer cpuDa, final Integer cpuA, final Timestamp dataUscita) {
		HibernateCallback<PubblicazioneDto> action = new HibernateCallback<PubblicazioneDto>() {
			@Override
			public PubblicazioneDto doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(StoricoCopertineVo.class, "sc");
				criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
				criteria.createCriteria("ap.periodicitaVo", "per");
				criteria.createCriteria("ap.limitiPeriodicitaVo", "lp");
				criteria.add(Restrictions.eq("sc.pk.codDl", coddl));
				criteria.add(Restrictions.between("sc.codicePubblicazione", cpuDa, cpuA));
				criteria.add(Restrictions.eq("sc.dataUscita", DateUtilities.floorDay(dataUscita)));
				criteria.add(Restrictions.eq("per.pk.periodicita", IGerivConstants.COD_PERIODICITA_QUOTIDIANO));
				ProjectionList properties = Projections.projectionList(); 
				properties.add(Projections.property("ap.titolo"), "titolo");
				properties.add(Projections.property("ap.codInizioQuotidiano"), "codInizioQuotidiano");
				properties.add(Projections.property("ap.codFineQuotidiano"), "codFineQuotidiano");
				properties.add(Projections.property("ap.numCopertinePrecedentiPerRifornimenti"), "numCopertinePrecedentiPerRifornimenti");
				properties.add(Projections.property("sc.codicePubblicazione"), "codicePubblicazione");
				properties.add(Projections.property("sc.sottoTitolo"), "sottoTitolo"); 
				properties.add(Projections.property("sc.numeroCopertina"), "numeroCopertina"); 
				properties.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertina");
				properties.add(Projections.property("sc.dataUscita"), "dataUscita");
				properties.add(Projections.property("sc.pk.idtn"), "idtn");
				properties.add(Projections.property("lp.giorniValiditaRichiesteRifornimento"), "giorniValiditaRichiesteRifornimento");
				properties.add(Projections.property("lp.maxStatisticaVisualizzare"), "maxStatisticaVisualizzare");
				properties.add(Projections.property("lp.numGiorniDaDataUscitaPerRichiestaRifornimento"), "numGiorniDaDataUscitaPerRichiestaRifornimento");
				properties.add(Projections.property("sc.giancezaPressoDl"), "giancezaPressoDl");
				properties.add(Projections.property("sc.codEdicolaCorrezioneBarcode"), "codEdicolaCorrezioneBarcode");
				properties.add(Projections.property("sc.pk.codDl"), "coddl");
				properties.add(Projections.property("per.pk.periodicita"), "periodicitaInt");
				criteria.setProjection(properties); 
				criteria.setResultTransformer(Transformers.aliasToBean(PubblicazioneDto.class));
				Object findByDetachedCriteria = criteria.uniqueResult();
				return findByDetachedCriteria != null ? (PubblicazioneDto) findByDetachedCriteria : null;
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	class PubblicazioneFornitoListFilter<T extends PubblicazioneFornito> implements ListFilter<T> {
		@Override
		public void filter(List<T> list, Map<String, Object> params) {
			Integer codFiegDl = (Integer) params.get("codFiegDl");
			Integer maxRows = params.get("maxRows") != null ? (Integer) params.get("maxRows") : null;
			boolean isEdicolaDeviettiTodis = codFiegDl.equals(IGerivConstants.COD_FIEG_DL_DEVIETTI) || codFiegDl.equals(IGerivConstants.COD_FIEG_DL_TODIS);

			Integer agenziaFatturazione = (Integer) params.get("agenziaFatturazione");
			Boolean isSecondaCintura =  (Boolean) params.get("isSecondaCintura");
			Timestamp dtPartenzaSecondaCintura = (Timestamp) params.get("dataPartSecCintura");
			
			if (isEdicolaDeviettiTodis && agenziaFatturazione != null && agenziaFatturazione > 0) {
				Iterator<PubblicazioneFornito> it = (Iterator<PubblicazioneFornito>) list.iterator();
				while (it.hasNext()) {
					// TODO SECONDA CINTURA					
					PubblicazioneFornito dto = it.next();
					if (dto.isEditoreComune()) {
						if (isSecondaCintura!=null && isSecondaCintura && dtPartenzaSecondaCintura!=null) {
							Timestamp dataUscita = dto.getDataUscita();
							if (dataUscita !=null && dataUscita.compareTo(dtPartenzaSecondaCintura)>=0) {
								agenziaFatturazione = 2;
							} else {
								agenziaFatturazione = 1;
							}
						}
						switch (agenziaFatturazione) {
						case 1:
							if (dto.getCoddl().equals(IGerivConstants.COD_FIEG_DL_TODIS)) {
							    it.remove();
							}
							break;
						case 2:
							if (dto.getCoddl().equals(IGerivConstants.COD_FIEG_DL_DEVIETTI)) {
							    it.remove();
							}
							break;
						}
					}
				}				
			} else {
				/*Group<T> group = group(list,
						isEdicolaDeviettiTodis 
						? by(on(PubblicazioneFornito.class).getCodicePubblicazioneeNumeroCopertina())
						: by(on(PubblicazioneFornito.class).getCodiceInforeteNumeroCopertinaInforete())
						);*/
				Group<T> group = group(list,
						by(on(PubblicazioneFornito.class).getCodiceInforeteNumeroCopertinaInforete())
						);
				if (group.subgroups().size() > 0) {
					for (Group<T> subgroup : group.subgroups()) {
						List<T> findAll = subgroup.findAll();
						if (findAll.size() > 1) {
							List<T> pubbFornite = select(findAll, having(on(PubblicazioneFornito.class).getFornito(), greaterThan(0)));
							if (pubbFornite != null && !pubbFornite.isEmpty()) {
								list.removeAll(select(findAll, having(on(PubblicazioneFornito.class).getFornito(), lessThanOrEqualTo(0))));
							} else {
								list.removeAll(select(findAll, having(on(PubblicazioneFornito.class).getCoddl(), not(equalTo(codFiegDl)))));
							}
						}
					}
				}
			}
			

			if (isEdicolaDeviettiTodis && maxRows != null && list.size() > (maxRows / 2)) {
				for (int i = list.size() - 1; i >= (maxRows / 2); i--) {
					list.remove(i);
				}
			}
		}
		
	}



	@Override
	public List<AnagraficaEditoreDto> getListAnagraficaEditori(Integer[] coddl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AnagraficaEditoreDlVo.class, "ae");
		criteria.add(Restrictions.in("ae.pk.codFiegDl", coddl));
		criteria.add(Restrictions.isNotNull("ae.nomeA"));
		criteria.addOrder(Order.asc("ae.nomeA"));
		
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("ae.pk.codFiegDl"), "codFiegDl");
		properties.add(Projections.property("ae.pk.codFornitore"), "codFornitore");
		properties.add(Projections.property("ae.nomeA"), "nomeA");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(AnagraficaEditoreDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
}
