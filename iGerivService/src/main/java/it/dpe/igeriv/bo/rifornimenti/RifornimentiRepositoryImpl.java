package it.dpe.igeriv.bo.rifornimenti;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.forEach;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.sort;
import static org.hamcrest.Matchers.equalTo;
import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.bo.livellamenti.LivellamentiService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.EsportazioneDatiDlResultDto;
import it.dpe.igeriv.dto.PrenotazioneDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.dto.RichiestaClienteDto;
import it.dpe.igeriv.dto.RichiestaFissaClienteDto;
import it.dpe.igeriv.dto.RichiestaRifornimentoDto;
import it.dpe.igeriv.enums.StatoRichiestaLivellamento;
import it.dpe.igeriv.enums.StatoRichiestaRifornimentoLivellamento;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.util.IGerivUtils;
import it.dpe.igeriv.vo.BollaDettaglioVo;
import it.dpe.igeriv.vo.FondoBollaDettaglioVo;
import it.dpe.igeriv.vo.LivellamentiVo;
import it.dpe.igeriv.vo.PrenotazioneVo;
import it.dpe.igeriv.vo.RichiestaClienteVo;
import it.dpe.igeriv.vo.RichiestaFissaClienteEdicolaVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoLivellamentiVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoVo;
import it.dpe.igeriv.vo.RisposteClientiCodificateVo;
import it.dpe.igeriv.vo.pk.RichiestaClientePk;
import it.dpe.igeriv.vo.pk.RichiestaRifornimentoPk;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.driver.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.commons.collections.CollectionUtils;
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
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.nativejdbc.C3P0NativeJdbcExtractor;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import ch.lambdaj.group.Group;
import groovy.util.OrderBy;

import com.google.common.base.Strings;

@Repository
class RifornimentiRepositoryImpl extends BaseRepositoryImpl implements RifornimentiRepository {
	private final PubblicazioniService pubblicazioniService;
	private final LivellamentiService<RichiestaRifornimentoLivellamentiVo> livellamentiService;
	private final IGerivUtils iGerivUtils;
	
	@Autowired
	RifornimentiRepositoryImpl(BaseDao<RichiestaRifornimentoVo> dao, PubblicazioniService pubblicazioniService, LivellamentiService<RichiestaRifornimentoLivellamentiVo> livellamentiService, IGerivUtils iGerivUtils) {
		super(dao);
		this.pubblicazioniService = pubblicazioniService;
		this.livellamentiService = livellamentiService;
		this.iGerivUtils = iGerivUtils;
	}
	
	@Override  
	public List<RichiestaClienteDto> getRichiesteClienteByIdClienteViewOnly(Integer[] codFiegDl, Integer[] codEdicolaDl, List<Long> codCliente, String titolo, String stato, Integer provenienza, Timestamp dataDa, Timestamp dataA, Boolean filterDataSospensionePrenotazioni) {
		DetachedCriteria criteria = buildRichiestaClienteVoCriteria(codFiegDl, codEdicolaDl, codCliente, titolo, stato, null, provenienza, dataDa, dataA);
		List<RichiestaClienteDto> richiesteCliente = getDao().findObjectByDetachedCriteria(criteria);
		if (filterDataSospensionePrenotazioni != null && filterDataSospensionePrenotazioni && richiesteCliente != null && !richiesteCliente.isEmpty()) {
			Timestamp now = new Timestamp(new Date().getTime());
			richiesteCliente = select(richiesteCliente, having(on(RichiestaClienteDto.class).isDateBetweenDateSospensione(now), equalTo(false)));
		}
		return richiesteCliente; 
	}
	
