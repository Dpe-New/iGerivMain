package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class BarraSceltaRapidaDestraPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9811")
	private Integer codFiegDl;
	@Column(name = "crivw9811")
	private Integer codEdicola;
	@Column(name = "cpu9811")
	private Integer codicePubblicazione;
}
