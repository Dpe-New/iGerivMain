package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.ZonaTipoPk;

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
@Table(name = "tbl_9111", schema = "")
public class ZonaTipoVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private ZonaTipoPk pk;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "crivw9111", updatable = false, insertable = false, referencedColumnName = "crivw9106")
	private AnagraficaEdicolaVo edicola;
}
