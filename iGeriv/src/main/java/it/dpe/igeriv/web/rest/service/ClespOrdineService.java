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

import it.dpe.igeriv.web.rest.dto.ClespNuovoOrdineDto;
import it.dpe.igeriv.web.rest.dto.ClespRisultatoNuovoOrdineDto;

@Component("ClespOrdineService")
public class ClespOrdineService extends BaseRestService<ClespRisultatoNuovoOrdineDto, ClespNuovoOrdineDto>
		implements RestService<ClespRisultatoNuovoOrdineDto, ClespNuovoOrdineDto> {

	private static final String URL_NUOVO_ORDINE = "/api/nuovo_ordine";

	private final String env;
	private final String txtRestUrl;

	@Autowired
	ClespOrdineService(@Value("${igeriv.scolastica.clesp.txt.rest.url}") String txtRestUrl,
			@Value("${igeriv.scolastica.txt.env.deploy.name}") String env) {
		this.txtRestUrl = txtRestUrl;
		this.env = env;
	}

	@Override
	public ResponseEntity<ClespRisultatoNuovoOrdineDto> getEntity(ClespNuovoOrdineDto params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<ClespRisultatoNuovoOrdineDto> putEntity(ClespNuovoOrdineDto entity) {
		Client client = Client.create();
		WebResource webResource = client.resource(txtRestUrl + URL_NUOVO_ORDINE);

		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String input = gson.toJson(entity.getListLibriOrdine());

		// POST method
		ClientResponse response = webResource.accept("application/json").type("application/json")
				.post(ClientResponse.class, input);

		// Check response status code
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		// Display response
		String output = response.getEntity(String.class);

//		String outputW = 
//				"{\"cod_pdc\":\"020101\",\"cod_consumatore\":100,\"id_ordine\":6789292,\"numero_Copie\":2,\"numero_titoli\":1,"
//				+ "[{\"id_riga_ordine\": 670892920,\"ean\": \"9788808223289\",\"copertina\": \"Si\",\"usato\": \"No\", \"stato\":\"P\"},"
//				+ "{\"id_riga_ordine\": 670892920,\"ean\": \"9788808223289\",\"copertina\": \"Si\",\"usato\": \"No\", \"stato\":\"P\"}]"
//				+ "}";
		
		
		ClespRisultatoNuovoOrdineDto res = gson.fromJson(output, ClespRisultatoNuovoOrdineDto.class);
		ResponseEntity<ClespRisultatoNuovoOrdineDto> result_return = new ResponseEntity<ClespRisultatoNuovoOrdineDto>(
				res, HttpStatus.OK);
		return result_return;
	}

	// NUOVO ORDINE

}
