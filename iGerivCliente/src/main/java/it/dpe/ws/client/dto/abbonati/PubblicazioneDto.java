package it.dpe.ws.client.dto.abbonati;

import it.dpe.igeriv.util.NumberUtils;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PubblicazioneDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer idCopertina;
	private Integer idProdotto;
	private String titolo;
	private String sottoTitolo;
	private String codiceEdizione;
	private String numeroCopertina;
	private String periodicita;
	private Double prezzoCopertina;
	private Integer quantita;
	private Date dataConsegna;
	private Date dataUscita;
	private Long codiceBarre;
	private String dl;
	private String edicola;
	private Integer addon;
	
	public Integer getIdCopertina() {
		return idCopertina;
	}

	public void setIdCopertina(Integer idCopertina) {
		this.idCopertina = idCopertina;
	}
	
	public Integer getIdProdotto() {
		return idProdotto;
	}

	public void setIdProdotto(Integer idProdotto) {
		this.idProdotto = idProdotto;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getSottoTitolo() {
		return (sottoTitolo == null) ? "" : sottoTitolo;
	}

	public void setSottoTitolo(String sottoTitolo) {
		this.sottoTitolo = sottoTitolo;
	}

	public String getCodiceEdizione() {
		return codiceEdizione;
	}

	public void setCodiceEdizione(String codiceEdizione) {
		this.codiceEdizione = codiceEdizione;
	}

	public String getNumeroCopertina() {
		return numeroCopertina;
	}

	public void setNumeroCopertina(String numeroCopertina) {
		this.numeroCopertina = numeroCopertina;
	}

	public String getPeriodicita() {
		return periodicita;
	}

	public void setPeriodicita(String periodicita) {
		this.periodicita = periodicita;
	}

	public Double getPrezzoCopertina() {
		return prezzoCopertina;
	}

	public void setPrezzoCopertina(Double prezzoCopertina) {
		this.prezzoCopertina = prezzoCopertina;
	}
	
	public Integer getQuantita() {
		return quantita;
	}

	public void setQuantita(Integer quantita) {
		this.quantita = quantita;
	}

	public String getPrezzoCopertinaFormat() {
		return (prezzoCopertina != null) ? NumberUtils.formatNumber(prezzoCopertina, 4, 2, false, true) : "";
	}

	public Date getDataConsegna() {
		return this.dataConsegna;
	}

	public void setDataConsegna(Date dataConsegna) {
		this.dataConsegna = dataConsegna;
	}
	
	public Date getDataUscita() {
		return this.dataUscita;
	}

	public void setDataUscita(Date dataUscita) {
		this.dataUscita = dataUscita;
	}
	
	public Long getCodiceBarre() {
		return codiceBarre;
	}

	public void setCodiceBarre(Long codiceBarre) {
		this.codiceBarre = codiceBarre;
	}

	public String getDl() {
		return dl;
	}

	public void setDl(String dl) {
		this.dl = dl;
	}

	public String getEdicola() {
		return edicola;
	}

	public void setEdicola(String edicola) {
		this.edicola = edicola;
	}

	public Integer getAddon() {
		return addon;
	}

	public void setAddon(Integer addon) {
		this.addon = addon;
	}
	
	
}
