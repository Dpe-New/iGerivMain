package it.dpe.igeriv.web.actions;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.json.annotations.SMDMethod;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.bo.messaggi.MessaggiService;
import it.dpe.igeriv.dto.AvvisoMessaggioDto;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.BaseVo;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per la gestione delle vendite.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("GeneralRpcAction")
public class GeneralRpcAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements ServletContextAware {
	private static final long serialVersionUID = 1L;
	private final MessaggiService messaggiService;
	
	public GeneralRpcAction() {
		this.messaggiService = null;
	}
	
	@Autowired
	public GeneralRpcAction(MessaggiService messaggiService) {
		this.messaggiService = messaggiService;
	}
	
	@Override
	public void validate() {
	}

	@Override
	public void setServletContext(ServletContext context) {
		this.context = context;
	}

	/**
	 * Ritorna la pk del messaggio, se esiste un messaggio di alta priroità non ancora letto 
	 * 
	 * @return String
	 */
	@SMDMethod
	public Map<String, String> getPkHighPriorityMessage() {
		Map<String, String> map = new HashMap<String, String>();
		AvvisoMessaggioDto messaggioNonLettoDto = messaggiService.getPkMessaggioNonLetto(getAuthUser().getId(), getAuthUser().getCodFiegDl(), getAuthUser().getGiroTipo(), getAuthUser().getGiri(), null, getAuthUser().getDtAttivazioneEdicola());
		String pk = getAuthUser().getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_EDICOLA) && messaggioNonLettoDto != null && messaggioNonLettoDto.getPriorita() != null && (messaggioNonLettoDto.getPriorita().equals(IGerivConstants.TIPO_MESSAGGIO_IMMEDIATO) || messaggioNonLettoDto.getPriorita().equals(IGerivConstants.TIPO_MESSAGGIO_UREGENTISSIMO)) ? messaggioNonLettoDto.getPk().toString() : "";
		map.put("priorita", messaggioNonLettoDto != null ? messaggioNonLettoDto.getPriorita().toString() : "");
		map.put("pk", pk);
		return map;		
	}
	
}
