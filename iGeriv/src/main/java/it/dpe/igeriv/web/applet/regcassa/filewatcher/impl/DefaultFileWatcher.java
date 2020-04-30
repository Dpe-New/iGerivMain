package it.dpe.igeriv.web.applet.regcassa.filewatcher.impl;

import java.io.File;

import it.dpe.igeriv.web.applet.regcassa.filewatcher.FileWatcher;

public class DefaultFileWatcher extends FileWatcher {

	@Override
	protected void doRun() {

	}

	@Override
	protected void onChange(File file, Enum Result) {

	}
	
	@Override
	public String toString() {
		return "DefaultFileWatcher";
	}
}
