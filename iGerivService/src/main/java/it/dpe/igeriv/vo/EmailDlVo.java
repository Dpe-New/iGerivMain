package it.dpe.igeriv.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella utenti
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9222", schema = "")
public class EmailDlVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "idema9222")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName="SEQUENZA_9222", allocationSize = 1)
	private Integer codEmailVo;
	@Column(name = "repa9222")
	private String reparto;
	@Column(name = "nome9222")
	private String nome;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "coddl9222", updatable = false, insertable = false, referencedColumnName = "coddl9107")
	private AnagraficaAgenziaVo anagraficaAgenziaVo;
	@Column(name = "email9222")
	private String email;
}
