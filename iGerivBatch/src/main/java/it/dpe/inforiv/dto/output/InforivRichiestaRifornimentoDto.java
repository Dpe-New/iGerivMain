package it.dpe.inforiv.dto.output;

import org.springframework.stereotype.Component;

import it.dpe.inforiv.dto.input.InforivBaseDto;
import it.dpe.inforiv.dto.input.InforivDto;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatNumber;
import com.ancientprogramming.fixedformat4j.annotation.Record;
import com.ancientprogramming.fixedformat4j.annotation.Sign;

/**
 * @author romanom
 */
@Component("InforivRichiestaRifornimentoDto")
@Record
public class InforivRichiestaRifornimentoDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private String tipoRecord;
	private Integer codFiegDl;
	private Integer codEdicola;
	private Integer codiceRichiesta;
	private String tipoMovimento;
	private Integer numeroSollecito;
	private String idProdotto;
	private Integer fornitoTotale;
	private Integer resaEsuberanzaExtra;
	private Integer giacenzaAttuale;
	private Integer copieRichieste;
	private String note;
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
	
	@Field(offset = 10, length = 9, align = Align.RIGHT, paddingChar = '0')
	public Integer getCodiceRichiesta() {
		return codiceRichiesta;
	}

	public void setCodiceRichiesta(Integer codiceRichiesta) {
		this.codiceRichiesta = codiceRichiesta;
	}
	
	@Field(offset = 19, length = 1)
	public String getTipoMovimento() {
		return tipoMovimento;
	}

	public void setTipoMovimento(String tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}
	
	@Field(offset = 20, length = 1, paddingChar = '0')
	public Integer getNumeroSollecito() {
		return numeroSollecito;
	}

	public void setNumeroSollecito(Integer numeroSollecito) {
		this.numeroSollecito = numeroSollecito;
	}
	
	@Field(offset = 21, length = 18)
	public String getIdProdotto() {
		return (idtnTrascodifica != null) ? idtnTrascodifica.toString() : idProdotto;
	}

	public void setIdProdotto(String idProdotto) {
		this.idProdotto = idProdotto;
	}
	
	@Field(offset = 39, length = 8, align = Align.RIGHT, paddingChar = '0')
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Integer getFornitoTotale() {
		return fornitoTotale;
	}

	public void setFornitoTotale(Integer fornitoTotale) {
		this.fornitoTotale = fornitoTotale;
	}
	
	@Field(offset = 47, length = 8, align = Align.RIGHT, paddingChar = '0')
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Integer getResaEsuberanzaExtra() {
		return resaEsuberanzaExtra;
	}

	public void setResaEsuberanzaExtra(Integer resaEsuberanzaExtra) {
		this.resaEsuberanzaExtra = resaEsuberanzaExtra;
	}
	
	@Field(offset = 55, length = 8, align = Align.RIGHT, paddingChar = '0')
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Integer getGiacenzaAttuale() {
		return giacenzaAttuale;
	}

	public void setGiacenzaAttuale(Integer giacenzaAttuale) {
		this.giacenzaAttuale = giacenzaAttuale;
	}
	
	@Field(offset = 63, length = 7, align = Align.RIGHT, paddingChar = '0')
	public Integer getCopieRichieste() {
		return copieRichieste;
	}

	public void setCopieRichieste(Integer copieRichieste) {
		this.copieRichieste = copieRichieste;
	}
	
	@Field(offset = 70, length = 30)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getIdtnTrascodifica() {
		return idtnTrascodifica;
	}

	public void setIdtnTrascodifica(String idtnTrascodifica) {
		this.idtnTrascodifica = idtnTrascodifica;
	}
	
}
