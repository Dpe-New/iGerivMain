package it.dpe.igeriv.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import it.dpe.igeriv.dto.BaseDto;

@Data
@EqualsAndHashCode(callSuper=false)
public class GiroTipoDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer idGiroTipo;
	private String descrizione;
}
