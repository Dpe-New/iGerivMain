package it.dpe.inforiv.job;

import static ch.lambdaj.Lambda.selectDistinct;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.FtpUtils;
import it.dpe.igeriv.util.FtpUtils.FtpLocalFilesBuilder;
import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.inforiv.bo.InforivImportBo;
import it.dpe.inforiv.ftp.importer.FileInforivFtpImporter;
import it.dpe.mail.MailingListService;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.google.common.base.Strings;

/**
 * Classe job schedulata che trasferisce e importa i file inforiv da ftp per ciascuna edicola con interfaccia inforiv.
 * 
 * @author romanom
 */
public class ImportInforivFtpJob extends QuartzJobBean {
	private final Logger log = Logger.getLogger(getClass());
	private InforivImportBo inforivImportBo;
	private MailingListService mailingListService;
	private FileInforivFtpImporter fileInforivFtpImporter;
	private String pathZipFiles;
	private String remoteDir;
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		log.info("Entered ImportInforivFtpJob.executeInternal");
		try {
			Collection<EdicolaDto> listEdicoleInforiv = inforivImportBo.getEdicoleInforiv();
			if (listEdicoleInforiv != null && !listEdicoleInforiv.isEmpty()) {
				listEdicoleInforiv = selectDistinct(listEdicoleInforiv, "ftpUser");
			}
			for (EdicolaDto vo : listEdicoleInforiv) {
				File dir = new File(pathZipFiles + System.getProperty("file.separator") + vo.getFtpUser() + System.getProperty("file.separator") + "bkp");
				FTPFileFilter inforivFTPFileFilter = (FTPFileFilter) SpringContextManager.getSpringContext().getBean("InforivFTPFileFilter", dir);
				FtpLocalFilesBuilder localFileBuilder = (FtpLocalFilesBuilder) SpringContextManager.getSpringContext().getBean("InforivFtpLocalFilesBuilder", vo.getFtpUser());
				String ftpDir = Strings.isNullOrEmpty(vo.getRemoteFtpDir()) ? remoteDir : vo.getRemoteFtpDir(); 
				List<String> inforivZipFiles = FtpUtils.filterAndTransferFtpFiles(vo.getFtpHost(), vo.getFtpUser(), vo.getFtpPwd(), ftpDir, localFileBuilder, inforivFTPFileFilter, 5000, 10000, vo.getDeleteInforivFtpFileAfterDownload());
				if (inforivZipFiles != null && !inforivZipFiles.isEmpty()) {
					for (String strFile : inforivZipFiles) {
						fileInforivFtpImporter.importaFile(new File(strFile));
					}
				}
			}
		} catch (IOException e) {
			// Gia' loggato in FtpUtils
		} catch (Throwable e) {
			try {
				log.error(IGerivMessageBundle.get("imp.ftp.inforiv.error.message.subject"), e);
				String message = MessageFormat.format(IGerivMessageBundle.get("imp.ftp.inforiv.error.message.generic"), (e.getMessage() != null ? e.getMessage() : ""), StringUtility.getStackTrace(e));
				mailingListService.sendEmailWithAttachment(null, IGerivMessageBundle.get("imp.ftp.inforiv.error.message.subject"), message, null, true, null, true, null);
			} catch (Throwable e1) {
				log.error(IGerivMessageBundle.get("imp.error.send.email"), e1);
			}
		}
	}
	
	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

	public InforivImportBo getInforivImportBo() {
		return inforivImportBo;
	}

	public void setInforivImportBo(InforivImportBo inforivImportBo) {
		this.inforivImportBo = inforivImportBo;
	}

	public FileInforivFtpImporter getFileInforivFtpImporter() {
		return fileInforivFtpImporter;
	}

	public void setFileInforivFtpImporter(FileInforivFtpImporter fileInforivFtpImporter) {
		this.fileInforivFtpImporter = fileInforivFtpImporter;
	}

	public String getPathZipFiles() {
		return pathZipFiles;
	}

	public void setPathZipFiles(String pathZipFiles) {
		this.pathZipFiles = pathZipFiles;
	}

	public String getRemoteDir() {
		return remoteDir;
	}

	public void setRemoteDir(String remoteDir) {
		this.remoteDir = remoteDir;
	}

}
