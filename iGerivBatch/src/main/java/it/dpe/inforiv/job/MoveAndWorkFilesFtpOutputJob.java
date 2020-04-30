package it.dpe.inforiv.job;

import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.inforiv.bo.InforivExportFileBo;
import it.dpe.inforiv.ftp.importer.FileInforivFtpImporter;
import it.dpe.mail.MailingListService;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Classe job schedulata 
 * 
 */
public class MoveAndWorkFilesFtpOutputJob extends QuartzJobBean {
	private final Logger log = Logger.getLogger(getClass());
	private AgenzieService agenzieService;
	private MailingListService mailingListService;
	private String pathFilesLocalOut;
	private String pathFolderOut;
	
	private InforivExportFileBo inforivExportFileBo;
	

	private FileInforivFtpImporter fileInforivFtpImporter;
	
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
			log.info("Entered MoveAndWorkFilesFtpOutputJob.executeInternal");
			try {
				//Get elenco dei dl abilitati alla modalità 2 inforiv
				List<AnagraficaAgenziaVo> listAgenzie = agenzieService.getAgenzieModalitaLocaleFtpInforiv(); //FLGMODFTP9107 = 1
					for(AnagraficaAgenziaVo agenzia : listAgenzie){
						log.info("Agenzia in lavorazione : "+agenzia.getCodFiegDlMaster()+" - "+agenzia.getRagioneSocialeDlPrimaRiga());
						String codDlFormat = String.format("%4s", agenzia.getCodFiegDl()).replace(' ', '0');
						File dirOutputLocalDL 	 = new File(pathFilesLocalOut + codDlFormat + System.getProperty("file.separator") + pathFolderOut);
						File dirOutputBKPLocalDL = new File(pathFilesLocalOut + codDlFormat + System.getProperty("file.separator") + pathFolderOut + System.getProperty("file.separator")+ "BKP");
						
						if(dirOutputLocalDL.isDirectory()){
							
							if (!dirOutputBKPLocalDL.isDirectory()) {
								dirOutputBKPLocalDL.mkdirs();
							}
							inforivExportFileBo.exportInforivFileLocal(agenzia,dirOutputLocalDL,dirOutputBKPLocalDL);
							
							
						}else{
							String msgErrorNotExistFolder = "Directory output ( "+dirOutputLocalDL+" ) non esistente per agenzia "+agenzia.getCodFiegDlMaster()+" - "+agenzia.getRagioneSocialeDlPrimaRiga();
							log.error(msgErrorNotExistFolder);
							mailingListService.sendEmailWithAttachment(null, IGerivMessageBundle.get("out.move.and.work.inforiv.ftp.error.message.subject"), msgErrorNotExistFolder, null, true, null, true, null);
						}
					}
					
				} catch (Throwable e) {
					try {
						log.error(IGerivMessageBundle.get("out.move.and.work.inforiv.ftp.error.message.subject"), e);
						String message = MessageFormat.format(IGerivMessageBundle.get("out.move.and.work.inforiv.ftp.error.message.generic"), (e.getMessage() != null ? e.getMessage() : ""), StringUtility.getStackTrace(e));
						mailingListService.sendEmailWithAttachment(null, IGerivMessageBundle.get("out.move.and.work.inforiv.error.message.subject"), message, null, true, null, true, null);
					} catch (Throwable e1) {
						log.error(IGerivMessageBundle.get("imp.error.send.email"), e1);
					}
				}
			log.info("End MoveAndWorkFilesFtpOutputJob.executeInternal");
	}
	
	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

	
	public InforivExportFileBo getInforivExportFileBo() {
		return inforivExportFileBo;
	}

	public void setInforivExportFileBo(InforivExportFileBo inforivExportFileBo) {
		this.inforivExportFileBo = inforivExportFileBo;
	}

	public FileInforivFtpImporter getFileInforivFtpImporter() {
		return fileInforivFtpImporter;
	}

	public void setFileInforivFtpImporter(FileInforivFtpImporter fileInforivFtpImporter) {
		this.fileInforivFtpImporter = fileInforivFtpImporter;
	}

	public AgenzieService getAgenzieService() {
		return agenzieService;
	}

	public void setAgenzieService(AgenzieService agenzieService) {
		this.agenzieService = agenzieService;
	}

	public String getPathFilesLocalOut() {
		return pathFilesLocalOut;
	}

	public void setPathFilesLocalOut(String pathFilesLocalOut) {
		this.pathFilesLocalOut = pathFilesLocalOut;
	}

	public String getPathFolderOut() {
		return pathFolderOut;
	}
	
	public void setPathFolderOut(String pathFolderOut) {
		this.pathFolderOut = pathFolderOut;
	}

	

}
