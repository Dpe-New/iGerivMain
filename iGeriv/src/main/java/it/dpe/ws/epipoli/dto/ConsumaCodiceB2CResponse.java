package it.dpe.ws.epipoli.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name="consumaCodiceB2CResponse", namespace="http://service.pinwarehouse.dotcom.ts.it/")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConsumaCodiceB2CResponse {

	@XmlElement(name = "ConsumaCodiceResponse")
	private List<ConsumaCodiceResponse> ConsumaCodiceResponse;

	public ConsumaCodiceB2CResponse(){}
	
			
	public List<ConsumaCodiceResponse> getConsumaCodiceResponse() {
		return ConsumaCodiceResponse;
	}

	public void setConsumaCodiceResponse(List<ConsumaCodiceResponse> consumaCodiceResponse) {
		ConsumaCodiceResponse = consumaCodiceResponse;
	}  
	
	
	
}
