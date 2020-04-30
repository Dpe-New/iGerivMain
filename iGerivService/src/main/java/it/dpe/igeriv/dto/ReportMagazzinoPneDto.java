package it.dpe.igeriv.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ReportMagazzinoPneDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Timestamp dataRegistrazione;
	private String descrizioneProdotto;
	private Integer quantitaProdotto;
	private Float prezzoProdotto;
	private Float importo;
	private Integer conto;
	private String fornitore;
	private String causale;
	private Integer giacenzaProdotto;
	private Long codProdotto;
	private Long codCategoria;
	private Long codSottocategoria;
}
