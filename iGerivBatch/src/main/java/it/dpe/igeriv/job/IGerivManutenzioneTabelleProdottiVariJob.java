package it.dpe.igeriv.job;

import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.resources.ExposablePropertyPaceholderConfigurer;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.mail.MailingListService;

import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Classe job che:
 * 1. Sposta in una cartella di backup i file delle immagini non più presenti nelle tabelle dei prodotti, categorie e sottocategorie prodotti vari dell'edicola.
 * 2. Cancella dal db le bolle di carico dei prodotti vari precedenti al parametro in giorni definito per singola edicola.
 * 
 * @author romanom
 */
public class IGerivManutenzioneTabelleProdottiVariJob extends QuartzJobBean {
	private final Logger log = Logger.getLogger(getClass());
	private IGerivBatchService iGerivBatchBo;
	private MailingListService mailingListService;
	private ExposablePropertyPaceholderConfigurer props;
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		log.info("Entered IGerivManutenzioneTabelleProdottiVariJob.executeInternal");
		try {
			iGerivBatchBo.cleanupNonUsedImagesProdottiVari();
			iGerivBatchBo.cleanupOldImagesPubblicazioni();
			//iGerivBatchBo.deleteBolleProdottiVari();
		} catch (Exception e) {
			try {
				String deployName =  props.getResolvedProps().get("igeriv.env.deploy.name");
				log.error(IGerivMessageBundle.get("msg.subject.errore.manutenzione.immagini.prodotti.vari"), e);
				mailingListService.sendEmail(null, IGerivMessageBundle.get("msg.subject.errore.manutenzione.immagini.prodotti.vari"), 
						MessageFormat.format(deployName+" - "+IGerivMessageBundle.get("msg.errore.manutenzione.immagini.prodotti.vari"), new Object[]{StringUtility.getStackTrace(e)}), true);
			} catch (Throwable e1) {
    			log.error(IGerivMessageBundle.get("imp.error.send.email"), e1);
    		}
		}
		log.info("Exiting IGerivManutenzioneTabelleProdottiVariJob.executeInternal");
	}
	
	public IGerivBatchService getiGerivBatchBo() {
		return iGerivBatchBo;
	}

	public void setiGerivBatchBo(IGerivBatchService iGerivBatchBo) {
		this.iGerivBatchBo = iGerivBatchBo;
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
	
	
	
}
