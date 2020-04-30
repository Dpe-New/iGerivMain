package it.dpe.igeriv.dto;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@XmlType
@XmlRootElement(name = "Edicola", namespace = "http://it.dpe.igeriv/prod/client/ven/schemas")
@XmlAccessorType(XmlAccessType.FIELD)
public class EdicolaClientVenditeDto extends BaseDto {
	private static final long serialVersionUID = -1535084580861696930L;
	@XmlElement(name="nome", namespace = "http://it.dpe.igeriv/prod/client/ven/schemas")
	private String nome;
	@XmlElement(name="localita", namespace = "http://it.dpe.igeriv/prod/client/ven/schemas")
	private String localita;
	@XmlElement(name="provincia", namespace = "http://it.dpe.igeriv/prod/client/ven/schemas")
	private String provincia;
	@XmlElement(name="cap", namespace = "http://it.dpe.igeriv/prod/client/ven/schemas")
	private String cap;
	@XmlElement(name="indirizzo", namespace = "http://it.dpe.igeriv/prod/client/ven/schemas")
	private String indirizzo;

	
	
}