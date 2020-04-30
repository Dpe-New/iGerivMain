package it.dpe.igeriv.job;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.resources.ExposablePropertyPaceholderConfigurer;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.mail.MailingListService;

/**
 * Classe job che:
 * Richiama la Stored Procedure dedita alla cancellazione delle pubblicazioni uscite prima di una certa data ( 12 mesi / 24 mesi ).
 * 
 */

public class IGerivManutenzionePubblicazioniJob extends QuartzJobBean {

	private final Logger log = Logger.getLogger(getClass());
	private IGerivBatchService iGerivBatchBo;
	private AgenzieService agenzieService;
	private MailingListService mailingListService;
	private ExposablePropertyPaceholderConfigurer props;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("Entered IGerivManutenzionePubblicazioniJob.executeInternal");
		List<AnagraficaAgenziaVo> listAgenzie = agenzieService.getAgenzie();
		for (AnagraficaAgenziaVo agenzia : listAgenzie) {
			iGerivBatchBo.callStoredProcedure_ManutenzionePubblicazioni(agenzia.getCodFiegDl());
		}
		log.info("Exiting IGerivManutenzionePubblicazioniJob.executeInternal");
	}

	
	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

	public ExposablePropertyPaceholderConfigurer getProps() {
		return props;
	}

	public void setProps(ExposablePropertyPaceholderConfigurer props) {
		this.props = props;
	}


	public IGerivBatchService getiGerivBatchBo() {
		return iGerivBatchBo;
	}


	public void setiGerivBatchBo(IGerivBatchService iGerivBatchBo) {
		this.iGerivBatchBo = iGerivBatchBo;
	}


	public AgenzieService getAgenzieService() {
		return agenzieService;
	}


	public void setAgenzieService(AgenzieService agenzieService) {
		this.agenzieService = agenzieService;
	}
	
	
}
