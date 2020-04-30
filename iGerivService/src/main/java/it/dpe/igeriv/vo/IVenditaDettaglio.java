package it.dpe.igeriv.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface IVenditaDettaglio {

	Integer getIdtn();

	BigDecimal getPrezzoCopertina();

	String getTitolo();

	String getSottoTitolo();

	String getNumeroCopertina();

	Integer getQuantita();

	BigDecimal getImportoTotale();

	Integer getProgressivo();

	String getImportoFormat();
	
	Timestamp getDataUscita();
	
	Boolean isProdottoNonEditoriale();
	
	Long getIdProdotto();
	
	String getBarcode();

}