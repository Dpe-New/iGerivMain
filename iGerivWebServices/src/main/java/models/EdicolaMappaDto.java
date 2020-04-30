package models;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "edicola")
public class EdicolaMappaDto implements Serializable {
	private static final long serialVersionUID = -4302488992570804096L;
	private String lat;
	private String lon;
	private String codice;
	private String codiceDL;
	private String ragSoc;
	private String via;
	private String localita;
	private String provincia;

	public String getLat() {
		return lat;
	}
	
	@XmlElement
	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}
	
	@XmlElement
	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getCodice() {
		return codice;
	}
	
	@XmlElement
	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getRagSoc() {
		return ragSoc;
	}
	
	@XmlElement
	public void setRagSoc(String ragSoc) {
		this.ragSoc = ragSoc;
	}

	public String getVia() {
		return via;
	}
	
	@XmlElement
	public void setVia(String via) {
		this.via = via;
	}

	public String getLocalita() {
		return localita;
	}
	
	@XmlElement
	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public String getProvincia() {
		return provincia;
	}
	
	@XmlElement
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	
	public String getCodiceDL() {
		return codiceDL;
	}
		
	@XmlElement
	public void setCodiceDL(String codiceDL) {
		this.codiceDL = codiceDL;
	}

}
