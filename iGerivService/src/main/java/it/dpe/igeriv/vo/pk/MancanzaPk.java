package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MancanzaPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9613")
	private Integer codDl;
	@Column(name = "crivw9613")
	private Integer codEdicola;
	@Column(name = "idtn9613")
	private Integer idtn;
}
