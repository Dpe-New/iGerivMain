package it.dpe.igeriv.web.applet.regcassa.detection.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;

import org.apache.http.auth.AuthenticationException;

import it.dpe.igeriv.web.applet.regcassa.detection.DetectionTest;
import it.dpe.igeriv.web.applet.regcassa.exception.RegCassaMissingActiveProcessException;
import netscape.javascript.JSObject;

public class DitronDetectionTest extends DetectionTest {
	private Logger logger;
	private String pathEmissioneScontrini; 
	private String installationDir;
	private JSObject window = null;
	private long millsTaskInterval;
	
	public DitronDetectionTest(String pathEmissioneScontrini, String installationDir, JSObject window, long millsTaskInterval, Logger logger) {
		this.pathEmissioneScontrini = pathEmissioneScontrini;
		this.installationDir = installationDir;
		this.window = window;
		this.millsTaskInterval = millsTaskInterval;
		this.logger = logger;
	}
	
	@Override
	public void detect() throws IOException, AuthenticationException, URISyntaxException, RegCassaMissingActiveProcessException, InterruptedException, BackingStoreException {
		logger.fine("Entered DitronDetectionTest.detect()");
		logger.fine("Exiting DitronDetectionTest.detect()");
	}
	
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.pathEmissioneScontrini + "," + this.installationDir + "," + this.window + "," + this.millsTaskInterval + ")";
	}
	
}
