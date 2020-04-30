package it.dpe.ws.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RitiriDlExportDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer numeroRecord;
	private Date dataRitiro;
	private String titolo;
	private String titoloSecondaParte;
	private Long copieRitirate;
	private Integer codiceRivenditaDl;
	private String ragioneSocialeRiv;
	private Integer codiceFiegDl;
	private Long barcode;
	private Integer issn;
	private Integer var;
	private Integer addon;
	private Date dataUscita;
	private Integer causale;
	@Getter(AccessLevel.NONE)
	private Double prezzo;
	private Double percentualeDefiscalizzazione;
	private Double sconto;
	private Double compenso;
	private Double prezzoUnitarioAccreditoCopie;
	private Double valoreAccreditoCopie;
	private Double percentualeAggio;
	private Double prezzoAggio;
	private Double valoreUnitarioAggio;
	private Double aggioDaAccreditare;
	private Integer tipoTessera;
	private String numeroCopertina;
	private String rivendita;
	private Integer codRivDl;
	private String dl;
	private String localita;
	private String provincia;
	private Double aggio;
	private BigDecimal prezzoBigDecimal;
	private String codiceProdottoDn;
	
	public Double getAggioTotale() {
		return (copieRitirate != null && aggio != null) ? copieRitirate * aggio : null;
	}
	
	public Double getPrezzoTotale() {
		Double result = null;
		
		if (copieRitirate != null) {
			if (prezzo != null) {
				result = copieRitirate * prezzo;
			}
			else if (prezzoBigDecimal != null) {
				result = copieRitirate * prezzoBigDecimal.doubleValue();
			}
		}
		
		return result;
	}
	
	public Double getPrezzo() {
		Double result = null;
		
		if (prezzo != null) {
			result = prezzo;
		}
		else if (prezzoBigDecimal != null) {
			result = prezzoBigDecimal.doubleValue();
		}
		
		return result;
	}
	
	public String getSort() {
		return getTipoTessera() + getTitolo() + getDataUscita().getTime();
	}

}
