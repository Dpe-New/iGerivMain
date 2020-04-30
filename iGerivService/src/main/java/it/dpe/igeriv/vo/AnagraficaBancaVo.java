package it.dpe.igeriv.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_9304", schema = "")
public class AnagraficaBancaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codban9304")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName="SEQUENZA_9304", allocationSize = 1)
	private Integer codBanca;
	@Column(name = "nomban9304")
	private String nome;
}
