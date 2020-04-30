package it.dpe.igeriv.web.applet.regcassa.dirfinder;

import java.util.logging.Logger;

/**
 * @author mromano
 *
 */
public abstract class DriverDirFinder {
	
	public abstract String find(String installationDir, Logger logger) throws Exception;
	
}
