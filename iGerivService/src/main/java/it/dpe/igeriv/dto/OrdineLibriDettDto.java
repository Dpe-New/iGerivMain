package it.dpe.igeriv.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdineLibriDettDto extends BaseDto {


	private static final long serialVersionUID = 1L;
	private Integer numeroOrdine;
	private Integer sku;
	private Integer barcode;
	private String titolo;
	private String autore;
	private String editore;
	private BigDecimal prezzoCopertina;
	private String urlImmagineCopertina;
	private Integer quantitaLibri;
	private String statoTracking;
}
