package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class ContestazioneResaPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9628")
	private Integer codFiegDl;
	@Column(name = "idcont9628")
	private Long idContestazione;
}
