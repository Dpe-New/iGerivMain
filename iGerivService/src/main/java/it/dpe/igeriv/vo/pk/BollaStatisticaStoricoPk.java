package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class BollaStatisticaStoricoPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9638")
	private Integer codFiegDl;
	@Column(name = "crivw9638")
	private Integer codEdicola;
	@Column(name = "idtn9638")
	private Integer idtn;
}
