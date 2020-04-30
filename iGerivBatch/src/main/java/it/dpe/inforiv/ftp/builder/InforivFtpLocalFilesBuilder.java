package it.dpe.inforiv.ftp.builder;

import it.dpe.igeriv.util.FtpUtils.FtpLocalFilesBuilder;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("InforivFtpLocalFilesBuilder")
@Scope("prototype")
public class InforivFtpLocalFilesBuilder implements FtpLocalFilesBuilder {
	@Value("${inforiv.zip.file.import.path.dir}")
	private String pathZipFiles;
	private String ftpUser;
	
	public InforivFtpLocalFilesBuilder() {}
	
	public InforivFtpLocalFilesBuilder(String ftpUser) {
		this.ftpUser = ftpUser;
	}
	
	@Override
	public List<String> build(List<FTPFile> ftpFiles) {
		List<String> listFiles = new ArrayList<String>();
		for (FTPFile file : ftpFiles) {
			listFiles.add(pathZipFiles + System.getProperty("file.separator") + ftpUser + System.getProperty("file.separator") + file.getName());
		}
		return listFiles;
	}

	public String getPathZipFiles() {
		return pathZipFiles;
	}

	public void setPathZipFiles(String pathZipFiles) {
		this.pathZipFiles = pathZipFiles;
	}

	public String getFtpUser() {
		return ftpUser;
	}

	public void setFtpUser(String ftpUser) {
		this.ftpUser = ftpUser;
	}
	
}
