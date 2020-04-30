package it.dpe.igeriv.security;

import it.dpe.igeriv.bo.ws.IGerivWSService;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivWSConstants;
import it.dpe.igeriv.vo.UserVo;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.security.AbstractWsSecurityInterceptor;
import org.springframework.ws.soap.security.WsSecuritySecurementException;
import org.springframework.ws.soap.security.WsSecurityValidationException;
import org.springframework.ws.soap.security.xwss.XwsSecurityValidationException;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpServletConnection;

import com.google.common.base.Strings;

public class IGerivGdoWsSecurityInterceptor extends AbstractWsSecurityInterceptor {
	//private final Logger log = Logger.getLogger(getClass());
	private Map<String, String> headerElements = null;
	private PasswordEncoder passwordEncoder;
	private IGerivWSService iGerivBo;
	private ReflectionSaltSource saltSource;
	
	@Override
	protected void validateMessage(SoapMessage soapMessage, MessageContext messageContext) throws WsSecurityValidationException {
		headerElements = new HashMap<String, String>();
		if (soapMessage.getSoapHeader() == null) {
			throw new XwsSecurityValidationException(IGerivMessageBundle.get("gp.login.failed"));
		}
		for (Iterator<SoapHeaderElement> it = soapMessage.getSoapHeader().examineAllHeaderElements(); it.hasNext();) {
			SoapHeaderElement s = it.next();
			QName name = s.getName();
			String value = s.getText();
			headerElements.put(name.getLocalPart(), value);
		}
		if (headerElements.containsKey("Token")) {
			String token = headerElements.get("Token");
			if (Strings.isNullOrEmpty(token) || !token.equals(IGerivWSConstants.IGERIV_GDO_TOKEN)) {
				throw new XwsSecurityValidationException(IGerivMessageBundle.get("gp.login.failed"));
			}
			Integer codEdicola = null; 
			String pwd = null; 
			if (headerElements.containsKey("UserName") && NumberUtils.isNumber(headerElements.get("UserName"))) {
				codEdicola = new Integer(headerElements.get("UserName"));
			} else {
				throw new XwsSecurityValidationException(IGerivMessageBundle.get("gp.login.failed"));
			}
			if (headerElements.containsKey("Password")) {
				pwd = headerElements.get("Password");
			} else {
				throw new XwsSecurityValidationException(IGerivMessageBundle.get("gp.login.failed"));
			} 
			UserAbbonato user = getUserAbbonato(codEdicola, pwd);
			if (user == null) {
				throw new XwsSecurityValidationException(IGerivMessageBundle.get("gp.login.failed"));
			}
			TransportContext context = TransportContextHolder.getTransportContext();
			HttpServletConnection connection = (HttpServletConnection) context.getConnection();
			connection.getHttpServletRequest().setAttribute("user", user);
		} else {
			throw new XwsSecurityValidationException(IGerivMessageBundle.get("gp.login.failed"));
		}
	}
	
	/**
	 * @param codEdicola
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	private UserAbbonato getUserAbbonato(Integer codEdicola, String pwd) throws XwsSecurityValidationException {
		UserVo edicolaByCodice = iGerivBo.getEdicolaByCodice(codEdicola.toString());
		if (edicolaByCodice == null) {
			throw new XwsSecurityValidationException(MessageFormat.format(IGerivMessageBundle.get("msg.rivendita.non.esiste.1"), codEdicola.toString()));
		}
		UserAbbonato ud = iGerivBo.buildUserDetails(edicolaByCodice.getCodUtente(), edicolaByCodice);
		String pwdEdicola = (ud.isPwdCriptata()) ? edicolaByCodice.getPwd() : passwordEncoder.encodePassword(edicolaByCodice.getPwd(), saltSource.getSalt(ud));
		if (!pwdEdicola.equals(pwd)) {
			throw new XwsSecurityValidationException(IGerivMessageBundle.get("gp.login.failed"));
		}
		if (!edicolaByCodice.getAbbinamentoEdicolaDlVo().getEdicolaiGerivPlus()) {
			throw new XwsSecurityValidationException(IGerivMessageBundle.get("gp.rivendita.non.abilitata.tgs"));
		}
		return ud;
	}

	@Override
	protected void secureMessage(SoapMessage soapMessage, MessageContext messageContext) throws WsSecuritySecurementException {

	}

	@Override
	protected void cleanUp() {

	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public IGerivWSService getiGerivBo() {
		return iGerivBo;
	}

	public void setiGerivBo(IGerivWSService iGerivBo) {
		this.iGerivBo = iGerivBo;
	}

	public ReflectionSaltSource getSaltSource() {
		return saltSource;
	}

	public void setSaltSource(ReflectionSaltSource saltSource) {
		this.saltSource = saltSource;
	}
	
}
