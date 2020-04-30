package it.dpe.igeriv.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class EstrattoContoDinamicoDto extends BaseDto {
	private static final long serialVersionUID = 9213472210194846706L;
	private Timestamp dataMovimento;
	private String descrizione;
	private BigDecimal importoDare;
	private BigDecimal importoAvere;
	private boolean isCalcRow;
	
	@Override
	public void accept(VisitorDto visitor) {
		visitor.visit(this);
	}
}
