package it.dpe.igeriv.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import it.dpe.igeriv.util.IGerivConstants;

@Data
@EqualsAndHashCode(callSuper=false)
public class BollaResaRichiamoPersonalizzatoDettaglioDto extends BollaResaBaseDto {
	private static final long serialVersionUID = 1L;
	
	public int getTipo() {
		return IGerivConstants.TIPO_BOLLA_RESA_RICHIAMO_PERSONALIZZATO;
	}
	
}
