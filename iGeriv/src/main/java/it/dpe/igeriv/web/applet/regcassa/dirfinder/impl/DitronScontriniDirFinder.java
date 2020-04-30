package it.dpe.igeriv.web.applet.regcassa.dirfinder.impl;

import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Strings;

import it.dpe.igeriv.util.WindowsRegistry;
import it.dpe.igeriv.web.applet.regcassa.dirfinder.ScontriniDirFinder;
import it.dpe.igeriv.web.applet.regcassa.driverinstall.DriverInstall;
import it.dpe.igeriv.web.applet.regcassa.exception.RegCassaDownloadResourceException;
import it.dpe.igeriv.web.applet.regcassa.exception.RegCassaDriverInstallationException;
import it.dpe.igeriv.web.applet.regcassa.exception.RegCassaMissingScontriniPathException;
import it.dpe.igeriv.web.applet.regcassa.exception.RegCassaNotConnectedException;
import it.dpe.igeriv.web.applet.regcassa.factory.DriverInstallFactory;
import it.dpe.igeriv.web.applet.regcassa.utils.MsWinSvc;
import it.dpe.igeriv.web.applet.regcassa.utils.OSValidator;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaConstants;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaUtils;

/**
 * @author mromano
 *
 */
public class DitronScontriniDirFinder extends ScontriniDirFinder {
	private static final String REG_KEY_COMMAND_FILE = "AutoRun CommandFile";
	private static final String REG_KEY_PORT = "Port";
	private static final String DITRON_CONFIG_REG_EDIT = "Ditron_Config.reg";
	private static final String DEVCON_EXE = "devcon.exe";
	private static final String REG_KEY_PATH_PORTS_CONFIG_ia64 = "SOFTWARE\\Wow6432Node\\Ditron\\Drivers\\WinEcrCom\\FirmwareA\\Ports\\1";
	private static final String REG_KEY_PATH_PORTS_CONFIG_i386 = "SOFTWARE\\Ditron\\Drivers\\WinEcrCom\\FirmwareA\\Ports\\1";
	
	@Override
	public String find(String userRegCassaLocalDir, Logger logger, String baseUrl, Window frame) throws Exception {
		logger.fine("Entered DitronScontriniDirFinder.find() with params: userRegCassaLocalDir=" + userRegCassaLocalDir + " logger=" + logger + " baseUrl=" + baseUrl);
		String dir = "";
		File userRegCassaLocalDirFile = (userRegCassaLocalDir == null || userRegCassaLocalDir.equals("")) ? null : new File(userRegCassaLocalDir);
		boolean userRegCassaPathIsValidDirectory = userRegCassaLocalDirFile != null && userRegCassaLocalDirFile.isDirectory();
		if (!userRegCassaPathIsValidDirectory) {
			if (OSValidator.isWindows()) {
				dir = findScontriniPathUsingRegEditAndConfigUsbPorts(logger, baseUrl, frame);
			} else {
				throw new RegCassaMissingScontriniPathException();
			}
		} else {
			dir = userRegCassaLocalDir;
		}
		logger.fine("Exiting DitronScontriniDirFinder.find() with dir=" + dir);
		return dir;
	}
	
