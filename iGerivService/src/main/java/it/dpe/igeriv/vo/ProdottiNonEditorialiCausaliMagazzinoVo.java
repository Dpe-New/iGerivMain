package it.dpe.igeriv.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella Causali Magazzino dei Prodotti Non Editoriali
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9550", schema = "")
public class ProdottiNonEditorialiCausaliMagazzinoVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "cau9550")
	private Integer codiceCausale;
	@Column(name = "des9550")
	private String descrizione;
	@Column(name = "dmag9550")
	private Integer magazzinoDa;
	@Column(name = "amag9550")
	private Integer magazzinoA;
}
