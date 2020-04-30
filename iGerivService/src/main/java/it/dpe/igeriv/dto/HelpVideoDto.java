package it.dpe.igeriv.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class HelpVideoDto extends BaseDto {
	
	private Integer codice;
	private String titolo;
	private String nomeFile;

}
