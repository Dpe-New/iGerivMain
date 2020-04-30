package it.dpe.igeriv.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9614", schema = "")
public class TipoFondoBollaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "trfon9614")
	private Integer tipoRecordFondoBolla;
	@Column(name = "descr9614")
	private String descrizione;
}
