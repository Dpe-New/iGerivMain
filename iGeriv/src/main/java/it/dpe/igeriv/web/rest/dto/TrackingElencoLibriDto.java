package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
@Data
public class TrackingElencoLibriDto implements Serializable{
	
	private String sku;
	private String keynum;
	private String stato;
	
	private List<TrackingParametriDto> parametri;
}
