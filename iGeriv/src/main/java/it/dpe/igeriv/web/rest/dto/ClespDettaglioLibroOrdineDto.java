package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClespDettaglioLibroOrdineDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@SerializedName("ean")
	public String ean;
	@SerializedName("codice_pdc")
	public Long codice_pdc;
	@SerializedName("codice_consumatore")
	public Long codice_consumatore;
	@SerializedName("copertinato")
	public String copertinato;
	@SerializedName("usato")
	public String usato;
	@SerializedName("riga1")
	public String riga1;
	@SerializedName("riga2")
	public String riga2;
	@SerializedName("riga3")
	public String riga3;
	
	
	
}
