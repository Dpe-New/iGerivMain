package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.AbbinamentoTipoMovimentoFondoBollaInforivPk;

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
@Table(name = "tbl_9604", schema = "")
public class AbbinamentoTipoMovimentoFondoBollaInforivVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private AbbinamentoTipoMovimentoFondoBollaInforivPk pk;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "trfon9604", updatable = false, insertable = false, referencedColumnName = "trfon9614")	
	private TipoFondoBollaVo tipoFondoBollaVo;
}
