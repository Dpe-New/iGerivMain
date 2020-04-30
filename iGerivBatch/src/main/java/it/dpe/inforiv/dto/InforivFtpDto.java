package it.dpe.inforiv.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InforivFtpDto {
	private String codEdicolaWeb;
	private String ftpHost;
	private String ftpUser;
	private String ftpPwd;

	public String getCodEdicolaWeb() {
		return codEdicolaWeb;
	}

	public void setCodEdicolaWeb(String codEdicolaWeb) {
		this.codEdicolaWeb = codEdicolaWeb;
	}

	public String getFtpHost() {
		return ftpHost;
	}

	public void setFtpHost(String ftpHost) {
		this.ftpHost = ftpHost;
	}

	public String getFtpUser() {
		return ftpUser;
	}

	public void setFtpUser(String ftpUser) {
		this.ftpUser = ftpUser;
	}

	public String getFtpPwd() {
		return ftpPwd;
	}

	public void setFtpPwd(String ftpPwd) {
		this.ftpPwd = ftpPwd;
	}

}
