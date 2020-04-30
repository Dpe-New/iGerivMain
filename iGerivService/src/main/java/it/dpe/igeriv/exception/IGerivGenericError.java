package it.dpe.igeriv.exception;

public class IGerivGenericError extends Exception {
	private static final long serialVersionUID = 1L;
	
	public IGerivGenericError(Throwable cause) {
		super(cause);
	}
	
	public IGerivGenericError(String message, Throwable cause) {
		super(message, cause);
	}
}
