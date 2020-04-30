package it.dpe.igeriv.dto;

import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.pk.IBollaDettaglioPk;
import it.dpe.igeriv.vo.pk.PeriodicitaPk;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper=false)
public class FondoBollaDettaglioDto extends BaseDto implements BollaDettaglio {
	private static final long serialVersionUID = 1L;
	private IBollaDettaglioPk pk;
	private String titolo;
	private String sottoTitolo;
	@Getter(AccessLevel.NONE)
	private String numeroPubblicazione;
	private Integer codicePubblicazione;
	private BigDecimal prezzoLordo;
	private BigDecimal sconto;
	private BigDecimal percentualeIva;
	private BigDecimal prezzoNetto;
	@Getter(AccessLevel.NONE)
	private Integer quantitaConsegnata;
	@Getter(AccessLevel.NONE)
	private BigDecimal importo;
	@Getter(AccessLevel.NONE)
	private Integer spunta;
	private Integer differenze;
	private Integer tipoRecordFondoBolla;
	private String tipoFondoBolla;
	private Integer ordini;
	private String immagine;
	private Integer idtn;
	private PeriodicitaPk periodicitaPk;
	private String barcode;
	private Integer indicatoreValorizzare;
	private String indicatorePrezzoVariato;
	private Integer rownum;
	private String note;
	private Integer quantitaSpuntata;
	private Integer confermaDifferenze;
	@Getter(AccessLevel.NONE)
	private Integer fornitoBollePrecedenti;
	@Getter(AccessLevel.NONE)
	private Integer mancanzeBollePrecedenti;
	@Getter(AccessLevel.NONE)
	private Integer resoDichiaratoBollePrecedenti;
	private Timestamp dataUscita;
	@Getter(AccessLevel.NONE)
	private Integer codInizioQuotidiano;
	
	private Integer idMessaggioIdtn = null;
	
	public String getNumeroPubblicazione() {
		return numeroPubblicazione != null ? numeroPubblicazione.trim() : numeroPubblicazione;
	}

	public Integer getQuantitaConsegnata() {
		return quantitaConsegnata == null ? 0 : quantitaConsegnata;
	}

	public BigDecimal getImporto() {
		return importo = (prezzoNetto != null && quantitaConsegnata != null) ? prezzoNetto.multiply(new BigDecimal(quantitaConsegnata)) : new BigDecimal(0);
	}

	public BigDecimal getImportoLordo() {
		return  (prezzoLordo != null && quantitaConsegnata != null) ? prezzoLordo.multiply(new BigDecimal(quantitaConsegnata)) : new BigDecimal(0);
	}
	
	public Integer getSpunta() {
		return spunta == null ? 0 : spunta;
	}

	public int getTipo() {
		return IGerivConstants.TIPO_FONDO_BOLLA;
	}

	public Integer getFornitoBollePrecedenti() {
		return fornitoBollePrecedenti == null ? 0 : fornitoBollePrecedenti;
	}

	public Integer getMancanzeBollePrecedenti() {
		return mancanzeBollePrecedenti == null ? 0 : mancanzeBollePrecedenti;
	}

	public Integer getResoDichiaratoBollePrecedenti() {
		return resoDichiaratoBollePrecedenti == null ? 0 : resoDichiaratoBollePrecedenti;
	}

	public Integer getVendutoBollePrecedenti() {
		return (getFornitoBollePrecedenti() + getMancanzeBollePrecedenti()) - getResoDichiaratoBollePrecedenti();
	}
	
	public Integer getCodInizioQuotidiano() {
		return codInizioQuotidiano == null ? 0 : codInizioQuotidiano;
	}
	
	@Override
	public Integer getVariazione() {
		return 0;
	}
	
	@Override
	public void accept(VisitorDto visitor) {
		visitor.visit(this);
	}
}
