package it.dpe.igeriv.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class VenditaRiepilogoDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private String raggruppamento; 
	private Integer copie;
	private BigDecimal importo;
	private BigDecimal importoNetto;
	private BigDecimal aggio;
}
