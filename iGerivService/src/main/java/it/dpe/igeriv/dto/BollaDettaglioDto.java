package it.dpe.igeriv.dto;

import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.NumberUtils;
import it.dpe.igeriv.vo.pk.IBollaDettaglioPk;
import it.dpe.igeriv.vo.pk.PeriodicitaPk;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper=false)
public class BollaDettaglioDto extends BaseDto implements BollaDettaglio {
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
	private Integer quantitaConsegnata;
	@Getter(AccessLevel.NONE)
	private BigDecimal importo;
	@Getter(AccessLevel.NONE)
	private Integer spunta;
	@Getter(AccessLevel.NONE)
	private Integer differenze;
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
	private String noteByCpu;
	private Timestamp dataFatturazionePrevista;
	private String motivoResaRespinta;
	private Integer quantitaSpuntata;
	@Getter(AccessLevel.NONE)
	private Integer confermaDifferenze;
	private boolean isSpuntaBolla;
	@Getter(AccessLevel.NONE)
	private Boolean contoDeposito;
	private Boolean contoDepositoInforete;
	private BigDecimal scontoInforete;
	private Timestamp dataUscita;
	@Getter(AccessLevel.NONE)
	private Integer codInizioQuotidiano;
	private Integer idMessaggioIdtn;
	private Boolean aggiuntaFuoriCompetenza;
	@Getter(AccessLevel.NONE)
	private Integer variazione;
	@Getter(AccessLevel.NONE)
	private Boolean pubblicazioneNonUscita;
	
	/* (non-Javadoc)
	 * @see it.dpe.igeriv.dto.BollaDettaglio#getNumeroPubblicazione()
	 */
	@Override
	public String getNumeroPubblicazione() {
		return numeroPubblicazione != null ? numeroPubblicazione.trim() : numeroPubblicazione;
	}

	/* (non-Javadoc)
	 * @see it.dpe.igeriv.dto.BollaDettaglio#getImporto()
	 */
	@Override
	public BigDecimal getImporto() {
		BigDecimal bigDecimal = importo = (indicatoreValorizzare == IGerivConstants.INDICATORE_VALORIZZARE && prezzoNetto != null && quantitaConsegnata != null) ? prezzoNetto.multiply(new BigDecimal(quantitaConsegnata)) : new BigDecimal(0);
		return bigDecimal;
	}
	
	/* (non-Javadoc)
	 * @see it.dpe.igeriv.dto.BollaDettaglio#getImportoLordo()
	 */
	@Override
	public BigDecimal getImportoLordo() {
		return  (indicatoreValorizzare == IGerivConstants.INDICATORE_VALORIZZARE && prezzoLordo != null && quantitaConsegnata != null) ? prezzoLordo.multiply(new BigDecimal(quantitaConsegnata)) : new BigDecimal(0);
	}
	
	/* (non-Javadoc)
	 * @see it.dpe.igeriv.dto.BollaDettaglio#getDifferenze()
	 */
	@Override
	public Integer getDifferenze() {
		if (isSpuntaBolla) {
			if (getConfermaDifferenze() != null && getConfermaDifferenze() > 0) {
				return getConfermaDifferenze();
			} else if (getQuantitaConsegnata() != null && (getConfermaDifferenze() == null || getConfermaDifferenze().equals(0))) {
				return -getQuantitaConsegnata();
			}
		}
		return (differenze != null && getSpunta().equals(0) && differenze.equals(0)) ? null : differenze;
	}

	/* (non-Javadoc)
	 * @see it.dpe.igeriv.dto.BollaDettaglio#getTipo()
	 */
	@Override
	public int getTipo() {
		return IGerivConstants.TIPO_BOLLA_DETTAGLIO;
	}

	/* (non-Javadoc)
	 * @see it.dpe.igeriv.dto.BollaDettaglio#getConfermaDifferenze()
	 */
	@Override
	public Integer getConfermaDifferenze() {
		if (getQuantitaSpuntata() != null && getQuantitaSpuntata() > 0) {
			return getQuantitaSpuntata() - getQuantitaConsegnata();
		} else if (confermaDifferenze != null && confermaDifferenze.equals(0)) {
			return null;
		}
		return confermaDifferenze;
	}

	public Boolean getContoDeposito() {
		return contoDeposito == null ? false : contoDeposito;
	}

	public String getScontoFormat() {
		return (getSconto() != null) ? NumberUtils.formatNumber(getSconto()) : "";
	}
	
	public String getScontoInforeteFormat() {
		return (getScontoInforete() != null) ? NumberUtils.formatNumber(getScontoInforete()) : "";
	}
	
	public Integer getSpunta() {
		return spunta == null ? 0 : spunta;
	}
	
	public Integer getVariazione() {
		return variazione == null ? 0 : variazione;
	}
	
	public Integer getCodInizioQuotidiano() {
		return codInizioQuotidiano == null ? 0 : codInizioQuotidiano;
	}
	
	public Boolean getPubblicazioneNonUscita() {
		return pubblicazioneNonUscita == null ? false : pubblicazioneNonUscita;
	}
	
	/* (non-Javadoc)
	 * @see it.dpe.igeriv.dto.BollaDettaglio#accept(it.dpe.igeriv.dto.VisitorDto)
	 */
	@Override
	public void accept(VisitorDto visitor) {
		visitor.visit(this);
	}

}
