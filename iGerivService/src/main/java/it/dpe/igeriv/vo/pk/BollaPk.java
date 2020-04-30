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
public class BollaPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9609")
	private Integer codFiegDl;
	@Column(name = "datbc9609")
	private Timestamp dtBolla;
	@Column(name = "tipbc9609")
	private String tipoBolla;
	@Column(name = "posiz9609")
	private Integer posizioneRiga;
	
	@Override
	public String toString() {
		return getCodFiegDl() + "|" + DateUtilities.getTimestampAsString(getDtBolla(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS) + "|" + getTipoBolla() + "|" + getPosizioneRiga();
	}
}
