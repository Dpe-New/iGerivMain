package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class ProdottiNonEditorialiCategoriaEdicolaPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "cat9523")
	private Long codCategoria;
	@Column(name = "crivw9523")
	private Integer codEdicola;
	
	@Override
	public String toString() {
		return getCodCategoria() + "|" + getCodEdicola();
	}

}
