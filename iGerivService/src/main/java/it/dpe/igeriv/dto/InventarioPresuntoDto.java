package it.dpe.igeriv.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper=false)
public class InventarioPresuntoDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer cpu;
	private String titolo;
	private String sottoTitolo;
	private String numeroCopertina;
	private Timestamp dataUscita;
	private BigDecimal prezzoCopertina;
	private BigDecimal prezzoEdicola;
	@Getter(AccessLevel.NONE)
	private Integer quantitaCopieContoDeposito;
	@Getter(AccessLevel.NONE)
	private Long giacenzaSP;
	private String nomeImmagine;
	
	public Integer getQuantitaCopieContoDeposito() {
		return quantitaCopieContoDeposito == null ? 0 : quantitaCopieContoDeposito;
	}

	public Long getGiacenzaSP() {
		return giacenzaSP == null ? new Long(0l) : giacenzaSP;
	}

	public BigDecimal getImporto() {
		if (getGiacenzaSP() != null && getPrezzoEdicola() != null) {
			return new BigDecimal(getGiacenzaSP()).multiply(getPrezzoEdicola());
		}
		return new BigDecimal(0);
	}
}
