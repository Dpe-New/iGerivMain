package it.dpe.igeriv.web.applet.regcassa.init.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import it.dpe.igeriv.web.applet.regcassa.filewatcher.impl.MCTDetectionInitRegCassaFileWatcher;
import it.dpe.igeriv.web.applet.regcassa.init.InitRegCassa;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaConstants;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaUtils;
import netscape.javascript.JSObject;

public class MCTInitRegCassaImpl extends InitRegCassa {
	private Logger logger;
	private static final String INIT_FILE_NAME = "iva.txt";
	private static final long TIMEOUT_MILLS = 5000;
	private String defaultLocalDir;
	private String installationDir; 
	private JSObject window = null;
	private long millsTaskInterval;
	private String initContent; 
	
	public MCTInitRegCassaImpl(String initContent, String defaultLocalDir, String installationDir, JSObject window, long millsTaskInterval, Logger logger) {
		this.initContent = initContent;
		this.defaultLocalDir = defaultLocalDir;
		this.installationDir = installationDir;
		this.window = window;
		this.millsTaskInterval = millsTaskInterval;
		this.logger = logger;
	}
	
	@Override
	public void init() throws IOException {
		logger.fine("Entered MCTInitRegCassaImpl.init()");
		File initFile = new File(new File(installationDir) + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + INIT_FILE_NAME);
		writeInitFile(initFile);
		File initFileDest = new File(defaultLocalDir + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + INIT_FILE_NAME);
		RegCassaUtils.copyFile(initFile, initFileDest);
		Timer timer = new Timer();
		TimerTask task = new MCTDetectionInitRegCassaFileWatcher(initFileDest, window, timer, TIMEOUT_MILLS, logger);
		timer.schedule(task, new Date(), new Long(millsTaskInterval));
		logger.fine("Exiting MCTInitRegCassaImpl.init()");
	}

	/**
	 * @param initFile
	 * @throws IOException
	 */
	private void writeInitFile(File initFile) throws IOException {
		logger.fine("Entered MCTInitRegCassaImpl.writeInitFile()");
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
		logger.fine("Exiting MCTInitRegCassaImpl.writeInitFile()");
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.initContent + "," + this.defaultLocalDir + "," + this.installationDir + "," + this.window + "," + this.millsTaskInterval + ")";
	}

}
