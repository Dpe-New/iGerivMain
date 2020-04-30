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
 */
@Component("InforivFondoBollaDto")
@Scope("prototype")
@Record
public class InforivFondoBollaDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private static final int lineLength = 139;
	private String tipoRecord;		
	private Integer codFiegDl;
	private Integer codEdicola;
	private Date dataBolla;
	private String tipoBolla;
	private Long numeroBolla;
	private String idProdotto;
	private Integer tipoMovimento;
	private String descrizioneMovimento;
	private Integer copie;
	private Float prezzoCopertina;
	private Float prezzoNetto;
	private Float prezzoCessione;
	private Float scontoPuntoVendita;
	private Float compensoCompiegamento;
	private Float sviluppoNettoFornitura;
	
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
	
	@Field(offset = 30, length = 18)
	public String getIdProdotto() {
		return idProdotto != null ? idProdotto.trim() : idProdotto;
	}

	public void setIdProdotto(String idProdotto) {
		this.idProdotto = idProdotto;
	}
	
	@Field(offset = 48, length = 2, paddingChar = '0')
	public Integer getTipoMovimento() {
		return tipoMovimento;
	}

	public void setTipoMovimento(Integer tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}
	
	@Field(offset = 50, length = 30)
	public String getDescrizioneMovimento() {
		return descrizioneMovimento != null ? descrizioneMovimento.trim() : descrizioneMovimento;
	}

	public void setDescrizioneMovimento(String descrizioneMovimento) {
		this.descrizioneMovimento = descrizioneMovimento;
	}
	
	@Field(offset = 80, length = 8)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Integer getCopie() {
		return copie;
	}

	public void setCopie(Integer copie) {
		this.copie = copie;
	}
	
	@Field(offset = 88, length = 6, paddingChar = '0', align = Align.RIGHT)
	@FixedFormatDecimal(decimals = 2)
	public Float getPrezzoCopertina() {
		return prezzoCopertina;
	}

	public void setPrezzoCopertina(Float prezzoCopertina) {
		this.prezzoCopertina = prezzoCopertina;
	}
	
	@Field(offset = 94, length = 11)
	@FixedFormatDecimal(decimals = 6)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getPrezzoNetto() {
		return prezzoNetto;
	}

	public void setPrezzoNetto(Float prezzoNetto) {
		this.prezzoNetto = prezzoNetto;
	}
	
	@Field(offset = 105, length = 10, paddingChar = '0', align = Align.RIGHT)
	@FixedFormatDecimal(decimals = 6)
	public Float getPrezzoCessione() {
		return prezzoCessione;
	}

	public void setPrezzoCessione(Float prezzoCessione) {
		this.prezzoCessione = prezzoCessione;
	}
	
	@Field(offset = 115, length = 5, paddingChar = '0', align = Align.RIGHT)
	@FixedFormatDecimal(decimals = 2)
	public Float getScontoPuntoVendita() {
		return scontoPuntoVendita;
	}

	public void setScontoPuntoVendita(Float scontoPuntoVendita) {
		this.scontoPuntoVendita = scontoPuntoVendita;
	}
	
	@Field(offset = 120, length = 8)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	@FixedFormatDecimal(decimals = 6)
	public Float getCompensoCompiegamento() {
		return compensoCompiegamento;
	}

	public void setCompensoCompiegamento(Float compensoCompiegamento) {
		this.compensoCompiegamento = compensoCompiegamento;
	}
	
	@Field(offset = 128, length = 12)
	@FixedFormatDecimal(decimals = 6)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getSviluppoNettoFornitura() {
		return sviluppoNettoFornitura;
	}

	public void setSviluppoNettoFornitura(Float sviluppoNettoFornitura) {
		this.sviluppoNettoFornitura = sviluppoNettoFornitura;
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
		} else if (getDataBolla() == null) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.data.bolla.nulla"));
		} else if (getTipoBolla() == null || getTipoBolla().trim().equals("")) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.tipo.bolla.nullo"));
		}
	}


}
