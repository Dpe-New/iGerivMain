package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClespRisultatoTrackingOrdineDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public ClespDettaglioTrakingOrdineDto[] tracking;
}
