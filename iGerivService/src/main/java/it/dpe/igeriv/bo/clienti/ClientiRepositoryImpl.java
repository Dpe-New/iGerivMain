package it.dpe.igeriv.bo.clienti;


import static ch.lambdaj.Lambda.forEach;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.sort;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.comparator.PubblicazioniRitiriComparator;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.ClienteEdicolaDto;
import it.dpe.igeriv.dto.DateEstrattoContoClienteDto;
import it.dpe.igeriv.dto.EstrattoContoClienti;
import it.dpe.igeriv.dto.EstrattoContoClientiProdottiDto;
import it.dpe.igeriv.dto.EstrattoContoClientiPubblicazioniDto;
import it.dpe.igeriv.dto.EstrattoContoFatturaClientiDto;
import it.dpe.igeriv.dto.FileFatturaDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.AnagraficaBancaVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import it.dpe.igeriv.vo.EmailTemplateVo;
import it.dpe.igeriv.vo.FatturaClienteEdicolaVo;
import it.dpe.igeriv.vo.MetodoPagamentoClienteVo;
import it.dpe.igeriv.vo.PagamentoClientiEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiBollaDettaglioVo;
import it.dpe.igeriv.vo.RichiestaClienteVo;
import it.dpe.igeriv.vo.RichiestaFissaClienteEdicolaVo;
import it.dpe.igeriv.vo.VenditaDettaglioVo;
import it.dpe.igeriv.vo.VenditaVo;
import it.dpe.igeriv.vo.pk.PagamentoClientiEdicolaPk;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;

@Repository
class ClientiRepositoryImpl extends BaseRepositoryImpl implements ClientiRepository {
	private final String daysEstrattoContoClienti;
	
	@Autowired
	ClientiRepositoryImpl(BaseDao<ClienteEdicolaVo> dao, @Value("${igeriv.giorni.estratto.conto.clienti.edicola}") String daysEstrattoContoClienti) {
		super(dao);
		this.daysEstrattoContoClienti = daysEstrattoContoClienti;
	}
	
