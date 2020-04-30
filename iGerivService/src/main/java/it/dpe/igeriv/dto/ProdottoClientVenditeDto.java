package it.dpe.igeriv.dto;
import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper=false)
@XmlType
@XmlRootElement(name = "ProdottoClientVendite", namespace = "http://it.dpe.igeriv/prod/client/ven/schemas")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProdottoClientVenditeDto extends BaseDto {
	private static final long serialVersionUID = -1535084580861696930L;
	@XmlElement(name="barcode", namespace = "http://it.dpe.igeriv/prod/client/ven/schemas")
	private String barcode;
	@XmlElement(name="idtn", namespace = "http://it.dpe.igeriv/prod/client/ven/schemas")
	private Integer idtn;
	@XmlElement(name="dataUscita", namespace = "http://it.dpe.igeriv/prod/client/ven/schemas")
	private Date dataUscita;
	@XmlElement(name="numeroCopertina", namespace = "http://it.dpe.igeriv/prod/client/ven/schemas")
	private String numeroCopertina;
	@XmlElement(name="titolo", namespace = "http://it.dpe.igeriv/prod/client/ven/schemas")
	private String titolo;
	@XmlElement(name="sottotitolo", namespace = "http://it.dpe.igeriv/prod/client/ven/schemas")
	private String sottotitolo;
	@XmlElement(name="prezzo", namespace = "http://it.dpe.igeriv/prod/client/ven/schemas")
	@Getter(AccessLevel.NONE)
	private Float prezzo; 
	@XmlElement(name="coddl", namespace = "http://it.dpe.igeriv/prod/client/ven/schemas")
	private Integer coddl;
	@XmlElement(name="cpu", namespace = "http://it.dpe.igeriv/prod/client/ven/schemas")
	private Integer cpu;
	@XmlElement(name="ciq", namespace = "http://it.dpe.igeriv/prod/client/ven/schemas")
	private Integer ciq;
	@XmlElement(name="cfq", namespace = "http://it.dpe.igeriv/prod/client/ven/schemas")
	private Integer cfq;
	@XmlTransient
	private BigDecimal prezzoCopertina;
	
	public Float getPrezzo() {
		return prezzoCopertina != null ? prezzoCopertina.floatValue() : 0f;
	}
	
	
}