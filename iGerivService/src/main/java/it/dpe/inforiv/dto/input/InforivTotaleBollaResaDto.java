package it.dpe.inforiv.dto.input;

import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;

import java.text.MessageFormat;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author romanom
 * 
 */
@Getter
@Setter
@Component("InforivTotaleBollaResaDto")
@Scope("prototype")
public class InforivTotaleBollaResaDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private static final int lineLength = 142;
	private String tipoRecord;		
	private Integer codFiegDl;
	private Integer codEdicola;
	private Date dataBolla;
	private String tipoBolla;
	private Long numeroBolla;
	private String descrizioneBolla;
	private Integer totaleCopieResa;
	private Float totaleBollaResa;
	private Float valoreFuoriResa;
	private Float totaleFieg;
	private Float totlaeAltri;
	private Float valoreBuoni;
	private Integer numeroPacchi;
	private Integer numeroCeste;
	private Integer numeroCesteVuote;
	private String note;
	
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
