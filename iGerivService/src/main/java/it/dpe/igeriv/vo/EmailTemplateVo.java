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

/**
 * Email template per email rivendita - clienti
 * 
 * @author mromano
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9644", schema = "")
public class EmailTemplateVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codte9644")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName="SEQUENZA_9644", allocationSize = 1)
	private Integer codice;
	@Column(name = "crivw9644")
	private Integer codEdicola;
	@Column(name = "nomte9644")
	private String nome;
	@Column(name = "conte9644")
	private String contenuto;
}
