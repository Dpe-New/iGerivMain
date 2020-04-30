package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class ConfermaOrdine implements Serializable{

	private String document;
	private Long codiceCliente;
	private String versionJson;
	private AnagraficaDto anagrafica;
	private OrdineDto ordine;
	private List<ElencoLibriDto> elencoLibri;
	
	
	
}
