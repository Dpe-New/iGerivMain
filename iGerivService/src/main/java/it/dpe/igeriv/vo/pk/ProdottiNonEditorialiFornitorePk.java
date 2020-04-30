package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class ProdottiNonEditorialiFornitorePk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "cforn9500")
	private Integer codFornitore;
	@Column(name = "crivw9500")
	private Integer codEdicola;
}
