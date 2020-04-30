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
public class BollaResaDettaglioPk extends BasePk implements IBollaDettaglioPk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9621")
	private Integer codFiegDl;
	@Column(name = "crivw9621")
	private Integer codEdicola;
	@Column(name = "datbr9621")
	private Timestamp dtBolla;
	@Column(name = "tipbr9621")
	private String tipoBolla;
	@Column(name = "posiz9621")
	private Integer posizioneRiga;
	
	@Override
	public String toString() {
		return getCodFiegDl() + "|" + getCodEdicola() + "|" + DateUtilities.getTimestampAsString(getDtBolla(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS) + "|" + getTipoBolla() + "|" + getPosizioneRiga();
	}
}
