package it.dpe.igeriv.vo.pk;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class MessaggiBollaPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9615")
	private Integer codFiegDl;
	@Column(name = "crivw9615")
	private Integer codEdicola;
	@Column(name = "datbc9615")
	private Timestamp dtBolla;
	@Column(name = "tipbc9615")
	private String tipoBolla;
	@Column(name = "times9615")
	private Integer tipoMessaggio;
	@Column(name = "progr9615")
	private Integer progressivo;
}
