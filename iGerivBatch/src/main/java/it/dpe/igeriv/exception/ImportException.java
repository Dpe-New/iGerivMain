package it.dpe.igeriv.exception;


public class ImportException extends Exception {
	private static final long serialVersionUID = 1L;
	private String descrizione;

	public ImportException(Throwable e) {
		super(e);
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

}
