package it.dpe.igeriv.dto;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ContestazioneResaDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Timestamp dataResa;
	private String tipoResa;
	private String titolo;
	private String numero;
	private Integer stato;
	private Integer copieDichiarate;
	private Integer copieApprovate;
	private Float importoDichiarato;
	private Float importoApprovato;
	private String motivazioneRifiuto;
	private String noteChiusura;

	public String getNote() { 
		return ((getMotivazioneRifiuto() == null) ? "" : getMotivazioneRifiuto()) + " " + ((getNoteChiusura() == null) ? "" : getNoteChiusura());
	}
	 
	public String getStatoDesc() {
		if (stato != null) {
			if (stato.equals(IGerivConstants.COD_STATO_CONTESTAZIONI_RESA_DA_LAVORARE)) {
				return IGerivMessageBundle.get(IGerivConstants.STATO_CONTESTAZIONI_RESA_DA_LAVORARE_TEXT);
			} else if (stato.equals(IGerivConstants.COD_STATO_CONTESTAZIONI_RESA_APPROVATA)) {
				return IGerivMessageBundle.get(IGerivConstants.STATO_CONTESTAZIONI_RESA_APPROVATA_TEXT);
			} else if (stato.equals(IGerivConstants.COD_STATO_CONTESTAZIONI_RESA_RIFIUTATA)) {
				return IGerivMessageBundle.get(IGerivConstants.STATO_CONTESTAZIONI_RESA_RIFIUTATA_TEXT);
			} else if (stato.equals(IGerivConstants.COD_STATO_CONTESTAZIONI_RESA_ANNULLATA)) {
				return IGerivMessageBundle.get(IGerivConstants.STATO_CONTESTAZIONI_RESA_ANNULLATA_TEXT);
			} 
		}
		return "";
	}
}
