package it.dpe.ws.dto;

import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.NumberUtils;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportRicaricheRivPeriodoDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private BigDecimal importoLordo;
	private BigDecimal importoNetto;
	
	public String getImportoLordoFormat() {
		return getImportoLordo() != null ? IGerivConstants.EURO_SIGN_HTML + " " + NumberUtils.formatNumber(getImportoLordo()) : "";
	}
	
	public String getImportoNettoFormat() {
		return getImportoNetto() != null ? IGerivConstants.EURO_SIGN_HTML + " " + NumberUtils.formatNumber(getImportoNetto()) : "";
	}
}
