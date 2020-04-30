package it.dpe.igeriv.dto;

import it.dpe.igeriv.util.DateUtilities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper=false)
public class InventarioDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Long idInventario;
	@Getter(AccessLevel.NONE)
	private Date dataInventario;
	private Integer quantita;
	private BigDecimal importoTotale;
	private Integer idtn;
	private String titolo;
	private String sottoTitolo;
	private String numeroCopertina;
	private Timestamp dataUscita;
	private String dataUscitaFormat;
	private BigDecimal prezzoCopertina;
	private BigDecimal prezzoEdicola;
	@Getter(AccessLevel.NONE)
	private Integer quantitaCopieContoDeposito;
	private Integer modalita;
	@Getter(AccessLevel.NONE)
	private Long giacenzaSP;
	@Getter(AccessLevel.NONE)
	private Boolean isScaduta;
	
	public Date getDataInventario() {
		return new Date(dataInventario.getTime());
	}

	public Integer getQuantitaCopieContoDeposito() {
		return quantitaCopieContoDeposito == null ? 0 : quantitaCopieContoDeposito;
	}

	public String getKeyStringForSelectBox() {
		return DateUtilities.getTimestampAsString(getDataInventario(), DateUtilities.FORMATO_DATA_SLASH) + "|" + getIdInventario();
	}
	
	public String getValueStringForSelectBox() {
		return DateUtilities.getTimestampAsString(getDataInventario(), DateUtilities.FORMATO_DATA_SLASH);
	}
	
	public Long getGiacenzaSP() {
		return giacenzaSP == null ? new Long(0l) : giacenzaSP;
	}

	public Boolean getIsScaduta() {
		return isScaduta == null ? false : isScaduta;
	}

	public BigDecimal getImporto() {
		if (getQuantita() != null && getPrezzoEdicola() != null) {
			return new BigDecimal(getQuantita()).multiply(getPrezzoEdicola());
		}
		return new BigDecimal(0);
	}
}
