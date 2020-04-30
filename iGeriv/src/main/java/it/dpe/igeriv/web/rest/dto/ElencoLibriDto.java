package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
@Data
public class ElencoLibriDto implements Serializable {
	
	private String sku;
	private List<ParametriDto> parametri;
	
	
}
