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

import it.dpe.igeriv.dto.RisultatoRicercaLibriDto;
import it.dpe.igeriv.web.rest.dto.RicercaLibriTestoDto;

@Component("LibriScolasticiDettaglioRestService")
public class LibriScolasticiDettaglioRestService extends BaseRestService<RisultatoRicercaLibriDto, RicercaLibriTestoDto> implements RestService<RisultatoRicercaLibriDto, RicercaLibriTestoDto> {

	private static final String VIEW_DETAIL_BOOK = "/api/item/%s/%s";
	
	private final String env; 
	private final String txtRestUrl;
	
	@Autowired
	LibriScolasticiDettaglioRestService(@Value("${igeriv.scolastica.txt.rest.url}") String txtRestUrl, @Value("${igeriv.scolastica.txt.env.deploy.name}") String env) {
		this.txtRestUrl = txtRestUrl;
		this.env = env;
	}
	
	
	@Override
	public ResponseEntity<RisultatoRicercaLibriDto> getEntity(RicercaLibriTestoDto params) {
		
		Client client = Client.create();
		WebResource webResource2 = client.resource(getUrl(params));
		ClientResponse response2 = webResource2.accept("application/json").get(ClientResponse.class);
		if (response2.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response2.getStatus());
		}
		String output2 = response2.getEntity(String.class);	
		Gson gson = new Gson(); 
		RisultatoRicercaLibriDto result = gson.fromJson(output2, RisultatoRicercaLibriDto.class);
		
		ResponseEntity<RisultatoRicercaLibriDto> rr = new ResponseEntity<RisultatoRicercaLibriDto>(result, HttpStatus.OK);
		
		return rr;
	}

	
	private String getUrl(RicercaLibriTestoDto params) {
		String op = null;
		op = String.format(VIEW_DETAIL_BOOK, params.getSku(), params.getGuid());
		String url = txtRestUrl  + op;
		return url;
	}
	
	@Override
	public ResponseEntity<RisultatoRicercaLibriDto> putEntity(RicercaLibriTestoDto entity) {
		return null;
		
	}

}
