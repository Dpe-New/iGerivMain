package it.dpe.inforiv.dto.input;

import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;

import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;

/**
 * @author romanom
 */
@Component("InforivMessaggiDto")
@Scope("prototype")
@Record
public class InforivMessaggiDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private static final int lineLength = 894;
	private String tipoRecord;		
	private Integer codFiegDl;
	private Integer codEdicola;
	private String idProdotto;
	private Date dataBollaEstrattoContoMessaggio;
	private String tipoBolla;
	private Long numeroBolla;
	private Long numeroEstrattoContoRivendita;
	private String causaleMesaggio;
	private Date dataInizioValiditaMessaggio;
	private Date dataFineValiditaMessaggio;
	private String priorita;
	private String testo;
	
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
	
	@Field(offset = 28, length = 8)
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataBollaEstrattoContoMessaggio() {
		return dataBollaEstrattoContoMessaggio;
	}

	public void setDataBollaEstrattoContoMessaggio(Date dataBollaEstrattoContoMessaggio) {
		this.dataBollaEstrattoContoMessaggio = dataBollaEstrattoContoMessaggio;
	}
	
	@Field(offset = 36, length = 2)
	public String getTipoBolla() {
		return tipoBolla != null ? tipoBolla.trim() : tipoBolla;
	}

	public void setTipoBolla(String tipoBolla) {
		this.tipoBolla = tipoBolla;
	}
	
	@Field(offset = 38, length = 10, paddingChar = '0')
	public Long getNumeroBolla() {
		return numeroBolla;
	}

	public void setNumeroBolla(Long numeroBolla) {
		this.numeroBolla = numeroBolla;
	}
	
	@Field(offset = 48, length = 10, paddingChar = '0')
	public Long getNumeroEstrattoContoRivendita() {
		return numeroEstrattoContoRivendita;
	}

	public void setNumeroEstrattoContoRivendita(Long numeroEstrattoContoRivendita) {
		this.numeroEstrattoContoRivendita = numeroEstrattoContoRivendita;
	}
	
	@Field(offset = 58, length = 20)
	public String getCausaleMesaggio() {
		return causaleMesaggio != null ? causaleMesaggio.trim() : causaleMesaggio;
	}

	public void setCausaleMesaggio(String causaleMesaggio) {
		this.causaleMesaggio = causaleMesaggio;
	}
	
	@Field(offset = 78, length = 8)
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataInizioValiditaMessaggio() {
		return dataInizioValiditaMessaggio;
	}

	public void setDataInizioValiditaMessaggio(Date dataInizioValiditaMessaggio) {
		this.dataInizioValiditaMessaggio = dataInizioValiditaMessaggio;
	}
	
	@Field(offset = 86, length = 8)
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataFineValiditaMessaggio() {
		return dataFineValiditaMessaggio;
	}

	public void setDataFineValiditaMessaggio(Date dataFineValiditaMessaggio) {
		this.dataFineValiditaMessaggio = dataFineValiditaMessaggio;
	}
	
	@Field(offset = 94, length = 1)
	public String getPriorita() {
		return priorita != null ? priorita.trim() : priorita;
	}

	public void setPriorita(String priorita) {
		this.priorita = priorita;
	}
	
	@Field(offset = 95, length = 800)
	public String getTesto() {
		return testo != null ? testo.trim() : testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
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
		} else if (getDataBollaEstrattoContoMessaggio() == null) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.data.bolla.nulla"));
		}
	}

}
