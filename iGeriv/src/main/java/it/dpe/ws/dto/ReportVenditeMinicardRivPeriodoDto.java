package it.dpe.ws.dto;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import it.dpe.igeriv.util.DateUtilities;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportVenditeMinicardRivPeriodoDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Date dataVendita;
	private Long copieConsegnate;
	
	public String getDataVenditaFormat() {
		return getDataVendita() != null ? DateUtilities.getTimestampAsString(getDataVendita(), DateUtilities.FORMATO_DATA_SLASH) : "";
	}
}
