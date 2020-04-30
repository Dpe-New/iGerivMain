package it.dpe.igeriv.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_9351", schema = "")
public class ProfessioneVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codprof9351")
	private Integer codice;
	@Column(name = "descpro9351")
	private String descrizione;
}
