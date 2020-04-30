package it.dpe.igeriv.exception;

public class NoLocalFtpPathException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public NoLocalFtpPathException() {
		super();
	}
	
	public NoLocalFtpPathException(String msg) {
		super(msg);
	}
	
}
