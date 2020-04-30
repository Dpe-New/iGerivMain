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
@Table(name = "tbl_9791", schema = "")
public class DlVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "coddl9791")
	private Integer coddl;
	@Column(name = "nome9791")
	private String nome;
}
