package it.dpe.inforiv.bo.impl;

import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.bo.inforiv.InforivExportService;
import it.dpe.igeriv.bo.ws.IGerivWSService;
import it.dpe.igeriv.dto.Edicola;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.dto.ImportazioneInforivReplyDto;
import it.dpe.igeriv.exception.EdicolaGiaEsistenteException;
import it.dpe.igeriv.exception.EdicolaInforivFtpException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.inforiv.bo.InforivImportBo;
import it.dpe.service.mail.MailingListService;

import java.text.MessageFormat;
import java.util.List;

import models.ResultSaveDto;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implementazione dell'interfaccia BO per l'importazione dati inforiv. 
 * 
 * @author romanom
 * 
 */
@Component("InforivImportBo")
@SuppressWarnings("null")
public class InforivImportBoImpl implements InforivImportBo {
	private final Logger log = Logger.getLogger(getClass());
	
	@Autowired
	private IGerivBatchService iGerivBatchBo;
	@Autowired
	private IGerivWSService iGerivWSService;
	@Autowired
	private InforivExportService inforivExportService;
	@Autowired
	private MailingListService mailingListService;
	@Value("${igeriv.inforiv.remote.working.dir}")
	private String remoteWorkingDir;
	@Value("${igeriv.inforiv.scheduler.cron.expr}")
	private String cronExpr;
	@Value("${inforiv.file.import.path.dir}")
	private String fileImportPathDir;
	@Value("${inforiv.zip.file.import.path.dir}")
	private String importZipPathDir;
	@Value("${igeriv.url}")
	private String igerivUrl;
	@Value("${inforiv.numero.giorni.trial.version}")
	private String giorniTrial;
	
	public InforivImportBoImpl() {
		System.out.println("Init import ftp");
	}
	
	@Override
	public ImportazioneInforivReplyDto importEdicolaInforiv(Edicola edicola) {
		ImportazioneInforivReplyDto reply = null;
		try {	
			reply = inforivExportService.importEdicola(edicola, false, new Integer(giorniTrial), igerivUrl, true, false);
			mailingListService.sendEmail(edicola.getEmail(), IGerivMessageBundle.get("msg.subject.nuovo.utente.edicola"), reply.getEmailMessage(), null);
		} catch (EdicolaInforivFtpException e) {
			reply.setStato(IGerivConstants.STATO_IMPORTAZIONE_EDICOLA_INFORIV_CONCLUSO_ERRORE);
			reply.setMessaggio(IGerivMessageBundle.get("msg.importazione.edicola.inforiv.ftp.invalido"));
		} catch (EdicolaGiaEsistenteException e) {
			reply.setStato(IGerivConstants.STATO_IMPORTAZIONE_EDICOLA_INFORIV_CONCLUSO_WARNING);
			reply.setMessaggio(MessageFormat.format(IGerivMessageBundle.get("msg.importazione.edicola.inforiv.gia.esistente"), igerivUrl));
		} catch (Exception e) {
			reply.setStato(IGerivConstants.STATO_IMPORTAZIONE_EDICOLA_INFORIV_CONCLUSO_ERRORE);
			reply.setMessaggio(IGerivMessageBundle.get("msg.importazione.edicola.errore.fatale"));
			log.error(IGerivMessageBundle.get("msg.importazione.edicola.errore.fatale"), e);
			String message = MessageFormat.format(IGerivMessageBundle.get("msg.email.dpe.errore.fatale.importazione.edicola.inforiv"), edicola.getCodDl(), edicola.getCodEdicolaDl(), edicola.getRagioneSociale(), StringUtility.getStackTrace(e));
			mailingListService.sendEmail(null, IGerivMessageBundle.get("msg.email.dpe.errore.fatale.importazione.edicola.inforiv.subject"), message, null);
		}
		return reply;
	}
	
