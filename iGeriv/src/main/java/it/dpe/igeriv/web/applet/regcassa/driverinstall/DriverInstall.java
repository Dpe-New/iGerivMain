package it.dpe.igeriv.web.applet.regcassa.driverinstall;

import java.awt.Window;
import java.util.logging.Logger;

public abstract class DriverInstall {
	
	public abstract void install(Logger logger, String baseUrl, Window frame) throws Exception;
	
}
