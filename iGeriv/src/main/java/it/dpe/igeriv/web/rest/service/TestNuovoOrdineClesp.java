package it.dpe.igeriv.web.rest.service;

import org.springframework.http.ResponseEntity;

import it.dpe.igeriv.web.rest.dto.ClespDettaglioLibroOrdineDto;
import it.dpe.igeriv.web.rest.dto.ClespNuovoOrdineDto;
import it.dpe.igeriv.web.rest.dto.ClespRisultatoNuovoOrdineDto;


public class TestNuovoOrdineClesp {

	public static void main(String[] args) {

		ClespOrdineService service = new ClespOrdineService("http://testmentaws.clesp.it:7875", "dpe");

		ClespDettaglioLibroOrdineDto dto = new ClespDettaglioLibroOrdineDto();
		dto.ean = "9788808223289";
		dto.codice_pdc = new Long("123123");
		dto.codice_consumatore = new Long("1234");
		dto.copertinato = "SI";
		dto.usato = "NO";
		dto.riga1 = "Marco";
		dto.riga2 = "Rossi";
		dto.riga3 = "3A";

		ClespDettaglioLibroOrdineDto dto2 = new ClespDettaglioLibroOrdineDto();
		dto2.ean = "9788808223289";
		dto2.codice_pdc = new Long("123123");
		dto2.codice_consumatore = new Long("1234");
		dto2.copertinato = "SI";
		dto2.usato = "NO";
		dto2.riga1 = "Marco";
		dto2.riga2 = "Rossi";
		dto2.riga3 = "3A";

		ClespNuovoOrdineDto dtoOrdine = new ClespNuovoOrdineDto();
		ClespDettaglioLibroOrdineDto[] listDettaglioOrdine = new ClespDettaglioLibroOrdineDto[2];
		listDettaglioOrdine[0] = dto;
		listDettaglioOrdine[1] = dto2;

		dtoOrdine.setListLibriOrdine(listDettaglioOrdine);

		ResponseEntity<ClespRisultatoNuovoOrdineDto> response = service.putEntity(dtoOrdine);
		if (response != null) {
			System.out.println(" RESPONSE LENGTH: " + response.getBody().getOrdine().length);

		}

	}

}
