package it.dpe.igeriv.dto;

import it.dpe.igeriv.util.DateUtilities;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author romanom
 * 
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class MancanzaBollaDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Timestamp dtBolla;
	private String tipoBolla;
	private Integer codicePubblicazione;
	private String titolo;
	private Integer idtn;
	private Timestamp dtUscita;
	private String numero;
	private Integer mancanze;
	private Integer mancanzeAccreditate;
	private String tipo;

	public String getDtBollaFormat() {
		if (getDtBolla() != null) {
			return DateUtilities.getTimestampAsStringExport(getDtBolla(), DateUtilities.FORMATO_DATA_SLASH);
		}
		return "";
	}

}
