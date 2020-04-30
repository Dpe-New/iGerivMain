package it.dpe.igeriv.dto;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper=false)
public class EstrattoContoFatturaClientiDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	@Getter(AccessLevel.NONE)
	private String fileName;
	private Integer tipoDocumento;
	private Timestamp dataCompetenza;
	private Timestamp dataDocumento;
	private Integer numeroDocumento;
	private Long codCliente;
	private Integer tipo;
	private BigDecimal importoTotale;
	
	public String getFileName() {
		return fileName == null ? "" : fileName;
	}

	public String getTipoDocumentoDesc() {
		if (getTipoDocumento() != null) {
			if (getTipoDocumento().equals(IGerivConstants.FATTURA)) {
				return IGerivMessageBundle.get("igeriv.fattura");
			} else if (getTipoDocumento().equals(IGerivConstants.STORNO_FATTURA)) {
				return IGerivMessageBundle.get("igeriv.nota.credito");
			} else if (getTipoDocumento().equals(IGerivConstants.FATTURA_STORNATA)) {
				return IGerivMessageBundle.get("igeriv.fattura.stornata");
			} else if (getTipoDocumento().equals(IGerivConstants.ESTRATTO_CONTO)) {
				return IGerivMessageBundle.get("igeriv.data.estratto.conto");
			}
		}
		return "";
	}
	
}
