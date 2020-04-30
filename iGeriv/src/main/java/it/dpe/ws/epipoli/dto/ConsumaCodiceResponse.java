package it.dpe.ws.epipoli.dto;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class ConsumaCodiceResponse {

	
	@XmlAttribute(name = "idRichiesta")
	private String idRichiesta;
	
	@XmlAttribute(name = "idTransazione")
    private String idTransazione;
	
	@XmlAttribute(name = "dataTransazione")
    private Calendar dataTransazione;
	
	@XmlAttribute(name = "esito")
    private String esito;
	
	@XmlAttribute(name = "erroreCodice")
    private String erroreCodice;
	
	@XmlAttribute(name = "erroreDescrizione")
    private String erroreDescrizione;
	
	@XmlAttribute(name = "ean")
    private String ean;
	
	@XmlAttribute(name = "serialNumber")
    private String serialNumber;
	
	@XmlAttribute(name = "pin")
    private String pin;
	
	@XmlAttribute(name = "valore")
    private Double valore;
	
	@XmlAttribute(name = "nomeProdotto")
    private String nomeProdotto;
	
	@XmlAttribute(name = "istruzioniAttivazione")
    private String istruzioniAttivazione;
	
	@XmlAttribute(name = "scadenzaAttivazione")
    private Calendar scadenzaAttivazione;
	
	@XmlAttribute(name = "scadenzaMesiUtilizzo")
    private Integer scadenzaMesiUtilizzo;
    
	public ConsumaCodiceResponse(){}
	
	
	public String getIdRichiesta() {
		return idRichiesta;
	}
	public void setIdRichiesta(String idRichiesta) {
		this.idRichiesta = idRichiesta;
	}
	public String getIdTransazione() {
		return idTransazione;
	}
	public void setIdTransazione(String idTransazione) {
		this.idTransazione = idTransazione;
	}
	public Calendar getDataTransazione() {
		return dataTransazione;
	}
	public void setDataTransazione(Calendar dataTransazione) {
		this.dataTransazione = dataTransazione;
	}
	public String getEsito() {
		return esito;
	}
	public void setEsito(String esito) {
		this.esito = esito;
	}
	public String getErroreCodice() {
		return erroreCodice;
	}
	public void setErroreCodice(String erroreCodice) {
		this.erroreCodice = erroreCodice;
	}
	public String getErroreDescrizione() {
		return erroreDescrizione;
	}
	public void setErroreDescrizione(String erroreDescrizione) {
		this.erroreDescrizione = erroreDescrizione;
	}
	public String getEan() {
		return ean;
	}
	public void setEan(String ean) {
		this.ean = ean;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public Double getValore() {
		return valore;
	}
	public void setValore(Double valore) {
		this.valore = valore;
	}
	public String getNomeProdotto() {
		return nomeProdotto;
	}
	public void setNomeProdotto(String nomeProdotto) {
		this.nomeProdotto = nomeProdotto;
	}
	public String getIstruzioniAttivazione() {
		return istruzioniAttivazione;
	}
	public void setIstruzioniAttivazione(String istruzioniAttivazione) {
		this.istruzioniAttivazione = istruzioniAttivazione;
	}
	public Calendar getScadenzaAttivazione() {
		return scadenzaAttivazione;
	}
	public void setScadenzaAttivazione(Calendar scadenzaAttivazione) {
		this.scadenzaAttivazione = scadenzaAttivazione;
	}
	public Integer getScadenzaMesiUtilizzo() {
		return scadenzaMesiUtilizzo;
	}
	public void setScadenzaMesiUtilizzo(Integer scadenzaMesiUtilizzo) {
		this.scadenzaMesiUtilizzo = scadenzaMesiUtilizzo;
	}
	
	
    
}
