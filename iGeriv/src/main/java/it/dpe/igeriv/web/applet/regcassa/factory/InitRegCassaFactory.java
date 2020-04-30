package it.dpe.igeriv.web.applet.regcassa.factory;

import java.util.logging.Logger;

import it.dpe.igeriv.web.applet.regcassa.init.InitRegCassa;
import it.dpe.igeriv.web.applet.regcassa.init.impl.DefaultInitRegCassa;
import it.dpe.igeriv.web.applet.regcassa.init.impl.DitronInitRegCassaImpl;
import it.dpe.igeriv.web.applet.regcassa.init.impl.MCTInitRegCassaImpl;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaConstants;
import netscape.javascript.JSObject;

public class InitRegCassaFactory {
	
	public static InitRegCassa buildInitRegCassa(String initContent, String defaultLocalDir, String installationDir, Integer type, JSObject window, long millsTaskInterval, Logger logger) {
		logger.fine("Entered InitRegCassaBuilder.buildInitRegCassa()");
		if (type != null) {
			InitRegCassa initRegCassaImpl = null;
			if (type.equals(RegCassaConstants.MCT_FLASH) || type.equals(RegCassaConstants.MCT_SPOT)) {
				initRegCassaImpl = new MCTInitRegCassaImpl(initContent, defaultLocalDir, installationDir, window, millsTaskInterval, logger);
			} else if (type.equals(RegCassaConstants.DITRON)) {
				initRegCassaImpl = new DitronInitRegCassaImpl(initContent, defaultLocalDir, installationDir, window, millsTaskInterval, logger);
			} else {
				initRegCassaImpl = new DefaultInitRegCassa();
			}
			logger.fine("Exiting InitRegCassaBuilder.buildInitRegCassa() with initRegCassaImpl=" + initRegCassaImpl);
			return initRegCassaImpl;
		}
		DefaultInitRegCassa defaultInitRegCassa = new DefaultInitRegCassa();
		logger.fine("Exiting FileWatcher.buildTimerTask() with defaultInitRegCassa=" + defaultInitRegCassa);
		return defaultInitRegCassa;
	}
	
}
