package it.dpe.ws.dto;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardStatusVo implements Serializable {
	private static final long	serialVersionUID	= 8234977886748914997L;
	private String				creditoResiduo;
	private String				dataInizioAbbonamento;
	private String				dataFineAbbonamento;
	private Integer				copieResidue;
	private String				titolo;
	private String				stato;
	private Boolean 			showCopie;
	private Integer				idProdotto;
	private String				tipoTessera;
}
