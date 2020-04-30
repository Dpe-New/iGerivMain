package it.dpe.igeriv.dto;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class RisultatoRicercaLibriDto extends BaseDto {
	
	private static final long serialVersionUID = 1L;

	@SerializedName("Parametri") 
	private ParametriRicercaLibriDto[] Parametri;
	
	@SerializedName("Ricerca") 
	private RisultatiRicercaLibriDto[] Ricerca;
	
	@SerializedName("Item") 
	private DettaglioRicercaLibriDto[] Item;
	
	@SerializedName("OrdineTXT") 
	private String OrdineTXT;
	
	
	
}
