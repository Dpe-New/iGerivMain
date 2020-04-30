package it.dpe.igeriv.dto;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PrenotazioneDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer codFiegDl;
	private Integer codicePubblicazione;
	private Integer idtn;
	private String titolo;
	private String sottotitolo;
	private String numero;
	private String argomento;
	private String periodicita;
	private Integer codEdicola;
	private Date dataRichiesta;
	private Integer quantitaRichiesta;
	private Integer quantitaInBolla;
	private String motivoRichiesta;
	private Integer indicatoreTrasmessoDl;
	private Timestamp dataUltimaTrasmissioneDl;

	public String getStatoDesc() {
		if (getIndicatoreTrasmessoDl().equals(IGerivConstants.INDICATORE_RECORD_NON_TRASMESSO_DL)) {
			return IGerivMessageBundle.get(IGerivConstants.STATO_INSERITO);
		} else if (getIndicatoreTrasmessoDl().equals(IGerivConstants.INDICATORE_RECORD_IN_TRASMISSIONE_DL)) {
			return IGerivMessageBundle.get(IGerivConstants.STATO_PRONTO_PER_INVIO);
		} else {
			return IGerivMessageBundle.get(IGerivConstants.STATO_INVIATO_DL);
		}
	}
	
}
