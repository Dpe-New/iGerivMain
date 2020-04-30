package it.dpe.igeriv.vo.pk;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class PagamentoClientiEdicolaPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "ccli9632")
	private Long codCliente;
	@Column(name = "daec9632")
	private Timestamp dataCompetenzaEstrattoConto;
	@Column(name = "tidoc9632")
	private Integer tipoDocumento;
}
