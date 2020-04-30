package it.dpe.igeriv.dto;

import it.dpe.igeriv.util.NumberUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PubblicazioneLocalVenditeDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer idtn;
	private Integer progressivo;
	private Integer coddl;
	private String titolo;
	private String sottoTitolo;
	private String barcode;
	private Integer codicePubblicazione;
	private String numeroCopertina;
	private BigDecimal prezzoCopertina;
	private Timestamp dataUscita;
	private Integer codInizioQuotidiano;
	private Integer codFineQuotidiano;
	private Integer quantita;
	private Integer giacenzaIniziale;
	private String strData;
	
	public String getPrezzoCopertinaFormat() {
		return (prezzoCopertina != null) ? NumberUtils.formatNumber(prezzoCopertina) : "";
	}
	
	public String getImportoFormat() {
		return (prezzoCopertina != null && quantita != null) ? NumberUtils.formatNumber(prezzoCopertina.multiply(new BigDecimal(quantita))) : "";
	}
	
}
