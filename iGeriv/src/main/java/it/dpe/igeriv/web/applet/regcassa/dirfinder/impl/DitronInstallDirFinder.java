package it.dpe.igeriv.web.applet.regcassa.dirfinder.impl;

import java.io.File;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;

import com.google.common.base.Strings;

import it.dpe.igeriv.util.WindowsRegistry;
import it.dpe.igeriv.web.applet.regcassa.dirfinder.InstallDirFinder;
import it.dpe.igeriv.web.applet.regcassa.exception.RegCassaMissingDriverException;
import it.dpe.igeriv.web.applet.regcassa.exception.RegCassaMissingInstallationPathException;
import it.dpe.igeriv.web.applet.regcassa.utils.OSValidator;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaConstants;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaUtils;

/**
 * @author mromano
 *
 */
public class DitronInstallDirFinder extends InstallDirFinder {

	@Override
	public String find(String userRegCassaLocalDir, Logger logger) throws Exception {
		logger.fine("Entered DitronInstallDirFinder.find() with params: userRegCassaLocalDir=" + userRegCassaLocalDir + " logger=" + logger);
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
		logger.fine("Exiting DitronInstallDirFinder.find() with dir=" + dir);
		return dir;
	}
	
	/**
	 * Prova a localizzare il path di installazione del registratore di cassa utilizzando il registro di windows
	 * 
	 * @param logger 
	 * @throws BackingStoreException
	 * @throws RegCassaMissingInstallationPathException
	 * @throws RegCassaMissingDriverException 
	 */
	private String findInstallPathUsingRegEdit(Logger logger) throws BackingStoreException, RegCassaMissingInstallationPathException, RegCassaMissingDriverException {
		logger.fine("Entered DitronInstallDirFinder.findScontriniPathUsingRegEdit()");
		String dir = null;
		try {
			dir = WindowsRegistry.getKeySz(WindowsRegistry.HKEY_LOCAL_MACHINE, (RegCassaUtils.is64bit() ? RegCassaConstants.REG_KEY_PATH_INSTALL_DIR_ia64 : RegCassaConstants.REG_KEY_PATH_INSTALL_DIR_i386), RegCassaConstants.REG_KEY_NAME_INSTALL_DIR);
		} catch (BackingStoreException e) {}
		if (dir == null) {
			throw new RegCassaMissingDriverException(OSValidator.isWindows() ? RegCassaConstants.DITRON_WIN_DRIVER_NAME : (OSValidator.isUnix() ? RegCassaConstants.DITRON_UX_DRIVER_NAME : null));
		}
		File fileDir = new File(dir);
		if (Strings.isNullOrEmpty(dir)) {
			logger.fine("Install path not found. Lauching RegCassaMissingInstallationPathException");
			throw new RegCassaMissingInstallationPathException();
		} else if (fileDir != null && !fileDir.isDirectory()) {
			File parentFile = fileDir.getParentFile();
			if (parentFile != null && parentFile.isDirectory()) {
				dir = parentFile.getAbsolutePath();
			} else {
				logger.fine("Install path not found. Lauching RegCassaMissingInstallationPathException");
				throw new RegCassaMissingInstallationPathException();
			}
		}
		logger.fine("Exiting DitronInstallDirFinder.findScontriniPathUsingRegEdit() with dir=" + dir);
		return dir;
	}

}
