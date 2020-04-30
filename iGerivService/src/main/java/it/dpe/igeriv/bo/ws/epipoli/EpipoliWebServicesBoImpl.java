package it.dpe.igeriv.bo.ws.epipoli;

import java.io.File;
import java.io.FileOutputStream;
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
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.ConsumaCodiceB2CResponse;
import it.dpe.igeriv.dto.ConsumaCodiceResponse;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.RegistrazioneRisposteWSEpipoliVo;


@Repository
public class EpipoliWebServicesBoImpl extends BaseRepositoryImpl implements EpipoliWebServicesBo {

	private final Logger log = Logger.getLogger(getClass());
	
	//TEST "http://test.pwh.dotcom.ts.it/testPinWarehouseWS/ws/servizioCodici?wsdl";
	private String urlWebServicesEpipoli;
	
	@Autowired
	EpipoliWebServicesBoImpl(BaseDao<?> dao, @Value("${igeriv.ws.epipoli.url}") String urlWSEpipoli) {
		super(dao);
		this.urlWebServicesEpipoli = urlWSEpipoli;
	}
	
	

	@Override
	public ConsumaCodiceB2CResponse consumaCodiceB2C(String codiceEan, String valore, String idRichiesta,Integer codFiegDl,Integer codEdicola) {

		ConsumaCodiceB2CResponse response = null;

		SOAPConnectionFactory soapConnectionFactory;
		try {
			soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			String url = urlWebServicesEpipoli;
			log.debug("SOAPConnection URL: "+url);
			
			String codiceCedi 	= StringUtility.fillSx(codFiegDl.toString(),7,'0'); // codicecedi = codiceDL
			String codicePdv 	= StringUtility.fillSx(codEdicola.toString(),6,'0');// codicePdv =  codEdicolaDL
			String codiceCassa 	= "000001";
			
			SOAPMessage soapRequest = createSoapRequest(codiceEan, valore, codiceCedi, codicePdv, codiceCassa, idRichiesta);
			
			// 21/02/2017 - test write soapMessage
			File file= new File("E:/igeriv/logs/test-write-soapmessage.txt");
		    file.createNewFile();
		    FileOutputStream fileOutputStream = new FileOutputStream(file);
		    soapRequest.writeTo(fileOutputStream);
		    fileOutputStream.flush();
		    fileOutputStream.close();
			
			
			log.info("EpipoliWebServicesBoImpl.consumaCodiceB2C soapRequest : "+soapRequest.toString());
			SOAPMessage soapResponse = soapConnection.call(soapRequest, url);
			response = createSoapResponse(soapResponse,codFiegDl,codEdicola);
			soapConnection.close();
			
		} catch (UnsupportedOperationException | SOAPException e) {
			log.error("EpipoliWebServicesBoImpl UnsupportedOperationException | SOAPException : "+e.getMessage());
		} catch (Exception e) {
			log.error("EpipoliWebServicesBoImpl Exception: "+e.getMessage());
		}
		
		return response;
	}

	
	@Override
	public List<RegistrazioneRisposteWSEpipoliVo> findRispostaByIdRichiesta(Integer[] idRichiesta, Integer codFiegDl,Integer codEdicola) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(RegistrazioneRisposteWSEpipoliVo.class, "rispostaIdRichiesta");
		criteria.add(Restrictions.in("rispostaIdRichiesta.idRichiesta", idRichiesta));
		criteria.add(Restrictions.eq("rispostaIdRichiesta.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("rispostaIdRichiesta.codEdicola", codEdicola));
		return getDao().findByDetachedCriteria(criteria);
		//return getDao().findUniqueResultByDetachedCriteria(criteria);
		
	}
	
	
	
	/**
	 * TEST CODICE EAN: "8033247266313" VALORE : 50.00 ID_RICHIESTA :
	 * 123123123...123123131
	 * 
	 * @param codiceEan
	 * @param valore
	 * @param idRichiesta
	 * @return
	 * @throws Exception
	 */
	private SOAPMessage createSoapRequest(String codiceEan, String valore,String codiceCedi, String codicePdv, String codiceCassa,String idRichiesta) throws Exception {

		boolean conDatiSpedizione = true;
		
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
		element1.addTextNode(codiceEan);

		SOAPElement element2 = element_body_root.addChildElement("valore");
		element2.addTextNode(valore);

		SOAPElement element3 = element_body_root.addChildElement("codiceCatena");
		element3.addTextNode("NEXTI");

		SOAPElement element4 = element_body_root.addChildElement("codiceCedi");
		element4.addTextNode(StringUtility.fillSx(codiceCedi,7,'0')); 	//Codice DL

		SOAPElement element5 = element_body_root.addChildElement("codicePdv");
		element5.addTextNode(StringUtility.fillSx(codicePdv,6,'0'));  	//Codice rivendita DL 000001

		SOAPElement element6 = element_body_root.addChildElement("codiceCassa");
		element6.addTextNode(StringUtility.fillSx(codiceCassa,6,'0'));  //Fisso 000001

		SOAPElement element7 = element_body_root.addChildElement("idRichiesta");
		element7.addTextNode(idRichiesta);

		SOAPElement element8 = element_body_root.addChildElement("datiSpedizione");

		// Vittorio 21/02/2017
		if (conDatiSpedizione) {
			element8.addChildElement("cap").setValue("00001");
			//element8.addTextNode("00001");

			element8.addChildElement("cognome").setValue("Nexti");
			//element8.addTextNode("Nexti");

			element8.addChildElement("nome").setValue("Nexti");
			//element8.addTextNode("Nexti");

			element8.addChildElement("nazione").setValue("IT");
			//element8.addTextNode("IT");

			element8.addChildElement("provincia").setValue("MI");
			//element8.addTextNode("MI");

			element8.addChildElement("comune").setValue("Nexti");
			//element8.addTextNode("Nexti");

			element8.addChildElement("indirizzo").setValue("Nexti");
			//element8.addTextNode("Nexti");

			element8.addChildElement("matricola").setValue("000001");
			//element8.addTextNode("000001");

			element8.addChildElement("telefono").setValue("000001");
			//element8.addTextNode("000001");

			element8.addChildElement("tipo").setValue("000");
			//element8.addTextNode("TNP");
		} else {
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
			
		}

		SOAPElement element9 = element_body_root.addChildElement("emailDestinatario");
		element9.addTextNode("dpe@dpe.it");

		SOAPElement element10 = element_body_root.addChildElement("invioMail");
		element10.addTextNode("false");

		SOAPElement element11 = element_body_root.addChildElement("presso");
		element11.addTextNode("DPE");

		SOAPElement element12 = element_body_root.addChildElement("spedizione");
		element12.addTextNode("0");

		soapMessage.saveChanges();
		soapMessage.writeTo(System.out);
		
		log.info("createSoapRequest : "+soapMessage.toString());
		
		return soapMessage;
	}

	/**
	 * 
	 * @param soapResponse
	 * @param codFiegDl
	 * @param codEdicola
	 * @return
	 * @throws Exception
	 */
	private ConsumaCodiceB2CResponse createSoapResponse(SOAPMessage soapResponse,Integer codFiegDl,Integer codEdicola) throws Exception  {
		
		if (soapResponse == null) {
			log.error("vittorio soapResponse nullo");
		}
		
		SOAPBody element = soapResponse.getSOAPBody();

		DOMSource source = new DOMSource(element);
		StringWriter stringResult = new StringWriter();
		TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(stringResult));
		String messageXML = stringResult.toString();
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
        	
        	RegistrazioneRisposteWSEpipoliVo registrazioneRisposta = new RegistrazioneRisposteWSEpipoliVo();
        	ConsumaCodiceResponse consumaCodiceResponse = new ConsumaCodiceResponse();
           
        	registrazioneRisposta.setCodFiegDl(codFiegDl);
        	registrazioneRisposta.setCodEdicola(codEdicola);
        	
           	NodeList idRichiesta = elementConsumaCodiceResponse.getElementsByTagName("idRichiesta");
           	Element idRichiesta_line = (Element) idRichiesta.item(0);
           	if(idRichiesta_line!=null){
           		consumaCodiceResponse.setIdRichiesta(getCharacterDataFromElement(idRichiesta_line));
           		registrazioneRisposta.setIdRichiesta(new Integer(consumaCodiceResponse.getIdRichiesta()));
           	}
           	NodeList idTransazione = elementConsumaCodiceResponse.getElementsByTagName("idTransazione");
           	Element idTransazione_line = (Element) idTransazione.item(0);
           	if(idTransazione_line!=null){
           		consumaCodiceResponse.setIdTransazione(getCharacterDataFromElement(idTransazione_line));
           		registrazioneRisposta.setIdTrans(consumaCodiceResponse.getIdTransazione());
           	}
        	NodeList dataTransazione = elementConsumaCodiceResponse.getElementsByTagName("dataTransazione");
           	Element dataTransazione_line = (Element) dataTransazione.item(0);
           	if(dataTransazione_line!=null){
           		String str_dataTransazione = getCharacterDataFromElement(dataTransazione_line);
           		String pattern = "yyyy-MM-dd'T'HH:mm:ss";
           		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
           		java.util.Date date_dataTransazione = sdf.parse(str_dataTransazione);
           		consumaCodiceResponse.setDataTransazione(DateUtilities.buildCalendarOnDate(date_dataTransazione));
           		registrazioneRisposta.setDtTransazione(new java.sql.Timestamp(date_dataTransazione.getTime()));
           		//System.out.println(DateUtilities.getTimestampAsStringExport(date_dataTransazione,DateUtilities.FORMATO_DATA));
           	}
	        
	        NodeList esito = element.getElementsByTagName("esito");
	        Element esito_line = (Element) esito.item(0);
	        if(esito_line!=null){
	        	consumaCodiceResponse.setEsito(getCharacterDataFromElement(esito_line));
	        	registrazioneRisposta.setEsito(consumaCodiceResponse.getEsito());
	        }
	        NodeList erroreCodice = element.getElementsByTagName("erroreCodice");
	        Element erroreCodice_line = (Element) erroreCodice.item(0);
	        if(erroreCodice_line!=null){
	        	consumaCodiceResponse.setErroreCodice(getCharacterDataFromElement(erroreCodice_line));
	        	registrazioneRisposta.setErrCodice(consumaCodiceResponse.getErroreCodice());
	        }
	        NodeList erroreDescrizione = element.getElementsByTagName("erroreDescrizione");
	        Element erroreDescrizione_line = (Element) erroreDescrizione.item(0);
	        if(erroreDescrizione_line!=null){
	        	consumaCodiceResponse.setErroreDescrizione(getCharacterDataFromElement(erroreDescrizione_line));	       
	        	registrazioneRisposta.setErrDescrizione(consumaCodiceResponse.getErroreDescrizione());
	        }
	        NodeList ean = element.getElementsByTagName("ean");
	        Element ean_line = (Element) ean.item(0);
	        if(ean_line!=null){
	        	consumaCodiceResponse.setEan(getCharacterDataFromElement(ean_line));
	        	registrazioneRisposta.setEan(consumaCodiceResponse.getEan());
	        }
	        NodeList serialNumber = element.getElementsByTagName("serialNumber");
	        Element serialNumber_line = (Element) serialNumber.item(0);
	        if(serialNumber_line!=null){
	        	consumaCodiceResponse.setSerialNumber(getCharacterDataFromElement(serialNumber_line));
	        	registrazioneRisposta.setSerialNumber(consumaCodiceResponse.getSerialNumber());
	        }
	        NodeList pin = element.getElementsByTagName("pin");
	        Element pin_line = (Element) pin.item(0);
	        if(pin_line!=null){
	        	consumaCodiceResponse.setPin(getCharacterDataFromElement(pin_line));
	        	registrazioneRisposta.setPin(consumaCodiceResponse.getPin());
	        }
	        NodeList valore = element.getElementsByTagName("valore");
	        Element valore_line = (Element) valore.item(0);
	        if(valore_line!=null){
	        	consumaCodiceResponse.setValore(new Double(getCharacterDataFromElement(valore_line)));
	        	registrazioneRisposta.setValore(consumaCodiceResponse.getValore());
	        }
	        NodeList nomeProdotto = element.getElementsByTagName("nomeProdotto");
	        Element nomeProdotto_line = (Element) nomeProdotto.item(0);
	        if(nomeProdotto_line!=null){
	        	consumaCodiceResponse.setNomeProdotto(getCharacterDataFromElement(nomeProdotto_line));
	        	registrazioneRisposta.setNomeProdotto(consumaCodiceResponse.getNomeProdotto());
	        }
	        NodeList istruzioniAttivazione = element.getElementsByTagName("istruzioniAttivazione");
	        Element istruzioniAttivazione_line = (Element) istruzioniAttivazione.item(0);
	        if(istruzioniAttivazione_line!=null){
	        	consumaCodiceResponse.setIstruzioniAttivazione(getCharacterDataFromElement(istruzioniAttivazione_line));
	        	registrazioneRisposta.setIstruzioniAttivazione(consumaCodiceResponse.getIstruzioniAttivazione());
	        }
	        NodeList scadenzaAttivazione = elementConsumaCodiceResponse.getElementsByTagName("scadenzaAttivazione");
           	Element scadenzaAttivazione_line = (Element) scadenzaAttivazione.item(0);
           	if(scadenzaAttivazione_line!=null){
           		String str_scadenzaAttivazione = getCharacterDataFromElement(scadenzaAttivazione_line);
           		String pattern = "yyyy-MM-dd'T'HH:mm:ss";
           		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
           		java.util.Date date_scadenzaAttivazione = sdf.parse(str_scadenzaAttivazione);
           		consumaCodiceResponse.setScadenzaAttivazione(DateUtilities.buildCalendarOnDate(date_scadenzaAttivazione));
           		registrazioneRisposta.setDtScadenzaAttivazione(new java.sql.Timestamp(date_scadenzaAttivazione.getTime()));
           		//System.out.println(DateUtilities.getTimestampAsStringExport(date_scadenzaAttivazione,DateUtilities.FORMATO_DATA));
           	}
           	NodeList scadenzaMesiUtilizzo = element.getElementsByTagName("scadenzaMesiUtilizzo");
	        Element scadenzaMesiUtilizzo_line = (Element) scadenzaMesiUtilizzo.item(0);
	    	if(scadenzaMesiUtilizzo_line!=null){
	    		consumaCodiceResponse.setScadenzaMesiUtilizzo(new Integer(getCharacterDataFromElement(scadenzaMesiUtilizzo_line)));
	    		registrazioneRisposta.setScadenzaMesiUtilizzo(consumaCodiceResponse.getScadenzaMesiUtilizzo());
	    	}
	        listConsumaCodiceResponse.add(consumaCodiceResponse);
	        
	        super.saveBaseVo(registrazioneRisposta);
	        

        }
		
