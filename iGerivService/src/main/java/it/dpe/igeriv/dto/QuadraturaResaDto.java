package it.dpe.igeriv.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class QuadraturaResaDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer cpu;
	private String titolo;
	private String numero;
	private Integer copieDichiarate;
	private Integer copieRiscontrate;
	private Integer copieRespinte;
	private String motivo;
	private BigDecimal prezzoDichiarato;
	private BigDecimal prezzoRiscontrato;
	
	public Integer getDifferenza() {
		return (getCopieDichiarate() != null ? getCopieDichiarate() : 0) - (getCopieRiscontrate() != null ? getCopieRiscontrate() : 0);
	}
	
	public BigDecimal getValoreDichiarato() {
		return (getCopieDichiarate() != null && getPrezzoDichiarato() != null) ? getPrezzoDichiarato().multiply(new BigDecimal(getCopieDichiarate())) : BigDecimal.ZERO;
	}
	
	public BigDecimal getValoreRiscontrato() {
		return (getCopieRiscontrate() != null && getPrezzoRiscontrato() != null) ? getPrezzoRiscontrato().multiply(new BigDecimal(getCopieRiscontrate())) : BigDecimal.ZERO;
	}
	
	public BigDecimal getDifferenzaValore() {
		return getValoreDichiarato().subtract(getValoreRiscontrato());
	}

}
