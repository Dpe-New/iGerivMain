package it.dpe.igeriv.security;

import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * Classe che setta le variabili di sessione una volta loggato l'utente.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Component("IGerivLoginSessionConfigurer")
public class IGerivLoginSessionConfigurer {
	private final String showMsgScaricoDati;
	
	@Autowired
	public IGerivLoginSessionConfigurer(@Value("${igeriv.dl.show.msg.scarico.dati}") String showMsgScaricoDati) {
		this.showMsgScaricoDati = showMsgScaricoDati;
	}
	
	public void configureSession(Integer tipoUtente, HttpServletRequest httpServletRequest, UserAbbonato user) {
		configureSessionEdicola(httpServletRequest, user);
	}
	
	public void configureSessionEdicola(HttpServletRequest request, UserAbbonato user) {
		HttpSession session = request.getSession();
		session.setAttribute(IGerivConstants.USER_ID, user.getCodUtente());
		session.setAttribute(IGerivConstants.SESSION_VAR_COD_FIEG_DL, user.getCodFiegDl());
		session.setAttribute("statiTipoMessaggi", buildStatiTipoMessaggi());
		session.setAttribute(IGerivConstants.SESSION_VAR_REQUEST_IP_ADDRESS, (request.getHeader("x-forwarded-for") != null && !request.getHeader("x-forwarded-for").equals("")) ? request.getHeader("x-forwarded-for") : request.getRemoteAddr());
		session.setAttribute(IGerivConstants.SESSION_VAR_TIMEOUT_MINUTES, session.getMaxInactiveInterval() / 60);
		session.setAttribute(IGerivConstants.SESSION_VAR_NETWORK_DETECTION_INTERVAL_MILLS, "");
		session.setAttribute(IGerivConstants.SESSION_VAR_SAVE_BOLLE_INTERVAL_MILLS, "");
		session.setAttribute(IGerivConstants.SESSION_VAR_HIGH_PRIORITY_MESSAGES_CHECK_INTERVAL_MILLS, "");
		session.setAttribute(IGerivConstants.SESSION_VAR_BROWSER_NAME, StringUtility.getBrowserName(request.getHeader(IGerivConstants.HEADER_USER_AGENT)));
		session.setAttribute("abilitataCorrezioneBarcode", false);
		session.setAttribute("showMsgScaricoDati", (Strings.isNullOrEmpty(this.showMsgScaricoDati) ? false : new Boolean(this.showMsgScaricoDati)));
	}
	
	/**
	 * @return
	 */
	public List<KeyValueDto> buildStatiTipoMessaggi() {
		List<KeyValueDto> stati = new ArrayList<KeyValueDto>();
		KeyValueDto dto1 = new KeyValueDto();
		dto1.setKey("0");
		dto1.setValue(IGerivMessageBundle.get("igeriv.normale.ext"));
		KeyValueDto dto2 = new KeyValueDto();
		dto2.setKey("1");
		dto2.setValue(IGerivMessageBundle.get("igeriv.allerta.ext"));
		KeyValueDto dto3 = new KeyValueDto();
		dto3.setKey("2");
		dto3.setValue(IGerivMessageBundle.get("igeriv.emergenza.ext"));
		KeyValueDto dto4 = new KeyValueDto();
		dto4.setKey("3");
		dto4.setValue(IGerivMessageBundle.get("igeriv.altissima.ext"));
		stati.add(dto1);
		stati.add(dto2);
		stati.add(dto3);
		stati.add(dto4);
		return stati;
	}
	
	
}
