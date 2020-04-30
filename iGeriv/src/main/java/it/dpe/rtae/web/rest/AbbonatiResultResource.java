package it.dpe.rtae.web.rest;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import it.dpe.ws.client.dto.abbonati.AbbonatiResultDto;

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
