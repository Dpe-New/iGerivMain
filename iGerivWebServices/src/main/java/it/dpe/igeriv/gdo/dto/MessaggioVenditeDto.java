package it.dpe.igeriv.gdo.dto;

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
@XmlRootElement(name = "Messaggio", namespace = "http://it.dpe.igeriv/gdo/ven/schemas")
@XmlAccessorType(XmlAccessType.FIELD)
public class MessaggioVenditeDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name="msg", namespace="http://it.dpe.igeriv/gdo/ven/schemas")
	private String msg;
	
	public MessaggioVenditeDto(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
