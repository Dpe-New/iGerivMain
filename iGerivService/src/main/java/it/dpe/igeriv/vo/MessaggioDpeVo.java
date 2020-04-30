package it.dpe.igeriv.vo;

import java.sql.Timestamp;

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
 * Tabella dei messaggi inviati da DPE
 * 
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9650", schema = "")
public class MessaggioDpeVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codme9650")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName="SEQUENZA_9650", allocationSize = 1)
	private Long codice;
	@Column(name = "datam9650")
	private Timestamp data;
	@Column(name = "titol9650")
	private String titolo;
	@Column(name = "testo9650")
	private String testo;
	@Column(name = "urlme9650")
	private String url;
	@Column(name = "prior9650")
	private Integer priorita;
	@Column(name = "abili9650")
	private Boolean abilitato;
}
