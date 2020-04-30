package it.dpe.inforiv.dto.input;

import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;

import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;

/**
 * @author romanom
 */
@Component("InforivFornitureSonoInoltreUsciteDto")
@Scope("prototype")
@Record
public class InforivFornitureSonoInoltreUsciteDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private static final int lineLength = 89;
	private String tipoRecord;		
	private Integer codFiegDl;
	private Integer codEdicola;
	private Date dataBolla;
	private String tipoBolla;
	private Long numeroBolla;
	private String descrizioneBolla;
	private String idProdotto;
	private String fascicoloGuida;
	private Integer numeroUscitePerArretrato;
	private Float maggiorazioneArretrati;
	
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
	
	@Field(offset = 50, length = 18)
	public String getIdProdotto() {
		return idProdotto != null ? idProdotto.trim() : idProdotto;
	}

	public void setIdProdotto(String idProdotto) {
		this.idProdotto = idProdotto;
	}
	
	@Field(offset = 68, length = 14)
	public String getFascicoloGuida() {
		return fascicoloGuida != null ? fascicoloGuida.trim() : fascicoloGuida;
	}

	public void setFascicoloGuida(String fascicoloGuida) {
		this.fascicoloGuida = fascicoloGuida;
	}
	
	@Field(offset = 82, length = 2, paddingChar = '0')
	public Integer getNumeroUscitePerArretrato() {
		return numeroUscitePerArretrato;
	}

	public void setNumeroUscitePerArretrato(Integer numeroUscitePerArretrato) {
		this.numeroUscitePerArretrato = numeroUscitePerArretrato;
	}
	
	//@Field(offset = 90, length = 6, align = Align.RIGHT)
	public Float getMaggiorazioneArretrati() {
		return maggiorazioneArretrati;
	}

	public void setMaggiorazioneArretrati(Float maggiorazioneArretrati) {
		this.maggiorazioneArretrati = maggiorazioneArretrati;
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
