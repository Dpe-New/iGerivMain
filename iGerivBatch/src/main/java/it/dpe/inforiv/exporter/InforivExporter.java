package it.dpe.inforiv.exporter;

import java.io.File;
import java.sql.Timestamp;
import java.util.Map;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

/**
 * @author romanom
 *
 */
public interface InforivExporter {
	
	/**
	 * @param tipoRecord 
	 * @param now
	 * @param codFiegDl
	 * @param mapExportFiles
	 * @param mapFtpParams
	 */
	public void exportData(String tipoRecord, Timestamp now, Integer codFiegDl, Map<Integer, File> mapExportFiles, Map<Integer, Map<String, String>> mapFtpParams, FixedFormatManager manager,File dirOutputLocalDL);
	
}
