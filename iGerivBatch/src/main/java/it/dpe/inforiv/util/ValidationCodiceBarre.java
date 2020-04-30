package it.dpe.inforiv.util;

import it.dpe.igeriv.resources.IGerivMessageBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


@Component("ValidationCodiceBarre")
public class ValidationCodiceBarre {
	private final Logger log = Logger.getLogger(getClass());
	public static final int LENGHT_CODICE_BARRE = 13;
	public static final int LENGHT_ADDON = 5;
	
	public String validateAndInsertCodiceBarre(String codiceBarre, String numeroInforeteOAddon){
		
		String resultCodBar = null;
		long zero = 0;
		
		if(codiceBarre!=null && !codiceBarre.equals("") 
					&& numeroInforeteOAddon!=null && !numeroInforeteOAddon.equals("")){
			
			if(codiceBarre.length()==LENGHT_CODICE_BARRE){
				try {
					Long intgCodiceBarre = new Long(codiceBarre);
					if(intgCodiceBarre.longValue() > zero ){
						String ver = codiceBarre.substring(0,3);
						if(!ver.equals("000")){
							resultCodBar = codiceBarre+numeroInforeteOAddon;
						}else{
							log.error(" Errore - Codice a barre con i primi tre caratteri invalidi :  "+codiceBarre);
						}
					}else{
						log.error(" Errore - Il campo barcode risulta essere zero :  "+codiceBarre);
					}
				} catch (Exception e) {
					log.error(" Errore - Conversione del codice a barre seguente :  "+codiceBarre);
				}				
				
			}else{
				log.error(" Errore - Codice barre di lunghezza non valida :  "+codiceBarre);
			}
			
			if(numeroInforeteOAddon.length()==LENGHT_ADDON){
				//TODO
			}else{
				log.error(" Errore - Codice barre di lunghezza non valida :  "+numeroInforeteOAddon);
			}
		}else{
			//log.error(" Errore - Codice barre/Add-on NULL  ( CODICE BARRE :"+codiceBarre+" | ADD-ON: "+numeroInforeteOAddon+" )");
		}
			
		return resultCodBar;
	}
	
}
