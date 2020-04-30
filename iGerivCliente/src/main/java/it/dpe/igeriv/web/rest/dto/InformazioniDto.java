package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class InformazioniDto implements Serializable{
	
	public InformazioniDto(String chiave,String valore){
		setChiave(chiave);
		setValore(valore);
	}
	private String chiave;
	private String valore;
}
