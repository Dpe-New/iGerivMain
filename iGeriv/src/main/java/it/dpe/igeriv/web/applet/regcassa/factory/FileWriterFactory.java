package it.dpe.igeriv.web.applet.regcassa.factory;

import java.io.File;
import java.util.logging.Logger;

import it.dpe.igeriv.web.applet.regcassa.filewriter.FileWriter;
import it.dpe.igeriv.web.applet.regcassa.filewriter.impl.DefaultFileWriter;
import it.dpe.igeriv.web.applet.regcassa.filewriter.impl.DitronFileWriter;
import it.dpe.igeriv.web.applet.regcassa.filewriter.impl.MCTFileWriter;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaConstants;

public class FileWriterFactory {
	
	public static FileWriter buildFileWriter(Integer type, File file, Logger logger) {
		logger.fine("Entered FileWriterBuilder.buildFileWriter()");
		if (type != null) {
			FileWriter fileWriter = null; 
			if (type.equals(RegCassaConstants.MCT_FLASH) || type.equals(RegCassaConstants.MCT_SPOT)) {
				fileWriter = new MCTFileWriter(file, logger);
			} else if (type.equals(RegCassaConstants.DITRON)) {
				fileWriter = new DitronFileWriter(file, logger);
			} else {
				fileWriter = new DefaultFileWriter();
			}
			logger.fine("Exiting FileWriterBuilder.buildFileWriter() with fileWriter=" + fileWriter);
			return fileWriter;
		}
		DefaultFileWriter defaultFileWriter = new DefaultFileWriter();
		logger.fine("Exiting FileWriterBuilder.buildFileWriter() with defaultFileWriter=" + defaultFileWriter);
		return defaultFileWriter;
	}
	
}
