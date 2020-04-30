package it.dpe.igeriv.gdo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
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
@XmlRootElement(name = "Vendita", namespace = "http://it.dpe.igeriv/gdo/ven/schemas")
@XmlAccessorType(XmlAccessType.FIELD)
public class GdoVenditaDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name="barcode", namespace="http://it.dpe.igeriv/gdo/ven/schemas")
	private String barcode;

	@XmlElement(name="idtn", namespace="http://it.dpe.igeriv/gdo/ven/schemas")
	private Integer idtn;

	@XmlElement(name="dataOraVendita", namespace="http://it.dpe.igeriv/gdo/ven/schemas")
	private Date dataOraVendita;

	@XmlElement(name="copie", namespace="http://it.dpe.igeriv/gdo/ven/schemas")
	private Integer copie;
	
	@XmlElement(name = "prezzoLordo", namespace = "http://it.dpe.igeriv/gdo/ven/schemas")
	private BigDecimal prezzoLordo;
	
	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Integer getIdtn() {
		return idtn;
	}

	public void setIdtn(Integer idtn) {
		this.idtn = idtn;
	}

	public Date getDataOraVendita() {
		return dataOraVendita;
	}

	public void setDataOraVendita(Date dataOraVendita) {
		this.dataOraVendita = dataOraVendita;
	}

	public Integer getCopie() {
		return copie;
	}

	public void setCopie(Integer copie) {
		this.copie = copie;
	}

	public BigDecimal getPrezzoLordo() {
		return prezzoLordo;
	}

	public void setPrezzoLordo(BigDecimal prezzoLordo) {
		this.prezzoLordo = prezzoLordo;
	}
	
	public BigDecimal getImporto() {
		return getPrezzoLordo().multiply(new BigDecimal(getCopie()));
	}
	
	@Override
	public String toString() {
		return "barcode=" + getBarcode() + " idtn=" + getIdtn() + " dataOraVendita=" + getDataOraVendita().getTime() + " copie=" + getCopie() + " prezzoLordo=" + getPrezzoLordo(); 
	}
	
}