	@Override
	public ClienteEdicolaVo getCienteEdicolaByCodice(Long codCliente) {
		return getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_USER_CLIENTE_EDICOLA_BY_CODICE, codCliente);
	}
	
	@Override
	public ClienteEdicolaVo getCienteEdicolaByEmail(String email) {
		return getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_USER_CLIENTE_EDICOLA_BY_EMAIL_JOINS, email);
	}
	
	@Override
	public ClienteEdicolaVo getCienteEdicolaByCodiceLogin(Long codCliente) {
		return getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_USER_CLIENTE_EDICOLA_BY_CODICE_JOINS, codCliente);
	}
	
	@Override
	public List<ClienteEdicolaDto> getClientiEdicola(Integer[] codEdicola, String nome, String cognome, String codiceFiscale, String piva) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ClienteEdicolaVo.class, "ce");
		criteria.createCriteria("tipoLocalita", "tl", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("localita", "lo", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("paese", "pa", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("provincia", "pr", JoinType.LEFT_OUTER_JOIN);
		
		if (codEdicola != null && codEdicola.length > 0) {
			criteria.add(Restrictions.in("codEdicola", codEdicola));
		}
		if (nome != null && !nome.equals("")) {
			criteria.add(Restrictions.ilike("nome", nome, MatchMode.ANYWHERE));
		}
		if (cognome != null && !cognome.equals("")) {
			criteria.add(Restrictions.ilike("cognome", cognome, MatchMode.ANYWHERE));
		}
		if (codiceFiscale != null && !codiceFiscale.equals("")) {
			criteria.add(Restrictions.eq("codiceFiscale", codiceFiscale));
		}
		if (piva != null && !piva.equals("")) {
			criteria.add(Restrictions.eq("piva", piva));
		}
		criteria.addOrder(Order.asc("ce.nome"));
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("ce.codCliente"), "codCliente");
		properties.add(Projections.property("ce.nome"), "nome"); 
		properties.add(Projections.property("ce.cognome"), "cognome"); 
		properties.add(Projections.property("lo.descrizione"), "localitaDesc"); 
		properties.add(Projections.property("pr.descrizione"), "provinciaDesc");
		properties.add(Projections.property("ce.cap"), "cap");
		properties.add(Projections.property("ce.telefono"), "telefono");
		properties.add(Projections.property("ce.email"), "email");
		properties.add(Projections.property("ce.tipoEstrattoConto"), "tipoEstrattoConto");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(ClienteEdicolaDto.class));
		
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<ClienteEdicolaVo> getClientiEdicola(Integer[] codEdicola, List<Long> listCodClienti) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ClienteEdicolaVo.class, "ce");
		criteria.add(Restrictions.in("ce.codEdicola", codEdicola));
		criteria.add(Restrictions.in("ce.codCliente", listCodClienti));
		criteria.createCriteria("ce.localita", "loc", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.paese", "pa", JoinType.LEFT_OUTER_JOIN);
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Set<ClienteEdicolaDto> getClientiEdicolaByPubblicazione(Integer[] codEdicola, String titolo, String sottotitolo, String numero, String argomento, String periodicita, BigDecimal prezzo, Integer codPubblicazione, String codBarre, boolean conPrenotazioniFisse) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaClienteVo.class, "rcvo");
		criteria.createCriteria("rcvo.storicoCopertineVo", "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
		criteria.createCriteria("ap.periodicitaVo", "per", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ap.argomentoVo", "arg", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("rcvo.clienteEdicolaVo", "cevo");
		criteria.createCriteria("cevo.tipoLocalita", "tl", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("cevo.localita", "lo", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("cevo.paese", "pa", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("cevo.provincia", "pr", JoinType.LEFT_OUTER_JOIN);
		
		if (codEdicola != null && codEdicola.length > 0) {
			criteria.add(Restrictions.in("cevo.codEdicola", codEdicola));
		}
		if (titolo != null && !titolo.equals("")) {
			criteria.add(Restrictions.ilike("ap.titolo", titolo, MatchMode.ANYWHERE));
		}
		if (sottotitolo != null && !sottotitolo.equals("")) {
			criteria.add(Restrictions.ilike("sc.sottoTitolo", sottotitolo, MatchMode.ANYWHERE));
		}
		if (numero != null && !numero.equals("")) {
			criteria.add(Restrictions.eq("sc.numeroCopertina", numero));
		}
		if (periodicita != null && !periodicita.equals("")) {
			String[] arrPk = periodicita.split("\\|");
			criteria.add(Restrictions.eq("per.pk.periodicita", new Integer(arrPk[1])));
		}
		if (argomento != null && !argomento.equals("")) {
			String[] arrPk = argomento.split("\\|");
			criteria.add(Restrictions.eq("arg.pk.segmento", new Integer(arrPk[1])));
		}
		if (prezzo != null) {
			criteria.add(Restrictions.eq("sc.prezzoCopertina", prezzo));
		}
		if (codPubblicazione != null) {
			criteria.add(Restrictions.eq("sc.codicePubblicazione", codPubblicazione));
		}
		if (!Strings.isNullOrEmpty(codBarre)) {
			criteria.add(Restrictions.eq("sc.codiceBarre", codBarre));
		}
		criteria.addOrder(Order.asc("cevo.nome"));
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.groupProperty("cevo.codCliente"), "codCliente");
		properties.add(Projections.groupProperty("cevo.nome"), "nome"); 
		properties.add(Projections.groupProperty("cevo.cognome"), "cognome"); 
		properties.add(Projections.groupProperty("lo.descrizione"), "localitaDesc"); 
		properties.add(Projections.groupProperty("pr.descrizione"), "provinciaDesc");
		properties.add(Projections.groupProperty("cevo.cap"), "cap");
		properties.add(Projections.groupProperty("cevo.telefono"), "telefono");
		properties.add(Projections.groupProperty("cevo.email"), "email");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(ClienteEdicolaDto.class));
		
		Set<ClienteEdicolaDto> prenotazioni = new HashSet(getDao().findObjectByDetachedCriteria(criteria));
		
		if (conPrenotazioniFisse) {
			DetachedCriteria criteria1 = DetachedCriteria.forClass(RichiestaFissaClienteEdicolaVo.class, "rcvo");
			criteria1.createCriteria("rcvo.anagraficaPubblicazioniVo", "ap");
			criteria1.createCriteria("ap.periodicitaVo", "per", JoinType.LEFT_OUTER_JOIN);
			criteria1.createCriteria("ap.argomentoVo", "arg", JoinType.LEFT_OUTER_JOIN);
			criteria1.createCriteria("rcvo.clienteEdicolaVo", "cevo");
			criteria1.createCriteria("cevo.tipoLocalita", "tl", JoinType.LEFT_OUTER_JOIN);
			criteria1.createCriteria("cevo.localita", "lo", JoinType.LEFT_OUTER_JOIN);
			criteria1.createCriteria("cevo.paese", "pa", JoinType.LEFT_OUTER_JOIN);
			criteria1.createCriteria("cevo.provincia", "pr", JoinType.LEFT_OUTER_JOIN);
			
			if (codEdicola != null && codEdicola.length > 0) {
				criteria1.add(Restrictions.in("cevo.codEdicola", codEdicola));
			}
			if (titolo != null && !titolo.equals("")) {
				criteria1.add(Restrictions.ilike("ap.titolo", titolo, MatchMode.ANYWHERE));
			}
			if (periodicita != null && !periodicita.equals("")) {
				String[] arrPk = periodicita.split("\\|");
				criteria1.add(Restrictions.eq("per.pk.periodicita", new Integer(arrPk[1])));
			}
			if (argomento != null && !argomento.equals("")) {
				String[] arrPk = argomento.split("\\|");
				criteria1.add(Restrictions.eq("arg.pk.segmento", new Integer(arrPk[1])));
			}
			criteria1.addOrder(Order.asc("cevo.nome"));
			ProjectionList properties1 = Projections.projectionList(); 
			properties1.add(Projections.groupProperty("cevo.codCliente"), "codCliente");
			properties1.add(Projections.groupProperty("cevo.nome"), "nome"); 
			properties1.add(Projections.groupProperty("cevo.cognome"), "cognome"); 
			properties1.add(Projections.groupProperty("lo.descrizione"), "localitaDesc"); 
			properties1.add(Projections.groupProperty("pr.descrizione"), "provinciaDesc");
			properties1.add(Projections.groupProperty("cevo.cap"), "cap");
			properties1.add(Projections.groupProperty("cevo.telefono"), "telefono");
			properties1.add(Projections.groupProperty("cevo.email"), "email");
			criteria1.setProjection(properties1); 
			criteria1.setResultTransformer(Transformers.aliasToBean(ClienteEdicolaDto.class));
			
			Set<ClienteEdicolaDto> prenotazioniFisse = new HashSet(getDao().findObjectByDetachedCriteria(criteria1));
			prenotazioni.addAll(prenotazioniFisse);
		}
		
		return prenotazioni;
	}
	 
	@Override
	public List<ClienteEdicolaDto> getClientiConEvasione(
			Integer[] codEdicola, String nome, String cognome,
			String codiceFiscale, String piva, boolean conPrenotazioniFisse) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaClienteVo.class, "rc");
		criteria.createCriteria("rc.clienteEdicolaVo", "ce");
		criteria.createCriteria("ce.tipoLocalita", "tl", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.localita", "lo", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.paese", "pa", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.provincia", "pr", JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.le("rc.statoEvasione", IGerivConstants.COD_STATO_EVASIONE_PARZIALE));
		
		if (codEdicola != null && codEdicola.length > 0) {
			criteria.add(Restrictions.in("ce.codEdicola", codEdicola));
		}
		if (nome != null && !nome.equals("")) {
			criteria.add(Restrictions.ilike("ce.nome", nome, MatchMode.ANYWHERE));
		}
		if (cognome != null && !cognome.equals("")) {
			criteria.add(Restrictions.ilike("ce.cognome", cognome, MatchMode.ANYWHERE));
		}
		if (codiceFiscale != null && !codiceFiscale.equals("")) {
			criteria.add(Restrictions.eq("ce.codiceFiscale", codiceFiscale));
		}
		if (piva != null && !piva.equals("")) {
			criteria.add(Restrictions.eq("ce.piva", piva));
		}
		
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("ce.codCliente"), "codCliente");
		properties.add(Projections.property("ce.nome"), "nome"); 
		properties.add(Projections.property("ce.cognome"), "cognome"); 
		properties.add(Projections.property("lo.descrizione"), "localitaDesc"); 
		properties.add(Projections.property("pr.descrizione"), "provinciaDesc");
		properties.add(Projections.property("ce.cap"), "cap");
		properties.add(Projections.property("ce.telefono"), "telefono");
		properties.add(Projections.property("ce.email"), "email");
		properties.add(Projections.property("ce.tipoEstrattoConto"), "tipoEstrattoConto");
		properties.add(Projections.property("ce.dtSospensionePrenotazioniDa"), "dtSospensionePrenotazioniDa");
		properties.add(Projections.property("ce.dtSospensionePrenotazioniA"), "dtSospensionePrenotazioniA");
		// Modificato da Vincenzo il 05/07/2013: 
		// ho sostituito qtaOrdiniDaEvadere con qtaOrdiniRichiesta (nuovo campo del dto)
		// ho aggiunto il campo qtaOrdiniEvasa
		// il campo qtaOrdiniDaEvadere non esiste più: al suo posto c'è il solo metodo get che restituisce la differenza tra qtaOrdiniRichiesta e qtaOrdiniEvasa
		properties.add(Projections.sum("rc.quantitaRichiesta"), "qtaOrdiniRichiesta");
		properties.add(Projections.sum("rc.quantitaEvasa"), "qtaOrdiniEvasa");
		properties.add(Projections.groupProperty("ce.codCliente")).add(Projections.groupProperty("ce.nome")).add(Projections.groupProperty("ce.cognome")).add(Projections.groupProperty("lo.descrizione")).add(Projections.groupProperty("pr.descrizione")).add(Projections.groupProperty("ce.cap")).add(Projections.groupProperty("ce.telefono")).add(Projections.groupProperty("ce.email")).add(Projections.groupProperty("ce.tipoEstrattoConto")).add(Projections.groupProperty("ce.dtSospensionePrenotazioniDa")).add(Projections.groupProperty("ce.dtSospensionePrenotazioniA"));
		
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(ClienteEdicolaDto.class));
		
		List<ClienteEdicolaDto> richieste = getDao().findObjectByDetachedCriteria(criteria);
		
		if (richieste != null && !richieste.isEmpty()) {
			Timestamp now = new Timestamp(new Date().getTime());
			richieste = select(richieste, having(on(ClienteEdicolaDto.class).isDateBetweenDateSospensione(now), equalTo(false)));
		}
		
		if (conPrenotazioniFisse) {
			DetachedCriteria criteria1 = DetachedCriteria.forClass(RichiestaFissaClienteEdicolaVo.class, "rc");
			criteria1.createCriteria("rc.clienteEdicolaVo", "ce");
			criteria1.createCriteria("ce.tipoLocalita", "tl", JoinType.LEFT_OUTER_JOIN);
			criteria1.createCriteria("ce.localita", "lo", JoinType.LEFT_OUTER_JOIN);
			criteria1.createCriteria("ce.paese", "pa", JoinType.LEFT_OUTER_JOIN);
			criteria1.createCriteria("ce.provincia", "pr", JoinType.LEFT_OUTER_JOIN);
			
			criteria1.add(Restrictions.or(Restrictions.eq("rc.numeroVolte", -1), Restrictions.gt("rc.numeroVolte", 0)));
			
			if (codEdicola != null && codEdicola.length > 0) {
				criteria1.add(Restrictions.in("ce.codEdicola", codEdicola));
			}
			if (nome != null && !nome.equals("")) {
				criteria1.add(Restrictions.ilike("ce.nome", nome, MatchMode.ANYWHERE));
			}
			if (cognome != null && !cognome.equals("")) {
				criteria1.add(Restrictions.ilike("ce.cognome", cognome, MatchMode.ANYWHERE));
			}
			if (codiceFiscale != null && !codiceFiscale.equals("")) {
				criteria1.add(Restrictions.eq("ce.codiceFiscale", codiceFiscale));
			}
			if (piva != null && !piva.equals("")) {
				criteria1.add(Restrictions.eq("ce.piva", piva));
			}
			
			ProjectionList properties1 = Projections.projectionList(); 
			properties1.add(Projections.property("ce.codCliente"), "codCliente");
			properties1.add(Projections.property("ce.nome"), "nome"); 
			properties1.add(Projections.property("ce.cognome"), "cognome"); 
			properties1.add(Projections.property("lo.descrizione"), "localitaDesc"); 
			properties1.add(Projections.property("pr.descrizione"), "provinciaDesc");
			properties1.add(Projections.property("ce.cap"), "cap");
			properties1.add(Projections.property("ce.telefono"), "telefono");
			properties1.add(Projections.property("ce.email"), "email");
			properties1.add(Projections.property("ce.tipoEstrattoConto"), "tipoEstrattoConto");
			properties1.add(Projections.property("ce.dtSospensionePrenotazioniDa"), "dtSospensionePrenotazioniDa");
			properties1.add(Projections.property("ce.dtSospensionePrenotazioniA"), "dtSospensionePrenotazioniA");
			properties1.add(Projections.sum("rc.quantitaRichiesta"),"qtaOrdiniRichiesta");
			properties1.add(Projections.groupProperty("ce.codCliente")).add(Projections.groupProperty("ce.nome")).add(Projections.groupProperty("ce.cognome")).add(Projections.groupProperty("lo.descrizione")).add(Projections.groupProperty("pr.descrizione")).add(Projections.groupProperty("ce.cap")).add(Projections.groupProperty("ce.telefono")).add(Projections.groupProperty("ce.email")).add(Projections.groupProperty("ce.tipoEstrattoConto")).add(Projections.groupProperty("ce.dtSospensionePrenotazioniDa")).add(Projections.groupProperty("ce.dtSospensionePrenotazioniA"));
			
			criteria1.setProjection(properties1); 
			criteria1.setResultTransformer(Transformers.aliasToBean(ClienteEdicolaDto.class));
			List<ClienteEdicolaDto> richiesteFisse = getDao().findObjectByDetachedCriteria(criteria1);
			
			if (richiesteFisse != null && !richiesteFisse.isEmpty()) {
				Timestamp now = new Timestamp(new Date().getTime());
				richieste = select(richieste, having(on(ClienteEdicolaDto.class).isDateBetweenDateSospensione(now), equalTo(false)));
			}
			for (ClienteEdicolaDto cedto : richiesteFisse) {
				if (!richieste.contains(cedto)) {
					richieste.add(cedto);
				}
			}
		}
		
		return richieste;
	}
	
	@Override
	public ClienteEdicolaVo getClienteEdicola(Integer[] codEdicola, Long idCliente) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ClienteEdicolaVo.class, "ce");
		criteria.add(Restrictions.in("ce.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("ce.codCliente", idCliente));
		criteria.createCriteria("ce.tipoLocalita", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.provincia", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.localita", "loc", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.paese", "pa", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.banca", "ba", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.tipoPagamento", "tp", JoinType.LEFT_OUTER_JOIN);
		return getDao().findUniqueResultByDetachedCriteria(criteria);
		
	}
	
	
	@Override
	public List<ClienteEdicolaDto> getClientiEdicolaConEstrattoConto(final Integer[] codEdicola, final List<Integer> tipiEstrattoConto, final Timestamp dataCompetenza, final Boolean estrattoContoChiuso, final String nome, final String cognome, final String codiceFiscale, final String piva, final Integer tipoProdottiInEstrattoConto, final List<Long> codClienti) {
		HibernateCallback<List<ClienteEdicolaDto>> action = new HibernateCallback<List<ClienteEdicolaDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<ClienteEdicolaDto> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(VenditaVo.class, "ve");
				criteria.createCriteria("ve.cliente", "ce");
				criteria.createCriteria("ve.contoProdottiVari", "cpro", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("ce.provincia", "pr", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("ce.localita", "loc", JoinType.LEFT_OUTER_JOIN);
				if (codClienti != null && !codClienti.isEmpty()) {
					criteria.add(Restrictions.in("ce.codCliente", codClienti));
					if (dataCompetenza != null) {
						criteria.add(Restrictions.eq("ve.dataCompetenzaEstrattoContoClienti", dataCompetenza));
					}
				} else if (dataCompetenza != null) {
					criteria.add(Restrictions.le("ve.dataCompetenzaEstrattoContoClienti", dataCompetenza));
				}
				criteria.add(Restrictions.in("ce.codEdicola", codEdicola));
				if (tipiEstrattoConto != null) {
					criteria.add(Restrictions.in("ce.tipoEstrattoConto", tipiEstrattoConto));
				}
				if (estrattoContoChiuso == null || !estrattoContoChiuso) {
					criteria.add(Restrictions.isNull("ve.dataEstrattoConto"));
					criteria.add(Restrictions.or(Restrictions.eq("ve.fatturaEmessa", false), Restrictions.isNull("ve.fatturaEmessa")));
					criteria.add(Restrictions.or(Restrictions.eq("ve.pagato", false), Restrictions.isNull("ve.pagato")));
				} else {
					if (tipoProdottiInEstrattoConto != null && tipoProdottiInEstrattoConto.equals(IGerivConstants.TIPO_PRODOTTO_EDITORIALE)) {
						criteria.add(Restrictions.isNotNull("ve.dataEstrattoConto"));
					} else if (tipoProdottiInEstrattoConto != null && tipoProdottiInEstrattoConto.equals(IGerivConstants.TIPO_PRODOTTO_MISTO)) {
						criteria.add(Restrictions.eq("ve.fatturaEmessa", true));
					} else {
						criteria.add(Restrictions.or(Restrictions.eq("ve.fatturaEmessa", true), Restrictions.isNotNull("ve.dataEstrattoConto")));
					}
				}
				if (!Strings.isNullOrEmpty(nome)) {
					criteria.add(Restrictions.ilike("ce.nome", nome, MatchMode.START));
				}
				if (!Strings.isNullOrEmpty(cognome)) {
					criteria.add(Restrictions.ilike("ce.cognome", cognome, MatchMode.START));		
				}
				if (!Strings.isNullOrEmpty(codiceFiscale)) {
					criteria.add(Restrictions.eq("ce.codiceFiscale", codiceFiscale));
				}
				if (!Strings.isNullOrEmpty(piva)) {
					criteria.add(Restrictions.eq("ce.piva", piva));
				}
				criteria.add(Restrictions.or(Restrictions.eq("ve.deleted", false), Restrictions.isNull("ve.deleted")));
				criteria.addOrder(Order.asc("ce.nome"));
				ProjectionList props = Projections.projectionList(); 
				props.add(Projections.distinct(Projections.projectionList().add(Projections.property("ce.codCliente"), "codCliente")));
				props.add(Projections.property("ce.nome"), "nome");
				props.add(Projections.property("ce.cognome"), "cognome");
				props.add(Projections.property("loc.descrizione"), "localitaDesc"); 
				props.add(Projections.property("pr.descrizione"), "provinciaDesc");
				props.add(Projections.property("ce.cap"), "cap");
				props.add(Projections.property("ce.telefono"), "telefono");
				props.add(Projections.property("ce.email"), "email");
				if (dataCompetenza != null) {
					props.add(Projections.property("ce.totaleEstrattoConto"), "totaleEstrattoConto");
					props.add(Projections.property("ce.totaleEstrattoContoPne"), "totaleEstrattoContoPne");
				}
				criteria.setProjection(props);
				criteria.setResultTransformer(Transformers.aliasToBean(ClienteEdicolaDto.class));
				if (dataCompetenza != null) {
					session.enableFilter("DtCompetenzaFilter").setParameter("dtCompetenza", dataCompetenza);
				}
				List<ClienteEdicolaDto> list = criteria.list();
				if (dataCompetenza != null) {
					session.disableFilter("DtCompetenzaFilter");
				}
				return list;
			}
		};
		List<ClienteEdicolaDto> list = getDao().findByHibernateCallback(action);
		if (tipoProdottiInEstrattoConto != null && tipoProdottiInEstrattoConto.equals(IGerivConstants.TIPO_PRODOTTO_EDITORIALE)) {
			list = select(list, having(on(ClienteEdicolaDto.class).getTotaleEstrattoContoPne(), equalTo(new BigDecimal(0))));
		} else if (tipoProdottiInEstrattoConto != null && tipoProdottiInEstrattoConto.equals(IGerivConstants.TIPO_PRODOTTO_MISTO)) {
			list = select(list, having(on(ClienteEdicolaDto.class).getTotaleEstrattoContoPne(), not(equalTo(new BigDecimal(0)))));
		} 
		return list;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<EstrattoContoClienti> getListEstrattoContoClienti(Integer[] codEdicola, List codClienti, Timestamp dataCompetenza, List tipiEstrattoConto, Integer tipoProdotti) {
		List<EstrattoContoClienti> list = null;
		if (tipoProdotti != null && tipoProdotti.equals(IGerivConstants.TIPO_PRODOTTO_EDITORIALE)) {
			list = buildDettagliEstrattoContoPubblicazioni(codEdicola, codClienti, dataCompetenza, tipiEstrattoConto);
		} else if (tipoProdotti != null && tipoProdotti.equals(IGerivConstants.TIPO_PRODOTTO_NON_EDITORIALE)) {
			list = buildDettagliEstrattoContoProdotti(codEdicola, codClienti, dataCompetenza, tipiEstrattoConto);
		} else {
			list = buildDettagliEstrattoContoPubblicazioni(codEdicola, codClienti, dataCompetenza, tipiEstrattoConto);
			List<EstrattoContoClienti> dettagliEstrattoContoProdotti = buildDettagliEstrattoContoProdotti(codEdicola, codClienti, dataCompetenza, tipiEstrattoConto);
			list.addAll(dettagliEstrattoContoProdotti);
			if (list != null && !list.isEmpty()) {
				list = sort(list, on(EstrattoContoClienti.class).getOrder());
			}
		}
		return list;
	}
	
	@Override
	public void updateDataEstrattoConto(final Timestamp dataCompetenza, final Integer tipoProdotti, final Map<Long, Set<Timestamp>> mapClienteDateCompetezaEc) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				Query query = arg0.createQuery("update VenditaVo vo set vo.dataEstrattoConto = :dataCompetenza, vo.tipoProdottiInEstrattoConto = :tipoProdottiInEstrattoConto where vo.codCliente = :codCliente and vo.dataCompetenzaEstrattoContoClienti in (:listDateCompetenza) and vo.pagato = false and vo.idFattura is null and vo.deleted = false");
				Integer updated = 0;
				for (Map.Entry<Long, Set<Timestamp>> entry : mapClienteDateCompetezaEc.entrySet()) {
					query.setTimestamp("dataCompetenza", dataCompetenza);
					query.setInteger("tipoProdottiInEstrattoConto", tipoProdotti);
					query.setLong("codCliente", entry.getKey());
					query.setParameterList("listDateCompetenza", entry.getValue());	
					updated += query.executeUpdate();
				}	
				return updated;
			}
		};
		getDao().executeByHibernateCallback(action);
	}
	
	@Override
	public void updateFatturaEmessa(final Map<Long, Set<Timestamp>> mapClienteDateCompetezaEc, final Map<Long, Integer> mapClienteNumeroFattura) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				Query query = arg0.createQuery("update VenditaVo vo set vo.fatturaEmessa = 1, vo.idFattura = :numeroFattura, vo.fatturaContoUnico = 0 where vo.codCliente = :codCliente and vo.dataCompetenzaEstrattoContoClienti in (:listDateCompetenza) and vo.fatturaEmessa = 0");
				Integer updated = 0;
				for (Map.Entry<Long, Set<Timestamp>> entry : mapClienteDateCompetezaEc.entrySet()) {
					Long codCliente = entry.getKey();
					query.setLong("codCliente", codCliente);
					query.setInteger("numeroFattura", mapClienteNumeroFattura.get(codCliente));
					query.setParameterList("listDateCompetenza", entry.getValue());
					updated += query.executeUpdate();
				}	
				return updated;
			}
		};
		getDao().executeByHibernateCallback(action);
	}
	
	@Override
	public List<DateEstrattoContoClienteDto> getDateReportEstrattiContoCliente(Long codCliente) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, - Integer.parseInt(daysEstrattoContoClienti));
		Timestamp dateFrom = new Timestamp(cal.getTimeInMillis());
		DetachedCriteria criteria = DetachedCriteria.forClass(PagamentoClientiEdicolaVo.class, "pce");
		criteria.add(Restrictions.eq("pce.pk.codCliente", codCliente));
		criteria.add(Restrictions.ge("pce.pk.dataCompetenzaEstrattoConto", dateFrom));
		criteria.add(Restrictions.eq("pce.pk.tipoDocumento", IGerivConstants.ESTRATTO_CONTO));
		criteria.addOrder(Order.desc("pce.pk.dataCompetenzaEstrattoConto"));
		ProjectionList props = Projections.projectionList();
		props.add(Projections.property("pce.pk.dataCompetenzaEstrattoConto"), "dataCompetenzaEstrattoContoClienti");
		props.add(Projections.property("pce.dataDocumento"), "dataEstrattoConto");
		props.add(Projections.property("pce.tipoProdottiInEstrattoConto"), "tipoProdottiInEstrattoConto");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(DateEstrattoContoClienteDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<EstrattoContoClienti> getEstrattoContoClienti(Integer[] codEdicola, Long codCliente, Timestamp dataA, Integer tipoProdottiInEstrattoConto, Integer numeroFattura) {
		List<EstrattoContoClienti> list = null;
		if (tipoProdottiInEstrattoConto != null && tipoProdottiInEstrattoConto.equals(IGerivConstants.TIPO_PRODOTTO_EDITORIALE)) {
			list = buildDettagliEstrattoContoChiusoPubblicazioni(codEdicola, codCliente, dataA, numeroFattura);
		} else if (tipoProdottiInEstrattoConto != null && tipoProdottiInEstrattoConto.equals(IGerivConstants.TIPO_PRODOTTO_NON_EDITORIALE)) {
			list = buildDettagliEstrattoContoChiusoProdotti(codEdicola, codCliente, dataA, numeroFattura);
		} else {
			list = buildDettagliEstrattoContoChiusoPubblicazioni(codEdicola, codCliente, dataA, numeroFattura);
			list.addAll(buildDettagliEstrattoContoChiusoProdotti(codEdicola, codCliente, dataA, numeroFattura));
			if (list != null && !list.isEmpty()) {
				list = sort(list, on(EstrattoContoClienti.class).getOrder());
			}
		}
		return list;
	}
	
	@Override
	public List<ClienteEdicolaDto> getClientiEdicolaConPagamentiPendenti(Integer[] arrCodEdicola, String nome, String cognome, String codiceFiscale, String piva, Boolean daPagare, Timestamp dataCompetenzaDa, Timestamp dataCompetenzaA) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PagamentoClientiEdicolaVo.class, "pce");
		criteria.createCriteria("pce.cliente", "ce");
		criteria.createCriteria("ce.tipoLocalita", "tl", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.localita", "lo", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.paese", "pa", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.provincia", "pr", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.tipoPagamento", "tp", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.in("ce.codEdicola", arrCodEdicola));
		if (!Strings.isNullOrEmpty(nome)) {
			criteria.add(Restrictions.ilike("ce.nome", nome, MatchMode.START));
		}
		if (!Strings.isNullOrEmpty(cognome)) {
			criteria.add(Restrictions.ilike("ce.cognome", cognome, MatchMode.START));		
		}
		if (!Strings.isNullOrEmpty(codiceFiscale)) {
			criteria.add(Restrictions.eq("ce.codiceFiscale", codiceFiscale));
		}
		if (!Strings.isNullOrEmpty(piva)) {
			criteria.add(Restrictions.eq("ce.piva", piva));
		}
		if (dataCompetenzaDa != null) {
			criteria.add(Restrictions.ge("pce.pk.dataCompetenzaEstrattoConto", dataCompetenzaDa));
		}
		if (dataCompetenzaA != null) {
			criteria.add(Restrictions.le("pce.pk.dataCompetenzaEstrattoConto", dataCompetenzaA));
		}
		criteria.add(Restrictions.eq("pce.pagato", !daPagare));
		criteria.addOrder(Order.asc("ce.nome")).addOrder(Order.desc("pce.pk.dataCompetenzaEstrattoConto"));
		ProjectionList props = Projections.projectionList();
		props.add(Projections.property("ce.codCliente"), "codCliente");
		props.add(Projections.property("ce.codCliente"), "codVendita");
		props.add(Projections.property("pce.pk.dataCompetenzaEstrattoConto"), "dataCompetenzaEstrattoConto");
		props.add(Projections.property("ce.nome"), "nome");
		props.add(Projections.property("ce.cognome"), "cognome");
		props.add(Projections.property("lo.descrizione"),"localitaDesc");
		props.add(Projections.property("ce.cap"), "cap");
		props.add(Projections.property("pr.descrizione"), "provinciaDesc");
		props.add(Projections.property("ce.email"), "email");
		props.add(Projections.property("ce.telefono"), "telefono");
		props.add(Projections.property("pce.importoTotale"), "importoTotale");
		props.add(Projections.property("tp.descrizione"), "metodoPagamento");
		props.add(Projections.property("pce.pk.tipoDocumento"), "tipoDocumento");
		props.add(Projections.property("pce.dataDocumento"), "dataDocumento");
		props.add(Projections.property("pce.numeroDocumento"), "numeroDocumento");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(ClienteEdicolaDto.class));
		List<ClienteEdicolaDto> findObjectByDetachedCriteria = getDao().findObjectByDetachedCriteria(criteria);
		return findObjectByDetachedCriteria;
	}
	
	@Override
	public List<MetodoPagamentoClienteVo> getMetodiPagamentoCliente() {
		DetachedCriteria criteria = DetachedCriteria.forClass(MetodoPagamentoClienteVo.class, "mp");
		criteria.addOrder(Order.asc("mp.codMetodoPagamento"));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public void insertPagamentoClienti(Timestamp dataCompetenza, Timestamp dataDocumento, Map<Long, BigDecimal> mapClienteImporto, Integer tipoProdotti, Map<Long, byte[]> mapClienteEstrattContoXml, Map<Long, Integer> mapClienteNumeroEC, Integer tipoDocumento) {
		for (Map.Entry<Long, BigDecimal> entry : mapClienteImporto.entrySet()) {
			PagamentoClientiEdicolaPk pk = new PagamentoClientiEdicolaPk();
			pk.setCodCliente(entry.getKey());
			pk.setDataCompetenzaEstrattoConto(dataCompetenza);
			pk.setTipoDocumento(tipoDocumento);
			PagamentoClientiEdicolaVo vo = new PagamentoClientiEdicolaVo();
			vo.setPk(pk);
			vo.setPagato(false);
			vo.setImportoTotale(entry.getValue());
			vo.setDataDocumento(dataDocumento);
			vo.setTipoProdottiInEstrattoConto(tipoProdotti);
			if (mapClienteEstrattContoXml != null) {
				vo.setEstrattoContoXml(mapClienteEstrattContoXml.get(entry.getKey()));
			}
			if (mapClienteNumeroEC != null) {
				vo.setNumeroDocumento(mapClienteNumeroEC.get(entry.getKey()));
			}
			getDao().saveOrUpdate(vo);
		}
	}
	
	@Override
	public void updatePagamentoClienti(List<Long> listCodCliente, List<Timestamp> listDataCompetenzaEstrattoConto, Boolean pagato) {
		for (int i = 0; i < listCodCliente.size(); i++) {
			getDao().bulkUpdate("update PagamentoClientiEdicolaVo vo set vo.pagato = ? where vo.pk.codCliente = ? and vo.pk.dataCompetenzaEstrattoConto = ?", new Object[]{pagato, listCodCliente.get(i), listDataCompetenzaEstrattoConto.get(i)});
			getDao().bulkUpdate("update VenditaVo vo set vo.pagato = ? where vo.codCliente = ? and vo.dataCompetenzaEstrattoContoClienti = ?", new Object[]{pagato, listCodCliente.get(i), listDataCompetenzaEstrattoConto.get(i)});
		}
	}
	
	/**
	 * Ritorna una lista di dettagli di un singolo estratto conto di prodotti editoriali
	 * 
	 * @param codEdicola
	 * @param codCliente
	 * @param dataA
	 * @param numeroFattura 
	 * @return List<EstrattoContoClientiPubblicazioniDto>
	 */
	private <T extends EstrattoContoClienti> List<T> buildDettagliEstrattoContoChiusoPubblicazioni(Integer[] codEdicola, Long codCliente, Timestamp dataA, Integer numeroFattura) {
		DetachedCriteria criteria = DetachedCriteria.forClass(VenditaDettaglioVo.class, "ved");
		criteria.createCriteria("ved.venditaVo", "ve");
		criteria.createCriteria("ve.cliente", "ce");
		criteria.createCriteria("ce.banca", "ba", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.tipoPagamento", "tp", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.tipoLocalita", "tl", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.localita", "lo", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.paese", "pa", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.provincia", "pr", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.or(Restrictions.eq("ved.deleted", false), Restrictions.isNull("ved.deleted")));
		criteria.add(Restrictions.or(Restrictions.eq("ve.deleted", false), Restrictions.isNull("ve.deleted")));
		if (dataA != null) {
			criteria.add(Restrictions.eq("ve.dataEstrattoConto", dataA));
		}
		if (numeroFattura != null) {
			criteria.add(Restrictions.eq("ve.idFattura", numeroFattura));
		}
		criteria.add(Restrictions.in("ce.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("ce.codCliente", codCliente));
		criteria.addOrder(Order.asc("ce.nome")).addOrder(Order.asc("ved.titolo"));
		ProjectionList props = Projections.projectionList();
		props.add(Projections.groupProperty("ce.codCliente"), "codCliente");
		props.add(Projections.groupProperty("ce.nome"), "nome");
		props.add(Projections.groupProperty("ce.cognome"), "cognome");
		props.add(Projections.groupProperty("ved.titolo"), "titolo");
		props.add(Projections.groupProperty("ved.prezzoCopertina"), "prezzo");
		props.add(Projections.sum("ved.quantita"), "copie");
		props.add(Projections.groupProperty("tl.descrizione"), "tipoLocalita");
		props.add(Projections.groupProperty("ce.indirizzo"),"indirizzo");
		props.add(Projections.groupProperty("ce.numeroCivico"),"numeroCivico");
		props.add(Projections.groupProperty("ce.estensione"),"estensione");
		props.add(Projections.groupProperty("lo.descrizione"),"localitaDesc");
		props.add(Projections.groupProperty("ce.cap"), "cap");
		props.add(Projections.groupProperty("pr.descrizione"), "provinciaDesc");
		props.add(Projections.groupProperty("ce.piva"), "piva");
		props.add(Projections.groupProperty("ce.codiceFiscale"), "codiceFiscale");
		props.add(Projections.groupProperty("ba.nome"), "banca");
		props.add(Projections.groupProperty("ce.contoCorrente"), "contoCorrente");
		props.add(Projections.groupProperty("ce.iban"), "iban");
		props.add(Projections.groupProperty("tp.descrizione"), "tipoPagamento");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(EstrattoContoClientiPubblicazioniDto.class));
		List<T> findObjectByDetachedCriteria = getDao().findObjectByDetachedCriteria(criteria);
		return findObjectByDetachedCriteria;
	}

	/**
	 * Ritorna una lista di dettagli degli estratti conto di prodotti non editoriali per una lista di clienti 
	 * 
	 * @param codEdicola
	 * @param codClienti
	 * @param dataCompetenza
	 * @param tipiEstrattoConto
	 * @return List<EstrattoContoClientiProdottiDto>
	 */
	private <T extends EstrattoContoClienti> List<T> buildDettagliEstrattoContoProdotti(Integer[] codEdicola, List<Long> codClienti, Timestamp dataCompetenza, List<Integer> tipiEstrattoConto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiBollaDettaglioVo.class, "ved");
		criteria.createCriteria("ved.bolla", "bo");
		criteria.createCriteria("ved.prodotto", "prod");
		criteria.createCriteria("prod.causaleIva", "cau");
		criteria.createCriteria("bo.vendita", "ve");
		criteria.createCriteria("ve.cliente", "ce");
		criteria.createCriteria("ce.tipoLocalita", "tl", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.localita", "lo", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.paese", "pa", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.provincia", "pr", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.tipoPagamento", "tp", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.banca", "ba", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.or(Restrictions.eq("bo.deleted", false), Restrictions.isNull("bo.deleted")));
		criteria.add(Restrictions.or(Restrictions.eq("ved.deleted", false), Restrictions.isNull("ved.deleted")));
		criteria.add(Restrictions.le("ve.dataCompetenzaEstrattoContoClienti", dataCompetenza));
		criteria.add(Restrictions.in("ce.codEdicola", codEdicola));
		criteria.add(Restrictions.in("ce.tipoEstrattoConto", tipiEstrattoConto));
		criteria.add(Restrictions.in("ce.codCliente", codClienti));
		criteria.add(Restrictions.isNull("ve.dataEstrattoConto"));
		criteria.add(Restrictions.eq("ve.pagato", new Boolean(false)));
		criteria.add(Restrictions.or(Restrictions.eq("ve.fatturaEmessa", false), Restrictions.isNull("ve.fatturaEmessa")));
		ProjectionList props = Projections.projectionList();
		props.add(Projections.groupProperty("ce.codCliente"), "codCliente");
		props.add(Projections.groupProperty("ce.nome"), "nome");
		props.add(Projections.groupProperty("ce.cognome"), "cognome");
		props.add(Projections.groupProperty("prod.descrizioneProdottoA"), "titolo");
		props.add(Projections.groupProperty("prod.aliquota"), "aliquota");
		props.add(Projections.groupProperty("ved.prezzo"), "prezzoF");
		props.add(Projections.sum("ved.quantita"), "copie");
		props.add(Projections.groupProperty("tl.descrizione"), "tipoLocalita");
		props.add(Projections.groupProperty("ce.indirizzo"),"indirizzo");
		props.add(Projections.groupProperty("ce.numeroCivico"),"numeroCivico");
		props.add(Projections.groupProperty("ce.estensione"),"estensione");
		props.add(Projections.groupProperty("ce.estensione"),"estensione");
		props.add(Projections.groupProperty("lo.descrizione"),"localitaDesc");
		props.add(Projections.groupProperty("ce.cap"), "cap");
		props.add(Projections.groupProperty("pr.descrizione"), "provinciaDesc");
		props.add(Projections.groupProperty("ce.piva"), "piva");
		props.add(Projections.groupProperty("ce.codiceFiscale"), "codiceFiscale");
		props.add(Projections.groupProperty("ce.giorniScadenzaPagamento"), "giorniScadenzaPagamento");
		props.add(Projections.groupProperty("ve.dataCompetenzaEstrattoContoClienti"), "dataCompetenzaEstrattoContoClienti");
		props.add(Projections.groupProperty("tp.descrizione"), "tipoPagamento");
		props.add(Projections.groupProperty("ce.contoCorrente"), "contoCorrente");
		props.add(Projections.groupProperty("ce.iban"), "iban");
		props.add(Projections.groupProperty("ba.nome"), "banca");
		props.add(Projections.groupProperty("cau.descrizione"), "descrizioneCausaleIva");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(EstrattoContoClientiProdottiDto.class));
		List<T> findObjectByDetachedCriteria = getDao().findObjectByDetachedCriteria(criteria);
		return findObjectByDetachedCriteria;
	}

	/**
	 * Ritorna una lista di dettagli degli estratti conto di prodotti editoriali per una lista di clienti 
	 * 
	 * @param codEdicola
	 * @param codClienti
	 * @param dataCompetenza
	 * @param tipiEstrattoConto
	 * @return List<EstrattoContoClientiPubblicazioniDto>
	 */
	private <T extends EstrattoContoClienti> List<T> buildDettagliEstrattoContoPubblicazioni(Integer[] codEdicola, List<Long> codClienti, Timestamp dataCompetenza, List<Integer> tipiEstrattoConto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(VenditaDettaglioVo.class, "ved");
		criteria.createCriteria("ved.venditaVo", "ve");
		criteria.createCriteria("ve.cliente", "ce");
		criteria.createCriteria("ce.tipoLocalita", "tl", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.localita", "lo", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.paese", "pa", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.provincia", "pr", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.tipoPagamento", "tp", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.banca", "ba", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.or(Restrictions.eq("ved.deleted", false), Restrictions.isNull("ved.deleted")));
		criteria.add(Restrictions.or(Restrictions.eq("ve.deleted", false), Restrictions.isNull("ve.deleted")));
		criteria.add(Restrictions.le("ve.dataCompetenzaEstrattoContoClienti", dataCompetenza));
		criteria.add(Restrictions.in("ce.codEdicola", codEdicola));
		criteria.add(Restrictions.in("ce.tipoEstrattoConto", tipiEstrattoConto));
		criteria.add(Restrictions.in("ce.codCliente", codClienti));
		criteria.add(Restrictions.isNull("ve.dataEstrattoConto"));
		criteria.add(Restrictions.eq("ve.pagato", new Boolean(false)));
		criteria.add(Restrictions.or(Restrictions.eq("ve.fatturaEmessa", false), Restrictions.isNull("ve.fatturaEmessa")));
		ProjectionList props = Projections.projectionList();
		props.add(Projections.groupProperty("ce.codCliente"), "codCliente");
		props.add(Projections.groupProperty("ce.nome"), "nome");
		props.add(Projections.groupProperty("ce.cognome"), "cognome");
		props.add(Projections.groupProperty("ved.titolo"), "titolo");
		props.add(Projections.groupProperty("ved.prezzoCopertina"), "prezzo");
		props.add(Projections.sum("ved.quantita"), "copie");
		props.add(Projections.groupProperty("tl.descrizione"), "tipoLocalita");
		props.add(Projections.groupProperty("ce.indirizzo"),"indirizzo");
		props.add(Projections.groupProperty("ce.numeroCivico"),"numeroCivico");
		props.add(Projections.groupProperty("ce.estensione"),"estensione");
		props.add(Projections.groupProperty("lo.descrizione"),"localitaDesc");
		props.add(Projections.groupProperty("ce.cap"), "cap");
		props.add(Projections.groupProperty("pr.descrizione"), "provinciaDesc");
		props.add(Projections.groupProperty("ce.piva"), "piva");
		props.add(Projections.groupProperty("ce.codiceFiscale"), "codiceFiscale");
		props.add(Projections.groupProperty("ce.giorniScadenzaPagamento"), "giorniScadenzaPagamento");
		props.add(Projections.groupProperty("ve.dataCompetenzaEstrattoContoClienti"), "dataCompetenzaEstrattoContoClienti");
		props.add(Projections.groupProperty("tp.descrizione"), "tipoPagamento");
		props.add(Projections.groupProperty("ce.contoCorrente"), "contoCorrente");
		props.add(Projections.groupProperty("ce.iban"), "iban");
		props.add(Projections.groupProperty("ba.nome"), "banca");
		// TODO RAGGRUPPAMENTO DEI QUOTIDIANI - RISOLVERE SUL GESTIONALE
		//props.add(Projections.groupProperty("ap.codInizioQuotidiano"), "codInizioQuotidiano");
		//props.add(Projections.groupProperty("ap.codFineQuotidiano"), "codFineQuotidiano");
		//props.add(Projections.groupProperty("ap.titoloInforete"), "titoloInforete");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(EstrattoContoClientiPubblicazioniDto.class));
		List<T> findObjectByDetachedCriteria = getDao().findObjectByDetachedCriteria(criteria);
		return findObjectByDetachedCriteria;
	}
	
	/**
	 * Ritorna una lista di dettagli di un singolo estratto conto di prodotti non editoriali
	 * 
	 * @param codEdicola
	 * @param codCliente
	 * @param dataA
	 * @param numeroFattura 
	 * @return List<EstrattoContoClientiProdottiDto>
	 */
	private <T extends EstrattoContoClienti> List<T> buildDettagliEstrattoContoChiusoProdotti(Integer[] codEdicola, Long codCliente, Timestamp dataA, Integer numeroFattura) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiBollaDettaglioVo.class, "ved");
		criteria.createCriteria("ved.bolla", "bo");
		criteria.createCriteria("ved.prodotto", "prod");
		criteria.createCriteria("bo.vendita", "ve");
		criteria.createCriteria("ve.cliente", "ce");
		criteria.createCriteria("ce.banca", "ba", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.tipoPagamento", "tp", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.tipoLocalita", "tl", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.localita", "lo", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.paese", "pa", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("ce.provincia", "pr", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.or(Restrictions.eq("bo.deleted", false), Restrictions.isNull("bo.deleted")));
		criteria.add(Restrictions.or(Restrictions.eq("ved.deleted", false), Restrictions.isNull("ved.deleted")));
		if (dataA != null) {
			criteria.add(Restrictions.eq("ve.dataEstrattoConto", dataA));
		}
		if (numeroFattura != null) {
			criteria.add(Restrictions.eq("ve.idFattura", numeroFattura));
		}
		criteria.add(Restrictions.in("ce.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("ce.codCliente", codCliente));
		criteria.addOrder(Order.asc("ce.nome")).addOrder(Order.asc("prod.descrizioneProdottoA"));
		ProjectionList props = Projections.projectionList();
		props.add(Projections.groupProperty("ce.codCliente"), "codCliente");
		props.add(Projections.groupProperty("ce.nome"), "nome");
		props.add(Projections.groupProperty("ce.cognome"), "cognome");
		props.add(Projections.groupProperty("prod.descrizioneProdottoA"), "titolo");
		props.add(Projections.groupProperty("ved.prezzo"), "prezzoF");
		props.add(Projections.sum("ved.quantita"), "copie");
		props.add(Projections.groupProperty("tl.descrizione"), "tipoLocalita");
		props.add(Projections.groupProperty("ce.indirizzo"),"indirizzo");
		props.add(Projections.groupProperty("ce.numeroCivico"),"numeroCivico");
		props.add(Projections.groupProperty("ce.estensione"),"estensione");
		props.add(Projections.groupProperty("lo.descrizione"),"localitaDesc");
		props.add(Projections.groupProperty("ce.cap"), "cap");
		props.add(Projections.groupProperty("pr.descrizione"), "provinciaDesc");
		props.add(Projections.groupProperty("ce.piva"), "piva");
		props.add(Projections.groupProperty("ce.codiceFiscale"), "codiceFiscale");
		props.add(Projections.groupProperty("ba.nome"), "banca");
		props.add(Projections.groupProperty("ce.contoCorrente"), "contoCorrente");
		props.add(Projections.groupProperty("ce.iban"), "iban");
		props.add(Projections.groupProperty("tp.descrizione"), "tipoPagamento");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(EstrattoContoClientiProdottiDto.class));
		List<T> findObjectByDetachedCriteria = getDao().findObjectByDetachedCriteria(criteria);
		return findObjectByDetachedCriteria;
	}
	
	@Override
	public List<FileFatturaDto> getFattureClienti(Integer codEdicola, Timestamp dataDa, Timestamp dataA, String nome, String cognome, String codiceFiscale, String piva) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FatturaClienteEdicolaVo.class, "fc");
		criteria.createCriteria("fc.cliente", "cli");
		criteria.add(Restrictions.eq("fc.codEdicola", codEdicola));
		if (dataDa != null) {
			criteria.add(Restrictions.ge("fc.dataFattura", dataDa));
		}
		if (dataA != null) {
			criteria.add(Restrictions.le("fc.dataFattura", dataA));
		}
		if (!Strings.isNullOrEmpty(nome)) {
			criteria.add(Restrictions.ilike("cli.nome", nome, MatchMode.START));
		}
		if (!Strings.isNullOrEmpty(cognome)) {
			criteria.add(Restrictions.ilike("cli.cognome", cognome, MatchMode.START));		
		}
		if (!Strings.isNullOrEmpty(codiceFiscale)) {
			criteria.add(Restrictions.eq("cli.codiceFiscale", codiceFiscale));
		}
		if (!Strings.isNullOrEmpty(piva)) {
			criteria.add(Restrictions.eq("cli.piva", piva));
		}
		criteria.addOrder(Order.desc("fc.dataFattura"));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("fc.nomeFileFattura"), "fileName");
		props.add(Projections.property("fc.dataFattura"), "data");
		props.add(Projections.property("fc.numeroFattura"), "numero");               
		props.add(Projections.property("fc.codCliente"), "codCliente");
		props.add(Projections.property("cli.nome"), "nome");   
		props.add(Projections.property("cli.cognome"), "cognome");   
		props.add(Projections.property("fc.tipoDocumento"), "tipoDocumento");     
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(FileFatturaDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	public void deletePagamentiCliente(final Long codCliente) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				Query createQuery = session.createQuery(IGerivQueryContants.HQL_DELETE_PAGAMENTI_CLIENTE);
				createQuery.setLong("codCliente", codCliente);
				return createQuery.executeUpdate();
			}
		};
		getDao().executeByHibernateCallback(action);	
	};
	
	@Override
	public void deleteCliente(Long codCliente) {
		getDao().bulkUpdate("delete from ClienteEdicolaVo vo where vo.codCliente = ?", new Object[]{codCliente});
	}
	
	@Override
	public List<AnagraficaBancaVo> getBanche() {
		DetachedCriteria criteria = DetachedCriteria.forClass(AnagraficaBancaVo.class, "ab");
		criteria.addOrder(Order.asc("ab.nome"));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public Timestamp getUltimaDataCompetenza(final Integer[] codEdicola, final List<Long> codClienti, final Timestamp dataCompetenza, final List<Integer> tipiEstrattoConto) {
		HibernateCallback<Timestamp> action = new HibernateCallback<Timestamp>() {
			@Override
			public Timestamp doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(VenditaVo.class, "ve");
				criteria.createCriteria("ve.cliente", "ce");
				criteria.add(Restrictions.lt("ve.dataEstrattoConto", dataCompetenza));
				criteria.add(Restrictions.in("ce.codEdicola", codEdicola));
				criteria.add(Restrictions.in("ce.tipoEstrattoConto", tipiEstrattoConto));
				criteria.add(Restrictions.in("ce.codCliente", codClienti));
				criteria.addOrder(Order.desc("ve.dataEstrattoConto"));
				ProjectionList props = Projections.projectionList();
				props.add(Projections.property("ve.dataEstrattoConto"));
				criteria.setProjection(props);
				criteria.setMaxResults(1);
				Object uniqueResult = criteria.uniqueResult();
				return uniqueResult != null ? (Timestamp) uniqueResult : null;
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}

	@Override
	public List<PubblicazioneDto> getRitiriCliente(Integer[] arrId, String titolo, Timestamp dataDa, Timestamp dataA, Long codCliente) {
		List<PubblicazioneDto> list = getRitiriPubblicazioni(arrId, dataDa, dataA, codCliente, titolo, false);
		list.addAll(getRitiriProdotti(arrId, dataDa, dataA, codCliente, titolo, false));
		Collections.sort(list, new PubblicazioniRitiriComparator());
		return list;
	}
	
	/**
	 * Ritorna i ritiri di prodotti vari
	 * @param ritiriCancellati 
	 *  
	 * @param Integer[] arrId
	 * @param Timestamp dataDa
	 * @param Timestamp dataA
	 * @param Long codCliente
	 * @param String titolo
	 * @return List<PubblicazioneDto>
	 */
	private List<PubblicazioneDto> getRitiriProdotti(Integer[] arrId, Timestamp dataDa, Timestamp dataA, Long codCliente, String titolo, boolean ritiriCancellati) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiBollaDettaglioVo.class, "vdet");
		criteria.createCriteria("vdet.bolla", "bol");
		criteria.createCriteria("bol.vendita", "ven");
		criteria.createCriteria("vdet.prodotto", "prod");
		if (ritiriCancellati) {
			criteria.add(Restrictions.or(Restrictions.eq("vdet.deleted", true), Restrictions.eq("ven.deleted", true)));
		} else {
			criteria.add(Restrictions.or(Restrictions.eq("vdet.deleted", false), Restrictions.isNull("vdet.deleted")));
			criteria.add(Restrictions.or(Restrictions.eq("ven.deleted", false), Restrictions.isNull("ven.deleted")));
		}
		criteria.add(Restrictions.in("ven.codEdicola", arrId));
		criteria.add(Restrictions.eq("ven.codCliente", codCliente));
		if (dataDa != null && dataA != null) {
			criteria.add(Restrictions.between("ven.dataVendita", dataDa, dataA));
		}
		if (!Strings.isNullOrEmpty(titolo)) {
			criteria.add(Restrictions.ilike("prod.descrizioneProdottoA", titolo, MatchMode.ANYWHERE));
		}
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("vdet.quantita"), "quantita");
		properties.add(Projections.property("vdet.pk"), "pk");
		properties.add(Projections.property("prod.descrizioneProdottoA"), "titolo");
		properties.add(Projections.property("prod.descrizioneProdottoB"), "sottoTitolo"); 
		properties.add(Projections.property("prod.prezzo"), "prezzo");
		properties.add(Projections.property("prod.nomeImmagine"), "immagine");
		properties.add(Projections.property("ven.pagato"), "pagato");
		properties.add(Projections.property("ven.dataVendita"), "dataVendita");
		properties.add(Projections.property("ven.dataEstrattoConto"), "dataEstrattoConto");
		properties.add(Projections.property("ven.dataFattura"), "dataFattura");
		properties.add(Projections.property("ven.provenienzaConto"), "provenienzaConto");
		properties.add(Projections.property("prod.prodottoDl"), "prodottoDl");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(PubblicazioneDto.class));
		List<PubblicazioneDto> findObjectByDetachedCriteria = getDao().findObjectByDetachedCriteria(criteria);
		if (findObjectByDetachedCriteria != null && !findObjectByDetachedCriteria.isEmpty()) {
			forEach(findObjectByDetachedCriteria).setProdottoNonEditoriale(true);
			List<PubblicazioneDto> listProdEdicola = select(findObjectByDetachedCriteria, having(on(PubblicazioneDto.class).getProdottoDl(), equalTo(false)));
			if (listProdEdicola != null && !listProdEdicola.isEmpty()) {
				forEach(listProdEdicola).setDirAlias(IGerivConstants.DIR_ALIAS_IMMAGINI_PROD_VARI_EDICOLA);
			}
			List<PubblicazioneDto> listProdDl = select(findObjectByDetachedCriteria, having(on(PubblicazioneDto.class).getProdottoDl(), equalTo(true)));
			if (listProdDl != null && !listProdDl.isEmpty()) {
				forEach(listProdDl).setDirAlias(IGerivConstants.DIR_ALIAS_IMMAGINI_PROD_VARI_DL);
			}
		}
		return findObjectByDetachedCriteria;
	}

	/**
	 * Ritorna i ritiri di pubblicazioni
	 * @param ritiriCancellati 
	 * 
	 * @param Integer[] arrId
	 * @param Timestamp dataDa
	 * @param Timestamp dataA
	 * @param Long codCliente
	 * @param String titolo
	 * @return List<PubblicazioneDto>
	 */
	private List<PubblicazioneDto> getRitiriPubblicazioni(Integer[] arrId, Timestamp dataDa, Timestamp dataA, Long codCliente, String titolo, boolean ritiriCancellati) {
		DetachedCriteria criteria = DetachedCriteria.forClass(VenditaDettaglioVo.class, "vdet");
		criteria.createCriteria("vdet.venditaVo", "ven");
		criteria.createCriteria("vdet.storicoCopertineVo", "sc", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("sc.immagine", "im", JoinType.LEFT_OUTER_JOIN);
		if (ritiriCancellati) {
			criteria.add(Restrictions.or(Restrictions.eq("vdet.deleted", true), Restrictions.eq("ven.deleted", true)));
		} else {
			criteria.add(Restrictions.or(Restrictions.eq("vdet.deleted", false), Restrictions.isNull("vdet.deleted")));
			criteria.add(Restrictions.or(Restrictions.eq("ven.deleted", false), Restrictions.isNull("ven.deleted")));
		}
		criteria.add(Restrictions.in("ven.codEdicola", arrId));
		criteria.add(Restrictions.eq("ven.codCliente", codCliente));
		if (dataDa != null && dataA != null) {
			criteria.add(Restrictions.between("ven.dataVendita", dataDa, dataA));
		}
		if (!Strings.isNullOrEmpty(titolo)) {
			criteria.add(Restrictions.ilike("vdet.titolo", titolo, MatchMode.ANYWHERE));
		}
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("vdet.quantita"), "quantita");
		properties.add(Projections.property("vdet.importoTotale"), "importoTotale");
		properties.add(Projections.property("vdet.pk"), "pk");
		properties.add(Projections.property("vdet.titolo"), "titolo");
		properties.add(Projections.property("sc.codicePubblicazione"), "codicePubblicazione");
		properties.add(Projections.property("sc.sottoTitolo"), "sottoTitolo"); 
		properties.add(Projections.property("sc.numeroCopertina"), "numeroCopertina"); 
		properties.add(Projections.property("vdet.prezzoCopertina"), "prezzoCopertinaVendita");
		properties.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertina");
		properties.add(Projections.property("sc.dataUscita"), "dataUscita");
		properties.add(Projections.property("sc.pk.idtn"), "idtn");
		properties.add(Projections.property("sc.pk.codDl"), "coddl");
		properties.add(Projections.property("im.nome"), "immagine");
		properties.add(Projections.property("ven.pagato"), "pagato");
		properties.add(Projections.property("ven.dataVendita"), "dataVendita");
		properties.add(Projections.property("ven.dataEstrattoConto"), "dataEstrattoConto");
		properties.add(Projections.property("ven.dataFattura"), "dataFattura");
		properties.add(Projections.property("ven.provenienzaConto"), "provenienzaConto");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(PubblicazioneDto.class));
		List<PubblicazioneDto> findObjectByDetachedCriteria = getDao().findObjectByDetachedCriteria(criteria);
		if (findObjectByDetachedCriteria != null && !findObjectByDetachedCriteria.isEmpty()) {
			forEach(findObjectByDetachedCriteria).setDirAlias(IGerivConstants.DIR_ALIAS_IMMAGINI);
		}
		return findObjectByDetachedCriteria;
	}
	
	@Override
	public List<PubblicazioneDto> getRitiriClienteCancellati(Integer[] arrId, Long codCliente) {
		List<PubblicazioneDto> list = getRitiriPubblicazioni(arrId, null, null, codCliente, null, true);
		list.addAll(getRitiriProdotti(arrId, null, null, codCliente, null, true));
		Collections.sort(list, new PubblicazioniRitiriComparator());
		return list;
	}
	
	@Override
	public void deleteEstrattoContoCliente(Long codCliente, Timestamp dtCompetenza, Integer tipoDocumento) {
		getDao().bulkUpdate("delete from PagamentoClientiEdicolaVo vo where vo.pk.codCliente = ? and vo.pk.dataCompetenzaEstrattoConto = ? and vo.pk.tipoDocumento = ?", new Object[]{codCliente, dtCompetenza, tipoDocumento});
		getDao().bulkUpdate("update VenditaVo vo set vo.dataEstrattoConto = null, vo.tipoProdottiInEstrattoConto = null, vo.pagato = false where vo.codCliente = ? and vo.dataEstrattoConto = ?", new Object[]{codCliente, dtCompetenza});
	}
	

	@Override
	public List<PagamentoClientiEdicolaVo> getPagamentoClientiEdicola(List<Long> listCodClienti, Timestamp dtCompetenza, Integer tipoDocumento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PagamentoClientiEdicolaVo.class, "pa");
		criteria.createCriteria("pa.cliente", "cli");
		criteria.add(Restrictions.in("cli.codCliente", listCodClienti));
		criteria.add(Restrictions.eq("pa.pk.dataCompetenzaEstrattoConto", dtCompetenza));
		criteria.add(Restrictions.eq("pa.pk.tipoDocumento", tipoDocumento));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public FatturaClienteEdicolaVo getFatturaClienteEdicola(Long codCliente, Integer codEdicola, Integer numeroFattura) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FatturaClienteEdicolaVo.class, "fat");
		criteria.add(Restrictions.eq("fat.codCliente", codCliente));
		criteria.add(Restrictions.eq("fat.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("fat.numeroFattura", numeroFattura));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public synchronized Integer getLastFatturaUtenteEdicola(Integer codEdicolaMaster, String codUtente) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FatturaClienteEdicolaVo.class, "fat");
		criteria.add(Restrictions.eq("fat.codEdicola", codEdicolaMaster));
		criteria.add(Restrictions.eq("fat.codUtente", codUtente));
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.max("fat.numeroFattura"));
		criteria.setProjection(properties); 
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<EstrattoContoFatturaClientiDto> getListEstrattiContoFattureClienti(Long codCliente, Timestamp dataDa, Timestamp dataA, Integer tipoDocumento) {
		List<EstrattoContoFatturaClientiDto> estrattiContoFatture = null;
		if (tipoDocumento != null && tipoDocumento.equals(IGerivConstants.ESTRATTO_CONTO)) {
			estrattiContoFatture = getListEstrattiConto(codCliente, dataDa, dataA);
		} else if (tipoDocumento != null && tipoDocumento.equals(IGerivConstants.FATTURA)) {
			estrattiContoFatture = getListFatture(codCliente, dataDa, dataA);
		} else {
			estrattiContoFatture = getListEstrattiConto(codCliente, dataDa, dataA);
			estrattiContoFatture.addAll(getListFatture(codCliente, dataDa, dataA));
			sort(estrattiContoFatture, on(EstrattoContoFatturaClientiDto.class).getDataCompetenza());
		}
		return estrattiContoFatture;
	}
	
	private List<EstrattoContoFatturaClientiDto> getListEstrattiConto(Long codCliente, Timestamp dataDa, Timestamp dataA) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PagamentoClientiEdicolaVo.class, "pce");
		criteria.add(Restrictions.eq("pce.pk.codCliente", codCliente));
		criteria.add(Restrictions.eq("pce.pk.tipoDocumento", IGerivConstants.ESTRATTO_CONTO));
		if (dataDa != null) {
			criteria.add(Restrictions.ge("pce.dataDocumento", dataDa));
		}
		if (dataA != null) {
			criteria.add(Restrictions.le("pce.dataDocumento", dataA));
		}
		criteria.addOrder(Order.desc("pce.pk.dataCompetenzaEstrattoConto"));
		ProjectionList props = Projections.projectionList();
		props.add(Projections.property("pce.pk.dataCompetenzaEstrattoConto"), "dataCompetenza");
		props.add(Projections.property("pce.dataDocumento"), "dataDocumento");
		props.add(Projections.property("pce.pk.tipoDocumento"), "tipoDocumento");
		props.add(Projections.property("pce.numeroDocumento"), "numeroDocumento");
		props.add(Projections.property("pce.importoTotale"), "importoTotale");
		props.add(Projections.property("pce.pk.codCliente"), "codCliente");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(EstrattoContoFatturaClientiDto.class));
		List<EstrattoContoFatturaClientiDto> estrattiConto = getDao().findObjectByDetachedCriteria(criteria);
		if (estrattiConto != null && !estrattiConto.isEmpty()) {
			forEach(estrattiConto).setTipo(IGerivConstants.ESTRATTO_CONTO);
		}
		return estrattiConto;
	}
	
	private List<EstrattoContoFatturaClientiDto> getListFatture(Long codCliente, Timestamp dataDa, Timestamp dataA) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FatturaClienteEdicolaVo.class, "fc");
		criteria.createCriteria("fc.pagamentoClientiEdicolaVo", "pc", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("fc.codCliente", codCliente));
		if (dataDa != null) {
			criteria.add(Restrictions.ge("fc.dataFattura", dataDa));
		}
		if (dataA != null) {
			criteria.add(Restrictions.le("fc.dataFattura", dataA));
		}
		criteria.addOrder(Order.desc("fc.dataFattura"));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("fc.nomeFileFattura"), "fileName");
		props.add(Projections.property("fc.dataFattura"), "dataDocumento");
		props.add(Projections.property("fc.numeroFattura"), "numeroDocumento");               
		props.add(Projections.property("fc.codCliente"), "codCliente");
		props.add(Projections.property("fc.tipoDocumento"), "tipoDocumento");
		props.add(Projections.property("pc.pk.dataCompetenzaEstrattoConto"), "dataCompetenza");
		props.add(Projections.property("pc.importoTotale"), "importoTotale");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(EstrattoContoFatturaClientiDto.class));
		List<EstrattoContoFatturaClientiDto> estrattiConto = getDao().findObjectByDetachedCriteria(criteria);
		if (estrattiConto != null && !estrattiConto.isEmpty()) {
			forEach(estrattiConto).setTipo(IGerivConstants.FATTURA);
		}
		return estrattiConto;
	}
	
	@Override
	public void chiudiMovimentiEstrattoContoClientiVo(Long codCliente, Timestamp dataUpdate, Timestamp dataA) {
		getDao().bulkUpdate("update MovimentiEstrattoContoClientiVo vo set vo.dataEstrattoConto = ? where vo.cliente.codCliente = ? and vo.dataEstrattoConto is null and vo.dataCompetenza <= ?", new Object[]{dataUpdate, codCliente, dataA});
	}
	
	@Override
	public void chiudiMovimentiVenditeEstrattoContoClientiVo(Integer codEdicola, Long codCliente, Timestamp dataUpdate, Timestamp dataA) {
		getDao().bulkUpdate("update VenditaVo vo set vo.dataEstrattoConto = ?, vo.pagato = ? where vo.codEdicola = ? and vo.codCliente = ? and vo.dataEstrattoConto is null and vo.dataVendita <= ?", new Object[]{dataUpdate, true, codEdicola, codCliente, dataA});
	}
	
	@Override
	public void deleteRichiesteCliente(final Integer[] codEdicola, final Integer[] codFiegDl, final Long codCliente) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				Query createQuery = session.createQuery(IGerivQueryContants.HQL_DELETE_RICHIESTE_CLIENTE);
				createQuery.setParameterList("codEdicola", codEdicola);
				createQuery.setParameterList("codDl", codFiegDl);
				createQuery.setLong("codCliente", codCliente);
				createQuery.executeUpdate();
				
				createQuery = session.createQuery(IGerivQueryContants.HQL_DELETE_RICHIESTE_FISSE_CLIENTE);
				createQuery.setParameterList("codEdicola", codEdicola);
				createQuery.setParameterList("codDl", codFiegDl);
				createQuery.setLong("codCliente", codCliente);
				return createQuery.executeUpdate();
			}
		};
		getDao().executeByHibernateCallback(action);
	}
	

	@Override
	public void resetCodClienteVendite(Long codCliente) {
		getDao().bulkUpdate("update VenditaVo vo set vo.codCliente = null where codCliente = ?", new Object[]{codCliente});
	}
	
	@Override
	public void deleteMessaggiCliente(Long codCliente) {
		getDao().bulkUpdate("delete from MessaggioClienteVo vo where vo.codCliente = ?", new Object[]{codCliente});
	}
	
	@Override
	public List<EmailTemplateVo> getEmailTemplates(Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EmailTemplateVo.class, "et");
		criteria.add(Restrictions.eq("et.codEdicola", codEdicola));
		criteria.addOrder(Order.asc("et.nome"));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public EmailTemplateVo getEmailTemplate(Integer codTemplate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EmailTemplateVo.class, "et");
		criteria.add(Restrictions.eq("et.codice", codTemplate));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}

	

	@Override
	public List<ClienteEdicolaDto> getClientiEdicola(Integer[] codEdicola,
			String nome, String cognome, Long codCliente) {
			DetachedCriteria criteria = DetachedCriteria.forClass(ClienteEdicolaVo.class, "ce");
			criteria.createCriteria("tipoLocalita", "tl", JoinType.LEFT_OUTER_JOIN);
			criteria.createCriteria("localita", "lo", JoinType.LEFT_OUTER_JOIN);
			criteria.createCriteria("paese", "pa", JoinType.LEFT_OUTER_JOIN);
			criteria.createCriteria("provincia", "pr", JoinType.LEFT_OUTER_JOIN);
			
			if (codEdicola != null && codEdicola.length > 0) {
				criteria.add(Restrictions.in("codEdicola", codEdicola));
			}
			if (nome != null && !nome.equals("")) {
				criteria.add(Restrictions.ilike("nome", nome, MatchMode.ANYWHERE));
			}
			if (cognome != null && !cognome.equals("")) {
				criteria.add(Restrictions.ilike("cognome", cognome, MatchMode.ANYWHERE));
			}
			if (codCliente != null) {
				criteria.add(Restrictions.eq("codCliente", codCliente));
			}
			
			criteria.addOrder(Order.asc("ce.nome"));
			ProjectionList properties = Projections.projectionList(); 
			properties.add(Projections.property("ce.codCliente"), "codCliente");
			properties.add(Projections.property("ce.nome"), "nome"); 
			properties.add(Projections.property("ce.cognome"), "cognome"); 
			properties.add(Projections.property("lo.descrizione"), "localitaDesc"); 
			properties.add(Projections.property("pr.descrizione"), "provinciaDesc");
			properties.add(Projections.property("ce.cap"), "cap");
			properties.add(Projections.property("ce.telefono"), "telefono");
			properties.add(Projections.property("ce.email"), "email");
			properties.add(Projections.property("ce.tipoEstrattoConto"), "tipoEstrattoConto");
			criteria.setProjection(properties); 
			criteria.setResultTransformer(Transformers.aliasToBean(ClienteEdicolaDto.class));
			
			return getDao().findObjectByDetachedCriteria(criteria);
	}

	
	@Override
	public List<ClienteEdicolaDto> getClientiEdicolaScolastica(Integer[] codEdicola,
			String nome, String cognome, Long codCliente, String numOrdineTxt) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ClienteEdicolaVo.class, "ce");
		criteria.createCriteria("tipoLocalita", "tl", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("localita", "lo", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("paese", "pa", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("provincia", "pr", JoinType.LEFT_OUTER_JOIN);
		if(numOrdineTxt!=null && !numOrdineTxt.equals(""))
			criteria.createCriteria("ordini", "ordini", JoinType.LEFT_OUTER_JOIN);
		
		if (codEdicola != null && codEdicola.length > 0) {
			criteria.add(Restrictions.in("codEdicola", codEdicola));
		}
		if (nome != null && !nome.equals("")) {
			criteria.add(Restrictions.ilike("nome", nome, MatchMode.ANYWHERE));
		}
		if (cognome != null && !cognome.equals("")) {
			criteria.add(Restrictions.ilike("cognome", cognome, MatchMode.ANYWHERE));
		}
		if (codCliente != null) {
			criteria.add(Restrictions.eq("codCliente", codCliente));
		}
		if(numOrdineTxt!=null && !numOrdineTxt.equals(""))
			criteria.add(Restrictions.ilike("ordini.numeroOrdineTxt", numOrdineTxt));
		
		criteria.addOrder(Order.asc("ce.nome"));
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("ce.codCliente"), "codCliente");
		properties.add(Projections.property("ce.nome"), "nome"); 
		properties.add(Projections.property("ce.cognome"), "cognome"); 
		properties.add(Projections.property("lo.descrizione"), "localitaDesc"); 
		properties.add(Projections.property("pr.descrizione"), "provinciaDesc");
		properties.add(Projections.property("ce.cap"), "cap");
		properties.add(Projections.property("ce.telefono"), "telefono");
		properties.add(Projections.property("ce.email"), "email");
		properties.add(Projections.property("ce.tipoEstrattoConto"), "tipoEstrattoConto");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(ClienteEdicolaDto.class));
		
		return getDao().findObjectByDetachedCriteria(criteria);
	}

}
