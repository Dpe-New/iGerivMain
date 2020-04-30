package it.dpe.igeriv.dto;

import it.dpe.igeriv.util.DateUtilities;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AvvisoMessaggioDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer codFiegDl;
	private Timestamp dtMessaggio;
	private Integer tipoDestinatario;
	private Integer destinatarioA;
	private Integer destinatarioB;
	private Integer priorita;
	private Integer categoria;
	
	public String getPk() {
		return getCodFiegDl() + "|" + DateUtilities.getTimestampAsString(getDtMessaggio(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS) + "|" + getTipoDestinatario() + "|" + getDestinatarioA() + "|" + getDestinatarioB();
	}

}
