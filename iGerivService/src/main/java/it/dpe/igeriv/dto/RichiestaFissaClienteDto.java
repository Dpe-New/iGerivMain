package it.dpe.igeriv.dto;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.pk.RichiestaFissaClienteEdicolaPk;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class RichiestaFissaClienteDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private RichiestaFissaClienteEdicolaPk pk;
	private String titolo;
	private String sottoTitolo;
	private String numeroCopertina;
	private Integer provenienza;
	private Integer statoEvasione;
	private Date dataOrdine;
	private Integer quantitaRichiesta;
	private Integer quantitaEvasa;
	private Integer idtn;
	
	public String getStatoEvasioneDesc() {
		return IGerivMessageBundle.get(IGerivConstants.STATO_PRENOTAZIONE_FISSA);
	}
	
	public String getProvenienzaDesc() {
		return null;
	}
}
