package it.dpe.igeriv.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "consegne")
public class EstrattoContoClientiXmlDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "header")
	private EstrattoContoHeaderFooterXmlDto header;
	
	@XmlElement(name = "consegnaProdotto")
	private List<EstrattoContoClientiProdottiDto> listConsegneProdotti;
	
	@XmlElement(name = "consegnaPubblicazioni")
	private List<EstrattoContoClientiPubblicazioniDto> listConsegnePubblicazioni;
	
	@XmlElement(name = "footer")
	private EstrattoContoHeaderFooterXmlDto footer;
	
}
