package it.dpe.igeriv.web.applet.regcassa.dirfinder.impl;

import java.awt.Window;
import java.io.File;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;

import it.dpe.igeriv.util.WindowsRegistry;
import it.dpe.igeriv.web.applet.regcassa.dirfinder.ScontriniDirFinder;
import it.dpe.igeriv.web.applet.regcassa.exception.RegCassaMissingInstallationPathException;
import it.dpe.igeriv.web.applet.regcassa.utils.OSValidator;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaConstants;


public class MCTScontriniDirFinder extends ScontriniDirFinder {
	private static final String REG_KEY_PATH_INSTALL_DIR = "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\OndaDriverServer_is1";
	private static final String REG_KEY_NAME_INSTALL_DIR = "Inno Setup: App Path";
	private static final String TO_SEND_DIR = "TOSEND";

	@Override
	public String find(String userRegCassaLocalDir, Logger logger, String baseUrl, Window frame) throws Exception {
		logger.fine("Entered MCTScontriniDirFinder.find() with params: userRegCassaLocalDir=" + userRegCassaLocalDir + " logger=" + logger + " baseUrl=" + baseUrl);
		String dir = "";
		File userRegCassaLocalDirFile = (userRegCassaLocalDir == null || userRegCassaLocalDir.equals("")) ? null : new File(userRegCassaLocalDir);
		boolean userRegCassaPathIsValidDirectory = userRegCassaLocalDirFile != null && userRegCassaLocalDirFile.isDirectory();
		if (!userRegCassaPathIsValidDirectory) {
			if (OSValidator.isWindows()) {
				dir = findScontriniPathUsingRegEdit();
			} else {
				throw new RegCassaMissingInstallationPathException();
			}
		}
		logger.fine("Exiting MCTScontriniDirFinder.find() with dir=" + dir);
		return dir;
	}
	
	

	/**
	 * Prova a localizzare il path di emissione degli scontrini del registratore di cassa utilizzando il registro di windows
	 * 
	 * @throws BackingStoreException
	 * @throws RegCassaMissingInstallationPathException
	 */
	private String findScontriniPathUsingRegEdit() throws BackingStoreException, RegCassaMissingInstallationPathException {
		String dir = null;
		String path = WindowsRegistry.getKeySz( WindowsRegistry.HKEY_LOCAL_MACHINE, REG_KEY_PATH_INSTALL_DIR, REG_KEY_NAME_INSTALL_DIR);
		if (path == null || path.equals("")) {
			throw new RegCassaMissingInstallationPathException();
		} else {
			dir = path + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + TO_SEND_DIR;
			File file = new File(dir);
			if (file != null && !file.isDirectory()) {
				throw new RegCassaMissingInstallationPathException();
			}
		}
		return dir;
	}

}
