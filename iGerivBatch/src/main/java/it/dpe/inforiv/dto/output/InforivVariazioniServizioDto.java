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
@Component("InforivVariazioniServizioDto")
@Record
public class InforivVariazioniServizioDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private String tipoRecord;
	private Integer codFiegDl;
	private Integer codEdicola;
	private Integer codiceTestata;
	private String titolo;
	private Integer fornitoUltimoNumero;
	private Integer giacenzaUltimoNumero;
	private Date dataInizioVariazione;
	private Date dataFineVariazione;
	private String numeroInizioVariazione;
	private String numeroFineVariazione;
	private Integer copieRichieste;
	private String note;

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
	
	@Field(offset = 10, length = 9, align = Align.RIGHT, paddingChar = '0')
	public Integer getCodiceTestata() {
		return codiceTestata;
	}

	public void setCodiceTestata(Integer codiceTestata) {
		this.codiceTestata = codiceTestata;
	}
	
	@Field(offset = 19, length = 20)
	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	
	@Field(offset = 39, length = 8, align = Align.RIGHT, paddingChar = '0')
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Integer getFornitoUltimoNumero() {
		return fornitoUltimoNumero;
	}

	public void setFornitoUltimoNumero(Integer fornitoUltimoNumero) {
		this.fornitoUltimoNumero = fornitoUltimoNumero;
	}
	
	@Field(offset = 47, length = 8, align = Align.RIGHT, paddingChar = '0')
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Integer getGiacenzaUltimoNumero() {
		return giacenzaUltimoNumero;
	}

	public void setGiacenzaUltimoNumero(Integer giacenzaUltimoNumero) {
		this.giacenzaUltimoNumero = giacenzaUltimoNumero;
	}
	
	@Field(offset = 55, length = 8, paddingChar = '0')
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataInizioVariazione() {
		return dataInizioVariazione;
	}

	public void setDataInizioVariazione(Date dataInizioVariazione) {
		this.dataInizioVariazione = dataInizioVariazione;
	}
	
	@Field(offset = 63, length = 8, paddingChar = '0')
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataFineVariazione() {
		return dataFineVariazione;
	}

	public void setDataFineVariazione(Date dataFineVariazione) {
		this.dataFineVariazione = dataFineVariazione;
	}
	
	@Field(offset = 71, length = 8)
	public String getNumeroInizioVariazione() {
		return numeroInizioVariazione;
	}

	public void setNumeroInizioVariazione(String numeroInizioVariazione) {
		this.numeroInizioVariazione = numeroInizioVariazione;
	}
	
	@Field(offset = 79, length = 8)
	public String getNumeroFineVariazione() {
		return numeroFineVariazione;
	}

	public void setNumeroFineVariazione(String numeroFineVariazione) {
		this.numeroFineVariazione = numeroFineVariazione;
	}
	
	@Field(offset = 87, length = 7, align = Align.RIGHT, paddingChar = '0')
	public Integer getCopieRichieste() {
		return copieRichieste;
	}

	public void setCopieRichieste(Integer copieRichieste) {
		this.copieRichieste = copieRichieste;
	}
	
	@Field(offset = 94, length = 30)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
