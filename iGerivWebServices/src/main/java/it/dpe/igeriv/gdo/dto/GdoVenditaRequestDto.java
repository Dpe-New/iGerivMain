package it.dpe.igeriv.gdo.dto;

import java.io.Serializable;
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
@XmlRootElement(name = "GdoVenRequest", namespace = "http://it.dpe.igeriv/gdo/ven/schemas")
@XmlAccessorType(XmlAccessType.FIELD)
public class GdoVenditaRequestDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name="codiceUtente", namespace="http://it.dpe.igeriv/gdo/ven/schemas")
	private String codiceUtente;
	
	@XmlElement(name="password", namespace="http://it.dpe.igeriv/gdo/ven/schemas")
	private String password;
	
	@XmlElement(name="codVendita", namespace="http://it.dpe.igeriv/gdo/ven/schemas")
	private Long codVendita;
	
	@XmlElement(name="listVendite", namespace="http://it.dpe.igeriv/gdo/ven/schemas")
	private List<GdoVenditaDto> listVendite;

	public String getCodiceUtente() {
		return codiceUtente;
	}

	public void setCodiceUtente(String codiceUtente) {
		this.codiceUtente = codiceUtente;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Long getCodVendita() {
		return codVendita;
	}

	public void setCodVendita(Long codVendita) {
		this.codVendita = codVendita;
	}

	public List<GdoVenditaDto> getListVendite() {
		return listVendite;
	}

	public void setListVendite(List<GdoVenditaDto> listVendite) {
		this.listVendite = listVendite;
	}

}
