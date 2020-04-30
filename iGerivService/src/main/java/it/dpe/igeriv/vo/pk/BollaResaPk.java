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
public class BollaResaPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9619")
	private Integer codFiegDl;
	@Column(name = "datbr9619")
	private Timestamp dtBolla;
	@Column(name = "tipbr9619")
	private String tipoBolla;
	@Column(name = "posiz9619")
	private Integer posizioneRiga;
	
	@Override
	public String toString() {
		return getCodFiegDl() + "|" + DateUtilities.getTimestampAsString(getDtBolla(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS) + "|" + getTipoBolla() + "|" + getPosizioneRiga();
	}
}
