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
public class EstrattoContoEdicolaDettaglioPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9631")
	private Integer codFiegDl;
	@Column(name = "crivw9631")
	private Integer codEdicola;
	@Column(name = "datec9631")
	private Date dataEstrattoConto;
	@Column(name = "prog9631")
	private Integer progressivo;
	
	@Override
	public String toString() {
		return getCodFiegDl() + "|" + getCodEdicola() + "|" + DateUtilities.getTimestampAsString(getDataEstrattoConto(), DateUtilities.FORMATO_DATA_YYYYMMDD) + "|" + getProgressivo();
	}

}
