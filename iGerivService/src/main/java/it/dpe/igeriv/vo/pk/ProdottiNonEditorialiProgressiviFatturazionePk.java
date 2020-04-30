package it.dpe.igeriv.vo.pk;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class ProdottiNonEditorialiProgressiviFatturazionePk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "crivw9502")
	private Integer codEdicola;
	@Column(name = "tdoc9502")
	private Integer tipoDocumento;
	@Column(name = "data9502")
	private Date data;
}
