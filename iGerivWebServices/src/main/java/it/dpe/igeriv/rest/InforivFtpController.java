package it.dpe.igeriv.rest;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Strings;

import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.FileUtils;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.inforiv.bo.InforivImportBo;
import it.dpe.inforiv.dto.ResultDto;

/**
 * Classe Spring Controller che riceve la chiamata REST per l'invio del file
 * di storico delle bolle dal client .NET dell'edicola.
 * 
 * @author mromano
 *
 */
@Controller
@RequestMapping("/inforiv")
public class InforivFtpController {
	public InforivFtpController() {
		System.out.println("Init Ftp Controller");
	}
	private final Logger log = Logger.getLogger(getClass());
	private Tika tika = new Tika();
	
	@Autowired
	private InforivImportBo inforivImportBo;
	
	@Value("${inforiv.zip.storico.client.file.import.path.dir}")
	private String pathZip;

	@Value("${inforiv.file.import.path.dir}")
	private String inputFileInforivDir;
	
	@RequestMapping(value="/ftp", method=RequestMethod.POST)
	@ResponseBody
	public final ResultDto receive(@RequestParam("name") String name, @RequestParam("file") MultipartFile file) {
		ResultDto res = new ResultDto();
		res.setSuccess(true);
		try {
			if (Strings.isNullOrEmpty(name) || file == null || file.getInputStream() == null || !isZipFile(file)) {
				throw new IGerivBusinessException(IGerivMessageBundle.get("imp.ftp.inforiv.ws.error.file.o.nome"));
			}
			File dir = new File(pathZip);
			if (!dir.isDirectory()) {
				dir.mkdirs();
			}
			File fo = new File(dir, name);
			FileUtils.buildFileFromInputStream(file.getInputStream(), fo);
			unzipFiles(fo);
		} catch (Throwable e) {
			res.setSuccess(false);
			res.setErrorMessage(e.getMessage() != null ? e.getMessage() : "");
			res.setStackTrace(StringUtility.getStackTrace(e));
		}
		return res;
	}

	/**
	 * Verifica se e' un file .zip ispezionando i bytes
	 * 
	 * @param MultipartFile file
	 * @return boolean
	 */
	private boolean isZipFile(MultipartFile file) {
		String detect = null;
		try {
			detect = tika.detect(file.getInputStream());
		} catch (IOException e) {
			log.error("Error detecting mime type for file " + file.getName());
		}
		return detect != null && detect.equals("application/zip");
	}

	/**
	 * Decomprime il file .zip nella cartella di input dove vengono presi i file inforiv .DAT
	 *
	 * @param File fo
	 * @throws ZipException
	 * @throws IOException
	 */
	private void unzipFiles(File fo) throws ZipException, IOException {
		ZipFile zipfile = new ZipFile(fo);
		try {
			List<? extends ZipEntry> list = Collections.list(zipfile.entries());
			Collections.sort(list, new Comparator<ZipEntry>() {
				@Override
				public int compare(ZipEntry o1, ZipEntry o2) {
					try {
						Date date1 = DateUtilities.parseDate(o1.getName().substring(o1.getName().lastIndexOf("_") + 1, o1.getName().lastIndexOf(".") - 2), DateUtilities.FORMATO_DATA_YYMMDD);
						Date date2 = DateUtilities.parseDate(o2.getName().substring(o2.getName().lastIndexOf("_") + 1, o2.getName().lastIndexOf(".") - 2), DateUtilities.FORMATO_DATA_YYMMDD);
						return date1.compareTo(date2);
					} catch (ParseException e) {
						//log.error("Error comparing dates in file names - file1 = " + o1.getName() + " file2 = " + o2.getName(), e);
					}
					return 0;
				}
			});
			for (ZipEntry entry : list) {
				FileUtils.unzipEntry(null, zipfile, entry, new File(inputFileInforivDir), false);
			}
		} finally {
			if (zipfile != null) {
				try {
					zipfile.close();
				} catch (IOException e) {
					log.error("Error closing zipfile " + (zipfile.getName() != null ? zipfile.getName() : ""));
				}
			}
		}
	}
	
}
