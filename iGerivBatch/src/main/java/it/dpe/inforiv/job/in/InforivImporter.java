package it.dpe.inforiv.job.in;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.inforiv.bo.InforivImportBo;
import it.dpe.mail.MailingListService;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
 
/**
 * Classe per l'importazione dei file inforiv.
 * 
 * @author romanom
 */
@Component("inforivImporter")
public class InforivImporter {
	@Autowired
	private InforivImportBo inforivImportBo;
	@Value("${inforiv.bkp.file.import.path.dir}")
	private String bkpPathDir;
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
	public synchronized void importaInforiv(File file) throws IOException, JAXBException {
		if (file.exists()) {
			File bkpDir = new File(bkpPathDir);
			if (!bkpDir.isDirectory()) {
				bkpDir.mkdir();
			}
			try {
				inforivImportBo.impInforiv(file);
				FileUtils.copyFile(file, new File(bkpDir.getAbsolutePath() + System.getProperty("file.separator") + file.getName()));
				file.delete();
			} catch (IOException e) {
				mailingListService.sendEmail(IGerivMessageBundle.get("dpe.validation.msg.errore.fatale.importazione.inforiv.subject"), 
						MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.fatale.importazione.inforiv.body"), file.getName(), StringUtility.getStackTrace(e)));
			} catch (Exception e) {
				FileUtils.copyFile(file, new File(bkpDir.getAbsolutePath() + System.getProperty("file.separator") + file.getName()));
				file.delete();
				mailingListService.sendEmail(IGerivMessageBundle.get("dpe.validation.msg.errore.fatale.importazione.inforiv.subject"), 
						MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.fatale.importazione.inforiv.body"), file.getName(), StringUtility.getStackTrace(e)));
			}
		}
	}
	
	public InforivImportBo getInforivImportBo() {
		return inforivImportBo;
	}
	
	public void setInforivImportBo(InforivImportBo inforivImportBo) {
		this.inforivImportBo = inforivImportBo;
	}

	public String getBkpPathDir() {
		return bkpPathDir;
	}

	public void setBkpPathDir(String bkpPathDir) {
		this.bkpPathDir = bkpPathDir;
	}

	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

}
