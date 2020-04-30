package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiGiacenzePk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "tbl_9507", schema = "")
public class ProdottiNonEditorialiGiacenzeVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private ProdottiNonEditorialiGiacenzePk pk;
	@Column(name = "giac9507")
	private Integer giacenza;
	@ManyToOne
	@JoinColumn(name = "coddl9507", insertable = false, updatable = false, referencedColumnName = "coddl9107")
	private AnagraficaAgenziaVo agenzia;
}
