package it.dpe.igeriv.web.applet.regcassa.dirfinder;

import java.util.logging.Logger;

/**
 * @author mromano
 *
 */
public abstract class InstallDirFinder {
	
	public abstract String find(String userRegCassaLocalDir, Logger logger) throws Exception;
	
}
