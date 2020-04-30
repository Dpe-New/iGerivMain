package it.dpe.igeriv.exception;

public class MimeMessageContentNotSupportedException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public MimeMessageContentNotSupportedException() {
		
	}
	
	public MimeMessageContentNotSupportedException(String msg) {
		super(msg);
	}
}
