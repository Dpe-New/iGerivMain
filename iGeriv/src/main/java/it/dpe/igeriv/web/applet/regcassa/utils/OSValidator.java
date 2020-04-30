package it.dpe.igeriv.web.applet.regcassa.utils;

import it.dpe.igeriv.web.applet.regcassa.RegistratoreCassaApplet;

/**
 * @author mromano
 *
 */
public class OSValidator {

	public static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
	}

	public static boolean isSolaris() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("sunos") >= 0);

	}
	
	public static Integer getCurrentOS() {
		if (isWindows()) {
			return RegistratoreCassaApplet.OS_WINDOWS;
		} else if (isUnix()) {
			return RegistratoreCassaApplet.OS_UNIX_LINUX;
		} else if (isMac()) {
			return RegistratoreCassaApplet.OS_MAC;
		} else if (isSolaris()) {
			return RegistratoreCassaApplet.OS_SOLARIS;
		} else {
			return -1;
		}
	}
}
