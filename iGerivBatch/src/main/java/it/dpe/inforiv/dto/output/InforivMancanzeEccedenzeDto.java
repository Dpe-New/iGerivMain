package it.dpe.inforiv.dto.output;

import it.dpe.inforiv.dto.input.InforivBaseDto;
import it.dpe.inforiv.dto.input.InforivDto;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatNumber;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;
import com.ancientprogramming.fixedformat4j.annotation.Sign;

/**
 * @author romanom
 */
@Component("InforivMancanzeEccedenzeDto")
@Record
@SuppressWarnings("unused")
public class InforivMancanzeEccedenzeDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private String tipoRecord;
	private Integer codFiegDl;
	private Integer codEdicola;
	private Date dataBolla;
	private String tipoBolla;
	private Long numeroBolla;
	private String tipoMovimento;
	private String idProdotto;
	private Integer fornito;
	private Integer copieMancantiEccedenti;
	private String note;
	private Integer differenze;
	private String idtnTrascodifica;
	
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
	
	@Field(offset = 30, length = 1)
	public String getTipoMovimento() {
		if (getDifferenze() != null && getDifferenze() > 0) {
			return "E";
		} else if (getDifferenze() != null && getDifferenze() < 0) {
			return "M";
		}
		return tipoMovimento;
	}

	public void setTipoMovimento(String tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}
	
	@Field(offset = 31, length = 18)
	public String getIdProdotto() {
		return (idtnTrascodifica != null) ? idtnTrascodifica.toString() : idProdotto;
	}

	public void setIdProdotto(String idProdotto) {
		this.idProdotto = idProdotto;
	}
	
	@Field(offset = 49, length = 8, align = Align.RIGHT, paddingChar = '0')
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Integer getFornito() {
		return fornito;
	}

	public void setFornito(Integer fornito) {
		this.fornito = fornito;
	}
	
	@Field(offset = 57, length = 7, align = Align.RIGHT, paddingChar = '0')
	public Integer getCopieMancantiEccedenti() {
		return copieMancantiEccedenti = Math.abs(getDifferenze());
	}

	public void setCopieMancantiEccedenti(Integer copieMancantiEccedenti) {
		this.copieMancantiEccedenti = copieMancantiEccedenti;
	}
	
	@Field(offset = 64, length = 30)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getDifferenze() {
		return differenze;
	}

	public void setDifferenze(Integer differenze) {
		this.differenze = differenze;
	}

	public String getIdtnTrascodifica() {
		return idtnTrascodifica;
	}

	public void setIdtnTrascodifica(String idtnTrascodifica) {
		this.idtnTrascodifica = idtnTrascodifica;
	}
	
}
