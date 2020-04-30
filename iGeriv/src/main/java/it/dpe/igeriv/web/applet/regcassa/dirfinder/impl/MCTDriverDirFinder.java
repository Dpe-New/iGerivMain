package it.dpe.igeriv.web.applet.regcassa.dirfinder.impl;

import java.util.logging.Logger;

import it.dpe.igeriv.web.applet.regcassa.dirfinder.DriverDirFinder;

public class MCTDriverDirFinder extends DriverDirFinder {

	@Override
	public String find(String installationDir, Logger logger) throws Exception {
		return installationDir;
	}

}
