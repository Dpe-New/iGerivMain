package it.dpe.rtae.web.rest;

import it.dpe.ws.client.dto.abbonati.AbbonatiResultDto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "abbonato")
public class AbbonatiResultResource {
	
	@XmlAttribute
	public volatile AbbonatiResultDto content;
	
	@XmlAttribute
	public volatile String status;
	
	public AbbonatiResultResource(AbbonatiResultDto abbonatiResultDto) {
		content = abbonatiResultDto;
	}

}
