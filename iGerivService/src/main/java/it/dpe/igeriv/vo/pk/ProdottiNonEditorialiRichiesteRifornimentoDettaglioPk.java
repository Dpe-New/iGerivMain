package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class ProdottiNonEditorialiRichiesteRifornimentoDettaglioPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "ordine9533")
	private Long codRichiestaRifornimento;
	@Column(name = "cpro9533")
	private Long codProdottoInterno;
}
