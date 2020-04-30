package it.dpe.igeriv.dto;

import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.BollaRiassunto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BollaRiassuntoDto extends BaseDto implements BollaRiassunto {
	private static final long serialVersionUID = 1L;
	private Integer codFiegDl;
	private Integer codEdicola;
	private Timestamp dtBolla;
	private String tipoBolla;
	private Integer numVoci;
	private BigDecimal valoreBolla;
	private Integer gruppoSconto;
	private Integer bollaTrasmessaDl;
	private Timestamp dtTrasmissione;
	@Getter(AccessLevel.NONE)
	private Integer totaleCopieBollaResaDettaglio;
	@Getter(AccessLevel.NONE)
	private Long totaleDifferenze;
	private Integer edicolaInFerie;
	
	@Override
	public String getDtAndTipoBolla() {
		return DateUtilities.getTimestampAsString(getDtBolla(), DateUtilities.FORMATO_DATA) + " " + IGerivConstants.TIPO + " " + getTipoBolla();
	}
	
	public String getDataTipoBolla() {
		return DateUtilities.getTimestampAsString(getDtBolla(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS) + "|" + getTipoBolla();
	}
	
	@Override
	public Timestamp getDtBolla() {
		return this.dtBolla;
	}
	
	@Override
	public String getTipoBolla() {
		return this.tipoBolla;
	}

	public Integer getTotaleCopieBollaResaDettaglio() {
		return this.totaleCopieBollaResaDettaglio == null ? 0 : this.totaleCopieBollaResaDettaglio;
	}

	public Long getTotaleDifferenze() {
		return this.totaleDifferenze == null ? 0l : this.totaleDifferenze;
	}
	
	public boolean isEdicolaInFerie()
	{
		if(edicolaInFerie != null)
		{
			return true;
		}
		return false;
	}
	
}
