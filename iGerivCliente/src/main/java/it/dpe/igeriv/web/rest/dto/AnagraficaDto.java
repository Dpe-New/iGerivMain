package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class AnagraficaDto implements Serializable {
	
	private String nome;
	private String cognome;
	private ContattiDto contatti;
	

}