	/**
	 * Ritorna il path degli scontrini cercando nelle chiavi del regedit.
	 * Se non è presente il path degli scontrini nelle chiavi del regedit:
	 * 1. Scarica devcon.exe e lo esegue il comando <see>RegCassaConstants.DEVCON_FIND_USB_PORTS_CMD</see> per trovare la USB SERIAL PORT collegata al reg. di cassa
	 * 2. Scarica il file regedit corrispondente alla piattaforma (32 o 64 bit), lo modifica aggiungendo la porta corretta e lo esegue
	 * 3. Cerca nuovamente tra le chiavi del regedit il path degli scontrini
	 * 
	 * @param logger 
	 * @param baseUrl 
	 * @param frame 
	 * @param window 
	 * @throws Exception 
	 */
	private String findScontriniPathUsingRegEditAndConfigUsbPorts(Logger logger, String baseUrl, Window frame) throws Exception {
		logger.fine("Entered DitronScontriniDirFinder.findScontriniPathUsingRegEditAndConfigUsbPorts()");
		String dir = findScontriniPathDir();
		logger.fine("Scontrini dir path=" + dir);
		checkUsbSerialPortDriver(logger, baseUrl, frame);
		String cmdFindUsbPort = findUsbSerialPort(logger, baseUrl, frame);
		if (Strings.isNullOrEmpty(dir)) {
			executeRegedit(logger, baseUrl, frame, cmdFindUsbPort);
			dir = findScontriniPathDir();
			logger.fine("Scontrini dir path=" + dir);
			dir = mkdirs(dir);
		} else {
			String regEditPort = WindowsRegistry.getKeySz(WindowsRegistry.HKEY_LOCAL_MACHINE, (RegCassaUtils.is64bit() ? REG_KEY_PATH_PORTS_CONFIG_ia64 : REG_KEY_PATH_PORTS_CONFIG_i386), REG_KEY_PORT);
			if (regEditPort == null || !cmdFindUsbPort.equals(regEditPort)) {
				DriverInstall di = DriverInstallFactory.buildDriverInstall(RegCassaConstants.DITRON, logger);
				di.install(logger, baseUrl, frame);
				executeRegedit(logger, baseUrl, frame, cmdFindUsbPort);
				dir = findScontriniPathDir();
				logger.fine("Scontrini dir path=" + dir);
				dir = mkdirs(dir);
			}
		}
		logger.fine("Stopping service: " + RegCassaConstants.DITRON_SPOOLER_WIN_SERVICE_NAME);
		MsWinSvc.stopService(RegCassaConstants.DITRON_SPOOLER_WIN_SERVICE_NAME);
		logger.fine("Starting service: " + RegCassaConstants.DITRON_SPOOLER_WIN_SERVICE_NAME);
		MsWinSvc.startService(RegCassaConstants.DITRON_SPOOLER_WIN_SERVICE_NAME);
		logger.fine("Exiting DitronScontriniDirFinder.findScontriniPathUsingRegEditAndConfigUsbPorts() with dir=" + dir);
		return dir;
	}

