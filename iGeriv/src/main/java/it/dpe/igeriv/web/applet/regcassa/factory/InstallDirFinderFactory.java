package it.dpe.igeriv.web.applet.regcassa.factory;

import java.util.logging.Logger;

import it.dpe.igeriv.web.applet.regcassa.dirfinder.InstallDirFinder;
import it.dpe.igeriv.web.applet.regcassa.dirfinder.impl.DefaultInstallDirFinder;
import it.dpe.igeriv.web.applet.regcassa.dirfinder.impl.DitronInstallDirFinder;
import it.dpe.igeriv.web.applet.regcassa.dirfinder.impl.MCTInstallDirFinder;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaConstants;

public class InstallDirFinderFactory {
	
	public static InstallDirFinder buildInstallDirFinder(Integer type, Logger logger) {
		logger.fine("Entered ScontriniDirFinder.buildScontriniDirFinder()");
		if (type != null) {
			if (type.equals(RegCassaConstants.MCT_FLASH) || type.equals(RegCassaConstants.MCT_SPOT)) {
				InstallDirFinder installDirFinder = new MCTInstallDirFinder();
				logger.fine("Exiting InstallDirFinder.buildInstallDirFinder() with installDirFinder=" + installDirFinder);
				return installDirFinder;
			} else if (type.equals(RegCassaConstants.DITRON)) {
				InstallDirFinder installDirFinder = new DitronInstallDirFinder();
				logger.fine("Exiting InstallDirFinder.buildInstallDirFinder() with installDirFinder=" + installDirFinder);
				return installDirFinder;
			}
		}
		InstallDirFinder installDirFinder = new DefaultInstallDirFinder();
		logger.fine("Exiting InstallDirFinder.buildInstallDirFinder() with installDirFinder=" + installDirFinder);
		return installDirFinder;
	}
	
}
