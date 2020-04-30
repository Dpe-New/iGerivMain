package it.dpe.igeriv.enums;

public enum StatoRichiestaLivellamento {
	
	INSERITO(0), ACCETTATO(1), NON_ACCETTATO(2), VENDUTO(3);
	
	private int value;

	private StatoRichiestaLivellamento(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
}
