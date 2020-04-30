package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class MotivoResaRespintaPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9623")
	private Integer codFiegDl;
	@Column(name = "resp9623")
	private Integer codMotivoRespinto;
}
