package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class ProdottiNonEditorialiCausaliContabilitaPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "TIPO9509")
	private Integer tipoCausale;
	@Column(name = "CCCG9509")
	private Integer codiceCausale;
	
	@Override
	public String toString() {
		return getTipoCausale() + "|" + getCodiceCausale();
	}

}
