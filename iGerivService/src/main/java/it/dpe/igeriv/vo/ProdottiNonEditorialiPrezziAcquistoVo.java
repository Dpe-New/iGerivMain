package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiPrezziAcquistoPk;

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
 * Tabella Prodotti Giacenze Non Editoriali
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9540", schema = "")
public class ProdottiNonEditorialiPrezziAcquistoVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private ProdottiNonEditorialiPrezziAcquistoPk pk;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "crivw9540", updatable = false, insertable = false, referencedColumnName = "crivw9106")
	private AnagraficaEdicolaVo edicola;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "crivw9540", updatable = false, insertable = false, referencedColumnName = "crivw9506"),
		@JoinColumn(name = "cpro9540", updatable = false, insertable = false, referencedColumnName = "cpro9506")
	})
	private ProdottiNonEditorialiVo prodotto;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "crivw9540", updatable = false, insertable = false, referencedColumnName = "crivw9500"),
		@JoinColumn(name = "cforn9540", updatable = false, insertable = false, referencedColumnName = "cforn9500")
	})
	private ProdottiNonEditorialiFornitoreVo fornitore;
	@Column(name = "cforn9540")
	private Integer codiceFornitore;
	@Column(name = "pda9540")
	private Float ultimoPrezzoAcquisto;
}
