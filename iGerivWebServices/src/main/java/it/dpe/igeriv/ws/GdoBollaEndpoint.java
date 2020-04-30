package it.dpe.igeriv.ws;

import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.gdo.bo.GdoBo;
import it.dpe.igeriv.gdo.dto.GdoBolReplyDto;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;

import java.util.Date;

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

import com.google.common.base.Strings;

@Endpoint
public class GdoBollaEndpoint {
	private final Logger log = Logger.getLogger(getClass());
	private static final String NAMESPACE_URI = "http://it.dpe.igeriv/gdo/bol/schemas";
	private final GdoBo gdoBo;
	private final String edicoleEscluseControlloScaricoBolle;
	private final Namespace namespace;
	
	@Autowired
	public GdoBollaEndpoint(GdoBo gdoBo, String edicoleEscluseControlloScaricoBolle) throws JDOMException {
		log.info("Inizio Inizializzazione GdoBollaEndpoint");
		this.gdoBo = gdoBo;
		this.edicoleEscluseControlloScaricoBolle = edicoleEscluseControlloScaricoBolle;
		this.namespace = Namespace.getNamespace("bol", NAMESPACE_URI);
		log.info("Fine Inizializzazione GdoBollaEndpoint");
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "GdoBollaRequest")
	@ResponsePayload
	public GdoBolReplyDto execute(@RequestPayload Element gdoRequest) throws Exception {
		log.info("Inizio metodo GdoBollaEndpoint.execute");
		GdoBolReplyDto reply = null;
		try {
			XPath dataBollaExpression = XPath.newInstance("//bol:dataBolla");
			dataBollaExpression.addNamespace(namespace);
			
			TransportContext context = TransportContextHolder.getTransportContext();
			HttpServletConnection connection = (HttpServletConnection) context.getConnection();
			UserAbbonato user = (UserAbbonato) connection.getHttpServletRequest().getAttribute("user");
			log.info("GdoBollaEndpoint - id utente=" + user.getId().toString());
			Date dataBolla = DateUtilities.parseDate(dataBollaExpression.valueOf(gdoRequest), DateUtilities.FORMATO_DATA_YYYY_MM_DD);
			reply = gdoBo.getDatiBollaGdo(user, dataBolla);
			if (reply.getListProdottiBolla() != null && !reply.getListProdottiBolla().isEmpty()) {
				if (isEdicolaDaValidare(user.getId())) {
					gdoBo.validateRichiestaDati(user, dataBolla);
				}
			}
			reply.setState(IGerivConstants.STATO_SCARICO_DATI_BOLLE_GDO_CONCLUSO_SUCCESS);
			reply.setMessage("");
		} catch (IGerivBusinessException e) { 
			log.error(e);
			reply = new GdoBolReplyDto();
			reply.setState(IGerivConstants.STATO_SCARICO_DATI_BOLLE_GDO_CONCLUSO_WARNING);
			reply.setMessage(e.getMessage());
		} catch (Throwable e) {
			log.error(StringUtility.getStackTrace(e), e);
			reply = new GdoBolReplyDto();
			reply.setState(IGerivConstants.STATO_SCARICO_DATI_BOLLE_GDO_CONCLUSO_ERRORE);
			reply.setMessage(StringUtility.getStackTrace(e));
		}
		if (reply != null) {
			reply.setDataOra(new Date());
		}
		log.info("Fine metodo GdoBollaEndpoint.execute");
		return reply;
	}

	/**
	 * @param id
	 * @return
	 */
	private boolean isEdicolaDaValidare(Integer id) {
		if (!Strings.isNullOrEmpty(edicoleEscluseControlloScaricoBolle)) {
			String[] split = edicoleEscluseControlloScaricoBolle.split(",");
			for (String s : split) {
				if (new Integer(s.trim()).equals(id)) {
					return false;
				}
			}
		}
		return true;
	}
	
}
