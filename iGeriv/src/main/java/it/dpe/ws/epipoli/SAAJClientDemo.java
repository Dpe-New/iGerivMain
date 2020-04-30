package it.dpe.ws.epipoli;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import it.dpe.igeriv.util.DateUtilities;
import it.dpe.ws.epipoli.dto.ConsumaCodiceB2CResponse;
import it.dpe.ws.epipoli.dto.ConsumaCodiceResponse;



public class SAAJClientDemo {

	private static SOAPMessage createSoapRequest() throws Exception{
		 
		 MessageFactory messageFactory = MessageFactory.newInstance();
		 SOAPMessage soapMessage = messageFactory.createMessage();
		 SOAPPart soapPart = soapMessage.getSOAPPart();
         SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
         
         setOptionalHeaders(soapMessage, soapEnvelope);
         
         soapEnvelope.addNamespaceDeclaration("ser", "http://service.pinwarehouse.dotcom.ts.it/");
		 
         SOAPBody soapBody = soapEnvelope.getBody();
         
         SOAPElement soapElement = soapBody.addChildElement("consumaCodiceB2C", "ser");
		 
         SOAPElement element_body_root = soapElement.addChildElement("ConsumaCodiceB2CRequest");
         
		 SOAPElement element1 = element_body_root.addChildElement("codiceEAN");
		 element1.addTextNode("8033247266313");
		 
		 SOAPElement element2 = element_body_root.addChildElement("valore");
		 element2.addTextNode("50.00");
		 
		 SOAPElement element3 = element_body_root.addChildElement("codiceCatena");
		 element3.addTextNode("NEXTI");
		 
		 SOAPElement element4 = element_body_root.addChildElement("codiceCedi");
		 element4.addTextNode("0000001");
		 
		 SOAPElement element5 = element_body_root.addChildElement("codicePdv");
		 element5.addTextNode("000001");
		 
		 SOAPElement element6 = element_body_root.addChildElement("codiceCassa");
		 element6.addTextNode("000001");
		 
		 SOAPElement element7 = element_body_root.addChildElement("idRichiesta");
		 //element7.addTextNode("123123123");
		 //element7.addTextNode("123123126");
		 //element7.addTextNode("123123127");
		 //element7.addTextNode("123123128");
		 //element7.addTextNode("123123129");
		 //element7.addTextNode("123123130");
		 //element7.addTextNode("123123131");
		 element7.addTextNode("123123132");
		 
		 SOAPElement element8 = element_body_root.addChildElement("datiSpedizione");
		 //element8.addTextNode("123123123");
		 element8.addChildElement("cap");
		 element8.addChildElement("cognome");
		 element8.addChildElement("nome");
		 element8.addChildElement("nazione");
		 element8.addChildElement("provincia");
		 element8.addChildElement("comune");
		 element8.addChildElement("indirizzo");
		 element8.addChildElement("matricola");
		 element8.addChildElement("telefono");
		 element8.addChildElement("tipo");
		 
		 SOAPElement element9 = element_body_root.addChildElement("emailDestinatario");
		 element9.addTextNode("benedetto.bifulco@dpe.it");
		 
		 SOAPElement element10 = element_body_root.addChildElement("invioMail");
		 element10.addTextNode("false");
		 
		 SOAPElement element11 = element_body_root.addChildElement("presso");
		 element11.addTextNode("Benedetto Bifulco");
		 
		 SOAPElement element12 = element_body_root.addChildElement("spedizione");
		 element12.addTextNode("0");
		 
		 soapMessage.saveChanges();
		 System.out.println("----------SOAP Request------------");
		 soapMessage.writeTo(System.out);
		 return soapMessage;
	 }

	private static void createSoapResponse(SOAPMessage soapResponse) throws Exception  {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		Source sourceContent = soapResponse.getSOAPPart().getContent();
		
		System.out.println("\n----------SOAP Response-----------");
		StreamResult result = new StreamResult(System.out);
		transformer.transform(sourceContent, result);
		
	 }
	 
