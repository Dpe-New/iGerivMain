package it.dpe.igeriv.bo.vendite;


import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.selectUnique;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.IdVenditaProdottNonEditorialeDto;
import it.dpe.igeriv.dto.IntervalloVenditaDto;
import it.dpe.igeriv.dto.KeyValueVenditaDto;
import it.dpe.igeriv.dto.PositionSizeDto;
import it.dpe.igeriv.dto.ProdottoClientVenditeDto;
import it.dpe.igeriv.dto.PubblicazioneLocalVenditeDto;
import it.dpe.igeriv.dto.VenditaDettaglioDto;
import it.dpe.igeriv.dto.VenditaRiepilogoDto;
import it.dpe.igeriv.dto.VendutoGiornalieroDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.BarraSceltaRapidaDestraVo;
import it.dpe.igeriv.vo.BarraSceltaRapidaProdottiVariVo;
import it.dpe.igeriv.vo.BarraSceltaRapidaSinistraVo;
import it.dpe.igeriv.vo.MessaggioRegistratoreCassaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiBollaDettaglioVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiBollaVo;
import it.dpe.igeriv.vo.RegistratoreCassaVo;
import it.dpe.igeriv.vo.RichiestaAggiornamentoBarcodeVo;
import it.dpe.igeriv.vo.VenditaDettaglioVo;
import it.dpe.igeriv.vo.VenditaVo;
import it.dpe.igeriv.vo.pk.BarraSceltaRapidaDestraPk;
import it.dpe.igeriv.vo.pk.BarraSceltaRapidaSinistraPk;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import ch.lambdaj.group.Group;

import com.google.common.base.Strings;

@Repository
class VenditeRepositoryImpl extends BaseRepositoryImpl implements VenditeRepository {
	private final int contiMaxResults;
	
	@Autowired
	VenditeRepositoryImpl(BaseDao<VenditaVo> dao, @Value("${igeriv.vendite.conti.max.results}") int contiMaxResults) {
		super(dao);
		this.contiMaxResults = contiMaxResults;
	}
	
