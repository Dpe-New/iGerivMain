package it.dpe.inforiv.importer.scanner;

import it.dpe.igeriv.util.IGerivBatchConstants;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

import org.apache.commons.io.filefilter.FileFileFilter;
import org.springframework.integration.file.DefaultDirectoryScanner;
import org.springframework.stereotype.Component;

/**
 * Implementa l'ordinamento dei file nella cartella di input dei file inforiv.
 * 
 * @author romanom
 *
 */
@Component("fileInforivDirectoryScanner")
public class FileInforivDirectoryScanner extends DefaultDirectoryScanner {
	
	@Override
	protected File[] listEligibleFiles(File directory) {
		File[] list = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return new File(dir, name).isFile() && !name.contains(IGerivBatchConstants.FILE_INFORIV_PREFIX_INVIO_EDICOLA_DL);
			}
		});
		if (list.length > 0) {
			return list;
		}
		return directory.listFiles((FileFilter) FileFileFilter.FILE);
	}
	
}
