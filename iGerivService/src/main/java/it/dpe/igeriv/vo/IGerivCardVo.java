package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.IGerivCardPk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_9307", schema = "")
public class IGerivCardVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private IGerivCardPk pk;
	@Column(name = "ccli9307")
	private Long codCliente;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ccli9307", updatable = false, insertable = false, referencedColumnName = "ccli9305")
	private ClienteEdicolaVo cliente;
}
