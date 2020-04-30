package it.dpe.igeriv.bo.batch;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.BollaResaProdottiNonEditorialiDto;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.dto.EsportazioneDatiDlResultDto;
import it.dpe.igeriv.dto.RichiestaRifornimentoProdottiDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.util.JmsConstants;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.AbbinamentoIdtnInforivVo;
import it.dpe.igeriv.vo.AbbinamentoTipoMovimentoFondoBollaInforivVo;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.AnagraficaPubblicazioniVo;
import it.dpe.igeriv.vo.ArgomentoVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.BollaDettaglioVo;
import it.dpe.igeriv.vo.BollaResaDettaglioVo;
import it.dpe.igeriv.vo.BollaResaRiassuntoVo;
import it.dpe.igeriv.vo.BollaResaVo;
import it.dpe.igeriv.vo.BollaRiassuntoVo;
import it.dpe.igeriv.vo.BollaStatisticaStoricoVo;
import it.dpe.igeriv.vo.BollaVo;
import it.dpe.igeriv.vo.DecodificaRichiamiResaVo;
import it.dpe.igeriv.vo.DlGruppoModuliVo;
import it.dpe.igeriv.vo.EmailRivenditaVo;
import it.dpe.igeriv.vo.EstrattoContoEdicolaDettaglioVo;
import it.dpe.igeriv.vo.EstrattoContoEdicolaVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.ImmaginePubblicazioneVo;
import it.dpe.igeriv.vo.InforivFtpFileVo;
import it.dpe.igeriv.vo.LavorazioneResaVo;
import it.dpe.igeriv.vo.LocalitaVo;
import it.dpe.igeriv.vo.MessaggiBollaVo;
import it.dpe.igeriv.vo.MessaggioVo;
import it.dpe.igeriv.vo.ParametriEdicolaVo;
import it.dpe.igeriv.vo.PeriodicitaTrascodificaInforeteVo;
import it.dpe.igeriv.vo.PeriodicitaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiBollaDettaglioVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCategoriaEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiGiacenzeVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiPrezziAcquistoVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiRichiesteRifornimentoDettaglioVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiSottoCategoriaEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiVo;
import it.dpe.igeriv.vo.ProvinciaVo;
import it.dpe.igeriv.vo.ResaRiscontrataVo;
import it.dpe.igeriv.vo.RichiestaClienteVo;
import it.dpe.igeriv.vo.RichiestaFissaClienteEdicolaVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoVo;
import it.dpe.igeriv.vo.StoricoCopertineVo;
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiBollaDettaglioPk;
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiRichiesteRifornimentoDettaglioPk;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.driver.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.support.nativejdbc.C3P0NativeJdbcExtractor;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;
import com.mchange.io.FileUtils;

/**
 * @author romanom
 * 
 */
@Repository("IGerivBatchBo")
@SuppressWarnings("unchecked")
class IGerivBatchBoImpl implements IGerivBatchBo {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(getClass());
	private final BaseDao<?> dao;
	private final String imgMiniaturePathDir;
	private final String igerivBkpImgProdottiVariDir;
	private final String imgPathDir;
	
	@Autowired
	IGerivBatchBoImpl(BaseDao<?> dao, @Value("${img.miniature.edicola.prodotti.vari.path.dir}") String imgMiniaturePathDir, @Value("${img.miniature.edicola.prodotti.vari.path.bkp.dir}") String igerivBkpImgProdottiVariDir, @Value("${img.path.dir}") String imgPathDir) {
		this.dao = dao;
		this.imgMiniaturePathDir = imgMiniaturePathDir;
		this.igerivBkpImgProdottiVariDir = igerivBkpImgProdottiVariDir;
		this.imgPathDir = imgPathDir;
	}

	@Override
	public synchronized void cleanupNonUsedImagesProdottiVari() {
		List<ProdottiNonEditorialiCategoriaEdicolaVo> listCategorie = getProdottiNonEditorialiCategorieEdicolaVo(null);
		List<ProdottiNonEditorialiSottoCategoriaEdicolaVo> listAllSottocatgorie = getProdottiNonEditorialiSottoCategorieEdicolaVo(null, null);
		List<ProdottiNonEditorialiVo> listProdottiEdicola = getProdottiNonEditorialiEdicola(null, null, null);
		List<String> immaginiCategorie = extract(listCategorie, on(ProdottiNonEditorialiCategoriaEdicolaVo.class).getImmagine());
		List<String> immaginiSottoCategorie = extract(listAllSottocatgorie, on(ProdottiNonEditorialiSottoCategoriaEdicolaVo.class).getImmagine());
		List<String> immaginiProdotti = extract(listProdottiEdicola, on(ProdottiNonEditorialiVo.class).getNomeImmagine());
		File dir = new File(imgMiniaturePathDir);
		File bkpDir = new File(igerivBkpImgProdottiVariDir);
		if (!bkpDir.isDirectory()) {
			bkpDir.mkdirs();
		}
		for (File file : dir.listFiles()) {
			String fileName = file.getName();
			if (!immaginiCategorie.contains(fileName) && !immaginiSottoCategorie.contains(fileName) && !immaginiProdotti.contains(fileName)) {
				log.info("Moving file: " + file.getName() + " to dir: " + bkpDir.getName());
				file.renameTo(new File(bkpDir, file.getName()));
			}
		}
	}
	
	@Override
	public synchronized void cleanupOldImagesPubblicazioni() {
		File dir = new File(imgPathDir);
		for (File file : dir.listFiles()) {
			String name = file.getName();
			if (isImageToDelete(name)) {
				log.info("Deleting file: " + name);
				//VERIFICA PROPRIETA DEL FILE - SE PIU' VECCHIO DI 15 GIORNI VIENE CANCELLATO
				if(it.dpe.igeriv.util.FileUtils.isDeleteFile_verificationDateFileLastModified(file,15))
					file.delete();
			}
		}
	}
	
	
	
	
	/**
	 * Verifica che l'immagine sia contenuta nella tabella delle uscite
	 * 
	 * @param String barcode
	 * @return boolean
	 */
	private boolean isImageToDelete(String name) {
		DetachedCriteria criteria = DetachedCriteria.forClass(StoricoCopertineVo.class, "sc");
		criteria.createCriteria("sc.immagine", "im");
		criteria.add(Restrictions.eq("im.nome", name));
		return dao.findUniqueResultObjectByDetachedCriteria(criteria) == null;
	}

