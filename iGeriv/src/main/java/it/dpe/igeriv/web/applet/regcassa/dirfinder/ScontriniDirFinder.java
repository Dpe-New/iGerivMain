package it.dpe.igeriv.web.applet.regcassa.dirfinder;

import java.awt.Window;
import java.util.logging.Logger;

/**
 * @author mromano
 *
 */
public abstract class ScontriniDirFinder {
	
	public abstract String find(String userRegCassaLocalDir, Logger logger, String baseUrl, Window frame) throws Exception;
	
}
