package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.GiroTipoPk;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9110", schema = "")
public class GiroTipoVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private GiroTipoPk pk;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "crivw9110", updatable = false, insertable = false, referencedColumnName = "crivw9106")
	private AnagraficaEdicolaVo edicola;
}
