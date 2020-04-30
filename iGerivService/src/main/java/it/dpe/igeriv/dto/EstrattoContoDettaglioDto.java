package it.dpe.igeriv.dto;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper=false)
public class EstrattoContoDettaglioDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Date dataEstrattoConto;
	private Integer tipoMovimento;
	@Getter(AccessLevel.NONE)
	private BigDecimal importoDare;
	@Getter(AccessLevel.NONE)
	private BigDecimal importoAvere;

	public BigDecimal getImportoDare() {
		return (importoDare == null) ? BigDecimal.ZERO : importoDare;
	}

	public BigDecimal getImportoAvere() {
		return (importoAvere == null) ? BigDecimal.ZERO : importoAvere;
	}

}
