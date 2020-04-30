package it.dpe.igeriv.dto;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.StringUtility;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper=false)
public class PromemoriaDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Long codPromemoria;
	private Integer codEdicola;
	private String messaggio;
	private Date dataMessaggio;
	@Getter(AccessLevel.NONE)
	private Boolean letto;
	private String messaggioEscape;
	
	public Boolean getLetto() {
		return letto == null ? false : letto;
	}

	public String getMessaggioShort() {
		String msg = null;
		if (messaggio != null) {
			msg = StringUtility.removeHTML(messaggio.trim());
			if (msg.length() > 80) {
				msg = msg.substring(0, 80) + "...";
			}
		}
		return msg;
	}
	
	public String getLettoDesc() {
		String msg = IGerivMessageBundle.get("igeriv.no");
		if (getLetto()) {
			msg = IGerivMessageBundle.get("igeriv.si");
		}
		return msg;
	}
	
}
