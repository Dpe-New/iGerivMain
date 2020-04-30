package it.dpe.inforiv.dto.output;

import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.inforiv.dto.input.InforivBaseDto;
import it.dpe.inforiv.dto.input.InforivDto;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author romanom
 * 
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Component("InforivTotaleBollaResaEdicolaDto")
@Scope("prototype")
public class InforivTotaleBollaResaEdicolaDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private static final int lineLength = 142;
	private String tipoRecord;		
	private Integer codFiegDl;
	private Integer codEdicola;
	private Date dataBolla;
	private String tipoBolla;
	private Long numeroBolla;
	private String descrizioneBolla;
	@Getter(AccessLevel.NONE)
	private Integer totaleCopieResa;
	@Getter(AccessLevel.NONE)
	private Float totaleBollaResa;
	private Float valoreFuoriResa;
	private Float totaleFieg;
	private Float totlaeAltri;
	private Float valoreBuoni;
	private Integer numeroPacchi;
	private Integer numeroCeste;
	private Integer numeroCesteVuote;
	private String note;
	@Getter(AccessLevel.NONE)
	private Integer totaleCopieBollaResaDettaglio;
	private BigDecimal totaleBollaResaDettaglio;
	@Getter(AccessLevel.NONE)
	private Integer totaleCopieBollaFuoriResa;
	@Getter(AccessLevel.NONE)
	private BigDecimal totaleBollaFuoriResa;
	@Getter(AccessLevel.NONE)
	private Integer totaleCopieBollaResaDimenticata;
	@Getter(AccessLevel.NONE)
	private BigDecimal totaleBollaResaDimenticata;
	
	public Integer getTotaleCopieResa() {
		return getTotaleCopieBollaResaDettaglio() + getTotaleCopieBollaFuoriResa() + getTotaleCopieBollaResaDimenticata();
	}

	public Float getTotaleBollaResa() {
		return getTotaleBollaResaDettaglio().add(getTotaleBollaFuoriResa()).add(getTotaleBollaResaDimenticata()).floatValue();
	}

	public Integer getTotaleCopieBollaResaDettaglio() {
		return totaleCopieBollaResaDettaglio == null ? 0 : totaleCopieBollaResaDettaglio;
	}

	public BigDecimal getTotaleBollaResaDettaglio() {
		return totaleBollaResaDettaglio == null ? BigDecimal.ZERO : totaleBollaResaDettaglio;
	}

	public Integer getTotaleCopieBollaFuoriResa() {
		return totaleCopieBollaFuoriResa == null ? 0 : totaleCopieBollaFuoriResa;
	}

	public BigDecimal getTotaleBollaFuoriResa() {
		return totaleBollaFuoriResa == null ? BigDecimal.ZERO : totaleBollaFuoriResa;
	}

	public Integer getTotaleCopieBollaResaDimenticata() {
		return totaleCopieBollaResaDimenticata == null ? 0 : totaleCopieBollaResaDimenticata;
	}

	public BigDecimal getTotaleBollaResaDimenticata() {
		return totaleBollaResaDimenticata == null ? BigDecimal.ZERO : totaleBollaResaDimenticata;
	}

	@Override
	public void validate(String riga) throws InvalidRecordException {
		super.validate(riga);
		if (riga.length() != lineLength) {
			throw new InvalidRecordException(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.lunghezza.errata.riga"), riga.length(), lineLength));
		} else if (getCodFiegDl() == null || getCodFiegDl().equals(0)) {
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
