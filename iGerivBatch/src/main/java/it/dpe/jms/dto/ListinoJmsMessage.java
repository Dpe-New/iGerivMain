package it.dpe.jms.dto;

import java.sql.Timestamp;

/**
 * Listino Prodotti Non Editoriali
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
public class ListinoJmsMessage extends BaseJmsMessage {
	private static final long serialVersionUID = 7547424370779211357L;
	private Long codProdottoInterno;
	private Long codiceCliente;
	private Integer gruppoSconto;
	private Timestamp dataValiditaFinale;
	private Timestamp dataValiditaIniziale;
	private Float prezzoLisitino;
	private Float scontoValore;
	private Float scontoPercentuale;

	public Long getCodProdottoInterno() {
		return codProdottoInterno;
	}

	public void setCodProdottoInterno(Long codProdottoInterno) {
		this.codProdottoInterno = codProdottoInterno;
	}
	
	public Long getCodiceCliente() {
		return codiceCliente;
	}

	public void setCodiceCliente(Long codiceCliente) {
		this.codiceCliente = codiceCliente;
	}

	public Integer getGruppoSconto() {
		return gruppoSconto;
	}

	public void setGruppoSconto(Integer gruppoSconto) {
		this.gruppoSconto = gruppoSconto;
	}

	public Timestamp getDataValiditaFinale() {
		return dataValiditaFinale;
	}

	public void setDataValiditaFinale(Timestamp dataValiditaFinale) {
		this.dataValiditaFinale = dataValiditaFinale;
	}

	public Timestamp getDataValiditaIniziale() {
		return dataValiditaIniziale;
	}

	public void setDataValiditaIniziale(Timestamp dataValiditaIniziale) {
		this.dataValiditaIniziale = dataValiditaIniziale;
	}

	public Float getPrezzoLisitino() {
		return prezzoLisitino;
	}

	public void setPrezzoLisitino(Float prezzoLisitino) {
		this.prezzoLisitino = prezzoLisitino;
	}

	public Float getScontoValore() {
		return scontoValore;
	}

	public void setScontoValore(Float scontoValore) {
		this.scontoValore = scontoValore;
	}
	
	public Float getScontoPercentuale() {
		return scontoPercentuale;
	}

	public void setScontoPercentuale(Float scontoPercentuale) {
		this.scontoPercentuale = scontoPercentuale;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("codProdottoInterno = " + codProdottoInterno + "\t");
		sb.append("codiceCliente = " + codiceCliente + "\t");
		sb.append("gruppoSconto = " + gruppoSconto + "\t");
		sb.append("dataValiditaFinale = " + dataValiditaFinale + "\t");
		sb.append("dataValiditaIniziale = " + dataValiditaIniziale + "\t");
		sb.append("prezzoLisitino = " + prezzoLisitino + "\t");
		sb.append("scontoValore = " + scontoValore + "\t");
		sb.append("scontoPercentuale = " + scontoPercentuale + "\t");
		return sb.toString();
	}

}
