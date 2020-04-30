package it.dpe.inforiv.job;

import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.dto.EdicolaDto;
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
 * 18/05/2015 - 0000059
 * Classe job 
 * 
 */
public class MoveAndWorkFilesInforivOutputJob extends QuartzJobBean {
	private final Logger log = Logger.getLogger(getClass());
	private AgenzieService agenzieService;
	private EdicoleService edicoleService;
	private MailingListService mailingListService;
	private String pathFilesLocalOut;
	private String pathFolderIn;
	private String pathFolderOut;
	private String pathFolderBkp;
	
	private InforivExportFileBo inforivExportFileBo;
	

	private FileInforivFtpImporter fileInforivFtpImporter;
	
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
			log.info("Entered MoveAndWorkFilesInforivOutputJob.executeInternal");
			try {
				//Get elenco dei dl abilitati alla modalità 2 inforiv
				List<AnagraficaAgenziaVo> listAgenzie = agenzieService.getAgenzieModalitaInforiv(); //FLGMODFTP9107 = 2
					for(AnagraficaAgenziaVo agenzia : listAgenzie){
						log.info("Agenzia in lavorazione : "+agenzia.getCodFiegDl()+" - "+agenzia.getRagioneSocialeDlPrimaRiga());
						List<EdicolaDto> listEdicoleAttive = edicoleService.getEdicoleDlAttive(agenzia.getCodFiegDl());
						log.info("Edicole attive per agenzia in lavorazione : "+listEdicoleAttive.size());
						
						if(listEdicoleAttive!=null && listEdicoleAttive.size()>0){
						for (EdicolaDto edicolaDto : listEdicoleAttive) {
							String codDlFormat = String.format("%4s", agenzia.getCodFiegDl()).replace(' ', '0');
														
							File dirOutputLocalDL 	 = new File(pathFilesLocalOut + codDlFormat + System.getProperty("file.separator") + edicolaDto.getCodEdicolaDl() + System.getProperty("file.separator")+  pathFolderIn);
							File dirOutputBKPLocalDL = new File(dirOutputLocalDL + System.getProperty("file.separator") + pathFolderBkp);
							
							if(dirOutputLocalDL.isDirectory()){
								
								if (!dirOutputBKPLocalDL.isDirectory()) {
									dirOutputBKPLocalDL.mkdirs();
								}
								inforivExportFileBo.exportInforivFileLocal(agenzia,edicolaDto.getCodEdicolaDl(),dirOutputLocalDL,dirOutputBKPLocalDL);
								
								
							}else{
								String msgErrorNotExistFolder = "Directory output ( "+dirOutputLocalDL+" ) non esistente per agenzia "+agenzia.getCodFiegDlMaster()+" - "+agenzia.getRagioneSocialeDlPrimaRiga();
								log.error(msgErrorNotExistFolder);
								mailingListService.sendEmailWithAttachment(null, IGerivMessageBundle.get("out.move.and.work.inforiv.ftp.error.message.subject"), msgErrorNotExistFolder, null, true, null, true, null);
							}
							
							}
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
			log.info("End MoveAndWorkFilesInforivOutputJob.executeInternal");
	}
	
	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

	public EdicoleService getEdicoleService() {
		return edicoleService;
	}

	public void setEdicoleService(EdicoleService edicoleService) {
		this.edicoleService = edicoleService;
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

	public String getPathFolderIn() {
		return pathFolderIn;
	}

	public void setPathFolderIn(String pathFolderIn) {
		this.pathFolderIn = pathFolderIn;
	}

	public String getPathFolderOut() {
		return pathFolderOut;
	}
	
	public void setPathFolderOut(String pathFolderOut) {
		this.pathFolderOut = pathFolderOut;
	}

	public String getPathFolderBkp() {
		return pathFolderBkp;
	}

	public void setPathFolderBkp(String pathFolderBkp) {
		this.pathFolderBkp = pathFolderBkp;
	}
	
	
	

}
