package it.dpe.igeriv.util;


public class Types {
	
	public static enum ContoDepositoType {
		TUTTO,
		ESCLUDI_CONTO_DEPOSITO,
		SOLO_CONTO_DEPOSITO;
		private String value;
		
        public String getKey() {
        	return this.name(); 
        }
        
        public String getValue() {
        	return this.value; 
        }

		public void setValue(String value) {
			this.value = value;
		}
    }
	
	public static enum TipoPubblicazioneType {
		TUTTO,
		ESCLUDI_PUBBLICAZIONI_SCADUTE,
		SOLO_PUBBLICAZIONI_SCADUTE;
		private String value;
		
        public String getKey() {
        	return this.name(); 
        }
        
        public String getValue() {
        	return this.value; 
        }

		public void setValue(String value) {
			this.value = value;
		}
    }
	
}
