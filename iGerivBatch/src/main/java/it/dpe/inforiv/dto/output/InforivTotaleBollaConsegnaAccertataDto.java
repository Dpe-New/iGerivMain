package it.dpe.inforiv.dto.output;

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
@Component("InforivTotaleBollaConsegnaAccertataDto")
@Record
public class InforivTotaleBollaConsegnaAccertataDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private String tipoRecord;
	private Integer codFiegDl;
	private Integer codEdicola;
	private Date dataBolla;
	private String tipoBolla;
	private Long numeroBolla;
	private String descrizioneBolla;
	private Integer totaleCopie;
	private Float totaleBollaConsegna;
	private Float valoreFondoBolla;
	private BigDecimal totaleBollaConsegnaBd;
	private BigDecimal valoreFondoBollaBd;
	private Float totlaeFieg;
	private Float totlaeAltri;
	private Integer numeroPacchi;
	private Integer numeroCeste;
	private Integer numeroTitoliFornitiInBolla;
	private String note;
	private Long totaleCopieConsegnate;
	private Long totaleDifferenze;

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
	public Integer getTotaleCopie() {
		if (totaleCopieConsegnate != null && totaleDifferenze != null) {
			Long totale = totaleCopieConsegnate + totaleDifferenze;
			return new Integer(totale.toString());
		}
		return totaleCopie;
	}

	public void setTotaleCopie(Integer totaleCopie) {
		this.totaleCopie = totaleCopie;
	}
	
	@Field(offset = 58, length = 9, align = Align.RIGHT, paddingChar = '0')
	@FixedFormatDecimal(decimals = 2)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getTotaleBollaConsegna() {
		if (totaleBollaConsegnaBd != null) {
			return totaleBollaConsegnaBd.floatValue();
		}
		return totaleBollaConsegna;
	}

	public void setTotaleBollaConsegna(Float totaleBollaConsegna) {
		this.totaleBollaConsegna = totaleBollaConsegna;
	}
	
	@Field(offset = 67, length = 9, align = Align.RIGHT, paddingChar = '0')
	@FixedFormatDecimal(decimals = 2)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getValoreFondoBolla() {
		if (valoreFondoBollaBd != null) {
			return valoreFondoBollaBd.floatValue();
		}
		return valoreFondoBolla;
	}

	public void setValoreFondoBolla(Float valoreFondoBolla) {
		this.valoreFondoBolla = valoreFondoBolla;
	}
	
	public BigDecimal getTotaleBollaConsegnaBd() {
		return totaleBollaConsegnaBd;
	}

	public void setTotaleBollaConsegnaBd(BigDecimal totaleBollaConsegnaBd) {
		this.totaleBollaConsegnaBd = totaleBollaConsegnaBd;
	}

	public BigDecimal getValoreFondoBollaBd() {
		return valoreFondoBollaBd;
	}

	public void setValoreFondoBollaBd(BigDecimal valoreFondoBollaBd) {
		this.valoreFondoBollaBd = valoreFondoBollaBd;
	}

	@Field(offset = 76, length = 9, align = Align.RIGHT, paddingChar = '0')
	@FixedFormatDecimal(decimals = 2)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getTotlaeFieg() {
		return totlaeFieg;
	}

	public void setTotlaeFieg(Float totlaeFieg) {
		this.totlaeFieg = totlaeFieg;
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
	
	@Field(offset = 94, length = 5, align = Align.RIGHT, paddingChar = '0')
	public Integer getNumeroPacchi() {
		return numeroPacchi;
	}

	public void setNumeroPacchi(Integer numeroPacchi) {
		this.numeroPacchi = numeroPacchi;
	}
	
	@Field(offset = 99, length = 3, align = Align.RIGHT, paddingChar = '0')
	public Integer getNumeroCeste() {
		return numeroCeste;
	}

	public void setNumeroCeste(Integer numeroCeste) {
		this.numeroCeste = numeroCeste;
	}
	
	@Field(offset = 102, length = 4, align = Align.RIGHT, paddingChar = '0')
	public Integer getNumeroTitoliFornitiInBolla() {
		return numeroTitoliFornitiInBolla;
	}

	public void setNumeroTitoliFornitiInBolla(Integer numeroTitoliFornitiInBolla) {
		this.numeroTitoliFornitiInBolla = numeroTitoliFornitiInBolla;
	}
	
	@Field(offset = 106, length = 30)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getTotaleCopieConsegnate() {
		return totaleCopieConsegnate;
	}

	public void setTotaleCopieConsegnate(Long totaleCopieConsegnate) {
		this.totaleCopieConsegnate = totaleCopieConsegnate;
	}

	public Long getTotaleDifferenze() {
		return totaleDifferenze;
	}

	public void setTotaleDifferenze(Long totaleDifferenze) {
		this.totaleDifferenze = totaleDifferenze;
	}
	
}
