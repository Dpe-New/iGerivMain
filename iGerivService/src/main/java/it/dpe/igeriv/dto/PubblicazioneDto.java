package it.dpe.igeriv.dto;

import it.dpe.igeriv.calc.Calculator;
import it.dpe.igeriv.calc.impl.GiacenzaCalculator;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.NumberUtils;
import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.igeriv.vo.pk.BasePk;
import it.dpe.igeriv.vo.pk.PeriodicitaPk;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper=false)
public class PubblicazioneDto extends BaseDto implements PubblicazioneFornito {
	private static final long serialVersionUID = 1L;
	private Integer coddl;
	private Integer crivw;
	private String titolo;
	@Getter(AccessLevel.NONE)
	private String sottoTitolo;
	private String barcode;
	
	@Getter
	@Setter
	private Timestamp dataCorrezioneBarcode;
	@Getter
	@Setter
	private Integer codEdicolaCorrezioneBarcode;
	@Getter
	@Setter
	private String codiceBarrePrecedente;
	
	@Getter(AccessLevel.NONE)
	private Integer codicePubblicazione;
	@Getter(AccessLevel.NONE)
	private String numeroCopertina;
	private String argomento;
	private Integer tipo;
	@Getter(AccessLevel.NONE)
	private Integer periodicitaInt;
	private String periodicita;
	@Getter(AccessLevel.NONE)
	private String periodicitaPk;
	private PeriodicitaPk periodicitaPkObj;
	@Getter(AccessLevel.NONE)
	private BigDecimal prezzoCopertina;
	@Getter(AccessLevel.NONE)
	private BigDecimal prezzoCopertinaVendita;
	private BigDecimal prezzoEdicola;
	private Timestamp dataUscita;
	private Timestamp dataRichiamoResa;
	private String tipoRichiamoResa;
	private String immagine;
	private String imgMiniaturaName;
	private Integer idtn;
	private Integer fornitoBolla;
	private Integer fornitoFondoBolla;
	private Integer fornitoRifornimenti;
	private Integer resoBolla;
	private Integer resoFuoriVoce;
	private Integer resoRichiamoPersonalizzato;
	@Getter(AccessLevel.NONE)
	private Integer resoRiscontrato;
	private Long vendite;
	@Getter(AccessLevel.NONE)
	private Integer respinto;
	private Integer progressivo;
	private Integer codInizioQuotidiano;
	private Integer codFineQuotidiano;
	private Integer numCopertinePrecedentiPerRifornimenti;
	private Integer giorniValiditaRichiesteRifornimento;
	private Integer maxStatisticaVisualizzare;
	private Integer giancezaPressoDl;
	private Integer contoDeposito;
	@Getter(AccessLevel.NONE)
	private Integer quantita;
	@Getter(AccessLevel.NONE)
	private Boolean prodottoNonEditoriale;
	private Long idProdotto;
	private Integer fornitoStorico;
	private Integer resoStorico;
	@Getter(AccessLevel.NONE)
	private Integer giacenzaIniziale;
	private String note;
	private String noteByCpu;

	@Getter(AccessLevel.NONE)
	private Integer quantitaCopieContoDeposito;
	private Long fornitoSP;
	private Boolean pagato;
	private Timestamp dataVendita;
	@Getter(AccessLevel.NONE)
	private Integer provenienzaConto;
	private Timestamp dataEstrattoConto;
	private Timestamp dataFattura;
	private Float prezzo;
	private BasePk pk;
	@Getter(AccessLevel.NONE)
	private BigDecimal importoTotale;
	private String dirAlias;
	@Getter(AccessLevel.NONE)
	private Boolean prodottoDl;
	private Long giacenzaSP;
	private Calculator giacenzaCalculator;
	private Integer codMotivoRespinto;
	@Getter(AccessLevel.NONE)
	private Integer numGiorniDaDataUscitaPerRichiestaRifornimento;
	@Getter(AccessLevel.NONE)
	private Boolean richiedereRifornimenti;
	@Getter(AccessLevel.NONE)
	private Boolean puoRichiedereRifornimenti;
	@Getter(AccessLevel.NONE)
	private Integer codiceInforete;
	@Getter(AccessLevel.NONE)
	private String numeroCopertinaInforete;
	private Integer aliquota;
	
	@Getter
	@Setter
	private String barcode_new;
	

	private String descrEditoreDlNomeA;
	

	public String getDescrEditoreDlNomeA() {
		return (descrEditoreDlNomeA == null) ? "" : descrEditoreDlNomeA;
	}
	
	public String getSottoTitolo() {
		return (sottoTitolo == null) ? "" : sottoTitolo;
	}

