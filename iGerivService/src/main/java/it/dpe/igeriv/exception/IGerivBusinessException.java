package it.dpe.igeriv.exception;

import java.util.List;

/**
 * Classe per contenere le eccezioni di business, che devono 
 * essere trattate dall'applicazione.
 * 
 * @author romanom
 *
 */
public class IGerivBusinessException extends Exception {
	private static final long serialVersionUID = 1L;
	private String stackTraceString;
	private Integer messageCode;
	private List<Object> listParams;
	
	public IGerivBusinessException() {
		super();
	}
	
	public IGerivBusinessException(String message, String stackTraceString) {
		super(message);
		this.stackTraceString = stackTraceString;
	}
	
	public IGerivBusinessException(Integer messageCode) {
		super();
		this.messageCode = messageCode;
	}
	
	public IGerivBusinessException(Exception e) {
		super(e);
	}

	public IGerivBusinessException(String message) {
		super(message);
	}

	public Integer getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(Integer messageCode) {
		this.messageCode = messageCode;
	}

	public List<Object> getListParams() {
		return listParams;
	}

	public void setListParams(List<Object> listParams) {
		this.listParams = listParams;
	}

	public String getStackTraceString() {
		return stackTraceString;
	}
	
}
