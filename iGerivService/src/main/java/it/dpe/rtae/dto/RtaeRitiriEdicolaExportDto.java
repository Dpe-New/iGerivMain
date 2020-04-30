package it.dpe.rtae.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class RtaeRitiriEdicolaExportDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Long barcode;
	private String codiceEdizione;
	private Integer codFiegDl;
	private Integer codiceRivenditaDl;
	private Long copie;
	private Integer idEsportazione;
}
