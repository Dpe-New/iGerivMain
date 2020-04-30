package it.dpe.igeriv.bo.edicole;

import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.dto.EstrattoContoDto;
import it.dpe.igeriv.dto.ParametriEdicolaDto;
import it.dpe.igeriv.dto.VenditaRiepilogoDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.AnagraficaEdicolaAggiuntiviVo;
import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.igeriv.vo.BonusInvitaUnCollegaVo;
import it.dpe.igeriv.vo.EdicolaDlVo;
import it.dpe.igeriv.vo.EmailRivenditaVo;
import it.dpe.igeriv.vo.EstrattoContoEdicolaPdfVo;
import it.dpe.igeriv.vo.ImmagineAnagraficaEdicolaVo;
import it.dpe.igeriv.vo.ParametriEdicolaVo;
import it.dpe.igeriv.vo.ParametriVo;
import it.dpe.igeriv.vo.PianoProfiliEdicolaVo;
import it.dpe.igeriv.vo.UserVo;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import scala.collection.generic.BitOperations.Int;

import com.google.common.base.Strings;

@Repository
class EdicoleRepositoryImpl extends BaseRepositoryImpl implements EdicoleRepository {
	
	@Autowired
	EdicoleRepositoryImpl(BaseDao<UserVo> dao) {
		super(dao);
	}
	
	@Override
	public List<EdicolaDto> getEdicole(Integer codDl, Integer codEdicolaWeb, Integer codEdicolaDl, String ragioneSociale) {
		return getEdicole(codDl, codEdicolaWeb, codEdicolaDl, ragioneSociale, false);
	}
	
