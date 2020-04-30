package it.dpe.igeriv.web.applet.regcassa.driverinstall.impl;

import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;

import it.dpe.igeriv.util.WindowsRegistry;
import it.dpe.igeriv.web.applet.regcassa.dirfinder.InstallDirFinder;
import it.dpe.igeriv.web.applet.regcassa.driverinstall.DriverInstall;
import it.dpe.igeriv.web.applet.regcassa.exception.RegCassaDriverInstallationException;
import it.dpe.igeriv.web.applet.regcassa.factory.InstallDirFinderFactory;
import it.dpe.igeriv.web.applet.regcassa.utils.MsWinSvc;
import it.dpe.igeriv.web.applet.regcassa.utils.OSValidator;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaConstants;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaUtils;

/**
 * Fa il download e installa il driver e il servizio spooler e fa partire il servizio
 * 
 * @author mromano
 *
 */
public class DitronDriverInstall extends DriverInstall {

	@Override
	public void install(Logger logger, String baseUrl, Window frame) throws Exception {
		String dir = null;
		try {
			dir = WindowsRegistry.getKeySz(WindowsRegistry.HKEY_LOCAL_MACHINE, (RegCassaUtils.is64bit() ? RegCassaConstants.REG_KEY_PATH_INSTALL_DIR_ia64 : RegCassaConstants.REG_KEY_PATH_INSTALL_DIR_i386), RegCassaConstants.REG_KEY_NAME_INSTALL_DIR);
		} catch (BackingStoreException e) {}
		if (dir == null) {
			downloadAndInstallDriver(logger, baseUrl, frame);
		}
		String installServiceFileUrl = baseUrl + RegCassaConstants.SLASH + RegCassaConstants.APPLET_DOWNLOADS_URI + RegCassaConstants.SLASH + RegCassaConstants.DITRON_FOLDER_NAME + RegCassaConstants.SLASH + (RegCassaUtils.is64bit() ? RegCassaConstants.WIN_64_BIT : RegCassaConstants.WIN_32_BIT) + RegCassaConstants.SLASH + RegCassaConstants.INSTALL_SERVICE_EXE;
		File installServiceFile = new File(System.getProperty(RegCassaConstants.JAVA_IO_TMP_DIR) + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + RegCassaConstants.INSTALL_SERVICE_EXE);
		if (!installServiceFile.exists()) {
			logger.fine("Downloading install service file exe: " + installServiceFileUrl);
			RegCassaUtils.httpDownload(installServiceFileUrl, installServiceFile, frame);
		}
		if (installServiceFile.exists()) {
			String checkServiceCmd = MessageFormat.format(RegCassaConstants.CHECK_SERVICE_INSTALLED_CMD, installServiceFile.getAbsolutePath(), RegCassaConstants.DITRON_SPOOLER_WIN_SERVICE_NAME);
			logger.fine("Executing service check command: " + checkServiceCmd);
			Process p1 = Runtime.getRuntime().exec(checkServiceCmd);
			p1.waitFor();
			if (p1.exitValue() == RegCassaConstants.ProcessResult.EXISTS.value) {
				restartService(logger);
			} else {
				installService(logger, installServiceFile);
				startService(logger, installServiceFile);
			}
		} else {
			throw new RegCassaDriverInstallationException();
		}
	}

	/**
	 * Riavvia il servizio
	 * 
	 * @param logger
	 * @throws Exception
	 */
	private void restartService(Logger logger) throws Exception {
		logger.fine("Service " + RegCassaConstants.DITRON_SPOOLER_WIN_SERVICE_NAME + " already exists");
		logger.fine("Stopping service: " + RegCassaConstants.DITRON_SPOOLER_WIN_SERVICE_NAME);
		MsWinSvc.stopService(RegCassaConstants.DITRON_SPOOLER_WIN_SERVICE_NAME);
		logger.fine("Starting service: " + RegCassaConstants.DITRON_SPOOLER_WIN_SERVICE_NAME);
		MsWinSvc.startService(RegCassaConstants.DITRON_SPOOLER_WIN_SERVICE_NAME);
	}

