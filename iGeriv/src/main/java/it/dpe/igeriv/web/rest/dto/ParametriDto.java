package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ParametriDto implements Serializable{
	
	private String chiave;
	private String valore;
	
}
