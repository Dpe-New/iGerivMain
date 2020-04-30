package it.dpe.igeriv.importer;

import static ch.lambdaj.Lambda.join;
import it.dpe.igeriv.bo.batch.IGerivImportService;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.FileUtils;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.EstrattoContoEdicolaPdfVo;
import it.dpe.igeriv.vo.FattureEdicolaPdfVo;
import it.dpe.igeriv.vo.pk.EstrattoContoEdicolaPdfPk;
import it.dpe.igeriv.vo.pk.FattureEdicolaPdfPk;
import it.dpe.mail.MailingListService;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Classe per l'importazione dei file pdf degli estratti conto edicola.
 * 
 * @author romanom
 */
@Component("estrattoContoPdfFileImporter")
public class EstrattoContoPdfFileImporter {
	private final Logger log = Logger.getLogger(getClass());
	@Autowired
	private IGerivImportService iGerivImportBo;
	@Autowired
	private MailingListService mailingListService;
	@Value("${path.estratto.conto.pdf.zip.tmp}")
	private String tempDir;
	@Value("${path.estratto.conto.pdf.zip.bkp}")
	private String backupDir;
	@Value("${path.estratto.conto.pdf}")
	private String targetDir;
	
	/**
	 * Import i file zippati con gli estratti conto pdf delle edicole
	 * 
	 * @param File file
	 * @throws IOException
	 * @throws JAXBException
	 */
	public synchronized void importaFile(File file) throws Exception {
		if (file.exists() && file.isFile() && file.canWrite()) {
			log.info("Iniziando importazione file " + file.getName());
			File bkpDir = new File(backupDir); 
			try {
				if (!bkpDir.isDirectory()) {
					bkpDir.mkdirs();
				}
				String[] params = file.getName().replace(".zip", "").replace(".ZIP", "").split("_");
				if (params.length < 3 || !NumberUtils.isNumber(params[1]) || !NumberUtils.isNumber(params[2])) {
					throw new IGerivBusinessException(IGerivMessageBundle.get("imp.ec.zip.file.name.format.error"), file.getName());
				}
				Integer coddl = new Integer(params[1]);
				Date dataEc = new Date(DateUtilities.parseDate(params[2], DateUtilities.FORMATO_DATA_YYYYMMDD).getTime());
				AnagraficaAgenziaVo vo = iGerivImportBo.getAgenziaByCodiceDlWeb(coddl);
				if (vo == null) {
					throw new IGerivBusinessException(MessageFormat.format(IGerivMessageBundle.get("msg.dl.non.esiste"), coddl));
			    } else if (vo.getDataInizioEstrattoContoPdf() == null) {
			    	vo.setDataInizioEstrattoContoPdf(dataEc);
			    	iGerivImportBo.saveBaseVo(vo);
			    }
				coddl = vo.getCodFiegDl();
				List<String> listBusinessErrors = new ArrayList<String>();
				log.info("Unzippando file : " + file.getName());
				unzipFile(file, coddl, dataEc, listBusinessErrors);
				FileUtils.copyFile(file, new File(bkpDir, file.getName()));
				file.delete();
				if (!listBusinessErrors.isEmpty()) {
					throw new IGerivBusinessException(join(listBusinessErrors), ""); 
				}
	        } catch (IOException e) {
	        	// Non cancello il file perchè potrebbe essere un errore causato dal file non ancora completo
	        	log.error("Errore in EstrattoContoPdfFileImporter.importaFile file : " + (file != null ? file.getName() : ""), e);
	        	String subject = IGerivMessageBundle.get("imp.ftp.zip.pdf.error.subject");
	    		String msgObject = MessageFormat.format(IGerivMessageBundle.get("imp.ftp.zip.pdf.error.message.file"), file.getName(), e.getMessage(), StringUtility.getStackTrace(e));
	    		try {
	    			mailingListService.sendEmailWithAttachment(null, subject, msgObject, null, true, null, true, null);
	    		} catch (Throwable e1) {
	    			log.error("Errore invio email in EstrattoContoPdfFileImporter.importaFile : ", e1);
	    		}
	        } catch (Throwable e) {
	        	log.error("Errore in EstrattoContoPdfFileImporter.importaFile file : " + (file != null ? file.getName() : ""), e);
	        	String subject = IGerivMessageBundle.get("imp.ftp.zip.pdf.error.subject");
	    		String msgObject = MessageFormat.format(IGerivMessageBundle.get("imp.ftp.zip.pdf.error.message.file"), file.getName(), e.getMessage(), StringUtility.getStackTrace(e));
	    		try {
	    			mailingListService.sendEmailWithAttachment(null, subject, msgObject, null, true, null, true, null);
	    		} catch (Throwable e1) {
	    			log.error("Errore invio email in EstrattoContoPdfFileImporter.importaFile : ", e1);
	    		}
	    		FileUtils.copyFile(file, new File(bkpDir, file.getName()));
				file.delete();
	        }
		}
	}
	
