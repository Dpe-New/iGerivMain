package it.dpe.igeriv.dto;

import it.dpe.igeriv.vo.BollaResa;
import it.dpe.igeriv.vo.pk.IBollaDettaglioPk;
import it.dpe.igeriv.vo.pk.PeriodicitaPk;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.google.common.base.Strings;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper=false)
public class BollaResaBaseDto extends BaseDto implements BollaResa {
	private static final long serialVersionUID = 1L;
	private IBollaDettaglioPk pk;
	private Integer cpuDl;
	private Integer idtn;
	private String barcode;
	private String titolo;
	private String sottoTitolo;
	@Getter(AccessLevel.NONE)
	private String numeroPubblicazione;
	private Long distribuito;
	private Integer reso;
	private Long giacenza;
	private Timestamp dataUscita;
	private BigDecimal prezzoLordo;
	private BigDecimal prezzoNetto;
	private String tipoRichiamo;
	private String tipoRichiamoExt;
	private String immagine;
	private int tipo;
	private Integer rownum;
	private String note;
	private String noteByCpu;
	private PeriodicitaPk periodicitaPk;
	@Getter(AccessLevel.NONE)
	private Integer idtnContoDeposito;
	private Integer qtaContoDeposito;
	private Boolean resaAnticipata;
	private Integer ordini;
	private String cesta;
	
	public void setCpuDl(Integer cpuDl) {
		this.cpuDl = cpuDl;
	}
	
	public String getNumeroPubblicazione() {
		return numeroPubblicazione != null ? numeroPubblicazione.trim() : numeroPubblicazione;
	}

	public BigDecimal getImportoLordo() {
		return (getPrezzoLordo() != null && reso != null) ? getPrezzoLordo()
				.multiply(new BigDecimal(reso)) : new BigDecimal(0);
	}

	public BigDecimal getImportoNetto() {
		return (getPrezzoNetto() != null && reso != null) ? getPrezzoNetto()
				.multiply(new BigDecimal(reso)) : new BigDecimal(0);
	}

	public Integer getIdtnContoDeposito() {
		return (getQtaContoDeposito() != null && getQtaContoDeposito() > 0) ? idtnContoDeposito : null;
	}
	
	public String getNoteRivendita() {
		return !Strings.isNullOrEmpty(getNote()) ? getNote() : (!Strings.isNullOrEmpty(getNoteByCpu()) ? getNoteByCpu() : "");
	}
	
	@Override
	public void accept(VisitorDto visitor) {
		visitor.visit(this);
	}
	
}
