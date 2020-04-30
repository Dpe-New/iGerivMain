package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.MotivoResaRespintaPk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella decodifica dei motivi respinti.
 * 
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9623", schema = "") 
public class MotivoResaRespintaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private MotivoResaRespintaPk pk;
	@Column(name = "descr9623")
	private String descrizione;
}
