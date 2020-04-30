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
 */
@Component("InforivEstrattoContoDto")
@Scope("prototype")
@Record
public class InforivEstrattoContoDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private static final int lineLength = 150;
	private String tipoRecord;
	private Integer codFiegDl;
	private Integer codEdicola;
	private Integer numeroEstrattoConto;
	private Date dataEstrattoConto;
	private Long numeroEstrattoContoRif;
	private Date dataEstrattoContoRif;
	private String descrizioneCausale;
	private Date dataMovimentoContabile;
	private Float importo;
	private Float importoFieg;
	private Float importoAltri;
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
	
	@Field(offset = 10, length = 10, paddingChar = '0')
	public Integer getNumeroEstrattoConto() {
		return numeroEstrattoConto;
	}

	public void setNumeroEstrattoConto(Integer numeroEstrattoConto) {
		this.numeroEstrattoConto = numeroEstrattoConto;
	}
	
	@Field(offset = 20, length = 8)
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataEstrattoConto() {
		return dataEstrattoConto;
	}

	public void setDataEstrattoConto(Date dataEstrattoConto) {
		this.dataEstrattoConto = dataEstrattoConto;
	}
	
	@Field(offset = 28, length = 10, paddingChar = '0')
	public Long getNumeroEstrattoContoRif() {
		return numeroEstrattoContoRif;
	}

	public void setNumeroEstrattoContoRif(Long numeroEstrattoContoRif) {
		this.numeroEstrattoContoRif = numeroEstrattoContoRif;
	}
	
	@Field(offset = 38, length = 8)
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataEstrattoContoRif() {
		return dataEstrattoContoRif;
	}

	public void setDataEstrattoContoRif(Date dataEstrattoContoRif) {
		this.dataEstrattoContoRif = dataEstrattoContoRif;
	}

	@Field(offset = 46, length = 40)
	public String getDescrizioneCausale() {
		return descrizioneCausale != null ? descrizioneCausale.trim() : descrizioneCausale;
	}

	public void setDescrizioneCausale(String descrizioneCausale) {
		this.descrizioneCausale = descrizioneCausale;
	}
	
	@Field(offset = 86, length = 8)
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataMovimentoContabile() {
		return dataMovimentoContabile;
	}

	public void setDataMovimentoContabile(Date dataMovimentoContabile) {
		this.dataMovimentoContabile = dataMovimentoContabile;
	}
	
	@Field(offset = 94, length = 9)
	@FixedFormatDecimal(decimals = 2)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getImporto() {
		return importo;
	}

	public void setImporto(Float importo) {
		this.importo = importo;
	}
	
	@Field(offset = 103, length = 9)
	@FixedFormatDecimal(decimals = 2)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getImportoFieg() {
		return importoFieg;
	}

	public void setImportoFieg(Float importoFieg) {
		this.importoFieg = importoFieg;
	}
	
	@Field(offset = 112, length = 9)
	@FixedFormatDecimal(decimals = 2)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getImportoAltri() {
		return importoAltri;
	}

	public void setImportoAltri(Float importoAltri) {
		this.importoAltri = importoAltri;
	}
	
	@Field(offset = 121, length = 30)
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
		} else */if (getCodFiegDl() == null || getCodFiegDl().equals(0)) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.codice.dl.nullo"));
		} else if (getCodEdicola() == null || getCodEdicola().equals(0)) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.edicola.nullo"));
		} 
	}
}
