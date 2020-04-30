package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class ProdottiNonEditorialiSottoCategoriaEdicolaPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "cat9524")
	private Long codCategoria;
	@Column(name = "scat9524")
	private Long codSottoCategoria;
	@Column(name = "crivw9524")
	private Integer codEdicola;
	
	@Override
	public String toString() {
		return getCodCategoria() + "|" + getCodSottoCategoria();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			ProdottiNonEditorialiSottoCategoriaEdicolaPk opk = (ProdottiNonEditorialiSottoCategoriaEdicolaPk) obj;
			return getCodCategoria().equals(opk.getCodCategoria()) && getCodSottoCategoria().equals(opk.getCodSottoCategoria());
		}
		return super.equals(obj);
	}
	
}
