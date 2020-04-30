package it.dpe.igeriv.web.applet.regcassa.filewatcher.impl;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.logging.Logger;

import it.dpe.igeriv.web.applet.regcassa.exception.RegCassaCommunicationException;
import it.dpe.igeriv.web.applet.regcassa.exception.TimeoutException;
import it.dpe.igeriv.web.applet.regcassa.filewatcher.FileWatcher;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaUtils;
import it.dpe.igeriv.web.resources.ClientMessageBundle;
import netscape.javascript.JSObject;

public class MCTDetectionInitRegCassaFileWatcher extends FileWatcher {
	private Logger logger;
	private File file;
	private JSObject window = null;
	private Timer timer = null;
	private Date startTime;
	private long millsTimeout;
	
	public MCTDetectionInitRegCassaFileWatcher(File file, JSObject window, Timer timer, long millsTimeout, Logger logger) {
		this.file = file;
		this.window = window;
		this.timer = timer;
		this.startTime = new Date();
		this.millsTimeout = millsTimeout;
		this.logger = logger;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void onChange(File file, Enum result) {
		logger.fine("Entered MCTDetectionInitRegCassaFileWatcher.onChange() with params: file=" + file + ", result=" + result);
		try {
			if (result.equals(Result.OK)) {
				logger.fine("Result.OK");
			} else if (result.equals(Result.KO)) {
				logger.fine("Result.KO");
				throw new RegCassaCommunicationException();
			} else if (result.equals(Result.TIMEOUT)) {
				logger.fine("Result.TIMEOUT");
				throw new TimeoutException();
			}
		} catch (RegCassaCommunicationException e) {
			window.eval("javascript:$.alerts.dialogClass = \"style_1\"; jAlert('" + ClientMessageBundle.get("igeriv.message.reg.cassa.test.communication.timeout") + "', attenzioneMsg.toUpperCase(), function() {$.alerts.dialogClass = null; unBlock(); $(\"#inputText\").val(\"\"); return false;});");
		} catch (TimeoutException e) {
			window.eval("javascript:$.alerts.dialogClass = \"style_1\"; jAlert('" + ClientMessageBundle.get("igeriv.message.reg.cassa.test.communication.timeout") + "', attenzioneMsg.toUpperCase(), function() {$.alerts.dialogClass = null; unBlock(); $(\"#inputText\").val(\"\"); return false;});");
		} catch (Throwable e) {
			logger.fine(RegCassaUtils.getStackTrace(e));
			window.eval("javascript:jAlert('" + ClientMessageBundle.get("igeriv.errore.imprevisto") + "'); unBlock();");
		} finally {
			timer.cancel();
		}
		logger.fine("Exiting MCTDetectionInitRegCassaFileWatcher.onChange()");
	}

	@Override
	protected void doRun() {
		Date currTime = new Date();
		long millsPassed = currTime.getTime() - startTime.getTime();
		if (millsPassed <= millsTimeout) {
			File parentDir = file.getParentFile();
			if (parentDir.isDirectory()) {
				String name = file.getName();
				String baseName = name.substring(0, name.lastIndexOf("."));
				File scontrinoOK = new File(parentDir, baseName + ".OK");
				File scontrinoKO = new File(parentDir, baseName + ".KO");
				if (scontrinoOK.exists()) {
					onChange(scontrinoOK, Result.OK);
				} else if (scontrinoKO.exists()) {
					onChange(scontrinoKO, Result.KO);
				}
			}
		} else {
			onChange(null, Result.TIMEOUT);
		}
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.file + "," + this.window + "," + this.timer + "," + this.startTime + "," + this.millsTimeout + ")";
	}

}
