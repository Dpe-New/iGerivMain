package it.dpe.igeriv.vo;

import it.dpe.igeriv.enums.StatoRichiestaLivellamento;

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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_9131b", schema = "")
public class LivellamentiVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "idliv9131b")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName="SEQUENZA_9131b", allocationSize = 1)
	private Long idLivellamento;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "idric9131b", updatable = true, insertable = true, referencedColumnName = "idric9131a")
	private RichiestaRifornimentoLivellamentiVo richiesta;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "crivw9131b", updatable = true, insertable = true, referencedColumnName = "crivw9106")
	private AnagraficaEdicolaVo edicola;
	@Column(name = "quant9131b")
	private Integer quantita;
	@Column(name = "stato9131b")
	private StatoRichiestaLivellamento statoRichiesta;
	@Column(name = "statov9131b")
	private StatoRichiestaLivellamento statoVendita;
	@Column(name = "datac9131b")
	private Timestamp dataConferma;
	@Column(name = "dataa9131b")
	private Timestamp dataAccettazione;
	@Column(name = "datav9131b")
	private Timestamp dataVendita;
	@Column(name = "dsca9131b")
	private Timestamp dataScadenza;
}
