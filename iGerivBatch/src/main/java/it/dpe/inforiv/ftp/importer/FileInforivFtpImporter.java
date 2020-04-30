package it.dpe.inforiv.ftp.importer;

import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.FileUtils;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.InforivFtpFileVo;
import it.dpe.mail.MailingListService;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Questa classe viene chiamata subito dopo lo scarico di un file .zip
 * dal sito FTP inforiv.
 * La deinizione del bean è in src/main/resources/dynamicFtpInboundAdapterContext.xml
 * 
 * @author mromano
 *
 */
@Component("FileInforivFtpImporter")
public class FileInforivFtpImporter {
	private final Logger log = Logger.getLogger(getClass());
	@Autowired
	private IGerivBatchService iGerivBatchBo;
	@Value("${inforiv.file.import.path.dir}")
	private String inputFileInforivDir;  // igeriv/edicole/inforiv/in
	@Autowired
	private MailingListService mailingListService;
	
	public void importaFile(File file) throws IOException {
		if (file.exists() && file.isFile()) {
			log.info("Unzippando file : " + (file != null ? file.getName() : ""));
			File bkpDir = new File(file.getParentFile(), "bkp"); 
			try {
				if (!bkpDir.isDirectory()) {
					bkpDir.mkdirs();
				}
				unzipFile(file);
	            insertInforivFtpFileVo(file);
				FileUtils.copyFile(file, new File(bkpDir, StringUtility.stripPercentage(file.getName())));
				file.delete();
	        } catch (IOException e) {
	        	// Non cancello il file perchè potrebbe essere un errore causato dal file non ancora completo
	        	log.error("Errore in FileInforivFtpImporter.importaFile file : " + (file != null ? file.getName() : ""), e);
	        	String subject = IGerivMessageBundle.get("imp.ftp.inforiv.error.subject");
	    		String msgObject = MessageFormat.format(IGerivMessageBundle.get("imp.ftp.inforiv.error.message.file"), file.getName(), e.getMessage(), StringUtility.getStackTrace(e));
	    		try {
	    			mailingListService.sendEmailWithAttachment(null, subject, msgObject, null, true, null, true, null);
	    		} catch (Throwable e1) {
	    			log.error("Errore invio email in FileInforivFtpImporter.importaFile : ", e1);
	    		}
	        } catch (Throwable e) {
	        	log.error("Errore in FileInforivFtpImporter.importaFile file : " + (file != null ? file.getName() : ""), e);
	        	String subject = IGerivMessageBundle.get("imp.ftp.inforiv.error.subject");
	    		String msgObject = MessageFormat.format(IGerivMessageBundle.get("imp.ftp.inforiv.error.message.file"), file.getName(), e.getMessage(), StringUtility.getStackTrace(e));
	    		try {
	    			mailingListService.sendEmailWithAttachment(null, subject, msgObject, null, true, null, true, null);
	    		} catch (Throwable e1) {
	    			log.error("Errore invio email in FileInforivFtpImporter.importaFile : ", e1);
	    		}
	    		FileUtils.copyFile(file, new File(bkpDir, StringUtility.stripPercentage(file.getName())));
				file.delete();
	        }
		}
	}

