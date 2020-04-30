package it.dpe.igeriv.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import it.dpe.igeriv.util.IGerivConstants;

@Data
@EqualsAndHashCode(callSuper=false)
public class BollaResaDettaglioDto extends BollaResaBaseDto {
	private static final long serialVersionUID = 1L;

	public int getTipo() {
		return IGerivConstants.TIPO_BOLLA_RESA;
	}
	
	public String getSortCriteria() {
		return String.format("%03d%04d", getPk().getCodFiegDl(), getPk().getPosizioneRiga());
	}
}
