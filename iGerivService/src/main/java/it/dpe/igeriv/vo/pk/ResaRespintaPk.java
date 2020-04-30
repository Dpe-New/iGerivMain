package it.dpe.igeriv.vo.pk;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class ResaRespintaPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9636")
	private Integer codFiegDl;
	@Column(name = "crivw9636")
	private Integer codEdicola;
	@Column(name = "datbr9636")
	private Timestamp dtBolla;
	@Column(name = "tipbr9636")
	private String tipoBolla;
	@Column(name = "idtn9636")
	private Integer idtn;
}
