package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class AssociazioneGruppoModuliPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "codgr9210")
	private Integer idGruppo;
	@Column(name = "codmo9210")
	private Integer idModulo;
}
