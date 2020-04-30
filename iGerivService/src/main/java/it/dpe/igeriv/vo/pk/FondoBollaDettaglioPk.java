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
public class FondoBollaDettaglioPk extends BasePk implements IBollaDettaglioPk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9612")
	private Integer codFiegDl;
	@Column(name = "crivw9612")
	private Integer codEdicola;
	@Column(name = "datbc9612")
	private Timestamp dtBolla;
	@Column(name = "tipbc9612")
	private String tipoBolla;
	@Column(name = "posiz9612")
	private Integer posizioneRiga;
	
	@Override
	public String toString() {
		return getCodFiegDl() + "|" + getCodEdicola() + "|" + DateUtilities.getTimestampAsString(getDtBolla(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS) + "|" + getTipoBolla() + "|" + getPosizioneRiga();
	}
}
