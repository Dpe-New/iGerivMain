package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClespDettaglioTrakingOrdineDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@SerializedName("cod_Pdc")
	public Long   cod_Pdc;
	@SerializedName("cod_consumatore")
	public Long   cod_consumatore;
	@SerializedName("id_Ordine")
	public Long   id_Ordine;
	@SerializedName("id_riga_ordine")
	public Long   id_riga_ordine;
	@SerializedName("ean")
	public String ean;
	@SerializedName("copertina")
	public String copertina;
	@SerializedName("usato")
	public String usato;
	@SerializedName("stato")
	public String stato;
	@SerializedName("descrizione_stato")
	public String descrizione_stato;
}
