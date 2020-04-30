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
public class BollaResaFuoriVocePk extends BasePk implements IBollaDettaglioPk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9622B")
	private Integer codFiegDl;
	@Column(name = "crivw9622B")
	private Integer codEdicola;
	@Column(name = "datbr9622B")
	private Timestamp dtBolla;
	@Column(name = "tipbr9622B")
	private String tipoBolla;
	@Column(name = "posiz9622B")
	private Integer posizioneRiga;
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			BollaResaFuoriVocePk rfvpk = (BollaResaFuoriVocePk) obj;
			return rfvpk.getCodEdicola().equals(this.getCodEdicola()) && rfvpk.getCodFiegDl().equals(this.getCodFiegDl()) && rfvpk.getDtBolla().equals(this.getDtBolla()) && rfvpk.getTipoBolla().equals(this.getTipoBolla());
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		int hash = 1;
	    hash = hash * 31 + getCodEdicola().hashCode();
	    hash = hash * 31 + getCodFiegDl().hashCode();
	    hash = hash * 31 + getDtBolla().hashCode();
	    hash = hash * 31 + getTipoBolla().hashCode();
	    return hash;
	}
	
	@Override
	public String toString() {
		return getCodFiegDl() + "|" + getCodEdicola() + "|" + DateUtilities.getTimestampAsString(getDtBolla(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS) + "|" + getTipoBolla() + "|" + getPosizioneRiga();
	}
	
}
