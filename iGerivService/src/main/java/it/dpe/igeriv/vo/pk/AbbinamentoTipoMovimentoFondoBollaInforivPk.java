package it.dpe.igeriv.vo.pk;

import it.dpe.igeriv.vo.TipoFondoBollaVo;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class AbbinamentoTipoMovimentoFondoBollaInforivPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@OneToOne
	@JoinColumn(name = "trfon9604", updatable = false, insertable = false, referencedColumnName = "trfon9614")
	private TipoFondoBollaVo tipoRecordFondoBolla;
	@Column(name = "trfoni9604")
	private Integer tipoRecordFondoBollaInforiv;
}
