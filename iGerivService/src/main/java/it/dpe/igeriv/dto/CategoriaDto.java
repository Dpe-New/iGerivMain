package it.dpe.igeriv.dto;

import it.dpe.igeriv.util.StringUtility;
import lombok.Getter;
import lombok.Setter;

import com.google.common.base.Strings;

@Getter
@Setter
public class CategoriaDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Long codCategoria;
	private String descrizione;
	private String immagine;
	
	public String getDescrizione() {
		return !Strings.isNullOrEmpty(descrizione) ? descrizione.trim() : descrizione;
	}

	@Override
	public boolean equals(Object obj) {
		CategoriaDto cdto = (CategoriaDto) obj;
		return getCodCategoria().equals(cdto.getCodCategoria());
	}
	
	public String getDescrizioneNoHtml() {
		return descrizione != null ? StringUtility.stripHtml(descrizione) : descrizione;
	}
	
}
