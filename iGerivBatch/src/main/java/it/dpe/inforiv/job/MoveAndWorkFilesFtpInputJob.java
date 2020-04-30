package it.dpe.inforiv.job;

import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.inforiv.bo.InforivImportBo;
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
 * 21/11/2014 - 0000074
 * Classe job 
 * 
 */
public class MoveAndWorkFilesFtpInputJob extends QuartzJobBean {
	private final Logger log = Logger.getLogger(getClass());
	private MailingListService mailingListService;
	private AgenzieService agenzieService;
	private String pathFilesLocalInp;
	private String pathFolderIn;
	
	
	private InforivImportBo inforivImportBo;
	private FileInforivFtpImporter fileInforivFtpImporter;
	
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
			log.info("Entered MoveAndWorkFilesFtpInputJob.executeInternal");
			try {
				//Get elenco dei dl abilitati alla modalità 2 inforiv
				List<AnagraficaAgenziaVo> listAgenzie = agenzieService.getAgenzieModalitaLocaleFtpInforiv();//FLGMODFTP9107 = 1
				for(AnagraficaAgenziaVo agenzia : listAgenzie){
					log.info("Agenzia in lavorazione : "+agenzia.getCodFiegDlMaster()+" - "+agenzia.getRagioneSocialeDlPrimaRiga());
					String codDlFormat = String.format("%4s", agenzia.getCodFiegDl().toString()).replace(' ', '0');
					File dirInputLocalDL = new File(pathFilesLocalInp + codDlFormat + System.getProperty("file.separator") + pathFolderIn);
					File dirBkpLocalDL 	 = new File(dirInputLocalDL + System.getProperty("file.separator") + "BKP");
					if(dirInputLocalDL.isDirectory()){
						File[] listFiles = dirInputLocalDL.listFiles();
						if (listFiles != null) {
							for (int i = 0; i < listFiles.length; i++) {
								if(listFiles[i].isFile()){
										String fileName = listFiles[i].getName();
										log.info(" ("+agenzia.getCodFiegDlMaster()+" - "+agenzia.getRagioneSocialeDlPrimaRiga()+") File in elaborazione  : "+fileName);
										fileInforivFtpImporter.importaFile(listFiles[i], dirBkpLocalDL);										
									}else{
										log.info(" ("+agenzia.getCodFiegDlMaster()+" - "+agenzia.getRagioneSocialeDlPrimaRiga()+") Nessun file nella directory input ");
									}
								}
							}else{
								log.info(" ("+agenzia.getCodFiegDlMaster()+" - "+agenzia.getRagioneSocialeDlPrimaRiga()+") Nessun file nella directory input ");
							}
					}else{
						String msgErrorNotExistFolder = "Directory input/bkp non esistente per agenzia "+agenzia.getCodFiegDlMaster()+" - "+agenzia.getRagioneSocialeDlPrimaRiga();
						log.error(msgErrorNotExistFolder);
						mailingListService.sendEmailWithAttachment(null, IGerivMessageBundle.get("imp.move.and.work.inforiv.ftp.error.message.subject"), msgErrorNotExistFolder, null, true, null, true, null);
					}
				}
				
				} catch (Throwable e) {
					try {
						log.error(IGerivMessageBundle.get("imp.move.and.work.inforiv.ftp.error.message.subject"), e);
						String message = MessageFormat.format(IGerivMessageBundle.get("imp.move.and.work.inforiv.ftp.error.message.generic"), (e.getMessage() != null ? e.getMessage() : ""), StringUtility.getStackTrace(e));
						mailingListService.sendEmailWithAttachment(null, IGerivMessageBundle.get("imp.move.and.work.inforiv.error.message.subject"), message, null, true, null, true, null);
					} catch (Throwable e1) {
						log.error(IGerivMessageBundle.get("imp.error.send.email"), e1);
					}
				}
			log.info("End MoveAndWorkFilesFtpInputJob.executeInternal");
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

	
	public AgenzieService getAgenzieService() {
		return agenzieService;
	}

	public void setAgenzieService(AgenzieService agenzieService) {
		this.agenzieService = agenzieService;
	}

	public String getPathFilesLocalInp() {
		return pathFilesLocalInp;
	}

	public void setPathFilesLocalInp(String pathFilesLocalInp) {
		this.pathFilesLocalInp = pathFilesLocalInp;
	}


	public String getPathFolderIn() {
		return pathFolderIn;
	}

	public void setPathFolderIn(String pathFolderIn) {
		this.pathFolderIn = pathFolderIn;
	}

	
	
	
}
