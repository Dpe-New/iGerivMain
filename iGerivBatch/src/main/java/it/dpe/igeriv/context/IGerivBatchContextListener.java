package it.dpe.igeriv.context;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.SpringContextManager;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.context.ContextLoaderListener;

/**
 * Classe che ascolta l'oggetto javax.servlet.ServletContext,
 * esegue operazioni di inizializzazione e setta variabili 
 * nel contesto "application".
 * 
 * @author romanom
 *
 */
public class IGerivBatchContextListener extends ContextLoaderListener {
	private final Logger log = Logger.getLogger(getClass());
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		IGerivMessageBundle.initialize();
		super.contextInitialized(event);
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		SchedulerFactoryBean fileInforivImportCronTriggerScheduler = SpringContextManager.getSpringContext().getBean("fileInforivImportCronTriggerScheduler", SchedulerFactoryBean.class);
		SchedulerFactoryBean manutenzioneTabelleProdottiVariScheduler = SpringContextManager.getSpringContext().getBean("manutenzioneTabelleProdottiVariScheduler", SchedulerFactoryBean.class);
		SchedulerFactoryBean inforivExportCronTriggerScheduler = SpringContextManager.getSpringContext().getBean("inforivExportCronTriggerScheduler", SchedulerFactoryBean.class);
		SchedulerFactoryBean prodottiExportCronTriggerScheduler = SpringContextManager.getSpringContext().getBean("prodottiExportCronTriggerScheduler", SchedulerFactoryBean.class);
		SchedulerFactoryBean bolleResaProdottiVariExportCronTriggerScheduler = SpringContextManager.getSpringContext().getBean("bolleResaProdottiVariExportCronTriggerScheduler", SchedulerFactoryBean.class);
		SchedulerFactoryBean renameBolleInErroreCronTriggerScheduler = SpringContextManager.getSpringContext().getBean("renameBolleInErroreCronTriggerScheduler", SchedulerFactoryBean.class);
		
		SchedulerFactoryBean moveAndWorkFilesInforivFtpInputCronTriggerScheduler = SpringContextManager.getSpringContext().getBean("moveAndWorkFilesInforivFtpInputCronTriggerScheduler", SchedulerFactoryBean.class);
		SchedulerFactoryBean moveAndWorkFilesInforivFtpOutputCronTriggerScheduler = SpringContextManager.getSpringContext().getBean("moveAndWorkFilesInforivFtpOutputCronTriggerScheduler", SchedulerFactoryBean.class);
		
		
		
		try {
			if (fileInforivImportCronTriggerScheduler != null) {
				fileInforivImportCronTriggerScheduler.getScheduler().shutdown(true);
			}
			if (manutenzioneTabelleProdottiVariScheduler != null) {
				manutenzioneTabelleProdottiVariScheduler.getScheduler().shutdown(true);
			}
			if (inforivExportCronTriggerScheduler != null) {
				inforivExportCronTriggerScheduler.getScheduler().shutdown(true);
			}
			if (prodottiExportCronTriggerScheduler != null) {
				prodottiExportCronTriggerScheduler.getScheduler().shutdown(true);
			}
			if (bolleResaProdottiVariExportCronTriggerScheduler != null) {
				bolleResaProdottiVariExportCronTriggerScheduler.getScheduler().shutdown(true);
			}
			if (renameBolleInErroreCronTriggerScheduler != null) {
				renameBolleInErroreCronTriggerScheduler.getScheduler().shutdown(true);
			}
			if (moveAndWorkFilesInforivFtpInputCronTriggerScheduler != null) {
				moveAndWorkFilesInforivFtpInputCronTriggerScheduler.getScheduler().shutdown(true);
			}
			if (moveAndWorkFilesInforivFtpOutputCronTriggerScheduler != null) {
				moveAndWorkFilesInforivFtpOutputCronTriggerScheduler.getScheduler().shutdown(true);
			}
		} catch (SchedulerException e) {
			log.error("error in IGerivBatchContextListener", e);
		}
		super.contextDestroyed(event);
	}
	
}
