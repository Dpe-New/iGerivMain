package it.dpe.igeriv.web.rest.dto;

import java.io.Serializable;

import lombok.Data;
@Data
public class ContattiDto  implements Serializable{

	public ContattiDto(String email,String mobile){
		setEmail(email);
		setMobile(mobile);
	}
	
	private String email ="";
	private String mobile="";

	
}
