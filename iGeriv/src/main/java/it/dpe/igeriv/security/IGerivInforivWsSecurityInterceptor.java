package it.dpe.igeriv.security;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;

import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.security.AbstractWsSecurityInterceptor;
import org.springframework.ws.soap.security.WsSecuritySecurementException;
import org.springframework.ws.soap.security.WsSecurityValidationException;
import org.springframework.ws.soap.security.xwss.XwsSecurityValidationException;

import com.google.common.base.Strings;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IGerivInforivWsSecurityInterceptor extends AbstractWsSecurityInterceptor {
	private Map<String, String> headerElements = null;
	private PasswordEncoder passwordEncoder;
	
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
			//String token = new String(passwordEncoder.encodePassword(IGerivConstants.IGERIV_INFORIV_PWD, IGerivConstants.IGERIV_INFORIV_SALT));
			if (Strings.isNullOrEmpty(token) || !token.equals(IGerivConstants.IGERIV_INFORIV_PWD)) {
				throw new XwsSecurityValidationException(IGerivMessageBundle.get("gp.login.failed"));
			}
		} else {
			throw new XwsSecurityValidationException(IGerivMessageBundle.get("gp.login.failed"));
		}
		
		/*
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		authList.add(new GrantedAuthorityImpl(RtaeConstants.ROLE_RTAE_RIV));
		UserAbbonato user = new UserAbbonato("358", "aaaaa", true, true, true, true, authList);
		user.setIdUtente(358);
		user.setTipoUtente(1);
		user.setIdAnagraficaDl(1);
        final Authentication auth = new UsernamePasswordAuthenticationToken(user, "aaaa", authList);
    	SecurityContextHolder.getContext().setAuthentication(auth);
    	TransportContext context = TransportContextHolder.getTransportContext();
		HttpServletConnection connection = (HttpServletConnection) context.getConnection();
    	connection.getHttpServletRequest().getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    	connection.getHttpServletRequest().setAttribute("idRivenditaAutenticata", "358");
    	*/
	}

	@Override
	protected void secureMessage(SoapMessage soapMessage, MessageContext messageContext) throws WsSecuritySecurementException {

	}

	@Override
	protected void cleanUp() {

	}

}