	public List<RichiestaClienteDto> getRichiesteClienteByPk(String pk) {
		String[] arrPk = pk.split(",");
		Integer[] codEdicola = new Integer[arrPk.length];
		Long[] codCliente = new Long[arrPk.length];
		Integer[] provenienza = new Integer[arrPk.length];
		Integer[] codDl = new Integer[arrPk.length];
		Integer[] idtn = new Integer[arrPk.length];
		for (int  i = 0; i < arrPk.length; i++) {
			String pkValue = arrPk[i].trim();
			String[] arrPkValue = pkValue.split("\\|");
			codEdicola[i] = new Integer(arrPkValue[0].trim());
			codCliente[i] = new Long(arrPkValue[1].trim());
			provenienza[i] = new Integer(arrPkValue[2].trim());
			codDl[i] = new Integer(arrPkValue[4].trim());
			idtn[i] = new Integer(arrPkValue[5].trim());
		}
		
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaClienteVo.class, "rc");
		criteria.createCriteria("rc.clienteEdicolaVo", "ce");
		criteria.createCriteria("rc.storicoCopertineVo", "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
		
		criteria.add(Restrictions.in("rc.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.in("rc.pk.codCliente", codCliente));
		criteria.add(Restrictions.in("rc.pk.provenienza", provenienza));
		criteria.add(Restrictions.in("rc.pk.codDl", codDl));
		criteria.add(Restrictions.in("rc.pk.idtn", idtn));
		criteria.addOrder(Order.asc("ce.nome")).addOrder(Order.desc("rc.pk.dataInserimento"));
		
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("rc.pk"), "pk");
		properties.add(Projections.property("ap.titolo"), "titolo"); 
		properties.add(Projections.property("sc.sottoTitolo"), "sottoTitolo");
		properties.add(Projections.property("sc.numeroCopertina"), "numeroCopertina");
		properties.add(Projections.property("sc.dataUscita"), "dataUscita");
		properties.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertina");
		properties.add(Projections.property("rc.pk.dataInserimento"), "dataOrdine");
		properties.add(Projections.property("ce.nome"), "nome");
		properties.add(Projections.property("ce.cognome"), "cognome");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(RichiestaClienteDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<RichiestaFissaClienteDto> getRichiesteClienteFisseByIdCliente(Integer[] codFiegDl, Integer[] codEdicolaDl, Long codCliente, String titolo, String stato, Timestamp dataDa, Timestamp dataA, Boolean findUltimoIdtnPrenotazioniFisse) {
		DetachedCriteria criteria = buildRichiestaFissaClienteEdicolaVoCriteria(codFiegDl, codEdicolaDl, codCliente, titolo, dataDa, dataA);
		List<RichiestaFissaClienteDto> list = getDao().findObjectByDetachedCriteria(criteria);
		if (findUltimoIdtnPrenotazioniFisse != null && findUltimoIdtnPrenotazioniFisse) {
			for (RichiestaFissaClienteDto dto : list) {
				dto.setIdtn(pubblicazioniService.getLastStoricoCopertina(dto.getPk().getCodDl(), dto.getPk().getCodicePubblicazione()).getPk().getIdtn());
			}
		}
		return list;
	}
	
	@Override
	public List<RichiestaClienteDto> getRichiesteClienteByIdtn(Integer[] codFiegDl, Integer[] codEdicolaDl, Integer idtn, Integer provenienza) {
		DetachedCriteria criteria = buildRichiestaClienteVoCriteria(codFiegDl, codEdicolaDl, null, null, null, idtn, provenienza, null, null);
		List<RichiestaClienteDto> findObjectByDetachedCriteria = getDao().findObjectByDetachedCriteria(criteria);
		return findObjectByDetachedCriteria;
	}
	
	@Override
	public List<RichiestaRifornimentoDto> getRichiesteCliente(Integer coddl, Integer[] codFiegDl, Integer[] codEdicolaDl, Integer idtn, Long codCliente, Integer provenienza) {
		List<RichiestaRifornimentoDto> list = new ArrayList<RichiestaRifornimentoDto>();
		Integer codEdicola = iGerivUtils.getCorrispondenzaCodEdicolaMultiDl(coddl, codFiegDl, codEdicolaDl);
		PubblicazioneDto copertina = pubblicazioniService.getCopertinaByIdtn(coddl, idtn);
		List<RichiestaRifornimentoDto> listNumeriPrecedentiPossibili = null;
		if (copertina.getCodInizioQuotidiano() == null || copertina.getCodFineQuotidiano() == null || (copertina.getCodInizioQuotidiano().equals(0) && copertina.getCodFineQuotidiano().equals(0))) {
			listNumeriPrecedentiPossibili = pubblicazioniService.getCopertineByCodPubblicazione(new Integer[]{coddl}, new Integer[]{codEdicola}, copertina.getCodicePubblicazione(), null, null, copertina.getNumCopertinePrecedentiPerRifornimenti(), null, null, false, null, null, null);
		} else {
			listNumeriPrecedentiPossibili = pubblicazioniService.getCopertineByCodPubblicazione(new Integer[]{coddl}, new Integer[]{codEdicola}, null, copertina.getCodInizioQuotidiano(), copertina.getCodFineQuotidiano(), copertina.getNumCopertinePrecedentiPerRifornimenti(), null, copertina.getDataUscita(), false, null, null, null);
		}
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaClienteVo.class, "rc");
		criteria.createCriteria("rc.storicoCopertineVo", "sc", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("rc.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("rc.pk.codDl", coddl));
		criteria.add(Restrictions.eq("rc.pk.provenienza", provenienza));
		criteria.add(Restrictions.eq("rc.pk.codCliente", codCliente));
		criteria.add(Restrictions.eq("sc.codicePubblicazione", copertina.getCodicePubblicazione()));
		List<RichiestaClienteVo> rifornimenti = getDao().findByDetachedCriteria(criteria);
		
		Timestamp today = DateUtilities.floorDay(getDao().getSysdate());
		
		for (RichiestaRifornimentoDto vo : listNumeriPrecedentiPossibili) {
			RichiestaClienteVo richiestaRifornimentoVoByIdtn = getRichiestaClienteVoByIdtn(vo.getIdtn(), rifornimenti); 
			if (richiestaRifornimentoVoByIdtn != null) {
				if ((richiestaRifornimentoVoByIdtn.getStatoEvasione() != null && richiestaRifornimentoVoByIdtn.getStatoEvasione() != IGerivConstants.COD_STATO_EVASIONE_INSERITO)) {
					vo.setEnabled(false);
				} 
				vo.setTitolo(vo.getTitolo());
				vo.setSottoTitolo(vo.getSottoTitolo());
				vo.setNumeroCopertina(vo.getNumeroCopertina());
				vo.setPrezzoCopertina(vo.getPrezzoCopertina());
				vo.setDataUscita(vo.getDataUscita());
				Integer quantitaRichiesta = (richiestaRifornimentoVoByIdtn.getQuantitaRichiesta() != null && richiestaRifornimentoVoByIdtn.getQuantitaRichiesta().equals(0)) ? null : richiestaRifornimentoVoByIdtn.getQuantitaRichiesta();
				vo.setQuantitaRichiesta(quantitaRichiesta);
				vo.setQuantitaEvasa(richiestaRifornimentoVoByIdtn.getQuantitaEvasa());
				vo.setStatoEvasione(richiestaRifornimentoVoByIdtn.getStatoEvasione());
				vo.setPk(richiestaRifornimentoVoByIdtn.getPk());
			} else {
				RichiestaClientePk pk = new RichiestaClientePk();
				pk.setCodDl(coddl);
				pk.setCodEdicola(codEdicola);
				pk.setCodCliente(codCliente);
				pk.setIdtn(vo.getIdtn());
				pk.setDataInserimento(today);
				pk.setProvenienza(provenienza);
				vo.setPk(pk);
			}
			list.add(vo);
		}
		return list;
	}
	
	@Override
	public List<RichiestaClienteVo> getRichiesteClienteByPk(Integer[] codFiegDl, Integer[] codEdicola, 
			Long codCliente, Integer provenienza, Set<String> pkSet) throws ParseException {
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaClienteVo.class, "rc");
		if (pkSet != null && !pkSet.isEmpty()) {
			Integer[] idtn  = new Integer[pkSet.size()];
			int i = 0;
			for (Object pkStr : pkSet) {
				String[] pkArrVal = pkStr.toString().split("\\|");
				idtn[i] = new Integer(pkArrVal[5]);
				i++;
			}
			criteria.add(Restrictions.in("rc.pk.codEdicola", codEdicola));
			criteria.add(Restrictions.in("rc.pk.codDl", codFiegDl));
			criteria.add(Restrictions.eq("rc.pk.codCliente", codCliente));
			criteria.add(Restrictions.in("rc.pk.idtn", idtn));
			if (provenienza != null && !provenienza.equals("")) {
				criteria.add(Restrictions.eq("rc.pk.provenienza", provenienza));
			}
		} 
		List<RichiestaClienteVo> rifornimenti = getDao().findByDetachedCriteria(criteria);
		return rifornimenti;
	}
	
	@Override
	public RichiestaFissaClienteEdicolaVo getRichiestaFissaClienteVo(
			final Integer[] codEdicolaDl, final Long codCliente, final Integer[] codFiegDl,
			final Integer codicePubblicazione) {
		HibernateCallback<RichiestaFissaClienteEdicolaVo> action = new HibernateCallback<RichiestaFissaClienteEdicolaVo>() {
			@Override
			public RichiestaFissaClienteEdicolaVo doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(IGerivQueryContants.HQL_QUERY_NAME_GET_RICHIESTE_FISSE_CLIENTE_EDICOLA_BY_CPU);
				query.setParameterList("codEdicola", codEdicolaDl);
				query.setParameterList("codDl", codFiegDl);
				query.setLong("codCliente", codCliente);
				query.setInteger("codicePubblicazione", codicePubblicazione);
				Object uniqueResult = query.uniqueResult();
				return uniqueResult != null ? (RichiestaFissaClienteEdicolaVo) uniqueResult : null;
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public List<RichiestaRifornimentoVo> getRichiesteRifornimentiVo(
			Integer[] codFiegDl, Integer[] codEdicolaDl, Set<String> pkSet) throws ParseException {
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaRifornimentoVo.class, "rr");
		criteria.createCriteria("rr.storicoCopertineVo", "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
		
		criteria.add(Restrictions.in("rr.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.in("rr.pk.codEdicola", codEdicolaDl));
		criteria.add(Restrictions.isNull("rr.stato"));
		if (pkSet != null && !pkSet.isEmpty()) {
			Timestamp[] dataOrdine  = new Timestamp[pkSet.size()];
			Integer[] idtn  = new Integer[pkSet.size()];
			int i = 0;
			for (Object pkStr : pkSet) {
				String[] pkArrVal = pkStr.toString().split("\\|");
				dataOrdine[i] = DateUtilities.parseDate(pkArrVal[2], DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS);
				idtn[i] = new Integer(pkArrVal[3]);
				i++;
			}
			criteria.add(Restrictions.in("rr.pk.dataOrdine", dataOrdine));
			criteria.add(Restrictions.in("rr.pk.idtn", idtn));
		} 
		
		return getDao().findByDetachedCriteria(criteria);
	}

	@Override
	public List<RichiestaRifornimentoDto> getRichiesteRifornimenti(Integer codDlPubb, 
			Integer[] arrCodFiegDl, Integer[] arrCodWebEdicola, Integer idtn, boolean isMultiDl, Timestamp dataStorico, Integer currCodDl, Map<String,Object> params) {
		List<RichiestaRifornimentoDto> list = new ArrayList<RichiestaRifornimentoDto>();
		PubblicazioneDto copertina = pubblicazioniService.getCopertinaByIdtn(codDlPubb, idtn);
		//Vittorio 26/08/2020
		//Integer crivw = iGerivUtils.getCorrispondenzaCodEdicolaMultiDl(codDlPubb, arrCodFiegDl, arrCodWebEdicola);
		//Integer[] arrCodDl = new Integer[]{codDlPubb};
		//Integer[] arrCrivw = new Integer[]{crivw};
		//Vittorio 15/02/2021

		if (copertina != null) {
			List<RichiestaRifornimentoDto> listNumeriPrecedentiPossibili = null;
			if (copertina.getCodInizioQuotidiano() == null || copertina.getCodFineQuotidiano() == null  || copertina.getCodInizioQuotidiano().equals(0) && copertina.getCodFineQuotidiano().equals(0)) {
				//listNumeriPrecedentiPossibili = pubblicazioniService.getCopertineByCodPubblicazione(arrCodFiegDl, arrCodWebEdicola, copertina.getCodicePubblicazione(), null, null, copertina.getNumCopertinePrecedentiPerRifornimenti(), dataStorico, null, isMultiDl, currCodDl, null, params);
				listNumeriPrecedentiPossibili = pubblicazioniService.getCopertineByCodPubblicazione(arrCodFiegDl, arrCodWebEdicola, copertina.getCodicePubblicazione(), null, null, copertina.getNumCopertinePrecedentiPerRifornimenti(), dataStorico, null, isMultiDl, currCodDl, null, new HashMap<String,Object>());
			} else {
				//listNumeriPrecedentiPossibili = pubblicazioniService.getCopertineByCodPubblicazione(arrCodFiegDl, arrCodWebEdicola, null, copertina.getCodInizioQuotidiano(), copertina.getCodFineQuotidiano(), copertina.getNumCopertinePrecedentiPerRifornimenti(), dataStorico, copertina.getDataUscita(), isMultiDl, currCodDl, null, params);
				listNumeriPrecedentiPossibili = pubblicazioniService.getCopertineByCodPubblicazione(arrCodFiegDl, arrCodWebEdicola, null, copertina.getCodInizioQuotidiano(), copertina.getCodFineQuotidiano(), copertina.getNumCopertinePrecedentiPerRifornimenti(), dataStorico, copertina.getDataUscita(), isMultiDl, currCodDl, null, new HashMap<String,Object>());
			}
			Timestamp sysdate = getDao().getSysdate();
			DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaRifornimentoVo.class);
			Timestamp dataOrdine = DateUtilities.floorDay(sysdate);
			criteria.add(Restrictions.in("pk.codFiegDl", arrCodFiegDl));
			criteria.add(Restrictions.in("pk.codEdicola", arrCodWebEdicola));
			criteria.add(Restrictions.ge("pk.dataOrdine", dataOrdine));
			criteria.add(Restrictions.isNull("stato"));
			List<RichiestaRifornimentoVo> rifornimenti = getDao().findByDetachedCriteria(criteria);
			
			Calendar cal = Calendar.getInstance();
			for (RichiestaRifornimentoDto vo : listNumeriPrecedentiPossibili) {
				List<RichiestaRifornimentoVo> richiesteRifornimentoVoByIdtn = getRichiesteRifornimentoVoByIdtn(vo.getIdtn(), rifornimenti);
				
				if (!richiesteRifornimentoVoByIdtn.isEmpty()) {
					for (RichiestaRifornimentoVo richiestaRifornimentoVoByIdtn : richiesteRifornimentoVoByIdtn) {
						if (richiestaRifornimentoVoByIdtn != null) {
							RichiestaRifornimentoDto vo1 = new RichiestaRifornimentoDto();
							vo1.setTitolo(vo.getTitolo());
							vo1.setSottoTitolo(vo.getSottoTitolo());
							vo1.setNumeroCopertina(vo.getNumeroCopertina());
							vo1.setPrezzoCopertina(vo.getPrezzoCopertina());
							vo1.setDataUscita(vo.getDataUscita());
							vo1.setDataOrdine(richiestaRifornimentoVoByIdtn.getDataOrdine());
							vo1.setDataRispostaDl(richiestaRifornimentoVoByIdtn.getDataRispostaDl());
							vo1.setQuantitaEvasa(richiestaRifornimentoVoByIdtn.getQuantitaEvasa());
							vo1.setDescCausaleNonEvadibilita(richiestaRifornimentoVoByIdtn.getDescCausaleNonEvadibilita());
							vo1.setCodicePubblicazione(vo.getCodicePubblicazione());
							if (richiestaRifornimentoVoByIdtn.getStato() != null) {
								vo1.setEnabled(false);
							}
							vo1.setPk(richiestaRifornimentoVoByIdtn.getPk());
							Integer quantitaRichiesta = (richiestaRifornimentoVoByIdtn.getQuantitaRichiesta() != null && richiestaRifornimentoVoByIdtn.getQuantitaRichiesta().equals(0)) ? null : richiestaRifornimentoVoByIdtn.getQuantitaRichiesta();
							vo1.setQuantitaRichiesta(quantitaRichiesta);
							vo1.setGiorniValiditaRichiesteRifornimento(richiestaRifornimentoVoByIdtn.getGiorniValiditaRichiesteRifornimento());
							vo1.setNoteVendita(richiestaRifornimentoVoByIdtn.getNoteVendita());
							vo1.setImmagine(vo.getImmagine());
							vo1.setGiacenza(pubblicazioniService.getGiacenza(richiestaRifornimentoVoByIdtn.getPk().getCodFiegDl(), richiestaRifornimentoVoByIdtn.getPk().getCodEdicola(), idtn, dataStorico));
							// Menta 10/01/2017 Visualizzare fornito all'interno della grid di richiesta rifornimento
							vo1.setFornitoBolla(pubblicazioniService.getFornito(richiestaRifornimentoVoByIdtn.getPk().getCodFiegDl(), richiestaRifornimentoVoByIdtn.getPk().getCodEdicola(), idtn).intValue());
							setRichiestaRifornimentoLivellamenti(vo1);
							list.add(vo1);
						} 
					}
				} else {
					RichiestaRifornimentoPk pk = new RichiestaRifornimentoPk();
					pk.setCodFiegDl(vo.getCoddl());
					Integer coded = iGerivUtils.getCorrispondenzaCodEdicolaMultiDl(vo.getCoddl(), arrCodFiegDl, arrCodWebEdicola);
					pk.setCodEdicola(coded);
					pk.setDataOrdine(sysdate);
					pk.setIdtn(vo.getIdtn());
					vo.setPk(pk); 
					cal.setTime(new Date(dataOrdine.getTime()));
					cal.add(Calendar.DAY_OF_YEAR, 120);
					vo.setGiorniValiditaRichiesteRifornimento(copertina.getGiorniValiditaRichiesteRifornimento());
					vo.setPrenotazione(0);
					vo.setGiacenza(pubblicazioniService.getGiacenza(vo.getCoddl(), coded, vo.getIdtn(), dataStorico));
					// Menta 10/01/2017 Visualizzare fornito all'interno della grid di richiesta rifornimento
					vo.setFornitoBolla(pubblicazioniService.getFornito(vo.getCoddl(), coded, vo.getIdtn()).intValue());
					// TOGLIERE IL COMMENT DOPO AVER ESEGUITO I TEST SUI LIVELLAMENTI 07/10/14 (CONTROLLARE SE SERVONO LE QUERY SUI FORNITI)					
					setRichiestaRifornimentoLivellamenti(vo);
					list.add(vo);
				}
			}
			list = sort(list, on(RichiestaRifornimentoDto.class).isEnabled());
		}
		return list;
	}

	
	
	@Override
	public List<RichiestaRifornimentoDto> getPubblicazioniPossibiliPerRichiesteRifornimenti(Integer codDlPubb, 
			Integer[] arrCodFiegDl, Integer[] arrCodWebEdicola, Integer idtn, boolean isMultiDl, Timestamp dataStorico, Integer currCodDl) {
		List<RichiestaRifornimentoDto> list = new ArrayList<RichiestaRifornimentoDto>();
		PubblicazioneDto copertina = pubblicazioniService.getCopertinaByIdtn(codDlPubb, idtn);
		if (copertina != null) {
			List<RichiestaRifornimentoDto> listNumeriPrecedentiPossibili = null;
			if (copertina.getCodInizioQuotidiano() == null || copertina.getCodFineQuotidiano() == null  || copertina.getCodInizioQuotidiano().equals(0) && copertina.getCodFineQuotidiano().equals(0)) {
				listNumeriPrecedentiPossibili = pubblicazioniService.getCopertineByCodPubblicazione(arrCodFiegDl, arrCodWebEdicola, copertina.getCodicePubblicazione(), null, null, copertina.getNumCopertinePrecedentiPerRifornimenti(), dataStorico, null, isMultiDl, currCodDl, null, null);
			} else {
				listNumeriPrecedentiPossibili = pubblicazioniService.getCopertineByCodPubblicazione(arrCodFiegDl, arrCodWebEdicola, null, copertina.getCodInizioQuotidiano(), copertina.getCodFineQuotidiano(), copertina.getNumCopertinePrecedentiPerRifornimenti(), dataStorico, copertina.getDataUscita(), isMultiDl, currCodDl, null, null);
			}
			Timestamp sysdate = getDao().getSysdate();
			Timestamp dataOrdine = DateUtilities.floorDay(sysdate);
			
			Calendar cal = Calendar.getInstance();
			for (RichiestaRifornimentoDto vo : listNumeriPrecedentiPossibili) {
				
					RichiestaRifornimentoPk pk = new RichiestaRifornimentoPk();
					pk.setCodFiegDl(vo.getCoddl());
					Integer coded = iGerivUtils.getCorrispondenzaCodEdicolaMultiDl(vo.getCoddl(), arrCodFiegDl, arrCodWebEdicola);
					pk.setCodEdicola(coded);
					pk.setDataOrdine(sysdate);
					pk.setIdtn(vo.getIdtn());
					vo.setPk(pk); 
					cal.setTime(new Date(dataOrdine.getTime()));
					cal.add(Calendar.DAY_OF_YEAR, 120);
					vo.setGiorniValiditaRichiesteRifornimento(copertina.getGiorniValiditaRichiesteRifornimento());
					vo.setPrenotazione(0);
					vo.setGiacenza(pubblicazioniService.getGiacenza(vo.getCoddl(), coded, vo.getIdtn(), dataStorico));
					// TOGLIERE IL COMMENT DOPO AVER ESEGUITO I TEST SUI LIVELLAMENTI 07/10/14 (CONTROLLARE SE SERVONO LE QUERY SUI FORNITI)					
					setRichiestaRifornimentoLivellamenti(vo);
					list.add(vo);
				
			}
			list = sort(list, on(RichiestaRifornimentoDto.class).isEnabled());
		}
		return list;
	}
	
	@Override
	public List<RichiestaRifornimentoDto> getPubblicazioniEsitoRichiesteRifornimenti(Integer codDlPubb, 
			Integer[] arrCodFiegDl, Integer[] arrCodWebEdicola, Integer idtn, boolean isMultiDl, Timestamp dataStorico, Integer currCodDl) {
		List<RichiestaRifornimentoDto> list = new ArrayList<RichiestaRifornimentoDto>();
		PubblicazioneDto copertina = pubblicazioniService.getCopertinaByIdtn(codDlPubb, idtn);
		if (copertina != null) {
			List<RichiestaRifornimentoDto> listNumeriPrecedentiPossibili = null;
			if (copertina.getCodInizioQuotidiano() == null || copertina.getCodFineQuotidiano() == null  || copertina.getCodInizioQuotidiano().equals(0) && copertina.getCodFineQuotidiano().equals(0)) {
				listNumeriPrecedentiPossibili = pubblicazioniService.getCopertineByCodPubblicazione(arrCodFiegDl, arrCodWebEdicola, copertina.getCodicePubblicazione(), null, null, copertina.getNumCopertinePrecedentiPerRifornimenti(), dataStorico, null, isMultiDl, currCodDl, null, null);
			} else {
				listNumeriPrecedentiPossibili = pubblicazioniService.getCopertineByCodPubblicazione(arrCodFiegDl, arrCodWebEdicola, null, copertina.getCodInizioQuotidiano(), copertina.getCodFineQuotidiano(), copertina.getNumCopertinePrecedentiPerRifornimenti(), dataStorico, copertina.getDataUscita(), isMultiDl, currCodDl, null, null);
			}
			Timestamp sysdate = getDao().getSysdate();
			DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaRifornimentoVo.class);
			Timestamp dataOrdine = DateUtilities.floorDay(sysdate);
			criteria.add(Restrictions.in("pk.codFiegDl", arrCodFiegDl));
			criteria.add(Restrictions.in("pk.codEdicola", arrCodWebEdicola));
			criteria.add(Restrictions.eq("pk.idtn", idtn));
			criteria.add(Restrictions.isNotNull("stato"));
			criteria.addOrder(Order.desc("pk.dataOrdine"));
			List<RichiestaRifornimentoVo> rifornimenti = getDao().findByDetachedCriteria(criteria);
			
			Calendar cal = Calendar.getInstance();
			for (RichiestaRifornimentoDto vo : listNumeriPrecedentiPossibili) {
				List<RichiestaRifornimentoVo> richiesteRifornimentoVoByIdtn = getRichiesteRifornimentoVoByIdtn(vo.getIdtn(), rifornimenti);
				if (!richiesteRifornimentoVoByIdtn.isEmpty()) {
					for (RichiestaRifornimentoVo richiestaRifornimentoVoByIdtn : richiesteRifornimentoVoByIdtn) {
						if (richiestaRifornimentoVoByIdtn != null) {
							RichiestaRifornimentoDto vo1 = new RichiestaRifornimentoDto();
							vo1.setTitolo(vo.getTitolo());
							vo1.setSottoTitolo(vo.getSottoTitolo());
							vo1.setNumeroCopertina(vo.getNumeroCopertina());
							vo1.setPrezzoCopertina(vo.getPrezzoCopertina());
							vo1.setDataUscita(vo.getDataUscita());
							vo1.setDataOrdine(richiestaRifornimentoVoByIdtn.getDataOrdine());
							vo1.setDataRispostaDl(richiestaRifornimentoVoByIdtn.getDataRispostaDl());
							vo1.setDataScadenza(richiestaRifornimentoVoByIdtn.getDataScadenzaRichiesta());
							vo1.setQuantitaEvasa(richiestaRifornimentoVoByIdtn.getQuantitaEvasa());
							vo1.setDescCausaleNonEvadibilita(richiestaRifornimentoVoByIdtn.getDescCausaleNonEvadibilita());
							
							vo1.setCodicePubblicazione(vo.getCodicePubblicazione());
							if (richiestaRifornimentoVoByIdtn.getStato() != null) {
								vo1.setEnabled(false);
							}
							vo1.setPk(richiestaRifornimentoVoByIdtn.getPk());
							Integer quantitaRichiesta = (richiestaRifornimentoVoByIdtn.getQuantitaRichiesta() != null && richiestaRifornimentoVoByIdtn.getQuantitaRichiesta().equals(0)) ? null : richiestaRifornimentoVoByIdtn.getQuantitaRichiesta();
							vo1.setQuantitaRichiesta(quantitaRichiesta);
							vo1.setGiorniValiditaRichiesteRifornimento(richiestaRifornimentoVoByIdtn.getGiorniValiditaRichiesteRifornimento());
							vo1.setNoteVendita(richiestaRifornimentoVoByIdtn.getNoteVendita());
							vo1.setImmagine(vo.getImmagine());
							vo1.setGiacenza(pubblicazioniService.getGiacenza(richiestaRifornimentoVoByIdtn.getPk().getCodFiegDl(), richiestaRifornimentoVoByIdtn.getPk().getCodEdicola(), idtn, dataStorico));
							vo1.setRichiestaSospesa(richiestaRifornimentoVoByIdtn.getRichiestaSospesa());
							
							setRichiestaRifornimentoLivellamenti(vo1);
							list.add(vo1);
						} 
					}
				} 
			}
			list = sort(list, on(RichiestaRifornimentoDto.class).isEnabled());
		}
		return list;
	}
	
	
	
	
	
	
	
	
	
	private void setRichiestaRifornimentoLivellamenti(RichiestaRifornimentoDto vo) {
		RichiestaRifornimentoPk pk = (RichiestaRifornimentoPk) vo.getPk();
		RichiestaRifornimentoLivellamentiVo richiestaRifornimentoLivellamenti = livellamentiService.getRichiestaRifornimentoLivellamenti(new Integer[]{pk.getCodFiegDl()}, new Integer[]{pk.getCodEdicola()}, pk.getIdtn(), Arrays.asList(new StatoRichiestaRifornimentoLivellamento[]{StatoRichiestaRifornimentoLivellamento.INSERITO}));
		if (richiestaRifornimentoLivellamenti != null) {
			vo.setIdRichiestaLivellamento(richiestaRifornimentoLivellamenti.getIdRichiestaLivellamento());
			vo.setStatoRichiestaRifornimentoLivellamento(richiestaRifornimentoLivellamenti.getStato());
			List<StatoRichiestaLivellamento> stati = extract(richiestaRifornimentoLivellamenti.getLivellamenti(), on(LivellamentiVo.class).getStatoVendita());
			vo.setLivellamentoEditable(!CollectionUtils.containsAny(stati, Arrays.asList(new StatoRichiestaLivellamento[]{StatoRichiestaLivellamento.ACCETTATO, StatoRichiestaLivellamento.NON_ACCETTATO, StatoRichiestaLivellamento.VENDUTO})));
			vo.setQuantitaRichiesta(richiestaRifornimentoLivellamenti.getQuantitaRichiesta());
		}
	}
	
	@Override
	public List<RichiestaRifornimentoDto> getRichiesteRifornimenti(
			final Integer codFiegDl, final Integer codEdicolaDl, final String titolo, final String stato, 
			final Timestamp dataDa, final Timestamp dataA, final boolean isDlInforiv) {
		HibernateCallback<List<RichiestaRifornimentoDto>> action = findRichiesteRifornimento(codFiegDl, codEdicolaDl, titolo, stato, dataDa, dataA);
		List<RichiestaRifornimentoDto> listRichiesteRifornimento = getDao().findByHibernateCallback(action);
		if (isDlInforiv && !listRichiesteRifornimento.isEmpty()) {
			List<Integer> listIdtn = extract(listRichiesteRifornimento, on(RichiestaRifornimentoDto.class).getIdtn());
			List<RichiestaRifornimentoDto> listRichiesteRifornimentoFondoBolla = findRichiesteRifornimentoFondoBolla(codFiegDl, codEdicolaDl, dataDa, dataA, listIdtn);
			if (!listRichiesteRifornimentoFondoBolla.isEmpty()) {
				forEach(listRichiesteRifornimentoFondoBolla).setRigaEvasione(true);
				listRichiesteRifornimento.addAll(listRichiesteRifornimentoFondoBolla);
				List<RichiestaRifornimentoDto> list = new ArrayList<RichiestaRifornimentoDto>();
				Group<RichiestaRifornimentoDto> group = group(listRichiesteRifornimento, by(on(RichiestaRifornimentoDto.class).getIdtn()));
				for (Group<RichiestaRifornimentoDto> subgroup : group.subgroups()) {
					List<RichiestaRifornimentoDto> sort = sort(subgroup.findAll(), on(RichiestaRifornimentoDto.class).getDataOrdine());
					Collections.reverse(sort);
					list.addAll(sort);
				}
				listRichiesteRifornimento = list;
			}
		}
		return listRichiesteRifornimento;
	}
	
	@Override
	public PrenotazioneVo getPrenotazione(Integer codFiegDl, Integer codEdicolaDl, Integer cpu) {
		return getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_PRENOTAZIONE_BY_ID, codFiegDl, cpu, codEdicolaDl);
	}
	
	
	@Override
	public List<PrenotazioneDto> getRichiesteVariazioni(final Integer[] codFiegDl, final Integer[] codEdicola, final String titolo, final Integer stato, final Timestamp dataDa, final Timestamp dataA, final Integer codUtente) {
		HibernateCallback<List<PrenotazioneDto>> action = new HibernateCallback<List<PrenotazioneDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<PrenotazioneDto> doInHibernate(Session session)
					throws HibernateException, SQLException {
				StringBuffer query = new StringBuffer(IGerivQueryContants.SQL_QUERY_GET_RICHIESTE_VARIAZIONI); 
				if (titolo != null && !titolo.equals("")) {
					query.append(query.indexOf("where") == -1 ? " where " : " and ");
					query.append("upper(pub1_.titolo9606) like :titolo ");
				}
				if (stato != null) {
					query.append(query.indexOf("where") == -1 ? " where " : " and ");
					query.append("this_.itdl9129 = :stato ");
				}
				if (dataDa != null && dataA != null) {
					query.append(query.indexOf("where") == -1 ? " where " : " and ");
					query.append("this_.dari9129 between :dataDa and :dataA ");
				}
				if (codUtente != null) {
					query.append(query.indexOf("where") == -1 ? " where " : " and ");
					query.append("this_.codut9129=:codUtente ");
				}
				SQLQuery sqlQuery = session.createSQLQuery(query.toString());
				sqlQuery.setResultTransformer( Transformers.aliasToBean(PrenotazioneDto.class));
				sqlQuery.setParameterList("codFiegDl", codFiegDl);
				sqlQuery.setParameterList("codEdicola", codEdicola);
				if (codUtente != null) {
					sqlQuery.setInteger("codUtente", codUtente);
				}
				if (titolo != null && !titolo.equals("")) {
					sqlQuery.setString("titolo", titolo.toUpperCase() + "%");
				}
				if (stato != null) {
					if (stato.equals(IGerivConstants.INDICATORE_RECORD_NON_TRASMESSO_DL)) {
						sqlQuery.setInteger("stato", IGerivConstants.INDICATORE_RECORD_NON_TRASMESSO_DL);
					} else if (stato.equals(IGerivConstants.INDICATORE_RECORD_IN_TRASMISSIONE_DL)) {
						sqlQuery.setInteger("stato", IGerivConstants.INDICATORE_RECORD_IN_TRASMISSIONE_DL);
					} else {
						sqlQuery.setInteger("stato", IGerivConstants.INDICATORE_RECORD_TRASMESSO_DL);
					}
				}
				if (dataDa != null && dataA != null) {
					sqlQuery.setTimestamp("dataDa", dataDa);
					sqlQuery.setTimestamp("dataA", dataA);
				}
				sqlQuery.addScalar("titolo", StringType.INSTANCE);
				sqlQuery.addScalar("codicePubblicazione", IntegerType.INSTANCE);
				sqlQuery.addScalar("argomento", StringType.INSTANCE);
				sqlQuery.addScalar("periodicita", StringType.INSTANCE);
				sqlQuery.addScalar("dataRichiesta", DateType.INSTANCE);
				sqlQuery.addScalar("quantitaRichiesta", IntegerType.INSTANCE);
				sqlQuery.addScalar("motivoRichiesta", StringType.INSTANCE);
				sqlQuery.addScalar("indicatoreTrasmessoDl", IntegerType.INSTANCE);
				sqlQuery.addScalar("dataUltimaTrasmissioneDl", TimestampType.INSTANCE);
				List<PrenotazioneDto> list = sqlQuery.list();
				return list;
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public void saveEvasionePrenotazioniClientiEdicola(final Map<Long, List<RichiestaClienteDto>> mapEvasione) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				int executeUpdate = 0;
				try {
					for (Map.Entry<Long, List<RichiestaClienteDto>> entry : mapEvasione.entrySet()) {
						List<RichiestaClienteDto> values = entry.getValue();
						for (RichiestaClienteDto dto : values) {
							Query queryFuoriVoce = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_UPDATE_RICHIESTA_CLIENTE_EDICOLA);
							queryFuoriVoce.setInteger("statoEvasione", IGerivConstants.COD_STATO_EVASIONE_PARZIALE);
							if (dto.getQuantitaEvasa().equals(dto.getQuantitaRichiesta()) || dto.getUltRisposta().equals(IGerivConstants.COD_MESSAGGIO_CODIFICATO_PUBBLICAZIONE_ESAURITA)) {
								queryFuoriVoce = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_UPDATE_RICHIESTA_CLIENTE_EDICOLA);
								queryFuoriVoce.setInteger("statoEvasione", IGerivConstants.COD_STATO_EVASIONE_COMPLETA);
							} else if (dto.getUltRisposta().equals(IGerivConstants.COD_MESSAGGIO_CODIFICATO_RIFIUTATO_CLIENTE) && dto.getQuantitaEvasa().equals(0)) {
								queryFuoriVoce.setInteger("statoEvasione", IGerivConstants.COD_STATO_EVASIONE_COMPLETA);
							}
							queryFuoriVoce.setInteger("quantitaEvasa", dto.getQuantitaEvasa());
							queryFuoriVoce.setInteger("codiceUltimaRisposta", dto.getUltRisposta());
							queryFuoriVoce.setString("descrizioneUltimaRisposta", dto.getMessagioLibero());
							queryFuoriVoce.setInteger("codEdicola", dto.getCodEdicola());
							queryFuoriVoce.setLong("codCliente", dto.getCodCliente());
							queryFuoriVoce.setInteger("provenienza", dto.getProvenienza());
							queryFuoriVoce.setTimestamp("dataInserimento", dto.getDataOrdine());
							queryFuoriVoce.setInteger("codDl", dto.getCodDl());
							queryFuoriVoce.setInteger("idtn", dto.getIdtn());
							executeUpdate += queryFuoriVoce.executeUpdate();
						}
					}
				} catch (Exception e) {
					throw new HibernateException(e);
				}
				return executeUpdate;
			}
		};
		getDao().executeByHibernateCallback(action);
	}
	
	/**
	 * @param pk
	 * @param qtaEvasa
	 * @param qtaDaEvadere
	 * @param ultimaRisposta
	 * @param messLibero
	 * @return
	 * @throws ParseException
	 */
	@Override
	public Map<Long, List<RichiestaClienteDto>> buildMapEvasione(String pk, String qtaEvasa, String qtaDaEvadere, String ultimaRisposta, String messLibero) throws ParseException {
		Map<Long, List<RichiestaClienteDto>> map = new HashMap<Long, List<RichiestaClienteDto>>();
		List<RichiestaClienteDto> list = new ArrayList<RichiestaClienteDto>();
		String[] arrPk = pk.split(",");
		String[] arrQtaEvasa = qtaEvasa.split(",");
		String[] arrQtaDaEvadere = qtaDaEvadere.split(",");
		String[] arrUltimaRisposta = ultimaRisposta.split(",");
		String[] arrMessLibero = messLibero.split(",");
		for (int  i = 0; i < arrPk.length; i++) {
			RichiestaClienteDto dto = new RichiestaClienteDto();
			String pkValue = arrPk[i].trim();
			String qtaEv = arrQtaEvasa[i].trim();
			String qtaDaEv = arrQtaDaEvadere[i].trim();
			Integer ultRisposta = new Integer(arrUltimaRisposta[i].trim().equals("") ? "0" : arrUltimaRisposta[i].trim());
			String mLibero = arrMessLibero[i].trim();
			String[] arrPkValue = pkValue.split("\\|");
			String codEdicola = arrPkValue[0].trim();
			String codCliente = arrPkValue[1].trim();
			String provenienza = arrPkValue[2].trim();
			String dataInserimento = arrPkValue[3].trim();
			String codDl = arrPkValue[4].trim();
			String idtn = arrPkValue[5].trim();
			dto.setQuantitaEvasa(new Integer(qtaEv.equals("") ? "0" : qtaEv));
			dto.setQuantitaRichiesta(new Integer(qtaDaEv));
			dto.setUltRisposta(ultRisposta);
			dto.setMessagioLibero(mLibero.equals("") ? null : mLibero);
			dto.setCodEdicola(new Integer(codEdicola));
			dto.setCodCliente(new Long(codCliente));
			dto.setCodDl(new Integer(codDl));
			dto.setIdtn(new Integer(idtn));
			dto.setProvenienza(new Integer(provenienza));
			dto.setDataOrdine(DateUtilities.parseDate(dataInserimento, DateUtilities.FORMATO_DATA_YYYY_MM_DD_HHMMSS));
			list.add(dto);
		}
		Group<RichiestaClienteDto> group = group(list, by(on(RichiestaClienteDto.class).getCodCliente()));
		for (Group<RichiestaClienteDto> subgroup : group.subgroups()) {
			List<RichiestaClienteDto> findAll = subgroup.findAll();
			map.put(findAll.get(0).getCodCliente(), findAll);
		}
		return map;
	}
	
	@Override
	public List<RisposteClientiCodificateVo> getRisposteClientiCodificate() {
		return getDao().findByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_RISPOSTE_CLIENTI_CODIFICATE);
	}
	
