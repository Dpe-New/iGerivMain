package it.dpe.igeriv.dto;

import it.dpe.igeriv.util.DateUtilities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class VenditaDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Long codVendita;
	private Integer codFiegDl;
	private Integer codEdicola;
	private Timestamp dataVendita;
	private Timestamp dataEstrattoConto;
	private Integer trasferitaGestionale;
	private Timestamp dataTrasfGestionale;
	private List<VenditaDettaglioDto> listVenditaDettaglio;
	private String codCliente;
	private String contoCliente;
	private String idScontrino;
	private String fatturaEmessa;
	private String fatturaContoUnico;
	private String numeroFattura;
	private String nomeCliente;
	
	public BigDecimal getSommaVenduto() {
		BigDecimal somma = new BigDecimal(0);
		if (listVenditaDettaglio != null) {
			for (VenditaDettaglioDto vo : listVenditaDettaglio) {
				somma = somma.add(vo.getImportoTotale());
			}
		}
		return somma;
	}
	
	public String getDataVenditaFormat() {
		return (dataVendita != null) ? DateUtilities.getTimestampAsString(dataVendita, DateUtilities.FORMATO_DATA_SLASH_HHMMSS) : "";
	}
	
	public String getDataEstrattoContoFormat() {
		return (dataEstrattoConto != null) ? DateUtilities.getTimestampAsString(dataEstrattoConto, DateUtilities.FORMATO_DATA) : "";
	}
	
}
