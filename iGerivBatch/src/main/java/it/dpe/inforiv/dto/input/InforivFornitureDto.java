package it.dpe.inforiv.dto.input;

import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
@Component("InforivFornitureDto")
@Scope("prototype")
@Record
public class InforivFornitureDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private static final int lineLength = 201;
	private String tipoRecord;		
	private Integer codFiegDl;
	private Integer codEdicola;
	private Date dataBolla;
	private String tipoBolla;
	private Long numeroBolla;
	private Integer numeroRiga;
	private String idProdotto;
	private String codiceAssociazione;
	private Integer numeroDistribuzione;
	private String codiceInvio;
	private Date dataConsegna;
	private Integer fornito;
	private Integer fornitoInvioEspositori;
	private Float prezzoCopertina;
	private Float prezzoNetto;
	private Float prezzoCessione;
	private Float scontoPuntoVendita;
	private Float compensoCompiegamento;
	private Float sviluppoNettoFornitura;
	private String flagPrezzoZero;
	private String contoDeposito;
	private Date dataAddebitoContoDeposito;
	private Long fascicoloGuida;
	private Integer numeroUscitePerArretrato;
	private Float maggiornazioneArretrati;
	private String note;
	
	private String dataAddebitoContoDepositoString;
	
	
	
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
	
	@Field(offset = 30, length = 3, align = Align.RIGHT, paddingChar = '0') 
	public Integer getNumeroRiga() {
		return numeroRiga;
	}

	public void setNumeroRiga(Integer numeroRiga) {
		this.numeroRiga = numeroRiga;
	}
	
	@Field(offset = 33, length = 18)
	public String getIdProdotto() {
		return idProdotto != null ? idProdotto.trim() : idProdotto;
	}

	public void setIdProdotto(String idProdotto) {
		this.idProdotto = idProdotto;
	}
	
	@Field(offset = 51, length = 1)
	public String getCodiceAssociazione() {
		return codiceAssociazione != null ? codiceAssociazione.trim() : codiceAssociazione;
	}

	public void setCodiceAssociazione(String codiceAssociazione) {
		this.codiceAssociazione = codiceAssociazione;
	}
	
	@Field(offset = 52, length = 1, paddingChar = '0')
	public Integer getNumeroDistribuzione() {
		return numeroDistribuzione;
	}

	public void setNumeroDistribuzione(Integer numeroDistribuzione) {
		this.numeroDistribuzione = numeroDistribuzione;
	}
	
	@Field(offset = 53, length = 1)
	public String getCodiceInvio() {
		return codiceInvio != null ? codiceInvio.trim() : codiceInvio;
	}

	public void setCodiceInvio(String codiceInvio) {
		this.codiceInvio = codiceInvio;
	}
	
	@Field(offset = 54, length = 8)
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataConsegna() {
		return dataConsegna;
	}

	public void setDataConsegna(Date dataConsegna) {
		this.dataConsegna = dataConsegna;
	}
	
	@Field(offset = 62, length = 8)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Integer getFornito() {
		return fornito;
	}

	public void setFornito(Integer fornito) {
		this.fornito = fornito;
	}
	
	@Field(offset = 70, length = 8)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Integer getFornitoInvioEspositori() {
		return fornitoInvioEspositori;
	}

	public void setFornitoInvioEspositori(Integer fornitoInvioEspositori) {
		this.fornitoInvioEspositori = fornitoInvioEspositori;
	}
	
	@Field(offset = 78, length = 6, paddingChar = '0', align = Align.RIGHT)
	@FixedFormatDecimal(decimals = 2)
	public Float getPrezzoCopertina() {
		return prezzoCopertina;
	}

	public void setPrezzoCopertina(Float prezzoCopertina) {
		this.prezzoCopertina = prezzoCopertina;
	}
	
	@Field(offset = 84, length = 11)
	@FixedFormatDecimal(decimals = 6)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getPrezzoNetto() {
		return prezzoNetto;
	}

	public void setPrezzoNetto(Float prezzoNetto) {
		this.prezzoNetto = prezzoNetto;
	}
	
	@Field(offset = 95, length = 10, paddingChar = '0', align = Align.RIGHT)
	@FixedFormatDecimal(decimals = 6)
	public Float getPrezzoCessione() {
		return prezzoCessione;
	}

	public void setPrezzoCessione(Float prezzoCessione) {
		this.prezzoCessione = prezzoCessione;
	}
	
	@Field(offset = 105, length = 5, paddingChar = '0', align = Align.RIGHT)
	@FixedFormatDecimal(decimals = 2)
	public Float getScontoPuntoVendita() {
		return scontoPuntoVendita;
	}

	public void setScontoPuntoVendita(Float scontoPuntoVendita) {
		this.scontoPuntoVendita = scontoPuntoVendita;
	}
	
	@Field(offset = 110, length = 8)
	@FixedFormatDecimal(decimals = 6)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getCompensoCompiegamento() {
		return compensoCompiegamento;
	}

	public void setCompensoCompiegamento(Float compensoCompiegamento) {
		this.compensoCompiegamento = compensoCompiegamento;
	}
	
	@Field(offset = 118, length = 12)
	@FixedFormatDecimal(decimals = 6)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getSviluppoNettoFornitura() {
		return sviluppoNettoFornitura;
	}

	public void setSviluppoNettoFornitura(Float sviluppoNettoFornitura) {
		this.sviluppoNettoFornitura = sviluppoNettoFornitura;
	}
	
	@Field(offset = 130, length = 1)
	public String getFlagPrezzoZero() {
		return flagPrezzoZero;
	}

	public void setFlagPrezzoZero(String flagPrezzoZero) {
		this.flagPrezzoZero = flagPrezzoZero;
	}
	
	@Field(offset = 131, length = 1)
	public String getContoDeposito() {
		return contoDeposito != null ? contoDeposito.trim() : contoDeposito;
	}

	public void setContoDeposito(String contoDeposito) {
		this.contoDeposito = contoDeposito;
	}
	
	
	@Field(offset = 132, length = 8, paddingChar = '0', align = Align.RIGHT)
	public String getDataAddebitoContoDepositoString() {
		return dataAddebitoContoDepositoString;
	}
	
	public void setDataAddebitoContoDepositoString(String dataAddebitoContoDepositoString) {
		if(dataAddebitoContoDepositoString!=null && !dataAddebitoContoDepositoString.equals("") && Integer.parseInt(dataAddebitoContoDepositoString)>0){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			try {
				this.setDataAddebitoContoDeposito(formatter.parse(dataAddebitoContoDepositoString));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
	}

//	@Field(offset = 132, length = 8)
//	@FixedFormatPattern("yyyyMMdd")
	public Date getDataAddebitoContoDeposito() {
		return dataAddebitoContoDeposito;
	}

	public void setDataAddebitoContoDeposito(Date dataAddebitoContoDeposito) {
		this.dataAddebitoContoDeposito = dataAddebitoContoDeposito;
	}
	
	@Field(offset = 140, length = 14, paddingChar = '0')
	public Long getFascicoloGuida() {
		return fascicoloGuida;
	}

	public void setFascicoloGuida(Long fascicoloGuida) {
		this.fascicoloGuida = fascicoloGuida;
	}
	
	@Field(offset = 154, length = 2, paddingChar = '0')
	public Integer getNumeroUscitePerArretrato() {
		return numeroUscitePerArretrato;
	}

	public void setNumeroUscitePerArretrato(Integer numeroUscitePerArretrato) {
		this.numeroUscitePerArretrato = numeroUscitePerArretrato;
	}
	
	@Field(offset = 156, length = 6, paddingChar = '0', align = Align.RIGHT)
	public Float getMaggiornazioneArretrati() {
		return maggiornazioneArretrati;
	}

	public void setMaggiornazioneArretrati(Float maggiornazioneArretrati) {
		this.maggiornazioneArretrati = maggiornazioneArretrati;
	}
	
	//@Field(offset = 162, length = 40)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public BigDecimal getPercentualeDefiscalizzazione() {
		Float perc = 0f;
		if (prezzoCopertina != null && prezzoCopertina.compareTo(0f) > 0
		&& prezzoCessione != null && prezzoCessione.compareTo(0f) > 0) {
			perc = 100f*prezzoCessione/prezzoCopertina;
		}
		BigDecimal bd = new BigDecimal(perc);
	    bd.setScale(2, RoundingMode.HALF_UP);
	    if (bd.compareTo(new BigDecimal(100))>0) {
	    	bd = BigDecimal.ZERO;
	    }
		return bd;
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
