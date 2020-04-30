package it.dpe.igeriv.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.NumberUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author mromano
 * 
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class EstrattoContoClientiEdicolaDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private String tipoMovimento;
	private Timestamp dataMovimento;
	private BigDecimal importoDare;
	private BigDecimal importoAvere;
	private String titolo;
	private String sottotitolo;
	private String numero;
	private Integer quantita;
	private BigDecimal importo;

	public String getImportoDareF() {
		return IGerivConstants.EURO_SIGN + " " + NumberUtils.formatNumber(getImportoDare(), IGerivConstants.DECIMAL_FORMAT_PATTERN);
	}
	
	public String getImportoAvereF() {
		return IGerivConstants.EURO_SIGN + " " + NumberUtils.formatNumber(getImportoAvere(), IGerivConstants.DECIMAL_FORMAT_PATTERN);
	}
	
	public String getImportoF() {
		return IGerivConstants.EURO_SIGN + " " + NumberUtils.formatNumber(getImporto(), IGerivConstants.DECIMAL_FORMAT_PATTERN);
	}

}
