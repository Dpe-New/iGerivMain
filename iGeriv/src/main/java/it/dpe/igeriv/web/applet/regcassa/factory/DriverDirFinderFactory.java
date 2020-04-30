package it.dpe.igeriv.web.applet.regcassa.factory;

import java.util.logging.Logger;

import it.dpe.igeriv.web.applet.regcassa.dirfinder.DriverDirFinder;
import it.dpe.igeriv.web.applet.regcassa.dirfinder.impl.DefaultDriverDirFinder;
import it.dpe.igeriv.web.applet.regcassa.dirfinder.impl.DitronDriverDirFinder;
import it.dpe.igeriv.web.applet.regcassa.dirfinder.impl.MCTDriverDirFinder;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaConstants;

public class DriverDirFinderFactory {
	
	public static DriverDirFinder buildDriverDirFinder(Integer type, Logger logger) {
		logger.fine("Entered DriverDirFinderBuilder.buildDriverDirFinder()");
		if (type != null) {
			if (type.equals(RegCassaConstants.MCT_FLASH) || type.equals(RegCassaConstants.MCT_SPOT)) {
				DriverDirFinder driverDirFinder = new MCTDriverDirFinder();
				logger.fine("Exiting DriverDirFinderBuilder.buildDriverDirFinder() with driverDirFinder=" + driverDirFinder);
				return driverDirFinder;
			} else if (type.equals(RegCassaConstants.DITRON)) {
				DriverDirFinder driverDirFinder = new DitronDriverDirFinder();
				logger.fine("Exiting DriverDirFinderBuilder.buildDriverDirFinder() with driverDirFinder=" + driverDirFinder);
				return driverDirFinder;
			}
		}
		DriverDirFinder driverDirFinder = new DefaultDriverDirFinder();
		logger.fine("Exiting DriverDirFinderBuilder.buildDriverDirFinder() with driverDirFinder=" + driverDirFinder);
		return driverDirFinder;
	}
	
}
