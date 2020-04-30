package it.dpe.igeriv.web.applet.regcassa.filewatcher.impl;

import java.io.File;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.logging.Logger;

import it.dpe.igeriv.web.applet.regcassa.exception.ConfigurazioneRapertiException;
import it.dpe.igeriv.web.applet.regcassa.exception.RegCassaCommunicationException;
import it.dpe.igeriv.web.applet.regcassa.exception.TimeoutException;
import it.dpe.igeriv.web.applet.regcassa.filewatcher.FileWatcher;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaUtils;
import it.dpe.igeriv.web.resources.ClientMessageBundle;
import netscape.javascript.JSObject;

public class DitronDetectionInitRegCassaFileWatcher extends FileWatcher {
	private Logger logger;
	private File file;
	private JSObject window = null;
	private Timer timer = null;
	private Date startTime;
	private long millsTimeout;
	
	public DitronDetectionInitRegCassaFileWatcher(File file, JSObject window, Timer timer, long millsTimeout, Logger logger) {
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
		logger.fine("Entered DitronDetectionInitRegCassaFileWatcher.onChange() with params: file=" + file + ", result=" + result);
		Scanner scan = null;
		try {
			if (result.equals(Result.OK)) {
				logger.fine("Result.OK");
				scan = new Scanner(file);
				if (scan != null && scan.hasNext()) {
					String line = scan.nextLine();
					if (line != null && line.length() > 0) {
						throw new ConfigurazioneRapertiException(line);
					}
				}
			} else if (result.equals(Result.KO)) {
				logger.fine("Result.KO");
				throw new RegCassaCommunicationException();
			} else if (result.equals(Result.TIMEOUT)) {
				logger.fine("Result.TIMEOUT");
				throw new TimeoutException();
			}
		} catch (ConfigurazioneRapertiException e) {
			window.eval("javascript:$.alerts.dialogClass = \"style_1\"; jAlert('" + ClientMessageBundle.get("igeriv.message.reg.cassa.ditron.configurazione.reparti.error") + "', attenzioneMsg.toUpperCase(), function() {$.alerts.dialogClass = null; unBlock(); $(\"#inputText\").val(\"\"); return false;});");
		} catch (RegCassaCommunicationException e) {
			window.eval("javascript:$.alerts.dialogClass = \"style_1\"; jAlert('" + ClientMessageBundle.get("igeriv.message.reg.cassa.test.communication.timeout") + "', attenzioneMsg.toUpperCase(), function() {$.alerts.dialogClass = null; unBlock(); $(\"#inputText\").val(\"\"); return false;});");
		} catch (TimeoutException e) {
			window.eval("javascript:$.alerts.dialogClass = \"style_1\"; jAlert('" + ClientMessageBundle.get("igeriv.message.reg.cassa.test.communication.timeout") + "', attenzioneMsg.toUpperCase(), function() {$.alerts.dialogClass = null; unBlock(); $(\"#inputText\").val(\"\"); return false;});");
		} catch (Throwable e) {
			logger.fine(RegCassaUtils.getStackTrace(e));
			window.eval("javascript:jAlert('" + ClientMessageBundle.get("igeriv.errore.imprevisto") + "'); unBlock();");
		} finally {
			timer.cancel();
			if (scan != null) {
				scan.close();
			}
		}
		logger.fine("Exiting DitronDetectionInitRegCassaFileWatcher.onChange()");
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
				File scontrinoOK = new File(parentDir, baseName + ".err");
				if (scontrinoOK.exists()) {
					onChange(scontrinoOK, Result.OK);
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
