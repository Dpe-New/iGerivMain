package it.dpe.igeriv.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class LivellamentiDto extends BaseDto {
	
	private static final long serialVersionUID = 1L;
	
	private Integer coddl;
	private Integer codEdicola;
	private String ragioneSocialeEdicolaPrimaRiga;
	
	private String titolo;
	private Timestamp dataUscita;
	private String numeroCopertina;
	private Integer quantitaRichiesta;

}
