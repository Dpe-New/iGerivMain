package it.dpe.igeriv.web.applet.regcassa.factory;

import java.io.File;
import java.util.Timer;
import java.util.logging.Logger;

import it.dpe.igeriv.web.applet.regcassa.filewatcher.FileWatcher;
import it.dpe.igeriv.web.applet.regcassa.filewatcher.impl.DefaultFileWatcher;
import it.dpe.igeriv.web.applet.regcassa.filewatcher.impl.DitronFileWatcher;
import it.dpe.igeriv.web.applet.regcassa.filewatcher.impl.MCTFileWatcher;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaConstants;
import netscape.javascript.JSObject;

public class FileWatcherFactory {

	public static FileWatcher buildTimerTask(File file, Integer type, JSObject window, Timer timer, long millsTaskTimeout, Logger logger) {
		logger.fine("Entered FileWatcher.buildTimerTask()");
		if (type != null) {
			FileWatcher fileWatcher = null;
			if (type.equals(RegCassaConstants.MCT_FLASH) || type.equals(RegCassaConstants.MCT_SPOT)) {
				fileWatcher = new MCTFileWatcher(file, window, timer, millsTaskTimeout, logger);
			} else if (type.equals(RegCassaConstants.DITRON)) {
				fileWatcher = new DitronFileWatcher(file, window, timer, millsTaskTimeout, logger);
			} else {
				fileWatcher = new DefaultFileWatcher();
			}
			logger.fine("Exiting FileWatcher.buildTimerTask() with fileWatcher=" + fileWatcher);
			return fileWatcher;
		}
		DefaultFileWatcher defaultFileWatcher = new DefaultFileWatcher();
		logger.fine("Exiting FileWatcher.buildTimerTask() with defaultFileWatcher="+defaultFileWatcher);
		return defaultFileWatcher;
	}
	
}
