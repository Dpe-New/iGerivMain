package it.dpe.igeriv.dto;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author romanom
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class MancanzaDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private String titolo;
	private String sottotitolo;
	private String numero;
	private Timestamp dtUscita;
	private Integer copie;
	private Integer stato;
	private Timestamp dtBolla;
	private String tipoBolla;
	private String note;

	public String getStatoDesc() {
		if (stato != null) {
			if (stato.equals(IGerivConstants.COD_STATO_MANCANZE_INSERITO)) {
				return IGerivMessageBundle.get(IGerivConstants.STATO_MANCANZE_INSERITO_TEXT);
			} else if (stato.equals(IGerivConstants.COD_STATO_MANCANZE_SOSPESO)) {
				return IGerivMessageBundle.get(IGerivConstants.STATO_MANCANZE_SOSPESO_TEXT);
			} else if (stato.equals(IGerivConstants.COD_STATO_MANCANZE_ACCREDITATA)) {
				return IGerivMessageBundle.get(IGerivConstants.STATO_MANCANZE_ACCREDITATA_TEXT);
			} else if (stato.equals(IGerivConstants.COD_STATO_MANCANZE_NON_ACCREDITATA)) {
				return IGerivMessageBundle.get(IGerivConstants.STATO_MANCANZE_NON_ACCREDITATA_TEXT);
			} else if (stato.equals(IGerivConstants.COD_STATO_MANCANZE_PAREGGIATE)) {
				return IGerivMessageBundle.get(IGerivConstants.STATO_MANCANZE_PAREGGIATE_TEXT);
			}
		}
		return "";
	}
}
