package it.dpe.igeriv.vo.pk;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class ResaRiscontrataPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9635")
	private Integer codFiegDl;
	@Column(name = "crivw9635")
	private Integer codEdicola;
	@Column(name = "datbr9635")
	private Timestamp dtBolla;
	@Column(name = "tipbr9635")
	private String tipoBolla;
	@Column(name = "idtn9635")
	private Integer idtn;
}
