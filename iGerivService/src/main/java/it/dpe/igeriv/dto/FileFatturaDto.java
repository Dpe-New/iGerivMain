package it.dpe.igeriv.dto;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.google.common.base.Strings;

@Data
@EqualsAndHashCode(callSuper=false)
public class FileFatturaDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private String fileName;
	private Timestamp data;
	private Integer numero;
	private Long codCliente;
	private String nome;
	private String cognome;
	private Integer tipoDocumento;
	
	public String getTipoDocumentoDesc() {
		if (getTipoDocumento() != null) {
			if (getTipoDocumento().equals(IGerivConstants.FATTURA)) {
				return IGerivMessageBundle.get("igeriv.fattura");
			} else if (getTipoDocumento().equals(IGerivConstants.STORNO_FATTURA)) {
				return IGerivMessageBundle.get("igeriv.nota.credito");
			} else if (getTipoDocumento().equals(IGerivConstants.FATTURA_STORNATA)) {
				return IGerivMessageBundle.get("igeriv.fattura.stornata");
			}
		}
		return "";
	}
	
	public String getNomeCognome() {
		return getNome() + (Strings.isNullOrEmpty(getCognome()) ? "" : (" " + getCognome()));
	}
	
	public String getFake() {
		return "";
	}
}
