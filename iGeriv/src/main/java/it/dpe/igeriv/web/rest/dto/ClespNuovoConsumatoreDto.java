package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClespNuovoConsumatoreDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String nome;
	public String cognome;
	public String classe;
	public String email;
	public String cellulare;
	public String codice_pdc_menta;
	public Integer codice_consumatore_menta;
	public String cod_fiscale;
	public String socio;
	
}
