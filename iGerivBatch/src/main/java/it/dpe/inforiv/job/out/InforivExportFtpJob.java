package it.dpe.inforiv.job.out;

import it.dpe.igeriv.bo.inforiv.InforivExportBo;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.inforiv.bo.InforivExportFileBo;
import it.dpe.inforiv.bo.InforivImportBo;
import it.dpe.mail.MailingListService;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

	@Component("inforivExportFtpJob")
	public class InforivExportFtpJob {
		private final Logger log = Logger.getLogger(getClass());
		@Autowired
		private InforivExportFileBo inforivExportBo;
		@Value("${inforiv.bkp.file.export.ftp.path.dir}")
		private String bkpOutPathDir;
		@Autowired
		private MailingListService mailingListService;
		
		/**
		 * Esegue l'importazione del file inforiv per edicola.
		 * Il metodo è transazionale.
		 * 
		 * @param File file
		 * @throws IOException
		 * @throws JAXBException
		 */
		public synchronized void esporta(File file) throws IOException, JAXBException {
			if (file.exists()) {
				log.info("Inforiv - copia ftp file : " + file.getPath());

				boolean toRename = false;
				String fileName = file.getName();
		        int len = fileName.length();        // 42
		        boolean zip = fileName.endsWith("zip") || fileName.endsWith("ZIP");
		        if (zip && len == 42) {
			        String coddl = fileName.substring(5, 8);
			        String criv = fileName.substring(0,4);
					//String bkpOutPathDir = "E://igeriv//edicole//inforiv//out//zip//bck";
					if (NumberUtils.isNumber(coddl) && NumberUtils.isNumber(criv)) {
						inforivExportBo.exportInforivFileFtp(Integer.valueOf(coddl), Integer.valueOf(criv), file, bkpOutPathDir);
					} else {
			        	toRename = true;
					}
		        } else {
		        	toRename = true;
		        }
		        if (toRename) {
		        	File dest = new File(file.getPath() + "._ERR1");
					file.renameTo(dest);
		        }
		    	try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					log.info("Exception : InterruptedException "+e.getMessage());
				}
			}
		}

}
