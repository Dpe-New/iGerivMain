package it.dpe.igeriv.web.applet.regcassa.dto;

import java.io.Serializable;

public class RegCassaDatiDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String progressivo;
	private String prodottoNonEditoriale;
	private String coddl;
	private String idtn;
	private String numeroCopertina;
	private String quantita;
	private String idProdotto;
	private String titolo;
	private String sottoTitolo;
	private String prezzoCopertina;
	private String prezzoCopertinaFormat;
	private String barcode;
	private String importoFormat;
	private String giacenzaIniziale;
	private String aliquota;

	public String getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(String progressivo) {
		this.progressivo = progressivo;
	}

	public Boolean getProdottoNonEditoriale() {
		return (prodottoNonEditoriale == null || prodottoNonEditoriale.equals("")) ? false : new Boolean(prodottoNonEditoriale);
	}

	public void setProdottoNonEditoriale(String prodottoNonEditoriale) {
		this.prodottoNonEditoriale = prodottoNonEditoriale;
	}

	public String getCoddl() {
		return coddl;
	}

	public void setCoddl(String coddl) {
		this.coddl = coddl;
	}

	public String getIdtn() {
		return idtn;
	}

	public void setIdtn(String idtn) {
		this.idtn = idtn;
	}

	public String getNumeroCopertina() {
		return numeroCopertina;
	}

	public void setNumeroCopertina(String numeroCopertina) {
		this.numeroCopertina = numeroCopertina;
	}

	public Integer getQuantita() {
		return (quantita == null || quantita.equals("")) ? 0 : new Integer(quantita);
	}

	public void setQuantita(String quantita) {
		this.quantita = quantita;
	}

	public String getIdProdotto() {
		return idProdotto;
	}

	public void setIdProdotto(String idProdotto) {
		this.idProdotto = idProdotto;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getSottoTitolo() {
		return sottoTitolo;
	}

	public void setSottoTitolo(String sottoTitolo) {
		this.sottoTitolo = sottoTitolo;
	}

	public Float getPrezzoCopertina() {
		return (prezzoCopertina == null || prezzoCopertina.equals("")) ? 0f : new Float(prezzoCopertina);
	}

	public void setPrezzoCopertina(String prezzoCopertina) {
		this.prezzoCopertina = prezzoCopertina;
	}

	public String getPrezzoCopertinaFormat() {
		return prezzoCopertinaFormat;
	}

	public void setPrezzoCopertinaFormat(String prezzoCopertinaFormat) {
		this.prezzoCopertinaFormat = prezzoCopertinaFormat;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getImportoFormat() {
		return importoFormat;
	}

	public void setImportoFormat(String importoFormat) {
		this.importoFormat = importoFormat;
	}

	public String getGiacenzaIniziale() {
		return giacenzaIniziale;
	}

	public void setGiacenzaIniziale(String giacenzaIniziale) {
		this.giacenzaIniziale = giacenzaIniziale;
	}

	public Integer getAliquota() {
		return (aliquota == null || aliquota.equals("")) ? 0 : new Integer(aliquota);
	}

	public void setAliquota(String aliquota) {
		this.aliquota = aliquota;
	}

}
