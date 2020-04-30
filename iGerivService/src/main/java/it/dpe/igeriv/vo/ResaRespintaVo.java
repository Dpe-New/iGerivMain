package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.ResaRespintaPk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella Resa respinta dal DL
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9636", schema = "")
public class ResaRespintaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private ResaRespintaPk pk;
	@Column(name = "cpu9636")
	private Integer cpu;
	@Column(name = "titolo9636")
	private String titolo;
	@Column(name = "num9636")
	private String numero;
	@Column(name = "prne9636")
	private Float prezzoNetto;
	@Column(name = "qresor9636")
	private Integer resoRespinto;
	@Column(name = "descr9636")
	private String motivo;
}
