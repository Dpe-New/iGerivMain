package it.dpe.inforiv.job.out;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.inforiv.bo.InforivExportFileBo;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Classe job schedulata che esporta i dati verso gli FTP Inforiv 
 * 
 * @author Marcello Romano - DPE
 */
public class InforivExportJob extends QuartzJobBean {
	private final Logger log = Logger.getLogger(getClass());
	private InforivExportFileBo inforivExportFileBo;
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			log.info("Export Inforiv - Inizio");
			inforivExportFileBo.exportInforiv();
			log.info("Export Inforiv - Fine");
		} catch (Throwable e) {
			log.error(IGerivMessageBundle.get("esp.dati.inforiv.fatal.error"), e);
		}
	}

	public InforivExportFileBo getInforivExportFileBo() {
		return inforivExportFileBo;
	}

	public void setInforivExportFileBo(InforivExportFileBo inforivExportFileBo) {
		this.inforivExportFileBo = inforivExportFileBo;
	}

}
