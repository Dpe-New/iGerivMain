package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.BarraSceltaRapidaProdottiVariPk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella Pubblicazioni Più Vendute
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9812", schema = "")
public class BarraSceltaRapidaProdottiVariVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private BarraSceltaRapidaProdottiVariPk pk;
	@Column(name = "top9812")
	private Integer top;
	@Column(name = "lef9812")
	private Integer left;
	@Column(name = "wid9812")
	private Integer width;
	@Column(name = "hei9812")
	private Integer height;
	@Column(name = "vis9812")
	private Boolean barraProdottiVariVisible;
	
}
