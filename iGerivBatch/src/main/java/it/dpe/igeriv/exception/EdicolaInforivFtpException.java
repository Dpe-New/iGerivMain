package it.dpe.igeriv.exception;

/**
 * @author romanom
 *
 */
public class EdicolaInforivFtpException extends Exception {
	private static final long serialVersionUID = 1L;
	private String ftpHost;
	private String ftpUser;
	private String ftpPwd;
	
	public EdicolaInforivFtpException() {
	}
	
	public EdicolaInforivFtpException(String ftpHost, String ftpUser, String ftpPwd) {
		super();
		this.ftpHost = ftpHost;
		this.ftpUser = ftpUser;
		this.ftpPwd = ftpPwd;
	}

	public String getFtpHost() {
		return ftpHost;
	}

	public String getFtpUser() {
		return ftpUser;
	}

	public String getFtpPwd() {
		return ftpPwd;
	}
	
}	
