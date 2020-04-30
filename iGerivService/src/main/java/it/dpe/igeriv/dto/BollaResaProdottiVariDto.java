package it.dpe.igeriv.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class BollaResaProdottiVariDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Long idDocumento;
	private Integer quantitaResa;
	private Float prezzo;
	private String numeroDocumento;
	private Timestamp dataDocumento;
	private String descrizioneProdotto;
	private String codiceProdottoFornitore;
	private Integer codFornitore;
	private String nomeFornitore;
	private String tipoLocalita;
	private String indirizzo;	
	private Integer numeroCivico;
	private String estensione;	
	private String localita;
	private String provincia;
	private String cap;
	private String piva;
	
	public Float getImporto() {
		return getQuantitaResa() != null && getPrezzo() != null ? getQuantitaResa().floatValue() * getPrezzo() : 0f; 
	}
	
}
