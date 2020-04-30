package it.dpe.inforiv.exporter.impl;

import static ch.lambdaj.Lambda.forEach;
import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.inforiv.dto.input.InforivBaseDto;
import it.dpe.inforiv.dto.input.InforivDto;
import it.dpe.mail.MailingListService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

/**
 * @author romanom
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class InforivBaseExporter {
	@Value("${inforiv.file.export.path.dir}")
	private String exportPathDir;
	@Autowired
	private MailingListService mailingListService;
	@Autowired
	private IGerivBatchService bo;
	@Autowired
	private AgenzieService agenzieService;
	
	@Value("${inforiv.file.import.path.dir.local}")
	private String pathDirInforiv;
	@Value("${inforiv.flg2.file.import.path.in}")
	private String folderInforivOutput;
	@Value("${inforiv.flg2.file.import.path.bkp}")
	private String folderInforivBackup;
	
	
	
	/**
	 * @param tipoRecord
	 * @param now
	 * @param codFiegDl
	 * @param mapExportFiles
	 * @param mapFtpParams
	 * @param manager
	 * @param list
	 */
	protected void exportLines(String tipoRecord, Timestamp now, Integer codFiegDl, Map<Integer, File> mapExportFiles, Map<Integer, Map<String, String>> mapFtpParams, FixedFormatManager manager, List list,File dirOutputLocalDL) {
		if (!list.isEmpty()) {
			Integer codEdicola = ((InforivBaseDto) list.get(0)).getCodEdicola();
			AbbinamentoEdicolaDlVo edicola = bo.getAbbinamentoEdicolaDlVoByCodEdicolaWeb(codEdicola);
			Integer codEdicolaDl = edicola.getCodEdicolaDl();
			addFtpParams(mapFtpParams, codEdicola, edicola);
			forEach(list, InforivBaseDto.class).setCodEdicola(codEdicolaDl);
			forEach(list, InforivBaseDto.class).setTipoRecord(tipoRecord);
			File file = null;
			if(agenzieService.isMod2Inforiv(codFiegDl)){
				//ticket 0000059
				file = createExportFileInforivMod2(now, mapExportFiles, codEdicola, codEdicolaDl, codFiegDl,dirOutputLocalDL);
			}else{
				file = createExportFile(now, mapExportFiles, codEdicola, codEdicolaDl, codFiegDl,dirOutputLocalDL);
			}
			for (InforivBaseDto dto : (List<InforivBaseDto>) list) {
				exportLine(dto, file, manager);
			}
		}
	}
	
	/**
	 * @param now
	 * @param mapExportFiles
	 * @param codEdicola
	 * @param codEdicolaDl
	 * @param codFiegDl 
	 * @return
	 * @throws IOException 
	 */
	private File createExportFile(final Timestamp now, Map<Integer, File> mapExportFiles, Integer codEdicola, Integer codEdicolaDl, Integer codFiegDl,File dirOutputLocalDL) {
		File file = mapExportFiles.get(codEdicola);
		try {
			if (file == null) {
				String timestampAsString = DateUtilities.getTimestampAsString(now, DateUtilities.FORMATO_DATA_YYMMDDHHMMSS);
				String yymmdd = timestampAsString.substring(0, 6);
				String hhmmss = timestampAsString.substring(6);
				String path = null;
				if(dirOutputLocalDL!=null){
					path = dirOutputLocalDL.getAbsolutePath();
				}else{
					path = exportPathDir + System.getProperty("file.separator") + codEdicola;
				}
				
				File dirEdicola = new File(exportPathDir + System.getProperty("file.separator") + codEdicola);
				if (!dirEdicola.isDirectory()) {
					dirEdicola.mkdirs();
				}
				String codEd = new DecimalFormat("0000").format(codEdicolaDl);
				String codDL = new DecimalFormat("000").format(codFiegDl);
				String fileNameWithTime = codEd + "_" + codDL + "_" + yymmdd + "_" + hhmmss + "_" + yymmdd;
				Integer counter = getFileCounter(fileNameWithTime, path);
				String fileName = fileNameWithTime + new DecimalFormat("00").format(counter) + ".DAT";
				file = new File(path + System.getProperty("file.separator") + fileName);
				if (!file.exists()) {
					file.createNewFile();
				}
				mapExportFiles.put(codEdicola, file);
			}
		} catch (IOException e) {
			throw new IGerivRuntimeException(e);
		}
		return file;
	}
	
	/**
	 * @param now
	 * @param mapExportFiles
	 * @param codEdicola
	 * @param codEdicolaDl
	 * @param codFiegDl 
	 * @return
	 * @throws IOException 
	 */
	private File createExportFileInforivMod2(final Timestamp now, Map<Integer, File> mapExportFiles, Integer codEdicola, Integer codEdicolaDl, Integer codFiegDl,File dirOutputLocalDL) {
		File file = mapExportFiles.get(codEdicola);
		
		try {
			if (file == null) {
				String timestampAsString = DateUtilities.getTimestampAsString(now, DateUtilities.FORMATO_DATA_YYMMDDHHMMSS);
				String yymmdd = timestampAsString.substring(0, 6);
				String hhmmss = timestampAsString.substring(6);
				
				String codDlFormat = String.format("%4s", codFiegDl).replace(' ', '0');
				String path = pathDirInforiv +codDlFormat+System.getProperty("file.separator") + codEdicolaDl+System.getProperty("file.separator")+folderInforivOutput;
				File dirEdicola = new File(path);
				if (!dirEdicola.isDirectory()) {
					dirEdicola.mkdirs();
				}
				String codEd = new DecimalFormat("0000").format(codEdicolaDl);
				String codDL = new DecimalFormat("000").format(codFiegDl);
				String fileNameWithTime = codEd + "_" + codDL + "_" + yymmdd + "_" + hhmmss + "_" + yymmdd;
				Integer counter = getFileCounter(fileNameWithTime, path);
				String fileName = fileNameWithTime + new DecimalFormat("00").format(counter) + ".DAT";
				file = new File(path + System.getProperty("file.separator") + fileName);
				if (!file.exists()) {
					file.createNewFile();
				}
				mapExportFiles.put(codEdicolaDl, file);
				
			}
		} catch (IOException e) {
			throw new IGerivRuntimeException(e);
		}
		return file;
	}
	
	/**
	 * Ritorna l'ultimo progressivo per un file il cui nome 
	 * contiene lo stesso identificativo di processo e la stessa data.
	 * @param exportDir 
	 * 
	 * @param String name
	 * @return String
	 */
	private static Integer getFileCounter(String name, String exportDir) {
		File fileExport = new File(exportDir);
		File[] listFiles = fileExport.listFiles();
		int higher = 0;
		if (listFiles != null) {
			for (int i = 0; i < listFiles.length; i++) {
				String fileName = listFiles[i].getName();
				if (fileName.contains(name)) {
					int counter = Integer.parseInt(fileName.substring(name.length(), fileName.length() - 4));
					if (counter > higher) {
						higher = counter;
					}
				}
	        }
		}
		return higher + 1;
	}
	
	/**
	 * @param mapFtpParams
	 * @param codEdicola
	 * @param edicola
	 */
	private void addFtpParams(Map<Integer, Map<String, String>> mapFtpParams, Integer codEdicola, AbbinamentoEdicolaDlVo edicola) {
		if (!mapFtpParams.containsKey(edicola.getCodEdicolaDl())) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("hostFtp", edicola.getHostFtp());
			params.put("userFtp", edicola.getUserFtp());
			params.put("pwdFtp", edicola.getPwdFtp());
			mapFtpParams.put(codEdicola, params);
		}
	}
	
	/**
	 * @param dto
	 * @param file
	 * @param manager 
	 */
	/* ticket 0000238
	private void exportLine(InforivDto dto, File file, FixedFormatManager manager) {
		String riga = manager.export(dto);
		riga = riga.replaceAll("\\+", " ");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
			if (file.length() > 0) {
				writer.newLine();
			}
			writer.write(riga);
			writer.close();
		} catch (IOException e) {
			mailingListService.sendEmail(IGerivMessageBundle.get("dpe.validation.msg.errore.fatale.esportazione.inforiv.subject"), 
					MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.fatale.esportazione.inforiv.body"), file.getName(), e.getLocalizedMessage()));
			throw new IGerivRuntimeException(e);
		}
	}
	*/
	//ticket 0000238
	private void exportLine(InforivDto dto, File file, FixedFormatManager manager) {
		String riga = manager.export(dto);
		riga = riga.replaceAll("\\+", " ");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
			writer.write(riga);
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			mailingListService.sendEmail(IGerivMessageBundle.get("dpe.validation.msg.errore.fatale.esportazione.inforiv.subject"), 
					MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.fatale.esportazione.inforiv.body"), file.getName(), e.getLocalizedMessage()));
			throw new IGerivRuntimeException(e);
		}
	}
	
	
	

	public String getExportPathDir() {
		return exportPathDir;
	}

	public void setExportPathDir(String exportPathDir) {
		this.exportPathDir = exportPathDir;
	}

	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

	public AgenzieService getAgenzieService() {
		return agenzieService;
	}

	public void setAgenzieService(AgenzieService agenzieService) {
		this.agenzieService = agenzieService;
	}

	public IGerivBatchService getBo() {
		return bo;
	}

	public void setBo(IGerivBatchService bo) {
		this.bo = bo;
	}

	public String getPathDirInforiv() {
		return pathDirInforiv;
	}

	public void setPathDirInforiv(String pathDirInforiv) {
		this.pathDirInforiv = pathDirInforiv;
	}

	public String getFolderInforivOutput() {
		return folderInforivOutput;
	}

	public void setFolderInforivOutput(String folderInforivOutput) {
		this.folderInforivOutput = folderInforivOutput;
	}

	public String getFolderInforivBackup() {
		return folderInforivBackup;
	}

	public void setFolderInforivBackup(String folderInforivBackup) {
		this.folderInforivBackup = folderInforivBackup;
	}

	

	
	
	
}