	/**
	 * Decomprime i file pdf nelle cartelle di destinazione
	 * 
	 * @param File file
	 * @param coddl 
	 * @param dataEc 
	 * @param listErrors 
	 * @throws IOException
	 */
	private void unzipFile(File file, Integer coddl, Date dataEc, List<String> listErrors) throws IOException {
		ZipFile zipfile = null;
		try {
			zipfile = new ZipFile(file);
			for (Enumeration<? extends ZipEntry> e = zipfile.entries(); e.hasMoreElements(); ) {
			    ZipEntry entry = (ZipEntry) e.nextElement();
			    String pdfFileName = entry.getName();
			    if (pdfFileName.toUpperCase().startsWith("EC_")) {
				    String[] params = pdfFileName.replace(".pdf", "").replace(".PDF", "").split("_");
				    if (params.length < 4 || !NumberUtils.isNumber(params[3])) {
				    	listErrors.add(MessageFormat.format(IGerivMessageBundle.get("imp.ec.pdf.file.name.format.error"), file.getName()) + System.getProperty("line.separator"));
				    	continue;
					}
					Integer codRivDl = new Integer(params[3]);
				    EdicolaDto edicolaByCodRivDl = iGerivImportBo.getEdicolaByCodRivDl(coddl, codRivDl);
				    if (edicolaByCodRivDl == null) {
				    	listErrors.add(MessageFormat.format(IGerivMessageBundle.get("msg.rivendita.non.esiste"), codRivDl, coddl) + System.getProperty("line.separator"));
				    	continue;
				    }
					Integer codEdicola = edicolaByCodRivDl.getCodEdicolaWeb();
				    File outputDir = new File(targetDir + System.getProperty("file.separator") + coddl + System.getProperty("file.separator") + codEdicola);
				    if (!outputDir.isDirectory()) {
				    	outputDir.mkdirs();
				    }
					FileUtils.unzipEntry(file, zipfile, entry, outputDir, false);
					insertEstrattoContoEdicolaPdfVo(codEdicola, coddl, dataEc, pdfFileName);
					
			    } else if (pdfFileName.toUpperCase().startsWith("FAT_")) {
				    String[] params = pdfFileName.replace(".pdf", "").replace(".PDF", "").split("_");
				    if (params.length < 5 || !NumberUtils.isNumber(params[3]) || !NumberUtils.isNumber(params[4])) {
				    	listErrors.add(MessageFormat.format(IGerivMessageBundle.get("imp.ec.pdf.file.name.format.error"), file.getName()) + System.getProperty("line.separator"));
				    	continue;
					}
					Integer codRivDl = new Integer(params[3]);
				    EdicolaDto edicolaByCodRivDl = iGerivImportBo.getEdicolaByCodRivDl(coddl, codRivDl);
				    if (edicolaByCodRivDl == null) {
				    	listErrors.add(MessageFormat.format(IGerivMessageBundle.get("msg.rivendita.non.esiste"), codRivDl, coddl) + System.getProperty("line.separator"));
				    	continue;
				    }
					Integer codEdicola = edicolaByCodRivDl.getCodEdicolaWeb();
					Integer numeroFattura = new Integer(params[4]);
				    File outputDir = new File(targetDir + System.getProperty("file.separator") + coddl + System.getProperty("file.separator") + codEdicola);
				    if (!outputDir.isDirectory()) {
				    	outputDir.mkdirs();
				    }
					FileUtils.unzipEntry(file, zipfile, entry, outputDir, false);
					insertFatturaEdicolaPdfVo(codEdicola, coddl, dataEc, numeroFattura, pdfFileName);
			    }
			}
		} finally {
			if (zipfile != null) {
				try {
					zipfile.close();
				} catch (IOException e) {
					log.error("Error closing zipfile " + (zipfile.getName() != null ? zipfile.getName() : ""));
				}
			}
		}
	}
	
	/**
	 * Inserisce il nome del file nella tabella degli estratti conto pdf
	 * 
	 * @param codEdicola 
	 * @param coddl 
	 * @param dataEc 
	 * @param fileName 
	 */
	private void insertEstrattoContoEdicolaPdfVo(Integer codEdicola, Integer coddl, Date dataEc, String fileName) {
		EstrattoContoEdicolaPdfVo vo = new EstrattoContoEdicolaPdfVo();
		EstrattoContoEdicolaPdfPk pk = new EstrattoContoEdicolaPdfPk();
		pk.setCodEdicola(codEdicola);
		pk.setCodFiegDl(coddl);
		pk.setDataEstrattoConto(dataEc);
		vo.setPk(pk);
		vo.setNomeFile(fileName);
		iGerivImportBo.saveBaseVo(vo);
	}
	
	/**
	 * Inserisce il nome del file nella tabella delle fatture pdf
	 * 
	 * @param codEdicola 
	 * @param coddl 
	 * @param dataFattura
	 * @param fileName 
	 */
	private void insertFatturaEdicolaPdfVo(Integer codEdicola, Integer coddl, Date dataFattura, Integer numeroFattura, String fileName) {
		FattureEdicolaPdfVo vo = new FattureEdicolaPdfVo();
		FattureEdicolaPdfPk pk = new FattureEdicolaPdfPk();
		pk.setCodEdicola(codEdicola);
		pk.setCodFiegDl(coddl);
		pk.setDataFattura(dataFattura);
		pk.setNumeroFattura(numeroFattura);
		vo.setPk(pk);
		vo.setNomeFile(fileName);
		iGerivImportBo.saveBaseVo(vo);
	}

	public IGerivImportService getiGerivImportBo() {
		return iGerivImportBo;
	}

	public void setiGerivImportBo(IGerivImportService iGerivImportBo) {
		this.iGerivImportBo = iGerivImportBo;
	}

	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

	public String getTempDir() {
		return tempDir;
	}

	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}

	public String getBackupDir() {
		return backupDir;
	}

	public void setBackupDir(String backupDir) {
		this.backupDir = backupDir;
	}

	public String getTargetDir() {
		return targetDir;
	}

	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}

}
