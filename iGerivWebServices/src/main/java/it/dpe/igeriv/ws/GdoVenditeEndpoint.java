package it.dpe.igeriv.ws;

import it.dpe.igeriv.gdo.bo.GdoBo;
import it.dpe.igeriv.gdo.dto.GdoVenditaDto;
import it.dpe.igeriv.gdo.dto.GdoVenditaReplyDto;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.xpath.XPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpServletConnection;

@Endpoint
@SuppressWarnings("unchecked")
public class GdoVenditeEndpoint {
	private final Logger log = Logger.getLogger(getClass());
	private static final String NAMESPACE_URI = "http://it.dpe.igeriv/gdo/ven/schemas";
	private final GdoBo gdoBo;
	private final Namespace namespace;
	
	@Autowired
	public GdoVenditeEndpoint(GdoBo gdoBo) throws JDOMException {
		log.info("Inizio Inizializzazione GdoVenditeEndpoint");
		this.gdoBo = gdoBo;
		this.namespace = Namespace.getNamespace("ven", NAMESPACE_URI);
		log.info("Fine Inizializzazione GdoVenditeEndpoint");
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "GdoVenRequest")
	@ResponsePayload
	public GdoVenditaReplyDto execute(@RequestPayload Element gdoRequest) throws Exception {
		log.info("Inizio metodo GdoVenditeEndpoint.execute");
		GdoVenditaReplyDto reply = null;
		try {
			XPath codUtenteExpression = XPath.newInstance("//codiceUtente");
			XPath pwdExpression = XPath.newInstance("//password");
			XPath listVenditeExpression = XPath.newInstance("//ven:listVendite");
			XPath barcodeExpression = XPath.newInstance(".//ven:barcode");
			XPath idtnExpression = XPath.newInstance(".//ven:idtn");
			XPath copieExpression = XPath.newInstance(".//ven:copie");
			XPath prezzoExpression = XPath.newInstance(".//ven:prezzoLordo");
			XPath dataOraVenditaExpression = XPath.newInstance(".//ven:dataOraVendita");
			
			codUtenteExpression.addNamespace(namespace);
			pwdExpression.addNamespace(namespace);
			listVenditeExpression.addNamespace(namespace);
			barcodeExpression.addNamespace(namespace);
			idtnExpression.addNamespace(namespace);
			copieExpression.addNamespace(namespace);
			prezzoExpression.addNamespace(namespace);
			dataOraVenditaExpression.addNamespace(namespace);
			
			TransportContext context = TransportContextHolder.getTransportContext();
			HttpServletConnection connection = (HttpServletConnection) context.getConnection();
			UserAbbonato user = (UserAbbonato) connection.getHttpServletRequest().getAttribute("user");
			log.info("GdoVenditaReplyDto - id utente=" + user.getId());
			List<GdoVenditaDto> list = new ArrayList<GdoVenditaDto>();
			List<Element> selectNodes = listVenditeExpression.selectNodes(gdoRequest);
			for (Element element : selectNodes) {
				GdoVenditaDto dto = new GdoVenditaDto();
				dto.setBarcode(barcodeExpression.valueOf(element));
				dto.setCopie(copieExpression.numberValueOf(element).intValue());
				Date dataBolla = getDataOraVendita(element, dataOraVenditaExpression);
				dto.setDataOraVendita(dataBolla);
				dto.setIdtn(idtnExpression.numberValueOf(element).intValue());
				dto.setPrezzoLordo(new BigDecimal(prezzoExpression.numberValueOf(element).doubleValue()));
				list.add(dto);
				log.info("dto=" + dto.toString());
			}
			reply = gdoBo.importDatiVenditeGdo(user, list);
		} catch (Throwable e) {
			log.error(StringUtility.getStackTrace(e), e);
			reply = new GdoVenditaReplyDto();
			reply.setState(IGerivConstants.STATO_IMPORTAZIONE_DATI_VENDITE_GDO_CONCLUSO_ERRORE);
			List<String> warnings = new ArrayList<String>();
			warnings.add(e.getMessage());
			reply.setListMessages(warnings.toArray(new String[warnings.size()]));
		}
		if (reply != null) {
			reply.setDataOra(new Date());
		}
		log.info("Fine metodo GdoVenditeEndpoint.execute");
		return reply;
	}

	/**
	 * @param element
	 * @param dataOraVenditaExpression 
	 * @return
	 * @throws JDOMException
	 * @throws ParseException
	 */
	private Date getDataOraVendita(Element element, XPath dataOraVenditaExpression) throws JDOMException, ParseException {
		Date date = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
			date = dateFormat.parse(dataOraVenditaExpression.valueOf(element));
		} catch (ParseException e) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			date = dateFormat.parse(dataOraVenditaExpression.valueOf(element));
		}
		return date;
	}
	
}
