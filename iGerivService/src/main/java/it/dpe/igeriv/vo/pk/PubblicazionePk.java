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
public class PubblicazionePk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9131")
	private Integer codFiegDl;
	@Column(name = "crivw9131")
	private Integer codEdicola;
	@Column(name = "dataord9131")
	private Timestamp dataOrdine;
	@Column(name = "idtn9131")
	private Integer idtn;

	@Override
	public String toString() {
		return getCodFiegDl() + "|" + getCodEdicola() + "|" + DateUtilities.getTimestampAsString(getDataOrdine(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS) + "|" + getIdtn();
	}
	
	@Override
	public int hashCode() {
		return getCodEdicola() + getCodFiegDl();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof PubblicazionePk) {
			PubblicazionePk obj2 = (PubblicazionePk)obj;
			return this.toString().equals(obj2.toString());
		}
		return super.equals(obj);
	}

}
