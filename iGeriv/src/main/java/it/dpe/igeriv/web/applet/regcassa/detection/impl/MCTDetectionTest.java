package it.dpe.igeriv.web.applet.regcassa.detection.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import it.dpe.igeriv.web.applet.regcassa.detection.DetectionTest;
import it.dpe.igeriv.web.applet.regcassa.filewatcher.impl.MCTDetectionTestFileWatcher;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaConstants;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaUtils;
import netscape.javascript.JSObject;

public class MCTDetectionTest extends DetectionTest {
	private Logger logger;
	private static final String TEST_FILE_NAME = "Avvio.txt";
	private static final long TIMEOUT_MILLS = 5000;
	private String pathEmissioneScontrini; 
	private String installationDir;
	private JSObject window = null;
	private long millsTaskInterval;
	
	public MCTDetectionTest(String pathEmissioneScontrini, String installationDir, JSObject window, long millsTaskInterval, Logger logger) {
		this.pathEmissioneScontrini = pathEmissioneScontrini;
		this.installationDir = installationDir;
		this.window = window;
		this.millsTaskInterval = millsTaskInterval;
		this.logger = logger;
	}
	
	@Override
	public void detect() throws IOException {
		logger.fine("Entered MCTDetectionTest.detect()");
		File testFile = new File(new File(installationDir) + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + TEST_FILE_NAME);
		File testFileDest = new File(pathEmissioneScontrini + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + TEST_FILE_NAME);
		RegCassaUtils.copyFile(testFile, testFileDest);
		Timer timer = new Timer();
		TimerTask task = new MCTDetectionTestFileWatcher(testFileDest, window, timer, TIMEOUT_MILLS, logger);
		timer.schedule(task, new Date(), new Long(millsTaskInterval));
		logger.fine("Exiting MCTDetectionTest.detect()");
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.pathEmissioneScontrini + "," + this.installationDir + "," + this.window + "," + this.millsTaskInterval + ")";
	}

	
}
