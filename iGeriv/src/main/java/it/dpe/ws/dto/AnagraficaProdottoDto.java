package it.dpe.ws.dto;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnagraficaProdottoDto implements Serializable {
	private static final long serialVersionUID = 837408301518237912L;
	private Integer idProdotto;
	private Integer idEditore;
	private String titoloPrimaParte;
	private String titoloSecondaParte;
	private String periodicita;
	@Getter(AccessLevel.NONE)
	private Boolean disponibilePerRicaricaEdicola;
	
	public Boolean getDisponibilePerRicaricaEdicola() {
		return disponibilePerRicaricaEdicola == null ? false : disponibilePerRicaricaEdicola;
	}
	
	
}
