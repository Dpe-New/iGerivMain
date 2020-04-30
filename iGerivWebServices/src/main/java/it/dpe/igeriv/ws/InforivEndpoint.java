package it.dpe.igeriv.ws;

import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.dto.ImportazioneInforivReplyDto;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.NumberUtils;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.inforiv.bo.InforivImportBo;

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

import com.google.common.base.Strings;

@Endpoint
public class InforivEndpoint {
	private final Logger log = Logger.getLogger(getClass());
	private static final String NAMESPACE_URI = "http://it.dpe.igeriv/inforiv/schemas";
	private final InforivImportBo inforivImportBo;
	private final Namespace namespace;
	
	@Autowired
	public InforivEndpoint(InforivImportBo inforivImportBo) throws JDOMException {
		log.info("Inizio Inizializzazione InforivEndpoint");
		this.inforivImportBo = inforivImportBo;
		this.namespace = Namespace.getNamespace("inforiv", NAMESPACE_URI);
		log.info("Fine Inizializzazione InforivEndpoint");
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "EdicolaInforivRequest")
	@ResponsePayload
	public ImportazioneInforivReplyDto execute(@RequestPayload Element inforivRequest) throws Exception {
		log.info("Inizio metodo InforivEndpoint.execute");
		ImportazioneInforivReplyDto reply = null;
		try {
			XPath codFiegDlExpression = XPath.newInstance("//inforiv:CodFiegDl");
			XPath codFiegDl2Expression = XPath.newInstance("//inforiv:CodFiegDl2");
			XPath codFiegDl3Expression = XPath.newInstance("//inforiv:CodFiegDl3");
			XPath codEdicolaDlExpression = XPath.newInstance("//inforiv:CodEdicolaDl");
			XPath codEdicolaDl2Expression = XPath.newInstance("//inforiv:CodEdicolaDl2");
			XPath codEdicolaDl3Expression = XPath.newInstance("//inforiv:CodEdicolaDl3");
			XPath ragioneSocialeExpression = XPath.newInstance("//inforiv:RagioneSociale");
			XPath indirizzoExpression = XPath.newInstance("//inforiv:Indirizzo");
			XPath localitaExpression = XPath.newInstance("//inforiv:Localita");
			XPath siglaProvinciaExpression = XPath.newInstance("//inforiv:SiglaProvincia");
			XPath capExpression = XPath.newInstance("//inforiv:Cap");
			XPath pivaExpression = XPath.newInstance("//inforiv:PartitaIva");
			XPath codFiscaleExpression = XPath.newInstance("//inforiv:CodiceFiscale");
			XPath telefonoExpression = XPath.newInstance("//inforiv:Telefono");
			XPath faxExpression = XPath.newInstance("//inforiv:Fax");
			XPath emailExpression = XPath.newInstance("//inforiv:Email");
			XPath ftpHostExpression = XPath.newInstance("//inforiv:FtpHost");
			XPath ftpUserExpression = XPath.newInstance("//inforiv:FtpUser");
			XPath ftpPwdExpression = XPath.newInstance("//inforiv:FtpPwd");
			
			codFiegDlExpression.addNamespace(namespace);
			codFiegDl2Expression.addNamespace(namespace);
			codFiegDl3Expression.addNamespace(namespace);
			codEdicolaDlExpression.addNamespace(namespace);
			codEdicolaDl2Expression.addNamespace(namespace);
			codEdicolaDl3Expression.addNamespace(namespace);
			ragioneSocialeExpression.addNamespace(namespace);
			indirizzoExpression.addNamespace(namespace);
			localitaExpression.addNamespace(namespace);
			siglaProvinciaExpression.addNamespace(namespace);
			capExpression.addNamespace(namespace);
			pivaExpression.addNamespace(namespace);
			codFiscaleExpression.addNamespace(namespace);
			telefonoExpression.addNamespace(namespace);
			faxExpression.addNamespace(namespace);
			emailExpression.addNamespace(namespace);
			ftpHostExpression.addNamespace(namespace);
			ftpUserExpression.addNamespace(namespace);
			ftpPwdExpression.addNamespace(namespace);
			
			EdicolaDto edicola = new EdicolaDto();
			edicola.setCodDl(NumberUtils.getAsInteger(codFiegDlExpression.valueOf(inforivRequest)));
			edicola.setCodDl2(Strings.isNullOrEmpty(codFiegDl2Expression.valueOf(inforivRequest)) ? null : NumberUtils.getAsInteger(codFiegDl2Expression.valueOf(inforivRequest)));
			edicola.setCodDl3(Strings.isNullOrEmpty(codFiegDl3Expression.valueOf(inforivRequest)) ? null : NumberUtils.getAsInteger(codFiegDl3Expression.valueOf(inforivRequest)));
			edicola.setCodEdicolaDl(NumberUtils.getAsInteger(codEdicolaDlExpression.valueOf(inforivRequest)));
			edicola.setCodEdicolaDl2(NumberUtils.getAsInteger(codEdicolaDl2Expression.valueOf(inforivRequest)));
			edicola.setCodEdicolaDl3(NumberUtils.getAsInteger(codEdicolaDl3Expression.valueOf(inforivRequest)));
			edicola.setRagioneSociale1(ragioneSocialeExpression.valueOf(inforivRequest));
			edicola.setIndirizzo(indirizzoExpression.valueOf(inforivRequest));
			edicola.setProvincia(siglaProvinciaExpression.valueOf(inforivRequest));
			edicola.setCap(capExpression.valueOf(inforivRequest));
			edicola.setPiva(pivaExpression.valueOf(inforivRequest));
			edicola.setCodFiscale(codFiscaleExpression.valueOf(inforivRequest));
			edicola.setTelefono(telefonoExpression.valueOf(inforivRequest));
			edicola.setFax(faxExpression.valueOf(inforivRequest));
			edicola.setEmail(emailExpression.valueOf(inforivRequest));
			edicola.setFtpHost(ftpHostExpression.valueOf(inforivRequest));
			edicola.setFtpUser(ftpUserExpression.valueOf(inforivRequest));
			edicola.setFtpPwd(ftpPwdExpression.valueOf(inforivRequest));
			reply = inforivImportBo.importEdicolaInforiv(edicola);
		} catch (Exception e) {
			log.error(e);
			reply.setStato(IGerivConstants.STATO_IMPORTAZIONE_EDICOLA_INFORIV_CONCLUSO_ERRORE);
			reply.setMessaggio(StringUtility.getStackTrace(e));
		}
		log.info("Fine metodo InforivEndpoint.execute");
		return reply;
	}
	
}
