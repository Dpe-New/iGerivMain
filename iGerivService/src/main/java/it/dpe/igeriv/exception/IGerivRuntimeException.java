package it.dpe.igeriv.exception;


/**
 * Classe per contenere le eccezioni di sistema, da cui non si 
 * può recuperare lo stato dell'applicazione.
 * 
 * @author romanom
 *
 */
public class IGerivRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public IGerivRuntimeException(Throwable e) {
		super(e);
	}
	
	public IGerivRuntimeException() {
		super();
	}
	
	public IGerivRuntimeException(Exception e) {
		super(e);
	}
	
	public IGerivRuntimeException(String msg) {
		super(msg);
	}
	
	public String getMessage() {
		return super.getMessage();
	}
	
	
}
