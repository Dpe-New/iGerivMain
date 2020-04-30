package it.dpe.igeriv.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class VendutoGiornalieroDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private BigDecimal totalePubblicazioni;
	private BigDecimal totaleProdotti;

	public VendutoGiornalieroDto(BigDecimal totalePubblicazioni, BigDecimal totaleProdotti) {
		super();
		this.totalePubblicazioni = totalePubblicazioni;
		this.totaleProdotti = totaleProdotti;
	}
	
	public BigDecimal getTotaleGenerale() {
		return totalePubblicazioni.add(totaleProdotti);
	}

}