	/**
	 * Fa il download del driver e lo installa
	 * 
	 * @param logger
	 * @param baseUrl
	 * @param frame
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws InterruptedException
	 * @throws RegCassaDriverInstallationException
	 */
	private void downloadAndInstallDriver(Logger logger, String baseUrl, Window frame) throws IOException, URISyntaxException, InterruptedException, RegCassaDriverInstallationException {
		String driverName = OSValidator.isWindows() ? RegCassaConstants.DITRON_WIN_DRIVER_NAME : (OSValidator.isUnix() ? RegCassaConstants.DITRON_UX_DRIVER_NAME : null);
		String fileUrl = baseUrl + RegCassaConstants.SLASH + RegCassaConstants.APPLET_DOWNLOADS_URI + RegCassaConstants.SLASH + RegCassaConstants.DITRON_FOLDER_NAME + RegCassaConstants.SLASH + (RegCassaUtils.is64bit() ? RegCassaConstants.WIN_64_BIT : RegCassaConstants.WIN_32_BIT) + RegCassaConstants.SLASH + driverName;
		File driverExeFile = new File(System.getProperty(RegCassaConstants.JAVA_IO_TMP_DIR) + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + driverName);
		if (!driverExeFile.exists()) {
			logger.fine("Downloading file driver: " + fileUrl);
			RegCassaUtils.httpDownload(fileUrl, driverExeFile, frame);
		}
		if (driverExeFile.exists()) {
			logger.fine("Executing file driver: " + driverExeFile.getAbsolutePath());
			Process p = Runtime.getRuntime().exec(driverExeFile.getAbsolutePath());
			p.waitFor();
		} else {
			throw new RegCassaDriverInstallationException();
		}
	}
	
	/**
	 * Installa il servizio
	 *  
	 * @param logger
	 * @param installServiceFile
	 * @throws Exception
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws RegCassaDriverInstallationException
	 */
	private void installService(Logger logger, File installServiceFile) throws Exception, IOException, InterruptedException, RegCassaDriverInstallationException {
		logger.fine("Installing new service: " + RegCassaConstants.DITRON_SPOOLER_WIN_SERVICE_NAME);
		InstallDirFinder finder = InstallDirFinderFactory.buildInstallDirFinder(new Integer(RegCassaConstants.DITRON), logger);
		String installationDir = finder.find(null, logger);
		if (installationDir.endsWith(System.getProperty(RegCassaConstants.FILE_SEPARATOR))) {
			installationDir = installationDir.substring(0, installationDir.lastIndexOf(System.getProperty(RegCassaConstants.FILE_SEPARATOR)));
		}
		logger.fine("InstallationDir: " + installationDir);
		String serviceTargetExePath = installationDir + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + RegCassaConstants.UTILITIES_FOLDER_NAME + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + RegCassaConstants.TARGET_SERVICE_EXE_FILE_NAME;
		String serviceParamPath = installationDir + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + RegCassaConstants.DRIVERS_FOLDER_NAME + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + RegCassaConstants.DITRON_SPOOLER_WIN_PARAM;
		String serviceFileExecCmd = MessageFormat.format(RegCassaConstants.INSTALL_SERVICE_CMD, installServiceFile.getAbsolutePath(), serviceTargetExePath, RegCassaConstants.DITRON_SPOOLER_WIN_SERVICE_NAME, RegCassaConstants.DITRON_SPOOLER_WIN_SERVICE_DESCRIPTION, serviceParamPath);
		logger.fine("Executing install service command: " + serviceFileExecCmd);
		Process p2 = Runtime.getRuntime().exec(serviceFileExecCmd);
		p2.waitFor();
		if (p2.exitValue() == RegCassaConstants.ProcessResult.FAILURE.value) {
			List<String> output = RegCassaUtils.bufferProcessOutput(p2);
			logger.fine("Error executing install service command: " + serviceFileExecCmd);
			for (String s : output) {
				logger.fine(s);
			}
			throw new RegCassaDriverInstallationException();
		}
	}

	/**
	 * Avvia il servizio
	 * 
	 * @param logger
	 * @param installServiceFile
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws RegCassaDriverInstallationException
	 */
	private void startService(Logger logger, File installServiceFile) throws IOException, InterruptedException, RegCassaDriverInstallationException {
		String startServiceExecCmd = MessageFormat.format(RegCassaConstants.START_SERVICE_CMD, installServiceFile.getAbsolutePath(), RegCassaConstants.DITRON_SPOOLER_WIN_SERVICE_NAME);
		logger.fine("Executing start service command: " + startServiceExecCmd);
		Process p3 = Runtime.getRuntime().exec(startServiceExecCmd);
		p3.waitFor();
		if (p3.exitValue() == RegCassaConstants.ProcessResult.FAILURE.value) {
			List<String> output = RegCassaUtils.bufferProcessOutput(p3);
			logger.fine("Error executing start service command: " + startServiceExecCmd);
			for (String s : output) {
				logger.fine(s);
			}
			throw new RegCassaDriverInstallationException();
		}
	}

}
