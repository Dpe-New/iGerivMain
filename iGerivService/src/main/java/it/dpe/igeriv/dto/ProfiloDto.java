package it.dpe.igeriv.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ProfiloDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String titolo;
	private String descrizione;
	private String roleName;
	private Integer tipoProfilo;
}
