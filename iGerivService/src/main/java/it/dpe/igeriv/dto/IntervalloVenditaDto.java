package it.dpe.igeriv.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class IntervalloVenditaDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Date data;
	private BigDecimal importo;
}
