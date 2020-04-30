package it.dpe.inforiv.ftp.importer;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.FileUtils;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.mail.MailingListService;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Questa classe viene chiamata alla ricezione di un file .zip 
 * dal DL Inforiv/Oliged nella cartella ${dl.path.dir.dati.inforiv.in}.
 * 
 * @author mromano
 *
 */
@Component("IGerivInforivDlZipFtpImporter")
public class IGerivInforivDlZipFtpImporter {
	private final Logger log = Logger.getLogger(getClass());
	@Autowired
	private MailingListService mailingListService;
	@Value("${inforiv.file.import.path.dir}")
	private String inputFileInforivDir;
	@Value("${dl.path.dir.dati.inforiv.in.bkp}")
	private String bkpDir;
	
	public void importa(File file) throws IOException {
		if (file.exists() && file.isFile()) {
			log.info("Unzippando file : " + (file != null ? file.getName() : ""));
			String fileName = file.getName();
			File bkpDirFile = null;
			try {
				String[] split = fileName.split("_");
				if (split.length >= 4) {
					String crivDl = split[0];
					String codFiegDl = split[1];
					bkpDirFile = new File(bkpDir, codFiegDl + "/" + crivDl); 
					if (!bkpDirFile.isDirectory()) {
						bkpDirFile.mkdirs();
					}
					unzipFile(file);
					FileUtils.copyFile(file, new File(bkpDirFile, StringUtility.stripPercentage(file.getName())));
					file.delete();
				}
			
			}catch (ZipException e){
				// Non cancello il file perchè potrebbe essere un errore causato dal file non ancora completo
	        	log.error("Errore in IGerivInforivDlZipFtpImporter.importa file : " + (file != null ? file.getName() : ""), e);
	        	String subject = IGerivMessageBundle.get("imp.ftp.inforiv.dl.error.subject");
	    		String msgObject = MessageFormat.format(IGerivMessageBundle.get("imp.ftp.inforiv.dl.error.message.file"), file.getName(), e.getMessage(), StringUtility.getStackTrace(e));
	    		try {
	    			mailingListService.sendEmailWithAttachment(null, subject, msgObject, null, true, null, true, null);
	    		} catch (Throwable e1) {
	    			log.error("Errore invio email in IGerivInforivDlZipFtpImporter.importa : ", e1);
	    		}
	    		if (bkpDirFile != null) {
	    			FileUtils.copyFile(file, new File(bkpDirFile, StringUtility.stripPercentage(file.getName())));
	    			file.delete();
	    		}
			
			} catch (IOException e) {
	        	// Non cancello il file perchè potrebbe essere un errore causato dal file non ancora completo
	        	log.error("Errore in IGerivInforivDlZipFtpImporter.importa file : " + (file != null ? file.getName() : ""), e);
	        	String subject = IGerivMessageBundle.get("imp.ftp.inforiv.dl.error.subject");
	    		String msgObject = MessageFormat.format(IGerivMessageBundle.get("imp.ftp.inforiv.dl.error.message.file"), file.getName(), e.getMessage(), StringUtility.getStackTrace(e));
	    		try {
	    			mailingListService.sendEmailWithAttachment(null, subject, msgObject, null, true, null, true, null);
	    		} catch (Throwable e1) {
	    			log.error("Errore invio email in IGerivInforivDlZipFtpImporter.importa : ", e1);
	    		}
	    		
	        } catch (Throwable e) {
	        	log.error("Errore in IGerivInforivDlZipFtpImporter.importa file : " + (file != null ? file.getName() : ""), e);
	        	String subject = IGerivMessageBundle.get("imp.ftp.inforiv.dl.error.subject");
	    		String msgObject = MessageFormat.format(IGerivMessageBundle.get("imp.ftp.inforiv.dl.error.message.file"), file.getName(), e.getMessage(), StringUtility.getStackTrace(e));
	    		try {
	    			mailingListService.sendEmailWithAttachment(null, subject, msgObject, null, true, null, true, null);
	    		} catch (Throwable e1) {
	    			log.error("Errore invio email in IGerivInforivDlZipFtpImporter.importa : ", e1);
	    		}
	    		if (bkpDirFile != null) {
	    			FileUtils.copyFile(file, new File(bkpDirFile, StringUtility.stripPercentage(file.getName())));
	    			file.delete();
	    		}
	        }
		}
	}

	/**
	 * Decomprime il file .zip nella cartella di input dove vengono presi i file inforiv .DAT
	 *
	 * @param File fo
	 * @throws ZipException
	 * @throws IOException
	 */
	private void unzipFile(File file) throws ZipException, IOException {
		ZipFile zipfile = null;
		try {
			zipfile = new ZipFile(file);
			for (Enumeration<? extends ZipEntry> e = zipfile.entries(); e.hasMoreElements(); ) {
			    ZipEntry entry = (ZipEntry) e.nextElement();
			    FileUtils.unzipEntry(file, zipfile, entry, new File(inputFileInforivDir), false);
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
	
	public String getInputFileInforivDir() {
		return bkpDir;
	}

	public void setInputFileInforivDir(String inputFileInforivDir) {
		this.bkpDir = inputFileInforivDir;
	}

	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

	public String getBkpDir() {
		return bkpDir;
	}

	public void setBkpDir(String bkpDir) {
		this.bkpDir = bkpDir;
	}
	
}
