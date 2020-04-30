package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClespRisultatoNuovoOrdineDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@SerializedName("cod_pdc")
	public String  cod_pdc;
	@SerializedName("cod_consumatore")
	public Integer cod_consumatore;
	@SerializedName("id_ordine")
	public Integer id_ordine;
	@SerializedName("numero_copie")
	public Integer numero_copie;
	@SerializedName("numero_titoli")
	public Integer numero_titoli;
	
	public ClespDettaglioRisultatoLibroOrdineDto[] ordine;	
	
	
}
