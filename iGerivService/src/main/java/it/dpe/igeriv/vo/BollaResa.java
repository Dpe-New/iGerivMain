package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.IBollaDettaglioPk;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface BollaResa {

	IBollaDettaglioPk getPk();
	
	Integer getCpuDl();
	
	String getTitolo();

	String getSottoTitolo();

	String getNumeroPubblicazione();

	Long getDistribuito();

	Integer getReso();
	
	Long getGiacenza();
	
	Timestamp getDataUscita();
	
	BigDecimal getPrezzoLordo();

	BigDecimal getPrezzoNetto();
	
	BigDecimal getImportoLordo();
	
	BigDecimal getImportoNetto();
	
	String getTipoRichiamo();
	
	String getImmagine();

	Integer getIdtn();

	void setGiacenza(Long giacenza);
	
}