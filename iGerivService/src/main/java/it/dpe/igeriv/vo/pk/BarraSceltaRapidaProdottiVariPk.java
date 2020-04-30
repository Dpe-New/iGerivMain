package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class BarraSceltaRapidaProdottiVariPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9812")
	private Integer codFiegDl;
	@Column(name = "crivw9812")
	private Integer codEdicola;
}
