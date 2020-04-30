package models;

import java.io.Serializable;

public class AnagraficaProdottoDto implements Serializable {
	private static final long serialVersionUID = 837408301518237912L;
	private Integer idProdotto;
	private String titoloPrimaParte;
	private String titoloSecondaParte;
	private String periodicita;
	private Boolean disponibilePerRicaricaEdicola;

	public Integer getIdProdotto() {
		return idProdotto;
	}

	public void setIdProdotto(Integer idProdotto) {
		this.idProdotto = idProdotto;
	}

	public String getTitoloPrimaParte() {
		return titoloPrimaParte;
	}

	public void setTitoloPrimaParte(String titoloPrimaParte) {
		this.titoloPrimaParte = titoloPrimaParte;
	}

	public String getTitoloSecondaParte() {
		return titoloSecondaParte;
	}

	public void setTitoloSecondaParte(String titoloSecondaParte) {
		this.titoloSecondaParte = titoloSecondaParte;
	}

	public String getPeriodicita() {
		return periodicita;
	}

	public void setPeriodicita(String periodicita) {
		this.periodicita = periodicita;
	}

	public Boolean getDisponibilePerRicaricaEdicola() {
		return disponibilePerRicaricaEdicola == null ? false : disponibilePerRicaricaEdicola;
	}

	public void setDisponibilePerRicaricaEdicola(Boolean disponibilePerRicaricaEdicola) {
		this.disponibilePerRicaricaEdicola = disponibilePerRicaricaEdicola;
	}

}
