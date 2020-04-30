package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.AbbinamentiIdtnInforivPk;

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
@Table(name = "tbl_9605", schema = "")
public class AbbinamentoIdtnInforivVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private AbbinamentiIdtnInforivPk pk;
	@Column(name = "idtn9605")
	private Integer idtn;
}
