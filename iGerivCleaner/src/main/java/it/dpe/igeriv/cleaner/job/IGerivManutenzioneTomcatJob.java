package it.dpe.igeriv.cleaner.job;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;
import it.dpe.igeriv.cleaner.exception.IGerivBusinessException;
import it.dpe.igeriv.cleaner.mail.MailingListService;
import it.dpe.igeriv.cleaner.resources.IGerivMessageBundle;
import it.dpe.igeriv.cleaner.util.IGerivCleanerFileUtils;
import it.dpe.igeriv.cleaner.util.IGerivCleanerStringUtility;
import it.dpe.igeriv.cleaner.util.MsWinSvc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import ch.lambdaj.group.Group;

import com.google.common.base.Strings;

/**
 * Classe job che:
 * 1. Ferma i servizi tomcat
 * 2. Cancella le cartelle igeriv- da ciascuna istanza tomcat (se ne esiste piu' di una con lo stesso nome)
 * 3. Sposta i file .war dell'igeriv in una cartella di backup (o li cancella in caso di errore) lasciando soltanto il file .war più recente
 * 4. Fa ripartire i serivizi tomcat
 * 
 * @author romanom
 */
public class IGerivManutenzioneTomcatJob extends QuartzJobBean {
	private final Logger log = Logger.getLogger(getClass());
	private MailingListService mailingListService;
	private String tomcatDirs;
	private String tomcatWinServiceNames;
	private String igerivFilesPrefixes;
	private String igerivBkpDir;
	private String envDeployName;
	private String enabledEnvNames;
	private String maxMemorySize;
	private Boolean cleanLogFilesAtStartup; 
	private String logFolderPath;
	private String tomcatWinServiceBatchName;
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		log.info("Entered IGerivManutenzioneTomcatJob.executeInternal");
		try {
			if (!Strings.isNullOrEmpty(tomcatDirs) && !Strings.isNullOrEmpty(tomcatWinServiceNames)) {
				List<String> enabledAppNameList = Arrays.asList(enabledEnvNames.split(","));
				final List<String> listAppPrefixes = Arrays.asList(igerivFilesPrefixes.split(","));
				boolean hasAppsToDelete = getHasAppsToDelete(listAppPrefixes);
				if (hasAppsToDelete || enabledAppNameList.contains(envDeployName) || isTomcatInstanceMemoryUsageTooHigh()) {
					Map<String, String> mapDirsWinServiceNames = buildMapDirsWinServiceNames();
					stopServices(mapDirsWinServiceNames);
					cleanLogFiles();
					for (Map.Entry<String, String> entry : mapDirsWinServiceNames.entrySet()) {
						String winServiceName = entry.getKey();
						String tomcatDir = entry.getValue();
						List<TomcatFile> listAppDirs = getTomcatAppDirsToDelete(tomcatDir, listAppPrefixes);
						List<TomcatFile> listAppWars = getTomcatAppWarsToBackupAndDelete(tomcatDir, listAppPrefixes);
						cleanTomcatDir(tomcatDir, listAppPrefixes, listAppDirs, listAppWars);
						MsWinSvc.startService(winServiceName);
					}
					MsWinSvc.stopService(tomcatWinServiceBatchName);
					MsWinSvc.startService(tomcatWinServiceBatchName);
				}
			}
		} catch (IGerivBusinessException e) {
			log.error(IGerivMessageBundle.get("msg.subject.errore.manutenzione.tomcat"), e);
			mailingListService.sendEmail(null, IGerivMessageBundle.get("msg.subject.errore.manutenzione.tomcat"), 
					MessageFormat.format(IGerivMessageBundle.get("msg.errore.errore.manutenzione.tomcat"), 
					new Object[] { e.getMessage() }),
					true);
			startServices();
		} catch (Throwable e) {
			log.error(IGerivMessageBundle.get("msg.subject.errore.manutenzione.tomcat"), e);
			mailingListService.sendEmail(null, IGerivMessageBundle.get("msg.subject.errore.manutenzione.tomcat"), 
					MessageFormat.format(IGerivMessageBundle.get("msg.errore.errore.manutenzione.tomcat"), 
					new Object[] { IGerivCleanerStringUtility.getStackTrace(e) }),
					true);
			startServices();
		}
		log.info("Exiting IGerivManutenzioneTomcatJob.executeInternal");
	}

	/**
	 * Ritorna true se una delle istanze tomcat 1,2 o 3 ha superato il limite massimo di memoria definito nel parametro maxMemorySize
	 * 
	 * @return
	 * @throws IOException
	 */
	private boolean isTomcatInstanceMemoryUsageTooHigh() throws IOException {
		String[] services;
		if (!Strings.isNullOrEmpty(tomcatWinServiceNames)) {
			services = tomcatWinServiceNames.split(",");
			if (services != null && services.length > 0) {
				Runtime r = Runtime.getRuntime();
				for (String serviceName : services) {
					BufferedReader input = null;
					try {
						log.info("TASKLIST /fi \"Services eq " + serviceName + "\"");
				    	Process p = r.exec("TASKLIST /fi \"Services eq " + serviceName + "\"");
				    	input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				    	String line;
				        while ((line = input.readLine()) != null) {
				        	if (!Strings.isNullOrEmpty(line) && line.contains(" K")) {
				        		String replace = line.replace(" K", "");
								String usedMemoryBytesStr = replace.substring(replace.lastIndexOf(" ")).trim().replaceAll("\\.", "");
				        		log.info("usedMemoryBytesStr=" + usedMemoryBytesStr + " maxMemorySize=" + maxMemorySize);
				        		if (!Strings.isNullOrEmpty(usedMemoryBytesStr) && NumberUtils.isNumber(usedMemoryBytesStr)) {
									Long memory = Long.parseLong(usedMemoryBytesStr);
					        		if (memory > Long.parseLong(maxMemorySize)) {
					        			log.info("returning true");
					        			input.close();
					        			return true;
					        		}
				        		}
				        	}
				        } 
					} finally {
						if (input != null) {
							input.close();
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Ritorna true se esiste almeno una cartella o un .war da cancellare dalle istanze tomcat
	 * 
	 * @param List<String> listAppPrefixes
	 * @return boolean
	 * @throws IGerivBusinessException
	 */
	private boolean getHasAppsToDelete(List<String> listAppPrefixes) throws IGerivBusinessException {
		String[] tomcatDirNames = tomcatDirs.split(",");
		for (int i = 0; i < tomcatDirNames.length; i++) {
			List<TomcatFile> tomcatAppDirsToDelete = getTomcatAppDirsToDelete(tomcatDirNames[i], listAppPrefixes);
			List<TomcatFile> tomcatAppWarsToBackupAndDelete = getTomcatAppWarsToBackupAndDelete(tomcatDirNames[i], listAppPrefixes); 
			if (tomcatAppDirsToDelete.size() > 0 || tomcatAppWarsToBackupAndDelete.size() > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Arresta i servizi tomcat che accedono al service layer 
	 * 
	 * @param mapDirsWinServiceNames
	 * @throws Exception
	 */
	private void stopServices(Map<String, String> mapDirsWinServiceNames) throws Exception {
		for (Map.Entry<String, String> entry : mapDirsWinServiceNames.entrySet()) {
			MsWinSvc.stopService(entry.getKey());
			log.debug(" * STOP SERVICE - "+entry.getKey());
			Thread.sleep(100000);
		}
	}
	
	/**
	 * Starta un servizio
	 */
	private void startServices() {
		String[] services;
		if (!Strings.isNullOrEmpty(tomcatWinServiceNames)) {
			services = tomcatWinServiceNames.split(",");
			if (services != null && services.length > 0) {
				for (String serviceName : services) {
					try {
						MsWinSvc.startService(serviceName);
					} catch (Throwable e1) {
						log.error("Fatal error ocurred during service " + serviceName + " startup", e1);
					}
				}
			}
		}
		try {
			MsWinSvc.startService(tomcatWinServiceBatchName);
		} catch (Throwable e1) {
			log.error("Fatal error ocurred during service " + tomcatWinServiceBatchName + " startup", e1);
		}
	}

	/**
	 * Ritorna le cartelle da cancellare di una istanza tomcat
	 * 
	 * @param String tomcarDirPath
	 * @return File[]
	 */
	private List<TomcatFile> getTomcatAppDirsToDelete(String tomcarDirPath, final List<String> listAppPrefixes) {
		List<TomcatFile> dirsToDelete = new ArrayList<TomcatFile>();
		FileFilter fileFilterDir = new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file != null && !Strings.isNullOrEmpty(file.getName()) && listAppPrefixes.size() > 0 && file.isDirectory() && listAppPrefixes.contains(file.getName().substring(0, file.getName().indexOf("##") + 2));
			}
		};
		List<TomcatFile> list = buildTomcatDirFileList(Arrays.asList(new File(tomcarDirPath).listFiles(fileFilterDir)));
		if (list.size() > 0) {
			Group<TomcatFile> group = group(list, by(on(TomcatFile.class).getTomcatName()));
			for (Group<TomcatFile> subgroup : group.subgroups()) {
				List<TomcatFile> findAll = sort(subgroup.findAll(), on(TomcatFile.class).getTomcatIndex());
				if (findAll.size() > 1) {
					findAll.subList(findAll.size() - 1, findAll.size()).clear();
					dirsToDelete.addAll(findAll);
				}
			}
		}
		return dirsToDelete;
	}
	
	/**
	 * Ritorna i files .war di una istanza tomcat da backuppare e cancellare
	 * 
	 * @param String tomcarDirPath
	 * @return File[]
	 */
	private List<TomcatFile> getTomcatAppWarsToBackupAndDelete(String tomcarDirPath, final List<String> listAppPrefixes) {
		List<TomcatFile> dirsToDelete = new ArrayList<TomcatFile>();
		FileFilter fileFilterDir = new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file != null && !Strings.isNullOrEmpty(file.getName()) && listAppPrefixes.size() > 0 && file.isFile() && listAppPrefixes.contains(file.getName().substring(0, file.getName().indexOf("##") + 2));
			}
		};
		List<TomcatFile> list = buildTomcatDirFileList(Arrays.asList(new File(tomcarDirPath).listFiles(fileFilterDir)));
		if (list.size() > 0) {
			Group<TomcatFile> group = group(list, by(on(TomcatFile.class).getTomcatName()));
			for (Group<TomcatFile> subgroup : group.subgroups()) {
				List<TomcatFile> findAll = sort(subgroup.findAll(), on(TomcatFile.class).getTomcatIndex());
				if (findAll.size() > 1) {
					findAll.subList(findAll.size() - 1, findAll.size()).clear();
					dirsToDelete.addAll(findAll);
				}
			}
		}
		return dirsToDelete;
	}
	
	/**
	 * @return
	 * @throws IGerivBusinessException
	 */
	private Map<String, String> buildMapDirsWinServiceNames() throws IGerivBusinessException {
		Map<String, String> map = new HashMap<String, String>();
		String[] arrWinServiceNames = tomcatWinServiceNames.split(",");
		String[] tomcatDirNames = tomcatDirs.split(",");
		if (arrWinServiceNames.length != tomcatDirNames.length) {
			throw new IGerivBusinessException(IGerivMessageBundle.get("igeriv.errore.configurazione.instanze.tomcat"));
		}
		for (int i = 0; i < arrWinServiceNames.length; i++) {
			map.put(arrWinServiceNames[i], tomcatDirNames[i]);
		}
		return map;
	}
	
	/**
	 * @param list
	 * @return
	 */
	private List<TomcatFile> buildTomcatDirFileList(List<File> list) {
		List<TomcatFile> tomcatFileList = new ArrayList<IGerivManutenzioneTomcatJob.TomcatFile>();
		for (File file : list) {
			TomcatFile tfile = new TomcatFile(file.getAbsolutePath());
			tomcatFileList.add(tfile);
		}
		return tomcatFileList;
	}

	/**
	 * Esegue le operazioni di manutenzione sulla cartella dell'istanza tomcat
	 * 
	 * @param String tomcatDir
	 * @param List<String> listAppPrefixes
	 * @param List<File> listAppDirs 
	 */
	private void cleanTomcatDir(String tomcatDir, List<String> listAppPrefixes, List<TomcatFile> listAppDirs, List<TomcatFile> listAppFiles) {
		cleanDirectories(listAppDirs);
		cleanWarFiles(listAppFiles);
	}

	/**
	 * Cancella le cartelle dell'igeriv.
	 * 
	 * @param List<File> list 
	 */
	private void cleanDirectories(List<TomcatFile> list) {
		for (File file : list) {
			log.info("deleting directory: " + file.getName());
			if (!IGerivCleanerFileUtils.deleteDir(file)) {
				log.error("error deleting dir " + file.getName() + " - sending alert email");
				mailingListService.sendEmail(null, IGerivMessageBundle.get("msg.subject.errore.manutenzione.tomcat"), MessageFormat.format(IGerivMessageBundle.get("msg.errore.errore.manutenzione.tomcat.delete.dir"), new Object[] { file.getName() }), true);
			}
		}
	}
	
	/**
	 * Sposta nella cartella di backup o cancella i file .war dell'igeriv.
	 * 
	 * @param List<File> list 
	 */
	private void cleanWarFiles(List<TomcatFile> list) {
		for (File file : list) {
			File bkpDir = new File(igerivBkpDir);
			if (!bkpDir.isDirectory()) {
				bkpDir.mkdirs();
			}
			log.info("moving file " + file.getName() + " to " + bkpDir.getName());
			if (!file.renameTo(new File(bkpDir, file.getName()))) {
				log.error("error moving file " + file.getName() + " to " + bkpDir.getName() + " - trying to delete it");
				if (!IGerivCleanerFileUtils.deleteDir(file)) {
					log.error("error deleting file " + file.getName() + " - sending alert email");
					mailingListService.sendEmail(null, IGerivMessageBundle.get("msg.subject.errore.manutenzione.tomcat"), MessageFormat.format(IGerivMessageBundle.get("msg.errore.errore.manutenzione.tomcat.move.file"), new Object[] { file.getName(), bkpDir.getName() }), true);
				}
			}
		}
	}
	
	/**
	 * Pulisce i file di log 
	 */
	private void cleanLogFiles() {
		if (getCleanLogFilesAtStartup()) {
			File logFolder = new File(logFolderPath);
			for (File file : logFolder.listFiles()) {
				if (file.isFile() && (file.getName().equals("application.log") || file.getName().equals("application-adm.log") || file.getName().equals("application-client.log") || file.getName().equals("application-dl.log"))) {
					file.delete();
				}
			}
		}
	}

	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

	public String getTomcatDirs() {
		return tomcatDirs;
	}

	public void setTomcatDirs(String tomcatDirs) {
		this.tomcatDirs = tomcatDirs;
	}

	public String getIgerivFilesPrefixes() {
		return igerivFilesPrefixes;
	}

	public void setIgerivFilesPrefixes(String igerivFilesPrefixes) {
		this.igerivFilesPrefixes = igerivFilesPrefixes;
	}

	public String getTomcatWinServiceNames() {
		return tomcatWinServiceNames;
	}

	public void setTomcatWinServiceNames(String tomcatWinServiceNames) {
		this.tomcatWinServiceNames = tomcatWinServiceNames;
	}

	public String getIgerivBkpDir() {
		return igerivBkpDir;
	}

	public void setIgerivBkpDir(String igerivBkpDir) {
		this.igerivBkpDir = igerivBkpDir;
	}
	
	public String getEnvDeployName() {
		return envDeployName;
	}

	public void setEnvDeployName(String envDeployName) {
		this.envDeployName = envDeployName;
	}

	public String getEnabledEnvNames() {
		return enabledEnvNames;
	}

	public void setEnabledEnvNames(String enabledEnvNames) {
		this.enabledEnvNames = enabledEnvNames;
	}
	
	public String getMaxMemorySize() {
		return maxMemorySize;
	}

	public void setMaxMemorySize(String maxMemorySize) {
		this.maxMemorySize = maxMemorySize;
	}
	
	public Boolean getCleanLogFilesAtStartup() {
		return cleanLogFilesAtStartup == null ? false : cleanLogFilesAtStartup;
	}

	public void setCleanLogFilesAtStartup(Boolean cleanLogFilesAtStartup) {
		this.cleanLogFilesAtStartup = cleanLogFilesAtStartup;
	}
	
	public String getLogFolderPath() {
		return logFolderPath;
	}

	public void setLogFolderPath(String logFolderPath) {
		this.logFolderPath = logFolderPath;
	}

	private class TomcatFile extends File {
		private static final long serialVersionUID = 1L;

		public TomcatFile(String pathname) {
			super(pathname);
		}
		
		public String getTomcatName() {
			return super.getName().substring(0, super.getName().indexOf("##"));
		}

		public Integer getTomcatIndex() {
			if (super.getName().endsWith(".war")) {
				return new Integer(super.getName().substring(super.getName().indexOf("##") + 2, super.getName().lastIndexOf(".")));
			} else {
				return new Integer(super.getName().substring(super.getName().indexOf("##") + 2));
			}
		}
		
	}
}
