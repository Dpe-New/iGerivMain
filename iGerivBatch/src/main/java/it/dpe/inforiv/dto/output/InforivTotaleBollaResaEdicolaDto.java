package it.dpe.inforiv.dto.output;

import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.inforiv.dto.input.InforivBaseDto;
import it.dpe.inforiv.dto.input.InforivDto;

import java.math.BigDecimal;
import java.text.MessageFormat;
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
@Component("InforivTotaleBollaResaEdicolaDto")
@Record
@SuppressWarnings("unused")
public class InforivTotaleBollaResaEdicolaDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private static final int lineLength = 142;
	private String tipoRecord;		
	private Integer codFiegDl;
	private Integer codEdicola;
	private Date dataBolla;
	private String tipoBolla;
	private Long numeroBolla;
	private String descrizioneBolla;
	private Integer totaleCopieResa;
	private Float totaleBollaResa;
	private Float valoreFuoriResa;
	private Float totaleFieg;
	private Float totlaeAltri;
	private Float valoreBuoni;
	private Integer numeroPacchi;
	private Integer numeroCeste;
	private Integer numeroCesteVuote;
	private String note;
	private Integer totaleCopieBollaResaDettaglio;
	private BigDecimal totaleBollaResaDettaglio;
	private Integer totaleCopieBollaFuoriResa;
	private BigDecimal totaleBollaFuoriResa;
	private Integer totaleCopieBollaResaDimenticata;
	private BigDecimal totaleBollaResaDimenticata;
	
	@Field(offset = 1, length = 2)
	public String getTipoRecord() {
		return tipoRecord;
	}

	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}
	
	@Field(offset = 3, length = 3, align = Align.RIGHT, paddingChar = '0')
	public Integer getCodFiegDl() {
		return codFiegDl;
	}

	public void setCodFiegDl(Integer codFiegDl) {
		this.codFiegDl = codFiegDl;
	}

	@Field(offset = 6, length = 4, align = Align.RIGHT, paddingChar = '0')
	public Integer getCodEdicola() {
		return codEdicola;
	}

	public void setCodEdicola(Integer codEdicola) {
		this.codEdicola = codEdicola;
	}

	@Field(offset = 10, length = 8)
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataBolla() {
		return dataBolla;
	}

	public void setDataBolla(Date dataBolla) {
		this.dataBolla = dataBolla;
	}
	
	@Field(offset = 18, length = 2)
	public String getTipoBolla() {
		return tipoBolla;
	}

	public void setTipoBolla(String tipoBolla) {
		this.tipoBolla = tipoBolla;
	}
	
	@Field(offset = 20, length = 10, align = Align.RIGHT, paddingChar = '0')
	public Long getNumeroBolla() {
		return numeroBolla;
	}

	public void setNumeroBolla(Long numeroBolla) {
		this.numeroBolla = numeroBolla;
	}
	
	@Field(offset = 30, length = 20)
	public String getDescrizioneBolla() {
		return descrizioneBolla;
	}

	public void setDescrizioneBolla(String descrizioneBolla) {
		this.descrizioneBolla = descrizioneBolla;
	}
	
	@Field(offset = 50, length = 8, align = Align.RIGHT, paddingChar = '0')
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Integer getTotaleCopieResa() {
		return getTotaleCopieBollaResaDettaglio() + getTotaleCopieBollaFuoriResa() + getTotaleCopieBollaResaDimenticata();
	}

	public void setTotaleCopieResa(Integer totaleCopieResa) {
		this.totaleCopieResa = totaleCopieResa;
	}
	
	@Field(offset = 58, length = 9, align = Align.RIGHT, paddingChar = '0')
	@FixedFormatDecimal(decimals = 2)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getTotaleBollaResa() {
		return getTotaleBollaResaDettaglio().add(getTotaleBollaFuoriResa()).add(getTotaleBollaResaDimenticata()).floatValue();
	}

	public void setTotaleBollaResa(Float totaleBollaResa) {
		this.totaleBollaResa = totaleBollaResa;
	}
	
	@Field(offset = 67, length = 9, align = Align.RIGHT, paddingChar = '0')
	@FixedFormatDecimal(decimals = 2)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getValoreFuoriResa() {
		return valoreFuoriResa;
	}

	public void setValoreFuoriResa(Float valoreFuoriResa) {
		this.valoreFuoriResa = valoreFuoriResa;
	}
	
	@Field(offset = 76, length = 9, align = Align.RIGHT, paddingChar = '0')
	@FixedFormatDecimal(decimals = 2)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getTotaleFieg() {
		return totaleFieg;
	}

	public void setTotaleFieg(Float totaleFieg) {
		this.totaleFieg = totaleFieg;
	}
	
	@Field(offset = 85, length = 9, align = Align.RIGHT, paddingChar = '0')
	@FixedFormatDecimal(decimals = 2)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getTotlaeAltri() {
		return totlaeAltri;
	}

	public void setTotlaeAltri(Float totlaeAltri) {
		this.totlaeAltri = totlaeAltri;
	}
	
	@Field(offset = 94, length = 8, paddingChar = '0', align = Align.RIGHT)
	@FixedFormatDecimal(decimals = 2)
	public Float getValoreBuoni() {
		return valoreBuoni;
	}

	public void setValoreBuoni(Float valoreBuoni) {
		this.valoreBuoni = valoreBuoni;
	}
	
	@Field(offset = 102, length = 5, align = Align.RIGHT, paddingChar = '0')
	public Integer getNumeroPacchi() {
		return numeroPacchi;
	}

	public void setNumeroPacchi(Integer numeroPacchi) {
		this.numeroPacchi = numeroPacchi;
	}
	
	@Field(offset = 107, length = 3, align = Align.RIGHT, paddingChar = '0')
	public Integer getNumeroCeste() {
		return numeroCeste;
	}

	public void setNumeroCeste(Integer numeroCeste) {
		this.numeroCeste = numeroCeste;
	}
	
	@Field(offset = 110, length = 3, align = Align.RIGHT, paddingChar = '0')
	public Integer getNumeroCesteVuote() {
		return numeroCesteVuote;
	}

	public void setNumeroCesteVuote(Integer numeroCesteVuote) {
		this.numeroCesteVuote = numeroCesteVuote;
	}
	
	@Field(offset = 113, length = 30)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public Integer getTotaleCopieBollaResaDettaglio() {
		return totaleCopieBollaResaDettaglio == null ? 0 : totaleCopieBollaResaDettaglio;
	}

	public void setTotaleCopieBollaResaDettaglio(Integer totaleCopieBollaResaDettaglio) {
		this.totaleCopieBollaResaDettaglio = totaleCopieBollaResaDettaglio;
	}

	public BigDecimal getTotaleBollaResaDettaglio() {
		return totaleBollaResaDettaglio == null ? new BigDecimal(0) : totaleBollaResaDettaglio;
	}

	public void setTotaleBollaResaDettaglio(BigDecimal totaleBollaResaDettaglio) {
		this.totaleBollaResaDettaglio = totaleBollaResaDettaglio;
	}

	public Integer getTotaleCopieBollaFuoriResa() {
		return totaleCopieBollaFuoriResa == null ? 0 : totaleCopieBollaFuoriResa;
	}

	public void setTotaleCopieBollaFuoriResa(Integer totaleCopieBollaFuoriResa) {
		this.totaleCopieBollaFuoriResa = totaleCopieBollaFuoriResa;
	}

	public BigDecimal getTotaleBollaFuoriResa() {
		return totaleBollaFuoriResa == null ? new BigDecimal(0) : totaleBollaFuoriResa;
	}

	public void setTotaleBollaFuoriResa(BigDecimal totaleBollaFuoriResa) {
		this.totaleBollaFuoriResa = totaleBollaFuoriResa;
	}

	public Integer getTotaleCopieBollaResaDimenticata() {
		return totaleCopieBollaResaDimenticata == null ? 0 : totaleCopieBollaResaDimenticata;
	}

	public void setTotaleCopieBollaResaDimenticata(Integer totaleCopieBollaResaDimenticata) {
		this.totaleCopieBollaResaDimenticata = totaleCopieBollaResaDimenticata;
	}

	public BigDecimal getTotaleBollaResaDimenticata() {
		return totaleBollaResaDimenticata == null ? new BigDecimal(0) : totaleBollaResaDimenticata;
	}

	public void setTotaleBollaResaDimenticata(BigDecimal totaleBollaResaDimenticata) {
		this.totaleBollaResaDimenticata = totaleBollaResaDimenticata;
	}

	@Override
	public void validate(String riga) throws InvalidRecordException {
		super.validate(riga);
		/*if (riga.length() != lineLength) {
			throw new InvalidRecordException(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.lunghezza.errata.riga"), riga.length(), lineLength));
		} else*/ if (getCodFiegDl() == null || getCodFiegDl().equals(0)) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.codice.dl.nullo"));
		} else if (getCodEdicola() == null || getCodEdicola().equals(0)) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.edicola.nullo"));
		} else if (getDataBolla() == null) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.data.bolla.nulla"));
		} else if (getTipoBolla() == null || getTipoBolla().trim().equals("")) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.tipo.bolla.nullo"));
		}
	}

}
