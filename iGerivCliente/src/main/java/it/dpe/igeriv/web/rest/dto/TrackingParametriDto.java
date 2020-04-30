package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;

import lombok.Data;
@Data
public class TrackingParametriDto  implements Serializable{
	
	private String chiave;
	private String valore;
}
