package it.dpe.igeriv.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.google.gson.annotations.SerializedName;

@Data
@EqualsAndHashCode(callSuper=false)
public class ParametriRicercaLibriDto extends BaseDto {
	
	private static final long serialVersionUID = 1L;
	
	@SerializedName("TOT_RECORD") 
	private String TOT_RECORD;
	@SerializedName("GUID") 
	private String GUID;
	
	
}
