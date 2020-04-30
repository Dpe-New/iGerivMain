package it.dpe.inforiv.bo.impl;

import it.dpe.ftp.FTPClientFactory;
import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.FileUtils;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.util.ZipUtils;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.inforiv.bo.InforivExportFileBo;
import it.dpe.inforiv.exporter.InforivExporter;
import it.dpe.mail.MailingListService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;

/**
 * @author romanom
 *
 */
@Component("InforivExportFileBo")
public class InforivExportFileBoImpl implements InforivExportFileBo {
	private final Logger log = Logger.getLogger(getClass());
	@Autowired
	private IGerivBatchService iGerivBatchBo;
	@Autowired
	private AgenzieService agenzieService;
	@Autowired
	private MailingListService mailingListService;
	@Resource
	private Map<String, InforivExporter> mapExporters;
	
	
	@Value("${inforiv.file.output.path.dir.local}")
	private String pathDirInforiv;
	@Value("${inforiv.flg2.file.import.path.in}")
	private String folderInforivOutput;
	@Value("${inforiv.flg2.file.import.path.bkp}")
	private String folderInforivBackup;
	
	@Override
	public synchronized void exportInforiv() {
		final Timestamp now = iGerivBatchBo.getSysdate();
		FixedFormatManager manager = new FixedFormatManagerImpl();
		List<AnagraficaAgenziaVo> agenzie = iGerivBatchBo.getAgenzieInforiv();
		for (AnagraficaAgenziaVo agenzia : agenzie) {
			Integer codFiegDl = agenzia.getCodFiegDl();
			Map<Integer, File> mapExportFiles = new HashMap<Integer, File>();
			Map<Integer, Map<String, String>> mapFtpParams = new HashMap<Integer, Map<String, String>>();
			for (Map.Entry<String, InforivExporter> entry : mapExporters.entrySet()) {
				String tipoRecord = entry.getKey();
				entry.getValue().exportData(tipoRecord, now, codFiegDl, mapExportFiles, mapFtpParams, manager,null);
			}
			for (Map.Entry<Integer, File> entry : mapExportFiles.entrySet()) {
				Integer codEdicola = entry.getKey();
				File file = entry.getValue();
				//String zipFileName = file.getAbsolutePath().replace(".DAT", "") + "-000001.zip";
				String zipFileName = "E://igeriv//edicole//inforiv//out//zip//" + file.getName().replace(".DAT", "") + "-000001.zip";
				File zipFile = new File(zipFileName);
				try {
					ZipUtils.zip(zipFile, file);
				} catch (IOException e) {
					mailingListService.sendEmail(IGerivMessageBundle.get("dpe.validation.msg.errore.fatale.esportazione.inforiv.subject"), 
							MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.fatale.esportazione.inforiv.body"), file.getName(), e.getLocalizedMessage()));
					log.error("Export Inforiv errore ", e);
				}
				/*
				Map<String, String> params = mapFtpParams.get(codEdicola);
				log.info("Exporting file: " + zipFile.getName() + " to ftp host: " + params.get("hostFtp"));
				String remoteDir = System.getProperty("file.separator");
				if (codFiegDl.equals(IGerivConstants.MORANDINI_CODE)) {
					remoteDir = "In_box";
				}
				sendToFtp(params.get("hostFtp"), params.get("userFtp"), params.get("pwdFtp"), remoteDir, zipFile);
				*/
				
				
				file.delete();
			}
		}
	}
	
	
	@Override
	public void exportInforivFileLocal(AnagraficaAgenziaVo agenzia,File dirOutputLocalDL,File dirOutputBKPLocalDL) {
			final Timestamp now = iGerivBatchBo.getSysdate();
			FixedFormatManager manager = new FixedFormatManagerImpl();

			Integer codFiegDl = agenzia.getCodFiegDl();
			Map<Integer, File> mapExportFiles = new HashMap<Integer, File>();
			Map<Integer, Map<String, String>> mapFtpParams = new HashMap<Integer, Map<String, String>>();
			
			for (Map.Entry<String, InforivExporter> entry : mapExporters.entrySet()) {
				String tipoRecord = entry.getKey();
				entry.getValue().exportData(tipoRecord, now, codFiegDl, mapExportFiles, mapFtpParams, manager,dirOutputLocalDL);
			}
			for (Map.Entry<Integer, File> entry : mapExportFiles.entrySet()) {
				Integer codEdicola = entry.getKey();
				File file = entry.getValue();
				String zipFileName = file.getAbsolutePath().replace(".DAT", "") + "-000001.zip";
				File zipFile = new File(zipFileName);
				try {
					ZipUtils.zip(zipFile, file);
				} catch (IOException e) {
					mailingListService.sendEmail(IGerivMessageBundle.get("dpe.validation.msg.errore.fatale.esportazione.inforiv.subject"), 
							MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.fatale.esportazione.inforiv.body"), file.getName(), e.getLocalizedMessage()));
				}
				
				try {
					FileUtils.copyFile(zipFile, new File(dirOutputBKPLocalDL, StringUtility.stripPercentage(zipFile.getName())));
					log.info("Copy bkp file zip : " + zipFile.getName() );
				} catch (IOException e) {
					e.printStackTrace();
					log.info("Exception - IOException - Copy bkp file Zip : " + e.getMessage());
				}
				Map<String, String> params = mapFtpParams.get(codEdicola);
				log.info("Exporting file: " + zipFile.getName() + " to ftp host: " + params.get("hostFtp"));
				file.delete();
				
				
			}
				
		
	}
	
	/**
	 * AMAZON ANDRIOLI
	 */
	public void exportInforivFileLocal(AnagraficaAgenziaVo agenzia,Integer codEdicolaWeb,File dirOutputLocalDL,File dirOutputBKPLocalDL){
			final Timestamp now = iGerivBatchBo.getSysdate();
			FixedFormatManager manager = new FixedFormatManagerImpl();
	
			Integer codFiegDl = agenzia.getCodFiegDl();
			Map<Integer, File> mapExportFiles = new HashMap<Integer, File>();
			Map<Integer, Map<String, String>> mapFtpParams = new HashMap<Integer, Map<String, String>>();
			
			for (Map.Entry<String, InforivExporter> entry : mapExporters.entrySet()) {
				String tipoRecord = entry.getKey();
				entry.getValue().exportData(tipoRecord, now, codFiegDl, mapExportFiles, mapFtpParams, manager,dirOutputLocalDL);
			}
			
			for (Map.Entry<Integer, File> entry : mapExportFiles.entrySet()) {
				
				Integer codEdicola 	= entry.getKey();
				String codDlFormat 	= String.format("%4s", agenzia.getCodFiegDl().toString()).replace(' ', '0');
				String path 		= pathDirInforiv+codDlFormat+System.getProperty("file.separator")+codEdicola+System.getProperty("file.separator")+folderInforivOutput;
				String pathBkp 		= path+System.getProperty("file.separator")+folderInforivBackup;
				
				File file = entry.getValue();
				String zipFileName = file.getAbsolutePath().replace(".DAT", "") + "-000001.zip";
				File zipFile = new File(zipFileName);
				try {
					ZipUtils.zip(zipFile, file);
				} catch (IOException e) {
					mailingListService.sendEmail(IGerivMessageBundle.get("dpe.validation.msg.errore.fatale.esportazione.inforiv.subject"), 
							MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.fatale.esportazione.inforiv.body"), file.getName(), e.getLocalizedMessage()));
				}
				
				try {
					FileUtils.copyFile(zipFile, new File(pathBkp, StringUtility.stripPercentage(zipFile.getName())));
					log.info("Copy bkp file zip : " + zipFile.getName() );
					file.delete();
					
				} catch (IOException e) {
					e.printStackTrace();
					log.info("Exception - IOException - Copy bkp file Zip : " + e.getMessage());
				}
//				Map<String, String> params = mapFtpParams.get(codEdicola);
//				log.info("Exporting file: " + zipFile.getName() + " to ftp host: " + params.get("hostFtp"));
//				file.delete();
				
				
			}
	}
	
	/**
	 * 
	 * @param coddl
	 * @param codRivenditaDL
	 * @param file
	 */
	public void exportInforivFileFtp(Integer coddl, Integer codRivenditaDL, File file, String bckDir) {
		boolean toRename = false;
		AbbinamentoEdicolaDlVo vo = iGerivBatchBo.getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(coddl, codRivenditaDL);
		if (vo != null) {
			String host = vo.getHostFtp();
			String user = vo.getUserFtp();
			String pwd = vo.getPwdFtp();
			if (host != null && user != null && pwd != null) {
				String remoteDir = System.getProperty("file.separator");
				if (coddl.equals(IGerivConstants.MORANDINI_CODE)) {
					remoteDir = "In_box";
				}
				sendToFtp(host, user, pwd, remoteDir, file);
				try {
					FileUtils.copyFile(file, new File(bckDir, StringUtility.stripPercentage(file.getName())));
					boolean deleted = file.delete();
					int i = 0;
					
				} catch (IOException e) {
					e.printStackTrace();
					log.info("Exception - IOException - Copy bkp file Zip : " + e.getMessage());
				}
				
			} else {
				toRename = true;
			}
		} else {
			toRename = true;
		}
		
		if (toRename) {
			File dest = new File(file.getPath() + "._ERR2");
			file.renameTo(dest);
		}
	}
	
	/**
	 * @param host
	 * @param user
	 * @param pwd
	 * @param remoteDir
	 * @param outputFile 
	 */
	private void sendToFtp(String host, String user, String pwd, String remoteDir, File outputFile) {
		FTPClient client = null;
		FileInputStream fis = null;
		try {
			//client = FTPClientFactory.getClient(host, user, pwd, remoteDir, null, null, FTPClient.ACTIVE_LOCAL_DATA_CONNECTION_MODE, null);
			client = FTPClientFactory.getClient(host, user, pwd, remoteDir, null, null, FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE, null);
			client.setDataTimeout(20000);
			//client.setFileType(FTPClient.ASCII_FILE_TYPE);
			client.setFileType(FTPClient.BINARY_FILE_TYPE);
			fis = new FileInputStream(outputFile);
			client.storeFile(outputFile.getName(), fis);
		} catch (IGerivRuntimeException e) {
			throw e;
		} catch (Throwable e) {
			log.info("host="+host+" user="+user+" pwd="+pwd+" remoteDir="+remoteDir);
			throw new IGerivRuntimeException(e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					throw new IGerivRuntimeException(e);
				}
			}
			if (client != null) {
				try {
					client.logout();
					client.disconnect();
					client = null;
				} catch (IOException e) {
					//throw new IGerivRuntimeException(e);
				}
			}
		}
	}

	public IGerivBatchService getiGerivBatchBo() {
		return iGerivBatchBo;
	}

	public void setiGerivBatchBo(IGerivBatchService iGerivBatchBo) {
		this.iGerivBatchBo = iGerivBatchBo;
	}

	public Map<String, InforivExporter> getMapExporters() {
		return mapExporters;
	}

	public void setMapExporters(Map<String, InforivExporter> mapExporters) {
		this.mapExporters = mapExporters;
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
