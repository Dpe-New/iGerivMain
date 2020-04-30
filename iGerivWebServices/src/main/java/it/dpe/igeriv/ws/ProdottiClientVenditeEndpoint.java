package it.dpe.igeriv.ws;

import it.dpe.igeriv.bo.vendite.VenditeService;
import it.dpe.igeriv.dto.EdicolaClientVenditeDto;
import it.dpe.igeriv.dto.ProdottoClientVenditeDto;
import it.dpe.igeriv.gdo.dto.ProdottoReplyDto;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;

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
public class ProdottiClientVenditeEndpoint {
	private final Logger log = Logger.getLogger(getClass());
	private static final String NAMESPACE_URI = "http://it.dpe.igeriv/prod/client/ven/schemas";
	private final VenditeService venditeService;
	private final Namespace namespace;
	
	@Autowired
	public ProdottiClientVenditeEndpoint(VenditeService venditeService) throws JDOMException {
		log.info("Inizio Inizializzazione ProdottiClientVenditeEndpoint");
		this.venditeService = venditeService;
		this.namespace = Namespace.getNamespace("prod", NAMESPACE_URI);
		log.info("Fine Inizializzazione ProdottiClientVenditeEndpoint");
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "ProdottiClientVenditeRequest")
	@ResponsePayload
	public ProdottoReplyDto execute(@RequestPayload Element request) throws Exception {
		log.info("Inizio metodo ProdottiClientVenditeEndpoint.execute");
		ProdottoReplyDto reply = null;
		try {
			XPath coddlExpression = XPath.newInstance("//prod:coddl");
			coddlExpression.addNamespace(namespace);
			
			TransportContext context = TransportContextHolder.getTransportContext();
			HttpServletConnection connection = (HttpServletConnection) context.getConnection();
			UserAbbonato user = (UserAbbonato) connection.getHttpServletRequest().getAttribute("user");
			log.info("ProdottiClientVenditeEndpoint - id utente=" + user.getId().toString());
			
			
			List<ProdottoClientVenditeDto> prodotti = venditeService.getProdottiClientVendite(user.getArrCodFiegDl(),user.getArrId());
			reply = new ProdottoReplyDto();
			reply.setEdicola(buildEdicolaClientVenditeDto(user));
			reply.setListProdottiBolla(prodotti);
			reply.setState(IGerivConstants.STATO_SCARICO_DATI_BOLLE_GDO_CONCLUSO_SUCCESS);
			reply.setMessage("");
		} catch (Throwable e) {
			log.error(StringUtility.getStackTrace(e), e);
			reply = new ProdottoReplyDto();
			reply.setState(IGerivConstants.STATO_SCARICO_DATI_BOLLE_GDO_CONCLUSO_ERRORE);
			reply.setMessage(StringUtility.getStackTrace(e));
		}
		log.info("Fine metodo ProdottiClientVenditeEndpoint.execute");
		return reply;
	}

	/**
	 * @param user
	 * @return
	 */
	private EdicolaClientVenditeDto buildEdicolaClientVenditeDto(UserAbbonato user) {
		EdicolaClientVenditeDto dto = new EdicolaClientVenditeDto();
		dto.setNome(user.getRagioneSocialeEdicola());
		dto.setIndirizzo(user.getIndirizzoEdicolaPrimaRiga());
		dto.setLocalita(user.getLocalitaEdicolaPrimaRiga());
		dto.setProvincia(user.getProvinciaEdicola());
		dto.setCap(user.getCapEdicola());
		return dto;
	}
	
}
