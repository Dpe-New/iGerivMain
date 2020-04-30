package it.dpe.igeriv.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class GiacenzaPneDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private String descrizione;
	private Float prezzoAcquisto;
	private Float prezzoVendita;
	private Integer giacenza;
	private Float prezzounitario;
	private Float prezzototale;
}
