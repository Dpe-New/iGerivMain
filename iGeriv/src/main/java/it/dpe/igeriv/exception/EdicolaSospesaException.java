package it.dpe.igeriv.exception;

import org.springframework.security.core.AuthenticationException;

public class EdicolaSospesaException extends AuthenticationException {
	private static final long serialVersionUID = 1L;
	
	public EdicolaSospesaException(String msg) {
		super(msg);
	}
}
