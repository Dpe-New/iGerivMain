package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivConstants;

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

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

@Getter
@Setter
@Entity
@Table(name = "tbl_9309", schema = "")
public class FatturaClienteEdicolaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "idfat9309")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName="SEQUENZA_9309", allocationSize = 1)
	private Long idFattura;
	@Column(name = "tidoc9309")
	private Integer tipoDocumento;
	@Column(name = "crivw9309")
	private Integer codEdicola;
	@Column(name = "codut9309")
	private String codUtente;
	@Column(name = "cclie9309")
	private Long codCliente;
	@Column(name = "nfifa9309")
	private String nomeFileFattura;
	@Column(name = "dafat9309")
	private Timestamp dataFattura;
	@Column(name = "dcofat9309")
	private Timestamp dataCompetenzaFattura;
	@Column(name = "nufat9309")
	private Integer numeroFattura;
	@Column(name = "fattu9309")
	private byte[] fattura;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "cclie9309", insertable = false, updatable = false, referencedColumnName = "ccli9305")
	private ClienteEdicolaVo cliente;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumnsOrFormulas({
		@JoinColumnOrFormula(column = @JoinColumn(name = "cclie9309", insertable = false, updatable = false, referencedColumnName = "ccli9632")),
		@JoinColumnOrFormula(column = @JoinColumn(name = "dcofat9309", insertable = false, updatable = false, referencedColumnName = "daec9632")),
		@JoinColumnOrFormula(formula = @JoinFormula(value = "" + IGerivConstants.FATTURA, referencedColumnName = "tidoc9632"))
	})
	private PagamentoClientiEdicolaVo pagamentoClientiEdicolaVo;
}
