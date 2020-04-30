package it.dpe.igeriv.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author romanom
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class StatisticaDettaglioDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Timestamp data;
	private String tipoBolla;
	private String tipoFondoBolla;
	private Integer copie;
	private Integer tipo;
}
