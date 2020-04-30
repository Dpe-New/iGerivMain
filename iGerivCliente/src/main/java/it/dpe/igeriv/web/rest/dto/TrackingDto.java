package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class TrackingDto implements Serializable {

	private String document;
	private String versionJson;
	private Long codiceCliente;
	private String ordineExt;
	private String ordineTXT;
	private String pdvDestinazione;
	private String pdvDestinazioneExt;
	private Date dataConsegna;
	
	private TrackingElencoLibriDto[] elencolibri;
	
}
