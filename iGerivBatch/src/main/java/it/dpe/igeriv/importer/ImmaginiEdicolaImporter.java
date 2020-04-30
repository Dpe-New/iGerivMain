package it.dpe.igeriv.importer;

import it.dpe.igeriv.bo.batch.IGerivImportService;
import it.dpe.igeriv.resources.ExposablePropertyPaceholderConfigurer;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.mail.MailingListService;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import javax.xml.bind.JAXBException;

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
 * @author romanom
 *
 */
@Component("immaginiEdicolaImporter")
public class ImmaginiEdicolaImporter {
	private final Logger log = Logger.getLogger(getClass());
	private final IGerivImportService iGerivImportBo;
	private final MailingListService mailingListService;
	private final String imgPathDir;
	private final String imgPathDirBkp;
	private final String imgPathDirErr;
	private final String addWatermark;
	private final ExposablePropertyPaceholderConfigurer props;
	
	@Autowired 
	public ImmaginiEdicolaImporter(IGerivImportService iGerivImportBo, MailingListService mailingListService, @Value("${img.path.dir}") String imgPathDir, @Value("${img.path.dir.bkp}") String imgPathDirBkp, @Value("${img.add.watermark}") String addWatermark, ExposablePropertyPaceholderConfigurer props,@Value("${img.tmp.path.err}") String imgPathDirErr) {
		this.iGerivImportBo = iGerivImportBo;
		this.mailingListService = mailingListService;
		this.imgPathDir = imgPathDir;
		this.imgPathDirBkp = imgPathDirBkp;
		this.imgPathDirErr = imgPathDirErr;
		this.addWatermark = addWatermark;
		this.props = props;
	}
	
	/**
	 * Esegue l'importazione delle immagini delle pubblicazioni trasferendole dalla cartella 
	 * temporaria alla cartella definitiva e inserendole nel DB.  
	 * Il metodo è transazionale.
	 * 
	 * @param File file
	 * @throws IOException
	 * @throws JAXBException
	 */
	public synchronized void importaImmaginiEdicole(File file) throws IOException, JAXBException {
		
		if (file.exists() && file.isFile() && file.canWrite()) {
			
			File imgDir = new File(imgPathDir);
			File imgDirBkp = new File(imgPathDirBkp);
			File imgDirErr = new File(imgPathDirErr);
			
			if (!imgDir.isDirectory()) {
				imgDir.mkdir();
			} 
			if (!imgDirBkp.isDirectory()) {
				imgDirBkp.mkdir();
			}
			if (!imgDirErr.isDirectory()) {
				imgDirErr.mkdir();
			}
			
			String nameFile = file.getName().substring(0, file.getName().lastIndexOf("."));
			boolean validationBarcodeLength  = (nameFile.length() > 8 )?true:false;
			//boolean validationBarcodeLength 	= (file.getName().length() > 12 )?true:false;
			boolean validationBarcodeLength2 	= (validationBarcodeLength && file.getName().substring(8, file.getName().lastIndexOf(".")).length()<=18)?true:false;
			
			if(validationBarcodeLength2){
				
				/**
				 * Validazione del nome file == true
				 * Il file viene copiato nella cartella work 
				 * Vengono rese persistenti le informazioni sul DB
				 * Viene cancellato il file dalla cartellina tmp
				 * Viene effettuato il backup del file dalla cartellina work a bkp
				 */
				
				File destFile2 = new File(imgDir.getAbsolutePath() + System.getProperty("file.separator") + file.getName());
				try {
					FileUtils.copyFile(file, destFile2);
				} catch (Exception e) {
					log.error("Error while copying file image " + file.getAbsolutePath() + " to file " + destFile2.getAbsolutePath());
				}
				
				try {
					
					log.info("before importImmagine " + file.getName());
					iGerivImportBo.importImmagine(file.getName());
					log.info("after importImmagine " + file.getName());

					file.delete();
					
					File destFile = new File(imgDirBkp.getAbsolutePath() + System.getProperty("file.separator") + destFile2.getName());
					try {
						FileUtils.copyFile(destFile2, destFile);
					} catch (Exception e) {
						log.error("Error while copying file image " + destFile2.getAbsolutePath() + " to backup file " + destFile.getAbsolutePath());
					}
					if (props.getResolvedProps().get("igeriv.env.deploy.name") != null 
								&& props.getResolvedProps().get("igeriv.env.deploy.name").equalsIgnoreCase("dpe") 
										&& !Strings.isNullOrEmpty(addWatermark) && Boolean.parseBoolean(addWatermark)) {
						iGerivImportBo.addWatermarkToImage(destFile2);
					}								
				} catch (Throwable e) {
					log.error("Error during file image import. File: " + destFile2.getAbsolutePath(), e);
					mailingListService.sendEmail(null, IGerivMessageBundle.get("msg.subject.errore.importazione.immagini"), MessageFormat.format(IGerivMessageBundle.get("msg.errore.importazione.immagini"), new Object[]{destFile2.getName(), destFile2.getParentFile().getAbsolutePath()}) + StringUtility.getStackTrace(e), true);
				} finally {
					log.info("fine import immagine");
				}
				
				
			}else{
					/**
					 * Validazione del nome file == false
					 * Il file viene copiato nella cartella error e viene cancellato dalla cartella tmp
					 * Viene inoltrata una mail per segnalare l'errore
					 */
					File destFileErr = new File(imgDirErr.getAbsolutePath() + System.getProperty("file.separator") + file.getName());
					try {
						FileUtils.copyFile(file, destFileErr);
						file.delete();
						log.error("importImmagine " + file.getName() + IGerivMessageBundle.get("msg.subject.errore.importazione.immagini"));
						mailingListService.sendEmail(null, IGerivMessageBundle.get("msg.subject.errore.importazione.immagini"), MessageFormat.format(IGerivMessageBundle.get("msg.errore.importazione.immagini.barcode"), new Object[]{destFileErr.getName(), destFileErr.getParentFile().getAbsolutePath()}), true);	
					} catch (Exception e) {
						log.error("Error while copying file image " + file.getAbsolutePath() + " to file " + destFileErr.getAbsolutePath());
						mailingListService.sendEmail(null, IGerivMessageBundle.get("msg.subject.errore.importazione.immagini"), 
									MessageFormat.format(IGerivMessageBundle.get("msg.errore.importazione.immagini"), new Object[]{destFileErr.getName(), destFileErr.getParentFile().getAbsolutePath()}) + StringUtility.getStackTrace(e), true);
					
					}
			}
			
		}else{
			log.info("importImmagine " + file.getName() + " " + file.exists() + " " +  file.isFile() + " " +  file.canWrite());
		}
	}
	
	
}