        consumaCodiceB2CResponse.setConsumaCodiceResponse(listConsumaCodiceResponse);
		
		return consumaCodiceB2CResponse;
		
	}
		
	/**
	 * ADD UsernameToken
	 * @param message
	 * @param envelope
	 * @throws SOAPException
	 */
	private void setOptionalHeaders(SOAPMessage message, SOAPEnvelope envelope) throws SOAPException {

		Random generator = new Random();
		String nonceString = String.valueOf(generator.nextInt(999999999));

		//String usr = "next01"; 		//TEST
		//String pwd = "next01NEXT"; 	//TEST
		String usr = "nextprd";			//PRODUZIONE
		String pwd = "3859W4PsJyS6";	//PRODUZIONE
		
		
		SOAPHeader header = message.getSOAPHeader();

		SOAPElement security = header.addChildElement("Security", "wsse",
				"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
		security.addAttribute(new QName("xmlns:wsu"),
				"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");

		SOAPElement usernameToken = security.addChildElement("UsernameToken", "wsse");
		usernameToken.addAttribute(new QName("wsu:Id"), "UsernameToken-1");

		SOAPElement username = usernameToken.addChildElement("Username", "wsse");
		username.addTextNode(usr);

		SOAPElement password = usernameToken.addChildElement("Password", "wsse");
		password.setAttribute("Type",
				"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
		password.addTextNode(pwd);

		SOAPElement nonce = usernameToken.addChildElement("Nonce", "wsse");
		nonce.setAttribute("EncodingType",
				"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary");

		nonce.addTextNode(Base64.encodeBase64String(hexEncode(nonceString).getBytes()));

		SOAPElement created = usernameToken.addChildElement("Created", "wsse");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		Calendar c1 = Calendar.getInstance();
		created.addTextNode(sdf.format(c1.getTime()));

		String serverURI = "http://somedomain.com/pe/ws/schema";
		envelope.addNamespaceDeclaration("sch", serverURI);
	}

	/**
	 * 
	 * @param in
	 * @return
	 */
	private String hexEncode(String in) {
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < (in.length() - 2) + 1; i = i + 2) {
			int c = Integer.parseInt(in.substring(i, i + 2), 16);
			char chr = (char) c;
			sb.append(chr);
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	private String getCharacterDataFromElement(Element e) {
		if (e != null) {
			Node child = e.getFirstChild();
			if (child instanceof CharacterData) {
				CharacterData cd = (CharacterData) child;
				return cd.getData();
			}
		}
		return "";
	}



	

}
