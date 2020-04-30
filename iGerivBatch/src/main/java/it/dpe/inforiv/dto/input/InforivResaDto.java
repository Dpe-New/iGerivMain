package it.dpe.inforiv.dto.input;

import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;

import java.util.Date;

import org.springframework.context.annotation.Scope;
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
 *
 */
@Component("InforivResaDto")
@Scope("prototype")
@Record
public class InforivResaDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private static final int lineLength = 164;
	private String tipoRecord;		
	private Integer codFiegDl;
	private Integer codEdicola;
	private Date dataBolla;
	private String tipoBolla;
	private Long numeroBolla;
	private Integer causaliResa;
	private String descrizioneRichiamoResa;
	private Integer numeroRiga;
	private String idProdotto;
	private Integer copie;
	private Float prezzoCopertina;
	private Float prezzoNetto;
	private Float prezzoCessione;
	private Float scontoPuntoVendita;
	private Float compensoCompiegamento;
	private Float sviluppoNettoResa;
	private String flagContoDepositoNonFatturato;
	private String flagBuono;
	private String note;
	private Integer numeroRiga8;
	
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
	
	@Field(offset = 30, length = 2, paddingChar = '0')
	public Integer getCausaliResa() {
		return causaliResa;
	}

	public void setCausaliResa(Integer causaliResa) {
		this.causaliResa = causaliResa;
	}

	@Field(offset = 32, length = 30)
	public String getDescrizioneRichiamoResa() {
		return descrizioneRichiamoResa != null ? descrizioneRichiamoResa.trim() : descrizioneRichiamoResa;
	}

	public void setDescrizioneRichiamoResa(String descrizioneRichiamoResa) {
		this.descrizioneRichiamoResa = descrizioneRichiamoResa;
	}
	
	@Field(offset = 62, length = 3, align = Align.RIGHT,  paddingChar = '0')
	public Integer getNumeroRiga() {
		return numeroRiga;
	}

	public void setNumeroRiga(Integer numeroRiga) {
		this.numeroRiga = numeroRiga;
	}

	@Field(offset = 65, length = 18)
	public String getIdProdotto() {
		return idProdotto != null ? idProdotto.trim() : idProdotto;
	}

	public void setIdProdotto(String idProdotto) {
		this.idProdotto = idProdotto;
	}
	
	@Field(offset = 83, length = 8, align = Align.RIGHT, paddingChar = '0')
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Integer getCopie() {
		return copie;
	}

	public void setCopie(Integer copie) {
		this.copie = copie;
	}

	@Field(offset = 91, length = 6, paddingChar = '0', align = Align.RIGHT)
	@FixedFormatDecimal(decimals = 2)
	public Float getPrezzoCopertina() {
		return prezzoCopertina;
	}

	public void setPrezzoCopertina(Float prezzoCopertina) {
		this.prezzoCopertina = prezzoCopertina;
	}
	
	@Field(offset = 97, length = 11, align = Align.RIGHT)
	@FixedFormatDecimal(decimals = 6)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getPrezzoNetto() {
		return prezzoNetto;
	}

	public void setPrezzoNetto(Float prezzoNetto) {
		this.prezzoNetto = prezzoNetto;
	}
	
	@Field(offset = 108, length = 10, paddingChar = '0', align = Align.RIGHT)
	@FixedFormatDecimal(decimals = 6)
	public Float getPrezzoCessione() {
		return prezzoCessione;
	}

	public void setPrezzoCessione(Float prezzoCessione) {
		this.prezzoCessione = prezzoCessione;
	}
	
	@Field(offset = 118, length = 5, paddingChar = '0', align = Align.RIGHT)
	@FixedFormatDecimal(decimals = 2)
	public Float getScontoPuntoVendita() {
		return scontoPuntoVendita;
	}

	public void setScontoPuntoVendita(Float scontoPuntoVendita) {
		this.scontoPuntoVendita = scontoPuntoVendita;
	}
	
	@Field(offset = 123, length = 8)
	@FixedFormatDecimal(decimals = 6)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getCompensoCompiegamento() {
		return compensoCompiegamento;
	}

	public void setCompensoCompiegamento(Float compensoCompiegamento) {
		this.compensoCompiegamento = compensoCompiegamento;
	}
	
	@Field(offset = 131, length = 12)
	@FixedFormatDecimal(decimals = 6)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getSviluppoNettoResa() {
		return sviluppoNettoResa;
	}

	public void setSviluppoNettoResa(Float sviluppoNettoResa) {
		this.sviluppoNettoResa = sviluppoNettoResa;
	}

	@Field(offset = 143, length = 1)
	public String getFlagContoDepositoNonFatturato() {
		return flagContoDepositoNonFatturato != null ? flagContoDepositoNonFatturato.trim() : flagContoDepositoNonFatturato;
	}

	public void setFlagContoDepositoNonFatturato(String flagContoDepositoNonFatturato) {
		this.flagContoDepositoNonFatturato = flagContoDepositoNonFatturato;
	}
	
	@Field(offset = 144, length = 1)
	public String getFlagBuono() {
		return flagBuono != null ? flagBuono.trim() : flagBuono;
	}

	public void setFlagBuono(String flagBuono) {
		this.flagBuono = flagBuono;
	}

	@Field(offset = 145, length = 20)
	public String getNote() {
		return note != null ? note.trim() : note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	@Field(offset = 165, length = 8, align = Align.RIGHT,  paddingChar = '0')
	public Integer getNumeroRiga8() {
		return numeroRiga8;
	}

	public void setNumeroRiga8(Integer numeroRiga8) {
		this.numeroRiga8 = numeroRiga8;
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
