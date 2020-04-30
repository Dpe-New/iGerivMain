package it.dpe.igeriv.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class GiroDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer codGiro;
	private String descGiro;
}
