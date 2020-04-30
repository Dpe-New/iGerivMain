package it.dpe.inforiv.dto.output;

import it.dpe.igeriv.util.DateUtilities;
import it.dpe.inforiv.dto.input.InforivBaseDto;
import it.dpe.inforiv.dto.input.InforivDto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author romanom
 * 
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Component("InforivVenditeDto")
@Scope("prototype")
public class InforivVenditeDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private String tipoRecord;
	private Integer codFiegDl;
	private Integer codEdicola;
	@Getter(AccessLevel.NONE)
	private Integer idUnitaVendita;
	private Date dataVendita;
	@Getter(AccessLevel.NONE)
	private Integer oraMinutoVendita;
	@Getter(AccessLevel.NONE)
	private Float totaleVenditaQuotidianiPeriodici;
	@Getter(AccessLevel.NONE)
	private String idProdotto;
	private Integer copieVendute;
	@Getter(AccessLevel.NONE)
	private Float prezzoVenduto;
	private String descrizione;
	private Long codVendita;
	private BigDecimal importoTotale;
	private BigDecimal prezzoCopertina;
	private String idtnTrascodifica;
	
	public Integer getIdUnitaVendita() {
		if (codVendita != null) {
			return new Integer(codVendita.toString());
		}
		return idUnitaVendita;
	}

	public Integer getOraMinutoVendita() {
		if (dataVendita != null) {
			return new Integer(DateUtilities.getTimestampAsString(dataVendita, DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS).substring(8, 12));
		}
		return oraMinutoVendita;
	}

	public Float getTotaleVenditaQuotidianiPeriodici() {
		if (importoTotale != null) {
			return importoTotale.floatValue();
		}
		return totaleVenditaQuotidianiPeriodici;
	}

	public String getIdProdotto() {
		return (idtnTrascodifica != null) ? idtnTrascodifica.toString() : idProdotto;
	}

	public Float getPrezzoVenduto() {
		if (prezzoCopertina != null) {
			return prezzoCopertina.floatValue();
		}
		return prezzoVenduto;
	}
	
}
