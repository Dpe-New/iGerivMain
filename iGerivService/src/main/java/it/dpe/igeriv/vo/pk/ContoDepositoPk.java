package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class ContoDepositoPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9618")
	private Integer codDl;
	@Column(name = "crivw9618")
	private Integer codEdicola;
	@Column(name = "idtn9618")
	private Integer idtn;
	
	@Override
	public String toString() {
		return getCodDl() + "|" + getCodEdicola() + "|" + getIdtn();
	}

}
