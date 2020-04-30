package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClespNuovoOrdineDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public ClespDettaglioLibroOrdineDto[] listLibriOrdine;
	
	
	
}
