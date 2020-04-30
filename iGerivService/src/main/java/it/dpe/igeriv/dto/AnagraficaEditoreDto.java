package it.dpe.igeriv.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class AnagraficaEditoreDto extends BaseDto{
	
	private static final long serialVersionUID = 1L;
	//PK
	private Integer codFiegDl;
	private Integer codFornitore;
	
	private String password;
	private String nomeA;
	private String nomeB;
	private String viaA;
	private String viaB;
	private String localitaA;
	private String localitaB;
	private Integer paese;
	private String provincia;
	private String cap;
	private String telefono;
	private String fax;
	private String email;
	private String url;
}
