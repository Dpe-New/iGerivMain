package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class PeriodicitaPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "tipop9216")
	private Integer tipo;
	@Column(name = "perio9216")
	private Integer periodicita;
	
	@Override
	public String toString() {
		return getTipo() + "|" + getPeriodicita();
	}

}
