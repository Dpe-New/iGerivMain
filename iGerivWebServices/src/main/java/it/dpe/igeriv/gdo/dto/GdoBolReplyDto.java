package it.dpe.igeriv.gdo.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
@XmlRootElement(name = "GdoBollaResponse", namespace = "http://it.dpe.igeriv/gdo/bol/schemas")
@XmlAccessorType(XmlAccessType.FIELD)
public class GdoBolReplyDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name="dataOra", namespace="http://it.dpe.igeriv/gdo/bol/schemas")
	private Date dataOra;
	
	@XmlElement(name="listProdottiBolla", namespace="http://it.dpe.igeriv/gdo/bol/schemas")
	private List<GdoBolProdottoDto> listProdottiBolla;
	
	@XmlElement(name="state", namespace="http://it.dpe.igeriv/gdo/bol/schemas")
	private Integer state;
	
	@XmlElement(name="message", namespace="http://it.dpe.igeriv/gdo/bol/schemas")
	private String message;

	public Date getDataOra() {
		return dataOra;
	}

	public void setDataOra(Date dataOra) {
		this.dataOra = dataOra;
	}

	public List<GdoBolProdottoDto> getListProdottiBolla() {
		return listProdottiBolla;
	}

	public void setListProdottiBolla(List<GdoBolProdottoDto> listProdottiBolla) {
		this.listProdottiBolla = listProdottiBolla;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
