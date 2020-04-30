package it.dpe.igeriv.job;

import java.text.MessageFormat;

import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.mail.MailingListService;
import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Getter
@Setter
public class IGerivTodisDeviettiBarcodeUpdateJob extends QuartzJobBean {
	private final Logger log = Logger.getLogger(getClass());
	private PubblicazioniService pubblicazioniService;
	private MailingListService mailingListService;
	private String daysUpdateBarcodeTodis;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("Entered IGerivTodisDeviettiBarcodeUpdateJob.executeInternal");
		try {
			pubblicazioniService.updateBarcodeTodisConDevietti(new Integer(daysUpdateBarcodeTodis));
		} catch (Throwable e) {
			try {
				log.error(IGerivMessageBundle.get("msg.subject.errore.update.barcode.todis.devietti"), e);
				mailingListService.sendEmail(null, IGerivMessageBundle.get("msg.subject.errore.update.barcode.todis.devietti"), 
						MessageFormat.format(IGerivMessageBundle.get("msg.update.barcode.todis.devietti"), new Object[]{StringUtility.getStackTrace(e)}), true);
			} catch (Throwable e1) {
    			log.error(IGerivMessageBundle.get("imp.error.send.email"), e1);
    		}
		}
		log.info("Entered IGerivTodisDeviettiBarcodeUpdateJob.executeInternal");
	}

}
