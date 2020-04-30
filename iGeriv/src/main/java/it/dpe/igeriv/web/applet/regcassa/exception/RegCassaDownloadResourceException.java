package it.dpe.igeriv.web.applet.regcassa.exception;

public class RegCassaDownloadResourceException extends Exception {
	private static final long serialVersionUID = 1L;
	private String fileName;
	
	public RegCassaDownloadResourceException(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
	
}
