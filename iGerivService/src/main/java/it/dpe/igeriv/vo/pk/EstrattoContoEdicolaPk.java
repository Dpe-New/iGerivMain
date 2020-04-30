package it.dpe.igeriv.vo.pk;

import it.dpe.igeriv.util.DateUtilities;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class EstrattoContoEdicolaPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9630")
	private Integer codFiegDl;
	@Column(name = "crivw9630")
	private Integer codEdicola;
	@Column(name = "datec9630")
	private Date dataEstrattoConto;
	
	@Override
	public String toString() {
		return getCodFiegDl() + "|" + getCodEdicola() + "|" + DateUtilities.getTimestampAsString(getDataEstrattoConto(), DateUtilities.FORMATO_DATA_YYYYMMDD);
	}
	
}
