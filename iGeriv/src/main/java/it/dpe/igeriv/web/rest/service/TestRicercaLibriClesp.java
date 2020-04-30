package it.dpe.igeriv.web.rest.service;

import java.util.Date;

import org.springframework.http.ResponseEntity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.dpe.igeriv.web.rest.dto.ClespRicercaCatalogoLibriDto;
import it.dpe.igeriv.web.rest.dto.ClespRisultatoRicercaCatalogoLibriDto;

public class TestRicercaLibriClesp {

	public static void main(String[] args) {
		
		ClespRicercaCatalogoLibriService serv = new ClespRicercaCatalogoLibriService("http://testmentaws.clesp.it:7875", "dpe");
		
		GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String jsonEanRicercaTesto = gson.toJson("Matematica");
        ClespRicercaCatalogoLibriDto ric = new ClespRicercaCatalogoLibriDto();
        ric.setEan_titolo(jsonEanRicercaTesto);
		
   	    ResponseEntity<ClespRisultatoRicercaCatalogoLibriDto> response = serv.putEntity(ric);
   	    if(response!=null){
			System.out.println(" RESPONSE LENGTH: "+response.getBody().getArticoli().length);
   	    	
		}	
      
	
	}

}
