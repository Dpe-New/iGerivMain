package it.dpe.igeriv.bo.messaggi;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.AvvisoMessaggioDto;
import it.dpe.igeriv.dto.ConfermaLetturaMessaggioDto;
import it.dpe.igeriv.dto.EmailRivenditaDto;
import it.dpe.igeriv.dto.GiroDto;
import it.dpe.igeriv.dto.GiroTipoDto;
import it.dpe.igeriv.dto.MessaggioDpeDto;
import it.dpe.igeriv.dto.MessaggioDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.EmailRivenditaVo;
import it.dpe.igeriv.vo.GiroTipoVo;
import it.dpe.igeriv.vo.MessaggiBollaVo;
import it.dpe.igeriv.vo.MessaggioClienteVo;
import it.dpe.igeriv.vo.MessaggioDpeVo;
import it.dpe.igeriv.vo.MessaggioIdtnVo;
import it.dpe.igeriv.vo.MessaggioVo;
import it.dpe.igeriv.vo.ZonaTipoVo;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BlobType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;

@Repository
class MessaggiRepositoryImpl extends BaseRepositoryImpl implements MessaggiRepository {
	private final AgenzieService agenzieService;
	
	@Autowired
	MessaggiRepositoryImpl(BaseDao<MessaggioVo> dao, AgenzieService agenzieService) {
		super(dao);
		this.agenzieService = agenzieService;
	}
	
	@Override
	public List<MessaggiBollaVo> getMessaggiBolla(final Integer[] codFiegDl, final Integer[] codEdicolaDl, final Timestamp dtBolla, final String tipo) {
		HibernateCallback<List<MessaggiBollaVo>> action = new HibernateCallback<List<MessaggiBollaVo>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<MessaggiBollaVo> doInHibernate(Session session) throws HibernateException, SQLException {
				Query createQuery = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_GET_MESSAGGI_BOLLA);
				createQuery.setParameterList("codFiegDl", codFiegDl);
				List<Integer> asList = new ArrayList<Integer>(Arrays.asList(codEdicolaDl));
				asList.add(0);
				createQuery.setParameterList("codEdicola", asList);
				createQuery.setTimestamp("dtBolla", dtBolla);
				createQuery.setString("tipoBolla", tipo);
				return createQuery.list();
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public List<MessaggiBollaVo> getMessaggiBollaEdicola(Integer codFiegDl, Integer codEdicolaDl, Timestamp dtBolla, String tipo) {
		return getDao().findByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_MESSAGGI_BOLLA, codFiegDl, codEdicolaDl, dtBolla, tipo);
	}
	
