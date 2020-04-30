package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.pk.ResaRiscontrataPk;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella Resa riscontrata dal DL
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
//@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "tbl_9635", schema = "")
public class ResaRiscontrataVo extends BaseVo implements BollaResaRiassunto {
	private static final long serialVersionUID = 1L;
	@Id
	private ResaRiscontrataPk pk;
	@Column(name = "cpu9635")
	private Integer cpu;
	@Column(name = "titolo9635")
	private String titolo;
	@Column(name = "num9635")
	private String numero;
	@Column(name = "prne9635")
	private Float prezzoNetto;
	@Column(name = "qresor9635")
	private Integer resoRiscontrato;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "coddl9635", updatable = false, insertable = false, referencedColumnName = "coddl9620"),
			@JoinColumn(name = "crivw9635", updatable = false, insertable = false, referencedColumnName = "crivw9620"),
			@JoinColumn(name = "datbr9635", updatable = false, insertable = false, referencedColumnName = "datbr9620"),
			@JoinColumn(name = "tipbr9635", updatable = false, insertable = false, referencedColumnName = "tipbr9620")
	})
	private BollaResaRiassuntoVo bollaResaRiassunto;
	
	@Override
	public Timestamp getDtBolla() {
		return getPk().getDtBolla();
	}

	@Override
	public String getTipoBolla() {
		return getPk().getTipoBolla();
	}

	@Override
	public Integer getGruppoSconto() {
		return 1;
	}

	@Override
	public Integer getBollaTrasmessaDl() {
		return IGerivConstants.INDICATORE_RECORD_IN_TRASMISSIONE_DL;
	}
}