	@Override
	public void deleteBolleProdottiVari() {
		List<AbbinamentoEdicolaDlVo> edicole = getEdicoleNonSospese();
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		for (AbbinamentoEdicolaDlVo ed : edicole) {
			cal.setTime(now);
			ParametriEdicolaVo parametroEdicola = getParametroEdicola(ed.getCodDpeWebEdicola(), IGerivConstants.COD_PARAMETRO_EDICOLA_GIORNI_STORICO_BOLLE_PRODOTTI_VARI);
			int numGiorni = IGerivConstants.DEFAULT_NUM_GIORNI_CANCELLA_PRODOTTI_VARI;
			if (parametroEdicola != null && !Strings.isNullOrEmpty(parametroEdicola.getValue())) {
				numGiorni = new Integer(parametroEdicola.getValue());
			}
			cal.add(Calendar.DAY_OF_MONTH, -numGiorni);
			deleteBolleProdottiVariEdicolaBeforeDate(ed.getCodDpeWebEdicola(), cal.getTime());
		}
	}
	
	@Override
	public BaseVo createBaseVo(BaseVo vo) {
		return dao.create(vo);
	}
	
	@Override
	public BaseVo saveBaseVo(BaseVo vo) {
		return dao.saveOrUpdate(vo);
	}

	@Override
	public BaseVo mergeBaseVo(BaseVo vo) {
		return dao.merge(vo);
	}

