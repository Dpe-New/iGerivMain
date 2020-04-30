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
import it.dpe.igeriv.web.rest.dto.ClespNuovoConsumatoreDto;
import it.dpe.igeriv.web.rest.dto.ClespRisultatoNuovoConsumatoreDto;
import it.dpe.igeriv.web.rest.dto.ClespRisultatoRicercaCatalogoLibriDto;

@Component("ClespConsumatoreService")
public class ClespConsumatoreService
		extends BaseRestService<ClespRisultatoNuovoConsumatoreDto, ClespNuovoConsumatoreDto>
		implements RestService<ClespRisultatoNuovoConsumatoreDto, ClespNuovoConsumatoreDto> {
	
	
	// NUOVO CONSUMATORE
	private static final String URL_NUOVO_CONSUMATORE = "/api/nuovo_consumatore";
	
	private final String env; 
	private final String txtRestUrl;
	
	@Autowired
	ClespConsumatoreService(@Value("${igeriv.scolastica.clesp.txt.rest.url}") String txtRestUrl, @Value("${igeriv.scolastica.txt.env.deploy.name}") String env) {
		this.txtRestUrl = txtRestUrl;
		this.env = env;
	}
	
	@Override
	public ResponseEntity<ClespRisultatoNuovoConsumatoreDto> getEntity(ClespNuovoConsumatoreDto params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<ClespRisultatoNuovoConsumatoreDto> putEntity(ClespNuovoConsumatoreDto entity) {
		Client client = Client.create();
        WebResource webResource = client.resource(txtRestUrl+URL_NUOVO_CONSUMATORE);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String input = gson.toJson(entity);
        
        
        // POST method
        ClientResponse response = webResource.accept("application/json")
                .type("application/json").post(ClientResponse.class, input);

        // Check response status code
        if (response.getStatus() != 200 ) {
            if(response.getStatus() == 400){
            	// Display response
                String output = response.getEntity(String.class);
                ClespRisultatoNuovoConsumatoreDto res = gson.fromJson(output, ClespRisultatoNuovoConsumatoreDto.class);
                ResponseEntity<ClespRisultatoNuovoConsumatoreDto> rr = new ResponseEntity<ClespRisultatoNuovoConsumatoreDto>(res, HttpStatus.OK);
        		return rr;
            	
            }else{
            	throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
            }
            
        }

        // Display response
        String output = response.getEntity(String.class);
        
       
        ClespRisultatoNuovoConsumatoreDto res = gson.fromJson(output, ClespRisultatoNuovoConsumatoreDto.class);
        
		ResponseEntity<ClespRisultatoNuovoConsumatoreDto> rr = new ResponseEntity<ClespRisultatoNuovoConsumatoreDto>(res, HttpStatus.OK);
		return rr;
	}

	

}
