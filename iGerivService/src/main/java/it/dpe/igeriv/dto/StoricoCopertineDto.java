package it.dpe.igeriv.dto;

import it.dpe.igeriv.vo.pk.PeriodicitaPk;
import it.dpe.igeriv.vo.pk.StoricoCopertinePk;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoricoCopertineDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private StoricoCopertinePk pk;
	private Integer codicePubblicazione;
	private Integer complementoDistribuzione;
	private String numeroCopertina;
	private Timestamp dataUscita;
	private String titolo;
	private String sottoTitolo;
	private String codiceBarre;
	private Timestamp dataRichiamoResa;
	private Integer tipoRichiamoResa;
	private Integer codiceInforete;
	private String numeroCopertinaInforete;
	private BigDecimal prezzoCopertina;
	private BigDecimal percIva;
	private BigDecimal compensoCompiegamento;
	private Integer indComponentePaccotto;
	private Integer multiplo;
	private Integer idtnr;
	private String nomeImmagine;
	private Integer fornitoBolla;
	private Integer fornitoFondoBolla;
	private Integer fornitoEstrattoConto;
	private BigDecimal prezzoEdicola;
	private Integer quantitaCopieContoDeposito;
	private String motivoResaRespinta;
	private Integer codMotivoRespinto;
	private PeriodicitaPk periodicita;
	private String cesta;
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			return ((StoricoCopertineDto) obj).getPk().equals(this.getPk());
		}
		return false;
	}
	
}
