package it.dpe.igeriv.web.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import it.dpe.igeriv.web.rest.dto.RicercaLibriTestoDto;
import it.dpe.igeriv.web.rest.dto.TrackingDto;


@Component("LibriScolasticiTrackingOrdiniClientRestService")
public class LibriScolasticiTrackingOrdiniClientRestService extends BaseRestService<TrackingDto, RicercaLibriTestoDto> implements RestService<TrackingDto, RicercaLibriTestoDto> {

	private static final String VIEW_TRACKING_ORDINE = "/api/OrdineMepe/%s/%s";
	private final String env; 
	private final String txtRestUrl;
	
	@Autowired
	LibriScolasticiTrackingOrdiniClientRestService(@Value("${igeriv.scolastica.txt.rest.url}") String txtRestUrl, @Value("${igeriv.scolastica.txt.env.deploy.name}") String env) {
		this.txtRestUrl = txtRestUrl;
		this.env = env;
	}
	
	
	@Override
	public ResponseEntity<TrackingDto> getEntity(RicercaLibriTestoDto params) {
		
		Client client = Client.create();
		WebResource webResource2 = client.resource(getUrl(params));
		ClientResponse response2 = webResource2.accept("application/json").get(ClientResponse.class);
		if (response2.getStatus() != 200) {
			TrackingDto result =null;
			return new ResponseEntity<TrackingDto>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			//throw new RuntimeException("Failed : HTTP error code : " + response2.getStatus());
		}
		String output2 = response2.getEntity(String.class);	
		Gson gson = new Gson(); 
		TrackingDto result = gson.fromJson(output2, TrackingDto.class);
		
		ResponseEntity<TrackingDto> rr = new ResponseEntity<TrackingDto>(result, HttpStatus.OK);
		
		return rr;
	}

	
	private String getUrl(RicercaLibriTestoDto params) {
		String op = null;
		op = String.format(VIEW_TRACKING_ORDINE, params.getNumOrdine(), params.getNumOrdineTxt());
		String url = txtRestUrl  + op;
		return url;
	}
	
	@Override
	public ResponseEntity<TrackingDto> putEntity(RicercaLibriTestoDto entity) {
		return null;
		
	}

}
