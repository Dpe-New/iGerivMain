package it.dpe.igeriv.util;

import it.dpe.ftp.FTPClientFactory;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.exception.NoLocalFtpPathException;
import it.dpe.igeriv.resources.IGerivMessageBundle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.log4j.Logger;

public class FtpUtils {
	private static final Logger log = Logger.getLogger(FtpUtils.class);
	
	/**
	 * Implementa la logica per la costruzione del path dei file locali in cui trasferire il contenuto dei file remoti
	 * 
	 * @author mromano
	 *
	 */
	public static interface FtpLocalFilesBuilder {
		public List<String> build(List<FTPFile> ftpFiles);
	}
	
	/**
	 * Filtra e trasferisce file da FTP
	 * 
	 * @param host Server FTP
	 * @param user Utente FTP
	 * @param pwd Password FTP
	 * @param remoteDir Directory remota
	 * @param localFileBuilder Oggetto "command" che implementa la logica per la costruzione del path dei file locali in cui trasferire il contenuto dei file remoti (obbligatorio)
	 * @param ftpFileFilter Filtro da applicare sulla lista di file remoti (puo' essere nullo)
	 * @param deleteRemoteFile se true cancella il file su FTP
	 * @return List<String> Lista dei file locali
	 * @throws IOException
	 * @throws NoLocalFtpPathException 
	 */
	public static List<String> filterAndTransferFtpFiles(String host, String user, String pwd, String remoteDir, FtpLocalFilesBuilder localFileBuilder, FTPFileFilter ftpFileFilter, Integer connectTimeout, Integer dataTimeout, boolean deleteRemoteFile) throws IOException, NoLocalFtpPathException {
		List<String> result = new ArrayList<String>();
		List<FTPFile> ftpFiles = new ArrayList<FTPFile>();
		FTPClient ftpClient = null;
		FTPFile lastFTPFile = null;
		try {
			ftpClient = FTPClientFactory.getClient(host, user, pwd, remoteDir, null, connectTimeout, null, null);
			if (dataTimeout != null) {
				ftpClient.setDataTimeout(dataTimeout);
			} else {
				ftpClient.setDataTimeout(IGerivConstants.FTP_DATA_TIMEOUT);
			}
			FTPFile[] files = null;
			if (ftpFileFilter != null) {
				files = ftpClient.listFiles(null, ftpFileFilter);
			} else {
				files = ftpClient.listFiles();
			}
			for (FTPFile ftpFile : files) {
				if (!ftpFile.isFile()) {
					continue;
				}
				lastFTPFile = ftpFile;
				ftpFiles.add(ftpFile);
			}
			if (ftpFiles != null && !ftpFiles.isEmpty()) {
				List<String> localFiles = null;
				if (localFileBuilder != null) {
					localFiles = localFileBuilder.build(ftpFiles);
				} else {
					throw new NoLocalFtpPathException();
				}
				for (int i = 0; i < ftpFiles.size(); ++i) {
					FTPFile ftpFile = ftpFiles.get(i);
					if (!ftpFile.isFile()) {
						continue;
					}
					lastFTPFile = ftpFiles.get(i);
					FileOutputStream fos = null;
					try {
						File file = new File(localFiles.get(i));
						if (!file.getParentFile().isDirectory()) {
							file.getParentFile().mkdirs();
						}
						fos = new FileOutputStream(file);
						boolean retrieveFile = ftpClient.retrieveFile(ftpFile.getName(), fos);
						if (retrieveFile) {
							result.add(localFiles.get(i));
						}
					} finally {
						if (fos != null) {
							fos.close();
							fos = null;
						}
					}
				}
			}
			if (deleteRemoteFile) {
				for (FTPFile ftpFile : files) {
					if (ftpFile.isFile()) {
						ftpClient.deleteFile(ftpFile.getName());
					}
				}
			}
		} catch (SocketTimeoutException e) {
			// Non loggo perche' molto frequente e non bloccante
		} catch (IOException e) {
			if (e.getCause() != null && !(e.getCause() instanceof SocketTimeoutException)) {
				// Loggo soltanto se non e' una SocketTimeoutException (molto frequente)
				log.error(String.format("Errore FTP in it.dpe.igeriv.util.FTPUtils.getFileImport host=%s user=%s : ", host, user), e);
			}
			throw e;
		} catch (NoLocalFtpPathException e) {
			log.error(String.format("Errore FTP in it.dpe.igeriv.util.FTPUtils.getFileImport host=%s user=%s : ", host, user), e);
			throw e;
		} catch (Throwable e) {
			log.error(String.format("Errore FTP in it.dpe.igeriv.util.FTPUtils.getFileImport host=%s user=%s : ", host, user), e);
			throw new IGerivRuntimeException(getErrorFtp(ftpClient, e, lastFTPFile));
		} finally {
			closeFtpClient(ftpClient);
		}
		return result;
	}
	
