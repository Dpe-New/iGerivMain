package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class ProdottiNonEditorialiSottoCategoriaPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "cat9522")
	private Long codCategoria;
	@Column(name = "scat9522")
	private Long codSottoCategoria;
	
	@Override
	public String toString() {
		return getCodCategoria() + "|" + getCodSottoCategoria();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			ProdottiNonEditorialiSottoCategoriaPk opk = (ProdottiNonEditorialiSottoCategoriaPk) obj;
			return getCodCategoria().equals(opk.getCodCategoria()) && getCodSottoCategoria().equals(opk.getCodSottoCategoria());
		}
		return super.equals(obj);
	}
	
}
