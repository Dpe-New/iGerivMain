package it.dpe.igeriv.dto;

import it.dpe.igeriv.vo.pk.BollaPk;
import it.dpe.igeriv.vo.pk.PeriodicitaPk;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class BollaVoDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private BollaPk pk;
	private Integer codFornitore;
	private String titolo;
	private String sottoTitolo;
	private String numeroCopertina;
	private BigDecimal prezzoCopertina;
	private String argomento;
	private String periodicita;
	private PeriodicitaPk periodicitaPk;
	private String immagine;
	private Integer idtn;
	private Integer rownum;
	private String fake;
}
