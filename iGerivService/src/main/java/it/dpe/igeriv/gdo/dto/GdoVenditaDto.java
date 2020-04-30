package it.dpe.igeriv.gdo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

/**
 * @author romanom
 * 
 */
@Data
public class GdoVenditaDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String barcode;
	private Integer idtn;
	private Date dataOraVendita;
	@Getter(AccessLevel.NONE)
	private Integer copie;
	private String idScontrino;
	@Getter(AccessLevel.NONE)
	private BigDecimal prezzoLordo;
	
	public Integer getCopie() {
		return copie == null ? 0 : copie;
	}

	public BigDecimal getPrezzoLordo() {
		return prezzoLordo == null ? new BigDecimal(0) : prezzoLordo;
	}

	public BigDecimal getImporto() {
		return getPrezzoLordo().multiply(new BigDecimal(getCopie()));
	}
	
}
