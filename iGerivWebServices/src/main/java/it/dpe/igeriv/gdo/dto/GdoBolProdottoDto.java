package it.dpe.igeriv.gdo.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author romanom
 * 
 */
@XmlType
@XmlRootElement(name = "ProdottoBolla", namespace = "http://it.dpe.igeriv/gdo/bol/schemas")
@XmlAccessorType(XmlAccessType.FIELD)
public class GdoBolProdottoDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "barcode", namespace = "http://it.dpe.igeriv/gdo/bol/schemas")
	private String barcode;

	@XmlElement(name = "idtn", namespace = "http://it.dpe.igeriv/gdo/bol/schemas")
	private Integer idtn;
	
	@XmlElement(name = "codicePubblicazione", namespace = "http://it.dpe.igeriv/gdo/bol/schemas")
	private Integer cpu;
	
	@XmlElement(name = "codiceInizio", namespace = "http://it.dpe.igeriv/gdo/bol/schemas")
	private Integer codiceInizio;
	
	@XmlElement(name = "tipoPubblicazione", namespace = "http://it.dpe.igeriv/gdo/bol/schemas")
	private String tipoPubblicazione;

	@XmlElement(name = "titolo", namespace = "http://it.dpe.igeriv/gdo/bol/schemas")
	private String titolo;

	@XmlElement(name = "sottotitolo", namespace = "http://it.dpe.igeriv/gdo/bol/schemas")
	private String sottotitolo;

	@XmlElement(name = "descrizione", namespace = "http://it.dpe.igeriv/gdo/bol/schemas")
	private String descrizione;

	@XmlElement(name = "dataUscita", namespace = "http://it.dpe.igeriv/gdo/bol/schemas")
	private String dataUscita;
	
	@XmlElement(name = "numeroCopertina", namespace = "http://it.dpe.igeriv/gdo/bol/schemas")
	private String numeroCopertina;

	@XmlElement(name = "prezzoAcquisto", namespace = "http://it.dpe.igeriv/gdo/bol/schemas")
	private BigDecimal prezzoAcquisto;

	@XmlElement(name = "prezzoLordo", namespace = "http://it.dpe.igeriv/gdo/bol/schemas")
	private BigDecimal prezzoLordo;
	
	@XmlElement(name = "quantitaConsegnata", namespace = "http://it.dpe.igeriv/gdo/bol/schemas")
	private Integer quantitaConsegnata;
	
	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Integer getIdtn() {
		return idtn;
	}

	public void setIdtn(Integer idtn) {
		this.idtn = idtn;
	}
	
	public Integer getCpu() {
		return cpu;
	}

	public void setCpu(Integer cpu) {
		this.cpu = cpu;
	}
	
	public Integer getCodiceInizio() {
		return codiceInizio == null ? 0 : codiceInizio;
	}

	public void setCodiceInizio(Integer codiceInizio) {
		this.codiceInizio = codiceInizio;
	}

	public String getTipoPubblicazione() {
		return tipoPubblicazione;
	}

	public void setTipoPubblicazione(String tipoPubblicazione) {
		this.tipoPubblicazione = tipoPubblicazione;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getSottotitolo() {
		return sottotitolo;
	}

	public void setSottotitolo(String sottotitolo) {
		this.sottotitolo = sottotitolo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	public String getDataUscita() {
		return dataUscita;
	}

	public void setDataUscita(String dataUscita) {
		this.dataUscita = dataUscita;
	}

	public String getNumeroCopertina() {
		return numeroCopertina;
	}

	public void setNumeroCopertina(String numeroCopertina) {
		this.numeroCopertina = numeroCopertina;
	}

	public BigDecimal getPrezzoAcquisto() {
		return prezzoAcquisto;
	}

	public void setPrezzoAcquisto(BigDecimal prezzoAcquisto) {
		this.prezzoAcquisto = prezzoAcquisto;
	}

	public BigDecimal getPrezzoLordo() {
		return prezzoLordo;
	}

	public void setPrezzoLordo(BigDecimal prezzoLordo) {
		this.prezzoLordo = prezzoLordo;
	}
	
	public Integer getQuantitaConsegnata() {
		return quantitaConsegnata == null ? 0 : quantitaConsegnata;
	}

	public void setQuantitaConsegnata(Integer quantitaConsegnata) {
		this.quantitaConsegnata = quantitaConsegnata;
	}

	@Override
	public String toString() {
		return "GdoBolProdottoDto [barcode=" + barcode + ", idtn=" + idtn + ", cpu=" + cpu + ", codiceInizio=" + codiceInizio + ", tipoPubblicazione=" + tipoPubblicazione + ", titolo=" + titolo + ", sottotitolo=" + sottotitolo + ", descrizione=" + descrizione
				+ ", dataUscita=" + dataUscita + ", numeroCopertina=" + numeroCopertina + ", prezzoAcquisto=" + prezzoAcquisto + ", prezzoLordo=" + prezzoLordo + ", quantitaConsegnata=" + quantitaConsegnata + "]";
	}
	

}