	@Override
	public List<MessaggioDto> getMessaggiRivendite(final Integer[] codDl, final Timestamp dtMessaggioDa, 
			final Timestamp dtMessaggioA, final Integer[] codEdicola, final boolean showMessaggioLetto) {
		HibernateCallback<List<MessaggioDto>> action = new HibernateCallback<List<MessaggioDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<MessaggioDto> doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer query = new StringBuffer();
				String codDlClause = "coddl9601 in (:codDl)";
				List<AnagraficaAgenziaVo> agenzie = null;
				if (codDl == null) {
					agenzie = agenzieService.getAgenzie();
				}
				query.append("select * from ( ");
				query.append("select coddl9601 as codFiegDl, tdest9601 as tipoDestinatario, desta9601 as destinatarioA, destb9601 as destinatarioB, testo9601 as messaggio, testb9601 as messaggioEsteso, tipme9601 as tipoMessaggio, datam9601 as dtMessaggio, stato9601 as statoMessaggio, aleg19601 as attachmentName1, aleg29601 as attachmentName2, aleg39601 as attachmentName3, edico9601 as edicolaLabel ");
				if (showMessaggioLetto) {
					query.append(",letto9602 as messaggioLetto ");
				}
				query.append("from tbl_9601 ");
				if (showMessaggioLetto) {
					query.append("left outer join tbl_9602 on coddl9602 = coddl9601 and datam9602 = datam9601 and coded9602 in (:codEdicola) ");
				}
				query.append("where " + codDlClause + " and tdest9601 = " + IGerivConstants.COD_TUTTE_LE_EDICOLE + " and desta9601 in (:destinatarioA) and destb9601 in (:destinatarioB) ");
				if (dtMessaggioDa != null && dtMessaggioA != null) {
					query.append("and datam9601 between :dataDa and :dataA ");
				} else if (dtMessaggioDa != null) {
					query.append("and datam9601 >= :dataDa ");
				} else if (dtMessaggioA != null) {
					query.append("and datam9601 <= :dataA ");
				}
				query.append("union all ");
				query.append("select coddl9601 as codFiegDl, tdest9601 as tipoDestinatario, desta9601 as destinatarioA, destb9601 as destinatarioB, testo9601 as messaggio, testb9601 as messaggioEsteso, tipme9601 as tipoMessaggio, datam9601 as dtMessaggio, stato9601 as statoMessaggio, aleg19601 as attachmentName1, aleg29601 as attachmentName2, aleg39601 as attachmentName3, edico9601 as edicolaLabel ");
				if (showMessaggioLetto) {
					query.append(",letto9602 as messaggioLetto ");
				}
				query.append("from tbl_9601 ");
				if (showMessaggioLetto) {
					query.append("left outer join tbl_9602 on coddl9602 = coddl9601 and datam9602 = datam9601 and coded9602 in (:codEdicola) ");
				}
				query.append("where " + codDlClause + " and tdest9601 = " + IGerivConstants.COD_EDICOLA_SINGOLA + " ");
				if (showMessaggioLetto) {
					query.append("and desta9601 in (:destinatarioA1) ");
					query.append("and destb9601 in (:destinatarioB1) ");
				}
				if (dtMessaggioDa != null && dtMessaggioA != null) {
					query.append("and datam9601 between :dataDa and :dataA ");
				} else if (dtMessaggioDa != null) {
					query.append("and datam9601 >= :dataDa ");
				} else if (dtMessaggioA != null) {
					query.append("and datam9601 <= :dataA ");
				}
				query.append("union all ");
				query.append("select coddl9601 as codFiegDl, tdest9601 as tipoDestinatario, desta9601 as destinatarioA, destb9601 as destinatarioB, testo9601 as messaggio, testb9601 as messaggioEsteso, tipme9601 as tipoMessaggio, datam9601 as dtMessaggio, stato9601 as statoMessaggio, aleg19601 as attachmentName1, aleg29601 as attachmentName2, aleg39601 as attachmentName3, edico9601 as edicolaLabel ");
				if (showMessaggioLetto) {
					query.append(",letto9602 as messaggioLetto ");
				}
				query.append("from tbl_9601 ");
				if (showMessaggioLetto) {
					query.append("left outer join tbl_9602 on coddl9602 = coddl9601 and datam9602 = datam9601 and coded9602 in (:codEdicola) ");
					query.append("inner join tbl_9110 on coddl9110 = coddl9601 and crivw9110  in (:codEdicola) and gti9110 = desta9601 and giro9110 = destb9601 ");
				}
				query.append("where " + codDlClause + " and tdest9601 = " + IGerivConstants.COD_GIRO_TIPO + "  ");
				if (dtMessaggioDa != null && dtMessaggioA != null) {
					query.append("and datam9601 between :dataDa and :dataA ");
				} else if (dtMessaggioDa != null) {
					query.append("and datam9601 >= :dataDa ");
				} else if (dtMessaggioA != null) {
					query.append("and datam9601 <= :dataA ");
				}
				query.append("union all ");
				query.append("select coddl9601 as codFiegDl, tdest9601 as tipoDestinatario, desta9601 as destinatarioA, destb9601 as destinatarioB, testo9601 as messaggio, testb9601 as messaggioEsteso, tipme9601 as tipoMessaggio, datam9601 as dtMessaggio, stato9601 as statoMessaggio, aleg19601 as attachmentName1, aleg29601 as attachmentName2, aleg39601 as attachmentName3, edico9601 as edicolaLabel ");
				if (showMessaggioLetto) {
					query.append(",letto9602 as messaggioLetto ");
				}
				query.append("from tbl_9601 ");
				if (showMessaggioLetto) {
					query.append("left outer join tbl_9602 on coddl9602 = coddl9601 and datam9602 = datam9601 and coded9602 in (:codEdicola) ");
					query.append("inner join tbl_9111 on coddl9111 = coddl9601 and crivw9111 in (:codEdicola) and zti9111 = desta9601 and zona9111 = destb9601 ");
				}
				query.append("where " + codDlClause + " and tdest9601 = " + IGerivConstants.COD_ZONA_TIPO + " ");
				if (dtMessaggioDa != null && dtMessaggioA != null) {
					query.append("and datam9601 between :dataDa and :dataA ");
				} else if (dtMessaggioDa != null) {
					query.append("and datam9601 >= :dataDa ");
				} else if (dtMessaggioA != null) {
					query.append("and datam9601 <= :dataA ");
				}
				query.append(") ");
				if (showMessaggioLetto) {
					query.append("order by messaggioLetto desc, tipoMessaggio desc, dtMessaggio desc ");
				} else {
					query.append("order by tipoMessaggio desc, dtMessaggio desc ");
				}
				
				SQLQuery sqlQuery = session.createSQLQuery(query.toString());
				if (showMessaggioLetto) {
					sqlQuery.setParameterList("codEdicola", codEdicola);
				}
				if (codDl == null && agenzie != null) {
					sqlQuery.setParameterList("codDl", extract(agenzie, on(AnagraficaAgenziaVo.class).getCodFiegDl()));
				} else {
					sqlQuery.setParameterList("codDl", codDl);
				}
				sqlQuery.setParameterList("destinatarioA", new Integer[]{0});
				if (showMessaggioLetto) {
					sqlQuery.setParameterList("destinatarioA1", codEdicola);
					sqlQuery.setParameterList("destinatarioB1", new Integer[]{0});
				}
				sqlQuery.setInteger("destinatarioB", 0);
				if (dtMessaggioDa != null && dtMessaggioA != null) {
					sqlQuery.setTimestamp("dataDa", dtMessaggioDa);
					sqlQuery.setTimestamp("dataA", dtMessaggioA);
				} else if (dtMessaggioDa != null) {
					sqlQuery.setTimestamp("dataDa", dtMessaggioDa);
				} else if (dtMessaggioA != null) {
					sqlQuery.setTimestamp("dataA", dtMessaggioA);
				}
				sqlQuery.setResultTransformer( Transformers.aliasToBean(MessaggioDto.class));
				sqlQuery.addScalar("codFiegDl", IntegerType.INSTANCE);
				sqlQuery.addScalar("tipoDestinatario", IntegerType.INSTANCE);
				sqlQuery.addScalar("destinatarioA", IntegerType.INSTANCE);
				sqlQuery.addScalar("destinatarioB", IntegerType.INSTANCE);
				sqlQuery.addScalar("messaggio", StringType.INSTANCE);
				sqlQuery.addScalar("messaggioEsteso", BlobType.INSTANCE);
				sqlQuery.addScalar("tipoMessaggio", IntegerType.INSTANCE);
				sqlQuery.addScalar("dtMessaggio", TimestampType.INSTANCE);
				sqlQuery.addScalar("statoMessaggio", IntegerType.INSTANCE);
				sqlQuery.addScalar("attachmentName1", StringType.INSTANCE);
				sqlQuery.addScalar("attachmentName2", StringType.INSTANCE);
				sqlQuery.addScalar("attachmentName3", StringType.INSTANCE);
				sqlQuery.addScalar("edicolaLabel", StringType.INSTANCE);
				if (showMessaggioLetto) {
					sqlQuery.addScalar("messaggioLetto", IntegerType.INSTANCE);
				}
				List<MessaggioDto> list = sqlQuery.list();
				return list;
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public List<ConfermaLetturaMessaggioDto> getConfermeLettura(final Integer codEdicola, final Integer codFiegDl, final Timestamp dtMessaggio, 
			final Integer tipoDestinatario, final Integer destinatarioA, final Integer destinatarioB) {
		HibernateCallback<List<ConfermaLetturaMessaggioDto>> action = new HibernateCallback<List<ConfermaLetturaMessaggioDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<ConfermaLetturaMessaggioDto> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = IGerivQueryContants.SQL_QUERY_GET_CONFERMA_LETTURA_MESSAGGIO;
				boolean hasFilterEdicolaSingola = (codEdicola != null && !codEdicola.equals(0)) || (tipoDestinatario != null && tipoDestinatario.equals(IGerivConstants.COD_EDICOLA_SINGOLA) && destinatarioA != null);
				boolean hasFilterGiroTipo = (tipoDestinatario != null && tipoDestinatario.equals(IGerivConstants.COD_GIRO_TIPO) && destinatarioA != null && destinatarioB != null);
				boolean hasFilterZonaTipo = (tipoDestinatario != null && tipoDestinatario.equals(IGerivConstants.COD_ZONA_TIPO) && destinatarioA != null && destinatarioB != null);
				String replace = "";
				if (hasFilterEdicolaSingola) {
					replace = "and t4.crivw9106 = :codEdicola";
				} else if (hasFilterGiroTipo) {
					replace = "and t4.crivw9106 in (select g.crivw9110 from tbl_9110 g where g.coddl9110 = t1.coddl9206 and g.gti9110 = :giroTipo and g.giro9110 = :giro)";
				} else if (hasFilterZonaTipo) {
					replace = "and t4.crivw9106 in (select z.crivw9111 from tbl_9111 z where z.coddl9111 = t1.coddl9206 and z.zti9111 = :zonaTipo and z.zona9111 = :zona)";
				}
				sql = sql.replace("$1", replace); 
				SQLQuery createSQLQuery = session.createSQLQuery(sql);
				createSQLQuery.setTimestamp("dtMessaggio", dtMessaggio);
				createSQLQuery.setInteger("codFiegDl", codFiegDl);
				if (hasFilterEdicolaSingola) {
					createSQLQuery.setInteger("codEdicola", codEdicola != null && !codEdicola.equals(0) ? codEdicola : destinatarioA);
				} else if (hasFilterGiroTipo) {
					createSQLQuery.setInteger("giroTipo", destinatarioA);
					createSQLQuery.setInteger("giro", destinatarioB);
				} else if (hasFilterZonaTipo) {
					createSQLQuery.setInteger("zonaTipo", destinatarioA);
					createSQLQuery.setInteger("zona", destinatarioB);
				}
				createSQLQuery.setResultTransformer( Transformers.aliasToBean(ConfermaLetturaMessaggioDto.class));
				createSQLQuery.addScalar("codEdicola", IntegerType.INSTANCE);
				createSQLQuery.addScalar("ragioneSociale", StringType.INSTANCE);
				createSQLQuery.addScalar("localita", StringType.INSTANCE);
				createSQLQuery.addScalar("messaggioLetto", IntegerType.INSTANCE);
				return createSQLQuery.list();
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public MessaggioDto getMessaggioRivendita(final Integer codEdicola, final Integer codFiegDl, final Timestamp dtMessaggio, 
			final Integer tipoDestinatario, final Integer destinatarioA, final Integer destinatarioB) {
		HibernateCallback<MessaggioDto> action = new HibernateCallback<MessaggioDto>() {
			@Override
			public MessaggioDto doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery(IGerivQueryContants.SQL_QUERY_GET_MESSAGGIO_RIVENDITA);
				sqlQuery.setInteger("codDl", codFiegDl);
				sqlQuery.setTimestamp("dtMessaggio", dtMessaggio);
				sqlQuery.setInteger("tipoDestinatario", tipoDestinatario);
				sqlQuery.setInteger("codEdicola", codEdicola);
				sqlQuery.setInteger("destinatarioA", destinatarioA);
				sqlQuery.setInteger("destinatarioB", destinatarioB);
				
				sqlQuery.setResultTransformer( Transformers.aliasToBean(MessaggioDto.class));
				sqlQuery.addScalar("codFiegDl", IntegerType.INSTANCE);
				sqlQuery.addScalar("tipoDestinatario", IntegerType.INSTANCE);
				sqlQuery.addScalar("destinatarioA", IntegerType.INSTANCE);
				sqlQuery.addScalar("destinatarioB", IntegerType.INSTANCE);
				sqlQuery.addScalar("messaggio", StringType.INSTANCE);
				sqlQuery.addScalar("messaggioEsteso", BlobType.INSTANCE);
				sqlQuery.addScalar("tipoMessaggio", IntegerType.INSTANCE);
				sqlQuery.addScalar("dtMessaggio", TimestampType.INSTANCE);
				sqlQuery.addScalar("statoMessaggio", IntegerType.INSTANCE);
				sqlQuery.addScalar("attachmentName1", StringType.INSTANCE);
				sqlQuery.addScalar("attachmentName2", StringType.INSTANCE);
				sqlQuery.addScalar("attachmentName3", StringType.INSTANCE);
				sqlQuery.addScalar("messaggioLetto", IntegerType.INSTANCE);
				
				return (MessaggioDto) sqlQuery.uniqueResult();
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public MessaggioVo getMessaggioRivenditaVo(Integer codFiegDl, Timestamp dtMessaggio, Integer tipoDestinatario, Integer destinatarioA, Integer destinatarioB) {
		return getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_MESSAGGIO_RIVENDITA, codFiegDl, dtMessaggio, tipoDestinatario, destinatarioA, destinatarioB);
	}
	
	@Override
	public AvvisoMessaggioDto getPkMessaggioNonLetto(final Integer codEdicola, final Integer codDl, 
			final List<Integer> giroZonaTipo, final List<Integer> giriZone, final Integer tipoMessaggio, 
			final Timestamp dtAttivazioneEdicola) {
		HibernateCallback<AvvisoMessaggioDto> action = new HibernateCallback<AvvisoMessaggioDto>() {
			@Override
			public AvvisoMessaggioDto doInHibernate(Session session) throws HibernateException, SQLException {
				Integer[] arrTipoMessaggio = (tipoMessaggio == null) ? new Integer[]{IGerivConstants.TIPO_MESSAGGIO_CON_AVVISO,IGerivConstants.TIPO_MESSAGGIO_IMMEDIATO,IGerivConstants.TIPO_MESSAGGIO_UREGENTISSIMO} : new Integer[]{tipoMessaggio}; 
				SQLQuery sqlQuery = session.createSQLQuery(IGerivQueryContants.SQL_QUERY_GET_PK_MESSAGGIO_NON_LETTO);
				sqlQuery.setInteger("codDl", codDl);
				sqlQuery.setInteger("codEdicola", codEdicola);
				sqlQuery.setInteger("destinatarioA", 0);
				sqlQuery.setInteger("destinatarioA1", codEdicola);
				sqlQuery.setInteger("destinatarioB", 0);
				sqlQuery.setInteger("destinatarioB1", 0);
				sqlQuery.setParameterList("tipoMessaggio", arrTipoMessaggio);
				sqlQuery.setTimestamp("dtAttivazioneEdicola", dtAttivazioneEdicola);
				sqlQuery.setResultTransformer( Transformers.aliasToBean(AvvisoMessaggioDto.class));
				sqlQuery.addScalar("codFiegDl", IntegerType.INSTANCE);
				sqlQuery.addScalar("dtMessaggio", TimestampType.INSTANCE);
				sqlQuery.addScalar("tipoDestinatario", IntegerType.INSTANCE);
				sqlQuery.addScalar("destinatarioA", IntegerType.INSTANCE);
				sqlQuery.addScalar("destinatarioB", IntegerType.INSTANCE);
				sqlQuery.addScalar("priorita", IntegerType.INSTANCE);
				sqlQuery.addScalar("categoria", IntegerType.INSTANCE);
				//return (AvvisoMessaggioDto) sqlQuery.uniqueResult();
				AvvisoMessaggioDto tmpobj = (AvvisoMessaggioDto) sqlQuery.uniqueResult();
				return tmpobj;
				
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public List<EmailRivenditaDto> getEmailInviatiDaRivendita(Integer codRivendita, Timestamp dataDa, Timestamp dataA) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EmailRivenditaVo.class, "er");
		criteria.createCriteria("er.edicola", "ed");
		criteria.add(Restrictions.eq("ed.codEdicola", codRivendita));
		criteria.add(Restrictions.between("er.dataMessaggio", dataDa, dataA));
		criteria.addOrder(Order.desc("er.dataMessaggio"));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("er.idEmailRivendita"), "idEmailRivendita");
		properties.add(Projections.property("er.dataMessaggio"), "dataMessaggio"); 
		properties.add(Projections.property("er.titolo"), "titolo");
		properties.add(Projections.property("er.messaggio"), "messaggio"); 
		properties.add(Projections.property("er.destinatari"), "destinatari");
		properties.add(Projections.property("er.allegato"), "allegato");
		properties.add(Projections.property("er.letto"), "letto");
		criteria.setProjection(properties);
		criteria.setResultTransformer(Transformers.aliasToBean(EmailRivenditaDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public EmailRivenditaDto getMessaggioInviatoDaRivendita(Integer idEmailRivendita) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EmailRivenditaVo.class, "er");
		criteria.createCriteria("er.edicola", "ed");
		criteria.add(Restrictions.eq("er.idEmailRivendita", idEmailRivendita));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("er.idEmailRivendita"), "idEmailRivendita");
		properties.add(Projections.property("er.dataMessaggio"), "dataMessaggio"); 
		properties.add(Projections.property("er.titolo"), "titolo");
		properties.add(Projections.property("er.messaggio"), "messaggio"); 
		properties.add(Projections.property("er.destinatari"), "destinatari");
		properties.add(Projections.property("er.allegato"), "allegato"); 
		criteria.setProjection(properties);
		criteria.setResultTransformer(Transformers.aliasToBean(EmailRivenditaDto.class));
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<GiroDto> getGiri(Integer codEdicola, Integer giroTipo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GiroTipoVo.class, "gtvo");
		criteria.add(Restrictions.eq("gtvo.pk.giroTipo", giroTipo));
		criteria.addOrder(Order.asc("gtvo.pk.giro"));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.distinct(Projections.projectionList().add(Projections.property("gtvo.pk.giro"))));
		properties.add(Projections.property("gtvo.pk.giro"), "codGiro");
		//properties.add(Projections.property("gtvo.pk.descGiro"), "descGiro"); 
		criteria.setProjection(properties);
		criteria.setResultTransformer(Transformers.aliasToBean(GiroDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<GiroDto> getZone(Integer codEdicola, Integer zonaTipo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ZonaTipoVo.class, "ztvo");
		criteria.add(Restrictions.eq("ztvo.pk.zonaTipo", zonaTipo));
		criteria.addOrder(Order.asc("ztvo.pk.zona"));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.distinct(Projections.projectionList().add(Projections.property("ztvo.pk.zona"))));
		properties.add(Projections.property("ztvo.pk.zona"), "codGiro");
		//properties.add(Projections.property("ztvo.pk.descZona"), "descZona"); 
		criteria.setProjection(properties);
		criteria.setResultTransformer(Transformers.aliasToBean(GiroDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<GiroTipoDto> getGiriTipo(Integer codFiegDl, Integer codEdicola, Integer codice, String desc) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GiroTipoVo.class, "gtvo");
		if (codice != null) {
			criteria.add(Restrictions.eq("gtvo.pk.giroTipo", codice));
		}
		if (desc != null) {
			//criteria.add(Restrictions.eq("", codice));
		}
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.distinct(Projections.projectionList().add(Projections.property("gtvo.pk.giroTipo"))));
		properties.add(Projections.property("gtvo.pk.giroTipo"), "idGiroTipo");
		//properties.add(Projections.property("gtvo.pk.descGiroTipo"), "descrizione"); 
		criteria.setProjection(properties);
		criteria.setResultTransformer(Transformers.aliasToBean(GiroTipoDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<GiroTipoDto> getZoneTipo(Integer codFiegDl, Integer codEdicola, Integer codice, String desc) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ZonaTipoVo.class, "ztvo");
		if (codice != null) {
			criteria.add(Restrictions.eq("ztvo.pk.zonaTipo", codice));
		}
		if (desc != null) {
			//criteria.add(Restrictions.eq("", codice));
		}
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.distinct(Projections.projectionList().add(Projections.property("ztvo.pk.zonaTipo"))));
		properties.add(Projections.property("ztvo.pk.zonaTipo"), "idGiroTipo");
		//properties.add(Projections.property("ztvo.pk.descZonaTipo"), "descrizione"); 
		criteria.setProjection(properties);
		criteria.setResultTransformer(Transformers.aliasToBean(GiroTipoDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<MessaggioDpeDto> getMessaggiDpe(Timestamp dataDa, Timestamp dataA, Boolean abilitati) {
		DetachedCriteria criteria = DetachedCriteria.forClass(MessaggioDpeVo.class, "mdvo");
		if (dataDa != null && dataA != null) {
			criteria.add(Restrictions.between("mdvo.data", dataDa, dataA));
		}
		if (abilitati != null) {
			criteria.add(Restrictions.eq("abilitato", abilitati));
		}
		criteria.addOrder(Order.asc("mdvo.priorita"));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("mdvo.codice"), "codice");
		props.add(Projections.property("mdvo.data"), "data");
		props.add(Projections.property("mdvo.titolo"), "titolo");
		props.add(Projections.property("mdvo.testo"), "testo");
		props.add(Projections.property("mdvo.url"), "url");
		props.add(Projections.property("mdvo.priorita"), "priorita");
		props.add(Projections.property("mdvo.abilitato"), "abilitato");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(MessaggioDpeDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public MessaggioDpeDto getMessaggioDpe(Long codMessaggio) {
		DetachedCriteria criteria = DetachedCriteria.forClass(MessaggioDpeVo.class, "mdvo");
		criteria.add(Restrictions.eq("mdvo.codice", codMessaggio));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("mdvo.codice"), "codice");
		props.add(Projections.property("mdvo.data"), "data");
		props.add(Projections.property("mdvo.titolo"), "titolo");
		props.add(Projections.property("mdvo.testo"), "testo");
		props.add(Projections.property("mdvo.url"), "url");
		props.add(Projections.property("mdvo.priorita"), "priorita");
		props.add(Projections.property("mdvo.abilitato"), "abilitato");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(MessaggioDpeDto.class));
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public MessaggioDpeVo getMessaggioDpeVo(Long codice) {
		DetachedCriteria criteria = DetachedCriteria.forClass(MessaggioDpeVo.class, "mdvo");
		criteria.add(Restrictions.ge("mdvo.codice", codice));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public Boolean hasMessaggiDpe() {
		DetachedCriteria criteria = DetachedCriteria.forClass(MessaggioDpeVo.class, "mdvo");
		criteria.add(Restrictions.eq("mdvo.abilitato", true));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.count("mdvo.codice"));
		criteria.setProjection(props);
		Number count = (Number) getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		return (count != null && count.intValue() > 0) ? true : false;
	}
	
	@Override
	public MessaggioIdtnVo getMessaggioIdtnVo(Integer codDl, Integer idtn) {
		DetachedCriteria criteria = DetachedCriteria.forClass(MessaggioIdtnVo.class, "mi");
		criteria.createCriteria("mi.anagraficaAgenziaVo", "aa");
		criteria.createCriteria("mi.storicoCopertineVo", "sc");
		criteria.add(Restrictions.eq("mi.codDl", codDl));
		criteria.add(Restrictions.eq("mi.idtn", idtn));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public MessaggioIdtnVo getMessaggioIdtnVo(Integer idMessaggioIdtn) {
		DetachedCriteria criteria = DetachedCriteria.forClass(MessaggioIdtnVo.class, "mi");
		criteria.createCriteria("mi.anagraficaAgenziaVo", "aa");
		criteria.createCriteria("mi.storicoCopertineVo", "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
		criteria.add(Restrictions.eq("mi.idMessaggioIdtn", idMessaggioIdtn));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public Timestamp getMaxSecondsDateMessaggio(Integer codFiegDl, Timestamp dtMessaggio) {
		Calendar startDate = Calendar.getInstance();
		startDate.setTimeInMillis(dtMessaggio.getTime());
		startDate.set(Calendar.SECOND, 0);
		
		Calendar endDate = Calendar.getInstance();
		endDate.setTimeInMillis(dtMessaggio.getTime());
		endDate.set(Calendar.SECOND, 59);
		
		DetachedCriteria criteria = DetachedCriteria.forClass(MessaggioVo.class, "m");
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.max("m.pk.dtMessaggio"));
		criteria.setProjection(props);
		criteria.add(Restrictions.eq("m.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.between("m.pk.dtMessaggio", new Timestamp(startDate.getTimeInMillis()), new Timestamp(endDate.getTimeInMillis())));
		
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public MessaggioClienteVo getMessaggioCliente(Long codMessaggio) {
		DetachedCriteria criteria = DetachedCriteria.forClass(MessaggioClienteVo.class, "mc");
		criteria.add(Restrictions.eq("mc.codice", codMessaggio));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public List<MessaggioClienteVo> getMessaggioCliente(Integer codEdicola, Date dataDa, Date dataA, String nome, String cognome, String codFiscale, String piva) {
		DetachedCriteria criteria = DetachedCriteria.forClass(MessaggioClienteVo.class, "mc");
		criteria.createCriteria("mc.cliente", "cli");
		criteria.add(Restrictions.eq("mc.codEdicola", codEdicola));
		criteria.add(Restrictions.between("mc.dataMessaggio", DateUtilities.floorDay(dataDa), DateUtilities.ceilDay(dataA)));
		if (!Strings.isNullOrEmpty(nome)) {
			criteria.add(Restrictions.ilike("cli.nome", nome, MatchMode.START));
		}
		if (!Strings.isNullOrEmpty(cognome)) {
			criteria.add(Restrictions.ilike("cli.cognome", cognome, MatchMode.START));
		}
		if (!Strings.isNullOrEmpty(codFiscale)) {
			criteria.add(Restrictions.eq("cli.codiceFiscale", codFiscale));
		}
		if (!Strings.isNullOrEmpty(piva)) {
			criteria.add(Restrictions.eq("cli.piva", piva));
		}
		criteria.addOrder(Order.desc("mc.dataMessaggio")).addOrder(Order.asc("cli.nome"));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public MessaggioVo getMessaggioRilevamenti(Integer codFiegDl, Integer codEdicola) {
		Timestamp now = getSysdate();
		DetachedCriteria criteria = DetachedCriteria.forClass(MessaggioVo.class, "ms");
		criteria.add(Restrictions.eq("ms.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.between("ms.pk.dtMessaggio", DateUtilities.floorDay(now), DateUtilities.ceilDay(now)));
		criteria.add(Restrictions.eq("ms.pk.tipoDestinatario", IGerivConstants.COD_EDICOLA_SINGOLA));
		criteria.add(Restrictions.eq("ms.pk.destinatarioA", codEdicola));
		criteria.add(Restrictions.eq("ms.pk.destinatarioB", IGerivConstants.COD_TUTTE_LE_EDICOLE));
		criteria.add(Restrictions.eq("ms.statoMessaggio", IGerivConstants.STATO_MESSAGGIO_INVIATO));
		criteria.add(Restrictions.eq("ms.categoria", IGerivConstants.CATEGORIA_MESSAGGIO_AVVISO_RILEVAMENTI));
		criteria.add(Restrictions.ge("ms.dtScadenzaMessaggio", now));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
}
