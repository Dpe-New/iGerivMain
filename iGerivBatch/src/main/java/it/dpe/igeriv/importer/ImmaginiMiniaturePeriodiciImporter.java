package it.dpe.igeriv.importer;

import it.dpe.igeriv.bo.batch.IGerivImportService;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.StringUtility;
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
 * Classe per l'importazione delle immagini delle pubblicazioni iGeriv.
 * La configurazione in integrationContext.xml implementa un monitor sulla cartella di importazione. 
 * 
 * @author romanom
 *
 */
@Component("immaginiMiniaturePeriodiciImporter")
public class ImmaginiMiniaturePeriodiciImporter {
	@Autowired
	private IGerivImportService iGerivImportBo;
	@Value("${img.miniature.path.dir}")
	private String imgMiniaturePathDir;
	@Autowired
	private MailingListService mailingListService;
	
	/**
	 * Esegue l'importazione delle immagini miniature delle pubblicazione inserendole nel DB nella
	 * tabella tbl_9606.  
	 * Il metodo è transazionale.
	 * 
	 * @param File file
	 * @throws IOException
	 * @throws JAXBException
	 */
	public synchronized void importaImmagini(File file) throws IOException, JAXBException {
		if (file.exists() && file.isFile() && file.canWrite()) {
			File imgTmpDir = new File(imgMiniaturePathDir); 
			if (!imgTmpDir.isDirectory()) {
				imgTmpDir.mkdir();
			}
			try {
				String titolo = file.getName().substring(0, file.getName().lastIndexOf("."));
				iGerivImportBo.importImmagineMiniaturaPeriodico(file, titolo);
			} catch (Exception e) {
				mailingListService.sendEmail(null, IGerivMessageBundle.get("msg.subject.errore.importazione.immagini"), 
						MessageFormat.format(IGerivMessageBundle.get("msg.errore.importazione.immagini"), new Object[]{file.getName(), file.getParentFile().getAbsolutePath()}) + StringUtility.getStackTrace(e), true);
			} finally {
				FileUtils.copyFile(file, new File(imgTmpDir.getAbsolutePath() + System.getProperty("file.separator") + StringUtility.stripPercentage(file.getName())));
				file.delete();
			}
		}
	}
	
	public IGerivImportService getiGerivImportBo() {
		return iGerivImportBo;
	}

	public void setiGerivImportBo(IGerivImportService iGerivImportBo) {
		this.iGerivImportBo = iGerivImportBo;
	}
	
	public String getImgMiniaturePathDir() {
		return imgMiniaturePathDir;
	}

	public void setImgMiniaturePathDir(String imgMiniaturePathDir) {
		this.imgMiniaturePathDir = imgMiniaturePathDir;
	}

	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

}
