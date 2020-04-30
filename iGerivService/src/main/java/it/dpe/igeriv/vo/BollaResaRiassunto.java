package it.dpe.igeriv.vo;

import java.sql.Timestamp;


public interface BollaResaRiassunto {
	
	public Timestamp getDtBolla();
	
	public String getTipoBolla();
	
	Integer getGruppoSconto();
	
	public Integer getBollaTrasmessaDl();

}