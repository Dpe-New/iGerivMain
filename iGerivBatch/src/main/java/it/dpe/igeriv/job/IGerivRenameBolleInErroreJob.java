package it.dpe.igeriv.job;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.mail.MailingListService;

import java.io.File;
import java.io.FileFilter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.google.common.base.Strings;

/**
 * Classe job che rinomina le bolle andate in errore (._ERR) con l'estanzione .DAT
 * e tiene traccia delle bolle rinominate in modo da fermarsi al 3° tentativo.
 * 
 * @author romanom
 */
@Getter
@Setter
public class IGerivRenameBolleInErroreJob extends QuartzJobBean {
	private final Logger log = Logger.getLogger(getClass());
	private static final Map<String, Integer> mapBolle = new HashMap<String, Integer>();
	private AgenzieService agenzieService;
	private MailingListService mailingListService;
	private String datiPathDir;
	private String logPathDir;
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		log.info("Entered IGerivRenameBolleInErroreJob.executeInternal");
		try {
			File dir = new File(datiPathDir);
			File[] files = dir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname != null && pathname.isFile() && (pathname.getName().contains("._ERR") || pathname.getName().contains("._err"));
				}
			});
			Set<String> fileNames = new HashSet<String>(extract(files, on(File.class).getName()));
			mapBolle.keySet().retainAll(fileNames);
			for (File file : files) {
				String name = file.getName();
				Integer mapBollaTries = mapBolle.get(name);
				Integer numTries = mapBollaTries != null ? mapBollaTries : 0;
				if (numTries < 3) {
					file.renameTo(new File(dir, name.replace("._ERR", "").replace("._err", "")));
				} else if (numTries.equals(3)) {
					String codDpeWebDlStr = StringUtils.countMatches(name, "_") >= 2 ? name.substring(name.indexOf("_") + 1, StringUtils.ordinalIndexOf(name, "_", 2)) : null;
					Integer codDpeWebDl = !Strings.isNullOrEmpty(codDpeWebDlStr) && NumberUtils.isNumber(codDpeWebDlStr) ? new Integer(codDpeWebDlStr) : null;
					AnagraficaAgenziaVo dl = codDpeWebDl != null ? agenzieService.getAgenziaByCodiceDpeWeb(codDpeWebDl) : null;
					String ragioneSocialeDlPrimaRiga = dl != null ? dl.getRagioneSocialeDlPrimaRiga() : "";
					String codDl = dl != null ? dl.getCodFiegDl().toString() : "";
					mailingListService.sendEmailWithAttachment(
						new String[]{"vittorio.bassignani@dpe.it","maurizio.costa@dpe.it"}, 
						MessageFormat.format(IGerivMessageBundle.get("msg.subject.errore.importazione.file.dl.rename"), new Object[]{name, codDl + " - " + ragioneSocialeDlPrimaRiga}), 
						MessageFormat.format(IGerivMessageBundle.get("msg.errore.rename.file"), new Object[]{file.getAbsolutePath(), (datiPathDir + "/RICEZIONE_LOG.TXT"), (logPathDir + "/igeriv_batch_application.log")}),  
						null, true, null, true, null);
				}
				mapBolle.put(name, ++numTries);
			}
		} catch (Throwable e) {
			try {
				log.error(IGerivMessageBundle.get("msg.subject.errore.rename.bolle.in.errore"), e);
				mailingListService.sendEmail(null, IGerivMessageBundle.get("msg.subject.errore.rename.bolle.in.errore"), 
						MessageFormat.format(IGerivMessageBundle.get("msg.errore.rename.bolle.in.errore"), new Object[]{StringUtility.getStackTrace(e)}), true);
			} catch (Throwable e1) {
    			log.error(IGerivMessageBundle.get("imp.error.send.email"), e1);
    		}
		}
		log.info("Exiting IGerivRenameBolleInErroreJob.executeInternal");
	}
	
}
