package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class GiroTipoPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9110")
	private Integer codFiegDl;
	@Column(name = "gti9110")
	private Integer giroTipo;
	@Column(name = "giro9110")
	private Integer giro;
}
