package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClespRisultatoNuovoConsumatoreDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@SerializedName("codice_pdc_menta")
	public String codice_pdc_menta;
	@SerializedName("codice_consumatore_menta")
	public Integer codice_consumatore_menta;
	

	
}
