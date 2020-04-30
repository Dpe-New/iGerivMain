package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestoScolasticoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer sku;
	private Integer barcode;
	private String titolo;
	private String autore;
	private String editore;
	private BigDecimal prezzoCopertina;
	private Integer disponibile;
	private String urlImmagineCopertina;

}
