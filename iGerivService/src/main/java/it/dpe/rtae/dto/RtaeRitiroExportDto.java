package it.dpe.rtae.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class RtaeRitiroExportDto extends BaseDto {
	private static final long serialVersionUID = -1351395438150542397l;
	private Integer idEsportazione;
	private Timestamp dataEsportazione;
	private Integer statoEsportazione;
	private String descrizioneErrore;
	private List<RtaeRitiriEdicolaExportDto> listGiornaleRitiriDto;
}
