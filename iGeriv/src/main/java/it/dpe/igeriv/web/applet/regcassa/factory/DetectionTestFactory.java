package it.dpe.igeriv.web.applet.regcassa.factory;

import java.util.logging.Logger;

import it.dpe.igeriv.web.applet.regcassa.detection.DetectionTest;
import it.dpe.igeriv.web.applet.regcassa.detection.impl.DefaultDetectionTest;
import it.dpe.igeriv.web.applet.regcassa.detection.impl.DitronDetectionTest;
import it.dpe.igeriv.web.applet.regcassa.detection.impl.MCTDetectionTest;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaConstants;
import netscape.javascript.JSObject;

public class DetectionTestFactory {
	
	public static DetectionTest buildDetectionTest(String defaultLocalDir, String installationDir, Integer type, JSObject window, long millsTaskInterval, Logger logger) {
		logger.fine("Entered DetectionTestBuilder.buildDetectionTest()");
		if (type != null) {
			if (type.equals(RegCassaConstants.MCT_FLASH) || type.equals(RegCassaConstants.MCT_SPOT)) {
				DetectionTest detectionTest = new MCTDetectionTest(defaultLocalDir, installationDir, window, millsTaskInterval, logger);
				logger.fine("Exiting DetectionTestBuilder.buildDetectionTest() with detectionTest=" + detectionTest);
				return detectionTest;
			} else if (type.equals(RegCassaConstants.DITRON)) {
				DetectionTest detectionTest = new DitronDetectionTest(defaultLocalDir, installationDir, window, millsTaskInterval, logger);
				logger.fine("Exiting DetectionTestBuilder.buildDetectionTest() with detectionTest=" + detectionTest);
				return detectionTest;
			}
		}
		DefaultDetectionTest defaultDetectionTest = new DefaultDetectionTest();
		logger.fine("Exiting FileWatcher.buildTimerTask() with defaultDetectionTest="+defaultDetectionTest);
		return defaultDetectionTest;
	}
	
}
