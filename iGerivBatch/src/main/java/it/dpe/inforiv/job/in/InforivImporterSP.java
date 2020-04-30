package it.dpe.inforiv.job.in;

import it.dpe.igeriv.bo.batch.IGerivImportService;
import it.dpe.igeriv.dto.ImportazioneFileDlResultDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.mail.MailingListService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * Classe per l'importazione delle immagini delle pubblicazioni iGeriv.
 * La configurazione in integrationContext.xml implementa un monitor sulla cartella di importazione. 
 * 
 * @author BassignaniV
 *
 */
@Component("inforivImporterSP")
public class InforivImporterSP {
	private final Logger log = Logger.getLogger(getClass());
	@Autowired
	private IGerivImportService iGerivImportBo;
	@Value("${inforiv.file.import.path.dir}")
	private String datiPathDir;
	@Value("${inforiv.bkp.file.import.path.dir}")
	private String datiPathDirBkp;
	@Value("${dl.path.import.log}")
	private String pathImportLog;
	@Value("${dl.file.name.import.inforiv.log}")
	private String fileNameImportLog;
	@Autowired
	private MailingListService mailingListService;
	
	/**
	 * Esegue l'importazione dei file delle bolle che vengono dai DL INFORIV.
	 * 
	 * @param File file
	 * @throws IOException
	 */
	public synchronized void importaFile(File file) throws IOException {
		if (file.exists() && file.isFile()) {
			log.info("Entered job InforivImporterSP with file " + file.getName());
			File datiDir = new File(datiPathDir);
			File datiBkpDir = new File(datiPathDirBkp);
			if (!datiDir.isDirectory()) {
				datiDir.mkdir();
			} 
			if (!datiBkpDir.isDirectory()) {
				datiBkpDir.mkdir();
			} 
			try {
				log.info("Reading file lines");
				List<String> list = readFileLines(file);
				log.info("Calling stored procedure " + IGerivQueryContants.SQL_QUERY_PROC_IMPORTA_FILE_INFORIV);
				ImportazioneFileDlResultDto importaFileDl = null;
				synchronized (this) {
					importaFileDl = iGerivImportBo.importaFileDlInforiv(file.getName(), list);
				}
				if (importaFileDl != null) {
					log.info("Writing log file " + pathImportLog + System.getProperty("file.separator") + fileNameImportLog);
					writeLogFile(importaFileDl.getLogContent(), fileNameImportLog);
					Long status = importaFileDl.getStatus();
					log.info("Return status " + status);
					if (status != null && status.equals(2l)) {
						log.info("Return error status " + status + " - throwing Exception");
						throw new Exception("");
					}
					log.info("Copying file " + file.getName() + " to " + datiBkpDir.getAbsolutePath());
					FileUtils.copyFile(file, new File(datiBkpDir.getAbsolutePath() + System.getProperty("file.separator") + file.getName()));
					log.info("Deleting file " + file.getName());
					file.delete();
				}
				log.info("Exiting job InforivImporterSP");
			} catch (Throwable e) {
				log.error(IGerivMessageBundle.get("msg.subject.errore.importazione.file.dl"), e);
				/*try {
					mailingListService.sendEmailWithAttachment(
						new String[]{"vittorio.bassignani@dpe.it","maurizio.costa@dpe.it"}, 
						IGerivMessageBundle.get("msg.subject.errore.importazione.file.dl"), 
						MessageFormat.format(IGerivMessageBundle.get("msg.errore.importazione.file.dl"), new Object[]{file.getAbsolutePath(), (datiPathDir + "/RICEZIONE_LOG.TXT"), StringUtility.getStackTrace(e)}),  
						null, true, null, true, null);
				} catch (Throwable e1) {}
				*/
				File dest = new File(datiDir + System.getProperty("file.separator") + file.getName() + "._ERR");
				file.renameTo(dest);
			} 
		}
	}

	/**
	 * Legge tutte le righe del file e le inserisce in un array di String da passare alla stored proc
	 * 
	 * @param File file
	 * @return List<String>
	 * @throws FileNotFoundException
	 */
	private List<String> readFileLines(File file) throws FileNotFoundException {
		List<String> list = new ArrayList<String>();
		Scanner scan = null;
		try {
			scan = new Scanner(file);
			if (scan != null) {
				while (scan.hasNext()) {
					String line = scan.nextLine();
					if (!Strings.isNullOrEmpty(line)) {
						list.add(line);
					}
				}
			}
		} finally {
			if (scan != null) {
				scan.close();
			}
		}
		return list;
	}
	
	/**
	 * Scrive le righe contenute in una lista di String in un file
	 * 
	 * @param List<String> logMsgs
	 * @param String fileName
	 * @throws IOException 
	 */
	private void writeLogFile(List<String> logMsgs, String fileName) throws IOException {
		FileOutputStream fos = null;
		OutputStreamWriter out = null;
		try {
			if (logMsgs != null && !logMsgs.isEmpty()) {
				fos = new FileOutputStream(pathImportLog + System.getProperty("file.separator") + fileName, true);
				out = new OutputStreamWriter(fos, "UTF-8"); 
				for (String line : logMsgs) {
		        	if (!Strings.isNullOrEmpty(line)) {
		        		out.append(line);
		        		out.append(System.getProperty("line.separator"));
		        	}
				} 
			}
		} finally {
	    	try {
	    		if (out != null) {
	    			out.close();
		    	}
		    	if (fos != null) {
		    		fos.close();
		    	}
	    	} catch (Throwable e) {
	    		log.error("Errore durante la chiusura del file " + pathImportLog + System.getProperty("file.separator") + fileName , e);
	    	}
	    }
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

	public String getDatiPathDir() {
		return datiPathDir;
	}

	public void setDatiPathDir(String datiPathDir) {
		this.datiPathDir = datiPathDir;
	}

	public String getDatiPathDirBkp() {
		return datiPathDirBkp;
	}

	public void setDatiPathDirBkp(String datiPathDirBkp) {
		this.datiPathDirBkp = datiPathDirBkp;
	}

	public String getPathImportLog() {
		return pathImportLog;
	}

	public void setPathImportLog(String pathImportLog) {
		this.pathImportLog = pathImportLog;
	}

	public String getFileNameImportLog() {
		return fileNameImportLog;
	}

	public void setFileNameImportLog(String fileNameImportLog) {
		this.fileNameImportLog = fileNameImportLog;
	}
	
}
