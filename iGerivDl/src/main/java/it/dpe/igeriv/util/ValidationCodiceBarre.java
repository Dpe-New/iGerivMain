package it.dpe.igeriv.util;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


@Component("ValidationCodiceBarre")
public class ValidationCodiceBarre {
	private final Logger log = Logger.getLogger(getClass());
	public static final int LENGHT_CODICE_BARRE = 13;
	public static final int LENGHT_ADDON = 5;
	
	public String validateCodiceBarre(String codiceBarre, String addon){
		
		String resultMsgErrorCodBar = null;
		long zero = 0;
		
		if(codiceBarre!=null && !codiceBarre.equals("") 
					&& addon!=null && !addon.equals("")){
			
			if(codiceBarre.length()==LENGHT_CODICE_BARRE){
				try {
					Long intgCodiceBarre = new Long(codiceBarre);
					if(intgCodiceBarre.longValue() > zero ){
						String ver = codiceBarre.substring(0,3);
						if(!ver.equals("000")){
							//resultCodBar = codiceBarre+addon;
						}else{
							resultMsgErrorCodBar = "Errore - Codice a barre con i primi tre caratteri invalidi :  "+codiceBarre;
						}
					}else{
						resultMsgErrorCodBar = "Errore - Il campo barcode risulta essere zero :  "+codiceBarre;
					}
				} catch (Exception e) {
					resultMsgErrorCodBar = " Errore - Conversione del codice a barre seguente :  "+codiceBarre;
				}				
				
			}else{
				resultMsgErrorCodBar = " Errore - Codice barre di lunghezza non valida :  "+codiceBarre;
			}
			
			if(addon.length()==LENGHT_ADDON){
				//TODO
			}else{
				resultMsgErrorCodBar = "  Errore - Codice barre di lunghezza non valida :  "+addon;
			}
		}else{
			resultMsgErrorCodBar = " Errore - Codice barre/Add-on NULL  ( CODICE BARRE :"+codiceBarre+" | ADD-ON: "+addon+" )";
		}
			
		return resultMsgErrorCodBar;
	}
	
}
