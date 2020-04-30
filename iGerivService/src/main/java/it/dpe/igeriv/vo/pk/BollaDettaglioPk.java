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
public class BollaDettaglioPk extends BasePk implements IBollaDettaglioPk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9611")
	private Integer codFiegDl;
	@Column(name = "crivw9611")
	private Integer codEdicola;
	@Column(name = "datbc9611")
	private Timestamp dtBolla;
	@Column(name = "tipbc9611")
	private String tipoBolla;
	@Column(name = "posiz9611")
	private Integer posizioneRiga;
	
	@Override
	public String toString() {
		return getCodFiegDl() + "|" + getCodEdicola() + "|" + DateUtilities.getTimestampAsString(getDtBolla(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS) + "|" + getTipoBolla() + "|" + getPosizioneRiga();
	}
}
