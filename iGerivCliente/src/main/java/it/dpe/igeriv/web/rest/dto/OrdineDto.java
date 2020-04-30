package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class OrdineDto implements Serializable{

	private String ordineExt;
	private String pdvDestinazione;
	private List<InformazioniDto> informazioni;
	
	
}
