package it.dpe.igeriv.bo.batch;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.dto.ImportazioneFileDlResultDto;
import it.dpe.igeriv.dto.ItemResaDto;
import it.dpe.igeriv.dto.ResaEdicolaDto;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.exception.ImageImportException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.util.ImageWatermark;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.AnagraficaPubblicazioniVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.ImmaginePubblicazioneVo;
import it.dpe.igeriv.vo.LavorazioneResaImmagineVo;
import it.dpe.igeriv.vo.LavorazioneResaVo;
import it.dpe.igeriv.vo.RitiriRtaeVo;
import it.dpe.igeriv.vo.pk.LavorazioneResaImmaginePk;
import it.dpe.rtae.dto.RtaeRitiriEdicolaExportDto;
import it.dpe.rtae.dto.RtaeRitiroExportDto;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.nativejdbc.C3P0NativeJdbcExtractor;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import ch.lambdaj.group.Group;

/**
 * Implementazione dell'interfaccia di importazione dati per iGeriv. 
 * 
 * @author romanom
 * 
 */
@Repository("IGerivImportBo")
class IGerivImportBoImpl implements IGerivImportBo {
	private final BaseDao<?> dao;
	private final IGerivBatchBo iGerivBatchBo;
	private final Logger log = Logger.getLogger(getClass());
	
	@Autowired
	IGerivImportBoImpl(BaseDao<?> dao, IGerivBatchBo iGerivBatchBo) {
		this.dao = dao;
		this.iGerivBatchBo = iGerivBatchBo;
	}
	
	@Override
	public BaseVo saveBaseVo(BaseVo vo) {
		return dao.saveOrUpdate(vo);
	}
	
