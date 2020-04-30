package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class RichiestaFissaClienteEdicolaPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "crivw9313")
	private Integer codEdicola;
	@Column(name = "ccli9313")
	private Long codCliente;
	@Column(name = "codl9313")
	private Integer codDl;
	@Column(name = "cpu9313")
	private Integer codicePubblicazione;
	
	@Override
	public String toString() {
		return getCodEdicola() + "|" + getCodCliente() + "|" + getCodDl() + "|" + getCodicePubblicazione();
	}

}
