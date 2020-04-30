package it.dpe.igeriv.dto;

import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import com.google.common.base.Strings;

@Data
@EqualsAndHashCode(callSuper=false)
@SuppressWarnings("unused")
public class VenditeParamDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private String inputText;
	private String idConto;
	private String barcode;
	private String progressivo;
	private String idtn;
	private String importo;
	private String titolo;
	private String sottoTitolo;
	private String numeroCopertina;
	private String dataUscita;
	private String dataDa;
	private String dataA;
	private BigDecimal importoBigDecimal;
	private Double importoNetto;
	@Getter(AccessLevel.NONE)
	private Integer copie;
	private Integer codFiegDlPubblicazione;
	private Integer codFiegDl;
	private Integer idEdicola;
	private Integer[] arrCodFiegDl;
	private Integer[] arrIdEdicola;
	private Integer codEdicolaDl;
	private String codUtente;
	private Integer idEditore;
	private Integer idProdotto;
	private String operation;
	private Integer codTipologiaMinicard;
	private Integer quantita;
	@Getter(AccessLevel.NONE)
	private Boolean prodottoNonEditoriale;
	private Long codProdottoNonEditoriale;
	@Getter(AccessLevel.NONE)
	private Boolean mostraTutteUscite;
	@Getter(AccessLevel.NONE)
	private Boolean ricercaProdottiVari;
	private String codiceVenditaReteEdicole;
	private Timestamp dataStorico;
	private boolean isMultiDl;
	@Getter(AccessLevel.NONE)
	private Boolean abilitataCorrezioneBarcode;
	@Getter(AccessLevel.NONE)
	private Boolean soloPubblicazioniBarcodeNullo;
	private Boolean findCopieInContoDeposito;
	private Boolean hasEdicoleAutorizzateAggiornaBarcode;
	@Getter(AccessLevel.NONE)
	private BigDecimal aliquota;
	private Long codCliente;
	private String datiCliente;
	private BigDecimal importoIva0;
	private BigDecimal importoIva0_74;
	private BigDecimal importoIva4;
	private BigDecimal importoIva10;
	private BigDecimal importoIva21;
	private BigDecimal totaleNetto0;
	private BigDecimal totaleNetto0_74;
	private BigDecimal totaleNetto4;
	private BigDecimal totaleNetto10;
	private BigDecimal totaleNetto21;
	private BigDecimal totaleGenerale;
	@Getter(AccessLevel.NONE)
	private Integer sconto;
	@Getter(AccessLevel.NONE)
	private Integer scontoPne;
	private String datiBancari;
	private String tipoPagamento;
	private String dataScadenzaPagamento;
	private Integer giorniScadenzaPagamento;
	private Timestamp dataCompetenzaEstrattoContoClienti;
	private String numeroDocumento;
	private Integer gruppoSconto;
	@Getter(AccessLevel.NONE)
	private Boolean findPrezzoEdicola;
	@Getter(AccessLevel.NONE)
	private Boolean findGiacenza;
	private Date dataDocumento;
	private Boolean dlInforiv;
	private String token;
	private String edizione;
	private String pwd;
	private String causaleEsenzione;
	private String dataBolla;
	private String tipoBolla;
	private Boolean hasRichiestaRifornimentoNelleVendite;
	private Boolean venditeEsauritoControlloGiacenzaDL;
	//Ticket 0000264
	private Boolean harModalitaRicercaContenuto;
	
	public BigDecimal getImportoBigDecimal() {
		return !Strings.isNullOrEmpty(importo) ? new BigDecimal(importo) : BigDecimal.ZERO;
	}

	public Integer getCopie() {
		return (copie == null) ? 1 : copie;
	}

	public Boolean getProdottoNonEditoriale() {
		return prodottoNonEditoriale == null ? false : prodottoNonEditoriale;
	}

	public Boolean getMostraTutteUscite() {
		return mostraTutteUscite == null ? false : mostraTutteUscite;
	}

	public Boolean getRicercaProdottiVari() {
		return ricercaProdottiVari == null ? false : ricercaProdottiVari;
	}
	
	public Boolean getAbilitataCorrezioneBarcode() {
		return abilitataCorrezioneBarcode == null ? false : abilitataCorrezioneBarcode;
	}

	public Boolean getSoloPubblicazioniBarcodeNullo() {
		return soloPubblicazioniBarcodeNullo == null ? false : soloPubblicazioniBarcodeNullo;
	}

	public BigDecimal getAliquota() {
		return aliquota == null ? BigDecimal.ZERO : aliquota;
	}

	public Integer getTipoProdotto() {
		if (getProdottoNonEditoriale() && getAliquota().intValue() > 0) {
			return IGerivConstants.TIPO_PRODOTTO_NON_EDITORIALE_CON_IVA;
		} else if (getProdottoNonEditoriale() && getAliquota().equals(0)) {
			return IGerivConstants.TIPO_PRODOTTO_NON_EDITORIALE_IVA_ESENTE;
		}
		return IGerivConstants.TIPO_PRODOTTO_EDITORIALE;
	}
	
	public String getAliquotaF() {
		return getAliquota() + "%";
	}
	
	public String getDataScadenzaPagamento() {
		if (getGiorniScadenzaPagamento() != null && getGiorniScadenzaPagamento() > 0 && getDataDocumento() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(getDataDocumento());
			cal.add(Calendar.DAY_OF_YEAR, getGiorniScadenzaPagamento());
			return DateUtilities.getTimestampAsStringExport(cal.getTime(), DateUtilities.FORMATO_DATA_SLASH);
		}
		return "";
	}

	public Boolean getFindPrezzoEdicola() {
		return findPrezzoEdicola == null ? false : findPrezzoEdicola;
	}

	public Boolean getFindGiacenza() {
		return findGiacenza == null ? false : findGiacenza;
	}

	public BigDecimal getImportoNettoCalc() {
		if (getImportoBigDecimal() != null && getAliquota() != null) {
			return new BigDecimal(100).divide(new BigDecimal(100).add(getAliquota()), MathContext.DECIMAL128).multiply(getImportoBigDecimal()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		}
		return BigDecimal.ZERO;
	}
	
	public BigDecimal getImportoNettoCalcScontato() {
		BigDecimal importoNettoCalc = getImportoNettoCalc();
		return (new BigDecimal(100).subtract(getScontoPubProd())).divide(new BigDecimal(100), MathContext.DECIMAL128).multiply(importoNettoCalc);
	}
	
	public BigDecimal getImportoTotaleNettoPerRiga() {
		BigDecimal impNetto = getImportoNettoCalc();
		if (getCopie() != null && impNetto != null) {
			return new BigDecimal(getCopie()).multiply(impNetto).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		}
		return BigDecimal.ZERO;
	}
	
	public BigDecimal getImportoTotaleNettoPerRigaScontato() {
		BigDecimal importoTotaleNettoPerRiga = getImportoTotaleNettoPerRiga();
		return (new BigDecimal(100).subtract(getScontoPubProd())).divide(new BigDecimal(100), MathContext.DECIMAL128).multiply(importoTotaleNettoPerRiga);
	}
	
	public BigDecimal getImportoIvaCalc() {
		return getAliquota().divide(new BigDecimal(100), MathContext.DECIMAL128).multiply(getImportoTotaleNettoPerRigaScontato()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}
	
	public String getSortCriteria() {
		return getTitolo() + "|" + getTipoProdotto().toString() + "|" + getImporto();
	}
	
	public Integer getSconto() {
		return sconto != null ? sconto : 0;
	}
	
	public Integer getScontoPne() {
		return scontoPne != null ? scontoPne : 0;
	}
	
	public BigDecimal getScontoPubProd() {
		return getProdottoNonEditoriale() ? new BigDecimal(getScontoPne()) : new BigDecimal(getSconto());
	}
	
}
