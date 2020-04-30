package it.dpe.igeriv.vo;

import java.math.BigDecimal;
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

/**
 * @author mromano
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9627", schema = "")
public class MovimentiEstrattoContoClientiVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "idmov9627")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName="SEQUENZA_9627", allocationSize = 1)
	private Long idMovimento;
	@Column(name = "crivw9627")
	private Integer codEdicola;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ccli9627", insertable = true, updatable = true, referencedColumnName = "ccli9305")
	private ClienteEdicolaVo cliente;
	@Column(name = "damov9627")
	private Timestamp dataMovimento;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "idcau9627", insertable = true, updatable = true, referencedColumnName = "idcau9629")
	private CausaleMovimentiEstrattoContoVo causale;
	@Column(name = "imp9627")
	private BigDecimal importo;
	@Column(name = "dataec9627")
	private Timestamp dataEstrattoConto;
	@Column(name ="dacomp9627")
	private Timestamp dataCompetenza;
}
