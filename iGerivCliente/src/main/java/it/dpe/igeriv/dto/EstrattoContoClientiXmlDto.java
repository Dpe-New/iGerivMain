package it.dpe.igeriv.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
	
	public List<EstrattoContoClientiProdottiDto> getListConsegneProdotti() {
		return listConsegneProdotti;
	}

	public void setListConsegneProdotti(List<EstrattoContoClientiProdottiDto> listConsegneProdotti) {
		this.listConsegneProdotti = listConsegneProdotti;
	}

	public List<EstrattoContoClientiPubblicazioniDto> getListConsegnePubblicazioni() {
		return listConsegnePubblicazioni;
	}

	public void setListConsegnePubblicazioni(List<EstrattoContoClientiPubblicazioniDto> listConsegnePubblicazioni) {
		this.listConsegnePubblicazioni = listConsegnePubblicazioni;
	}

	public EstrattoContoHeaderFooterXmlDto getHeader() {
		return header;
	}

	public void setHeader(EstrattoContoHeaderFooterXmlDto header) {
		this.header = header;
	}

	public EstrattoContoHeaderFooterXmlDto getFooter() {
		return footer;
	}

	public void setFooter(EstrattoContoHeaderFooterXmlDto footer) {
		this.footer = footer;
	}
	
}
