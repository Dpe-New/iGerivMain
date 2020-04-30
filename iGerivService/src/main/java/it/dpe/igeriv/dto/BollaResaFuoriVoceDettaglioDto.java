package it.dpe.igeriv.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;

@Data
@EqualsAndHashCode(callSuper=false)
public class BollaResaFuoriVoceDettaglioDto extends BollaResaBaseDto {
	private static final long serialVersionUID = 1L;
	
	public int getTipo() {
		return IGerivConstants.TIPO_BOLLA_RESA_FUORI_VOCE;
	}
	
	@Override
	public String getTipoRichiamo() {
		return (getResaAnticipata() != null && getResaAnticipata()) ? IGerivMessageBundle.get("igeriv.resa.anticipata") : "";
	}
}
