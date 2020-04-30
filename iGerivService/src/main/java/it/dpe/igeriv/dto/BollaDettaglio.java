package it.dpe.igeriv.dto;

import it.dpe.igeriv.vo.pk.IBollaDettaglioPk;
import it.dpe.igeriv.vo.pk.PeriodicitaPk;

import java.math.BigDecimal;

public interface BollaDettaglio {

	public abstract String getBarcode();

	public abstract Integer getCodicePubblicazione();

	public abstract Integer getIdtn();

	public abstract String getImmagine();

	public abstract String getIndicatorePrezzoVariato();

	public abstract Integer getIndicatoreValorizzare();

	public abstract String getNote();

	public abstract Integer getOrdini();

	public abstract BigDecimal getPercentualeIva();

	public abstract PeriodicitaPk getPeriodicitaPk();

	public abstract IBollaDettaglioPk getPk();

	public abstract BigDecimal getPrezzoLordo();

	public abstract BigDecimal getPrezzoNetto();

	public abstract Integer getQuantitaConsegnata();

	public abstract Integer getQuantitaSpuntata();

	public abstract Integer getRownum();

	public abstract BigDecimal getSconto();

	public abstract String getSottoTitolo();

	public abstract String getTipoFondoBolla();

	public abstract String getTitolo();

	public abstract void setBarcode(String barcode);

	public abstract void setCodicePubblicazione(Integer codicePubblicazione);

	public abstract void setConfermaDifferenze(Integer confermaDifferenze);

	public abstract void setDifferenze(Integer differenze);

	public abstract void setIdtn(Integer idtn);

	public abstract void setImmagine(String immagine);

	public abstract void setImporto(BigDecimal importo);

	public abstract void setIndicatorePrezzoVariato(String indicatorePrezzoVariato);

	public abstract void setIndicatoreValorizzare(Integer indicatoreValorizzare);

	public abstract void setNote(String note);

	public abstract void setNumeroPubblicazione(String numeroPubblicazione);

	public abstract void setOrdini(Integer ordini);

	public abstract void setPercentualeIva(BigDecimal percentualeIva);

	public abstract void setPeriodicitaPk(PeriodicitaPk periodicitaPk);

	public abstract void setPk(IBollaDettaglioPk pk);

	public abstract void setPrezzoLordo(BigDecimal prezzoLordo);

	public abstract void setPrezzoNetto(BigDecimal prezzoNetto);

	public abstract void setQuantitaConsegnata(Integer quantitaConsegnata);

	public abstract void setQuantitaSpuntata(Integer quantitaSpuntata);

	public abstract void setRownum(Integer rownum);

	public abstract void setSconto(BigDecimal sconto);

	public abstract void setSottoTitolo(String sottoTitolo);

	public abstract void setSpunta(Integer spunta);

	public abstract void setTipoFondoBolla(String tipoFondoBolla);

	public abstract void setTitolo(String titolo);

	public abstract int hashCode();

	public abstract String getNumeroPubblicazione();

	public abstract BigDecimal getImporto();

	public abstract BigDecimal getImportoLordo();

	public abstract Integer getDifferenze();

	public abstract int getTipo();

	public abstract Integer getConfermaDifferenze();

	public abstract Integer getSpunta();
	
	public abstract Integer getVariazione();

	public abstract void accept(VisitorDto visitor);

}