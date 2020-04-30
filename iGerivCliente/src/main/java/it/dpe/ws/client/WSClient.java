package it.dpe.ws.client;

import java.io.Serializable;

import javax.xml.namespace.QName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;

/**
 * 
 * @author romanom
 *
 */
@Component("WebServiceClient")
public class WSClient {
	
	@Autowired
    private WebServiceTemplate webServiceTemplate;

    public void setDefaultUri(String defaultUri) {
        webServiceTemplate.setDefaultUri(defaultUri);
    }

    public Object sendAndReceive(final Serializable params, final String username, final String pwd) {
        Object marshalSendAndReceive = webServiceTemplate.marshalSendAndReceive(params, new WebServiceMessageCallback() {
            public void doWithMessage(WebServiceMessage message) {
                SoapHeader soapHeader = ((SoapMessage)message).getSoapHeader();
                soapHeader.addHeaderElement(new QName("UserName", username));
        		soapHeader.addHeaderElement(new QName("Password", pwd));
            }
        });
        return marshalSendAndReceive;
    	
    }
    
	public WebServiceTemplate getWebServiceTemplate() {
		return webServiceTemplate;
	}

	public void setWebServiceTemplate(WebServiceTemplate webServiceTemplate) {
		this.webServiceTemplate = webServiceTemplate;
	}

	
    
}
