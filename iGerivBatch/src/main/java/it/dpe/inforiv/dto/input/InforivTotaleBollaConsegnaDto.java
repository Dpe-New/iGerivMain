package it.dpe.inforiv.dto.input;

import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;

import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatDecimal;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatNumber;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;
import com.ancientprogramming.fixedformat4j.annotation.Sign;

/**
 * @author romanom
 * 
 */
@Component("InforivTotaleBollaConsegnaDto")
@Scope("prototype")
@Record
public class InforivTotaleBollaConsegnaDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private static final int lineLength = 135;
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
	private Float totlaeFieg;
	private Float totlaeAltri;
	private Integer numeroPacchi;
	private Integer numeroCeste;
	private Integer numeroTitoliFornitiInBolla;
	private String note;

	@Field(offset = 1, length = 2)
	public String getTipoRecord() {
		return tipoRecord != null ? tipoRecord.trim() : tipoRecord;
	}

	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}
	
	@Field(offset = 3, length = 3)
	public Integer getCodFiegDl() {
		return codFiegDl;
	}

	public void setCodFiegDl(Integer codFiegDl) {
		this.codFiegDl = codFiegDl;
	}
	
	@Field(offset = 6, length = 4)
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
		return tipoBolla != null ? tipoBolla.trim() : tipoBolla;
	}

	public void setTipoBolla(String tipoBolla) {
		this.tipoBolla = tipoBolla;
	}
	
	@Field(offset = 20, length = 10, paddingChar = '0')
	public Long getNumeroBolla() {
		return numeroBolla;
	}

	public void setNumeroBolla(Long numeroBolla) {
		this.numeroBolla = numeroBolla;
	}
	
	@Field(offset = 30, length = 20, paddingChar = '0')
	public String getDescrizioneBolla() {
		return descrizioneBolla != null ? descrizioneBolla.trim() : descrizioneBolla;
	}

	public void setDescrizioneBolla(String descrizioneBolla) {
		this.descrizioneBolla = descrizioneBolla;
	}
	
	@Field(offset = 50, length = 8)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Integer getTotaleCopie() {
		return totaleCopie;
	}

	public void setTotaleCopie(Integer totaleCopie) {
		this.totaleCopie = totaleCopie;
	}
	
	@Field(offset = 58, length = 9)
	@FixedFormatDecimal(decimals = 2)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getTotaleBollaConsegna() {
		return totaleBollaConsegna;
	}

	public void setTotaleBollaConsegna(Float totaleBollaConsegna) {
		this.totaleBollaConsegna = totaleBollaConsegna;
	}
	
	@Field(offset = 67, length = 9)
	@FixedFormatDecimal(decimals = 2)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getValoreFondoBolla() {
		return valoreFondoBolla;
	}

	public void setValoreFondoBolla(Float valoreFondoBolla) {
		this.valoreFondoBolla = valoreFondoBolla;
	}
	
	@Field(offset = 76, length = 9)
	@FixedFormatDecimal(decimals = 2)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getTotlaeFieg() {
		return totlaeFieg;
	}

	public void setTotlaeFieg(Float totlaeFieg) {
		this.totlaeFieg = totlaeFieg;
	}
	
	@Field(offset = 85, length = 9)
	@FixedFormatDecimal(decimals = 2)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getTotlaeAltri() {
		return totlaeAltri;
	}

	public void setTotlaeAltri(Float totlaeAltri) {
		this.totlaeAltri = totlaeAltri;
	}
	
	@Field(offset = 94, length = 5, paddingChar = '0')
	public Integer getNumeroPacchi() {
		return numeroPacchi;
	}

	public void setNumeroPacchi(Integer numeroPacchi) {
		this.numeroPacchi = numeroPacchi;
	}
	
	@Field(offset = 99, length = 3, paddingChar = '0')
	public Integer getNumeroCeste() {
		return numeroCeste;
	}

	public void setNumeroCeste(Integer numeroCeste) {
		this.numeroCeste = numeroCeste;
	}
	
	@Field(offset = 102, length = 4, paddingChar = '0')
	public Integer getNumeroTitoliFornitiInBolla() {
		return numeroTitoliFornitiInBolla;
	}

	public void setNumeroTitoliFornitiInBolla(Integer numeroTitoliFornitiInBolla) {
		this.numeroTitoliFornitiInBolla = numeroTitoliFornitiInBolla;
	}
	
	//@Field(offset = 106, length = 30)
	public String getNote() {
		return note != null ? note.trim() : note;
	}

	public void setNote(String note) {
		this.note = note;
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