	/**
	 * Trasferisce i file da remoto ai file locali
	 * 
	 * @param ftpClientFactory
	 *            istamnza ftp
	 * @param ftpFiles
	 *            list file da ricevere
	 * @param localFiles
	 *            lista de file locali copiati dai file remoti
	 * @return la lista dei file ricevuti
	 * @throws IOException 
	 */

	public static String[] transferFromFtpClient(String host, String user, String pwd, String remoteDir, FTPFile[] ftpFiles, String[] localFiles) throws IOException {
		FTPClient ftpClient = null;
		FTPFile lastFTPFile = null;
		ArrayList<String> result = new ArrayList<String>();
		try {
			ftpClient = FTPClientFactory.getClient(host, user, pwd, remoteDir, null, null, null, null);
			for (int i = 0; i < ftpFiles.length; ++i) {
				FTPFile ftpFile = ftpFiles[i];
				if (!ftpFile.isFile()) {
					continue;
				}
				lastFTPFile = ftpFiles[i];
				FileOutputStream fos = null;
				try {
					File file = new File(localFiles[i]);
					if (!file.getParentFile().isDirectory()) {
						file.getParentFile().mkdirs();
					}
					fos = new FileOutputStream(file);
					boolean retrieveFile = ftpClient.retrieveFile(ftpFile.getName(), fos);
					if (retrieveFile) {
						result.add(localFiles[i]);
					}
				} finally {
					if (fos != null) {
						fos.close();
					}
				}
			}
		} catch (IOException e) {
			log.error("Errore FTP in it.dpe.igeriv.util.FTPUtils.transferFromFtpClient : ", e);
			throw e;
		} catch (Throwable e) {
			log.error("Errore FTP in it.dpe.igeriv.util.FTPUtils.transferFromFtpClient : ", e);
			throw new IGerivRuntimeException(getErrorFtp(ftpClient, e, lastFTPFile));
		} finally {
			closeFtpClient(ftpClient);
		}
		return result.toArray(new String[0]);
	}
	
	/**
	 * Elimina il file del server ftp
	 * 
	 * @param ftpClientFactory
	 * @param ftpFiles
	 *            collezione di filee da eliminare
	 * @return nr file eliminati
	 * @throws IOException 
	 */
	public static int delete(String host, String user, String pwd, String remoteDir, FTPFile[] ftpFiles) throws IOException {
		FTPClient ftpClient = null;
		FTPFile lastFTPFile = null;
		int result = 0;
		try {
			ftpClient = FTPClientFactory.getClient(host, user, pwd, remoteDir, null, null, null, null);
			for (int i = 0; i < ftpFiles.length; ++i) {
				FTPFile ftpFile = ftpFiles[i];
				if (!ftpFile.isFile()) {
					continue;
				}
				lastFTPFile = ftpFile;
				result += ftpClient.deleteFile(ftpFile.getName()) ? 1 : 0;
			}
			return result;
		} catch (IOException e) {
			log.error("Errore FTP in it.dpe.igeriv.util.FTPUtils.delete : ", e);
			throw e;
		} catch (Throwable e) {
			log.error("Errore FTP in it.dpe.igeriv.util.FTPUtils.delete : ", e);
			throw new IGerivRuntimeException(getErrorFtp(ftpClient, e, lastFTPFile));
		} finally {
			closeFtpClient(ftpClient);
		}
	}
	
	/**
	 * @param client
	 */
	private static void closeFtpClient(FTPClient client) {
		if (client != null) {
			try {
				client.logout();
				client.disconnect();
				client = null;
			} catch (IOException e) {
				//log.error("Errore FTP in it.dpe.igeriv.util.FTPUtils.closeFtpClient : ", e);
			}
		}
	}
	
	private static String getErrorFtp(FTPClient ftpClientFactory, Throwable ex, FTPFile lastFTPFile) {
		String msgErr = ex != null ? ex.getLocalizedMessage() : "";
		String msgFtp = ftpClientFactory != null ? "Host= " + ftpClientFactory.getRemoteAddress().getHostName() + " Port= " + ftpClientFactory.getRemotePort() : "";
		String fileName = "File FTP = " + (lastFTPFile != null && lastFTPFile.getName() != null ? lastFTPFile.getName() : "");
		String result = String.format("%s: %s %s %s", IGerivMessageBundle.get("rtae.ftp.error"), msgFtp, fileName, msgErr);
		return result;
	}

}
