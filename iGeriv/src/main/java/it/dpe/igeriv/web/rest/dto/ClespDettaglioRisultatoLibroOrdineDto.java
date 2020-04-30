package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClespDettaglioRisultatoLibroOrdineDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@SerializedName("id_riga_ordine")
	public Long id_riga_ordine;
	@SerializedName("ean")
	public String ean;
	@SerializedName("copertina")
	public String copertina;
	@SerializedName("usato")
	public String usato;
	@SerializedName("stato")
	public String stato;
	
	
}