	public String getPeriodicitaStr() {
		return (getTipo() != null && getPeriodicitaInt() != null) ? getTipo() + "|" + getPeriodicitaInt() : "";
	}
	
	public String getPeriodicitaPk() {
		return periodicitaPk == null ? ((periodicitaPkObj != null) ? periodicitaPkObj.toString() : null) : periodicitaPk;
	}

	public BigDecimal getPrezzoCopertina() {
		return prezzo != null ? new BigDecimal(prezzo) : prezzoCopertina;
	}

	public BigDecimal getPrezzoCopertinaVendita() {
		return prezzoCopertinaVendita == null ? getPrezzoCopertina() : prezzoCopertinaVendita;
	}

	public String getDataUscitaFormat() {
		return (dataUscita != null) ? DateUtilities.getTimestampAsString(dataUscita, DateUtilities.FORMATO_DATA_SLASH) : "";
	}

	public Integer getResoRiscontrato() {
		int resoStorico = (getResoStorico() != null) ? getResoStorico() : 0;
		int rr = resoRiscontrato != null ? resoRiscontrato : 0;
		return rr + resoStorico;
	}
	
	@Override
	public Integer getFornito() {
		int fornitoBollaInt = (getFornitoBolla() != null) ? getFornitoBolla().intValue() : 0;
		int fornitoFondoBollaInt = (getFornitoFondoBolla() != null) ? getFornitoFondoBolla().intValue() : 0;
		int fornitoRifornimentiInt = (getFornitoRifornimenti() != null) ? getFornitoRifornimenti().intValue() : 0;
		int fornitoStorico = (getFornitoStorico() != null) ? getFornitoStorico().intValue() : 0;
		return (fornitoBollaInt + fornitoFondoBollaInt + fornitoRifornimentiInt + fornitoStorico);
	}

	public Integer getReso() {
		int resoBollaInt = (getResoBolla() != null) ? getResoBolla().intValue() : 0;
		int resoFuoriVoceInt = (getResoFuoriVoce() != null) ? getResoFuoriVoce().intValue() : 0;
		int resoRichiamoPersonalizzato = (getResoRichiamoPersonalizzato() != null) ? getResoRichiamoPersonalizzato().intValue() : 0;
		if (getResoBolla() == null && getResoFuoriVoce() == null && getResoRichiamoPersonalizzato() == null) {
			return null;
		}
		return (resoBollaInt + resoFuoriVoceInt + resoRichiamoPersonalizzato);
	}

	public Integer getVenduto() {
		int fornitoInt = (getFornito() != null) ? getFornito().intValue() : 0;
		int resoInt = (getReso() != null) ? getReso().intValue() : 0;
		if (getReso() == null) {
			return 0;
		}
		return fornitoInt - resoInt;
	}
	
	public Integer getRespinto() {
		return respinto == null ? 0 : respinto;
	}
	
	public Number getGiacenza() {
		return getGiacenzaCalculator() == null ? SpringContextManager.getSpringContext().getBean("GiacenzaCalculator", GiacenzaCalculator.class).getResult(this) : getGiacenzaCalculator().getResult(this);
	}
	
	public String getPrezzoCopertinaFormat() {
		return (prezzoCopertina != null) ? NumberUtils.formatNumber(prezzoCopertina) : "";
	}
	
	public String getPrezzoEdicolaFormat() {
		return (prezzoEdicola != null) ? NumberUtils.formatNumber(prezzoEdicola) : "";
	}
	
	public String getImportoFormat() {
		return (prezzoCopertina != null && quantita != null) ? NumberUtils.formatNumber(prezzoCopertina.multiply(new BigDecimal(quantita))) : "";
	}

	public Integer getQuantita() {
		return quantita == null ? 1 : quantita;
	}

	public Boolean getProdottoNonEditoriale() {
		return prodottoNonEditoriale == null ? false : prodottoNonEditoriale;
	}

	public Integer getGiacenzaIniziale() {
		return giacenzaIniziale == null ? 0 : giacenzaIniziale;
	}

	public Integer getQuantitaCopieContoDeposito() {
		return quantitaCopieContoDeposito == null ? 0 : quantitaCopieContoDeposito;
	}

	public Integer getProvenienzaConto() {
		return provenienzaConto == null ? IGerivConstants.PROVENIENZA_CONTO_VENDITE_DETTAGLIO : provenienzaConto;
	}

	public Timestamp getDataECFattura() {
		return getDataEstrattoConto() != null ? getDataEstrattoConto() : getDataFattura();
	}
	
