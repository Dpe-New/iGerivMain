package it.dpe.igeriv.dto;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.StringUtility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper=false)
public class MessaggioDpeDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Long codice;
	private Timestamp data;
	private String titolo;
	@Getter(AccessLevel.NONE)
	private String testo;
	private String url;
	private Integer priorita;
	private Boolean abilitato;
	private Boolean inviaNotificaDl;
	
	public String getTesto() {
		return  testo != null ? testo.trim() : testo;
	}

	public String getMessaggioEscape() throws UnsupportedEncodingException {
		return getTesto() != null ? URLEncoder.encode(getTesto(), "UTF-8") : null;
	}
	
	public String getTitoloEscape() throws UnsupportedEncodingException {
		return getTitolo() != null ? URLEncoder.encode(getTitolo(), "UTF-8") : null;
	}

	public String getMessaggioShort() {
		String msg = null;
		if (testo != null) {
			msg = StringUtility.removeHTML(testo.trim().replaceAll("\\\\'", "'"));
			if (msg.length() > 80) {
				msg = msg.substring(0, 80) + "...";
			}
		}
		return msg;
	}
	
	public String getTitoloShort() {
		String msg = null;
		if (titolo != null) {
			msg = StringUtility.removeHTML(titolo.trim().replaceAll("\\\\'", "'"));
			if (msg.length() > 40) {
				msg = msg.substring(0, 40) + "...";
			}
		}
		return msg;
	}
	
	public String getUrlShort() {
		String msg = null;
		if (url != null) {
			msg = StringUtility.removeHTML(url.trim());
			if (msg.length() > 40) {
				msg = msg.substring(0, 40) + "...";
			}
		}
		return msg;
	}
	
	public String getAbilitatoDesc() {
		if (abilitato != null && abilitato) {
			return IGerivMessageBundle.get("igeriv.si");
		} else {
			return IGerivMessageBundle.get("igeriv.no");
		}
	}
}
