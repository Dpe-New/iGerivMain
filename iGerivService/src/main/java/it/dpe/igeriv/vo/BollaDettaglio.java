package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.IBollaDettaglioPk;

import java.math.BigDecimal;

public interface BollaDettaglio {

	IBollaDettaglioPk getPk();

	Integer getIdtn();

	BigDecimal getPrezzoLordo();

	BigDecimal getPrezzoNetto();

	BigDecimal getSconto();

	Integer getQuantitaConsegnata();

	Integer getDifferenze();

	void setDifferenze(Integer differenze);
	
	void setOrdini(Integer ordini);

	Integer getSpunta();

	void setSpunta(Integer spunta);

	Integer getOrdini();

	String getBarcode();

	String getTipoFondoBolla();

	String getTitolo();

	String getSottoTitolo();

	String getNumeroPubblicazione();

	BigDecimal getPercentualeIva();

	String getImmagine();
	
	BigDecimal getImporto();
	
}