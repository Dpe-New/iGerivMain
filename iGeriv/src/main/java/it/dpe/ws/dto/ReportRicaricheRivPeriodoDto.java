package it.dpe.ws.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.NumberUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportRicaricheRivPeriodoDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private BigDecimal importoRicarica; 
	private Integer numRicariche;
	private BigDecimal importoLordo;
	private BigDecimal importoNetto;
	
	private BigDecimal importoTotaleLordo;
	
	
	public ReportRicaricheRivPeriodoDto(){
		importoRicarica = new BigDecimal(0);
		numRicariche = new Integer(0);
		importoLordo = new BigDecimal(0);
		importoNetto = new BigDecimal(0);
		importoTotaleLordo = new BigDecimal(0);
	}
	
	public String getImportoRicaricaFormat() {
		//ticket 0000375
		return getImportoRicarica() != null ? IGerivConstants.EURO_SIGN_HTML + " " + NumberUtils.formatNumber(getImportoRicarica()) : "TOTALE";
	}
	public String getNumRicaricheFormat() {
		return getNumRicariche() != null ? getNumRicariche().toString() : "";
	}
	
	public String getImportoLordoFormat() {
		return getImportoLordo() != null ? IGerivConstants.EURO_SIGN_HTML + " " + NumberUtils.formatNumber(getImportoLordo()) : "";
	}
	
	public String getImportoNettoFormat() {
		return getImportoNetto() != null ? IGerivConstants.EURO_SIGN_HTML + " " + NumberUtils.formatNumber(getImportoNetto()) : "";
	}
	
	
	public String getImportoTotaleLordoFormat() {
		return getImportoTotaleLordo() != null ? IGerivConstants.EURO_SIGN_HTML + " " + NumberUtils.formatNumber(getImportoTotaleLordo()) : "";
	}
	
	
}