	private static void setOptionalHeaders(SOAPMessage message, SOAPEnvelope envelope) throws SOAPException{
		 	
		 	Random generator = new Random();
		    String nonceString = String.valueOf(generator.nextInt(999999999));
		 
		 	String usr = "next01";
		 	String pwd = "next01NEXT";
		 
		 	SOAPHeader header = message.getSOAPHeader();
	 	
	        SOAPElement security = header.addChildElement("Security", "wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
	        security.addAttribute(new QName("xmlns:wsu"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
	        
	        
	        SOAPElement usernameToken = security.addChildElement("UsernameToken", "wsse");
	        usernameToken.addAttribute(new QName("wsu:Id"), "UsernameToken-1");

	        SOAPElement username = usernameToken.addChildElement("Username", "wsse");
	        username.addTextNode(usr);

	        SOAPElement password = usernameToken.addChildElement("Password", "wsse");
	        password.setAttribute("Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
	        password.addTextNode(pwd);

	        SOAPElement nonce = usernameToken.addChildElement("Nonce", "wsse");
	        nonce.setAttribute("EncodingType", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary");
	        
	        nonce.addTextNode(Base64.encodeBase64String(hexEncode(nonceString).getBytes()));
	        //nonce.addTextNode(Base64.encodeBase64String(pwd.getBytes()));

	        SOAPElement created = usernameToken.addChildElement("Created", "wsse");
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	        Calendar c1 = Calendar.getInstance();
	        created.addTextNode(sdf.format(c1.getTime()));

	        String serverURI = "http://somedomain.com/pe/ws/schema";
	        envelope.addNamespaceDeclaration("sch", serverURI);
	    }
	 
	private static String hexEncode(String in) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < (in.length() - 2) + 1; i = i + 2) {
            int c = Integer.parseInt(in.substring(i, i + 2), 16);
            char chr = (char) c;
            sb.append(chr);
        }
        return sb.toString();
    }
	 
	public static String getCharacterDataFromElement(Element e) {
	    if(e!=null){
			Node child = e.getFirstChild();
	        if (child instanceof CharacterData) {
		      CharacterData cd = (CharacterData) child;
		      return cd.getData();
		    }
	    }
	    return "";
	  }
	
	
	public static void main(String args[]){
	    try{
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			String url = "http://test.pwh.dotcom.ts.it/testPinWarehouseWS/ws/servizioCodici?wsdl";
			
			SOAPMessage soapRequest = createSoapRequest();
			//hit soapRequest to the server to get response
			SOAPMessage soapResponse = soapConnection.call(soapRequest, url);
			
			//createSoapResponse(soapResponse);
			
			
			SOAPBody element = soapResponse.getSOAPBody();
			DOMSource source = new DOMSource(element);
			StringWriter stringResult = new StringWriter();
			TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(stringResult));
			
			
			String messageXML = stringResult.toString();
			
//			String messageXML = "<ns2:consumaCodiceB2CResponse xmlns:ns2=\"http://service.pinwarehouse.dotcom.ts.it/\" xmlns:ns3=\"http://www.dotcom.ts.it/pinwarehouse\">"
//					+ "<ns3:ConsumaCodiceResponse><idRichiesta>123123123</idRichiesta><idTransazione>000000698</idTransazione>"
//					+ "<dataTransazione>2016-09-22T14:53:03.623+02:00</dataTransazione><esito>OK</esito><ean>8033247266313</ean>"
//					+ "<serialNumber>99996004365684</serialNumber><pin>9999955598404399</pin><valore>50.0</valore>"
//					+ "<nomeProdotto>eGift Card Volagratis 50E</nomeProdotto>"
//					+ "<istruzioniAttivazione>Attivare la Gift Card è semplicissimo! Collegati al sito www.giftcard.volagratis.com e registrati "
//					+ "inserendo il codice di attivazione (PIN) e gli altri dati richiesti. Riceverai via e-mail il Codice Voucher che varrà"
//					+ "come credito per prenotare il tuo volo su volagratis.com. La tua Gift Card può essere attivata entro 6 mesi dalla data di acquisto. "
//					+ "La scadenza di utilizzo ti sarà comunicata in fase di attivazione.</istruzioniAttivazione>"
//					+ "<scadenzaAttivazione>2017-03-22T00:00:00+01:00</scadenzaAttivazione>"
//					+ "<scadenzaMesiUtilizzo>0</scadenzaMesiUtilizzo>"
//					+ "</ns3:ConsumaCodiceResponse></ns2:consumaCodiceB2CResponse>";
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        InputSource is = new InputSource();
	        is.setCharacterStream(new StringReader(messageXML));

	        Document doc = db.parse(is);
	        NodeList nodes = doc.getElementsByTagName("ns3:ConsumaCodiceResponse");

	       
	        ConsumaCodiceB2CResponse consumaCodiceB2CResponse = new ConsumaCodiceB2CResponse();	
	        List<ConsumaCodiceResponse> listConsumaCodiceResponse = new ArrayList<ConsumaCodiceResponse>();
	        
	        System.out.println("\n");
	        // iterate the employees
	        for (int i = 0; i < nodes.getLength(); i++) {
	        	Element elementConsumaCodiceResponse = (Element) nodes.item(i);
	           
	        	ConsumaCodiceResponse consumaCodiceResponse = new ConsumaCodiceResponse();
	           
	           	NodeList idRichiesta = elementConsumaCodiceResponse.getElementsByTagName("idRichiesta");
	           	Element idRichiesta_line = (Element) idRichiesta.item(0);
	           	if(idRichiesta_line!=null)
	           		consumaCodiceResponse.setIdRichiesta(getCharacterDataFromElement(idRichiesta_line));
	           	
	           	NodeList idTransazione = elementConsumaCodiceResponse.getElementsByTagName("idTransazione");
	           	Element idTransazione_line = (Element) idTransazione.item(0);
	           	if(idTransazione_line!=null)
	           		consumaCodiceResponse.setIdTransazione(getCharacterDataFromElement(idTransazione_line));
		        
	        	NodeList dataTransazione = elementConsumaCodiceResponse.getElementsByTagName("dataTransazione");
	           	Element dataTransazione_line = (Element) dataTransazione.item(0);
	           	if(dataTransazione_line!=null){
	           		String str_dataTransazione = getCharacterDataFromElement(dataTransazione_line);
	           		String pattern = "yyyy-MM-dd'T'HH:mm:ss";
	           		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	           		java.util.Date date_dataTransazione = sdf.parse(str_dataTransazione);
	           		consumaCodiceResponse.setDataTransazione(DateUtilities.buildCalendarOnDate(date_dataTransazione));
	           		System.out.println(DateUtilities.getTimestampAsStringExport(date_dataTransazione,DateUtilities.FORMATO_DATA));
	           	}
		        
		        NodeList esito = element.getElementsByTagName("esito");
		        Element esito_line = (Element) esito.item(0);
		        if(esito_line!=null)
		        	consumaCodiceResponse.setEsito(getCharacterDataFromElement(esito_line));
		        
		        NodeList erroreCodice = element.getElementsByTagName("erroreCodice");
		        Element erroreCodice_line = (Element) erroreCodice.item(0);
		        if(erroreCodice_line!=null)
		        	consumaCodiceResponse.setErroreCodice(getCharacterDataFromElement(erroreCodice_line));
		        
		        NodeList erroreDescrizione = element.getElementsByTagName("erroreDescrizione");
		        Element erroreDescrizione_line = (Element) erroreDescrizione.item(0);
		        if(erroreDescrizione_line!=null)
		        	consumaCodiceResponse.setErroreDescrizione(getCharacterDataFromElement(erroreDescrizione_line));	       
		       
		        NodeList ean = element.getElementsByTagName("ean");
		        Element ean_line = (Element) ean.item(0);
		        if(ean_line!=null)
		        	consumaCodiceResponse.setEan(getCharacterDataFromElement(ean_line));
		        
		        NodeList serialNumber = element.getElementsByTagName("serialNumber");
		        Element serialNumber_line = (Element) serialNumber.item(0);
		        if(serialNumber_line!=null)
		        	consumaCodiceResponse.setSerialNumber(getCharacterDataFromElement(serialNumber_line));
		        		        
		        NodeList pin = element.getElementsByTagName("pin");
		        Element pin_line = (Element) pin.item(0);
		        if(pin_line!=null)
		        	consumaCodiceResponse.setPin(getCharacterDataFromElement(pin_line));
		        
		        NodeList valore = element.getElementsByTagName("valore");
		        Element valore_line = (Element) valore.item(0);
		        if(valore_line!=null)
		        	consumaCodiceResponse.setValore(new Double(getCharacterDataFromElement(valore_line)));
		    	
		        NodeList nomeProdotto = element.getElementsByTagName("nomeProdotto");
		        Element nomeProdotto_line = (Element) nomeProdotto.item(0);
		        if(nomeProdotto_line!=null)
		        	consumaCodiceResponse.setNomeProdotto(getCharacterDataFromElement(nomeProdotto_line));
		        
		        NodeList istruzioniAttivazione = element.getElementsByTagName("istruzioniAttivazione");
		        Element istruzioniAttivazione_line = (Element) istruzioniAttivazione.item(0);
		        if(istruzioniAttivazione_line!=null)
		        	consumaCodiceResponse.setIstruzioniAttivazione(getCharacterDataFromElement(istruzioniAttivazione_line));
		        
		        NodeList scadenzaAttivazione = elementConsumaCodiceResponse.getElementsByTagName("scadenzaAttivazione");
	           	Element scadenzaAttivazione_line = (Element) scadenzaAttivazione.item(0);
	           	if(scadenzaAttivazione_line!=null){
	           		String str_scadenzaAttivazione = getCharacterDataFromElement(scadenzaAttivazione_line);
	           		String pattern = "yyyy-MM-dd'T'HH:mm:ss";
	           		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	           		java.util.Date date_scadenzaAttivazione = sdf.parse(str_scadenzaAttivazione);
	           		consumaCodiceResponse.setScadenzaAttivazione(DateUtilities.buildCalendarOnDate(date_scadenzaAttivazione));
	           		System.out.println(DateUtilities.getTimestampAsStringExport(date_scadenzaAttivazione,DateUtilities.FORMATO_DATA));
	           		
	           	}
	           	NodeList scadenzaMesiUtilizzo = element.getElementsByTagName("scadenzaMesiUtilizzo");
		        Element scadenzaMesiUtilizzo_line = (Element) scadenzaMesiUtilizzo.item(0);
		    	if(scadenzaMesiUtilizzo_line!=null)
		    		consumaCodiceResponse.setScadenzaMesiUtilizzo(new Integer(getCharacterDataFromElement(scadenzaMesiUtilizzo_line)));
		        
		        listConsumaCodiceResponse.add(consumaCodiceResponse);

	        }
			
	        consumaCodiceB2CResponse.setConsumaCodiceResponse(listConsumaCodiceResponse);
			
			soapConnection.close();
		}catch (Exception e) {
		     e.printStackTrace();
		}
	 }
	
	
}
