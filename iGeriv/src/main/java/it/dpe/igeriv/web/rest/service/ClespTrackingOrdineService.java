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

import it.dpe.igeriv.web.rest.dto.ClespDettaglioTrakingOrdineDto;
import it.dpe.igeriv.web.rest.dto.ClespRicercaTrackingOrdineDto;
import it.dpe.igeriv.web.rest.dto.ClespRisultatoTrackingOrdineDto;

@Component("ClespTrackingOrdineService")
public class ClespTrackingOrdineService
		extends BaseRestService<ClespRisultatoTrackingOrdineDto, ClespRicercaTrackingOrdineDto>
		implements RestService<ClespRisultatoTrackingOrdineDto, ClespRicercaTrackingOrdineDto> {

	private static final String URL_TRACKING_ORDINE = "/api/tracking_ordine";
	
	private final String env; 
	private final String txtRestUrl;
	
	@Autowired
	ClespTrackingOrdineService(@Value("${igeriv.scolastica.clesp.txt.rest.url}") String txtRestUrl, @Value("${igeriv.scolastica.txt.env.deploy.name}") String env) {
		this.txtRestUrl = txtRestUrl;
		this.env = env;
	}
		
	@Override
	public ResponseEntity<ClespRisultatoTrackingOrdineDto> getEntity(ClespRicercaTrackingOrdineDto params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<ClespRisultatoTrackingOrdineDto> putEntity(ClespRicercaTrackingOrdineDto entity) {
		Client client = Client.create();
        WebResource webResource = client.resource(txtRestUrl+URL_TRACKING_ORDINE);

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
        
        ClespRisultatoTrackingOrdineDto res =gson.fromJson(output, ClespRisultatoTrackingOrdineDto.class);
        
//        ClespDettaglioTrakingOrdineDto[] res = gson.fromJson(output, ClespDettaglioTrakingOrdineDto[].class);
//        ClespRisultatoTrackingOrdineDto result = new ClespRisultatoTrackingOrdineDto();
//        result.setListDettaglioTrakingOrdine(res);
        
		ResponseEntity<ClespRisultatoTrackingOrdineDto> result_return = new ResponseEntity<ClespRisultatoTrackingOrdineDto>(res, HttpStatus.OK);
		return result_return;
	}

}
