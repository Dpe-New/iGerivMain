package it.dpe.igeriv.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella pagina da monitorare per le statistiche
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9721", schema = "")
public class PaginaMonitorStatisticaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codpa9721")
	private Integer codPagina;
	@Column(name = "nompa9721")
	private String nomePagina;
}
