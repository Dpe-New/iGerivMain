package it.dpe.igeriv.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Causali movimenti estratto conto clienti edicola 
 * 
 * @author mromano
 *
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9629", schema = "")
public class CausaleMovimentiEstrattoContoVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "idcau9629")
	private Integer codice;
	@Column(name = "decau9629")
	private String descrizione;
	@Column(name = "ticau9629")
	private Integer tipo;
}
