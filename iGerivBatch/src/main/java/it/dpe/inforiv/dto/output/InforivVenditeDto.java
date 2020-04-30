package it.dpe.inforiv.dto.output;

import it.dpe.igeriv.util.DateUtilities;
import it.dpe.inforiv.dto.input.InforivBaseDto;
import it.dpe.inforiv.dto.input.InforivDto;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatDecimal;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatNumber;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;
import com.ancientprogramming.fixedformat4j.annotation.Sign;

/**
 * @author romanom
 */
@Component("InforivVenditeDto")
@Record
public class InforivVenditeDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	public static final int lineLength = 101;
	private String tipoRecord;
	private Integer codFiegDl;
	private Integer codEdicola;
	private Integer idUnitaVendita;
	private Date dataVendita;
	private Integer oraMinutoVendita;
	private Float totaleVenditaQuotidianiPeriodici;
	private String idProdotto;
	private Integer copieVendute;
	private Float prezzoVenduto;
	private String descrizione;
	private Long codVendita;
	private BigDecimal importoTotale;
	private BigDecimal prezzoCopertina;
	private String idtnTrascodifica;
	
	private BigDecimal campoNonUtilizzato; 
	
	
	
	@Field(offset = 1, length = 2)
	public String getTipoRecord() {
		return tipoRecord;
	}

	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}
	
	@Field(offset = 3, length = 3,  align = Align.RIGHT, paddingChar = '0')
	public Integer getCodFiegDl() {
		return codFiegDl;
	}

	public void setCodFiegDl(Integer codFiegDl) {
		this.codFiegDl = codFiegDl;
	}

	@Field(offset = 6, length = 4,  align = Align.RIGHT, paddingChar = '0')
	public Integer getCodEdicola() {
		return codEdicola;
	}

	public void setCodEdicola(Integer codEdicola) {
		this.codEdicola = codEdicola;
	}
	
	@Field(offset = 10, length = 9,  align = Align.RIGHT, paddingChar = '0')
	public Integer getIdUnitaVendita() {
		if (codVendita != null) {
			return new Integer(codVendita.toString());
		}
		return idUnitaVendita;
	}

	public void setIdUnitaVendita(Integer idUnitaVendita) {
		this.idUnitaVendita = idUnitaVendita;
	}
	
	@Field(offset = 19, length = 8)
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataVendita() {
		return dataVendita;
	}

	public void setDataVendita(Date dataVendita) {
		this.dataVendita = dataVendita;
	}
	
	@Field(offset = 27, length = 4,  align = Align.RIGHT, paddingChar = '0')
	public Integer getOraMinutoVendita() {
		if (dataVendita != null) {
			return new Integer(DateUtilities.getTimestampAsString(dataVendita, DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS).substring(8, 12));
		}
		return oraMinutoVendita;
	}

	public void setOraMinutoVendita(Integer oraMinutoVendita) {
		this.oraMinutoVendita = oraMinutoVendita;
	}
	
	//CAMPO NON UTILIZZATO
	@Field(offset = 31, length = 9, paddingChar = '0', align = Align.RIGHT)
	@FixedFormatDecimal(decimals = 2)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')	
	public BigDecimal getCampoNonUtilizzato() {
		return campoNonUtilizzato;
	}
	//CAMPO NON UTILIZZATO
	public void setCampoNonUtilizzato(BigDecimal campoNonUtilizzato) {
		this.campoNonUtilizzato = campoNonUtilizzato;
	}

	@Field(offset = 40, length = 9, paddingChar = '0', align = Align.RIGHT)
	@FixedFormatDecimal(decimals = 2)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getTotaleVenditaQuotidianiPeriodici() {
		if (importoTotale != null) {
			return importoTotale.floatValue();
		}
		return totaleVenditaQuotidianiPeriodici;
	}

	public void setTotaleVenditaQuotidianiPeriodici(Float totaleVenditaQuotidianiPeriodici) {
		this.totaleVenditaQuotidianiPeriodici = totaleVenditaQuotidianiPeriodici;
	}
	
	@Field(offset = 49, length = 18)
	public String getIdProdotto() {
		return (idtnTrascodifica != null) ? idtnTrascodifica.toString() : idProdotto;
	}

	public void setIdProdotto(String idProdotto) {
		this.idProdotto = idProdotto;
	}
	
	@Field(offset = 67, length = 8, align = Align.RIGHT, paddingChar = '0')
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Integer getCopieVendute() {
		return copieVendute;
	}

	public void setCopieVendute(Integer copieVendute) {
		this.copieVendute = copieVendute;
	}
	
	@Field(offset = 75, length = 7, align = Align.RIGHT, paddingChar = '0')
	@FixedFormatDecimal(decimals = 2)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getPrezzoVenduto() {
		if (prezzoCopertina != null) {
			return prezzoCopertina.floatValue();
		}
		return prezzoVenduto;
	}

	public void setPrezzoVenduto(Float prezzoVenduto) {
		this.prezzoVenduto = prezzoVenduto;
	}
	
	@Field(offset = 82, length = 20)
	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Long getCodVendita() {
		return codVendita;
	}

	public void setCodVendita(Long codVendita) {
		this.codVendita = codVendita;
	}

	public BigDecimal getImportoTotale() {
		return importoTotale;
	}

	public void setImportoTotale(BigDecimal importoTotale) {
		this.importoTotale = importoTotale;
	}

	public BigDecimal getPrezzoCopertina() {
		return prezzoCopertina;
	}

	public void setPrezzoCopertina(BigDecimal prezzoCopertina) {
		this.prezzoCopertina = prezzoCopertina;
	}

	public String getIdtnTrascodifica() {
		return idtnTrascodifica;
	}

	public void setIdtnTrascodifica(String idtnTrascodifica) {
		this.idtnTrascodifica = idtnTrascodifica;
	}

	public Float getImporto() {
		return getPrezzoVenduto() != null && getCopieVendute() != null ? getPrezzoVenduto() * ((float) getCopieVendute().intValue()) : 0f;
	}
	
}
