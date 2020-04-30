package it.dpe.igeriv.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class DateTipiBollaDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer idDataTipoBolla;
	private String dataBollaFormat;
	private String tipoBolla;
	private Boolean readonly;
	private Integer gruppoSconto;
	private Integer bollaTrasmessaDl;
	private String dataRegistrazioneDocumento;
	private String oraRegistrazioneDocumento;
}
