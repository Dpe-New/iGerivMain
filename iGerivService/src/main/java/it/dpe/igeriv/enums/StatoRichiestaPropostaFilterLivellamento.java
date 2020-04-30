package it.dpe.igeriv.enums;

public enum StatoRichiestaPropostaFilterLivellamento {
	
	INSERITI(0), DA_RITIRARE(1), RITIRATI(2), DA_CONSEGNARE(3), CONSEGNATI(4);
	
	private int value;

	private StatoRichiestaPropostaFilterLivellamento(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
}
