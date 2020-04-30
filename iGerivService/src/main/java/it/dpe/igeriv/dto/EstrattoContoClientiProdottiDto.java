package it.dpe.igeriv.dto;

import it.dpe.igeriv.exchange.adapter.BigDecimalAdapter;
import it.dpe.igeriv.exchange.adapter.DateAdapter;
import it.dpe.igeriv.exchange.adapter.FloatAdapter;
import it.dpe.igeriv.exchange.adapter.IntegerAdapter;
import it.dpe.igeriv.exchange.adapter.LongAdapter;
import it.dpe.igeriv.exchange.adapter.StringAdapter;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import com.google.common.base.Strings;

@Data
@EqualsAndHashCode(callSuper=false)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "consegnaProdotto")
public class EstrattoContoClientiProdottiDto extends BaseDto implements EstrattoContoClienti {
	private static final long serialVersionUID = 1L;
	
	@XmlJavaTypeAdapter(LongAdapter.class)
	private Long codCliente;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String nome;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String cognome;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	@Getter(AccessLevel.NONE)
	private String titolo;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String telefono;
	
	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	@Getter(AccessLevel.NONE)
	private BigDecimal prezzo;
	
	@XmlJavaTypeAdapter(FloatAdapter.class)
	private Float prezzoF;
	
	@XmlJavaTypeAdapter(LongAdapter.class)
	private Long copie;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String tipoLocalita;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String indirizzo;
	
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	private Integer numeroCivico;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String estensione;	
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String localitaDesc;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String cap;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String provinciaDesc;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String piva;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String codiceFiscale;
	
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Timestamp dataCompetenzaEstrattoContoClienti;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String banca;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String contoCorrente;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String iban;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String tipoPagamento;
	
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	private Integer giorniScadenzaPagamento;

	@XmlJavaTypeAdapter(IntegerAdapter.class)
	private Integer sconto;
	
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	private Integer scontoPne;

	@XmlJavaTypeAdapter(BigDecimalAdapter.class)
	private BigDecimal marcaBollo;
	
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	private Integer numeroEstrattoConto;
	
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String descrizioneCausaleIva;
	
	@XmlTransient
	private Integer aliquota;

	@XmlTransient
	private Date dataDocumento;
	
	@Override
	public String getTitolo() {
		return Strings.isNullOrEmpty(titolo) ? "" : titolo.toUpperCase().replaceAll(IGerivConstants.EURO_SIGN_DECIMAL, IGerivConstants.EURO_SIGN_ASCII);
	}

	@Override
	public BigDecimal getPrezzo() {
		return getPrezzoF() != null ? new BigDecimal(getPrezzoF()) : prezzo;
	}

	@Override
	public String getNomeCognome() {
		return getNome() + " " + (Strings.isNullOrEmpty(getCognome()) ? "" : getCognome());
	}

	@Override
	public String getIndirizzoCompleto() {
		String tipoLoc = Strings.isNullOrEmpty(getTipoLocalita()) ? "" : getTipoLocalita().trim() + " ";
		String indirizzo = Strings.isNullOrEmpty(getIndirizzo()) ? "" : getIndirizzo().trim() + " ";
		String ext = Strings.isNullOrEmpty(getEstensione()) ? "" : " / " + getEstensione() + " ";
		String num = getNumeroCivico() == null ? "" : ", " + getNumeroCivico() + ext + " ";
		String cap = Strings.isNullOrEmpty(getCap()) ? "" : getCap().trim() + " ";
		String localita = Strings.isNullOrEmpty(getLocalitaDesc()) ? "" : (Strings.isNullOrEmpty(cap) ? "" : " - ") + getLocalitaDesc().trim() + " ";
		String prov = Strings.isNullOrEmpty(getProvinciaDesc()) ? "" : " (" + getProvinciaDesc().trim() + ") ";
		return (Strings.isNullOrEmpty(indirizzo) ? "" : tipoLoc + " " + indirizzo + num + " \n") + cap + localita + prov;
	}

