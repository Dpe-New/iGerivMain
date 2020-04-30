package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class ProdottiNonEditorialiPrezziAcquistoPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "crivw9540")
	private Integer codEdicola;
	@Column(name = "cpro9540")
	private Long codProdotto;
	@Column(name = "cprofo9540") 
	private String codiceProdottoFornitore;
}
