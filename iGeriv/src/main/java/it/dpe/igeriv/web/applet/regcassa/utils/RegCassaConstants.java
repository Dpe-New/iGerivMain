package it.dpe.igeriv.web.applet.regcassa.utils;

public class RegCassaConstants {
	
	public enum ProcessResult {
		SUCCESS(1), EXISTS(1), MISSING(0), FAILURE(0), ERROR(-1);
		public int value;
		private ProcessResult(int value) {
			this.value = value;
		}
	};
	
	public static final Integer MCT_FLASH = 1;
	public static final Integer MCT_SPOT = 2;
	public static final Integer DITRON = 10;
	
	public static void main(String[] args) {
		System.out.println(System.getProperty("java.io.tmpdir"));
	}
	
	public static final String DOS_LINEFEED = "\r\n";
	public static final String UNIX_LINEFEED = "\n";
	public static final String REPARTO_IVA_PUBBLICAZIONI = "R5";
//	public static final String DEVCON_FIND_USB_COM_PORTS_CMD = "find \"@FTDIBUS\\VID_0403+PID_6001+AD024UXVA\\*\"";
	public static final String DEVCON_FIND_USB_COM_PORTS_CMD = "find \"@FTDIBUS\\VID_0403+PID_6001+*\"";
	public static final String WIN_32_BIT = "i386";
	public static final String WIN_64_BIT = "ia64";
	public static final String COM1_PORT = "COM1";
	public static final String UTF_8 = "UTF-8";
	public static final String UTF_16 = "UTF-16";
	public static final String COLON = ":";
	public static final String SLASH = "/";
	public static final String DOUBLE_SLASH = "//";
	public static final String DEVCON_EXE = "devcon.exe";
	public static final String REGEDIT_CMD = "regedit";
	public static final String REG_CASSA_APPLET_LOG = "reg_cassa_applet.log";
	public static final String JAVA_IO_TMP_DIR = "java.io.tmpdir";
	public static final String FILE_SEPARATOR = "file.separator";
	public static final String APPLET_DOWNLOADS_URI = "applet_downloads";
	public static final String DITRON_WIN_DRIVER_NAME = "SetupWinECRCom1.9.9FirmwareA_IT.exe";
	public static final String DITRON_UX_DRIVER_NAME = "xditron40182.tar";
	public static final String DITRON_FOLDER_NAME = "ditron";
	public static final String DITRON_SPOOLER_WIN_PARAM = "SoEcrCom.exe /p 1";
	public static final String DITRON_SPOOLER_WIN_SERVICE_NAME = "Ditron ECR Spooler Port1";
	public static final String FTDIBUS_DRIVER_NAME = "FTDIBUS";
	public static final String FTSER2K_DRIVER_NAME = "FTSER2K";
	public static final String DITRON_SPOOLER_WIN_SERVICE_DESCRIPTION = "Driver di stmapa ECR Ditron Linea HT/LC";
	public static final String TARGET_SERVICE_EXE_FILE_NAME = "srvany.exe";
	public static final String INSTALL_SERVICE_EXE = "DpeInstService.exe";
	public static final String USB_SERIAL_PORT_DRIVER_EXE = "CDM_v2.08.30_WHQL_Certified.exe";
	public static final String UTILITIES_FOLDER_NAME = "Utilities";
	public static final String DRIVERS_FOLDER_NAME = "Drivers";
	public static final String CHECK_SERVICE_INSTALLED_CMD = "{0} -x -s \"{1}\"";
	public static final String CHECK_USB_SERIAL_PORT_DRIVER_CMD = "{0} -D -n \"{1}\"";
	public static final String INSTALL_SERVICE_CMD = "{0} -i -f \"{1}\" -s \"{2}\" -d \"{3}\" -p \"{4}\"";
	public static final String START_SERVICE_CMD = "{0} -r -s \"{1}\"";
	public static final String REG_KEY_PATH_INSTALL_DIR_ia64 = "SOFTWARE\\Wow6432Node\\Ditron\\Drivers\\WinEcrCom\\FirmwareA";
	public static final String REG_KEY_PATH_INSTALL_DIR_i386 = "SOFTWARE\\Ditron\\Drivers\\WinEcrCom\\FirmwareA";
	public static final String REG_KEY_NAME_INSTALL_DIR = "InstallationDir";
	
}
