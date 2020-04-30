package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class ParametriEdicolaPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "crivw9710")
	private Integer codEdicola;
	@Column(name = "codpar9710")
	private Integer codParametro;
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			ParametriEdicolaPk parametriEdicolaPk = (ParametriEdicolaPk) obj;
			return parametriEdicolaPk.getCodEdicola().equals(this.getCodEdicola()) && parametriEdicolaPk.getCodParametro().equals(this.getCodParametro());
		}
		return super.equals(obj);
	}

}
