package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class ZonaTipoPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9111")
	private Integer codFiegDl;
	@Column(name = "zti9111")
	private Integer zonaTipo;
	@Column(name = "zona9111")
	private Integer zona;
}
