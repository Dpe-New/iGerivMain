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
@Component("InforivFornitureDto")
@Scope("prototype")
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
