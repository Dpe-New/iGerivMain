package it.dpe.igeriv.exception;

public class ProdottoNonEsistenteExcepton extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ProdottoNonEsistenteExcepton(String msg) {
		super(msg);
	}

}
