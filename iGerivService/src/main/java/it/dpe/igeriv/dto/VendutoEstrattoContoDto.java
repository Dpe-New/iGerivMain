package it.dpe.igeriv.dto;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper=false)
public class VendutoEstrattoContoDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Date dataEstrattoConto;
	@Getter(AccessLevel.NONE)
	private BigDecimal fornitoQuotidiani;
	@Getter(AccessLevel.NONE)
	private BigDecimal resoQuotidiani;
	@Getter(AccessLevel.NONE)
	private BigDecimal fornitoPeriodici;
	@Getter(AccessLevel.NONE)
	private BigDecimal resoPeriodici;

	public BigDecimal getFornitoQuotidiani() {
		return (fornitoQuotidiani == null) ? new BigDecimal(0) : fornitoQuotidiani;
	}

	public BigDecimal getResoQuotidiani() {
		return (resoQuotidiani == null) ? BigDecimal.ZERO : resoQuotidiani;
	}

	public BigDecimal getFornitoPeriodici() {
		return (fornitoPeriodici == null) ? BigDecimal.ZERO : fornitoPeriodici;
	}

	public BigDecimal getResoPeriodici() {
		return (resoPeriodici == null) ? BigDecimal.ZERO : resoPeriodici;
	}

	public BigDecimal getVendutoQuotidiani() {
		return getFornitoQuotidiani().subtract(getResoQuotidiani());
	}
	
	public BigDecimal getVendutoPeriodici() {
		return getFornitoPeriodici().subtract(getResoPeriodici());
	}

	public BigDecimal getVenduto() {
		return getVendutoQuotidiani().add(getVendutoPeriodici());
	}

}