package it.dpe.igeriv.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StatisticaUtilizzoDto extends BaseDto {
	private static final long serialVersionUID = -480043363847626464L;
	private Integer codRivenditaDl;
	private Integer codRivenditaWeb;
	private String nomeRivendita;
	private Long numBolle;
	private Long numRese;
	private Long numVendite;
	private Long numRifornimenti;
	private Long numVariazioni;
	private Long numMancanze;
	private Long numEccedenze;
}
