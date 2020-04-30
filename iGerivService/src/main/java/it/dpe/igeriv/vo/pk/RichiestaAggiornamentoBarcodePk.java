package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class RichiestaAggiornamentoBarcodePk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9651")
	private Integer codFiegDl;
	@Column(name = "idtn9651")
	private Integer idtn;
	@Column(name = "barcode9651")
	private String codiceBarre;
}
