package it.dpe.igeriv.web.applet.regcassa.dto;

import java.io.Serializable;
import java.text.Normalizer;

public class RegCassaDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String reparto;
	private String prezzo;
	private String quantita;
	private String descrizione;

	public String getReparto() {
		return reparto;
	}

	public void setReparto(String reparto) {
		this.reparto = reparto;
	}

	public String getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(String prezzo) {
		this.prezzo = prezzo;
	}

	public String getQuantita() {
		return quantita;
	}

	public void setQuantita(String quantita) {
		this.quantita = quantita;
	}

	public String getDescrizione() {
		return descrizione != null ? Normalizer.normalize(descrizione, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "") : descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	@Override
	public String toString() {
		return getReparto() + "|" + getPrezzo() + "|" + getQuantita() + "|" + getDescrizione();
	}
}
