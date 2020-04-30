package it.dpe.igeriv.web.applet.regcassa.dirfinder.impl;

import java.io.File;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;

import com.google.common.base.Strings;

import it.dpe.igeriv.util.WindowsRegistry;
import it.dpe.igeriv.web.applet.regcassa.dirfinder.InstallDirFinder;
import it.dpe.igeriv.web.applet.regcassa.exception.RegCassaMissingInstallationPathException;
import it.dpe.igeriv.web.applet.regcassa.utils.OSValidator;

/**
 * @author mromano
 *
 */
public class MCTInstallDirFinder extends InstallDirFinder {
	private static final String REG_KEY_PATH_INSTALL_DIR = "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\OndaDriverServer_is1";
	private static final String REG_KEY_NAME_INSTALL_DIR = "InstallLocation";

	@Override
	public String find(String userRegCassaLocalDir, Logger logger) throws Exception {
		logger.fine("Entered MCTInstallDirFinder.find() with params: userRegCassaLocalDir=" + userRegCassaLocalDir + " logger=" + logger);
		String dir = "";
		File userRegCassaLocalDirFile = (userRegCassaLocalDir == null || userRegCassaLocalDir.equals("")) ? null : new File(userRegCassaLocalDir);
		boolean userRegCassaPathIsValidDirectory = userRegCassaLocalDirFile != null && userRegCassaLocalDirFile.isDirectory();
		if (!userRegCassaPathIsValidDirectory) {
			if (OSValidator.isWindows()) {
				dir = findInstallPathUsingRegEdit(logger);
			} else {
				throw new RegCassaMissingInstallationPathException();
			}
		} else {
			dir = userRegCassaLocalDir;
		}
		logger.fine("Exiting MCTInstallDirFinder.find() with dir=" + dir);
		return dir;
	}
	
	/**
	 * Prova a localizzare il path di installazione del registratore di cassa utilizzando il registro di windows
	 * 
	 * @param logger 
	 * @throws BackingStoreException
	 * @throws RegCassaMissingInstallationPathException
	 */
	private String findInstallPathUsingRegEdit(Logger logger) throws BackingStoreException, RegCassaMissingInstallationPathException {
		logger.fine("Entered MCTInstallDirFinder.findInstallPathUsingRegEdit()");
		String dir = WindowsRegistry.getKeySz(WindowsRegistry.HKEY_LOCAL_MACHINE, REG_KEY_PATH_INSTALL_DIR, REG_KEY_NAME_INSTALL_DIR);
		File fileDir = new File(dir);
		if (Strings.isNullOrEmpty(dir)) {
			logger.fine("Install path not found. Launching RegCassaMissingInstallationPathException");
			throw new RegCassaMissingInstallationPathException();
		} else if (fileDir != null && !fileDir.isDirectory()) {
			File parentFile = fileDir.getParentFile();
			if (parentFile != null && parentFile.isDirectory()) {
				dir = parentFile.getAbsolutePath();
			} else {
				logger.fine("Install path not found. Launching RegCassaMissingInstallationPathException");
				throw new RegCassaMissingInstallationPathException();
			}
		}
		logger.fine("Exiting MCTInstallDirFinder.findInstallPathUsingRegEdit() with dir=" + dir);
		return dir;
	}
}
