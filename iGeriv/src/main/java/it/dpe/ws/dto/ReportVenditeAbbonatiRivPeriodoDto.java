package it.dpe.ws.dto;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import it.dpe.igeriv.util.DateUtilities;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportVenditeAbbonatiRivPeriodoDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private java.sql.Date dataProdottoVenduto;
	private Long copieConsegnate;
	
	public String getDataProdottoVendutoFormat() {
		return getDataProdottoVenduto() != null ? DateUtilities.getTimestampAsString(getDataProdottoVenduto(), DateUtilities.FORMATO_DATA_SLASH) : "";
	}
}
