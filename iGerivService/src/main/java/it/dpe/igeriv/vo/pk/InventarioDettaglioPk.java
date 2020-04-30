package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class InventarioDettaglioPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "idinve9751")
	private Long idInventario;
	@Column(name = "idtnpu9751")
	private Integer idtn;

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof InventarioDettaglioPk) {
			return ((InventarioDettaglioPk) obj).getIdtn().equals(this.getIdtn()) && ((InventarioDettaglioPk) obj).getIdInventario().equals(this.getIdInventario());
		}
		return super.equals(obj);
	}
}
