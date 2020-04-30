package it.dpe.igeriv.web.applet.regcassa.filewatcher;
import java.io.File;
import java.util.TimerTask;

/**
 * @author mromano
 *
 */
public abstract class FileWatcher extends TimerTask {
	protected enum Result {
		OK, KO, TIMEOUT, ERROR
	}
	
	@Override
	public final void run() {
		doRun();
	}

	protected abstract void doRun();
	
	protected abstract void onChange(File file, Enum Result);

}
