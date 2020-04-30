package it.dpe.igeriv.gdo.dto;

import it.dpe.igeriv.dto.EdicolaClientVenditeDto;
import it.dpe.igeriv.dto.ProdottoClientVenditeDto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author romanom
 * 
 */
@Data
@EqualsAndHashCode(callSuper=false)
@XmlType
@XmlRootElement(name = "ProdottiClientVenditeResponse", namespace = "http://it.dpe.igeriv/prod/client/ven/schemas")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProdottoReplyDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name="edicola", namespace="http://it.dpe.igeriv/prod/client/ven/schemas")
	private EdicolaClientVenditeDto edicola;
	
	@XmlElement(name="listProdotti", namespace="http://it.dpe.igeriv/prod/client/ven/schemas")
	private List<ProdottoClientVenditeDto> listProdottiBolla;
	
	@XmlElement(name="state", namespace="http://it.dpe.igeriv/prod/client/ven/schemas")
	private Integer state;
	
	@XmlElement(name="message", namespace="http://it.dpe.igeriv/prod/client/ven/schemas")
	private String message;

}
