package it.dpe.igeriv.enums;

public enum StatoRichiestaRifornimentoLivellamento {
	INSERITO(0), DA_RITIRARE(1), RITIRATI(2);    
	
	//WORKFLOW : INSERITO -> DA_RITIRAR --> RITIRATI (CHIUSO)
	
	private int value;

	private StatoRichiestaRifornimentoLivellamento(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
