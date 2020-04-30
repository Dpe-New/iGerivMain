package it.dpe.igeriv.web.rest.service;

import org.springframework.http.ResponseEntity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.dpe.igeriv.web.rest.dto.ClespRicercaCatalogoLibriDto;
import it.dpe.igeriv.web.rest.dto.ClespRicercaTrackingOrdineDto;
import it.dpe.igeriv.web.rest.dto.ClespRisultatoRicercaCatalogoLibriDto;
import it.dpe.igeriv.web.rest.dto.ClespRisultatoTrackingOrdineDto;

public class TestTrackingClesp {

	public static void main(String[] args) {
		ClespTrackingOrdineService serv = new ClespTrackingOrdineService("http://mentaws.clesp.it:7875", "dpe");
		
		ClespRicercaTrackingOrdineDto dto = new ClespRicercaTrackingOrdineDto();
		dto.setNumero_ordine(123L);
		
		
   	    ResponseEntity<ClespRisultatoTrackingOrdineDto> response = serv.putEntity(dto);
   	    if(response!=null){
			System.out.println(" RESPONSE LENGTH: "+response.getBody().tracking.length);
   	    	
		}	

	}

}
