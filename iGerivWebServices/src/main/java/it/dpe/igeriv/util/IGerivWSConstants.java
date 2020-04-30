package it.dpe.igeriv.util;

import it.dpe.igeriv.resources.IGerivMessageBundle;

public class IGerivWSConstants {
	
	public static enum Exception {
		RIVENDITA_NON_ESISTE(100, IGerivMessageBundle.get("msg.rivendita.non.esiste.1")),
        CREDENZIALI_INVALIDE(200, IGerivMessageBundle.get("gp.login.failed")),
		VERSIONE_CLIENT_VENDITE_NON_VALIDA(300, IGerivMessageBundle.get("gp.invalid.client.vendite.version"));
        
		private final int code;
		private final String message;
		
		Exception(int code, String message) {
			this.code = code;
			this.message = message;
		}
		
		@Override
        public String toString() {     
        	return this.code + "|" + this.message; 
        } 
    }
	
	public static final String IGERIV_CLIENT_VENDITE = "IGERIV_CLIENT_VENDITE";
	public static final String IGERIV_GDO_TOKEN = "1cffd5fa93c49d77dca812a310e449683281b9e5";
	public static final String QUOTIDIANO = "Q";
	public static final String PERIODICO = "P";
	public static final String DATA_RESPONSE_WS_FORMAT = "ddMMyyyy";
	public static final String DATA_ORA_RESPONSE_WS_FORMAT = "ddMMyyyyHHmmss";
	public static final String FILE_INFORIV_INPUT_PATTERN = ".+(\\.zip|\\.ZIP)$"; //"([\\d]{4})_([\\d]{3})_([\\d]{6})_([\\d]{6})_([\\d]{8}).zip$";
}
