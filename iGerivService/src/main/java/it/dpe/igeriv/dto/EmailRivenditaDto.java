package it.dpe.igeriv.dto;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.StringUtility;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class EmailRivenditaDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer idEmailRivendita;
	private Timestamp dataMessaggio;
	private String titolo;
	private String messaggio;
	private String destinatari;
	private String allegato;
	private String messaggioEscape;
	public Boolean letto;
	
	public String getLettoDesc() {
		if (getLetto() != null && getLetto()) {
			return IGerivMessageBundle.get("igeriv.si");
		} else {
			return IGerivMessageBundle.get("igeriv.no");
		}
	}

	public String getTitoloShort() {
		String msg = null;
		if (titolo != null) {
			msg = StringUtility.removeHTML(titolo.trim());
			if (msg.length() > 50) {
				msg = msg.substring(0, 50) + "...";
			}
		}
		return msg;
	}
	
	public String getMessaggioShort() {
		String msg = null;
		if (messaggio != null) {
			msg = StringUtility.removeHTML(messaggio.trim());
			if (msg.length() > 50) {
				msg = msg.substring(0, 50) + "...";
			}
		}
		return msg;
	}
	
	@Override
	public void accept(VisitorDto visitor) {
		visitor.visit(this);
	}
	
}
