package it.dpe.igeriv.web.applet.regcassa.init.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import it.dpe.igeriv.web.applet.regcassa.exception.RegCassaMissingActiveProcessException;
import it.dpe.igeriv.web.applet.regcassa.filewatcher.impl.DitronDetectionInitRegCassaFileWatcher;
import it.dpe.igeriv.web.applet.regcassa.init.InitRegCassa;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaConstants;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaUtils;
import netscape.javascript.JSObject;

public class DitronInitRegCassaImpl extends InitRegCassa {
	private Logger logger;
	private static final String INIT_FILE_NAME = "Programmazione_Reparti.txt";
	private static final String SERVICE_NAME_EXE = "srvany.exe";
	private static final String UTILITIES_DIR = "Utilities";
	private static final long TIMEOUT_MILLS = 5000;
	private String defaultLocalDir;
	private String installationDir; 
	private JSObject window = null;
	private long millsTaskInterval;
	private String initContent; 
	
	public DitronInitRegCassaImpl(String initContent, String defaultLocalDir, String installationDir, JSObject window, long millsTaskInterval, Logger logger) {
		this.initContent = initContent;
		this.defaultLocalDir = defaultLocalDir;
		this.installationDir = installationDir;
		this.window = window;
		this.millsTaskInterval = millsTaskInterval;
		this.logger = logger;
	}
	
	@Override
	public void init() throws IOException, RegCassaMissingActiveProcessException {
		logger.fine("Entered DitronInitRegCassaImpl.init()");
		boolean processExists = RegCassaUtils.isProcessRunning(SERVICE_NAME_EXE);
		if (!processExists) {
			processExists = RegCassaUtils.executeProcess(new File(installationDir + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + UTILITIES_DIR + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + SERVICE_NAME_EXE));
			if (!processExists) {
				throw new RegCassaMissingActiveProcessException();
			}
		}
		File initFile = new File(new File(defaultLocalDir) + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + INIT_FILE_NAME);
		writeInitFile(initFile);
		Timer timer = new Timer();
		TimerTask task = new DitronDetectionInitRegCassaFileWatcher(initFile, window, timer, TIMEOUT_MILLS, logger);
		timer.schedule(task, new Date(), new Long(millsTaskInterval));
		logger.fine("Exiting DitronInitRegCassaImpl.init()");
	}

	/**
	 * @param initFile
	 * @throws IOException
	 */
	private void writeInitFile(File initFile) throws IOException {
		logger.fine("Entered DitronInitRegCassaImpl.writeInitFile()");
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			fw = new FileWriter(initFile);
			pw = new PrintWriter(fw);
			String[] split = initContent.split("<br/>");
			for (String line : split) {
				pw.println(line);
			}
		} finally {
			if (fw != null) {
				fw.close();
			}
			if (pw != null) {
				pw.close();
			}
		}
		logger.fine("Exiting DitronInitRegCassaImpl.writeInitFile()");
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.initContent + "," + this.defaultLocalDir + "," + this.installationDir + "," + this.window + "," + this.millsTaskInterval + ")";
	}

}
