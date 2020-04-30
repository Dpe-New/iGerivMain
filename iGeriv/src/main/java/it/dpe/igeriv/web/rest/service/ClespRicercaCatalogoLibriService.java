package it.dpe.igeriv.web.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import it.dpe.igeriv.web.rest.dto.ClespDettaglioLibroDto;
import it.dpe.igeriv.web.rest.dto.ClespRicercaCatalogoLibriDto;
import it.dpe.igeriv.web.rest.dto.ClespRisultatoRicercaCatalogoLibriDto;

@Component("ClespRicercaCatalogoLibriService")
public class ClespRicercaCatalogoLibriService extends BaseRestService<ClespRisultatoRicercaCatalogoLibriDto, ClespRicercaCatalogoLibriDto> 
									implements RestService<ClespRisultatoRicercaCatalogoLibriDto, ClespRicercaCatalogoLibriDto> {

	//RICERCA ARTICOLI
	/*
	Request: /api/ricerca_articoli
	• Request Method: POST
	• Content Type: application/json
	• Request Body (esempio) [ { "ean_titolo":"matematica blu" } ]
	*/
	private static final String URL_RICERCA_ARTICOLI = "/api/ricerca_articoli";
	
	private final String env; 
	private final String txtRestUrl;
	
	@Autowired
	ClespRicercaCatalogoLibriService(@Value("${igeriv.scolastica.clesp.txt.rest.url}") String txtRestUrl, @Value("${igeriv.scolastica.txt.env.deploy.name}") String env) {
		this.txtRestUrl = txtRestUrl;
		this.env = env;
	}

	@Override
	public ResponseEntity<ClespRisultatoRicercaCatalogoLibriDto> getEntity(ClespRicercaCatalogoLibriDto params) {
		return null;
	}

	
	
	@Override
	public ResponseEntity<ClespRisultatoRicercaCatalogoLibriDto> putEntity(ClespRicercaCatalogoLibriDto entity) {
		Client client = Client.create();
        WebResource webResource = client.resource(txtRestUrl+URL_RICERCA_ARTICOLI);

        //String input = "{\"ean_titolo\":\"matem%\"}";

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String input = gson.toJson(entity);
        
        
        // POST method
        ClientResponse response = webResource.accept("application/json")
                .type("application/json").post(ClientResponse.class, input);

        // Check response status code
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
        }

        // Display response
        String output = response.getEntity(String.class);
        
//        String xxx = "["
//	        		+ "{\"ean\": \"\",\"titolo\": \"\",\"autore\": \"\",\"editore\": \"\",\"volume\": \"\",\"prezzo\": 0,\"copertinabile\": \"No\",\"numero_tomi\": 0,\"disponibilita\": \"\",\"prenotabile\": \"\",\"url_immagine\":\"\"},"
//	        		+ "{\"ean\": \"\",\"titolo\": \"\",\"autore\": \"\",\"editore\": \"\",\"volume\": \"\",\"prezzo\": 0,\"copertinabile\": \"No\",\"numero_tomi\": 0,\"disponibilita\": \"\",\"prenotabile\": \"\",\"url_immagine\":\"\"}"
//	        		+ "]";
        
        
        //Gson gson = new Gson(); 
//        ClespDettaglioLibroDto res = gson.fromJson(output, ClespDettaglioLibroDto.class);
//        ClespDettaglioLibroDto[] listDettaglioLibro = new ClespDettaglioLibroDto[1];  
//        listDettaglioLibro[0] = res;
        
        ClespRisultatoRicercaCatalogoLibriDto result = gson.fromJson(output, ClespRisultatoRicercaCatalogoLibriDto.class);
        
        
//        ClespRisultatoRicercaCatalogoLibriDto result = new ClespRisultatoRicercaCatalogoLibriDto();
//        result.setListDettaglioLibro(res);
        
		ResponseEntity<ClespRisultatoRicercaCatalogoLibriDto> rr = new ResponseEntity<ClespRisultatoRicercaCatalogoLibriDto>(result, HttpStatus.OK);
		return rr;
	}
	
}
