package it.dpe.igeriv.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface BollaRiassunto {

	public abstract Integer getBollaTrasmessaDl();

	public abstract Timestamp getDtTrasmissione();

	public abstract Integer getGruppoSconto();

	public abstract Integer getNumVoci();

	public abstract BigDecimal getValoreBolla();

	public abstract void setBollaTrasmessaDl(Integer bollaTrasmessaDl);

	public abstract void setDtTrasmissione(Timestamp dtTrasmissione);

	public abstract void setGruppoSconto(Integer gruppoSconto);

	public abstract void setNumVoci(Integer numVoci);

	public abstract void setValoreBolla(BigDecimal valoreBolla);

	public abstract String getDtAndTipoBolla();

	public abstract Timestamp getDtBolla();
	
	public abstract String getTipoBolla();
	
	public abstract Integer getCodFiegDl();
	
	public abstract Integer getCodEdicola();

}