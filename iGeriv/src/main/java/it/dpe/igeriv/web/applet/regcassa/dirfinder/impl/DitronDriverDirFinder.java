package it.dpe.igeriv.web.applet.regcassa.dirfinder.impl;

import java.util.logging.Logger;

import it.dpe.igeriv.web.applet.regcassa.dirfinder.DriverDirFinder;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaConstants;

public class DitronDriverDirFinder extends DriverDirFinder {

	@Override
	public String find(String installationDir, Logger logger) throws Exception {
		logger.fine("Entered DitronInstallDirFinder.find() with params: installationDir=" + installationDir + " logger=" + logger);
		return installationDir + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + "Drivers";
	}
	
}
