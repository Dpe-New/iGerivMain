package it.dpe.igeriv.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class DateEstrattoContoClienteDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Timestamp dataEstrattoConto;
	private Timestamp dataCompetenzaEstrattoContoClienti;
	private Integer tipoProdottiInEstrattoConto;
}
