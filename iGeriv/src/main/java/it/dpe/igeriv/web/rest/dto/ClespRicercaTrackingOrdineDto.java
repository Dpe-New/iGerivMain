package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClespRicercaTrackingOrdineDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public Long numero_ordine;
	
}
