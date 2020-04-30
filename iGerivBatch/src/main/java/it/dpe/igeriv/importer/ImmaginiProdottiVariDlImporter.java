package it.dpe.igeriv.importer;

import it.dpe.igeriv.bo.batch.IGerivImportService;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.FileUtils;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.mail.MailingListService;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import javax.imageio.IIOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Classe per l'importazione delle immagini delle pubblicazioni iGeriv.
 * La configurazione in integrationContext.xml implementa un monitor sulla cartella di importazione. 
 * 
 * @author romanom
 */
@Component("immaginiProdottiVariDlImporter")
public class ImmaginiProdottiVariDlImporter {
	@Autowired
	private IGerivImportService iGerivImportBo;
	@Value("${img.tmp.prodotti.vari.dl.path.dir}")
	private String imgPathProdottiVariDlTempDir;
	@Value("${img.prodotti.vari.dl.path.dir}")
	private String imgPathProdottiVariDlDir;
	@Value("${img.miniature.edicola.prodotti.vari.path.dir}")
	private String imgPathProdottiVariEdicoleDir;
	@Value("${prodotti.vari.icon.width}")
	private Integer prodottiVariIconWidth;
	@Value("${prodotti.vari.icon.height}")
	private Integer prodottiVariconHeight;
	@Autowired
	private MailingListService mailingListService;
	
	/**
	 * Esegue l'importazione delle immagini dei prodotti vari del DL:
	 * crea la minitaura dell'immagine del prodotto del DL nella cartella imgPathProdottiVariEdicoleDir
	 * 
	 * @param File file
	 * @throws IOException 
	 */
	public synchronized void importaImmaginiProdottiVariDl(File file) {
		if (file.exists() && file.isFile() && file.canWrite()) {
			File imgImgProdottiVariDLDir = new File(imgPathProdottiVariDlDir);
			File imgMiniatureDir = new File(imgPathProdottiVariEdicoleDir);
			if (!imgImgProdottiVariDLDir.isDirectory()) {
				imgImgProdottiVariDLDir.mkdir();
			}
			if (!imgMiniatureDir.isDirectory()) {
				imgMiniatureDir.mkdir();
			}
			try {
				FileUtils.resizeImage(file, imgMiniatureDir, prodottiVariIconWidth, prodottiVariconHeight);
				FileUtils.copyFile(file, new File(imgImgProdottiVariDLDir.getAbsolutePath() + System.getProperty("file.separator") + file.getName()));
				file.delete();
			} catch (IIOException e) {
				// Possibile errore di lettura immagine -> Non cancello l'immagine in modo da riprocessarla
			} catch (Exception e) {
				file.delete();
				mailingListService.sendEmail(null, IGerivMessageBundle.get("msg.subject.errore.importazione.immagini.prodotti.vari.dl"), 
						MessageFormat.format(IGerivMessageBundle.get("msg.errore.importazione.immagini.prodotti.vari.dl"), new Object[]{file.getAbsolutePath(), StringUtility.getStackTrace(e)}), true);
			}
		}
	}
	
	public IGerivImportService getiGerivImportBo() {
		return iGerivImportBo;
	}

	public void setiGerivImportBo(IGerivImportService iGerivImportBo) {
		this.iGerivImportBo = iGerivImportBo;
	}
	
	public String getImgPathProdottiVariDlTempDir() {
		return imgPathProdottiVariDlTempDir;
	}

	public void setImgPathProdottiVariDlTempDir(String imgPathProdottiVariDlTempDir) {
		this.imgPathProdottiVariDlTempDir = imgPathProdottiVariDlTempDir;
	}

	public String getImgPathProdottiVariEdicoleDir() {
		return imgPathProdottiVariEdicoleDir;
	}

	public void setImgPathProdottiVariEdicoleDir(String imgPathProdottiVariEdicoleDir) {
		this.imgPathProdottiVariEdicoleDir = imgPathProdottiVariEdicoleDir;
	}

	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

	public Integer getProdottiVariIconWidth() {
		return prodottiVariIconWidth;
	}

	public void setProdottiVariIconWidth(Integer prodottiVariIconWidth) {
		this.prodottiVariIconWidth = prodottiVariIconWidth;
	}

	public Integer getProdottiVariconHeight() {
		return prodottiVariconHeight;
	}

	public void setProdottiVariconHeight(Integer prodottiVariconHeight) {
		this.prodottiVariconHeight = prodottiVariconHeight;
	}

	public String getImgPathProdottiVariDlDir() {
		return imgPathProdottiVariDlDir;
	}

	public void setImgPathProdottiVariDlDir(String imgPathProdottiVariDlDir) {
		this.imgPathProdottiVariDlDir = imgPathProdottiVariDlDir;
	}
	
}
