package it.dpe.igeriv.vo;

import java.sql.Date;

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
@Table(name = "tbl_9616", schema = "")
public class PromemoriaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codpr9616")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName="SEQUENZA_9616", allocationSize = 1)
	private Long codPromemoria;
	@Column(name = "crivw9616")
	private Integer codEdicola;
	@Column(name = "testo9616")
	private String messaggio;
	@Column(name = "datam9616")
	private Date dataMessaggio;
	@Column(name = "letto9616")
	private Boolean letto;
}
