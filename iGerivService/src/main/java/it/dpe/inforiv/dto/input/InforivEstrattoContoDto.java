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
@Component("InforivEstrattoContoDto")
@Scope("prototype")
public class InforivEstrattoContoDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private static final int lineLength = 150;
	private String tipoRecord;
	private Integer codFiegDl;
	private Integer codEdicola;
	private Integer numeroEstrattoConto;
	private Date dataEstrattoConto;
	private Long numeroEstrattoContoRif;
	private Date dataEstrattoContoRif;
	private String descrizioneCausale;
	private Date dataMovimentoContabile;
	private Float importo;
	private Float importoFieg;
	private Float importoAltri;
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
		} 
	}
}