	@Override
	public ResultSaveDto importEdicolaNetEdicola(Edicola edicola) {
		ResultSaveDto dto = new ResultSaveDto();
		ImportazioneInforivReplyDto reply = null;
		try {	
			reply = inforivExportService.importEdicola(edicola, true, new Integer(giorniTrial), igerivUrl, true, false);
			mailingListService.sendEmail(edicola.getEmail(), IGerivMessageBundle.get("msg.subject.nuovo.utente.edicola"), reply.getEmailMessage(), null);
		} catch (EdicolaInforivFtpException e) {
			reply.setStato(IGerivConstants.STATO_IMPORTAZIONE_EDICOLA_INFORIV_CONCLUSO_ERRORE);
			reply.setMessaggio(IGerivMessageBundle.get("msg.importazione.edicola.inforiv.ftp.invalido"));
		} catch (EdicolaGiaEsistenteException e) {
			reply.setStato(IGerivConstants.STATO_IMPORTAZIONE_EDICOLA_INFORIV_CONCLUSO_WARNING);
			reply.setMessaggio(MessageFormat.format(IGerivMessageBundle.get("msg.importazione.edicola.inforiv.gia.esistente"), igerivUrl));
		} catch (Exception e) {
			reply.setStato(IGerivConstants.STATO_IMPORTAZIONE_EDICOLA_INFORIV_CONCLUSO_ERRORE);
			reply.setMessaggio(IGerivMessageBundle.get("msg.importazione.edicola.errore.fatale"));
			log.error(IGerivMessageBundle.get("msg.importazione.edicola.errore.fatale"), e);
			String message = MessageFormat.format(IGerivMessageBundle.get("msg.email.dpe.errore.fatale.importazione.edicola.inforiv"), edicola.getCodDl(), edicola.getCodEdicolaDl(), edicola.getRagioneSociale(), StringUtility.getStackTrace(e));
			mailingListService.sendEmail(null, IGerivMessageBundle.get("msg.email.dpe.errore.fatale.importazione.edicola.inforiv.subject"), message, null);
		}
		if (reply != null) {
			dto.setType(reply.getStato().equals(IGerivConstants.STATO_IMPORTAZIONE_EDICOLA_INFORIV_CONCLUSO_SUCCESS) ? ResultSaveDto.ResultType.SUCCESS : (reply.getStato().equals(IGerivConstants.STATO_IMPORTAZIONE_EDICOLA_INFORIV_CONCLUSO_WARNING) ? ResultSaveDto.ResultType.WARNING : ResultSaveDto.ResultType.ERROR));
			dto.setMessage(reply.getMessaggio());
		}
		return dto;
	}
	

	@Override
	public List<EdicolaDto> getEdicoleInforiv() {
		return iGerivBatchBo.getEdicoleInforiv();
	}

	public IGerivWSService getiGerivWSService() {
		return iGerivWSService;
	}

	public void setiGerivWSService(IGerivWSService iGerivWSService) {
		this.iGerivWSService = iGerivWSService;
	}

	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

	public String getRemoteWorkingDir() {
		return remoteWorkingDir;
	}

	public void setRemoteWorkingDir(String remoteWorkingDir) {
		this.remoteWorkingDir = remoteWorkingDir;
	}

	public String getCronExpr() {
		return cronExpr;
	}

	public void setCronExpr(String cronExpr) {
		this.cronExpr = cronExpr;
	}

	public String getFileImportPathDir() {
		return fileImportPathDir;
	}

	public void setFileImportPathDir(String fileImportPathDir) {
		this.fileImportPathDir = fileImportPathDir;
	}
	
	public String getImportZipPathDir() {
		return importZipPathDir;
	}

	public void setImportZipPathDir(String importZipPathDir) {
		this.importZipPathDir = importZipPathDir;
	}

	public IGerivBatchService getiGerivBatchBo() {
		return iGerivBatchBo;
	}

	public void setiGerivBatchBo(IGerivBatchService iGerivBatchBo) {
		this.iGerivBatchBo = iGerivBatchBo;
	}

	public InforivExportService getInforivExportService() {
		return inforivExportService;
	}

	public void setInforivExportService(InforivExportService inforivExportService) {
		this.inforivExportService = inforivExportService;
	}

	public String getIgerivUrl() {
		return igerivUrl;
	}

	public void setIgerivUrl(String igerivUrl) {
		this.igerivUrl = igerivUrl;
	}

	public String getGiorniTrial() {
		return giorniTrial;
	}

	public void setGiorniTrial(String giorniTrial) {
		this.giorniTrial = giorniTrial;
	}
	
}
