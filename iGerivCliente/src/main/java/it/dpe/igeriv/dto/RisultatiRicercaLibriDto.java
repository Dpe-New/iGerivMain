package it.dpe.igeriv.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.google.gson.annotations.SerializedName;


@Data
@EqualsAndHashCode(callSuper = false)
public class RisultatiRicercaLibriDto extends BaseDto {
	
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
	@SerializedName("URL") 
	private String URL;
	@SerializedName("Disponibile") 
	private String disponibile;
	public boolean isDisponibile() {
		try {
			if (disponibile == null) {
				return true;
			} else {
				return Integer.parseInt(disponibile)>0;
			}
		}
		catch (NumberFormatException ex) {return true;}
		
	}
}