	@Override
	public void importRitiriRtae(Serializable object) throws IGerivBusinessException {
		if (object instanceof RtaeRitiroExportDto) {
			RtaeRitiroExportDto ritiri = (RtaeRitiroExportDto) object;
			for (RtaeRitiriEdicolaExportDto dto : ritiri.getListGiornaleRitiriDto()) {
				Long barcode = dto.getBarcode();
				Integer codFiegDl = dto.getCodFiegDl();
				String codiceEdizione = dto.getCodiceEdizione();
				Integer codiceRivenditaDl = dto.getCodiceRivenditaDl();
				Long copie = dto.getCopie();
				RitiriRtaeVo vo = dao.findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_ESPORTAZIONE_RTAE_BY_ID, ("" + barcode), codFiegDl, codiceRivenditaDl);
				if (vo == null) {
					vo = new RitiriRtaeVo();
					Integer codEdicola = iGerivBatchBo.getCodDpeWebEdicolaAbbinamentoEdicolaDlVo(codiceRivenditaDl, codFiegDl);
					if (codEdicola == null) {
						throw new IGerivBusinessException(MessageFormat.format(IGerivMessageBundle.get("msg.igeriv.rtae.cod.edicole.non.corrispondono"), new Object[] {"" + codiceRivenditaDl, "" + codFiegDl}));
					}
					vo.setCodEdicola(codEdicola);
					vo.setCodFiegDl(codFiegDl);
					vo.setCodiceRivenditaDl(codiceRivenditaDl);
					vo.setCodiceEdizione(codiceEdizione);
					vo.setCodiceBarre("" + barcode);
				} 
				copie += (vo.getCopieRitirate() != null) ? vo.getCopieRitirate() : 0;
				vo.setCopieRitirate(copie);
				dao.saveOrUpdate(vo);
			}
		}
	}

	@Override
	public void importLavorazioneResaRivendita(ResaEdicolaDto resaEdicolaDto) {
		AbbinamentoEdicolaDlVo abbinamentoEdicolaDlVo = iGerivBatchBo.getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(resaEdicolaDto.getCodiceDl(), resaEdicolaDto.getCodiceRivendita());
		if (abbinamentoEdicolaDlVo == null) {
			throw new IGerivRuntimeException(MessageFormat.format(IGerivMessageBundle.get("msg.rivendita.non.esiste"), resaEdicolaDto.getCodiceRivendita(), resaEdicolaDto.getCodiceDl()));
		}
		LavorazioneResaVo lrvo = iGerivBatchBo.getLavorazioneResaVo(resaEdicolaDto.getZipFile());
		if (lrvo == null) {
			lrvo = new LavorazioneResaVo();
			lrvo.setNomeFile(resaEdicolaDto.getZipFile());
		}
		lrvo.setAnagraficaEdicolaVo(abbinamentoEdicolaDlVo.getAnagraficaEdicolaVo());
		lrvo.setAnagraficaAgenziaVo(abbinamentoEdicolaDlVo.getAnagraficaAgenziaVo());
		lrvo.setDataResa(new Date(resaEdicolaDto.getDataResa().getTime()));
		lrvo.setTipoResa(resaEdicolaDto.getTipoResa());
		if (lrvo.getListLavorazioneResaImmagineVo() != null) {
			lrvo.getListLavorazioneResaImmagineVo().clear();
		} else {
			lrvo.setListLavorazioneResaImmagineVo(new ArrayList<LavorazioneResaImmagineVo>());
		}
		for (ItemResaDto irdto : resaEdicolaDto.getListItensResa()) {
			LavorazioneResaImmagineVo lrivo = new LavorazioneResaImmagineVo();
			LavorazioneResaImmaginePk pk = new LavorazioneResaImmaginePk();
			pk.setIdtn(irdto.getIdtn());
			pk.setDataOraLavorazione(new Timestamp(irdto.getDataOraLavorazione().getTime()));
			pk.setLavorazioneResaVo(lrvo);
			lrivo.setPk(pk);
			lrivo.setNomeImmagine(irdto.getNomeFile());
			lrivo.setCopie(irdto.getCopie());
			lrivo.setCodFiegDl(lrvo.getAnagraficaAgenziaVo().getCodFiegDl());
			lrvo.getListLavorazioneResaImmagineVo().add(lrivo);
		}
		iGerivBatchBo.mergeBaseVo(lrvo);
	}
	
	@Override
	public void importImmagine(String name) {
		log.info("image name lenght " + name.length() + "    " + name);
		
		if (name.length() > 8) {
			String barcode = name.substring(8, name.lastIndexOf("."));
			
			log.info("image barcode " + barcode + "    " + name);
			ImmaginePubblicazioneVo vo = iGerivBatchBo.getImmaginePubblicazione(barcode);
			if (vo == null) {
				vo = new ImmaginePubblicazioneVo();
				vo.setBarcode(barcode);
			}
			
			vo.setNome(name);
			log.info("image before save " + name);
			iGerivBatchBo.saveBaseVo(vo);
			log.info("image after save " + name);
		}
	}
	
	@Override
	public ImportazioneFileDlResultDto importaFileDl(final String fileName, final List<String> list) {
		HibernateCallback<ImportazioneFileDlResultDto> action = new HibernateCallback<ImportazioneFileDlResultDto>() {
			@Override
			public ImportazioneFileDlResultDto doInHibernate(Session session)
					throws HibernateException, SQLException {
		        ImportazioneFileDlResultDto doReturningWork = session.doReturningWork(new ReturningWork<ImportazioneFileDlResultDto>() {
					@Override
					public ImportazioneFileDlResultDto execute(Connection connection) throws SQLException {
						ImportazioneFileDlResultDto value = new ImportazioneFileDlResultDto();
						ARRAY array = null;
						ARRAY snagContentOut = null;
						ARRAY logContentOut = null;
						CallableStatement cs = null;
						try {
							C3P0NativeJdbcExtractor cp30NativeJdbcExtractor = new C3P0NativeJdbcExtractor();
							OracleConnection conn = (OracleConnection) cp30NativeJdbcExtractor.getNativeConnection(connection);
							ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor("ARRAY_VARCHAR", conn);
							array = new ARRAY(descriptor, conn, list.toArray(new String[] {}));
							snagContentOut = new ARRAY(descriptor, conn, new String[] {});
							logContentOut = new ARRAY(descriptor, conn, new String[] {});
							cs = conn.prepareCall(IGerivQueryContants.SQL_QUERY_PROC_IMPORTA_FILE); 
							cs.clearParameters(); 
							cs.setString(1, fileName);
					        cs.setArray(2, array);
					        cs.setString(3, "");
					        cs.setArray(4, snagContentOut);
					        cs.setArray(5, logContentOut);
					        cs.setInt(6, 0);
					        cs.registerOutParameter(3, OracleTypes.VARCHAR);
					        cs.registerOutParameter(4, OracleTypes.ARRAY, "ARRAY_VARCHAR");
					        cs.registerOutParameter(5, OracleTypes.ARRAY, "ARRAY_VARCHAR");
					        cs.registerOutParameter(6, OracleTypes.INTEGER);
					        cs.execute();
					        String snagFileName = cs.getString(3);
					        List<String> snagContent = cs.getArray(4) != null && cs.getArray(4).getArray() != null ? Arrays.asList((String[]) cs.getArray(4).getArray()) : null;
							List<String> logContent = cs.getArray(5) != null && cs.getArray(5).getArray() != null ? Arrays.asList((String[]) cs.getArray(5).getArray()) : null;
							long status = cs.getLong(6);
							value.setLogContent(logContent);
							value.setSnagContent(snagContent);
							value.setSnagFileName(snagFileName);
							value.setStatus(status);
						} finally {
					        if (cs != null) {
					        	cs.close();
					        	cs = null;
					        }
					        array = null;
					        snagContentOut = null;
					        logContentOut = null;
						}
						return value;
					}
				});
		        return doReturningWork;
			}
		};
		return dao.findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public ImportazioneFileDlResultDto importaFileDlInforiv(final String fileName, final List<String> list) {
		HibernateCallback<ImportazioneFileDlResultDto> action = new HibernateCallback<ImportazioneFileDlResultDto>() {
			@Override
			public ImportazioneFileDlResultDto doInHibernate(Session session)
					throws HibernateException, SQLException {
		        ImportazioneFileDlResultDto doReturningWork = session.doReturningWork(new ReturningWork<ImportazioneFileDlResultDto>() {
					@Override
					public ImportazioneFileDlResultDto execute(Connection connection) throws SQLException {
						ImportazioneFileDlResultDto value = new ImportazioneFileDlResultDto();
						ARRAY array = null;
						ARRAY logContentOut = null;
						CallableStatement cs = null;
						try {
							C3P0NativeJdbcExtractor cp30NativeJdbcExtractor = new C3P0NativeJdbcExtractor();
							OracleConnection conn = (OracleConnection) cp30NativeJdbcExtractor.getNativeConnection(connection);
							ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor("ARRAY_VARCHAR", conn);
							array = new ARRAY(descriptor, conn, list.toArray(new String[] {}));
							logContentOut = new ARRAY(descriptor, conn, new String[] {});
							cs = conn.prepareCall(IGerivQueryContants.SQL_QUERY_PROC_IMPORTA_FILE_INFORIV); 
							cs.clearParameters(); 
							cs.setString(1, fileName);
					        cs.setArray(2, array);
					        cs.setArray(3, logContentOut);
					        cs.setInt(4, 0);
					        cs.registerOutParameter(3, OracleTypes.ARRAY, "ARRAY_VARCHAR");
					        cs.registerOutParameter(4, OracleTypes.INTEGER);
					        cs.execute();
							List<String> logContent = cs.getArray(3) != null && cs.getArray(3).getArray() != null ? Arrays.asList((String[]) cs.getArray(3).getArray()) : null;
							long status = cs.getLong(4);
							value.setLogContent(logContent);
							value.setStatus(status);
						} finally {
					        if (cs != null) {
					        	cs.close();
					        	cs = null;
					        }
					        array = null;
					        logContentOut = null;
						}
						return value;
					}
				});
		        return doReturningWork;
			}
		};
		return dao.findUniqueResultByHibernateCallback(action);
	}

	@Override
	public void addWatermarkToImage(File file) throws IOException {
		BufferedImage bi = ImageIO.read(file);
		ImageWatermark.watermark(bi, "DPE srl");
		ImageIO.write(bi, "jpg", file);
	}
	
	@Override
	public void importImmagineMiniaturaQuotidiano(File fileImg, String titolo) throws IOException, ImageImportException {
		List<AnagraficaPubblicazioniVo> listAnagraficaPubblicazioniVo = iGerivBatchBo.getQuotidianoByTitolo(titolo);
		listAnagraficaPubblicazioniVo = filterListAnagraficaPubblicazioniVo(titolo, listAnagraficaPubblicazioniVo);
		if (listAnagraficaPubblicazioniVo != null) {
			for (AnagraficaPubblicazioniVo apvo : listAnagraficaPubblicazioniVo) {
				if (apvo.getCodInizioQuotidiano() != null && !apvo.getCodInizioQuotidiano().equals(0)) {
					List<AnagraficaPubblicazioniVo> listAnagraficaPubblicazione = iGerivBatchBo.getListAnagraficaPubblicazioneByCodQuotidiano(apvo.getPk().getCodFiegDl(), apvo.getCodInizioQuotidiano(), apvo.getCodFineQuotidiano());
					for (AnagraficaPubblicazioniVo vo : listAnagraficaPubblicazione) {
						vo.setImgMiniaturaName(StringUtility.stripPercentage(fileImg.getName()));
						iGerivBatchBo.saveBaseVo(vo);
					}
				} else {
					apvo.setImgMiniaturaName(StringUtility.stripPercentage(fileImg.getName()));
					iGerivBatchBo.saveBaseVo(apvo);
				}
			}
		} else {
			throw new ImageImportException("Impossibile importare immagine. Esistono più di 2 gruppi di quotidiani associati");
		}
	}
	
	@Override
	public void importImmagineMiniaturaPeriodico(File fileImg, String titolo) throws IOException {
		List<AnagraficaPubblicazioniVo> listAnagraficaPubblicazioniVo = iGerivBatchBo.getPeriodicoByTitolo(titolo);
		listAnagraficaPubblicazioniVo = filterListPeriodicoVo(titolo, listAnagraficaPubblicazioniVo);
		for (AnagraficaPubblicazioniVo apvo : listAnagraficaPubblicazioniVo) {
			if (apvo.getCodInizioQuotidiano() != null && !apvo.getCodInizioQuotidiano().equals(0)) {
				List<AnagraficaPubblicazioniVo> listAnagraficaPubblicazione = iGerivBatchBo.getListAnagraficaPubblicazioneByCodQuotidiano(apvo.getPk().getCodFiegDl(), apvo.getCodInizioQuotidiano(), apvo.getCodFineQuotidiano());
				for (AnagraficaPubblicazioniVo vo : listAnagraficaPubblicazione) {
					vo.setImgMiniaturaName(StringUtility.stripPercentage(fileImg.getName()));
					iGerivBatchBo.saveBaseVo(vo);
				}
			} else {
				apvo.setImgMiniaturaName(StringUtility.stripPercentage(fileImg.getName()));
				iGerivBatchBo.saveBaseVo(apvo);
			}
		}
	}
	
	@Override
	public AnagraficaAgenziaVo getAgenziaByCodice(Integer codFiegDl) {
		return dao.findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_ANAGRAFICA_AGENZIE_BY_ID, codFiegDl);
	}
	
	@Override
	public AnagraficaAgenziaVo getAgenziaByCodiceDlWeb(Integer codiceDlWeb) {
		return dao.findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_ANAGRAFICA_AGENZIE_BY_ID_WEB, codiceDlWeb);
	}
	
	@Override
	public EdicolaDto getEdicolaByCodRivDl(Integer coddl, Integer codRivDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AbbinamentoEdicolaDlVo.class, "ab");
		criteria.createCriteria("ab.anagraficaEdicolaVo", "ae");
		criteria.createCriteria("ab.anagraficaAgenziaVo", "aa");
		if (coddl != null) {
			criteria.add(Restrictions.eq("aa.codFiegDl", coddl));
		}
		if (codRivDl != null) {
			criteria.add(Restrictions.eq("ab.codEdicolaDl", codRivDl));
		} 
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
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(EdicolaDto.class));
		return dao.findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	/**
	 * Implementa un filtro basato sul titolo pubblicazione.
	 * 
	 * @param String titolo
	 * @param List<AnagraficaPubblicazioniVo> listAnagraficaPubblicazioniVo
	 * @return List<AnagraficaPubblicazioniVo>
	 */
	private List<AnagraficaPubblicazioniVo> filterListPeriodicoVo(String titolo, List<AnagraficaPubblicazioniVo> listAnagraficaPubblicazioniVo) {
		Group<AnagraficaPubblicazioniVo> group = group(listAnagraficaPubblicazioniVo, by(on(AnagraficaPubblicazioniVo.class).getPk().getCodicePubblicazione()), by(on(AnagraficaPubblicazioniVo.class).getPk().getCodFiegDl()));
		if (group.subgroups().size() == 1) {
			return group.subgroups().get(0).findAll(); 
		}
		List<AnagraficaPubblicazioniVo> retList = new ArrayList<AnagraficaPubblicazioniVo>();
		titolo = titolo.toUpperCase().replaceAll("%", " ").replaceAll("\\b\\s{2,}\\b", " ").replaceAll("[^a-zA-Z0-9 ]", "").trim();
		for (Group<AnagraficaPubblicazioniVo> subgroup : group.subgroups()) {
			List<AnagraficaPubblicazioniVo> findAll = subgroup.findAll();
			String tit = findAll.get(0).getTitolo();
			tit = tit.toUpperCase().replaceAll("\\b\\s{2,}\\b", " ").replaceAll("[^a-zA-Z0-9 ]", "").trim();
			if (titolo.equals(tit)) {
				retList.addAll(findAll);
			}
		}
		return retList;
	}

	/**
	 * Implementa un filtro basato sul titolo pubblicazione.
	 * 
	 * @param String titolo
	 * @param List<AnagraficaPubblicazioniVo> listAnagraficaPubblicazioniVo
	 * @return List<AnagraficaPubblicazioniVo>
	 */
	private List<AnagraficaPubblicazioniVo> filterListAnagraficaPubblicazioniVo(String titolo, List<AnagraficaPubblicazioniVo> listAnagraficaPubblicazioniVo) {
		Group<AnagraficaPubblicazioniVo> group = group(listAnagraficaPubblicazioniVo, by(on(AnagraficaPubblicazioniVo.class).getCodInizioQuotidiano()), by(on(AnagraficaPubblicazioniVo.class).getCodFineQuotidiano()));
		if (group.subgroups().size() == 1) {
			return group.subgroups().get(0).findAll(); 
		}
		List<AnagraficaPubblicazioniVo> retList = new ArrayList<AnagraficaPubblicazioniVo>();
		titolo = titolo.toUpperCase().replaceAll("%", " ").replaceAll("\\b\\s{2,}\\b", " ").replaceAll("[^a-zA-Z0-9 ]", "").trim();
		for (Group<AnagraficaPubblicazioniVo> subgroup : group.subgroups()) {
			List<AnagraficaPubblicazioniVo> findAll = subgroup.findAll();
			String tit = findAll.get(0).getTitolo();
			tit = tit.toUpperCase().replaceAll("\\b\\s{2,}\\b", " ").replaceAll("[^a-zA-Z0-9 ]", "").trim();
			if (StringUtils.countOccurrencesOf(tit, " ") > 1) {
				tit = tit.substring(0, tit.lastIndexOf(" "));
			}
			tit = togliGiorniSettimana(tit);
			if (tit.contains("NAZ")) {
				retList.addAll(findAll);
			} else if (titolo.equals(tit)) {
				retList.addAll(findAll);
			} 
		}
		return retList;
	}

	private String togliGiorniSettimana(String tit) {
		if (tit.contains(" LUN")) {
			tit = tit.substring(0, tit.indexOf(" LUN"));
		} else if (tit.contains(" MAR")) {
			tit = tit.substring(0, tit.indexOf(" MAR"));
		} else if (tit.contains(" MER")) {
			tit = tit.substring(0, tit.indexOf(" MER"));
		} else if (tit.contains(" GIO")) {
			tit = tit.substring(0, tit.indexOf(" GIO"));
		} else if (tit.contains(" VEN")) {
			tit = tit.substring(0, tit.indexOf(" VEN"));
		} else if (tit.contains(" SAB")) {
			tit = tit.substring(0, tit.indexOf(" SAB"));
		} else if (tit.contains(" DOM")) {
			tit = tit.substring(0, tit.indexOf(" DOM"));
		} else if (tit.contains(" FES")) {
			tit = tit.substring(0, tit.indexOf(" FES"));
		}
		return tit;
	}
	
}
