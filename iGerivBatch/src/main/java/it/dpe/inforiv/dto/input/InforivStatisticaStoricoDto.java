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
@Component("InforivStatisticaStoricoDto")
@Scope("prototype")
@Record
public class InforivStatisticaStoricoDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private static final int lineLength = 185;
	private String tipoRecord;
	private Integer codFiegDl;
	private Integer codEdicola;
	private String idProdotto;
	private String codiceAssociazione;
	private Integer fornito;
	private Integer rifornimenti;
	private Integer arretrati;
	private Integer mancazneEccedenze;
	private Integer reso;
	private Integer resoPerEsubero;
	private Integer fornitoEspositori;
	private Float prezzoCessione;
	private Float sconto;
	private Float compensoCompiegamento;
	private Integer copieInContoDeposito;
	private Date dataAddebitoinContoDeposito;
	private Long fascicoloGuida;
	private String note;
	private Date dataResa;
	private Integer tipoResa;
	
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
	
	@Field(offset = 10, length = 18)
	public String getIdProdotto() {
		return idProdotto != null ? idProdotto.trim() : idProdotto;
	}

	public void setIdProdotto(String idProdotto) {
		this.idProdotto = idProdotto;
	}
	
	@Field(offset = 28, length = 1)
	public String getCodiceAssociazione() {
		return codiceAssociazione != null ? codiceAssociazione.trim() : codiceAssociazione;
	}

	public void setCodiceAssociazione(String codiceAssociazione) {
		this.codiceAssociazione = codiceAssociazione;
	}
	
	@Field(offset = 29, length = 8)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Integer getFornito() {
		return fornito;
	}

	public void setFornito(Integer fornito) {
		this.fornito = fornito;
	}
	
	@Field(offset = 37, length = 8)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Integer getRifornimenti() {
		return rifornimenti;
	}

	public void setRifornimenti(Integer rifornimenti) {
		this.rifornimenti = rifornimenti;
	}
	
	@Field(offset = 45, length = 8)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Integer getArretrati() {
		return arretrati;
	}

	public void setArretrati(Integer arretrati) {
		this.arretrati = arretrati;
	}
	
	@Field(offset = 53, length = 8)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Integer getMancazneEccedenze() {
		return mancazneEccedenze;
	}

	public void setMancazneEccedenze(Integer mancazneEccedenze) {
		this.mancazneEccedenze = mancazneEccedenze;
	}
	
	@Field(offset = 61, length = 8)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Integer getReso() {
		return reso;
	}

	public void setReso(Integer reso) {
		this.reso = reso;
	}
	
	@Field(offset = 69, length = 8)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Integer getResoPerEsubero() {
		return resoPerEsubero;
	}

	public void setResoPerEsubero(Integer resoPerEsubero) {
		this.resoPerEsubero = resoPerEsubero;
	}
	
	@Field(offset = 77, length = 8)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Integer getFornitoEspositori() {
		return fornitoEspositori;
	}

	public void setFornitoEspositori(Integer fornitoEspositori) {
		this.fornitoEspositori = fornitoEspositori;
	}
	
	@Field(offset = 85, length = 10, paddingChar = '0', align = Align.RIGHT)
	@FixedFormatDecimal(decimals = 6)
	public Float getPrezzoCessione() {
		return prezzoCessione;
	}

	public void setPrezzoCessione(Float prezzoCessione) {
		this.prezzoCessione = prezzoCessione;
	}
	
	@Field(offset = 95, length = 5, paddingChar = '0', align = Align.RIGHT)
	@FixedFormatDecimal(decimals = 2)
	public Float getSconto() {
		return sconto;
	}

	public void setSconto(Float sconto) {
		this.sconto = sconto;
	}
	
	@Field(offset = 100, length = 8)
	@FixedFormatDecimal(decimals = 6)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getCompensoCompiegamento() {
		return compensoCompiegamento;
	}

	public void setCompensoCompiegamento(Float compensoCompiegamento) {
		this.compensoCompiegamento = compensoCompiegamento;
	}
	
	@Field(offset = 108, length = 7, paddingChar = '0', align = Align.RIGHT)
	public Integer getCopieInContoDeposito() {
		return copieInContoDeposito;
	}

	public void setCopieInContoDeposito(Integer copieInContoDeposito) {
		this.copieInContoDeposito = copieInContoDeposito;
	}
	
	@Field(offset = 115, length = 8, paddingChar = '0')
	public Date getDataAddebitoinContoDeposito() {
		return dataAddebitoinContoDeposito;
	}

	public void setDataAddebitoinContoDeposito(Date dataAddebitoinContoDeposito) {
		this.dataAddebitoinContoDeposito = dataAddebitoinContoDeposito;
	}
	
	@Field(offset = 123, length = 14, paddingChar = '0')
	public Long getFascicoloGuida() {
		return fascicoloGuida;
	}

	public void setFascicoloGuida(Long fascicoloGuida) {
		this.fascicoloGuida = fascicoloGuida;
	}
	
	@Field(offset = 137, length = 40)
	public String getNote() {
		return note != null ? note.trim() : note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	@Field(offset = 177, length = 8)
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataResa() {
		return dataResa;
	}

	public void setDataResa(Date dataResa) {
		this.dataResa = dataResa;
	}
	
	@Field(offset = 185, length = 1)
	public Integer getTipoResa() {
		return tipoResa;
	}

	public void setTipoResa(Integer tipoResa) {
		this.tipoResa = tipoResa;
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
		} else if (getIdProdotto() == null || getIdProdotto().equals("")) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.idtn.nullo"));
		} 
	}
}
