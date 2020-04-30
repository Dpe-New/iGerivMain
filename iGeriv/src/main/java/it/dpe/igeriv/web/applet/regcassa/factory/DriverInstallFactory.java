package it.dpe.igeriv.web.applet.regcassa.factory;

import java.util.logging.Logger;

import it.dpe.igeriv.web.applet.regcassa.driverinstall.DriverInstall;
import it.dpe.igeriv.web.applet.regcassa.driverinstall.impl.DefaultDriverInstall;
import it.dpe.igeriv.web.applet.regcassa.driverinstall.impl.DitronDriverInstall;
import it.dpe.igeriv.web.applet.regcassa.driverinstall.impl.MCTDriverInstall;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaConstants;

public class DriverInstallFactory {
	
	public static DriverInstall buildDriverInstall(Integer type, Logger logger) {
		logger.fine("Entered DriverInstallBuilder.buildDriverInstall()");
		if (type != null) {
			if (type.equals(RegCassaConstants.MCT_FLASH) || type.equals(RegCassaConstants.MCT_SPOT)) {
				DriverInstall driverInstall = new MCTDriverInstall();
				logger.fine("Exiting DriverInstallBuilder.buildDriverInstall() with driverInstall=" + driverInstall);
				return driverInstall;
			} else if (type.equals(RegCassaConstants.DITRON)) {
				DriverInstall driverInstall = new DitronDriverInstall();
				logger.fine("Exiting DriverInstallBuilder.buildDriverInstall() with driverInstall=" + driverInstall);
				return driverInstall;
			}
		}
		DriverInstall driverInstall = new DefaultDriverInstall();
		logger.fine("Exiting DriverInstallBuilder.buildDriverInstall() with driverInstall=" + driverInstall);
		return driverInstall;
	}
	
}
