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
@Component("InforivStatisticaStoricoDto")
@Scope("prototype")
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
	
	@Override
	public void validate(String riga) throws InvalidRecordException {
		super.validate(riga);
		if (riga.length() != lineLength) {
			throw new InvalidRecordException(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.lunghezza.errata.riga"), riga.length(), lineLength));
		} else if (getCodFiegDl() == null || getCodFiegDl().equals(0)) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.codice.dl.nullo"));
		} else if (getCodEdicola() == null || getCodEdicola().equals(0)) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.edicola.nullo"));
		} else if (getIdProdotto() == null || getIdProdotto().equals("")) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.idtn.nullo"));
		} 
	}
}
