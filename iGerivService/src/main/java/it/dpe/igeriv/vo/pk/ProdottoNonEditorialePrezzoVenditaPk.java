package it.dpe.igeriv.vo.pk;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class ProdottoNonEditorialePrezzoVenditaPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "crivw9508")
	private Integer codEdicola;
	@Column(name = "cpro9508")
	private Long codProdottoInterno;
	@Column(name = "dvf9508")
	private Date dataValiditaFinale;
	
	@Override
	public String toString() {
		return getCodEdicola() + "|" + getCodProdottoInterno() + "|" + getDataValiditaFinale();
	}

}
