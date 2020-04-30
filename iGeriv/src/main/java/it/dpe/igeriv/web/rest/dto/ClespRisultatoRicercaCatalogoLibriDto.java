package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClespRisultatoRicercaCatalogoLibriDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public ClespDettaglioLibroDto[] articoli;
	
}

	