	@Override
	public List<ProdottiNonEditorialiCategoriaEdicolaVo> getProdottiNonEditorialiCategorieEdicolaVo(Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiCategoriaEdicolaVo.class, "pnecat");
		if (codEdicola != null) {
			criteria.add(Restrictions.eq("pnecat.pk.codEdicola", codEdicola));
		}
		criteria.addOrder(Order.asc("pnecat.posizione"));
		return dao.findByDetachedCriteria(criteria);
	}

	@Override
	public List<ProdottiNonEditorialiSottoCategoriaEdicolaVo> getProdottiNonEditorialiSottoCategorieEdicolaVo(Long categoria, Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiSottoCategoriaEdicolaVo.class, "pne");
		criteria.createCriteria("pne.edicola", "ed");
		if (codEdicola != null) {
			criteria.add(Restrictions.eq("ed.codEdicola", codEdicola));
		}
		if (categoria != null) {
			criteria.add(Restrictions.eq("pne.pk.codCategoria", categoria));
		}
		criteria.addOrder(Order.asc("pne.posizione"));
		return dao.findByDetachedCriteria(criteria);
	}

	@Override
	public List<ProdottiNonEditorialiVo> getProdottiNonEditorialiEdicola(final Long categoria, final Long sottocategoria, final Integer codEdicola) {
		HibernateCallback<List<ProdottiNonEditorialiVo>> action = new HibernateCallback<List<ProdottiNonEditorialiVo>>() {
			@Override
			public List<ProdottiNonEditorialiVo> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(ProdottiNonEditorialiVo.class, "pne");
				criteria.createCriteria("pne.categoria", "cat");
				criteria.createCriteria("pne.sottocategoria", "scat");
				criteria.createCriteria("pne.edicola", "ed");
				criteria.createCriteria("pne.prezzi", "prezzi", JoinType.LEFT_OUTER_JOIN);
				if (codEdicola != null) {
					criteria.add(Restrictions.eq("ed.codEdicola", codEdicola));
				}
				if (categoria != null) {
					criteria.add(Restrictions.eq("pne.codCategoria", categoria));
				}
				if (sottocategoria != null) {
					criteria.add(Restrictions.eq("pne.codSottoCategoria", sottocategoria));
				}
				criteria.addOrder(Order.asc("pne.posizione"));
				criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
				return criteria.list();
			}
		};
		return dao.findByHibernateCallback(action);
	}

	@Override
	public List<AbbinamentoEdicolaDlVo> getEdicoleNonSospese() {
		DetachedCriteria criteria = DetachedCriteria.forClass(AbbinamentoEdicolaDlVo.class, "ab");
		criteria.add(Restrictions.isNull("ab.dtSospensioneEdicola"));
		return dao.findByDetachedCriteria(criteria);
	}

	@Override
	public ParametriEdicolaVo getParametroEdicola(Integer codEdicola, Integer codParametro) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ParametriEdicolaVo.class, "pevo");
		criteria.add(Restrictions.eq("pevo.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("pevo.pk.codParametro", codParametro));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public void deleteBolleProdottiVariEdicolaBeforeDate(Integer codEdicola, Date time) {
		dao.bulkUpdate("delete from ProdottiNonEditorialiBollaDettaglioVo vo where vo.pk.idDocumento in (select vo1.idDocumento from ProdottiNonEditorialiBollaVo vo1 where vo1.dataDocumento < ? and vo1.edicola.codEdicola = ?)", new Object[]{new Timestamp(time.getTime()), codEdicola});
		dao.bulkUpdate("delete from ProdottiNonEditorialiBollaVo vo where vo.dataDocumento < ? and vo.edicola.codEdicola = ?", new Object[]{new Timestamp(time.getTime()), codEdicola});
	}

	@Override
	public Integer getCodDpeWebEdicolaAbbinamentoEdicolaDlVo(Integer codiceRivenditaDl, Integer codFiegDl) {
		return dao.findUniqueResultObjectByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_COD_EDICOLA_WEB, codiceRivenditaDl, codFiegDl);
	}

	@Override
	public AbbinamentoEdicolaDlVo getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(Integer codiceDl, Integer codiceRivendita) {
		return dao.findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_ABBINAMENTO_EDICOLA_DL, codiceRivendita, codiceDl);
	}

	@Override
	public LavorazioneResaVo getLavorazioneResaVo(String zipFile) {
		return dao.findUniqueResultObjectByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_LAVORAZIONE_RESA_BY_ID, zipFile);
	}

	@Override
	public ImmaginePubblicazioneVo getImmaginePubblicazione(String barcode) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ImmaginePubblicazioneVo.class, "brvo");
		criteria.add(Restrictions.eq("brvo.barcode", barcode));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public List<AnagraficaPubblicazioniVo> getQuotidianoByTitolo(String titolo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AnagraficaPubblicazioniVo.class, "ap");
		criteria.createCriteria("ap.periodicitaVo", "per");
		criteria.add(Restrictions.eq("per.pk.periodicita", IGerivConstants.PERIODICITA_QUOTIDIANO));
		criteria.add(Restrictions.ilike("ap.titolo", titolo, MatchMode.ANYWHERE));
		criteria.add(Restrictions.or(Restrictions.isNull("ap.imgMiniaturaName"), Restrictions.ilike("ap.imgMiniaturaName", "_fake", MatchMode.ANYWHERE)));
		return dao.findByDetachedCriteria(criteria);
	}

	@Override
	public List<AnagraficaPubblicazioniVo> getPeriodicoByTitolo(String titolo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AnagraficaPubblicazioniVo.class, "ap");
		criteria.createCriteria("ap.periodicitaVo", "per");
		criteria.add(Restrictions.ne("per.pk.periodicita", IGerivConstants.PERIODICITA_QUOTIDIANO));
		criteria.add(Restrictions.ilike("ap.titolo", titolo, MatchMode.ANYWHERE));
		criteria.add(Restrictions.or(Restrictions.isNull("ap.imgMiniaturaName"), Restrictions.ilike("ap.imgMiniaturaName", "_fake", MatchMode.ANYWHERE)));
		return dao.findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<AnagraficaPubblicazioniVo> getListAnagraficaPubblicazioneByCodQuotidiano(Integer codFiegDl, Integer codInizioQuotidiano, Integer codFineQuotidiano) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AnagraficaPubblicazioniVo.class, "ap");
		criteria.add(Restrictions.eq("ap.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("ap.codInizioQuotidiano", codInizioQuotidiano));
		criteria.add(Restrictions.eq("ap.codFineQuotidiano", codFineQuotidiano));
		return dao.findByDetachedCriteria(criteria);
	}

	@Override
	public Timestamp getSysdate() {
		return dao.getSysdate();
	}

	@Override
	public List<AnagraficaAgenziaVo> getAgenzieInforiv() {
		DetachedCriteria criteria = DetachedCriteria.forClass(AnagraficaAgenziaVo.class, "aavo");
		criteria.add(Restrictions.eq("aavo.dlInforiv", true));
		criteria.addOrder(Order.asc("aavo.ragioneSocialeDlPrimaRiga"));
		return dao.findByDetachedCriteria(criteria);
	}

	@Override
	public AnagraficaAgenziaVo getAgenziaByCodice(Integer codFiegDl) {
		return dao.findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_ANAGRAFICA_AGENZIE_BY_ID, codFiegDl);
	}

	@Override
	public <T extends BaseVo> List<T> saveVoList(List<T> list) {
		List<T> list1 = new ArrayList<T>();
		for (T vo : list) {
			T saveOrUpdate = dao.saveOrUpdate(vo);
			list1.add(saveOrUpdate);
		}
		return list1; 
	}

	@Override
	public Long getNextSeqVal(String seqUtentiEdicola) {
		return dao.getNextSeqVal(seqUtentiEdicola);
	}

	@Override
	public GruppoModuliVo getGruppoModuliByRole(String roleIgerivBaseAdmin) {
		return dao.findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_GRUPPO_BY_ROLE_JOINS, roleIgerivBaseAdmin);
	}

	@Override
	public DlGruppoModuliVo getDlGruppoModuliVo(Integer idGruppoModuli, Integer codDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DlGruppoModuliVo.class, "dlgmvo");
		criteria.add(Restrictions.eq("dlgmvo.pk.codGruppo", idGruppoModuli));
		criteria.add(Restrictions.eq("dlgmvo.pk.codDl", codDl));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public Integer getLastId(Class<?> clazz, String pkName, String restriction, Object objRestriction) {
		DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
		if (restriction != null) {
			criteria.add(Restrictions.eq(restriction, objRestriction));
		}
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.max(pkName));
		criteria.setProjection(props);
		Integer lastId = dao.findUniqueResultObjectByDetachedCriteria(criteria);
		return ((lastId == null) ? 0 : lastId) + 1;
	}

	@Override
	public AbbinamentoEdicolaDlVo getAbbinamentoEdicolaDlVoByCodEdicolaWeb(Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AbbinamentoEdicolaDlVo.class, "ab");
		criteria.add(Restrictions.eq("ab.codDpeWebEdicola", codEdicola));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public PeriodicitaTrascodificaInforeteVo getPeriodicitaTrascodificaInforete(Integer codPeriodicitaInforete) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PeriodicitaTrascodificaInforeteVo.class, "brvo");
		criteria.createCriteria("brvo.periodicita", "per");
		criteria.add(Restrictions.eq("brvo.periodicitaInforete", codPeriodicitaInforete));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public AnagraficaPubblicazioniVo getAnagraficaPubblicazioneByPk(Integer codFiegDl, Integer codPubblicazioneInt) {
		/*
		DetachedCriteria criteria = DetachedCriteria.forClass(StoricoCopertineVo.class, "sc");
		criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
		criteria.createCriteria("ap.limitiPeriodicitaVo", "lp");
		criteria.add(Restrictions.eq("ap.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("ap.pk.codicePubblicazione", codPubblicazioneInt));
		StoricoCopertineVo copertina = dao.findUniqueResultByDetachedCriteria(criteria);
		return (copertina != null) ? copertina.getAnagraficaPubblicazioniVo() : null;
		*/ 

		DetachedCriteria criteria = DetachedCriteria.forClass(AnagraficaPubblicazioniVo.class, "ap");
		criteria.add(Restrictions.eq("ap.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("ap.pk.codicePubblicazione", codPubblicazioneInt));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public <T extends BaseVo> List<T> getPeriodicita() {
		return dao.findByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_PERIODICITA);
	}

	@Override
	public AbbinamentoIdtnInforivVo getAbbinamentoIdtnInforiv(Integer codFiegDl, String idtni) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AbbinamentoIdtnInforivVo.class, "abii");
		criteria.add(Restrictions.eq("abii.pk.codDl", codFiegDl));
		criteria.add(Restrictions.eq("abii.pk.idtnInforete", idtni));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public StoricoCopertineVo getStoricoCopertinaByPk(Integer codFiegDl, Integer idtn) {
		DetachedCriteria criteria = DetachedCriteria.forClass(StoricoCopertineVo.class, "scvo");
		criteria.createCriteria("scvo.anagraficaPubblicazioniVo", "ap");
		criteria.add(Restrictions.eq("scvo.pk.codDl", codFiegDl));
		criteria.add(Restrictions.eq("scvo.pk.idtn", idtn));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public <T extends BaseVo> void deleteVo(T vo) {
		dao.delete(vo);
	}

	@Override
	public Integer getLastProgressivoEstrattoConto(Integer codFiegDl, Integer codEdicolaWeb, java.sql.Date dataEstrattoConto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EstrattoContoEdicolaDettaglioVo.class, "eced");
		criteria.add(Restrictions.eq("eced.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("eced.pk.codEdicola", codEdicolaWeb));
		criteria.add(Restrictions.eq("eced.pk.dataEstrattoConto", dataEstrattoConto));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.max("eced.pk.progressivo"));
		criteria.setProjection(props);
		return dao.findUniqueResultObjectByDetachedCriteria(criteria);
	}

	@Override
	public EstrattoContoEdicolaDettaglioVo getEstrattoContoEdicolaDettaglioVo(Integer codFiegDl, Integer codEdicolaWeb, java.sql.Date dataEstrattoConto, int progressivo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EstrattoContoEdicolaDettaglioVo.class, "eced");
		criteria.add(Restrictions.eq("eced.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("eced.pk.codEdicola", codEdicolaWeb));
		criteria.add(Restrictions.eq("eced.pk.dataEstrattoConto", dataEstrattoConto));
		criteria.add(Restrictions.eq("eced.pk.progressivo", progressivo));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public EstrattoContoEdicolaVo getEstrattoContoEdicolaVo(Integer codFiegDl, Integer codEdicolaWeb, java.sql.Date dataEstrattoConto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EstrattoContoEdicolaVo.class, "ecevo");
		criteria.createCriteria("ecevo.dettagli", "det");
		criteria.add(Restrictions.eq("ecevo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("ecevo.pk.codEdicola", codEdicolaWeb));
		criteria.add(Restrictions.eq("ecevo.pk.dataEstrattoConto", dataEstrattoConto));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public AbbinamentoTipoMovimentoFondoBollaInforivVo getAbbinamentoTipoMovimentoFondoBollaInforiv(Integer tipoMovimentoInforiv) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AbbinamentoTipoMovimentoFondoBollaInforivVo.class, "tmvo");
		criteria.add(Restrictions.eq("tmvo.pk.tipoRecordFondoBollaInforiv", tipoMovimentoInforiv));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public void deleteFondoBollaEdicolaInforiv(Integer codFiegDl, Integer codEdicolaWeb, Timestamp dataBolla, String tipoBolla) {
		dao.bulkUpdate("delete from FondoBollaDettaglioVo vo where vo.pk.codFiegDl = ? and vo.pk.codEdicola = ? and vo.pk.dtBolla = ? and vo.pk.tipoBolla = ?", new Object[]{codFiegDl, codEdicolaWeb, dataBolla, tipoBolla});
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
		return dao.findUniqueResultObjectByDetachedCriteria(criteria);
	}

	@Override
	public BollaVo getBollaVo(Integer codFiegDl, Date dataBolla, String tipoBolla, Integer posizioneRiga) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaVo.class, "bvo");
		criteria.add(Restrictions.eq("bvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("bvo.pk.dtBolla", dataBolla));
		criteria.add(Restrictions.eq("bvo.pk.tipoBolla", tipoBolla));
		criteria.add(Restrictions.eq("bvo.pk.posizioneRiga", posizioneRiga));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public BollaDettaglioVo getDettaglioBolla(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla, Integer posizioneRiga) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaDettaglioVo.class, "bvo");
		criteria.add(Restrictions.eq("bvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("bvo.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("bvo.pk.dtBolla", dataBolla));
		criteria.add(Restrictions.eq("bvo.pk.tipoBolla", tipoBolla));
		criteria.add(Restrictions.eq("bvo.pk.posizioneRiga", posizioneRiga));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public MessaggioVo getMessaggioRivenditaVo(Integer codFiegDl, Timestamp dtMessaggio, Integer tipoDestinatario, Integer destinatarioA, Integer destinatarioB) {
		return dao.findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_MESSAGGIO_RIVENDITA, codFiegDl, dtMessaggio, tipoDestinatario, destinatarioA, destinatarioB);
	}

	@Override
	public List<MessaggiBollaVo> getMessaggiBollaEdicola(final Integer codFiegDl, final Integer codEdicolaDl, final Timestamp dtBolla, final String tipo) {
		HibernateCallback<List<MessaggiBollaVo>> action = new HibernateCallback<List<MessaggiBollaVo>>() {
			@Override
			public List<MessaggiBollaVo> doInHibernate(Session session) throws HibernateException, SQLException {
				Query namedQuery = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_GET_MESSAGGI_BOLLA);
				namedQuery.setParameterList("codFiegDl", new Integer[]{codFiegDl});
				namedQuery.setParameterList("codEdicola", new Integer[]{codEdicolaDl});
				namedQuery.setTimestamp("dtBolla", dtBolla);
				namedQuery.setString("tipoBolla", tipo);
				return namedQuery.list();
			}
		};
		return dao.findByHibernateCallback(action);
	}

	@Override
	public ResaRiscontrataVo getResaRiscontrataVo(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla, Integer idtn) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ResaRiscontrataVo.class, "brvo");
		criteria.add(Restrictions.eq("brvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("brvo.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("brvo.pk.dtBolla", dataBolla));
		criteria.add(Restrictions.eq("brvo.pk.tipoBolla", tipoBolla));
		criteria.add(Restrictions.eq("brvo.pk.idtn", idtn));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public BollaResaVo getBollaResaVo(Integer codFiegDl, Date dataBolla, String tipoBolla, Integer numeroRiga) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaResaVo.class, "brvo");
		criteria.add(Restrictions.eq("brvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("brvo.pk.dtBolla", dataBolla));
		criteria.add(Restrictions.eq("brvo.pk.tipoBolla", tipoBolla));
		criteria.add(Restrictions.eq("brvo.pk.posizioneRiga", numeroRiga));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public DecodificaRichiamiResaVo getRichiamoResa(Integer codFiegDl, Integer tipoRichiamoResa) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DecodificaRichiamiResaVo.class, "brvo");
		criteria.add(Restrictions.eq("brvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("brvo.pk.tipoRichiamoResa", tipoRichiamoResa));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public List<BollaResaRiassuntoVo> getBollaResaRiassunto(final Integer[] codFiegDl, final Integer[] codEdicola, final Timestamp dtBolla, final String tipo) {
		HibernateCallback<List<BollaResaRiassuntoVo>> action = new HibernateCallback<List<BollaResaRiassuntoVo>>() {
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
		return dao.findByHibernateCallback(action);
	}

	@Override
	public BollaResaDettaglioVo getBollaResaDettaglioVo(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla, Integer idtn) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaResaDettaglioVo.class, "brvo");
		criteria.add(Restrictions.eq("brvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("brvo.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("brvo.pk.dtBolla", dataBolla));
		criteria.add(Restrictions.eq("brvo.pk.tipoBolla", tipoBolla));
		criteria.add(Restrictions.eq("brvo.pk.posizioneRiga", idtn));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public RichiestaRifornimentoVo getRichiestaRifornimento(Integer codFiegDl, Integer codEdicolaWeb, Integer idtn, Integer ordine) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaRifornimentoVo.class, "eced");
		criteria.add(Restrictions.eq("eced.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("eced.pk.codEdicola", codEdicolaWeb));
		criteria.add(Restrictions.eq("eced.pk.idtn", idtn));
		criteria.add(Restrictions.eq("eced.ordine", ordine));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}

	@Override
	public BollaStatisticaStoricoVo getBollaStatisticaStoricoVo(Integer codFiegDl, Integer codEdicola, Integer idtn) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaStatisticaStoricoVo.class, "brvo");
		criteria.add(Restrictions.eq("brvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("brvo.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("brvo.pk.idtn", idtn));
		return dao.findUniqueResultByDetachedCriteria(criteria);
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
		Integer lastRigaBolla = dao.findUniqueResultObjectByDetachedCriteria(criteria);
		lastRigaBolla = (lastRigaBolla == null ? 0 : lastRigaBolla);
		return lastRigaBolla;
	}

	@Override
	public BollaRiassuntoVo getBollaRiassunto(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipo) {
		return dao.findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_BOLLA_RIASSUNTO_BY_ID, codFiegDl, codEdicola, dtBolla, tipo);
	}
	
	@Override
	public PeriodicitaVo getPeriodicita(Integer tipoOperazione, Integer codPeriodicita) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PeriodicitaVo.class, "per");
		criteria.add(Restrictions.eq("per.pk.tipo", tipoOperazione));
		criteria.add(Restrictions.eq("per.pk.periodicita", codPeriodicita));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public List<RichiestaRifornimentoProdottiDto> getRichiesteRifornimentoDaEvadere(boolean soloRichiesteDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiRichiesteRifornimentoDettaglioVo.class, "rr");
		criteria.createCriteria("rr.richiesteRifornimento", "rf");
		criteria.createCriteria("rf.fornitore", "fo");
		criteria.createCriteria("rr.prodotto", "pr");
		criteria.createCriteria("rr.edicola", "ed");
		criteria.add(Restrictions.eq("rr.statoEsportazione", JmsConstants.COD_STATO_MESSAGGIO_INVIATO_SENZA_CONFERMA_RICEZIONE));
		if (soloRichiesteDl) {
			DetachedCriteria subCriteria = DetachedCriteria.forClass(AnagraficaAgenziaVo.class, "aa");
			subCriteria.setProjection(Projections.property("aa.codFiegDl"));
			List<Integer> listCoddl = dao.findObjectByDetachedCriteria(subCriteria);
			criteria.add(Restrictions.in("fo.pk.codFornitore", listCoddl));
		}
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("rr.pk.codProdottoInterno"), "codProdottoInterno");
		properties.add(Projections.property("rr.pk.codRichiestaRifornimento"), "codRichiestaRifornimento");
		properties.add(Projections.property("pr.codiceProdottoFornitore"), "codiceProdottoFornitore");
		properties.add(Projections.property("rf.dataRichiesta"), "dataRichiesta");
		properties.add(Projections.property("rr.quatitaRichiesta"), "quatitaRichiesta");
		properties.add(Projections.property("ed.codEdicolaDl"), "codEdicolaDl");
		properties.add(Projections.property("fo.pk.codFornitore"), "codFornitore");
		properties.add(Projections.property("ed.codiceContabileCliente"), "codiceContabileCliente");
		properties.add(Projections.property("pr.formazionePacco"), "formazionePacco");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(RichiestaRifornimentoProdottiDto.class));
		return dao.findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<BollaResaProdottiNonEditorialiDto> getReseProdottiVariDaInviare() {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiBollaDettaglioVo.class, "rr");
		criteria.createCriteria("rr.bolla", "re");
		criteria.createCriteria("re.fornitore", "fo");
		criteria.createCriteria("rr.prodotto", "pr");
		criteria.createCriteria("re.edicola", "ed");
		criteria.add(Restrictions.eq("rr.statoEsportazione", JmsConstants.COD_STATO_MESSAGGIO_INVIATO_SENZA_CONFERMA_RICEZIONE));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("re.idDocumento"), "idDocumento");
		properties.add(Projections.property("re.numeroDocumento"), "numeroDocumento");
		properties.add(Projections.property("pr.codProdottoInterno"), "codProdottoInterno");
		properties.add(Projections.property("pr.codiceProdottoFornitore"), "codiceProdottoFornitore");
		properties.add(Projections.property("re.dataRegistrazione"), "dataRegistrazione");
		properties.add(Projections.property("rr.quantita"), "quatitaRichiesta");
		properties.add(Projections.property("ed.codEdicolaDl"), "codEdicolaDl");
		properties.add(Projections.property("fo.pk.codFornitore"), "codFornitore");
		properties.add(Projections.property("ed.codiceContabileCliente"), "codiceContabileCliente");
		properties.add(Projections.property("pr.formazionePacco"), "formazionePacco");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(BollaResaProdottiNonEditorialiDto.class));
		return dao.findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public void updateEsportazioneRichiestaRifornimentoTemp() {
		HibernateCallback<?> action = new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query createQuery = session.createQuery("select vo.pk from ProdottiNonEditorialiRichiesteRifornimentoDettaglioVo vo where vo.stato = :stato and vo.statoEsportazione is null");
				createQuery.setString("stato", IGerivConstants.STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_INSERITO);
				createQuery.setMaxResults(100);
				List<ProdottiNonEditorialiRichiesteRifornimentoDettaglioPk> list = (List<ProdottiNonEditorialiRichiesteRifornimentoDettaglioPk>) createQuery.list();
				Timestamp sysdate = getSysdate();
				for (ProdottiNonEditorialiRichiesteRifornimentoDettaglioPk pk : list) {
					Query updateQuery = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_UPDATE_RICHIESTE_RIFORNIMENTO_TEMP);
					updateQuery.setInteger("statoEsportazione", JmsConstants.COD_STATO_MESSAGGIO_INVIATO_SENZA_CONFERMA_RICEZIONE);
					updateQuery.setTimestamp("dataEsportazioneVersoDl", sysdate);
					updateQuery.setLong("codRichiestaRifornimento", pk.getCodRichiestaRifornimento());
					updateQuery.setLong("codProdottoInterno", pk.getCodProdottoInterno());
					updateQuery.executeUpdate();
					Query updateQuery1 = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_UPDATE_RICHIESTE_RIFORNIMENTO_DATA_INVIO);
					updateQuery1.setTimestamp("dataInvioRichiestaDl", sysdate);
					updateQuery1.setLong("codRichiestaRifornimento", pk.getCodRichiestaRifornimento());
					updateQuery1.executeUpdate();
				}
				return null;
			}
		};
		dao.executeByHibernateCallback(action);
	}
	
	@Override
	public void updateEsportazioneResaTemp() {
		HibernateCallback<?> action = new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query createQuery = session.createQuery("select vo.pk from ProdottiNonEditorialiBollaDettaglioVo vo where vo.causale.codiceCausale = " + IGerivConstants.CODICE_CAUSALE_RESA + " and vo.statoEsportazione = " + JmsConstants.COD_STATO_MESSAGGIO_PRONTO_PER_INVIO);
				createQuery.setMaxResults(100);
				List<ProdottiNonEditorialiBollaDettaglioPk> list = (List<ProdottiNonEditorialiBollaDettaglioPk>) createQuery.list();
				Timestamp sysdate = getSysdate();
				for (ProdottiNonEditorialiBollaDettaglioPk pk : list) {
					Query updateQuery = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_UPDATE_RESA_TEMP);
					updateQuery.setInteger("statoEsportazione", JmsConstants.COD_STATO_MESSAGGIO_INVIATO_SENZA_CONFERMA_RICEZIONE);
					updateQuery.setTimestamp("dataEsportazioneVersoDl", sysdate);
					updateQuery.setLong("idDocumento", pk.getIdDocumento());
					updateQuery.setLong("progressivo", pk.getProgressivo());
					updateQuery.executeUpdate();
				}
				return null;
			}
		};
		dao.executeByHibernateCallback(action);
	}
	
	@Override
	public void updateEsportazioneRichiestaRifornimentoSuccess(String correlationId) {
		dao.bulkUpdate("update ProdottiNonEditorialiRichiesteRifornimentoDettaglioVo vo set vo.statoEsportazione = ?, vo.correlationId = ? where vo.statoEsportazione = ?", 
				new Object[]{JmsConstants.COD_STATO_MESSAGGIO_INVIATO_CON_CONFERMA_RICEZIONE, correlationId, JmsConstants.COD_STATO_MESSAGGIO_INVIATO_SENZA_CONFERMA_RICEZIONE});
	}
	
	@Override
	public void updateEsportazioneRichiestaRifornimentoError(String correlationId) {
		dao.bulkUpdate("update ProdottiNonEditorialiRichiesteRifornimentoDettaglioVo vo set vo.statoEsportazione = ?, vo.correlationId = ? where vo.statoEsportazione = ?", 
				new Object[]{JmsConstants.COD_STATO_MESSAGGIO_ERRORE, correlationId, JmsConstants.COD_STATO_MESSAGGIO_INVIATO_SENZA_CONFERMA_RICEZIONE});
	}
	
	@Override
	public void updateEsportazioneResaSuccessDettagli(String correlationId) {
		dao.bulkUpdate("update ProdottiNonEditorialiBollaDettaglioVo vo set vo.statoEsportazione = ?, vo.correlationId = ? where vo.statoEsportazione = ?", 
				new Object[]{JmsConstants.COD_STATO_MESSAGGIO_INVIATO_CON_CONFERMA_RICEZIONE, correlationId, JmsConstants.COD_STATO_MESSAGGIO_INVIATO_SENZA_CONFERMA_RICEZIONE});
	}
	
	@Override
	public void updateEsportazioneResaErrorDettagli(String correlationId) {
		dao.bulkUpdate("update ProdottiNonEditorialiBollaDettaglioVo vo set vo.statoEsportazione = ?, vo.correlationId = ? where vo.statoEsportazione = ?", 
				new Object[]{JmsConstants.COD_STATO_MESSAGGIO_ERRORE, correlationId, JmsConstants.COD_STATO_MESSAGGIO_INVIATO_SENZA_CONFERMA_RICEZIONE});
	}
	
	@Override
	public void updateEsportazioneResaSuccessTesta(final List<Long> listIdDocumento) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery("update ProdottiNonEditorialiBollaVo vo set vo.inviatoAlDl = :inviatoAlDl where vo.idDocumento in (:listIdDocumento)");
				query.setBoolean("inviatoAlDl", true);
				query.setParameterList("listIdDocumento", listIdDocumento);
				return query.executeUpdate();
			}
		};
		dao.executeByHibernateCallback(action);
	}
	
	@Override
	public void updateEsportazioneResaErrorTesta(final List<Long> listIdDocumento) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery("update ProdottiNonEditorialiBollaVo vo set vo.inviatoAlDl = :inviatoAlDl where vo.idDocumento in (:listIdDocumento)");
				query.setBoolean("inviatoAlDl", false);
				query.setParameterList("listIdDocumento", listIdDocumento);
				return query.executeUpdate();
			}
		};
		dao.executeByHibernateCallback(action);
	}
	
	@Override
	public void updateRichiestaRifornimentoEvasa(Long codRichiestaRifornimento, Long codProdottoInterno, String stato, Integer quatitaEvasa, String note) {
		dao.bulkUpdate("update ProdottiNonEditorialiRichiesteRifornimentoDettaglioVo vo set stato = ?, vo.quatitaEvasa = ?, vo.note = ? where vo.pk.codRichiestaRifornimento = ? and vo.pk.codProdottoInterno = ?", 
				new Object[]{stato, quatitaEvasa, note, codRichiestaRifornimento, codProdottoInterno});
	}
	
	@Override
	public List<EdicolaDto> getEdicoleAttive(Integer coddl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AbbinamentoEdicolaDlVo.class, "ab");
		criteria.createCriteria("ab.anagraficaEdicolaVo", "ae");
		criteria.createCriteria("ab.anagraficaAgenziaVo", "aa");
		criteria.add(Restrictions.eq("aa.codFiegDl", coddl));
		criteria.add(Restrictions.isNull("ab.dtSospensioneEdicola"));
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("ab.codDpeWebEdicola"), "codEdicolaWeb");
		properties.add(Projections.property("ab.codiceContabileCliente"), "codiceContabileCliente");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(EdicolaDto.class));
		return dao.findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<EdicolaDto> getEdicoleInforiv() {
		DetachedCriteria criteria = DetachedCriteria.forClass(AbbinamentoEdicolaDlVo.class, "ab");
		criteria.createCriteria("ab.anagraficaEdicolaVo", "ae");
		criteria.createCriteria("ab.anagraficaAgenziaVo", "aa");
		criteria.add(Restrictions.or(Restrictions.eq("ab.edicolaIGerivInforiv", true), Restrictions.eq("ab.edicolaIGerivNet", true)));
		criteria.add(Restrictions.or(Restrictions.isNull("ab.dtSospensioneEdicola"), Restrictions.ge("ab.dtSospensioneEdicola", getSysdate())));
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
		properties.add(Projections.property("ae.email"), "email");
		properties.add(Projections.property("ab.userFtp"), "ftpUser");
		properties.add(Projections.property("ab.pwdFtp"), "ftpPwd");
		properties.add(Projections.property("ab.hostFtp"), "ftpHost");
		properties.add(Projections.property("ab.hostFtp"), "ftpHost");
		properties.add(Projections.property("ab.edicolaIGerivInforiv"), "edicolaIGerivInforiv");
		properties.add(Projections.property("ab.edicolaIGerivNet"), "edicolaIGerivNet");
		properties.add(Projections.property("ab.deleteInforivFtpFileAfterDownload"), "deleteInforivFtpFileAfterDownload");
		properties.add(Projections.property("ab.remoteFtpDir"), "remoteFtpDir");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(EdicolaDto.class));
		return dao.findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public LocalitaVo getLocalita(String localita) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LocalitaVo.class, "loc");
		criteria.add(Restrictions.ilike("loc.descrizione", localita));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public ProvinciaVo getProvincia(String provincia) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProvinciaVo.class, "pr");
		criteria.add(Restrictions.eq("pr.descrizione", provincia.toUpperCase()));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public ProdottiNonEditorialiPrezziAcquistoVo getProdottiNonEditorialiPrezziAcquistoVo(Integer codEdicola, Integer codFornitore, String codiceProdottoFornitore) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiPrezziAcquistoVo.class, "prod");
		criteria.createCriteria("prod.fornitore", "fo");
		criteria.add(Restrictions.eq("prod.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("prod.pk.codiceProdottoFornitore", codiceProdottoFornitore));
		criteria.add(Restrictions.eq("fo.pk.codFornitore", codFornitore));
		criteria.add(Restrictions.eq("fo.pk.codEdicola", codEdicola));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public ProdottiNonEditorialiGiacenzeVo getGiacenza(Integer codFornitore, String codProdottoFornitore) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiGiacenzeVo.class, "giac");
		criteria.add(Restrictions.eq("giac.pk.codDl", codFornitore));
		criteria.add(Restrictions.eq("giac.pk.codProdottoFornitore", codProdottoFornitore));
		criteria.add(Restrictions.eq("giac.pk.numeroMagazzino", IGerivConstants.COD_MAGAZZINO_INTERNO));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public EmailRivenditaVo getEmailRivenditaVo(Integer idEmailRivendita) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EmailRivenditaVo.class, "em");
		criteria.add(Restrictions.eq("em.idEmailRivendita", idEmailRivendita));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public InforivFtpFileVo getInforivFtpFile(String nomeFile) {
		DetachedCriteria criteria = DetachedCriteria.forClass(InforivFtpFileVo.class, "inf");
		// 13/10/2016 eq invece di ilike - Timeout delle connessioni edismart 
		// Abbiamo effettuato il backup della tabella tbl_9760 e cancellato i 32000 circa record presenti (full table scan) 
		criteria.add(Restrictions.eq("inf.fileName", nomeFile));
		//criteria.add(Restrictions.ilike("inf.fileName", nomeFile));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public ArgomentoVo getArgomentoVo(Integer codDl, Integer segmento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArgomentoVo.class, "ar");
		criteria.add(Restrictions.eq("ar.pk.codDl", codDl));
		criteria.add(Restrictions.eq("ar.pk.segmento", segmento));
		return dao.findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public List<BollaRiassuntoVo> getTestaBolleConsegna(Integer codFiegDl, Integer codEdicola, Integer giorni) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaRiassuntoVo.class, "br");
		criteria.add(Restrictions.eq("br.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("br.pk.codEdicola", codEdicola));
		if (giorni != null) {
			Date date = DateUtilities.togliGiorni(new Date(), giorni);
			criteria.add(Restrictions.ge("br.pk.dtBolla", date));
		}
		return dao.findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<Long> getListIdDocumentoBollaDettaglioFromCorrelationId(String correlationId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiBollaDettaglioVo.class, "bd");
		criteria.add(Restrictions.eq("bd.correlationId", correlationId));
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.distinct(Projections.projectionList().add(Projections.property("bd.pk.idDocumento"))));
		criteria.setProjection(properties); 
		return dao.findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public void saveMessaggioWithBlob(final MessaggioVo vo, final byte[] messaggio) {
		HibernateCallback<Void> action = new HibernateCallback<Void>() {
			@Override
			public Void doInHibernate(Session session) throws HibernateException, SQLException {
				Blob blob = null;
				if (messaggio != null) {
					blob = Hibernate.getLobCreator(session).createBlob(messaggio);
					vo.setMessaggioEsteso(blob);
				}
				session.saveOrUpdate(vo);
				return null;
			}
		};
		dao.executeByHibernateCallback(action);
	}

	@Override
	public void callStoredProcedure_ManutenzionePubblicazioni(final Integer codFiegDl) {
		HibernateCallback<Void> action = new HibernateCallback<Void>() {
			@Override
			public Void doInHibernate(Session session) throws HibernateException, SQLException {
				//session.doReturningWork(new ManutenzionePubblicazioniStoredProcedureWork(codFiegDl));
				Query query = session.createSQLQuery(IGerivQueryContants.SQL_STORED_PROCEDURE_MANUTENZIONE_PUBBLICAZIONI);
				query.setInteger(0,codFiegDl);
				query.executeUpdate();
				return null;
				}
		};
		dao.executeByHibernateCallback(action);
		
	}
	
	/**
	 * Classe interna per l'esecuzione della procedure. 
	 */
	private class ManutenzionePubblicazioniStoredProcedureWork implements ReturningWork<Void>{
		private Integer codDl;

		public ManutenzionePubblicazioniStoredProcedureWork(Integer codDl) {
			this.codDl = codDl;
		}

		@Override
		public Void execute(Connection connection) throws SQLException {
			CallableStatement call = null;
			
				try {
					call = connection.prepareCall(IGerivQueryContants.SQL_STORED_PROCEDURE_MANUTENZIONE_PUBBLICAZIONI);
					C3P0NativeJdbcExtractor cp30NativeJdbcExtractor = new C3P0NativeJdbcExtractor();
		            OracleConnection conn = (OracleConnection) cp30NativeJdbcExtractor.getNativeConnection(connection);
					call.setLong(1, codDl);
					call.execute();
					
				} catch (SQLException e) {
					e.printStackTrace();
				}finally {
					if (call != null) {
						call.close();
						call = null;
					}
				}
				return null;
		}
		
	}
	
	@Override
	public List<RichiestaFissaClienteEdicolaVo> getRichiesteFisseClienti(Integer codFiegDl, Integer codEdicola, Integer codPubblicazione, Integer idTestataNumero, Timestamp data) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaFissaClienteEdicolaVo.class, "pr");
		criteria.add(Restrictions.eq("pr.pk.codDl", codFiegDl));
		criteria.add(Restrictions.eq("pr.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("pr.pk.codicePubblicazione", codPubblicazione));
		criteria.add(Restrictions.gt("pr.quantitaRichiesta", 0));
		criteria.add(Restrictions.or(Restrictions.gt("pr.numeroVolte",0), Restrictions.eq("pr.numeroVolte", -1)));
		criteria.createCriteria("pr.clienteEdicolaVo", "cl");
		criteria.add(Restrictions.or(Restrictions.isNull("cl.dtSospensionePrenotazioniDa"), 
				Restrictions.lt("cl.dtSospensionePrenotazioniDa",data), 
				Restrictions.gt("cl.dtSospensionePrenotazioniA", data)));

		DetachedCriteria subCriteria = DetachedCriteria.forClass(RichiestaClienteVo.class, "prd");
		subCriteria.add(Property.forName("prd.pk.codEdicola").eqProperty("pr.pk.codEdicola"));
		subCriteria.add(Property.forName("prd.pk.codCliente").eqProperty("pr.pk.codCliente"));
		subCriteria.add(Restrictions.eq("prd.pk.idtn", idTestataNumero));

		criteria.add(Subqueries.notExists(subCriteria.setProjection(Projections.property("prd.pk.codEdicola"))));
		return dao.findByDetachedCriteria(criteria);
	}
}