	/**
	 * Verifica se il driver USB - Seriale è già installato, 
	 * altrimenti fa il download dei files e lo installa 
	 * 
	 * @param logger
	 * @param baseUrl
	 * @param frame
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws InterruptedException
	 * @throws RegCassaDriverInstallationException
	 */
	private void checkUsbSerialPortDriver(Logger logger, String baseUrl, Window frame) throws IOException, URISyntaxException, InterruptedException, RegCassaDriverInstallationException {
		File installServiceFile = new File(System.getProperty(RegCassaConstants.JAVA_IO_TMP_DIR) + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + RegCassaConstants.INSTALL_SERVICE_EXE);
		if (!installServiceFile.exists()) {
			String installServiceFileUrl = baseUrl + RegCassaConstants.SLASH + RegCassaConstants.APPLET_DOWNLOADS_URI + RegCassaConstants.SLASH + RegCassaConstants.DITRON_FOLDER_NAME + RegCassaConstants.SLASH + (RegCassaUtils.is64bit() ? RegCassaConstants.WIN_64_BIT : RegCassaConstants.WIN_32_BIT) + RegCassaConstants.SLASH + RegCassaConstants.INSTALL_SERVICE_EXE;
			logger.fine("Downloading install service file exe: " + installServiceFileUrl);
			RegCassaUtils.httpDownload(installServiceFileUrl, installServiceFile, frame);
		}
		if (installServiceFile.exists()) {
			String usbSerialPortDriverCheckCmd1 = MessageFormat.format(RegCassaConstants.CHECK_USB_SERIAL_PORT_DRIVER_CMD, installServiceFile.getAbsolutePath(), RegCassaConstants.FTDIBUS_DRIVER_NAME);
			String usbSerialPortDriverCheckCmd2 = MessageFormat.format(RegCassaConstants.CHECK_USB_SERIAL_PORT_DRIVER_CMD, installServiceFile.getAbsolutePath(), RegCassaConstants.FTSER2K_DRIVER_NAME);
			logger.fine("Executing usb serial port driver check command: " + usbSerialPortDriverCheckCmd1);
			Process p1 = Runtime.getRuntime().exec(usbSerialPortDriverCheckCmd1);
			p1.waitFor();
			logger.fine("Executing usb serial port driver check command: " + usbSerialPortDriverCheckCmd2);
			Process p2 = Runtime.getRuntime().exec(usbSerialPortDriverCheckCmd2);
			p2.waitFor();
			if (p1.exitValue() == RegCassaConstants.ProcessResult.MISSING.value || p2.exitValue() == RegCassaConstants.ProcessResult.MISSING.value) {
				logger.fine("Usb serial port driver missing. Installing driver");
				File usbSerialPortDriverExeFile = new File(System.getProperty(RegCassaConstants.JAVA_IO_TMP_DIR) + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + RegCassaConstants.USB_SERIAL_PORT_DRIVER_EXE);
				if (!usbSerialPortDriverExeFile.exists()) {
					String usbSerialPortDriverExeFileUrl = baseUrl + RegCassaConstants.SLASH + RegCassaConstants.APPLET_DOWNLOADS_URI + RegCassaConstants.SLASH + RegCassaConstants.DITRON_FOLDER_NAME + RegCassaConstants.SLASH + (RegCassaUtils.is64bit() ? RegCassaConstants.WIN_64_BIT : RegCassaConstants.WIN_32_BIT) + RegCassaConstants.SLASH + RegCassaConstants.USB_SERIAL_PORT_DRIVER_EXE;
					logger.fine("Downloading usb serial port driver exe file: " + usbSerialPortDriverExeFileUrl);
					RegCassaUtils.httpDownload(usbSerialPortDriverExeFileUrl, usbSerialPortDriverExeFile, frame);
				}
				if (usbSerialPortDriverExeFile.exists()) {
					logger.fine("Executing usb serial port driver exe file: " + usbSerialPortDriverExeFile.getAbsolutePath());
					Process p = Runtime.getRuntime().exec(usbSerialPortDriverExeFile.getAbsolutePath());
					p.waitFor();
				} else {
					throw new RegCassaDriverInstallationException();
				}
			}
		}
		
	}

	/**
	 * Scarica ed esegue il devcon.exe per trovare la porta usb seriale su cui connesso il reg. di cassa 
	 * 
	 * @param logger
	 * @param baseUrl
	 * @param frame
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws InterruptedException
	 * @throws RegCassaDownloadResourceException
	 * @throws RegCassaNotConnectedException
	 */
	private String findUsbSerialPort(Logger logger, String baseUrl, Window frame) throws IOException, URISyntaxException, InterruptedException, RegCassaDownloadResourceException, RegCassaNotConnectedException {
		File devConExeFile = new File(System.getProperty(RegCassaConstants.JAVA_IO_TMP_DIR) + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + DEVCON_EXE);
		if (!devConExeFile.exists()) {
			String fileUrl = baseUrl + RegCassaConstants.SLASH + RegCassaConstants.APPLET_DOWNLOADS_URI + RegCassaConstants.SLASH + RegCassaConstants.DITRON_FOLDER_NAME + RegCassaConstants.SLASH + (RegCassaUtils.is64bit() ? RegCassaConstants.WIN_64_BIT : RegCassaConstants.WIN_32_BIT) + RegCassaConstants.SLASH + DEVCON_EXE;
			logger.fine("Downloading file devcon: " + fileUrl);
			RegCassaUtils.httpDownload(fileUrl, devConExeFile, frame);
		} 
		if (!devConExeFile.exists()) {
			logger.fine("Error downloading file devcon: " + devConExeFile.getAbsolutePath() + ". Launching RegCassaDownloadResourceException");
			throw new RegCassaDownloadResourceException(devConExeFile.getName());
		}
		logger.fine("Finding USB Serial Port");
		String usbPortName = RegCassaUtils.getUsbSerialPortNumber();
		logger.fine("USB Serial Port=" + usbPortName);
		if (Strings.isNullOrEmpty(usbPortName)) {
			logger.fine("USB Serial Port not found. Launching RegCassaNotConnectedException");
			throw new RegCassaNotConnectedException();
		}
		return usbPortName;
	}
	
