package it.dpe.igeriv.web.applet.regcassa.factory;

import java.util.logging.Logger;

import it.dpe.igeriv.web.applet.regcassa.dirfinder.ScontriniDirFinder;
import it.dpe.igeriv.web.applet.regcassa.dirfinder.impl.DefaultScontriniDirFinder;
import it.dpe.igeriv.web.applet.regcassa.dirfinder.impl.DitronScontriniDirFinder;
import it.dpe.igeriv.web.applet.regcassa.dirfinder.impl.MCTScontriniDirFinder;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaConstants;

public class ScontriniDirFinderFactory {

	public static ScontriniDirFinder buildScontriniDirFinder(Integer type, Logger logger, String context) {
		logger.fine("Entered ScontriniDirFinder.buildScontriniDirFinder() with type=" + type + " and context=" + context);
		if (type != null) {
			if (type.equals(RegCassaConstants.MCT_FLASH) || type.equals(RegCassaConstants.MCT_SPOT)) {
				ScontriniDirFinder scontriniDirFinder = new MCTScontriniDirFinder();
				logger.fine("Exiting ScontriniDirFinder.buildScontriniDirFinder()");
				return scontriniDirFinder;
			} else if (type.equals(RegCassaConstants.DITRON)) {
				ScontriniDirFinder scontriniDirFinder = new DitronScontriniDirFinder();
				logger.fine("Exiting ScontriniDirFinder.buildScontriniDirFinder()");
				return scontriniDirFinder;
			}
		}
		ScontriniDirFinder scontriniDirFinder = new DefaultScontriniDirFinder();
		logger.fine("Exiting ScontriniDirFinder.buildScontriniDirFinder()");
		return scontriniDirFinder;
	}

}
