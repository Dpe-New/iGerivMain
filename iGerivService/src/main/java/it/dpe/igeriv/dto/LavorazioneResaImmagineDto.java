package it.dpe.igeriv.dto;

import it.dpe.igeriv.util.DateUtilities;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class LavorazioneResaImmagineDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer idtn;
	private Timestamp dataOraLavorazione;
	private Integer copie;
	private String nomeImmagine;
	private String titolo;
	private String numeroCopertina;
	
	public String getDataOraLavorazioneFormat() {
		return DateUtilities.getTimestampAsStringExport(getDataOraLavorazione(), DateUtilities.FORMATO_DATA_SLASH);
	}
}
