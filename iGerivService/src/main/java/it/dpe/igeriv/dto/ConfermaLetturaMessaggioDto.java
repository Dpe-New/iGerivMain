package it.dpe.igeriv.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;

@Data
@EqualsAndHashCode(callSuper=false)
public class ConfermaLetturaMessaggioDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer messaggioLetto;
	private Integer codEdicola;
	private String ragioneSociale;
	private String localita;
	
	public String getMessaggioLettoDesc() {
		return (messaggioLetto != null && messaggioLetto.equals(IGerivConstants.COD_MESSAGGIO_LETTO) ? IGerivMessageBundle.get("igeriv.si") : IGerivMessageBundle.get("igeriv.no"));
	}

}
