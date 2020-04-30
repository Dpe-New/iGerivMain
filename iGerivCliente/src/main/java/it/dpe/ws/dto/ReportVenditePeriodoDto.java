package it.dpe.ws.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportVenditePeriodoDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Timestamp dataVendita;
	private Integer idTessera;
	private Date dataProdottoVenduto;
	private Long copieConsegnate;
	private Integer codRivenditaDl;
	private String ragioneSocialeDlPrimaRiga;
	
	public String getPk() {
		return idTessera + "|" + dataProdottoVenduto;
	}

	public Date getDataVenditaTrunc() {
		return (dataVendita != null) ? new Date(dataVendita.getTime()) : null;
	}

}
