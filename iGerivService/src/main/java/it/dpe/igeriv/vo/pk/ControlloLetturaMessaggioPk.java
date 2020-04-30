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
public class ControlloLetturaMessaggioPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coded9602")
	private Integer codiceEdicola;
	@Column(name = "coddl9602")
	private Integer codFiegDl;
	@Column(name = "datam9602")
	private Timestamp dtMessaggio;
	
	@Override
	public String toString() {
		return getCodiceEdicola() + "|" +  getCodFiegDl() + "|" + DateUtilities.getTimestampAsString(getDtMessaggio(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS);
	}

}