	/**
	 * 21/11/2014 - 0000074
	 * Effettua l'operazione di unzip all'interno della cartella input utilizzata dal processo di elaborazione dati .
	 * Inserisci il nome del file all'interno della tabella tbl_9760
	 * Copia il file nella cartella bkp
	 * Cancella il file
	 * 
	 * @param file
	 * @param dirBkpLocalDL
	 * @throws IOException
	 */
	public void importaFile(File file,File dirBkpLocalDL) throws IOException {
		if (file.exists() && file.isFile()) {
			log.info("Unzippando file : " + (file != null ? file.getName() : ""));
			
			try {
				if (!dirBkpLocalDL.isDirectory()) {
					dirBkpLocalDL.mkdirs();
				}
				unzipFile(file);
				//ZipUtils.unzip(file, new File(inputFileInforivDir));
	            insertInforivFtpFileVo(file);
				FileUtils.copyFile(file, new File(dirBkpLocalDL, StringUtility.stripPercentage(file.getName())));
				file.delete();
	        } catch (IOException e) {
	        	// Non cancello il file perchè potrebbe essere un errore causato dal file non ancora completo
	        	log.error("Errore in FileInforivFtpImporter.importaFile file : " + (file != null ? file.getName() : ""), e);
	        	String subject = IGerivMessageBundle.get("imp.ftp.inforiv.error.subject");
	    		String msgObject = MessageFormat.format(IGerivMessageBundle.get("imp.ftp.inforiv.error.message.file"), file.getName(), e.getMessage(), StringUtility.getStackTrace(e));
	    		try {
	    			mailingListService.sendEmailWithAttachment(null, subject, msgObject, null, true, null, true, null);
	    		} catch (Throwable e1) {
	    			log.error("Errore invio email in FileInforivFtpImporter.importaFile : ", e1);
	    		}
	        } catch (Throwable e) {
	        	log.error("Errore in FileInforivFtpImporter.importaFile file : " + (file != null ? file.getName() : ""), e);
	        	String subject = IGerivMessageBundle.get("imp.ftp.inforiv.error.subject");
	    		String msgObject = MessageFormat.format(IGerivMessageBundle.get("imp.ftp.inforiv.error.message.file"), file.getName(), e.getMessage(), StringUtility.getStackTrace(e));
	    		try {
	    			mailingListService.sendEmailWithAttachment(null, subject, msgObject, null, true, null, true, null);
	    		} catch (Throwable e1) {
	    			log.error("Errore invio email in FileInforivFtpImporter.importaFile : ", e1);
	    		}
	    		FileUtils.copyFile(file, new File(dirBkpLocalDL, StringUtility.stripPercentage(file.getName())));
				file.delete();
	        }
		}
	}
	
	
	
	
	/**
	 * Inserisce il nome del file nella tabella di storico file downloadati da FTP
	 * 
	 * @param File file
	 */
	private void insertInforivFtpFileVo(File file) {
		InforivFtpFileVo vo = new InforivFtpFileVo();
		vo.setFileName(file.getName());
		vo.setDataDownload(iGerivBatchBo.getSysdate());
		iGerivBatchBo.saveBaseVo(vo);
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
		List<String> listNameFileDat = new ArrayList<String>();;
		
		try {
			zipfile = new ZipFile(file);
			for (Enumeration<? extends ZipEntry> e = zipfile.entries(); e.hasMoreElements(); ) {
			    ZipEntry entry = (ZipEntry) e.nextElement();
			    FileUtils.unzipEntry(file, zipfile, entry, new File(inputFileInforivDir), true);
			    //Nome file interno
			    listNameFileDat.add(entry.getName()); 
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
		
		if(!listNameFileDat.isEmpty()){
			for (String stringNameFileDat : listNameFileDat) {
				File entryFileDat = new File(inputFileInforivDir, stringNameFileDat);
				if(entryFileDat.exists()){
					DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");  
				    df.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));  
			    	String addonNameFile = df.format(new Date());
			    	File fileRenameDat = new File(inputFileInforivDir, StringUtility.stripPercentage(entryFileDat.getName().replace(".dat", "").replace(".DAT", "")+"_"+addonNameFile+".dat"));
			    	FileUtils.copyFile(entryFileDat,fileRenameDat);
			    	log.info(" COPY AND RENAME FILE DAT "+fileRenameDat.getAbsolutePath());
			    	if(fileRenameDat.exists()){
			    		entryFileDat.delete();
			    		log.info("DELETE FILE DAT "+entryFileDat.getAbsolutePath());
			    	}
			    	try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
						log.info("Exception : InterruptedException "+e.getMessage());
					}
				}
			}
		}
		
	}
	
	
	
	
	public IGerivBatchService getiGerivBatchBo() {
		return iGerivBatchBo;
	}

	public void setiGerivBatchBo(IGerivBatchService iGerivBatchBo) {
		this.iGerivBatchBo = iGerivBatchBo;
	}

	public String getInputFileInforivDir() {
		return inputFileInforivDir;
	}

	public void setInputFileInforivDir(String inputFileInforivDir) {
		this.inputFileInforivDir = inputFileInforivDir;
	}

	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

}
