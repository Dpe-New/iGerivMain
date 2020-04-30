package it.dpe.igeriv.dto;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;

import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

@Data
@EqualsAndHashCode(callSuper=false)
public class MessaggioDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(getClass());
	private String messaggio;
	private Integer tipoMessaggio;
	private Timestamp dtMessaggio;
	private Integer statoMessaggio;
	private String attachmentName1;
	private String attachmentName2;
	private String attachmentName3;
	private Integer messaggioLetto;
	private String messaggioEscape;
	private Integer codFiegDl;
	private Integer tipoDestinatario;
	private Integer destinatarioA;
	private Integer destinatarioB;
	private String edicolaLabel;
	private Blob messaggioEsteso;
	
	public String getMessaggioShort() {
		String msg = getMessaggio();
		if (msg != null) {
			msg = Jsoup.parse(msg.trim()).text().replace(IGerivConstants.EURO_SIGN, IGerivConstants.EURO_SIGN_DECIMAL);
			if (msg.length() > 50) {
				msg = msg.substring(0, 50) + "...";
			}
		}
		return msg;
	}
	
	public String getStatoMessaggioDesc() {
		return (getStatoMessaggio() != null && getStatoMessaggio().equals(IGerivConstants.STATO_MESSAGGIO_INVIATO)) ? IGerivMessageBundle.get("igeriv.inviato") : IGerivMessageBundle.get("igeriv.inserito");
	}
	
	public String getTipoMessaggioDesc() {
		String desc = null;
		 if (getTipoMessaggio() != null) {
			 if (getTipoMessaggio().equals(IGerivConstants.TIPO_MESSAGGIO_NORMALE)) {
				 desc = IGerivMessageBundle.get("igeriv.normale");
			 } else if (getTipoMessaggio().equals(IGerivConstants.TIPO_MESSAGGIO_CON_AVVISO)) {
				 desc = IGerivMessageBundle.get("igeriv.allerta");
			 } else if (getTipoMessaggio().equals(IGerivConstants.TIPO_MESSAGGIO_IMMEDIATO)) {
				 desc = IGerivMessageBundle.get("igeriv.emergenza");
			 } else if (getTipoMessaggio().equals(IGerivConstants.TIPO_MESSAGGIO_UREGENTISSIMO)) {
				 desc = IGerivMessageBundle.get("igeriv.altissima");
			 }
		}
		return desc;
	}
	
	public String getMessaggioLettoDesc() {
		return (messaggioLetto != null && messaggioLetto.equals(1)) ? IGerivMessageBundle.get("igeriv.si") : IGerivMessageBundle.get("igeriv.no");
	}
	
	public String getPk() {
		return getCodFiegDl() + "|" + DateUtilities.getTimestampAsString(getDtMessaggio(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS) + "|" + getTipoDestinatario() + "|" + getDestinatarioA() + "|" + getDestinatarioB();
	}
	
	public String getMessaggio() {
		try {
			if (getMessaggioEsteso() != null) {
				byte[] bdata = getMessaggioEsteso().getBytes(1, (int) getMessaggioEsteso().length());
				return new String(bdata);
			}
		} catch (SQLException e) { 
			log.error("Error converting Blob in MessaggioDto.getMessaggio()", e);
		}
		return messaggio;
	}
	
	@Override
	public void accept(VisitorDto visitor) {
		visitor.visit(this);
	}
	
}
