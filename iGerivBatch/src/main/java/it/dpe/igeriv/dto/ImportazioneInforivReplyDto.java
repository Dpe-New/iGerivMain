package it.dpe.igeriv.dto;

import java.io.Serializable;

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
@XmlRootElement(name = "EdicolaInforivResponse", namespace = "http://it.dpe.igeriv/inforiv/schemas")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportazioneInforivReplyDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name="Stato", namespace="http://it.dpe.igeriv/inforiv/schemas")
	private Integer stato;
	
	@XmlElement(name="Messaggio", namespace="http://it.dpe.igeriv/inforiv/schemas")
	private String messaggio;
	
	public Integer getStato() {
		return stato;
	}

	public void setStato(Integer stato) {
		this.stato = stato;
	}
	
	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

}
