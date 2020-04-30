package it.dpe.igeriv.vo;

import java.sql.Timestamp;

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

import org.apache.xpath.operations.Bool;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella emails inviati da rivendita al DL.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9603", schema = "")
public class EmailRivenditaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "idema9603")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName="SEQUENZA_9603", allocationSize = 1)
	private Integer idEmailRivendita;
	@Column(name = "datin9603")
	private Timestamp dataMessaggio;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "crivw9603", updatable = true, insertable = true, referencedColumnName = "crivw9106")
	private AnagraficaEdicolaVo edicola;
	@Column(name = "titol9603")
	private String titolo; 
	@Column(name = "messa9603")
	private String messaggio; 
	@Column(name = "detin9603")
	private String destinatari;
	@Column(name = "alleg9603")
	private String allegato;
	@Column(name = "letto9603")
	private Boolean letto;
	@Column(name="send9603")
	private Boolean isSend;
}
