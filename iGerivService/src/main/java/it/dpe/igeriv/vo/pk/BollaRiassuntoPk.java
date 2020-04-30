package it.dpe.igeriv.vo.pk;

import it.dpe.igeriv.util.DateUtilities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class BollaRiassuntoPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9610")
	private Integer codFiegDl;
	@Column(name = "crivw9610")
	private Integer codEdicola;
	@Column(name = "datbc9610")
	private Timestamp dtBolla;
	@Column(name = "tipbc9610")
	private String tipoBolla;
	
	@Override
	public String toString() {
		return getCodFiegDl() + "|" + getCodEdicola() + "|" + DateUtilities.getTimestampAsString(getDtBolla(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS) + "|" + getTipoBolla();
	}

}
