package it.dpe.igeriv.gdo.dto;

import java.io.Serializable;
import java.util.Date;

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
@XmlRootElement(name = "GdoVenResponse", namespace = "http://it.dpe.igeriv/gdo/ven/schemas")
@XmlAccessorType(XmlAccessType.FIELD)
public class GdoVenditaReplyDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name="dataOra", namespace="http://it.dpe.igeriv/gdo/ven/schemas")
	private Date dataOra;
	
	@XmlElement(name="state", namespace="http://it.dpe.igeriv/gdo/ven/schemas")
	private Integer state;
	
	@XmlElement(name="listMessages", namespace="http://it.dpe.igeriv/gdo/ven/schemas")
	private String[] listMessages;

	public Date getDataOra() {
		return dataOra;
	}

	public void setDataOra(Date dataOra) {
		this.dataOra = dataOra;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String[] getListMessages() {
		return listMessages;
	}

	public void setListMessages(String[] listMessages) {
		this.listMessages = listMessages;
	}

}
