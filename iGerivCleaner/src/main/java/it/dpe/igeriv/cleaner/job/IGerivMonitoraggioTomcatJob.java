package it.dpe.igeriv.cleaner.job;

import it.dpe.igeriv.cleaner.mail.MailingListService;
import it.dpe.igeriv.cleaner.resources.IGerivMessageBundle;
import it.dpe.igeriv.cleaner.util.MsWinSvc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.google.common.base.Strings;

public class IGerivMonitoraggioTomcatJob  extends QuartzJobBean{

	private final Logger log = Logger.getLogger(getClass());
	private MailingListService mailingListService;
	private String tomcatDirs;
	private String tomcatWinServiceNames;
	private String tomcatWinServiceBatchName;
	
	@Override
	protected void executeInternal(JobExecutionContext context)throws JobExecutionException {
		boolean okMail = true;
		log.info("** Start ** IGerivMonitoraggioTomcatJob.executeInternal");
		
		log.info(tomcatDirs);
		log.info(tomcatWinServiceNames);
		log.info(tomcatWinServiceBatchName);
		
		String[] services;
		if (!Strings.isNullOrEmpty(tomcatWinServiceNames)) {
			services = tomcatWinServiceNames.split(",");
			if (services != null && services.length > 0) {
				Runtime r = Runtime.getRuntime();
				for (String serviceName : services) {
					try {
						boolean ver = isTomcatInstanceMemoryUsageTooHigh(serviceName);
						if(!ver){
							try {
								MsWinSvc.startService(serviceName);
								log.info(" *** START SERVICE *** ( "+serviceName+" )");
								Thread.sleep(60000);
								mailingListService.sendEmail(null, "Monitoraggio TomcatJob "+IGerivMessageBundle.get("msg.subject.errore.manutenzione.tomcat")," *** START SERVICE *** ( "+serviceName+" )" , true);
							
							} catch (Throwable e1) {
								log.error("Fatal error ocurred during service " + serviceName + " startup", e1);
								
							}
							okMail = false;
						}
					
					} catch (IOException e) {
						e.printStackTrace();
					}
				
				}
				if(okMail){
					mailingListService.sendEmail(null, "Monitoraggio TomcatJob "," Monitoraggio completato con successo" , true);
				}
			}
		}
		
		
		
		log.info("** End ** IGerivMonitoraggioTomcatJob.executeInternal");
		
	}

	
	/**
	 * Ritorna true se una delle istanze tomcat 1,2 o 3 ha superato il limite massimo di memoria definito nel parametro maxMemorySize
	 * 
	 * @return
	 * @throws IOException
	 */
	private boolean isTomcatInstanceMemoryUsageTooHigh(String serviceName) throws IOException {
	
		Runtime r = Runtime.getRuntime();
		BufferedReader input = null;
		try {
			log.info("TASKLIST /fi \"Services eq " + serviceName + "\"");
			Process p = r.exec("TASKLIST /fi \"Services eq " + serviceName+ "\"");
			input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				if (!Strings.isNullOrEmpty(line) && line.contains(" K")) {
					String replace = line.replace(" K", "");
					String usedMemoryBytesStr = replace.substring(replace.lastIndexOf(" ")).trim().replaceAll("\\.", "");
					log.info("usedMemoryBytesStr=" + usedMemoryBytesStr);
					log.info("returning true");
					input.close();
					return true;
				}
			}
		} finally {
			if (input != null) {
				input.close();
			}
		}
				
		return false;
	}
	
	
	
	public String getTomcatDirs() {
		return tomcatDirs;
	}

	public void setTomcatDirs(String tomcatDirs) {
		this.tomcatDirs = tomcatDirs;
	}

	public String getTomcatWinServiceNames() {
		return tomcatWinServiceNames;
	}

	public void setTomcatWinServiceNames(String tomcatWinServiceNames) {
		this.tomcatWinServiceNames = tomcatWinServiceNames;
	}

	public String getTomcatWinServiceBatchName() {
		return tomcatWinServiceBatchName;
	}

	public void setTomcatWinServiceBatchName(String tomcatWinServiceBatchName) {
		this.tomcatWinServiceBatchName = tomcatWinServiceBatchName;
	}

	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}
	
	
}
