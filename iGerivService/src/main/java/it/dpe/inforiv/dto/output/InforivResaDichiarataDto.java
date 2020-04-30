package it.dpe.inforiv.dto.output;

import it.dpe.inforiv.dto.input.InforivBaseDto;
import it.dpe.inforiv.dto.input.InforivDto;

import java.math.BigDecimal;
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
@Component("InforivResaDichiarataDto")
@Scope("prototype")
public class InforivResaDichiarataDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private String tipoRecord;		
	private Integer codFiegDl;
	private Integer codEdicola;
	private Date dataBolla;
	private String tipoBolla;
	private Long numeroBolla;
	private Integer causaliResa;
	private String descrizioneRichiamoResa;
	private Integer numeroRiga;
	@Getter(AccessLevel.NONE)
	private String idProdotto;
	private Integer copie;
	@Getter(AccessLevel.NONE)
	private Float prezzoCopertina;
	@Getter(AccessLevel.NONE)
	private Float prezzoNetto;
	private Float prezzoCessione;
	private Float scontoPuntoVendita;
	private Float compensoCompiegamento;
	@Getter(AccessLevel.NONE)
	private Float sviluppoNettoResa;
	@Getter(AccessLevel.NONE)
	private String flagContoDepositoNonFatturato;
	private String flagBuono;
	private String note;
	private BigDecimal prezzoCopertinaBd;
	private Integer resoInContoDeposito;
	private String idtnTrascodifica;
	private BigDecimal prezzoNettoBd;
	
	public String getIdProdotto() {
		return (idtnTrascodifica != null) ? idtnTrascodifica.toString() : idProdotto;
	}

	public Float getPrezzoCopertina() {
		if (prezzoCopertinaBd != null) {
			return prezzoCopertinaBd.floatValue();
		}
		return prezzoCopertina;
	}

	public Float getPrezzoNetto() {
		return (prezzoNettoBd != null) ? prezzoNettoBd.floatValue() : prezzoNetto;
	}

	public Float getSviluppoNettoResa() {
		if (resoInContoDeposito == null && getPrezzoNetto() != null) {
			return getPrezzoNetto() * getCopie();
		}
		return sviluppoNettoResa;
	}

	public String getFlagContoDepositoNonFatturato() {
		return (resoInContoDeposito == null) ? "N" : "S";
	}
	
}
