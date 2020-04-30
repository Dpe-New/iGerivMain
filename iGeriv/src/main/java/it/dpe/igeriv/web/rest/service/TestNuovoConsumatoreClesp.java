package it.dpe.igeriv.web.rest.service;

import org.springframework.http.ResponseEntity;

import it.dpe.igeriv.web.rest.dto.ClespNuovoConsumatoreDto;
import it.dpe.igeriv.web.rest.dto.ClespRisultatoNuovoConsumatoreDto;
import it.dpe.igeriv.web.rest.dto.ClespRisultatoRicercaCatalogoLibriDto;

public class TestNuovoConsumatoreClesp {

	public static void main(String[] args) {
		
		ClespConsumatoreService service = new ClespConsumatoreService("http://testmentaws.clesp.it:7875", "dpe");
		
		ClespNuovoConsumatoreDto dto = new ClespNuovoConsumatoreDto();
		dto.nome="Benedetto";
		dto.cognome= "Bifulco";
		dto.classe ="3A";
		dto.email="benedetto.bifulco@dpe.it";
		dto.cellulare ="";
		dto.codice_pdc_menta="020101";
		dto.codice_consumatore_menta=102;
		dto.cod_fiscale="CMPMR**********";
		dto.socio="SI";
		
		ResponseEntity<ClespRisultatoNuovoConsumatoreDto> response = service.putEntity(dto);
   	    if(response!=null){
			System.out.println(" RESPONSE LENGTH: "+response.getBody().codice_pdc_menta);
   	    	
		}
		
	}
	
}