	@Override
	public List<EdicolaDto> getEdicole(Integer codDl, Integer idProfiloDL, Integer codEdicolaWeb, Integer codEdicolaDl, String ragioneSociale) {
		return getEdicole(codDl, idProfiloDL,codEdicolaWeb, codEdicolaDl, ragioneSociale, false);
	}
	
	
	private List<EdicolaDto> getEdicole(Integer codDl, Integer idProfiloDL, Integer codEdicolaWeb, Integer codEdicolaDl, String ragioneSociale, boolean soloUtentiAmministratori) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserVo.class, "uvo");
		criteria.createCriteria("uvo.abbinamentoEdicolaDlVo", "ab");
		criteria.createCriteria("uvo.dlGruppoModuliVo", "gm");
		criteria.createCriteria("gm.gruppoModuli", "gmo");
		criteria.createCriteria("ab.anagraficaEdicolaVo", "ae");
		criteria.createCriteria("ab.anagraficaAgenziaVo", "aa");
		if (codDl != null) {
			criteria.add(Restrictions.eq("aa.codFiegDl", codDl));
		}
		if (idProfiloDL != null) {
			criteria.add(Restrictions.eq("gmo.id", idProfiloDL));
		}
		if (codEdicolaWeb != null && !codEdicolaWeb.equals("")) {
			criteria.add(Restrictions.eq("ab.codDpeWebEdicola", codEdicolaWeb));
		} 
		if (codEdicolaDl != null && !codEdicolaDl.equals("")) {
			criteria.add(Restrictions.eq("ab.codEdicolaDl", codEdicolaDl));
		} 
		if (ragioneSociale != null && !ragioneSociale.equals("")) {
			criteria.add(Restrictions.ilike("ae.ragioneSocialeEdicolaPrimaRiga", ragioneSociale, MatchMode.ANYWHERE));
		}
		if (soloUtentiAmministratori) {
			criteria.add(Restrictions.eq("uvo.utenteAmministratore", 1));
		}
		criteria.addOrder(Order.asc("ae.ragioneSocialeEdicolaPrimaRiga"));
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("ab.codEdicolaDl"), "codEdicolaDl");
		properties.add(Projections.property("ab.codDpeWebEdicola"), "codEdicolaWeb");
		properties.add(Projections.property("aa.codFiegDl"), "codDl");
		properties.add(Projections.property("ae.ragioneSocialeEdicolaPrimaRiga"), "ragioneSociale1");
		properties.add(Projections.property("ae.ragioneSocialeEdicolaSecondaRiga"), "ragioneSociale2"); 
		properties.add(Projections.property("ae.indirizzoEdicolaPrimaRiga"), "indirizzo");
		properties.add(Projections.property("ae.localitaEdicolaPrimaRiga"), "localita");
		properties.add(Projections.property("ae.siglaProvincia"), "provincia");
		properties.add(Projections.property("ab.dtAttivazioneEdicola"), "dataInserimento");
		properties.add(Projections.property("ab.dtSospensioneEdicola"), "dataSospensione");
		properties.add(Projections.property("uvo.pwd"), "password");
		properties.add(Projections.property("ae.email"), "email");
		properties.add(Projections.property("gm.pk.codGruppo"), "profilo");
		properties.add(Projections.property("gmo.titolo"), "descProfilo");
		properties.add(Projections.property("ab.edicolaTest"), "edicolaTest");
		properties.add(Projections.property("ab.dataInizioiGerivPromo"), "dataInizioiGerivPromo");
		properties.add(Projections.property("ab.dataFineiGerivPromo"), "dataFineiGerivPromo");
		properties.add(Projections.property("ab.dataInizioiGerivPlus"), "dataInizioiGerivPlus");
		properties.add(Projections.property("ab.dataFineiGerivPlus"), "dataFineiGerivPlus");
		properties.add(Projections.property("ab.anagraficaCompilata"), "anagraficaCompilata");
		properties.add(Projections.property("ab.condizioniUsoAccettate"), "condizioniUsoAccettate");
		properties.add(Projections.property("ab.edicolaTest"), "strEdicolaTest");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(EdicolaDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<EdicolaDto> getEdicole(Integer codDl, Integer codEdicolaWeb, Integer codEdicolaDl, String ragioneSociale, boolean soloUtentiAmministratori) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserVo.class, "uvo");
		criteria.createCriteria("uvo.abbinamentoEdicolaDlVo", "ab");
		criteria.createCriteria("uvo.dlGruppoModuliVo", "gm");
		criteria.createCriteria("gm.gruppoModuli", "gmo");
		criteria.createCriteria("ab.anagraficaEdicolaVo", "ae");
		criteria.createCriteria("ab.anagraficaAgenziaVo", "aa");
		
		criteria.add(Restrictions.eqProperty("uvo.codUtente", "ab.codDpeWebEdicolaString"));

		if (codDl != null) {
			criteria.add(Restrictions.eq("aa.codFiegDl", codDl));
		}
		if (codEdicolaWeb != null && !codEdicolaWeb.equals("")) {
			criteria.add(Restrictions.eq("ab.codDpeWebEdicola", codEdicolaWeb));
		} 
		if (codEdicolaDl != null && !codEdicolaDl.equals("")) {
			criteria.add(Restrictions.eq("ab.codEdicolaDl", codEdicolaDl));
		} 
		if (ragioneSociale != null && !ragioneSociale.equals("")) {
			criteria.add(Restrictions.ilike("ae.ragioneSocialeEdicolaPrimaRiga", ragioneSociale, MatchMode.ANYWHERE));
		}
		if (soloUtentiAmministratori) {
			criteria.add(Restrictions.eq("uvo.utenteAmministratore", 1));
		}
		criteria.addOrder(Order.asc("ae.ragioneSocialeEdicolaPrimaRiga"));
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("ab.codEdicolaDl"), "codEdicolaDl");
		properties.add(Projections.property("ab.codDpeWebEdicola"), "codEdicolaWeb");
		properties.add(Projections.property("aa.codFiegDl"), "codDl");
		properties.add(Projections.property("ae.ragioneSocialeEdicolaPrimaRiga"), "ragioneSociale1");
		properties.add(Projections.property("ae.ragioneSocialeEdicolaSecondaRiga"), "ragioneSociale2"); 
		properties.add(Projections.property("ae.indirizzoEdicolaPrimaRiga"), "indirizzo");
		properties.add(Projections.property("ae.localitaEdicolaPrimaRiga"), "localita");
		properties.add(Projections.property("ae.siglaProvincia"), "provincia");
		properties.add(Projections.property("ab.dtAttivazioneEdicola"), "dataInserimento");
		properties.add(Projections.property("ab.dtSospensioneEdicola"), "dataSospensione");
		properties.add(Projections.property("uvo.pwd"), "password");
		properties.add(Projections.property("ae.email"), "email");
		properties.add(Projections.property("gm.pk.codGruppo"), "profilo");
		properties.add(Projections.property("gmo.titolo"), "descProfilo");
		properties.add(Projections.property("ab.edicolaTest"), "edicolaTest");
		properties.add(Projections.property("ab.dataInizioiGerivPromo"), "dataInizioiGerivPromo");
		properties.add(Projections.property("ab.dataFineiGerivPromo"), "dataFineiGerivPromo");
		properties.add(Projections.property("ab.dataInizioiGerivPlus"), "dataInizioiGerivPlus");
		properties.add(Projections.property("ab.dataFineiGerivPlus"), "dataFineiGerivPlus");
		properties.add(Projections.property("ab.anagraficaCompilata"), "anagraficaCompilata");
		properties.add(Projections.property("ab.condizioniUsoAccettate"), "condizioniUsoAccettate");
		properties.add(Projections.property("ab.edicolaTest"), "strEdicolaTest");
		properties.add(Projections.property("ab.checkConsegneGazzetta"), "checkConsegneGazzetta");
				
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(EdicolaDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	
	@Override
	public List<EdicolaDto> getEdicoleInforivDl(Integer codDl, Integer codEdicolaWeb, Integer codEdicolaDl, String ragioneSociale, boolean utenteAmministratore) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserVo.class, "uvo");
		criteria.createCriteria("uvo.abbinamentoEdicolaDlVo", "ab");
		criteria.createCriteria("uvo.dlGruppoModuliVo", "gm");
		criteria.createCriteria("gm.gruppoModuli", "gmo");
		criteria.createCriteria("ab.anagraficaEdicolaVo", "ae");
		criteria.createCriteria("ab.anagraficaAgenziaVo", "aa");
		if (codDl != null) {
			criteria.add(Restrictions.eq("aa.codFiegDl", codDl));
		}
		if (codEdicolaWeb != null && !codEdicolaWeb.equals("")) {
			criteria.add(Restrictions.eq("ab.codDpeWebEdicola", codEdicolaWeb));
		} 
		if (codEdicolaDl != null && !codEdicolaDl.equals("")) {
			criteria.add(Restrictions.eq("ab.codEdicolaDl", codEdicolaDl));
		} 
		if (ragioneSociale != null && !ragioneSociale.equals("")) {
			criteria.add(Restrictions.ilike("ae.ragioneSocialeEdicolaPrimaRiga", ragioneSociale, MatchMode.ANYWHERE));
		}
		if (utenteAmministratore) {
			criteria.add(Restrictions.eq("uvo.utenteAmministratore", 1));
		}
		criteria.add(Restrictions.eq("ab.edicolaIGerivInforivDl", true));
		
		criteria.addOrder(Order.asc("ae.ragioneSocialeEdicolaPrimaRiga"));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("uvo.codUtente"), "codUtente");
		properties.add(Projections.property("ab.codEdicolaDl"), "codEdicolaDl");
		properties.add(Projections.property("ab.codDpeWebEdicola"), "codEdicolaWeb");
		properties.add(Projections.property("aa.codFiegDl"), "codDl");
		properties.add(Projections.property("ae.ragioneSocialeEdicolaPrimaRiga"), "ragioneSociale1");
		properties.add(Projections.property("ae.ragioneSocialeEdicolaSecondaRiga"), "ragioneSociale2"); 
		properties.add(Projections.property("ae.indirizzoEdicolaPrimaRiga"), "indirizzo");
		properties.add(Projections.property("ae.localitaEdicolaPrimaRiga"), "localita");
		properties.add(Projections.property("ae.siglaProvincia"), "provincia");
		properties.add(Projections.property("ab.dtAttivazioneEdicola"), "dataInserimento");
		properties.add(Projections.property("ab.dtSospensioneEdicola"), "dataSospensione");
		properties.add(Projections.property("uvo.pwd"), "password");
		properties.add(Projections.property("ae.email"), "email");
		properties.add(Projections.property("gm.pk.codGruppo"), "profilo");
		properties.add(Projections.property("gmo.titolo"), "descProfilo");
		properties.add(Projections.property("ab.edicolaTest"), "edicolaTest");
		properties.add(Projections.property("ab.dataInizioiGerivPromo"), "dataInizioiGerivPromo");
		properties.add(Projections.property("ab.dataFineiGerivPromo"), "dataFineiGerivPromo");
		properties.add(Projections.property("ab.dataInizioiGerivPlus"), "dataInizioiGerivPlus");
		properties.add(Projections.property("ab.dataFineiGerivPlus"), "dataFineiGerivPlus");
		properties.add(Projections.property("ab.anagraficaCompilata"), "anagraficaCompilata");
		properties.add(Projections.property("ab.condizioniUsoAccettate"), "condizioniUsoAccettate");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(EdicolaDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public AbbinamentoEdicolaDlVo getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(Integer codiceDl, Integer codiceRivendita) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(AbbinamentoEdicolaDlVo.class, "ae");
		criteria.createCriteria("ae.anagraficaAgenziaVo", "aa", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("aa.codFiegDl", codiceDl));
		criteria.add(Restrictions.eq("ae.codEdicolaDl", codiceRivendita));
		//criteria.add(Restrictions.eq("ae.codDpeWebEdicola", codiceRivendita));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
		//return getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_ABBINAMENTO_EDICOLA_DL, codiceRivendita, codiceDl);		
	}
	
	@Override
	public AnagraficaEdicolaVo getEdicolaByCodFiegDlCodRivDl(Integer codiceDl, Integer codiceRivendita) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AbbinamentoEdicolaDlVo.class, "ab");
		criteria.createCriteria("ab.anagraficaAgenziaVo", "aa");
		criteria.createCriteria("ab.anagraficaEdicolaVo", "ae");
		criteria.add(Restrictions.eq("aa.codFiegDl", codiceDl));
		criteria.add(Restrictions.eq("ab.codEdicolaDl", codiceRivendita));
		AbbinamentoEdicolaDlVo aed = getDao().findUniqueResultByDetachedCriteria(criteria);
		return aed != null ? aed.getAnagraficaEdicolaVo() : null;
	}
	
	@Override
	public AnagraficaEdicolaAggiuntiviVo getAnagraficaEdicolaAggiuntiviVoByCodEdicolaWeb(Integer codiceRivenditaWeb) {
		return getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_ATTRIBUTI_AGGIUNTIVI_EDICOLA_BY_PK, codiceRivenditaWeb);
	}
	
	@Override
	public Integer getCodDpeWebEdicolaAbbinamentoEdicolaDlVo(Integer codiceRivenditaDl, Integer codFiegDl) {
		return getDao().findUniqueResultObjectByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_COD_EDICOLA_WEB, codiceRivenditaDl, codFiegDl);
	}
	
	@Override
	public void updateDataSospensioneEdicole(final Integer codDl, final String codEdicole, final String dateSospensione, final Boolean checkConsegneGazzetta) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				int executeUpdate = 0;
				try {
					
					if( codDl == IGerivConstants.COD_FIEG_MENTA || codDl == IGerivConstants.COD_FIEG_DL_CDL ){
						executeUpdate += updateDataSospensioneEdicole(codDl, dateSospensione, checkConsegneGazzetta, codEdicole, session);
					}else if (dateSospensione != null) {
						executeUpdate += updateDataSospensioneEdicole(codDl, dateSospensione, checkConsegneGazzetta, codEdicole, session);
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
	public void updatePwdUserEdicola(final Integer codEdicola, final String password, final boolean changePassword, final boolean isPwdCripata) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_UPDATE_PWD_EDICOLA);
				query.setString("password", password);
				query.setInteger("changePassword", changePassword ? 1 : 0);
				query.setInteger("pwdCriptata", isPwdCripata ? 1 : 0);
				query.setInteger("codEdicola", codEdicola);
				return query.executeUpdate();
			}
		};
		getDao().executeByHibernateCallback(action);
	}
	
	@Override
	public AnagraficaEdicolaVo getAnagraficaEdicola(Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AnagraficaEdicolaVo.class, "ae");
		criteria.add(Restrictions.eq("ae.codEdicola", codEdicola));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public void updateDatiEdicole(final Integer codUtente, final String pk, final String dataInserimento, final String dataSospensione, final String profilo, final String edicolaTest, final String edicolaPromoDtIni, final String edicolaPromoDtFin, final String edicolaPlusDtIni, final String edicolaPlusDtFin) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				int executeUpdate = 0;
				try {
					if (pk != null && session != null && (dataInserimento != null || dataSospensione != null || profilo != null || edicolaTest != null || edicolaPromoDtIni != null || edicolaPromoDtFin != null || edicolaPlusDtIni != null || edicolaPlusDtFin != null)) {
						if (dataInserimento != null) {
							executeUpdate += updateDateInserimento(pk, session, dataInserimento);
						}
						if (dataSospensione != null) {
							executeUpdate += updateDateSospensione(pk, session, dataSospensione);
						}
						if (profilo != null) {
							executeUpdate += updateProfilo(session, profilo);
						}
						if (edicolaTest != null) {
							executeUpdate += updateEdicolaTest(session, edicolaTest);
						}
						if (edicolaPromoDtIni != null) {
							executeUpdate += updateEdicolaPromoDtIni(session, edicolaPromoDtIni);
						}
						if (edicolaPromoDtFin != null) {
							executeUpdate += updateEdicolaPromoDtFin(session, edicolaPromoDtFin);
						}
						if (!Strings.isNullOrEmpty(edicolaPlusDtIni)) {
							executeUpdate += updateEdicolaPlusDtIni(session, edicolaPlusDtIni);
						}
						if (!Strings.isNullOrEmpty(edicolaPlusDtFin)) {
							executeUpdate += updateEdicolaPlusDtFin(session, edicolaPlusDtFin);
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
	 * Aggiorna il profilo delle edicole.
	 * 
	 * @param Session session
	 * @param String profilo
	 * @return int
	 */
	private int updateProfilo(Session session, String profilo) {
		int executeUpdate = 0;
		Query query = session.createQuery(IGerivQueryContants.HQL_UPDATE_PROFILO_EDICOLA);
		String[] arrProfilo = profilo.split(",");
		for (int i = 0; i < arrProfilo.length; i++) {
			String pkValue = arrProfilo[i].trim();
			String[] split = pkValue.split("\\|");
			Integer codEdicola = new Integer(split[0]);
			Integer idProfilo = Strings.isNullOrEmpty(split[1]) ? null : new Integer(split[1]);
			query.setInteger("idProfilo", idProfilo);
			query.setInteger("codDpeWebEdicola", codEdicola);
			executeUpdate += query.executeUpdate();
		}
		return executeUpdate;
	}
	
	/**
	 * Aggiorna le date di inserimento edicola.
	 * 
	 * @param String pks
	 * @param Session session
	 * @param String date
	 * @return int
	 * @throws ParseException
	 */
	private int updateDateInserimento(String pks, Session session, String date) throws ParseException {
		int executeUpdate = 0;
		Query query = session.createQuery(IGerivQueryContants.HQL_UPDATE_DATA_INSERIMENTO_EDICOLA);
		String[] arrDate = date.split(",");
		String[] arrPks = pks.split(",");
		for (int i = 0; i < arrPks.length; i++) {
			if(arrDate.length > i){
				String pkValue = arrPks[i].trim();
				String dateStr = (arrDate.length > i) ? arrDate[i].trim() : ""; 
				query.setTimestamp("dtAttivazioneEdicola", (dateStr.equals("")) ? null : DateUtilities.parseDate(dateStr, DateUtilities.FORMATO_DATA_SLASH));
				query.setInteger("codDpeWebEdicola", new Integer(pkValue));
				executeUpdate += query.executeUpdate();
			}else{
				break;
			}
		}
		return executeUpdate;
	}
	
	/**
	 * Aggiorna le date di sospensione edicola.
	 * 
	 * @param String pks
	 * @param Session session
	 * @param String date
	 * @return int
	 * @throws ParseException
	 */
	private int updateDateSospensione(String pks, Session session, String date) throws ParseException {
		int executeUpdate = 0;
		Query query = session.createQuery(IGerivQueryContants.HQL_UPDATE_DATA_SOSPENSIONE_EDICOLA);
		String[] arrDate = date.split(",");
		String[] arrPks = pks.split(",");
		for (int i = 0; i < arrPks.length; i++) {
			if(arrDate.length > i){
				String pkValue = arrPks[i].trim();
				String dateStr = (arrDate.length > i) ? arrDate[i].trim() : ""; 
				query.setTimestamp("dtSospensioneEdicola", (dateStr.equals("")) ? null : DateUtilities.parseDate(dateStr, DateUtilities.FORMATO_DATA_SLASH));
				query.setInteger("codDpeWebEdicola", new Integer(pkValue));
				executeUpdate += query.executeUpdate();
			}else{
				break;
			}
			
//			String pkValue = arrPks[i].trim();
//			String dateStr = (arrDate.length > i) ? arrDate[i].trim() : "";
//			query.setTimestamp("dtSospensioneEdicola", (dateStr.equals("")) ? null : DateUtilities.parseDate(dateStr, DateUtilities.FORMATO_DATA_SLASH));
//			query.setInteger("codDpeWebEdicola", new Integer(pkValue));
//			executeUpdate += query.executeUpdate();
		}
		return executeUpdate;
	}
	
	/**
	 * Setta le edicole igeriv promo data inizio.
	 * 
	 * @param Session session
	 * @throws ParseException 
	 */
	private int updateEdicolaPromoDtIni(Session session, String edicolaPromo) throws ParseException {
		int executeUpdate = 0;
		Query query = session.createQuery(IGerivQueryContants.HQL_UPDATE_EDICOLA_PROMO_DT_INI);
		String[] arrProfilo = edicolaPromo.split(",");
		for (int i = 0; i < arrProfilo.length; i++) {
			String pkValue = arrProfilo[i].trim();
			if (!Strings.isNullOrEmpty(pkValue)) {
				String[] split = pkValue.split("\\|");
				Integer codEdicola = new Integer(split[1].trim());
				Timestamp dateStr = (split.length < 3 || Strings.isNullOrEmpty(split[2].trim())) ? null : DateUtilities.parseDate(split[2].trim(), DateUtilities.FORMATO_DATA_SLASH);
				query.setTimestamp("date", dateStr);
				query.setInteger("codDpeWebEdicola", codEdicola);
				executeUpdate += query.executeUpdate();
			}
		}
		return executeUpdate;
	}
	
	/**
	 * Setta le edicole igeriv promo data fine.
	 * 
	 * @param Session session
	 * @throws ParseException 
	 */
	private int updateEdicolaPromoDtFin(Session session, String edicolaPromo) throws ParseException {
		int executeUpdate = 0;
		Query query = session.createQuery(IGerivQueryContants.HQL_UPDATE_EDICOLA_PROMO_DT_FIN);
		String[] arrProfilo = edicolaPromo.split(",");
		for (int i = 0; i < arrProfilo.length; i++) {
			String pkValue = arrProfilo[i].trim();
			if (!Strings.isNullOrEmpty(pkValue)) {
				String[] split = pkValue.split("\\|");
				Integer codEdicola = new Integer(split[1].trim());
				Timestamp dateStr = (split.length < 3 || Strings.isNullOrEmpty(split[2].trim())) ? null : DateUtilities.parseDate(split[2].trim(), DateUtilities.FORMATO_DATA_SLASH);
				query.setTimestamp("date", dateStr);
				query.setInteger("codDpeWebEdicola", codEdicola);
				executeUpdate += query.executeUpdate();
			}
		}
		return executeUpdate;
	}
	
	/**
	 * Setta le edicole igeriv plus data inizio.
	 * 
	 * @param Session session
	 * @throws ParseException 
	 */
	private int updateEdicolaPlusDtIni(Session session, String edicolaPlus) throws ParseException {
		int executeUpdate = 0;
		Query query = session.createQuery(IGerivQueryContants.HQL_UPDATE_EDICOLA_PLUS_DT_INI);
		String[] arrProfilo = edicolaPlus.split(",");
		for (int i = 0; i < arrProfilo.length; i++) {
			String pkValue = arrProfilo[i].trim();
			if (!Strings.isNullOrEmpty(pkValue)) {
				String[] split = pkValue.split("\\|");
				Integer codEdicola = new Integer(split[1].trim());
				Timestamp dateStr = (split.length < 3 || Strings.isNullOrEmpty(split[2].trim())) ? null : DateUtilities.parseDate(split[2].trim(), DateUtilities.FORMATO_DATA_SLASH);
				query.setTimestamp("date", dateStr);
				query.setInteger("codDpeWebEdicola", codEdicola);
				executeUpdate += query.executeUpdate();
			}
		}
		return executeUpdate;
	}
	
	/**
	 * Setta le edicole igeriv plus data fine.
	 * 
	 * @param Session session
	 * @throws ParseException 
	 */
	private int updateEdicolaPlusDtFin(Session session, String edicolaPlus) throws ParseException {
		int executeUpdate = 0;
		Query query = session.createQuery(IGerivQueryContants.HQL_UPDATE_EDICOLA_PLUS_DT_FIN);
		String[] arrProfilo = edicolaPlus.split(",");
		for (int i = 0; i < arrProfilo.length; i++) {
			String pkValue = arrProfilo[i].trim();
			if (!Strings.isNullOrEmpty(pkValue)) {
				String[] split = pkValue.split("\\|");
				Integer codEdicola = new Integer(split[1].trim());
				Timestamp dateStr = (split.length < 3 || Strings.isNullOrEmpty(split[2].trim())) ? null : DateUtilities.parseDate(split[2].trim(), DateUtilities.FORMATO_DATA_SLASH);
				query.setTimestamp("date", dateStr);
				query.setInteger("codDpeWebEdicola", codEdicola);
				executeUpdate += query.executeUpdate();
			}
		}
		return executeUpdate;
	}
	
	/**
	 * Setta le edicole test.
	 * 
	 * @param Session session
	 * @param String edicolaTest
	 * @return int
	 */
	private int updateEdicolaTest(Session session, String edicolaTest) {
		int executeUpdate = 0;
		Query query = session.createQuery(IGerivQueryContants.HQL_UPDATE_EDICOLA_TEST);
		String[] arrProfilo = edicolaTest.split(",");
		for (int i = 0; i < arrProfilo.length; i++) {
			String pkValue = arrProfilo[i].trim();
			String[] split = pkValue.split("\\|");
			Integer codEdicola = new Integer(split[0]);
			Boolean checked = Boolean.valueOf(split[1]);
			query.setBoolean("edicolaTest", checked);
			query.setInteger("codDpeWebEdicola", codEdicola);
			executeUpdate += query.executeUpdate();
		}
		return executeUpdate;
	}
	
	/**
	 * @param dateSospensione
	 * @param pk
	 * @param session
	 * @return
	 * @throws ParseException 
	 */
	private int updateDataSospensioneEdicole(Integer codDl, String dateSospensione, Boolean checkConsegneGazzetta, String codEdicole, Session session) throws ParseException {
		int executeUpdate = 0;
		Query query = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_UPDATE_DATA_SOSPENSIONE_EDICOLA);
		String[] arrDataSosp = null;
		
		if(dateSospensione!=null){
			arrDataSosp = dateSospensione.split(",");
		}
		
		String[] arrCodEdicola = codEdicole.split(",");
		
		for (int i = 0; i < arrCodEdicola.length; i++) {
			Integer codEdicola = new Integer(arrCodEdicola[i].trim());
			String dtSosp = (arrDataSosp!=null)?arrDataSosp[i].trim():null;
			query.setTimestamp("dtSospensioneEdicola", (dtSosp != null && !dtSosp.trim().equals("")) ? DateUtilities.parseDate(dtSosp, DateUtilities.FORMATO_DATA_SLASH) : null);
			query.setBoolean("checkConsegneGazzetta", checkConsegneGazzetta);
			query.setInteger("codFiegDl", codDl);
			query.setInteger("codEdicolaDl", codEdicola);
			executeUpdate += query.executeUpdate();
		}
		return executeUpdate;
	}
	
	@Override
	public AbbinamentoEdicolaDlVo getAbbinamentoEdicolaDlVoByCodEdicolaWeb(Integer codEdicola) {
		AbbinamentoEdicolaDlVo result = null;
		
		DetachedCriteria criteria = DetachedCriteria.forClass(AbbinamentoEdicolaDlVo.class, "ab");
		criteria.createCriteria("ab.anagraficaAgenziaVo", "aa");
		criteria.createCriteria("ab.anagraficaEdicolaVo", "ae");
		criteria.add(Restrictions.eq("ab.codDpeWebEdicola", codEdicola));
		
		result = getDao().findUniqueResultByDetachedCriteria(criteria);
		
		return result;
	}
	
	@Override
	public List<ParametriEdicolaDto> getParametriEdicola(final Integer codEdicola) {
		HibernateCallback<List<ParametriEdicolaDto>> action = new HibernateCallback<List<ParametriEdicolaDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<ParametriEdicolaDto> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(ParametriVo.class, "pevo");
				ProjectionList props = Projections.projectionList(); 
				props.add(Projections.property("pevo.codParametro"), "codParametro");
				props.add(Projections.property("pevo.sqlType"), "sqlType");
				props.add(Projections.property("pevo.value"), "value");
				props.add(Projections.property("pevo.defaultValue"), "defaultValue");
				criteria.setProjection(props);
				criteria.setResultTransformer(Transformers.aliasToBean(ParametriEdicolaDto.class));
				session.enableFilter("ParamtriFilter").setParameter("codEdicola", codEdicola);
				List<ParametriEdicolaDto> list = criteria.list();
				session.disableFilter("ParamtriFilter");
				return list;
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public ParametriEdicolaVo getParametroEdicola(Integer codEdicola, Integer codParametro) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ParametriEdicolaVo.class, "pevo");
		criteria.add(Restrictions.eq("pevo.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("pevo.pk.codParametro", codParametro));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public List<AbbinamentoEdicolaDlVo> getEdicoleNonSospese() {
		DetachedCriteria criteria = DetachedCriteria.forClass(AbbinamentoEdicolaDlVo.class, "ab");
		criteria.add(Restrictions.isNull("ab.dtSospensioneEdicola"));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<AbbinamentoEdicolaDlVo> getEdicoleNonSospese(Integer coddl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AbbinamentoEdicolaDlVo.class, "ab");
		criteria.createCriteria("ab.anagraficaEdicolaVo", "ae", JoinType.INNER_JOIN);
		criteria.createCriteria("ab.anagraficaAgenziaVo", "aa", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("aa.codFiegDl", coddl));
		criteria.add(Restrictions.isNull("ab.dtSospensioneEdicola"));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<AbbinamentoEdicolaDlVo> getEdicoleAutorizzateAggiornamentoBarcode(Integer coddl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AbbinamentoEdicolaDlVo.class, "ab");
		criteria.createCriteria("ab.anagraficaAgenziaVo", "aa");
		criteria.add(Restrictions.eq("ab.abilitataCorrezioneBarcode", true));
		criteria.add(Restrictions.eq("aa.codFiegDl", coddl));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public EdicolaDlVo getEdicolaDl(Integer codEdicolaDl, Integer codDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EdicolaDlVo.class, "ed");
		criteria.add(Restrictions.eq("ed.pk.codDl", codDl));
		criteria.add(Restrictions.eq("ed.pk.codRivDl", codEdicolaDl));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public List<EdicolaDlVo> getEdicoleDl(Integer coddl, Integer codEdicola, String ragSoc) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EdicolaDlVo.class, "ed");
		criteria.add(Restrictions.eq("ed.pk.codDl", coddl));
		if (codEdicola != null) {
			criteria.add(Restrictions.eq("ed.pk.codRivDl", codEdicola));
		}
		if (!Strings.isNullOrEmpty(ragSoc)) {
			criteria.add(Restrictions.ilike("ed.ragioneSocialeDlPrimaRiga", ragSoc, MatchMode.ANYWHERE));
		}
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public BonusInvitaUnCollegaVo getBonusInvitaUnCollega(Integer codDl, Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BonusInvitaUnCollegaVo.class, "bo");
		criteria.add(Restrictions.eq("bo.codDl", codDl));
		criteria.add(Restrictions.eq("bo.codEdicolaDl", codEdicola));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public Boolean getEmailValidoMultiDl(Integer[] arrId, Integer[] arrCodFiegDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AbbinamentoEdicolaDlVo.class, "abe");
		criteria.createCriteria("abe.anagraficaAgenziaVo", "aa");
		criteria.add(Restrictions.in("abe.codDpeWebEdicola", arrId));
		criteria.add(Restrictions.in("aa.codFiegDl", arrCodFiegDl));
		criteria.add(Restrictions.eq("abe.emailValido", true));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.count("abe.codDpeWebEdicola"));
		criteria.setProjection(props);
		Number count = (Number) getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		return (count != null && count.intValue() > 0) ? true : false;
	}
	
	@Override
	public EstrattoContoDto getEstrattoContoEdicolaPdf(Integer coddl, Integer codEdicola, java.sql.Date dataEstrattoConto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EstrattoContoEdicolaPdfVo.class, "ec");
		criteria.add(Restrictions.eq("ec.pk.codFiegDl", coddl));
		criteria.add(Restrictions.eq("ec.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("ec.pk.dataEstrattoConto", dataEstrattoConto));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("ec.nomeFile"), "nomeFile");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(EstrattoContoDto.class));
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public EmailRivenditaVo getEmailRivenditaVo(Integer idEmailRivendita) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EmailRivenditaVo.class, "em");
		criteria.add(Restrictions.eq("em.idEmailRivendita", idEmailRivendita));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	
	@Override
	public ImmagineAnagraficaEdicolaVo getImmagineAnagraficaEdicola(Integer codEdicola, Integer progressivo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ImmagineAnagraficaEdicolaVo.class, "im");
		criteria.add(Restrictions.eq("im.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("im.pk.progressivo", progressivo));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public List<EdicolaDto> getNuoveEdicole(Integer codFiegDl, Date dataCreazione) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AbbinamentoEdicolaDlVo.class, "ab");
		criteria.createCriteria("ab.anagraficaEdicolaVo", "ae");
		criteria.createCriteria("ab.anagraficaAgenziaVo", "aa");
		criteria.add(Restrictions.eq("aa.codFiegDl", codFiegDl));
		criteria.add(Restrictions.ge("ab.dtAttivazioneEdicola", dataCreazione));
		criteria.add(Restrictions.eq("ab.aggiornataProdottiVariDl", false));
		criteria.addOrder(Order.asc("ae.ragioneSocialeEdicolaPrimaRiga"));
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("ab.codEdicolaDl"), "codEdicolaDl");
		properties.add(Projections.property("ab.codDpeWebEdicola"), "codEdicolaWeb");
		properties.add(Projections.property("aa.codFiegDl"), "codDl");
		properties.add(Projections.property("ab.codiceContabileCliente"), "codiceContabileCliente");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(EdicolaDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<EdicolaDto> getEdicoleByIdtn(final Integer codFiegDl, final Integer idtn) {
		HibernateCallback<List<EdicolaDto>> action = new HibernateCallback<List<EdicolaDto>>() {

			@Override
			public List<EdicolaDto> doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuilder select = new StringBuilder();
				select.append("SELECT DISTINCT nomeriva9106 as ragioneSociale1, viariva9106 as indirizzo, locariva9106 as localita, provriv9106 as provincia, capriv9106 as cap, teleriv9106 as telefono ");
				select.append("FROM tbl_9611 INNER JOIN tbl_9106 ON (crivw9106 = crivw9611) ");
				select.append("WHERE coddl9611 = :coddl AND idtn9611 = :idtn ");
				select.append("ORDER BY capriv9106, locariva9106, nomeriva9106");
				
				SQLQuery sqlQuery = session.createSQLQuery(select.toString());
				sqlQuery.setResultTransformer(Transformers.aliasToBean(EdicolaDto.class));
				sqlQuery.setInteger("coddl", codFiegDl);
				sqlQuery.setInteger("idtn", idtn);
				
				sqlQuery.addScalar("ragioneSociale1", StringType.INSTANCE);
				sqlQuery.addScalar("indirizzo", StringType.INSTANCE);
				sqlQuery.addScalar("localita", StringType.INSTANCE);
				sqlQuery.addScalar("provincia", StringType.INSTANCE);
				sqlQuery.addScalar("cap", StringType.INSTANCE);
				sqlQuery.addScalar("telefono", StringType.INSTANCE);
				
				return sqlQuery.list();
			}
			
		};
		
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public List<EdicolaDto> getEdicoleConPubblicazioneInGiacenza(Integer coddl,
			Integer idtn) {
		return null;
	}
	
	@Override
	public List<AbbinamentoEdicolaDlVo> getEdicoleInforivDlByCodEdicolaWebMaster(Integer codEdicolaMaster) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AbbinamentoEdicolaDlVo.class, "ab");
		criteria.createCriteria("ab.anagraficaEdicolaVo", "ae");
		criteria.createCriteria("ab.anagraficaAgenziaVo", "aa");
		criteria.add(Restrictions.eq("ab.codEdicolaMaster", codEdicolaMaster));
		criteria.add(Restrictions.eq("ab.edicolaIGerivInforivDl", true));
		criteria.addOrder(Order.asc("ae.ragioneSocialeEdicolaPrimaRiga"));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	
	@Override
	public List<EdicolaDto> getEdicoleInArea(final AnagraficaEdicolaVo edicola, final Integer kmArea) {
		
		HibernateCallback<List<EdicolaDto>> action = new HibernateCallback<List<EdicolaDto>>() {

			@Override
			public List<EdicolaDto> doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuilder select = new StringBuilder();
				
				Double lat  = edicola.getLatitudine();
				Double lng  = edicola.getLongitudine();
				String str_lat = String.format("%.3f",lat).replace(",", ".");
				String str_lng = String.format("%.3f",lng).replace(",", ".");
				
				select.append(" select "
							+ "	crivw9106 as codEdicolaWeb, "
							+ " nomeriva9106 as ragioneSociale1, "
							+ " viariva9106 as indirizzo, "
							+ " locariva9106 as localita, "
							+ " provriv9106 as provincia, "
							+ " capriv9106 as cap, "
							+ " teleriv9106 as telefono, "
							+ " latit9106 as latitudine, "
							+ " longi9106 as longitudine, "
							+ " flggeo9106 as flggeo ");
				select.append(" from tbl_9106 e ");
				select.append(" where e.crivw9106 in ( ");
				select.append(" SELECT crivw");
				select.append(" FROM (select 6372 * ACOS( ");
				select.append(" (SIN(3.14*"+str_lat+"/180))*(SIN(3.14*z.LATIT9106/180)) + ");
				select.append(" (COS(3.14*"+str_lat+"/180))*(COS(3.14*z.LATIT9106/180)) * ");
				select.append(" COS(ABS((3.14*"+str_lng+"/180)-(3.14*z.LONGI9106/180)))) as DISTANZA_KM ,z.crivw9106 as crivw ");
				select.append(" from tbl_9106 z ");
				select.append(" Where  z.PROVRIV9106 = :prov and z.crivw9106 != :codEdicola) ");
				select.append(" WHERE DISTANZA_KM <= :kmArea) ");
				
				SQLQuery sqlQuery = session.createSQLQuery(select.toString());
				sqlQuery.setResultTransformer(Transformers.aliasToBean(EdicolaDto.class));
				sqlQuery.setString("prov", edicola.getSiglaProvincia());
				sqlQuery.setInteger("codEdicola", edicola.getCodEdicola());
				sqlQuery.setInteger("kmArea", kmArea);
				
				sqlQuery.addScalar("codEdicolaWeb", IntegerType.INSTANCE);
				sqlQuery.addScalar("ragioneSociale1", StringType.INSTANCE);
				sqlQuery.addScalar("indirizzo", StringType.INSTANCE);
				sqlQuery.addScalar("localita", StringType.INSTANCE);
				sqlQuery.addScalar("provincia", StringType.INSTANCE);
				sqlQuery.addScalar("cap", StringType.INSTANCE);
				sqlQuery.addScalar("telefono", StringType.INSTANCE);
				sqlQuery.addScalar("latitudine", DoubleType.INSTANCE);
				sqlQuery.addScalar("longitudine", DoubleType.INSTANCE);
				sqlQuery.addScalar("flggeo", StringType.INSTANCE);
				
				return sqlQuery.list();
			}
			
		};
		
		return getDao().findByHibernateCallback(action);
	}
	
	
	public List<EdicolaDto> getEdicoleDlAttive(Integer codFiegDl){
		DetachedCriteria criteria = DetachedCriteria.forClass(UserVo.class, "uvo");
		criteria.createCriteria("uvo.abbinamentoEdicolaDlVo", "ab");
		criteria.createCriteria("uvo.dlGruppoModuliVo", "gm");
		criteria.createCriteria("gm.gruppoModuli", "gmo");
		criteria.createCriteria("ab.anagraficaEdicolaVo", "ae");
		criteria.createCriteria("ab.anagraficaAgenziaVo", "aa");
		
		criteria.add(Restrictions.eq("aa.codFiegDl", codFiegDl));
		
		Timestamp now = new Timestamp(new Date().getTime());
		
		Criterion c1 = Restrictions.isNull("ab.dtSospensioneEdicola");
		Criterion c2 = Restrictions.ge("ab.dtSospensioneEdicola", now);
		Criterion c3 = Restrictions.or(c1, c2);
		criteria.add(c3);	
				
		criteria.addOrder(Order.asc("ae.ragioneSocialeEdicolaPrimaRiga"));
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("ab.codEdicolaDl"), "codEdicolaDl");
		properties.add(Projections.property("ab.codDpeWebEdicola"), "codEdicolaWeb");
		properties.add(Projections.property("aa.codFiegDl"), "codDl");
		properties.add(Projections.property("ae.ragioneSocialeEdicolaPrimaRiga"), "ragioneSociale1");
		properties.add(Projections.property("ae.ragioneSocialeEdicolaSecondaRiga"), "ragioneSociale2"); 
		properties.add(Projections.property("ae.indirizzoEdicolaPrimaRiga"), "indirizzo");
		properties.add(Projections.property("ae.localitaEdicolaPrimaRiga"), "localita");
		properties.add(Projections.property("ae.siglaProvincia"), "provincia");
		properties.add(Projections.property("ab.dtAttivazioneEdicola"), "dataInserimento");
		properties.add(Projections.property("ab.dtSospensioneEdicola"), "dataSospensione");
		properties.add(Projections.property("uvo.pwd"), "password");
		properties.add(Projections.property("ae.email"), "email");
		properties.add(Projections.property("gm.pk.codGruppo"), "profilo");
		properties.add(Projections.property("gmo.titolo"), "descProfilo");
		properties.add(Projections.property("ab.edicolaTest"), "edicolaTest");
		properties.add(Projections.property("ab.dataInizioiGerivPromo"), "dataInizioiGerivPromo");
		properties.add(Projections.property("ab.dataFineiGerivPromo"), "dataFineiGerivPromo");
		properties.add(Projections.property("ab.dataInizioiGerivPlus"), "dataInizioiGerivPlus");
		properties.add(Projections.property("ab.dataFineiGerivPlus"), "dataFineiGerivPlus");
		properties.add(Projections.property("ab.anagraficaCompilata"), "anagraficaCompilata");
		properties.add(Projections.property("ab.condizioniUsoAccettate"), "condizioniUsoAccettate");
		properties.add(Projections.property("ab.edicolaTest"), "strEdicolaTest");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(EdicolaDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}

	@Override
	public List<PianoProfiliEdicolaVo> getPianoProfiliEdicola(Integer codEdicolaWeb, Integer codDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PianoProfiliEdicolaVo.class, "pp");
		criteria.createCriteria("pp.anagraficaEdicolaVo", "ae");
		criteria.createCriteria("pp.anagraficaAgenziaVo", "aa");
		criteria.createCriteria("pp.codiceGruppoVo", "cg");
		criteria.add(Restrictions.eq("pp.codDpeWebEdicola", codEdicolaWeb));
		criteria.add(Restrictions.eq("aa.codFiegDl", codDl));
		criteria.addOrder(Order.desc("pp.dtAttivazioneProfiloEdicola"));
		return getDao().findByDetachedCriteria(criteria);
	}

	@Override
	public PianoProfiliEdicolaVo getPianoProfiloEdicolaAttivo(Integer codEdicolaWeb, Integer codDl) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