	@Override
	public String getRagSocCliente() {
		return ((getNomeCognome() != null) ? getNomeCognome().replaceAll("&nbsp;", " ") : "");
	}

	@Override
	public String getIndirizzoCliente() {
		return (!Strings.isNullOrEmpty(getIndirizzoCompleto()) ? getIndirizzoCompleto().replaceAll("&nbsp;", " ") : "");
	}

	@Override
	public String getDatiFiscali() {
		return IGerivMessageBundle.get("dpe.piva.cod.fiscale") + ": " + (Strings.isNullOrEmpty(getPiva()) ? Strings.isNullOrEmpty(getCodiceFiscale()) ? "" : getCodiceFiscale() : getPiva());
	}

	@Override
	public String getPartitaIva() {
		return IGerivMessageBundle.get("dpe.piva.cod.fiscale") + ": " + (!Strings.isNullOrEmpty(getDatiFiscali()) ? getDatiFiscali().replaceAll("&nbsp;", " ") : "");
	}

	@Override
	public String getOrder() {
		return getCodCliente() + "_" + getAliquota() + "_" + getNomeCognome() + "_" + getTipoProdotto() + "_" + getTitolo();
	}
	
	@Override
	public String getDatiCliente() {
		String piva = (Strings.isNullOrEmpty(getPiva()) ? "" : "\n" + IGerivMessageBundle.get("dpe.piva") + ": " + getPiva());
		String cf = (Strings.isNullOrEmpty(getCodiceFiscale()) ? "" : "\n" + IGerivMessageBundle.get("dpe.codice.fiscale.3") + ": " + getCodiceFiscale());
		return getNomeCognome() + "\n" + getIndirizzoCompleto() + piva + cf;
	}
	
	@Override
	public String getDatiBancari() {
		return !Strings.isNullOrEmpty(getBanca()) ? IGerivMessageBundle.get("igeriv.banca.appoggio") + ": " + getBanca() + "   " + IGerivMessageBundle.get("igeriv.cc.short") + ": " + getContoCorrente() + "\n" + IGerivMessageBundle.get("igeriv.iban") + ": " + getIban() : "";
	}
	
	@Override
	public Integer getTipoProdotto() {
		return IGerivConstants.TIPO_PRODOTTO_NON_EDITORIALE;
	}
	
	@Override
	public Integer getGiorniScadenzaPagamento() {
		return giorniScadenzaPagamento == null ? 0 : giorniScadenzaPagamento;
	}

	@Override
	public String getDataScadenzaPagamento() {
		if (getGiorniScadenzaPagamento() != null && getGiorniScadenzaPagamento() > 0 && getDataDocumento() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(getDataDocumento());
			cal.add(Calendar.DAY_OF_YEAR, getGiorniScadenzaPagamento());
			return DateUtilities.getTimestampAsStringExport(cal.getTime(), DateUtilities.FORMATO_DATA_SLASH);
		}
		return "";
	}

	@Override
	public BigDecimal getImportoPne() {
		if (getImportoLordoPne() != null && getAliquota() != null) {
			return getImportoLordoPne().divide(new BigDecimal(getAliquota()).divide(new BigDecimal(100)).add(new BigDecimal(1)), 2, RoundingMode.HALF_UP);
		}
		return new BigDecimal(0);
	}
	
	@Override
	public BigDecimal getImportoLordoPne() {
		return (getPrezzo() != null && getCopie() != null) ? getPrezzo().multiply(new BigDecimal(getCopie())) : null;
	}
	
	@Override
	public String getSortCriteria() {
		return getTitolo() + "|" + getTipoProdotto().toString() + "|" + getPrezzo().toString();
	}
	
	@Override
	public BigDecimal getImporto() {
		return BigDecimal.ZERO;
	}
	
}
