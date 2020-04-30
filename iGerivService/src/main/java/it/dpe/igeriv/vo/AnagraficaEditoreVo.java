package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.AnagraficaEditorePk;

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
@Table(name = "tbl_9109", schema = "")
public class AnagraficaEditoreVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private AnagraficaEditorePk pk;
	@Column(name = "descr9109")
	private String descrizione;
}