	@Override
	public List<VenditaDettaglioDto> getContiVendite(final List<Long> idConti, final String codUtente) {	
		HibernateCallback<List<VenditaDettaglioDto>> action = new HibernateCallback<List<VenditaDettaglioDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<VenditaDettaglioDto> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String whereClauseUtente = "";
				String whereClauseUtenteProdotti = "";
				if (!Strings.isNullOrEmpty(codUtente)) {
					whereClauseUtente = "and codut9625 = :codUtente";
					whereClauseUtenteProdotti = "and codut9545 = :codUtente";
				}
				String query = IGerivQueryContants.SQL_QUERY_GET_CONTI_VENDITA.replace(":wcUtente", whereClauseUtente).replace(":wcProd", whereClauseUtenteProdotti);
				if (!idConti.isEmpty()) {
					StringBuffer sb = new StringBuffer();
					for (int i = 0, n = idConti.size(); i < n; i++) {
						sb.append(String.format(":id%d, ", i));
					}
					sb.append("-1");
					query = query.replaceAll(":ids", sb.toString());
				}
				SQLQuery sqlQuery = session.createSQLQuery(query);
				if (!idConti.isEmpty()) {
					for (int i = 0, n = idConti.size(); i < n; i++) {
						sqlQuery.setLong(String.format("id%d", i), (Long) idConti.get(i));
					}
				}
				if (!Strings.isNullOrEmpty(codUtente)) {
					sqlQuery.setString("codUtente", codUtente);
				}
				sqlQuery.setResultTransformer( Transformers.aliasToBean(VenditaDettaglioDto.class));
				sqlQuery.addScalar("idVendita", LongType.INSTANCE);
				sqlQuery.addScalar("dataVendita", TimestampType.INSTANCE);
				sqlQuery.addScalar("prodottoNonEditoriale", BooleanType.INSTANCE);
				sqlQuery.addScalar("barcode", StringType.INSTANCE);
				sqlQuery.addScalar("idProdotto", LongType.INSTANCE);
				sqlQuery.addScalar("idtn", IntegerType.INSTANCE);
				sqlQuery.addScalar("numeroCopertina", StringType.INSTANCE);
				sqlQuery.addScalar("prezzoCopertina", BigDecimalType.INSTANCE);
				sqlQuery.addScalar("importoTotale", BigDecimalType.INSTANCE);
				sqlQuery.addScalar("progressivo", IntegerType.INSTANCE);
				sqlQuery.addScalar("quantita", IntegerType.INSTANCE);
				sqlQuery.addScalar("sottoTitolo", StringType.INSTANCE);
				sqlQuery.addScalar("titolo", StringType.INSTANCE);
				sqlQuery.addScalar("trasferitaGestionale", IntegerType.INSTANCE);
				sqlQuery.addScalar("idDocumentoProdottiVari", LongType.INSTANCE);
				sqlQuery.addScalar("dataUscita", TimestampType.INSTANCE);
				sqlQuery.addScalar("codFiegDl", IntegerType.INSTANCE);
				sqlQuery.addScalar("aliquota", IntegerType.INSTANCE);
				sqlQuery.addScalar("ragSocClientePrimaRiga", StringType.INSTANCE);
				sqlQuery.addScalar("ragSocClienteSecondaRiga", StringType.INSTANCE);
				//GIFT CARD
				sqlQuery.addScalar("flagProDigitale",StringType.INSTANCE); //fprodig9506
                return sqlQuery.list();
			}
			
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public List<IdVenditaProdottNonEditorialeDto> getLastContiVendita(final Integer codFiegDlMaster, final Integer codEdicolaMaster, final String codUtente, final Date dateInizio, final Date dateFine, final boolean limitResults) {
		HibernateCallback<List<IdVenditaProdottNonEditorialeDto>> action = new HibernateCallback<List<IdVenditaProdottNonEditorialeDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<IdVenditaProdottNonEditorialeDto> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String query = IGerivQueryContants.SQL_QUERY_GET_LAST_ID_CONTI_VENDITA.replace(":wcUtente", (!Strings.isNullOrEmpty(codUtente) ? "and codut9625 = :codUtente" : ""));
				SQLQuery sqlQuery = session.createSQLQuery(query);
				sqlQuery.setInteger("codFiegDl", codFiegDlMaster);
				sqlQuery.setInteger("codEdicola", codEdicolaMaster);
				if (!Strings.isNullOrEmpty(codUtente)) {
					sqlQuery.setString("codUtente", codUtente);
				}
				sqlQuery.setTimestamp("dataVenditaIni", DateUtilities.floorDay(dateInizio));
				sqlQuery.setTimestamp("dataVenditaFine", DateUtilities.ceilDay(dateFine));
				if (limitResults) {
					sqlQuery.setMaxResults(contiMaxResults);
				}
				sqlQuery.setResultTransformer(Transformers.aliasToBean(IdVenditaProdottNonEditorialeDto.class));
				sqlQuery.addScalar("codVendita", LongType.INSTANCE);
				sqlQuery.addScalar("idDocumentoProdottiVari", LongType.INSTANCE);
				sqlQuery.addScalar("dataVendita", TimestampType.INSTANCE);
				sqlQuery.addScalar("trasferitaGestionale", IntegerType.INSTANCE);
				sqlQuery.addScalar("dataEstrattoConto", TimestampType.INSTANCE);
				sqlQuery.addScalar("codCliente", LongType.INSTANCE);
				sqlQuery.addScalar("nome", StringType.INSTANCE);
				sqlQuery.addScalar("cognome", StringType.INSTANCE);
				sqlQuery.addScalar("idScontrino", StringType.INSTANCE);
				sqlQuery.addScalar("fatturaEmessa", BooleanType.INSTANCE);
				sqlQuery.addScalar("fatturaContoUnico", BooleanType.INSTANCE);
				sqlQuery.addScalar("numeroFattura", IntegerType.INSTANCE);
                return sqlQuery.list();
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public List<VenditaRiepilogoDto> getVenditeRiepilogo(final Integer codFiegDl,
			final Integer codEdicola, final String codUtente, final Timestamp dataDa, final Timestamp dataA,
			final Integer codRaggruppamento) {
		HibernateCallback<List<VenditaRiepilogoDto>> action = new HibernateCallback<List<VenditaRiepilogoDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<VenditaRiepilogoDto> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery sqlQuery = null;
				String whereClauseUtente = "";
				String whereClauseUtenteProdotti = "";
				if (!Strings.isNullOrEmpty(codUtente)) {
					whereClauseUtente = "and codut9625 = :codUtente";
					whereClauseUtenteProdotti = "and codut9545 = :codUtente";
				}
				if (codRaggruppamento.equals(IGerivConstants.COD_RAGGRUPPAMENTO_PERIODICITA)) {
					sqlQuery = session.createSQLQuery("select * from (" + IGerivQueryContants.SQL_QUERY_GET_VENDITE_RIEPILOGO_PER_PERIODICITA.replace(":wcClauseUtente", whereClauseUtente)
							+ " " + IGerivQueryContants.SQL_QUERY_GET_VENDITE_RIEPILOGO_PRODOTTI_VARI.replace(":label", "'" + IGerivMessageBundle.get("igeriv.prodotti.vari") + "'").replace(":wcProdotti", whereClauseUtenteProdotti)
							+ " " + IGerivQueryContants.SQL_QUERY_GET_VENDITE_RIEPILOGO_A_VALORE.replace(":label", "'" + IGerivMessageBundle.get("igeriv.vendite.a.valore") + "'").replace(":wcClauseUtente", whereClauseUtente)
							+ ") order by ordine");
				} else {
					sqlQuery = session.createSQLQuery("select * from (" + IGerivQueryContants.SQL_QUERY_GET_VENDITE_RIEPILOGO_PER_ARGOMENTO.replace(":wcClauseUtente", whereClauseUtente)
							+ " " + IGerivQueryContants.SQL_QUERY_GET_VENDITE_RIEPILOGO_PRODOTTI_VARI.replace(":label", "'" + IGerivMessageBundle.get("igeriv.prodotti.vari") + "'").replace(":wcProdotti", whereClauseUtenteProdotti)
							+ " " + IGerivQueryContants.SQL_QUERY_GET_VENDITE_RIEPILOGO_A_VALORE.replace(":label", "'" + IGerivMessageBundle.get("igeriv.vendite.a.valore") + "'").replace(":wcClauseUtente", whereClauseUtente)
							+ ") order by ordine");
				}
				sqlQuery.setInteger("codDl", codFiegDl);
				sqlQuery.setTimestamp("dtIni", DateUtilities.floorDay(dataDa));
				sqlQuery.setTimestamp("dtFine", DateUtilities.ceilDay(dataA));
				sqlQuery.setInteger("codEdicola", codEdicola);
				if (!Strings.isNullOrEmpty(codUtente)) {
					sqlQuery.setString("codUtente", codUtente);
				}
				sqlQuery.setResultTransformer( Transformers.aliasToBean(VenditaRiepilogoDto.class));
				
				sqlQuery.addScalar("importo", BigDecimalType.INSTANCE);
				sqlQuery.addScalar("importoNetto", BigDecimalType.INSTANCE);
				sqlQuery.addScalar("copie", IntegerType.INSTANCE);
				sqlQuery.addScalar("raggruppamento", StringType.INSTANCE);
				
				return sqlQuery.list();
			}
		};
		List<VenditaRiepilogoDto> findByDetachedCriteria = getDao().findByHibernateCallback(action);
		for (VenditaRiepilogoDto dto: findByDetachedCriteria) {
			if (dto.getImporto() != null && dto.getImportoNetto() != null) {
				dto.setAggio(dto.getImporto().subtract(dto.getImportoNetto()));
			}
		}
		return findByDetachedCriteria;
	}
	
	@Override
	public void deleteBarraMenuSceltaRapida(final Integer[] codFiegDl, final Integer[] codEdicola) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				Query createQuery = session.createQuery(IGerivQueryContants.HQL_DELETE_BARRA_MENU_SCELTA_RAPIDA_SX);
				createQuery.setParameterList("codFiegDl", codFiegDl);
				createQuery.setParameterList("codEdicola", codEdicola);
				
				Query createQuery1 = session.createQuery(IGerivQueryContants.HQL_DELETE_BARRA_MENU_SCELTA_RAPIDA_DX);
				createQuery1.setParameterList("codFiegDl", codFiegDl);
				createQuery1.setParameterList("codEdicola", codEdicola);
				
				return (createQuery.executeUpdate() + createQuery1.executeUpdate());
			}
		};
		getDao().executeByHibernateCallback(action);
	}
	
	@Override
	public VendutoGiornalieroDto getVendutoGionaliero(Integer codFiegDl, Integer codEdicola) {
		Date now = new Date();
		Timestamp dataDa = DateUtilities.floorDay(now);
		Timestamp dataA = DateUtilities.ceilDay(now);
		BigDecimal sommaVendite = getDao().findUniqueResultObjectByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_SOMMA_VENDITE_INTERVALLO, codFiegDl, codEdicola, dataDa, dataA);
		Double sommaVenditeProdottiVari = getDao().findUniqueResultObjectByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_SOMMA_VENDITE_PRODOTTI_VARI_INTERVALLO, codEdicola, dataDa, dataA);
		BigDecimal sommaVend = sommaVendite == null ? new BigDecimal(0) : sommaVendite;
		BigDecimal sommaProd = sommaVenditeProdottiVari == null ? new BigDecimal(0) : new BigDecimal(sommaVenditeProdottiVari);
		return new VendutoGiornalieroDto(sommaVend, sommaProd);
	}
	
	@Override
	public VenditaVo getContoVendite(Long codContoVendite) {
		return getDao().findUniqueResultObjectByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_CONTO_VENDITA_BY_PK, codContoVendite);
	}
	
	@Override
	public void updateBarraSceltaRapidaVo(Integer codFiegDl, Integer codEdicola, PositionSizeDto psDto) {
		if (psDto.getTop() != null && psDto.getLeft() != null && psDto.getWidth() != null && psDto.getHeight() != null) {
			getDao().bulkUpdate(IGerivQueryContants.HQL_UPDATE_POSIZIONE_DIMENSIONI_BARRA_MENU_SCELTA_RAPIDA, new Object[]{psDto.getTop(), psDto.getLeft(), psDto.getWidth(), psDto.getHeight(), codFiegDl, codEdicola});
		}
	}
	
	@Override
	public List<VenditaDettaglioDto> getContiVenditeDettaglio(Integer codFiegDl, Integer codEdicola, String codUtente, Timestamp dataDa, Timestamp dataA, Integer tipoProdotto) {
		List<VenditaDettaglioDto> listVenditaDettaglio = null;
		List<IdVenditaProdottNonEditorialeDto> listCodVenditaCodProdotti = getLastContiVendita(codFiegDl, codEdicola, codUtente, dataDa, dataA, false);
		if (listCodVenditaCodProdotti != null && !listCodVenditaCodProdotti.isEmpty()) {
			List<Long> listCodVendita = extract(listCodVenditaCodProdotti, on(IdVenditaProdottNonEditorialeDto.class).getCodVendita());
			listVenditaDettaglio = getContiVendite(listCodVendita, codUtente);
			if (tipoProdotto != null) {
				if (tipoProdotto.equals(0)) {
					listVenditaDettaglio = filter(having(on(VenditaDettaglioDto.class).getProdottoNonEditoriale(), equalTo(false)).and(having(on(VenditaDettaglioDto.class).getTitolo(), not(equalTo(IGerivMessageBundle.get("label.various.print.Value_Sell_Label"))))), listVenditaDettaglio);
				} else if (tipoProdotto.equals(1)) {
					listVenditaDettaglio = filter(having(on(VenditaDettaglioDto.class).getProdottoNonEditoriale(), equalTo(true)), listVenditaDettaglio);
				} else if (tipoProdotto.equals(2)) {
					listVenditaDettaglio = filter(having(on(VenditaDettaglioDto.class).getProdottoNonEditoriale(), equalTo(false)).and(having(on(VenditaDettaglioDto.class).getTitolo(), equalTo(IGerivMessageBundle.get("label.various.print.Value_Sell_Label")))), listVenditaDettaglio);
				}
			} 
		}
		return listVenditaDettaglio;
	}
	
	@Override
	public BarraSceltaRapidaSinistraVo getBarraSceltaRapidaSinistra(Integer codEdicola, Integer codFiegDl, Integer cpu) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BarraSceltaRapidaSinistraVo.class, "bsrvo");
		criteria.add(Restrictions.eq("bsrvo.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("bsrvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("bsrvo.pk.codicePubblicazione", cpu));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public BarraSceltaRapidaDestraVo getBarraSceltaRapidaDestra(Integer codEdicola, Integer codFiegDl, Integer cpu) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BarraSceltaRapidaDestraVo.class, "bsrvo");
		criteria.add(Restrictions.eq("bsrvo.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("bsrvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("bsrvo.pk.codicePubblicazione", cpu));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public void saveBarraSceltaRapidaSinistra(String[] codPubblicazioni, Integer[] arrCoddl, Integer[] arrCodEdicola, String[] nomiImmagine, String[] tipiImmagine) {
		int pos = 1;
		for (int i = 0; i < codPubblicazioni.length; i++) {
			if (!codPubblicazioni[i].trim().equals("")) {
				Integer coddl = arrCoddl[i];
				Integer codEdicolaWeb = arrCodEdicola[i];
				Integer codicePubblicazione = Integer.valueOf(codPubblicazioni[i].trim());
				String nomeImg = !Strings.isNullOrEmpty(nomiImmagine[i]) ? nomiImmagine[i] : "";
				Integer tipoImg = !Strings.isNullOrEmpty(tipiImmagine[i]) ? new Integer(tipiImmagine[i]) : IGerivConstants.COD_TIPO_IMMAGINE_MINIATURA;
				BarraSceltaRapidaSinistraVo vo = new BarraSceltaRapidaSinistraVo();
				BarraSceltaRapidaSinistraPk pk = new BarraSceltaRapidaSinistraPk();
				pk.setCodEdicola(codEdicolaWeb);
				pk.setCodFiegDl(coddl);
				pk.setCodicePubblicazione(codicePubblicazione);
				vo.setPk(pk);
				vo.setPosizione(pos++);
				vo.setNomeImmagine(nomeImg);
				vo.setTipoImmagine(tipoImg);
				saveBaseVo(vo);
			}
		}
	}
	
	@Override
	public void saveBarraSceltaRapidaDestra(String[] codPubblicazioni, Integer[] arrCoddl, Integer[] arrCodEdicola, String[] nomiImmagine, String[] tipiImmagine) {
		int pos = 1;
		for (int i = 0; i < codPubblicazioni.length; i++) {
			if (!codPubblicazioni[i].trim().equals("")) {
				Integer coddl = arrCoddl[i];
				Integer codEdicolaWeb = arrCodEdicola[i];
				Integer codicePubblicazione = Integer.valueOf(codPubblicazioni[i].trim());
				String nomeImg = !Strings.isNullOrEmpty(nomiImmagine[i]) ? nomiImmagine[i] : "";
				Integer tipoImg = !Strings.isNullOrEmpty(tipiImmagine[i]) ? new Integer(tipiImmagine[i]) : IGerivConstants.COD_TIPO_IMMAGINE_MINIATURA;
				BarraSceltaRapidaDestraVo vo = new BarraSceltaRapidaDestraVo();
				BarraSceltaRapidaDestraPk pk = new BarraSceltaRapidaDestraPk();
				pk.setCodEdicola(codEdicolaWeb);
				pk.setCodFiegDl(coddl);
				pk.setCodicePubblicazione(codicePubblicazione);
				vo.setPk(pk);
				vo.setPosizione(pos++);
				vo.setNomeImmagine(nomeImg);
				vo.setTipoImmagine(tipoImg);
				saveBaseVo(vo);
			}
		}
	}
	
	@Override
	public void deleteListVenditaDettaglio(Long codVendita) {
		getDao().bulkUpdate("delete from VenditaDettaglioVo vo where vo.pk.venditaVo.codVendita = ?", new Object[]{codVendita});
	}
	
	@Override
	public RichiestaAggiornamentoBarcodeVo getRichiestaAggiornamentoBarcodeVo(Integer coddl, Integer idtn, String barcode, Boolean richiestaEseguita) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaAggiornamentoBarcodeVo.class, "ra");
		criteria.add(Restrictions.eq("ra.pk.codFiegDl", coddl));
		criteria.add(Restrictions.eq("ra.pk.idtn", idtn));
		criteria.add(Restrictions.eq("ra.pk.codiceBarre", barcode));
		criteria.add(Restrictions.eq("ra.richiestaEseguita", richiestaEseguita));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public List<RichiestaAggiornamentoBarcodeVo> getRichiesteEseguiteAggiornamentoBarcodeVo(Integer coddl, Date dataDa) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaAggiornamentoBarcodeVo.class, "ra");
		criteria.add(Restrictions.eq("ra.pk.codFiegDl", coddl));
		criteria.add(Restrictions.eq("ra.richiestaEseguita", true));
		criteria.add(Restrictions.ge("ra.dataEsecuzione", dataDa));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public Long getCountRichiesteAggiornamentoNelPeriodo(Integer coddl, Integer codEdicola, java.util.Date dataIni, java.util.Date dataFine) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaAggiornamentoBarcodeVo.class, "ra");
		criteria.add(Restrictions.eq("ra.pk.codFiegDl", coddl));
		criteria.add(Restrictions.eq("ra.codEdicola", codEdicola));
		criteria.add(Restrictions.between("ra.dataInvioRichiesta", dataIni, dataFine));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.count("ra.pk.idtn"));
		criteria.setProjection(props);
		Long count = (Long) getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		return count;
	}
	
	@Override
	public List<PubblicazioneLocalVenditeDto> getLocalDataVendite(final Integer[] codDl, final Integer[] codEdicola) {
		HibernateCallback<List<PubblicazioneLocalVenditeDto>> action = new HibernateCallback<List<PubblicazioneLocalVenditeDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<PubblicazioneLocalVenditeDto> doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = IGerivQueryContants.SQL_QUERY_GET_UTLIME_PUBBLICAZIONI_PER_VENDITE_LOCALI;
				sql = sql.replaceAll(":addWc", (codDl.length > 1 && (codEdicola != null && codEdicola.length > 1)) ? IGerivQueryContants.SQL_QUERY_GET_UTLIME_PUBBLICAZIONI_PER_VENDITE_LOCALI_MULTI_DL_WHERE_CLAUSE : "");
				StringBuffer sb = new StringBuffer();
				for (int i = 0, n = codDl.length; i < n; i++) {
					sb.append(String.format(":coddl%d, ", i));
				}
				sb.append("-1");
				sql = sql.replaceAll(":coddl", sb.toString());
				if (codEdicola != null && codEdicola.length > 1) {
					StringBuffer sb1 = new StringBuffer();
					for (int i = 0, n = codEdicola.length; i < n; i++) {
						sb1.append(String.format(":codEdicola%d, ", i));
					}
					sb1.append("-1");
					sql = sql.replaceAll(":codEdicola", sb1.toString());
				}
				SQLQuery createSQLQuery = session.createSQLQuery(sql);
				for (int i = 0, n = codDl.length; i < n; i++) {
					createSQLQuery.setInteger(String.format("coddl%d", i), codDl[i]);
				}
				if (codDl.length > 1 && (codEdicola != null && codEdicola.length > 1)) {
					for (int i = 0, n = codEdicola.length; i < n; i++) {
						createSQLQuery.setInteger(String.format("codEdicola%d", i), codEdicola[i]);
					}
				}
				Timestamp today = DateUtilities.floorDay(new Date());
				Date cutoffDateSettimanali = DateUtilities.togliGiorni(today, 6);
				Date cutoffDateMensili = DateUtilities.togliGiorni(today, 20);
				createSQLQuery.setTimestamp("today", today);
				createSQLQuery.setTimestamp("cutoffDateSettimanali", cutoffDateSettimanali);
				createSQLQuery.setTimestamp("cutoffDateMensili", cutoffDateMensili);
				createSQLQuery.setResultTransformer( Transformers.aliasToBean(PubblicazioneLocalVenditeDto.class));
				createSQLQuery.addScalar("idtn", IntegerType.INSTANCE);
				createSQLQuery.addScalar("barcode", StringType.INSTANCE);
				createSQLQuery.addScalar("prezzoCopertina",BigDecimalType.INSTANCE);
				createSQLQuery.addScalar("codicePubblicazione", IntegerType.INSTANCE);
				createSQLQuery.addScalar("numeroCopertina",StringType.INSTANCE);
				createSQLQuery.addScalar("titolo", StringType.INSTANCE);
				createSQLQuery.addScalar("sottoTitolo", StringType.INSTANCE);
				createSQLQuery.addScalar("dataUscita",TimestampType.INSTANCE);
				createSQLQuery.addScalar("coddl", IntegerType.INSTANCE);
				createSQLQuery.addScalar("codInizioQuotidiano", IntegerType.INSTANCE);
				createSQLQuery.addScalar("codFineQuotidiano", IntegerType.INSTANCE);
				createSQLQuery.addScalar("strData", StringType.INSTANCE);
				return createSQLQuery.list();
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public List<RegistratoreCassaVo> getRegistratoriCassa() {
		DetachedCriteria criteria = DetachedCriteria.forClass(RegistratoreCassaVo.class, "reg");
		criteria.addOrder(Order.asc("reg.modello"));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public RegistratoreCassaVo getRegistratoreCassa(Integer codRegCassa) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RegistratoreCassaVo.class, "reg");
		criteria.add(Restrictions.eq("reg.codRegCassa", codRegCassa));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public void resetCodClienteVendite(Long codCliente) {
		getDao().bulkUpdate("update VenditaVo vo set vo.codCliente = null where codCliente = ?", new Object[]{codCliente});
	}
	
	@Override
	public MessaggioRegistratoreCassaVo getMessaggioRegistratoreCassa(Integer codEdicola, Integer codRegCassa) {
		DetachedCriteria criteria = DetachedCriteria.forClass(MessaggioRegistratoreCassaVo.class, "ms");
		criteria.add(Restrictions.eq("ms.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("ms.pk.codRegCassa", codRegCassa));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public void deleteVenditaDettaglio(final String pk) {
		String[] arrProfilo = pk.split(",");
		List<KeyValueVenditaDto> listKeyValueVenditaDto = new ArrayList<KeyValueVenditaDto>();
		for (int i = 0; i < arrProfilo.length; i++) {
			String pkValue = arrProfilo[i].trim();
			String[] split = pkValue.split("\\|");
			KeyValueVenditaDto dto = new KeyValueVenditaDto();
			dto.setCodVendita(new Long(split[0]));
			dto.setProgressivo(new Integer(split[1]));
			dto.setIsProdottoNonEditoriale(Boolean.valueOf(split[2]));
			listKeyValueVenditaDto.add(dto);
		}
		Group<KeyValueVenditaDto> group = group(listKeyValueVenditaDto, by(on(KeyValueVenditaDto.class).getCodVendita()));
		for (Group<KeyValueVenditaDto> subgroup : group.subgroups()) {
			List<KeyValueVenditaDto> findAll = subgroup.findAll();
			if (!findAll.isEmpty()) {
				
				
				Long codVendita = findAll.get(0).getCodVendita();
				VenditaVo contoVendite = getContoVendite(codVendita);
				//13/02/2017 - Cancellazione dei prodotti non editoriali ( anomalia riscontata Edicola J&V )
				//contoVendite = (contoVendite == null) ? getContoVenditeByIdProdotti(codVendita) : contoVendite;
				contoVendite = (contoVendite == null || findAll.get(0).getIsProdottoNonEditoriale()) ? getContoVenditeByIdProdotti(codVendita) : contoVendite;
				
				if (contoVendite != null) {
					List<VenditaDettaglioVo> listVenditaDettaglio = contoVendite.getListVenditaDettaglio();
					ProdottiNonEditorialiBollaVo contoProdottiVari = contoVendite.getContoProdottiVari();
					List<ProdottiNonEditorialiBollaDettaglioVo> dettagliProdotti = contoProdottiVari != null ? contoProdottiVari.getDettagli() : null;
					BigDecimal importoTotale = contoVendite.getImportoTotale();
					BigDecimal newImportoTotale = new BigDecimal(0);
					for (KeyValueVenditaDto dto : findAll) {
						if (dto.getIsProdottoNonEditoriale() && dettagliProdotti != null && !dettagliProdotti.isEmpty()) {
							ProdottiNonEditorialiBollaDettaglioVo prodotto = selectUnique(dettagliProdotti, having(on(ProdottiNonEditorialiBollaDettaglioVo.class).getPk().getIdDocumento(), equalTo(dto.getCodVendita())).and(having(on(ProdottiNonEditorialiBollaDettaglioVo.class).getPk().getProgressivo(), equalTo(dto.getProgressivo()))));
							if (prodotto != null) {
								BigDecimal imp = prodotto.getPrezzoCopertina().multiply(new BigDecimal(prodotto.getQuantita()));
								newImportoTotale = newImportoTotale.add(imp);
								prodotto.setDeleted(true);
							}
						} else if (listVenditaDettaglio != null && !listVenditaDettaglio.isEmpty()) {
							VenditaDettaglioVo pubblicazione = selectUnique(listVenditaDettaglio, having(on(VenditaDettaglioVo.class).getPk().getCodVendita(), equalTo(dto.getCodVendita())).and(having(on(VenditaDettaglioVo.class).getPk().getProgressivo(), equalTo(dto.getProgressivo()))));
							if (pubblicazione != null) {
								BigDecimal imp = pubblicazione.getPrezzoCopertina().multiply(new BigDecimal(pubblicazione.getQuantita()));
								newImportoTotale = newImportoTotale.add(imp);
								pubblicazione.setDeleted(true);
							}
						}
					}
					if (importoTotale.equals(newImportoTotale)) {
						contoVendite.setDeleted(true);
						contoVendite.setImportoTotale(new BigDecimal(0));
					} else {
						contoVendite.setImportoTotale(importoTotale.subtract(newImportoTotale));
					}
					getDao().update(contoVendite);
				}
			}
		}
	}
	
	private VenditaVo getContoVenditeByIdProdotti(Long idDocumentoProdottiVari) {
		return getDao().findUniqueResultObjectByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_CONTO_VENDITA_BY_ID_PRODOTTI, idDocumentoProdottiVari);
	}
	
	@Override
	public void updateVenditePerStornoFattura(Long codCliente, Integer numFattura) {
		getDao().bulkUpdate("update VenditaVo vo set vo.dataEstrattoConto = null, vo.fatturaEmessa = 0, vo.fatturaContoUnico = 0, vo.idFattura = null where vo.codCliente = ? and vo.idFattura = ?", new Object[]{codCliente, numFattura});
	}
	
	@Override
	public void restoreVenditaDettaglio(String pk) {
		String[] arrProfilo = pk.split(",");
		for (int i = 0; i < arrProfilo.length; i++) {
			String pkValue = arrProfilo[i].trim();
			String[] split = pkValue.split("\\|");
			Long codVendita = new Long(split[0]);
			Integer progressivo = new Integer(split[1]);
			Boolean prodottoNonEdit = Boolean.valueOf(split[2]);
			if (prodottoNonEdit) {
				DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiBollaDettaglioVo.class, "vdet");
				criteria.createCriteria("bolla", "bo");
				criteria.add(Restrictions.eq("vdet.pk.idDocumento", codVendita));
				criteria.add(Restrictions.eq("vdet.pk.progressivo", progressivo));
				ProdottiNonEditorialiBollaDettaglioVo prod = getDao().findUniqueResultByDetachedCriteria(criteria);
				prod.setDeleted(false);
				ProdottiNonEditorialiBollaVo bolla = prod.getBolla();
				bolla.setDeleted(false);
				saveBaseVo(prod);
				saveBaseVo(bolla);
			} else {
				DetachedCriteria criteria = DetachedCriteria.forClass(VenditaDettaglioVo.class, "vdet");
				criteria.createCriteria("vdet.venditaVo", "ve");
				criteria.add(Restrictions.eq("vdet.pk.venditaVo.codVendita", codVendita));
				criteria.add(Restrictions.eq("vdet.pk.progressivo", progressivo));
				VenditaDettaglioVo pubb = getDao().findUniqueResultByDetachedCriteria(criteria);
				VenditaVo venditaVo = pubb.getVenditaVo();
				venditaVo.setDeleted(false);
				pubb.setDeleted(false);
				saveBaseVo(venditaVo);
				saveBaseVo(pubb);
			}
		}
	}
	

	@Override
	public Boolean getHasRitiriCancellati(Integer[] arrId, Long codCliente) {
		DetachedCriteria criteria = DetachedCriteria.forClass(VenditaDettaglioVo.class, "ved");
		criteria.createCriteria("ved.venditaVo", "ve");
		criteria.add(Restrictions.eq("ve.codCliente", codCliente));
		criteria.add(Restrictions.in("ved.codEdicola", arrId));
		criteria.add(Restrictions.eq("ved.deleted", true));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.count("ved.idtn"));
		criteria.setProjection(props);
		Number count = (Number) getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		return (count != null && count.intValue() > 0) ? true : false;
	}
	
	@Override
	public List<IntervalloVenditaDto> getVenditeIntervalli(Integer codFiegDl, Integer codEdicola, Timestamp dataDa, Timestamp dataA) {
		// TODO 
		// RITORNARE LE VENDITE PRODOTTI VARI RAGGRUPPATE PER PERIODO / CATEGORIA
		/*
		 * select datar9545, sum((prezzo9546 * qt9546)) as importo 
			from tbl_9545, tbl_9546 
			where id9546 = id9545
			and datar9545 between sysdate-100 and sysdate
			and caus9546 = 1
			and crivw9545 = 949
			group by datar9545
			having sum((prezzo9546 * qt9546)) > 0;
		 */
		return null;
	}
	
	@Override
	public List<ProdottoClientVenditeDto> getProdottiClientVendite(Integer[] coddl,Integer[] codEdicola) {
		List<PubblicazioneLocalVenditeDto> localDataVendite = getLocalDataVendite(coddl, codEdicola);
		List<ProdottoClientVenditeDto> list = new ArrayList<>();
		for (PubblicazioneLocalVenditeDto dto : localDataVendite) {
			ProdottoClientVenditeDto pcv = new ProdottoClientVenditeDto();
			pcv.setBarcode(dto.getBarcode());
			pcv.setDataUscita(dto.getDataUscita());
			pcv.setIdtn(dto.getIdtn());
			pcv.setNumeroCopertina(dto.getNumeroCopertina());
			pcv.setPrezzoCopertina(dto.getPrezzoCopertina());
			pcv.setSottotitolo(dto.getSottoTitolo());
			pcv.setTitolo(dto.getTitolo());
			pcv.setCoddl(dto.getCoddl());
			pcv.setPrezzo(dto.getPrezzoCopertina() != null ? dto.getPrezzoCopertina().floatValue() : 0f);
			pcv.setCpu(dto.getCodicePubblicazione());
			pcv.setCiq(dto.getCodInizioQuotidiano());
			pcv.setCfq(dto.getCodFineQuotidiano());
			list.add(pcv);
		}
		return list;
	}
	
	@Override
	public PositionSizeDto getBarraSceltaRapidaProdottiVariPositionSizeDto(Integer codFiegDl, Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BarraSceltaRapidaProdottiVariVo.class, "bsr");
		criteria.add(Restrictions.eq("bsr.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("bsr.pk.codEdicola", codEdicola));
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("bsr.top"), "top");
		properties.add(Projections.property("bsr.left"), "left"); 
		properties.add(Projections.property("bsr.width"), "width");
		properties.add(Projections.property("bsr.height"), "height");
		properties.add(Projections.property("bsr.barraProdottiVariVisible"), "barraProdottiVariVisible");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(PositionSizeDto.class));
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public BarraSceltaRapidaProdottiVariVo getBarraSceltaRapidaProdottiVariVo(Integer codFiegDl, Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BarraSceltaRapidaProdottiVariVo.class, "bsr");
		criteria.add(Restrictions.eq("bsr.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("bsr.pk.codEdicola", codEdicola));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public int getCopieConsegnateGazzettaCard(final Integer codFiegDl, final Integer codEdicolaDl, final Timestamp dataUscita) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				//String query = String.format("select sum(copie) copie from (select Sum(copie9803) copie from tabella_9803@rae where dapro9803=:dataUscita AND coriv9803=:codRivendita union all select sum(copie9854) copie from tabella_9854@rae where dapro9854=:dataUscita and coriv9854=:codRivendita)");
				String query1 = String.format("select nvl(sum(copie9803),0) copie from tabella_9803@rae where dapro9803=:dataUscita and coriv9803=:codRivendita");
				SQLQuery sqlQuery1 = session.createSQLQuery(query1);
				sqlQuery1.setInteger("codRivendita", codEdicolaDl);
				sqlQuery1.setTimestamp("dataUscita", dataUscita);
				sqlQuery1.addScalar("copie", IntegerType.INSTANCE);
				int copieCard = (Integer) sqlQuery1.uniqueResult();

				String query2 = String.format("select nvl(sum(copie9854),0) copie from tabella_9854@rae where dapro9854=:dataUscita and coriv9854=:codRivendita");
				SQLQuery sqlQuery2 = session.createSQLQuery(query2);
				sqlQuery2.setInteger("codRivendita", codEdicolaDl);
				sqlQuery2.setTimestamp("dataUscita", dataUscita);
				sqlQuery2.addScalar("copie", IntegerType.INSTANCE);
				int copieMiniCard = (Integer) sqlQuery2.uniqueResult();

				return copieCard+copieMiniCard;
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}


	@Override
	public int getDistribuitoGazzetta(final Integer codFiegDl, final Integer codEdicolaWeb, final Timestamp dataUscita) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuilder query = new StringBuilder();
				query.append("select sum(copie) copie from (\n");
				query.append("select sum(quant9611) copie\n");
				query.append("from tbl_9607\n");
				query.append("inner join tbl_9611 on coddl9611=coddl9607 and idtn9611=idtn9607 and crivw9611=:codEdicolaWeb\n");
				query.append("where coddl9607=:codFiegDl\n");
				query.append("  and (cpu9607 between 1 and 7 or cpu9607 between 41 and 47)\n");
				query.append("  and datausc9607=:dataUscita\n");
				query.append("union all\n");
				query.append("select sum(quant9612b) copie\n");
				query.append("from tbl_9607\n");
				query.append("inner join tbl_9612b on coddl9612b=coddl9607 and idtn9612b=idtn9607 and crivw9612b=:codEdicolaWeb\n");
				query.append("where coddl9607=:codFiegDl\n");
				query.append("  and (cpu9607 between 1 and 7 or cpu9607 between 41 and 47)\n");
				query.append("  and datausc9607=:dataUscita\n");
				query.append(")\n");
				SQLQuery sqlQuery = session.createSQLQuery(query.toString());
				sqlQuery.setInteger("codEdicolaWeb", codEdicolaWeb);
				sqlQuery.setInteger("codFiegDl", codFiegDl);
				sqlQuery.setTimestamp("dataUscita", dataUscita);
				sqlQuery.addScalar("copie", IntegerType.INSTANCE);
				return (Integer) sqlQuery.uniqueResult();
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
}