	public String getTipoRitiro() {
		return getProvenienzaConto().equals(IGerivConstants.PROVENIENZA_CONTO_VENDITE_DETTAGLIO) ? IGerivMessageBundle.get("igeriv.tipo.pgto.edicola") : IGerivMessageBundle.get("igeriv.evasione.ordini");
	}
	
	public BigDecimal getImportoTotale() {
		return importoTotale == null ? ((getPrezzo() != null && getQuantita() != null) ? new BigDecimal(getPrezzo() * getQuantita()) : null) : importoTotale;
	}

	public String getIsContoDeposito() {
		return getQuantitaCopieContoDeposito() > 0 ? IGerivMessageBundle.get("igeriv.si") : IGerivMessageBundle.get("igeriv.no");
	}
	
	public Boolean getProdottoDl() {
		return prodottoDl == null ? false : prodottoDl;
	}

	public Integer getNumGiorniDaDataUscitaPerRichiestaRifornimento() {
		return numGiorniDaDataUscitaPerRichiestaRifornimento == null ? 0 : numGiorniDaDataUscitaPerRichiestaRifornimento;
	}

	public Boolean getRichiedereRifornimenti() {
		return richiedereRifornimenti == null ? false : richiedereRifornimenti;
	}

	public Boolean getPuoRichiedereRifornimenti() {
		return puoRichiedereRifornimenti == null ? false : puoRichiedereRifornimenti;
	}

	public String getIsScaduta() {
		return codMotivoRespinto != null && codMotivoRespinto.equals(IGerivConstants.COD_MOTIVO_RESPINTO_SCADUTO) ? IGerivMessageBundle.get("igeriv.si") : IGerivMessageBundle.get("igeriv.no");
	}
	
	public BigDecimal getImporto() {
		if (getPrezzoEdicola() != null && getFornitoSP() != null) {
			return new BigDecimal(getFornitoSP()).multiply(getPrezzoEdicola());
		}
		return new BigDecimal(0);
	}
	
	public Integer getCodiceInforete() {
		return codiceInforete == null ? 0 : codiceInforete;
	}
	
	public String getNumeroCopertinaInforete() {
		return numeroCopertinaInforete == null ? "" : numeroCopertinaInforete;
	}
	
	public Integer getCodicePubblicazione() {
		return codicePubblicazione == null ? 0 : codicePubblicazione;
	}

	public String getNumeroCopertina() {
		return numeroCopertina == null ? "" : numeroCopertina;
	}
	
	public Integer getPeriodicitaInt() {
		return periodicitaInt == null ? (getPeriodicitaPk() != null ? new Integer(getPeriodicitaPk().split("\\|")[1]) : null) : periodicitaInt;
	}
	
	@Override
	public String getCodiceInforeteNumeroCopertinaInforete() {
		return getCodiceInforete().toString() + getNumeroCopertinaInforete();
	}
	
	@Override
	public String getCodicePubblicazioneeNumeroCopertina() {
		return getCodicePubblicazione().toString() + getNumeroCopertina();
	}
	
	public Map<Integer, Integer> getMapCiqCfq() {
		if (((getPeriodicitaInt() != null && getPeriodicitaInt().equals(IGerivConstants.COD_PERIODICITA_QUOTIDIANO))
				|| (getPeriodicitaPk() != null && getPeriodicitaPk().split("\\|")[1].equals("" + IGerivConstants.COD_PERIODICITA_QUOTIDIANO))
				|| (getPeriodicita() != null && getPeriodicita().equals("" + IGerivConstants.COD_PERIODICITA_QUOTIDIANO)))
				&& getCodInizioQuotidiano() != null && getCodFineQuotidiano() != null) {
			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			int cpu = getCodInizioQuotidiano().intValue();
			if (getCodFineQuotidiano() != null && getCodFineQuotidiano().intValue() != cpu) {
				map.put(Calendar.MONDAY, cpu);
				map.put(Calendar.TUESDAY, ++cpu);
				map.put(Calendar.WEDNESDAY, ++cpu);
				map.put(Calendar.THURSDAY, ++cpu);
				map.put(Calendar.FRIDAY, ++cpu);
				map.put(Calendar.SATURDAY, ++cpu);
				map.put(Calendar.SUNDAY, ++cpu);
			} else {
				map.put(Calendar.MONDAY, cpu);
				map.put(Calendar.TUESDAY, cpu);
				map.put(Calendar.WEDNESDAY, cpu);
				map.put(Calendar.THURSDAY, cpu);
				map.put(Calendar.FRIDAY, cpu);
				map.put(Calendar.SATURDAY, cpu);
				map.put(Calendar.SUNDAY, cpu);
			}
			return map;
		}
		return null;
	}
	
	@Override
	public void accept(VisitorDto visitor) {
		visitor.visit(this);
	}
	
	
	

}
