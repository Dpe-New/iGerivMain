package it.dpe.igeriv.vo.pk;

import it.dpe.igeriv.util.DateUtilities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class BollaResaRiassuntoPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9620")
	private Integer codFiegDl;
	@Column(name = "crivw9620")
	private Integer codEdicola;
	@Column(name = "datbr9620")
	private Timestamp dtBolla;
	@Column(name = "tipbr9620")
	private String tipoBolla;
	
	@Override
	public String toString() {
		return getCodFiegDl() + "|" + getCodEdicola() + "|" + DateUtilities.getTimestampAsString(getDtBolla(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS) + "|" + getTipoBolla();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dtBolla == null) ? 0 : dtBolla.hashCode());
		result = prime * result + ((tipoBolla == null) ? 0 : tipoBolla.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BollaResaRiassuntoPk other = (BollaResaRiassuntoPk) obj;
		if (dtBolla == null) {
			if (other.dtBolla != null)
				return false;
		} else if (!dtBolla.equals(other.dtBolla))
			return false;
		if (tipoBolla == null) {
			if (other.tipoBolla != null)
				return false;
		} else if (!tipoBolla.equals(other.tipoBolla))
			return false;
		return true;
	}

}
