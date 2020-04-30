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

import lombok.Getter;
import lombok.Setter;

/**
 * Statistiche sulle pagina
 * 
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9720", schema = "")
public class StatistichePagineVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codst9720")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName="SEQUENZA_9720", allocationSize = 1)
	private Long codStatistica;
	@Column(name = "crivw9720")
	private Integer codEdicola;
	@Column(name = "codut9720")
	private String codUtente;
	@Column(name = "codpa9720")
	private Integer codPagina;
	@Column(name = "datin9720")
	private Timestamp dataIngresso;
	@Column(name = "datus9720")
	private Timestamp dataUscita;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "codpa9720", updatable = false, insertable = false, referencedColumnName = "codpa9721")
	private PaginaMonitorStatisticaVo pagina;
}
