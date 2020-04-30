package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class ProdottiNonEditorialiGiacenzePk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9507")
	private Integer codDl;
	@Column(name = "cprofo9507")
	private String codProdottoFornitore;
	@Column(name = "maga9507")
	private Integer numeroMagazzino;
}