	/**
	 * Scarica ed esegue il file .reg con le configurazioni per il polling sulla cartella degli scontrini
	 * 
	 * @param logger
	 * @param baseUrl
	 * @param frame
	 * @param usbPortName
	 * @throws Exception 
	 */
	private void executeRegedit(Logger logger, String baseUrl, Window frame, String usbPortName) throws Exception {
		File regEditFile = new File(System.getProperty(RegCassaConstants.JAVA_IO_TMP_DIR) + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + DITRON_CONFIG_REG_EDIT);
		String fileUrl = baseUrl + RegCassaConstants.SLASH + RegCassaConstants.APPLET_DOWNLOADS_URI + RegCassaConstants.SLASH + RegCassaConstants.DITRON_FOLDER_NAME + RegCassaConstants.SLASH + (RegCassaUtils.is64bit() ? RegCassaConstants.WIN_64_BIT : RegCassaConstants.WIN_32_BIT) + RegCassaConstants.SLASH + DITRON_CONFIG_REG_EDIT;
		logger.fine("Downloading file regedit: " + fileUrl);
		RegCassaUtils.httpDownload(fileUrl, regEditFile, frame);
		if (regEditFile.exists()) {
			logger.fine("Editing file regedit: " + regEditFile.getAbsolutePath());
			String input = FileUtils.readFileToString(regEditFile, RegCassaConstants.UTF_16);
			String output = input.replaceAll(RegCassaConstants.COM1_PORT, usbPortName);
		    FileUtils.writeStringToFile(regEditFile, output);
		} else {
			logger.fine("Error downloading file regedit: " + regEditFile.getAbsolutePath() + ". Launching RegCassaDownloadResourceException");
			throw new RegCassaDownloadResourceException(regEditFile.getName());
		}
		logger.fine("Executing file regedit: " + regEditFile.getAbsolutePath());
		String[] cmd = {RegCassaConstants.REGEDIT_CMD, "/s", regEditFile.getAbsolutePath()};
		Process p = Runtime.getRuntime().exec(cmd);
		p.waitFor();
	}
	
	/**
	 * Cerca nel registro il path alla cartella degli scontrini
	 * 
	 * @return
	 */
	private String findScontriniPathDir() {
		String dir = null;
		try {
			dir = WindowsRegistry.getKeySz(WindowsRegistry.HKEY_LOCAL_MACHINE, (RegCassaUtils.is64bit() ? REG_KEY_PATH_PORTS_CONFIG_ia64 : REG_KEY_PATH_PORTS_CONFIG_i386), REG_KEY_COMMAND_FILE);
		} catch (BackingStoreException e) {}
		return dir;
	}
	
	/**
	 * Crea il path alla cartella degli scontrini
	 * 
	 * @param dir
	 * @return
	 */
	private String mkdirs(String dir) {
		if (!Strings.isNullOrEmpty(dir)) {
			if (dir.endsWith(System.getProperty(RegCassaConstants.FILE_SEPARATOR)) || dir.contains("*.")) {
				dir = dir.substring(0, dir.lastIndexOf(System.getProperty(RegCassaConstants.FILE_SEPARATOR)));
			}
			File fileDir = new File(dir);
			if (!fileDir.isDirectory()) {
				fileDir.mkdirs();
			}
		}
		return dir;
	}

}
