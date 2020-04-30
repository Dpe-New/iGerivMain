package it.dpe.inforiv.dto.output;

import it.dpe.inforiv.dto.input.InforivBaseDto;
import it.dpe.inforiv.dto.input.InforivDto;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author romanom
 * 
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Component("InforivVariazioniServizioDto")
@Scope("prototype")
public class InforivVariazioniServizioDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private String tipoRecord;
	private Integer codFiegDl;
	private Integer codEdicola;
	private Integer codiceTestata;
	private String titolo;
	private Integer fornitoUltimoNumero;
	private Integer giacenzaUltimoNumero;
	private Date dataInizioVariazione;
	private Date dataFineVariazione;
	private String numeroInizioVariazione;
	private String numeroFineVariazione;
	private Integer copieRichieste;
	private String note;
}
