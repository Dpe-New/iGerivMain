package it.dpe.igeriv.dto;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class DettaglioRicercaLibriDto extends BaseDto {
	
	private static final long serialVersionUID = 1L;

	@SerializedName("SKU") 
	private String SKU;
	@SerializedName("BARCODE") 
	private String BARCODE;
	@SerializedName("Titolo") 
	private String TITOLO;
	@SerializedName("Autori") 
	private String AUTORI;
	@SerializedName("Editore") 
	private String EDITORE;
	@SerializedName("PREZZO") 
	private String PREZZO;
	@SerializedName("ALIQUOTA") 
	private String ALIQUOTA;
	@SerializedName("Disponibile") 
	private String DISPONIBILE;
	@SerializedName("MENU1") 
	private String MENU1;
	@SerializedName("MENU2") 
	private String MENU2;
	@SerializedName("Abstract") 
	private String ABSTRACT;
	@SerializedName("Tomi") 
	private String TOMI;
	@SerializedName("AnnoPubbl") 
	private String ANNAPUBBL;
	@SerializedName("URL") 
	private String URL;
	@SerializedName("classe_sconto") 
	private Integer classe_sconto;
	@SerializedName("VOLUME") 
	private String VOLUME;
	
	
	
	
	
	
}