	@Override
	public EsportazioneDatiDlResultDto esportaRifornimenti(final Integer codDl) {
		HibernateCallback<EsportazioneDatiDlResultDto> action = new HibernateCallback<EsportazioneDatiDlResultDto>() {
			@Override
			public EsportazioneDatiDlResultDto doInHibernate(Session session)
				throws HibernateException, SQLException {
		        return session.doReturningWork(new EsportaRifornimentiFunctionWork(codDl));
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public EsportazioneDatiDlResultDto esportaAltriDati(final Integer codDl) {
		HibernateCallback<EsportazioneDatiDlResultDto> action = new HibernateCallback<EsportazioneDatiDlResultDto>() {
			@Override
			public EsportazioneDatiDlResultDto doInHibernate(Session session)
				throws HibernateException, SQLException {
				return session.doReturningWork(new EsportaAltriDatiFunctionWork(codDl));
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public EsportazioneDatiDlResultDto esportaTutto(final Integer codDl) {
		HibernateCallback<EsportazioneDatiDlResultDto> action = new HibernateCallback<EsportazioneDatiDlResultDto>() {
			@Override
			public EsportazioneDatiDlResultDto doInHibernate(Session session)
				throws HibernateException, SQLException {
				return session.doReturningWork(new EsportaTuttoFunctionWork(codDl));
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public List<RichiestaClienteDto> getOrdiniClienti(Integer codFiegDl, Integer codEdicola, Timestamp dataBolla) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaDettaglioVo.class, "bd");
		criteria.add(Restrictions.eq("bd.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("bd.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("bd.pk.dtBolla", dataBolla));
		criteria.add(Restrictions.isNotNull("bd.ordini"));
		criteria.add(Restrictions.gt("bd.ordini", 0));
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("bd.idtn"), "idtn");
		criteria.setProjection(properties);
		String idtnOrdini = getDao().findObjectByDetachedCriteria(criteria).toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");
		return getOrdiniClienti(codFiegDl, codEdicola, idtnOrdini);
	}
	
	@Override
	public List<RichiestaClienteDto> getOrdiniClienti(Integer codFiegDl, Integer codEdicola, String idtnOrdini) {
		List<RichiestaClienteDto> ret = null;
		String[] split = idtnOrdini.split(",");
		List<Integer> list = new ArrayList<Integer>();
		for (String s : split) {
			if (!s.trim().equals("")) {
				list.add(new Integer(s));
			}
		}
		if (list.size() > 0) {
			DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaClienteVo.class, "rc");
			criteria.createCriteria("rc.clienteEdicolaVo", "ce");
			criteria.createCriteria("rc.storicoCopertineVo", "sc");
			criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
			criteria.add(Restrictions.eq("pk.codDl", codFiegDl));
			criteria.add(Restrictions.eq("pk.codEdicola", codEdicola));
			criteria.add(Restrictions.in("pk.idtn", list));
			criteria.addOrder(Order.asc("ap.titolo")).addOrder(Order.asc("sc.sottoTitolo")).addOrder(Order.asc("sc.numeroCopertina"));
			ProjectionList properties = Projections.projectionList();
			properties.add(Projections.property("pk"), "pk");
			properties.add(Projections.property("ap.titolo"), "titolo"); 
			properties.add(Projections.property("sc.sottoTitolo"), "sottoTitolo");
			properties.add(Projections.property("ce.nome"), "nome"); 
			properties.add(Projections.property("ce.cognome"), "cognome");
			properties.add(Projections.property("ce.telefono"), "telefono");
			properties.add(Projections.property("sc.numeroCopertina"), "numeroCopertina");
			properties.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertina");
			properties.add(Projections.property("pk.dataInserimento"), "dataOrdine");
			properties.add(Projections.property("dataUltimaRisposta"), "dataUltimaRisposta");
			properties.add(Projections.property("quantitaRichiesta"), "quantitaRichiesta");
			properties.add(Projections.property("quantitaEvasa"), "quantitaEvasa");
			properties.add(Projections.property("statoEvasione"), "statoEvasione");
			properties.add(Projections.property("codiceUltimaRisposta"), "codiceUltimaRisposta");
			properties.add(Projections.property("descrizioneUltimaRisposta"), "messagioLibero");
			properties.add(Projections.property("pk.provenienza"), "provenienza");
			properties.add(Projections.property("ce.dtSospensionePrenotazioniDa"), "dtSospensionePrenotazioniDa");
			properties.add(Projections.property("ce.dtSospensionePrenotazioniA"), "dtSospensionePrenotazioniA");
			criteria.setProjection(properties); 
			criteria.setResultTransformer(Transformers.aliasToBean(RichiestaClienteDto.class));
			ret = getDao().findObjectByDetachedCriteria(criteria);
			if (ret != null && !ret.isEmpty()) {
				Timestamp now = new Timestamp(new Date().getTime());
				ret = select(ret, having(on(RichiestaClienteDto.class).isDateBetweenDateSospensione(now), equalTo(false)));
			}
		} 
		return ret;
	}
	
	@Override
	public List<RichiestaClienteDto> getOrdiniClientiNotifiche(Integer[] codFiegDl, Integer[] codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaClienteVo.class, "rc");
		criteria.createCriteria("rc.clienteEdicolaVo", "ce");
		criteria.createCriteria("rc.storicoCopertineVo", "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
		criteria.add(Restrictions.in("rc.pk.codDl", codFiegDl));
		criteria.add(Restrictions.in("rc.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("rc.pk.provenienza", IGerivConstants.PROVENIENZA_CLIENTE));
		criteria.add(Restrictions.eq("rc.notificareOrdineRivendita", new Boolean(true)));
		criteria.addOrder(Order.desc("ap.titolo")).addOrder(Order.desc("sc.sottoTitolo")).addOrder(Order.desc("sc.numeroCopertina"));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("pk"), "pk");
		properties.add(Projections.property("ap.titolo"), "titolo"); 
		properties.add(Projections.property("sc.sottoTitolo"), "sottoTitolo");
		properties.add(Projections.property("ce.nome"), "nome"); 
		properties.add(Projections.property("ce.cognome"), "cognome");
		properties.add(Projections.property("ce.telefono"), "telefono");
		properties.add(Projections.property("sc.numeroCopertina"), "numeroCopertina");
		properties.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertina");
		properties.add(Projections.property("pk.dataInserimento"), "dataOrdine");
		properties.add(Projections.property("dataUltimaRisposta"), "dataUltimaRisposta");
		properties.add(Projections.property("quantitaRichiesta"), "quantitaRichiesta");
		properties.add(Projections.property("quantitaEvasa"), "quantitaEvasa");
		properties.add(Projections.property("statoEvasione"), "statoEvasione");
		properties.add(Projections.property("codiceUltimaRisposta"), "codiceUltimaRisposta");
		properties.add(Projections.property("descrizioneUltimaRisposta"), "messagioLibero");
		properties.add(Projections.property("pk.provenienza"), "provenienza");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(RichiestaClienteDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	/**
	 * Classe interna per l'esecuzione della function. 
	 * 
	 * @author romanom
	 *
	 */
	private class EsportaRifornimentiFunctionWork implements ReturningWork<EsportazioneDatiDlResultDto> {
		private Integer codDl;

		public EsportaRifornimentiFunctionWork(Integer codDl) {
			this.codDl = codDl;
		}

		@Override
		public EsportazioneDatiDlResultDto execute(Connection connection) throws SQLException {
			CallableStatement call = null;
			try {
				call = connection.prepareCall("{ call P_INTERFACCE.P_ESPORTA_RIFORNIMENTI(?,?,?) }");
				C3P0NativeJdbcExtractor cp30NativeJdbcExtractor = new C3P0NativeJdbcExtractor();
	            OracleConnection conn = (OracleConnection) cp30NativeJdbcExtractor.getNativeConnection(connection);
				ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor("ARRAY_VARCHAR", conn);
				ARRAY fileContentOut = new ARRAY(descriptor, conn, new String[] {});
				call.setLong(1, codDl);
				call.setArray(2, fileContentOut);
				call.setString(3,  "");
				call.registerOutParameter(2, OracleTypes.ARRAY, "ARRAY_VARCHAR");
			    call.registerOutParameter(3, Types.VARCHAR); 
			    call.execute();
			    List<String> fileContent = call.getArray(2) != null && call.getArray(2).getArray() != null ? Arrays.asList((String[]) call.getArray(2).getArray()) : null;
			    EsportazioneDatiDlResultDto value = new EsportazioneDatiDlResultDto();
			    value.setFileContent(fileContent);
			    value.setResultMsg(call.getString(3));
			    return value;
			} finally {
				if (call != null) {
					call.close();
					call = null;
				}
			}
		}
	}
	
	/**
	 * Classe interna per l'esecuzione della function. 
	 * 
	 * @author romanom
	 *
	 */
	private class EsportaAltriDatiFunctionWork implements ReturningWork<EsportazioneDatiDlResultDto> {
		private Integer codDl;

		public EsportaAltriDatiFunctionWork(Integer codDl) {
			this.codDl = codDl;
		}

		@Override
		public EsportazioneDatiDlResultDto execute(Connection connection) throws SQLException {
			CallableStatement call = null;
			try {
				call = connection.prepareCall("{ call P_INTERFACCE.P_ESPORTA_ALTRO(?,?,?) }");
				C3P0NativeJdbcExtractor cp30NativeJdbcExtractor = new C3P0NativeJdbcExtractor();
	            OracleConnection conn = (OracleConnection) cp30NativeJdbcExtractor.getNativeConnection(connection);
				ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor("ARRAY_VARCHAR", conn);
				ARRAY fileContentOut = new ARRAY(descriptor, conn, new String[] {});
				call.setLong(1, codDl);
				call.setArray(2, fileContentOut);
				call.setString(3,  "");
				call.registerOutParameter(2, OracleTypes.ARRAY, "ARRAY_VARCHAR");
			    call.registerOutParameter(3, Types.VARCHAR); 
			    call.execute();
			    List<String> fileContent = call.getArray(2) != null && call.getArray(2).getArray() != null ? Arrays.asList((String[]) call.getArray(2).getArray()) : null;
			    EsportazioneDatiDlResultDto value = new EsportazioneDatiDlResultDto();
			    value.setFileContent(fileContent);
			    value.setResultMsg(call.getString(3));
			    return value;
			} finally {
				if (call != null) {
					call.close();
					call = null;
				}
			}
		}
	}
	
	/**
	 * Classe interna per l'esecuzione della function. 
	 * 
	 * @author romanom
	 *
	 */
	private class EsportaTuttoFunctionWork implements ReturningWork<EsportazioneDatiDlResultDto> {
		private Integer codDl;

		public EsportaTuttoFunctionWork(Integer codDl) {
			this.codDl = codDl;
		}

		@Override
		public EsportazioneDatiDlResultDto execute(Connection connection) throws SQLException {
			CallableStatement call = null;
			try {
				call = connection.prepareCall("{ ? = call P_INTERFACCE.P_ESPORTA_TUTTO(?,?,?) }");
				C3P0NativeJdbcExtractor cp30NativeJdbcExtractor = new C3P0NativeJdbcExtractor();
	            OracleConnection conn = (OracleConnection) cp30NativeJdbcExtractor.getNativeConnection(connection);
				ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor("ARRAY_VARCHAR", conn);
				ARRAY fileContentOut = new ARRAY(descriptor, conn, new String[] {});
				call.setLong(1, codDl);
				call.setArray(2, fileContentOut);
				call.setString(3,  "");
				call.registerOutParameter(2, OracleTypes.ARRAY, "ARRAY_VARCHAR");
			    call.registerOutParameter(3, Types.VARCHAR); 
			    call.execute();
			    List<String> fileContent = call.getArray(2) != null && call.getArray(2).getArray() != null ? Arrays.asList((String[]) call.getArray(2).getArray()) : null;
			    EsportazioneDatiDlResultDto value = new EsportazioneDatiDlResultDto();
			    value.setFileContent(fileContent);
			    value.setResultMsg(call.getString(3));
			    return value;
			} finally {
				if (call != null) {
					call.close();
					call = null;
				}
			}
		}
	}
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @param titolo
	 * @param stato
	 * @param dataDa
	 * @param dataA
	 * @return
	 */
	private HibernateCallback<List<RichiestaRifornimentoDto>> findRichiesteRifornimento(final Integer codFiegDl, final Integer codEdicolaDl, final String titolo, final String stato, final Timestamp dataDa, final Timestamp dataA) {
		HibernateCallback<List<RichiestaRifornimentoDto>> action = new HibernateCallback<List<RichiestaRifornimentoDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<RichiestaRifornimentoDto> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(RichiestaRifornimentoVo.class, "rr");
				criteria.createCriteria("rr.storicoCopertineVo", "sc");
				criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
				criteria.add(Restrictions.eq("rr.pk.codFiegDl", codFiegDl));
				criteria.add(Restrictions.eq("rr.pk.codEdicola", codEdicolaDl));
				criteria.add(Restrictions.ne("rr.quantitaRichiesta", 0));
				if (stato == null) {
					criteria.add(Restrictions.isNull("rr.stato"));
				} else if (!stato.equals("") && !stato.equals("null")) {
					criteria.add(Restrictions.eq("rr.stato", stato));
				}
				if (!Strings.isNullOrEmpty(titolo)) {
					criteria.add(Restrictions.ilike("ap.titolo", titolo.toUpperCase(), MatchMode.ANYWHERE));
				} 
				if (dataDa != null && dataA != null) {
					criteria.add(Restrictions.between("rr.pk.dataOrdine", dataDa, dataA));
				}
				criteria.addOrder(Order.desc("rr.pk.dataOrdine"));
				ProjectionList properties = Projections.projectionList();
				properties.add(Projections.property("rr.pk"), "pk");
				properties.add(Projections.property("rr.pk.idtn"), "idtn");
				properties.add(Projections.property("rr.pk.dataOrdine"), "dataOrdine");
				properties.add(Projections.property("ap.titolo"), "titolo");
				properties.add(Projections.property("sc.codicePubblicazione"), "codicePubblicazione");
				properties.add(Projections.property("sc.sottoTitolo"), "sottoTitolo");
				properties.add(Projections.property("sc.numeroCopertina"), "numeroCopertina");
				properties.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertina"); 
				properties.add(Projections.property("sc.dataUscita"), "dataUscita");
				properties.add(Projections.property("rr.giorniValiditaRichiesteRifornimento"), "giorniValiditaRichiesteRifornimento");
				properties.add(Projections.property("rr.quantitaRichiesta"), "quantitaRichiesta");
				properties.add(Projections.property("rr.quantitaRichiestaGestioneClienti"), "quantitaRichiestaGestioneClienti");
				properties.add(Projections.property("rr.noteVendita"), "noteVendita");
				properties.add(Projections.property("sc.fornitoBolla"), "fornitoBolla");
				properties.add(Projections.property("sc.fornitoFondoBolla"), "fornitoFondoBolla");
				properties.add(Projections.property("rr.stato"), "stato");
				properties.add(Projections.property("rr.quantitaEvasa"), "quantitaEvasa");
				properties.add(Projections.property("rr.dataRispostaDl"), "dataRispostaDl");
				properties.add(Projections.property("rr.descCausaleNonEvadibilita"), "descCausaleNonEvadibilita");
				properties.add(Projections.property("rr.dataBollaAddebito"), "dataBollaAddebito");
				criteria.setProjection(properties); 
				criteria.setResultTransformer(Transformers.aliasToBean(RichiestaRifornimentoDto.class));
				session.enableFilter("FornitoFilter").setParameter("codEdicola", codEdicolaDl).setParameter("codEdicola2", codEdicolaDl);
				session.enableFilter("StoricoFilter").setParameter("dataStorico", new Timestamp(new Date(0L).getTime()));
				List<RichiestaRifornimentoDto> list = criteria.list();
				session.disableFilter("FornitoFilter");
				session.disableFilter("StoricoFilter");
				return list;
			}
		};
		return action;
	}
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @param titolo
	 * @param stato
	 * @param dataDa
	 * @param dataA
	 * @param listIdtn 
	 * @return
	 */
	private List<RichiestaRifornimentoDto> findRichiesteRifornimentoFondoBolla(Integer codFiegDl, Integer codEdicolaDl, Timestamp dataDa, Timestamp dataA, List<Integer> listIdtn) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FondoBollaDettaglioVo.class, "bd");
		criteria.createCriteria("bd.tipoFondoBollaVo", "fb");
		criteria.createCriteria("bd.storicoCopertineVo", "sc", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("bd.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("bd.pk.codEdicola", codEdicolaDl));
		criteria.add(Restrictions.in("bd.idtn", listIdtn));
		criteria.add(Restrictions.eq("fb.tipoRecordFondoBolla", IGerivConstants.TIPO_RECORD_FONDO_BOLLA_RIFORNIMENTI));
		if (dataDa != null && dataA != null) {
			criteria.add(Restrictions.between("bd.pk.dtBolla", dataDa, dataA));
		}
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("bd.idtn"), "idtn");
		properties.add(Projections.property("bd.pk.dtBolla"), "dataOrdine");
		properties.add(Projections.property("bd.titolo"), "titolo"); 
		properties.add(Projections.property("sc.codicePubblicazione"), "codicePubblicazione");
		properties.add(Projections.property("bd.sottoTitolo"), "sottoTitolo"); 
		properties.add(Projections.property("sc.numeroCopertina"), "numeroCopertina"); 
		properties.add(Projections.property("bd.prezzoLordo"), "prezzoCopertina");
		properties.add(Projections.property("sc.dataUscita"), "dataUscita");
		properties.add(Projections.property("bd.quantitaConsegnata"), "quantitaEvasa");
		properties.add(Projections.property("bd.pk.dtBolla"), "dataRispostaDl");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(RichiestaRifornimentoDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @param codCliente
	 * @param titolo
	 * @param stato
	 * @param idtn
	 * @param provenienza 
	 * @param dataA 
	 * @param dataDa 
	 * @return DetachedCriteria
	 */
	private DetachedCriteria buildRichiestaClienteVoCriteria(Integer[] codFiegDl,
			Integer[] codEdicolaDl, List<Long> codCliente, String titolo,
			String stato, Integer idtn, Integer provenienza, Timestamp dataDa, Timestamp dataA) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaClienteVo.class, "rc");
		criteria.createCriteria("rc.clienteEdicolaVo", "ce");
		criteria.createCriteria("rc.storicoCopertineVo", "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
		if (titolo != null && !titolo.equals("")) {
			criteria.add(Restrictions.ilike("ap.titolo", titolo.toUpperCase(), MatchMode.ANYWHERE));
		} 
		if (dataDa != null) {
			criteria.add(Restrictions.ge("rc.pk.dataInserimento", dataDa));
		}
		if (dataA != null) {
			criteria.add(Restrictions.le("rc.pk.dataInserimento", dataA));
		}
		if (stato != null && !stato.equals("")) {
			List<Integer> lisStatoEvasione = new ArrayList<Integer>();
			switch (Integer.parseInt(stato)) {
				case IGerivConstants.PRENOTAZIONI_INSERITE:
					lisStatoEvasione.add(IGerivConstants.COD_STATO_EVASIONE_INSERITO);
					break;
				case IGerivConstants.PRENOTAZIONI_EVASE:
					lisStatoEvasione.add(IGerivConstants.COD_STATO_EVASIONE_COMPLETA);
					break;
				case IGerivConstants.PRENOTAZIONI_PARZIALMENTE_EVASE:
					lisStatoEvasione.add(IGerivConstants.COD_STATO_EVASIONE_PARZIALE);
					break;
				case IGerivConstants.PRENOTAZIONI_INSERITE_O_PARZIALMENTE_EVASE:
					lisStatoEvasione.add(IGerivConstants.COD_STATO_EVASIONE_INSERITO);
					lisStatoEvasione.add(IGerivConstants.COD_STATO_EVASIONE_PARZIALE);
					break;
			}
			criteria.add(Restrictions.in("statoEvasione", lisStatoEvasione.toArray()));
		} 
		if (codCliente != null && !codCliente.isEmpty()) {
			criteria.add(Restrictions.in("pk.codCliente", codCliente));
		} 
		if (idtn != null && !idtn.equals("")) {
			criteria.add(Restrictions.eq("pk.idtn", idtn));
		} 
		if (provenienza != null && !provenienza.equals("")) {
			criteria.add(Restrictions.eq("pk.provenienza", provenienza));
		} 
		criteria.add(Restrictions.in("pk.codDl", codFiegDl));
		criteria.add(Restrictions.in("pk.codEdicola", codEdicolaDl));
		criteria.add(Restrictions.gt("rc.quantitaRichiesta", 0));
		
		if (codCliente != null && codCliente.size() > 1) {
			criteria.addOrder(Order.asc("ce.nome")).addOrder(Order.desc("pk.dataInserimento"));
		}
		
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("pk"), "pk");
		properties.add(Projections.property("ap.titolo"), "titolo"); 
		properties.add(Projections.property("sc.sottoTitolo"), "sottoTitolo");
		properties.add(Projections.property("ce.nome"), "nome"); 
		properties.add(Projections.property("ce.cognome"), "cognome");
		properties.add(Projections.property("ce.email"), "email");
		properties.add(Projections.property("sc.numeroCopertina"), "numeroCopertina");
		properties.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertina");
		properties.add(Projections.property("pk.dataInserimento"), "dataOrdine");
		properties.add(Projections.property("dataUltimaRisposta"), "dataUltimaRisposta");
		properties.add(Projections.property("quantitaRichiesta"), "quantitaRichiesta");
		properties.add(Projections.property("quantitaEvasa"), "quantitaEvasa");
		properties.add(Projections.property("statoEvasione"), "statoEvasione");
		properties.add(Projections.property("codiceUltimaRisposta"), "codiceUltimaRisposta");
		properties.add(Projections.property("descrizioneUltimaRisposta"), "messagioLibero");
		properties.add(Projections.property("pk.provenienza"), "provenienza");
		properties.add(Projections.property("ce.codCliente"), "codCliente");
		properties.add(Projections.property("ce.dtSospensionePrenotazioniDa"), "dtSospensionePrenotazioniDa");
		properties.add(Projections.property("ce.dtSospensionePrenotazioniA"), "dtSospensionePrenotazioniA");
		properties.add(Projections.property("ce.tipoEstrattoConto"), "tipoEstrattoConto");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(RichiestaClienteDto.class));
		
		return criteria;
	}
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @param codCliente
	 * @param titolo
	 * @param dataA 
	 * @param dataDa 
	 * @return
	 */
	private DetachedCriteria buildRichiestaFissaClienteEdicolaVoCriteria(
			Integer[] codFiegDl, Integer[] codEdicolaDl, Long codCliente,
			String titolo, Timestamp dataDa, Timestamp dataA) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaFissaClienteEdicolaVo.class, "rf");
		criteria.createCriteria("rf.anagraficaPubblicazioniVo", "ap");
		if (titolo != null && !titolo.equals("")) {
			criteria.add(Restrictions.ilike("ap.titolo", titolo.toUpperCase(), MatchMode.ANYWHERE));
		} 
		if (dataDa != null && dataA != null) {
			criteria.add(Restrictions.between("rf.dataRichiesta", dataDa, dataA));
		}
		if (codCliente != null && !codCliente.equals("")) {
			criteria.add(Restrictions.eq("rf.pk.codCliente", codCliente));
		} 
		criteria.add(Restrictions.in("rf.pk.codDl", codFiegDl));
		criteria.add(Restrictions.in("rf.pk.codEdicola", codEdicolaDl));
		criteria.add(Restrictions.gt("rf.quantitaRichiesta", 0));
		
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("ap.titolo"), "titolo"); 
		properties.add(Projections.property("rf.dataRichiesta"), "dataOrdine");
		properties.add(Projections.property("rf.quantitaRichiesta"), "quantitaRichiesta");
		properties.add(Projections.property("rf.pk"), "pk");
		criteria.setProjection(properties);
		criteria.setResultTransformer(Transformers.aliasToBean(RichiestaFissaClienteDto.class));
		
		return criteria;
	}
	
	/**
	 * @param idtn
	 * @param rifornimenti
	 * @return
	 */
	private RichiestaClienteVo getRichiestaClienteVoByIdtn(Integer idtn,
			List<RichiestaClienteVo> rifornimenti) {
		for (RichiestaClienteVo vo : rifornimenti) {
			if (vo.getPk().getIdtn().equals(idtn)) {
				return vo;
			}
		}
		return null;
	}
	
	/**
	 * @param idtn
	 * @param rifornimenti
	 * @return
	 */
	private List<RichiestaRifornimentoVo> getRichiesteRifornimentoVoByIdtn(Integer idtn,
			List<RichiestaRifornimentoVo> rifornimenti) {
		List<RichiestaRifornimentoVo> list = new ArrayList<RichiestaRifornimentoVo>();
		for (RichiestaRifornimentoVo vo : rifornimenti) {
			if (vo.getPk().getIdtn().equals(idtn)) {
				list.add(vo);
			}
		}
		return list;
	}
	
	@Override
	public Boolean hasNotificheOrdiniClienti(Integer codFiegDl, Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaClienteVo.class, "rcvo");
		criteria.add(Restrictions.eq("rcvo.pk.codDl", codFiegDl));
		criteria.add(Restrictions.eq("rcvo.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("rcvo.pk.provenienza", IGerivConstants.PROVENIENZA_CLIENTE));
		criteria.add(Restrictions.eq("rcvo.notificareOrdineRivendita", new Boolean(true)));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.count("rcvo.pk.codEdicola"));
		criteria.setProjection(props);
		Number count = (Number) getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		return (count != null && count.intValue() > 0) ? true : false;
	}
	
	@Override
	public RichiestaRifornimentoVo getRichiestaRifornimento(Integer codFiegDl, Integer codEdicolaWeb, Integer idtn, Integer ordine) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaRifornimentoVo.class, "eced");
		criteria.add(Restrictions.eq("eced.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("eced.pk.codEdicola", codEdicolaWeb));
		criteria.add(Restrictions.eq("eced.pk.idtn", idtn));
		criteria.add(Restrictions.eq("eced.ordine", ordine));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public RichiestaRifornimentoVo getRichiestaRifornimento(Integer codFiegDl, Integer codEdicolaWeb, Integer idtn) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaRifornimentoVo.class, "eced");
		criteria.add(Restrictions.eq("eced.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("eced.pk.codEdicola", codEdicolaWeb));
		criteria.add(Restrictions.eq("eced.pk.idtn", idtn));
		//criteria.add(Restrictions.isNotNull("eced.dataRispostaDl"));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
		
	}
	
	@Override
	public RichiestaRifornimentoVo getRichiestaRifornimento(Integer codFiegDl, Integer codEdicolaWeb,
			Timestamp dataInserimento, Integer idtn) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaRifornimentoVo.class, "eced");
		criteria.add(Restrictions.eq("eced.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("eced.pk.codEdicola", codEdicolaWeb));
		criteria.add(Restrictions.eq("eced.pk.dataOrdine", dataInserimento));
		criteria.add(Restrictions.eq("eced.pk.idtn", idtn));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	
	@Override
	public void updateOrdiniClientiNotifiche(List<RichiestaClientePk> pks) {
		for (RichiestaClientePk pk : pks) {
			getDao().bulkUpdate(IGerivQueryContants.SQL_UPDATE_ORDINI_NOTIFICHE_CLIENTI, new Object[]{false, pk.getCodEdicola(), pk.getCodCliente(), pk.getProvenienza(), pk.getDataInserimento(), pk.getCodDl(), pk.getIdtn()});
		}
	}
	
	@Override
	public RichiestaClienteVo getRichiestaClienteVo(Integer codEdicola, Long codCliente, Integer provenienza, Timestamp dataInserimento, Integer codDl, Integer idtn) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaClienteVo.class, "rc");
		criteria.add(Restrictions.eq("rc.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("rc.pk.codCliente", codCliente));
		criteria.add(Restrictions.eq("rc.pk.provenienza", provenienza));
		criteria.add(Restrictions.eq("rc.pk.dataInserimento", dataInserimento));
		criteria.add(Restrictions.eq("rc.pk.codDl", codDl));
		criteria.add(Restrictions.eq("rc.pk.idtn", idtn));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public RichiestaFissaClienteEdicolaVo getRichiestaFissaClienteEdicolaVo(Integer codEdicola, Long codCliente, Integer codDl, Integer codicePubblicazione) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaFissaClienteEdicolaVo.class, "rc");
		criteria.add(Restrictions.eq("rc.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("rc.pk.codCliente", codCliente));
		criteria.add(Restrictions.eq("rc.pk.codDl", codDl));
		criteria.add(Restrictions.eq("rc.pk.codicePubblicazione", codicePubblicazione));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public Boolean hasPrenotazioniFisseNonEvase(final Integer codEdicola, final Integer codDl, final Timestamp dtBolla, final String tipoBolla) {
		HibernateCallback<Boolean> action = new HibernateCallback<Boolean>() {
			@Override
			public Boolean doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery("select count(*) from (" + IGerivQueryContants.SQL_GET_ORDINI_IN_BOLLA_NON_EVASI + ")");
				sqlQuery.setInteger("codEdicola", codEdicola);
				sqlQuery.setTimestamp("dtBolla", dtBolla);
				sqlQuery.setString("tipoBolla", tipoBolla);
				sqlQuery.setInteger("coddl", codDl);
				return ((BigDecimal) sqlQuery.uniqueResult()).intValue() > 0 ? true : false;
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public List<PrenotazioneDto> getPrenotazioniFisseNonEvase(final Integer codEdicola, final Integer codDl, final Timestamp dtBolla, final String tipoBolla) {
		HibernateCallback<List<PrenotazioneDto>> action = new HibernateCallback<List<PrenotazioneDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<PrenotazioneDto> doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery(IGerivQueryContants.SQL_GET_ORDINI_IN_BOLLA_NON_EVASI);
				sqlQuery.setInteger("codEdicola", codEdicola);
				sqlQuery.setTimestamp("dtBolla", dtBolla);
				sqlQuery.setString("tipoBolla", tipoBolla);
				sqlQuery.setInteger("coddl", codDl);
				sqlQuery.setResultTransformer( Transformers.aliasToBean(PrenotazioneDto.class));
				sqlQuery.addScalar("quantitaInBolla", IntegerType.INSTANCE);
				sqlQuery.addScalar("titolo", StringType.INSTANCE);
				sqlQuery.addScalar("sottotitolo", StringType.INSTANCE);
				sqlQuery.addScalar("numero", StringType.INSTANCE);
				sqlQuery.addScalar("idtn", IntegerType.INSTANCE);
				sqlQuery.addScalar("quantitaRichiesta", IntegerType.INSTANCE);
				return sqlQuery.list();
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public List<RichiestaRifornimentoDto> getRichiesteRifornimento(final Integer codDl, final Timestamp dataOrdineIniziale, final Timestamp dataOrdineFinale, final Integer codRivenditaDl, final Integer codPubblicazione, final Integer idtn) {
		HibernateCallback<List<RichiestaRifornimentoDto>> action = new HibernateCallback<List<RichiestaRifornimentoDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<RichiestaRifornimentoDto> doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuilder query = new StringBuilder();
				query.append("select dataord9131 as dataOrdine, datscad9131 as dataScadenza, quaric9131 as quantitaRichiesta, stato9131 as stato, quaev9131 as quantitaEvasa, datarisp9131 as dataRispostaDl, acausne9131 as descCausaleNonEvadibilita, ");
				query.append("cpu9606 as codicePubblicazione, titolo9606 as titolo, num9607 as numeroCopertina, crivdl9206 as codRivendita, nomeriva9106 as ragioneSocialeRivendita ");
				query.append("from tbl_9131 inner join tbl_9607 on (coddl9131 = coddl9607 and idtn9131 = idtn9607) ");
				query.append("inner join tbl_9606 on (coddl9607 = coddl9606 and cpu9607 = cpu9606) ");
				query.append("inner join tbl_9206 on (crivw9131 = crivw9206) ");
				query.append("inner join tbl_9106 on (crivw9131 = crivw9106) ");
				query.append("where coddl9131 = :codDl and trunc(dataord9131, 'dd') between :dataOrdineIniziale and :dataOrdineFinale ");
				if (codRivenditaDl != null) {
					query.append("and crivdl9206 = :codRivendita ");
				}
				if (idtn != null) {
					query.append("and idtn9607 = :idtn ");
				}
				else if (codPubblicazione != null) {
					query.append("and cpu9607 = :codPubblicazione ");
				}
				query.append("order by trunc(dataord9131), titolo9606, num9607, nomeriva9106");
				
				SQLQuery sqlQuery = session.createSQLQuery(query.toString());
				sqlQuery.setInteger("codDl", codDl);
				sqlQuery.setTimestamp("dataOrdineIniziale", dataOrdineIniziale);
				sqlQuery.setTimestamp("dataOrdineFinale", dataOrdineFinale);
				if (codRivenditaDl != null) {
					sqlQuery.setInteger("codRivendita", codRivenditaDl);
				}
				if (idtn != null) {
					sqlQuery.setInteger("idtn", idtn);
				}
				else if (codPubblicazione != null) {
					sqlQuery.setInteger("codPubblicazione", codPubblicazione);
				}
				
				sqlQuery.setResultTransformer(Transformers.aliasToBean(RichiestaRifornimentoDto.class));
				sqlQuery.addScalar("dataOrdine", TimestampType.INSTANCE);
				sqlQuery.addScalar("dataScadenza", TimestampType.INSTANCE);
				sqlQuery.addScalar("quantitaRichiesta", IntegerType.INSTANCE);				
				sqlQuery.addScalar("stato", StringType.INSTANCE);
				sqlQuery.addScalar("quantitaEvasa", IntegerType.INSTANCE);				
				sqlQuery.addScalar("dataRispostaDl", TimestampType.INSTANCE);				
				sqlQuery.addScalar("descCausaleNonEvadibilita", StringType.INSTANCE);				
				sqlQuery.addScalar("codicePubblicazione", IntegerType.INSTANCE);				
				sqlQuery.addScalar("titolo", StringType.INSTANCE);				
				sqlQuery.addScalar("numeroCopertina", StringType.INSTANCE);
				sqlQuery.addScalar("codRivendita", IntegerType.INSTANCE);
				sqlQuery.addScalar("ragioneSocialeRivendita", StringType.INSTANCE);
				
				return sqlQuery.list();
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
}
