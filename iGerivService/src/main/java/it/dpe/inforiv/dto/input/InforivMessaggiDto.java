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
@Component("InforivMessaggiDto")
@Scope("prototype")
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
	
	@Override
	public void validate(String riga) throws InvalidRecordException {
		super.validate(riga);
		if (riga.length() != lineLength) {
			throw new InvalidRecordException(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.lunghezza.errata.riga"), riga.length(), lineLength));
		} else if (getCodFiegDl() == null || getCodFiegDl().equals(0)) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.codice.dl.nullo"));
		} else if (getCodEdicola() == null || getCodEdicola().equals(0)) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.edicola.nullo"));
		} else if (getDataBollaEstrattoContoMessaggio() == null) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.data.bolla.nulla"));
		}
	}

}
