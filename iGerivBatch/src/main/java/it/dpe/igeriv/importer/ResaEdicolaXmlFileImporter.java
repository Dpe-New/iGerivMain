package it.dpe.igeriv.importer;

import it.dpe.igeriv.bo.batch.IGerivImportService;
import it.dpe.igeriv.dto.ResaEdicolaDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.XmlUtils;
import it.dpe.igeriv.util.ZipUtils;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.mail.MailingListService;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Classe per l'importazione della resa delle edicole da Redic.
 * La configurazione in integrationContext.xml implementa un monitor sulla cartella FTP
 * di importazione. 
 * 
 * @author romanom
 */
@Component("resaEdicolaXmlFileImporter")
public class ResaEdicolaXmlFileImporter {
	@Autowired
	private IGerivImportService iGerivImportBo;
	@Value("${edicole.resa.img.dir}")
	private String pathToInDir;
	@Autowired
	private MailingListService mailingListService;
	@Value("${edicole.resa.resized.dir.name}")
	private String resizedDirName;
	@Value("${edicole.resa.bkp.dir.name}")
	private String backupDirName;
	@Value("${edicole.resa.numero.giorni.mantenimento.files}")
	private int numGiorniMantenimentoFiles;
	
	/**
	 * Esegue l'importazione del file xml dei dati della resa di un'edicola 
	 * e del file .zip con le relative immagini. 
	 * Il metodo è transazionale.
	 * 
	 * @param File file
	 * @throws IOException
	 * @throws JAXBException
	 */
	public synchronized void importaResaEdicole(File file) throws Exception {
		if (file.exists() && file.isFile() && file.canWrite()) {
			ResaEdicolaDto resaEdicolaDto = (ResaEdicolaDto) XmlUtils.unmarshall(ResaEdicolaDto.class, file);
			SimpleDateFormat sdf = new SimpleDateFormat(DateUtilities.FORMATO_DATA_YYYYMMDD);
			String dir = pathToInDir + System.getProperty("file.separator") + sdf.format(resaEdicolaDto.getDataResa()) + "_" + resaEdicolaDto.getTipoResa();
			File outImgDir = new File(dir);
			File outImgDirResized = new File(dir + System.getProperty("file.separator") + resizedDirName);
			if (!outImgDir.isDirectory()) {
				outImgDir.mkdir();
			}
			if (!outImgDirResized.isDirectory()) {
				outImgDirResized.mkdir();
			} 
			it.dpe.igeriv.util.FileUtils.deleteDirectoryContent(outImgDir);
			it.dpe.igeriv.util.FileUtils.deleteDirectoryContent(outImgDirResized);
			File zipFile = new File(pathToInDir + System.getProperty("file.separator") + resaEdicolaDto.getZipFile());
			ZipUtils.unzip(zipFile, outImgDir);
			it.dpe.igeriv.util.FileUtils.resizeImageDir(outImgDir, outImgDirResized, 100, 100);
			try {
				iGerivImportBo.importLavorazioneResaRivendita(resaEdicolaDto);
			} catch (Exception e) {
				moveFiles(file, zipFile);
				deleteOldFiles(new File(pathToInDir + System.getProperty("file.separator") + backupDirName));
				AnagraficaAgenziaVo agenzia = iGerivImportBo.getAgenziaByCodice(resaEdicolaDto.getCodiceDl());
				try {
					mailingListService.sendEmail(new String[]{agenzia.getEmail()}, IGerivMessageBundle.get("msg.subject.edicola.insesistente"), e.getLocalizedMessage(), true);
				} catch (Exception ce) {
					throw e;
				}
			}
			moveFiles(file, zipFile);
			deleteOldFiles(new File(pathToInDir + System.getProperty("file.separator") + backupDirName));
		}
	}

	/**
	 * Copia il file .xml e il file .zip in una cartella di backup e cancella 
	 * i file dalla cartella di input.
	 * 
	 * @param File file
	 * @param File zipFile
	 * @throws IOException
	 */
	private void moveFiles(File file, File zipFile) throws IOException {
		File destFile = new File(pathToInDir + "/bkp");
		if (!destFile.isDirectory()) {
			destFile.mkdir();
		}
		FileUtils.copyFile(file, new File(pathToInDir + System.getProperty("file.separator") + backupDirName + System.getProperty("file.separator") + file.getName()));
		FileUtils.copyFile(zipFile, new File(pathToInDir + System.getProperty("file.separator") + backupDirName + System.getProperty("file.separator") + zipFile.getName()));
		file.delete();
		zipFile.delete();
	}
	
	/**
	 * Cancella i file contenti nella cartella File con data di aggiornamento 
	 * precendente a oggi - <see>numGiorniMantenimentoFiles</see>
	 * 
	 * @param File dir
	 */
	private void deleteOldFiles(File dir) {
		if (dir.exists()) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DAY_OF_MONTH, -numGiorniMantenimentoFiles);
			Date when = cal.getTime();
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isFile() && new Date(file.lastModified()).before(when)) {
					file.delete();
				}
			}
		}
	}
	
	public IGerivImportService getiGerivImportBo() {
		return iGerivImportBo;
	}

	public void setiGerivImportBo(IGerivImportService iGerivImportBo) {
		this.iGerivImportBo = iGerivImportBo;
	}

	public String getPathToInDir() {
		return pathToInDir;
	}

	public void setPathToInDir(String pathToInDir) {
		this.pathToInDir = pathToInDir;
	}

	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

	public String getResizedDirName() {
		return resizedDirName;
	}

	public void setResizedDirName(String resizedDirName) {
		this.resizedDirName = resizedDirName;
	}

	public String getBackupDirName() {
		return backupDirName;
	}

	public void setBackupDirName(String backupDirName) {
		this.backupDirName = backupDirName;
	}

	public int getNumGiorniMantenimentoFiles() {
		return numGiorniMantenimentoFiles;
	}

	public void setNumGiorniMantenimentoFiles(int numGiorniMantenimentoFiles) {
		this.numGiorniMantenimentoFiles = numGiorniMantenimentoFiles;
	}
	
}
